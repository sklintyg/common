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

package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import se.inera.intyg.common.af00213.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;

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

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> values() {
            return Stream.of("test string", "", null);
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitAddress(String expectedValue) {
            var grundData = new GrundData();
            var hosPersonal = new HoSPersonal();
            var vardenhet = new Vardenhet();
            vardenhet.setPostadress(expectedValue);
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            final var utlatande =
                Af00213UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();

            final var certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostadress());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitCity(String expectedValue) {
            var grundData = new GrundData();
            var hosPersonal = new HoSPersonal();
            var vardenhet = new Vardenhet();
            vardenhet.setPostort(expectedValue);
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            final var utlatande =
                Af00213UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();

            final var certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostort());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitZipCode(String expectedValue) {
            var grundData = new GrundData();
            var hosPersonal = new HoSPersonal();
            var vardenhet = new Vardenhet();
            vardenhet.setPostnummer(expectedValue);
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            final var utlatande =
                Af00213UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();

            final var certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostnummer());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitPhonenumber(String expectedValue) {
            var grundData = new GrundData();
            var hosPersonal = new HoSPersonal();
            var vardenhet = new Vardenhet();
            vardenhet.setTelefonnummer(expectedValue);
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            final var utlatande =
                Af00213UtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();

            final var certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer());
        }
    }

    @Nested
    class ToCertificate {

        private Af00213UtlatandeV1 internalCertificate;

        @Nested
        class CommonMetadata {

            @BeforeEach
            void setUp() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

            }

            @Test
            void shouldIncludeId() {
                final var metadata = MetaDataGrundData.toCertificate(internalCertificate, texts);
                assertEquals(internalCertificate.getId(), metadata.getId());
            }

            @Test
            void shouldIncludeType() {
                final var metadata = MetaDataGrundData.toCertificate(internalCertificate, texts);
                assertEquals(internalCertificate.getTyp(), metadata.getType());
            }

            @Test
            void shouldIncludeTypeVersion() {
                final var metadata = MetaDataGrundData.toCertificate(internalCertificate, texts);
                assertEquals(internalCertificate.getTextVersion(), metadata.getTypeVersion());
            }

            @Test
            void shouldIncludeName() {
                final var metadata = MetaDataGrundData.toCertificate(internalCertificate, texts);
                assertEquals("Arbetsförmedlingens medicinska utlåtande", metadata.getName());
            }

            @Test
            void shouldIncludeDescription() {
                final var metadata = MetaDataGrundData.toCertificate(internalCertificate, texts);
                assertTrue(metadata.getDescription().trim().length() > 0, "Should contain description");
            }

            @Test
            void shouldIncludeUnit() {
                final var metadata = MetaDataGrundData.toCertificate(internalCertificate, texts);
                assertNotNull(metadata.getUnit(), "Missing unit!");
            }

            @Test
            void shouldIncludeIssuedBy() {
                final var metadata = MetaDataGrundData.toCertificate(internalCertificate, texts);
                assertNotNull(metadata.getIssuedBy(), "Missing issued by!");
            }
        }
    }
}