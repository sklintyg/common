/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import com.google.common.base.Strings;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.ModelException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Diabetes;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Syn;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateTypeFormatEnum;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.support.Constants.KV_ID_KONTROLL_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_INTYGET_AVSER_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_KORKORTSBEHORIGHET_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_UTLATANDETYP_INTYG_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.*;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_SVAR_ID_40;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ALLVARLIG_HYPOGLYKEMI_SVAR_ID_39;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ALLVARLIG_HYPOGLYKEMI_UNDER_VAKEN_TID_SVAR_ID_42;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ANNAN_BEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ANTAL_EPISODER_ALLVARLIG_HYPOGLYKEMI_DELSVAR_ID_39;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ANTAL_EPISODER_TIDPUNKT_ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_DELSVAR_ID_40;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.AR_FOR_DIABETESDIAGNOS_DELSVAR_ID_35;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.AR_FOR_DIABETESDIAGNOS_SVAR_ID_35;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BEHANDLING_DIABETES_SVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BINOKULART_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_DELSVAR_ID_34;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_SVAR_ID_34;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.DUBBELSEENDE_DELSVAR_ID_6;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.DUBBELSEENDE_SVAR_ID_6;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.EGENKONTROLLER_BLODSOCKER_DELSVAR_ID_41;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.EGENKONTROLLER_BLODSOCKER_SVAR_ID_41;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_ALLVARLIG_HYPOGLYKEMI_DELSVAR_ID_39;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_DELSVAR_ID_40;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_ALLVARLIG_HYPOGLYKEMI_UNDER_VAKEN_TID_DELSVAR_ID_42;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_HYPOGLYKEMIER_DELSVAR_ID_37;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_HYPOGLYKEMIER_SVAR_ID_37;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.IDENTITET_STYRKT_GENOM_ID_2;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID_2;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INSULINBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INSULINBEHANDLING_SEDAN_AR_DELSVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.KOSTBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.KUNSKAP_ATGARDER_HYPOGLYKEMI_DELSVAR_ID_36;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.KUNSKAP_ATGARDER_HYPOGLYKEMI_SVAR_ID_36;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.LAMPLIGHET_INNEHA_BEHORIGHET_DELSVAR_ID_45;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID_45;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.NOT_AVAILABLE;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.OGONLAKARINTYG_SKICKAS_SEPARAT_DELSVAR_ID_43;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.OGONLAKARINTYG_SKICKAS_SEPARAT_SVAR_ID_43;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.OVRIGA_KOMMENTARER_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.OVRIGA_KOMMENTARER_SVAR_ID_32;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SAKNAR_FORMAGA_KANNA_VARNINGSTECKEN_DELSVAR_ID_38;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SAKNAR_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID_38;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SYNFALTSPROVNING_DONDER_DELSVAR_ID_44;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SYNFALTSPROVNING_DONDER_SVAR_ID_44;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SYNSKARPA_SVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TABLETTBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TIDPUNKT_ALLVARLIG_HYPOGLYKEMI_UNDER_VAKEN_TID_DELSVAR_ID_42;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TYP_AV_DIABETES_DELSVAR_ID_18;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TYP_AV_DIABETES_SVAR_ID_18;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.UPPFYLLER_KRAV_FOR_BEHORIGHET_DELSVAR_ID_33;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.UPPFYLLER_KRAV_FOR_BEHORIGHET_SVAR_ID_33;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.model.converter.InternalToTransportUtil.getVersion;

public final class UtlatandeToIntyg {

    private static final String DEFAULT_VERSION = "2.6";

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(TsDiabetesUtlatandeV2 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, true);
        complementArbetsplatskodIfMissing(intyg);
        intyg.setTyp(getTypAvIntyg());
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setVersion(getVersion(utlatande).orElse(DEFAULT_VERSION));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg() {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(TsDiabetesEntryPoint.KV_UTLATANDETYP_INTYG_CODE);
        typAvIntyg.setCodeSystem(KV_UTLATANDETYP_INTYG_CODE_SYSTEM);
        typAvIntyg.setDisplayName(TsDiabetesEntryPoint.ISSUER_MODULE_NAME);
        return typAvIntyg;
    }

