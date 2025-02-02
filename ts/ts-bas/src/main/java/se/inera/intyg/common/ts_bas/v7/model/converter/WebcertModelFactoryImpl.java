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
package se.inera.intyg.common.ts_bas.v7.model.converter;

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
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.common.ts_bas.v7.model.internal.Bedomning;
import se.inera.intyg.common.ts_bas.v7.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_bas.v7.model.internal.Funktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.internal.HjartKarl;
import se.inera.intyg.common.ts_bas.v7.model.internal.HorselBalans;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvser;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medicinering;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medvetandestorning;
import se.inera.intyg.common.ts_bas.v7.model.internal.NarkotikaLakemedel;
import se.inera.intyg.common.ts_bas.v7.model.internal.Sjukhusvard;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.common.ts_bas.v7.model.internal.Vardkontakt;

/**
 * Factory for creating a editable model.
 */
@Component("ts-bas.v7.WebcertModelFactoryImpl")
public class WebcertModelFactoryImpl implements WebcertModelFactory<TsBasUtlatandeV7> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new TS-bas draft pre-populated with the attached data.
     *
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link TsBasUtlatandeV7}
     * @throws ConverterException if something unforeseen happens
     */
    @Override
    public TsBasUtlatandeV7 createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());
        TsBasUtlatandeV7.Builder template = TsBasUtlatandeV7.builder();
        GrundData grundData = new GrundData();

        template.setId(newDraftData.getCertificateId());
        // Default to latest minor version available for major version of intygtype
        template.setTextVersion(
            intygTexts.getLatestVersionForSameMajorVersion(TsBasEntryPoint.MODULE_ID, newDraftData.getIntygTypeVersion()));

        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        template.setGrundData(grundData);

        template.setBedomning(Bedomning.builder().setKorkortstyp(EnumSet.noneOf(BedomningKorkortstyp.class)).build());
        template.setDiabetes(Diabetes.builder().build());
        template.setFunktionsnedsattning(Funktionsnedsattning.builder().build());
        template.setHjartKarl(HjartKarl.builder().build());
        template.setHorselBalans(HorselBalans.builder().build());
        template.setIntygAvser(IntygAvser.create(null));
        template.setMedicinering(Medicinering.builder().build());
        template.setMedvetandestorning(Medvetandestorning.builder().build());
        template.setNarkotikaLakemedel(NarkotikaLakemedel.builder().build());
        template.setSjukhusvard(Sjukhusvard.builder().build());
        template.setSyn(Syn.builder().build());
        template.setVardkontakt(Vardkontakt.create(null, null));

        return template.build();
    }

    @Override
    public TsBasUtlatandeV7 createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!(template instanceof TsBasUtlatandeV7)) {
            throw new ConverterException("Template is not of type TsBasUtlatandeV7");
        }
        TsBasUtlatandeV7 tsBasUtlatande = (TsBasUtlatandeV7) template;
        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), tsBasUtlatande.getId());

        TsBasUtlatandeV7.Builder templateBuilder = tsBasUtlatande.toBuilder();

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

    private void populateWithId(TsBasUtlatandeV7.Builder utlatande, String utlatandeId) throws ConverterException {
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
