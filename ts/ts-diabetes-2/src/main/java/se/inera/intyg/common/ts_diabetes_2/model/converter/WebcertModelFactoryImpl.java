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
package se.inera.intyg.common.ts_diabetes_2.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.ts_diabetes_2.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes_2.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes_2.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes_2.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes_2.model.internal.TsDiabetes2Utlatande;
import se.inera.intyg.common.ts_diabetes_2.model.internal.TsDiabetes2Utlatande.Builder;
import se.inera.intyg.common.ts_diabetes_2.support.TsDiabetes2EntryPoint;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

import java.util.EnumSet;

/**
 * Factory for creating an editable model.
 */
public class WebcertModelFactoryImpl implements WebcertModelFactory<TsDiabetes2Utlatande> {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new ts-diabetes-2 draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @return {@link TsDiabetes2Utlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    @Override
    public TsDiabetes2Utlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        Builder template = TsDiabetes2Utlatande.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        resetDataInGrundData(grundData);
        template.setSignature(null);
        //initialize körkortsval to empty sets
        template.setIntygAvser(IntygAvser.create(EnumSet.noneOf(IntygAvserKategori.class)));
        template.setBedomning(Bedomning.create(EnumSet.noneOf(BedomningKorkortstyp.class), null, null));


        // Default to latest version available of intyg
        template.setTextVersion(intygTexts.getLatestVersion(TsDiabetes2EntryPoint.MODULE_ID));

        return template.setGrundData(grundData).build();
    }

    @Override
    public TsDiabetes2Utlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!TsDiabetes2Utlatande.class.isInstance(template)) {
            throw new ConverterException("Template is not of type TsDiabetes2Utlatande");
        }

        TsDiabetes2Utlatande tsDiabetes2Utlatande = (TsDiabetes2Utlatande) template;

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), tsDiabetes2Utlatande.getId());

        TsDiabetes2Utlatande.Builder templateBuilder = tsDiabetes2Utlatande.toBuilder();
        GrundData grundData = tsDiabetes2Utlatande.getGrundData();

        populateWithId(templateBuilder, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

        resetDataInGrundData(grundData);
        templateBuilder.setSignature(null);
        return templateBuilder.build();
    }

    private void populateWithId(Builder utlatande, String utlatandeId) throws ConverterException {
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
