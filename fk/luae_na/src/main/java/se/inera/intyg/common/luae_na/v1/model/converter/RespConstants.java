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
package se.inera.intyg.common.luae_na.v1.model.converter;

public class RespConstants {

    public static final String DIAGNOS_CATEGORY_ID = "diagnos";
    public static final String DIAGNOS_SVAR_ID_6 = "6";
    public static final String DIAGNOS_CATEGORY_TEXT_ID = "KAT_4.RBK";
    public static final String DIAGNOS_SVAR_TEXT_ID = "FRG_6.RBK";
    public static final String DIAGNOS_SVAR_DESCRIPTION_ID = "FRG_6.HLP";
    public static final String DIAGNOSGRUND_SVAR_ID_7 = "7";
    public static final String DIAGNOSGRUND_TEXT_ID = "FRG_7.RBK";
    public static final String DIAGNOSGRUND_DELSVAR_ID_7 = "7.1";
    public static final String NYDIAGNOS_SVAR_ID_45 = "45";
    public static final String DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45 = "45.1";
    public static final String DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45 = "45.2";

    public static final String DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT_ID = "FRG_45.RBK";
    public static final String DIAGNOSGRUND_NYBEDOMNING_SVAR_DESCRIPTION_ID = "FRG_45.HLP";

    public static final String DIAGNOSGRUND_NYUPPDATERING_SVAR_TEXT_ID = "DFR_45.2.RBK";
    public static final String DIAGNOSGRUND_SVAR_JSON_ID_7 = "diagnosgrund";
    public static final String DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45 = "nyBedomningDiagnosgrund";
    public static final String DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45 = "diagnosForNyBedomning";

    public static final String ANSWER_YES = "Ja";
    public static final String ANSWER_NOT_SELECTED = "Ej angivet";

    public static final String GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM = "KV_FKMU_0001";
    public static final String GRUNDFORMU_CATEGORY_ID = "grundformu";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1 = "1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1 = "1.1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1 = "1.2";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1 = "1.3";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1 = "undersokningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1 = "journaluppgifter";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1 = "anhorigsBeskrivningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1 = "annatGrundForMU";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1 = "annatGrundForMUBeskrivning";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1 = "motiveringTillInteBaseratPaUndersokning";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID = "FRG_1.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT = "DFR_1.3.RBK";
    public static final String GRUNDFORMU_JOURNALUPPGIFTER_LABEL = "KV_FKMU_0001.JOURNALUPPGIFTER.RBK";
    public static final String GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL = "KV_FKMU_0001.ANHORIG.RBK";
    public static final String GRUNDFORMU_UNDERSOKNING_LABEL = "KV_FKMU_0001.UNDERSOKNING.RBK";
    public static final String GRUNDFORMU_ANNAT_LABEL = "KV_FKMU_0001.ANNAT.RBK";


    public static final String KANNEDOM_SVAR_ID_2 = "2";
    public static final String KANNEDOM_DELSVAR_ID_2 = "2.1";
    public static final String KANNEDOM_SVAR_JSON_ID_2 = "kannedomOmPatient";


    public static final String UNDERLAGFINNS_SVAR_ID_3 = "3";

    public static final String UNDERLAGFINNS_DELSVAR_ID_3 = "3.1";
    public static final String UNDERLAGFINNS_SVAR_JSON_ID_3 = "underlagFinns";

    public static final String UNDERLAG_SVAR_ID_4 = "4";
    public static final String UNDERLAG_TYP_DELSVAR_ID_4 = "4.1";
    public static final String UNDERLAG_SVAR_JSON_ID_4 = "underlag";

    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1 = "1.4";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_TEXT = "Motivering till varför det medicinska underlaget "
        + "inte baseras på en undersökning av patienten:";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DESCRIPTION_ID = "FRG_25.RBK";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DESCRIPTION = "Detta är ett stöd för ifyllnad och ingen fråga"
        + " från Försäkringskassan. Informationen du anger här kan vara till hjälp för Försäkringskassan i deras handläggning. "
        + "Informationen du anger nedan, kommer att överföras till fältet \"{0}\" vid signering.";

