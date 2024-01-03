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

package se.inera.intyg.common.support.facade.testsetup.model.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;

public abstract class ConfigViewListTest extends ConfigTest {

    protected abstract CertificateMessagesProvider getMessageProviderMock();

    protected abstract String getMessageId();

    @Override
    protected CertificateDataConfigTypes getType() {
        return CertificateDataConfigTypes.UE_VIEW_LIST;
    }

    @Override
    protected CertificateTextProvider getTextProviderMock() {
        return null;
    }

    @Override
    protected String getTextId() {
        return null;
    }

    @Test
    void shallIncludeMessageId() {
        final var question = getElement();
        if (getMessageId() != null && getMessageProviderMock() != null) {
            assertTrue(question.getConfig().getText().trim().length() > 0, "Missing text");
            verify(getMessageProviderMock(), atLeastOnce()).get(getMessageId());
        } else if (getMessageId() != null) {
            assertEquals(question.getConfig().getText(), getMessageId());
        }
    }
}
