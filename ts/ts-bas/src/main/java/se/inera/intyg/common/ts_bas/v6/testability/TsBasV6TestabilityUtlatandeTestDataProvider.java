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

package se.inera.intyg.common.ts_bas.v6.testability;

import static se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question.QuestionDiabetesTyp.DIABETES_TYP_2;
import static se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question.QuestionIdentitet.ID_KORT;

import java.util.EnumSet;
import se.inera.intyg.common.support.facade.util.TestabilityUtlatandeTestDataProvider;
import se.inera.intyg.common.ts_bas.v6.model.internal.Bedomning;
import se.inera.intyg.common.ts_bas.v6.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_bas.v6.model.internal.Diabetes;
import se.inera.intyg.common.ts_bas.v6.model.internal.Funktionsnedsattning;
import se.inera.intyg.common.ts_bas.v6.model.internal.HjartKarl;
import se.inera.intyg.common.ts_bas.v6.model.internal.HorselBalans;
import se.inera.intyg.common.ts_bas.v6.model.internal.IntygAvser;
import se.inera.intyg.common.ts_bas.v6.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_bas.v6.model.internal.Kognitivt;
import se.inera.intyg.common.ts_bas.v6.model.internal.Medicinering;
import se.inera.intyg.common.ts_bas.v6.model.internal.Medvetandestorning;
import se.inera.intyg.common.ts_bas.v6.model.internal.NarkotikaLakemedel;
import se.inera.intyg.common.ts_bas.v6.model.internal.Neurologi;
import se.inera.intyg.common.ts_bas.v6.model.internal.Njurar;
import se.inera.intyg.common.ts_bas.v6.model.internal.Psykiskt;
import se.inera.intyg.common.ts_bas.v6.model.internal.Sjukhusvard;
import se.inera.intyg.common.ts_bas.v6.model.internal.SomnVakenhet;
import se.inera.intyg.common.ts_bas.v6.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v6.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;
import se.inera.intyg.common.ts_bas.v6.model.internal.Utvecklingsstorning;
import se.inera.intyg.common.ts_bas.v6.model.internal.Vardkontakt;

public class TsBasV6TestabilityUtlatandeTestDataProvider implements TestabilityUtlatandeTestDataProvider<TsBasUtlatandeV6> {

