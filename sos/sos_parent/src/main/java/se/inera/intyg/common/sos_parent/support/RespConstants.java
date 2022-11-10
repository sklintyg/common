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
package se.inera.intyg.common.sos_parent.support;

public final class RespConstants {

    public static final String ID_JSON_ID = "id";
    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String TEXTVERSION_JSON_ID = "textVersion";
    public static final String SIGNATURE = "signature";

    public static final String IDENTITET_STYRKT_QUESTION_TEXT_ID = "DFR_1.1.RBK";
    public static final String DODSDATUM_SAKERT_QUESTION_TEXT_ID = "FRG_2.RBK";
    public static final String DODSDATUM_SAKERT_QUESTION_DESCRIPTION_ID = "FRG_2.HLP";
    public static final String DODSDATUM_QUESTION_TEXT_ID = "DFR_2.2.RBK";
    public static final String DODSDATUM_SAKERT_QUESTION_SELECTED_TEXT = "SVAR_SAKERT.RBK";
    public static final String DODSDATUM_SAKERT_QUESTION_UNSELECTED_TEXT = "SVAR_EJ_SAKERT.RBK";
    public static final String ANTRAFFAD_DOD_QUESTION_TEXT_ID = "DFR_2.3.RBK";
    public static final String DODSPLATS_QUESTION_TEXT_ID = "FRG_3.RBK";
    public static final String DODSPLATS_KOMMUN_TEXT_ID = "DFR_3.1.RBK";
    public static final String DODSPLATS_BOENDE_QUESTION_TEXT_ID = "DFR_3.2.RBK";
    public static final String DODSPLATS_BOENDE_QUESTION_DESCRIPTION_ID = "DFR_3.2.HLP";
    public static final String BARN_QUESTION_TEXT_ID = "DFR_4.1.RBK";
    public static final String EXPLOSIVT_IMPLANTAT_QUESTION_TEXT_ID = "DFR_5.1.RBK";
    public static final String EXPLOSIVT_AVLAGSNAT_QUESTION_TEXT_ID = "DFR_5.2.RBK";
    public static final String LAND_QUESTION_TEXT_ID = "DFR_14.1.RBK";
    public static final String GRUNDER_QUESTION_TEXT_ID = "DFR_13.1.RBK";

    public static final String EXPLOSIVT_AVLAGSNAT_QUESTION_SELECTED_TEXT = "SVAR_JA.RBK";
    public static final String EXPLOSIVT_IMPLANTAT_QUESTION_SELECTED_TEXT = "SVAR_JA.RBK";
    public static final String EXPLOSIVT_AVLAGSNAT_QUESTION_UNSELECTED_TEXT = "SVAR_NEJ.RBK";
    public static final String EXPLOSIVT_IMPLANTAT_QUESTION_UNSELECTED_TEXT = "SVAR_NEJ.RBK";
    public static final String UNDERSOKNING_YTTRE_QUESTION_TEXT_ID = "DFR_6.1.RBK";
    public static final String UNDERSOKNING_DATUM_QUESTION_TEXT_ID = "DFR_6.3.RBK";
    public static final String POLISANMALAN_QUESTION_TEXT_ID = "DFR_7.1.RBK";
    public static final String POLISANMALAN_QUESTION_SELECTED_TEXT = "DFR_7.1.SVA_1.RBK";
    public static final String POLISANMALAN_QUESTION_UNSELECTED_TEXT = "SVAR_NEJ.RBK";
    public static final String IDENTITET_STYRKT_JSON_ID = "identitetStyrkt";
    public static final String DODSDATUM_JSON_ID = "dodsdatum";
    public static final String DODSDATUM_SAKERT_JSON_ID = "dodsdatumSakert";
    public static final String ANTRAFFAT_DOD_DATUM_JSON_ID = "antraffatDodDatum";
    public static final String DODSPLATS_BOENDE_JSON_ID = "dodsplatsBoende";
    public static final String DODSPLATS_KOMMUN_JSON_ID = "dodsplatsKommun";
    public static final String BARN_JSON_ID = "barn";
    public static final String BARN_QUESTION_SELECTED_QUESTION = "SVAR_JA.RBK";
    public static final String BARN_QUESTION_UNSELECTED_QUESTION = "SVAR_NEJ.RBK";
    public static final String EXPLOSIV_IMPLANTAT_JSON_ID = "explosivImplantat";
    public static final String EXPLOSIV_AVLAGSNAT_JSON_ID = "explosivAvlagsnat";
    public static final String UNDERSOKNING_YTTRE_JSON_ID = "undersokningYttre";
    public static final String UNDERSOKNING_DATUM_JSON_ID = "undersokningDatum";
    public static final String POLISANMALAN_JSON_ID = "polisanmalan";

