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
package se.inera.intyg.common.support.modules.support.api.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import se.inera.intyg.common.support.common.enumerations.RelationKod;

/**
 * Reusable generic representation of a Relation between two intyg. While an instance of this class usually exists
 * as a relation (no pun intended!) on an Certificate instance, the relation may either be a child-relation
 * (e.g. a child is referencing me) or as a parent (I am referencing my parent)
 *
 * CERT B <- from <- RELATION (REPLACES) -> to -> CERT A.
 *
 * An instance of this class is identified by fromIntygsId, toIntygsId and relationKod.
 *
 * Applications may subclass this class in order to add application-specific information about relations such
 * as UtkastState in Webcert.
 *
 * Created by eriklupander on 2017-05-10.
 */
public class CertificateRelation {

    private String fromIntygsId;
    private String toIntygsId;
    private RelationKod relationKod;
    private LocalDateTime skapad;

    public CertificateRelation() {
    }

    public CertificateRelation(String fromIntygsId, String toIntygsId, RelationKod relationKod, LocalDateTime skapad) {
        this.fromIntygsId = fromIntygsId;
        this.toIntygsId = toIntygsId;
        this.relationKod = relationKod;
        this.skapad = skapad;
    }

    public String getFromIntygsId() {
        return fromIntygsId;
    }

    public void setFromIntygsId(String fromIntygsId) {
        this.fromIntygsId = fromIntygsId;
    }

    public String getToIntygsId() {
        return toIntygsId;
    }

    public void setToIntygsId(String toIntygsId) {
        this.toIntygsId = toIntygsId;
    }

    public RelationKod getRelationKod() {
        return relationKod;
    }

    public void setRelationKod(RelationKod relationKod) {
        this.relationKod = relationKod;
    }

    public LocalDateTime getSkapad() {
        return skapad;
    }

    public void setSkapad(LocalDateTime skapad) {
        this.skapad = skapad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CertificateRelation)) {
            return false;
        }

        CertificateRelation that = (CertificateRelation) o;

        if (!fromIntygsId.equals(that.fromIntygsId)) {
            return false;
        }
        if (!toIntygsId.equals(that.toIntygsId)) {
            return false;
        }
        return relationKod == that.relationKod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromIntygsId, toIntygsId, relationKod);
    }
}