    @Override
    public TsBasUtlatandeV6 getMinimumValues(TsBasUtlatandeV6 utlatande) {
        return utlatande.toBuilder()
            .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvserKategori.C1)))
            .setVardkontakt(Vardkontakt.create(null, ID_KORT))
            .setSyn(
                Syn.builder()
                    .setSynfaltsdefekter(false)
                    .setNattblindhet(false)
                    .setProgressivOgonsjukdom(false)
                    .setDiplopi(false)
                    .setNystagmus(false)
                    .setHogerOga(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(2.0)
                            .build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(2.0)
                            .build()
                    )
                    .setBinokulart(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(2.0)
                            .build()
                    )
                    .build()
            )
            .setHorselBalans(
                HorselBalans.builder()
                    .setBalansrubbningar(false)
                    .build()
            )
            .setFunktionsnedsattning(
                Funktionsnedsattning.builder()
                    .setFunktionsnedsattning(false)
                    .build()
            )
            .setHjartKarl(
                HjartKarl.builder()
                    .setHjartKarlSjukdom(false)
                    .setHjarnskadaEfterTrauma(false)
                    .setRiskfaktorerStroke(false)
                    .build()
            )
            .setDiabetes(
                Diabetes.builder()
                    .setHarDiabetes(false)
                    .build()
            )
            .setNeurologi(
                Neurologi.create(false)
            )
            .setMedvetandestorning(
                Medvetandestorning.builder()
                    .setMedvetandestorning(false)
                    .build()
            )
            .setNjurar(
                Njurar.create(false)
            )
            .setKognitivt(
                Kognitivt.create(false)
            )
            .setSomnVakenhet(
                SomnVakenhet.create(false)
            )
            .setNarkotikaLakemedel(
                NarkotikaLakemedel.builder()
                    .setTeckenMissbruk(false)
                    .setForemalForVardinsats(false)
                    .setLakarordineratLakemedelsbruk(false)
                    .build()
            )
            .setPsykiskt(
                Psykiskt.create(false)
            )
            .setUtvecklingsstorning(
                Utvecklingsstorning.builder()
                    .setPsykiskUtvecklingsstorning(false)
                    .setHarSyndrom(false)
                    .build()
            )
            .setSjukhusvard(
                Sjukhusvard.builder()
                    .setSjukhusEllerLakarkontakt(false)
                    .build()
            )
            .setMedicinering(
                Medicinering.builder()
                    .setStadigvarandeMedicinering(false)
                    .build()
            )
            .setBedomning(
                Bedomning.builder()
                    .setKorkortstyp(EnumSet.of(BedomningKorkortstyp.KAN_INTE_TA_STALLNING))
                    .build()
            )
            .build();
    }

    @Override
    public TsBasUtlatandeV6 getMaximumValues(TsBasUtlatandeV6 utlatande) {
        return utlatande.toBuilder()
            .setIntygAvser(
                IntygAvser.create(
                    EnumSet.of(
                        IntygAvserKategori.C1,
                        IntygAvserKategori.C1E,
                        IntygAvserKategori.C,
                        IntygAvserKategori.CE,
                        IntygAvserKategori.D1,
                        IntygAvserKategori.D1E,
                        IntygAvserKategori.D,
                        IntygAvserKategori.DE,
                        IntygAvserKategori.TAXI,
                        IntygAvserKategori.ANNAT
                    )
                )
            )
            .setVardkontakt(Vardkontakt.create(null, ID_KORT))
            .setSyn(
                Syn.builder()
                    .setSynfaltsdefekter(true)
                    .setNattblindhet(true)
                    .setProgressivOgonsjukdom(true)
                    .setDiplopi(true)
                    .setNystagmus(true)
                    .setHogerOga(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(0.1)
                            .setMedKorrektion(2.0)
                            .setKontaktlins(true)
                            .build()
                    )
                    .setVansterOga(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(0.1)
                            .setMedKorrektion(2.0)
                            .setKontaktlins(true)
                            .build()
                    )
                    .setBinokulart(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(0.1)
                            .setMedKorrektion(2.0)
                            .build()
                    )
                    .setKorrektionsglasensStyrka(true)
                    .build()
            )
            .setHorselBalans(
                HorselBalans.builder()
                    .setBalansrubbningar(true)
                    .setSvartUppfattaSamtal4Meter(true)
                    .build()
            )
            .setFunktionsnedsattning(
                Funktionsnedsattning.builder()
                    .setFunktionsnedsattning(true)
                    .setBeskrivning("En funktionsnedsättning")
                    .setOtillrackligRorelseformaga(true)
                    .build()
            )
            .setHjartKarl(
                HjartKarl.builder()
                    .setHjartKarlSjukdom(true)
                    .setHjarnskadaEfterTrauma(true)
                    .setRiskfaktorerStroke(true)
                    .setBeskrivningRiskfaktorer("En sjukdom")
                    .build()
            )
            .setDiabetes(
                Diabetes.builder()
                    .setHarDiabetes(true)
                    .setDiabetesTyp(DIABETES_TYP_2)
                    .setKost(true)
                    .setTabletter(true)
                    .setInsulin(true)
                    .build()
            )
            .setNeurologi(
                Neurologi.create(true)
            )
            .setMedvetandestorning(
                Medvetandestorning.builder()
                    .setMedvetandestorning(true)
                    .setBeskrivning("Annan medvetenandestörning")
                    .build()
            )
            .setNjurar(
                Njurar.create(true)
            )
            .setKognitivt(
                Kognitivt.create(true)
            )
            .setSomnVakenhet(
                SomnVakenhet.create(true)
            )
            .setNarkotikaLakemedel(
                NarkotikaLakemedel.builder()
                    .setTeckenMissbruk(true)
                    .setForemalForVardinsats(true)
                    .setProvtagningBehovs(true)
                    .setLakarordineratLakemedelsbruk(true)
                    .setLakemedelOchDos("Läkemedel och dos")
                    .build()
            )
            .setPsykiskt(
                Psykiskt.create(true)
            )
            .setUtvecklingsstorning(
                Utvecklingsstorning.builder()
                    .setPsykiskUtvecklingsstorning(true)
                    .setHarSyndrom(true)
                    .build()
            )
            .setSjukhusvard(
                Sjukhusvard.builder()
                    .setSjukhusEllerLakarkontakt(true)
                    .setTidpunkt("När")
                    .setVardinrattning("Var")
                    .setAnledning("Vad")
                    .build()
            )
            .setMedicinering(
                Medicinering.builder()
                    .setStadigvarandeMedicinering(true)
                    .setBeskrivning("Mediciner")
                    .build()
            )
            .setBedomning(
                Bedomning.builder()
                    .setKorkortstyp(
                        EnumSet.of(
                            BedomningKorkortstyp.C1,
                            BedomningKorkortstyp.C1E,
                            BedomningKorkortstyp.C,
                            BedomningKorkortstyp.CE,
                            BedomningKorkortstyp.D1,
                            BedomningKorkortstyp.D1E,
                            BedomningKorkortstyp.D,
                            BedomningKorkortstyp.DE,
                            BedomningKorkortstyp.TAXI,
                            BedomningKorkortstyp.ANNAT
                        )
                    )
                    .build()
            )
            .build();
    }
}
