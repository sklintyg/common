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

package se.inera.intyg.common.support.model;

import com.google.common.base.Objects;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A way of handling date intervals in our internal model that allows faulty user input,
 * this is needed at this stage because of the auto save function among other things. <br/>
 * <br/>
 *
 * This class contains util methods for various things such as getting the start or end dates as {@link LocalDate}[s]
 * etc.
 *
 * @author erik
 */
public class InternalLocalDateInterval {

    private InternalDate from;
    private InternalDate tom;

    public InternalLocalDateInterval() {
    }

    /**
     * Construct an InternalLocalDateInterval from strings.
     *
     * @param from
     *            String representing start date
     * @param tom
     *            String representing end date
     * @throws ModelException if from or tom is null
     */
    public InternalLocalDateInterval(String from, String tom) {
        if (from == null || tom == null) {
            throw new ModelException("Got null while trying to create InternalLocalDateInterval");
        }
        this.from = new InternalDate(from);
        this.tom = new InternalDate(tom);
    }
    public InternalLocalDateInterval(InternalDate from, InternalDate tom) {
        if (from == null || tom == null) {
            throw new ModelException("Got null while trying to create InternalLocalDateInterval");
        }
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
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof InternalLocalDateInterval)) {
            return false;
        }
        InternalLocalDateInterval otherInterval = (InternalLocalDateInterval) other;

        if (!this.isValid() || !otherInterval.isValid()) {
            return false;
        }
        return this.fromAsLocalDate().equals(otherInterval.fromAsLocalDate()) && this.tomAsLocalDate().equals(otherInterval.tomAsLocalDate());
    }

    @JsonIgnore
    @Override
    public int hashCode() {
        return Objects.hashCode(fromAsLocalDate(), tomAsLocalDate());
    }

}
