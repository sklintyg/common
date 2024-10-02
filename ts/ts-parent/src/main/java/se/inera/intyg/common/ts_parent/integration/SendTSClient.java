/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_parent.integration;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.Dispatch;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.soap.SOAPBinding;
import java.io.StringReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public abstract class SendTSClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendTSClient.class);

    private final MessageFactory messageFactory;
    private final DocumentBuilderFactory builderFactory;

    private Service service;

    private final String url;

    /**
     * @param url the RegisterCertificate ws-endpoint
     */
    public SendTSClient(String url) {
        LOGGER.info("RegisterCertificate for {} invoked", url);
        this.url = url;

        setupService();

        try {
            messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
            builderFactory = DocumentBuilderFactory.newInstance();
        } catch (SOAPException e) {
            throw new RuntimeException(e);
        }
    }

    public SOAPMessage registerCertificate(String message, String logicalAddress) {
        LOGGER.debug("Creating SoapMessage in sendTsClient");

        try {
            builderFactory.setNamespaceAware(true);

            SOAPMessage soapMessage = messageFactory.createMessage();
            soapMessage.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");

            SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            SOAPElement address = soapHeader.addChildElement("To", "ns", "http://www.w3.org/2005/08/addressing");
            address.addTextNode(logicalAddress);

            // Create a Document from the message
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(message)));

            // Add doc to soap body and save
            soapBody.addDocument(doc);
            soapMessage.saveChanges();

            // Create dispatcher and set SOAP actions
            Dispatch<SOAPMessage> dispSOAPMsg = createDispatchMessage();
            return dispSOAPMsg.invoke(soapMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Dispatch<SOAPMessage> createDispatchMessage();

    protected abstract void setupService();

    void setupService(Service service, QName port) {
        this.service = service;
        this.service.addPort(port, SOAPBinding.SOAP11HTTP_BINDING, this.url);
    }

    Dispatch<SOAPMessage> createDispatchMessage(String namespace, QName port) {
        Dispatch<SOAPMessage> dispSOAPMsg = service.createDispatch(port, SOAPMessage.class, Service.Mode.MESSAGE);
        dispSOAPMsg.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, true);
        dispSOAPMsg.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, namespace);
        return dispSOAPMsg;
    }

}
