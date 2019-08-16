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

import com.google.common.collect.ImmutableSet;
import java.util.stream.Stream;

public enum Korkortsbehorighet {
    AM("VAR12", "AM", "Moped klass I"),
    A1("VAR13", "A1", "Lätt motorcykel"),
    A2("VAR14", "A2", "Mellanstor motorcykel"),
    A("VAR15", "A", "Motorcykel"),
    B("VAR16", "B", "Personbil och lätt lastbil"),
    BE("VAR17", "BE", "Personbil, lätt lastbil och ett eller flera släpfordon"),
    TRAKTOR("VAR18", "Traktor", "Traktor"),
    C1("VAR1", "C1", "Medeltung lastbil och enbart ett lätt släpfordon"),
    C1E("VAR2", "C1E", "Medeltung lastbil och ett eller flera släpfordon oavsett vikt"),
    C("VAR3", "C", "Tung lastbil och enbart ett lätt släpfordon"),
    CE("VAR4", "CE", "Tung lastbil och ett eller flera släpfordon oavsett vikt"),
    D1("VAR5", "D1", "Mellanstor buss"),
    D1E("VAR6", "D1E", "Mellanstor buss och ett eller flera släpfordon oavsett vikt"),
    D("VAR7", "D", "Buss"),
    DE("VAR8", "DE", "Buss och enbart ett lätt släpfordon"),
    TAXI("VAR9", "Taxi", "Taxiförarlegitimation"),
    ANNAT("VAR10", "Annat (AM, A1, A2, A, B, BE eller Traktor)", "Annan körkortsbehörighet"),
    KANINTETASTALLNING("VAR11", "Kan inte ta ställning", "Kan inte ta ställning");

    private final String code;
    private final String value;
    private final String description;

    Korkortsbehorighet(final String code, final String value, final String description) {
        this.code = code;
        this.value = value;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static Korkortsbehorighet fromCode(final String code) {
        return Stream.of(Korkortsbehorighet.values()).filter(s -> code.equals(s.getCode())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(code));
    }

    /**
     * Hjälpmetoder för att hämta olika subset av kodverket som
     * resulterar i att specifika regler ytterligare måste valideras för intyget.
     */
    public static ImmutableSet<Korkortsbehorighet> getAllaBehorigheter() {
        return ImmutableSet.of(AM, A1, A2, A, B, BE, TRAKTOR, C1, C1E, C, CE, D1, D1E, D, DE, TAXI);
    }

    public static ImmutableSet<Korkortsbehorighet> getABTraktorBehorigheter() {
        return ImmutableSet.of(AM, A1, A2, A, B, BE, TRAKTOR);
    }

    public static ImmutableSet<Korkortsbehorighet> getCEBehorigHeter() {
        return ImmutableSet.of(C1, C1E, C, CE);
    }

    public static ImmutableSet<Korkortsbehorighet> getDBehorigHeter() {
        return ImmutableSet.of(D1, D1E, D, DE);
    }

    public static ImmutableSet<Korkortsbehorighet> getTaxiBehorigheter() {
        return ImmutableSet.of(TAXI);
    }

    public static ImmutableSet<Korkortsbehorighet> getKanintetastallning() {
        return ImmutableSet.of(KANINTETASTALLNING);
    }


}
