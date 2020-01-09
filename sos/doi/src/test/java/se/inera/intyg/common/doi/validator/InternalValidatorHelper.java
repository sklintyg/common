/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.doi.validator;

import se.inera.intyg.common.support.model.InternalDate;

import java.time.LocalDate;

public class InternalValidatorHelper {

    public void setDateToCurrentYear(InternalDate date) {
        if (date != null) {
            modifyDateYear(date);
        }
    }

    public void setDateToLastYear(InternalDate date) {
        if (date != null) {
            modifyDateYear(date, -1);
        }
    }

    private void modifyDateYear(InternalDate date, int years) {
        date.setDate(String.valueOf(LocalDate.now().getYear() + years) + date.getDate().substring(4));
    }

    private void modifyDateYear(InternalDate date) {
        this.modifyDateYear(date, 0);
    }

}
