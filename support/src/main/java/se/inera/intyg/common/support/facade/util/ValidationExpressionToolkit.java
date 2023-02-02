/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

    public static String exists(String expression) {
        return String.format("exists(%s)", expression);
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

    public static String withCitation(String expression) {
        return "'" + expression + "'";
    }

    public static String wrapWithNotEmpty(String expression) {
        return "!empty(" + expression + ")";
    }

    public static String lessThan(String s1, String s2) {
        return s1 + " < " + s2;
    }

    public static String lessThanOrEqual(String s1, String s2) {
        return s1 + " <= " + s2;
    }

    public static String moreThanOrEqual(String s1, String s2) {
        return s1 + " >= " + s2;
    }

    public static String moreThanOrEqual(String s1, Long l) {
        return moreThanOrEqual(s1, l.toString());
    }

    public static String lessThanOrEqual(String s1, Long l) {
        return lessThanOrEqual(s1, l.toString());
    }

    public static String moreThan(String s1, String s2) {
        return s1 + " > " + s2;
    }

    public static String moreThan(String s1, Long l) {
        return moreThan(s1, l.toString());
    }

    public static String appendAttribute(String s, String attribute) {
        return s + '.' + attribute;
    }

    public static String equalsWith(String s, String value) {
        return s + " == " + value;
    }

    public static String subtract(String s, String value) {
        return s + " - " + value;
    }

    public static String to(String s) {
        return s + ".to";
    }

    public static String from(String s) {
        return s + ".from";
    }
}
