/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.appendAttribute;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.equalsWith;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.from;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.lessThan;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.lessThanOrEqual;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.moreThan;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.moreThanOrEqual;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleAndExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.not;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.subtract;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.to;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.withCitation;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithAttribute;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithNotEmpty;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithParenthesis;

import org.junit.jupiter.api.Test;

class ValidationExpressionToolkitTest {

    @Test
    void shallPrefixWithDollarCharacter() {
        assertEquals("$variable", singleExpression("variable"));
    }

    @Test
    void shallWrapWithParenthesis() {
        assertEquals("(variable)", wrapWithParenthesis("variable"));
    }

    @Test
    void shallPrefixWithExclamation() {
        assertEquals("!variable", not("variable"));
    }

    @Test
    void shallWrapWithExistsFunction() {
        assertEquals("exists(variable)", exists("variable"));
    }

    @Test
    void shallAddDoublePipeBetweenExpression() {
        assertEquals("variable1 || variable2 || variable3", multipleOrExpression("variable1", "variable2", "variable3"));
    }

    @Test
    void shallAddDoubleAmpersandBetweenExpression() {
        assertEquals("variable1 && variable2 && variable3", multipleAndExpression("variable1", "variable2", "variable3"));
    }

    @Test
    void shallWrapWithApostrophe() {
        assertEquals("'variable'", withCitation("variable"));
    }

    @Test
    void shallAddLessThan() {
        assertEquals("variable1 < variable2", lessThan("variable1", "variable2"));
    }

    @Test
    void shallAddLessThanOrEquals() {
        assertEquals("variable1 <= variable2", lessThanOrEqual("variable1", "variable2"));
    }

    @Test
    void shallAddLessThanOrEqualsWithLong() {
        assertEquals("variable1 <= 1", lessThanOrEqual("variable1", 1L));
    }

    @Test
    void shallAddMoreThan() {
        assertEquals("variable1 > variable2", moreThan("variable1", "variable2"));
    }

    @Test
    void shallAddMoreThanWithLong() {
        assertEquals("variable1 > 1", moreThan("variable1", 1L));
    }

    @Test
    void shallAppendAttribute() {
        assertEquals("variable1.variable2", appendAttribute("variable1", "variable2"));
    }

    @Test
    void shallAddEquals() {
        assertEquals("variable1 == variable2", equalsWith("variable1", "variable2"));
    }

    @Test
    void shallWrapWithNotEmpty() {
        assertEquals("!empty(variable)", wrapWithNotEmpty("variable"));
    }

    @Test
    void shallSubtract() {
        assertEquals("variable - variable", subtract("variable", "variable"));
    }

    @Test
    void shallAddTo() {
        assertEquals("variable.to", to("variable"));
    }

    @Test
    void shallAddFrom() {
        assertEquals("variable.from", from("variable"));
    }

    @Test
    void shallAddMoreThanOrEquals() {
        assertEquals("variable >= variable2", moreThanOrEqual("variable", "variable2"));
    }

    @Test
    void shallAddMoreThanOrEqualsWithLong() {
        assertEquals("variable >= 2", moreThanOrEqual("variable", 2L));
    }

    @Test
    void shallWrapWithAttribute() {
        assertEquals("attribute(variable)", wrapWithAttribute("variable", "attribute"));
    }
}
