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
import org.joda.time.Partial;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author andreaskaltenbach
 */
public class PartialAdapterTest {

    private static final String YEAR = "2013";
    private static final String YEAR_MONTH = "2013-12";
    private static final String YEAR_MONTH_DAY = "2013-12-24";

    private static final Partial YEAR_PARTIAL = new Partial(DateTimeFieldType.year(), 2013);
    private static final Partial YEAR_MONTH_PARTIAL = YEAR_PARTIAL.with(DateTimeFieldType.monthOfYear(), 12);
    private static final Partial YEAR_MONTH_DAY_PARTIAL = YEAR_MONTH_PARTIAL.with(DateTimeFieldType.dayOfMonth(), 24);

    @Test
    public void testParseYear() {
        Partial partial = se.inera.intyg.common.util.integration.schema.adapter.PartialAdapter.parsePartial(YEAR);
        assertEquals(YEAR_PARTIAL, partial);
    }

    @Test
    public void testParseYearMonth() {
        Partial partial = se.inera.intyg.common.util.integration.schema.adapter.PartialAdapter.parsePartial(YEAR_MONTH);
        assertEquals(YEAR_MONTH_PARTIAL, partial);
    }

    @Test
    public void testParseYearMonthDay() {
        Partial partial = se.inera.intyg.common.util.integration.schema.adapter.PartialAdapter.parsePartial(YEAR_MONTH_DAY);
        assertEquals(YEAR_MONTH_DAY_PARTIAL, partial);
    }

    @Test
    public void testPrintYear() {
        String date = se.inera.intyg.common.util.integration.schema.adapter.PartialAdapter.printPartial(YEAR_PARTIAL);
        assertEquals(YEAR, date);
    }

    @Test
    public void testPrintYearMonth() {
        String date = se.inera.intyg.common.util.integration.schema.adapter.PartialAdapter.printPartial(YEAR_MONTH_PARTIAL);
        assertEquals(YEAR_MONTH, date);
    }

    @Test
    public void testPrintYearMonthDay() {
        String date = se.inera.intyg.common.util.integration.schema.adapter.PartialAdapter.printPartial(YEAR_MONTH_DAY_PARTIAL);
        assertEquals(YEAR_MONTH_DAY, date);
    }
}
