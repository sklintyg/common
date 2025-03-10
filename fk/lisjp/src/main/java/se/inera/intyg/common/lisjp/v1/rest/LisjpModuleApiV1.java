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
package se.inera.intyg.common.lisjp.v1.rest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.rest.FkParentModuleApi;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.lisjp.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.lisjp.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.v1.pdf.AbstractLisjpPdfDefinitionBuilder;
import se.inera.intyg.common.lisjp.v1.pdf.DefaultLisjpPdfDefinitionBuilder;
import se.inera.intyg.common.lisjp.v1.testability.LispTestabilityCertificateTestdataProvider;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.messages.DefaultCertificateMessagesProvider;
import se.inera.intyg.common.services.messages.MessagesParser;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateLink;
import se.inera.intyg.common.support.facade.model.CertificateText;
import se.inera.intyg.common.support.facade.model.CertificateTextType;
import se.inera.intyg.common.support.facade.util.TestabilityToolkit;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.AdditionalMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.modules.support.facade.FillType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.lisjp.v1")
public class LisjpModuleApiV1 extends FkParentModuleApi<LisjpUtlatandeV1> {

    public static final String SCHEMATRON_FILE = "lisjp.v1.sch";
    private static final Logger LOG = LoggerFactory.getLogger(LisjpModuleApiV1.class);

    private static final String CERTIFICATE_FILE_PREFIX = "lakarintyg_sjukpenning";
    private static final String ADDITIONAL_INFO_LABEL = "Gäller intygsperiod";
    private static final String LINK_FK_ID = "linkFK";
    private static final String PREAMBLE_FOR_CITIZENS = "Det här är ditt intyg. Intyget innehåller all information som vården fyllt i. "
        + "Du kan inte ändra något i ditt intyg. Har du frågor kontaktar du den som skrivit ditt intyg. "
        + "Om du vill ansöka om sjukpenning, gör du det på {" + LINK_FK_ID + "}.";
    private static final String FK_URL = "http://www.forsakringskassan.se/sjuk";
    private static final String FK_NAME = "Försäkringskassan";
    private Map<String, String> validationMessages;
    @Autowired(required = false)
    private SummaryConverter summaryConverter;
    @Value("${pdf.watermark.description.text:Du kan se intyget genom att logga in på 1177.se}")
    private String pdfWatermarkDescription;

    @Value("${pdf.margin.printed.from.app.name:Intyget är utskrivet från 1177 intyg}")
    private String pdfMinaIntygMarginText;

    public LisjpModuleApiV1() {
        super(LisjpUtlatandeV1.class);
        init();
    }

