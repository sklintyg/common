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
package se.inera.intyg.common.ag114.model.converter;

/**
 * Created by marced on 2018-11-30.
 */
public final class RespConstants {
    public static final String CATEGORY_SYSSELSATTNING = "sysselsattning";
    public static final String CATEGORY_DIAGNOS = "diagnos";
    public static final String CATEGORY_ARBETSFORMAGA = "arbetsformaga";
    public static final String CATEGORY_BEDOMNING = "bedomning";
    public static final String CATEGORY_KONTAKT = "kontakt";

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
    public static final String TYP_AV_DIAGNOS_BESKRIVNING_DELSVAR_ID_4 = "4.1";
    public static final String TYP_AV_DIAGNOS_DELSVAR_ID_4 = "4.2";
    public static final String TYP_AV_DIAGNOS_SVAR_JSON_ID_4 = "diagnoser";

    public static final String NEDSATT_ARBETSFORMAGA_SVAR_ID_5 = "5";
    public static final String NEDSATT_ARBETSFORMAGA_DELSVAR_ID_5 = "5.1";
    public static final String NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5 = "nedsattArbetsformaga";

    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6 = "6";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1 = "6.1";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_2 = "6.2";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_1 = "arbetsformagaTrotsSjukdom";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_2 = "arbetsformagaTrotsSjukdomBeskrivning";

    public static final String OVRIGT_SVAR_ID_8 = "8";
    public static final String OVRIGT_DELSVAR_ID_8 = "8.1";
    public static final String OVRIGT_SVAR_JSON_ID_8 = "ovrigaUpplysningar";

    public static final String BEDOMNING_SVAR_ID_7 = "7";
    public static final String SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1 = "7.1";
    public static final String SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2 = "7.2";
    public static final String SJUKSKRIVNINGSGRAD_SVAR_JSON_ID_7_1 = "sjukskrivningsgrad";
    public static final String SJUKSKRIVNINGSPERIOD_SVAR_JSON_ID_7_2 = "sjukskrivningsperiod";
    public static final String SJUKSKRIVNINGSGRAD_UNIT_OF_MEASURE = "%";

    public static final String KONTAKT_ONSKAS_SVAR_ID_9 = "9";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID_9 = "9.1";
    public static final String KONTAKT_ONSKAS_SVAR_JSON_ID_9 = "kontaktMedArbetsgivaren";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_9 = "anledningTillKontakt";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID_9 = "9.2";

    private RespConstants() {
    }
}
