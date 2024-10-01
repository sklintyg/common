/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import com.google.common.base.MoreObjects;
import java.time.LocalDateTime;
import java.util.Objects;

public class GrundData {

    private LocalDateTime signeringsdatum;
    private HoSPersonal skapadAv;
    private Patient patient;
    private Relation relation;
    private boolean isTestIntyg;

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

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public boolean isTestIntyg() {
        return isTestIntyg;
    }

    public void setTestIntyg(boolean isTestIntyg) {
        this.isTestIntyg = isTestIntyg;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof GrundData)) {
            return false;
        }
        final GrundData that = (GrundData) object;
        return Objects.equals(this.signeringsdatum, that.signeringsdatum)
            && Objects.equals(this.skapadAv, that.skapadAv)
            && Objects.equals(this.patient, that.patient)
            && Objects.equals(this.relation, that.relation)
            && Objects.equals(this.isTestIntyg, that.isTestIntyg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.signeringsdatum, this.skapadAv, this.patient, this.relation, this.isTestIntyg);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("signeringsdatum", signeringsdatum)
            .add("skapadAv", skapadAv)
            .add("patient", patient)
            .add("relation", relation)
            .add("isTestIntyg", isTestIntyg)
            .toString();
    }
}
