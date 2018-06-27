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
package se.inera.intyg.common.doi.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import se.inera.intyg.common.doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

public class WebcertModelFactoryImpl implements WebcertModelFactory<DoiUtlatande> {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactoryImpl.class);

    @Override
    public DoiUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {
        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        DoiUtlatande.Builder template = DoiUtlatande.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        WebcertModelFactoryUtil.populateGrunddataFromCreateNewDraftHolder(grundData, newDraftData);

        template.setGrundData(grundData);
        template.setTextVersion("1.0");

        return template.build();
    }

    @Override
    public DoiUtlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        if (DoiUtlatande.class.isInstance(template)) {
            DoiUtlatande doiUtlatande = (DoiUtlatande) template;

            LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), doiUtlatande.getId());

            DoiUtlatande.Builder templateBuilder = doiUtlatande.toBuilder();
            GrundData grundData = doiUtlatande.getGrundData();

            populateWithId(templateBuilder, copyData.getCertificateId());
            WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

            resetDataInCopy(grundData);
            templateBuilder.setSignature(null);
            return templateBuilder.build();
        } else if (SosUtlatande.class.isInstance(template)) {
            SosUtlatande sosUtlatande = (SosUtlatande) template;
            DoiUtlatande.Builder builder = DoiUtlatande.builder();
            GrundData grundData = sosUtlatande.getGrundData();

            populateWithId(builder, copyData.getCertificateId());
            WebcertModelFactoryUtil.populateGrunddataFromCreateDraftCopyHolder(grundData, copyData);

            resetDataInCopy(grundData);

            builder.setGrundData(grundData);
            builder.setTextVersion("1.0");

            builder.setIdentitetStyrkt(sosUtlatande.getIdentitetStyrkt());
            builder.setDodsdatumSakert(sosUtlatande.getDodsdatumSakert());
            builder.setDodsdatum(new InternalDate(sosUtlatande.getDodsdatum().getDate()));
            if (sosUtlatande.getAntraffatDodDatum() != null) {
                builder.setAntraffatDodDatum(new InternalDate(sosUtlatande.getAntraffatDodDatum().getDate()));
            }
            builder.setDodsplatsKommun(sosUtlatande.getDodsplatsKommun());
            builder.setDodsplatsBoende(sosUtlatande.getDodsplatsBoende());
            builder.setBarn(sosUtlatande.getBarn());
            builder.setSignature(null);
            return builder.build();
        } else {
            throw new ConverterException("Template is not of correct type");
        }
    }

    private void populateWithId(DoiUtlatande.Builder utlatande, String utlatandeId) throws ConverterException {
        if (Strings.nullToEmpty(utlatandeId).trim().isEmpty()) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void resetDataInCopy(GrundData grundData) {
        grundData.setSigneringsdatum(null);
    }
}
