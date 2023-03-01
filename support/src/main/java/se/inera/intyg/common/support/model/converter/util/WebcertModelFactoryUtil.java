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
package se.inera.intyg.common.support.model.converter.util;

import com.google.common.base.Strings;
import java.time.LocalDateTime;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;

public final class WebcertModelFactoryUtil {

    private WebcertModelFactoryUtil() {
    }

    public static void updateSkapadAv(Utlatande utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().setSkapadAv(hosPerson);
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
    }

    public static void populateGrunddataFromCreateDraftCopyHolder(GrundData grundData, CreateDraftCopyHolder copyData)
        throws ConverterException {
        validateRequest(copyData.getSkapadAv());

        if (grundData.getSkapadAv().getVardenhet().getEnhetsid().equals(copyData.getSkapadAv().getVardenhet().getEnhetsid())) {
            // grundData is the copied information that we received from the original certificate. This object contains
            // information that we want to preserve if the enhet is the same as in the logged in user. See INTYG-2835.
            populateWithMissingInfo(copyData.getSkapadAv().getVardenhet(), grundData.getSkapadAv().getVardenhet());
        }

        grundData.setSkapadAv(copyData.getSkapadAv());
        grundData.setRelation(copyData.getRelation());
        grundData.setTestIntyg(copyData.isTestIntyg());

        if (copyData.hasPatient()) {
            populateWithPatientInfo(grundData, copyData.getPatient());
        }

        if (copyData.hasNewPersonnummer()) {
            grundData.getPatient().setPersonId(copyData.getNewPersonnummer());
        }
    }

    public static void populateGrunddataFromCreateNewDraftHolder(GrundData grundData,
        CreateNewDraftHolder newDraftData) throws ConverterException {
        validateRequest(newDraftData.getSkapadAv());
        grundData.setSkapadAv(newDraftData.getSkapadAv());
        grundData.setTestIntyg(newDraftData.isTestIntyg());
        populateWithPatientInfo(grundData, newDraftData.getPatient());
    }

    public static void populateWithPatientInfo(GrundData grundData, Patient patient) throws ConverterException {
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        if (patientGotValuesFromPU(patient)) {
            grundData.setPatient(patient);
        } else {
            patient.setFullstandigtNamn(grundData.getPatient().getFullstandigtNamn());
            patient.setFornamn(grundData.getPatient().getFornamn());
            patient.setMellannamn(grundData.getPatient().getMellannamn());
            patient.setEfternamn(grundData.getPatient().getEfternamn());
            patient.setPostadress(grundData.getPatient().getPostadress());
            patient.setPostort(grundData.getPatient().getPostort());
            patient.setPostnummer(grundData.getPatient().getPostnummer());
        }

        // INTYG-5573, if adress is incomplete dont use it, to stay consistent how other parts of Webcert behave.
        if (!patient.isCompleteAddressProvided()) {
            patient.setPostadress(null);
            patient.setPostnummer(null);
            patient.setPostort(null);
        }
    }

    private static boolean patientGotValuesFromPU(Patient patient) {
        return patient.getFornamn() != null && patient.getEfternamn() != null;
    }

    private static void populateWithMissingInfo(Vardenhet target, Vardenhet source) {
        if (Strings.nullToEmpty(target.getPostadress()).trim().isEmpty()) {
            target.setPostadress(source.getPostadress());
        }
        if (Strings.nullToEmpty(target.getPostnummer()).trim().isEmpty()) {
            target.setPostnummer(source.getPostnummer());
        }
        if (Strings.nullToEmpty(target.getPostort()).trim().isEmpty()) {
            target.setPostort(source.getPostort());
        }
        if (Strings.nullToEmpty(target.getTelefonnummer()).trim().isEmpty()) {
            target.setTelefonnummer(source.getTelefonnummer());
        }

    }

    private static void validateRequest(HoSPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }
    }
}
