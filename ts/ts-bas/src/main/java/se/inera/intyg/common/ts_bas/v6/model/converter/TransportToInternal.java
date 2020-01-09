/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v6.model.converter;

import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getBooleanContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ADHD_ADD_DAMP_ASPERGERS_TOURETTES_DELSVAR_ID_29;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ADHD_ADD_DAMP_ASPERGERS_TOURETTES_SVAR_ID_29;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BALANSRUBBNINGAR_YRSEL_DELSVAR_ID_10;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BALANSRUBBNINGAR_YRSEL_SVAR_ID_10;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BEHANDLING_DIABETES_SVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BINOKULART_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_DELSVAR_ID_34;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_SVAR_ID_34;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.DUBBELSEENDE_DELSVAR_ID_6;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.DUBBELSEENDE_SVAR_ID_6;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID_21;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_RISKFAKTORER_STROKE_DELSVAR_ID_16;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID_31;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.HAR_DIABETES_DELSVAR_ID_17;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.HAR_DIABETES_SVAR_ID_17;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.HJART_ELLER_KARLSJUKDOM_DELSVAR_ID_14;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.HJART_ELLER_KARLSJUKDOM_SVAR_ID_14;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.IDENTITET_STYRKT_GENOM_ID_2;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID_2;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INSULINBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.KONTAKTLINSER_HOGER_OGA_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.KONTAKTLINSER_VANSTER_OGA_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.KOSTBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.LAKEMEDEL_ORDINERAD_DOS_DELSVAR_ID_26;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.MEDICINER_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID_31;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.MEDVETANDESTORNING_SVAR_ID_21;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.MISSBRUK_BEROENDE_SVAR_ID_25;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.NEDSATT_NJURFUNKTION_DELSVAR_ID_22;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.NEDSATT_NJURFUNKTION_SVAR_ID_22;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.NYSTAGMUS_DELSVAR_ID_7;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.NYSTAGMUS_SVAR_ID_7;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.OTILLRACKLIG_RORELSEFORMAGA_DELSVAR_ID_13;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.OTILLRACKLIG_RORELSEFORMAGA_SVAR_ID_13;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.OVRIGA_KOMMENTARER_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.OVRIGA_KOMMENTARER_SVAR_ID_32;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.PLATS_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.PROGRESSIV_OGONSJUKDOM_DELSVAR_ID_5;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.PROGRESSIV_OGONSJUKDOM_SVAR_ID_5;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID_25;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.PSYKISK_SJUKDOM_STORNING_DELSVAR_ID_27;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.PSYKISK_SJUKDOM_STORNING_SVAR_ID_27;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID_28;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.PSYKISK_UTVECKLINGSSTORNING_SVAR_ID_28;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_DELSVAR_ID_26;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_SVAR_ID_26;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.RISKFAKTORER_STROKE_SVAR_ID_16;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SEENDE_NEDSATT_BELYSNING_DELSVAR_ID_4;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SEENDE_NEDSATT_BELYSNING_SVAR_ID_4;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID_12;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.STADIGVARANDE_MEDICINERING_SVAR_ID_31;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SYNFALTSDEFEKTER_DELSVAR_ID_3;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SYNFALTSDEFEKTER_SVAR_ID_3;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.SYNSKARPA_SVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TABLETTBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID_25;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_NEUROLOGISK_SJUKDOM_DELSVAR_ID_20;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_NEUROLOGISK_SJUKDOM_SVAR_ID_20;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_PA_HJARNSKADA_DELSVAR_ID_15;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_PA_HJARNSKADA_SVAR_ID_15;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_DELSVAR_ID_24;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID_24;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_DELSVAR_ID_23;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_SVAR_ID_23;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TIDPUNKT_ORSAK_ANNAN_MEDVETANDESTORNING_DELSVAR_ID_21;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TIDPUNKT_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TYP_AV_DIABETES_DELSVAR_ID_18;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TYP_AV_DIABETES_SVAR_ID_18;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID_16;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.TYP_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_DELSVAR_ID_9;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID_9;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.UPPFATTA_SAMTALSTAMMA_DELSVAR_ID_11;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.UPPFATTA_SAMTALSTAMMA_SVAR_ID_11;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.UPPFYLLER_KRAV_FOR_BEHORIGHET_DELSVAR_ID_33;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.UPPFYLLER_KRAV_FOR_BEHORIGHET_SVAR_ID_33;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.VARDINSATSER_MISSBRUK_BEROENDE_DELSVAR_ID_25;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.VARDKONTAKT_TYP;
import static se.inera.intyg.common.ts_parent.codes.RespConstants.VARD_SJUKHUS_KONTAKT_LAKARE_SVAR_ID_30;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.ts_bas.v6.codes.TsBasKorkortsbehorighetKod;
import se.inera.intyg.common.ts_bas.v6.model.internal.*;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import java.util.EnumSet;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private TransportToInternal() {
    }

    public static TsBasUtlatandeV6 convert(Intyg source) throws ConverterException {
        TsBasUtlatandeV6.Builder utlatande = TsBasUtlatandeV6.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, true));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(TsBasUtlatandeV6.Builder utlatande, Intyg source) throws ConverterException {
        Syn.Builder syn = Syn.builder();
        HorselBalans.Builder horselBalans = HorselBalans.builder();
        Funktionsnedsattning.Builder funktionsnedsattning = Funktionsnedsattning.builder();
        HjartKarl.Builder hjartKarl = HjartKarl.builder();
        Diabetes.Builder diabetes = Diabetes.builder();
        NarkotikaLakemedel.Builder narkotika = NarkotikaLakemedel.builder();
        Bedomning.Builder bedomning = Bedomning.builder();
        Utvecklingsstorning.Builder utvecklingsstorning = Utvecklingsstorning.builder();

        EnumSet<IntygAvserKategori> intygAvserSet = EnumSet.noneOf(IntygAvserKategori.class);
        EnumSet<BedomningKorkortstyp> bedomningsSet = EnumSet.noneOf(BedomningKorkortstyp.class);

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case INTYG_AVSER_SVAR_ID_1:
                    handleIntygAvser(utlatande, svar, intygAvserSet);
                    break;
                case IDENTITET_STYRKT_GENOM_SVAR_ID_2:
                    handleIdentitetStyrktGenom(utlatande, svar);
                    break;
                case SYNFALTSDEFEKTER_SVAR_ID_3:
                    handleSynfaltsdefekter(syn, svar);
                    break;
                case SEENDE_NEDSATT_BELYSNING_SVAR_ID_4:
                    handleSeendeNedsattBelysning(syn, svar);
                    break;
                case PROGRESSIV_OGONSJUKDOM_SVAR_ID_5:
                    handleProgressivOgonsjukdom(syn, svar);
                    break;
                case DUBBELSEENDE_SVAR_ID_6:
                    handleDubbelseende(syn, svar);
                    break;
                case NYSTAGMUS_SVAR_ID_7:
                    handleNystagmus(syn, svar);
                    break;
                case SYNSKARPA_SVAR_ID_8:
                    handleSynskarpa(syn, svar);
                    break;
                case UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID_9:
                    handleUndersokning8DioptriersKorrektionsgrad(syn, svar);
                    break;
                case BALANSRUBBNINGAR_YRSEL_SVAR_ID_10:
                    handleBalansrubbningarYrsel(horselBalans, svar);
                    break;
                case UPPFATTA_SAMTALSTAMMA_SVAR_ID_11:
                    handleUppfattaSamtalstamma(horselBalans, svar);
                    break;
                case SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID_12:
                    handleSjukdomFunktionsnedsattning(funktionsnedsattning, svar);
                    break;
                case OTILLRACKLIG_RORELSEFORMAGA_SVAR_ID_13:
                    handleOtillrackligRorelseformaga(funktionsnedsattning, svar);
                    break;
                case HJART_ELLER_KARLSJUKDOM_SVAR_ID_14:
                    handleHjartEllerKarlsjukdom(hjartKarl, svar);
                    break;
                case TECKEN_PA_HJARNSKADA_SVAR_ID_15:
                    handleTeckenPaHjarnskada(hjartKarl, svar);
                    break;
                case RISKFAKTORER_STROKE_SVAR_ID_16:
                    handleRiskfaktorerStroke(hjartKarl, svar);
                    break;
                case HAR_DIABETES_SVAR_ID_17:
                    handleHarDiabetes(diabetes, svar);
                    break;
                case TYP_AV_DIABETES_SVAR_ID_18:
                    handleTypAvDiabetes(diabetes, svar);
                    break;
                case BEHANDLING_DIABETES_SVAR_ID_19:
                    handleBehandlingDiabetes(diabetes, svar);
                    break;
                case TECKEN_NEUROLOGISK_SJUKDOM_SVAR_ID_20:
                    handleTeckenNeurologiskSjukdom(utlatande, svar);
                    break;
                case MEDVETANDESTORNING_SVAR_ID_21:
                    handleMedvetandestorning(utlatande, svar);
                    break;
                case NEDSATT_NJURFUNKTION_SVAR_ID_22:
                    handleNedsattNjurfunktion(utlatande, svar);
                    break;
                case TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_SVAR_ID_23:
                    handleTeckenSviktandeKognitivFunktion(utlatande, svar);
                    break;
                case TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID_24:
                    handleTeckenSomnEllerVakenhetsstorning(utlatande, svar);
                    break;
                case MISSBRUK_BEROENDE_SVAR_ID_25:
                    handleMissbrukBeroende(narkotika, svar);
                    break;
                case REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_SVAR_ID_26:
                    handleRegelbundetLakarordineratBrukLakemedel(narkotika, svar);
                    break;
                case PSYKISK_SJUKDOM_STORNING_SVAR_ID_27:
                    handlePsykiskSjukdomStorning(utlatande, svar);
                    break;
                case PSYKISK_UTVECKLINGSSTORNING_SVAR_ID_28:
                    handlePsykiskUtvecklingsstorning(utvecklingsstorning, svar);
                    break;
                case ADHD_ADD_DAMP_ASPERGERS_TOURETTES_SVAR_ID_29:
                    handleAdhdAddDampAspergersTourettes(utvecklingsstorning, svar);
                    break;
                case VARD_SJUKHUS_KONTAKT_LAKARE_SVAR_ID_30:
                    handleVardSjukhusKontaktLakare(utlatande, svar);
                    break;
                case STADIGVARANDE_MEDICINERING_SVAR_ID_31:
                    handleStadigvarandeMedicinering(utlatande, svar);
                    break;
                case OVRIGA_KOMMENTARER_SVAR_ID_32:
                    handleOvrigaKommentarer(utlatande, svar);
                    break;
                case UPPFYLLER_KRAV_FOR_BEHORIGHET_SVAR_ID_33:
                    handleUppfyllerKravForBehorighet(bedomning, svar, bedomningsSet);
                    break;
                case BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_SVAR_ID_34:
                    handleBorUndersokasAvSpecialistlakare(bedomning, svar);
                    break;
            }
        }

        bedomning.setKorkortstyp(bedomningsSet);

        utlatande.setSyn(syn.build());
        utlatande.setHorselBalans(horselBalans.build());
        utlatande.setFunktionsnedsattning(funktionsnedsattning.build());
        utlatande.setHjartKarl(hjartKarl.build());
        utlatande.setDiabetes(diabetes.build());
        utlatande.setNarkotikaLakemedel(narkotika.build());
        utlatande.setUtvecklingsstorning(utvecklingsstorning.build());
        utlatande.setBedomning(bedomning.build());
        utlatande.setIntygAvser(IntygAvser.create(intygAvserSet));
    }

    private static void handleIntygAvser(TsBasUtlatandeV6.Builder utlatande, Svar svar,
        EnumSet<IntygAvserKategori> intygAvserSet) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case INTYG_AVSER_DELSVAR_ID_1:
                    intygAvserSet.add(IntygAvserKategori.valueOf(IntygAvserKod.fromCode(getCVSvarContent(delsvar).getCode()).name()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleIdentitetStyrktGenom(TsBasUtlatandeV6.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case IDENTITET_STYRKT_GENOM_ID_2:
                    utlatande.setVardkontakt(Vardkontakt.create(VARDKONTAKT_TYP,
                        IdKontrollKod.fromCode(getCVSvarContent(delsvar).getCode()).name()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSynfaltsdefekter(Syn.Builder syn, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case SYNFALTSDEFEKTER_DELSVAR_ID_3:
                    syn.setSynfaltsdefekter(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSeendeNedsattBelysning(Syn.Builder syn, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case SEENDE_NEDSATT_BELYSNING_DELSVAR_ID_4:
                    syn.setNattblindhet(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleProgressivOgonsjukdom(Syn.Builder syn, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PROGRESSIV_OGONSJUKDOM_DELSVAR_ID_5:
                    syn.setProgressivOgonsjukdom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleDubbelseende(Syn.Builder syn, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case DUBBELSEENDE_DELSVAR_ID_6:
                    syn.setDiplopi(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleNystagmus(Syn.Builder syn, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case NYSTAGMUS_DELSVAR_ID_7:
                    syn.setNystagmus(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSynskarpa(Syn.Builder syn, Svar svar) {
        Synskarpevarden.Builder hogerOga = null;
        Synskarpevarden.Builder vansterOga = null;
        Synskarpevarden.Builder binokulart = null;

        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8:
                    if (hogerOga == null) {
                        hogerOga = Synskarpevarden.builder();
                    }
                    hogerOga.setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8:
                    if (hogerOga == null) {
                        hogerOga = Synskarpevarden.builder();
                    }
                    hogerOga.setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case KONTAKTLINSER_HOGER_OGA_DELSVAR_ID_8:
                    if (hogerOga == null) {
                        hogerOga = Synskarpevarden.builder();
                    }
                    hogerOga.setKontaktlins(getBooleanContent(delsvar));
                    break;
                case VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8:
                    if (vansterOga == null) {
                        vansterOga = Synskarpevarden.builder();
                    }
                    vansterOga.setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8:
                    if (vansterOga == null) {
                        vansterOga = Synskarpevarden.builder();
                    }
                    vansterOga.setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case KONTAKTLINSER_VANSTER_OGA_DELSVAR_ID_8:
                    if (vansterOga == null) {
                        vansterOga = Synskarpevarden.builder();
                    }
                    vansterOga.setKontaktlins(getBooleanContent(delsvar));
                    break;
                case BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8:
                    if (binokulart == null) {
                        binokulart = Synskarpevarden.builder();
                    }
                    binokulart.setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case BINOKULART_MED_KORREKTION_DELSVAR_ID_8:
                    if (binokulart == null) {
                        binokulart = Synskarpevarden.builder();
                    }
                    binokulart.setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
            }
        }
        if (hogerOga != null) {
            syn.setHogerOga(hogerOga.build());
        }
        if (vansterOga != null) {
            syn.setVansterOga(vansterOga.build());
        }
        if (binokulart != null) {
            syn.setBinokulart(binokulart.build());
        }
    }

    private static void handleUndersokning8DioptriersKorrektionsgrad(Syn.Builder syn, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_DELSVAR_ID_9:
                    syn.setKorrektionsglasensStyrka(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBalansrubbningarYrsel(HorselBalans.Builder horselBalans, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case BALANSRUBBNINGAR_YRSEL_DELSVAR_ID_10:
                    horselBalans.setBalansrubbningar(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleUppfattaSamtalstamma(HorselBalans.Builder horselBalans, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case UPPFATTA_SAMTALSTAMMA_DELSVAR_ID_11:
                    horselBalans.setSvartUppfattaSamtal4Meter(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSjukdomFunktionsnedsattning(Funktionsnedsattning.Builder funktionsnedsattning, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID_12:
                    funktionsnedsattning.setFunktionsnedsattning(getBooleanContent(delsvar));
                    break;
                case TYP_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID_12:
                    funktionsnedsattning.setBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOtillrackligRorelseformaga(Funktionsnedsattning.Builder funktionsnedsattning, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case OTILLRACKLIG_RORELSEFORMAGA_DELSVAR_ID_13:
                    funktionsnedsattning.setOtillrackligRorelseformaga(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHjartEllerKarlsjukdom(HjartKarl.Builder hjartKarl, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case HJART_ELLER_KARLSJUKDOM_DELSVAR_ID_14:
                    hjartKarl.setHjartKarlSjukdom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTeckenPaHjarnskada(HjartKarl.Builder hjartKarl, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_PA_HJARNSKADA_DELSVAR_ID_15:
                    hjartKarl.setHjarnskadaEfterTrauma(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleRiskfaktorerStroke(HjartKarl.Builder hjartKarl, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_RISKFAKTORER_STROKE_DELSVAR_ID_16:
                    hjartKarl.setRiskfaktorerStroke(getBooleanContent(delsvar));
                    break;
                case TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID_16:
                    hjartKarl.setBeskrivningRiskfaktorer(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHarDiabetes(Diabetes.Builder diabetes, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case HAR_DIABETES_DELSVAR_ID_17:
                    diabetes.setHarDiabetes(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTypAvDiabetes(Diabetes.Builder diabetes, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TYP_AV_DIABETES_DELSVAR_ID_18:
                    diabetes.setDiabetesTyp(DiabetesKod.fromCode(getCVSvarContent(delsvar).getCode()).name());
                    break;

                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBehandlingDiabetes(Diabetes.Builder diabetes, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case KOSTBEHANDLING_DELSVAR_ID_19:
                    diabetes.setKost(getBooleanContent(delsvar));
                    break;
                case TABLETTBEHANDLING_DELSVAR_ID_19:
                    diabetes.setTabletter(getBooleanContent(delsvar));
                    break;
                case INSULINBEHANDLING_DELSVAR_ID_19:
                    diabetes.setInsulin(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTeckenNeurologiskSjukdom(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_NEUROLOGISK_SJUKDOM_DELSVAR_ID_20:
                    utlatande.setNeurologi(Neurologi.create(getBooleanContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleMedvetandestorning(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        Medvetandestorning.Builder medvetande = Medvetandestorning.builder();
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID_21:
                    medvetande.setMedvetandestorning(getBooleanContent(delsvar));
                    break;
                case TIDPUNKT_ORSAK_ANNAN_MEDVETANDESTORNING_DELSVAR_ID_21:
                    medvetande.setBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        utlatande.setMedvetandestorning(medvetande.build());
    }

    private static void handleNedsattNjurfunktion(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case NEDSATT_NJURFUNKTION_DELSVAR_ID_22:
                    utlatande.setNjurar(Njurar.create(getBooleanContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTeckenSviktandeKognitivFunktion(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_DELSVAR_ID_23:
                    utlatande.setKognitivt(Kognitivt.create(getBooleanContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTeckenSomnEllerVakenhetsstorning(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_SOMN_ELLER_VAKENHETSSTORNING_DELSVAR_ID_24:
                    utlatande.setSomnVakenhet(SomnVakenhet.create(getBooleanContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleMissbrukBeroende(NarkotikaLakemedel.Builder narkotika, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID_25:
                    narkotika.setTeckenMissbruk(getBooleanContent(delsvar));
                    break;
                case VARDINSATSER_MISSBRUK_BEROENDE_DELSVAR_ID_25:
                    narkotika.setForemalForVardinsats(getBooleanContent(delsvar));
                    break;
                case PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID_25:
                    narkotika.setProvtagningBehovs(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleRegelbundetLakarordineratBrukLakemedel(NarkotikaLakemedel.Builder narkotika, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_DELSVAR_ID_26:
                    narkotika.setLakarordineratLakemedelsbruk(getBooleanContent(delsvar));
                    break;
                case LAKEMEDEL_ORDINERAD_DOS_DELSVAR_ID_26:
                    narkotika.setLakemedelOchDos(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handlePsykiskSjukdomStorning(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PSYKISK_SJUKDOM_STORNING_DELSVAR_ID_27:
                    utlatande.setPsykiskt(Psykiskt.create(getBooleanContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handlePsykiskUtvecklingsstorning(Utvecklingsstorning.Builder utvecklingsstorning, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID_28:
                    utvecklingsstorning.setPsykiskUtvecklingsstorning(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleAdhdAddDampAspergersTourettes(Utvecklingsstorning.Builder utvecklingsstorning, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ADHD_ADD_DAMP_ASPERGERS_TOURETTES_DELSVAR_ID_29:
                    utvecklingsstorning.setHarSyndrom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleVardSjukhusKontaktLakare(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        Sjukhusvard.Builder sjukhusvard = Sjukhusvard.builder();
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30:
                    sjukhusvard.setSjukhusEllerLakarkontakt(getBooleanContent(delsvar));
                    break;
                case TIDPUNKT_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30:
                    sjukhusvard.setTidpunkt(getStringContent(delsvar));
                    break;
                case PLATS_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30:
                    sjukhusvard.setVardinrattning(getStringContent(delsvar));
                    break;
                case ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30:
                    sjukhusvard.setAnledning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        utlatande.setSjukhusvard(sjukhusvard.build());
    }

    private static void handleStadigvarandeMedicinering(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        Medicinering.Builder medicinering = Medicinering.builder();
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID_31:
                    medicinering.setStadigvarandeMedicinering(getBooleanContent(delsvar));
                    break;
                case MEDICINER_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID_31:
                    medicinering.setBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        utlatande.setMedicinering(medicinering.build());
    }

    private static void handleOvrigaKommentarer(TsBasUtlatandeV6.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case OVRIGA_KOMMENTARER_DELSVARSVAR_ID_32:
                    utlatande.setKommentar(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleUppfyllerKravForBehorighet(Bedomning.Builder bedomning, Svar svar,
        EnumSet<BedomningKorkortstyp> bedomningsSet) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case UPPFYLLER_KRAV_FOR_BEHORIGHET_DELSVAR_ID_33:
                    TsBasKorkortsbehorighetKod korkortsbehorighetKod =
                        TsBasKorkortsbehorighetKod.fromCode(getCVSvarContent(delsvar).getCode());
                    BedomningKorkortstyp bedomningKorkortstyp = BedomningKorkortstyp.valueOf(korkortsbehorighetKod.name());
                    bedomningsSet.add(bedomningKorkortstyp);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBorUndersokasAvSpecialistlakare(Bedomning.Builder bedomning, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_DELSVAR_ID_34:
                    bedomning.setLakareSpecialKompetens(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

}
