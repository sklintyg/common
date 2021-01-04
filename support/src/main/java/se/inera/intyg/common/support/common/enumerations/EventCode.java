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
package se.inera.intyg.common.support.common.enumerations;

import java.util.stream.Stream;

public enum EventCode {

    SKAPAT("SKAPAT", "Skapat"),
    RADERAT("RADERAT", "Raderat"),
    LAST("LAST", "Låst"),
    KFSIGN("KFSIGN", "Klart för signering"),
    SIGNAT("SIGNAT", "Signerat"),
    SKICKAT("SKICKAT", "Skickat"),
    NYFRFM("NYFRFM", "Ny fråga från mottagare"),
    HANFRFM("HANFRFM", "Hanterad fråga från mottagare"),
    NYFRFV("NYFRFV", "Ny fråga från vården"),
    NYSVFM("NYSVFM", "Nytt svar från mottagare"),
    HANFRFV("HANFRFV", "Hanterad fråga från vården"),
    PAMINNELSE("PAMINNELSE", "Påminnelse"),
    KOMPLBEGARAN("KOMPLBEGARAN", "Kompletteringsbegäran"),
    MAKULERAT("MAKULERAT", "Makulerat"),
    ERSATTER("ERSATTER", "Ersätter"),
    KOMPLETTERAR("KOMPLETTERAR", "Kompletterar"),
    FORLANGER("FORLANGER", "Förlänger"),
    SKAPATFRAN("SKAPATFRAN", "Skapat från"),
    KOPIERATFRAN("KOPIERATFRAN", "Kopierat från"),
    RELINTYGMAKULE("RELINTYGMAKULE", "Relaterat intyg makulerat");


    private final String value;
    private final String description;

    EventCode(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String value() {
        return value;
    }

    public String getDescription() {
        return this.description;
    }

    public static EventCode fromValue(String value) {
        return Stream.of(EventCode.values()).filter(s -> value.equals(s.value())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(value));
    }

}
