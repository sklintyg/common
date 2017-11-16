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
package se.inera.intyg.common.doi.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.doi.model.converter.InternalToTransport;
import se.inera.intyg.common.doi.model.converter.TransportToInternal;
import se.inera.intyg.common.doi.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.doi.pdf.DoiPdfGenerator;
import se.inera.intyg.common.doi.support.DoiModuleEntryPoint;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.pdf.SoSPdfGeneratorException;
import se.inera.intyg.common.sos_parent.rest.SosParentModuleApi;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

import java.util.List;

public class DoiModuleApi extends SosParentModuleApi<DoiUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(DoiModuleApi.class);
    private static final String PDF_FILENAME_PREFIX = "dodsorsaksintyg";

    public DoiModuleApi() {
        super(DoiUtlatande.class);
    }

    @Override
    protected DoiUtlatande transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected RegisterCertificateType internalToTransport(DoiUtlatande utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected Intyg utlatandeToIntyg(DoiUtlatande utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected String getSchematronFileName() {
        return DoiModuleEntryPoint.SCHEMATRON_FILE;
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, boolean isUtkast)
            throws ModuleException {
        try {
            if (ApplicationOrigin.WEBCERT != applicationOrigin) {
                throw new IllegalArgumentException("Generating PDF not allowed for application origin " + applicationOrigin);
            }
            DoiUtlatande intyg = getInternal(internalModel);
            IntygTexts texts = getTexts(DoiModuleEntryPoint.MODULE_ID, intyg.getTextVersion());
            DoiPdfGenerator pdfGenerator = new DoiPdfGenerator(intyg, texts, statuses, isUtkast);
            return new PdfResponse(pdfGenerator.getBytes(),
                    pdfGenerator.generatePdfFilename(intyg.getGrundData().getPatient().getPersonId(), PDF_FILENAME_PREFIX));
        } catch (SoSPdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for " + DoiModuleEntryPoint.MODULE_ID + " certificate!", e);
        }
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields, boolean isUtkast) throws ModuleException {
        throw new RuntimeException("Not applicable for dodsorsaksintyg.");
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        // This is used for Mina intyg and since that is unsupported for DOI we return empty string
        return "";
    }
}
