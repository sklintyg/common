/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.model.converter.util;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

public final class WebcertModelFactoryUtil {

    private WebcertModelFactoryUtil() {
    }

    public static void updateSkapadAv(Utlatande utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().setSkapadAv(hosPerson);
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
    }

    public static void populateGrunddataFromCreateDraftCopyHolder(GrundData grundData, CreateDraftCopyHolder copyData) throws ConverterException {
        validateRequest(copyData.getSkapadAv());

        if (grundData.getSkapadAv().getVardenhet().getEnhetsid().equals(copyData.getSkapadAv().getVardenhet().getEnhetsid())) {
            // grundData is the copied information that we received from the original certificate. This object contains
            // information that we want to preserve if the enhet is the same as in the logged in user. See INTYG-2835.
            populateWithMissingInfo(copyData.getSkapadAv().getVardenhet(), grundData.getSkapadAv().getVardenhet());
        }

        grundData.setSkapadAv(copyData.getSkapadAv());
        grundData.setRelation(copyData.getRelation());

        if (copyData.hasPatient()) {
            populateWithPatientInfo(grundData, copyData.getPatient());
        }

        if (copyData.hasNewPersonnummer()) {
            grundData.getPatient().setPersonId(copyData.getNewPersonnummer());
        }
    }

    public static void populateGrunddataFromCreateNewDraftHolder(GrundData grundData, CreateNewDraftHolder newDraftData) throws ConverterException {
        validateRequest(newDraftData.getSkapadAv());
        grundData.setSkapadAv(newDraftData.getSkapadAv());
        populateWithPatientInfo(grundData, newDraftData.getPatient());
    }

    public static void populateWithPatientInfo(GrundData grundData, Patient patient) throws ConverterException {
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }
        grundData.setPatient(patient);
    }

    private static void populateWithMissingInfo(Vardenhet target, Vardenhet source) {
        if (StringUtils.isBlank(target.getPostadress())) {
            target.setPostadress(source.getPostadress());
        }
        if (StringUtils.isBlank(target.getPostnummer())) {
            target.setPostnummer(source.getPostnummer());
        }
        if (StringUtils.isBlank(target.getPostort())) {
            target.setPostort(source.getPostort());
        }
        if (StringUtils.isBlank(target.getTelefonnummer())) {
            target.setTelefonnummer(source.getTelefonnummer());
        }

    }

    private static void validateRequest(HoSPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }
    }

}