    public static final String KANNEDOM_SVAR_TEXT = "FRG_2.RBK";
    public static final String UNDERLAGFINNS_SELECTED_TEXT = "Ja";
    public static final String UNDERLAGFINNS_UNSELECTED_TEXT = "Nej";
    public static final String UNDERLAGFINNS_SVAR_TEXT = "FRG_3.RBK";
    public static final String UNDERLAG_TYPE_TEXT_ID = "FRG_4.RBK";
    public static final String UNDERLAG_DATUM_TEXT = "Datum";
    public static final String UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4 = "4.3";
    public static final String UNDERLAG_DATUM_DELSVAR_ID_4 = "4.2";
    public static final String UNDERLAG_INFORMATION_SOURCE_TEXT_ID = "DFR_4.3.RBK";
    public static final String UNDERLAG_INFORMATION_SOURCE_DESCRIPTION_ID = "DFR_4.3.HLP";
    public static final String UNDERLAG_CODE_SYSTEM = "KV_FKMU_0005";
    public static final String BAKGRUND_CATEGORY_ID = "bakgrund";
    public static final String BAKGRUND_CATEGORY_TEXT_ID = "KAT_3.RBK";
    public static final String SJUKDOMSFORLOPP_SVAR_ID_5 = "5";
    public static final String SJUKDOMSFORLOPP_DELSVAR_ID_5 = "5.1";
    public static final String SJUKDOMSFORLOPP_SVAR_JSON_ID_5 = "sjukdomsforlopp";
    public static final String SJUKDOMSFORLOPP_QUESTION_TEXT_ID = "FRG_5.RBK";

    public static final String FUNKTIONSNEDSATTNING_CATEGORY_ID = "funktionsnedsattning";
    public static final String FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID = "KAT_5.RBK";
    public static final String FUNKTIONSNEDSATTNING_CATEGORY_DESCRIPTION_ID = "KAT_5.HLP";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8 = "8";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8 = "8.1";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_TEXT_ID = "FRG_8.RBK";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_DESCRIPTION_ID = "FRG_8.HLP";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_TEXT_ID = "DFR_8.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8 = "funktionsnedsattningIntellektuell";

    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9 = "9";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9 = "9.1";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_TEXT_ID = "FRG_9.RBK";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_DESCRIPTION_ID = "FRG_9.HLP";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_TEXT_ID = "DFR_9.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9 = "funktionsnedsattningKommunikation";

    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10 = "10";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10 = "10.1";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_TEXT_ID = "FRG_10.RBK";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_DESCRIPTION_ID = "FRG_10.HLP";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_TEXT_ID = "DFR_10.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10 = "funktionsnedsattningKoncentration";

    public static final String FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11 = "11";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11 = "11.1";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_TEXT_ID = "FRG_11.RBK";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_DESCRIPTION_ID = "FRG_11.HLP";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_TEXT_ID = "DFR_11.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11 = "funktionsnedsattningPsykisk";

    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12 = "12";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12 = "12.1";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_TEXT_ID = "FRG_12.RBK";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_DESCRIPTION_ID = "FRG_12.HLP";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_TEXT_ID = "DFR_12.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12 = "funktionsnedsattningSynHorselTal";

    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13 = "13";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13 = "13.1";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_TEXT_ID = "FRG_13.RBK";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DESCRIPTION_ID = "FRG_13.HLP";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_TEXT_ID = "DFR_13.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13 = "funktionsnedsattningBalansKoordination";

    public static final String FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14 = "14";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14 = "14.1";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_TEXT_ID = "FRG_14.RBK";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_DESCRIPTION_ID = "FRG_14.HLP";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_TEXT_ID = "DFR_14.1.RBK";
    public static final String FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14 = "funktionsnedsattningAnnan";

    public static final String MEDICINSKABEHANDLINGAR_CATEGORY_ID = "medicinskabehandlingar";
    public static final String MEDICINSKABEHANDLINGAR_CATEGORY_TEXT = "KAT_7.RBK";

    public static final String AVSLUTADBEHANDLING_SVAR_ID_18 = "18";
    public static final String AVSLUTADBEHANDLING_DELSVAR_ID_18 = "18.1";
    public static final String AVSLUTADBEHANDLING_SVAR_JSON_ID_18 = "avslutadBehandling";
    public static final String AVSLUTADBEHANDLING_SVAR_TEXT = "FRG_18.RBK";
    public static final String AVSLUTADBEHANDLING_DELSVAR_TEXT = "DFR_18.1.RBK";

    public static final String PAGAENDEBEHANDLING_SVAR_ID_19 = "19";
    public static final String PAGAENDEBEHANDLING_DELSVAR_ID_19 = "19.1";
    public static final String PAGAENDEBEHANDLING_SVAR_JSON_ID_19 = "pagaendeBehandling";
    public static final String PAGAENDEBEHANDLING_SVAR_TEXT = "FRG_19.RBK";
    public static final String PAGAENDEBEHANDLING_DELSVAR_TEXT = "DFR_19.1.RBK";

