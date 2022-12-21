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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_TYP1_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_TYP2_LABEL_ID;

import java.util.List;
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
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigRadioMultipleCodeTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeTest;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;

@ExtendWith(MockitoExtension.class)
class QuestionDiabetesTypTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setup() {
        when(textProvider.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesTyp.toCertificate(Diabetes.builder().build(), 0, textProvider);
        }

        @Override
        protected String getId() {
            return TYP_AV_DIABETES_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return HAR_DIABETES_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigRadioButtonMultipleCodeTests extends ConfigRadioMultipleCodeTest {

        @Override
        protected List<RadioMultipleCode> getExpectedRadioMultipleCodes() {
            return List.of(
                RadioMultipleCode.builder()
                    .id(DiabetesKod.DIABETES_TYP_1.name())
                    .label(textProvider.get(TYP_AV_DIABETES_SVAR_TYP1_LABEL_ID))
                    .build(),
                RadioMultipleCode.builder()
                    .id(DiabetesKod.DIABETES_TYP_2.name())
                    .label(textProvider.get(TYP_AV_DIABETES_SVAR_TYP2_LABEL_ID))
                    .build()
            );
        }

        @Override
        protected Layout getExpectedLayout() {
            return Layout.ROWS;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return textProvider;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesTyp.toCertificate(Diabetes.builder().build(), 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return TYP_AV_DIABETES_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    class IncludeValueCodeTests extends ValueCodeTest {


        @Override
        protected CertificateDataElement getElement() {
            final var diabetes = Diabetes.builder().setDiabetesTyp(DiabetesKod.DIABETES_TYP_1.name()).build();
            return QuestionDiabetesTyp.toCertificate(diabetes, 0, textProvider);
        }

        @Override
        protected String getCodeId() {
            return DiabetesKod.DIABETES_TYP_1.name();
        }

        @Override
        protected String getCode() {
            return DiabetesKod.DIABETES_TYP_1.name();
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return TYP_AV_DIABETES_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return DiabetesKod.DIABETES_TYP_1.name() + " || " + DiabetesKod.DIABETES_TYP_2.name();
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesTyp.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationShowTests extends ValidationShowTest {

        @Override
        protected String getQuestionId() {
            return HAR_DIABETES_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + HAR_DIABETES_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesTyp.toCertificate(null, 0, textProvider);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }
    }

    @Nested
    class ToInternal {

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        class IncludeInternalValuePairTest extends InternalValueTest<Diabetes, String> {

            @Override
            protected CertificateDataElement getElement(Diabetes input) {
                return QuestionDiabetesTyp.toCertificate(input, 0, textProvider);
            }

            @Override
            protected String toInternalValue(Certificate certificate) {
                return QuestionDiabetesTyp.toInternal(certificate);
            }

            @Override
            protected List<InputExpectedValuePair<Diabetes, String>> inputExpectedValuePairList() {
                return List.of(
                    new InputExpectedValuePair(null, null),
                    new InputExpectedValuePair(Diabetes.builder().setDiabetesTyp(null).build(), null),
                    new InputExpectedValuePair(
                        Diabetes.builder().setDiabetesTyp(DiabetesKod.DIABETES_TYP_1.name()).build(),
                        DiabetesKod.DIABETES_TYP_1.name()
                    ),
                    new InputExpectedValuePair(
                        Diabetes.builder().setDiabetesTyp(DiabetesKod.DIABETES_TYP_2.name()).build(),
                        DiabetesKod.DIABETES_TYP_2.name()
                    )
                );
            }
        }

        @Test
        void shouldHandleCodeWithEmptyStringValues() {
            final var expectedValue = Diabetes.builder().setDiabetesTyp(DiabetesKod.DIABETES_TYP_1.name()).build();
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDiabetesTyp.toCertificate(expectedValue, 0, textProvider))
                .build();

            certificate.getData().put(TYP_AV_DIABETES_SVAR_ID, CertificateDataElement.builder()
                .value(
                    CertificateDataValueCode.builder()
                        .id("")
                        .code("")
                        .build()
                )
                .build()
            );

            final var actualValue = QuestionDiabetesTyp.toInternal(certificate);

            assertTrue(actualValue.isEmpty());
        }
    }
}