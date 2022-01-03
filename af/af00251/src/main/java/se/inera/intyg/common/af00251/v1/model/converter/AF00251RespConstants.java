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
package se.inera.intyg.common.af00251.v1.model.converter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Shared constants between ALL versions of AF00251 intyg.
 */
public final class AF00251RespConstants {

    public static final String MEDICINSKUNDERLAG_SVAR_ID_1 = "1";
    public static final String MEDICINSKUNDERLAG_DELSVAR_ID_11 = "1.1";
    public static final String MEDICINSKUNDERLAG_DELSVAR_ID_12 = "1.2";
    public static final String MEDICINSKUNDERLAG_DELSVAR_ID_13 = "1.3";
    public static final String MEDICINSKUNDERLAG_SVAR_JSON_UNDERSOKNING = "undersokning";
    public static final String MEDICINSKUNDERLAG_SVAR_JSON_UNDERSOKNINGS_DATUM = "undersokningsDatum";
    public static final String MEDICINSKUNDERLAG_SVAR_JSON_ANNAT_DATUM = "annatDatum";
    public static final String MEDICINSKUNDERLAG_SVAR_JSON_ANNAT_BESKRIVNING = "annatBeskrivning";


    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID_2 = "2";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2 = "arbetsmarknadspolitisktProgram";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_21 = "2.1";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_21 = "medicinskBedomning";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_22 = "2.2";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_22 = "omfattning";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID_23 = "2.3";
    public static final String ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23 = "omfattningDeltid";

    public static final String FUNKTIONSNEDSATTNING_SVAR_ID_3 = "3";
    public static final String FUNKTIONSNEDSATTNING_SVAR_JSON_ID_3 = "funktionsnedsattning";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_ID_31 = "3.1";
    public static final String FUNKTIONSNEDSATTNING_SVAR_JSON_ID_31 = "funktionsnedsattning";

    public static final String AKTIVITETSBEGRANSNING_SVAR_ID_4 = "4";
    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_4 = "aktivitetsbegransning";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID_41 = "4.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_41 = "aktivitetsbegransning";

    public static final String FORHINDER_SVAR_ID_5 = "5";
    public static final String FORHINDER_SVAR_JSON_ID_5 = "harForhinder";
    public static final String FORHINDER_DELSVAR_ID_51 = "5.1";
    public static final String FORHINDER_SVAR_JSON_ID_51 = "harForhinder";

    public static final String SJUKFRANVARO_SVAR_ID_6 = "6";
    public static final String SJUKFRANVARO_SVAR_JSON_ID_6 = "sjukfranvaro";
    public static final String SJUKFRANVARO_DELSVAR_ID_61 = "6.1";
    public static final String SJUKFRANVARO_SVAR_JSON_ID_61 = "niva";
    public static final String SJUKFRANVARO_DELSVAR_ID_62 = "6.2";
    public static final String SJUKFRANVARO_SVAR_JSON_ID_62 = "period";
    public static final String SJUKFRANVARO_SVAR_JSON_ID_62_FROM = "from";
    public static final String SJUKFRANVARO_SVAR_JSON_ID_62_TOM = "tom";
    public static final String SJUKFRANVARO_SVAR_JSON_CHECKED = "checked";

    public static final String BEGRANSNING_SJUKFRANVARO_SVAR_ID_7 = "7";
    public static final String BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_7 = "begransningSjukfranvaro";
    public static final String BEGRANSNING_SJUKFRANVARO_DELSVAR_ID_71 = "7.1";
    public static final String BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_71 = "kanBegransas";
    public static final String BEGRANSNING_SJUKFRANVARO_DELSVAR_ID_72 = "7.2";
    public static final String BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_72 = "beskrivning";

    public static final String PROGNOS_ATERGANG_SVAR_ID_8 = "8";
    public static final String PROGNOS_ATERGANG_SVAR_JSON_ID_8 = "prognosAtergang";
    public static final String PROGNOS_ATERGANG_DELSVAR_ID_81 = "8.1";
    public static final String PROGNOS_ATERGANG_SVAR_JSON_ID_81 = "prognos";
    public static final String PROGNOS_ATERGANG_DELSVAR_ID_82 = "8.2";
    public static final String PROGNOS_ATERGANG_SVAR_JSON_ID_82 = "anpassningar";

    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String TEXTVERSION_JSON_ID = "textVersion";
    public static final String ID_JSON_ID = "id";
    public static final String SIGNATURE = "signature";

    private AF00251RespConstants() {
    }

    public enum UnderlagsTyp {

        UNDERSOKNING("UNDERSOKNING", "Min undersökning av patienten"),
        TELEFONKONTAKT("TELEFONKONTAKT", "Min telefonkontakt med patienten"),
        JOURNALUPPGIFT("JOURNALUPPGIFT", "Journaluppgifter från den"),
        ANHORIG("ANHORIG", "Anhörigs beskrivning av patienten"),
        ANNAT("ANNAT", "Annat");

        private final String id;
        private final String label;

        UnderlagsTyp(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public static final String KODVERK = "FKMU_0001";

        @JsonValue
        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        @JsonCreator
        public static UnderlagsTyp fromId(@JsonProperty("id") String id) {
            String normId = id != null ? id.trim() : null;
            for (UnderlagsTyp typ : values()) {
                if (typ.id.equals(normId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
