/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.util.integration.schema.adapter;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.format.ISODateTimeFormat;

import static org.joda.time.DateTimeFieldType.dayOfMonth;
import static org.joda.time.DateTimeFieldType.monthOfYear;
import static org.joda.time.DateTimeFieldType.year;
import static org.joda.time.format.ISODateTimeFormat.yearMonth;
import static org.joda.time.format.ISODateTimeFormat.yearMonthDay;

/**
 * @author andreaskaltenbach
 */
public final class PartialAdapter {

    public static final int YEAR_LENGTH = 4;
    public static final int YEAR_MONTH_LENGTH = 7;

    private PartialAdapter() {
    }

    /**
     * Converts an intyg:common-model:1:partialDate to a Joda Partial.
     */
    public static Partial parsePartial(String dateString) {
        if (dateString == null) {
            return null;
        }

        LocalDate localDate = new LocalDate(dateString);

        switch (dateString.length()) {
            case YEAR_LENGTH: // only year provided
                return new Partial(year(), localDate.get(year()));
            case YEAR_MONTH_LENGTH: // year and month provided
                return new Partial(new DateTimeFieldType[]{year(), monthOfYear()},
                        new int[]{localDate.get(year()), localDate.get(monthOfYear())});
            default:
                return new Partial(new DateTimeFieldType[]{year(), monthOfYear(), dayOfMonth()},
                        new int[]{localDate.get(year()), localDate.get(monthOfYear()), localDate.get(dayOfMonth())});
        }
    }

    /**
     * Converts a Joda Partial to an intyg:common-model:1:partialDate.
     */
    public static String printPartial(Partial partial) {

        if (partial == null) {
            return null;
        }

        if (!partial.isSupported(dayOfMonth()) && !partial.isSupported(monthOfYear())) {
            return partial.toString(ISODateTimeFormat.year());
        }

        if (!partial.isSupported(dayOfMonth())) {
            return partial.toString(yearMonth());
        }

        return partial.toString(yearMonthDay());
    }
}
