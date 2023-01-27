/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag114.v1.model.converter;

/**
 * Created by marced on 2018-11-30.
 */
public final class RespConstants {

    public static final String CATEGORY_GRUNDFORMU_ID = "grundformu";
    public static final String CATEGORY_SYSSELSATTNING_ID = "sysselsattning";
    public static final String CATEGORY_SYSSELSATTNING_TEXT_ID = "KAT_1.RBK";
    public static final String CATEGORY_DIAGNOS_ID = "diagnos";
    public static final String CATEGORY_DIAGNOS_TEXT_ID = "KAT_2.RBK";
    public static final String CATEGORY_ARBETSFORMAGA_ID = "arbetsformaga";
    public static final String CATEGORY_ARBETSFORMAGA_TEXT_ID = "KAT_3.RBK";
    public static final String CATEGORY_BEDOMNING_ID = "bedomning";
    public static final String CATEGORY_BEDOMNING_TEXT_ID = "KAT_4.RBK";
    public static final String CATEGORY_OVRIGT_ID = "ovrigt";
    public static final String CATEGORY_OVRIGT_TEXT_ID = "KAT_5.RBK";
    public static final String CATEGORY_KONTAKT_ID = "kontakt";
    public static final String CATEGORY_KONTAKT_TEXT_ID = "KAT_6.RBK";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_ID = "1";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_JSON_ID = "sysselsattning";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_LABEL_ID = "KV_FKMU_0002.NUVARANDE_ARBETE.RBK";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_TEXT_ID = "FRG_1.RBK";
    public static final String ANSWER_YES = "Ja";
    public static final String ANSWER_NO = "Nej";
    public static final String ANSWER_NOT_SELECTED = "Ej angivet";
    public static final String NUVARANDE_ARBETE_SVAR_ID = "2";
    public static final String NUVARANDE_ARBETE_SVAR_TEXT_ID = "FRG_2.RBK";
    public static final String NUVARANDE_ARBETE_SVAR_DESCRIPTION_ID = "FRG_2.HLP";
    public static final String NUVARANDE_ARBETE_SVAR_JSON_ID = "nuvarandeArbete";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID = "3";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_SVAR_TEXT_ID = "FRG_3.RBK";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_SVAR_DESCRIPTION_ID = "FRG_3.HLP";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID = "onskarFormedlaDiagnos";
    public static final String TYP_AV_DIAGNOS_SVAR_ID = "4";
    public static final String TYP_AV_DIAGNOS_SVAR_TEXT_ID = "FRG_4.RBK";
    public static final String NEDSATT_ARBETSFORMAGA_SVAR_ID = "5";
    public static final String NEDSATT_ARBETSFORMAGA_SVAR_TEXT_ID = "FRG_5.RBK";
    public static final String NEDSATT_ARBETSFORMAGA_SVAR_DESCRIPTION_ID = "FRG_5.HLP";
    public static final String NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID = "nedsattArbetsformaga";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID = "6";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_TEXT_ID = "FRG_6.RBK";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_DESCRIPTION_ID = "FRG_6.HLP";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID = "6.2";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_TEXT_ID = "DFR_6.2.RBK";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID = "arbetsformagaTrotsSjukdom";
    public static final String ARBETSFORMAGA_TROTS_SJUKDOM_BESKRIVNING_SVAR_JSON_ID = "arbetsformagaTrotsSjukdomBeskrivning";
    public static final String OVRIGT_SVAR_ID = "8";
    public static final String OVRIGT_SVAR_TEXT_ID = "FRG_8.RBK";
    public static final String OVRIGT_SVAR_JSON_ID = "ovrigaUpplysningar";
    public static final String BEDOMNING_SVAR_ID = "7";
    public static final String SJUKSKRIVNINGSGRAD_SVAR_ID = "7.1";
    public static final String SJUKSKRIVNINGSGRAD_HEADER_ID = "sjukskrivningsgradHeader";
    public static final String SJUKSKRIVNINGSGRAD_HEADER_TEXT_ID = "FRG_7.RBK";
    public static final String SJUKSKRIVNINGSGRAD_HEADER_DESCRIPTION_ID = "FRG_7.HLP";
    public static final String SJUKSKRIVNINGSPERIOD_DELSVAR_ID = "7.2";
    public static final String SJUKSKRIVNINGSGRAD_SVAR_JSON_ID = "sjukskrivningsgrad";
    public static final String SJUKSKRIVNINGSGRAD_SVAR_TEXT_ID = "DFR_7.1.RBK";
    public static final String SJUKSKRIVNINGSPERIOD_SVAR_JSON_ID = "sjukskrivningsperiod";
    public static final String SJUKSKRIVNINGSGRAD_UNIT_OF_MEASURE = "%";
    public static final String SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID = "7.2";
    public static final String SJUKSKRIVNINGSGRAD_PERIOD_SVAR_TEXT_ID = "DFR_7.2.RBK";
    public static final String SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_FROM_ID = "FROM";
    public static final String SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_TOM_ID = "TOM";
    public static final String SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID = "sjukskrivningsgradPeriod";
    public static final String SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_ID = "7.3";
    public static final String SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_TEXT_ID = "SKL-001.ALERT";
    public static final String KONTAKT_ONSKAS_SVAR_ID = "9";
    public static final String KONTAKT_ONSKAS_SVAR_JSON_ID = "kontaktMedArbetsgivaren";
    public static final String KONTAKT_ONSKAS_SVAR_LABEL_ID = "FRG_9.RBK";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID = "9.2";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID = "anledningTillKontakt";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT_ID = "DFR_9.2.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT_ID = "KAT_7.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID = "10";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID = "FRG_10.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_DESCRIPTION_TEXT_ID = "FRG_10.HLP";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID = "10.3";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID = "annatGrundForMUBeskrivning";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_TEXT_ID = "DFR_10.3.RBK";


    private RespConstants() {
    }

    public enum ReferensTyp {
        UNDERSOKNING("UNDERSOKNING", "Min undersökning av patienten"),
        TELEFONKONTAKT("TELEFONKONTAKT", "Min telefonkontakt med patienten"),
        JOURNAL("JOURNALUPPGIFTER", "Journaluppgifter från den"),
        ANNAT("ANNAT", "Annat");

        public final String transportId;
        public final String label;

        ReferensTyp(String transportId, String label) {
            this.transportId = transportId;
            this.label = label;
        }

        public static ReferensTyp byTransportId(String transportId) {
            String normId = transportId != null ? transportId.trim() : null;
            for (ReferensTyp referensTyp : values()) {
                if (referensTyp.transportId.equals(normId)) {
                    return referensTyp;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
