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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;

public abstract class ConfigCheckboxBooleanTest extends ConfigTest {

    protected abstract String getJsonId();

    protected abstract String getLabelId();

    protected abstract String getSelectedTextId();

    protected abstract String getUnselectedTextId();

    @Override
    protected CertificateDataConfigTypes getType() {
        return CertificateDataConfigTypes.UE_CHECKBOX_BOOLEAN;
    }

    @Test
    void shouldIncludeConfigId() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxBoolean) question.getConfig();
        assertEquals(getJsonId(), config.getId());
    }

    @Test
    void shouldIncludeLabelId() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxBoolean) question.getConfig();
        assertTrue(config.getLabel().trim().length() > 0, "Missing label");
        verify(getTextProviderMock(), atLeastOnce()).get(getLabelId());
    }

    @Test
    void shouldIncludeSelectedText() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxBoolean) question.getConfig();
        if (getSelectedTextId() != null && !config.getSelectedText().equals(getSelectedTextId())) {
            assertTrue(config.getSelectedText().trim().length() > 0, "Missing selected text");
            verify(getTextProviderMock(), atLeastOnce()).get(getSelectedTextId());
        } else {
            assertEquals(getSelectedTextId(), config.getSelectedText(), "Missing selected text");
        }
    }

    @Test
    void shouldIncludeUnselectedText() {
        final var question = getElement();
        final var config = (CertificateDataConfigCheckboxBoolean) question.getConfig();
        if (getUnselectedTextId() != null && !config.getUnselectedText().equals(getUnselectedTextId())) {
            assertTrue(config.getUnselectedText().trim().length() > 0, "Missing unselected text");
            verify(getTextProviderMock(), atLeastOnce()).get(getUnselectedTextId());
        } else {
            assertEquals(getUnselectedTextId(), config.getUnselectedText(), "Missing selected text");
        }
    }
}
