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
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.lisjp.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

class QuestionSysselsattningYrkeTest {

    @Mock
    WebcertModuleService moduleService;
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
    class ToCertificate {

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
            final var expectedIndex = 8;

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

            assertAll("Validating question",
                () -> assertEquals(NUVARANDE_ARBETE_SVAR_ID_29, question.getId()),
                () -> assertEquals(expectedIndex, question.getIndex()),
                () -> assertEquals(TYP_AV_SYSSELSATTNING_SVAR_ID_28, question.getParent()),
                () -> assertNotNull(question.getValue(), "Missing value"),
                () -> assertNotNull(question.getValidation(), "Missing validation"),
                () -> assertNotNull(question.getConfig(), "Missing config")
            );
        }

        @Test
        void shouldIncludeQuestionConfig() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

            assertEquals(CertificateDataConfigType.UE_TEXTAREA, question.getConfig().getType());

            final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
            assertAll("Validating question configuration",
                () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                () -> assertNull(certificateDataConfigTextArea.getDescription(), "Shouldnt have description"),
                () -> assertEquals(NUVARANDE_ARBETE_SVAR_JSON_ID_29, certificateDataConfigTextArea.getId())
            );
        }

        @Test
        void shouldIncludeQuestionValueText() {
            final var expectedText = "Text value for question";
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setNuvarandeArbete(expectedText)
                .build();

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

            final var certificateDataValueText = (CertificateDataValueText) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(NUVARANDE_ARBETE_SVAR_JSON_ID_29, certificateDataValueText.getId()),
                () -> assertEquals(expectedText, certificateDataValueText.getText())
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

            final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

            final var certificateDataValueText = (CertificateDataValueText) question.getValue();
            assertAll("Validating question value",
                () -> assertEquals(NUVARANDE_ARBETE_SVAR_JSON_ID_29, certificateDataValueText.getId()),
                () -> assertNull(certificateDataValueText.getText())
            );
        }

        @Test
        void shouldIncludeQuestionValidationMandatory() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

            final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
            assertAll("Validation question validation",
                () -> assertEquals(NUVARANDE_ARBETE_SVAR_ID_29, certificateDataValidationMandatory.getQuestionId()),
                () -> assertEquals("$" + NUVARANDE_ARBETE_SVAR_JSON_ID_29,
                    certificateDataValidationMandatory.getExpression())
            );
        }

        @Test
        void shouldIncludeQuestionValidationShow() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            final var question = certificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29);

            final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
            assertAll("Validation question validation",
                () -> assertEquals(TYP_AV_SYSSELSATTNING_SVAR_ID_28, certificateDataValidationShow.getQuestionId()),
                () -> assertEquals("exists(" + SysselsattningsTyp.NUVARANDE_ARBETE.getId() + ")",
                    certificateDataValidationShow.getExpression())
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ToInternal {

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
        void shouldIncludeNuvarandeArbeteValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionSysselsattningYrke.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);
            if (expectedValue == null || expectedValue.isEmpty()) {
                assertNull(updatedCertificate.getNuvarandeArbete());
            } else {
                assertEquals(expectedValue, updatedCertificate.getNuvarandeArbete());
            }
        }
    }
}
