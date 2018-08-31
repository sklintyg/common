/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.model.converter;

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
import se.inera.intyg.common.ts_bas.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_bas.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_bas.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_bas.model.internal.TsBasUtlatande;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intygstjanster.ts.services.v1.Korkortsbehorighet;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private TransportToInternal() {
    }

    public static TsBasUtlatande convert(Intyg source) throws ConverterException {
        TsBasUtlatande utlatande = new TsBasUtlatande();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, false));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande;
    }

    private static void setSvar(TsBasUtlatande utlatande, Intyg source) throws ConverterException {
        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case INTYG_AVSER_SVAR_ID_1:
                    handleIntygAvser(utlatande, svar);
                    break;
                case IDENTITET_STYRKT_GENOM_SVAR_ID_2:
                    handleIdentitetStyrktGenom(utlatande, svar);
                    break;
                case SYNFALTSDEFEKTER_SVAR_ID_3:
                    handleSynfaltsdefekter(utlatande, svar);
                    break;
                case SEENDE_NEDSATT_BELYSNING_SVAR_ID_4:
                    handleSeendeNedsattBelysning(utlatande, svar);
                    break;
                case PROGRESSIV_OGONSJUKDOM_SVAR_ID_5:
                    handleProgressivOgonsjukdom(utlatande, svar);
                    break;
                case DUBBELSEENDE_SVAR_ID_6:
                    handleDubbelseende(utlatande, svar);
                    break;
                case NYSTAGMUS_SVAR_ID_7:
                    handleNystagmus(utlatande, svar);
                    break;
                case SYNSKARPA_SVAR_ID_8:
                    handleSynskarpa(utlatande, svar);
                    break;
                case UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID_9:
                    handleUndersokning8DioptriersKorrektionsgrad(utlatande, svar);
                    break;
                case BALANSRUBBNINGAR_YRSEL_SVAR_ID_10:
                    handleBalansrubbningarYrsel(utlatande, svar);
                    break;
                case UPPFATTA_SAMTALSTAMMA_SVAR_ID_11:
                    handleUppfattaSamtalstamma(utlatande, svar);
                    break;
                case SJUKDOM_FUNKTIONSNEDSATTNING_SVAR_ID_12:
                    handleSjukdomFunktionsnedsattning(utlatande, svar);
                    break;
                case OTILLRACKLIG_RORELSEFORMAGA_SVAR_ID_13:
                    handleOtillrackligRorelseformaga(utlatande, svar);
                    break;
                case HJART_ELLER_KARLSJUKDOM_SVAR_ID_14:
                    handleHjartEllerKarlsjukdom(utlatande, svar);
                    break;
                case TECKEN_PA_HJARNSKADA_SVAR_ID_15:
                    handleTeckenPaHjarnskada(utlatande, svar);
                    break;
                case RISKFAKTORER_STROKE_SVAR_ID_16:
                    handleRiskfaktorerStroke(utlatande, svar);
                    break;
                case HAR_DIABETES_SVAR_ID_17:
                    handleHarDiabetes(utlatande, svar);
                    break;
                case TYP_AV_DIABETES_SVAR_ID_18:
                    handleTypAvDiabetes(utlatande, svar);
                    break;
                case BEHANDLING_DIABETES_SVAR_ID_19:
                    handleBehandlingDiabetes(utlatande, svar);
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
                    handleMissbrukBeroende(utlatande, svar);
                    break;
                case REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_SVAR_ID_26:
                    handleRegelbundetLakarordineratBrukLakemedel(utlatande, svar);
                    break;
                case PSYKISK_SJUKDOM_STORNING_SVAR_ID_27:
                    handlePsykiskSjukdomStorning(utlatande, svar);
                    break;
                case PSYKISK_UTVECKLINGSSTORNING_SVAR_ID_28:
                    handlePsykiskUtvecklingsstorning(utlatande, svar);
                    break;
                case ADHD_ADD_DAMP_ASPERGERS_TOURETTES_SVAR_ID_29:
                    handleAdhdAddDampAspergersTourettes(utlatande, svar);
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
                    handleUppfyllerKravForBehorighet(utlatande, svar);
                    break;
                case BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_SVAR_ID_34:
                    handleBorUndersokasAvSpecialistlakare(utlatande, svar);
                    break;
            }
        }
    }

    private static void handleIntygAvser(TsBasUtlatande utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case INTYG_AVSER_DELSVAR_ID_1:
                    IntygAvserKod intygAvserKod = IntygAvserKod.fromCode(getCVSvarContent(delsvar).getCode());
                    utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.valueOf(intygAvserKod.name()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleIdentitetStyrktGenom(TsBasUtlatande utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case IDENTITET_STYRKT_GENOM_ID_2:
                    utlatande.getVardkontakt().setTyp(VARDKONTAKT_TYP);
                    utlatande.getVardkontakt().setIdkontroll(IdKontrollKod.fromCode(getCVSvarContent(delsvar).getCode()).name());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSynfaltsdefekter(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case SYNFALTSDEFEKTER_DELSVAR_ID_3:
                    utlatande.getSyn().setSynfaltsdefekter(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSeendeNedsattBelysning(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case SEENDE_NEDSATT_BELYSNING_DELSVAR_ID_4:
                    utlatande.getSyn().setNattblindhet(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleProgressivOgonsjukdom(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PROGRESSIV_OGONSJUKDOM_DELSVAR_ID_5:
                    utlatande.getSyn().setProgressivOgonsjukdom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleDubbelseende(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case DUBBELSEENDE_DELSVAR_ID_6:
                    utlatande.getSyn().setDiplopi(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleNystagmus(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case NYSTAGMUS_DELSVAR_ID_7:
                    utlatande.getSyn().setNystagmus(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSynskarpa(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case HOGER_OGA_UTAN_KORREKTION_DELSVAR_ID_8:
                    if (utlatande.getSyn().getHogerOga() == null) {
                        utlatande.getSyn().setHogerOga(new Synskarpevarden());
                    }
                    utlatande.getSyn().getHogerOga().setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case HOGER_OGA_MED_KORREKTION_DELSVAR_ID_8:
                    if (utlatande.getSyn().getHogerOga() == null) {
                        utlatande.getSyn().setHogerOga(new Synskarpevarden());
                    }
                    utlatande.getSyn().getHogerOga().setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case KONTAKTLINSER_HOGER_OGA_DELSVAR_ID_8:
                    if (utlatande.getSyn().getHogerOga() == null) {
                        utlatande.getSyn().setHogerOga(new Synskarpevarden());
                    }
                    utlatande.getSyn().getHogerOga().setKontaktlins(getBooleanContent(delsvar));
                    break;
                case VANSTER_OGA_UTAN_KORREKTION_DELSVAR_ID_8:
                    if (utlatande.getSyn().getVansterOga() == null) {
                        utlatande.getSyn().setVansterOga(new Synskarpevarden());
                    }
                    utlatande.getSyn().getVansterOga().setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case VANSTER_OGA_MED_KORREKTION_DELSVAR_ID_8:
                    if (utlatande.getSyn().getVansterOga() == null) {
                        utlatande.getSyn().setVansterOga(new Synskarpevarden());
                    }
                    utlatande.getSyn().getVansterOga().setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case KONTAKTLINSER_VANSTER_OGA_DELSVAR_ID_8:
                    if (utlatande.getSyn().getVansterOga() == null) {
                        utlatande.getSyn().setVansterOga(new Synskarpevarden());
                    }
                    utlatande.getSyn().getVansterOga().setKontaktlins(getBooleanContent(delsvar));
                    break;
                case BINOKULART_UTAN_KORREKTION_DELSVAR_ID_8:
                    if (utlatande.getSyn().getBinokulart() == null) {
                        utlatande.getSyn().setBinokulart(new Synskarpevarden());
                    }
                    utlatande.getSyn().getBinokulart().setUtanKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
                case BINOKULART_MED_KORREKTION_DELSVAR_ID_8:
                    if (utlatande.getSyn().getBinokulart() == null) {
                        utlatande.getSyn().setBinokulart(new Synskarpevarden());
                    }
                    utlatande.getSyn().getBinokulart().setMedKorrektion(Double.valueOf(getStringContent(delsvar)));
                    break;
            }
        }
    }

    private static void handleUndersokning8DioptriersKorrektionsgrad(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_DELSVAR_ID_9:
                    utlatande.getSyn().setKorrektionsglasensStyrka(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBalansrubbningarYrsel(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case BALANSRUBBNINGAR_YRSEL_DELSVAR_ID_10:
                    utlatande.getHorselBalans().setBalansrubbningar(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleUppfattaSamtalstamma(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case UPPFATTA_SAMTALSTAMMA_DELSVAR_ID_11:
                    utlatande.getHorselBalans().setSvartUppfattaSamtal4Meter(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSjukdomFunktionsnedsattning(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID_12:
                    utlatande.getFunktionsnedsattning().setFunktionsnedsattning(getBooleanContent(delsvar));
                    break;
                case TYP_SJUKDOM_FUNKTIONSNEDSATTNING_DELSVAR_ID_12:
                    utlatande.getFunktionsnedsattning().setBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOtillrackligRorelseformaga(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case OTILLRACKLIG_RORELSEFORMAGA_DELSVAR_ID_13:
                    utlatande.getFunktionsnedsattning().setOtillrackligRorelseformaga(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHjartEllerKarlsjukdom(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case HJART_ELLER_KARLSJUKDOM_DELSVAR_ID_14:
                    utlatande.getHjartKarl().setHjartKarlSjukdom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTeckenPaHjarnskada(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_PA_HJARNSKADA_DELSVAR_ID_15:
                    utlatande.getHjartKarl().setHjarnskadaEfterTrauma(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleRiskfaktorerStroke(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_RISKFAKTORER_STROKE_DELSVAR_ID_16:
                    utlatande.getHjartKarl().setRiskfaktorerStroke(getBooleanContent(delsvar));
                    break;
                case TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID_16:
                    utlatande.getHjartKarl().setBeskrivningRiskfaktorer(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleHarDiabetes(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case HAR_DIABETES_DELSVAR_ID_17:
                    utlatande.getDiabetes().setHarDiabetes(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTypAvDiabetes(TsBasUtlatande utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TYP_AV_DIABETES_DELSVAR_ID_18:
                    utlatande.getDiabetes().setDiabetesTyp(DiabetesKod.fromCode(getCVSvarContent(delsvar).getCode()).name());
                    break;

                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBehandlingDiabetes(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case KOSTBEHANDLING_DELSVAR_ID_19:
                    utlatande.getDiabetes().setKost(getBooleanContent(delsvar));
                    break;
                case TABLETTBEHANDLING_DELSVAR_ID_19:
                    utlatande.getDiabetes().setTabletter(getBooleanContent(delsvar));
                    break;
                case INSULINBEHANDLING_DELSVAR_ID_19:
                    utlatande.getDiabetes().setInsulin(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTeckenNeurologiskSjukdom(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_NEUROLOGISK_SJUKDOM_DELSVAR_ID_20:
                    utlatande.getNeurologi().setNeurologiskSjukdom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleMedvetandestorning(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID_21:
                    utlatande.getMedvetandestorning().setMedvetandestorning(getBooleanContent(delsvar));
                    break;
                case TIDPUNKT_ORSAK_ANNAN_MEDVETANDESTORNING_DELSVAR_ID_21:
                    utlatande.getMedvetandestorning().setBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleNedsattNjurfunktion(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case NEDSATT_NJURFUNKTION_DELSVAR_ID_22:
                    utlatande.getNjurar().setNedsattNjurfunktion(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTeckenSviktandeKognitivFunktion(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_SVIKTANDE_KOGNITIV_FUNKTION_DELSVAR_ID_23:
                    utlatande.getKognitivt().setSviktandeKognitivFunktion(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleTeckenSomnEllerVakenhetsstorning(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_SOMN_ELLER_VAKENHETSSTORNING_DELSVAR_ID_24:
                    utlatande.getSomnVakenhet().setTeckenSomnstorningar(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleMissbrukBeroende(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TECKEN_MISSBRUK_BEROENDE_DELSVAR_ID_25:
                    utlatande.getNarkotikaLakemedel().setTeckenMissbruk(getBooleanContent(delsvar));
                    break;
                case VARDINSATSER_MISSBRUK_BEROENDE_DELSVAR_ID_25:
                    utlatande.getNarkotikaLakemedel().setForemalForVardinsats(getBooleanContent(delsvar));
                    break;
                case PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID_25:
                    utlatande.getNarkotikaLakemedel().setProvtagningBehovs(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleRegelbundetLakarordineratBrukLakemedel(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case REGELBUNDET_LAKARORDINERAT_BRUK_LAKEMEDEL_DELSVAR_ID_26:
                    utlatande.getNarkotikaLakemedel().setLakarordineratLakemedelsbruk(getBooleanContent(delsvar));
                    break;
                case LAKEMEDEL_ORDINERAD_DOS_DELSVAR_ID_26:
                    utlatande.getNarkotikaLakemedel().setLakemedelOchDos(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handlePsykiskSjukdomStorning(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PSYKISK_SJUKDOM_STORNING_DELSVAR_ID_27:
                    utlatande.getPsykiskt().setPsykiskSjukdom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handlePsykiskUtvecklingsstorning(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PSYKISK_UTVECKLINGSSTORNING_DELSVAR_ID_28:
                    utlatande.getUtvecklingsstorning().setPsykiskUtvecklingsstorning(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleAdhdAddDampAspergersTourettes(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ADHD_ADD_DAMP_ASPERGERS_TOURETTES_DELSVAR_ID_29:
                    utlatande.getUtvecklingsstorning().setHarSyndrom(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleVardSjukhusKontaktLakare(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30:
                    utlatande.getSjukhusvard().setSjukhusEllerLakarkontakt(getBooleanContent(delsvar));
                    break;
                case TIDPUNKT_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30:
                    utlatande.getSjukhusvard().setTidpunkt(getStringContent(delsvar));
                    break;
                case PLATS_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30:
                    utlatande.getSjukhusvard().setVardinrattning(getStringContent(delsvar));
                    break;
                case ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID_30:
                    utlatande.getSjukhusvard().setAnledning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleStadigvarandeMedicinering(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FOREKOMST_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID_31:
                    utlatande.getMedicinering().setStadigvarandeMedicinering(getBooleanContent(delsvar));
                    break;
                case MEDICINER_STADIGVARANDE_MEDICINERING_DELSVARSVAR_ID_31:
                    utlatande.getMedicinering().setBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOvrigaKommentarer(TsBasUtlatande utlatande, Svar svar) {
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

    private static void handleUppfyllerKravForBehorighet(TsBasUtlatande utlatande, Svar svar) throws ConverterException {
        utlatande.getBedomning().setKanInteTaStallning(false);
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case UPPFYLLER_KRAV_FOR_BEHORIGHET_DELSVAR_ID_33:
                    KorkortsbehorighetKod korkortsbehorighetKod = KorkortsbehorighetKod.fromCode(getCVSvarContent(delsvar).getCode());
                    if (korkortsbehorighetKod == KorkortsbehorighetKod.KANINTETEASTALLNING) {
                        utlatande.getBedomning().setKanInteTaStallning(true);
                    } else {
                        Korkortsbehorighet korkortsbehorighet = Korkortsbehorighet.fromValue(korkortsbehorighetKod.name());
                        BedomningKorkortstyp bedomningKorkortstyp = BedomningKorkortstyp.valueOf(korkortsbehorighet.value());
                        utlatande.getBedomning().getKorkortstyp().add(bedomningKorkortstyp);
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBorUndersokasAvSpecialistlakare(TsBasUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case BOR_UNDERSOKAS_AV_SPECIALISTLAKARE_DELSVAR_ID_34:
                    utlatande.getBedomning().setLakareSpecialKompetens(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

}
