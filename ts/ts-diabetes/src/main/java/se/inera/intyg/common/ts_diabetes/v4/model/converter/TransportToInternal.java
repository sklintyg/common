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

import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getBooleanContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getPartialDateContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

import java.util.EnumSet;
import java.util.Set;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Synfunktion;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvIdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static TsDiabetesUtlatandeV4 convert(Intyg source) throws ConverterException {
        TsDiabetesUtlatandeV4.Builder utlatande = TsDiabetesUtlatandeV4.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, PatientInfo.BASIC));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(TsDiabetesUtlatandeV4.Builder utlatande, Intyg source) throws ConverterException {

        Set<IntygAvserKategori> intygAvserSet = EnumSet.noneOf(IntygAvserKategori.class);
        Allmant.Builder allmant = Allmant.builder();
        Bedomning.Builder bedomning = Bedomning.builder();
        Set<BedomningKorkortstyp> bedomningUppfyllerBehorighetskrav = EnumSet.noneOf(BedomningKorkortstyp.class);
        Hypoglykemier.Builder hypoglykemier = Hypoglykemier.builder();
        Synfunktion.Builder synfunktion = Synfunktion.builder();

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case RespConstants.INTYGETAVSER_SVAR_ID:
                    handleIntygAvser(intygAvserSet, svar);
                    break;
                case RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID:
                    handleIdentitetStyrkt(utlatande, svar);
                    break;
                case RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID:
                    handleAllmantDiabetesDiagnosAr(allmant, svar);
                    break;
                case RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID:
                    handleAllmantTypAvDiabetes(allmant, svar);
                    break;
                case RespConstants.ALLMANT_BEHANDLING_SVAR_ID:
                    handleAllmantBehandling(allmant, svar);
                    break;
                case RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_SVAR_ID:
                    handleHypoglykemierEgenkontrollBlodsocker(hypoglykemier, svar);
                    break;
                case RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_SVAR_ID:
                    handleHypoglykemierNedsattHjarnfunktion(hypoglykemier, svar);
                    break;
                case RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_SVAR_ID:
                    handleHypoglykemierSjukdomUnderKontroll(hypoglykemier, svar);
                    break;
                case RespConstants.HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_SVAR_ID:
                    handleHypoglykemierFormagaVarningstecken(hypoglykemier, svar);
                    break;
                case RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_SVAR_ID:
                    handleHypoglykemierAterkommandeSenasteAret(hypoglykemier, svar);
                    break;
                case RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_SVAR_ID:
                    handleHypoglykemierAterkommandeSenasteKvartalet(hypoglykemier, svar);
                    break;
                case RespConstants.HYPOGLYKEMIER_FOREKOMST_SENASTE_TRAFIK_SVAR_ID:
                    handleHypoglykemierForekomstSenasteTrafik(hypoglykemier, svar);
                    break;
                case RespConstants.SYNFUNKTION_MISSTANKE_OGONSJUKDOM_SVAR_ID:
                    handleSynfunktion(synfunktion, svar);
                    break;
                case RespConstants.SYNFUNKTION_SYNSKARPA_SVAR_ID:
                    handleSynfunktionSynskarpa(synfunktion, svar);
                    break;
                case RespConstants.OVRIGT_SVAR_ID:
                    handleOvrigt(utlatande, svar);
                    break;
                case RespConstants.BEDOMNING_SVAR_ID:
                    handleBedomning(bedomningUppfyllerBehorighetskrav, svar);
                    break;
                case RespConstants.BEDOMNING_LAMPLIGHET_SVAR_ID:
                    handleBedomningLamplighet(bedomning, svar);
                    break;
                case RespConstants.BEDOMNING_BOR_UNDERSOKAS_SVAR_ID:
                    handleBedomningBorUndersokas(bedomning, svar);
                    break;
                default:
                    break;
            }
        }

        if (!intygAvserSet.isEmpty()) {
            utlatande.setIntygAvser(IntygAvser.create(intygAvserSet));
        }
        utlatande.setAllmant(allmant.build());
        if (!bedomningUppfyllerBehorighetskrav.isEmpty()) {
            bedomning.setUppfyllerBehorighetskrav(bedomningUppfyllerBehorighetskrav);
        }
        utlatande.setBedomning(bedomning.build());
        utlatande.setHypoglykemier(hypoglykemier.build());
        utlatande.setSynfunktion(synfunktion.build());
    }

    private static void handleIntygAvser(Set<IntygAvserKategori> intygAvserSet, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.INTYGETAVSER_DELSVAR_ID:
                    intygAvserSet.add(IntygAvserKategori.valueOf(getCVSvarContent(delsvar).getCode()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleIdentitetStyrkt(TsDiabetesUtlatandeV4.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.IDENTITET_STYRKT_GENOM_DELSVAR_ID:
                    utlatande.setIdentitetStyrktGenom(IdKontroll.create(KvIdKontroll.fromCode(getCVSvarContent(delsvar).getCode())));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleAllmantDiabetesDiagnosAr(Allmant.Builder allmant, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_DELSVAR_ID:
                    allmant.setDiabetesDiagnosAr(getPartialDateContent(delsvar).getValue().toString());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleAllmantTypAvDiabetes(Allmant.Builder allmant, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.ALLMANT_TYP_AV_DIABETES_DELSVAR_ID:
                    allmant.setTypAvDiabetes(KvTypAvDiabetes.fromCode(getCVSvarContent(delsvar).getCode()));
                    break;
                case RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_DELSVAR_ID:
                    allmant.setTypAvDiabetes(KvTypAvDiabetes.ANNAN);
                    allmant.setBeskrivningAnnanTypAvDiabetes(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleAllmantBehandling(Allmant.Builder allmant, Svar svar) throws ConverterException {
        Behandling.Builder behandling = Behandling.builder();
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.ALLMANT_BEHANDLING_ENDAST_KOST_DELSVAR_ID:
                    behandling.setEndastKost(getBooleanContent(delsvar));
                    break;
                case RespConstants.ALLMANT_BEHANDLING_TABLETTER_DELSVAR_ID:
                    behandling.setTabletter(getBooleanContent(delsvar));
                    break;
                case RespConstants.ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID:
                    behandling.setInsulin(getBooleanContent(delsvar));
                    break;
                case RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_DELSVAR_ID:
                    behandling.setInsulinSedanAr(getPartialDateContent(delsvar).getValue().toString());
                    break;
                case RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_DELSVAR_ID:
                    behandling.setAnnanBehandling(getBooleanContent(delsvar));
                    break;
                case RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_DELSVAR_ID:
                    behandling.setAnnanBehandlingBeskrivning(getStringContent(delsvar));
                    break;
                case RespConstants.ALLMANT_BEHANDLING_RISK_HYPOGLYKEMI_DELSVAR_ID:
                    behandling.setRiskHypoglykemi(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        allmant.setBehandling(behandling.build());
    }

    private static void handleHypoglykemierEgenkontrollBlodsocker(Hypoglykemier.Builder hypoglykemier, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_DELSVAR_ID:
                    hypoglykemier.setEgenkontrollBlodsocker(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHypoglykemierNedsattHjarnfunktion(Hypoglykemier.Builder hypoglykemier, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_DELSVAR_ID:
                    hypoglykemier.setNedsattHjarnfunktion(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHypoglykemierSjukdomUnderKontroll(Hypoglykemier.Builder hypoglykemier, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_DELSVAR_ID:
                    hypoglykemier.setSjukdomenUnderKontroll(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHypoglykemierFormagaVarningstecken(Hypoglykemier.Builder hypoglykemier, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_DELSVAR_ID:
                    hypoglykemier.setFormagaVarningstecken(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHypoglykemierAterkommandeSenasteAret(Hypoglykemier.Builder hypoglykemier, Svar svar)
        throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID:
                    hypoglykemier.setAterkommandeSenasteAret(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID:
                    hypoglykemier.setAterkommandeSenasteTidpunkt(new InternalDate(getPartialDateContent(delsvar).getValue().toString()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHypoglykemierAterkommandeSenasteKvartalet(Hypoglykemier.Builder hypoglykemier, Svar svar)
        throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_DELSVAR_ID:
                    hypoglykemier.setAterkommandeSenasteKvartalet(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_DELSVAR_ID:
                    hypoglykemier.setSenasteTidpunktVaken(new InternalDate(getPartialDateContent(delsvar).getValue().toString()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHypoglykemierForekomstSenasteTrafik(Hypoglykemier.Builder hypoglykemier, Svar svar)
        throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_SVAR_DELSVAR_ID:
                    hypoglykemier.setForekomstTrafik(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_DELSVAR_ID:
                    hypoglykemier.setForekomstTrafikTidpunkt(new InternalDate(getPartialDateContent(delsvar).getValue().toString()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSynfunktion(Synfunktion.Builder synfunktion, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.SYNFUNKTION_MISSTANKE_OGONSJUKDOM_DELSVAR_ID:
                    synfunktion.setMisstankeOgonsjukdom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSynfunktionSynskarpa(Synfunktion.Builder synfunktion, Svar svar) {
        Synskarpevarden.Builder hoger = Synskarpevarden.builder();
        Synskarpevarden.Builder vanster = Synskarpevarden.builder();
        Synskarpevarden.Builder binokulart = Synskarpevarden.builder();
        Boolean skickasSeparat = Boolean.FALSE;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_DELSVAR_ID:
                    skickasSeparat = getBooleanContent(delsvar);
                    break;
                case RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_UTAN_KORREKTION_DELSVAR_ID:
                    hoger.setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_UTAN_KORREKTION_DELSVAR_ID:
                    vanster.setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_UTAN_KORREKTION_DELSVAR_ID:
                    binokulart.setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_MED_KORREKTION_DELSVAR_ID:
                    hoger.setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_MED_KORREKTION_DELSVAR_ID:
                    vanster.setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_MED_KORREKTION_DELSVAR_ID:
                    binokulart.setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        synfunktion.setSkickasSeparat(skickasSeparat);
        synfunktion.setHoger(hoger.build());
        synfunktion.setVanster(vanster.build());
        synfunktion.setBinokulart(binokulart.build());
    }

    private static void handleOvrigt(TsDiabetesUtlatandeV4.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
            case RespConstants.OVRIGT_DELSVAR_ID:
                utlatande.setOvrigt(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void handleBedomning(Set<BedomningKorkortstyp> bedomningUppfyllerBehorighetskrav, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID:
                    bedomningUppfyllerBehorighetskrav.add(BedomningKorkortstyp.valueOf(getCVSvarContent(delsvar).getCode()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBedomningLamplighet(Bedomning.Builder bedomning, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.BEDOMNING_LAMPLIGHET_ATT_INNEHA_DELSVAR_ID:
                    bedomning.setLampligtInnehav(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBedomningBorUndersokas(Bedomning.Builder bedomning, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.BEDOMNING_BOR_UNDERSOKAS_DELSVAR_ID:
                    bedomning.setBorUndersokasBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
