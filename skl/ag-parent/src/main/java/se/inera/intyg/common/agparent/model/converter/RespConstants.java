/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.agparent.model.converter;

public final class RespConstants {

    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String TEXTVERSION_JSON_ID = "textVersion";
    public static final String ID_JSON_ID = "id";
    public static final String SIGNATURE = "signature";

    public static final String TYP_AV_SYSSELSATTNING_CODE_SYSTEM = "KV_FKMU_0002";

    // Flytta ner till intyget sen!

    public static final String TYP_AV_SYSSELSATTNING_SVAR_ID_1 = "1";
    public static final String TYP_AV_SYSSELSATTNING_DELSVAR_ID_1 = "1.1";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1 = "sysselsattning";

    public static final String NUVARANDE_ARBETE_SVAR_ID_2 = "2";
    public static final String NUVARANDE_ARBETE_DELSVAR_ID_2 = "2.1";
    public static final String NUVARANDE_ARBETE_SVAR_JSON_ID_2 = "nuvarandeArbete";

    public static final String ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_3 = "3";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_3 = "3.1";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3 = "onskarFormedlaDiagnos";

    public static final String TYP_AV_DIAGNOS_SVAR_ID_4 = "4";
    public static final String TYP_AV_DIAGNOS_DELSVAR_ID_4 = "4.1";
    public static final String TYP_AV_DIAGNOS_SVAR_JSON_ID_4 = "typAvDiagnos";

    public static final String NEDSATT_ARBETSFORMAGA_SVAR_ID_5 = "5";
    public static final String NEDSATT_ARBETSFORMAGA_DELSVAR_ID_5 = "5.1";
    public static final String NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5 = "nedsattArbetsformaga";

    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6 = "6";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1 = "6.1";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_2 = "6.2";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_1 = "arbetsformagaTrotsSjukdom";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_2 = "arbetsformagaTrotsSjukdomBeskrivning";

    private RespConstants() {
    }

    public static String getJsonPropertyFromFrageId(String frageId) {
        switch (frageId) {
        default:
            throw new IllegalArgumentException("The supplied value of frageId " + frageId + " is not supported.");
        }
    }
}
