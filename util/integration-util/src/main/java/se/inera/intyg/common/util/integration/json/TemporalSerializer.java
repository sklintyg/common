/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import java.io.IOException;
import java.time.temporal.Temporal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import se.inera.intyg.common.util.integration.schema.adapter.PartialDateAdapter;

/**
 * @author andreaskaltenbach
 */
public class TemporalSerializer extends StdSerializer<Temporal> {

    private static final long serialVersionUID = 1L;

    public TemporalSerializer() {
        super(Temporal.class);
    }

    @Override
    public void serialize(Temporal partial, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(PartialDateAdapter.printPartialDate(partial));
    }
}
