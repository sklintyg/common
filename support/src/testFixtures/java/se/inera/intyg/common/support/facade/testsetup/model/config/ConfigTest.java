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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;

public abstract class ConfigTest {

    protected abstract CertificateTextProvider getTextProviderMock();

    protected abstract CertificateDataElement getElement();

    protected abstract CertificateDataConfigTypes getType();

    protected String getHeaderId() {
        return null;
    }

    protected String getLabelId() {
        return null;
    }

    protected String getIconId() {
        return null;
    }

    protected abstract String getTextId();

    protected abstract String getDescriptionId();

    @Test
    void shouldIncludeConfigCertificateDataConfig() {
        final var question = getElement();
        assertEquals(getType(), question.getConfig().getType());
    }

    @Test
    void shouldIncludeHeader() {
        final var question = getElement();
        if (getHeaderId() != null) {
            assertTrue(question.getConfig().getHeader().trim().length() > 0, "Missing Header");
            verify(getTextProviderMock(), atLeastOnce()).get(getHeaderId());
        }
    }

    @Test
    void shouldIncludeLabel() {
        final var question = getElement();
        if (getLabelId() != null) {
            assertTrue(question.getConfig().getLabel().trim().length() > 0, "Missing label");
            verify(getTextProviderMock(), atLeastOnce()).get(getLabelId());
        }
    }

    @Test
    void shouldIncludeIcon() {
        final var question = getElement();
        if (getIconId() != null) {
            assertEquals(getIconId(), question.getConfig().getIcon());
        }
    }

    @Test
    void shouldIncludeConfigText() {
        final var question = getElement();
        if (getTextId() != null && !question.getConfig().getText().equals(getTextId())) {
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(getTextProviderMock(), atLeastOnce()).get(getTextId());
        } else {
            assertEquals(getTextId(), question.getConfig().getText());
        }
    }

    @Test
    void shouldIncludeConfigDescription() {
        final var question = getElement();
        if (getDescriptionId() != null && !question.getConfig().getDescription().equals(getDescriptionId())) {
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing description");
            verify(getTextProviderMock(), atLeastOnce()).get(getDescriptionId());
        } else {
            assertEquals(getDescriptionId(), question.getConfig().getDescription());
        }
    }
}
