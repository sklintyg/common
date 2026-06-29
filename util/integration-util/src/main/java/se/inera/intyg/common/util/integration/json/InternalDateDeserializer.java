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
package se.inera.intyg.common.util.integration.json;

import java.time.Instant;
import java.time.ZoneId;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.util.integration.schema.adapter.InternalDateAdapter;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

public class InternalDateDeserializer extends StdDeserializer<InternalDate> {

  private static final long serialVersionUID = 1L;

  public InternalDateDeserializer() {
    super(InternalDate.class);
  }

  @Override
  public InternalDate deserialize(JsonParser jp, DeserializationContext ctxt) {
    switch (jp.currentToken()) {
      case START_ARRAY:
        // [yyyy,MM,dd,hh,mm,ss,ms]

        jp.nextToken(); // VALUE_NUMBER_INT
        int year = jp.getIntValue();

        jp.nextToken(); // VALUE_NUMBER_INT
        int month = jp.getIntValue();

        jp.nextToken(); // VALUE_NUMBER_INT
        int day = jp.getIntValue();

        // We are only interested in year, month and day
        // Skip the time and return at date
        return InternalDateAdapter.parseInternalDate(year, month, day);
      case VALUE_NUMBER_INT:
        return new InternalDate(
            Instant.ofEpochMilli(jp.getLongValue()).atZone(ZoneId.systemDefault()).toLocalDate());
      case VALUE_STRING:
        String str = jp.getString().trim();
        if (str.isEmpty()) { // [JACKSON-360]
          return null;
        }

        return InternalDateAdapter.parseInternalDate(str);
      default:
        throw ctxt.wrongTokenException(
            jp, InternalDate.class, JsonToken.START_ARRAY, "expected JSON Array, Number or String");
    }
  }
}
