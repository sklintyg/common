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
package se.inera.intyg.common.support.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import java.time.LocalDate;

/**
 * A way of handling date intervals in our internal model that allows faulty
 * user input, this is needed at this stage because of the auto save function
 * among other things. <br/>
 * <br/>
 *
 * This class contains util methods for various things such as getting the start
 * or end dates as {@link LocalDate}[s] etc.
 *
 * @author erik
 */
public class InternalLocalDateInterval {

    private static final InternalDate MIN_DATE = new InternalDate("1900-01-01");
    private static final InternalDate MAX_DATE = new InternalDate("2099-12-12");

    private InternalDate from;
    private InternalDate tom;

    public InternalLocalDateInterval() {
        // Needed for deserialization
    }

    /**
     * Construct an InternalLocalDateInterval from strings.
     *
     * @param from String representing start date
     * @param tom String representing end date
     */
    public InternalLocalDateInterval(String from, String tom) {
        this.from = new InternalDate(from);
        this.tom = new InternalDate(tom);
    }

    public InternalLocalDateInterval(InternalDate from, InternalDate tom) {
        this.from = from;
        this.tom = tom;
    }

    public InternalDate getFrom() {
        return from;
    }

    public void setFrom(InternalDate from) {
        this.from = from;
    }

    public InternalDate getTom() {
        return tom;
    }

    public void setTom(InternalDate tom) {
        this.tom = tom;
    }

    /**
     * Attempts to parse the String held as start date to a LocalDate.
     *
     * @return {@link LocalDate} if parsing was successful
     */
    public LocalDate fromAsLocalDate() {
        if (from == null) {
            return null;
        }
        try {
            return from.asLocalDate();
        } catch (ModelException e) {
            return null;
        }
    }

    /**
     * Attempts to parse the String held as end date to a LocalDate.
     *
     * @return {@link LocalDate} if parsing was successful
     */
    public LocalDate tomAsLocalDate() {
        if (tom == null) {
            return null;
        }
        try {
            return tom.asLocalDate();
        } catch (ModelException e) {
            return null;
        }
    }

    @JsonIgnore
    public boolean isValid() {
        if (this.from == null || this.tom == null) {
            return false;
        }
        if (from.isValidDate() && tom.isValidDate()) {
            return !tom.asLocalDate().isBefore(from.asLocalDate());
        } else {
            return false;
        }
    }

    @JsonIgnore
    public boolean isReasonable() {
        boolean reasonableFrom = this.from.asLocalDate().isAfter(MIN_DATE.asLocalDate())
            && this.from.asLocalDate().isBefore(MAX_DATE.asLocalDate());
        boolean reasonableTo = this.tom.asLocalDate().isAfter(MIN_DATE.asLocalDate())
            && this.tom.asLocalDate().isBefore(MAX_DATE.asLocalDate());
        return isValid() && reasonableFrom && reasonableTo;
    }

    @JsonIgnore
    public boolean overlaps(Object other) {
        if (!(other instanceof InternalLocalDateInterval)) {
            return false;
        }
        InternalLocalDateInterval otherInterval = (InternalLocalDateInterval) other;

        if (!this.isValid() || !otherInterval.isValid()) {
            return false;
        }

        // We overlap if intervals are equal
        if (this.equals(otherInterval)) {
            return true;
        }

        // We overlap if end and start dates are equal
        if (this.fromAsLocalDate().equals(otherInterval.tomAsLocalDate())
            || this.tomAsLocalDate().equals(otherInterval.fromAsLocalDate())) {
            return true;
        }

        // We overlap if from1 < tom2 and tom1 > from2
        // http://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap
        return this.fromAsLocalDate().isBefore(otherInterval.tomAsLocalDate())
            && this.tomAsLocalDate().isAfter(otherInterval.fromAsLocalDate());
    }

    @JsonIgnore
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof InternalLocalDateInterval)) {
            return false;
        }
        InternalLocalDateInterval otherInterval = (InternalLocalDateInterval) other;

        return Objects.equal(this.fromAsLocalDate(), otherInterval.fromAsLocalDate())
            && Objects.equal(this.tomAsLocalDate(), otherInterval.tomAsLocalDate());
    }

    @JsonIgnore
    @Override
    public int hashCode() {
        return Objects.hashCode(fromAsLocalDate(), tomAsLocalDate());
    }

}
