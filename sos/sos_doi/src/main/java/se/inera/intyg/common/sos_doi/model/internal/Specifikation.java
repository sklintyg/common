/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.sos_doi.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Specifikation {
    KRONISK("90734009", "kronisk"),
    PLOTSLIG("424124008", "plötslig debut och/eller kort duration");

    private final String id;
    private final String label;

    Specifikation(String id, String label) {
        this.id = id;
        this.label = label;
    }

    @JsonCreator
    public static Specifikation fromId(@JsonProperty("id") String id) {
        String normId = id != null ? id.trim() : null;
        for (Specifikation typ : values()) {
            if (typ.id.equals(normId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException();
    }

    @JsonValue
    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}
