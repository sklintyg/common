/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

import java.util.Arrays;

public final class ValidationExpressionToolkit {

    private ValidationExpressionToolkit() {

    }

    public static String singleExpression(String id) {
        return "$" + id;
    }

    public static String wrapWithParenthesis(String expression) {
        return "(" + expression + ")";
    }

    public static String not(String expression) {
        return "!" + expression;
    }

    public static String multipleOrExpression(String... expression) {
        return Arrays.stream(expression).reduce("", (s, s2) -> {
            if (!s.isEmpty()) {
                s += " || ";
            }
            s += s2;
            return s;
        });
    }

    public static String multipleAndExpression(String... expression) {
        return Arrays.stream(expression).reduce("", (s, s2) -> {
            if (!s.isEmpty()) {
                s += " && ";
            }
            s += s2;
            return s;
        });
    }
}
