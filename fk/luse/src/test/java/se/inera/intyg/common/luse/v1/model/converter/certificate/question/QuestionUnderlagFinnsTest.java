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

package se.inera.intyg.common.luse.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.UNDERLAGFINNS_SELECTED_TEXT;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_TEXT;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.UNDERLAGFINNS_UNSELECTED_TEXT;

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
class QuestionUnderlagFinnsTest {

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
            return QuestionUnderlagFinns.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return UNDERLAGFINNS_SVAR_ID_3;
        }

        @Override
        protected String getParent() {
            return GRUNDFORMU_CATEGORY_ID;
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
            return UNDERLAGFINNS_SVAR_JSON_ID_3;
        }

        @Override
        protected String getSelectedText() {
            return UNDERLAGFINNS_SELECTED_TEXT;
        }

        @Override
        protected String getUnselectedText() {
            return UNDERLAGFINNS_UNSELECTED_TEXT;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUnderlagFinns.toCertificate(null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return UNDERLAGFINNS_SVAR_TEXT;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }
    }

    @Nested
    class IncludeValueBooleanTests extends ValueBooleanTest {

        private final Boolean expectedValue = true;

        @Override
        protected String getJsonId() {
            return UNDERLAGFINNS_SVAR_JSON_ID_3;
        }

        @Override
        protected Boolean getBoolean() {
            return expectedValue;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUnderlagFinns.toCertificate(expectedValue, 0, texts);
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return UNDERLAGFINNS_SVAR_ID_3;
        }

        @Override
        protected String getExpression() {
            return "exists(" + UNDERLAGFINNS_SVAR_JSON_ID_3 + ")";
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionUnderlagFinns.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalBooleanTests extends InternalBooleanValueTest {

        @Override
        protected CertificateDataElement getElement(Boolean expectedValue) {
            return QuestionUnderlagFinns.toCertificate(expectedValue, 0, texts);
        }

        @Override
        protected Boolean toInternalBooleanValue(Certificate certificate) {
            return QuestionUnderlagFinns.toInternal(certificate);
        }
    }
}
