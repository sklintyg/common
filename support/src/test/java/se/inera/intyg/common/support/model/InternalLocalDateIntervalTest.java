/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import org.junit.Test;

public class InternalLocalDateIntervalTest {

    @Test
    public void testInternaDatesAreValid() {
        InternalDate fromString = new InternalDate("2011-01-01");
        LocalDate fromDate = LocalDate.parse("2011-01-01");
        InternalDate tomString = new InternalDate("2011-01-02");
        LocalDate tomDate = LocalDate.parse("2011-01-02");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertEquals("Constructed from date did not match expected", interval.fromAsLocalDate(), fromDate);
        assertEquals("Constructed tom date did not match expected", interval.tomAsLocalDate(), tomDate);
    }

    @Test
    public void testInternaDatesAreInvalid() {
        InternalDate fromDate = new InternalDate("2011-01-01");
        InternalDate tomDate = new InternalDate("2011-01");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromDate, tomDate);
        interval.fromAsLocalDate();
        assertEquals(interval.tomAsLocalDate(), null);
    }

    @Test
    public void testInternaDatesAreWeirdlyInvalid() {
        InternalDate fromDate = new InternalDate("");
        InternalDate tomDate = new InternalDate();

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromDate, tomDate);
        interval.fromAsLocalDate();
        assertEquals(interval.tomAsLocalDate(), null);
    }

    @Test
    public void testInternalDateIntervalIsValid() {
        InternalDate fromString = new InternalDate("2011-01-01");
        InternalDate tomString = new InternalDate("2011-01-02");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertTrue(interval.isValid());

    }

    @Test
    public void testSingleDayIntervalIsValid() {
        InternalDate fromString = new InternalDate("2011-01-01");
        InternalDate tomString = fromString;

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertTrue(interval.isValid());

    }

    @Test
    public void testInternalDateIntervalIsInvalid() {
        InternalDate fromString = new InternalDate("2011-02-01");
        InternalDate tomString = new InternalDate("2011-01-02");

        InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
        assertFalse(interval.isValid());

    }

    @Test
    public void testEquals() {
        InternalDate fromString = new InternalDate("2011-02-01");
        InternalDate tomString = new InternalDate("2011-03-02");

        InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString, tomString);
        InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString, tomString);
        assertTrue(interval1.equals(interval2));
    }

    @Test
    public void testNotEquals() {
        InternalDate fromString1 = new InternalDate("2011-02-01");
        InternalDate tomString1 = new InternalDate("2011-03-02");

        InternalDate fromString2 = new InternalDate("2012-02-01");
        InternalDate tomString2 = new InternalDate("2012-03-02");

        InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString1, tomString1);
        InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString2, tomString2);
        assertFalse(interval1.equals(interval2));
    }

    @Test
    public void testOverlaps() {

        // Check that interval overlaps itself
        InternalDate fromString = new InternalDate("2011-01-01");
        InternalDate tomString = new InternalDate("2011-01-02");

        InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString, tomString);
        InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString, tomString);
        assertTrue(interval1.overlaps(interval2));

        // Check that interval overlaps if end of first and start of second is the same
        InternalDate fromString2 = new InternalDate("2011-01-02");
        InternalDate tomString2 = new InternalDate("2011-01-03");
        InternalLocalDateInterval interval3 = new InternalLocalDateInterval(fromString2, tomString2);
        assertTrue(interval2.overlaps(interval3));

        // Check that interval overlaps if start of second is between first
        fromString = new InternalDate("2011-01-01");
        tomString = new InternalDate("2011-01-03");
        fromString2 = new InternalDate("2011-01-02");
        tomString2 = new InternalDate("2011-01-04");
        interval1 = new InternalLocalDateInterval(fromString, tomString);
        interval2 = new InternalLocalDateInterval(fromString2, tomString2);
        assertTrue(interval1.overlaps(interval2));

        // Check that overlaps does not assert overlap if there is no overlap
        fromString = new InternalDate("2011-01-01");
        tomString = new InternalDate("2011-01-02");
        fromString2 = new InternalDate("2011-01-03");
        tomString2 = new InternalDate("2011-01-04");
        interval1 = new InternalLocalDateInterval(fromString, tomString);
        interval2 = new InternalLocalDateInterval(fromString2, tomString2);
        assertFalse(interval1.overlaps(interval2));
    }
}
