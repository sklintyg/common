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
package se.inera.intyg.common.ag7804.converter;

public final class RespConstants {

    public static final String DESCRIPTION = "FRM_2.RBK";
    public static final String ANSWER_YES = "Ja";
    public static final String ANSWER_NO = "Nej";
    public static final String ANSWER_NOT_SELECTED = "Ej angivet";

    public static final String CATEGORY_GRUNDFORMU = "grundformu";
    public static final String CATEGORY_DIAGNOS = "diagnos";
    public static final String CATEGORY_SYSSELSATTNING = "sysselsattning";
    public static final String CATEGORY_FUNKTIONSNEDSATTNING = "funktionsnedsattning";
    public static final String CATEGORY_MEDICINSKABEHANDLINGAR = "medicinskabehandlingar";
    public static final String CATEGORY_BEDOMNING = "bedomning";
    public static final String CATEGORY_ATGARDER = "atgarder";
    public static final String CATEGORY_OVRIGT = "ovrigt";
    public static final String CATEGORY_KONTAKT = "kontakt";

    public static final String GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT = "KAT_1.RBK";
    public static final String GRUNDFORMU_CATEGORY_ID = "grundformu";

    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1 = "1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1 = "baseratPa";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1 = "1.1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1 = "1.2";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1 = "1.3";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1 = "undersokningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1 = "journaluppgifter";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1 = "annatGrundForMU";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1 = "telefonkontaktMedPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1 = "annatGrundForMUBeskrivning";

    public static final String GRUNDFORMU_JOURNALUPPGIFTER_LABEL = "KV_FKMU_0001.JOURNALUPPGIFTER.RBK";
    public static final String GRUNDFORMU_TELEFONKONTAKT_LABEL = "KV_FKMU_0001.TELEFONKONTAKT.RBK";
    public static final String GRUNDFORMU_UNDERSOKNING_LABEL = "KV_FKMU_0001.UNDERSOKNING.RBK";
    public static final String GRUNDFORMU_ANNAT_LABEL = "KV_FKMU_0001.ANNAT.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT = "FRG_1.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_BESKRIVNING = "FRG_1.HLP";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT = "DFR_1.3.RBK";

    public static final String ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100 = "100";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_100 = "100.1";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100 = "onskarFormedlaDiagnos";
    public static final String DIAGNOS_SVAR_ID_6 = "6";
    public static final String DIAGNOS_BESKRIVNING_DELSVAR_ID_6 = "6.1";
    public static final String DIAGNOS_DELSVAR_ID_6 = "6.2";
    public static final String BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6 = "6.3";
    public static final String BIDIAGNOS_1_DELSVAR_ID_6 = "6.4";
    public static final String BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6 = "6.5";
    public static final String BIDIAGNOS_2_DELSVAR_ID_6 = "6.6";
    public static final String DIAGNOS_SVAR_JSON_ID_6 = "diagnoser";


    public static final String AKTIVITETSBEGRANSNING_SVAR_ID_17 = "17";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID_17 = "17.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17 = "aktivitetsbegransning";


    public static final String PAGAENDEBEHANDLING_SVAR_ID_19 = "19";
    public static final String PAGAENDEBEHANDLING_DELSVAR_ID_19 = "19.1";
    public static final String PAGAENDEBEHANDLING_SVAR_JSON_ID_19 = "pagaendeBehandling";

    public static final String PLANERADBEHANDLING_SVAR_ID_20 = "20";
    public static final String PLANERADBEHANDLING_DELSVAR_ID_20 = "20.1";
    public static final String PLANERADBEHANDLING_SVAR_JSON_ID_20 = "planeradBehandling";


    public static final String OVRIGT_SVAR_ID_25 = "25";
    public static final String OVRIGT_DELSVAR_ID_25 = "25.1";
    public static final String OVRIGT_SVAR_JSON_ID_25 = "ovrigt";

    public static final String KONTAKT_ONSKAS_SVAR_ID_103 = "103";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID_103 = "103.1";
    public static final String KONTAKT_ONSKAS_SVAR_JSON_ID_103 = "kontaktMedAg";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103 = "anledningTillKontakt";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID_103 = "103.2";

    public static final String AVSTANGNING_SMITTSKYDD_SVAR_ID_27 = "27";
    public static final String AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27 = "27.1";
    public static final String AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27 = "avstangningSmittskydd";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_TEXT = "KAT_10.RBK";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_DESCRIPTION = "KAT_10.HLP";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_ID = "smittbararpenning";
    public static final String AVSTANGNING_SMITTSKYDD_QUESTION_LABEL = "FRG_27.RBK";

    public static final String SYSSELSATTNING_CATEGORY_ID = "sysselsattning";
    public static final String SYSSELSATTNING_CATEGORY_TEXT = "KAT_2.RBK";
    public static final String SYSSELSATTNING_SVAR_TEXT = "FRG_28.RBK";
    public static final String SYSSELSATTNING_SVAR_BESKRIVNING = "FRG_28.HLP";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_ID_28 = "28";
    public static final String TYP_AV_SYSSELSATTNING_DELSVAR_ID_28 = "28.1";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28 = "sysselsattning";
    public static final String SYSSELSATTNING_ARBETE = "KV_FKMU_0002.NUVARANDE_ARBETE.RBK";
    public static final String SYSSELSATTNING_STUDIER = "KV_FKMU_0002.STUDIER.RBK";
    public static final String SYSSELSATTNING_FORALDRALEDIG = "KV_FKMU_0002.FORALDRALEDIG.RBK";
    public static final String SYSSELSATTNING_ARBETSSOKANDE = "KV_FKMU_0002.ARBETSSOKANDE.RBK";

    public static final String NUVARANDE_ARBETE_SVAR_ID_29 = "29";
    public static final String NUVARANDE_ARBETE_DELSVAR_ID_29 = "29.1";
    public static final String NUVARANDE_ARBETE_SVAR_JSON_ID_29 = "nuvarandeArbete";
    public static final String NUVARANDE_ARBETE_SVAR_TEXT = "FRG_29.RBK";

    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32 = "32";
    public static final String BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32 = "32.1";
    public static final String BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32 = "32.2";
    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 = "sjukskrivningar";

    public static final String ARBETSTIDSFORLAGGNING_SVAR_ID_33 = "33";
    public static final String ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33 = "33.1";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33 = "33.2";
    public static final String ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33 = "arbetstidsforlaggning";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33 = "arbetstidsforlaggningMotivering";

    public static final String ARBETSRESOR_SVAR_ID_34 = "34";
    public static final String ARBETSRESOR_OM_DELSVAR_ID_34 = "34.1";
    public static final String ARBETSRESOR_SVAR_JSON_ID_34 = "arbetsresor";


    public static final String FUNKTIONSNEDSATTNING_SVAR_ID_35 = "35";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_ID_35 = "35.1";
    public static final String FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35 = "funktionsnedsattning";

    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37 = "37";
    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37 = "37.1";
    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37 = "forsakringsmedicinsktBeslutsstod";

    public static final String PROGNOS_SVAR_ID_39 = "39";
    public static final String PROGNOS_BESKRIVNING_DELSVAR_ID_39 = "39.1";
    public static final String PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39 = "39.3";
    public static final String PROGNOS_SVAR_JSON_ID_39 = "prognos";

    public static final String ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40 = "40";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40 = "40.1";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40 = "arbetslivsinriktadeAtgarder";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44 = "44";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44 = "44.1";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44 = "arbetslivsinriktadeAtgarderBeskrivning";


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
