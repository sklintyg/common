/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.facade.testsetup.model.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public abstract class AccordionTest {

    protected abstract String getExpectedOpenText();

    protected abstract String getExpectedCloseText();

    protected abstract String getExpectedAccordionHeader();

    protected abstract CertificateTextProvider getTextProviderMock();

    protected abstract CertificateDataElement getElement();

    @Test
    void shouldIncludeConfigAccordionOpenText() {
        final var question = getElement();
        assertEquals(getExpectedOpenText(), question.getConfig().getAccordion().getOpenText());
    }

    @Test
    void shouldIncludeConfigAccordionClosedText() {
        final var question = getElement();
        assertEquals(getExpectedCloseText(), question.getConfig().getAccordion().getCloseText());
    }

    @Test
    void shouldIncludeConfigAccordionHeader() {
        final var question = getElement();
        assertTrue(question.getConfig().getAccordion().getHeader().trim().length() > 0, "Missing text");
        verify(getTextProviderMock(), atLeastOnce()).get(getExpectedAccordionHeader());
    }
}
