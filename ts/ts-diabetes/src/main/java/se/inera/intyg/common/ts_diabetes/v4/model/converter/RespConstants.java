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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

public final class RespConstants {

    // Kat 1 - Intyget avser
    public static final String INTYG_AVSER_CATEGORY_ID = "intygAvser";
    public static final String INTYG_AVSER_CATEGORY_TEXT_ID = "KAT_1.RBK";
    public static final String INTYG_AVSER_SVAR_ID = "1";
    public static final String INTYG_AVSER_DELSVAR_ID = "1.1";
    public static final String INTYG_AVSER_SVAR_JSON_ID = "intygAvser";
    public static final String INTYG_AVSER_SVAR_TEXT_ID = "FRG_1.RBK";
    public static final String INTYG_AVSER_SVAR_DESCRIPTION_ID = "FRG_1.HLP";

    public static final String INTYG_AVSER_VAR1_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR1.RBK";
    public static final String INTYG_AVSER_VAR2_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR2.RBK";
    public static final String INTYG_AVSER_VAR3_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR3.RBK";
    public static final String INTYG_AVSER_VAR4_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR4.RBK";
    public static final String INTYG_AVSER_VAR5_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR5.RBK";
    public static final String INTYG_AVSER_VAR6_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR6.RBK";
    public static final String INTYG_AVSER_VAR7_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR7.RBK";
    public static final String INTYG_AVSER_VAR8_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR8.RBK";
    public static final String INTYG_AVSER_VAR9_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR9.RBK";
    public static final String INTYG_AVSER_VAR12_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR12.RBK";
    public static final String INTYG_AVSER_VAR13_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR13.RBK";
    public static final String INTYG_AVSER_VAR14_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR14.RBK";
    public static final String INTYG_AVSER_VAR15_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR15.RBK";
    public static final String INTYG_AVSER_VAR16_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR16.RBK";
    public static final String INTYG_AVSER_VAR17_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR17.RBK";
    public static final String INTYG_AVSER_VAR18_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR18.RBK";

    // Kat 2 - Identitet
    public static final String IDENTITET_CATEGORY_ID = "identitetStyrktGenom";
    public static final String IDENTITET_CATEGORY_TEXT_ID = "KAT_2.RBK";
    public static final String IDENTITET_STYRKT_GENOM_SVAR_ID = "2";
    public static final String IDENTITET_STYRKT_GENOM_DELSVAR_ID = "2.1";
    public static final String IDENTITET_STYRKT_GENOM_JSON_ID = "identitetStyrktGenom";
    public static final String IDENTITET_STYRKT_GENOM_TEXT_ID = "FRG_2.RBK";
    public static final String IDENTITET_STYRKT_GENOM_DESCRIPTION_ID = "FRG_2.HLP";
    public static final String IDENTITET_STYRKT_GENOM_ID_KORT_TEXT_ID = "KV_ID_KONTROLL.IDK1.RBK";
    public static final String IDENTITET_STYRKT_GENOM_FORETAG_ELLER_TJANSTEKORT_TEXT_ID = "KV_ID_KONTROLL.IDK2.RBK";
    public static final String IDENTITET_STYRKT_GENOM_KORKORT_TEXT_ID = "KV_ID_KONTROLL.IDK3.RBK";
    public static final String IDENTITET_STYRKT_GENOM_PERS_KANNEDOM_TEXT_ID = "KV_ID_KONTROLL.IDK4.RBK";
    public static final String IDENTITET_STYRKT_GENOM_FORSAKRAN_KAP18_TEXT_ID = "KV_ID_KONTROLL.IDK5.RBK";
    public static final String IDENTITET_STYRKT_GENOM_PASS_TEXT_ID = "KV_ID_KONTROLL.IDK6.RBK";

    // Kat 3 - Allmänt
    public static final String ALLMANT_CATEGORY_ID = "allmant";
    public static final String ALLMANT_CATEGORY_TEXT_ID = "KAT_3.RBK";
    public static final String ALLMANT_JSON_ID = "allmant";
    public static final String ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID = "205";
    public static final String ALLMANT_PATIENTEN_FOLJS_AV_DELSVAR_ID = "205.1";
    public static final String ALLMANT_PATIENTEN_FOLJS_AV_JSON_ID = "patientenFoljsAv";
    public static final String ALLMANT_PATIENTEN_FOLJS_AV_PRIMARVARD_LABEL_ID = "KV_VARDNIVA_VN1.RBK";
    public static final String ALLMANT_PATIENTEN_FOLJS_AV_SPECIALISTVARD_LABEL_ID = "KV_VARDNIVA_VN2.RBK";

    public static final String ALLMANT_PATIENTEN_FOLJS_AV_TEXT_ID = "FRG_205.RBK";
    public static final String ALLMANT_PATIENTEN_FOLJS_AV_DESCRIPTION_ID = "FRG_205.HLP";

