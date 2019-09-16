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
package se.inera.intyg.common.lisjp.v1.model.converter.prefill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import se.riv.clinicalprocess.healthcond.certificate.v3.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public class PrefillUtilsTest {


    @Test
    public void testGetValidatedBoolean() throws PrefillWarningException {
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add("true");
        assertTrue(PrefillUtils.getValidatedBoolean(delsvar));

        delsvar.getContent().clear();
        delsvar.getContent().add("false");
        assertFalse(PrefillUtils.getValidatedBoolean(delsvar));
    }

    @Test(expected = PrefillWarningException.class)
    public void testGetValidatedBooleanFails() throws PrefillWarningException {
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add("sant");
        PrefillUtils.getValidatedBoolean(delsvar);
    }

    @Test
    public void testGetValidatedString() throws PrefillWarningException {
        String expected = "Lite text";
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(expected);
        assertEquals(expected, PrefillUtils.getValidatedString(delsvar, 100));

        delsvar.getContent().clear();
        delsvar.getContent().add("");
        assertEquals("", PrefillUtils.getValidatedString(delsvar, 100));
    }

    @Test(expected = PrefillWarningException.class)
    public void testGetValidatedStringFailsForOtherType() throws PrefillWarningException {
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(new ObjectFactory().createSvarDelsvar());
        PrefillUtils.getValidatedString(delsvar, 100);
    }

    @Test(expected = PrefillWarningException.class)
    public void testGetValidatedStringFailsWhenExceedingMaxLength() throws PrefillWarningException {
        String expected = "Lite text";
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(expected);
        PrefillUtils.getValidatedString(delsvar, 2);
    }

    @Test
    public void testGetValidatedDateString() throws PrefillWarningException {
        String expected = "2019-05-06";
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(expected);
        assertEquals(expected, PrefillUtils.getValidatedDateString(delsvar));
    }

    @Test
    public void testGetValidatedDateStringFailsForFaultyDateStrings() {

        assertTrue(isValid("2019-02-02"));

        assertFalse(isValid(null));
        assertFalse(isValid(""));
        assertFalse(isValid("0"));
        assertFalse(isValid("2019"));
        assertFalse(isValid("20190101"));
        assertFalse(isValid("201901-01"));
        assertFalse(isValid("2019-1-1"));
        assertFalse(isValid("20191-01-01"));
        assertFalse(isValid("2019-01-011"));
        assertFalse(isValid("2019-13-01"));
        assertFalse(isValid("2019-02-33"));
    }

    private boolean isValid(String input) {
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(input);
        try {
            final String output = PrefillUtils.getValidatedDateString(delsvar);
            return output.equals(input);
        } catch (PrefillWarningException e) {
            return false;
        }
    }
}