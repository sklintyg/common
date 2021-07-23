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
package se.inera.intyg.common.af00213.v1.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.af00213.pdf.PdfGenerator;
import se.inera.intyg.common.af00213.support.Af00213EntryPoint;
import se.inera.intyg.common.af00213.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.af00213.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.af00213.v1.model.converter.InternalToTransport;
import se.inera.intyg.common.af00213.v1.model.converter.TransportToInternal;
import se.inera.intyg.common.af00213.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.af_parent.rest.AfParentModuleApi;
import se.inera.intyg.common.services.texts.DefaultCertificateTextProvider;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.facade.model.Certificate;
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
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Component(value = "moduleapi.af00213.v1")
public class Af00213ModuleApiV1 extends AfParentModuleApi<Af00213UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(Af00213ModuleApiV1.class);
    public static final String SCHEMATRON_FILE = "af00213.v1.sch";

    public Af00213ModuleApiV1() {
        super(Af00213UtlatandeV1.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException {
        Af00213UtlatandeV1 af00213Intyg = getInternal(internalModel);
        IntygTexts texts = getTexts(Af00213EntryPoint.MODULE_ID, af00213Intyg.getTextVersion());

        Personnummer personId = af00213Intyg.getGrundData().getPatient().getPersonId();
        return new PdfGenerator().generatePdf(af00213Intyg.getId(), internalModel, getMajorVersion(af00213Intyg.getTextVersion()), personId,
            texts, statuses, applicationOrigin,
            utkastStatus);
    }

    private String getMajorVersion(String textVersion) {
        return textVersion.split("\\.", 0)[0];
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
        List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {
        return null;
    }

    @Override
    protected String getSchematronFileName() {
        return SCHEMATRON_FILE;
    }

    @Override
    protected RegisterCertificateType internalToTransport(Af00213UtlatandeV1 utlatande) throws ConverterException {
        return InternalToTransport.convert(utlatande);
    }

    @Override
    protected Af00213UtlatandeV1 transportToInternal(Intyg intyg) throws ConverterException {
        return TransportToInternal.convert(intyg);
    }

    @Override
    protected Intyg utlatandeToIntyg(Af00213UtlatandeV1 utlatande) throws ConverterException {
        return UtlatandeToIntyg.convert(utlatande);
    }

    @Override
    protected Af00213UtlatandeV1 decorateWithSignature(Af00213UtlatandeV1 utlatande, String base64EncodedSignatureXml) {
        return utlatande.toBuilder().setSignature(base64EncodedSignatureXml).build();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        return null;
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template)
        throws ModuleException {
        try {
            if (!Af00213UtlatandeV1.class.isInstance(template)) {
                LOG.error("Could not create a new internal Webcert model using template of wrong type");
                throw new ModuleConverterException("Could not create a new internal Webcert model using template of wrong type");
            }

            Af00213UtlatandeV1 internal = (Af00213UtlatandeV1) template;

            // Null out applicable fields
            Af00213UtlatandeV1 renewCopy = internal.toBuilder()
                .build();

            Relation relation = draftCopyHolder.getRelation();
            draftCopyHolder.setRelation(relation);

            return toInternalModelResponse(webcertModelFactory.createCopy(draftCopyHolder, renewCopy));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public Certificate getCertificateFromJson(String certificateAsJson) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var textProvider = getTextProvider(internalCertificate.getTyp(), internalCertificate.getTextVersion());
        return InternalToCertificate.convert(internalCertificate, textProvider);
    }

    @Override
    public String getJsonFromCertificate(Certificate certificate, String certificateAsJson) throws ModuleException {
        final var internalCertificate = getInternal(certificateAsJson);
        final var updateInternalCertificate = CertificateToInternal.convert(certificate, internalCertificate);
        return toInternalModelResponse(updateInternalCertificate);
    }
}
