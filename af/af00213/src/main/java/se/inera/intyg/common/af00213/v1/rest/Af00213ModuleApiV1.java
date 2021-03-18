/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00213.v1.rest;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.af00213.pdf.PdfGenerator;
import se.inera.intyg.common.af00213.support.Af00213EntryPoint;
import se.inera.intyg.common.af00213.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.af00213.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.af00213.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.af_parent.rest.AfParentModuleApi;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataBooleanValueDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataConfigDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataElementDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataTextValueDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateDataValidationDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateMetaDataDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificatePatientDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateStaffDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateStatusDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.CertificateUnitDTO;
import se.inera.intyg.common.support.modules.support.facade.dto.ValidationErrorDTO;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.af00213.v1")
public class Af00213ModuleApiV1 extends AfParentModuleApi<Af00213UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(Af00213ModuleApiV1.class);
    public static final String SCHEMATRON_FILE = "af00213.v1.sch";

    public Af00213ModuleApiV1() {
        super(Af00213UtlatandeV1.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        Af00213UtlatandeV1 af00213Intyg = getInternal(internalModel);
        IntygTexts texts = getTexts(Af00213EntryPoint.MODULE_ID, af00213Intyg.getTextVersion());

        Personnummer personId = af00213Intyg.getGrundData().getPatient().getPersonId();
        return new PdfGenerator().generatePdf(af00213Intyg.getId(), internalModel, getMajorVersion(af00213Intyg.getTextVersion()), personId,
            texts, statuses, applicationOrigin,
            utkastStatus);
    }

    private String getMajorVersion(String textVersion) {
        return textVersion.split("\\.", 0)[0];
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {
        return null;
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(Af00213UtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected Af00213UtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(Af00213UtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected Af00213UtlatandeV1 decorateWithSignature(Af00213UtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        return null;
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
        throws ModuleException {
        try {
            if (!Af00213UtlatandeV1.class.isInstance(template)) {
                LOG.error("Could not create a new internal Webcert model using template of wrong type");
                throw new ModuleConverterException("Could not create a new internal Webcert model using template of wrong type");
            }

            Af00213UtlatandeV1 internal = (Af00213UtlatandeV1) template;

            // Null out applicable fields
            Af00213UtlatandeV1 renewCopy = internal.toBuilder()
                .build();

            Relation relation = draftCopyHolder.getRelation();
            draftCopyHolder.setRelation(relation);

            return toInternalModelResponse(webcertModelFactory.createCopy(draftCopyHolder, renewCopy));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public CertificateDTO getCertificateDTOFromJson(String certificateAsJson) throws ModuleException, IOException {
        final var internalCertificate = getInternal(certificateAsJson);

        final var certificateDTO = new CertificateDTO();

        final var metadata = new CertificateMetaDataDTO();
        metadata.setCertificateId(internalCertificate.getId());
        metadata.setCertificateType(internalCertificate.getTyp());
        metadata.setCertificateTypeVersion(internalCertificate.getTextVersion());
        metadata.setCertificateName("Arbetsförmedlingens medicinska utlåtande");
        metadata.setCertificateDescription(
            "Arbetsförmedlingen behöver ett medicinskt utlåtande för en arbetssökande som har ett behov av fördjupat stöd.\n"
                + "\n"
                + "Vi behöver ett utlåtande för att kunna:\n"
                + "\n"
                + "• utreda och bedöma om den arbetssökande har en funktionsnedsättning som medför nedsatt arbetsförmåga\n"
                + "• bedöma om vi behöver göra anpassningar i program eller insatser\n"
                + "• erbjuda lämpliga utredande, vägledande, rehabiliterande eller arbetsförberedande insatser.");
        metadata.setTestCertificate(internalCertificate.getGrundData().isTestIntyg());
        metadata.setCertificateStatus(
            internalCertificate.getSignature() != null ? CertificateStatusDTO.SIGNED : CertificateStatusDTO.UNSIGNED);

        final var internalCareUnit = internalCertificate.getGrundData().getSkapadAv().getVardenhet();
        final var unit = new CertificateUnitDTO();
        unit.setUnitId(internalCareUnit.getEnhetsid());
        unit.setUnitName(internalCareUnit.getEnhetsnamn());
        unit.setAddress(internalCareUnit.getPostadress());
        unit.setZipCode(internalCareUnit.getPostnummer());
        unit.setCity(internalCareUnit.getPostort());
        unit.setPhoneNumber(internalCareUnit.getTelefonnummer());
        unit.setEmail(internalCareUnit.getEpost());
        // TODO: Manage additional unit information.
        metadata.setUnit(unit);

        final var internalCareProvider = internalCertificate.getGrundData().getSkapadAv().getVardenhet().getVardgivare();
        final var careProvider = new CertificateUnitDTO();
        careProvider.setUnitId(internalCareProvider.getVardgivarid());
        careProvider.setUnitName(internalCareProvider.getVardgivarnamn());
        // TODO: Manage additional unit information.
        metadata.setCareProvider(careProvider);

        final var internalPatient = internalCertificate.getGrundData().getPatient();
        final var patient = new CertificatePatientDTO();
        patient.setPersonId(internalPatient.getPersonId().getPersonnummer());
        patient.setFirstName(internalPatient.getFornamn());
        patient.setLastName(internalPatient.getEfternamn());
        patient.setFullName(internalPatient.getFullstandigtNamn());
        patient.setCoordinationNumber(internalPatient.isSamordningsNummer());
        patient.setProtectedPerson(internalPatient.isSekretessmarkering());
        patient.setDeceased(internalPatient.isAvliden());
        patient.setTestIndicated(internalPatient.isTestIndicator());
        // TODO: Manage patient address
        metadata.setPatient(patient);

        final var internalIssuer = internalCertificate.getGrundData().getSkapadAv();
        final var issuedBy = new CertificateStaffDTO();
        issuedBy.setPersonId(internalIssuer.getPersonId());
        issuedBy.setFullName(internalIssuer.getFullstandigtNamn());
        issuedBy.setPrescriptionCode(internalIssuer.getForskrivarKod());
        // TODO: Manage speciality and positions
        metadata.setIssuedBy(issuedBy);

        certificateDTO.setMetadata(metadata);

        final var data = certificateDTO.getData();

        var index = 0;

        final var categoryFunktionsnedsattning = new CertificateDataElementDTO();
        categoryFunktionsnedsattning.setId(FUNKTIONSNEDSATTNING_CATEGORY_ID);
        categoryFunktionsnedsattning.setIndex(index++);
        categoryFunktionsnedsattning.setMandatory(false);
        categoryFunktionsnedsattning.setParent(null);
        categoryFunktionsnedsattning.setReadOnly(true);
        categoryFunktionsnedsattning.setVisible(true);
        final var categoryFunktionsnedsattningConfig = new CertificateDataConfigDTO();
        categoryFunktionsnedsattningConfig.setComponent("category");
        categoryFunktionsnedsattningConfig.setText("Funktionsnedsättning");
        categoryFunktionsnedsattningConfig.setDescription("");
        categoryFunktionsnedsattning.setConfig(categoryFunktionsnedsattningConfig);
        categoryFunktionsnedsattning.setValue(null);
        categoryFunktionsnedsattning.setValidation(null);
        categoryFunktionsnedsattning.setValidationError(new ValidationErrorDTO[0]);
        data.put(categoryFunktionsnedsattning.getId(), categoryFunktionsnedsattning);

        final var questionHarFunktionsnedsattning = new CertificateDataElementDTO();
        questionHarFunktionsnedsattning.setId(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);
        questionHarFunktionsnedsattning.setIndex(index++);
        questionHarFunktionsnedsattning.setMandatory(internalCertificate.getHarFunktionsnedsattning() == null);
        questionHarFunktionsnedsattning.setParent(FUNKTIONSNEDSATTNING_CATEGORY_ID);
        questionHarFunktionsnedsattning.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionHarFunktionsnedsattning.setVisible(true);
        final var questionHarFunktionsnedsattningConfig = new CertificateDataConfigDTO();
        questionHarFunktionsnedsattningConfig.setComponent("ue-radio");
        questionHarFunktionsnedsattningConfig.setText("Finns besvär på grund av sjukdom eller skada som medför funktionsnedsättning?");
        questionHarFunktionsnedsattningConfig
            .setDescription("Med besvär avses sådant som påverkar psykiska, psykosociala eller kroppsliga funktioner.");
        questionHarFunktionsnedsattningConfig.setProp(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11);
        questionHarFunktionsnedsattning.setConfig(questionHarFunktionsnedsattningConfig);
        final var questionHarFunktionsnedsattningValue = new CertificateDataBooleanValueDTO();
        questionHarFunktionsnedsattningValue.setSelected(internalCertificate.getHarFunktionsnedsattning());
        questionHarFunktionsnedsattningValue.setSelectedText("Ja");
        questionHarFunktionsnedsattningValue.setUnselectedText("Nej");
        questionHarFunktionsnedsattning.setValue(questionHarFunktionsnedsattningValue);
        final var questionHarFunktionsnedsattningValidation = new CertificateDataValidationDTO();
        questionHarFunktionsnedsattningValidation.setRequired(true);
        questionHarFunktionsnedsattning.setValidation(questionHarFunktionsnedsattningValidation);
        questionHarFunktionsnedsattning.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionHarFunktionsnedsattning.getId(), questionHarFunktionsnedsattning);

        final var questionFunktionsnedsattning = new CertificateDataElementDTO();
        questionFunktionsnedsattning.setId(FUNKTIONSNEDSATTNING_DELSVAR_ID_12);
        questionFunktionsnedsattning.setIndex(index++);
        questionFunktionsnedsattning
            .setMandatory(internalCertificate.getFunktionsnedsattning() == null || internalCertificate.getFunktionsnedsattning().isBlank());
        questionFunktionsnedsattning.setParent(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);
        questionFunktionsnedsattning.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionFunktionsnedsattning.setVisible(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED ||
            (internalCertificate.getHarFunktionsnedsattning() == null ? false : internalCertificate.getHarFunktionsnedsattning()));
        final var questionFunktionsnedsattningConfig = new CertificateDataConfigDTO();
        questionFunktionsnedsattningConfig.setComponent("ue-textarea");
        questionFunktionsnedsattningConfig
            .setText("Beskriv de funktionsnedsättningar som har observerats (undersökningsfynd). Ange, om möjligt, varaktighet.");
        questionFunktionsnedsattningConfig
            .setDescription(
                "Ange de nedsättningar som har framkommit vid undersökning eller utredning.\n\nTill exempel:\nMedvetenhet, uppmärksamhet, orienteringsförmåga\nSocial interaktion, agitation\nKognitiva störningar som t ex minnessvårigheter\nStörningar på sinnesorganen som t ex syn- och hörselnedsättning, balansrubbningar\nSmärta i rörelseorganen\nRörelseinskränkning, rörelseomfång, smidighet\nUthållighet, koordination\n\nMed varaktighet menas permanent eller övergående. Ange i så fall tidsangivelse vid övergående.");
        questionFunktionsnedsattningConfig.setProp(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12);
        questionFunktionsnedsattning.setConfig(questionFunktionsnedsattningConfig);
        final var questionFunktionsnedsattningValue = new CertificateDataTextValueDTO();
        questionFunktionsnedsattningValue.setText(internalCertificate.getFunktionsnedsattning());
        questionFunktionsnedsattning.setValue(questionFunktionsnedsattningValue);
        final var questionFunktionsnedsattningValidation = new CertificateDataValidationDTO();
        questionFunktionsnedsattningValidation.setRequired(true);
        questionFunktionsnedsattningValidation.setHideExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11);
        questionFunktionsnedsattning.setValidation(questionFunktionsnedsattningValidation);
        questionFunktionsnedsattning.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionFunktionsnedsattning.getId(), questionFunktionsnedsattning);

        final var categoryAktivitetsbegransning = new CertificateDataElementDTO();
        categoryAktivitetsbegransning.setId(AKTIVITETSBEGRANSNING_CATEGORY_ID);
        categoryAktivitetsbegransning.setIndex(index++);
        categoryAktivitetsbegransning.setMandatory(false);
        categoryAktivitetsbegransning.setParent(null);
        categoryAktivitetsbegransning.setReadOnly(true);
        categoryAktivitetsbegransning.setVisible(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED ||
            (internalCertificate.getHarFunktionsnedsattning() == null ? false : internalCertificate.getHarFunktionsnedsattning()));
        final var categoryAktivitetsbegransningConfig = new CertificateDataConfigDTO();
        categoryAktivitetsbegransningConfig.setComponent("category");
        categoryAktivitetsbegransningConfig.setText("Aktivitetsbegränsning");
        categoryAktivitetsbegransningConfig.setDescription("");
        categoryAktivitetsbegransning.setConfig(categoryAktivitetsbegransningConfig);
        categoryAktivitetsbegransning.setValue(null);
        final var categoryAktivitetsbegransningValidation = new CertificateDataValidationDTO();
        categoryAktivitetsbegransningValidation.setHideExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11);
        categoryAktivitetsbegransning.setValidation(categoryAktivitetsbegransningValidation);
        categoryAktivitetsbegransning.setValidationError(new ValidationErrorDTO[0]);
        data.put(categoryAktivitetsbegransning.getId(), categoryAktivitetsbegransning);

        final var questionHarAktivitetsbegransning = new CertificateDataElementDTO();
        questionHarAktivitetsbegransning.setId(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);
        questionHarAktivitetsbegransning.setIndex(index++);
        questionHarAktivitetsbegransning.setMandatory(internalCertificate.getHarAktivitetsbegransning() == null);
        questionHarAktivitetsbegransning.setParent(AKTIVITETSBEGRANSNING_CATEGORY_ID);
        questionHarAktivitetsbegransning.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionHarAktivitetsbegransning.setVisible(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED ||
            (internalCertificate.getHarFunktionsnedsattning() == null ? false : internalCertificate.getHarFunktionsnedsattning()));
        final var questionHarAktivitetsbegransningConfig = new CertificateDataConfigDTO();
        questionHarAktivitetsbegransningConfig.setComponent("ue-radio");
        questionHarAktivitetsbegransningConfig
            .setText("Leder funktionsnedsättningarna till aktivitetsbegränsningar i relation till arbete eller studier?");
        questionHarAktivitetsbegransningConfig
            .setDescription(
                "Aktivitet innebär personens möjlighet att genomföra en uppgift eller handling. Aktivitetsbegränsning ska bedömas utifrån de begränsningar personen har kopplat till att kunna söka arbete, genomföra en arbetsuppgift/arbetsuppgifter, kunna studera eller delta i aktivitet hos Arbetsförmedlingen.");
        questionHarAktivitetsbegransningConfig.setProp(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21);
        questionHarAktivitetsbegransning.setConfig(questionHarAktivitetsbegransningConfig);
        final var questionHarAktivitetsbegransningValue = new CertificateDataBooleanValueDTO();
        questionHarAktivitetsbegransningValue.setSelected(internalCertificate.getHarAktivitetsbegransning());
        questionHarAktivitetsbegransningValue.setSelectedText("Ja");
        questionHarAktivitetsbegransningValue.setUnselectedText("Nej");
        questionHarAktivitetsbegransning.setValue(questionHarAktivitetsbegransningValue);
        final var questionHarAktivitetsbegransningValidation = new CertificateDataValidationDTO();
        questionHarAktivitetsbegransningValidation.setRequired(true);
        questionHarAktivitetsbegransningValidation.setHideExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11);
        questionHarAktivitetsbegransning.setValidation(questionHarAktivitetsbegransningValidation);
        questionHarAktivitetsbegransning.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionHarAktivitetsbegransning.getId(), questionHarAktivitetsbegransning);

        final var questionAktivitetsbegransning = new CertificateDataElementDTO();
        questionAktivitetsbegransning.setId(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);
        questionAktivitetsbegransning.setIndex(index++);
        questionAktivitetsbegransning.setMandatory(
            internalCertificate.getAktivitetsbegransning() == null || internalCertificate.getAktivitetsbegransning().isBlank());
        questionAktivitetsbegransning.setParent(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);
        questionAktivitetsbegransning.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionAktivitetsbegransning.setVisible(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED ||
            (internalCertificate.getHarAktivitetsbegransning() == null ? false : internalCertificate.getHarAktivitetsbegransning()));
        final var questionAktivitetsbegransningConfig = new CertificateDataConfigDTO();
        questionAktivitetsbegransningConfig.setComponent("ue-textarea");
        questionAktivitetsbegransningConfig
            .setText("Ange vilka aktivitetsbegränsningar? Ange hur och om möjligt varaktighet/prognos.");
        questionAktivitetsbegransningConfig
            .setDescription(
                "Ge konkreta exempel på aktivitetsbegränsningar utifrån personens planerade insatser hos Arbetsförmedlingen eller personens möjlighet att söka arbete, genomföra en arbetsuppgift/arbetsuppgifter eller studera. Till exempel:\n\natt ta till sig en instruktion\natt ta reda på och förstå muntlig eller skriftlig information\natt kunna fokusera\natt kunna bära eller lyfta\natt kunna hantera statiskt arbete");
        questionAktivitetsbegransningConfig.setProp(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22);
        questionAktivitetsbegransning.setConfig(questionAktivitetsbegransningConfig);
        final var questionAktivitetsbegransningValue = new CertificateDataTextValueDTO();
        questionAktivitetsbegransningValue.setText(internalCertificate.getAktivitetsbegransning());
        questionAktivitetsbegransning.setValue(questionAktivitetsbegransningValue);
        final var questionAktivitetsbegransningValidation = new CertificateDataValidationDTO();
        questionAktivitetsbegransningValidation.setRequired(true);
        questionAktivitetsbegransningValidation.setHideExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21);
        questionAktivitetsbegransning.setValidation(questionAktivitetsbegransningValidation);
        questionAktivitetsbegransning.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionAktivitetsbegransning.getId(), questionAktivitetsbegransning);

        final var categoryUtredningBehandling = new CertificateDataElementDTO();
        categoryUtredningBehandling.setId(UTREDNING_BEHANDLING_CATEGORY_ID);
        categoryUtredningBehandling.setIndex(index++);
        categoryUtredningBehandling.setMandatory(false);
        categoryUtredningBehandling.setParent(null);
        categoryUtredningBehandling.setReadOnly(true);
        categoryUtredningBehandling.setVisible(true);
        final var categoryUtredningBehandlingConfig = new CertificateDataConfigDTO();
        categoryUtredningBehandlingConfig.setComponent("category");
        categoryUtredningBehandlingConfig.setText("Utredning och behandling");
        categoryUtredningBehandlingConfig.setDescription("");
        categoryUtredningBehandling.setConfig(categoryUtredningBehandlingConfig);
        categoryUtredningBehandling.setValue(null);
        categoryUtredningBehandling.setValidationError(new ValidationErrorDTO[0]);
        data.put(categoryUtredningBehandling.getId(), categoryUtredningBehandling);

        final var questionHarUtredningsbehandling = new CertificateDataElementDTO();
        questionHarUtredningsbehandling.setId(UTREDNING_BEHANDLING_DELSVAR_ID_31);
        questionHarUtredningsbehandling.setIndex(index++);
        questionHarUtredningsbehandling.setMandatory(internalCertificate.getHarUtredningBehandling() == null);
        questionHarUtredningsbehandling.setParent(UTREDNING_BEHANDLING_CATEGORY_ID);
        questionHarUtredningsbehandling.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionHarUtredningsbehandling.setVisible(true);
        final var questionHarUtredningsBehandlingConfig = new CertificateDataConfigDTO();
        questionHarUtredningsBehandlingConfig.setComponent("ue-radio");
        questionHarUtredningsBehandlingConfig
            .setText(
                "Finns pågående eller planerade utredningar/behandlingar som påverkar den planering som Arbetsförmedlingen har beskrivit i förfrågan?");
        questionHarUtredningsBehandlingConfig
            .setDescription(
                "Till exempel remiss för bedömning eller åtgärd inom annan vårdenhet eller aktiviteter inom egna verksamheten.");
        questionHarUtredningsBehandlingConfig.setProp(UTREDNING_BEHANDLING_SVAR_JSON_ID_31);
        questionHarUtredningsbehandling.setConfig(questionHarUtredningsBehandlingConfig);
        final var questionHarUtredningsBehandlingValue = new CertificateDataBooleanValueDTO();
        questionHarUtredningsBehandlingValue.setSelected(internalCertificate.getHarUtredningBehandling());
        questionHarUtredningsBehandlingValue.setSelectedText("Ja");
        questionHarUtredningsBehandlingValue.setUnselectedText("Nej");
        questionHarUtredningsbehandling.setValue(questionHarUtredningsBehandlingValue);
        final var questionHarUtredningsBehandlingValidation = new CertificateDataValidationDTO();
        questionHarUtredningsBehandlingValidation.setRequired(true);
        questionHarUtredningsbehandling.setValidation(questionHarUtredningsBehandlingValidation);
        questionHarUtredningsbehandling.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionHarUtredningsbehandling.getId(), questionHarUtredningsbehandling);

        final var questionUtredningsbehandling = new CertificateDataElementDTO();
        questionUtredningsbehandling.setId(UTREDNING_BEHANDLING_DELSVAR_ID_32);
        questionUtredningsbehandling.setIndex(index++);
        questionUtredningsbehandling
            .setMandatory(internalCertificate.getUtredningBehandling() == null || internalCertificate.getUtredningBehandling().isBlank());
        questionUtredningsbehandling.setParent(UTREDNING_BEHANDLING_DELSVAR_ID_31);
        questionUtredningsbehandling.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionUtredningsbehandling
            .setVisible(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED ||
                (internalCertificate.getHarUtredningBehandling() == null ? false : internalCertificate.getHarUtredningBehandling()));
        final var questionUtredningsBehandlingConfig = new CertificateDataConfigDTO();
        questionUtredningsBehandlingConfig.setComponent("ue-textarea");
        questionUtredningsBehandlingConfig
            .setText(
                "Hur påverkar utredningarna/behandlingarna planeringen? När planeras utredningarna/behandlingarna att vara avslutade?");
        questionUtredningsBehandlingConfig
            .setDescription(
                "Utgå från den beskrivning Arbetsförmedlingen har gjort av personen och Arbetsförmedlingens planerade aktiviteter.\n\nAnge förväntat resultat för de utredningar eller behandlingar som ska genomföras i vården och när personen kan genomföra/delta i Arbetsförmedlingens planerade aktiviteter.");
        questionUtredningsBehandlingConfig.setProp(UTREDNING_BEHANDLING_SVAR_JSON_ID_32);
        questionUtredningsbehandling.setConfig(questionUtredningsBehandlingConfig);
        final var questionUtredningsBehandlingValue = new CertificateDataTextValueDTO();
        questionUtredningsBehandlingValue.setText(internalCertificate.getUtredningBehandling());
        questionUtredningsbehandling.setValue(questionUtredningsBehandlingValue);
        final var questionUtredningsBehandlingValidation = new CertificateDataValidationDTO();
        questionUtredningsBehandlingValidation.setRequired(true);
        questionUtredningsBehandlingValidation.setHideExpression(UTREDNING_BEHANDLING_SVAR_JSON_ID_31);
        questionUtredningsbehandling.setValidation(questionUtredningsBehandlingValidation);
        questionUtredningsbehandling.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionUtredningsbehandling.getId(), questionUtredningsbehandling);

        final var categoryArbetsPaverkan = new CertificateDataElementDTO();
        categoryArbetsPaverkan.setId(ARBETETS_PAVERKAN_CATEGORY_ID);
        categoryArbetsPaverkan.setIndex(index++);
        categoryArbetsPaverkan.setMandatory(false);
        categoryArbetsPaverkan.setParent(null);
        categoryArbetsPaverkan.setReadOnly(true);
        categoryArbetsPaverkan.setVisible(true);
        final var categoryArbetsPaverkanConfig = new CertificateDataConfigDTO();
        categoryArbetsPaverkanConfig.setComponent("category");
        categoryArbetsPaverkanConfig.setText("Arbetets påverkan på sjukdom/skada");
        categoryArbetsPaverkanConfig.setDescription("");
        categoryArbetsPaverkan.setConfig(categoryArbetsPaverkanConfig);
        categoryArbetsPaverkan.setValue(null);
        categoryArbetsPaverkan.setValidationError(new ValidationErrorDTO[0]);
        data.put(categoryArbetsPaverkan.getId(), categoryArbetsPaverkan);

        final var questionHarArbetsPaverkan = new CertificateDataElementDTO();
        questionHarArbetsPaverkan.setId(ARBETETS_PAVERKAN_DELSVAR_ID_41);
        questionHarArbetsPaverkan.setIndex(index++);
        questionHarArbetsPaverkan.setMandatory(internalCertificate.getHarArbetetsPaverkan() == null);
        questionHarArbetsPaverkan.setParent(ARBETETS_PAVERKAN_CATEGORY_ID);
        questionHarArbetsPaverkan.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionHarArbetsPaverkan.setVisible(true);
        final var questionHarArbetsPaverkanConfig = new CertificateDataConfigDTO();
        questionHarArbetsPaverkanConfig.setComponent("ue-radio");
        questionHarArbetsPaverkanConfig
            .setText(
                "Kan sjukdomen/skadan förvärras av vissa arbetsuppgifter/arbetsmoment?");
        questionHarArbetsPaverkanConfig
            .setDescription(
                "Utgå från den beskrivning Arbetsförmedlingen har gjort av personen och Arbetsförmedlingens planerade aktiviteter.");
        questionHarArbetsPaverkanConfig.setProp(ARBETETS_PAVERKAN_SVAR_JSON_ID_41);
        questionHarArbetsPaverkan.setConfig(questionHarArbetsPaverkanConfig);
        final var questionHarArbetsPaverkanValue = new CertificateDataBooleanValueDTO();
        questionHarArbetsPaverkanValue.setSelected(internalCertificate.getHarArbetetsPaverkan());
        questionHarArbetsPaverkanValue.setSelectedText("Ja");
        questionHarArbetsPaverkanValue.setUnselectedText("Nej");
        questionHarArbetsPaverkan.setValue(questionHarArbetsPaverkanValue);
        final var questionHarArbetsPaverkanValidation = new CertificateDataValidationDTO();
        questionHarArbetsPaverkanValidation.setRequired(true);
        questionHarArbetsPaverkan.setValidation(questionHarArbetsPaverkanValidation);
        questionHarArbetsPaverkan.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionHarArbetsPaverkan.getId(), questionHarArbetsPaverkan);

        final var questionArbetsPaverkan = new CertificateDataElementDTO();
        questionArbetsPaverkan.setId(ARBETETS_PAVERKAN_DELSVAR_ID_42);
        questionArbetsPaverkan.setIndex(index++);
        questionArbetsPaverkan
            .setMandatory(internalCertificate.getArbetetsPaverkan() == null || internalCertificate.getArbetetsPaverkan().isBlank());
        questionArbetsPaverkan.setParent(ARBETETS_PAVERKAN_DELSVAR_ID_41);
        questionArbetsPaverkan.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionArbetsPaverkan
            .setVisible(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED ||
                (internalCertificate.getHarArbetetsPaverkan() == null ? false : internalCertificate.getHarArbetetsPaverkan()));
        final var questionArbetsPaverkanConfig = new CertificateDataConfigDTO();
        questionArbetsPaverkanConfig.setComponent("ue-textarea");
        questionArbetsPaverkanConfig
            .setText(
                "Vilken typ av arbetsuppgifter/arbetsmoment?");
        questionArbetsPaverkanConfig
            .setDescription(
                "Utgå från den beskrivning Arbetsförmedlingen har gjort av personen och Arbetsförmedlingens planerade aktiviteter.\n\nTill exempel:\n\narmarbete ovan axelhöjd\narbete på höga höjder\nskärmarbete vid dator under längre tid\nstatiskt arbete");
        questionArbetsPaverkanConfig.setProp(ARBETETS_PAVERKAN_SVAR_JSON_ID_42);
        questionArbetsPaverkan.setConfig(questionArbetsPaverkanConfig);
        final var questionArbetsPaverkanValue = new CertificateDataTextValueDTO();
        questionArbetsPaverkanValue.setText(internalCertificate.getArbetetsPaverkan());
        questionArbetsPaverkan.setValue(questionArbetsPaverkanValue);
        final var questionArbetsPaverkanValidation = new CertificateDataValidationDTO();
        questionArbetsPaverkanValidation.setRequired(true);
        questionArbetsPaverkanValidation.setHideExpression(ARBETETS_PAVERKAN_SVAR_JSON_ID_41);
        questionArbetsPaverkan.setValidation(questionArbetsPaverkanValidation);
        questionArbetsPaverkan.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionArbetsPaverkan.getId(), questionArbetsPaverkan);

        final var categoryOvrigt = new CertificateDataElementDTO();
        categoryOvrigt.setId(OVRIGT_CATEGORY_ID);
        categoryOvrigt.setIndex(index++);
        categoryOvrigt.setMandatory(false);
        categoryOvrigt.setParent(null);
        categoryOvrigt.setReadOnly(true);
        categoryOvrigt.setVisible(true);
        final var categoryOvrigtConfig = new CertificateDataConfigDTO();
        categoryOvrigtConfig.setComponent("category");
        categoryOvrigtConfig.setText("Övrigt");
        categoryOvrigtConfig.setDescription("");
        categoryOvrigt.setConfig(categoryOvrigtConfig);
        categoryOvrigt.setValue(null);
        categoryOvrigt.setValidationError(new ValidationErrorDTO[0]);
        data.put(categoryOvrigt.getId(), categoryOvrigt);

        final var questionOvrigt = new CertificateDataElementDTO();
        questionOvrigt.setId(OVRIGT_DELSVAR_ID_5);
        questionOvrigt.setIndex(index++);
        questionOvrigt.setMandatory(false);
        questionOvrigt.setParent(OVRIGT_CATEGORY_ID);
        questionOvrigt.setReadOnly(metadata.getCertificateStatus() != CertificateStatusDTO.UNSIGNED);
        questionOvrigt.setVisible(true);
        final var questionOvrigtConfig = new CertificateDataConfigDTO();
        questionOvrigtConfig.setComponent("ue-textarea");
        questionOvrigtConfig
            .setText(
                "Övrigt som Arbetsförmedlingen bör känna till?");
        questionOvrigtConfig
            .setDescription(
                "Som t ex risker för försämring vid andra aktiviteter än de som Arbetsförmedlingen har beskrivit i förfrågan.");
        questionOvrigt.setConfig(questionOvrigtConfig);
        final var questionOvrigtValue = new CertificateDataTextValueDTO();
        questionOvrigtValue.setText(internalCertificate.getOvrigt());
        questionOvrigt.setValue(questionOvrigtValue);
        final var questionOvrigtValidation = new CertificateDataValidationDTO();
        questionOvrigtValidation.setRequired(false);
        questionOvrigt.setValidation(questionOvrigtValidation);
        questionOvrigt.setValidationError(new ValidationErrorDTO[0]);
        data.put(questionOvrigt.getId(), questionOvrigt);

        return certificateDTO;
    }

    @Override
    public String getJsonFromCertificateDTO(CertificateDTO certificate, String certificateAsJson) throws ModuleException, IOException {
        final var internalCertificate = getInternal(certificateAsJson);

        final var harFunktionsNedsattning = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_11);
        final var funktionsNedsattning = certificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_12);
        final var harAktivitetsbegransning = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);
        final var aktivitetsbegransning = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22);
        final var harUtredningBehandling = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_31);
        final var utredningBehandling = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);
        final var harArbetspaverkan = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_41);
        final var arbetspaverkan = certificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_42);
        final var ovrigt = certificate.getData().get(OVRIGT_DELSVAR_ID_5);

        final var grundData = internalCertificate.getGrundData();
        grundData.getSkapadAv().getVardenhet().setPostadress(certificate.getMetadata().getUnit().getAddress());
        grundData.getSkapadAv().getVardenhet().setPostnummer(certificate.getMetadata().getUnit().getZipCode());
        grundData.getSkapadAv().getVardenhet().setPostort(certificate.getMetadata().getUnit().getCity());
        grundData.getSkapadAv().getVardenhet().setTelefonnummer(certificate.getMetadata().getUnit().getPhoneNumber());

        final var updateInternalCertificate = Af00213UtlatandeV1.builder()
            .setGrundData(grundData)
            .setHarFunktionsnedsattning(((CertificateDataBooleanValueDTO) harFunktionsNedsattning.getValue()).getSelected())
            .setFunktionsnedsattning(((CertificateDataTextValueDTO) funktionsNedsattning.getValue()).getText())
            .setHarAktivitetsbegransning(((CertificateDataBooleanValueDTO) harAktivitetsbegransning.getValue()).getSelected())
            .setAktivitetsbegransning(((CertificateDataTextValueDTO) aktivitetsbegransning.getValue()).getText())
            .setHarUtredningBehandling(((CertificateDataBooleanValueDTO) harUtredningBehandling.getValue()).getSelected())
            .setUtredningBehandling(((CertificateDataTextValueDTO) utredningBehandling.getValue()).getText())
            .setHarArbetetsPaverkan(((CertificateDataBooleanValueDTO) harArbetspaverkan.getValue()).getSelected())
            .setArbetetsPaverkan(((CertificateDataTextValueDTO) arbetspaverkan.getValue()).getText())
            .setOvrigt(((CertificateDataTextValueDTO) ovrigt.getValue()).getText())
            .setId(certificate.getMetadata().getCertificateId())
            .setTextVersion(certificate.getMetadata().getCertificateTypeVersion())
            .build();

        return toInternalModelResponse(updateInternalCertificate);
    }

    private CertificateDTO createDummyCertificate() {
        final CertificateMetaDataDTO metaData = new CertificateMetaDataDTO();
        metaData.setCertificateId("bed26d3e-7112-4f08-98bf-01be40e26c80");
        metaData.setCertificateType("af00213");
        metaData.setCertificateTypeVersion("1.0");
        metaData.setCertificateName("Arbetsförmedlingens medicinska utlåtande");
        metaData.setCertificateDescription("Här är intygstypens beskrivning...");
        metaData.setCertificateStatus(CertificateStatusDTO.UNSIGNED);

        final CertificateDataElementDTO funktionsnedsattningCategoryDataElement = new CertificateDataElementDTO();
        funktionsnedsattningCategoryDataElement.setId("funktionsnedsattning");
        funktionsnedsattningCategoryDataElement.setParent("");
        funktionsnedsattningCategoryDataElement.setIndex(0);
        funktionsnedsattningCategoryDataElement.setVisible(true);
        funktionsnedsattningCategoryDataElement.setReadOnly(false);

        final CertificateDataConfigDTO funktionsnedsattningCategoryDataConfig = new CertificateDataConfigDTO();
        funktionsnedsattningCategoryDataConfig.setText("Funktionsnedsättning");
        funktionsnedsattningCategoryDataConfig.setComponent("category");

        funktionsnedsattningCategoryDataElement.setConfig(funktionsnedsattningCategoryDataConfig);

        final CertificateDataElementDTO harFunktionsnedsattningDataElement = new CertificateDataElementDTO();
        harFunktionsnedsattningDataElement.setId("1");
        harFunktionsnedsattningDataElement.setParent("funktionsnedsattning");
        harFunktionsnedsattningDataElement.setIndex(1);
        harFunktionsnedsattningDataElement.setVisible(true);
        harFunktionsnedsattningDataElement.setReadOnly(false);
        harFunktionsnedsattningDataElement.setMandatory(true);

        final CertificateDataConfigDTO harFunktionsnedsattningDataConfig = new CertificateDataConfigDTO();
        harFunktionsnedsattningDataConfig.setText("Finns besvär på grund av sjukdom eller skada som medför funktionsnedsättning?");
        harFunktionsnedsattningDataConfig
            .setDescription("Med besvär avses sådant som påverkar psykiska, psykosociala eller kroppsliga funktioner.");
        harFunktionsnedsattningDataConfig.setComponent("ue-radio");

        final CertificateDataBooleanValueDTO harFunktionsnedsattningValue = new CertificateDataBooleanValueDTO();
        harFunktionsnedsattningValue.setSelectedText("Ja");
        harFunktionsnedsattningValue.setUnselectedText("Nej");

        final CertificateDataValidationDTO harFunktionsnedsattningValidation = new CertificateDataValidationDTO();
        harFunktionsnedsattningValidation.setRequired(true);
        harFunktionsnedsattningValidation.setRequiredProp("harFunktionsnedsattning");

        harFunktionsnedsattningDataElement.setConfig(harFunktionsnedsattningDataConfig);
        harFunktionsnedsattningDataElement.setValue(harFunktionsnedsattningValue);
        harFunktionsnedsattningDataElement.setValidation(harFunktionsnedsattningValidation);

        final CertificateDataElementDTO funktionsnedsattningDataElement = new CertificateDataElementDTO();
        funktionsnedsattningDataElement.setId("1.1");
        funktionsnedsattningDataElement.setParent("1");
        funktionsnedsattningDataElement.setIndex(2);
        funktionsnedsattningDataElement.setVisible(false);
        funktionsnedsattningDataElement.setReadOnly(false);
        funktionsnedsattningDataElement.setMandatory(true);

        final CertificateDataConfigDTO funktionsnedsattningDataConfig = new CertificateDataConfigDTO();
        funktionsnedsattningDataConfig
            .setText("Beskriv de funktionsnedsättningar som har observerats (undersökningsfynd). Ange, om möjligt, varaktighet.");
        funktionsnedsattningDataConfig
            .setDescription(
                "Ange de nedsättningar som har framkommit vid undersökning eller utredning.\n\nTill exempel:\nMedvetenhet, uppmärksamhet, orienteringsförmåga\nSocial interaktion, agitation\nKognitiva störningar som t ex minnessvårigheter\nStörningar på sinnesorganen som t ex syn- och hörselnedsättning, balansrubbningar\nSmärta i rörelseorganen\nRörelseinskränkning, rörelseomfång, smidighet\nUthållighet, koordination\n\nMed varaktighet menas permanent eller övergående. Ange i så fall tidsangivelse vid övergående.");
        funktionsnedsattningDataConfig.setComponent("ue-textarea");

        final CertificateDataTextValueDTO funktionsnedsattningValue = new CertificateDataTextValueDTO();
        funktionsnedsattningValue.setLimit(50);

        final CertificateDataValidationDTO funktionsnedsattningValidation = new CertificateDataValidationDTO();
        funktionsnedsattningValidation.setRequired(true);
        funktionsnedsattningValidation.setRequiredProp("harFunktionsnedsattning");
        funktionsnedsattningValidation.setHideExpression("!harFunktionsnedsattning");

        funktionsnedsattningDataElement.setConfig(funktionsnedsattningDataConfig);
        funktionsnedsattningDataElement.setValue(funktionsnedsattningValue);
        funktionsnedsattningDataElement.setValidation(funktionsnedsattningValidation);

        final CertificateDTO certificate = new CertificateDTO();
        certificate.setMetadata(metaData);
        certificate.setData(new HashMap<>());
        certificate.getData().put(funktionsnedsattningCategoryDataElement.getId(), funktionsnedsattningCategoryDataElement);
        certificate.getData().put(harFunktionsnedsattningDataElement.getId(), harFunktionsnedsattningDataElement);
        certificate.getData().put(funktionsnedsattningDataElement.getId(), funktionsnedsattningDataElement);

        return certificate;
    }
}
