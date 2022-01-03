/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import java.util.Objects;

import java.time.LocalDate;

import se.inera.intyg.common.support.common.enumerations.RelationKod;

public class Relation {

    private RelationKod relationKod;

    private String relationIntygsId;

    private String meddelandeId;

    private LocalDate sistaGiltighetsDatum;

    private String sistaSjukskrivningsgrad;

    private String referensId;

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Relation)) {
            return false;
        }
        final Relation that = (Relation) object;
        return Objects.equals(this.relationKod, that.relationKod)
            && Objects.equals(this.relationIntygsId, that.relationIntygsId)
            && Objects.equals(this.meddelandeId, that.meddelandeId)
            && Objects.equals(this.sistaGiltighetsDatum, that.sistaGiltighetsDatum)
            && Objects.equals(this.sistaSjukskrivningsgrad, that.sistaSjukskrivningsgrad)
            && Objects.equals(this.referensId, that.referensId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.relationKod, this.relationIntygsId,
            this.meddelandeId, this.sistaGiltighetsDatum, this.sistaSjukskrivningsgrad, this.referensId);
    }

    public RelationKod getRelationKod() {
        return relationKod;
    }

    public void setRelationKod(RelationKod relationKod) {
        this.relationKod = relationKod;
    }

    public String getRelationIntygsId() {
        return relationIntygsId;
    }

    public void setRelationIntygsId(String relationIntygsId) {
        this.relationIntygsId = relationIntygsId;
    }

    public String getMeddelandeId() {
        return meddelandeId;
    }

    public void setMeddelandeId(String meddelandeId) {
        this.meddelandeId = meddelandeId;
    }

    public LocalDate getSistaGiltighetsDatum() {
        return sistaGiltighetsDatum;
    }

    public void setSistaGiltighetsDatum(LocalDate sistaGiltighetsDatum) {
        this.sistaGiltighetsDatum = sistaGiltighetsDatum;
    }

    public String getSistaSjukskrivningsgrad() {
        return sistaSjukskrivningsgrad;
    }

    public void setSistaSjukskrivningsgrad(String sistaSjukskrivningsgrad) {
        this.sistaSjukskrivningsgrad = sistaSjukskrivningsgrad;
    }

    public String getReferensId() {
        return referensId;
    }

    public void setReferensId(String referensId) {
        this.referensId = referensId;
    }

}
