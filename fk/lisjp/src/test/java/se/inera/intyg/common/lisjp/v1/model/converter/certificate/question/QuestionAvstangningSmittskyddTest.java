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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_QUESTION_LABEL;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;

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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigCheckboxBooleanTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalBooleanValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueBooleanTest;

@ExtendWith(MockitoExtension.class)
class QuestionAvstangningSmittskyddTest {

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
            return QuestionAvstangningSmittskydd.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
        }

        @Override
        protected String getParent() {
            return AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigCheckboxBooleanTests extends ConfigCheckboxBooleanTest {

        @Override
        protected String getJsonId() {
            return AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
        }

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionAvstangningSmittskydd.toCertificate(null, 0, texts);
        }

        @Override
        protected String getLabelId() {
            return AVSTANGNING_SMITTSKYDD_QUESTION_LABEL;
        }

        @Override
        protected String getTextId() {
            return null;
        }

        @Override
        protected String getDescriptionId() {
            return null;
        }

        @Override
        protected String getSelectedTextId() {
            return ANSWER_YES;
        }

        @Override
        protected String getUnselectedTextId() {
            return ANSWER_NOT_SELECTED;
        }
    }

    @Nested
    class IncludeValueBooelanTests extends ValueBooleanTest {

        @Override
        protected String getJsonId() {
            return AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
        }

        @Override
        protected Boolean getBoolean() {
            return true;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionAvstangningSmittskydd.toCertificate(true, 0, texts);
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class IncludeInternalBooleanTests extends InternalBooleanValueTest {

        @Override
        protected CertificateDataElement getElement(Boolean expectedValue) {
            return QuestionAvstangningSmittskydd.toCertificate(expectedValue, 0, texts);
        }

        @Override
        protected Boolean toInternalBooleanValue(Certificate certificate) {
            return QuestionAvstangningSmittskydd.toInternal(certificate);
        }
    }
}
