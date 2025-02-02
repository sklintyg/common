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

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigType;

public abstract class ConfigRadioBooleanTest extends ConfigTest {

    protected abstract String getId();

    protected abstract String getSelectedText();

    protected abstract String getUnselectedText();

    @Override
    protected CertificateDataConfigType getType() {
        return CertificateDataConfigType.UE_RADIO_BOOLEAN;
    }

    @Test
    void includeId() {
        final var question = (CertificateDataConfigRadioBoolean) getElement().getConfig();
        assertEquals(getId(), question.getId());
    }

    @Test
    void includesSelectedText() {
        final var question = (CertificateDataConfigRadioBoolean) getElement().getConfig();
        assertTrue(question.getSelectedText().trim().length() > 0, "Missing text");
        assertEquals(getSelectedText(), question.getSelectedText());
    }

    @Test
    void includesUnselectedTextText() {
        final var question = (CertificateDataConfigRadioBoolean) getElement().getConfig();
        assertTrue(question.getUnselectedText().trim().length() > 0, "Missing text");
        assertEquals(getUnselectedText(), question.getUnselectedText());
    }
}
