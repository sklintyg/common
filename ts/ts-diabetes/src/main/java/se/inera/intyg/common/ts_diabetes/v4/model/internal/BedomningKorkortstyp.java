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
package se.inera.intyg.common.ts_diabetes.v4.model.internal;

import java.util.stream.Stream;

/**
 * This is a subset (VAR1-9, VAR11-18) of "Kv körkortsbehörighet"
 * {@link se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod}.
 *
 * Note - the order is adjusted to match requirements.
 */
public enum BedomningKorkortstyp {
    VAR12("AM"),
    VAR13("A1"),
    VAR14("A2"),
    VAR15("A"),
    VAR16("B"),
    VAR17("BE"),
    VAR18("TRAKTOR"),
    VAR1("C1"),
    VAR2("C1E"),
    VAR3("C"),
    VAR4("CE"),
    VAR5("D1"),
    VAR6("D1E"),
    VAR7("D"),
    VAR8("DE"),
    VAR9("TAXI"),
    VAR11("KANINTETASTALLNING");

    final String type;

    BedomningKorkortstyp(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static BedomningKorkortstyp fromCode(String type) {
        return Stream.of(BedomningKorkortstyp.values()).filter(s -> type.equals(s.getType())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(type));
    }
}
