/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

/**
 * Created by eriklupander on 2016-11-24.
 */
public class ValidatorUtilTest {

    @Test
    public void testDateValidationForValidDate() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDate(new InternalDate("2016-01-02"), errors, "testcategory", "testfield", null);
        assertTrue(valid);
        assertEquals(0, errors.size());
    }

    @Test
    public void testDateValidationForInvalidDateCorrectFormat() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDate(new InternalDate("2016-02-30"), errors, "testcategory", "testfield", null);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals("common.validation.date_invalid", errors.get(0).getMessage());
    }

    @Test
    public void testDateValidationForInvalidDate() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDate(new InternalDate("notADate"), errors, "testcategory", "testfield", null);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertNull(errors.get(0).getMessage());
    }

    @Test
    public void testDateValidationForInvalidDateWithMessage() {
        List<ValidationMessage> errors = new ArrayList<>();
        final String message = "message";
        boolean valid = ValidatorUtil.validateDate(new InternalDate("notADate"), errors, "testcategory", "testfield", message);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals(message, errors.get(0).getMessage());
    }

    @Test
    public void testDateValidationForEmptyDate() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil.validateDate(null, errors, "testcategory", "testfield", null);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals("testcategory", "testfield", errors.get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, errors.get(0).getType());
    }

    @Test
    public void testDateValidationForDateWhenBefore1900() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil
            .validateDateAndCheckIfFuture(new InternalDate(LocalDate.now().withYear(1850)), errors, "testcategory", "testfield",
                "futureError");
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals("common.validation.date_out_of_range", errors.get(0).getMessage());
    }

    @Test
    public void testDateValidationForDateWhenOneHundredYearsInTheFuture() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil
            .validateDateAndCheckIfFuture(new InternalDate(LocalDate.now().plusYears(100L)), errors, "testcategory", "testfield",
                "futureError");
        assertFalse(valid);
        assertEquals(2, errors.size());
    }

    @Test
    public void testDateValidationForDateOnDayInTheFuture() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil
            .validateDateAndCheckIfFuture(new InternalDate(LocalDate.now().plusDays(1)), errors, "testcategory", "testfield",
                "futureError");
        assertTrue(valid);
        assertEquals(1, errors.size());
        assertEquals("futureError", errors.get(0).getMessage());
    }

    @Test
    public void testGarbageDateDoesNotReturnFutureMessage() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil
            .validateDateAndCheckIfFuture(new InternalDate("notADate"), errors, "testcategory", "testfield", "futureError");
        assertFalse(valid);
        assertEquals(1, errors.size());
    }

    @Test
    public void testTjansgoringsTidHeltalIsNotInvalid() {
        boolean result = ValidatorUtil.isInvalidTjanstgoringstid("40");
        assertFalse(result);
    }

    @Test
    public void testTjansgoringsTidDecimaltalWithDotIsNotInvalid() {
        boolean result = ValidatorUtil.isInvalidTjanstgoringstid("37.5");
        assertFalse(result);
    }

    @Test
    public void testTjansgoringsTidDecimaltalWithCommaIsInvalid() {
        boolean result = ValidatorUtil.isInvalidTjanstgoringstid("37,5");
        assertTrue(result);
    }

    @Test
    public void testTjansgoringsTidNonNumberIsInvalid() {
        boolean result = ValidatorUtil.isInvalidTjanstgoringstid("trettiosjukommafem");
        assertTrue(result);
    }

    @Test
    public void testValidateInternalDateIntervalInvalidDate() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil
            .validateInternalDateInterval(new InternalLocalDateInterval("2016-02-30", "2016-03-01"), errors, "testcategory", "testfield",
                null);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals("common.validation.date_invalid", errors.get(0).getMessage());

        errors = new ArrayList<>();
        valid = ValidatorUtil
            .validateInternalDateInterval(new InternalLocalDateInterval("2016-01-10", "2016-02-30"), errors, "testcategory", "testfield",
                null);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals("common.validation.date_invalid", errors.get(0).getMessage());
    }

    @Test
    public void testValidateInternalDateIntervalInvalidFormat() {
        List<ValidationMessage> errors = new ArrayList<>();
        boolean valid = ValidatorUtil
            .validateInternalDateInterval(new InternalLocalDateInterval("2099-02", "2016-03-01"), errors, "testcategory", "testfield",
                null);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertNull(errors.get(0).getMessage());

        errors = new ArrayList<>();
        valid = ValidatorUtil
            .validateInternalDateInterval(new InternalLocalDateInterval("2016-01-10", "2100-0"), errors, "testcategory", "testfield", null);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertNull(errors.get(0).getMessage());
    }

    @Test
    public void testValidateInternalDateIntervalInvalidFormatWithMessage() {
        List<ValidationMessage> errors = new ArrayList<>();
        final String message = "message";
        boolean valid = ValidatorUtil
            .validateInternalDateInterval(new InternalLocalDateInterval("2099-02", "2016-03-01"), errors, "testcategory", "testfield",
                message);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals(message, errors.get(0).getMessage());

        errors = new ArrayList<>();
        valid = ValidatorUtil
            .validateInternalDateInterval(new InternalLocalDateInterval("2016-01-10", "2100-0"), errors, "testcategory", "testfield",
                message);
        assertFalse(valid);
        assertEquals(1, errors.size());
        assertEquals(message, errors.get(0).getMessage());
    }

}
