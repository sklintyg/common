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
package se.inera.intyg.common.ts_diabetes.v2.model.converter;

import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Syn;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Vardkontakt;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.ts_parent.model.converter.InternalToTransportUtil;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.BedomningTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.Diabetes;
import se.inera.intygstjanster.ts.services.v1.Hypoglykemier;
import se.inera.intygstjanster.ts.services.v1.IdentifieringsVarden;
import se.inera.intygstjanster.ts.services.v1.IdentitetStyrkt;
import se.inera.intygstjanster.ts.services.v1.IntygsAvserTypDiabetes;
import se.inera.intygstjanster.ts.services.v1.Korkortsbehorighet;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.SynfunktionDiabetes;
import se.inera.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.inera.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

import static se.inera.intyg.common.ts_parent.model.converter.InternalToTransportUtil.DELIMITER_REGEXP;

public final class InternalToTransportConverter {

    private static final String DEFAULT_UTGAVA = "06";
    private static final String DEFAULT_VERSION = "02";

    private InternalToTransportConverter() {
    }

    public static RegisterTSDiabetesType convert(TsDiabetesUtlatandeV2 utlatande) {
        RegisterTSDiabetesType registerTsDiabetes = new RegisterTSDiabetesType();
        TSDiabetesIntyg result = new TSDiabetesIntyg();

        result.setBedomning(readBedomning(utlatande.getBedomning()));
        result.setDiabetes(readDiabetes(utlatande.getDiabetes()));
        result.setGrundData(InternalToTransportUtil.buildGrundData(utlatande.getGrundData()));
        result.setHypoglykemier(readHypoglykemier(utlatande.getHypoglykemier()));
        result.setIdentitetStyrkt(readIdentitetStyrkt(utlatande.getVardkontakt()));
        result.setIntygAvser(readIntygAvser(utlatande.getIntygAvser()));
        result.setIntygsId(utlatande.getId());
        result.setIntygsTyp(TsDiabetesEntryPoint.MODULE_ID);
        result.setSeparatOgonLakarintygKommerSkickas(utlatande.getSyn().getSeparatOgonlakarintyg());
        result.setOvrigKommentar(utlatande.getKommentar());
        if (utlatande.getSyn().getSeparatOgonlakarintyg() != null && !utlatande.getSyn().getSeparatOgonlakarintyg()) {
            result.setSynfunktion(readSynfunktionDiabetes(utlatande.getSyn()));
        }

        if (!Strings.nullToEmpty(utlatande.getTextVersion()).trim().isEmpty()) {
            List<String> versionInfo = Splitter.onPattern(DELIMITER_REGEXP).splitToList(utlatande.getTextVersion());
            result.setUtgava(String.format("%02d", Integer.parseInt(versionInfo.get(1))));
            result.setVersion(String.format("%02d", Integer.parseInt(versionInfo.get(0))));
        } else {
            result.setUtgava(DEFAULT_UTGAVA);
            result.setVersion(DEFAULT_VERSION);
        }
        registerTsDiabetes.setIntyg(result);
        return registerTsDiabetes;
    }

    private static IdentitetStyrkt readIdentitetStyrkt(Vardkontakt vardkontakt) {
        IdentitetStyrkt result = new IdentitetStyrkt();
        result.setIdkontroll(IdentifieringsVarden.fromValue(IdKontrollKod.valueOf(vardkontakt.getIdkontroll()).getCode()));
        return result;
    }

    private static SynfunktionDiabetes readSynfunktionDiabetes(Syn syn) {
        SynfunktionDiabetes result = new SynfunktionDiabetes();
        result.setHarDiplopi(syn.getDiplopi() != null && syn.getDiplopi());
        result.setSynskarpaMedKorrektion(readMedKorrektion(syn));
        result.setSynskarpaUtanKorrektion(readUtanKorrektion(syn));
        result.setFinnsSynfaltsprovning(syn.getSynfaltsprovning() != null && syn.getSynfaltsprovning());
        result.setSynfaltsprovningUtanAnmarkning(
            syn.getSynfaltsprovningUtanAnmarkning() != null && syn.getSynfaltsprovningUtanAnmarkning());
        result.setFinnsProvningOgatsRorlighet(syn.getProvningOgatsRorlighet() != null && syn.getProvningOgatsRorlighet());
        return result;
    }

    private static SynskarpaUtanKorrektion readUtanKorrektion(Syn syn) {
        SynskarpaUtanKorrektion result = new SynskarpaUtanKorrektion();
        if (syn.getBinokulart() != null && syn.getBinokulart().getUtanKorrektion() != null) {
            result.setBinokulart(syn.getBinokulart().getUtanKorrektion());
        }
        if (syn.getHoger() != null && syn.getHoger().getUtanKorrektion() != null) {
            result.setHogerOga(syn.getHoger().getUtanKorrektion());
        }
        if (syn.getVanster() != null && syn.getVanster().getUtanKorrektion() != null) {
            result.setVansterOga(syn.getVanster().getUtanKorrektion());
        }
        return result;
    }

