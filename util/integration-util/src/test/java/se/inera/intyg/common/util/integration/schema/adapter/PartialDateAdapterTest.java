/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.Temporal;
import org.junit.Test;

/**
 * @author andreaskaltenbach
 */
public class PartialDateAdapterTest {

    private static final String YEAR = "2013";
    private static final String YEAR_MONTH = "2013-12";
    private static final String YEAR_MONTH_DAY = "2013-12-24";

    private static final Year YEAR_PARTIAL = Year.of(2013);
    private static final YearMonth YEAR_MONTH_PARTIAL = YearMonth.of(2013, 12);
    private static final LocalDate YEAR_MONTH_DAY_PARTIAL = LocalDate.of(2013, 12, 24);

    @Test
    public void testParseYear() {
        Temporal partial = PartialDateAdapter.parsePartialDate(YEAR);
        assertEquals(YEAR_PARTIAL, partial);
    }

    @Test
    public void testParseYearMonth() {
        Temporal partial = PartialDateAdapter.parsePartialDate(YEAR_MONTH);
        assertEquals(YEAR_MONTH_PARTIAL, partial);
    }

    @Test
    public void testParseYearMonthDay() {
        Temporal partial = PartialDateAdapter.parsePartialDate(YEAR_MONTH_DAY);
        assertEquals(YEAR_MONTH_DAY_PARTIAL, partial);
    }

    @Test
    public void testPrintYear() {
        String date = PartialDateAdapter.printPartialDate(YEAR_PARTIAL);
        assertEquals(YEAR, date);
    }

    @Test
    public void testPrintYearMonth() {
        String date = PartialDateAdapter.printPartialDate(YEAR_MONTH_PARTIAL);
        assertEquals(YEAR_MONTH, date);
    }

    @Test
    public void testPrintYearMonthDay() {
        String date = PartialDateAdapter.printPartialDate(YEAR_MONTH_DAY_PARTIAL);
        assertEquals(YEAR_MONTH_DAY, date);
    }
}
