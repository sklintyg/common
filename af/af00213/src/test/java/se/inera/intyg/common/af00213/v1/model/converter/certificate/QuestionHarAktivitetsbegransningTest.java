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
package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;

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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class QuestionHarAktivitetsbegransningTest {

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

        Stream<Boolean> booleanValues() {
            return Stream.of(true, false, null);
        }

        @ParameterizedTest
        @MethodSource("booleanValues")
        void shouldIncludeHarAktivitetsbegransningValue(Boolean expectedValue) {
            final var index = 1;

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionHarAktivitetsbegransning.toCertificate(expectedValue, index, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate);

            assertEquals(expectedValue, updatedCertificate.getHarAktivitetsbegransning());
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
        class QuestionHarAktivitetsbegransning {

            private Af00213UtlatandeV1 internalCertificate;

            @BeforeEach
            void createAf00213ToConvert() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarAktivitetsbegransning(true)
                    .build();
            }

            @Test
            void shouldIncludeQuestionElement() {
                final var expectedIndex = 4;

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                assertAll("Validating question HarAktivitetsbegränsning",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_DELSVAR_ID_21, question.getId()),
                    () -> assertEquals(expectedIndex, question.getIndex()),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_CATEGORY_ID, question.getParent()),
                    () -> assertNotNull(question.getValue(), "Should include a value"),
                    () -> assertNotNull(question.getValidation(), "Should include validation"),
                    () -> assertNotNull(question.getConfig(), "Should include config")
                );
            }

            @Test
            void shouldIncludeQuestionConfig() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                assertEquals(CertificateDataConfigTypes.UE_RADIO_BOOLEAN, question.getConfig().getType());

                final var certificateDataConfigBoolean = (CertificateDataConfigRadioBoolean) question.getConfig();
                assertAll("Validating question configuration",
                    () -> assertTrue(certificateDataConfigBoolean.getText().trim().length() > 0, "Missing text"),
                    () -> assertTrue(certificateDataConfigBoolean.getDescription().trim().length() > 0, "Missing description"),
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataConfigBoolean.getId()),
                    () -> assertTrue(certificateDataConfigBoolean.getSelectedText().trim().length() > 0, "Missing selected text"),
                    () -> assertTrue(certificateDataConfigBoolean.getUnselectedText().trim().length() > 0, "Missing unselected text")
                );
            }

            @Test
            void shouldIncludeQuestionValueTrue() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarAktivitetsbegransning(), certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValueFalse() {
                internalCertificate = Af00213UtlatandeV1.builder()
                    .setGrundData(grundData)
                    .setId("id")
                    .setTextVersion("TextVersion")
                    .setHarAktivitetsbegransning(false)
                    .build();

                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValueBoolean.getId()),
                    () -> assertEquals(internalCertificate.getHarAktivitetsbegransning(), certificateDataValueBoolean.getSelected())
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

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValueBoolean = (CertificateDataValueBoolean) question.getValue();
                assertAll("Validating question value",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValueBoolean.getId()),
                    () -> assertNull(certificateDataValueBoolean.getSelected())
                );
            }

            @Test
            void shouldIncludeQuestionValidationMandatory() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValidationMandatory = (CertificateDataValidationMandatory) question.getValidation()[0];
                assertAll("Validation question validation",
                    () -> assertEquals(AKTIVITETSBEGRANSNING_DELSVAR_ID_21, certificateDataValidationMandatory.getQuestionId()),
                    () -> assertEquals("$" + AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21, certificateDataValidationMandatory.getExpression())
                );
            }

            @Test
            void shouldIncludeQuestionValidationShow() {
                final var certificate = InternalToCertificate.convert(internalCertificate, texts);

                final var question = certificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21);

                final var certificateDataValidationShow = (CertificateDataValidationShow) question.getValidation()[1];
                assertAll("Validation question validation",
                    () -> assertEquals(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, certificateDataValidationShow.getQuestionId()),
                    () -> assertEquals("$" + FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11, certificateDataValidationShow.getExpression())
                );
            }
        }
    }
}