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

package se.inera.intyg.common.support.model.common.internal;

import org.joda.time.LocalDateTime;

import java.util.Objects;

public class GrundData {
    private LocalDateTime signeringsdatum;
    private HoSPersonal skapadAv;
    private Patient patient;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final GrundData that = (GrundData) object;
        return Objects.equals(this.signeringsdatum, that.signeringsdatum) &&
                Objects.equals(this.skapadAv, that.skapadAv) &&
                Objects.equals(this.patient, that.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.signeringsdatum, this.skapadAv, this.patient);
    }

    public LocalDateTime getSigneringsdatum() {
        return signeringsdatum;
    }

    public void setSigneringsdatum(LocalDateTime signeringsdatum) {
        this.signeringsdatum = signeringsdatum;
    }

    public HoSPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HoSPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