    public static final String ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID = "35";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_DELSVAR_ID = "35.1";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID = "diabetesDiagnosAr";

    public static final String ALLMANT_TYP_AV_DIABETES_SVAR_ID = "18";
    public static final String ALLMANT_TYP_AV_DIABETES_DELSVAR_ID = "18.1";
    public static final String ALLMANT_TYP_AV_DIABETES_JSON_ID = "typAvDiabetes";
    public static final String ALLMANT_TYP_AV_DIABETES_TEXT_ID = "FRG_18.RBK";
    public static final String ALLMANT_TYP_AV_DIABETES_TYP1_LABEL_ID = "SVAR_TYP1.RBK";
    public static final String ALLMANT_TYP_AV_DIABETES_TYP2_LABEL_ID = "SVAR_TYP2.RBK";
    public static final String ALLMANT_TYP_AV_DIABETES_LADA_LABEL_ID = "SVAR_LADA.RBK";
    public static final String ALLMANT_TYP_AV_DIABETES_ANNAN_LABEL_ID = "SVAR_ANNAN.RBK";

    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID = "18.2";
    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_TEXT_ID = "DFR_18.2.RBK";
    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID = "beskrivningAnnanTypAvDiabetes";

    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID = "207";
    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_DELSVAR_ID = "207.1";
    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_JSON_ID = "medicineringForDiabetes";
    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_TEXT_ID = "FRG_207.RBK";

    public static final String SVAR_JA_TEXT_ID = "SVAR_JA.RBK";
    public static final String SVAR_NEJ_TEXT_ID = "SVAR_NEJ.RBK";

    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID = "208";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_DELSVAR_ID = "208.1";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID = "medicineringMedforRiskForHypoglykemi";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TEXT_ID = "FRG_208.RBK";

    public static final String ALLMANT_BEHANDLING_SVAR_ID = "209";
    public static final String ALLMANT_BEHANDLING_JSON_ID = "behandling";
    public static final String ALLMANT_BEHANDLING_TEXT_ID = "FRG_209.RBK";
    public static final String ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID = "209.1";
    public static final String ALLMANT_BEHANDLING_INSULIN_JSON_ID = "insulin";
    public static final String ALLMANT_BEHANDLING_INSULIN_TEXT_ID = "DFR_209.1.RBK";
    public static final String ALLMANT_BEHANDLING_TABLETTER_DELSVAR_ID = "209.2";
    public static final String ALLMANT_BEHANDLING_TABLETTER_JSON_ID = "tabletter";
    public static final String ALLMANT_BEHANDLING_TABLETTER_TEXT_ID = "DFR_209.2.RBK";
    public static final String ALLMANT_BEHANDLING_ANNAN_DELSVAR_ID = "209.3";
    public static final String ALLMANT_BEHANDLING_ANNAN_JSON_ID = "annan";
    public static final String ALLMANT_BEHANDLING_ANNAN_TEXT_ID = "DFR_209.3.RBK";
    public static final String ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID = "209.4";
    public static final String ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID = "annanAngeVilken";
    public static final String ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_TEXT_ID = "DFR_209.4.RBK";

    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_SVAR_ID = "210";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_DELSVAR_ID = "210.1";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID
        = "medicineringMedforRiskForHypoglykemiTidpunkt";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_TEXT_ID = "FRG_210.RBK";

    // Kat 4 - Hypoglykemi
    public static final String HYPOGLYKEMI_CATEGORY_ID = "hypoglykemi";
    public static final String HYPOGLYKEMI_CATEGORY_TEXT_ID = "KAT_4.RBK";

    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID = "200";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_DELSVAR_ID = "200.1";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID = "kontrollSjukdomstillstand";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_TEXT_ID = "FRG_200.RBK";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_DESCRIPTION_ID = "FRG_200.HLP";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID = "200.2";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID = "kontrollSjukdomstillstandVarfor";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_TEXT_ID = "DFR_200.2.RBK";

    public static final String HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID = "201";
    public static final String HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DELSVAR_ID = "201.1";
    public static final String HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID = "forstarRiskerMedHypoglykemi";
    public static final String HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_TEXT_ID = "FRG_201.RBK";
    public static final String HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DESCRIPTION_ID = "FRG_201.HLP";

    public static final String HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID = "110";
    public static final String HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_DELSVAR_ID = "110.1";
    public static final String HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID = "formagaKannaVarningstecken";
    public static final String HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_TEXT_ID = "FRG_110.RBK";
    public static final String HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_DESCRIPTION_ID = "FRG_110.HLP";

