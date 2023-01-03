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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import com.google.common.base.Strings;
import java.util.EnumSet;
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
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;

@Component("ts-diabetes.v4.WebcertModelFactoryImpl")
public class WebcertModelFactoryImpl implements WebcertModelFactory<TsDiabetesUtlatandeV4> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new ts-diabetes V4 draft pre-populated with the attached data.
     *
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link TsDiabetesUtlatandeV4} or throws a ConverterException if something unforeseen happens
     */
    @Override
    public TsDiabetesUtlatandeV4 createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        final var template = TsDiabetesUtlatandeV4.builder();
        final var grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        resetDataInGrundData(grundData);
        template.setSignature(null);
        // initialize otherwise empty utlatande
        template.setIntygAvser(IntygAvser.create(EnumSet.noneOf(IntygAvserKategori.class)));
        template.setAllmant(Allmant.builder().build());
        template.setOvrigt(Ovrigt.builder().build());
        template.setBedomning(Bedomning.builder().setUppfyllerBehorighetskrav(EnumSet.noneOf(BedomningKorkortstyp.class)).build());

        // Default to latest minor version available for major version of intygtype
        template.setTextVersion(
            intygTexts.getLatestVersionForSameMajorVersion(TsDiabetesEntryPoint.MODULE_ID, newDraftData.getIntygTypeVersion()));

        return template.setGrundData(grundData).build();
    }

    @Override
    public TsDiabetesUtlatandeV4 createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!(template instanceof TsDiabetesUtlatandeV4)) {
            throw new ConverterException("Template is not of type TsDiabetesUtlatandeV4");
        }

        final var tsDiabetesUtlatandeV4 = (TsDiabetesUtlatandeV4) template;

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), tsDiabetesUtlatandeV4.getId());

        final var templateBuilder = tsDiabetesUtlatandeV4.toBuilder();
        final var grundData = tsDiabetesUtlatandeV4.getGrundData();

        populateWithId(templateBuilder, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

        resetDataInGrundData(grundData);
        templateBuilder.setSignature(null);
        return templateBuilder.build();
    }

    private void populateWithId(TsDiabetesUtlatandeV4.Builder utlatande, String utlatandeId) throws ConverterException {
        if (Strings.nullToEmpty(utlatandeId).trim().isEmpty()) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void resetDataInGrundData(GrundData grundData) {
        final var patient = new Patient();
        patient.setPersonId(grundData.getPatient().getPersonId());
        grundData.setPatient(patient);

        grundData.setSigneringsdatum(null);
    }
}
