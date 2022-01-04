/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v2.validator.transport;

import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class ValidationContext {

    private final TSDiabetesIntyg utlatande;

    public ValidationContext(TSDiabetesIntyg utlatande) {
        this.utlatande = utlatande;
    }

    public boolean isHogreContext() {
        for (KorkortsbehorighetTsDiabetes intygAvser : utlatande.getIntygAvser().getKorkortstyp()) {
            if (intygAvser != null && IntygAvserKod.isHogreKorkortsbehorighet(IntygAvserKod.valueOf(intygAvser.value().value()))) {
                return true;
            }
        }
        return false;
    }
}
