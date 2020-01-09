/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

import java.io.IOException;
import java.time.temporal.Temporal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import se.inera.intyg.common.util.integration.schema.adapter.PartialDateAdapter;

/**
 * @author andreaskaltenbach
 */
public class TemporalDeserializer extends StdDeserializer<Temporal> {

    private static final long serialVersionUID = 1L;

    public TemporalDeserializer() {
        super(Temporal.class);
    }

    @Override
    public Temporal deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException {

        if (jp.getCurrentToken() != VALUE_STRING) {
            throw ctxt.wrongTokenException(jp, VALUE_STRING, "expected JSON String");

        }

        return PartialDateAdapter.parsePartialDate(jp.getText().trim());
    }
}
