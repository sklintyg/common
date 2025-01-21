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
package se.inera.intyg.common.lisjp.v1.model.converter;

public class RespConstants {

    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1 = "1";
    public static final String GRUNDFORMU_CATEGORY_ID = "grundformu";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID = "FRG_1.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_DESCRIPTION_ID = "FRG_1.HLP";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1 = "undersokningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1 = "telefonkontaktMedPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1 = "journaluppgifter";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1 = "annatGrundForMU";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT_ID = "DFR_1.3.RBK";

    public static final String GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1 = "annatGrundForMUBeskrivning";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1 = "1.2";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1 = "1.1";

    public static final String DIAGNOS_CATEGORY_ID = "diagnos";
    public static final String DIAGNOS_SVAR_ID_6 = "6";
    public static final String DIAGNOS_SVAR_TEXT_ID = "FRG_6.RBK";
    public static final String DIAGNOS_SVAR_DESCRIPTION_ID = "FRG_6.HLP";

    public static final String DIAGNOS_ICD_10_ID = "ICD_10_SE";
    public static final String DIAGNOS_ICD_10_LABEL = "ICD-10-SE";
    public static final String DIAGNOS_KSH_97_ID = "KSH_97_P";
    public static final String DIAGNOS_KSH_97_LABEL = "KSH97-P (Primärvård)";

    public static final String DESCRIPTION = "FRM_2.RBK";
    public static final String ANSWER_YES = "Ja";
    public static final String ANSWER_NO = "Nej";
    public static final String ANSWER_NOT_SELECTED = "Ej angivet";

    public static final String GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT_ID = "KAT_1.RBK";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1 = "motiveringTillInteBaseratPaUndersokning";

    public static final String DIAGNOS_SVAR_JSON_ID_6 = "diagnoser";
    public static final String DIAGNOS_CATEGORY_TEXT = "KAT_3.RBK";
    public static final String DIAGNOSES_LIST_ITEM_1_ID = "diagnoser[0].row";
    public static final String DIAGNOSES_LIST_ITEM_2_ID = "diagnoser[1].diagnoskod";
    public static final String DIAGNOSES_LIST_ITEM_3_ID = "diagnoser[2].diagnoskod";


    public static final String FUNKTIONSNEDSATTNING_CATEGORY_ID = "funktionsnedsattning";
    public static final String FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID = "KAT_4.RBK";

    public static final String AKTIVITETSBEGRANSNING_SVAR_ID_17 = "17";

    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17 = "aktivitetsbegransning";
    public static final String AKTIVITETSBEGRANSNING_SVAR_TEXT = "FRG_17.RBK";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_TEXT = "DFR_17.1.RBK";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_BESKRIVNING = "DFR_17.1.HLP";
    public static final String AKTIVITETSBEGRANSNING_ICF_INFO = "Välj enbart de svårigheter som påverkar patientens sysselsättning.";
    public static final String AKTIVITETSBEGRANSNING_ICF_COLLECTION = "Svårigheter som påverkar patientens sysselsättning:";
    public static final String AKTIVITETSBEGRANSNING_ICF_PLACEHOLDER = "Hur begränsar ovanstående patientens sysselsättning och i "
        + "vilken utsträckning?";


    public static final String MEDICINSKABEHANDLINGAR_CATEGORY_ID = "medicinskabehandlingar";
    public static final String MEDICINSKABEHANDLINGAR_CATEGORY_TEXT = "KAT_5.RBK";

    public static final String PAGAENDEBEHANDLING_SVAR_ID_19 = "19";
    public static final String PAGAENDEBEHANDLING_SVAR_JSON_ID_19 = "pagaendeBehandling";

    public static final String PLANERADBEHANDLING_SVAR_ID_20 = "20";
    public static final String PLANERADBEHANDLING_SVAR_JSON_ID_20 = "planeradBehandling";
    public static final String OVRIGT_CATEGORY_ID = "ovrigt";
    public static final String OVRIGT_SVAR_ID_25 = "25";
    public static final String OVRIGT_SVAR_JSON_ID_25 = "ovrigt";
    public static final String OVRIGT_CATEGORY_TEXT = "KAT_8.RBK";
    public static final String KONTAKT_CATEGORY_ID = "kontakt";
    public static final String KONTAKT_ONSKAS_SVAR_ID_26 = "26";
    public static final String KONTAKT_ONSKAS_SVAR_JSON_ID_26 = "kontaktMedFk";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26 = "anledningTillKontakt";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26 = "26.2";
    public static final String KONTAKT_ONSKAS_SVAR_TEXT = "FRG_26.RBK";
    public static final String KONTAKT_ONSKAS_SVAR_BESKRIVNING = "FRG_26.HLP";
    public static final String KONTAKT_ONSKAS_DELSVAR_TEXT = "DFR_26.1.RBK";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT = "DFR_26.2.RBK";
    public static final String KONTAKT_CATEGORY_TEXT = "KAT_9.RBK";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_ID = "smittbararpenning";
    public static final String AVSTANGNING_SMITTSKYDD_SVAR_ID_27 = "27";
    public static final String AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27 = "avstangningSmittskydd";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_TEXT = "KAT_10.RBK";
    public static final String AVSTANGNING_SMITTSKYDD_CATEGORY_DESCRIPTION = "KAT_10.HLP";
    public static final String AVSTANGNING_SMITTSKYDD_QUESTION_LABEL = "FRG_27.RBK";


