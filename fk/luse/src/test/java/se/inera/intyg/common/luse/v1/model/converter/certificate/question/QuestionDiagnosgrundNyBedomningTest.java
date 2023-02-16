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

package se.inera.intyg.common.luse.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SELECTED_TEXT;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_TEXT_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_UNSELECTED_TEXT;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigRadioBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalBooleanValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueBooleanTest;

@ExtendWith(MockitoExtension.class)
class QuestionDiagnosgrundNyBedomningTest {

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
            return QuestionDiagnosgrundNyBedomning.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID;
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
    class IncludeConfigRadioBooleanTests extends ConfigRadioBooleanTest {

        @Override
        protected String getId() {
            return DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID;
        }

        @Override
        protected String getSelectedText() {
            return DIAGNOSGRUND_NY_BEDOMNING_SELECTED_TEXT;
        }

        @Override
        protected String getUnselectedText() {
            return DIAGNOSGRUND_NY_BEDOMNING_UNSELECTED_TEXT;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiagnosgrundNyBedomning.toCertificate(null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return DIAGNOSGRUND_NY_BEDOMNING_SVAR_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return DIAGNOSGRUND_NY_BEDOMNING_SVAR_DESCRIPTION_ID;
        }
    }

    @Nested
    class IncludeValueBooleanTests extends ValueBooleanTest {

        private final boolean expectedBoolean = true;

        @Override
        protected String getJsonId() {
            return DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID;
        }

        @Override
        protected Boolean getBoolean() {
            return expectedBoolean;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiagnosgrundNyBedomning.toCertificate(expectedBoolean, 0, texts);
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "exists(" + DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID + ")";
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionDiagnosgrundNyBedomning.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalValidationBoolean extends InternalBooleanValueTest {

        @Override
        protected CertificateDataElement getElement(Boolean expectedValue) {
            return QuestionDiagnosgrundNyBedomning.toCertificate(expectedValue, 0, texts);
        }

        @Override
        protected Boolean toInternalBooleanValue(Certificate certificate) {
            return QuestionDiagnosgrundNyBedomning.toInternal(certificate);
        }
    }
}
