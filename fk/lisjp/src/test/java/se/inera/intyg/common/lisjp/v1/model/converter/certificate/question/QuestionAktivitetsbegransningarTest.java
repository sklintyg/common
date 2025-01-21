/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_COLLECTION;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_INFO;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import se.inera.intyg.common.lisjp.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigIcf;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataIcfValue;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionAktivitetsbegransningarTest {

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
    class QuestionAktivitetsbegransning {

        private LisjpUtlatandeV1 internalCertificate;

        @BeforeEach
        void createInternalCertificateToConvert() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        @Test
        void shouldIncludeQuestionElement() {
            final var expectedIndex = 13;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

            assertAll("Validating question",
                () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_ID_17, question.getId()),
                () -> assertEquals(expectedIndex, question.getIndex()),
                () -> assertEquals(FUNKTIONSNEDSATTNING_CATEGORY_ID, question.getParent()),
                () -> assertNotNull(question.getValue(), "Missing value"),
                () -> assertNotNull(question.getValidation(), "Missing validation"),
                () -> assertNotNull(question.getConfig(), "Missing config")
            );
        }

        @Test
        void shouldIncludeQuestionConfig() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

            assertEquals(CertificateDataConfigType.UE_ICF, question.getConfig().getType());

            final var certificateDataConfigIcf = (CertificateDataConfigIcf) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertTrue(certificateDataConfigIcf.getHeader().trim().length() > 0, "Missing header"),
                () -> assertTrue(certificateDataConfigIcf.getText().trim().length() > 0, "Missing text"),
                () -> assertTrue(certificateDataConfigIcf.getDescription().trim().length() > 0, "Missing description"),
                () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataConfigIcf.getId()),
                () -> assertEquals(AKTIVITETSBEGRANSNING_ICF_INFO, certificateDataConfigIcf.getModalLabel()),
                () -> assertEquals(AKTIVITETSBEGRANSNING_ICF_COLLECTION, certificateDataConfigIcf.getCollectionsLabel()),
                () -> assertEquals(AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER, certificateDataConfigIcf.getPlaceholder())
            );
        }

        @Test
        void shouldIncludeQuestionValueIcf() {
            final var expectedText = "Text value for question";
            final var expectedIcfValues = Arrays.asList("Test", "Test 2", "Test 3");
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setAktivitetsbegransning(expectedText)
                .setAktivitetsKategorier(expectedIcfValues)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

            final var certificateDataValueIcf = (CertificateDataIcfValue) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataValueIcf.getId()),
                () -> assertEquals(expectedText, certificateDataValueIcf.getText()),
                () -> assertEquals(expectedIcfValues, certificateDataValueIcf.getIcfCodes())
            );
        }

        @Test
        void shouldIncludeQuestionValueTextEmpty() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

            final var certificateDataValueText = (CertificateDataIcfValue) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataValueText.getId()),
                () -> assertNull(certificateDataValueText.getText())
            );
        }

        @Test
        void shouldIncludeQuestionValueIcfEmpty() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

            final var certificateDataValueIcf = (CertificateDataIcfValue) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataValueIcf.getId()),
                () -> assertTrue(certificateDataValueIcf.getIcfCodes().isEmpty())
            );
        }

        @Test
        void shouldIncludeQuestionValidationMandatory() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_ID_17, certificateDataValidationMandatory.getQuestionId()),
                () -> assertEquals("$" + AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, certificateDataValidationMandatory.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationHide() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17);

            final var certificateDataValidationHide = (CertificateDataValidationHide) question.getValidation()[1];
            assertAll("Validation question validation",
                () -> assertEquals(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, certificateDataValidationHide.getQuestionId()),
                () -> assertEquals("$" + AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, certificateDataValidationHide.getExpression())
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

        @Mock
        WebcertModuleService moduleService;
        private LisjpUtlatandeV1 internalCertificate;

        @BeforeEach
        void setup() {
            internalCertificate = LisjpUtlatandeV1.builder()
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
        void shouldIncludeAktivitetsBegransningValueText(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionAktivitetsbegransningar.toCertificate(expectedValue, Collections.emptyList(), index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAktivitetsbegransning());
        }

        Stream<List<String>> icfCodeValues() {
            return Stream.of(Arrays.asList("Test 0", "Test 1", "Test 2"), null, Collections.emptyList());
        }

        @ParameterizedTest
        @MethodSource({"icfCodeValues"})
        void shouldIncludeAktivitetsBegransningValueIcfCodes(List<String> expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionAktivitetsbegransningar.toCertificate("", expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getAktivitetsKategorier());
        }
    }
}
