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
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvIdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvVardniva;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static TsDiabetesUtlatandeV4 convert(Intyg source) throws ConverterException {
        final var utlatande = TsDiabetesUtlatandeV4.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, PatientInfo.BASIC));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(TsDiabetesUtlatandeV4.Builder utlatande, Intyg source) throws ConverterException {
        final var intygAvserSet = EnumSet.noneOf(IntygAvserKategori.class);
        final var allmant = Allmant.builder();
        final var bedomning = Bedomning.builder();
        final var bedomningUppfyllerBehorighetskrav = EnumSet.noneOf(BedomningKorkortstyp.class);
        final var hypoglykemi = Hypoglykemi.builder();
        final var ovrigt = Ovrigt.builder();

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case RespConstants.INTYGETAVSER_SVAR_ID:
                    handleIntygAvser(intygAvserSet, svar);
                    break;
                case RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID:
                    handleIdentitetStyrkt(utlatande, svar);
                    break;
                case RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_SVAR_ID:
                    handleAllmantPatientenFoljsAv(allmant, svar);
                    break;
                case RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID:
                    handleAllmantDiabetesDiagnosAr(allmant, svar);
                    break;
                case RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID:
                    handleAllmantTypAvDiabetes(allmant, svar);
                    break;
                case RespConstants.ALLMANT_MEDICINERING_FOR_DIABETES_SVAR_ID:
                    handleAllmantMedicineringForDiabetes(allmant, svar);
                    break;
                case RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID:
                    handleAllmantMedicineringMedforRiskForHypoglykemi(allmant, svar);
                    break;
                case RespConstants.ALLMANT_BEHANDLING_SVAR_ID:
                    handleAllmantBehandling(allmant, svar);
                    break;
                case RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_SVAR_ID:
                    handleAllmantMedicineringMedforRiskForHypoglykemiTidpunkt(allmant, svar);
                    break;
                case RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID:
                    handleHypoglykemiKontrollSjukdomstillstand(hypoglykemi, svar);
                    break;
                case RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID:
                    handleHypoglykemiForstarRiskerMedHypoglykemi(hypoglykemi, svar);
                    break;
                case RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID:
                    handleHypoglykemiFormagaKannaVarningstecken(hypoglykemi, svar);
                    break;
                case RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID:
                    handleHypoglykemiVidtaAdekvataAtgarder(hypoglykemi, svar);
                    break;
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID:
                    handleHypoglykemiAterkommandeSenasteAret(hypoglykemi, svar);
                    break;
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID:
                    handleHypoglykemiAterkommandeVaketSenasteTolv(hypoglykemi, svar);
                    break;
                case RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID:
                    handleHypoglykemiAllvarligSenasteTolvManaderna(hypoglykemi, svar);
                    break;
                case RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID:
                    handleHypoglykemiRegelbundnaBlodsockerkontroller(hypoglykemi, svar);
                    break;

                case RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID:
                    handleKomplikationerAvSjukdomen(ovrigt, svar);
                    break;
                case RespConstants.OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_SVAR_ID:
                    handleBorUndersokasAvSpecialist(ovrigt, svar);
                    break;

                case RespConstants.BEDOMNING_SVAR_ID:
                    handleBedomning(bedomningUppfyllerBehorighetskrav, svar);
                    break;
                case RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID:
                    handleOvrigaKommentarer(bedomning, svar);
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
        utlatande.setHypoglykemi(hypoglykemi.build());
        utlatande.setOvrigt(ovrigt.build());
    }

    private static void handleIntygAvser(Set<IntygAvserKategori> intygAvserSet, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.INTYGETAVSER_DELSVAR_ID:
                    intygAvserSet.add(IntygAvserKategori.valueOf(getCVSvarContent(delsvar).getCode()));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid delsvar id received, expected id IntigetAvser.");
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
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleIdentitetStyrkt'.");
            }
        }
    }

    private static void handleAllmantPatientenFoljsAv(Allmant.Builder allmant, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_DELSVAR_ID:
                    allmant.setPatientenFoljsAv(KvVardniva.fromCode(getCVSvarContent(delsvar).getCode()));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleAllmantPatientenFoljsAv'.");
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
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleAllmantDiabetesDiagnosAr'.");
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
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleAllmantTypAvDiabetes'.");
            }
        }
    }

    private static void handleAllmantMedicineringForDiabetes(Allmant.Builder allmant, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.ALLMANT_MEDICINERING_FOR_DIABETES_DELSVAR_ID:
                    allmant.setMedicineringForDiabetes(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleAllmantMedicineringForDiabetes'.");
            }
        }
    }

    private static void handleAllmantMedicineringMedforRiskForHypoglykemi(Allmant.Builder allmant, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_DELSVAR_ID:
                    allmant.setMedicineringMedforRiskForHypoglykemi(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleAllmantMedicineringMedforRiskForHypoglykemi'.");
            }
        }
    }

    private static void handleAllmantBehandling(Allmant.Builder allmant, Svar svar) throws ConverterException {
        Behandling.Builder behandling = Behandling.builder();
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID:
                    behandling.setInsulin(getBooleanContent(delsvar));
                    break;
                case RespConstants.ALLMANT_BEHANDLING_TABLETTER_DELSVAR_ID:
                    behandling.setTabletter(getBooleanContent(delsvar));
                    break;
                case RespConstants.ALLMANT_BEHANDLING_ANNAN_DELSVAR_ID:
                    behandling.setAnnan(getBooleanContent(delsvar));
                    break;
                case RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID:
                    behandling.setAnnanAngeVilken(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleAllmantBehandling'.");
            }
        }
        allmant.setBehandling(behandling.build());
    }

    private static void handleAllmantMedicineringMedforRiskForHypoglykemiTidpunkt(Allmant.Builder allmant, Svar svar)
        throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_DELSVAR_ID:
                    allmant.setMedicineringMedforRiskForHypoglykemiTidpunkt(
                        new InternalDate(getPartialDateContent(delsvar).getValue().toString()));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleAllmantMedicineringMedforRiskForHypoglykemiTidpunkt'.");
            }
        }
    }

    private static void handleHypoglykemiKontrollSjukdomstillstand(Hypoglykemi.Builder hypoglykemi, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_DELSVAR_ID:
                    hypoglykemi.setKontrollSjukdomstillstand(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID:
                    hypoglykemi.setKontrollSjukdomstillstandVarfor(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleHypoglykemiKontrollSjukdomstillstand'.");
            }
        }
    }

    private static void handleHypoglykemiForstarRiskerMedHypoglykemi(Hypoglykemi.Builder hypoglykemi, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DELSVAR_ID:
                    hypoglykemi.setForstarRiskerMedHypoglykemi(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleHypoglykemiForstarRiskerMedHypoglykemi'.");
            }
        }
    }

    private static void handleHypoglykemiFormagaKannaVarningstecken(Hypoglykemi.Builder hypoglykemi, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_DELSVAR_ID:
                    hypoglykemi.setFormagaKannaVarningstecken(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleHypoglykemiFormagaKannaVarningstecken'.");
            }
        }
    }

    private static void handleHypoglykemiVidtaAdekvataAtgarder(Hypoglykemi.Builder hypoglykemi, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_DELSVAR_ID:
                    hypoglykemi.setVidtaAdekvataAtgarder(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleHypoglykemiVidtaAdekvataAtgarder'.");
            }
        }
    }

    private static void handleHypoglykemiAterkommandeSenasteAret(Hypoglykemi.Builder hypoglykemi, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_DELSVAR_ID:
                    hypoglykemi.setAterkommandeSenasteAret(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID:
                    hypoglykemi.setAterkommandeSenasteAretTidpunkt(new InternalDate(getPartialDateContent(delsvar).getValue().toString()));
                    break;
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID:
                    hypoglykemi.setAterkommandeSenasteAretKontrolleras(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID:
                    hypoglykemi.setAterkommandeSenasteAretTrafik(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleHypoglykemiAterkommandeSenasteAret'.");
            }
        }
    }

    private static void handleHypoglykemiAterkommandeVaketSenasteTolv(Hypoglykemi.Builder hypoglykemi, Svar svar)
        throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_DELSVAR_ID:
                    hypoglykemi.setAterkommandeVaketSenasteTolv(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID:
                    hypoglykemi.setAterkommandeVaketSenasteTre(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID:
                    hypoglykemi.setAterkommandeVaketSenasteTreTidpunkt(new InternalDate(getPartialDateContent(delsvar).getValue()
                        .toString()));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleHypoglykemiAterkommandeVaketSenasteTolv'.");
            }
        }
    }

    private static void handleHypoglykemiAllvarligSenasteTolvManaderna(Hypoglykemi.Builder hypoglykemi, Svar svar)
        throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_DELSVAR_ID:
                    hypoglykemi.setAllvarligSenasteTolvManaderna(getBooleanContent(delsvar));
                    break;
                case RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID:
                    hypoglykemi.setAllvarligSenasteTolvManadernaTidpunkt(new InternalDate(getPartialDateContent(delsvar).getValue()
                        .toString()));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleHypoglykemiAllvarligSenasteTolvManaderna'.");
            }
        }
    }

    private static void handleHypoglykemiRegelbundnaBlodsockerkontroller(Hypoglykemi.Builder hypoglykemi, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_DELSVAR_ID:
                    hypoglykemi.setRegelbundnaBlodsockerkontroller(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleHypoglykemiRegelbundnaBlodsockerkontroller'.");
            }
        }
    }

    private static void handleKomplikationerAvSjukdomen(Ovrigt.Builder ovrigt, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_DELSVAR_ID:
                    ovrigt.setKomplikationerAvSjukdomen(getBooleanContent(delsvar));
                    break;
                case RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID:
                    ovrigt.setKomplikationerAvSjukdomenAnges(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleKomplikationerAvSjukdomen'.");
            }
        }
    }

    private static void handleBorUndersokasAvSpecialist(Ovrigt.Builder ovrigt, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_DELSVAR_ID:
                    ovrigt.setBorUndersokasAvSpecialist(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleBorUndersokasAvSpecialist'.");
            }
        }
    }

    private static void handleBedomning(Set<BedomningKorkortstyp> bedomningUppfyllerBehorighetskrav, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID:
                    bedomningUppfyllerBehorighetskrav.add(BedomningKorkortstyp.valueOf(getCVSvarContent(delsvar).getCode()));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleBedomning'.");
            }
        }
    }

    private static void handleOvrigaKommentarer(Bedomning.Builder bedomning, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_DELSVAR_ID:
                    bedomning.setOvrigaKommentarer(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected delsvar id '" + delsvar.getId()
                        + "' received in method 'handleOvrigaKommentarer'.");
            }
        }
    }
}