    public static final String PLANERADBEHANDLING_SVAR_ID_20 = "20";
    public static final String PLANERADBEHANDLING_DELSVAR_ID_20 = "20.1";
    public static final String PLANERADBEHANDLING_SVAR_JSON_ID_20 = "planeradBehandling";
    public static final String PLANERADBEHANDLING_SVAR_TEXT = "FRG_20.RBK";
    public static final String PLANERADBEHANDLING_DELSVAR_TEXT = "DFR_20.1.RBK";

    public static final String SUBSTANSINTAG_SVAR_ID_21 = "21";
    public static final String SUBSTANSINTAG_DELSVAR_ID_21 = "21.1";
    public static final String SUBSTANSINTAG_SVAR_JSON_ID_21 = "substansintag";
    public static final String SUBSTANSINTAG_SVAR_TEXT = "FRG_21.RBK";
    public static final String SUBSTANSINTAG_DELSVAR_TEXT = "DFR_21.1.RBK";

    public static final String AKTIVITETSBEGRANSNING_SVAR_ID_17 = "17";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID_17 = "17.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17 = "aktivitetsbegransning";
    public static final String AKTIVITETSBEGRANSNING_CATEGORY_ID = "aktivitetsbegransningar";
    public static final String AKTIVITETSBEGRANSNING_CATEGORY_TEXT_ID = "KAT_6.RBK";
    public static final String AKTIVITETSBEGRANSNING_SVAR_TEXT_ID = "FRG_17.RBK";
    public static final String AKTIVITETSBEGRANSNING_SVAR_DESCRIPTION_ID = "FRG_17.HLP";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_TEXT_ID = "DFR_17.1.RBK";

    public static final String CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE = "medicinskaforutsattningarforarbete";
    public static final String CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE_TEXT_ID = "KAT_8.RBK";

    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22 = "22";
    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22 = "22.1";
    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_TEXT_ID = "FRG_22.RBK";
    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_DESCRIPTION_ID = "FRG_22.HLP";
    public static final String MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22 = "medicinskaForutsattningarForArbete";

    public static final String FORMAGATROTSBEGRANSNING_SVAR_ID_23 = "23";
    public static final String FORMAGATROTSBEGRANSNING_DELSVAR_ID_23 = "23.1";
    public static final String FORMAGATROTSBEGRANSNING_TEXT_ID = "FRG_23.RBK";
    public static final String FORMAGATROTSBEGRANSNING_DESCRIPTION_ID = "FRG_23.HLP";
    public static final String FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23 = "formagaTrotsBegransning";

    public static final String FORSLAG_TILL_ATGARD_SVAR_ID_24 = "24";
    public static final String FORSLAG_TILL_ATGARD_DELSVAR_ID_24 = "24.1";
    public static final String FORSLAG_TILL_ATGARD_TEXT_ID = "FRG_24.RBK";
    public static final String FORSLAG_TILL_ATGARD_DESCRIPTION_ID = "FRG_24.HLP";
    public static final String FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24 = "forslagTillAtgard";

    public static final String OVRIGT_CATEGORY_ID = "ovrigt";
    public static final String OVRIGT_CATEGORY_TEXT_ID = "KAT_9.RBK";

    public static final String OVRIGT_SVAR_ID_25 = "25";
    public static final String OVRIGT_DELSVAR_ID_25 = "25.1";
    public static final String OVRIGT_TEXT_ID = "FRG_25.RBK";
    public static final String OVRIGT_SVAR_JSON_ID_25 = "ovrigt";

    public static final String KONTAKT_CATEGORY_ID = "kontakt";
    public static final String KONTAKT_CATEGORY_TEXT_ID = "KAT_10.RBK";
    public static final String KONTAKT_ONSKAS_SVAR_ID_26 = "26";
    public static final String KONTAKT_ONSKAS_SVAR_JSON_ID_26 = "kontaktMedFk";
    public static final String KONTAKT_ONSKAS_SVAR_TEXT_ID = "FRG_26.RBK";
    public static final String KONTAKT_ONSKAS_SVAR_DESCRIPTION_ID = "FRG_26.HLP";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID_26 = "26.1";
    public static final String KONTAKT_ONSKAS_DELSVAR_TEXT_ID = "DFR_26.1.RBK";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26 = "26.2";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26 = "anledningTillKontakt";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT_ID = "DFR_26.2.RBK";
}
