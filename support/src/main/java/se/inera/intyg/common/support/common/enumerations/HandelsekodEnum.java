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
package se.inera.intyg.common.support.common.enumerations;

import java.util.stream.Stream;

/**
 * Handelsekod for notification schema v2 and later.
 */
public enum HandelsekodEnum {

    // Codes introduced in v2
    SKAPAT("SKAPAT", "Intygsutkast skapas"),
    SIGNAT("SIGNAT", "Intyg signerat"),
    SKICKA("SKICKA", "Intyg skickat till mottagare"),
    RADERA("RADERA", "Intygsutkast raderat"),
    MAKULE("MAKULE", "Intyg eller intygsutkast makulerat"),
    NYFRFM("NYFRFM", "Ny fråga från mottagare"),
    NYSVFM("NYSVFM", "Nytt svar från mottagare"),
    NYFRFV("NYFRFV", "Ny fråga från vården"),
    HANFRFV("HANFRFV", "Hanterad fråga från vården"),
    HANFRFM("HANFRFM", "Hanterad fråga från mottagare"),
    ANDRAT("ANDRAT", "Intygsutkast ändrat"),

    // Codes introduced in v2.1
    KFSIGN("KFSIGN", "Intygsutkast klart för signering");

    private final String value;
    private final String description;

    HandelsekodEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String value() {
        return value;
    }

    public String description() {
        return description;
    }

    public static HandelsekodEnum fromValue(String value) {
        return Stream.of(HandelsekodEnum.values()).filter(s -> value.equals(s.value())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(value));
    }
}
