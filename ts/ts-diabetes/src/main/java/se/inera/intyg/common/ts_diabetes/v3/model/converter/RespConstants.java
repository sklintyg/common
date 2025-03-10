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
package se.inera.intyg.common.ts_diabetes.v3.model.converter;

public final class RespConstants {

    // Kat 1 - Intyget avser
    public static final String INTYGETAVSER_SVAR_ID = "1";
    public static final String INTYGETAVSER_DELSVAR_ID = "1.1";
    public static final String INTYGETAVSER_SVAR_JSON_ID = "intygAvser";

    public static final String INTYG_AVSER_CATEGORY_ID = "intygAvser";
    public static final String INTYG_AVSER_CATEGORY_TEXT_ID = "KAT_1.RBK";
    // Kat 2 - Identitet
    public static final String IDENTITET_CATEGORY_ID = "identitetStyrktGenom";
    public static final String IDENTITET_CATEGORY_TEXT_ID = "KAT_2.RBK";
    public static final String IDENTITET_STYRKT_GENOM_SVAR_ID = "2";
    public static final String IDENTITET_STYRKT_GENOM_DELSVAR_ID = "2.1";
    public static final String IDENTITET_STYRKT_GENOM_JSON_ID = "identitetStyrktGenom";

    // Kat 3 - Allmänt
    public static final String ALLMANT_CATEGORY_ID = "allmant";
    public static final String ALLMANT_CATEGORY_TEXT_ID = "KAT_3.RBK";
    public static final String ALLMANT_JSON_ID = "allmant";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID = "35";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_DELSVAR_ID = "35.1";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11 = "diabetesDiagnosAr";

    public static final String ALLMANT_TYP_AV_DIABETES_SVAR_ID = "18";
    public static final String ALLMANT_TYP_AV_DIABETES_DELSVAR_ID = "18.1";
    public static final String ALLMANT_TYP_AV_DIABETES_JSON_ID = "typAvDiabetes";
    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID = "18.2";
    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID = "beskrivningAnnanTypAvDiabetes";

    public static final String ALLMANT_BEHANDLING_SVAR_ID = "109";
    public static final String ALLMANT_BEHANDLING_JSON_ID = "behandling";
    public static final String ALLMANT_BEHANDLING_ENDAST_KOST_DELSVAR_ID = "109.1";
    public static final String ALLMANT_BEHANDLING_ENDAST_KOST_JSON_ID = "endastKost";
    public static final String ALLMANT_BEHANDLING_TABLETTER_DELSVAR_ID = "109.2";
    public static final String ALLMANT_BEHANDLING_TABLETTER_JSON_ID = "tabletter";
    public static final String ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID = "109.3";
    public static final String ALLMANT_BEHANDLING_INSULIN_JSON_ID = "insulin";
    public static final String ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_DELSVAR_ID = "109.4";
    public static final String ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID = "insulinSedanAr";
    public static final String ALLMANT_BEHANDLING_ANNAN_BEHANDLING_DELSVAR_ID = "109.5";
    public static final String ALLMANT_BEHANDLING_ANNAN_BEHANDLING_JSON_ID = "annanBehandling";
    public static final String ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_DELSVAR_ID = "109.6";
    public static final String ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID = "annanBehandlingBeskrivning";
    public static final String ALLMANT_BEHANDLING_RISK_HYPOGLYKEMI_DELSVAR_ID = "109.7";
    public static final String ALLMANT_BEHANDLING_RISK_HYPOGLYKEMI_JSON_ID = "riskHypoglykemi";

    // Kat 4 - Hypoglykemier
    public static final String HYPOGLYKEMI_CATEGORY_ID = "hypoglykemi";
    public static final String HYPOGLYKEMI_CATEGORY_TEXT_ID = "KAT_4.RBK";
    public static final String HYPOGLYKEMIER_JSON_ID = "hypoglykemier";
    public static final String HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_SVAR_ID = "41";
    public static final String HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_DELSVAR_ID = "41.1";
    public static final String HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID = "egenkontrollBlodsocker";
    public static final String HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_SVAR_ID = "37";
    public static final String HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_DELSVAR_ID = "37.1";
    public static final String HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID = "nedsattHjarnfunktion";
    public static final String HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_SVAR_ID = "100";
    public static final String HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_DELSVAR_ID = "100.1";
    public static final String HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID = "sjukdomenUnderKontroll";
    public static final String HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_SVAR_ID = "110";
    public static final String HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_DELSVAR_ID = "110.1";
    public static final String HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_JSON_ID = "formagaVarningstecken";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_SVAR_ID = "106";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID = "106.1";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID = "aterkommandeSenasteAret";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID = "106.2";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID = "aterkommandeSenasteTidpunkt";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_SVAR_ID = "107";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_DELSVAR_ID = "107.1";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID = "aterkommandeSenasteKvartalet";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_DELSVAR_ID = "107.2";
    public static final String HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID = "senasteTidpunktVaken";
    public static final String HYPOGLYKEMIER_FOREKOMST_SENASTE_TRAFIK_SVAR_ID = "108";
    public static final String HYPOGLYKEMIER_FOREKOMST_TRAFIK_SVAR_DELSVAR_ID = "108.1";
    public static final String HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID = "forekomstTrafik";
    public static final String HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_DELSVAR_ID = "108.2";
    public static final String HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID = "forekomstTrafikTidpunkt";