    private static SynskarpaMedKorrektion readMedKorrektion(Syn syn) {
        SynskarpaMedKorrektion result = new SynskarpaMedKorrektion();
        result.setBinokulart(syn.getBinokulart() != null ? syn.getBinokulart().getMedKorrektion() : null);
        result.setHogerOga(syn.getHoger() != null ? syn.getHoger().getMedKorrektion() : null);
        result.setVansterOga(syn.getVanster() != null ? syn.getVanster().getMedKorrektion() : null);
        return result;
    }

    private static IntygsAvserTypDiabetes readIntygAvser(IntygAvser intygAvser) {
        IntygsAvserTypDiabetes result = new IntygsAvserTypDiabetes();

        for (IntygAvserKategori kat : intygAvser.getKorkortstyp()) {
            KorkortsbehorighetTsDiabetes bh = KorkortsbehorighetTsDiabetes.fromValue(Korkortsbehorighet.fromValue(kat.name()));
            result.getKorkortstyp().add(bh);
        }

        return result;
    }

    private static Hypoglykemier readHypoglykemier(se.inera.intyg.common.ts_diabetes.v2.model.internal.Hypoglykemier hypoglykemier) {
        Hypoglykemier result = new Hypoglykemier();
        result.setHarKunskapOmAtgarder(hypoglykemier.getKunskapOmAtgarder() != null && hypoglykemier.getKunskapOmAtgarder());
        result.setHarTeckenNedsattHjarnfunktion(hypoglykemier.getTeckenNedsattHjarnfunktion() != null
            && hypoglykemier.getTeckenNedsattHjarnfunktion());

        if (hypoglykemier.getSaknarFormagaKannaVarningstecken() != null) {
            result.setSaknarFormagaKannaVarningstecken(hypoglykemier.getSaknarFormagaKannaVarningstecken());
        }

        if (hypoglykemier.getAllvarligForekomst() != null) {
            result.setHarAllvarligForekomst(hypoglykemier.getAllvarligForekomst());
            result.setAllvarligForekomstBeskrivning(hypoglykemier.getAllvarligForekomstBeskrivning());
        }

        if (hypoglykemier.getAllvarligForekomstTrafiken() != null) {
            result.setHarAllvarligForekomstTrafiken(hypoglykemier.getAllvarligForekomstTrafiken());
            result.setAllvarligForekomstTrafikBeskrivning(hypoglykemier.getAllvarligForekomstTrafikBeskrivning());
        }

        if (hypoglykemier.getEgenkontrollBlodsocker() != null) {
            result.setGenomforEgenkontrollBlodsocker(hypoglykemier.getEgenkontrollBlodsocker());
        }

        if (hypoglykemier.getAllvarligForekomstVakenTid() != null) {
            result.setHarAllvarligForekomstVakenTid(hypoglykemier.getAllvarligForekomstVakenTid());
            result.setAllvarligForekomstVakenTidAr(hypoglykemier.getAllvarligForekomstVakenTidObservationstid() != null ? hypoglykemier
                .getAllvarligForekomstVakenTidObservationstid().getDate() : null);
        }
        return result;
    }

    private static Diabetes readDiabetes(se.inera.intyg.common.ts_diabetes.v2.model.internal.Diabetes diabetes) {
        Diabetes result = new Diabetes();
        result.setAnnanBehandlingBeskrivning(diabetes.getAnnanBehandlingBeskrivning());
        result.setDebutArDiabetes(diabetes.getObservationsperiod());
        result.setHarBehandlingInsulin(diabetes.getInsulin());
        result.setHarBehandlingKost(diabetes.getEndastKost());
        result.setHarBehandlingTabletter(diabetes.getTabletter());
        result.setInsulinBehandlingSedanAr(diabetes.getInsulinBehandlingsperiod());

        if (diabetes.getDiabetestyp() != null) {
            result.getDiabetesTyp().add(InternalToTransportUtil.convertDiabetesTyp(DiabetesKod.valueOf(diabetes.getDiabetestyp())));
        }
        return result;
    }

    private static BedomningTypDiabetes readBedomning(Bedomning bedomning) {
        BedomningTypDiabetes result = new BedomningTypDiabetes();
        result.setBehovAvLakareSpecialistKompetens(bedomning.getLakareSpecialKompetens());
        result.setKanInteTaStallning(bedomning.getKanInteTaStallning());
        if (bedomning.getLamplighetInnehaBehorighet() != null) {
            result.setLamplighetInnehaBehorighetSpecial(bedomning.getLamplighetInnehaBehorighet());
        }
        result.setOvrigKommentar(bedomning.getKommentarer());

        for (BedomningKorkortstyp typ : bedomning.getKorkortstyp()) {
            result.getKorkortstyp().add(KorkortsbehorighetTsDiabetes.fromValue(Korkortsbehorighet.fromValue(typ.name())));
        }

        return result;
    }
}
