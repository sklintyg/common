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
import se.inera.intyg.common.support.facade.util.TestabilityTestDataDecorator;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Vardkontakt;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;

public class TSTRK1031TestabilityTestDataDecorator implements TestabilityTestDataDecorator<TsDiabetesUtlatandeV2> {

    @Override
    public void decorateWithMinimumValues(TsDiabetesUtlatandeV2 utlatande) {
        utlatande.getIntygAvser().getKorkortstyp().add(IntygAvserKategori.C1E);
        utlatande.setVardkontakt(new Vardkontakt());
        utlatande.getVardkontakt().setIdkontroll(IdKontrollKod.KORKORT.name());
    }

    @Override
    public void decorateWithMaximumValues(TsDiabetesUtlatandeV2 utlatande) {
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
    }
}
