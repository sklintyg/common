/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.stream.Stream;

public enum AnmalanAvser {
    OLAMPLIGHET("OLAMPLIGHET", "Anmälan om olämplighet"),
    SANNOLIK_OLAMPLIGHET("SANNOLIK_OLAMPLIGHET", "Anmälan om sannolik olämplighet");

    private final String code;
    private final String description;

    AnmalanAvser(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static AnmalanAvser fromId(@JsonProperty("id") String id) {
        final String normId = id != null ? id.trim() : null;
        for (AnmalanAvser typ : values()) {
            if (typ.name().equals(normId)) {
                return typ;
            }
        }
        throw new IllegalArgumentException(id);
    }

    public static AnmalanAvser fromCode(final String code) {
        return Stream.of(AnmalanAvser.values()).filter(s -> code.equals(s.getCode())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(code));
    }

    public String getId() {
        return this.name();
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
