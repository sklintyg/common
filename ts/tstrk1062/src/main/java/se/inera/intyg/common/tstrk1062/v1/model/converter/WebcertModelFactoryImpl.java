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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint;
import se.inera.intyg.common.tstrk1062.v1.model.internal.Bedomning;
import se.inera.intyg.common.tstrk1062.v1.model.internal.IntygAvser;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;

/**
 * Factory for creating a editable model.
 */
@Component("tstrk1062.v1.WebcertModelFactoryImpl")
public class WebcertModelFactoryImpl implements WebcertModelFactory<TsTrk1062UtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;


    public WebcertModelFactoryImpl() {

    }

    public WebcertModelFactoryImpl(IntygTextsService intygTexts) {
        this.intygTexts = intygTexts;
    }

    @Override
    public TsTrk1062UtlatandeV1 createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        final TsTrk1062UtlatandeV1.Builder template = TsTrk1062UtlatandeV1.builder();

        final GrundData grundData = new GrundData();

        template.setId(newDraftData.getCertificateId());

        template.setTextVersion(intygTexts.getLatestVersionForSameMajorVersion(TsTrk1062EntryPoint.MODULE_ID,
            newDraftData.getIntygTypeVersion()));

        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        resetDataInGrundData(grundData);
        template.setGrundData(grundData);

        template.setIntygAvser(IntygAvser.create(EnumSet.noneOf(IntygAvser.BehorighetsTyp.class)));

        template.setBedomning(Bedomning.builder().setUppfyllerBehorighetskrav(EnumSet.noneOf(Bedomning.BehorighetsTyp.class)).build());

        return template.build();
    }

    @Override
    public TsTrk1062UtlatandeV1 createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        TsTrk1062UtlatandeV1 tsTrk1062Utlatande;
        if (template instanceof TsTrk1062UtlatandeV1) {
            tsTrk1062Utlatande = (TsTrk1062UtlatandeV1) template;
        } else {
            throw new ConverterException("Template is not of type TsTrk1062UtlatandeV1");
        }

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), tsTrk1062Utlatande.getId());

        TsTrk1062UtlatandeV1.Builder templateBuilder = tsTrk1062Utlatande.toBuilder();

        GrundData grundData = tsTrk1062Utlatande.getGrundData();
        if (grundData != null) {
            WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);
            resetDataInGrundData(grundData);
        }

        populateWithId(templateBuilder, copyData.getCertificateId());

        templateBuilder.setSignature(null);

        return templateBuilder.build();
    }

    private void resetDataInGrundData(GrundData grundData) {
        Patient patient = new Patient();
        patient.setPersonId(grundData.getPatient().getPersonId());
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(null);
    }

    private void populateWithId(TsTrk1062UtlatandeV1.Builder utlatande, String utlatandeId) throws ConverterException {
        if (Strings.isNullOrEmpty(utlatandeId)) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }
}
