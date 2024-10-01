/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BINOKULART_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HOGER_OGA_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_HOGER_OGA_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KONTAKTLINSER_VANSTER_OGA_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.MED_KORREKTION_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFUNKTIONER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UTAN_KORREKTION_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VANSTER_OGA_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARDEN_FOR_SYNSKARPA_ID;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.BINOCULAR;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.LEFT_EYE;
import static se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa.VisualAcuityEnum.RIGHT_EYE;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigVisualAcuity;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueType;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueVisualAcuities;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationDisableTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v7.model.internal.Synskarpevarden;

@ExtendWith(MockitoExtension.class)
class QuestionSynskarpaTest {

    @Mock
    CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        when(textProvider.get(any(String.class))).thenReturn("test string");
    }

    @Nested
    class ToCertificate {

        @Nested
        class IncludeCommonElementTests extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                final var syn = Syn.builder().build();
                return QuestionSynskarpa.toCertificate(syn, 0, textProvider);
            }

            @Override
            protected String getId() {
                return VARDEN_FOR_SYNSKARPA_ID;
            }

            @Override
            protected String getParent() {
                return SYNFUNKTIONER_CATEGORY_ID;
            }

            @Override
            protected int getIndex() {
                return 0;
            }
        }

        @Nested
        class ConfigVisualAcuityTests {

            @Test
            void shouldIncludeConfigTypeVisualAcuity() {
                final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                final var config = question.getConfig().getType();
                assertEquals(CertificateDataConfigType.UE_VISUAL_ACUITY, config);
            }

            @Test
            void shouldIncludeWithoutCorrectionLabel() {
                final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                verify(textProvider, atLeastOnce()).get(UTAN_KORREKTION_TEXT_ID);
            }

            @Test
            void shouldIncludeWithCorrectionLabel() {
                final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                verify(textProvider, atLeastOnce()).get(MED_KORREKTION_TEXT_ID);
            }

            @Test
            void shouldIncludeContactLensesLabel() {
                final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                verify(textProvider, atLeastOnce()).get(KONTAKTLINSER_TEXT_ID);
            }

            @Nested
            class RightEye {

                @Test
                void shouldIncludeRightEyeLabel() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    verify(textProvider, atLeastOnce()).get(HOGER_OGA_LABEL_ID);
                }

                @Test
                void shouldIncludeRightEyeWithoutCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var config = (CertificateDataConfigVisualAcuity) question.getConfig();
                    assertEquals(HOGER_OGA_UTAN_KORREKTION_JSON_ID, config.getRightEye().getWithoutCorrectionId());
                }

                @Test
                void shouldIncludeRightEyeWithCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var config = (CertificateDataConfigVisualAcuity) question.getConfig();
                    assertEquals(HOGER_OGA_MED_KORREKTION_JSON_ID, config.getRightEye().getWithCorrectionId());
                }

                @Test
                void shouldIncludeRightEyeContactLensesId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var config = (CertificateDataConfigVisualAcuity) question.getConfig();
                    assertEquals(KONTAKTLINSER_HOGER_OGA_DELSVAR_JSON_ID, config.getRightEye().getContactLensesId());
                }
            }

            @Nested
            class LeftEye {

                @Test
                void shouldIncludeLeftEyeLabel() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    verify(textProvider, atLeastOnce()).get(VANSTER_OGA_LABEL_ID);
                }

                @Test
                void shouldIncludeLeftEyeWithoutCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var config = (CertificateDataConfigVisualAcuity) question.getConfig();
                    assertEquals(VANSTER_OGA_UTAN_KORREKTION_JSON_ID, config.getLeftEye().getWithoutCorrectionId());
                }

                @Test
                void shouldIncludeLeftEyeWithCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var config = (CertificateDataConfigVisualAcuity) question.getConfig();
                    assertEquals(VANSTER_OGA_MED_KORREKTION_JSON_ID, config.getLeftEye().getWithCorrectionId());
                }

                @Test
                void shouldIncludeLeftEyeContactLensesId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var config = (CertificateDataConfigVisualAcuity) question.getConfig();
                    assertEquals(KONTAKTLINSER_VANSTER_OGA_JSON_ID, config.getLeftEye().getContactLensesId());
                }
            }

            @Nested
            class Binocular {

                @Test
                void shouldIncludeBinocularLabel() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    verify(textProvider, atLeastOnce()).get(BINOKULART_LABEL_ID);
                }

                @Test
                void shouldIncludeBinocularWithoutCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var config = (CertificateDataConfigVisualAcuity) question.getConfig();
                    assertEquals(BINOKULART_UTAN_KORREKTION_JSON_ID, config.getBinocular().getWithoutCorrectionId());
                }

                @Test
                void shouldIncludeBinocularWithCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var config = (CertificateDataConfigVisualAcuity) question.getConfig();
                    assertEquals(BINOKULART_MED_KORREKTION_JSON_ID, config.getBinocular().getWithCorrectionId());
                }
            }
        }

        @Nested
        class ValueVisualAcuityTests {

            @Test
            void shouldIncludeValueTypeVisualAcuity() {
                final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                final var valueType = question.getValue().getType();
                assertEquals(CertificateDataValueType.VISUAL_ACUITIES, valueType);
            }

            @Nested
            class RightEye {

                @Test
                void shouldIncludeValueRightEye() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertNotNull(value.getRightEye());
                }

                @Test
                void shouldIncludeValueRightEyeWithoutCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(HOGER_OGA_UTAN_KORREKTION_JSON_ID, value.getRightEye().getWithoutCorrection().getId());
                }

                @Test
                void shouldIncludeValueRightEyeWithoutCorrectionNumber() {
                    final var expectedNumber = 2.2;
                    final var syn = Syn.builder()
                        .setHogerOga(
                            Synskarpevarden.builder()
                                .setUtanKorrektion(expectedNumber)
                                .build()
                        )
                        .build();
                    final var question = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(expectedNumber, value.getRightEye().getWithoutCorrection().getValue());
                }

                @Test
                void shouldIncludeValueRightEyeWithCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(HOGER_OGA_MED_KORREKTION_JSON_ID, value.getRightEye().getWithCorrection().getId());
                }

                @Test
                void shouldIncludeValueRightEyeWithCorrectionNumber() {
                    final var expectedNumber = 2.2;
                    final var syn = Syn.builder()
                        .setHogerOga(
                            Synskarpevarden.builder()
                                .setMedKorrektion(expectedNumber)
                                .build()
                        )
                        .build();
                    final var question = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(expectedNumber, value.getRightEye().getWithCorrection().getValue());
                }

                @Test
                void shouldIncludeValueRightEyeBinocularId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(KONTAKTLINSER_HOGER_OGA_DELSVAR_JSON_ID, value.getRightEye().getContactLenses().getId());
                }

                @Test
                void shouldIncludeValueRightEyeBinocularSelected() {
                    final var expectedNumber = true;
                    final var syn = Syn.builder()
                        .setHogerOga(
                            Synskarpevarden.builder()
                                .setKontaktlins(expectedNumber)
                                .build()
                        )
                        .build();
                    final var question = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(expectedNumber, value.getRightEye().getContactLenses().getSelected());
                }
            }

            @Nested
            class LeftEye {

                @Test
                void shouldIncludeValueLeftEye() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertNotNull(value.getLeftEye());
                }

                @Test
                void shouldIncludeValueLeftEyeWithoutCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(VANSTER_OGA_UTAN_KORREKTION_JSON_ID, value.getLeftEye().getWithoutCorrection().getId());
                }

                @Test
                void shouldIncludeValueLeftEyeWithoutCorrectionNumber() {
                    final var expectedNumber = 2.2;
                    final var syn = Syn.builder()
                        .setVansterOga(
                            Synskarpevarden.builder()
                                .setUtanKorrektion(expectedNumber)
                                .build()
                        )
                        .build();
                    final var question = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(expectedNumber, value.getLeftEye().getWithoutCorrection().getValue());
                }

                @Test
                void shouldIncludeValueLeftEyeWithCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(VANSTER_OGA_MED_KORREKTION_JSON_ID, value.getLeftEye().getWithCorrection().getId());
                }

                @Test
                void shouldIncludeValueLeftEyeWithCorrectionNumber() {
                    final var expectedNumber = 2.2;
                    final var syn = Syn.builder()
                        .setVansterOga(
                            Synskarpevarden.builder()
                                .setMedKorrektion(expectedNumber)
                                .build()
                        )
                        .build();
                    final var question = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(expectedNumber, value.getLeftEye().getWithCorrection().getValue());
                }

                @Test
                void shouldIncludeValueLeftEyeBinocularId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(KONTAKTLINSER_VANSTER_OGA_JSON_ID, value.getLeftEye().getContactLenses().getId());
                }

                @Test
                void shouldIncludeValueLeftEyeBinocularSelected() {
                    final var expectedNumber = true;
                    final var syn = Syn.builder()
                        .setVansterOga(
                            Synskarpevarden.builder()
                                .setKontaktlins(expectedNumber)
                                .build()
                        )
                        .build();
                    final var question = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(expectedNumber, value.getLeftEye().getContactLenses().getSelected());
                }
            }

            @Nested
            class Binocular {

                @Test
                void shouldIncludeValueBinocular() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertNotNull(value.getBinocular());
                }

                @Test
                void shouldIncludeValueBinocularWithoutCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(BINOKULART_UTAN_KORREKTION_JSON_ID, value.getBinocular().getWithoutCorrection().getId());
                }

                @Test
                void shouldIncludeValueBinocularWithoutCorrectionNumber() {
                    final var expectedNumber = 2.2;
                    final var syn = Syn.builder()
                        .setBinokulart(
                            Synskarpevarden.builder()
                                .setUtanKorrektion(expectedNumber)
                                .build()
                        )
                        .build();
                    final var question = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(expectedNumber, value.getBinocular().getWithoutCorrection().getValue());
                }

                @Test
                void shouldIncludeValueBinocularWithCorrectionId() {
                    final var question = QuestionSynskarpa.toCertificate(Syn.builder().build(), 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(BINOKULART_MED_KORREKTION_JSON_ID, value.getBinocular().getWithCorrection().getId());
                }

                @Test
                void shouldIncludeValueBinocularWithCorrectionNumber() {
                    final var expectedNumber = 2.2;
                    final var syn = Syn.builder()
                        .setBinokulart(
                            Synskarpevarden.builder()
                                .setMedKorrektion(expectedNumber)
                                .build()
                        )
                        .build();
                    final var question = QuestionSynskarpa.toCertificate(syn, 0, textProvider);
                    final var value = (CertificateDataValueVisualAcuities) question.getValue();
                    assertEquals(expectedNumber, value.getBinocular().getWithCorrection().getValue());
                }
            }
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return VARDEN_FOR_SYNSKARPA_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + BINOKULART_UTAN_KORREKTION_JSON_ID
                + ") && exists(" + VANSTER_OGA_UTAN_KORREKTION_JSON_ID
                + ") && exists(" + HOGER_OGA_UTAN_KORREKTION_JSON_ID + ")";
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSynskarpa.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationDisableTests extends ValidationDisableTest {

        @Override
        protected String getQuestionId() {
            return SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + SYNKARPA_SKICKAS_SEPARAT_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSynskarpa.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }
    }

    @Nested
    class ToInternal {

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class ToInternalRightEye {

            Stream<Syn> synStream() {
                return Stream.of(
                    Syn.builder()
                        .setHogerOga(
                            Synskarpevarden.builder()
                                .setKontaktlins(true)
                                .setMedKorrektion(2.0)
                                .setUtanKorrektion(2.0)
                                .build()
                        )
                        .build(),
                    Syn.builder()
                        .setHogerOga(
                            Synskarpevarden.builder().build()
                        )
                        .build(),
                    Syn.builder()
                        .setHogerOga(
                            Synskarpevarden.builder()
                                .setUtanKorrektion(null)
                                .setUtanKorrektion(null)
                                .setKontaktlins(null)
                                .build()
                        )
                        .build()
                );
            }

            @ParameterizedTest
            @MethodSource("synStream")
            void shouldIncludeValues(Syn expectedSyn) {
                final var certificate = CertificateBuilder.create()
                    .addElement(
                        QuestionSynskarpa.toCertificate(expectedSyn, 0, textProvider)
                    )
                    .build();
                final var synskarpevarden = QuestionSynskarpa.toInternal(certificate, RIGHT_EYE);
                assertEquals(expectedSyn.getHogerOga(), synskarpevarden);
            }
        }

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class ToInternalLeftEye {

            Stream<Syn> synStream() {
                return Stream.of(
                    Syn.builder()
                        .setVansterOga(
                            Synskarpevarden.builder()
                                .setKontaktlins(true)
                                .setMedKorrektion(2.0)
                                .setUtanKorrektion(2.0)
                                .build()
                        )
                        .build(),
                    Syn.builder()
                        .setVansterOga(
                            Synskarpevarden.builder().build()
                        )
                        .build(),
                    Syn.builder()
                        .setVansterOga(
                            Synskarpevarden.builder()
                                .setUtanKorrektion(null)
                                .setUtanKorrektion(null)
                                .setKontaktlins(null)
                                .build()
                        )
                        .build()
                );
            }

            @ParameterizedTest
            @MethodSource("synStream")
            void shouldIncludeValues(Syn expectedSyn) {
                final var certificate = CertificateBuilder.create()
                    .addElement(
                        QuestionSynskarpa.toCertificate(expectedSyn, 0, textProvider)
                    )
                    .build();
                final var synskarpevarden = QuestionSynskarpa.toInternal(certificate, LEFT_EYE);
                assertEquals(expectedSyn.getVansterOga(), synskarpevarden);
            }
        }

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class ToInternalBinocular {

            Stream<Syn> synStream() {
                return Stream.of(
                    Syn.builder()
                        .setBinokulart(
                            Synskarpevarden.builder()
                                .setMedKorrektion(2.0)
                                .setUtanKorrektion(2.0)
                                .build()
                        )
                        .build(),
                    Syn.builder()
                        .setBinokulart(
                            Synskarpevarden.builder().build()
                        )
                        .build(),
                    Syn.builder()
                        .setBinokulart(
                            Synskarpevarden.builder()
                                .setUtanKorrektion(null)
                                .setUtanKorrektion(null)
                                .build()
                        )
                        .build()
                );
            }

            @ParameterizedTest
            @MethodSource("synStream")
            void shouldIncludeValues(Syn expectedSyn) {
                final var certificate = CertificateBuilder.create()
                    .addElement(
                        QuestionSynskarpa.toCertificate(expectedSyn, 0, textProvider)
                    )
                    .build();
                final var synskarpevarden = QuestionSynskarpa.toInternal(certificate, BINOCULAR);
                assertEquals(expectedSyn.getBinokulart(), synskarpevarden);
            }

            @Test
            void shouldNotIncludeKontaktLins() {
                final var expectedSyn = Syn.builder()
                    .setBinokulart(
                        Synskarpevarden.builder()
                            .setKontaktlins(true)
                            .build()
                    )
                    .build();
                final var certificate = CertificateBuilder.create()
                    .addElement(
                        QuestionSynskarpa.toCertificate(expectedSyn, 0, textProvider)
                    )
                    .build();

                final var synskarpevarden = QuestionSynskarpa.toInternal(certificate, BINOCULAR);

                assertNull(synskarpevarden.getKontaktlins());
            }
        }
    }
}