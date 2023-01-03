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
package se.inera.intyg.common.support.facade.testsetup.model.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes.UE_MESSAGE;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;

public abstract class ConfigMessageTest extends ConfigTest {

    protected abstract String getMessageId();

    protected abstract MessageLevel getMessageLevel();

    @Override
    protected CertificateDataConfigTypes getType() {
        return UE_MESSAGE;
    }

    @Test
    void shouldIncludeConfigMessage() {
        final var certificateDataElement = getElement();
        final var certificateDataElementConfig = (CertificateDataConfigMessage) certificateDataElement.getConfig();
        assertTrue(certificateDataElementConfig.getMessage().trim().length() > 0, "Missing message");
        verify(getTextProviderMock(), atLeastOnce()).get(getMessageId());
    }

    @Test
    void shouldIncludeConfigMessageLevel() {
        final var question = getElement();
        final var config = (CertificateDataConfigMessage) question.getConfig();
        assertEquals(getMessageLevel(), config.getLevel());
    }
}
