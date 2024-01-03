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
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_DESCRIPTION_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_TEXT_ID;

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
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigTextAreaTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationMandatoryTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.InternalTextValueTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;

@ExtendWith(MockitoExtension.class)
class QuestionMedicinskaForutsattningarForArbeteTest {

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
            return QuestionMedicinskaForutsattningarForArbete.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE;
        }

        @Override
        protected int getIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeConfigTextAreaTests extends ConfigTextAreaTest {

        @Override
        protected CertificateTextProvider getTextProviderMock() {
            return texts;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionMedicinskaForutsattningarForArbete.toCertificate(null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return MEDICINSKAFORUTSATTNINGARFORARBETE_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return MEDICINSKAFORUTSATTNINGARFORARBETE_DESCRIPTION_ID;
        }

        @Override
        protected String getJsonId() {
            return MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID;
        }
    }

    @Nested
    class IncludeValueTextTests extends ValueTextTest {

        private final String expectedText = "text";

        @Override
        protected CertificateDataElement getElement() {
            return QuestionMedicinskaForutsattningarForArbete.toCertificate(expectedText, 0, texts);
        }

        @Override
        protected String getJsonId() {
            return MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID;
        }

        @Override
        protected String getText() {
            return expectedText;
        }
    }

    @Nested
    class IncludeValidationMandatoryTests extends ValidationMandatoryTest {

        @Override
        protected String getQuestionId() {
            return MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID;
        }

        @Override
        protected String getExpression() {
            return "$" + MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID;
        }

        @Override
        protected CertificateDataElement getElement() {
            return QuestionMedicinskaForutsattningarForArbete.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 0;
        }
    }

    @Nested
    class IncludeValidationTextTests extends ValidationTextTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionMedicinskaForutsattningarForArbete.toCertificate(null, 0, texts);
        }

        @Override
        protected int getValidationIndex() {
            return 1;
        }

        @Override
        protected short getLimit() {
            return 3500;
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class InternalTextValueTests extends InternalTextValueTest {

        @Override
        protected CertificateDataElement getElement(String expectedValue) {
            return QuestionMedicinskaForutsattningarForArbete.toCertificate(expectedValue, 0, texts);
        }

        @Override
        protected String toInternalTextValue(Certificate certificate) {
            return QuestionMedicinskaForutsattningarForArbete.toInternal(certificate);
        }
    }
}
