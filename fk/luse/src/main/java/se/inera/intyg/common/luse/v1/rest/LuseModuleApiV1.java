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
package se.inera.intyg.common.luse.v1.rest;

import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMU_ANNAT_LABEL;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMU_JOURNALUPPGIFTER_LABEL;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMU_UNDERSOKNING_LABEL;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.rest.FkParentModuleApi;
import se.inera.intyg.common.luse.support.LuseEntryPoint;
import se.inera.intyg.common.luse.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.luse.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.luse.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.luse.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.luse.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.luse.v1.model.internal.LuseUtlatandeV1;
import se.inera.intyg.common.luse.v1.pdf.LusePdfDefinitionBuilder;
import se.inera.intyg.common.luse.v1.testability.LuseTestabilityCertificateTestdataProvider;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.messages.DefaultCertificateMessagesProvider;
import se.inera.intyg.common.services.messages.MessagesParser;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.util.TestabilityToolkit;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.AdditionalMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.modules.support.facade.FillType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.luse.v1")
public class LuseModuleApiV1 extends FkParentModuleApi<LuseUtlatandeV1> {

    private static final String ADDITIONAL_INFO_LABEL = "Avser diagnos";
    @Autowired
    private InternalToCertificate internalToCertificate;
    @Autowired
    private CertificateToInternal certificateToInternal;
    public static final String SCHEMATRON_FILE = "luse.v1.sch";
    private static final Logger LOG = LoggerFactory.getLogger(LuseModuleApiV1.class);
    private static final String CERTIFICATE_FILE_PREFIX = "lakarutlatande_sjukersattning";

    private Map<String, String> validationMessages;


    public LuseModuleApiV1() {
        super(LuseUtlatandeV1.class);
        init();
    }

    private void init() {
        try {
            final var inputStream1 = new ClassPathResource("/META-INF/resources/webjars/common/webcert/messages.js").getInputStream();
            final var inputStream2
                = new ClassPathResource("/META-INF/resources/webjars/luse/webcert/views/messages.js").getInputStream();
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
        try {
            LuseUtlatandeV1 luseIntyg = getInternal(internalModel);
            LusePdfDefinitionBuilder builder = new LusePdfDefinitionBuilder();
            IntygTexts texts = getTexts(LuseEntryPoint.MODULE_ID, luseIntyg.getTextVersion());

            final FkPdfDefinition fkPdfDefinition = builder.buildPdfDefinition(luseIntyg, statuses, applicationOrigin, texts, utkastStatus);

            return new PdfResponse(PdfGenerator.generatePdf(fkPdfDefinition),
                PdfGenerator.generatePdfFilename(LocalDateTime.now(), CERTIFICATE_FILE_PREFIX));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate!", e);
        }
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus)
        throws ModuleException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(LuseUtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande, moduleService);
    }

    @Override
    protected LuseUtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(LuseUtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande, moduleService);
    }

    @Override
    protected LuseUtlatandeV1 decorateDiagnoserWithDescriptions(LuseUtlatandeV1 utlatande) {
        List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
            .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
            .collect(Collectors.toList());
        return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
    }

    @Override
    protected LuseUtlatandeV1 decorateUtkastWithComment(LuseUtlatandeV1 utlatande, String comment) {
        return utlatande.toBuilder()
            .setOvrigt(concatOvrigtFalt(utlatande.getOvrigt(), comment))
            .build();
    }

    @Override
    protected LuseUtlatandeV1 decorateWithSignature(LuseUtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        try {
            ImmutableList<Diagnos> diagnoser = transportToInternal(intyg).getDiagnoser();
            if (!diagnoser.isEmpty()) {
                return diagnoser.get(0).getDiagnosBeskrivning();
            } else {
                return null;
            }
        } catch (ConverterException e) {
            throw new ModuleException("Could convert Intyg to Utlatande and as a result could not get additional info", e);
        }
    }

    @Override
    public Optional<AdditionalMetaData> getAdditionalMetaData(Intyg certificate) throws ModuleException {
        final var additionalMetaData = new AdditionalMetaData();

        final var luseCertificate = convertToInternal(certificate);
        final var diagnoses = getDiagnoses(luseCertificate.getDiagnoser());

        additionalMetaData.setDiagnoses(diagnoses);

        return Optional.of(additionalMetaData);
    }

    private LuseUtlatandeV1 convertToInternal(Intyg certificate) throws ModuleException {
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
        return internalToCertificate.convert(internalCertificate, certificateTextProvider);
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var updateInternalCertificate = certificateToInternal.convert(certificate, internalCertificate);
        return toInternalModelResponse(updateInternalCertificate);
    }

    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        final var dynamicKeys = getDynamicKeyMap();
        return DefaultCertificateMessagesProvider.create(validationMessages, dynamicKeys);
    }

    @Override
    public String getJsonFromUtlatande(Utlatande utlatande) throws ModuleException {
        if (utlatande instanceof LuseUtlatandeV1) {
            return toInternalModelResponse((LuseUtlatandeV1) utlatande);
        }
        final var message = utlatande == null ? "null" : utlatande.getClass().toString();
        throw new IllegalArgumentException(
            "Utlatande was not instance of class LuseUtlatandeV1, utlatande was instance of class: " + message);
    }

    @Override
    public String getUpdatedJsonWithTestData(String model, FillType fillType, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var certificate = getCertificateFromJson(model, typeAheadProvider);
        TestabilityToolkit.fillCertificateWithTestData(certificate, fillType, new LuseTestabilityCertificateTestdataProvider());
        return getJsonFromCertificate(certificate, model);
    }

    @Override
    public String getAdditionalInfoLabel() {
        return ADDITIONAL_INFO_LABEL;
    }

    private Map<String, String> getDynamicKeyMap() {
        final var provider = getTextProvider(LuseEntryPoint.MODULE_ID);

        return Map.of(GRUNDFORMU_UNDERSOKNING_LABEL, provider.get(GRUNDFORMU_UNDERSOKNING_LABEL),
            GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL, provider.get(GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL),
            GRUNDFORMU_JOURNALUPPGIFTER_LABEL, provider.get(GRUNDFORMU_JOURNALUPPGIFTER_LABEL),
            GRUNDFORMU_ANNAT_LABEL, provider.get(GRUNDFORMU_ANNAT_LABEL));
    }
}
