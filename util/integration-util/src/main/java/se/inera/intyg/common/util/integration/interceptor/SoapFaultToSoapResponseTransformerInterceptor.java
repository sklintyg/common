/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.util.integration.interceptor;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.cxf.feature.transform.*;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.util.logging.LogMarkers;

/**
 * CXF interceptor which turns SOAP faults into valid SOAP responses.
 *
 * Transformation is performed using XSLTs which transform the <soap:Fault> element to a proper response element
 * containing a <result> element giving more specifics about the error.
 *
 * @author andreaskaltenbach
 */
public class SoapFaultToSoapResponseTransformerInterceptor extends XSLTOutInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapFaultToSoapResponseTransformerInterceptor.class);

    static {
        try {
            // Configure the private TransformerFactory defined in AbstractXSLTInterceptor
            Field transformFactoryField  = AbstractXSLTInterceptor.class.getDeclaredField("TRANSFORM_FACTORIY");
            transformFactoryField.setAccessible(true);
            TransformerFactory transformerFactory = (TransformerFactory) transformFactoryField.get(null);
            transformerFactory.setURIResolver(new ClasspathUriResolver());
            transformFactoryField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set UriResolver for TransactionFactory", e);
        }
    }

    public static final int HTTP_OK = 200;

    public SoapFaultToSoapResponseTransformerInterceptor(String phase) {
        super(phase);
    }

    @Override
    public void handleMessage(Message message) {
        Exception exception = message.getContent(Exception.class);
        Throwable cause = exception.getCause();
        if (cause instanceof javax.xml.bind.UnmarshalException) {
            LOGGER.error(LogMarkers.VALIDATION, exception.getMessage());
        } else {
            LOGGER.error(exception.getMessage(), exception);
        }

        // switch HTTP status from 500 (internal server error) to 200 (ok)
        message.getExchange().getOutFaultMessage().put(Message.RESPONSE_CODE, HTTP_OK);

        super.handleMessage(message);
    }

    @Override
    public void handleFault(Message message) {
        Exception e = message.getContent(Exception.class);
        try {
            SOAPEnvelope envelope = MessageFactory.newInstance().createMessage().getSOAPPart().getEnvelope();
            SOAPFault soapFault = envelope.getBody().addFault();
            soapFault.setFaultString(e != null ? e.getMessage() : "Unknown error");

            StringWriter sw = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(envelope), new StreamResult(sw));
            InputStream transformedStream = XSLTUtils.transform(getXSLTTemplate(),
                    new ByteArrayInputStream(sw.getBuffer().toString().getBytes(StandardCharsets.UTF_8)));
            IOUtils.copyAndCloseInput(transformedStream, message.getContent(OutputStream.class));

        } catch (SOAPException | TransformerException | TransformerFactoryConfigurationError | IOException ex) {
            LOGGER.error("Error occured during error handling: {}", e.getMessage());
        }
    }
}
