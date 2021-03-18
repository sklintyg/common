package se.inera.intyg.common.support.modules.support.facade.dto;

public class CertificateRelationsDTO {

    private CertificateRelationDTO parent;
    private CertificateRelationDTO[] children;

    public CertificateRelationDTO getParent() {
        return parent;
    }

    public void setParent(CertificateRelationDTO parent) {
        this.parent = parent;
    }

    public CertificateRelationDTO[] getChildren() {
        return children;
    }

    public void setChildren(CertificateRelationDTO[] children) {
        this.children = children;
    }
}