    public static final String DODSORSAK_OM_JSON_ID = "beskrivning";
    public static final String DODSORSAK_DATUM_JSON_ID = "datum";
    public static final String DODSORSAK_SPECIFIKATION_JSON_ID = "specifikation";

    public static final String TERMINAL_DODSORSAK_JSON_ID = "terminalDodsorsak";
    public static final String FOLJD_JSON_ID = "foljd";
    public static final String BIDRAGANDE_SJUKDOM_JSON_ID = "bidragandeSjukdomar";
    public static final String OPERATION_OM_JSON_ID = "operation";
    public static final String OPERATION_DATUM_JSON_ID = "operationDatum";
    public static final String OPERATION_ANLEDNING_JSON_ID = "operationAnledning";
    public static final String FORGIFTNING_OM_JSON_ID = "forgiftning";
    public static final String FORGIFTNING_ORSAK_JSON_ID = "forgiftningOrsak";
    public static final String FORGIFTNING_DATUM_JSON_ID = "forgiftningDatum";
    public static final String FORGIFTNING_UPPKOMMELSE_JSON_ID = "forgiftningUppkommelse";
    public static final String GRUNDER_JSON_ID = "grunder";
    public static final String LAND_JSON_ID = "land";

    public static final String KOMPLETTERANDE_PATIENTUPPGIFTER_CATEGORY_ID = "kompletterandePatientuppgifter";
    public static final String DODSORSAKS_UPPGIFTER_CATEGORY_ID = "dodsorsaksUppgifter";
    public static final String KOMPLETTERANDE_PATIENTUPPGIFTER_TEXT_ID = "KAT_1.RBK";
    public static final String DODSDATUM_DODSPLATS_CATEGORY_ID = "dodsdatumDodsplats";
    public static final String DODSDATUM_DODSPLATS_TEXT_ID = "KAT_2.RBK";
    public static final String BARN_CATEGORY_ID = "barnKategori";
    public static final String BARN_CATEGORY_TEXT_ID = "KAT_3.RBK";
    public static final String BARN_AUTO_FILL_WITHIN_MESSAGE_ID = "DFR_4.1_INOM28.INFO";
    public static final String BARN_AUTO_FILL_AFTER_MESSAGE_ID = "DFR_4.1_EJ_INOM28.INFO";
    public static final String EXPLOSIVT_IMPLANTAT_CATEGORY_ID = "explosivtImplantat";
    public static final String EXPLOSIVT_IMPLANTAT_CATEGORY_TEXT_ID = "KAT_4.RBK";
    public static final String EXPLOSIVT_IMPLANTAT_CATEGORY_DESCRIPTION_ID = "KAT_4.HLP";
    public static final String UNDERSOKNING_YTTRE_CATEGORY_ID = "undersokningYttre";
    public static final String UNDERSOKNING_YTTRE_CATEGORY_TEXT_ID = "KAT_5.RBK";
    public static final String DODSORSAK_UPPGIFTER_TEXT_ID = "KAT_10.RBK";
    public static final String POLISANMALAN_CATEGORY_ID = "polisanmalan";
    public static final String POLISANMALAN_CATEGORY_TEXT_ID = "KAT_6.RBK";
    public static final String POLISANMALAN_CATEGORY_DESCRIPTION_ID = "KAT_6.HLP";
    public static final String POLISANMALAN_PRINT_MESSAGE_ID = "DFR_7.1.OBS";
    public static final String POLISANMALAN_PREFILL_MESSAGE_ID = "DFR_7.1_UNDERSOKNINGS_SKA_GORAS.INFO";

