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
package se.inera.intyg.common.support.common.enumerations;

import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;

/**
 * Representation of inmplemented intyg types from kodverket Kv Intygstyp.
 * see https://riv-ta.atlassian.net/wiki/spaces/KINT/pages/270532953/Kodverk+i+nationella+tj+nstekontrakt.
 * Note: The codes
 * - LIAG
 * - TSTRK1031-002
 * - AFU
 * - AFU_UTVIDGAD
 * are not included as they are not implemented.
 *
 * TSTRK1007 and TSTRK1031 also exists in Kv Intygstyp, but they still use KvUtlatandetyp for legacy reasons.
 */
public enum KvIntygstyp {

    LISJP("LISJP", "Läkarintyg för sjukpenning"), // " för " no actually in rivta version just yet.
    LUSE("LUSE", "Läkarutlåtande för sjukersättning"),
    LUAE_NA("LUAE_NA", "Läkarutlåtande för aktivitetsersättning vid nedsatt arbetsförmåga"),
    LUAE_FS("LUAE_FS", "Läkarutlåtande för aktivitetsersättning vid förlängd skolgång"),
    DB("DB", "Dödsbevis"),
    DOI("DOI", "Dödsorsaksintyg"),
    AF00213("AF00213", "Arbetsförmedlingens medicinska utlåtande"),
    TSTRK1009("TSTRK1009",
        "Läkares anmälan om medicinsk olämplighet att inneha körkort, körkortstillstånd, traktorkort eller taxiförarlegitimation"),
    TSTRK1062("TSTRK1062", "Läkarintyg avseende ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning"),
    AG1_14("AG1-14", "Läkarintyg om arbetsförmåga – sjuklöneperiod"),
    AG7804("AG7804", "Läkarintyg om arbetsförmåga – arbetsgivare"),
    AF00251("AF00251",
        "Läkarintyg för deltagare i arbetsmarknadspolitiska program med aktivitetsstöd, utvecklingsersättning eller etableringsersättning");

    private final String displayName;
    private final String codeValue;

    KvIntygstyp(String codeValue, String displayName) {
        this.codeValue = codeValue;
        this.displayName = displayName;
    }

    public String getCodeSystem() {
        //All types share the same codeSystem OID.
        return KV_INTYGSTYP_CODE_SYSTEM;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String getDisplayNameFromCodeValue(String codeValue) {
        for (var intygstyp : KvIntygstyp.values()) {
            if (intygstyp.getCodeValue().equalsIgnoreCase(codeValue)) {
                return intygstyp.getDisplayName();
            }
        }
        return "Unknown";
    }

}
