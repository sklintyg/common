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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_ANNAN_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_LADA_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_TYP1_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_TYP2_LABEL_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueCodeTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;

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
            return QuestionDiabetesTyp.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getId() {
            return ALLMANT_TYP_AV_DIABETES_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return ALLMANT_CATEGORY_ID;
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
                    .id(KvTypAvDiabetes.TYP1.getCode())
                    .label(textProvider.get(ALLMANT_TYP_AV_DIABETES_TYP1_LABEL_ID))
                    .build(),
                RadioMultipleCode.builder()
                    .id(KvTypAvDiabetes.TYP2.getCode())
                    .label(textProvider.get(ALLMANT_TYP_AV_DIABETES_TYP2_LABEL_ID))
                    .build(),
                RadioMultipleCode.builder()
                    .id(KvTypAvDiabetes.LADA.getCode())
                    .label(textProvider.get(ALLMANT_TYP_AV_DIABETES_LADA_LABEL_ID))
                    .build(),
                RadioMultipleCode.builder()
                    .id(KvTypAvDiabetes.ANNAN.getCode())
                    .label(textProvider.get(ALLMANT_TYP_AV_DIABETES_ANNAN_LABEL_ID))
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
            return QuestionDiabetesTyp.toCertificate(null, 0, textProvider);
        }

        @Override
        protected String getTextId() {
            return ALLMANT_TYP_AV_DIABETES_TEXT_ID;
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
            final var allmant = Allmant.builder().setTypAvDiabetes(KvTypAvDiabetes.TYP1).build();
            return QuestionDiabetesTyp.toCertificate(allmant, 0, textProvider);
        }

        @Override
        protected String getCodeId() {
            return KvTypAvDiabetes.TYP1.getCode();
        }

        @Override
        protected String getCode() {
            return KvTypAvDiabetes.TYP1.getCode();
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return ALLMANT_TYP_AV_DIABETES_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return KvTypAvDiabetes.TYP1.getCode() + " || " + KvTypAvDiabetes.TYP2.getCode() + " || " + KvTypAvDiabetes.LADA.getCode()
                + " || " + KvTypAvDiabetes.ANNAN.getCode();
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
    class ToInternal {

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        class IncludeInternalValuePairTest extends InternalValueTest<Allmant, KvTypAvDiabetes> {

            @Override
            protected CertificateDataElement getElement(Allmant input) {
                return QuestionDiabetesTyp.toCertificate(input, 0, textProvider);
            }

            @Override
            protected KvTypAvDiabetes toInternalValue(Certificate certificate) {
                return QuestionDiabetesTyp.toInternal(certificate);
            }

            @Override
            protected List<InputExpectedValuePair<Allmant, KvTypAvDiabetes>> inputExpectedValuePairList() {
                return List.of(
                    new InputExpectedValuePair<>(null, null),
                    new InputExpectedValuePair<>(Allmant.builder().setTypAvDiabetes(null).build(), null),
                    new InputExpectedValuePair<>(
                        Allmant.builder().setTypAvDiabetes(KvTypAvDiabetes.TYP1).build(),
                        KvTypAvDiabetes.TYP1
                    ),
                    new InputExpectedValuePair<>(
                        Allmant.builder().setTypAvDiabetes(KvTypAvDiabetes.TYP2).build(),
                        KvTypAvDiabetes.TYP2
                    ),
                    new InputExpectedValuePair<>(
                        Allmant.builder().setTypAvDiabetes(KvTypAvDiabetes.LADA).build(),
                        KvTypAvDiabetes.LADA
                    ),
                    new InputExpectedValuePair<>(
                        Allmant.builder().setTypAvDiabetes(KvTypAvDiabetes.ANNAN).build(),
                        KvTypAvDiabetes.ANNAN
                    )
                );
            }
        }

        @Test
        void shouldHandleCodeWithEmptyStringValues() {
            final var certificate = CertificateBuilder.create()
                .addElement(QuestionDiabetesTyp.toCertificate(null, 0, textProvider))
                .build();

            certificate.getData().put(ALLMANT_TYP_AV_DIABETES_SVAR_ID, CertificateDataElement.builder()
                .value(
                    CertificateDataValueCode.builder()
                        .id("")
                        .code("")
                        .build()
                )
                .build()
            );

            final var actualValue = QuestionDiabetesTyp.toInternal(certificate);

            assertNull(actualValue);
        }
    }
}