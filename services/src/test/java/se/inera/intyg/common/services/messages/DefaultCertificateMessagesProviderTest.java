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
package se.inera.intyg.common.services.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;
import org.junit.jupiter.api.Test;

class DefaultCertificateMessagesProviderTest {

    @Test
    void shallReturnMessageIfKeyIsPresent() {
        final var expectedValue = "value";
        final var messages = Map.of("key", "value");
        final var provider = DefaultCertificateMessagesProvider.create(messages, null);
        assertEquals(expectedValue, provider.get("key"));
    }

    @Test
    void shallReturnNullIfKeyIsMissing() {
        final var messages = Map.of("key", "value");
        final var provider = DefaultCertificateMessagesProvider.create(messages, null);
        assertNull(provider.get("notKey"));
    }

    @Test
    void shallReplaceDynamicKeyWithValue() {
        final var messages = Map.of("key", "text with dynamic key {0}");
        final var dynamicKeyMap = Map.of("dynamicKey", "dynamicValue");
        final var provider = DefaultCertificateMessagesProvider.create(messages, dynamicKeyMap);
        final var actualMessage = provider.get("key", "dynamicKey");
        assertEquals("text with dynamic key dynamicValue", actualMessage);
    }

    @Test
    void shallReturnDynamicKeyAsValueIfKeyIsMissing() {
        final var messages = Map.of("key", "text without key value = {0}");
        final var dynamicKeyMap = Map.of("dynamicKey", "dynamicValue");
        final var provider = DefaultCertificateMessagesProvider.create(messages, dynamicKeyMap);
        final var expectedValue = "text without key value = notDynamicKey";
        final var actualValue = provider.get("key", "notDynamicKey");
        assertEquals(expectedValue, actualValue);
    }
}