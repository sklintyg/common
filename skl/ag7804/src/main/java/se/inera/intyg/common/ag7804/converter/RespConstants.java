/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
    public static final String YES_ID = "YES";
    public static final String NO_ID = "NO";
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
    public static final String ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_100 = "onskarFormedlaDiagnos";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100 = "onskarFormedlaDiagnos";
    public static final String ONSKAR_FORMEDLA_DIAGNOS_TEXT = "FRG_100.RBK";
    public static final String DIAGNOS_SVAR_ID_6 = "6";
    public static final String DIAGNOS_BESKRIVNING_DELSVAR_ID_6 = "6.1";
    public static final String DIAGNOS_DELSVAR_ID_6 = "6.2";
    public static final String BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6 = "6.3";
    public static final String BIDIAGNOS_1_DELSVAR_ID_6 = "6.4";
    public static final String BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6 = "6.5";
    public static final String BIDIAGNOS_2_DELSVAR_ID_6 = "6.6";
    public static final String DIAGNOS_SVAR_JSON_ID_6 = "diagnoser";
    public static final String DIAGNOS_CATEGORY_TEXT = "KAT_3.RBK";
    public static final String DIAGNOS_SVAR_TEXT = "FRG_6.RBK";
    public static final String DIAGNOS_SVAR_BESKRIVNING = "FRG_6.HLP";
    public static final String DIAGNOS_ICD_10_ID = "ICD_10_SE";
    public static final String DIAGNOS_ICD_10_LABEL = "ICD-10-SE";
    public static final String DIAGNOS_KSH_97_ID = "KSH_97_P";
    public static final String DIAGNOS_KSH_97_LABEL = "KSH97-P (Primärvård)";

    public static final String FUNKTIONSNEDSATTNING_CATEGORY_TEXT = "KAT_4.RBK";
    public static final String FUNKTIONSNEDSATTNING_SVAR_TEXT = "FRG_35.RBK";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_TEXT = "DFR_35.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING = "DFR_35.1.HLP";

    public static final String AKTIVITETSBEGRANSNING_SVAR_ID_17 = "17";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID_17 = "17.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17 = "aktivitetsbegransning";
    public static final String AKTIVITETSBEGRANSNING_SVAR_TEXT = "FRG_17.RBK";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_TEXT = "DFR_17.1.RBK";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING = "DFR_17.1.HLP";

    public static final String PAGAENDEBEHANDLING_SVAR_ID_19 = "19";
    public static final String PAGAENDEBEHANDLING_DELSVAR_ID_19 = "19.1";
    public static final String PAGAENDEBEHANDLING_SVAR_JSON_ID_19 = "pagaendeBehandling";

    public static final String PLANERADBEHANDLING_SVAR_ID_20 = "20";
    public static final String PLANERADBEHANDLING_DELSVAR_ID_20 = "20.1";
    public static final String PLANERADBEHANDLING_SVAR_JSON_ID_20 = "planeradBehandling";

    public static final String PAGAENDEBEHANDLING_SVAR_TEXT = "FRG_19.RBK";
    public static final String PAGAENDEBEHANDLING_DELSVAR_TEXT = "DFR_19.1.RBK";
    public static final String PLANERADBEHANDLING_SVAR_TEXT = "FRG_20.RBK";
    public static final String PLANERADBEHANDLING_DELSVAR_TEXT = "DFR_20.1.RBK";

    public static final String MEDICINSKABEHANDLINGAR_CATEGORY_TEXT = "KAT_5.RBK";

    public static final String OVRIGT_SVAR_ID_25 = "25";
    public static final String OVRIGT_DELSVAR_ID_25 = "25.1";
    public static final String OVRIGT_SVAR_JSON_ID_25 = "ovrigt";
    public static final String OVRIGT_CATEGORY_TEXT = "KAT_8.RBK";
    public static final String OVRIGT_SVAR_TEXT = "FRG_25.RBK";

    public static final String KONTAKT_CATEGORY_TEXT = "KAT_9.RBK";
    public static final String KONTAKT_ONSKAS_SVAR_ID_103 = "103";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID_103 = "103.1";
    public static final String KONTAKT_ONSKAS_SVAR_JSON_ID_103 = "kontaktMedAg";
    public static final String KONTAKT_ONSKAS_SVAR_TEXT = "FRG_103.RBK";
    public static final String KONTAKT_ONSKAS_DELSVAR_TEXT = "DFR_103.1.RBK";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103 = "anledningTillKontakt";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID_103 = "103.2";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT = "DFR_103.2.RBK";


    public static final String AVSTANGNING_SMITTSKYDD_SVAR_ID_27 = "27";
    public static final String AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27 = "27.1";
    public static final String AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27 = "avstangningSmittskydd";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_TEXT = "KAT_10.RBK";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_DESCRIPTION = "KAT_10.HLP";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_ID = "smittbararpenning";
    public static final String AVSTANGNING_SMITTSKYDD_QUESTION_LABEL = "FRG_27.RBK";

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

    public static final String BEDOMNING_CATEGORY_TEXT = "KAT_6.RBK";

    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32 = "32";
    public static final String BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32 = "32.1";
    public static final String BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32 = "32.2";
    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 = "sjukskrivningar";

    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_TEXT = "FRG_32.RBK";
    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_BESKRIVNING = "FRG_32.HLP";
    public static final String BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL = "KV_FKMU_0003.EN_FJARDEDEL.RBK";
    public static final String BEHOV_AV_SJUKSKRIVNING_HALFTEN = "KV_FKMU_0003.HALFTEN.RBK";
    public static final String BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL = "KV_FKMU_0003.TRE_FJARDEDEL.RBK";
    public static final String BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT = "KV_FKMU_0003.HELT_NEDSATT.RBK";

    public static final String ARBETSTIDSFORLAGGNING_SVAR_ID_33 = "33";
    public static final String ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33 = "33.1";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33 = "33.2";
    public static final String ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33 = "arbetstidsforlaggning";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33 = "arbetstidsforlaggningMotivering";
    public static final String ARBETSTIDSFORLAGGNING_SVAR_TEXT = "FRG_33.RBK";
    public static final String ARBETSTIDSFORLAGGNING_SVAR_BESKRIVNING = "FRG_33.HLP";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_TEXT = "DFR_33.2.RBK";

    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_TEXT = "FRG_37.RBK";
    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_BESKRIVNING = "FRG_37.HLP";

    public static final String ARBETSRESOR_SVAR_ID_34 = "34";
    public static final String ARBETSRESOR_OM_DELSVAR_ID_34 = "34.1";
    public static final String ARBETSRESOR_SVAR_JSON_ID_34 = "arbetsresor";
    public static final String ARBETSRESOR_SVAR_TEXT = "FRG_34.RBK";


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
    public static final String PROGNOS_SVAR_TEXT = "FRG_39.RBK";
    public static final String PROGNOS_SVAR_BESKRIVNING = "FRG_39.HLP";
    public static final String PROGNOS_SVAR_ATER_X_ANTAL_DAGAR = "KV_FKMU_0006.ATER_X_ANTAL_DGR.RBK";
    public static final String PROGNOS_SVAR_PROGNOS_OKLAR = "KV_FKMU_0006.PROGNOS_OKLAR.RBK";
    public static final String PROGNOS_SVAR_SANNOLIKT_INTE = "KV_FKMU_0006.SANNOLIKT_INTE.RBK";
    public static final String PROGNOS_SVAR_STOR_SANNOLIKHET = "KV_FKMU_0006.STOR_SANNOLIKHET.RBK";
    public static final String PROGNOS_DAGAR_30 = "KV_FKMU_0007.TRETTIO_DGR.RBK";
    public static final String PROGNOS_DAGAR_60 = "KV_FKMU_0007.SEXTIO_DGR.RBK";
    public static final String PROGNOS_DAGAR_90 = "KV_FKMU_0007.NITTIO_DGR.RBK";
    public static final String PROGNOS_DAGAR_180 = "KV_FKMU_0007.HUNDRAATTIO_DAGAR.RBK";
    public static final String PROGNOS_DAGAR_365 = "KV_FKMU_0007.TREHUNDRASEXTIOFEM_DAGAR.RBK";

    public static final String ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40 = "40";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40 = "40.1";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40 = "arbetslivsinriktadeAtgarder";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44 = "44";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44 = "44.1";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44 = "arbetslivsinriktadeAtgarderBeskrivning";
    public static final String ATGARDER_CATEGORY_TEXT = "KAT_7.RBK";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_TEXT = "FRG_40.RBK";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_TEXT = "FRG_44.RBK";


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
