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
package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.af00213.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.af00213.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
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
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class QuestionUtredningBehandlingTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
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

        Stream<String> textValues() {
            return Stream.of("Här kommer en text!", "", null);
        }

        @ParameterizedTest
        @MethodSource("textValues")
        void shouldIncludeUtredningBehandlingValue(String expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionUtredningBehandling.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            if (expectedValue == null || expectedValue.isEmpty()) {
                assertNull(updatedCertificate.getUtredningBehandling());
            } else {
                assertEquals(expectedValue, updatedCertificate.getUtredningBehandling());
            }
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ToCertificate {

        private GrundData grundData;

        @BeforeEach
        void setup() {
            final var unit = new Vardenhet();

            final var skapadAv = new HoSPersonal();
            skapadAv.setVardenhet(unit);

            grundData = new GrundData();
            grundData.setSkapadAv(skapadAv);

            final var patient = new Patient();
            patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
            grundData.setPatient(patient);
        }

        @Nested
        class QuestionUtredningOchBehandling {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setUtredningBehandling("Text som beskriver Utredning/Behandling.")
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 8;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                assertAll("Validating question Funktionsnedsättning",
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_32, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_31, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                assertEquals(CertificateDataConfigType.UE_TEXTAREA, question.getConfig().getType());

                final var certificateDataConfigTextArea = (CertificateDataConfigTextArea) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigTextArea.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigTextArea.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_32, certificateDataConfigTextArea.getId())
                );
            }

            @Test
            void shouldIncludeQuestionValue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                final var certificateDataTextValue = (CertificateDataValueText) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_32, certificateDataTextValue.getId()),
                    () -> assertEquals(internalCertificate.getUtredningBehandling(), certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValueEmpty() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                final var certificateDataTextValue = (CertificateDataValueText) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(UTREDNING_BEHANDLING_SVAR_JSON_ID_32, certificateDataTextValue.getId()),
                    () -> assertNull(certificateDataTextValue.getText())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_32, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + UTREDNING_BEHANDLING_SVAR_JSON_ID_32, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(UTREDNING_BEHANDLING_DELSVAR_ID_31, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + UTREDNING_BEHANDLING_SVAR_JSON_ID_31, certificateDataValidationShow.getExpression())
                );
            }
        }
    }
}
