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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_DESCRIPTION_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_TEXT_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.testsetup.model.CommonElementTest;
import se.inera.intyg.common.support.facade.testsetup.model.config.ConfigTextAreaTest;
import se.inera.intyg.common.support.facade.testsetup.model.validation.ValidationTextTest;
import se.inera.intyg.common.support.facade.testsetup.model.value.ValueTextTest;

@ExtendWith(MockitoExtension.class)
class QuestionFunktionsnedsattningIntellektuellTest {

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
            return QuestionFunktionsnedsattningIntellektuell.toCertificate(null, 0, texts);
        }

        @Override
        protected String getId() {
            return FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID;
        }

        @Override
        protected String getParent() {
            return FUNKTIONSNEDSATTNING_CATEGORY_ID;
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
            return QuestionFunktionsnedsattningIntellektuell.toCertificate(null, 0, texts);
        }

        @Override
        protected String getTextId() {
            return FUNKTIONSNEDSATTNING_INTELLEKTUELL_TEXT_ID;
        }

        @Override
        protected String getDescriptionId() {
            return FUNKTIONSNEDSATTNING_INTELLEKTUELL_DESCRIPTION_ID;
        }

        @Override
        protected String getJsonId() {
            return FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID;
        }
    }

    @Nested
    class IncludeAccordionTests {

        @Test
        void shouldIncludeConfigAccordionOpenText() {
            final var expectedOpenText = "Visa fritextfältet";
            final var question = QuestionFunktionsnedsattningIntellektuell.toCertificate(null, 0, texts);
            assertEquals(expectedOpenText, question.getConfig().getAccordion().getOpenText());
        }

        @Test
        void shouldIncludeConfigAccordionClosedText() {
            final var expectedOpenText = "Dölj fritextfältet";
            final var question = QuestionFunktionsnedsattningIntellektuell.toCertificate(null, 0, texts);
            assertEquals(expectedOpenText, question.getConfig().getAccordion().getCloseText());
        }

        @Test
        void shouldIncludeConfigAccordionHeader() {
            final var question = QuestionFunktionsnedsattningIntellektuell.toCertificate(null, 0, texts);
            assertTrue(question.getConfig().getAccordion().getHeader().trim().length() > 0, "Missing text");
            verify(texts, atLeastOnce()).get(FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_TEXT_ID);
        }
    }

    @Nested
    class IncludeValueTextTests extends ValueTextTest {

        private final String expectedValue = "test";

        @Override
        protected CertificateDataElement getElement() {
            return QuestionFunktionsnedsattningIntellektuell.toCertificate(expectedValue, 0, texts);
        }

        @Override
        protected String getJsonId() {
            return FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID;
        }

        @Override
        protected String getText() {
            return expectedValue;
        }
    }

    @Nested
    class IncludeValidationTextTests extends ValidationTextTest {

        @Override
        protected CertificateDataElement getElement() {
            return QuestionFunktionsnedsattningIntellektuell.toCertificate(null, 0, texts);
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
}
