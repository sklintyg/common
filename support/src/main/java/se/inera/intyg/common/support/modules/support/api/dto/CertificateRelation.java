package se.inera.intyg.common.support.modules.support.api.dto;

import se.inera.intyg.common.support.common.enumerations.RelationKod;

import java.time.LocalDateTime;

/**
 * Reusable generic representation of a Relation between two intyg. While an instance of this class usually exists
 * as a relation (no pun intended!) on an Certificate instance, the relation may either be a child-relation
 * (e.g. a child is referencing me) or as a parent (I am referencing my parent)
 *
 * CERT B <- from <- RELATION (REPLACES) -> to -> CERT A.
 *
 * An instance of this class is identified by franIntygsId, tillIntygsId and relationKod.
 *
 * Applications may subclass this class in order to add application-specific information about relations such
 * as UtkastState in Webcert.
 *
 * Created by eriklupander on 2017-05-10.
 */
public class CertificateRelation {

    private String franIntygsId;
    private String tillIntygsId;
    private RelationKod relationKod;
    private LocalDateTime skapad;

    public CertificateRelation() {
    }

    public CertificateRelation(String franIntygsId, String tillIntygsId, RelationKod relationKod, LocalDateTime skapad) {
        this.franIntygsId = franIntygsId;
        this.tillIntygsId = tillIntygsId;
        this.relationKod = relationKod;
        this.skapad = skapad;
    }

    public String getFranIntygsId() {
        return franIntygsId;
    }

    public void setFranIntygsId(String franIntygsId) {
        this.franIntygsId = franIntygsId;
    }

    public String getTillIntygsId() {
        return tillIntygsId;
    }

    public void setTillIntygsId(String tillIntygsId) {
        this.tillIntygsId = tillIntygsId;
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

        if (!franIntygsId.equals(that.franIntygsId)) {
            return false;
        }
        if (!tillIntygsId.equals(that.tillIntygsId)) {
            return false;
        }
        return relationKod == that.relationKod;
    }

    // CHECKSTYLE:OFF MagicNumber
    @Override
    public int hashCode() {
        int result = franIntygsId.hashCode();
        result = 31 * result + tillIntygsId.hashCode();
        result = 31 * result + relationKod.hashCode();
        return result;
    }
    // CHECKSTYLE:ON MagicNumber
}
