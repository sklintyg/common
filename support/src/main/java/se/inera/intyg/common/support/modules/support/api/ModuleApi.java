/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import se.inera.intyg.common.support.facade.model.CertificateText;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.mapper.Mapper;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.AdditionalMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftCreationResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.ModuleFacadeApi;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

/**
 * The module API defines methods that interact with one of the tree models that every module handles:
 * <ul>
 * <li>Transport model (XML-model that can be used to transmit an intyg between different parties)
 * <li>Internal model (JSON-model used to visualize an intyg in Mina Intyg and Webcert)
 * </ul>
 * <p>
 * There exists methods for converting between models, generate PDFs, interact with the internal model and extract meta
 * data.
 */
public interface ModuleApi extends ModuleFacadeApi {

    /**
     * Validates the internal model. The status (complete, incomplete) and a list of validation errors is returned.
     *
     * @param internalModel internal model to validate.
     * @return response the validation result.
     */
    ValidateDraftResponse validateDraft(String internalModel) throws ModuleException;

    /**
     * Generates a PDF from an internal model.
     *
     * @param internalModel the internal model to generate a PDF from
     * @param statuses a list of {@link Status} which signifies what has happened to the certificate
     * @param applicationOrigin the context from which this method was called (i.e Webcert or Mina intyg)
     * @param utkastStatus Status indicating whether the certificate we're printing is a draft or not. Triggers the "UTKAST" or
     * "UTKAST_LOCKED" watermark.
     * @return a {@link PdfResponse} consisting of a binary stream containing a PDF data and a suitable filename
     * @throws ModuleException if the PDF could not be generated
     */
    PdfResponse pdf(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, UtkastStatus utkastStatus)
        throws ModuleException;

    /**
     * Generates a PDF suited for the employer from the internal model.
     * <p>
     * A list of optional fields is required which specifies which of the optional fields in the PDF should be filled out.
     * The format, meaning and syntax of the optionalFields id's is determined within each implementing
     * module project.
     *
     * @param internalModel the internal model to generate a PDF from
     * @param applicationOrigin the context from which this method was called (i.e Webcert or Mina intyg)
     * @param optionalFields the optional field references to include in the PDF
     * @param utkastStatus Status indicating whether the certificate we're printing is a draft or not. Triggers the "UTKAST" or
     * "UTKAST_LOCKED" watermark.
     * @return A {@link PdfResponse} consisting of a binary stream containing a PDF data and a suitable filename.
     * @throws ModuleException if the PDF could not be generated
     */
    PdfResponse pdfEmployer(String internalModel, List<Status> statuses, ApplicationOrigin applicationOrigin, List<String> optionalFields,
        UtkastStatus utkastStatus)
        throws ModuleException;

    /**
     * Creates a new internal model.
     * <p>
     * The model is prepopulated using data contained in the {@link CreateNewDraftHolder} parameter.
     *
     * @param draftCertificateHolder The id of the new internal model, the {@link HoSPersonal}
     * and {@link se.inera.intyg.common.support.model.common.internal.Patient} data.
     * @return the new instance of the internal model mapped to a String
     * @throws ModuleException if the new model could not be created, typically because the conversion to String failed
     */
    String createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException;

