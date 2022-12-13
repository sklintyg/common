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

package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.NYDIAGNOS_SVAR_ID_45;
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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigRadioBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalRadioBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueBooleanTest;

@ExtendWith(MockitoExtension.class)
class QuestionNyBedomningDiagnosgrundTest {

    @Mock
    private CertificateTextProvider textProvider;

    @Nested
    class ToCertificate {

        @BeforeEach
        void setup() {
            when(textProvider.get(any(String.class))).thenReturn("Test string");
        }

        @Nested
        class IncludeCommonElementTest extends CommonElementTest {

            @Override
            protected CertificateDataElement getElement() {
                return QuestionNyBedomningDiagnosgrund.toCertificate(true, 0, textProvider);
            }

            @Override
            protected String getId() {
                return NYDIAGNOS_SVAR_ID_45;
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
        class IncludeConfigRadioBooleanTest extends ConfigRadioBooleanTest {

            @Override
            protected String getId() {
                return DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45;
            }

            @Override
            protected CertificateTextProvider getTextProviderMock() {
                return textProvider;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionNyBedomningDiagnosgrund.toCertificate(true, 0, textProvider);
            }

            @Override
            protected String getTextId() {
                return DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT_ID;
            }

            @Override
            protected String getDescriptionId() {
                return DIAGNOSGRUND_NYBEDOMNING_SVAR_DESCRIPTION_ID;
            }
        }

        @Nested
        class IncludeValueBooleanTest extends ValueBooleanTest {

            @Override
            protected String getJsonId() {
                return DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45;
            }

            @Override
            protected Boolean getBoolean() {
                return true;
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionNyBedomningDiagnosgrund.toCertificate(true, 0, textProvider);
            }

        }

        @Nested
        class IncludeValidationMandatoryTest extends ValidationMandatoryTest {

            @Override
            protected String getQuestionId() {
                return DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45;
            }

            @Override
            protected String getExpression() {
                return singleExpression(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45);
            }

            @Override
            protected CertificateDataElement getElement() {
                return QuestionNyBedomningDiagnosgrund.toCertificate(true, 0, textProvider);
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
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class IncludeInternalRadioBooleanTest extends InternalRadioBooleanTest {

            @Override
            protected CertificateDataElement toCertificate(Boolean expectedValue) {
                return QuestionNyBedomningDiagnosgrund.toCertificate(expectedValue, 0, textProvider);
            }

            @Override
            protected Boolean toInternal(Certificate certificate) {
                return QuestionNyBedomningDiagnosgrund.toInternal(certificate);
            }
        }
    }
}