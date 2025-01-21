/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

import com.google.common.base.Strings;
import java.time.Year;

/**
 * Validator intended for use in draft validation,
 * performs validation of strings against various conditions.
 */
public class StringValidator {

    static final String YEAR_FORMAT = "yyyy";
    static final String CENTURY = "^(19|20)\\d{2}";
    private static final String POSTALCODE_FORMAT = "\\d{3}\\s?\\d{2}";

    /**
     * Validate that a given String contains a year of the format yyyy,<br/>
     * and that the year it contains is in the general vicinity of current date i.e 19xx-20xx,<br/>
     * and that the year if otherwise valid is not set in the future.
     *
     * @param source the string, should not be null
     * @return true if the string is a valid year, false otherwise
     */
    public boolean validateStringIsYear(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return false;
        }
        if (!source.matches(CENTURY)) {
            return false;
        }
        return !isYearInFuture(source);
    }

    /**
     * Validate if a given string matches the accepted format for postal codes.
     *
     * @param source the string
     * @return true if it does, false otherwise
     */
    public boolean validateStringAsPostalCode(String source) {
        return source.matches(POSTALCODE_FORMAT);
    }

    private boolean isYearInFuture(String source) {
        return Year.parse(source).isAfter(Year.now());
    }
}
