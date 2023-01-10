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
package se.inera.intyg.common.luse.v1.model.converter;

public class RespConstants {

    public static final String GRUNDFORMU_CATEGORY_ID = "grundformu";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_CATEGORY_TEXT_ID = "KAT_1.RBK";

    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1 = "1";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1 = "1.3";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1 = "undersokningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1 = "journaluppgifter";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANHORIGS_BESKRIVNING_SVAR_JSON_ID_1 = "anhorigsBeskrivningAvPatienten";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1 = "annatGrundForMU";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1 = "annatGrundForMUBeskrivning";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1 = "motiveringTillInteBaseratPaUndersokning";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_SVAR_TEXT_ID = "FRG_1.RBK";
    public static final String GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_TEXT = "DFR_1.3.RBK";
    public static final String KANNEDOM_SVAR_ID_2 = "2";
    public static final String KANNEDOM_SVAR_TEXT_ID = "FRG_2.RBK";
    public static final String KANNEDOM_SVAR_JSON_ID_2 = "kannedomOmPatient";


    public static final String UNDERLAGFINNS_SVAR_ID_3 = "3";
    public static final String UNDERLAGFINNS_SVAR_JSON_ID_3 = "underlagFinns";

    public static final String UNDERLAG_SVAR_ID_4 = "4";
    public static final String UNDERLAG_SVAR_JSON_ID_4 = "underlag";

    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1 = "1.4";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_TEXT = "Motivering till varför det medicinska underlaget "
        + "inte baseras på en undersökning av patienten:";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DESCRIPTION_ID = "FRG_25.RBK";
    public static final String MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DESCRIPTION = "Detta är ett stöd för ifyllnad och ingen fråga"
        + " från Försäkringskassan. Informationen du anger här kan vara till hjälp för Försäkringskassan i deras handläggning. "
        + "Informationen du anger nedan, kommer att överföras till fältet \"{0}\" vid signering.";
    public static final String UNDERLAGFINNS_SELECTED_TEXT = "Ja";
    public static final String UNDERLAGFINNS_UNSELECTED_TEXT = "Nej";
    public static final String UNDERLAGFINNS_SVAR_TEXT = "FRG_3.RBK";

    public static final String UNDERLAG_TYPE_TEXT_ID = "FRG_4.RBK";
    public static final String UNDERLAG_DATUM_TEXT = "Datum";
    public static final String UNDERLAG_INFORMATION_SOURCE_TEXT_ID = "DFR_4.3.RBK";
    public static final String UNDERLAG_INFORMATION_SOURCE_DESCRIPTION_ID = "DFR_4.3.HLP";

    public static final String DIAGNOS_CATEGORY_ID = "diagnos";
    public static final String DIAGNOS_CATEGORY_TEXT_ID = "KAT_4.RBK";
    public static final String DIAGNOS_SVAR_TEXT_ID = "FRG_6.RBK";
    public static final String DIAGNOS_SVAR_ID = "6";
    public static final String DIAGNOS_ICD_10_ID = "ICD_10_SE";
    public static final String DIAGNOS_ICD_10_LABEL = "ICD-10-SE";
    public static final String DIAGNOS_KSH_97_ID = "KSH_97_P";
    public static final String DIAGNOS_KSH_97_LABEL = "KSH97-P (Primärvård)";

    public static final String DIAGNOSGRUND_SVAR_ID = "7";
    public static final String DIAGNOSGRUND_SVAR_TEXT_ID = "FRG_7.RBK";

    public static final String DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID = "45";
    public static final String DIAGNOSGRUND_NY_BEDOMNING_SVAR_TEXT_ID = "FRG_45.RBK";
    public static final String DIAGNOSGRUND_NY_BEDOMNING_SVAR_DESCRIPTION_ID = "FRG_45.HLP";
    public static final String DIAGNOSGRUND_NY_BEDOMNING_SELECTED_TEXT = "Ja";
    public static final String DIAGNOSGRUND_NY_BEDOMNING_UNSELECTED_TEXT = "Nej";

    public static final String DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_ID = "45.2";
    public static final String DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_TEXT_ID = "DFR_45.2.RBK";
    public static final String DIAGNOSGRUND_SVAR_JSON_ID = "diagnosgrund";
    public static final String DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID = "DiagnosgrundNyBedomning";
    public static final String DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_JSON_ID = "diagnosgrundForNyBedomning";

}
