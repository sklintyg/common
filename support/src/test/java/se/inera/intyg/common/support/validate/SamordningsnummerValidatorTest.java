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

import com.google.common.base.Joiner;
import java.time.LocalDate;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the {@link PersonnummerValidator}.
 *
 * @author Gustav Norbäcker, R2M
 */
public class SamordningsnummerValidatorTest {

    /**
     * The validator to test.
     */
    private SamordningsnummerValidator validator;

    @Before
    public void setup() {
        validator = new SamordningsnummerValidator();
        // Set a fixed date for the validator so test don't break in the future.
        validator.setReferenceDate(LocalDate.parse("2013-08-22"));
    }

    /**
     * Test that only dates in the samordningsnummer series are supported.
     */
    @Test
    public void testPersonnummerDate() throws Exception {
        assertListSize(0, validator.validateExtension("19800191-0002"));
        assertListSize(0, validator.validateExtension("19800289-0005"));

        assertListSize(1, validator.validateExtension("19810289-0004"));
        assertListSize(1, validator.validateExtension("19800131-0005"));
        assertListSize(1, validator.validateExtension("19800229-0008"));
    }

    private void assertListSize(int size, List<String> collection) {
        String validationMessage = Joiner.on(',').join(collection);
        assertEquals(validationMessage, size, collection.size());
    }
}
