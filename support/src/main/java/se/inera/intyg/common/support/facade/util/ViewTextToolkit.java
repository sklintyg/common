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

public final class ViewTextToolkit {

    private static final String NOT_SPECIFIED = "Ej Angivet";
    private static final String YES = "Ja";
    private static final String NO = "Nej";

    private ViewTextToolkit() {
    }

    public static String booleanValue(Boolean value) {
        if (value == null) {
            return NOT_SPECIFIED;
        }
        if (value) {
            return YES;
        } else {
            return NO;
        }
    }

    public static String stringValue(String value) {
        if (value == null) {
            return NOT_SPECIFIED;
        } else {
            return value;
        }
    }

    public static String doubleValue(Double value) {
        if (value == null) {
            return NOT_SPECIFIED;
        } else {
            return value.toString();
        }
    }

    public static String multipleStringValues(String... value) {
        if (value == null) {
            return NOT_SPECIFIED;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String text : value) {
            if (text == null) {
                continue;
            }
            stringBuilder.append(text).append(" ");
        }

        final var text = stringBuilder.toString().trim();

        if (text.isEmpty()) {
            return NOT_SPECIFIED;
        }

        return stringBuilder.toString().trim();
    }

    public static String withComma(String... value) {
        final var stringBuilder = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            if (i < value.length - 1) {
                stringBuilder.append(value[i]).append(", ");
            } else {
                stringBuilder.append(value[i]);
            }
        }
        return stringBuilder.toString();
    }
}
