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
package se.inera.intyg.common.ag114.v1.model.converter;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
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
@Component(value = "ag114.WebcertModelFactoryImpl.v1")
public class WebcertModelFactoryImpl implements WebcertModelFactory<Ag114UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new ag114 draft pre-populated with the attached data.
     *
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link Ag114UtlatandeV1} or throws a ConverterException if something unforeseen happens
     */
    @Override
    public Ag114UtlatandeV1 createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        Ag114UtlatandeV1.Builder template = Ag114UtlatandeV1.builder();
        GrundData grundData = new GrundData();

        List<Sysselsattning> sysselsattning = Arrays.asList(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE));
        template.setSysselsattning(sysselsattning);

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        resetDataInGrundData(grundData);

        // Default to latest minor version available for major version of intygtype
        template.setTextVersion(
            intygTexts.getLatestVersionForSameMajorVersion(Ag114EntryPoint.MODULE_ID, newDraftData.getIntygTypeVersion()));

        return template.setGrundData(grundData).build();
    }

    @Override
    public Ag114UtlatandeV1 createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!Ag114UtlatandeV1.class.isInstance(template)) {
            throw new ConverterException("Template is not of type Ag114Utlatande");
        }

        Ag114UtlatandeV1 ag114UtlatandeV1 = (Ag114UtlatandeV1) template;
        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), ag114UtlatandeV1.getId());

        Ag114UtlatandeV1.Builder templateBuilder = ag114UtlatandeV1.toBuilder();
        GrundData grundData = ag114UtlatandeV1.getGrundData();

        populateWithId(templateBuilder, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

        resetDataInGrundData(grundData);
        templateBuilder.setSignature(null);
        return templateBuilder.build();
    }

    private void populateWithId(Ag114UtlatandeV1.Builder utlatande, String utlatandeId) throws ConverterException {
        if (utlatandeId == null || utlatandeId.trim().length() == 0) {
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
