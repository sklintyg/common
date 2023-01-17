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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR11_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR12_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR13_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR14_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR15_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR16_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR17_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR18_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR1_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR2_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR3_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR4_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR5_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR6_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR7_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR8_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_KORKORTSBEHORIGHET_VAR9_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_TEXT_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationDisableSubElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeListTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.BedomningKorkortstyp;

@ExtendWith(MockitoExtension.class)
class QuestionBedomningUppfyllerBehorighetskravTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        doReturn("Text!").when(textProvider).get(anyString());
    }

    @Nested
    class IncludeCommonElementTest extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningUppfyllerBehorighetskrav.toCertificate(null, getIndex(), textProvider);
        }

        @Override
        protected String getId() {
            return BEDOMNING_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return BEDOMNING_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 3;
        }
    }

    @Nested
    class IncludeConfigCheckboxMultipleCodeTest extends ConfigCheckboxMultipleCodeTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningUppfyllerBehorighetskrav.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DESCRIPTION_ID;
        }

        @Override
        protected Layout getLayout() {
            return Layout.INLINE;
        }

        @Override
        protected List<CheckboxMultipleCode> getExpectedListOfCodes() {
            return List.of(
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR12.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR12_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR13.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR13_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR14.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR14_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR15.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR15_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR16.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR16_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR17.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR17_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR18.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR18_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR1.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR1_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR2.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR2_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR3.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR3_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR4.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR4_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR5.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR5_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR6.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR6_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR7.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR7_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR8.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR8_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR9.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR9_LABEL_ID))
                    .build(),
                CheckboxMultipleCode.builder()
                    .id(BedomningKorkortstyp.VAR11.getType())
                    .label(textProvider.get(BEDOMNING_KORKORTSBEHORIGHET_VAR11_LABEL_ID))
                    .build()
           );
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeValueCodeListTest extends ValueCodeListTest<Set<BedomningKorkortstyp>> {

        @Override
        protected CertificateDataElement getElement(Set<BedomningKorkortstyp> input) {
            final var bedomning = Bedomning.builder().setUppfyllerBehorighetskrav(input).build();
            return QuestionBedomningUppfyllerBehorighetskrav.toCertificate(bedomning, 0, textProvider);
        }

        @Override
        protected List<InputExpectedValuePair<Set<BedomningKorkortstyp>, CertificateDataValueCodeList>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(
                    null,
                    CertificateDataValueCodeList.builder().list(Collections.emptyList()).build()
                ),
                new InputExpectedValuePair<>(
                    EnumSet.noneOf(BedomningKorkortstyp.class),
                    CertificateDataValueCodeList.builder().list(Collections.emptyList()).build()
                ),
                new InputExpectedValuePair<>(
                    EnumSet.of(
                        BedomningKorkortstyp.VAR3, BedomningKorkortstyp.VAR2, BedomningKorkortstyp.VAR1, BedomningKorkortstyp.VAR12
                    ),
                    CertificateDataValueCodeList.builder().list(
                        List.of(
                            CertificateDataValueCode.builder()
                                .id(BedomningKorkortstyp.VAR12.getType())
                                .code(BedomningKorkortstyp.VAR12.getType())
                                .build(),
                            CertificateDataValueCode.builder()
                                .id(BedomningKorkortstyp.VAR1.getType())
                                .code(BedomningKorkortstyp.VAR1.getType())
                                .build(),
                            CertificateDataValueCode.builder()
                                .id(BedomningKorkortstyp.VAR2.getType())
                                .code(BedomningKorkortstyp.VAR2.getType())
                                .build(),
                            CertificateDataValueCode.builder()
                                .id(BedomningKorkortstyp.VAR3.getType())
                                .code(BedomningKorkortstyp.VAR3.getType())
                                .build()
                        ))
                        .build()
                ));
        }
    }

    @Nested
    class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningUppfyllerBehorighetskrav.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }

        @Override
        protected String getQuestionId() {
            return BEDOMNING_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return BedomningKorkortstyp.VAR12.getType() + " || " + BedomningKorkortstyp.VAR13.getType()
                + " || " + BedomningKorkortstyp.VAR14.getType() + " || " + BedomningKorkortstyp.VAR15.getType()
                + " || " + BedomningKorkortstyp.VAR16.getType() + " || " + BedomningKorkortstyp.VAR17.getType()
                + " || " + BedomningKorkortstyp.VAR18.getType() + " || " + BedomningKorkortstyp.VAR1.getType()
                + " || " + BedomningKorkortstyp.VAR2.getType() + " || " + BedomningKorkortstyp.VAR3.getType()
                + " || " + BedomningKorkortstyp.VAR4.getType() + " || " + BedomningKorkortstyp.VAR5.getType()
                + " || " + BedomningKorkortstyp.VAR6.getType() + " || " + BedomningKorkortstyp.VAR7.getType()
                + " || " + BedomningKorkortstyp.VAR8.getType() + " || " + BedomningKorkortstyp.VAR9.getType()
                + " || " + BedomningKorkortstyp.VAR11.getType();
        }
    }

    @Nested
    class IncludeValidationDisableSubElementTest extends ValidationDisableSubElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningUppfyllerBehorighetskrav.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected String getQuestionId() {
            return BEDOMNING_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return BedomningKorkortstyp.VAR12.getType() + " || " + BedomningKorkortstyp.VAR13.getType()
                + " || " + BedomningKorkortstyp.VAR14.getType() + " || " + BedomningKorkortstyp.VAR15.getType()
                + " || " + BedomningKorkortstyp.VAR16.getType() + " || " + BedomningKorkortstyp.VAR17.getType()
                + " || " + BedomningKorkortstyp.VAR18.getType() + " || " + BedomningKorkortstyp.VAR1.getType()
                + " || " + BedomningKorkortstyp.VAR2.getType() + " || " + BedomningKorkortstyp.VAR3.getType()
                + " || " + BedomningKorkortstyp.VAR4.getType() + " || " + BedomningKorkortstyp.VAR5.getType()
                + " || " + BedomningKorkortstyp.VAR6.getType() + " || " + BedomningKorkortstyp.VAR7.getType()
                + " || " + BedomningKorkortstyp.VAR8.getType() + " || " + BedomningKorkortstyp.VAR9.getType();
        }

        @Override
        protected List<String> getListOfIds() {
            return List.of(BedomningKorkortstyp.VAR11.getType());
        }
    }

    @Nested
    class IncludeValidationDisableSubElementTest2 extends ValidationDisableSubElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionBedomningUppfyllerBehorighetskrav.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 2;
        }

        @Override
        protected String getQuestionId() {
            return BEDOMNING_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + BedomningKorkortstyp.VAR11.getType();
        }

        @Override
        protected List<String> getListOfIds() {
            return List.of(BedomningKorkortstyp.VAR12.getType(), BedomningKorkortstyp.VAR13.getType(), BedomningKorkortstyp.VAR14.getType(),
                BedomningKorkortstyp.VAR15.getType(), BedomningKorkortstyp.VAR16.getType(), BedomningKorkortstyp.VAR17.getType(),
                BedomningKorkortstyp.VAR18.getType(), BedomningKorkortstyp.VAR1.getType(), BedomningKorkortstyp.VAR2.getType(),
                BedomningKorkortstyp.VAR3.getType(), BedomningKorkortstyp.VAR4.getType(), BedomningKorkortstyp.VAR5.getType(),
                BedomningKorkortstyp.VAR6.getType(), BedomningKorkortstyp.VAR7.getType(), BedomningKorkortstyp.VAR8.getType(),
                BedomningKorkortstyp.VAR9.getType());
        }
    }

    @Nested
    class ToInternal {

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        class IncludeInternalValuePairTest extends InternalValueTest<Bedomning, Set<BedomningKorkortstyp>> {

            @Override
            protected CertificateDataElement getElement(Bedomning input) {
                return QuestionBedomningUppfyllerBehorighetskrav.toCertificate(input, 0, textProvider);
            }

            @Override
            protected Set<BedomningKorkortstyp> toInternalValue(Certificate certificate) {
                return QuestionBedomningUppfyllerBehorighetskrav.toInternal(certificate);
            }

            @Override
            protected List<InputExpectedValuePair<Bedomning, Set<BedomningKorkortstyp>>> inputExpectedValuePairList() {
                return List.of(
                    new InputExpectedValuePair<>(null, Collections.emptySet()),
                    new InputExpectedValuePair<>(Bedomning.builder().build(), Collections.emptySet()),
                    new InputExpectedValuePair<>(
                        Bedomning.builder().setUppfyllerBehorighetskrav(EnumSet.noneOf(BedomningKorkortstyp.class)).build(),
                        Collections.emptySet()
                    ),
                    new InputExpectedValuePair<>(
                        Bedomning.builder()
                            .setUppfyllerBehorighetskrav(EnumSet.of(BedomningKorkortstyp.VAR3, BedomningKorkortstyp.VAR2,
                                BedomningKorkortstyp.VAR1)).build(),
                            Set.of(BedomningKorkortstyp.VAR3, BedomningKorkortstyp.VAR2, BedomningKorkortstyp.VAR1)
                    )
                );
            }
        }

        @Test
        void shouldHandleCodeWithEmptyStringValues() {
            final var bedomning = Bedomning.builder().build();
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionBedomningUppfyllerBehorighetskrav.toCertificate(bedomning, 0, textProvider))
                .build();

            certificate.getData().put(BEDOMNING_SVAR_ID, CertificateDataElement.builder()
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

            final var actualValue = QuestionBedomningUppfyllerBehorighetskrav.toInternal(certificate);

            assertTrue(Objects.requireNonNull(actualValue).isEmpty());
        }
    }
}
