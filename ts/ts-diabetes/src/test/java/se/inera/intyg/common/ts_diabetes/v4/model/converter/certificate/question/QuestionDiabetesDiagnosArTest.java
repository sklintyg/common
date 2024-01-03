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

package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_TEXT_ID;

import java.time.LocalDate;
import java.util.List;
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
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueYearTest;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class QuestionDiabetesDiagnosArTest {

    @Mock
    private CertificateTextProvider texts;
    private Personnummer patientId = Personnummer.createPersonnummer("19121212-1212").get();

    @BeforeEach
    void setup() {
        when(texts.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class IncludeCommonElementTests extends CommonElementTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesDiagnosAr.toCertificate(null, patientId, 0, texts);
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
            return QuestionDiabetesDiagnosAr.toCertificate(null, patientId, 0, texts);
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

        @Override
        protected Integer getMinYear() {
            return 1912;
        }

        @Override
        protected Integer getMaxYear() {
            return LocalDate.now().getYear();
        }
    }

    @Nested
    class IncludeValueYearTests extends ValueYearTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiabetesDiagnosAr.toCertificate(Allmant.builder().setDiabetesDiagnosAr("2022").build(), patientId, 0, texts);
        }

        @Override
        protected String getJsonId() {
            return ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
        }

        @Override
        protected Integer getYear() {
            return 2022;
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
            return QuestionDiabetesDiagnosAr.toCertificate(null, patientId, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalTextValueTests extends InternalValueTest<String, String> {

        @Override
        protected CertificateDataElement getElement(String input) {
            return QuestionDiabetesDiagnosAr.toCertificate(Allmant.builder().setDiabetesDiagnosAr(input).build(), patientId, 0, texts);
        }

        @Override
        protected String toInternalValue(Certificate certificate) {
            return QuestionDiabetesDiagnosAr.toInternal(certificate);
        }

        @Override
        protected List<InputExpectedValuePair<String, String>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(null, null),
                new InputExpectedValuePair<>("1912", "1912")
            );
        }
    }


}
