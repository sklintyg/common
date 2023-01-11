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
package se.inera.intyg.common.luae_fs.v1.model.converter;

public class RespConstants {

    public static final String GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM = "KV_FKMU_0001";
    public static final String GRUNDFORMU_CATEGORY_ID = "grundformu";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT_ID = "KAT_1.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1 = "1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1 = "baseratPa";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID = "FRG_1.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1 = "1.1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1 = "1.2";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1 = "1.3";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT_ID = "DFR_1.3.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1 = "undersokningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1 = "journaluppgifter";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1 = "anhorigsBeskrivningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1 = "annatGrundForMU";
    public static final String GRUNDFORMU_UNDERSOKNING_LABEL_ID = "KV_FKMU_0001.UNDERSOKNING.RBK";
    public static final String GRUNDFORMU_JOURNALUPPGIFTER_LABEL_ID = "KV_FKMU_0001.JOURNALUPPGIFTER.RBK";
    public static final String GRUNDFORMU_ANHORIG_BESKRIVNING_LABEL_ID = "KV_FKMU_0001.ANHORIG.RBK";
    public static final String GRUNDFORMU_ANNAT_LABEL_ID = "KV_FKMU_0001.ANNAT.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1 = "annatGrundForMUBeskrivning";

    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1 = "motiveringTillInteBaseratPaUndersokning";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1 = "1.4";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DESCRIPTION_ID = "FRG_25.RBK";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_TEXT = "Motivering till varför det medicinska underlaget "
        + "inte baseras på en undersökning av patienten:";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DESCRIPTION = "Detta är ett stöd för ifyllnad och ingen fråga"
        + " från Försäkringskassan. Informationen du anger här kan vara till hjälp för Försäkringskassan i deras handläggning. "
        + "Informationen du anger nedan, kommer att överföras till fältet \"{0}\" vid signering.";

    public static final String KANNEDOM_SVAR_ID_2 = "2";
    public static final String KANNEDOM_DELSVAR_ID_2 = "2.1";
    public static final String KANNEDOM_SVAR_JSON_ID_2 = "kannedomOmPatient";
    public static final String KANNEDOM_SVAR_TEXT_ID = "FRG_2.RBK";

    public static final String UNDERLAGFINNS_SVAR_ID_3 = "3";
    public static final String UNDERLAGFINNS_SVAR_JSON_ID_3 = "underlagFinns";
    public static final String UNDERLAGFINNS_SVAR_TEXT_ID = "FRG_3.RBK";
    public static final String UNDERLAGFINNS_DELSVAR_ID_3 = "3.1";
    public static final String UNDERLAGFINNS_SELECTED_TEXT = "Ja";
    public static final String UNDERLAGFINNS_UNSELECTED_TEXT = "Nej";

    public static final String UNDERLAG_CODE_SYSTEM = "KV_FKMU_0005";
    public static final String UNDERLAG_SVAR_ID_4 = "4";
    public static final String UNDERLAG_SVAR_JSON_ID_4 = "underlag";
    public static final String UNDERLAG_TYPE_TEXT_ID = "FRG_4.RBK";
    public static final String UNDERLAG_DATUM_TEXT = "Datum";
    public static final String UNDERLAG_INFORMATION_SOURCE_TEXT_ID = "DFR_4.3.RBK";
    public static final String UNDERLAG_INFORMATION_SOURCE_DESCRIPTION_ID = "DFR_4.3.HLP";
    public static final String UNDERLAG_TYP_DELSVAR_ID_4 = "4.1";
    public static final String UNDERLAG_DATUM_DELSVAR_ID_4 = "4.2";
    public static final String UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4 = "4.3";

    public static final String DIAGNOS_CATEGORY_ID = "diagnos";
    public static final String DIAGNOS_SVAR_ID_6 = "6";
    public static final String DIAGNOS_CATEGORY_TEXT_ID = "KAT_3.RBK";
    public static final String DIAGNOS_SVAR_TEXT_ID = "FRG_6.RBK";
    public static final String FUNKTIONSNEDSATTNING_CATEGORY_ID = "funktionsnedsattning";
    public static final String FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID = "KAT_4.RBK";
    public static final String FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15 = "15";
    public static final String FUNKTIONSNEDSATTNING_DEBUT_DELSVAR_ID_15 = "15.1";
    public static final String FUNKTIONSNEDSATTNING_DEBUT_SVAR_JSON_ID_15 = "funktionsnedsattningDebut";
    public static final String FUNKTIONSNEDSATTNING_DEBUT_TEXT_ID = "FRG_15.RBK";
    public static final String FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16 = "16";
    public static final String FUNKTIONSNEDSATTNING_PAVERKAN_DELSVAR_ID_16 = "16.1";
    public static final String FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16 = "funktionsnedsattningPaverkan";
    public static final String FUNKTIONSNEDSATTNING_PAVERKAN_TEXT_ID = "FRG_16.RBK";

    public static final String OVRIGT_CATEGORY_ID = "ovrigt";
    public static final String OVRIGT_CATEGORY_TEXT_ID = "KAT_5.RBK";
    public static final String OVRIGT_SVAR_ID_25 = "25";
    public static final String OVRIGT_DELSVAR_ID_25 = "25.1";
    public static final String OVRIGT_TEXT_ID = "FRG_25.RBK";
    public static final String OVRIGT_SVAR_JSON_ID_25 = "ovrigt";

    public static final String ANSWER_YES = "Ja";
    public static final String ANSWER_NOT_SELECTED = "Ej angivet";
    public static final String KONTAKT_CATEGORY_ID = "kontakt";
    public static final String KONTAKT_CATEGORY_TEXT_ID = "KAT_6.RBK";
    public static final String KONTAKT_ONSKAS_SVAR_ID_26 = "26";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID_26 = "26.1";
    public static final String KONTAKT_ONSKAS_SVAR_JSON_ID_26 = "kontaktMedFk";
    public static final String KONTAKT_ONSKAS_SVAR_TEXT_ID = "FRG_26.RBK";
    public static final String KONTAKT_ONSKAS_SVAR_DESCRIPTION_ID = "FRG_26.HLP";
    public static final String KONTAKT_ONSKAS_DELSVAR_TEXT_ID = "DFR_26.1.RBK";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26 = "26.2";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26 = "anledningTillKontakt";
    public static final String ANLEDNING_TILL_KONTAKT_DELSVAR_TEXT_ID = "DFR_26.2.RBK";
}
