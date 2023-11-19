/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_parent.rest;

import static se.inera.intyg.common.support.Constants.KV_PART_CODE_SYSTEM;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.services.texts.DefaultCertificateTextProvider;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateText;
import se.inera.intyg.common.support.facade.model.CertificateTextType;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.converter.InternalToRevoke;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;
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
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public abstract class TsParentModuleApi<T extends Utlatande> implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(TsParentModuleApi.class);

    public static final String REGISTER_CERTIFICATE_VERSION1 = "v1";
    public static final String REGISTER_CERTIFICATE_VERSION3 = "v3";
    private static final String ADDITIONAL_INFO_LABEL = "Avser behörighet";
    private static final String PREAMBLE_FOR_CITIZENS = "Det här är ditt intyg. Intyget innehåller all information som vården fyllt i. "
        + "Du kan inte ändra något i ditt intyg. Har du frågor kontaktar du den som skrivit ditt intyg.";

    @Autowired(required = false)
    protected WebcertModuleService moduleService;

    @Autowired
    private InternalDraftValidator<T> validator;

    @Autowired
    private WebcertModelFactory<T> webcertModelFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private GetCertificateResponderInterface getCertificateResponderInterface;

    @Autowired(required = false)
    @Qualifier("registerCertificateClient")
    protected RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Autowired(required = false)
    private RevokeCertificateResponderInterface revokeCertificateClient;

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    private Class<T> type;

    public TsParentModuleApi(Class<T> type) {
        this.type = type;
    }

    private RegisterCertificateValidator xmlValidator = getRegisterCertificateValidator();

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

        RegisterCertificateResponseType response = registerCertificateResponderInterface.registerCertificate(logicalAddress, request);

        // check whether call was successful or not
        if (response.getResult().getResultCode() == ResultCodeType.INFO) {
            throw new ExternalServiceCallException(response.getResult().getResultText(),
                "Certificate already exists".equals(response.getResult().getResultText())
                    ? ExternalServiceCallException.ErrorIdEnum.VALIDATION_ERROR
                    : ExternalServiceCallException.ErrorIdEnum.APPLICATION_ERROR);
        } else if (response.getResult().getResultCode() == ResultCodeType.ERROR) {
            throw new ExternalServiceCallException(response.getResult().getErrorId() + " : " + response.getResult().getResultText());
        }
    }

    @Override
    public ValidateDraftResponse validateDraft(String internalModel) throws ModuleException {
        return validator.validateDraft(getInternal(internalModel));
    }

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
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
        throws ModuleException {
        return createNewInternalFromTemplate(draftCopyHolder, template);
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
        return updateInternal(internalModel, patient);
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus)
        throws ModuleException {
        throw new ModuleException("Feature not supported");
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws ModuleException, IOException {
        return objectMapper.readValue(utlatandeJson, type);
    }

    @Override
    public boolean shouldNotify(String persistedState, String currentState) throws ModuleException {
        return true;
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        return inputXml;
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        return XmlValidator.validate(xmlValidator, inputXml);
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande, List<String> frageIds) {
        throw new UnsupportedOperationException();
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
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        List<CVType> types = new ArrayList<>();
        try {
            for (Svar svar : intyg.getSvar()) {
                if (INTYG_AVSER_SVAR_ID_1.equals(svar.getId())) {
                    for (Delsvar delsvar : svar.getDelsvar()) {
                        if (INTYG_AVSER_DELSVAR_ID_1.equals(delsvar.getId())) {
                            CVType cv = TransportConverterUtil.getCVSvarContent(delsvar);
                            if (cv != null) {
                                types.add(cv);
                            }
                        }
                    }
                }
            }
        } catch (ConverterException e) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: {}", intyg.getIntygsId().getExtension(), e.getMessage());
            return null;
        }

        if (types.isEmpty()) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: Found no types.", intyg.getIntygsId().getExtension());
            return null;
        }

        return types.stream()
            .map(cv -> IntygAvserKod.fromCode(cv.getCode()))
            .map(IntygAvserKod::getDescription)
            .collect(Collectors.joining(", "));
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        final RevokeCertificateType request = JAXB.unmarshal(new StringReader(xmlBody), RevokeCertificateType.class);
        RevokeCertificateResponseType response = revokeCertificateClient.revokeCertificate(logicalAddress, request);
        if (!response.getResult().getResultCode().equals(ResultCodeType.OK)) {
            String message = "Could not send revoke to " + logicalAddress;
            LOG.error(message);
            throw new ExternalServiceCallException(message);
        }
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException {
        try {
            final RevokeCertificateType revoke = InternalToRevoke.convert(utlatande, skapatAv, meddelande);
            final JAXBElement<RevokeCertificateType> el = new ObjectFactory().createRevokeCertificate(revoke);
            return XmlMarshallerHelper.marshal(el);
        } catch (ConverterException e) {
            throw new ModuleException(e.getMessage());
        }
    }

    @Override
    public String updateAfterSigning(String jsonModel, String signatureXml) throws ModuleException {
        // Note - until we've migrated T-intyg to v3 of RegisterCertificate for WC -> IT, we cannot attach the signature.
        return jsonModel;
    }

    protected abstract Intyg utlatandeToIntyg(T utlatande) throws ConverterException;

    protected T getInternal(String internalModel) throws ModuleException {
        try {
            return objectMapper.readValue(internalModel, type);
        } catch (IOException e) {
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

    private String updateInternal(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
        throws ModuleException {
        try {
            T utlatande = decorateDiagnoserWithDescriptions(getInternal(internalModel));
            WebcertModelFactoryUtil.updateSkapadAv(utlatande, hosPerson, signingDate);
            return toInternalModelResponse(utlatande);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model", e);
        }
    }

    private String updateInternal(String internalModel, Patient patient) throws ModuleException {
        T utlatande = getInternal(internalModel);
        try {
            WebcertModelFactoryUtil.populateWithPatientInfo(utlatande.getGrundData(), patient);
        } catch (ConverterException e) {
            throw new ModuleException("Failed to update internal model with patient", e);
        }
        return toInternalModelResponse(utlatande);
    }

    protected IntygTexts getTexts(String intygsTyp, String version) {
        if (intygTexts == null) {
            throw new IllegalStateException("intygTextsService not available in this context");
        }
        return intygTexts.getIntygTextsPojo(intygsTyp, version);
    }

    protected abstract RegisterCertificateValidator getRegisterCertificateValidator();

    protected abstract RegisterCertificateType internalToTransport(T utlatande) throws ConverterException;

    protected abstract T transportToInternal(Intyg intyg) throws ConverterException;

    protected T decorateDiagnoserWithDescriptions(T utlatande) {
        // Default implementation. Only TS certificate types with diagnoses need to override this method.
        return utlatande;
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

    protected void handleResponse(RegisterCertificateResponseType response, RegisterCertificateType request)
        throws ExternalServiceCallException {
        if (response.getResult() != null && response.getResult().getResultCode() != ResultCodeType.OK) {
            String certificateId = getCertificateId(request);
            String message = response.getResult().getResultText();

            if (response.getResult().getResultCode() == ResultCodeType.ERROR) {
                LOG.error("Error occurred when sending certificate '{}': {}", certificateId, message);
                throw new ExternalServiceCallException(message);
            } else if (response.getResult().getResultCode() == ResultCodeType.INFO) {
                LOG.info("Certificate '{}' was sent, but recipient returned result code {}. Message from recipient: {}",
                    certificateId, ResultCodeType.INFO, message);
            }
        }
    }

    private String getCertificateId(RegisterCertificateType request) {
        return (request.getIntyg() != null && request.getIntyg().getIntygsId() != null)
            ? request.getIntyg().getIntygsId().getExtension() : null;
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson,
        TypeAheadProvider typeAheadProvider) throws ModuleException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateTextProvider getTextProvider(String certificateType, String certificateTypeVersion) {
        final var intygTexts = getTexts(certificateType, certificateTypeVersion);
        return DefaultCertificateTextProvider.create(intygTexts);
    }

    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAdditionalInfoLabel() {
        return ADDITIONAL_INFO_LABEL;
    }

    @Override
    public CertificateText getPreambleForCitizens() {
        return CertificateText.builder()
            .type(CertificateTextType.PREAMBLE_TEXT)
            .text(PREAMBLE_FOR_CITIZENS)
            .build();
    }
}
