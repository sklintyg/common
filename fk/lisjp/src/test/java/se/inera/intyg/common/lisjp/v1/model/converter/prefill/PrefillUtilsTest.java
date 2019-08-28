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
    public void getValidatedBoolean() throws PrefillWarningException {
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add("true");
        assertTrue(PrefillUtils.getValidatedBoolean(delsvar));

        delsvar.getContent().clear();
        delsvar.getContent().add("false");
        assertFalse(PrefillUtils.getValidatedBoolean(delsvar));
    }

    @Test(expected = PrefillWarningException.class)
    public void getValidatedBooleanFails() throws PrefillWarningException {
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add("sant");
        PrefillUtils.getValidatedBoolean(delsvar);
    }

    @Test
    public void getValidatedString() throws PrefillWarningException {
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
    public void getValidatedStringFailsForOtherType() throws PrefillWarningException {
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(new ObjectFactory().createSvarDelsvar());
        PrefillUtils.getValidatedString(delsvar,100);
    }

    @Test(expected = PrefillWarningException.class)
    public void getValidatedStringFailsWhenExceedingMaxLength() throws PrefillWarningException {
        String expected = "Lite text";
        Delsvar delsvar = new ObjectFactory().createSvarDelsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(expected);
        PrefillUtils.getValidatedString(delsvar,2);
    }
}