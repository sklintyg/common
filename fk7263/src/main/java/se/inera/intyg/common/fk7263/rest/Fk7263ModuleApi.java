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
package se.inera.intyg.common.fk7263.rest;

import static se.inera.intyg.common.fk7263.integration.RegisterMedicalCertificateResponderImpl.CERTIFICATE_ALREADY_EXISTS;
import static se.inera.intyg.common.fk7263.model.converter.UtlatandeToIntyg.BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fk7263.model.converter.UtlatandeToIntyg.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import iso.v21090.dt.v1.CD;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateRequestType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateResponseType;
import se.inera.intyg.common.fk7263.model.converter.ArbetsformagaToGiltighet;
import se.inera.intyg.common.fk7263.model.converter.InternalToCertificate;
import se.inera.intyg.common.fk7263.model.converter.InternalToTransport;
import se.inera.intyg.common.fk7263.model.converter.TransportToInternal;
import se.inera.intyg.common.fk7263.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.model.util.Fk7263ModelCompareUtil;
import se.inera.intyg.common.fk7263.pdf.PdfDefaultGenerator;
import se.inera.intyg.common.fk7263.pdf.PdfGeneratorException;
import se.inera.intyg.common.fk7263.schemas.clinicalprocess.healthcond.certificate.converter.ClinicalProcessCertificateMetaTypeConverter;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.fk7263.testability.Fk7263TestabilityUtlatandeTestDataProvider;
import se.inera.intyg.common.fk7263.validator.InternalDraftValidator;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter.ModelConverter;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.messages.DefaultCertificateMessagesProvider;
import se.inera.intyg.common.services.messages.MessagesParser;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateLink;
import se.inera.intyg.common.support.facade.model.CertificateText;
import se.inera.intyg.common.support.facade.model.CertificateTextType;
import se.inera.intyg.common.support.facade.util.TestabilityToolkit;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
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
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.modules.support.facade.FillType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

/**
 * @author andreaskaltenbach, marced
 */
