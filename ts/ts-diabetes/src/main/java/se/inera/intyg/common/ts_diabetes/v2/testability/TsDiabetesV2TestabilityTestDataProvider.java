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

package se.inera.intyg.common.ts_diabetes.v2.testability;

import java.time.LocalDate;
import se.inera.intyg.common.support.facade.util.TestabilityUtlatandeTestDataProvider;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Vardkontakt;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;

public class TsDiabetesV2TestabilityTestDataProvider implements TestabilityUtlatandeTestDataProvider<TsDiabetesUtlatandeV2> {

    @Override
    public TsDiabetesUtlatandeV2 getMinimumValues(TsDiabetesUtlatandeV2 utlatande) {
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.C1E);
        utlatande.setVardkontakt(new Vardkontakt());
        utlatande.getVardkontakt().setIdkontroll(IdKontrollKod.KORKORT.name());
        utlatande.getDiabetes().setObservationsperiod(String.valueOf(LocalDate.now()));
        utlatande.getDiabetes().setDiabetestyp(DiabetesKod.DIABETES_TYP_1.name());
        utlatande.getDiabetes().setTabletter(true);

        utlatande.getHypoglykemier().setKunskapOmAtgarder(false);
        utlatande.getHypoglykemier().setTeckenNedsattHjarnfunktion(false);
        utlatande.getHypoglykemier().setSaknarFormagaKannaVarningstecken(false);
        utlatande.getHypoglykemier().setAllvarligForekomst(false);
        utlatande.getHypoglykemier().setAllvarligForekomstTrafiken(false);
        utlatande.getHypoglykemier().setEgenkontrollBlodsocker(false);
        utlatande.getHypoglykemier().setAllvarligForekomstVakenTid(false);

        utlatande.getSyn().setSeparatOgonlakarintyg(true);

        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.BE);
        utlatande.getBedomning().setKanInteTaStallning(false);
        utlatande.getBedomning().setLamplighetInnehaBehorighet(false);
        return utlatande;
    }

    @Override
    public TsDiabetesUtlatandeV2 getMaximumValues(TsDiabetesUtlatandeV2 utlatande) {
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.C1E);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.D1E);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.A);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.D1);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.D);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.C1);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.C);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.BE);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.B);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.A1);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.A2);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.AM);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.TAXI);
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.TRAKTOR);

        utlatande.setVardkontakt(new Vardkontakt());
        utlatande.getVardkontakt().setIdkontroll(IdKontrollKod.KORKORT.name());

        utlatande.getDiabetes().setObservationsperiod(String.valueOf(LocalDate.now()));
        utlatande.getDiabetes().setDiabetestyp(DiabetesKod.DIABETES_TYP_2.name());
        utlatande.getDiabetes().setInsulin(true);
        utlatande.getDiabetes().setEndastKost(true);
        utlatande.getDiabetes().setTabletter(true);
        utlatande.getDiabetes().setInsulinBehandlingsperiod("insulin behandling period");
        utlatande.getDiabetes().setAnnanBehandlingBeskrivning("annan behandling beskrivning");

        utlatande.getHypoglykemier().setKunskapOmAtgarder(true);
        utlatande.getHypoglykemier().setTeckenNedsattHjarnfunktion(true);
        utlatande.getHypoglykemier().setSaknarFormagaKannaVarningstecken(true);
        utlatande.getHypoglykemier().setAllvarligForekomst(true);
        utlatande.getHypoglykemier().setAllvarligForekomstBeskrivning("hur många sådana episoder?");
        utlatande.getHypoglykemier().setAllvarligForekomstTrafiken(true);
        utlatande.getHypoglykemier().setAllvarligForekomstTrafikBeskrivning("hur många sådana episoder och när inträffade de?");
        utlatande.getHypoglykemier().setEgenkontrollBlodsocker(true);
        utlatande.getHypoglykemier().setAllvarligForekomstVakenTid(true);
        utlatande.getHypoglykemier().setAllvarligForekomstVakenTidObservationstid(new InternalDate(LocalDate.now()));

        utlatande.getSyn().setSeparatOgonlakarintyg(false);
        utlatande.getSyn().setSynfaltsprovningUtanAnmarkning(true);
        final var synskarpaValue = 1.5;
        utlatande.getSyn().setHoger(synskarpaValue, synskarpaValue);
        utlatande.getSyn().setVanster(synskarpaValue, synskarpaValue);
        utlatande.getSyn().setBinokulart(synskarpaValue, synskarpaValue);
        utlatande.getSyn().setDiplopi(true);

        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.BE);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.B);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.A);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.AM);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.A1);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.A2);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.TAXI);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.TRAKTOR);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.C);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.C1E);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.C1);
        utlatande.getBedomning().getKorkortstyp().add(BedomningKorkortstyp.CE);

        utlatande.getBedomning().setKanInteTaStallning(true);
        utlatande.getBedomning()
            .setLakareSpecialKompetens("Patienten bör före ärendets avgörande undersökas av läkare med specialistkompetens i");
        utlatande.getBedomning().setKommentarer("En kommentar");
        utlatande.getBedomning().setLamplighetInnehaBehorighet(true);

        utlatande.setKommentarer("Övriga kommentarer");
        return utlatande;
    }
}
