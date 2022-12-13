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

package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV10_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV1_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV2_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV3_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV4_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV5_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV6_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV7_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV8_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_IAV9_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INTYG_AVSER_SVAR_TEXT_ID;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxMultipleCodeTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCheckboxMultipleCodeTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvserKategori;

@ExtendWith(MockitoExtension.class)
class QuestionIntygetAvserTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Nested
        class IncludeCommonElementTest extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionIntygetAvser.toCertificate(null, 0, texts);
            }

            @Override
            protected String getId() {
                return INTYG_AVSER_SVAR_ID_1;
            }

            @Override
            protected String getParent() {
                return INTYG_AVSER_CATEGORY_ID;
            }

            @Override
            protected int getIndex() {
                return 0;
            }
        }

        @Nested
        class IncludeConfigCheckboxMultipleCodeTest extends ConfigCheckboxMultipleCodeTest {

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return texts;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionIntygetAvser.toCertificate(null, 0, texts);
            }

            @Override
            protected String getTextId() {
                return INTYG_AVSER_SVAR_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return INTYG_AVSER_SVAR_DESCRIPTION_ID;
            }

            @Override
            protected List<CheckboxMultipleCode> getListOfCodes() {
                return List.of(
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV1.name())
                        .label(texts.get(INTYG_AVSER_IAV1_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV2.name())
                        .label(texts.get(INTYG_AVSER_IAV2_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV3.name())
                        .label(texts.get(INTYG_AVSER_IAV3_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV4.name())
                        .label(texts.get(INTYG_AVSER_IAV4_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV5.name())
                        .label(texts.get(INTYG_AVSER_IAV5_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV6.name())
                        .label(texts.get(INTYG_AVSER_IAV6_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV7.name())
                        .label(texts.get(INTYG_AVSER_IAV7_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV8.name())
                        .label(texts.get(INTYG_AVSER_IAV8_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV9.name())
                        .label(texts.get(INTYG_AVSER_IAV9_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.IAV10.name())
                        .label(texts.get(INTYG_AVSER_IAV10_LABEL_ID))
                        .build());
            }

            @Override
            protected Integer getRows() {
                return 2;
            }

            @Override
            protected Integer getColumns() {
                return 0;
            }
        }

        @Nested
        class IncludeValueCheckboxMultipleCodeTest extends ValueCheckboxMultipleCodeTest {


            @Override
            protected CertificateDataElement getElement() {
                return QuestionIntygetAvser.toCertificate(null, 0, texts);
            }

            @Override
            protected CertificateDataElement getElementWithValues() {
                return QuestionIntygetAvser.toCertificate(Set.of(
                    IntygAvserKategori.IAV3, IntygAvserKategori.IAV2, IntygAvserKategori.IAV1), 0, texts);
            }

            @Override
            protected List<Object> getValues() {
                return List.of(
                    CertificateDataValueCode.builder()
                        .id(IntygAvserKategori.IAV1.name())
                        .code(IntygAvserKategori.IAV1.name())
                        .build(),
                    CertificateDataValueCode.builder()
                        .id(IntygAvserKategori.IAV2.name())
                        .code(IntygAvserKategori.IAV2.name())
                        .build(),
                    CertificateDataValueCode.builder()
                        .id(IntygAvserKategori.IAV3.name())
                        .code(IntygAvserKategori.IAV3.name())
                        .build()
                );
            }
        }

        @Nested
        class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

            @Override
            protected String getQuestionId() {
                return INTYG_AVSER_SVAR_ID_1;
            }

            @Override
            protected String getExpression() {
                return IntygAvserKategori.IAV1.name() + " || " + IntygAvserKategori.IAV2.name() + " || "
                    + IntygAvserKategori.IAV3.name() + " || " + IntygAvserKategori.IAV4.name() + " || "
                    + IntygAvserKategori.IAV5.name() + " || " + IntygAvserKategori.IAV6.name() + " || "
                    + IntygAvserKategori.IAV7.name() + " || " + IntygAvserKategori.IAV8.name() + " || "
                    + IntygAvserKategori.IAV9.name() + " || " + IntygAvserKategori.IAV10.name();
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionIntygetAvser.toCertificate(null, 0, texts);
            }

            @Override
            protected int getValidationIndex() {
                return 0;
            }
        }
    }

    @Nested
    class ToInternal {

        @Test
        void shouldIncludeKorkortsTyp() {
            final Set<IntygAvserKategori> expectedValue = Set.of(IntygAvserKategori.IAV1, IntygAvserKategori.IAV2,
                IntygAvserKategori.IAV3, IntygAvserKategori.IAV4, IntygAvserKategori.IAV5);

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionIntygetAvser.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionIntygetAvser.toInternal(certificate);

            assertEquals(expectedValue, actualValue.getKorkortstyp());
        }

        @Test
        void shouldHandleEmptyValues() {
            final Set<IntygAvserKategori> expectedValue = Set.of();

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionIntygetAvser.toCertificate(expectedValue, 0, texts))
                .build();

            final var actualValue = QuestionIntygetAvser.toInternal(certificate);

            assertEquals(expectedValue, actualValue.getKorkortstyp());
        }

        @Test
        void shouldHandleCodeWithEmptyStringValues() {
            final Set<IntygAvserKategori> expectedValue = Set.of();

            final var certificate = CertificateBuilder.create()
                .addElement(QuestionIntygetAvser.toCertificate(expectedValue, 0, texts))
                .build();

            certificate.getData().put(INTYG_AVSER_SVAR_ID_1, CertificateDataElement.builder()
                .value(
                    CertificateDataValueCodeList.builder()
                        .list(
                            List.of(
                                CertificateDataValueCode.builder()
                                    .id("")
                                    .code("")
                                    .build(),
                                CertificateDataValueCode.builder()
                                    .id("")
                                    .code("")
                                    .build()
                            )
                        )
                        .build()
                )
                .build()
            );

            final var actualValue = QuestionIntygetAvser.toInternal(certificate);

            assertTrue(actualValue.getKorkortstyp().isEmpty());
        }
    }
}