    public static final String OPERATION_CATEGORY_ID = "operation";
    public static final String OPERATION_CATEGORY_TEXT_ID = "KAT_8.RBK";
    public static final String OPERATION_CATEGORY_DECRIPTION_ID = "KAT_8.HLP";
    public static final String OPERATION_QUESTION_TEXT_ID = "DFR_11.1.RBK";
    public static final String OPERATION_QUESTION_SELECTED_TEXT_ID = "DFR_11.1.JA.RBK";
    public static final String OPERATION_QUESTION_UNSELECTED_TEXT_ID = "DFR_11.1.NEJ.RBK";
    public static final String OPERATION_QUESTION_UNKNOWN_TEXT_ID = "DFR_11.1.UPPGIFT_SAKNAS.RBK";
    public static final String OPERATION_DATUM_QUESTION_TEXT_ID = "DFR_11.2.RBK";
    public static final String OPERATION_ANLEDNING_QUESTION_TEXT_ID = "DFR_11.3.RBK";

    public static final String FORGIFTNING_CATEGORY_ID = "forgiftning";
    public static final String FORGIFTNING_CATEGORY_TEXT_ID = "KAT_9.RBK";
    public static final String FORGIFTNING_OM_QUESTION_TEXT_ID = "DFR_12.1.RBK";
    public static final String FORGIFTNING_ORSAK_QUESTION_TEXT_ID = "DFR_12.2.RBK";
    public static final String FORGIFTNING_ORSAK_QUESTION_DESCRIPTION_ID = "DFR_12.2.HLP";
    public static final String FORGIFTNING_DATUM_QUESTION_TEXT_ID = "DFR_12.3.RBK";
    public static final String FORGIFTNING_UPPKOMMELSE_QUESTION_TEXT_ID = "DFR_12.4.RBK";
    public static final String FORGIFTNING_UPPKOMMELSE_DESCRIPTION_TEXT_ID = "DFR_12.4.HLP";

    public static final String IDENTITET_STYRKT_SVAR_ID = "1";
    public static final String DODSDATUM_SVAR_ID = "2";
    public static final String DODSPLATS_SVAR_ID = "3";
    public static final String BARN_SVAR_ID = "4";
    public static final String EXPLOSIV_IMPLANTAT_SVAR_ID = "5";
    public static final String UNDERSOKNING_SVAR_ID = "6";
    public static final String POLISANMALAN_SVAR_ID = "7";
    public static final String DODSORSAK_SVAR_ID = "8";
    public static final String FOLJD_SVAR_ID = "9";
    public static final String BIDRAGANDE_SJUKDOM_SVAR_ID = "10";
    public static final String OPERATION_SVAR_ID = "11";
    public static final String FORGIFTNING_SVAR_ID = "12";

