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

package se.inera.intyg.common.support.facade.testsetup.model.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.Accordion;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;

public abstract class ConfigTest {

    protected abstract CertificateTextProvider getTextProviderMock();

    protected abstract CertificateDataElement getElement();

    protected abstract CertificateDataConfigTypes getType();

    protected abstract String getTextId();

    protected abstract String getDescriptionId();

    Accordion getAccordion() {
        return null;
    }

    @Test
    void shouldIncludeConfigCertificateDataConfig() {
        final var question = getElement();
        assertEquals(getType(), question.getConfig().getType());
    }

    @Test
    void shouldIncludeConfigText() {
        final var question = getElement();
        assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
        verify(getTextProviderMock(), atLeastOnce()).get(getTextId());
    }

    @Test
    void shouldIncludeConfigDescription() {
        final var question = getElement();
        assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
        verify(getTextProviderMock(), atLeastOnce()).get(getDescriptionId());
    }
}