    /**
     * Creates a new internal model from a template.
     * <p>
     * The model is prepopulated using data contained in the {@link CreateNewDraftHolder} parameter and template.
     *
     * @param draftCopyHolder the id of the new internal model, the {@link HoSPersonal}
     * and optional {@link se.inera.intyg.common.support.model.common.internal.Patient} data.
     * @param template the Utlatande to be used as a template for the new internal model
     * @return the new instance of the internal mapped to a String
     * @throws ModuleException if the new model could not be created, typically because the conversion to String failed
     */
    String createNewInternalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template) throws ModuleException;

    /**
     * Creates a new internal model from a template as a completion.
     * <p>
     * The model is prepopulated using data contained in the {@link CreateNewDraftHolder} parameter, template and the
     * comment if any.
     * </p>
     *
     * @param draftCopyHolder the id of the new internal model, the {@link HoSPersonal}
     * and optional {@link se.inera.intyg.common.support.model.common.internal.Patient} data.
     * @param template the Utlatande to be used as a template for the new internal model.
     * @param comment optional comment of why no new medicinal information can be entered into new Utlatande.
     */
    default String createCompletionFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template, String comment)
        throws ModuleException {
        throw new UnsupportedOperationException();
    }

    /**
     * Register certificate in Intygstjänsten.
     *
     * @param internalModel the internal model of the certificate to send
     * @param logicalAddress the logical address of receiving system, i.e Intygstjänsten
     * @throws ModuleException if the certificate could not be sent to recipient
     */
    void registerCertificate(String internalModel, String logicalAddress) throws ModuleException;

    /**
     * Send certificate to specified recipient.
     *
     * @param xmlBody Xml representation of the certificate to send.
     * @param logicalAddress The recipient's logical address
     * @param recipientId The recipient's identifier
     * @throws ModuleException if the certificate could not be sent to recipient
     */
    void sendCertificateToRecipient(String xmlBody, String logicalAddress, String recipientId)
        throws ModuleException;

    /**
     * Sends the revoke request to Intygstjänsten.
     *
     * @param xmlBody the request
     * @param logicalAddress the logical address of receiving system, i.e Intygstjansten
     * @throws ModuleException if the receiving system could not handle the revoke request
     */
    void revokeCertificate(String xmlBody, String logicalAddress) throws ModuleException;

    /**
     * Fetch a certificate from Intygstjansten.
     *
     * @param certificateId the certificate id
     * @param logicalAddress the logical address of system from the certificate is requested, i.e Intygstjansten
     * @param recipientId the recipient id for the requester, used to determine which statuses which should be returned
     * @return internal model of the certificate
     * @throws ModuleException if the producer did not respond or responded with ERROR
     */
    CertificateResponse getCertificate(String certificateId, String logicalAddress, String recipientId) throws ModuleException;

    /**
     * Determine whether a notification about changed state in a certificate should be sent,
     * using current and persisted model states.
     *
     * @param persistedState the state which is saved in Webcert currently
     * @param currentState the state which frontend sent back
     * @return true a notification should be sent, false otherwise
     * @throws ModuleException if one of the models could not be converted, typically the currentState
     */
    boolean shouldNotify(String persistedState, String currentState) throws ModuleException;

    /**
     * Returns an updated version of the internal model for saving, with new HoS person information.
     *
     * @param internalModel the internal model to use as a base
     * @param hosPerson the HoS person to complement the model with
     * @return the new internal model updated with the hosPerson info
     * @throws ModuleException if the mapping of the internal model to String failed
     */
    String updateBeforeSave(String internalModel, HoSPersonal hosPerson) throws ModuleException;

    /**
     * Returns an updated version of the internal model for saving, with new patient information applied.
     *
     * @param internalModel the internal model to use as a base
     * @param patient the patient complement the model with
     * @return the new internal model updated with the Patient info
     * @throws ModuleException if the mapping of the internal model to String failed
     */
    String updateBeforeSave(String internalModel, Patient patient) throws ModuleException;

    /**
     * Returns an updated version of the internal model for signing, with new HoS person information.
     *
     * @param internalModel the internal model to use as a base
     * @param hosPerson the HoS person to complement the model with
     * @param signingDate the timestamp of the signing of the intyg
     * @return A new internal model updated with the hosPerson info
     * @throws ModuleException if the mapping of the internal model to String failed
     */
    String updateBeforeSigning(String internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException;

    /**
     * Returns an updated version of the internal model for viewing, with new patient information applied.
     *
     * @param internalModel the internal model to use as a base
     * @param patient the patient complement the model with
     * @return the new internal model updated with the Patient info
     * @throws ModuleException if the mapping of the internal model to String failed
     */
    String updateBeforeViewing(String internalModel, Patient patient) throws ModuleException;

    /**
     * Create a revoke request using the Utlatande and the HoSPersonal.
     *
     * @param utlatande the information regarding the certificate
     * @param skapatAv the person who revoked
     * @param meddelande voluntary message of why the certificate was revoked
     * @return the XML request as a String
     * @throws ModuleException if the creation of the request from utlatande failed
     */
    String createRevokeRequest(Utlatande utlatande, HoSPersonal skapatAv, String meddelande) throws ModuleException;

    /**
     * Returns an instance of the implementation of Utlatande that this module handles.
     *
     * @param utlatandeJson the model represented as JSON (internal)
     * @return the converted utlatande
     * @throws ModuleException if the mapper could not read the Utlatande
     */
    Utlatande getUtlatandeFromJson(String utlatandeJson) throws ModuleException, IOException;

    /**
     * Returns an instance of the implementation of Utlatande that this module handles.
     *
     * @param xml the model represented as XML (transport)
     * @return the model represented as JSON (internal)
     * @throws ModuleException if there was an error in the conversion
     */
    Utlatande getUtlatandeFromXml(String xml) throws ModuleException;

    /**
     * Converts the XML in inputXml to be of correct version.
     * <p>
     * Statistiktjänsten handles RegisterCertificate of version 3. All certificates which are communicated (FK7263) in other
     * ways
     * need to be converted to RegisterCertificate version 3.0 before they are sent to Statistiktjänsten.
     * <p>
     * If the XML already is in correct version then nothing should be done and inputXml can be returned.
     *
     * @param inputXml the transport model to be converted
     * @return the XML representation as RegisterCertificate 3.0
     * @throws ModuleException if the conversion threw an exception
     */
    String transformToStatisticsService(String inputXml) throws ModuleException;

    /**
     * Perform module specific xml validation.
     *
     * @param inputXml the XML to be validated
     * @return the result of the validation
     * @throws ModuleException if the validator failed
     */
    ValidateXmlResponse validateXml(String inputXml) throws ModuleException;

    /**
     * Get Arende parameters specific to module such as parameters belonging to a certain frage id.
     *
     * @param utlatande the internal model, only used to determine certificate type
     * @param frageIds the ids of the questions in transport format requested
     * @return a mapping from frage id to a list of property handles in the JSON-representation of the certificate
     * @throws ModuleException if the version module api could not be found
     */
    Map<String, List<String>> getModuleSpecificArendeParameters(Utlatande utlatande, List<String> frageIds) throws ModuleException;

    /**
     * Creates a new internal representation, which is meant to be used as a renewal of a certificate based on template.
     * <p>
     * This should clear some fields specific to the certificate type which should not be transferred to the renewal copy.
     * <p>
     * NOTE: This can and will change fields in template before writing it to a new String.
     * Do *NOT* use the template after calling this method.
     *
     * @param draftCopyHolder the meta information about the new certificate
     * @param template the template certificate which the new renewal should be based upon
     * @return the new renewal certificate mapped to a String
     * @throws ModuleException if the conversion threw an exception
     */
    String createRenewalFromTemplate(CreateDraftCopyHolder draftCopyHolder, Utlatande template) throws ModuleException;

    /**
     * Converts the internal representation of the certificate to transport format.
     *
     * @param utlatande the internal certificate to be converted
     * @return the transport representation of the certificate
     * @throws ModuleException if the conversion threw an exception
     */
    Intyg getIntygFromUtlatande(Utlatande utlatande) throws ModuleException;

    /**
     * Generates the additional info which should be shown in Mina intyg.
     *
     * @param intyg the certificate to generate the info about
     * @return the information as a String
     * @throws ModuleException if the certificate could not be converted to internal
     */
    String getAdditionalInfo(Intyg intyg) throws ModuleException;

    /**
     * Generates the label for the additional info which should be shown in Mina intyg.
     *
     * @return the information as a String
     */
    String getAdditionalInfoLabel();

    /**
     * Generates the preamble text for citizens which should be shown in Mina intyg.
     *
     * @return the information as a String
     */
    CertificateText getPreambleForCitizens();

    /**
     * Injects the XML digital signature into the json model encoded into Base64.
     *
     * @param jsonModel the certificate as JSON.
     * @param signatureXml Plain-text XML signature.
     * @return The updated JSON model.
     */
    String updateAfterSigning(String jsonModel, String signatureXml) throws ModuleException;

    /**
     * Each module needs to specify in which order patient details should be resolved. <br/>
     * Defaults to FK presets (with no predessorId, <1, ResolveOrder.PU_NAME_ONLY> and <2, ResolveOrder.PARAMS_NAME_ONLY>).
     */
    default PatientDetailResolveOrder getPatientDetailResolveOrder() {
        return PatientDetailResolveOrder.defaultOrder();
    }

    /**
     * Returns critera for an intyg's type, major version and age (time since signed) a source candidate intyg
     * must match in order to be a copy-from-candicate when creating a new draft with
     * {@link se.riv.clinicalprocess.healthcond.certificate.createdraftcertificateresponder.v3.CreateDraftCertificateResponderInterface}.
     *
     * Since most types does not supports this, the defaults is none.
     */
    default Optional<GetCopyFromCriteria> getCopyFromCriteria() {
        return Optional.empty();
    }

    /**
     * Returns a mapping implementation between this module certificate type and another module certificate type.
     * E.g. mapping between FK7804 and AG7804.
     *
     * @return a mapping between certificate types
     */
    default Optional<Mapper> getMapper() {
        return Optional.empty();
    }

    /**
     * Validates if creation of a new draft is permitted.
     *
     * @return Optional validation result or empty
     */
    default Optional<ValidateDraftCreationResponse> validateDraftCreation(Set<String> existingRelatedCertificates) {
        return Optional.empty();
    }

    /**
     * Returns additional metadata that can be stored outside the xml-representation of the certificate. By default no additional
     * metadata is provided.
     *
     * @param certificate the certificate to get additional information about
     * @return Optional additional metadata or empty
     * @throws ModuleException if the certificate could not be converted to internal
     */
    default Optional<AdditionalMetaData> getAdditionalMetaData(Intyg certificate) throws ModuleException {
        return Optional.empty();
    }
}
