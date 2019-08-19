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
package se.inera.intyg.common.ag7804.v1.model.converter;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.ag7804.support.Ag7804EntryPoint;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
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
@Component("ag7804.v1.WebcertModelFactoryImpl")
public class WebcertModelFactoryImpl implements WebcertModelFactory<Ag7804UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new Ag7804UtlatandeV1 draft pre-populated with the attached data.
     *
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link Ag7804UtlatandeV1} or throws a ConverterException if something unforeseen happens
     */
    @Override
    public Ag7804UtlatandeV1 createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        Ag7804UtlatandeV1.Builder template = Ag7804UtlatandeV1.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        resetDataInGrundData(grundData);
        template.setSignature(null);

        // Default to latest minor version available for major version of intygtype
        template.setTextVersion(
            intygTexts.getLatestVersionForSameMajorVersion(Ag7804EntryPoint.MODULE_ID, newDraftData.getIntygTypeVersion()));

        return template.setGrundData(grundData).build();
    }

    @Override
    public Ag7804UtlatandeV1 createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {

        if (Ag7804UtlatandeV1.class.isInstance(template)) {
            Ag7804UtlatandeV1 ag7804pUtlatandeV1 = (Ag7804UtlatandeV1) template;

            LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), ag7804pUtlatandeV1.getId());

            Ag7804UtlatandeV1.Builder templateBuilder = ag7804pUtlatandeV1.toBuilder();
            GrundData grundData = ag7804pUtlatandeV1.getGrundData();

            populateWithId(templateBuilder, copyData.getCertificateId());
            WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

            resetDataInGrundData(grundData);
            templateBuilder.setSignature(null);
            return templateBuilder.build();
        } else if (LisjpUtlatandeV1.class.isInstance(template)) {
            return handleFromLisjpCopy(copyData, (LisjpUtlatandeV1) template);
        } else {
            throw new ConverterException("Template is of an unsupported type : " + template.getClass());
        }

    }

    private Ag7804UtlatandeV1 handleFromLisjpCopy(CreateDraftCopyHolder copyData, LisjpUtlatandeV1 lisjpTemplate)
        throws ConverterException {
        LOG.trace("Creating copy with id {} from LisjpUtlatandeV1 with id {}", copyData.getCertificateId(), lisjpTemplate.getId());

        Ag7804UtlatandeV1.Builder templateBuilder = Ag7804UtlatandeV1.builder();

        populateWithId(templateBuilder, copyData.getCertificateId());

        // Retain patient and skapadAv
        GrundData grundData = new GrundData();
        grundData.setPatient(copyData.getPatient());
        grundData.setSkapadAv(copyData.getSkapadAv());
        grundData.setSigneringsdatum(null);
        grundData.setRelation(null);

        templateBuilder.setGrundData(grundData);
        templateBuilder.setTextVersion(copyData.getIntygTypeVersion());
        templateBuilder.setSignature(null);

        CopyFromUtlatandeHelper.copyFrom(lisjpTemplate, templateBuilder);
        return templateBuilder.build();
    }

    private void populateWithId(Ag7804UtlatandeV1.Builder utlatande, String utlatandeId) throws ConverterException {
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