    public static final String GRUNDER_SVAR_ID = "13";
    public static final String LAND_SVAR_ID = "14";
    public static final String IDENTITET_STYRKT_DELSVAR_ID = "1.1";
    public static final String DODSDATUM_SAKERT_DELSVAR_ID = "2.1";
    public static final String DODSDATUM_DELSVAR_ID = "2.2";
    public static final String DODSDATUM_OSAKERT_DELSVAR_ID = "2.2.1";
    public static final String ANTRAFFAT_DOD_DATUM_DELSVAR_ID = "2.3";
    public static final String DODSPLATS_KOMMUN_DELSVAR_ID = "3.1";
    public static final String DODSPLATS_BOENDE_DELSVAR_ID = "3.2";
    public static final String BARN_DELSVAR_ID = "4.1";
    public static final String BARN_AUTOFILL_WITHIN_MESSAGE_DELSVAR_ID = "4.1.1";
    public static final String BARN_AUTOFILL_AFTER_MESSAGE_DELSVAR_ID = "4.1.2";
    public static final String EXPLOSIV_IMPLANTAT_DELSVAR_ID = "5.1";
    public static final String EXPLOSIV_AVLAGSNAT_DELSVAR_ID = "5.2";
    public static final String UNDERSOKNING_YTTRE_DELSVAR_ID = "6.1";
    public static final String UNDERSOKNING_DETALJER_DELSVAR_ID = "6.2";
    public static final String UNDERSOKNING_DATUM_DELSVAR_ID = "6.3";
    public static final String POLISANMALAN_DELSVAR_ID = "7.1";
    public static final String POLISANMALAN_PREFILL_MESSAGE_DELSVAR_ID = "7.1.1";
    public static final String POLISANMALAN_PRINT_MESSAGE_DELSVAR_ID = "7.1.2";
    public static final String DODSORSAK_DELSVAR_ID = "8.1";
    public static final String DODSORSAK_DATUM_DELSVAR_ID = "8.2";
    public static final String DODSORSAK_SPECIFIKATION_DELSVAR_ID = "8.3";
    public static final String FOLJD_OM_DELSVAR_ID = "9.1";
    public static final String FOLJD_DATUM_DELSVAR_ID = "9.2";
    public static final String FOLJD_SPECIFIKATION_DELSVAR_ID = "9.3";
    public static final String BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID = "10.1";
    public static final String BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID = "10.2";
    public static final String BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID = "10.3";
    public static final String OPERATION_OM_DELSVAR_ID = "11.1";
    public static final String OPERATION_DATUM_DELSVAR_ID = "11.2";
    public static final String OPERATION_ANLEDNING_DELSVAR_ID = "11.3";
    public static final String FORGIFTNING_OM_DELSVAR_ID = "12.1";
    public static final String FORGIFTNING_ORSAK_DELSVAR_ID = "12.2";
    public static final String FORGIFTNING_DATUM_DELSVAR_ID = "12.3";
    public static final String FORGIFTNING_UPPKOMMELSE_DELSVAR_ID = "12.4";
    public static final String FORGIFTNING_OM_QUESTION_SELECTED_TEXT = "SVAR_JA.RBK";
    public static final String FORGIFTNING_OM_QUESTION_UNSELECTED_TEXT = "SVAR_NEJ.RBK";
    public static final String GRUNDER_DELSVAR_ID = "13.1";
    public static final String LAND_DELSVAR_ID = "14.1";

    public static final String DODSPLATS_BOENDE_CODE_SYSTEM = "65f0069f-14b5-4634-b187-5193580a3349"; //kv dödsplats boende
    public static final String UNDERSOKNING_DETALJER_CODE_SYSTEM = "da46dd8c-b3f1-4e39-8d62-777d069213ea"; //kv detaljer undersökning
    public static final String GRUNDER_CODE_SYSTEM = "f9051865-b97c-4ee3-98ab-db2b7a11176e"; //kv dödsorsaksuppgifter
    public static final String FORGIFTNING_ORSAK_CODE_SYSTEM = "e5d1d1fb-d918-47b4-9e69-483d3be523ee"; //kv orsak

    public static final String UPPGIFT_SAKNAS_CODE = "NI";
    public static final String UPPGIFT_SAKNAS_DISPLAY_NAME = "Uppgift saknas";

    public static final int TWENTY_EIGHT_DAYS = 28;
    public static final String TO_EPOCH_DAY = "toEpochDay";

    private RespConstants() {
    }
}
