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

package se.inera.intyg.common.ag7804.v1.model.converter.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.ag7804.support.Ag7804EntryPoint;
import se.inera.intyg.common.ag7804.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class MetaDataGrundDataTest {

    private GrundData grundData;
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());

        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class MetaData {

        private Ag7804UtlatandeV1 internalCertificate;

        @Nested
        class CommonMetadata {

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion 1")
                    .build();

            }

            @Test
            void shouldIncludeCertificateId() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(internalCertificate.getId(), certificate.getMetadata().getId());
            }

            @Test
            void shouldIncludeCertificateType() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(internalCertificate.getTyp(), certificate.getMetadata().getType());
            }

            @Test
            void shouldIncludeCertificateTypeVersion() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(internalCertificate.getTextVersion(), certificate.getMetadata().getTypeVersion());
            }

            @Test
            void shouldIncludeCertificateName() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(Ag7804EntryPoint.MODULE_NAME, certificate.getMetadata().getName());
            }

            @Test
            void shouldIncludeCertificateDescription() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertTrue(certificate.getMetadata().getDescription().trim().length() > 0, "Should contain description");
            }
        }

        @Nested
        class ValidateUnit {

            private Vardenhet unit;

            @BeforeEach
            void createInternalCertificateToConvert() {
                unit = new Vardenhet();

                grundData.getSkapadAv().setVardenhet(unit);

                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shallIncludeUnitId() {
                final var expectedUnitId = "UnitID";
                unit.setEnhetsid(expectedUnitId);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitId, certificate.getMetadata().getUnit().getUnitId());
            }

            @Test
            void shallIncludeUnitName() {
                final var expectedUnitName = "UnitName";
                unit.setEnhetsnamn(expectedUnitName);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitName, certificate.getMetadata().getUnit().getUnitName());
            }

            @Test
            void shallIncludeUnitAddress() {
                final var expectedUnitAddress = "UnitAddress";
                unit.setPostadress(expectedUnitAddress);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitAddress, certificate.getMetadata().getUnit().getAddress());
            }

            @Test
            void shallIncludeUnitZipCode() {
                final var expectedUnitZipCode = "UnitZipCode";
                unit.setPostnummer(expectedUnitZipCode);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitZipCode, certificate.getMetadata().getUnit().getZipCode());
            }

            @Test
            void shallIncludeUnitCity() {
                final var expectedUnitCity = "UnitCity";
                unit.setPostort(expectedUnitCity);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitCity, certificate.getMetadata().getUnit().getCity());
            }

            @Test
            void shallIncludeUnitEmail() {
                final var expectedUnitEmail = "UnitEmail";
                unit.setEpost(expectedUnitEmail);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitEmail, certificate.getMetadata().getUnit().getEmail());
            }

            @Test
            void shallIncludeUnitPhoneNumber() {
                final var expectedUnitPhoneNumber = "UnitPhoneNumber";
                unit.setTelefonnummer(expectedUnitPhoneNumber);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedUnitPhoneNumber, certificate.getMetadata().getUnit().getPhoneNumber());
            }
        }

        @Nested
        class ValidateIssuedBy {

            @BeforeEach
            void createInternalCertificateToConvert() {
                internalCertificate = Ag7804UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shallIncludePersonId() {
                final var expectedPersonId = "PersonId";
                grundData.getSkapadAv().setPersonId(expectedPersonId);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedPersonId, certificate.getMetadata().getIssuedBy().getPersonId());
            }

            @Test
            void shallIncludeFullName() {
                final var expectedFullName = "Fullname";
                grundData.getSkapadAv().setFullstandigtNamn(expectedFullName);

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                assertEquals(expectedFullName, certificate.getMetadata().getIssuedBy().getFullName());
            }
        }
    }
}
