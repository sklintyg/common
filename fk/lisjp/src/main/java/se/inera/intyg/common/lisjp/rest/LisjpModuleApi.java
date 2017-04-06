/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

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
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        return generatePdf(new DefaultLisjpPdfDefinitionBuilder(), statuses, internalModel, applicationOrigin, CERTIFICATE_FILE_PREFIX);
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields) throws ModuleException {
        final EmployeeLisjpPdfDefinitionBuilder builder = new EmployeeLisjpPdfDefinitionBuilder(optionalFields);
        String fileNamePrefix = getEmployerCopyFilePrefix(builder, applicationOrigin);
        return generatePdf(builder, statuses, internalModel, applicationOrigin,
                fileNamePrefix);
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

    private PdfResponse generatePdf(AbstractLisjpPdfDefinitionBuilder builder, List<Status> statuses, String internalModel,
            ApplicationOrigin applicationOrigin, String filePrefix) throws ModuleException {
        try {
            LisjpUtlatande luseIntyg = getInternal(internalModel);
            IntygTexts texts = getTexts(LisjpEntryPoint.MODULE_ID, luseIntyg.getTextVersion());

            final FkPdfDefinition fkPdfDefinition = builder.buildPdfDefinition(luseIntyg, statuses, applicationOrigin, texts);
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
