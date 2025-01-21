/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_parent.integration.stub;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.Provider;
import jakarta.xml.ws.ServiceMode;
import jakarta.xml.ws.WebServiceProvider;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultType;
import se.riv.clinicalprocess.healthcond.certificate.v1.Utlatande;

@WebServiceProvider
@ServiceMode(value = jakarta.xml.ws.Service.Mode.MESSAGE)
public final class RegisterCertificateResponderStub implements Provider<SOAPMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterCertificateResponderStub.class);

    @Autowired
    private TSCertificateStore tsCertificatesStore;

    private MessageFactory messageFactory;

    private RegisterCertificateResponderStub() {
        try {
            messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        } catch (SOAPException e) {
            LOGGER.error("Failed to initialize RegisterCertificateResponderStub: {}", e.fillInStackTrace());
            throw new RuntimeException(e);
        }
    }

    public RegisterCertificateResponseType registerCertificate(RegisterCertificateType request) {
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();

        Utlatande utlatande = request.getUtlatande();
        String id = utlatande.getUtlatandeId().getExtension();

        Map<String, String> props = new HashMap<>();
        props.put("Personnummer", utlatande.getPatient().getPersonId().getExtension());
        props.put("Makulerad", "NEJ");

        LOGGER.info("TS-STUB Received request");
        LOGGER.info("Request with id: {}", request.getUtlatande().getUtlatandeId().getExtension());
        tsCertificatesStore.addCertificate(id, props);
        response.setResult(okResult());
        return response;
    }

    private ResultType okResult() {
        ResultType result = new ResultType();
        result.setResultCode(ResultCodeType.OK);
        return result;
    }

    @Override
    public SOAPMessage invoke(SOAPMessage request) {
        LOGGER.debug("Invoking response for incoming request in stub");
        SOAPMessage response = null;
        SOAPBody soapBody = null;
        try {
            response = messageFactory.createMessage();
            soapBody = response.getSOAPPart().getEnvelope().getBody();

            SOAPElement result = soapBody.addChildElement(new QName("urn:riv:clinicalprocess:healthcond:certificate:1", "result"));

            if (request.getSOAPBody().hasFault()) {
                result.setTextContent("ERROR");
            } else {
                SOAPBody requestBody = request.getSOAPBody();
                Document doc = requestBody.extractContentAsDocument();
                Source source = new DOMSource(doc);
                StringWriter stringWriter = new StringWriter();
                Result streamResult = new StreamResult(stringWriter);
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                transformer.transform(source, streamResult);
                String xml = stringWriter.getBuffer().toString();
                JAXBElement<RegisterCertificateType> el = XmlMarshallerHelper.unmarshal(xml);
                RegisterCertificateType registerCertificateType = el.getValue();
                registerCertificate(registerCertificateType);

                String id = registerCertificateType.getUtlatande().getUtlatandeId().getExtension();
                LOGGER.debug("UtlatandeID received in stub: {}", id);

                result.setTextContent("OK");
            }
            response.saveChanges();
        } catch (SOAPException e) {
            LOGGER.error("Error while invoking response in RegisterCertificateResponderStub: {}", e.fillInStackTrace());
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            LOGGER.error("Error while transforming response in RegisterCertificateResponderStub: {}", e.fillInStackTrace());
            throw new RuntimeException(e);
        }
        return response;
    }

}
