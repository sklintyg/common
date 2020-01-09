/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.model.converter;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.tstrk1009.support.Tstrk1009EntryPoint;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;

/**
 * Factory for creating a editable model.
 */
@Component("tstrk1009.v1.WebcertModelFactoryImpl")
public class WebcertModelFactoryImpl implements WebcertModelFactory<Tstrk1009UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new TS-bas draft pre-populated with the attached data.
     *
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link Tstrk1009UtlatandeV1}
     * @throws se.inera.intyg.common.support.model.converter.util.ConverterException if something unforeseen happens
     */
    @Override
    public Tstrk1009UtlatandeV1 createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());
        Tstrk1009UtlatandeV1.Builder template = Tstrk1009UtlatandeV1.builder();
        GrundData grundData = new GrundData();

        template.setId(newDraftData.getCertificateId());
        // Default to latest minor version available for major version of intygtype
        template.setTextVersion(
            intygTexts.getLatestVersionForSameMajorVersion(Tstrk1009EntryPoint.MODULE_ID, newDraftData.getIntygTypeVersion()));

        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        template.setGrundData(grundData);

        return template.build();
    }

    @Override
    public Tstrk1009UtlatandeV1 createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!Tstrk1009UtlatandeV1.class.isInstance(template)) {
            throw new ConverterException("Template is not of type Tstrk1009UtlatandeV1");
        }
        Tstrk1009UtlatandeV1 tsBasUtlatande = (Tstrk1009UtlatandeV1) template;
        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), tsBasUtlatande.getId());

        Tstrk1009UtlatandeV1.Builder templateBuilder = tsBasUtlatande.toBuilder();

        populateWithId(templateBuilder, copyData.getCertificateId());
        GrundData grundData = tsBasUtlatande.getGrundData();
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);
        resetDataInCopy(grundData);
        templateBuilder.setSignature(null);
        return templateBuilder.build();
    }

    private void resetDataInCopy(GrundData grundData) {
        grundData.setSigneringsdatum(null);
    }

    private void populateWithId(Tstrk1009UtlatandeV1.Builder utlatande, String utlatandeId) throws ConverterException {
        if (Strings.isNullOrEmpty(utlatandeId)) {
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
