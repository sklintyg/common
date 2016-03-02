/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.common.enumerations;

import java.util.stream.Stream;

/**
 * Handelsekod for notification schema v2.
 */
public enum HandelsekodEnum {

    SKAPAT("SKAPAT"),
    SIGNAT("SIGNAT"),
    SKICKA("SKICKA"),
    RADERA("RADERA"),
    MAKULE("MAKULE"),
    NYFRFM("NYFRFM"),
    NYSVFM("NYSVFM"),
    NYFRTM("NYFRTM"),
    HANFRA("HANFRA"),
    HANSVA("HANSVA"),
    ANDRAT("ANDRAT");

    private String value;

    HandelsekodEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static HandelsekodEnum fromValue(String value) {
        return Stream.of(HandelsekodEnum.values()).filter(s -> value.equals(s.value())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(value));
    }
}