    // Kat 5 - Synfunktion
    public static final String SYN_CATEGORY_ID = "syn";
    public static final String SYN_CATEGORY_TEXT_ID = "KAT_5.RBK";
    public static final String SYNFUNKTION_JSON_ID = "synfunktion";
    public static final String SYNFUNKTION_MISSTANKE_OGONSJUKDOM_SVAR_ID = "103";
    public static final String SYNFUNKTION_MISSTANKE_OGONSJUKDOM_DELSVAR_ID = "103.1";
    public static final String SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID = "misstankeOgonsjukdom";

    public static final String SYNFUNKTION_SYNSKARPA_SVAR_ID = "8";
    public static final String SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_DELSVAR_ID = "8.1";
    public static final String SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_JSON_ID = "skickasSeparat";

    public static final String SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID = "hoger";
    public static final String SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID = "vanster";
    public static final String SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID = "binokulart";
    public static final String SYNFUNKTION_SYNSKARPA_UTAN_KORREKTION_JSON_ID = "utanKorrektion";
    public static final String SYNFUNKTION_SYNSKARPA_MED_KORREKTION_JSON_ID = "medKorrektion";

    public static final String SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID = "utanKorrektion";
    public static final String SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID = "medKorrektion";
    public static final String SYNFUNKTION_SYNSKARPA_HOGER_UTAN_KORREKTION_DELSVAR_ID = "8.2";
    public static final String SYNFUNKTION_SYNSKARPA_VANSTER_UTAN_KORREKTION_DELSVAR_ID = "8.3";
    public static final String SYNFUNKTION_SYNSKARPA_BINOKULART_UTAN_KORREKTION_DELSVAR_ID = "8.4";
    public static final String SYNFUNKTION_SYNSKARPA_HOGER_MED_KORREKTION_DELSVAR_ID = "8.5";
    public static final String SYNFUNKTION_SYNSKARPA_VANSTER_MED_KORREKTION_DELSVAR_ID = "8.6";
    public static final String SYNFUNKTION_SYNSKARPA_BINOKULART_MED_KORREKTION_DELSVAR_ID = "8.7";

    // Kat 6 - Övrigt
    public static final String OVRIGT_CATEGORY_ID = "ovrigt";
    public static final String OVRIGT_CATEGORY_TEXT_ID = "KAT_6.RBK";
    public static final String OVRIGT_SVAR_ID = "32";
    public static final String OVRIGT_DELSVAR_ID = "32.1";
    public static final String OVRIGT_DELSVAR_JSON_ID = "ovrigt";

    // Kat 7 - Bedömning
    public static final String BEDOMNING_CATEGORY_ID = "bedomning";
    public static final String BEDOMNING_CATEGORY_TEXT_ID = "KAT_7.RBK";
    public static final String BEDOMNING_SVAR_ID = "33";
    public static final String BEDOMNING_JSON_ID = "bedomning";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID = "33.1";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID = "uppfyllerBehorighetskrav";
    public static final String BEDOMNING_LAMPLIGHET_SVAR_ID = "45";
    public static final String BEDOMNING_LAMPLIGHET_ATT_INNEHA_DELSVAR_ID = "45.1";
    public static final String BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID = "lampligtInnehav";
    public static final String BEDOMNING_BOR_UNDERSOKAS_SVAR_ID = "34";
    public static final String BEDOMNING_BOR_UNDERSOKAS_DELSVAR_ID = "34.1";
    public static final String BEDOMNING_BOR_UNDERSOKAS_JSON_ID = "borUndersokasBeskrivning";

    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String TEXTVERSION_JSON_ID = "textVersion";
    public static final String ID_JSON_ID = "id";
    public static final String SIGNATURE = "signature";

    private RespConstants() {
    }

