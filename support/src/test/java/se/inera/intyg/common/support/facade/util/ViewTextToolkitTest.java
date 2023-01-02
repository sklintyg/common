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

package se.inera.intyg.common.support.facade.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.booleanValue;
import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.multipleStringValues;
import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.stringValue;
import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.withComma;

import org.junit.jupiter.api.Test;

class ViewTextToolkitTest {

    private static final String NOT_SPECIFIED = "Ej Angivet";
    private static final String YES = "Ja";
    private static final String NO = "Nej";

    @Test
    void shallConvertBooleanValues() {
        assertAll(
            () -> assertEquals(YES, booleanValue(true)),
            () -> assertEquals(NO, booleanValue(false)),
            () -> assertEquals(NOT_SPECIFIED, booleanValue(null))
        );
    }

    @Test
    void shallConvertStringValue() {
        final var expectedValue = "expectedValue";
        assertAll(
            () -> assertEquals(expectedValue, stringValue(expectedValue)),
            () -> assertEquals(NOT_SPECIFIED, stringValue(null))
        );
    }


    @Test
    void shallConvertMultipleStringValues() {
        final var expectedValue = "expectedValue";
        final var expectedValuex3 = "expectedValue expectedValue expectedValue";
        assertAll(
            () -> assertEquals(expectedValuex3, multipleStringValues(expectedValue, expectedValue, expectedValue)),
            () -> assertEquals(NOT_SPECIFIED, multipleStringValues(null, null, null))
        );
    }

    @Test
    void shallAddCommas() {
        final var expectedValue = "test, test, test";
        assertEquals(expectedValue, withComma("test", "test", "test"));
    }
}