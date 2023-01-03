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
package se.inera.intyg.common.af00251.v1.model.converter;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.af00251.support.AF00251EntryPoint;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

/**
 * Factory for creating an editable model.
 */
@Component("af00251.v1.WebcertModelFactoryImpl")
public class WebcertModelFactoryImpl implements WebcertModelFactory<AF00251UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new lisjp draft pre-populated with the attached data.
     *
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link AF00251UtlatandeV1} or throws a ConverterException if something unforeseen happens
     */
    @Override
    public AF00251UtlatandeV1 createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        AF00251UtlatandeV1.Builder template = AF00251UtlatandeV1.builder();
        GrundData grundData = new GrundData();

        template.setTextVersion(newDraftData.getIntygTypeVersion());
        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        resetDataInGrundData(grundData);
        template.setSignature(null);

        // Default to latest minor version available for major version of intygtype
        template.setTextVersion(
            intygTexts.getLatestVersionForSameMajorVersion(AF00251EntryPoint.MODULE_ID, newDraftData.getIntygTypeVersion()));

        return template.setGrundData(grundData).build();
    }

    @Override
    public AF00251UtlatandeV1 createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!AF00251UtlatandeV1.class.isInstance(template)) {
            throw new ConverterException("Template is not of type AF00251UtlatandeV1");
        }

        AF00251UtlatandeV1 utlatande = (AF00251UtlatandeV1) template;

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), utlatande.getId());

        AF00251UtlatandeV1.Builder templateBuilder = utlatande.toBuilder();
        GrundData grundData = utlatande.getGrundData();

        populateWithId(templateBuilder, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

        resetDataInGrundData(grundData);
        templateBuilder.setSignature(null);
        return templateBuilder.build();
    }

    private void populateWithId(AF00251UtlatandeV1.Builder utlatande, String utlatandeId) throws ConverterException {
        if (Strings.nullToEmpty(utlatandeId).trim().isEmpty()) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void resetDataInGrundData(GrundData grundData) {
        Patient patient = new Patient();
        patient.setPersonId(grundData.getPatient().getPersonId());
        grundData.setPatient(patient);

        grundData.setSigneringsdatum(null);
    }
}
