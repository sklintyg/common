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
package se.inera.intyg.common.ts_diabetes.v2.model.converter;

public final class RespConstants {

    // Kat 1 - Intyget avser
    public static final String INTYG_AVSER_CATEGORY_ID = "intygAvser";
    public static final String INTYG_AVSER_CATEGORY_TEXT_ID = "KAT_99.RBK";
    public static final String INTYG_AVSER_SVAR_ID = "1";

    // Kat 2 - Identitet
    public static final String IDENTITET_CATEGORY_ID = "identitetStyrktGenom";
    public static final String IDENTITET_CATEGORY_TEXT_ID = "KAT_100.RBK";
    public static final String IDENTITET_STYRKT_GENOM_SVAR_ID = "2";

    // Kat 3 - Allmänt
    public static final String ALLMANT_CATEGORY_ID = "allmant";
    public static final String ALLMANT_CATEGORY_TEXT_ID = "KAT_1.RBK";

    public static final String ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID = "35";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_TEXT_ID = "FRG_35.RBK";

    public static final String ALLMANT_TYP_AV_DIABETES_SVAR_ID = "18";
    public static final String ALLMANT_TYP_AV_DIABETES_TEXT_ID = "FRG_18.RBK";

    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID = "18.2";
    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID = "beskrivningAnnanTypAvDiabetes";

    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID = "207";

    public static final String SVAR_JA_TEXT_ID = "SVAR_JA.RBK";
    public static final String SVAR_NEJ_TEXT_ID = "SVAR_NEJ.RBK";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID = "208";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID = "medicineringMedforRiskForHypoglykemi";
    public static final String ALLMANT_BEHANDLING_SVAR_ID = "209";
    public static final String ALLMANT_BEHANDLING_TEXT_ID = "FRG_19.RBK";
    public static final String ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID = "209.1";
    public static final String ALLMANT_BEHANDLING_INSULIN_TEXT_ID = "DFR_19.4.RBK";
    public static final String ALLMANT_BEHANDLING_ANNAN_DELSVAR_ID = "209.3";
    public static final String ALLMANT_BEHANDLING_ANNAN_TEXT_ID = "DFR_19.5.RBK";

    // Kat 4 - Hypoglykemi
    public static final String HYPOGLYKEMI_CATEGORY_ID = "hypoglykemi";
    public static final String HYPOGLYKEMI_CATEGORY_TEXT_ID = "KAT_2.RBK";
    public static final String HYPOGLYKEMI_KUNSKAP_OM_ATGARDER_SVAR_ID = "200";
    public static final String HYPOGLYKEMI_KUNSKAP_OM_ATGARDER_SVAR_TEXT_ID = "FRG_36.RBK";
    public static final String HYPOGLYKEMI_TECKEN_NEDSATT_HJARNFUNKTION_SVAR_ID = "201";
    public static final String HYPOGLYKEMI_TECKEN_NEDSATT_HJARNFUNKTION_SVAR_TEXT_ID = "FRG_37.RBK";
    public static final String HYPOGLYKEMI_SAKNAR_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID = "202";
    public static final String HYPOGLYKEMI_SAKNAR_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_TEXT_ID = "FRG_38.RBK";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_SVAR_ID = "203";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_SVAR_TEXT_ID = "FRG_39.RBK";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_BESKRIVNING_SVAR_ID = "203.1";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_BESKRIVNING_SVAR_TEXT_ID = "DFR_39.2.RBK";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_TRAFIKEN_SVAR_ID = "204";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_TRAFIKEN_SVAR_TEXT_ID = "FRG_40.RBK";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_TRAFIKEN_BESKRIVNING_SVAR_ID = "205";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_TRAFIKEN_BESKRIVNING_SVAR_TEXT_ID = "DFR_40.2.RBK";
    public static final String HYPOGLYKEMI_EGENKONTROLL_BLODSOCKER_SVAR_ID = "206";
    public static final String HYPOGLYKEMI_EGENKONTROLL_BLODSOCKER_SVAR_TEXT_ID = "FRG_41.RBK";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_VAKEN_TID_SVAR_ID = "207";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_VAKEN_TID_SVAR_TEXT_ID = "FRG_42.RBK";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_VAKEN_TID_OBSERVATIONSTID_SVAR_ID = "208";
    public static final String HYPOGLYKEMI_ALLVARLIG_FOREKOMST_VAKEN_TID_OBSERVATIONSTID_SVAR_TEXT_ID = "DFR_42.2.RBK";
    // Kat 5 - Syn
    public static final String SYN_CATEGORY_ID = "syn";
    public static final String SYN_CATEGORY_TEXT_ID = "KAT_3.RBK";
    public static final String SYN_SEPARAT_OGONLAKARINTYG_SVAR_ID = "309";
    public static final String SYN_SEPARAT_OGONLAKARINTYG_SVAR_TEXT_ID = "FRG_43.RBK";
    public static final String SYN_UTAN_ANMARKNING_SVAR_ID = "310";
    public static final String SYN_UTAN_ANMARKNING_SVAR_TEXT_ID = "FRG_44.RBK";
    public static final String SYN_VARDEN_SYNSKARPA_SVAR_ID = "311";
    public static final String SYN_VARDEN_SYNSKARPA_SVAR_TEXT_ID = "FRG_8.RBK";
    public static final String SYN_DUBBELSEENDE_SVAR_ID = "312";
    public static final String SYN_DUBBELSEENDE_SVAR_TEXT_ID = "FRG_6.RBK";
    public static final String HOGER_OGA_LABEL = "Höger öga";
    public static final String VANSTER_OGA_LABEL = "Vänster öga";
    public static final String BINOKULART_LABEL = "Binokulärt";
    public static final String UTAN_KORREKTION_TEXT = "Utan korrektion";
    public static final String MED_KORREKTION_TEXT = "Med korrektion";
    public static final String SYNSKARPA_TYP_ID = "synskarpaTyp";
    public static final String UTAN_KORREKTION_ID = "utanKorrektion";
    public static final String MED_KORREKTION_ID = "medKorrektion";

    // Kat 7 - Bedömning
    public static final String BEDOMNING_CATEGORY_ID = "bedomning";
    public static final String BEDOMNING_CATEGORY_TEXT_ID = "KAT_4.RBK";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID = "33.1";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_TEXT_ID = "DFR_33.1.RBK";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_HEADER_SVAR_ID = "33.0";
    public static final String BEDOMNING_LAKARE_SPECIAL_KOMPETENS_SVAR_ID = "34";
    public static final String BEDOMNING_LAKARE_SPECIAL_KOMPETENS_SVAR_TEXT_ID = "FRG_34.RBK";
    public static final String BEDOMNING_LAMPLIGHET_SVAR_ID = "35.0";
    public static final String BEDOMNING_LAMPLIGHET_SVAR_TEXT_ID = "FRG_45.RBK";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_HEADER_TEXT =
        "Patienten uppfyller kraven enligt 6 kap. i Transportstyrelsens "
            + "föreskrifter och allmänna råd (TSFS 2010:125) om medicinska krav för innehav av körkort m.m. Föreskrivna krav på läkarens "
            + "specialistkompetens vid diabetessjukdom anges i 17 kap.";

    // Kat 6 - Övrigt
    public static final String OVRIGT_CATEGORY_ID = "ovrigt";
    public static final String OVRIGT_CATEGORY_TEXT_ID = "FRG_32.RBK";
    public static final String OVRIGT_KOMMENTARER_SVAR_ID = "350";

    private RespConstants() {
    }
}
