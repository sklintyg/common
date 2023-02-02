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

package se.inera.intyg.common.ts_diabetes.v3.testability;

import java.time.LocalDate;
import java.util.EnumSet;
import se.inera.intyg.common.support.facade.util.TestabilityUtlatandeTestDataProvider;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Hypoglykemier;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.IdKontroll;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synfunktion;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.inera.intyg.common.ts_diabetes.v3.model.kodverk.KvIdKontroll;
import se.inera.intyg.common.ts_diabetes.v3.model.kodverk.KvTypAvDiabetes;

public class TsDiabetesV3TestabilityTestDataProvider implements TestabilityUtlatandeTestDataProvider<TsDiabetesUtlatandeV3> {

    @Override
    public TsDiabetesUtlatandeV3 getMinimumValues(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande;
    }

    @Override
    public TsDiabetesUtlatandeV3 getMaximumValues(TsDiabetesUtlatandeV3 utlatande) {
        return utlatande.toBuilder()
            .setIntygAvser(
                IntygAvser.create(
                    EnumSet.of(
                        IntygAvserKategori.IAV1,
                        IntygAvserKategori.IAV2,
                        IntygAvserKategori.IAV3,
                        IntygAvserKategori.IAV4,
                        IntygAvserKategori.IAV5,
                        IntygAvserKategori.IAV6,
                        IntygAvserKategori.IAV7,
                        IntygAvserKategori.IAV8,
                        IntygAvserKategori.IAV9,
                        IntygAvserKategori.IAV11,
                        IntygAvserKategori.IAV12,
                        IntygAvserKategori.IAV13,
                        IntygAvserKategori.IAV14,
                        IntygAvserKategori.IAV15,
                        IntygAvserKategori.IAV16,
                        IntygAvserKategori.IAV17
                    )
                )
            )
            .setIdentitetStyrktGenom(
                IdKontroll.create(KvIdKontroll.PASS)
            )
            .setAllmant(
                Allmant.builder()
                    .setDiabetesDiagnosAr(String.valueOf(LocalDate.now()))
                    .setTypAvDiabetes(KvTypAvDiabetes.ANNAN)
                    .setBeskrivningAnnanTypAvDiabetes("Annan typ av diabetes")
                    .setBehandling(
                        Behandling.builder()
                            .setEndastKost(true)
                            .setInsulin(true)
                            .setInsulinSedanAr(String.valueOf(LocalDate.now()))
                            .setTabletter(true)
                            .setAnnanBehandling(true)
                            .setAnnanBehandlingBeskrivning("Annan behandling beskrivning")
                            .setRiskHypoglykemi(true)
                            .build()
                    )
                    .build()
            )
            .setHypoglykemier(
                Hypoglykemier.builder()
                    .setEgenkontrollBlodsocker(true)
                    .setNedsattHjarnfunktion(true)
                    .setSjukdomenUnderKontroll(true)
                    .setFormagaVarningstecken(true)
                    .setAterkommandeSenasteAret(true)
                    .setAterkommandeSenasteTidpunkt(new InternalDate(LocalDate.now()))
                    .setForekomstTrafik(true)
                    .setForekomstTrafikTidpunkt(new InternalDate(LocalDate.now()))
                    .setAterkommandeSenasteKvartalet(true)
                    .setSenasteTidpunktVaken(new InternalDate(LocalDate.now()))
                    .build()
            )
            .setSynfunktion(
                Synfunktion.builder()
                    .setBinokulart(
                        Synskarpevarden.builder()
                            .setMedKorrektion(1.0)
                            .setUtanKorrektion(1.0)
                            .build()
                    )
                    .setHoger(
                        Synskarpevarden.builder()
                            .setUtanKorrektion(1.0)
                            .setMedKorrektion(1.0)
                            .build()
                    )
                    .setVanster(
                        Synskarpevarden.builder()
                            .setMedKorrektion(1.0)
                            .setUtanKorrektion(1.0)
                            .build()
                    )
                    .setMisstankeOgonsjukdom(true)
                    .setSkickasSeparat(true)
                    .build()
            )
            .setOvrigt("Övrigt")
            .setBedomning(
                Bedomning.builder()
                    .setLampligtInnehav(true)
                    .setUppfyllerBehorighetskrav(
                        EnumSet.of(
                            BedomningKorkortstyp.VAR1,
                            BedomningKorkortstyp.VAR2,
                            BedomningKorkortstyp.VAR3,
                            BedomningKorkortstyp.VAR4,
                            BedomningKorkortstyp.VAR5,
                            BedomningKorkortstyp.VAR6,
                            BedomningKorkortstyp.VAR7,
                            BedomningKorkortstyp.VAR8,
                            BedomningKorkortstyp.VAR11,
                            BedomningKorkortstyp.VAR12,
                            BedomningKorkortstyp.VAR13,
                            BedomningKorkortstyp.VAR14
                        )
                    )
                    .setBorUndersokasBeskrivning("Undersökas beskrivning")
                    .build()
            )
            .build();
    }
}
