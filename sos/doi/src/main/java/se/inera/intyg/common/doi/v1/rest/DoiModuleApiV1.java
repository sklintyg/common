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
package se.inera.intyg.common.doi.v1.rest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.db.support.DbModuleEntryPoint;
import se.inera.intyg.common.doi.support.DoiModuleEntryPoint;
import se.inera.intyg.common.doi.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.doi.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.doi.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.doi.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.doi.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.doi.v1.model.mapper.DbToDoiMapper;
import se.inera.intyg.common.doi.v1.pdf.DoiPdfGenerator;
import se.inera.intyg.common.doi.v1.testability.DoiTestabilityTestdataToolkit;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.messages.DefaultCertificateMessagesProvider;
import se.inera.intyg.common.services.messages.MessagesParser;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.pdf.SoSPdfGeneratorException;
import se.inera.intyg.common.sos_parent.rest.SosParentModuleApi;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.util.TestabilityToolkit;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.mapper.Mapper;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.GetCopyFromCriteria;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftCreationResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.common.support.modules.support.facade.FillType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;

@Component(value = "moduleapi.doi.v1")
public class DoiModuleApiV1 extends SosParentModuleApi<DoiUtlatandeV1> {

    @Autowired
    private InternalToCertificate internalToCertificate;

    @Autowired
    private CertificateToInternal certificateToInternal;

    public static final String SCHEMATRON_FILE = "doi.v1.sch";
    private static final Logger LOG = LoggerFactory.getLogger(DoiModuleApiV1.class);
    private static final String PDF_FILENAME_PREFIX = "dodsorsaksintyg";

    private static final String SUPPORTED_DB_MAJOR_VERSION = "1";

    private Map<String, String> validationMessages;

    public DoiModuleApiV1() {
        super(DoiUtlatandeV1.class);
        init();
    }

    private void init() {
        try {
            final var inputStream1 = new ClassPathResource("/META-INF/resources/webjars/common/webcert/messages.js").getInputStream();
            final var inputStream2
                = new ClassPathResource("/META-INF/resources/webjars/doi/webcert/views/messages.js").getInputStream();
            validationMessages = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        } catch (IOException exception) {
            LOG.error("Error during initialization. Could not read messages files");
            throw new RuntimeException("Error during initialization. Could not read messages files", exception);
        }
    }

    @Override
    protected DoiUtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected RegisterCertificateType internalToTransport(DoiUtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected Intyg utlatandeToIntyg(DoiUtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected DoiUtlatandeV1 decorateWithSignature(DoiUtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        try {
            if (ApplicationOrigin.WEBCERT != applicationOrigin) {
                throw new IllegalArgumentException("Generating PDF not allowed for application origin " + applicationOrigin);
            }
            DoiUtlatandeV1 intyg = getInternal(internalModel);
            IntygTexts texts = getTexts(DoiModuleEntryPoint.MODULE_ID, intyg.getTextVersion());
            DoiPdfGenerator pdfGenerator = new DoiPdfGenerator(intyg, texts, statuses, utkastStatus);
            return new PdfResponse(pdfGenerator.getBytes(),
                pdfGenerator.generatePdfFilename(LocalDateTime.now(), PDF_FILENAME_PREFIX));
        } catch (SoSPdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for " + DoiModuleEntryPoint.MODULE_ID + " certificate!", e);
        }
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {
        throw new RuntimeException("Not applicable for dodsorsaksintyg.");
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        // This is used for Mina intyg and since that is unsupported for DOI we return empty string
        return "";
    }

    @Override
    public Optional<GetCopyFromCriteria> getCopyFromCriteria() {
        return Optional.of(new GetCopyFromCriteria(DbModuleEntryPoint.MODULE_ID, SUPPORTED_DB_MAJOR_VERSION));
    }

    @Override
    public Optional<Mapper> getMapper() {
        return Optional.of(new DbToDoiMapper(objectMapper));
    }

    @Override
    public Optional<ValidateDraftCreationResponse> validateDraftCreation(Set<String> existingRelatedCertificates) {
        if (existingRelatedCertificates.stream().noneMatch(KvIntygstyp.DB.getCodeValue()::equalsIgnoreCase)) {
            return Optional.of(new ValidateDraftCreationResponse("Det finns inget dödsbevis i nuläget inom "
                + "vårdgivaren. Dödsorsaksintyget bör alltid skapas efter dödsbeviset.", ResultCodeType.INFO));
        }
        return Optional.empty();
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var certificateTextProvider = getTextProvider(internalCertificate.getTyp(), internalCertificate.getTextVersion());
        return internalToCertificate.convert(internalCertificate, certificateTextProvider, typeAheadProvider);
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var updateInternalCertificate = certificateToInternal.convert(certificate, internalCertificate);
        return toInternalModelResponse(updateInternalCertificate);
    }

    @Override
    public CertificateMessagesProvider getMessagesProvider() {
        return DefaultCertificateMessagesProvider.create(validationMessages);
    }

    @Override
    public String getJsonFromUtlatande(Utlatande utlatande) throws ModuleException {
        if (utlatande instanceof DoiUtlatandeV1) {
            return toInternalModelResponse(utlatande);
        }
        final var message = utlatande == null ? "null" : utlatande.getClass().toString();
        throw new IllegalArgumentException(
            "Utlatande was not instance of class DoiUtlatandeV1, utlatande was instance of class: " + message);
    }

    @Override
    public String getUpdatedJsonWithTestData(String model, FillType fillType, TypeAheadProvider typeAheadProvider) throws ModuleException {
        final var certificate = getCertificateFromJson(model, typeAheadProvider);
        TestabilityToolkit.fillCertificateWithTestData(certificate, fillType, new DoiTestabilityTestdataToolkit());
        return getJsonFromCertificate(certificate, model);
    }
}
