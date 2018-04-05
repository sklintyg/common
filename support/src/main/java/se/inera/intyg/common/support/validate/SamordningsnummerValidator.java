/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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

import se.inera.intyg.schemas.contract.InvalidPersonNummerException;
import se.inera.intyg.schemas.contract.Personnummer;

import java.time.LocalDate;

import static se.inera.intyg.common.support.Constants.SAMORDNING_ID_OID;

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
     * Controls if a civic registration number is a 'samordningsnummer' or not.
     * @param personNummer the civic registration number
     * @return true if the civic registration number is a 'samordningsnummer', otherwise false
     */
    public static boolean isSamordningsNummer(Personnummer personNummer) {

        // In order to determine if a personnummer is a samordningsnummer, we need to have a normalized yyyyMMdd-NNNN
        // number. If we cannot parse the encapsulated string, it certainly isn't a personnummer.
        try {
            String normalizedPersonnummer = personNummer.getNormalizedPnr();
            char dateDigit = normalizedPersonnummer.charAt(SAMORDNING_MONTH_INDEX);
            return Character.getNumericValue(dateDigit) >= SAMORDNING_MONTH_VALUE_MIN;
        } catch (InvalidPersonNummerException e) {
            // An invalid personnummer cannot be a samordningsnummer.
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoot() {
        return SAMORDNING_ID_OID;
    }

    /**
     * Samordningsnummer have 60 added to the day. In order to calculate the birth day
     * of the citizen, this needs to be subtracted.
     */
    // CHECKSTYLE:OFF MagicNumber
    @Override
    protected LocalDate getBirthDay(String birthDate) {
        String personNummer = birthDate;
        // In case we got a birthDate YYMMdd or YYYYMMdd we normalize it before checking if it is a samordningsnummer.
        if (personNummer != null && (personNummer.trim().length() == 6 || personNummer.trim().length() == 8) && isNumeric(personNummer)) {
            personNummer = personNummer + "-0000";
        }

        if (!isSamordningsNummer(new Personnummer(personNummer))) {
            throw new IllegalArgumentException("personNummer " + personNummer + " is not a valid 'samordningsnummer");
        }

        StringBuilder sb = new StringBuilder(personNummer);
        String substractedWith6 = String.valueOf((char) (sb.charAt(6) - 6));
        sb.replace(6, 7, substractedWith6);

        return super.getBirthDay(sb.toString());
    }
    // CHECKSTYLE:ON MagicNumber

    private boolean isNumeric(String personnummer) {
        try {
            Integer.parseInt(personnummer);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
