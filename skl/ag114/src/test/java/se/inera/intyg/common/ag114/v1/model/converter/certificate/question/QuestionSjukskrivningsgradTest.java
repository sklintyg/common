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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_HEADER_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_TEXT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_UNIT_OF_MEASURE;

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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigIntegerTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InputExpectedValuePair;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueIntegerTest;

@ExtendWith(MockitoExtension.class)
class QuestionSjukskrivningsgradTest {

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
            return QuestionSjukskrivningsgrad.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return SJUKSKRIVNINGSGRAD_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return SJUKSKRIVNINGSGRAD_HEADER_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigIntegerTests extends ConfigIntegerTest {

        @Override
        protected String getJsonId() {
            return SJUKSKRIVNINGSGRAD_SVAR_JSON_ID;
        }

        @Override
        protected Integer getMin() {
            return 0;
        }

        @Override
        protected Integer getMax() {
            return 100;
        }

        @Override
        protected String getUnitOfMeasurement() {
            return SJUKSKRIVNINGSGRAD_UNIT_OF_MEASURE;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSjukskrivningsgrad.toCertificate(null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return SJUKSKRIVNINGSGRAD_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    class IncludeValueIntegerTests extends ValueIntegerTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSjukskrivningsgrad.toCertificate("100", 0, texts);
        }

        @Override
        protected String getJsonId() {
            return SJUKSKRIVNINGSGRAD_SVAR_JSON_ID;
        }

        @Override
        protected Integer getValue() {
            return 100;
        }

        @Override
        protected String getUnitOfMeasurement() {
            return SJUKSKRIVNINGSGRAD_UNIT_OF_MEASURE;
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return SJUKSKRIVNINGSGRAD_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + SJUKSKRIVNINGSGRAD_SVAR_JSON_ID + ")";
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionSjukskrivningsgrad.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalValueTests extends InternalValueTest<String, String> {

        @Override
        protected CertificateDataElement getElement(String input) {
            return QuestionSjukskrivningsgrad.toCertificate(input, 0, texts);
        }

        @Override
        protected String toInternalValue(Certificate certificate) {
            return QuestionSjukskrivningsgrad.toInternal(certificate);
        }

        @Override
        protected List<InputExpectedValuePair<String, String>> inputExpectedValuePairList() {
            return List.of(
                new InputExpectedValuePair<>(null, null),
                new InputExpectedValuePair<>("", null),
                new InputExpectedValuePair<>("100", "100"),
                new InputExpectedValuePair<>("test", null),
                new InputExpectedValuePair<>("50   ", null)
            );
        }
    }
}
