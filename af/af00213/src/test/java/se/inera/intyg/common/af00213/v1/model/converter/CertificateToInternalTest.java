/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.af00213.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;

@DisplayName("Should convert Certificate to AF00213")
class CertificateToInternalTest {

    private CertificateTextProvider texts;

    @BeforeEach
    private void setup() {
        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionHarFunktionsnedsattning {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeHarFunktionsnedsattningValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createHarFunktionsnedsattningsQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getHarFunktionsnedsattning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionFunktionsnedsattning {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeFunktionsnedsattningValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createFunktionsnedsattningsQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getFunktionsnedsattning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionHarAktivitetsbegransning {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeHarAktivitetsbegransningValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createHarAktivitetsbegransningsQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getHarAktivitetsbegransning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionAktivitetsbegransning {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeAktivitetsbegransningValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createAktivitetsbegransningsQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getAktivitetsbegransning());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionHarUtredningBehandling {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeHarUtredningBehandlingValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createHarUtredningBehandlingsQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getHarUtredningBehandling());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionUtredningBehandling {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeUtredningBehandlingValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createUtredningBehandlingsQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getUtredningBehandling());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionHarArbetetspaverkan {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

        }

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeHarArbetetspaverkanValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createHarArbetspaverkansQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getHarArbetetsPaverkan());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionArbetetspaverkan {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeArbetetspaverkanValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createArbetspaverkansQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getArbetetsPaverkan());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class QuestionOvrigt {

        private Af00213UtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = Af00213UtlatandeV1.builder()
                .setGrundData(new GrundData())
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeOvrigtValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(InternalToCertificate.createOvrigtQuestion(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getOvrigt());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GrundDataTest {

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
                .metadata(InternalToCertificate.createMetadata(utlatande, texts))
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
                .metadata(InternalToCertificate.createMetadata(utlatande, texts))
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
                .metadata(InternalToCertificate.createMetadata(utlatande, texts))
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
                .metadata(InternalToCertificate.createMetadata(utlatande, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer());
        }
    }
}