/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.model.internal;

import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getABTraktorBehorigheter;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getAllaBehorigheter;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getCEBehorigHeter;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getDBehorigHeter;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getKanintetastallning;
import static se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet.getTaxiBehorigheter;

import com.google.common.collect.ImmutableSet;

public enum KorkortBehorighetGrupp {
    ALLA(getAllaBehorigheter()),
    A_B_TRAKTOR(getABTraktorBehorigheter()),
    C_E(getCEBehorigHeter()),
    D(getDBehorigHeter()),
    TAXI(getTaxiBehorigheter()),
    KANINTETASTALLNING(getKanintetastallning());

    private final ImmutableSet<Korkortsbehorighet> korkortsbehorigheter;

    KorkortBehorighetGrupp(final ImmutableSet<Korkortsbehorighet> korkortsbehorigheter) {
        this.korkortsbehorigheter = korkortsbehorigheter;
    }

    public ImmutableSet<Korkortsbehorighet> getKorkortsbehorigheter() {
        return korkortsbehorigheter;
    }
}
