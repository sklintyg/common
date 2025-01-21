/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.db.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.stream.Stream;

public enum Undersokning {
    @JsonProperty("SVAR_JA")
    JA("", UndersokningConstants.UNDERSOKNING_JA), // Transport is never used as it is represented as boolean 'true'
    @JsonProperty("DETALJER_UNDERSOKNING.UNDERSOKNING_GJORT_KORT_FORE_DODEN")
    UNDERSOKNING_GJORT_KORT_FORE_DODEN(UndersokningConstants.UNDERSOKNING_GJORT_TRANSPORT, UndersokningConstants.UNDERSOKNING_GJORT_TEXT),
    @JsonProperty("DETALJER_UNDERSOKNING.UNDERSOKNING_SKA_GORAS")
    UNDERSOKNING_SKA_GORAS(UndersokningConstants.UNDERSOKNING_SKA_GORAS_TRANSPORT, UndersokningConstants.UNDERSOKNING_SKA_GORAS_TEXT);

    private final String transport;
    private final String beskrivning;

    Undersokning(final String transport, final String beskrivning) {
        this.transport = transport;
        this.beskrivning = beskrivning;
    }

    public String getTransport() {
        return transport;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public static Undersokning fromTransport(String transport) {
        return Stream.of(Undersokning.values())
            .filter(v -> v.getTransport().equals(transport))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("unknown value: " + transport));
    }
}