    private static void complementArbetsplatskodIfMissing(Intyg intyg) {
        if (Strings.nullToEmpty(intyg.getSkapadAv().getEnhet().getArbetsplatskod().getExtension()).trim().isEmpty()) {
            intyg.getSkapadAv().getEnhet().getArbetsplatskod().setExtension(NOT_AVAILABLE);
        }
    }

    private static List<Svar> getSvar(TsDiabetesUtlatandeV2 source) {
        List<Svar> svars = new ArrayList<>();

        int intygAvserInstans = 1;
        for (IntygAvserKategori korkortstyp : source.getIntygAvser().getKorkortstyp()) {
            IntygAvserKod intygAvser = IntygAvserKod.valueOf(korkortstyp.name());
            svars.add(aSvar(INTYG_AVSER_SVAR_ID_1, intygAvserInstans++)
                .withDelsvar(INTYG_AVSER_DELSVAR_ID_1,
                    aCV(KV_INTYGET_AVSER_CODE_SYSTEM, intygAvser.getCode(), intygAvser.getDescription()))
                .build());
        }

        if (source.getVardkontakt() != null && source.getVardkontakt().getIdkontroll() != null) {
            IdKontrollKod idKontroll = IdKontrollKod.valueOf(source.getVardkontakt().getIdkontroll());
            svars.add(aSvar(IDENTITET_STYRKT_GENOM_SVAR_ID_2)
                .withDelsvar(IDENTITET_STYRKT_GENOM_ID_2,
                    aCV(KV_ID_KONTROLL_CODE_SYSTEM, idKontroll.getCode(), idKontroll.getDescription()))
                .build());
        }

        buildDiabetesSvar(source.getDiabetes(), svars);
        buildHypoglykemierSvar(source.getHypoglykemier(), svars);
        buildSynSvar(source.getSyn(), svars);
        buildBedomningSvar(source.getBedomning(), svars);
        addIfNotBlank(svars, OVRIGA_KOMMENTARER_SVAR_ID_32, OVRIGA_KOMMENTARER_DELSVARSVAR_ID_32, source.getKommentar());

        return svars;
    }

    private static void buildDiabetesSvar(Diabetes source, List<Svar> svars) {
        if (source == null) {
            return;
        }

        Year observationsperiod = getYearContent(source.getObservationsperiod());
        if (observationsperiod != null) {
            svars.add(aSvar(AR_FOR_DIABETESDIAGNOS_SVAR_ID_35)
                .withDelsvar(AR_FOR_DIABETESDIAGNOS_DELSVAR_ID_35,
                    aPartialDate(PartialDateTypeFormatEnum.YYYY, observationsperiod))
                .build());
        }

        if (source.getDiabetestyp() != null) {
            DiabetesKod diabetesKod = DiabetesKod.valueOf(source.getDiabetestyp());
            svars.add(aSvar(TYP_AV_DIABETES_SVAR_ID_18)
                .withDelsvar(TYP_AV_DIABETES_DELSVAR_ID_18,
                    aCV(Diagnoskodverk.ICD_10_SE.getCodeSystem(), diabetesKod.getCode(), diabetesKod.getDescription()))
                .build());
        }
        InternalConverterUtil.SvarBuilder diabetesBehandling = aSvar(BEHANDLING_DIABETES_SVAR_ID_19);
        if (source.getEndastKost() != null) {
            diabetesBehandling.withDelsvar(KOSTBEHANDLING_DELSVAR_ID_19, source.getEndastKost().toString());
        }
        if (source.getTabletter() != null) {
            diabetesBehandling.withDelsvar(TABLETTBEHANDLING_DELSVAR_ID_19, source.getTabletter().toString());
        }
        if (source.getInsulin() != null) {
            diabetesBehandling.withDelsvar(INSULINBEHANDLING_DELSVAR_ID_19, source.getInsulin().toString());
        }
        Year insulinBehandlingsperiod = getYearContent(source.getInsulinBehandlingsperiod());
        if (insulinBehandlingsperiod != null) {
            diabetesBehandling.withDelsvar(INSULINBEHANDLING_SEDAN_AR_DELSVAR_ID_19,
                aPartialDate(PartialDateTypeFormatEnum.YYYY, insulinBehandlingsperiod));
        }
        if (!Strings.nullToEmpty(source.getAnnanBehandlingBeskrivning()).trim().isEmpty()) {
            diabetesBehandling.withDelsvar(ANNAN_BEHANDLING_DELSVAR_ID_19, source.getAnnanBehandlingBeskrivning());
        }
        if (!diabetesBehandling.delSvars.isEmpty()) {
            svars.add(diabetesBehandling.build());
        }
    }

