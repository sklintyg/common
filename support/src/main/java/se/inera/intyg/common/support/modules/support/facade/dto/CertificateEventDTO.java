package se.inera.intyg.common.support.modules.support.facade.dto;

import java.time.LocalDateTime;

public class CertificateEventDTO {

    private String certificateId;
    private LocalDateTime timestamp;
    private CertificateEventTypeDTO type;
    private String relatedCertificateId;
    private CertificateStatusDTO relatedCertificateStatus;

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

    public CertificateStatusDTO getRelatedCertificateStatus() {
        return relatedCertificateStatus;
    }

    public void setRelatedCertificateStatus(CertificateStatusDTO relatedCertificateStatus) {
        this.relatedCertificateStatus = relatedCertificateStatus;
    }
}
