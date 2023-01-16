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

package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR12_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR13_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR14_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR15_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR16_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR17_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR18_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR1_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR2_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR3_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR4_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR5_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR6_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR7_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR8_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_VAR9_LABEL_ID;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxMultipleCodeTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeListTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;

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
                return INTYG_AVSER_SVAR_ID;
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
            protected List<CheckboxMultipleCode> getExpectedListOfCodes() {
                return List.of(
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR12.name())
                        .label(texts.get(INTYG_AVSER_VAR12_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR13.name())
                        .label(texts.get(INTYG_AVSER_VAR13_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR14.name())
                        .label(texts.get(INTYG_AVSER_VAR14_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR15.name())
                        .label(texts.get(INTYG_AVSER_VAR15_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR16.name())
                        .label(texts.get(INTYG_AVSER_VAR16_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR17.name())
                        .label(texts.get(INTYG_AVSER_VAR17_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR18.name())
                        .label(texts.get(INTYG_AVSER_VAR18_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR1.name())
                        .label(texts.get(INTYG_AVSER_VAR1_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR2.name())
                        .label(texts.get(INTYG_AVSER_VAR2_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR3.name())
                        .label(texts.get(INTYG_AVSER_VAR3_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR4.name())
                        .label(texts.get(INTYG_AVSER_VAR4_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR5.name())
                        .label(texts.get(INTYG_AVSER_VAR5_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR6.name())
                        .label(texts.get(INTYG_AVSER_VAR6_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR7.name())
                        .label(texts.get(INTYG_AVSER_VAR7_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR8.name())
                        .label(texts.get(INTYG_AVSER_VAR8_LABEL_ID))
                        .build(),
                    CheckboxMultipleCode.builder()
                        .id(IntygAvserKategori.VAR9.name())
                        .label(texts.get(INTYG_AVSER_VAR9_LABEL_ID))
                        .build()
                );
            }

            @Override
            protected Layout getLayout() {
                return Layout.INLINE;
            }

        }

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        class IncludeValueCodeListTest extends ValueCodeListTest<IntygAvser> {

            @Override
            protected CertificateDataElement getElement(IntygAvser input) {
                return QuestionIntygetAvser.toCertificate(input, 0, texts);
            }

            @Override
            protected List<InputExpectedValuePair<IntygAvser, CertificateDataValueCodeList>> inputExpectedValuePairList() {
                return List.of(
                    new InputExpectedValuePair<>(null, CertificateDataValueCodeList.builder().list(Collections.emptyList()).build()),
                    new InputExpectedValuePair<>(IntygAvser.create(null),
                        CertificateDataValueCodeList.builder()
                            .list(Collections.emptyList())
                            .build()),
                    new InputExpectedValuePair<>(IntygAvser.create(
                        EnumSet.copyOf(Set.of(IntygAvserKategori.VAR3, IntygAvserKategori.VAR2, IntygAvserKategori.VAR1))),
                        CertificateDataValueCodeList.builder().list(
                            List.of(
                                CertificateDataValueCode.builder()
                                    .id(IntygAvserKategori.VAR1.name())
                                    .code(IntygAvserKategori.VAR1.name())
                                    .build(),
                                CertificateDataValueCode.builder()
                                    .id(IntygAvserKategori.VAR2.name())
                                    .code(IntygAvserKategori.VAR2.name())
                                    .build(),
                                CertificateDataValueCode.builder()
                                    .id(IntygAvserKategori.VAR3.name())
                                    .code(IntygAvserKategori.VAR3.name())
                                    .build()
                            ))
                            .build()
                    ));
            }
        }

        @Nested
        class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

            @Override
            protected String getQuestionId() {
                return INTYG_AVSER_SVAR_ID;
            }

            @Override
            protected String getExpression() {
                return IntygAvserKategori.VAR12.name() + " || " + IntygAvserKategori.VAR13.name() + " || "
                    + IntygAvserKategori.VAR14.name() + " || " + IntygAvserKategori.VAR15.name() + " || "
                    + IntygAvserKategori.VAR16.name() + " || " + IntygAvserKategori.VAR17.name() + " || "
                    + IntygAvserKategori.VAR18.name() + " || " + IntygAvserKategori.VAR1.name() + " || "
                    + IntygAvserKategori.VAR2.name() + " || " + IntygAvserKategori.VAR3.name() + " || "
                    + IntygAvserKategori.VAR4.name() + " || " + IntygAvserKategori.VAR5.name() + " || "
                    + IntygAvserKategori.VAR6.name() + " || " + IntygAvserKategori.VAR7.name() + " || "
                    + IntygAvserKategori.VAR8.name() + " || " + IntygAvserKategori.VAR9.name();
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

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        class IncludeInternalValuePairTest extends InternalValueTest<IntygAvser, IntygAvser> {

            @Override
            protected CertificateDataElement getElement(IntygAvser input) {
                return QuestionIntygetAvser.toCertificate(input, 0, texts);
            }

            @Override
            protected IntygAvser toInternalValue(Certificate certificate) {
                return QuestionIntygetAvser.toInternal(certificate);
            }

            @Override
            protected List<InputExpectedValuePair<IntygAvser, IntygAvser>> inputExpectedValuePairList() {
                return List.of(
                    new InputExpectedValuePair<>(null, IntygAvser.create(EnumSet.noneOf(IntygAvserKategori.class))),
                    new InputExpectedValuePair<>(IntygAvser.create(null), IntygAvser.create(EnumSet.noneOf(IntygAvserKategori.class))),
                    new InputExpectedValuePair<>(
                        IntygAvser.create(
                            EnumSet.copyOf(Set.of(IntygAvserKategori.VAR3, IntygAvserKategori.VAR2, IntygAvserKategori.VAR1))),
                        IntygAvser.create(
                            EnumSet.copyOf(Set.of(IntygAvserKategori.VAR3, IntygAvserKategori.VAR2, IntygAvserKategori.VAR1)))
                    )
                );
            }
        }

        @Test
        void shouldHandleCodeWithEmptyStringValues() {
            final var expectedValue = IntygAvser.create(
                EnumSet.noneOf(IntygAvserKategori.class));
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionIntygetAvser.toCertificate(expectedValue, 0, texts))
                .build();

            certificate.getData().put(INTYG_AVSER_SVAR_ID, CertificateDataElement.builder()
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

            assertTrue(Objects.requireNonNull(actualValue.getKategorier()).isEmpty());
        }
    }
}