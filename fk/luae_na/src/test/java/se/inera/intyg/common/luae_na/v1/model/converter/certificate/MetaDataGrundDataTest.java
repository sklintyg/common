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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.certificate.MetaDataGrundData.toInternal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Patient;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.Unit;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class MetaDataGrundDataTest {

    private GrundData grundData;
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        final var patient = new se.inera.intyg.common.support.model.common.internal.Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        grundData.setPatient(patient);

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        private LuaenaUtlatandeV1 internalCertificate;

        @Nested
        class CommonMetadata {

            @BeforeEach
            void setUp() {
                internalCertificate = LuaenaUtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();
            }

            @Test
            void shouldIncludeId() {
                final var metadata = MetaDataGrundData.toCertificate(
                    internalCertificate, texts);
                assertEquals(internalCertificate.getId(), metadata.getId());
            }

            @Test
            void shouldIncludeType() {
                final var metadata = MetaDataGrundData.toCertificate(
                    internalCertificate, texts);
                assertEquals(internalCertificate.getTyp(), metadata.getType());
            }

            @Test
            void shouldIncludeTypeVersion() {
                final var metadata = MetaDataGrundData.toCertificate(
                    internalCertificate, texts);
                assertEquals(internalCertificate.getTextVersion(), metadata.getTypeVersion());
            }

            @Test
            void shouldIncludeName() {
                final var metadata = MetaDataGrundData.toCertificate(
                    internalCertificate, texts);
                assertEquals("Läkarutlåtande för aktivitetsersättning vid nedsatt arbetsförmåga", metadata.getName());
            }

            @Test
            void shouldIncludeDescription() {
                final var metadata = MetaDataGrundData.toCertificate(
                    internalCertificate, texts);
                assertTrue(metadata.getDescription().trim().length() > 0, "Should contain description");
            }

            @Test
            void shouldIncludeUnit() {
                final var metadata = MetaDataGrundData.toCertificate(
                    internalCertificate, texts);
                assertNotNull(metadata.getUnit(), "Missing unit!");
            }

            @Test
            void shouldIncludeIssuedBy() {
                final var metadata = MetaDataGrundData.toCertificate(
                    internalCertificate, texts);
                assertNotNull(metadata.getIssuedBy(), "Missing issued by!");
            }

            @Test
            void shouldIncludePatient() {
                final var metadata = MetaDataGrundData.toCertificate(internalCertificate, texts);
                assertNotNull(metadata.getPatient(), "Missing patient!");
            }
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        private CertificateMetadata metadata;

        @BeforeEach
        void setUp() {
            metadata = CertificateMetadata.builder()
                .unit(Unit.builder().build())
                .patient(Patient.builder().build())
                .build();
        }

        @Test
        void shouldIncludeGrundData() {
            final var actualGrundData = toInternal(metadata, grundData);
            assertNotNull(actualGrundData, "Missing grundData!");
        }
    }
}
