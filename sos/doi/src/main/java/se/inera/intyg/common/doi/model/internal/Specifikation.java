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
package se.inera.intyg.common.doi.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.UPPGIFT_SAKNAS_CODE;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UPPGIFT_SAKNAS_DISPLAY_NAME;
import static se.inera.intyg.common.support.Constants.KV_V3_CODE_SYSTEM_NULLFLAVOR_CODE_SYSTEM;

import java.util.stream.Stream;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;

public enum Specifikation {

    UPPGIFT_SAKNAS(UPPGIFT_SAKNAS_CODE, UPPGIFT_SAKNAS_DISPLAY_NAME, KV_V3_CODE_SYSTEM_NULLFLAVOR_CODE_SYSTEM),
    KRONISK("90734009", "kronisk", Diagnoskodverk.SNOMED_CT.getCodeSystem()),
    PLOTSLIG("424124008", "plötslig debut och/eller kort duration", Diagnoskodverk.SNOMED_CT.getCodeSystem());

    private final String id;
    private final String label;
    private final String codeSystem;

    Specifikation(String id, String label, String codeSystem) {
        this.id = id;
        this.label = label;
        this.codeSystem = codeSystem;
    }

    public static Specifikation fromId(String id) {
        return Stream.of(Specifikation.values())
            .filter(undersokning -> undersokning.getId().equals(id))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException());
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getCodeSystem() {
        return codeSystem;
    }
}
