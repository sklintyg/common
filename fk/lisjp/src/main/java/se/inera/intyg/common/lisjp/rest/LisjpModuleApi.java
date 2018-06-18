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
package se.inera.intyg.common.lisjp.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.rest.FkParentModuleApi;
import se.inera.intyg.common.lisjp.model.converter.InternalToTransport;
import se.inera.intyg.common.lisjp.model.converter.TransportToInternal;
import se.inera.intyg.common.lisjp.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.pdf.AbstractLisjpPdfDefinitionBuilder;
import se.inera.intyg.common.lisjp.pdf.DefaultLisjpPdfDefinitionBuilder;
import se.inera.intyg.common.lisjp.pdf.EmployeeLisjpPdfDefinitionBuilder;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
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
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LisjpModuleApi extends FkParentModuleApi<LisjpUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(LisjpModuleApi.class);

    private static final String CERTIFICATE_FILE_PREFIX = "lakarintyg_sjukpenning";
    private static final String MINIMAL_CERTIFICATE_FILE_PREFIX = "anpassat_lakarintyg_sjukpenning";

    public LisjpModuleApi() {
        super(LisjpUtlatande.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
            throws ModuleException {
        return generatePdf(new DefaultLisjpPdfDefinitionBuilder(), statuses, internalModel, applicationOrigin, CERTIFICATE_FILE_PREFIX,
                utkastStatus);
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {
        final EmployeeLisjpPdfDefinitionBuilder builder = new EmployeeLisjpPdfDefinitionBuilder(optionalFields);
        String fileNamePrefix = getEmployerCopyFilePrefix(builder, applicationOrigin);
        return generatePdf(builder, statuses, internalModel, applicationOrigin,
                fileNamePrefix, utkastStatus);
    }

    @Override
    protected String getSchematronFileName() {
        return LisjpEntryPoint.SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(LisjpUtlatande utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected LisjpUtlatande transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(LisjpUtlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected LisjpUtlatande decorateDiagnoserWithDescriptions(LisjpUtlatande utlatande) {
        List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
                .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                        moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
                .collect(Collectors.toList());
        return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
    }

    @Override
    protected LisjpUtlatande decorateUtkastWithComment(LisjpUtlatande utlatande, String comment) {

        return utlatande.toBuilder()
                .setOvrigt(concatOvrigtFalt(utlatande.getOvrigt(), comment))
                .build();
    }

    @Override
    protected LisjpUtlatande decorateWithSignature(LisjpUtlatande utlatande, String base64EncodedSignatureXml) {
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
            if (!LisjpUtlatande.class.isInstance(template)) {
                LOG.error("Could not create a new internal Webcert model using template of wrong type");
                throw new ModuleConverterException("Could not create a new internal Webcert model using template of wrong type");
            }

            LisjpUtlatande internal = (LisjpUtlatande) template;

            // Null out applicable fields
            LisjpUtlatande renewCopy = internal.toBuilder()
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

    private PdfResponse generatePdf(AbstractLisjpPdfDefinitionBuilder builder, List<Status> statuses, String internalModel,
            ApplicationOrigin applicationOrigin, String filePrefix, UtkastStatus utkastStatus) throws ModuleException {
        try {
            LisjpUtlatande luseIntyg = getInternal(internalModel);
            IntygTexts texts = getTexts(LisjpEntryPoint.MODULE_ID, luseIntyg.getTextVersion());

            final FkPdfDefinition fkPdfDefinition = builder.buildPdfDefinition(luseIntyg, statuses, applicationOrigin, texts, utkastStatus);

            Personnummer personId = luseIntyg.getGrundData().getPatient().getPersonId();
            return new PdfResponse(PdfGenerator.generatePdf(fkPdfDefinition),
                    PdfGenerator.generatePdfFilename(personId, filePrefix));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate!", e);
        }
    }

    private String getEmployerCopyFilePrefix(EmployeeLisjpPdfDefinitionBuilder builder, ApplicationOrigin applicationOrigin) {
        if (applicationOrigin.equals(ApplicationOrigin.MINA_INTYG)) {
            if (!builder.hasDeselectedOptionalFields()) {
                return CERTIFICATE_FILE_PREFIX;
            } else {
                return MINIMAL_CERTIFICATE_FILE_PREFIX;
            }
        } else {
            return MINIMAL_CERTIFICATE_FILE_PREFIX;
        }
    }

}
