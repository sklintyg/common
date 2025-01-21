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
package se.inera.intyg.common.support.facade.util;

import java.time.LocalDate;
import se.inera.intyg.schemas.contract.Personnummer;

public final class PatientToolkit {

    public static LocalDate birthDate(Personnummer personId) {
        if (personId == null) {
            throw new IllegalArgumentException("Cannot get birth date when personId is null!");
        }

        return LocalDate.of(
            Integer.parseInt(personId.getOriginalPnr().substring(0, 4)),
            Integer.parseInt(personId.getOriginalPnr().substring(4, 6)),
            getDayOfMonth(personId)
        );
    }

    private static int getDayOfMonth(Personnummer personId) {
        final var dayOfMonth = Integer.parseInt(personId.getOriginalPnr().substring(6, 8));
        if (dayOfMonth > 60) {
            return dayOfMonth - 60;
        }
        return dayOfMonth;
    }

    public static Integer birthYear(Personnummer personId) {
        if (personId == null) {
            throw new IllegalArgumentException("Cannot get birth year when personId is null!");
        }
        return Integer.parseInt(personId.getOriginalPnr().substring(0, 4));
    }
}
