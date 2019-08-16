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

import com.google.common.base.Joiner;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HsaIdValidatorTest {

    private HsaIdValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new HsaIdValidator();
    }

    @Test
    public void testHsaIdParser() {
        /** This should work */
        assertListSize(0, validator.validateExtension("SE0000000000-1337"));
        assertListSize(0, validator.validateExtension("SE5565594230-1337"));
        assertListSize(0, validator.validateExtension("SE0000000000-012345678901234567"));
        assertListSize(0, validator.validateExtension("SE160000000000-1337"));
        assertListSize(0, validator.validateExtension("SE0000000000- '()+,-./:=?"));
        assertListSize(0, validator.validateExtension("SE5565594230-YJ54"));

        /** Expect errors */
        assertListSize(1, validator.validateExtension("DK000000000037"));
        assertListSize(1, validator.validateExtension("SE160000000000- '()+,-./:=?&"));
        assertListSize(1, validator.validateExtension("SE000000000037"));
        assertListSize(1, validator.validateExtension("SE0000000000001337"));
        assertListSize(1, validator.validateExtension("SE000000000000-1337"));
        assertListSize(1, validator.validateExtension("SE0000000000-0123456789012345678"));
    }

    private void assertListSize(int size, List<String> collection) {
        String validationMessage = Joiner.on(',').join(collection);
        Assert.assertEquals(validationMessage, size, collection.size());
    }

}
