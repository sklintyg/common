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
package se.inera.intyg.common.support.facade.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.common.support.facade.util.MetaDataToolkit.toCertificate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class MetaDataToolkitTest {

    @Nested
    class ValidateUnit {

        private Vardenhet unit;

        @BeforeEach
        void setUp() {
            unit = new Vardenhet();
        }

        @Test
        void shallIncludeUnitId() {
            final var expectedUnitId = "UnitID";
            unit.setEnhetsid(expectedUnitId);

            final var unit = toCertificate(this.unit);

            assertEquals(expectedUnitId, unit.getUnitId());
        }

        @Test
        void shallIncludeUnitName() {
            final var expectedUnitName = "UnitName";
            unit.setEnhetsnamn(expectedUnitName);

            final var unit = toCertificate(this.unit);

            assertEquals(expectedUnitName, unit.getUnitName());
        }

        @Test
        void shallIncludeUnitAddress() {
            final var expectedUnitAddress = "UnitAddress";
            unit.setPostadress(expectedUnitAddress);

            final var unit = toCertificate(this.unit);

            assertEquals(expectedUnitAddress, unit.getAddress());
        }

        @Test
        void shallIncludeUnitZipCode() {
            final var expectedUnitZipCode = "UnitZipCode";
            unit.setPostnummer(expectedUnitZipCode);

            final var unit = toCertificate(this.unit);

            assertEquals(expectedUnitZipCode, unit.getZipCode());
        }

        @Test
        void shallIncludeUnitCity() {
            final var expectedUnitCity = "UnitCity";
            unit.setPostort(expectedUnitCity);

            final var unit = toCertificate(this.unit);

            assertEquals(expectedUnitCity, unit.getCity());
        }

        @Test
        void shallIncludeUnitEmail() {
            final var expectedUnitEmail = "UnitEmail";
            unit.setEpost(expectedUnitEmail);

            final var unit = toCertificate(this.unit);

            assertEquals(expectedUnitEmail, unit.getEmail());
        }

        @Test
        void shallIncludeUnitPhoneNumber() {
            final var expectedUnitPhoneNumber = "UnitPhoneNumber";
            unit.setTelefonnummer(expectedUnitPhoneNumber);

            final var unit = toCertificate(this.unit);

            assertEquals(expectedUnitPhoneNumber, unit.getPhoneNumber());
        }
    }

    @Nested
    class ValidateIssuedBy {

        private HoSPersonal hoSPersonal;

        @BeforeEach
        void createInternalCertificateToConvert() {
            hoSPersonal = new HoSPersonal();
        }

        @Test
        void shallIncludePersonId() {
            final var expectedPersonId = "PersonId";
            hoSPersonal.setPersonId(expectedPersonId);

            final var staff = toCertificate(hoSPersonal);

            assertEquals(expectedPersonId, staff.getPersonId());
        }

        @Test
        void shallIncludeFullName() {
            final var expectedFullName = "Fullname";
            hoSPersonal.setFullstandigtNamn(expectedFullName);

            final var staff = toCertificate(hoSPersonal);

            assertEquals(expectedFullName, staff.getFullName());
        }
    }

    @Nested
    class ValidatePatient {

        private Patient patient;

        @BeforeEach
        void createInternalCertificateToConvert() {
            patient = new Patient();
            patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
            patient.setEfternamn("Last name");
            patient.setFornamn("First name");
            patient.setMellannamn("Middle name");
            patient.setFullstandigtNamn("FullName name");
            patient.setPostadress("Storgatan 1");
            patient.setPostnummer("831 33");
            patient.setPostort("Östersund");
            patient.setAddressDetailsSourcePU(true);
        }

        @Test
        void shallIncludePersonId() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.getPersonId().getPersonnummerWithDash(), actualPatient.getPersonId().getId());
        }

        @Test
        void shallIncludeFirstName() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.getFornamn(), actualPatient.getFirstName());
        }

        @Test
        void shallIncludeMiddleName() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.getMellannamn(), actualPatient.getMiddleName());
        }

        @Test
        void shallIncludeLastName() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.getEfternamn(), actualPatient.getLastName());
        }

        @Test
        void shallIncludeFullName() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.getFullstandigtNamn(), actualPatient.getFullName());
        }

        @Test
        void shallIncludeStreet() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.getPostadress(), actualPatient.getStreet());
        }

        @Test
        void shallIncludeZipCode() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.getPostnummer(), actualPatient.getZipCode());
        }

        @Test
        void shallIncludeCity() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.getPostort(), actualPatient.getCity());
        }

        @Test
        void shallIncludeAddressFromPU() {
            final var actualPatient = toCertificate(this.patient);
            assertEquals(patient.isAddressDetailsSourcePU(), actualPatient.isAddressFromPU());
        }
    }
}