/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class InternalLocalDateIntervalTest {

  @Test
  void testInternaDatesAreValid() {
    InternalDate fromString = new InternalDate("2011-01-01");
    LocalDate fromDate = LocalDate.parse("2011-01-01");
    InternalDate tomString = new InternalDate("2011-01-02");
    LocalDate tomDate = LocalDate.parse("2011-01-02");

    InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
    assertEquals(
        interval.fromAsLocalDate(), fromDate, "Constructed from date did not match expected");
    assertEquals(interval.tomAsLocalDate(), tomDate, "Constructed tom date did not match expected");
  }

  @Test
  void testInternaDatesAreInvalid() {
    InternalDate fromDate = new InternalDate("2011-01-01");
    InternalDate tomDate = new InternalDate("2011-01");

    InternalLocalDateInterval interval = new InternalLocalDateInterval(fromDate, tomDate);
    interval.fromAsLocalDate();
    assertNull(interval.tomAsLocalDate());
  }

  @Test
  void testInternaDatesAreWeirdlyInvalid() {
    InternalDate fromDate = new InternalDate("");
    InternalDate tomDate = new InternalDate();

    InternalLocalDateInterval interval = new InternalLocalDateInterval(fromDate, tomDate);
    interval.fromAsLocalDate();
    assertNull(interval.tomAsLocalDate());
  }

  @Test
  void testInternalDateIntervalIsValid() {
    InternalDate fromString = new InternalDate("2011-01-01");
    InternalDate tomString = new InternalDate("2011-01-02");

    InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
    assertTrue(interval.isValid());
  }

  @Test
  void testSingleDayIntervalIsValid() {
    InternalDate fromString = new InternalDate("2011-01-01");
    InternalDate tomString = fromString;

    InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
    assertTrue(interval.isValid());
  }

  @Test
  void testInternalDateIntervalIsInvalid() {
    InternalDate fromString = new InternalDate("2011-02-01");
    InternalDate tomString = new InternalDate("2011-01-02");

    InternalLocalDateInterval interval = new InternalLocalDateInterval(fromString, tomString);
    assertFalse(interval.isValid());
  }

  @Test
  void testEquals() {
    InternalDate fromString = new InternalDate("2011-02-01");
    InternalDate tomString = new InternalDate("2011-03-02");

    InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString, tomString);
    InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString, tomString);
    assertEquals(interval1, interval2);
  }

  @Test
  void testEqualsWithNullInternal() {
    InternalDate fromString = null;
    InternalDate tomString = null;

    InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString, tomString);
    InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString, tomString);
    assertEquals(interval1, interval2);
  }

  @Test
  void testEqualsWithFromAsNull() {
    InternalDate fromString = null;
    InternalDate tomString = new InternalDate("2011-03-02");

    InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString, tomString);
    InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString, tomString);
    assertEquals(interval1, interval2);
  }

  @Test
  void testEqualsWithTomAsNull() {
    InternalDate fromString = new InternalDate("2011-02-01");
    InternalDate tomString = null;

    InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString, tomString);
    InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString, tomString);
    assertEquals(interval1, interval2);
  }

  @Test
  void testNotEquals() {
    InternalDate fromString1 = new InternalDate("2011-02-01");
    InternalDate tomString1 = new InternalDate("2011-03-02");

    InternalDate fromString2 = new InternalDate("2012-02-01");
    InternalDate tomString2 = new InternalDate("2012-03-02");

    InternalLocalDateInterval interval1 = new InternalLocalDateInterval(fromString1, tomString1);
    InternalLocalDateInterval interval2 = new InternalLocalDateInterval(fromString2, tomString2);
    assertNotEquals(interval1, interval2);
  }

  @Test
  void testOverlaps() {

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
