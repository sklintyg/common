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

package se.inera.intyg.common.schemas.insuranceprocess.healthreporting.converter;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

public class ModelConverterTest {

    @Test
    public void testVardreferensId() {
        String intygId = "INTYGID";
        LocalDateTime time = LocalDateTime.of(2011, 01, 02, 23, 59, 01, 1);
        String res = ModelConverter.buildVardReferensId(intygId, time);
        assertEquals("REVOKE-" + intygId + "-20110102T235901.000" , res);
    }
}
