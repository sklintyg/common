package se.inera.intyg.common.support.modules.support.facade.dto;

import java.time.LocalDateTime;

public class CertificateRelationDTO {

    private String certificateId;
    private CertificateRelationTypeDTO type;
    private CertificateStatusDTO status;
    private LocalDateTime created;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public CertificateRelationTypeDTO getType() {
        return type;
    }

    public void setType(CertificateRelationTypeDTO type) {
        this.type = type;
    }

    public CertificateStatusDTO getStatus() {
        return status;
    }

    public void setStatus(CertificateStatusDTO status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
