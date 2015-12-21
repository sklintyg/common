/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
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

/**
 * Common utils used for validation.
 *
 * @author Gustav Norbäcker, R2M
 */
public final class ValidatorUtils {

    public static final int BASE_10 = 10;

    private ValidatorUtils() {
    }

    /**
     * Calculates the modulo 10 checksum of a numeric string (the luhn algorithm).
     *
     * @param number A numeric string (in order to support leading zeroes).
     * @return The modulo 10 checksum.
     */
    public static int calculateMod10(String number) {
        int cs = 0;
        int multiple = 2;
        for (int i = 0; i < number.length(); i++) {
            int code = Integer.parseInt(number.substring(i, i + 1));
            int pos = multiple * code;
            cs += pos % BASE_10 + pos / BASE_10;
            multiple = (multiple == 1 ? 2 : 1);
        }

        // Subtract the sum modulo 10 from 10.
        // The remainder becomes the checksum. If the remainder is 10 the
        // checksum i 0.
        return (BASE_10 - (cs % BASE_10)) % BASE_10;
    }
}
