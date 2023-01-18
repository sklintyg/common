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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_TEXT_ID;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigYearTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMaxYearTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMinYearTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalTextValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;

@ExtendWith(MockitoExtension.class)
class QuestionDiabetesDiagnosArTest {

    @Mock
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesDiagnosAr.toCertificate(null, null, 0, texts);
        }

        @Override
        protected String getId() {
            return ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
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
    class IncludeConfigYearTests extends ConfigYearTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesDiagnosAr.toCertificate(null, null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return ALLMANT_DIABETES_DIAGNOS_AR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected String getJsonId() {
            return ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
        }
    }

    @Nested
    class IncludeValueTextTests extends ValueTextTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesDiagnosAr.toCertificate(Allmant.builder().setDiabetesDiagnosAr("2022").build(), null, 0, texts);
        }

        @Override
        protected String getJsonId() {
            return ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
        }

        @Override
        protected String getText() {
            return "2022";
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesDiagnosAr.toCertificate(null, null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationMaxYearTests extends ValidationMaxYearTest {

        @Override
        protected String getId() {
            return ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
        }

        @Override
        protected short getYearsInFuture() {
            return 0;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesDiagnosAr.toCertificate(null, null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }
    }

    @Nested
    class IncludeValidationMinYearTests extends ValidationMinYearTest {

        @Override
        protected String getId() {
            return ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
        }

        @Override
        protected short getYearsInPast() {
            return 10;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesDiagnosAr.toCertificate(null, LocalDate.now().minusYears(10).toString(), 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 2;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalTextValueTests extends InternalTextValueTest {

        @Override
        protected CertificateDataElement getElement(String expectedValue) {
            return QuestionDiabetesDiagnosAr.toCertificate(Allmant.builder().setDiabetesDiagnosAr(expectedValue).build(), null, 0, texts);
        }

        @Override
        protected String toInternalTextValue(Certificate certificate) {
            return QuestionDiabetesDiagnosAr.toInternal(certificate);
        }
    }
}
