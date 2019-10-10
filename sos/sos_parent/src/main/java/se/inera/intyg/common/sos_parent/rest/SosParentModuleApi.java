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
package se.inera.intyg.common.sos_parent.rest;

import static se.inera.intyg.common.support.Constants.KV_PART_CODE_SYSTEM;


import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.MarshallingFailureException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.converter.InternalToRevoke;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;

public abstract class SosParentModuleApi<T extends SosUtlatande> implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(SosParentModuleApi.class);

    @Autowired
    protected WebcertModelFactory<T> webcertModelFactory;

    @Autowired
    private InternalDraftValidator<T> internalDraftValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    @Qualifier("registerCertificateClient")
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Autowired(required = false)
    private GetCertificateResponderInterface getCertificateResponderInterface;

    @Autowired(required = false)
    private RevokeCertificateResponderInterface revokeCertificateClient;

    private Class<T> type;

    private RegisterCertificateValidator validator = new RegisterCertificateValidator(getSchematronFileName());

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    public SosParentModuleApi(Class<T> type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValidateDraftResponse validateDraft(String internalModel) throws ModuleException {
        return internalDraftValidator.validateDraft(getInternal(internalModel));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInternalModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public String createNewInternalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, Utlatande template)
        throws ModuleException {
        try {
            return toInternalModelResponse(webcertModelFactory.createCopy(draftCertificateHolder, template));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, Utlatande template)
        throws ModuleException {
        return createNewInternalFromTemplate(draftCertificateHolder, template);
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        if (xmlBody == null || Strings.isNullOrEmpty(logicalAddress)) {
            throw new ModuleException("Request does not contain the original xml");
        }
        JAXBElement<RegisterCertificateType> el = XmlMarshallerHelper.unmarshal(xmlBody);
        RegisterCertificateType request = el.getValue();

        try {
            RegisterCertificateResponseType response = registerCertificateResponderInterface.registerCertificate(logicalAddress, request);

            if (response.getResult() != null && response.getResult().getResultCode() != ResultCodeType.OK) {
                String message = response.getResult().getResultText();
                LOG.error("Error occured when sending certificate '{}': {}",
                    request.getIntyg() != null ? request.getIntyg().getIntygsId() : null,
                    message);
                throw new ExternalServiceCallException(message);
            }
        } catch (SOAPFaultException e) {
            throw new ExternalServiceCallException(e);
        }
    }

    @Override
    public boolean shouldNotify(String persistedState, String currentState) throws ModuleException {
        return true;
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress, String recipientId) throws ModuleException {
        GetCertificateType request = new GetCertificateType();
        request.setIntygsId(getIntygsId(certificateId));
        request.setPart(getPart(recipientId));

        try {
            return convert(getCertificateResponderInterface.getCertificate(logicalAddress, request));
        } catch (SOAPFaultException e) {
            String error = String.format("Could not get certificate with id %s from Intygstjansten. SOAPFault: %s",
                certificateId, e.getMessage());
            LOG.error(error);
            throw new ModuleException(error);
        }
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        RegisterCertificateType request;
        try {
            request = internalToTransport(getInternal(internalModel));
        } catch (ConverterException e) {
            LOG.error("Failed to convert to transport format during registerCertificate", e);
            throw new ModuleConverterException("Failed to convert to transport format during registerCertificate", e);
        }

        RegisterCertificateResponseType response2 = registerCertificateResponderInterface.registerCertificate(logicalAddress, request);

        // check whether call was successful or not
        if (response2.getResult().getResultCode() == ResultCodeType.INFO) {
            throw new ExternalServiceCallException(response2.getResult().getResultText(),
                "Certificate already exists".equals(response2.getResult().getResultText())
                    ? ExternalServiceCallException.ErrorIdEnum.VALIDATION_ERROR
                    : ExternalServiceCallException.ErrorIdEnum.APPLICATION_ERROR);
        } else if (response2.getResult().getResultCode() == ResultCodeType.ERROR) {
            throw new ExternalServiceCallException(response2.getResult().getErrorId() + " : " + response2.getResult().getResultText());
        }
    }

    @Override
    public String updateBeforeSave(String internalModel, HoSPersonal hosPerson) throws ModuleException {
        return updateInternal(internalModel, hosPerson, null);
    }

    @Override
    public String updateBeforeSave(String internalModel, Patient patient) throws ModuleException {
        return updateInternal(internalModel, patient);
    }

    @Override
    public String updateBeforeSigning(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
        throws ModuleException {
        return updateInternal(internalModel, hosPerson, signingDate);
    }

    @Override
    public String updateBeforeViewing(String internalModel, Patient patient) throws ModuleException {
        try {
            Utlatande utlatande = this.getInternal(internalModel);
            WebcertModelFactoryUtil.populateWithPatientInfo(utlatande.getGrundData(), patient);
            return this.toInternalModelResponse(utlatande);
        } catch (ConverterException | ModuleException var4) {
            throw new ModuleException("Error while updating internal model", var4);
        }
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws ModuleException, IOException {
        return objectMapper.readValue(utlatandeJson, type);
    }

    @Override
    public Utlatande getUtlatandeFromXml(String xml) throws ModuleException {
        try {
            JAXBElement<RegisterCertificateType> el = XmlMarshallerHelper.unmarshal(xml);
            return transportToInternal(el.getValue().getIntyg());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    @Override
    public Intyg getIntygFromUtlatande(Utlatande utlatande) throws ModuleException {
        try {
            return utlatandeToIntyg(type.cast(utlatande));
        } catch (Exception e) {
            LOG.error("Could not get intyg from utlatande: {}", e.getMessage());
            throw new ModuleException("Could not get intyg from utlatande", e);
        }
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        return inputXml;
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        return XmlValidator.validate(validator, inputXml);
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande, List<String> frageIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        JAXBElement<RevokeCertificateType> el = XmlMarshallerHelper.unmarshal(xmlBody);
        RevokeCertificateResponseType response = revokeCertificateClient.revokeCertificate(logicalAddress, el.getValue());
        if (!response.getResult().getResultCode().equals(ResultCodeType.OK)) {
            String message = "Could not send revoke to " + logicalAddress;
            LOG.error(message);
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException {
        try {
            JAXBElement<RevokeCertificateType> el = new ObjectFactory().createRevokeCertificate(
                InternalToRevoke.convert(utlatande, skapatAv, meddelande));
            return XmlMarshallerHelper.marshal(el);
        } catch (ConverterException | MarshallingFailureException e) {
            throw new ModuleException(e.getMessage());
        }
    }

    @Override
    public String updateAfterSigning(String jsonModel, String signatureXml) throws ModuleException {
        if (signatureXml == null) {
            return jsonModel;
        }
        String base64EncodedSignatureXml = Base64.getEncoder().encodeToString(signatureXml.getBytes(Charset.forName("UTF-8")));
        return updateInternalAfterSigning(jsonModel, base64EncodedSignatureXml);
    }

    protected IntygTexts getTexts(String intygsTyp, String version) {
        if (intygTexts == null) {
            throw new IllegalStateException("intygTextsService not available in this context");
        }
        return intygTexts.getIntygTextsPojo(intygsTyp, version);
    }

    protected abstract T transportToInternal(Intyg intyg) throws ConverterException;

    protected abstract RegisterCertificateType internalToTransport(T utlatande) throws ConverterException;

    protected abstract Intyg utlatandeToIntyg(T utlatande) throws ConverterException;

    protected abstract String getSchematronFileName();

    protected T getInternal(String internalModel) throws ModuleException {
        try {
            return objectMapper.readValue(internalModel, type);
        } catch (IOException e) {
            e.printStackTrace(); // We need to see the cause
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    protected String toInternalModelResponse(Utlatande internalModel) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(internalModel);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    protected abstract T decorateWithSignature(T utlatande, String base64EncodedSignatureXml);

    private CertificateResponse convert(GetCertificateResponseType response) throws ModuleException {
        try {
            T utlatande = transportToInternal(response.getIntyg());
            String internalModel = toInternalModelResponse(utlatande);
            CertificateMetaData metaData = TransportConverterUtil.getMetaData(response.getIntyg(), getAdditionalInfo(response.getIntyg()));
            boolean revoked = response.getIntyg().getStatus().stream()
                .anyMatch(status -> StatusKod.CANCEL.name().equals(status.getStatus().getCode()));
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    private String updateInternal(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
        throws ModuleException {
        try {
            T utlatande = getInternal(internalModel);
            WebcertModelFactoryUtil.updateSkapadAv(utlatande, hosPerson, signingDate);
            return toInternalModelResponse(utlatande);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model", e);
        }
    }

    private String updateInternal(String internalModel, Patient patient)
        throws ModuleException {
        try {
            T utlatande = getInternal(internalModel);
            WebcertModelFactoryUtil.populateWithPatientInfo(utlatande.getGrundData(), patient);
            return toInternalModelResponse(utlatande);
        } catch (ModuleException | ConverterException e) {
            throw new ModuleException("Error while updating internal model", e);
        }
    }

    private String updateInternalAfterSigning(String internalModel, String base64EncodedSignatureXml)
        throws ModuleException {
        try {
            T utlatande = decorateWithSignature(getInternal(internalModel), base64EncodedSignatureXml);
            return toInternalModelResponse(utlatande);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model with signature", e);
        }
    }

    private IntygId getIntygsId(String certificateId) {
        IntygId intygId = new IntygId();
        intygId.setRoot("SE5565594230-B31");
        intygId.setExtension(certificateId);
        return intygId;
    }

    private Part getPart(String recipientId) {
        Part part = new Part();
        part.setCode(recipientId);
        part.setCodeSystem(KV_PART_CODE_SYSTEM);
        return part;
    }

}