    private void init() {
        try {
            final var inputStream1 = new ClassPathResource("/common/messages.js").getInputStream();
            final var inputStream2 = new ClassPathResource("lisjp-messages.js").getInputStream();
            validationMessages = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        } catch (IOException exception) {
            LOG.error("Error during initialization. Could not read messages files");
            throw new RuntimeException("Error during initialization. Could not read messages files", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        LisjpUtlatandeV1 luseIntyg = getInternal(internalModel);
        return generatePdf(new DefaultLisjpPdfDefinitionBuilder(pdfWatermarkDescription), statuses, luseIntyg, applicationOrigin,
            CERTIFICATE_FILE_PREFIX,
            utkastStatus);
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(LisjpUtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande, moduleService);
    }

    @Override
    protected LisjpUtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(LisjpUtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande, moduleService);
    }

    @Override
    protected LisjpUtlatandeV1 decorateDiagnoserWithDescriptions(LisjpUtlatandeV1 utlatande) {
        List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
            .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
            .collect(Collectors.toList());
        return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
    }

    @Override
    protected LisjpUtlatandeV1 decorateUtkastWithComment(LisjpUtlatandeV1 utlatande, String comment) {

        return utlatande.toBuilder()
            .setOvrigt(concatOvrigtFalt(utlatande.getOvrigt(), comment))
            .build();
    }

    @Override
    protected LisjpUtlatandeV1 decorateWithSignature(LisjpUtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        try {
            return transportToInternal(intyg).getSjukskrivningar().stream()
                .map(Sjukskrivning::getPeriod)
                .sorted(Comparator.comparing(InternalLocalDateInterval::fromAsLocalDate))
                .reduce((a, b) -> new InternalLocalDateInterval(a.getFrom(), b.getTom()))
                .map(interval -> interval.getFrom().toString() + " - " + interval.getTom().toString())
                .orElse(null);

        } catch (ConverterException e) {
            throw new ModuleException("Could not convert Intyg to Utlatande and as a result could not get additional info", e);
        }
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
        throws ModuleException {
        try {
            if (!LisjpUtlatandeV1.class.isInstance(template)) {
                LOG.error("Could not create a new internal Webcert model using template of wrong type");
                throw new ModuleConverterException("Could not create a new internal Webcert model using template of wrong type");
            }

            LisjpUtlatandeV1 internal = (LisjpUtlatandeV1) template;

            // Null out applicable fields
            LisjpUtlatandeV1 renewCopy = internal.toBuilder()
                .setKontaktMedFk(false)
                .setAnledningTillKontakt(null)
                .setUndersokningAvPatienten(null)
                .setTelefonkontaktMedPatienten(null)
                .setJournaluppgifter(null)
                .setAnnatGrundForMU(null)
                .setAnnatGrundForMUBeskrivning(null)
                .setMotiveringTillTidigtStartdatumForSjukskrivning(null)
                .setMotiveringTillInteBaseratPaUndersokning(null)
                .setPrognos(null)
                .setSjukskrivningar(new ArrayList<>())
                .setArbetstidsforlaggning(null)
                .setArbetstidsforlaggningMotivering(null)
                .build();

            Relation relation = draftCopyHolder.getRelation();
            Optional<LocalDate> lastDateOfLastIntyg = internal.getSjukskrivningar().stream()
                .sorted((s1, s2) -> s2.getPeriod().getTom().asLocalDate().compareTo(s1.getPeriod().getTom().asLocalDate()))
                .map(sjukskrivning -> sjukskrivning.getPeriod().getTom().asLocalDate())
                .findFirst();
            relation.setSistaGiltighetsDatum(lastDateOfLastIntyg.orElse(LocalDate.now()));
            Optional<Sjukskrivning.SjukskrivningsGrad> lastSjukskrivningsgradOfLastIntyg = internal.getSjukskrivningar().stream()
                .sorted((s1, s2) -> s2.getPeriod().getTom().asLocalDate().compareTo(s1.getPeriod().getTom().asLocalDate()))
                .map(sjukskrivning -> sjukskrivning.getSjukskrivningsgrad())
                .findFirst();
            lastSjukskrivningsgradOfLastIntyg.ifPresent(grad -> relation.setSistaSjukskrivningsgrad(grad.getLabel()));

            draftCopyHolder.setRelation(relation);

            return toInternalModelResponse(webcertModelFactory.createCopy(draftCopyHolder, renewCopy));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    private PdfResponse generatePdf(AbstractLisjpPdfDefinitionBuilder builder, List<Status> statuses, LisjpUtlatandeV1 luseIntyg,
        ApplicationOrigin applicationOrigin, String filePrefix, UtkastStatus utkastStatus) throws ModuleException {
        try {
            IntygTexts texts = getTexts(LisjpEntryPoint.MODULE_ID, luseIntyg.getTextVersion());

            final FkPdfDefinition fkPdfDefinition = builder.buildPdfDefinition(luseIntyg, statuses, applicationOrigin, texts, utkastStatus,
                pdfMinaIntygMarginText);

            return new PdfResponse(PdfGenerator.generatePdf(fkPdfDefinition, filePrefix),
                PdfGenerator.generatePdfFilename(LocalDateTime.now(), filePrefix));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate!", e);
        }
    }

    @Override
    public Optional<AdditionalMetaData> getAdditionalMetaData(Intyg certificate) throws ModuleException {
        final var additionalMetaData = new AdditionalMetaData();

        final var lisjpCertificate = convertToInternal(certificate);

        final var diagnoses = getDiagnoses(lisjpCertificate.getDiagnoser());

        additionalMetaData.setDiagnoses(diagnoses);

        return Optional.of(additionalMetaData);
    }

    private LisjpUtlatandeV1 convertToInternal(Intyg certificate) throws ModuleException {
        try {
            return transportToInternal(certificate);
        } catch (ConverterException e) {
            throw new ModuleException("Could convert Intyg to Utlatande", e);
        }
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var certificateTextProvider = getTextProvider(internalCertificate.getTyp(), internalCertificate.getTextVersion());
        final var certificate = InternalToCertificate.convert(internalCertificate, certificateTextProvider);
        final var certificateSummary = summaryConverter.convert(this, getIntygFromUtlatande(internalCertificate));
        certificate.getMetadata().setSummary(certificateSummary);
        return certificate;
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var updateInternalCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);
        return toInternalModelResponse(updateInternalCertificate);
    }

    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        return DefaultCertificateMessagesProvider.create(validationMessages);
    }

    @Override
    public String getJsonFromUtlatande(Utlatande utlatande) throws ModuleException {
        if (utlatande instanceof LisjpUtlatandeV1) {
            return toInternalModelResponse((LisjpUtlatandeV1) utlatande);
        }
        final var message = utlatande == null ? "null" : utlatande.getClass().toString();
        throw new IllegalArgumentException(
            "Utlatande was not instance of class LisjpUtlatandeV1, utlatande was instance of class: " + message);
    }

    @Override
    public String getAdditionalInfoLabel() {
        return ADDITIONAL_INFO_LABEL;
    }

    @Override
    public String getUpdatedJsonWithTestData(String model, FillType fillType, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var certificate = getCertificateFromJson(model, typeAheadProvider);
        TestabilityToolkit.fillCertificateWithTestData(certificate, fillType, new LispTestabilityCertificateTestdataProvider());
        return getJsonFromCertificate(certificate, model);
    }

    @Override
    public CertificateText getPreambleForCitizens() {
        return CertificateText
            .builder()
            .text(PREAMBLE_FOR_CITIZENS)
            .type(CertificateTextType.PREAMBLE_TEXT)
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
