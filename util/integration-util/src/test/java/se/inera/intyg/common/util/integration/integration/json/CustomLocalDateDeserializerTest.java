/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.util.integration.integration.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.util.integration.json.CustomLocalDateDeserializer;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;

final class CustomLocalDateDeserializerTest {

  private static CustomLocalDateDeserializer deserializer;
  private static JsonFactory factory;

  @BeforeAll
  static void setup() {
    deserializer = new CustomLocalDateDeserializer();
    factory = new JsonFactory();
  }

  @Test
  void deserializeWhenDateTimeIsUTC() throws StreamReadException {

    String date = "2014-09-22T00:00:00.000Z";
    String json = "{\"journalanteckningar\":\"" + date + "\"}";

    JsonParser jp =
        factory.createParser(ObjectReadContext.empty(), json.getBytes(StandardCharsets.UTF_8));
    setJsonParserAtCorrectToken(jp);

    DeserializationContext ctxt = mock(DeserializationContext.class);
    when(ctxt.wrongTokenException(
            any(JsonParser.class), any(Class.class), any(JsonToken.class), anyString()))
        .thenReturn(
            DatabindException.from(
                jp,
                "Unexpected token ("
                    + jp.currentToken()
                    + "), expected "
                    + JsonToken.START_ARRAY
                    + ": expected JSON Array, Number or String")); // Mock implementation

    // Deserialize JSON string
    LocalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertLocalDate(ld);
  }

  @Test
  void deserializeWhenDateTimeIsLocal() throws StreamReadException {

    String date = "2014-09-22T00:00:00.000";
    String json = "{\"journalanteckningar\":\"" + date + "\"}";

    JsonParser jp =
        factory.createParser(ObjectReadContext.empty(), json.getBytes(StandardCharsets.UTF_8));
    setJsonParserAtCorrectToken(jp);

    DeserializationContext ctxt = mock(DeserializationContext.class);
    when(ctxt.wrongTokenException(
            any(JsonParser.class), any(Class.class), any(JsonToken.class), anyString()))
        .thenReturn(
            DatabindException.from(
                jp,
                "Unexpected token ("
                    + jp.currentToken()
                    + "), expected "
                    + JsonToken.START_ARRAY
                    + ": expected JSON Array, Number or String")); // Mock implementation

    // Deserialize JSON string
    LocalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertLocalDate(ld);
  }

  @Test
  void deserializeWhenOnlyDate() throws StreamReadException {

    String date = "2014-09-22";
    String json = "{\"journalanteckningar\":\"" + date + "\"}";

    JsonParser jp =
        factory.createParser(ObjectReadContext.empty(), json.getBytes(StandardCharsets.UTF_8));
    setJsonParserAtCorrectToken(jp);

    DeserializationContext ctxt = mock(DeserializationContext.class);
    when(ctxt.wrongTokenException(
            any(JsonParser.class), any(Class.class), any(JsonToken.class), anyString()))
        .thenReturn(
            DatabindException.from(
                jp,
                "Unexpected token ("
                    + jp.currentToken()
                    + "), expected "
                    + JsonToken.START_ARRAY
                    + ": expected JSON Array, Number or String")); // Mock implementation

    // Deserialize JSON string
    LocalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertLocalDate(ld);
  }

  @Test
  void deserializeWhenDateTimeIsJsonArray() throws StreamReadException {

    String date = "[2014,9,22]";
    String json = "{\"journalanteckningar\":" + date + "}";

    JsonParser jp =
        factory.createParser(ObjectReadContext.empty(), json.getBytes(StandardCharsets.UTF_8));
    setJsonParserAtCorrectToken(jp);

    DeserializationContext ctxt = mock(DeserializationContext.class);
    when(ctxt.wrongTokenException(
            any(JsonParser.class), any(Class.class), any(JsonToken.class), anyString()))
        .thenReturn(
            DatabindException.from(
                jp,
                "Unexpected token ("
                    + jp.currentToken()
                    + "), expected "
                    + JsonToken.START_ARRAY
                    + ": expected JSON Array, Number or String")); // Mock implementation

    // Deserialize JSON string
    LocalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertLocalDate(ld);
  }

  @Test
  void deserializeWhenDateTimeIsLong() throws StreamReadException {

    String date = "1411391603828";
    String json = "{\"journalanteckningar\":" + date + "}";

    JsonParser jp =
        factory.createParser(ObjectReadContext.empty(), json.getBytes(StandardCharsets.UTF_8));
    setJsonParserAtCorrectToken(jp);

    DeserializationContext ctxt = mock(DeserializationContext.class);
    when(ctxt.wrongTokenException(
            any(JsonParser.class), any(Class.class), any(JsonToken.class), anyString()))
        .thenReturn(
            DatabindException.from(
                jp,
                "Unexpected token ("
                    + jp.currentToken()
                    + "), expected "
                    + JsonToken.START_ARRAY
                    + ": expected JSON Array, Number or String")); // Mock implementation

    // Deserialize JSON string
    LocalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertLocalDate(ld);
  }

  @Test
  void failWhenDeserializationIsOnlyTime() throws StreamReadException {

    String date = "01:01:01";
    String json = "{\"journalanteckningar\":\"" + date + "\"}";

    JsonParser jp =
        factory.createParser(ObjectReadContext.empty(), json.getBytes(StandardCharsets.UTF_8));
    setJsonParserAtCorrectToken(jp);

    DeserializationContext ctxt = mock(DeserializationContext.class);
    when(ctxt.wrongTokenException(
            any(JsonParser.class), any(Class.class), any(JsonToken.class), anyString()))
        .thenReturn(
            DatabindException.from(
                jp,
                "Unexpected token ("
                    + jp.currentToken()
                    + "), expected "
                    + JsonToken.START_ARRAY
                    + ": expected JSON Array, Number or String")); // Mock implementation

    // Deserialize JSON string
    assertThrows(DateTimeParseException.class, () -> deserializer.deserialize(jp, ctxt));
  }

  private void setJsonParserAtCorrectToken(JsonParser jp) throws StreamReadException {
    // loop over all fields in JSON object
    while (jp.nextToken() != JsonToken.END_OBJECT) {
      String field = jp.currentName();
      if ("journalanteckningar".equals(field)) {
        break;
      }
      // move to next token (which is the field value)
      jp.nextToken();
    }
  }

  private void assertLocalDate(LocalDate localDate) throws JacksonException {
    assertEquals(2014, localDate.getYear());
    assertEquals(Month.SEPTEMBER, localDate.getMonth());
    assertEquals(22, localDate.getDayOfMonth());
  }
}
