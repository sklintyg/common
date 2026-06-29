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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class InternalDateTest {

  @Test
  void testInternaDateIsValid() {
    String dateString = "2011-01-01";

    InternalDate date1 = new InternalDate(dateString);
    assertTrue(date1.isValidDate());
  }

  @Test
  void testInternalDateIsNotValid() {
    String bogusString = "blahonga";
    String yearString = "2001";

    InternalDate date = new InternalDate(bogusString);
    assertFalse(date.isValidDate());

    // Allow only yyyy-MM-dd
    InternalDate date2 = new InternalDate(yearString);
    assertFalse(date2.isValidDate());
  }

  @Test
  void testAsLocalDate() {
    String valid = "2014-12-30";
    InternalDate internalDate = new InternalDate(valid);
    LocalDate localDate = internalDate.asLocalDate();

    assertEquals(
        valid,
        localDate.toString(),
        String.format("LocalDate %s did not match %s", localDate, valid));
  }

  @Test
  void testIsDateInFuture() {
    InternalDate validNotFuture = new InternalDate(LocalDate.now().minusMonths(6));
    InternalDate validOneYearInFuture = new InternalDate(LocalDate.now().plusYears(1));
    InternalDate validOneDayInFuture = new InternalDate(LocalDate.now().plusDays(1));
    InternalDate validMoreThenOneYearAgo =
        new InternalDate(LocalDate.now().minusYears(1).minusDays(1));
    InternalDate validSameDate = new InternalDate(LocalDate.now());

    InternalDate invalidDate = new InternalDate("2001-01");

    assertFalse(validNotFuture.beforeMinDateOrInFuture(LocalDate.now().minusYears(1)));
    assertFalse(validSameDate.beforeMinDateOrInFuture(LocalDate.now().minusYears(1)));

    // Should be invalid
    assertTrue(validOneYearInFuture.beforeMinDateOrInFuture(LocalDate.now().minusYears(1)));
    assertTrue(validOneDayInFuture.beforeMinDateOrInFuture(LocalDate.now().minusYears(1)));
    assertTrue(validMoreThenOneYearAgo.beforeMinDateOrInFuture(LocalDate.now().minusYears(1)));
    assertTrue(invalidDate.beforeMinDateOrInFuture(LocalDate.now().minusYears(1)));
  }

  @Test
  void testInvalidAsLocalDate() {
    InternalDate partialDate = new InternalDate("2001-");
    InternalDate justText = new InternalDate("blörk");
    InternalDate dateButIncorrect = new InternalDate("2011-13-32");
    assertThrows(ModelException.class, partialDate::asLocalDate);

    assertThrows(ModelException.class, justText::asLocalDate);

    assertThrows(ModelException.class, dateButIncorrect::asLocalDate);
  }

  @Test
  void testInvalidEmpty() {
    InternalDate empty = new InternalDate("");
    assertThrows(ModelException.class, empty::asLocalDate);
  }

  @Test
  void testVagueDateInFutureYear0000() {
    InternalDate date = new InternalDate("0000-00-00");
    assertFalse(date.vagueDateInFuture());
  }

  @Test
  void testVagueDateInFutureEarlierYearFalse() {
    int currentYear = LocalDate.now().getYear();

    InternalDate date = new InternalDate((currentYear - 1) + "-00-00");
    assertFalse(date.vagueDateInFuture());
  }

  @Test
  void testVagueDateInFutureSameYearFalse() {
    int currentYear = LocalDate.now().getYear();

    InternalDate date = new InternalDate(currentYear + "-00-00");
    assertFalse(date.vagueDateInFuture());
  }

  @Test
  void testVagueDateInFutureYearTrue() {
    int currentYear = LocalDate.now().getYear();

    InternalDate date = new InternalDate((currentYear + 1) + "-00-00");
    assertTrue(date.vagueDateInFuture());
  }

  @Test
  void testVagueDateInFutureMonthFalse() {
    int currentYear = LocalDate.now().getYear();
    int currentMonth = LocalDate.now().getMonthValue();

    InternalDate date =
        new InternalDate(currentYear + "-" + addLeadingZeros(currentMonth, 2) + "-00");
    assertFalse(date.vagueDateInFuture());
  }

  @Test
  void testVagueDateInFutureMonthTrue() {
    int currentYear = LocalDate.now().getYear();
    int currentMonth = LocalDate.now().getMonthValue();

    InternalDate date =
        new InternalDate(currentYear + "-" + addLeadingZeros(currentMonth + 1, 2) + "-00");
    assertTrue(date.vagueDateInFuture());
  }

  private String addLeadingZeros(int digits, int expectedNumberOfDigits) {
    StringBuilder number = new StringBuilder(String.valueOf(digits));

    int diff = expectedNumberOfDigits - number.length();

    if (diff > 0) {
      for (int i = 0; i < diff; i++) {
        number.insert(0, "0");
      }
    }

    return number.toString();
  }

  @Test
  void testIsBeforeBeginningOfLastYearTrue() {
    int year = LocalDate.now().getYear() - 2;
    InternalDate date = new InternalDate(year + "-12-31");
    assertTrue(date.isBeforeBeginningOfLastYear());
  }

  @Test
  void testIsBeforeBeginningOfLastYearFalse() {
    int year = LocalDate.now().getYear() - 1;
    InternalDate date = new InternalDate(year + "-01-01");
    assertFalse(date.isBeforeBeginningOfLastYear());
  }

  @Test
  void shouldReturnTrueIfDateIsBeforePatientBirthDate() {
    final var localDate = LocalDate.now().minusYears(2).minusDays(1);
    final var patientBirthDate = LocalDate.now().minusYears(2);
    InternalDate date = new InternalDate(localDate);
    assertTrue(date.isBefore(patientBirthDate));
  }

  @Test
  void shouldReturnFalseIfDateIsAfterPatientBirthDate() {
    final var localDate = LocalDate.now().minusYears(2).plusDays(1);
    final var patientBirthDate = LocalDate.now().minusYears(2);
    InternalDate date = new InternalDate(localDate);
    assertFalse(date.isBefore(patientBirthDate));
  }
}
