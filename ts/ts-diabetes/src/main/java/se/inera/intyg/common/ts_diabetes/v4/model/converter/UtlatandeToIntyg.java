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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import static java.lang.Boolean.TRUE;
import static se.inera.intyg.common.support.Constants.KV_ID_KONTROLL_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_KORKORTSBEHORIGHET_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_UTLATANDETYP_INTYG_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_VARDNIVA_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aPartialDate;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getYearContent;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_FOR_DIABETES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateTypeFormatEnum;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(TsDiabetesUtlatandeV4 utlatande) {
        final var intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC);
        intyg.setTyp(getTypAvIntyg());
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg() {
        final var typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(TsDiabetesEntryPoint.KV_UTLATANDETYP_INTYG_CODE);
        typAvIntyg.setCodeSystem(KV_UTLATANDETYP_INTYG_CODE_SYSTEM);
        typAvIntyg.setDisplayName(TsDiabetesEntryPoint.ISSUER_MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(TsDiabetesUtlatandeV4 source) {
        final var svars = new ArrayList<Svar>();

        // Kat 1 - Intyget avser
        if (source.getIntygAvser() != null && source.getIntygAvser().getKategorier() != null
            && source.getIntygAvser().getKategorier().size() > 0) {
            int intygAvserInstans = 1;
            for (IntygAvserKategori intygAvserKategori : source.getIntygAvser().getKategorier()) {
                final var korkortsbehorighetKod = KorkortsbehorighetKod.fromCode(intygAvserKategori.name());
                svars.add(aSvar(INTYG_AVSER_SVAR_ID, intygAvserInstans++)
                    .withDelsvar(INTYG_AVSER_DELSVAR_ID,
                        aCV(KV_KORKORTSBEHORIGHET_CODE_SYSTEM, korkortsbehorighetKod.getCode(), korkortsbehorighetKod.getDescription()))
                    .build());
            }
        }

        // Kat 2 - Identitet
        if (source.getIdentitetStyrktGenom() != null && source.getIdentitetStyrktGenom().getTyp() != null) {
            svars.add(aSvar(IDENTITET_STYRKT_GENOM_SVAR_ID)
                .withDelsvar(IDENTITET_STYRKT_GENOM_DELSVAR_ID,
                    aCV(KV_ID_KONTROLL_CODE_SYSTEM, source.getIdentitetStyrktGenom().getTyp().getCode(),
                        source.getIdentitetStyrktGenom().getTyp().getDescription()))
                .build());
        }

        // Kat 3 - Allmänt
        if (source.getAllmant() != null) {
            buildAllmant(source.getAllmant(), svars);
        }

        // Kat 4 - Hypoglykemi
        if (source.getHypoglykemi() != null) {
            buildHypoglykemi(source.getHypoglykemi(), svars);
        }

        // Kat 6 - Övrigt
        if (source.getOvrigt() != null) {
            buildOvrigt(source.getOvrigt(), svars);
        }

        // Kat 7 - Bedömning
        if (source.getBedomning() != null) {
            buildBedomning(source.getBedomning(), svars);
        }

        return svars;
    }

    private static void buildAllmant(Allmant allmant, List<Svar> svars) {
        if (allmant.getPatientenFoljsAv() != null) {
            svars.add(aSvar(ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID)
                .withDelsvar(ALLMANT_PATIENTEN_FOLJS_AV_DELSVAR_ID,
                    aCV(KV_VARDNIVA_CODE_SYSTEM, allmant.getPatientenFoljsAv().getCode(),
                        allmant.getPatientenFoljsAv().getDescription()))
                .build());
        }

        if (allmant.getDiabetesDiagnosAr() != null) {
            // If getDiabetesDiagnosAr can not be converted to year getYearContent will return null and the svar will not be added.
            final var diabetesDiagnosAr = getYearContent(allmant.getDiabetesDiagnosAr());
            if (diabetesDiagnosAr != null) {
                svars.add(aSvar(ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID)
                    .withDelsvar(ALLMANT_DIABETES_DIAGNOS_AR_DELSVAR_ID,
                        aPartialDate(PartialDateTypeFormatEnum.YYYY, diabetesDiagnosAr))
                    .build());
            }
        }

        if (allmant.getTypAvDiabetes() != null || allmant.getBeskrivningAnnanTypAvDiabetes() != null) {
            // Here we rely on withDelsvar not adding a delsvar if content is null
            svars.add(aSvar(ALLMANT_TYP_AV_DIABETES_SVAR_ID)
                .withDelsvar(ALLMANT_TYP_AV_DIABETES_DELSVAR_ID,
                    // Skip CV if "other" is selected
                    allmant.getTypAvDiabetes() != null && !KvTypAvDiabetes.ANNAN.equals(allmant.getTypAvDiabetes())
                        ? aCV(Diagnoskodverk.ICD_10_SE.getCodeSystem(), allmant.getTypAvDiabetes().getCode(),
                        allmant.getTypAvDiabetes().getDescription())
                        : null)
                .withDelsvar(ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID,
                    allmant.getBeskrivningAnnanTypAvDiabetes())
                .build());
        }

        if (allmant.getMedicineringForDiabetes() != null) {
            svars.add(aSvar(ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID)
                .withDelsvar(ALLMANT_MEDICINERING_FOR_DIABETES_DELSVAR_ID,
                    InternalConverterUtil.getBooleanContent(allmant.getMedicineringForDiabetes()))
                .build());
        }

        if (allmant.getMedicineringMedforRiskForHypoglykemi() != null) {
            svars.add(aSvar(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID)
                .withDelsvar(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_DELSVAR_ID,
                    InternalConverterUtil.getBooleanContent(allmant.getMedicineringMedforRiskForHypoglykemi()))
                .build());
        }

        if (allmant.getBehandling() != null && TRUE.equals(allmant.getMedicineringMedforRiskForHypoglykemi())) {
            // Here we rely on withDelsvar not adding a delsvar if content is null
            final var behandlingSvar = aSvar(ALLMANT_BEHANDLING_SVAR_ID)
                .withDelsvar(ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID,
                    InternalConverterUtil.getBooleanContent(allmant.getBehandling().getInsulin()))
                .withDelsvar(ALLMANT_BEHANDLING_TABLETTER_DELSVAR_ID,
                    InternalConverterUtil.getBooleanContent(allmant.getBehandling().getTabletter()))
                .withDelsvar(ALLMANT_BEHANDLING_ANNAN_DELSVAR_ID,
                    InternalConverterUtil.getBooleanContent(allmant.getBehandling().getAnnan()))
                .withDelsvar(ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID,
                    allmant.getBehandling().getAnnanAngeVilken())
                .build();
            final var validElementInIntygXmlSchema = behandlingSvar.getDelsvar().size() != 0;
            if (validElementInIntygXmlSchema) {
                svars.add(behandlingSvar);
            }
        }

        if (allmant.getMedicineringMedforRiskForHypoglykemiTidpunkt() != null) {
            svars.add(aSvar(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_SVAR_ID)
                .withDelsvar(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_DELSVAR_ID,
                    aPartialDate(PartialDateTypeFormatEnum.YYYY_MM_DD,
                        allmant.getMedicineringMedforRiskForHypoglykemiTidpunkt().asLocalDate()))
                .build());
        }
    }

    private static void buildHypoglykemi(Hypoglykemi hypoglykemi, List<Svar> answers) {

        final var hasControl = hypoglykemi.getKontrollSjukdomstillstand();
        if (hasControl != null) {
            final var answerBuilder = aSvar(HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID);
            answerBuilder.withDelsvar(HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_DELSVAR_ID, InternalConverterUtil
                .getBooleanContent(hasControl));

            if (!hasControl && hypoglykemi.getKontrollSjukdomstillstandVarfor() != null) {
                answerBuilder.withDelsvar(HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID, hypoglykemi
                    .getKontrollSjukdomstillstandVarfor());
            }
            answers.add(answerBuilder.build());
        }

        InternalConverterUtil.addIfNotNull(answers, HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID,
            HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DELSVAR_ID, hypoglykemi.getForstarRiskerMedHypoglykemi());

        InternalConverterUtil.addIfNotNull(answers, HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID,
            HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_DELSVAR_ID, hypoglykemi.getFormagaKannaVarningstecken());

        InternalConverterUtil.addIfNotNull(answers, HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID,
            HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_DELSVAR_ID, hypoglykemi.getVidtaAdekvataAtgarder());

        final var recurringThisYear = hypoglykemi.getAterkommandeSenasteAret();
        if (recurringThisYear != null) {
            final var answerBuilder = aSvar(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID);
            answerBuilder.withDelsvar(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID, InternalConverterUtil
                .getBooleanContent(recurringThisYear));

            if (recurringThisYear && hypoglykemi.getAterkommandeSenasteAretTidpunkt() != null) {
                answerBuilder.withDelsvar(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID,
                    aPartialDate(PartialDateTypeFormatEnum.YYYY_MM_DD, hypoglykemi.getAterkommandeSenasteAretTidpunkt().asLocalDate()));
            }
            if (recurringThisYear && hypoglykemi.getAterkommandeSenasteAretKontrolleras() != null) {
                answerBuilder.withDelsvar(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID, InternalConverterUtil
                    .getBooleanContent(hypoglykemi.getAterkommandeSenasteAretKontrolleras()));
            }
            if (recurringThisYear && hypoglykemi.getAterkommandeSenasteAretTrafik() != null) {
                answerBuilder.withDelsvar(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID, InternalConverterUtil
                    .getBooleanContent(hypoglykemi.getAterkommandeSenasteAretTrafik()));
            }
            answers.add(answerBuilder.build());
        }

        final var recurringAwakeLastTwelveMonths = hypoglykemi.getAterkommandeVaketSenasteTolv();
        if (recurringAwakeLastTwelveMonths != null) {
            final var answerBuilder = aSvar(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID);
            answerBuilder.withDelsvar(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_DELSVAR_ID, InternalConverterUtil
                .getBooleanContent(recurringAwakeLastTwelveMonths));

            final var recurringAwakeLastThreeMonths = hypoglykemi.getAterkommandeVaketSenasteTre();
            if (recurringAwakeLastTwelveMonths && recurringAwakeLastThreeMonths != null) {
                answerBuilder.withDelsvar(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID, InternalConverterUtil
                    .getBooleanContent(recurringAwakeLastThreeMonths));

                final var recurringAwakeLastThreeMonthsTimepoint = hypoglykemi.getAterkommandeVaketSenasteTreTidpunkt();
                if (recurringAwakeLastThreeMonths && recurringAwakeLastThreeMonthsTimepoint != null) {
                    answerBuilder.withDelsvar(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID,
                        aPartialDate(PartialDateTypeFormatEnum.YYYY_MM_DD, recurringAwakeLastThreeMonthsTimepoint.asLocalDate()));
                }
            }
            answers.add(answerBuilder.build());
        }

        final var seriousLastTwelveMonths = hypoglykemi.getAllvarligSenasteTolvManaderna();
        if (seriousLastTwelveMonths != null) {
            final var answerBuilder = aSvar(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID);
            answerBuilder.withDelsvar(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_DELSVAR_ID, InternalConverterUtil
                .getBooleanContent(seriousLastTwelveMonths));

            final var seriousLastTwelveMonthsTimepoint = hypoglykemi.getAllvarligSenasteTolvManadernaTidpunkt();
            if (seriousLastTwelveMonths && seriousLastTwelveMonthsTimepoint != null) {
                answerBuilder.withDelsvar(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID,
                    aPartialDate(PartialDateTypeFormatEnum.YYYY_MM_DD, seriousLastTwelveMonthsTimepoint.asLocalDate()));
            }
            answers.add(answerBuilder.build());
        }

        InternalConverterUtil.addIfNotNull(answers, HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID,
            HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_DELSVAR_ID, hypoglykemi.getRegelbundnaBlodsockerkontroller());
    }

    private static void buildOvrigt(Ovrigt ovrigt, List<Svar> answers) {
        final var hasComplications = ovrigt.getKomplikationerAvSjukdomen();
        if (hasComplications != null) {
            final var answerBuilder = aSvar(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID);
            answerBuilder.withDelsvar(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_DELSVAR_ID, InternalConverterUtil
                .getBooleanContent(hasComplications));

            if (hasComplications && ovrigt.getKomplikationerAvSjukdomenAnges() != null) {
                answerBuilder.withDelsvar(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID, ovrigt.getKomplikationerAvSjukdomenAnges());
            }

            answers.add(answerBuilder.build());
        }

        final var shouldBeExamined = ovrigt.getBorUndersokasAvSpecialist();
        final var resolvedShouldBeExamined = !Strings.nullToEmpty(shouldBeExamined).trim().isEmpty() ? shouldBeExamined : null;
        InternalConverterUtil.addIfNotBlank(answers, OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_SVAR_ID,
            OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_DELSVAR_ID, resolvedShouldBeExamined);
    }

    private static void buildBedomning(Bedomning bedomning, List<Svar> svars) {
        if (bedomning.getUppfyllerBehorighetskrav() != null) {
            int behorighetskravInstans = 1;
            for (BedomningKorkortstyp bedomningKorkortstyp : bedomning.getUppfyllerBehorighetskrav()) {
                KorkortsbehorighetKod korkortsbehorighetKod = KorkortsbehorighetKod.fromCode(bedomningKorkortstyp.name());
                svars.add(aSvar(BEDOMNING_SVAR_ID, behorighetskravInstans++)
                    .withDelsvar(BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID,
                        aCV(KV_KORKORTSBEHORIGHET_CODE_SYSTEM, korkortsbehorighetKod.getCode(),
                            korkortsbehorighetKod.getDescription()))
                    .build());
            }
        }

        final var comments = bedomning.getOvrigaKommentarer();
        final var resolvedComments = !Strings.nullToEmpty(comments).trim().isEmpty() ? comments : null;
        InternalConverterUtil.addIfNotBlank(svars, BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID, BEDOMNING_OVRIGA_KOMMENTARER_DELSVAR_ID,
            resolvedComments);
    }

}
