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
package se.inera.intyg.common.sos_doi.se.inera.intyg.common.sos_doi.rest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

public class SosDoiModuleApi<T extends Utlatande> implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(SosDoiModuleApi.class);

    @Override
    public ValidateDraftResponse validateDraft(String internalModel) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String createNewInternalFromTemplate(CreateDraftCopyHolder draftCopyHolder, String template) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress, String recipientId) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldNotify(String persistedState, String currentState) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String updateBeforeSave(String internalModel, HoSPersonal hosPerson) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String updateBeforeSave(String internalModel, Patient patient) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String updateBeforeSigning(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Utlatande getUtlatandeFromXml(String xml) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String transformToStatisticsService(String inputXml) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande, List<String> frageIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, String internalModelHolder) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Intyg getIntygFromUtlatande(Utlatande utlatande) throws ModuleException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        throw new UnsupportedOperationException();
    }
}
