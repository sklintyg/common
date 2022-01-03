/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.model.converter;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.lisjp.support.LisjpEntryPoint;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillHandler;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillResult;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

/**
 * Factory for creating an editable model.
 */
@Component("lisjp.v1.WebcertModelFactoryImpl")
public class WebcertModelFactoryImpl implements WebcertModelFactory<LisjpUtlatandeV1> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);
    @Autowired(required = false)
    protected WebcertModuleService moduleService;
    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new lisjp draft pre-populated with the attached data.
     *
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link LisjpUtlatandeV1} or throws a ConverterException if something unforeseen happens
     */
    @Override
    public LisjpUtlatandeV1 createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        LisjpUtlatandeV1.Builder template = LisjpUtlatandeV1.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);
        resetDataInGrundData(grundData);
        template.setSignature(null);

        // Default to latest minor version available for major version of intygtype
        String fullVersion = intygTexts.getLatestVersionForSameMajorVersion(LisjpEntryPoint.MODULE_ID, newDraftData.getIntygTypeVersion());
        template.setTextVersion(fullVersion);
        if (newDraftData.getForifyllnad().isPresent()) {
            PrefillHandler prefillHandler = new PrefillHandler(moduleService, newDraftData.getCertificateId(), LisjpEntryPoint.MODULE_ID,
                fullVersion);
            PrefillResult prefillResult = prefillHandler.prefill(template, newDraftData.getForifyllnad().get());
            LOG.info("Prefill result log: " + prefillResult.toJsonReport());
        }
        return template.setGrundData(grundData).build();
    }

    @Override
    public LisjpUtlatandeV1 createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!LisjpUtlatandeV1.class.isInstance(template)) {
            throw new ConverterException("Template is not of type LisjpUtlatandeV1");
        }

        LisjpUtlatandeV1 lisjpUtlatandeV1 = (LisjpUtlatandeV1) template;

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), lisjpUtlatandeV1.getId());

        LisjpUtlatandeV1.Builder templateBuilder = lisjpUtlatandeV1.toBuilder();
        GrundData grundData = lisjpUtlatandeV1.getGrundData();
        templateBuilder.setTextVersion(copyData.getIntygTypeVersion());

        populateWithId(templateBuilder, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

        resetDataInGrundData(grundData);
        templateBuilder.setSignature(null);
        return templateBuilder.build();
    }

    private void populateWithId(LisjpUtlatandeV1.Builder utlatande, String utlatandeId) throws ConverterException {
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
