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
package se.inera.intyg.common.luae_na.v1.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;

import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.pdf.PdfGenerator;
import se.inera.intyg.common.fkparent.pdf.PdfGeneratorException;
import se.inera.intyg.common.fkparent.pdf.model.FkPdfDefinition;
import se.inera.intyg.common.fkparent.rest.FkParentModuleApi;
import se.inera.intyg.common.luae_na.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.luae_na.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.luae_na.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.luae_na.v1.pdf.LuaenaPdfDefinitionBuilder;
import se.inera.intyg.common.luae_na.support.LuaenaEntryPoint;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.luae_na.v1")
public class LuaenaModuleApiV1 extends FkParentModuleApi<LuaenaUtlatandeV1> {

    public static final String SCHEMATRON_FILE = "luae_na.v1.sch";
    private static final Logger LOG = LoggerFactory.getLogger(LuaenaModuleApiV1.class);

    private static final String CERTIFICATE_FILE_PREFIX = "lakarutlatande_aktivitetsersattning";

    public LuaenaModuleApiV1() {
        super(LuaenaUtlatandeV1.class);
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        try {
            LuaenaUtlatandeV1 luaenaIntyg = getInternal(internalModel);
            LuaenaPdfDefinitionBuilder builder = new LuaenaPdfDefinitionBuilder();
            IntygTexts texts = getTexts(LuaenaEntryPoint.MODULE_ID, luaenaIntyg.getTextVersion());

            final FkPdfDefinition fkPdfDefinition = builder.buildPdfDefinition(luaenaIntyg, statuses, applicationOrigin,
                texts, utkastStatus);

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
    protected RegisterCertificateType internalToTransport(LuaenaUtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande, moduleService);
    }

    @Override
    protected LuaenaUtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(LuaenaUtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande, moduleService);
    }

    @Override
    protected LuaenaUtlatandeV1 decorateDiagnoserWithDescriptions(LuaenaUtlatandeV1 utlatande) {
        List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
            .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
            .collect(Collectors.toList());
        return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
    }

    @Override
    protected LuaenaUtlatandeV1 decorateUtkastWithComment(LuaenaUtlatandeV1 utlatande, String comment) {
        return utlatande.toBuilder()
            .setOvrigt(concatOvrigtFalt(utlatande.getOvrigt(), comment))
            .build();
    }

    @Override
    protected LuaenaUtlatandeV1 decorateWithSignature(LuaenaUtlatandeV1 utlatande, String base64EncodedSignatureXml) {
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
}
