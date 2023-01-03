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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_SVAR_ID_7;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_SVAR_JSON_ID_7;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigTextAreaTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalTextValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;

@ExtendWith(MockitoExtension.class)
class QuestionDiagnosgrundTest {

    private static final String EXPECTED_TEXT = "Hej hej";
    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setup() {
        when(textProvider.get(any(String.class))).thenReturn("Test string");
    }

    @Nested
    class ToCertificate {

        @Nested
        class IncludeCommonElementTest extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnosgrund.toCertificate(EXPECTED_TEXT, 0, textProvider);
            }

            @Override
            protected String getId() {
                return DIAGNOSGRUND_SVAR_ID_7;
            }

            @Override
            protected String getParent() {
                return DIAGNOS_CATEGORY_ID;
            }

            @Override
            protected int getIndex() {
                return 0;
            }
        }

        @Nested
        class IncludeConfigTextAreaTest extends ConfigTextAreaTest {

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return textProvider;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnosgrund.toCertificate(EXPECTED_TEXT, 0, textProvider);
            }

            @Override
            protected String getTextId() {
                return DIAGNOSGRUND_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return null;
            }

            @Override
            protected String getJsonId() {
                return DIAGNOSGRUND_SVAR_JSON_ID_7;
            }
        }

        @Nested
        class IncludeValueTest extends ValueTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnosgrund.toCertificate(EXPECTED_TEXT, 0, textProvider);
            }

            @Override
            protected String getJsonId() {
                return DIAGNOSGRUND_SVAR_JSON_ID_7;
            }

            @Override
            protected String getText() {
                return EXPECTED_TEXT;
            }
        }

        @Nested
        class IncludeValidationTextTest extends ValidationTextTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnosgrund.toCertificate(EXPECTED_TEXT, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 0;
            }

            @Override
            protected short getLimit() {
                return 3500;
            }
        }

        @Nested
        class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

            @Override
            protected String getQuestionId() {
                return DIAGNOSGRUND_SVAR_JSON_ID_7;
            }

            @Override
            protected String getExpression() {
                return singleExpression(DIAGNOSGRUND_SVAR_JSON_ID_7);
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionDiagnosgrund.toCertificate(EXPECTED_TEXT, 0, textProvider);
            }

            @Override
            protected int getValidationIndex() {
                return 1;
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
                return QuestionDiagnosgrund.toCertificate(expectedValue, 0, textProvider);
            }

            @Override
            protected String toInternalTextValue(Certificate certificate) {
                return QuestionDiagnosgrund.toInternal(certificate);
            }
        }
    }
}