    public static final String SYSSELSATTNING_CATEGORY_ID = "sysselsattning";
    public static final String TYP_AV_SYSSELSATTNING_SVAR_ID_28 = "28";
    public static final String SYSSELSATTNING_CATEGORY_TEXT = "KAT_2.RBK";
    public static final String SYSSELSATTNING_SVAR_TEXT = "FRG_28.RBK";
    public static final String SYSSELSATTNING_SVAR_BESKRIVNING = "FRG_28.HLP";
    public static final String SYSSELSATTNING_ARBETE = "KV_FKMU_0002.NUVARANDE_ARBETE.RBK";
    public static final String SYSSELSATTNING_STUDIER = "KV_FKMU_0002.STUDIER.RBK";
    public static final String SYSSELSATTNING_FORALDRALEDIG = "KV_FKMU_0002.FORALDRALEDIG.RBK";
    public static final String SYSSELSATTNING_ARBETSSOKANDE = "KV_FKMU_0002.ARBETSSOKANDE.RBK";


    public static final String NUVARANDE_ARBETE_SVAR_ID_29 = "29";
    public static final String NUVARANDE_ARBETE_SVAR_JSON_ID_29 = "nuvarandeArbete";
    public static final String NUVARANDE_ARBETE_SVAR_TEXT = "FRG_29.RBK";


    public static final String BEDOMNING_CATEGORY_ID = "bedomning";
    public static final String BEDOMNING_CATEGORY_TEXT = "KAT_6.RBK";

    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32 = "32";
    public static final String BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32 = "32.1";
    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 = "sjukskrivningar";
    public static final String MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID = "motiveringTillTidigtStartdatumForSjukskrivning";
    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_ID_TEXT = "FRG_32.RBK";
    public static final String BEHOV_AV_SJUKSKRIVNING_SVAR_BESKRIVNING = "FRG_32.HLP";
    public static final String BEHOV_AV_SJUKSKRIVNING_EN_FJARDEDEL = "KV_FKMU_0003.EN_FJARDEDEL.RBK";
    public static final String BEHOV_AV_SJUKSKRIVNING_HALFTEN = "KV_FKMU_0003.HALFTEN.RBK";
    public static final String BEHOV_AV_SJUKSKRIVNING_TRE_FJARDEDEL = "KV_FKMU_0003.TRE_FJARDEDEL.RBK";
    public static final String BEHOV_AV_SJUKSKRIVNING_HELT_NEDSATT = "KV_FKMU_0003.HELT_NEDSATT.RBK";

    public static final String ARBETSTIDSFORLAGGNING_SVAR_ID_33 = "33";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33 = "33.2";
    public static final String ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33 = "arbetstidsforlaggning";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33 = "arbetstidsforlaggningMotivering";
    public static final String ARBETSTIDSFORLAGGNING_SVAR_TEXT = "FRG_33.RBK";
    public static final String ARBETSTIDSFORLAGGNING_SVAR_BESKRIVNING = "FRG_33.HLP";
    public static final String ARBETSTIDSFORLAGGNING_MOTIVERING_TEXT = "DFR_33.2.RBK";

    public static final String ARBETSRESOR_SVAR_ID_34 = "34";
    public static final String ARBETSRESOR_SVAR_JSON_ID_34 = "arbetsresor";
    public static final String ARBETSRESOR_SVAR_TEXT = "FRG_34.RBK";

    public static final String FUNKTIONSNEDSATTNING_SVAR_ID_35 = "35";
    public static final String FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35 = "funktionsnedsattning";
    public static final String FUNKTIONSNEDSATTNING_SVAR_TEXT = "FRG_35.RBK";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_TEXT = "DFR_35.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING = "DFR_35.1.HLP";
    public static final String FUNKTIONSNEDSATTNING_ICF_INFO = "Välj enbart de problem som påverkar patienten.";
    public static final String FUNKTIONSNEDSATTNING_ICF_COLLECTION = "Problem som påverkar patientens möjlighet att utföra sin "
        + "sysselsättning:";
    public static final String FUNKTIONSNEDSATTNING_ICF_PLACEHOLDER = "Vad grundar sig bedömningen på? På vilket sätt och i vilken "
        + "utsträckning är patienten påverkad?";


    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37 = "37";
    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37 = "forsakringsmedicinsktBeslutsstod";
    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_TEXT = "FRG_37.RBK";
    public static final String FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_BESKRIVNING = "FRG_37.HLP";

    public static final String PROGNOS_SVAR_ID_39 = "39";
    public static final String PROGNOS_BESKRIVNING_DELSVAR_ID_39 = "39.1";
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

    public static final String ATGARDER_CATEGORY_ID = "atgarder";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40 = "40";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40 = "arbetslivsinriktadeAtgarder";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44 = "44";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44 = "arbetslivsinriktadeAtgarderBeskrivning";
    public static final String ATGARDER_CATEGORY_TEXT = "KAT_7.RBK";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_TEXT = "FRG_40.RBK";
    public static final String ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_TEXT = "FRG_44.RBK";

    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String ID_JSON_ID = "id";
    public static final String SIGNATURE = "signature";
}
