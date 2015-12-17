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

import org.apache.commons.lang3.CharUtils;
import org.joda.time.LocalDate;

/**
 * Performs validation of a 'Samordningsnummer'.
 *
 * @author Gustav Norbäcker, R2M
 * @see PersonnummerValidator
 */
public class SamordningsnummerValidator extends PersonnummerValidator {

    private static final int SAMORDNING_MONTH_INDEX = 6;
    private static final int SAMORDNING_MONTH_VALUE_MIN = 6;

    /**
     * The root for samordningsnummer.
     */
    public static final String SAMORDNINGSNUMMER_ROOT = "1.2.752.129.2.1.3.3";

    /**
     * Controls if a civic registration number is a 'samordningsnummer' or not.
     * @param personNummer the civic registration number
     * @return true if the civic registration number is a 'samordningsnummer', otherwise false
     */
    public static boolean isSamordningsNummer(String personNummer) {
        char dateDigit = personNummer.charAt(SAMORDNING_MONTH_INDEX);
        return (CharUtils.toIntValue(dateDigit) >= SAMORDNING_MONTH_VALUE_MIN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoot() {
        return SAMORDNINGSNUMMER_ROOT;
    }

    /**
     * Samordningsnummer have 60 added to the day. In order to calculate the birth day
     * of the citizen, this needs to be substracted.
     */
    // CHECKSTYLE:OFF MagicNumber
    protected LocalDate getBirthDay(String personNummer) throws IllegalArgumentException {
        if (!isSamordningsNummer(personNummer)) {
            throw new IllegalArgumentException("personNummer " + personNummer + " is not a valid 'samordningsnummer");
        }

        StringBuilder sb = new StringBuilder(personNummer);
        String substractedWith6 = String.valueOf((char) (sb.charAt(6) - 6));
        sb.replace(6, 7, substractedWith6);

        return super.getBirthDay(sb.toString());
    }
    // CHECKSTYLE:ON MagicNumber

}
