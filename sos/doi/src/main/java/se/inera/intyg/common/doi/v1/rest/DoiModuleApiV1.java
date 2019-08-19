/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PARAMS_OR_PU;
import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PREDECESSOR;
import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PU;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import se.inera.intyg.common.doi.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.doi.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.doi.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.doi.v1.pdf.DoiPdfGenerator;
import se.inera.intyg.common.doi.support.DoiModuleEntryPoint;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.pdf.SoSPdfGeneratorException;
import se.inera.intyg.common.sos_parent.rest.SosParentModuleApi;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.doi.v1")
public class DoiModuleApiV1 extends SosParentModuleApi<DoiUtlatandeV1> {

    public static final String SCHEMATRON_FILE = "doi.v1.sch";
    private static final Logger LOG = LoggerFactory.getLogger(DoiModuleApiV1.class);
    private static final String PDF_FILENAME_PREFIX = "dodsorsaksintyg";

    public DoiModuleApiV1() {
        super(DoiUtlatandeV1.class);
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
    public PatientDetailResolveOrder getPatientDetailResolveOrder() {
        List<ResolveOrder> adressStrat = Arrays.asList(PREDECESSOR, PU);
        List<ResolveOrder> avlidenStrat = Arrays.asList(PARAMS_OR_PU);
        List<ResolveOrder> otherStrat = Arrays.asList(PREDECESSOR, PU);

        return new PatientDetailResolveOrder("db", adressStrat, avlidenStrat, otherStrat);
    }

}
