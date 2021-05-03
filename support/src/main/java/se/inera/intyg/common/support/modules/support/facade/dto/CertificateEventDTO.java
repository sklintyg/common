package se.inera.intyg.common.support.modules.support.facade.dto;

import java.time.LocalDateTime;
import se.inera.intyg.common.support.facade.model.CertificateStatus;

public class CertificateEventDTO {

    private String certificateId;
    private LocalDateTime timestamp;
    private CertificateEventTypeDTO type;
    private String relatedCertificateId;
    private CertificateStatus relatedCertificateStatus;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public CertificateEventTypeDTO getType() {
        return type;
    }

    public void setType(CertificateEventTypeDTO type) {
        this.type = type;
    }

    public String getRelatedCertificateId() {
        return relatedCertificateId;
    }

    public void setRelatedCertificateId(String relatedCertificateId) {
        this.relatedCertificateId = relatedCertificateId;
    }

    public CertificateStatus getRelatedCertificateStatus() {
        return relatedCertificateStatus;
    }

    public void setRelatedCertificateStatus(CertificateStatus relatedCertificateStatus) {
        this.relatedCertificateStatus = relatedCertificateStatus;
    }
}
