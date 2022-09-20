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
package se.inera.intyg.common.services.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class MessageParserTest {

    @Test
    void parseOneFile() throws IOException {
        final var inputStream = new ClassPathResource("/messages/lisjpMessages.js").getInputStream();

        final var map = MessagesParser.create().parse(inputStream).collect();

        assertEquals(map.keySet().size(), 37);
    }

    @Test
    void parseTwoFiles() throws IOException {
        final var inputStream1 = new ClassPathResource("/messages/lisjpMessages.js").getInputStream();
        final var inputStream2 = new ClassPathResource("/messages/webcertMessages.js").getInputStream();

        final var map = MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();

        assertEquals(map.keySet().size(), 709);
    }

    @Test
    void parseThreeFiles() throws IOException {
        final var inputStream1 = new ClassPathResource("/messages/lisjpMessages.js").getInputStream();
        final var inputStream2 = new ClassPathResource("/messages/webcertMessages.js").getInputStream();
        final var inputStream3 = new ClassPathResource("/messages/lisjpMessages2.js").getInputStream();

        final var map = MessagesParser.create().parse(inputStream1).parse(inputStream2).parse(inputStream3).collect();

        assertEquals(map.keySet().size(), 710);
    }

    @Test
    void filesContainsSameKeyShallThrowException() throws IOException {
        final var inputStream1 = new ClassPathResource("/messages/lisjpMessages.js").getInputStream();
        final var inputStream2 = new ClassPathResource("/messages/lisjpMessages.js").getInputStream();

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            MessagesParser.create().parse(inputStream1).parse(inputStream2).collect();
        });

        assertEquals("Maps contains same keys. This need to be resolved.", exception.getMessage());
    }

    @Test
    void noStartOrEndPositionShallThrowException() throws IOException {
        final var inputStream = new ClassPathResource("/messages/withoutStartAndEndPosition.txt").getInputStream();

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            MessagesParser.create().parse(inputStream);
        });

        assertEquals("No start or end position found. Start position found: false. End position found: false", exception.getMessage());
    }

    @Test
    void notPossbileToConvertToJsonShallThrowException() throws IOException {
        final var inputStream = new ClassPathResource("/messages/notConvertableToJson.txt").getInputStream();

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            MessagesParser.create().parse(inputStream);
        });

        assertEquals("Could not convert json to map", exception.getMessage());
    }

}