/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.modules.support.api;

import org.joda.time.LocalDateTime;

import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal;
import se.inera.intyg.common.support.modules.support.api.dto.InternalModelHolder;
import se.inera.intyg.common.support.modules.support.api.dto.InternalModelResponse;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.notification.NotificationMessage;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The module API defines methods that interact with one of the tree models that every module handles:
 * <ul>
 * <li>Transport model (XML-model that can be used to transmit an intyg between different parties)
 * <li>Internal model (JSON-model used to visualize an intyg in Mina Intyg and Webcert)
 * </ul>
 *
 * There exists methods for converting between models, generate PDFs, interact with the internal model and extract meta
 * data.
 */
public interface ModuleApi {

    /**
     * Register the container for this module.
     *
     * @param moduleContainer
     *            The module container.
     */
    void setModuleContainer(ModuleContainerApi moduleContainer);

    /**
     * Get the container for this module.
     *
     * @return the module container.
     */
    ModuleContainerApi getModuleContainer();

    /**
     * Validates the internal model. The status (complete, incomplete) and a list of validation errors is returned.
     *
     * @param internalModel
     *            The internal model to validate.
     *
     * @return response The validation result.
     */
    ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException;

    /**
     * Generates a PDF from the internal model.
     *
     * @param internalModel
     *            The internal model to generate a PDF from.
     * @param applicationOrigin
     *            The context from which this method was called (i.e Webcert or MinaIntyg)
     *
     * @return A {@link PdfResponse} consisting of a binary stream containing a PDF data and a suitable filename.
     */
    PdfResponse pdf(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException;

    /**
     * Generates a PDF suited for the employer from the internal model.
     *
     * @param internalModel
     *            The internal model to generate a PDF from.
     * @param applicationOrigin
     *            The context from which this method was called (i.e Webcert or MinaIntyg)
     *
     * @return A {@link PdfResponse} consisting of a binary stream containing a PDF data and a suitable filename.
     */
    PdfResponse pdfEmployer(InternalModelHolder internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin) throws ModuleException;

    /**
     * Creates a new internal model. The model is prepopulated using data contained in the {@link CreateNewDraftHolder}
     * parameter.
     *
     * @param draftCertificateHolder
     *            The id of the new internal model, the {@link HoSPersonal} and
     *            {@link se.inera.intyg.common.support.modules.support.api.dto.Patient} data.
     *
     * @return A new instance of the internal model.
     */
    InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException;

    /**
     * Creates a new internal model. The model is prepopulated using data contained in the {@link CreateNewDraftHolder}
     * parameter and {@link InternalModelHolder} template.
     *
     * @param draftCopyHolder
     *            The id of the new internal model, the {@link HoSPersonal} and optional
     *            {@link se.inera.intyg.common.support.modules.support.api.dto.Patient} data.
     * @param template
     *            An internal model used as a template for the new internal model.
     *
     * @return A new instance of the internal model.
     */
    InternalModelResponse createNewInternalFromTemplate(CreateDraftCopyHolder draftCopyHolder, InternalModelHolder template)
            throws ModuleException;

    /**
     * Register certificate in Intygstjänsten.
     *
     * @param internalModel
     *            The internal model of the certificate to send.
     * @param logicalAddress
     *            Logical address of receiving system, i.e Intygstjansten
     */
    void registerCertificate(InternalModelHolder internalModel, String logicalAddress) throws ModuleException;

    /**
     * Send certificate to specified recipient.
     *
     * INFO: This method is only here to fix JIRA issue
     * <a href="https://inera-certificate.atlassian.net/browse/WEBCERT-1442">WEBCERT-1442</a>
     *
     * @param internalModel
     *            The internal model of the certificate to send.
     * @param logicalAddress
     *            The recipient's logical address
     * @param recipientId
     *            The recipient's identifier
     */
    void sendCertificateToRecipient(InternalModelHolder internalModel, String logicalAddress, String recipientId) throws ModuleException;

    /**
     * Fetch a certificate from Intygstjansten.
     *
     * @param certificateId
     *            The certificate id.
     * @param logicalAddress
     *            Logical address of receiving system, i.e Intygstjansten
     * @return internal model of the certificate
     */
    CertificateResponse getCertificate(String certificateId, String logicalAddress) throws ModuleException;

    /**
     * Check whether there are changes between the current and persisted model states.
     *
     * @param persistedState
     * @param currentState
     * @return true if there are changes, false otherwise
     * @throws ModuleException
     */
    boolean isModelChanged(String persistedState, String currentState) throws ModuleException;

    /**
     * Returns an updated version of the internal model for saving, with new HoS person information.
     *
     * @param internalModel
     *            The internal model to use as a base.
     * @param hosPerson
     *            The HoS person to complement the model with.
     *
     * @return A new internal model updated with the hosPerson info.
     * @throws ModuleException
     */
    InternalModelResponse updateBeforeSave(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException;

    /**
     * Returns an updated version of the internal model for signing, with new HoS person information.
     *
     * @param internalModel
     *            The internal model to use as a base.
     * @param hosPerson
     *            The HoS person to complement the model with.
     * @param signingDate
     *            The timestamp of the signing of the intyg.
     *
     * @return A new internal model updated with the hosPerson info.
     */
    InternalModelResponse updateBeforeSigning(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate)
            throws ModuleException;

    /**
     * Create a notification message based on the supplied model.
     */
    Object createNotification(NotificationMessage notificationMessage) throws ModuleException;

    /**
     * Exposed for testing purposes.
     *
     * @return an XML string
     */
    String marshall(String jsonString) throws ModuleException;

    /** Returns an instance of the particular sub class of Utlatande that this module handles. */
    Utlatande getUtlatandeFromJson(String utlatandeJson) throws IOException;

    Utlatande getUtlatandeFromIntyg(Intyg intyg) throws ConverterException;

    String transformToStatisticsService(String inputXml) throws ModuleException;

    /** Perform module specific xml validation. */
    ValidateXmlResponse validateXml(String inputXml) throws ModuleException;

    /** Get Arende parameters specific to module such as parameters belonging to a certain frage id. */
    Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande);

    String decorateUtlatande(String utlatandeJson) throws ModuleException;
}
