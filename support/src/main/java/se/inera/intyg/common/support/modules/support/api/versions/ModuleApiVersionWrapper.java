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
package se.inera.intyg.common.support.modules.support.api.versions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
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

/**
 * Created by marced on 2018-09-18.
 * This class acts as a call-through wrapper for access to the concrete ModuleApi implementations that now are
 * versioned, i.e each concrete ModuleAPi implementation only handles a specific version of a specifig intygtype.
 *
 * THe logic of choosing implementation is limited to this class, instead of having such code spread out in each
 * moduleAPi or in code using the ModuleAPi.
 */
public class ModuleApiVersionWrapper implements ModuleApi {
    private final String intygType;
    private final ModuleApiVersionResolver moduleApiVersionResolver;

    /**
     * Constructor for an instance of the wrapper for a specific intygType.
     * We also get a {@link ModuleApiVersionResolver} that will do actual resolving of versions and beans.
     * @param intygType
     * @param moduleApiVersionResolver
     */
    public ModuleApiVersionWrapper(String intygType, ModuleApiVersionResolver moduleApiVersionResolver) {
        this.intygType = intygType;
        this.moduleApiVersionResolver = moduleApiVersionResolver;
    }

    @Override
    public ValidateDraftResponse validateDraft(String internalModel) throws ModuleException {
        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(internalModel))
                .validateDraft(internalModel);
    }

    @Override
    public PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
            throws ModuleException {

        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(internalModel))
                .pdf(internalModel, statuses, applicationOrigin, utkastStatus);
    }

    @Override
    public PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin,
            List<String> optionalFields, UtkastStatus utkastStatus) throws ModuleException {

        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(internalModel))
                .pdfEmployer(internalModel, statuses, applicationOrigin, optionalFields, utkastStatus);
    }

    @Override
    public String createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, draftCertificateHolder.getIntygTypeVersion())
                .createNewInternal(draftCertificateHolder);
    }

    @Override
    public String createNewInternalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, template.getTextVersion())
                .createNewInternalFromTemplate(draftCopyHolder, template);
    }

    @Override
    public String createCompletionFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template, String comment)
            throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, template.getTextVersion())
                .createCompletionFromTemplate(draftCopyHolder, template, comment);
    }

    @Override
    public void registerCertificate(String internalModel, String logicalAddress) throws ModuleException {
        moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(internalModel))
                .registerCertificate(internalModel, logicalAddress);
    }

    @Override
    public void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId, String intygTypeVersion)
            throws ModuleException {
        moduleApiVersionResolver.getVersionedModuleApi(this.intygType, intygTypeVersion)
                .sendCertificateToRecipient(xmlBody, logicalAddress, recipientId, intygTypeVersion);
    }

    @Override
    public void revokeCertificate(String xmlBody, String logicalAddress, String intygTypeVersion) throws ModuleException {
        moduleApiVersionResolver.getVersionedModuleApi(this.intygType, intygTypeVersion)
                .revokeCertificate(xmlBody, logicalAddress, intygTypeVersion);
    }

    @Override
    public CertificateResponse getCertificate(String certificateId, String logicalAddress, String recipientId, String intygTypeVersion)
            throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, intygTypeVersion)
                .getCertificate(certificateId, logicalAddress, recipientId, intygTypeVersion);
    }

    @Override
    public boolean shouldNotify(String persistedState, String currentState) throws ModuleException {
        // Both arguments are Utlatande json strings (of same version)
        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(currentState))
                .shouldNotify(persistedState, currentState);
    }

    @Override
    public String updateBeforeSave(String internalModel, HoSPersonal hosPerson) throws ModuleException {
        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(internalModel))
                .updateBeforeSave(internalModel, hosPerson);
    }

    @Override
    public String updateBeforeSave(String internalModel, Patient patient) throws ModuleException {
        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(internalModel))
                .updateBeforeSave(internalModel, patient);
    }

    @Override
    public String updateBeforeSigning(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(internalModel))
                .updateBeforeSigning(internalModel, hosPerson, signingDate);
    }

    @Override
    public String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, utlatande.getTextVersion())
                .createRevokeRequest(utlatande, skapatAv, meddelande);
    }

    @Override
    public Utlatande getUtlatandeFromJson(String utlatandeJson) throws ModuleException, IOException {
        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(utlatandeJson))
                .getUtlatandeFromJson(utlatandeJson);
    }

    @Override
    public Utlatande getUtlatandeFromXml(String xml, String intygTypeVersion) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, intygTypeVersion)
                .getUtlatandeFromXml(xml, intygTypeVersion);
    }

    @Override
    public String transformToStatisticsService(String inputXml, String intygTypeVersion) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, intygTypeVersion)
                .transformToStatisticsService(inputXml, intygTypeVersion);
    }

    @Override
    public ValidateXmlResponse validateXml(String inputXml, String intygTypeVersion) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, intygTypeVersion)
                .validateXml(inputXml, intygTypeVersion);
    }

    @Override
    public Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande, List<String> frageIds) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, utlatande.getTextVersion())
                .getModuleSpecificArendeParameters(utlatande, frageIds);
    }

    @Override
    public String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, template.getTextVersion())
                .createRenewalFromTemplate(draftCopyHolder, template);
    }

    @Override
    public Intyg getIntygFromUtlatande(Utlatande utlatande) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, utlatande.getTextVersion())
                .getIntygFromUtlatande(utlatande);
    }

    @Override
    public String getAdditionalInfo(Intyg intyg) throws ModuleException {
        return moduleApiVersionResolver.getVersionedModuleApi(this.intygType, intyg.getVersion())
                .getAdditionalInfo(intyg);
    }

    @Override
    public String updateAfterSigning(String jsonModel, String signatureXml) throws ModuleException {
        return moduleApiVersionResolver
                .getVersionedModuleApi(this.intygType, moduleApiVersionResolver.resolveVersionFromUtlatandeJson(jsonModel))
                .updateAfterSigning(jsonModel, signatureXml);
    }
}
