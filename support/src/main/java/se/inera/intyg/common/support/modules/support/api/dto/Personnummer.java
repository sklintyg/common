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

package se.inera.intyg.common.support.modules.support.api.dto;

import java.util.Calendar;

import se.inera.intyg.common.util.logging.HashUtility;
import se.inera.intyg.common.support.validate.SamordningsnummerValidator;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;

@JsonDeserialize(using = PersonnummerDeserializer.class)
public class Personnummer {

    private final String pnr;

    public Personnummer(String pnr) {
        this.pnr = pnr;
    }

    @JsonValue
    public String getPersonnummer() {
        return pnr;
    }

    /**
     * Will return the hashed personnummer to make sure the real personnummer is not accidentally logged.
     * @return Hashed personnummer
     */
    @Override
    public String toString() {
        assert false : "Don't use implicit toString. Use explicit getPnrHash or getPersonnummer instead.";
        return getPnrHash();
    }

    public String getPnrHash() {
        return HashUtility.hash(getNormalizedPnrIfPossible(pnr));
    }

    /**
     * Returns the normalized personnummer if it is valid, otherwise returns the originally set personnummer.
     */
    private String getNormalizedPnrIfPossible(String returnValueWhenInvalid) {
        try {
            return getNormalizedPnr();
        } catch (InvalidPersonNummerException e) {
            return returnValueWhenInvalid;
        }
    }

    /**
     * Get the personnummer in a standardized format (yyyyMMddxxxx) regardless of how it was entered.
     */
    public String getNormalizedPnr() throws InvalidPersonNummerException {
        if (pnr == null) {
            throw new InvalidPersonNummerException("Can not normalize null");
        }
        if (pnr.matches("[0-9]{8}[-+]?[0-9]{4}")) {
            return pnr.replace("-", "").replace("+", "");
        }
        if (pnr.matches("[0-9]{6}[+-]?[0-9]{4}")) {
            return getCenturyFromYearAndSeparator(pnr) + pnr.replace("-", "").replace("+", "");
        }
        throw new InvalidPersonNummerException("Personnummer format not handled: " + pnr);
    }

    private String getCenturyFromYearAndSeparator(String personnummer) {
        final Calendar now = Calendar.getInstance();
        final int currentYear = now.getWeekYear();
        final boolean personnummerContainsCentury = personnummer.matches("[0-9]{8}[-+]?[0-9]{4}");
        final int yearStartIndex = personnummerContainsCentury ? 2 : 0;
        final int yearFromPersonnummer = Integer.parseInt(personnummer.substring(yearStartIndex, yearStartIndex + 2));
        final int dividerToRemoveNonCenturyYear = 100;
        final int century = (currentYear - yearFromPersonnummer) / dividerToRemoveNonCenturyYear;
        if (personnummer.contains("+")) {
            return String.valueOf(century - 1);
        }
        return String.valueOf(century);
    }

    public boolean isSamordningsNummer() {
        final String normalizedPnr = getNormalizedPnrIfPossible(null);
        if (normalizedPnr == null) {
            return false;
        }
        return SamordningsnummerValidator.isSamordningsNummer(normalizedPnr);
    }

    public String getPersonnummerWithoutDash() {
        if (pnr == null) {
            return null;
        }
        return pnr.replace("-", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Personnummer that = (Personnummer) o;
        return Objects.equal(getNormalizedPnrIfPossible(pnr), that.getNormalizedPnrIfPossible(that.pnr));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNormalizedPnrIfPossible(pnr));
    }

    public static String getPnrHashSafe(Personnummer personnummer) {
        return personnummer == null ? HashUtility.hash(null) : personnummer.getPnrHash();
    }

    public static Personnummer empty() {
        return new Personnummer(null);
    }

}
