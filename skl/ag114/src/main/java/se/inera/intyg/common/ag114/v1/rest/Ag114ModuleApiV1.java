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
package se.inera.intyg.common.ag114.v1.rest;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.ag114.pdf.PdfGenerator;
import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
import se.inera.intyg.common.ag114.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.ag114.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.ag114.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.agparent.rest.AgParentModuleApi;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.ag114.v1")
public class Ag114ModuleApiV1 extends AgParentModuleApi<Ag114UtlatandeV1> {

    public static final String SCHEMATRON_FILE = "ag114.v1.sch";
    private static final Logger LOG = LoggerFactory.getLogger(Ag114ModuleApiV1.class);

    public Ag114ModuleApiV1() {
        super(Ag114UtlatandeV1.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        try {
            Ag114UtlatandeV1 utlatande = getInternal(internalModel);
            IntygTexts texts = getTexts(Ag114EntryPoint.MODULE_ID, utlatande.getTextVersion());
            Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
            return new PdfGenerator().generatePdf(utlatande.getId(), internalModel, getMajorVersion(utlatande.getTextVersion()), personId,
                texts, statuses,
                applicationOrigin, utkastStatus, null);
        } catch (Exception e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate (standard copy) PDF for certificate", e);
        }
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus)
        throws ModuleException {
        try {
            Ag114UtlatandeV1 utlatande = getInternal(internalModel);
            IntygTexts texts = getTexts(Ag114EntryPoint.MODULE_ID, utlatande.getTextVersion());
            Personnummer personId = utlatande.getGrundData().getPatient().getPersonId();
            return new PdfGenerator().generatePdf(utlatande.getId(), internalModel, getMajorVersion(utlatande.getTextVersion()), personId,
                texts, statuses,
                applicationOrigin, utkastStatus, optionalFields);
        } catch (Exception e) {
            LOG.error("Failed to generate pdfEmployer for certificate!", e);
            throw new ModuleSystemException("Failed to generate (pdfEmployer) PDF for certificate!", e);
        }
    }

    private String getMajorVersion(String textVersion) {
        return textVersion.split("\\.", 0)[0];
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        try {
            final InternalLocalDateInterval sjukskrivningsperiod = transportToInternal(intyg).getSjukskrivningsperiod();
            if (sjukskrivningsperiod != null) {
                return sjukskrivningsperiod.getFrom().toString() + " - " + sjukskrivningsperiod.getTom().toString();
            }
            return null;
        } catch (ConverterException e) {
            throw new ModuleException("Could not convert Intyg to Utlatande and as a result could not get additional info", e);
        }
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(Ag114UtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande, moduleService);
    }

    @Override
    protected Ag114UtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(Ag114UtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande, moduleService);
    }

    @Override
    protected Ag114UtlatandeV1 decorateWithSignature(Ag114UtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    protected Ag114UtlatandeV1 decorateDiagnoserWithDescriptions(Ag114UtlatandeV1 utlatande) {
        if (utlatande.getDiagnoser() != null && utlatande.getDiagnoser().size() > 0) {
            List<Diagnos> decoratedDiagnoser = utlatande.getDiagnoser().stream()
                .map(diagnos -> Diagnos.create(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem(), diagnos.getDiagnosBeskrivning(),
                    moduleService.getDescriptionFromDiagnosKod(diagnos.getDiagnosKod(), diagnos.getDiagnosKodSystem())))
                .collect(Collectors.toList());
            return utlatande.toBuilder().setDiagnoser(decoratedDiagnoser).build();
        } else {
            return utlatande;
        }
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCertificateHolder, Utlatande template) {
        throw new UnsupportedOperationException("AG1-14 does not support renewewal.");
    }

    @Override
    public String getUtlatandeToInternalModelResponse(Utlatande utlatande) {
        throw new UnsupportedOperationException();
    }
}
