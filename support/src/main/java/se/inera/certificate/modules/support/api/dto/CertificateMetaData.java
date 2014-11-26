package se.inera.certificate.modules.support.api.dto;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import se.inera.certificate.model.Status;

public class CertificateMetaData {

    protected String certificateId;
    protected String certificateType;
    protected LocalDate validFrom;
    protected LocalDate validTo;
    protected String issuerName;
    protected String facilityName;
    protected LocalDateTime signDate;
    protected boolean available;
    
    public String getCertificateId() {
        return certificateId;
    }
    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }
    public String getCertificateType() {
        return certificateType;
    }
    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
    public LocalDate getValidFrom() {
        return validFrom;
    }
    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }
    public LocalDate getValidTo() {
        return validTo;
    }
    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
    public String getIssuerName() {
        return issuerName;
    }
    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }
    public String getFacilityName() {
        return facilityName;
    }
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }
    public LocalDateTime getSignDate() {
        return signDate;
    }
    public void setSignDate(LocalDateTime signDate) {
        this.signDate = signDate;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public List<Status> getStatus() {
        return status;
    }
    public void setStatus(List<Status> status) {
        this.status = status;
    }
    public String getAdditionalInfo() {
        return additionalInfo;
    }
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    protected List<Status> status;
    protected String additionalInfo;

}