@Component(value = "moduleapi.fk7263.v1")
public class Fk7263ModuleApi implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

    private static final Comparator<? super DatePeriodType> PERIOD_START = Comparator.comparing(DatePeriodType::getStart);
    private static final String SPACE = "---";
    public static final String ADDITIONAL_INFO_LABEL = "Gäller intygsperiod";
    private static final String LINK_FK_ID = "linkFK";
    private static final String PREAMBLE_FOR_CITIZEN = "Det här är ditt intyg. Intyget innehåller all information som vården fyllt i. "
        + "Du kan inte ändra något i ditt intyg. Har du frågor kontaktar du den som skrivit ditt intyg. "
        + "Om du vill ansöka om sjukpenning, gör du det på {" + LINK_FK_ID + "}.";
    private static final String FK_URL = "http://www.forsakringskassan.se/sjuk";
    private static final String FK_NAME = "Försäkringskassan";

    @Autowired
    private WebcertModelFactory<Fk7263Utlatande> webcertModelFactory;

    @Autowired
    private InternalDraftValidator internalDraftValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    @Qualifier("registerMedicalCertificateClient")
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient;

    @Autowired
    private Fk7263ModelCompareUtil modelCompareUtil;

    @Autowired(required = false)
    private GetMedicalCertificateResponderInterface getMedicalCertificateResponderInterface;

    @Autowired(required = false)
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;
    @Autowired(required = false)
    private SummaryConverter summaryConverter;
    private Map<String, String> validationMessages;

    public Fk7263ModuleApi() {
        init();
    }

    private void init() {
        try {
            final var inputStream1 = new ClassPathResource("/META-INF/resources/webjars/common/webcert/messages.js").getInputStream();
            final var inputStream2
                = new ClassPathResource("/META-INF/resources/webjars/fk7263/webcert/views/messages.js").getInputStream();
            validationMessages = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        } catch (IOException exception) {
            LOG.error("Error during initialization. Could not read messages files");
            throw new RuntimeException("Error during initialization. Could not read messages files", exception);
        }
    }

    private static String buildSistaSjukskrivningsgrad(Fk7263Utlatande internal) {
        InternalDate lastPeriod = null;
        String lastSjukskrivningsgrad = null;
        if (internal.getNedsattMed25() != null && internal.getNedsattMed25().tomAsLocalDate() != null) {
            lastPeriod = internal.getNedsattMed25().getTom();
            lastSjukskrivningsgrad = Nedsattningsgrad.NEDSATT_MED_1_4.name();
        }
        if (internal.getNedsattMed50() != null && internal.getNedsattMed50().tomAsLocalDate() != null
            && (lastPeriod == null || lastPeriod.asLocalDate().isBefore(internal.getNedsattMed50().tomAsLocalDate()))) {
            lastPeriod = internal.getNedsattMed50().getTom();
            lastSjukskrivningsgrad = Nedsattningsgrad.NEDSATT_MED_1_2.name();
        }
        if (internal.getNedsattMed75() != null && internal.getNedsattMed75().tomAsLocalDate() != null
            && (lastPeriod == null || lastPeriod.asLocalDate().isBefore(internal.getNedsattMed75().tomAsLocalDate()))) {
            lastPeriod = internal.getNedsattMed75().getTom();
            lastSjukskrivningsgrad = Nedsattningsgrad.NEDSATT_MED_3_4.name();
        }
        if (internal.getNedsattMed100() != null && internal.getNedsattMed100().tomAsLocalDate() != null
            && (lastPeriod == null || lastPeriod.asLocalDate().isBefore(internal.getNedsattMed100().tomAsLocalDate()))) {
            lastSjukskrivningsgrad = Nedsattningsgrad.HELT_NEDSATT.name();
        }
        return lastSjukskrivningsgrad;
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
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        try {
            Fk7263Utlatande intyg = getInternal(internalModel);
            PdfDefaultGenerator pdfGenerator = new PdfDefaultGenerator(intyg, statuses, applicationOrigin, utkastStatus);
            return new PdfResponse(pdfGenerator.getBytes(), pdfGenerator.generatePdfFilename(LocalDateTime.now(), false));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus)
        throws ModuleException {
        throw new RuntimeException("Not implemented");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {

        // Check that we got any data at all
        if (xmlBody == null) {
            throw new ModuleException("No xml body found in call to sendCertificateToRecipient!");
        }

        if (logicalAddress == null) {
            throw new ModuleException("No LogicalAddress found in call to sendCertificateToRecipient!");
        }

        JAXBElement<RegisterMedicalCertificateType> el = XmlMarshallerHelper.unmarshal(xmlBody);
        sendCertificateToRecipient(el.getValue(), logicalAddress, recipientId);
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress, String recipientId) throws ModuleException {
        GetMedicalCertificateRequestType request = new GetMedicalCertificateRequestType();
        request.setCertificateId(certificateId);
        request.setPart(recipientId);

        GetMedicalCertificateResponseType response = getMedicalCertificateResponderInterface.getMedicalCertificate(logicalAddress, request);

        switch (response.getResult().getResultCode()) {
            case INFO:
            case OK:
                return convert(response, false);
            case ERROR:
                ErrorIdType errorId = response.getResult().getErrorId();
                String resultText = response.getResult().getResultText();
                switch (errorId) {
                    case REVOKED:
                        return convert(response, true);
                    default:
                        LOG.error("Error of type {} occured when retrieving certificate '{}': {}", errorId, certificateId, resultText);
                        throw new ModuleException(
                            "Error of type " + errorId + " occured when retrieving certificate " + certificateId + ", " + resultText);
                }
        }
        LOG.error("An unidentified error occured when retrieving certificate '{}': {}", certificateId,
            response.getResult().getResultText());
        throw new ModuleException(
            "An unidentified error occured when retrieving certificate " + certificateId + ", "
                + response.getResult().getResultText());
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        // Check that we got any data at all
        if (internalModel == null) {
            throw new ModuleException("No internal model found in call to sendCertificateToRecipient!");
        }

        if (logicalAddress == null) {
            throw new ModuleException("No LogicalAddress found in call to sendCertificateToRecipient!");
        }
        RegisterMedicalCertificateType request;
        try {
            request = InternalToTransport.getJaxbObject(getUtlatandeFromJson(internalModel));
        } catch (IOException | ConverterException e) {
            throw new ModuleException(e.getMessage());
        }
        sendCertificateToRecipient(request, logicalAddress, null);
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        Fk7263Utlatande utlatande = getUtlatandeFromXml(inputXml);
        Intyg intyg = getIntygFromUtlatande(utlatande);
        RegisterCertificateType type = new RegisterCertificateType();
        type.setIntyg(intyg);
        JAXBElement<RegisterCertificateType> el = new ObjectFactory().createRegisterCertificate(type);
        return XmlMarshallerHelper.marshal(el);
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
    public boolean shouldNotify(String persistedState, String currentState) throws ModuleException {
        Fk7263Utlatande oldUtlatande;
        Fk7263Utlatande newUtlatande;

        try {
            oldUtlatande = objectMapper.readValue(persistedState, Fk7263Utlatande.class);
            newUtlatande = objectMapper.readValue(currentState, Fk7263Utlatande.class);
        } catch (IOException e) {
            throw new ModuleException(e);
        }

        return modelCompareUtil.modelDiffers(oldUtlatande, newUtlatande);
    }

    /*
     * (non-Javadoc)
     *
     * Hard code code system name to ICD-10.
     *
     * This is a special case to solve JIRA issue https://inera-certificate.atlassian.net/browse/WEBCERT-1442.
     * It should be removed when Forsakringskassan can handle code system name correctly.
     */
    RegisterMedicalCertificateType whenFkIsRecipientThenSetCodeSystemToICD10(final RegisterMedicalCertificateType request)
        throws ModuleException {

        LOG.debug("Recipient of RegisterMedicalCertificate certificate is Försäkringskassan");
        LOG.debug("Set element 'lakarutlatande/medicinsktTillstand/tillstandsKod/codeSystemName' to value 'ICD-10'");

        // Check that we got a lakarutlatande element
        if (request.getLakarutlatande() == null) {
            throw new ModuleException("No Lakarutlatande element found in request data!");
        }

        LakarutlatandeType lakarutlatande = request.getLakarutlatande();

        // Decide if this certificate has smittskydd checked
        boolean inSmittskydd = findAktivitetWithCode(request.getLakarutlatande().getAktivitet(),
            Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null;

        if (!inSmittskydd) {
            // Check that we got a medicinsktTillstand element
            if (lakarutlatande.getMedicinsktTillstand() == null) {
                throw new ModuleException("No medicinsktTillstand element found in request data. Cannot set codeSystemName to 'ICD-10'!");
            }

            MedicinsktTillstandType medicinsktTillstand = lakarutlatande.getMedicinsktTillstand();

            // Check that we got a tillstandskod element
            if (medicinsktTillstand.getTillstandskod() == null) {
                throw new ModuleException("No tillstandskod element found in request data. Cannot set codeSystemName to 'ICD-10'!");
            }

            CD tillstandskod = medicinsktTillstand.getTillstandskod();
            tillstandskod.setCodeSystemName(Diagnoskodverk.ICD_10_SE.getCodeSystemName());

            // Update request
            request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(tillstandskod);

        } else {
            try {
                // tillstandskod is not mandatory when smittskydd is true, just try to set it.
                request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod()
                    .setCodeSystemName(Diagnoskodverk.ICD_10_SE.getCodeSystemName());

            } catch (NullPointerException npe) {
                LOG.debug("No tillstandskod element found in request data. "
                    + "Element is not mandatory when Smittskydd is checked. "
                    + "Cannot set codeSystemName to 'ICD-10'");
            }
        }

        return request;
    }

    @Override
    public Fk7263Utlatande getUtlatandeFromJson(String utlatandeJson) throws ModuleException, IOException {
        // return objectMapper.readValue(utlatandeJson, Fk7263Utlatande.class);

        Fk7263Utlatande utlatande = objectMapper.readValue(utlatandeJson, Fk7263Utlatande.class);

        // Explicitly populate the giltighet interval since it is information derived from
        // the arbetsformaga but needs to be serialized into the Utkast model.
        utlatande.setGiltighet(ArbetsformagaToGiltighet.getGiltighetFromUtlatande(utlatande));
        return utlatande;
    }

    @Override
    public Fk7263Utlatande getUtlatandeFromXml(String xml) throws ModuleException {
        JAXBElement<RegisterMedicalCertificateType> el = XmlMarshallerHelper.unmarshal(xml);
        try {
            return TransportToInternal.convert(el.getValue().getLakarutlatande());
        } catch (ConverterException e) {
            LOG.error("Could not get utlatande from xml: {}", e.getMessage());
            throw new ModuleException("Could not get utlatande from xml", e);
        }
    }

    private CertificateResponse convert(GetMedicalCertificateResponseType response, boolean revoked) throws ModuleException {
        try {
            Fk7263Utlatande utlatande = TransportToInternal.convert(response.getLakarutlatande());
            String internalModel = objectMapper.writeValueAsString(utlatande);
            CertificateMetaData metaData = ClinicalProcessCertificateMetaTypeConverter.toCertificateMetaData(response.getMeta());
            return new CertificateResponse(internalModel, utlatande, metaData, revoked);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

    private AktivitetType findAktivitetWithCode(List<AktivitetType> aktiviteter, Aktivitetskod aktivitetskod) throws ModuleException {
        AktivitetType foundAktivitet = null;

        try {
            if (aktiviteter != null) {
                for (int i = 0; i < aktiviteter.size(); i++) {
                    AktivitetType listAktivitet = aktiviteter.get(i);
                    if (listAktivitet.getAktivitetskod() != null && listAktivitet.getAktivitetskod().compareTo(aktivitetskod) == 0) {
                        foundAktivitet = listAktivitet;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new ModuleException(e.getMessage(), e);
        }

        return foundAktivitet;
    }

    // - - - - - Private transformation methods for building responses - - - - - //

    private void sendCertificateToRecipient(RegisterMedicalCertificateType originalRequest, final String logicalAddress,
        final String recipientId)
        throws ModuleException {
        RegisterMedicalCertificateType request = originalRequest;
        // This is a special case when recipient is Forsakringskassan. See JIRA issue WEBCERT-1442.
        if (!Strings.isNullOrEmpty(recipientId) && recipientId.equalsIgnoreCase(Fk7263EntryPoint.DEFAULT_RECIPIENT_ID)) {
            request = whenFkIsRecipientThenSetCodeSystemToICD10(request);
        }

        // Due to sekretessmarkering and not storing patient names anymore, make sure PatientType#fullstandigtNamn is not null.
        if (Strings.isNullOrEmpty(request.getLakarutlatande().getPatient().getFullstandigtNamn())) {
            request.getLakarutlatande().getPatient().setFullstandigtNamn(SPACE);
        }

        AttributedURIType address = new AttributedURIType();
        address.setValue(logicalAddress);

        try {
            RegisterMedicalCertificateResponseType response = registerMedicalCertificateClient.registerMedicalCertificate(address, request);

            // check whether call was successful or not
            if (response.getResult().getResultCode() != ResultCodeEnum.OK) {
                boolean info = response.getResult().getResultCode() == ResultCodeEnum.INFO;

                // This monstrosity is required because we want to handle when the certificate already exists in
                // Intygstjänsten. When this happens Intygstjänsten will return an INFO result with a specified
                // InfoText. To make sure we do not try to resend this request we need to throw an exception with
                // ErrorIdEnum of ValidationError.
                if (recipientId == null && info && CERTIFICATE_ALREADY_EXISTS.equals(response.getResult().getInfoText())) {
                    LOG.warn("Tried to register certificate ({}) which already exist in Intygstjänsten",
                        request.getLakarutlatande().getLakarutlatandeId());
                    throw new ExternalServiceCallException(response.getResult().getInfoText(), ErrorIdEnum.VALIDATION_ERROR);
                } else {
                    String message = info
                        ? response.getResult().getInfoText()
                        : response.getResult().getErrorId() + " : " + response.getResult().getErrorText();
                    LOG.error("Error occured when sending certificate '{}': {}",
                        request.getLakarutlatande() != null ? request.getLakarutlatande().getLakarutlatandeId() : null,
                        message);
                    throw new ExternalServiceCallException(message);
                }
            }
        } catch (SOAPFaultException e) {
            throw new ExternalServiceCallException(e);
        }

    }

    private Fk7263Utlatande getInternal(String internalModel)
        throws ModuleException {

        try {
            Fk7263Utlatande utlatande = objectMapper.readValue(internalModel, Fk7263Utlatande.class);

            // Explicitly populate the giltighet interval since it is information derived from
            // the arbetsformaga but needs to be serialized into the Utkast model.
            utlatande.setGiltighet(ArbetsformagaToGiltighet.getGiltighetFromUtlatande(utlatande));
            return utlatande;

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private String updateInternal(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
        throws ModuleException {
        try {
            Fk7263Utlatande intyg = getInternal(internalModel);
            WebcertModelFactoryUtil.updateSkapadAv(intyg, hosPerson, signingDate);
            return toInternalModelResponse(intyg);
        } catch (ModuleException e) {
            throw new ModuleException("Error while updating internal model skapadAv", e);
        }
    }

    private String updateInternal(String internalModel, Patient patient)
        throws ModuleException {
        try {
            Fk7263Utlatande intyg = getInternal(internalModel);
            WebcertModelFactoryUtil.populateWithPatientInfo(intyg.getGrundData(), patient);
            return toInternalModelResponse(intyg);
        } catch (ModuleException | ConverterException e) {
            throw new ModuleException("Error while updating internal model with patient", e);
        }
    }

    private String toInternalModelResponse(Fk7263Utlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return writer.toString();
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        return ValidateXmlResponse.createValidResponse();
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande,
        List<String> frageIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
        throws ModuleException {
        try {
            if (!Fk7263Utlatande.class.isInstance(template)) {
                LOG.error("Could not create a new internal Webcert model from template");
                throw new ModuleConverterException("Could not create a new internal Webcert model from template");
            }

            Fk7263Utlatande internal = (Fk7263Utlatande) template;

            Relation relation = draftCopyHolder.getRelation();
            relation.setSistaGiltighetsDatum(internal.getGiltighet().getTom());
            relation.setSistaSjukskrivningsgrad(buildSistaSjukskrivningsgrad(internal));
            draftCopyHolder.setRelation(relation);

            internal.setKontaktMedFk(false);
            internal.setNedsattMed100(null);
            internal.setNedsattMed25(null);
            internal.setNedsattMed50(null);
            internal.setNedsattMed75(null);
            internal.setNedsattMed25Beskrivning(null);
            internal.setNedsattMed50Beskrivning(null);
            internal.setNedsattMed75Beskrivning(null);
            internal.setTjanstgoringstid(null);
            internal.setTelefonkontaktMedPatienten(null);
            internal.setUndersokningAvPatienten(null);
            internal.setJournaluppgifter(null);
            internal.setAnnanReferens(null);
            internal.setAnnanReferensBeskrivning(null);

            return toInternalModelResponse(webcertModelFactory.createCopy(draftCopyHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public Intyg getIntygFromUtlatande(Utlatande utlatande) throws ModuleException {
        try {
            return UtlatandeToIntyg.convert((Fk7263Utlatande) utlatande);
        } catch (Exception e) {
            LOG.error("Could not get intyg from utlatande: {}", e.getMessage());
            throw new ModuleException("Could not get intyg from utlatande", e);
        }
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        List<DatePeriodType> periods = intyg.getSvar().stream()
            .filter(svar -> BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32.equals(svar.getId()))
            .map(Svar::getDelsvar)
            .flatMap(List::stream)
            .filter(delsvar -> delsvar != null && BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32.equals(delsvar.getId()))
            .map(delsvar -> {
                try {
                    return TransportConverterUtil.getDatePeriodTypeContent(delsvar);
                } catch (ConverterException ce) {
                    LOG.error("Failed retrieving additionalInfo for certificate {}: {}",
                        intyg.getIntygsId().getExtension(), ce.getMessage());
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .sorted(PERIOD_START)
            .collect(Collectors.toList());

        if (periods.isEmpty()) {
            LOG.error("Failed retrieving additionalInfo for certificate {}: Found no periods.", intyg.getIntygsId().getExtension());
            return null;
        }

        return periods.get(0).getStart().toString() + " - " + periods.get(periods.size() - 1).getEnd().toString();
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        AttributedURIType uri = new AttributedURIType();
        uri.setValue(logicalAddress);

        JAXBElement<RevokeMedicalCertificateRequestType> el = XmlMarshallerHelper.unmarshal(xmlBody);

        RevokeMedicalCertificateResponseType response =
            revokeCertificateClient.revokeMedicalCertificate(uri, el.getValue());
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

        JAXBElement<RevokeMedicalCertificateRequestType> el =
            new se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.ObjectFactory()
                .createRevokeMedicalCertificateRequest(request);

        return XmlMarshallerHelper.marshal(el);
    }

    @Override
    public String updateAfterSigning(String jsonModel, String signatureXml) throws ModuleException {
        // Signatures are not applicable for FK7263.
        return jsonModel;
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var certificateMessagesProvider = getMessagesProvider();
        final var certificate = InternalToCertificate.convert(internalCertificate, certificateMessagesProvider);
        final var certificateSummary = summaryConverter.convert(this, getIntygFromUtlatande(internalCertificate));
        certificate.getMetadata().setSummary(certificateSummary);
        return certificate;
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateTextProvider getTextProvider(String certificateType, String certificateTypeVersion) {
        throw new UnsupportedOperationException();
    }


    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        return DefaultCertificateMessagesProvider.create(validationMessages);
    }

    @Override
    public String getJsonFromUtlatande(Utlatande utlatande) throws ModuleException {
        if (utlatande instanceof Fk7263Utlatande) {
            return toInternalModelResponse((Fk7263Utlatande) utlatande);
        }
        final var message = utlatande == null ? "null" : utlatande.getClass().toString();
        throw new IllegalArgumentException(
            "Utlatande was not instance of class Fk7263Utlatande, utlatande was instance of class: " + message);
    }

    @Override
    public String getAdditionalInfoLabel() {
        return ADDITIONAL_INFO_LABEL;
    }

    @Override
    public String getUpdatedJsonWithTestData(String model, FillType fillType, TypeAheadProvider typeAheadProvider) throws ModuleException {
        try {
            final var utlatande = getUtlatandeFromJson(model);
            final var updatedUtlatande = TestabilityToolkit.getUtlatandeWithTestData(utlatande, fillType,
                new Fk7263TestabilityUtlatandeTestDataProvider());
            return getJsonFromUtlatande(updatedUtlatande);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CertificateText getPreambleForCitizens() {
        return CertificateText
            .builder()
            .type(CertificateTextType.PREAMBLE_TEXT)
            .text(PREAMBLE_FOR_CITIZEN)
            .links(List.of(
                CertificateLink.builder()
                    .url(FK_URL)
                    .name(FK_NAME)
                    .id(LINK_FK_ID)
                    .build()
            ))
            .build();
    }
}