    public static final String INTYG_AVSER_SVAR_ID = "1";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_TEXT_ID = "FRG_35.RBK";
    public static final String ALLMANT_TYP_AV_DIABETES_TEXT_ID = "FRG_18.RBK";
    public static final String ALLMANT_BEHANDLING_TEXT_ID = "FRG_109.RBK";
    public static final String ALLMANT_BEHANDLING_INSULIN_TEXT_ID = "DFR_109.4.RBK";
    public static final String ALLMANT_BEHANDLING_ANNAN_DELSVAR_ID = "209.3";
    public static final String ALLMANT_BEHANDLING_ANNAN_TEXT_ID = "DFR_109.6.RBK";
    public static final String HYPOGLYKEMI_TECKEN_NEDSATT_HJARNFUNKTION_SVAR_ID = "201";
    public static final String HYPOGLYKEMI_TECKEN_NEDSATT_HJARNFUNKTION_SVAR_TEXT_ID = "FRG_37.RBK";
    public static final String HYPOGLYKEMI_EGENKONTROLL_BLODSOCKER_SVAR_ID = "206";
    public static final String HYPOGLYKEMI_GODTAGBAR_KONTROLL_SVAR_ID = "208.0";
    public static final String HYPOGLYKEMI_FORMOGA_VARNINGSTECKEN_SVAR_ID = "208";
    public static final String HYPOGLYKEMI_FORMOGA_VARNINGSTECKEN_SVAR_TEXT_ID = "FRG_110.RBK";
    public static final String HYPOGLYKEMI_GODTAGBAR_KONTROLL_SVAR_TEXT_ID = "FRG_100.RBK";
    public static final String HYPOGLYKEMI_EGENKONTROLL_BLODSOCKER_SVAR_TEXT_ID = "FRG_41.RBK";
    public static final String SYN_SEPARAT_OGONLAKARINTYG_SVAR_ID = "309";
    public static final String SYN_SEPARAT_OGONLAKARINTYG_SVAR_TEXT_ID = "DFR_8.1.RBK";
    public static final String SYN_UTAN_ANMARKNING_SVAR_ID = "310";
    public static final String SYN_UTAN_ANMARKNING_SVAR_TEXT_ID = "FRG_103.RBK";
    public static final String SYN_VARDEN_SYNSKARPA_SVAR_ID = "311";
    public static final String SYN_VARDEN_SYNSKARPA_MESSAGE_ID = "311.0";
    public static final String SYN_VARDEN_MISSTANKE_OGONSJUKDOM_MESSAGE_ID = "312.0";
    public static final String SYN_VARDEN_MISSTANKE_OGONSJUKDOM_MESSAGE_TEXT_ID = "TSDIA-001.ALERT";
    public static final String SYN_VARDEN_SYNSKARPA_SVAR_TEXT_ID = "FRG_8.RBK";
    public static final String SYN_VARDEN_SYNSKARPA_MESSAGE_TEXT_ID = "TSDIA-002.ALERT";
    public static final String HOGER_OGA_LABEL = "Höger öga";
    public static final String VANSTER_OGA_LABEL = "Vänster öga";
    public static final String BINOKULART_LABEL = "Binokulärt";
    public static final String UTAN_KORREKTION_TEXT = "Utan korrektion";
    public static final String MED_KORREKTION_TEXT = "Med korrektion";
    public static final String SYNSKARPA_TYP_ID = "synskarpaTyp";
    public static final String UTAN_KORREKTION_ID = "utanKorrektion";
    public static final String MED_KORREKTION_ID = "medKorrektion";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_TEXT_ID = "FRG_33.RBK";
    public static final String BEDOMNING_LAKARE_SPECIAL_KOMPETENS_SVAR_ID = "34";
    public static final String BEDOMNING_LAKARE_SPECIAL_KOMPETENS_SVAR_TEXT_ID = "FRG_34.RBK";
    public static final String BEDOMNING_LAMPLIGHET_SVAR_TEXT_ID = "FRG_45.RBK";
    public static final String OVRIGT_KOMMENTARER_SVAR_ID = "350";
    public static final String OVRIGT_KOMMENTARER_SVAR_TEXT_ID = "FRG_32.RBK";
    public static final String INTYG_AVSER_SVAR_TEXT_ID = "FRG_1.RBK";
    public static final String IDENTITET_STYRKT_GENOM_TEXT_ID = "FRG_2.RBK";
    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_TEXT_ID = "DFR_18.2.RBK";

    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID = "207";

    public static final String SVAR_JA_TEXT_ID = "SVAR_JA.RBK";
    public static final String SVAR_NEJ_TEXT_ID = "SVAR_NEJ.RBK";

    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID = "308";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID = "medicineringMedforRiskForHypoglykemi";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TEXT_ID = "DFR_109.7.RBK";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID = "200";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID = "106";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID = "aterkommandeSenasteAret";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TEXT_ID = "FRG_106.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID = "106.2";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_TEXT_ID = "DFR_106.2.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID = "106.5";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_TIDPUNKT_DELSVAR_ID = "106.6";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_TEXT_ID = "FRG_108.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_TIDPUNKT_TEXT_ID = "DFR_108.2.RBK";

    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID = "107";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID = "107.3";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TEXT_ID = "FRG_107.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID = "107.5";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_TEXT_ID = "DFR_107.2.RBK";

    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID = "203";

    // Kat 6 - Övrigt
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID = "206";

}
