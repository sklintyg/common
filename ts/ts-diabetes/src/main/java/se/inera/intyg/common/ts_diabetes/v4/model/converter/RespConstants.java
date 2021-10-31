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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

public final class RespConstants {

    // Kat 1 - Intyget avser
    public static final String INTYGETAVSER_SVAR_ID = "1";
    public static final String INTYGETAVSER_DELSVAR_ID = "1.1";
    public static final String INTYGETAVSER_SVAR_JSON_ID = "intygAvser";

    // Kat 2 - Identitet
    public static final String IDENTITET_STYRKT_GENOM_SVAR_ID = "2";
    public static final String IDENTITET_STYRKT_GENOM_DELSVAR_ID = "2.1";
    public static final String IDENTITET_STYRKT_GENOM_JSON_ID = "identitetStyrktGenom";

    // Kat 3 - Allmänt
    public static final String ALLMANT_JSON_ID = "allmant";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID = "35";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_DELSVAR_ID = "35.1";
    public static final String ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11 = "diabetesDiagnosAr";

    public static final String ALLMANT_TYP_AV_DIABETES_SVAR_ID = "18";
    public static final String ALLMANT_TYP_AV_DIABETES_DELSVAR_ID = "18.1";
    public static final String ALLMANT_TYP_AV_DIABETES_JSON_ID = "typAvDiabetes";
    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID = "18.2";
    public static final String ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID = "beskrivningAnnanTypAvDiabetes";

    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID = "207";
    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_DELSVAR_ID = "207.1";
    public static final String ALLMANT_MEDICINERING_FOR_DIABETES_JSON_ID = "medicineringForDiabetes";

    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID = "208";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_DELSVAR_ID = "208.1";
    public static final String ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID = "medicineringMedforRiskForHypoglykemi";

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

    // Kat 4 - Hypoglykemi
    public static final String HYPOGLYKEMI_JSON_ID = "hypoglykemi";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID = "200";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_DELSVAR_ID = "200.1";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID = "kontrollSjukdomstillstand";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID = "200.2";
    public static final String HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID = "kontrollSjukdomstillstandVarfor";

    public static final String HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID = "201";
    public static final String HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DELSVAR_ID = "201.1";
    public static final String HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID = "forstarRiskerMedHypoglykemi";

    public static final String HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID = "110";
    public static final String HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_DELSVAR_ID = "110.1";
    public static final String HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID = "formagaKannaVarningstecken";

    public static final String HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID = "202";
    public static final String HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_DELSVAR_ID = "202.1";
    public static final String HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID = "vidtaAdekvataAtgarder";

    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID = "106";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID = "106.1";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID = "aterkommandeSenasteAret";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID = "106.2";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID = "aterkommandeSenasteAretTidpunkt";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID = "106.3";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID = "aterkommandeSenasteAretKontrolleras";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID = "106.5";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID = "aterkommandeSenasteAretTrafik";

    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID = "107";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_DELSVAR_ID = "107.1";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID = "aterkommandeVaketSenasteTolv";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID = "107.3";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID = "aterkommandeVaketSenasteTre";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID = "107.5";
    public static final String HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID = "aterkommandeVaketSenasteTreTidpunkt";

    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID = "203";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_DELSVAR_ID = "203.1";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID = "allvarligSenasteTolvManaderna";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID = "203.2";
    public static final String HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID = "allvarligSenasteTolvManadernaTidpunkt";

    public static final String HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID = "204";
    public static final String HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_DELSVAR_ID = "204.1";
    public static final String HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID = "regelbundnaBlodsockerkontroller";

    // Kat 5 - Synfunktion
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
    public static final String OVRIGT_SVAR_ID = "32";
    public static final String OVRIGT_DELSVAR_ID = "32.1";
    public static final String OVRIGT_DELSVAR_JSON_ID = "ovrigt";

    // Kat 7 - Bedömning
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

}
