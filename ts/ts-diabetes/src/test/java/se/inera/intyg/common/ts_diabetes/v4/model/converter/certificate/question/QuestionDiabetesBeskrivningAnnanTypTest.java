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

package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigTextFieldTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationShowTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalTextValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;

@ExtendWith(MockitoExtension.class)
class QuestionDiabetesBeskrivningAnnanTypTest {

    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setup() {
        when(textProvider.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Nested
        class IncludeTestCommon extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiabetesBeskrivningAnnanTyp.toCertificate(null, getIndex(), textProvider);
            }

            @Override
            protected String getId() {
                return ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID;
            }

            @Override
            protected String getParent() {
                return ALLMANT_TYP_AV_DIABETES_SVAR_ID;
            }

            @Override
            protected int getIndex() {
                return 3;
            }
        }

        @Nested
        class IncludeConfigTextFieldTest extends ConfigTextFieldTest {

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return textProvider;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiabetesBeskrivningAnnanTyp.toCertificate(null, 0, getTextProviderMock());
            }

            @Override
            protected String getTextId() {
                return ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return null;
            }

            @Override
            protected String getJsonId() {
                return ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
            }
        }

        @Nested
        class IncludeValueTextTest extends ValueTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiabetesBeskrivningAnnanTyp.toCertificate(
                    Allmant.builder().setBeskrivningAnnanTypAvDiabetes(getText()).build(), 0,
                    textProvider);
            }

            @Override
            protected String getJsonId() {
                return ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
            }

            @Override
            protected String getText() {
                return "Här är en text";
            }
        }

        @Nested
        class IncludeValidationTextTest extends ValidationTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiabetesBeskrivningAnnanTyp.toCertificate(null, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 0;
            }

            @Override
            protected short getLimit() {
                return 53;
            }
        }

        @Nested
        class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiabetesBeskrivningAnnanTyp.toCertificate(null, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
            }

            @Override
            protected String getQuestionId() {
                return ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID;
            }

            @Override
            protected String getExpression() {
                return "$" + ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
            }
        }

        @Nested
        class IncludeValidationShowTest extends ValidationShowTest {

            @Override
            protected String getQuestionId() {
                return ALLMANT_TYP_AV_DIABETES_SVAR_ID;
            }

            @Override
            protected String getExpression() {
                return "$" + KvTypAvDiabetes.ANNAN.getCode();
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiabetesBeskrivningAnnanTyp.toCertificate(null, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 2;
            }
        }
    }

    @Nested
    class ToInternal {

        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class IncludeInternalTextValue extends InternalTextValueTest {

            @Override
            protected CertificateDataElement getElement(String expectedValue) {
                return QuestionDiabetesBeskrivningAnnanTyp.toCertificate(
                    Allmant.builder().setBeskrivningAnnanTypAvDiabetes(expectedValue).build(), 0, textProvider);
            }

            @Override
            protected String toInternalTextValue(Certificate certificate) {
                return QuestionDiabetesBeskrivningAnnanTyp.toInternal(certificate);
            }
        }
    }
}
