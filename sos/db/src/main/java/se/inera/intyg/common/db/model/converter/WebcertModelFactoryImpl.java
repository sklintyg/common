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
package se.inera.intyg.common.db.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import se.inera.intyg.common.db.model.internal.DbUtlatande;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

public class WebcertModelFactoryImpl implements WebcertModelFactory<DbUtlatande> {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Override
    public DbUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        DbUtlatande.Builder template = DbUtlatande.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);

        template.setGrundData(grundData);
        template.setTextVersion("1.0");

        return template.build();
    }

    @Override
    public DbUtlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (!DbUtlatande.class.isInstance(template)) {
            throw new ConverterException("Template is not of type DbUtlatande");
        }

        DbUtlatande dbUtlatande = (DbUtlatande) template;

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), dbUtlatande.getId());

        DbUtlatande.Builder templateBuilder = dbUtlatande.toBuilder();
        GrundData grundData = dbUtlatande.getGrundData();

        populateWithId(templateBuilder, copyData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

        resetDataInCopy(grundData);

        return templateBuilder.build();
    }

    private void populateWithId(DbUtlatande.Builder utlatande, String utlatandeId) throws ConverterException {
        if (Strings.nullToEmpty(utlatandeId).trim().isEmpty()) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void resetDataInCopy(GrundData grundData) {
        grundData.setSigneringsdatum(null);
    }
}
