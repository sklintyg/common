/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.validate;

import org.junit.Test;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by eriklupander on 2016-11-24.
 */
public class ValidatorUtilTest {

    @Test
    public void testDateValidationForValidDate() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDate(new InternalDate("2016-01-02"), errors, "testfield");
        assertTrue(valid);
        assertEquals(0, errors.size());
    }

    @Test
    public void testDateValidationForEmptyDate() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDate(null, errors, "testfield");
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals("testfield", errors.get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, errors.get(0).getType());
    }

    @Test
    public void testDateValidationForDateWhenBefore1900() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDateAndWarnIfFuture(new InternalDate(LocalDate.now().withYear(1850)), errors, "testfield");
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals("common.validation.date_out_of_range", errors.get(0).getMessage());
    }

    @Test
    public void testDateValidationForDateWhenOneHundredYearsInTheFuture() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDateAndWarnIfFuture(new InternalDate(LocalDate.now().plusYears(100L)), errors, "testfield");
        assertFalse(valid);
        assertEquals(2, errors.size());
    }

    @Test
    public void testDateValidationForDateOnDayInTheFuture() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDateAndWarnIfFuture(new InternalDate(LocalDate.now().plusDays(1)), errors, "testfield");
        assertTrue(valid);
        assertEquals(1, errors.size());
        assertEquals("common.validation.future.datum", errors.get(0).getMessage());
    }

    @Test
    public void testGarbageDateDoesNotReturnFutureMessage() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDateAndWarnIfFuture(new InternalDate("notADate"), errors, "testfield");
        assertFalse(valid);
        assertEquals(1, errors.size());
    //    assertEquals(assertEquals("common.validation.future.datum", errors.get(0).getMessage());)
    }

}