    public static final String HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID = "202";
    public static final String HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_DELSVAR_ID = "202.1";
    public static final String HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID = "vidtaAdekvataAtgarder";
    public static final String HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_TEXT_ID = "FRG_202.RBK";
    public static final String HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_DESCRIPTION_ID = "FRG_202.HLP";

    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID = "106";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID = "106.1";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID = "aterkommandeSenasteAret";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TEXT_ID = "FRG_106.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_DESCRIPTION_ID = "FRG_106.HLP";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID = "106.2";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID = "aterkommandeSenasteAretTidpunkt";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_TEXT_ID = "DFR_106.2.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID = "106.3";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID = "aterkommandeSenasteAretKontrolleras";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_TEXT_ID = "DFR_106.3.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID = "106.5";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID = "aterkommandeSenasteAretTrafik";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_TEXT_ID = "DFR_106.5.RBK";

    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID = "107";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_DELSVAR_ID = "107.1";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID = "aterkommandeVaketSenasteTolv";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_TEXT_ID = "FRG_107.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID = "107.3";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID = "aterkommandeVaketSenasteTre";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TEXT_ID = "DFR_107.3.RBK";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID = "107.5";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID = "aterkommandeVaketSenasteTreTidpunkt";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_TEXT_ID = "DFR_107.5.RBK";

    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID = "203";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_DELSVAR_ID = "203.1";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID = "allvarligSenasteTolvManaderna";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TEXT_ID = "FRG_203.RBK";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID = "203.2";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID = "allvarligSenasteTolvManadernaTidpunkt";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_TEXT_ID = "DFR_203.2.RBK";

    public static final String HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID = "204";
    public static final String HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_DELSVAR_ID = "204.1";
    public static final String HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID = "regelbundnaBlodsockerkontroller";
    public static final String HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_TEXT_ID = "FRG_204.RBK";
    public static final String HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_DESCRIPTION_ID = "FRG_204.HLP";

    // Kat 6 - Övrigt
    public static final String OVRIGT_CATEGORY_ID = "ovrigt";
    public static final String OVRIGT_CATEGORY_TEXT_ID = "KAT_6.RBK";
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID = "206";
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_DELSVAR_ID = "206.1";
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID = "komplikationerAvSjukdomen";
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_TEXT_ID = "FRG_206.RBK";
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID = "206.2";
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID = "komplikationerAvSjukdomenAnges";
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_TEXT_ID = "DFR_206.2.RBK";
    public static final String OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DESCRIPTION_ID = "DFR_206.2.HLP";

    public static final String OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_SVAR_ID = "34";
    public static final String OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_DELSVAR_ID = "34.1";
    public static final String OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_JSON_ID = "borUndersokasAvSpecialist";
    public static final String OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_TEXT_ID = "FRG_34.RBK";

    // Kat 7 - Bedömning
    public static final String BEDOMNING_CATEGORY_ID = "bedomning";
    public static final String BEDOMNING_CATEGORY_TEXT_ID = "KAT_7.RBK";
    public static final String BEDOMNING_SVAR_ID = "33";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID = "33.1";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID = "uppfyllerBehorighetskrav";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_TEXT = "Patienten uppfyller kraven enligt 6 kap. i Transportstyrelsens "
        + "föreskrifter och allmänna råd (TSFS 2010:125) om medicinska krav för innehav av körkort m.m. Föreskrivna krav på läkarens "
        + "specialistkompetens vid diabetessjukdom anges i 17 kap.";
    public static final String BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DESCRIPTION_ID = "FRG_33.HLP";

    public static final String BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID = "32";
    public static final String BEDOMNING_OVRIGA_KOMMENTARER_DELSVAR_ID = "32.1";
    public static final String BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID = "ovrigaKommentarer";
    public static final String BEDOMNING_OVRIGA_KOMMENTARER_TEXT_ID = "FRG_32.RBK";

    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR12_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR12.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR13_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR13.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR14_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR14.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR15_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR15.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR16_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR16.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR17_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR17.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR18_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR18.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR1_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR1.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR2_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR2.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR3_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR3.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR4_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR4.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR5_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR5.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR6_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR6.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR7_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR7.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR8_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR8.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR9_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR9.RBK";
    public static final String BEDOMNING_KORKORTSBEHORIGHET_VAR11_LABEL_ID = "KV_KORKORTSBEHORIGHET.VAR11.RBK";

    public static final String GRUNDDATA_SVAR_JSON_ID = "grundData";
    public static final String TEXTVERSION_JSON_ID = "textVersion";
    public static final String ID_JSON_ID = "id";
    public static final String SIGNATURE = "signature";
    public static final String KATEGORIER_JSON_ID = "kategorier";
    public static final String TYP_JSON_ID = "typ";

    private RespConstants() {
    }
}
