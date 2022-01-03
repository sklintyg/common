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
package se.inera.intyg.common.support.common.enumerations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DiagnoskodverkTest {

    @Test
    public void testGetEnumByCodeSystem() {
        assertEquals(Diagnoskodverk.ICD_10_SE, Diagnoskodverk.getEnumByCodeSystem(Diagnoskodverk.ICD_10_SE.getCodeSystem()));
        assertEquals(Diagnoskodverk.KSH_97_P, Diagnoskodverk.getEnumByCodeSystem(Diagnoskodverk.KSH_97_P.getCodeSystem()));
        assertEquals(Diagnoskodverk.SNOMED_CT, Diagnoskodverk.getEnumByCodeSystem(Diagnoskodverk.SNOMED_CT.getCodeSystem()));
        assertNull(Diagnoskodverk.getEnumByCodeSystem("Doesnt exist"));
    }
}
