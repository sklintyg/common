/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.rest;

import java.io.StringReader;
import java.io.StringWriter;

import javax.ws.rs.NotSupportedException;
import javax.xml.bind.JAXB;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter.ModelConverter;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.XslTransformer;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.ts_diabetes.integration.RegisterTSDiabetesResponderImpl;
import se.inera.intyg.common.ts_diabetes.model.converter.InternalToTransportConverter;
import se.inera.intyg.common.ts_diabetes.model.converter.TransportToInternalConverter;
import se.inera.intyg.common.ts_diabetes.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_diabetes.model.internal.TsDiabetesUtlatande;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.util.TSDiabetesCertificateMetaTypeConverter;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.ts_parent.rest.TsParentModuleApi;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten and Mina-Intyg).
 *
 * @author Gustav Norbäcker, R2M
 */
public class TsDiabetesModuleApi extends TsParentModuleApi<TsDiabetesUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(TsDiabetesModuleApi.class);

    @Autowired(required = false)
    @Qualifier("sendTsDiabetesClient")
    private SendTSClient sendTsDiabetesClient;

    @Autowired(required = false)
    @Qualifier("diabetesGetClient")
    private GetTSDiabetesResponderInterface diabetesGetClient;

    @Autowired(required = false)
    @Qualifier("diabetesRegisterClient")
    private RegisterTSDiabetesResponderInterface diabetesRegisterClient;

    @Autowired(required = false)
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;

    @Autowired(required = false)
    @Qualifier("tsDiabetesXslTransformer")
    private XslTransformer xslTransformer;

    public TsDiabetesModuleApi() {
        super(TsDiabetesUtlatande.class);
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        RegisterTSDiabetesType request = InternalToTransportConverter.convert(getInternal(internalModel));

        RegisterTSDiabetesResponseType response = diabetesRegisterClient.registerTSDiabetes(logicalAddress, request);

        // check whether call was successful or not
        if (response.getResultat().getResultCode() == ResultCodeType.INFO) {
            throw new ExternalServiceCallException(response.getResultat().getResultText(),
                    RegisterTSDiabetesResponderImpl.CERTIFICATE_ALREADY_EXISTS.equals(response.getResultat().getResultText())
                            ? ErrorIdEnum.VALIDATION_ERROR
                            : ErrorIdEnum.APPLICATION_ERROR);
        } else if (response.getResultat().getResultCode() == ResultCodeType.ERROR) {
            throw new ExternalServiceCallException(response.getResultat().getErrorId() + " : " + response.getResultat().getResultText());
        }
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        String transformedPayload = xslTransformer.transform(xmlBody);

        try {
            SOAPMessage response = sendTsDiabetesClient.registerCertificate(transformedPayload, logicalAddress);
            SOAPEnvelope contents = response.getSOAPPart().getEnvelope();
            if (contents.getBody().hasFault()) {
                throw new ExternalServiceCallException(contents.getBody().getFault().getTextContent());
            }
        } catch (Exception e) {
            LOG.error("Error in sendCertificateToRecipient with msg: " + e.getMessage(), e);
            throw new ModuleException("Error in sendCertificateToRecipient.", e);
        }
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress, String recipientId) throws ModuleException {
        GetTSDiabetesType type = new GetTSDiabetesType();
        type.setIntygsId(certificateId);

        GetTSDiabetesResponseType diabetesResponseType = diabetesGetClient.getTSDiabetes(logicalAddress, type);

        switch (diabetesResponseType.getResultat().getResultCode()) {
        case INFO:
        case OK:
            return convert(diabetesResponseType, false);
        case ERROR:
            switch (diabetesResponseType.getResultat().getErrorId()) {
            case REVOKED:
                return convert(diabetesResponseType, true);
            case VALIDATION_ERROR:
                throw new ModuleException("GetTSDiabetes WS call: VALIDATION_ERROR :"
                        + diabetesResponseType.getResultat().getResultText());
            default:
                throw new ModuleException(
                        "GetTSDiabetes WS call: ERROR :" + diabetesResponseType.getResultat().getResultText());
            }
        }
        throw new ModuleException("GetTSDiabetes WS call: ERROR :" + diabetesResponseType.getResultat().getResultText());
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        AttributedURIType uri = new AttributedURIType();
        uri.setValue(logicalAddress);

        StringBuffer sb = new StringBuffer(xmlBody);
        RevokeMedicalCertificateRequestType request = JAXB.unmarshal(new StreamSource(new StringReader(sb.toString())),
                RevokeMedicalCertificateRequestType.class);
        RevokeMedicalCertificateResponseType response = revokeCertificateClient.revokeMedicalCertificate(uri, request);
        if (!response.getResult().getResultCode().equals(ResultCodeEnum.OK)) {
            String message = "Could not send revoke to " + logicalAddress;
            LOG.error(message);
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException {
        RevokeMedicalCertificateRequestType request = new RevokeMedicalCertificateRequestType();
        request.setRevoke(ModelConverter.buildRevokeTypeFromUtlatande(utlatande, meddelande));

        StringWriter writer = new StringWriter();
        JAXB.marshal(request, writer);
        return writer.toString();
    }

    @Override
    public TsDiabetesUtlatande getUtlatandeFromXml(String xml) throws ModuleException {
        RegisterTSDiabetesType jaxbObject = JAXB.unmarshal(new StringReader(xml),
                RegisterTSDiabetesType.class);
        try {
            return TransportToInternalConverter.convert(jaxbObject.getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    private CertificateResponse convert(GetTSDiabetesResponseType diabetesResponseType, boolean revoked) throws ModuleException {
        try {
            TsDiabetesUtlatande utlatande = TransportToInternalConverter.convert(diabetesResponseType.getIntyg());
            String internalModel = toInternalModelResponse(utlatande);
            CertificateMetaData metaData = TSDiabetesCertificateMetaTypeConverter.toCertificateMetaData(diabetesResponseType.getMeta(),
                    diabetesResponseType.getIntyg());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    @Override
    protected Intyg utlatandeToIntyg(TsDiabetesUtlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected String getSchematronFileName() {
        return TsDiabetesEntryPoint.SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(TsDiabetesUtlatande utlatande) throws ConverterException {
        throw new NotSupportedException();
    }

    @Override
    protected TsDiabetesUtlatande transportToInternal(Intyg intyg) throws ConverterException {
        throw new NotSupportedException();
    }
}
