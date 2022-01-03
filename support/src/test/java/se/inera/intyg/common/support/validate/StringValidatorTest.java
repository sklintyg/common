/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

public class StringValidatorTest {

    @Test
    public void testValidateStringIsYear() {
        final String nineteenEleven = "1911";
        final String twentyEleven = "2011";
        final String thirtyEleven = "3011";

        Calendar now = Calendar.getInstance();
        int thisYear = now.get(Calendar.YEAR);
        final String validThisYear = Integer.toString(thisYear);
        final String validButFuture = Integer.toString(thisYear + 1);

        final String eighteenHundred = "1800";
        final String nineHundred = "0900";
        final String other = "2011-12-12";
        final String inTheFuture = "5666";

        StringValidator validator = new StringValidator();
        assertTrue(validator.validateStringIsYear(twentyEleven));
        assertTrue(validator.validateStringIsYear(nineteenEleven));
        assertFalse(validator.validateStringIsYear(thirtyEleven));
        assertFalse(validator.validateStringIsYear(nineHundred));
        assertFalse(validator.validateStringIsYear(eighteenHundred));

        //Check that current year is valid, but next year is not
        assertTrue(validator.validateStringIsYear(validThisYear));
        assertFalse(validator.validateStringIsYear(validButFuture));

        assertFalse(validator.validateStringIsYear(inTheFuture));

        assertFalse(validator.validateStringIsYear(other));
    }

    @Test
    public void testValidateNullStringGenerates() {
        StringValidator validator = new StringValidator();
        assertFalse(validator.validateStringIsYear(null));
    }
}
