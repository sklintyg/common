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
package se.inera.intyg.common.luae_na.rest;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.rest.FkParentModuleApi;
import se.inera.intyg.common.luae_na.model.converter.InternalToTransport;
import se.inera.intyg.common.luae_na.model.converter.TransportToInternal;
import se.inera.intyg.common.luae_na.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.luae_na.model.internal.LuaenaUtlatande;
import se.inera.intyg.common.luae_na.pdf.LuaenaPdfDefinitionBuilder;
import se.inera.intyg.common.luae_na.support.LuaenaEntryPoint;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

import java.util.List;
import java.util.stream.Collectors;

public class LuaenaModuleApi extends FkParentModuleApi<LuaenaUtlatande> {
    private static final Logger LOG = LoggerFactory.getLogger(LuaenaModuleApi.class);

    private static final String CERTIFICATE_FILE_PREFIX = "lakarutlatande_aktivitetsersattning";

    public LuaenaModuleApi() {
        super(LuaenaUtlatande.class);
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, boolean isUtkast)
            throws ModuleException {
        try {
            LuaenaUtlatande luaenaIntyg = getInternal(internalModel);
            LuaenaPdfDefinitionBuilder builder = new LuaenaPdfDefinitionBuilder();
            IntygTexts texts = getTexts(LuaenaEntryPoint.MODULE_ID, luaenaIntyg.getTextVersion());

            final FkPdfDefinition fkPdfDefinition = builder.buildPdfDefinition(luaenaIntyg, statuses, applicationOrigin, texts, isUtkast);
            Personnummer personId = luaenaIntyg.getGrundData().getPatient().getPersonId();
            return new PdfResponse(PdfGenerator.generatePdf(fkPdfDefinition),
                    PdfGenerator.generatePdfFilename(personId, CERTIFICATE_FILE_PREFIX));
        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate!", e);
        }
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields, boolean isUtkast)
            throws ModuleException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected String getSchematronFileName() {
        return LuaenaEntryPoint.SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(LuaenaUtlatande utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected LuaenaUtlatande transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(LuaenaUtlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected LuaenaUtlatande decorateDiagnoserWithDescriptions(LuaenaUtlatande utlatande) {
        List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
                .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                        moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
                .collect(Collectors.toList());
        return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
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
}
