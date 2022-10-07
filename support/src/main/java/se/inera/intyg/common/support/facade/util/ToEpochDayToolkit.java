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

package se.inera.intyg.common.support.facade.util;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.appendAttribute;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.lessThanOrEqual;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.moreThan;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.time.LocalDate;
import se.inera.intyg.schemas.contract.Personnummer;

public final class ToEpochDayToolkit {

    public static String getLessThanOrEqual(Personnummer personId, String jsonId, int daysToAdd) {
        return
            lessThanOrEqual(
                singleExpression(
                    appendAttribute(jsonId, "toEpochDay")
                ),
                getDaysFromEpoch(personId, daysToAdd)
            );
    }

    public static String getMoreThan(Personnummer personId, String jsonId, int daysToAdd) {
        return
            moreThan(
                singleExpression(
                    appendAttribute(jsonId, "toEpochDay")
                ),
                getDaysFromEpoch(personId, daysToAdd)
            );
    }

    private static String getDaysFromEpoch(Personnummer personId, int daysToAdd) {
        return String.valueOf(
            LocalDate.of(
                Integer.parseInt(personId.getOriginalPnr().substring(0, 4)),
                Integer.parseInt(personId.getOriginalPnr().substring(4, 6)),
                Integer.parseInt(personId.getOriginalPnr().substring(6, 8))
            ).plusDays(daysToAdd));
    }
}
