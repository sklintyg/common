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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.util.integration.json.InternalDateDeserializer;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;

public final class InternalDateDeserializerTest {

  private static InternalDateDeserializer deserializer;
  private static JsonFactory factory;

  @BeforeAll
  public static void setup() {
    deserializer = new InternalDateDeserializer();
    factory = new JsonFactory();
  }

  @Test
  public void deserializeWhenDateTimeIsUTC() throws StreamReadException, IOException {

    String date = "2014-09-22T00:00:00.000Z";
    String json = "{\"journalanteckningar\":\"" + date + "\"}";

    JsonParser jp = factory.createParser(json);
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
    InternalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertInternalDate(ld);
  }

  @Test
  public void deserializeWhenDateTimeIsLocal() throws StreamReadException, IOException {

    String date = "2014-09-22T00:00:00.000";
    String json = "{\"journalanteckningar\":\"" + date + "\"}";

    JsonParser jp = factory.createParser(json);
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
    InternalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertInternalDate(ld);
  }

  @Test
  public void deserializeWhenOnlyDate() throws StreamReadException, IOException {

    String date = "2014-09-22";
    String json = "{\"journalanteckningar\":\"" + date + "\"}";

    JsonParser jp = factory.createParser(json);
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
    InternalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertInternalDate(ld);
  }

  @Test
  public void deserializeWhenDateTimeIsJsonArray() throws StreamReadException, IOException {

    String date = "[2014,9,22,00,00,00,000]";
    String json = "{\"journalanteckningar\":" + date + "}";

    JsonParser jp = factory.createParser(json);
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
    InternalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertInternalDate(ld);
  }

  @Test
  public void deserializeWhenDateTimeIsLong() throws StreamReadException, IOException {

    String date = "1411391603828";
    String json = "{\"journalanteckningar\":" + date + "}";

    JsonParser jp = factory.createParser(json);
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
    InternalDate ld = deserializer.deserialize(jp, ctxt);

    // Assert that we have a correct date
    assertInternalDate(ld);
  }

  private void setJsonParserAtCorrectToken(JsonParser jp) throws IOException, StreamReadException {
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

  private void assertInternalDate(InternalDate localDate) throws IOException, JacksonException {
    assertEquals("2014-09-22", localDate.getDate());
  }
}