    private static void buildHypoglykemierSvar(Hypoglykemier source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        addIfNotNull(svars, KUNSKAP_ATGARDER_HYPOGLYKEMI_SVAR_ID_36, KUNSKAP_ATGARDER_HYPOGLYKEMI_DELSVAR_ID_36,
            source.getKunskapOmAtgarder());
        addIfNotNull(svars, FOREKOMST_HYPOGLYKEMIER_SVAR_ID_37, FOREKOMST_HYPOGLYKEMIER_DELSVAR_ID_37,
            source.getTeckenNedsattHjarnfunktion());
        addIfNotNull(svars, SAKNAR_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID_38, SAKNAR_FORMAGA_KANNA_VARNINGSTECKEN_DELSVAR_ID_38,
            source.getSaknarFormagaKannaVarningstecken());
        InternalConverterUtil.SvarBuilder allvarligHypoglykemi = aSvar(ALLVARLIG_HYPOGLYKEMI_SVAR_ID_39);
        if (source.getAllvarligForekomst() != null) {
            allvarligHypoglykemi.withDelsvar(FOREKOMST_ALLVARLIG_HYPOGLYKEMI_DELSVAR_ID_39,
                source.getAllvarligForekomst().toString());
        }
        if (!Strings.nullToEmpty(source.getAllvarligForekomstBeskrivning()).trim().isEmpty()) {
            allvarligHypoglykemi.withDelsvar(ANTAL_EPISODER_ALLVARLIG_HYPOGLYKEMI_DELSVAR_ID_39,
                source.getAllvarligForekomstBeskrivning());
        }
        if (!allvarligHypoglykemi.delSvars.isEmpty()) {
            svars.add(allvarligHypoglykemi.build());
        }
        InternalConverterUtil.SvarBuilder allvarligHypoglykemiTrafiken = aSvar(ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_SVAR_ID_40);
        if (source.getAllvarligForekomstTrafiken() != null) {
            allvarligHypoglykemiTrafiken.withDelsvar(FOREKOMST_ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_DELSVAR_ID_40,
                source.getAllvarligForekomstTrafiken().toString());
        }
        if (!Strings.nullToEmpty(source.getAllvarligForekomstTrafikBeskrivning()).trim().isEmpty()) {
            allvarligHypoglykemiTrafiken.withDelsvar(ANTAL_EPISODER_TIDPUNKT_ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN_DELSVAR_ID_40,
                source.getAllvarligForekomstTrafikBeskrivning());
        }
        if (!allvarligHypoglykemiTrafiken.delSvars.isEmpty()) {
            svars.add(allvarligHypoglykemiTrafiken.build());
        }
        addIfNotNull(svars, EGENKONTROLLER_BLODSOCKER_SVAR_ID_41, EGENKONTROLLER_BLODSOCKER_DELSVAR_ID_41,
            source.getEgenkontrollBlodsocker());
        InternalConverterUtil.SvarBuilder allvarligHypoglykemiVakenTid = aSvar(ALLVARLIG_HYPOGLYKEMI_UNDER_VAKEN_TID_SVAR_ID_42);
        if (source.getAllvarligForekomstVakenTid() != null) {
            allvarligHypoglykemiVakenTid.withDelsvar(FOREKOMST_ALLVARLIG_HYPOGLYKEMI_UNDER_VAKEN_TID_DELSVAR_ID_42,
                source.getAllvarligForekomstVakenTid().toString());
        }
        if (source.getAllvarligForekomstVakenTidObservationstid() != null
            && source.getAllvarligForekomstVakenTidObservationstid().isValidDate()) {
            // CHECKSTYLE:OFF EmptyBlock
            try {
                allvarligHypoglykemiVakenTid.withDelsvar(TIDPUNKT_ALLVARLIG_HYPOGLYKEMI_UNDER_VAKEN_TID_DELSVAR_ID_42,
                    source.getAllvarligForekomstVakenTidObservationstid().asLocalDate().toString());
            } catch (ModelException | IllegalArgumentException e) {
                /*
                 * During conversion for CertificateStatusUpdateForCare v3
                 * the utlatande might still be an utkast, meaning dates might
                 * be invalid - in that case conversion skips them.
                 */
            }
            // CHECKSTYLE:ON EmptyBlock
        }
        if (!allvarligHypoglykemiVakenTid.delSvars.isEmpty()) {
            svars.add(allvarligHypoglykemiVakenTid.build());
        }
    }

    private static void buildSynSvar(Syn source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        addIfNotNull(svars, OGONLAKARINTYG_SKICKAS_SEPARAT_SVAR_ID_43, OGONLAKARINTYG_SKICKAS_SEPARAT_DELSVAR_ID_43,
            source.getSeparatOgonlakarintyg());
        addIfNotNull(svars, SYNFALTSPROVNING_DONDER_SVAR_ID_44, SYNFALTSPROVNING_DONDER_DELSVAR_ID_44,
            source.getSynfaltsprovningUtanAnmarkning());

        InternalConverterUtil.SvarBuilder synskarpa = aSvar(SYNSKARPA_SVAR_ID_8);
        if (source.getHoger() != null) {
            if (source.getHoger().getUtanKorrektion() != null) {
                synskarpa.withDelsvar(HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8, source.getHoger().getUtanKorrektion().toString());
            }
            if (source.getHoger().getMedKorrektion() != null) {
                synskarpa.withDelsvar(HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8, source.getHoger().getMedKorrektion().toString());
            }
        }
        if (source.getVanster() != null) {
            if (source.getVanster().getUtanKorrektion() != null) {
                synskarpa.withDelsvar(VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8, source.getVanster().getUtanKorrektion().toString());
            }
            if (source.getVanster().getMedKorrektion() != null) {
                synskarpa.withDelsvar(VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8, source.getVanster().getMedKorrektion().toString());
            }
        }
        if (source.getBinokulart() != null) {
            if (source.getBinokulart().getUtanKorrektion() != null) {
                synskarpa.withDelsvar(BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8, source.getBinokulart().getUtanKorrektion().toString());
            }
            if (source.getBinokulart().getMedKorrektion() != null) {
                synskarpa.withDelsvar(BINOKULART_MED_KORREKTION_DELSVAR_ID_8, source.getBinokulart().getMedKorrektion().toString());
            }
        }
        if (!synskarpa.delSvars.isEmpty()) {
            svars.add(synskarpa.build());
        }
        addIfNotNull(svars, DUBBELSEENDE_SVAR_ID_6, DUBBELSEENDE_DELSVAR_ID_6, source.getDiplopi());
    }

    private static void buildBedomningSvar(Bedomning source, List<Svar> svars) {
        if (source == null) {
            return;
        }
        int behorighetInstans = 1;
        for (BedomningKorkortstyp korkortstyp : source.getKorkortstyp()) {
            KorkortsbehorighetKod korkortsbehorighet = KorkortsbehorighetKod.valueOf(korkortstyp.name());
            svars.add(aSvar(UPPFYLLER_KRAV_FOR_BEHORIGHET_SVAR_ID_33, behorighetInstans++)
                .withDelsvar(UPPFYLLER_KRAV_FOR_BEHORIGHET_DELSVAR_ID_33,
                    aCV(KV_KORKORTSBEHORIGHET_CODE_SYSTEM, korkortsbehorighet.getCode(), korkortsbehorighet.getDescription()))
                .build());
        }
        if (source.getKanInteTaStallning() != null && source.getKanInteTaStallning()) {
            svars.add(aSvar(UPPFYLLER_KRAV_FOR_BEHORIGHET_SVAR_ID_33, behorighetInstans)
                .withDelsvar(UPPFYLLER_KRAV_FOR_BEHORIGHET_DELSVAR_ID_33,
                    aCV(KV_KORKORTSBEHORIGHET_CODE_SYSTEM, KorkortsbehorighetKod.KANINTETASTALLNING.getCode(),
                        KorkortsbehorighetKod.KANINTETASTALLNING.getDescription()))
                .build());
        }
        addIfNotNull(svars, LAMPLIGHET_INNEHA_BEHORIGHET_SVAR_ID_45, LAMPLIGHET_INNEHA_BEHORIGHET_DELSVAR_ID_45,
            source.getLamplighetInnehaBehorighet());
        addIfNotBlank(svars, BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_SVAR_ID_34, BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_DELSVAR_ID_34,
            source.getLakareSpecialKompetens());
    }
}
