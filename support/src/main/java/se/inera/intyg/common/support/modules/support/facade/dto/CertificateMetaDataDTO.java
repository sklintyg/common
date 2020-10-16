package se.inera.intyg.common.support.modules.support.facade.dto;

import java.time.LocalDateTime;

public class CertificateMetaDataDTO {

    private String certificateId;
    private String certificateType;
    private String certificateTypeVersion;
    private String certificateName;
    private String certificateDescription;
    private LocalDateTime created;
    private boolean testCertificate;
    private CertificateStatusDTO certificateStatus;
    private CertificateStaffDTO issuedBy;
    private CertificateUnitDTO unit;
    private CertificateUnitDTO careProvider;
    private CertificatePatientDTO patient;
    private CertificateRelationsDTO relations;
    private long version;

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

    public String getCertificateTypeVersion() {
        return certificateTypeVersion;
    }

    public void setCertificateTypeVersion(String certificateTypeVersion) {
        this.certificateTypeVersion = certificateTypeVersion;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public void setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isTestCertificate() {
        return testCertificate;
    }

    public void setTestCertificate(boolean testCertificate) {
        this.testCertificate = testCertificate;
    }

    public CertificateStatusDTO getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(CertificateStatusDTO certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public CertificateStaffDTO getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(CertificateStaffDTO issuedBy) {
        this.issuedBy = issuedBy;
    }

    public CertificateUnitDTO getUnit() {
        return unit;
    }

    public void setUnit(CertificateUnitDTO unit) {
        this.unit = unit;
    }

    public CertificateUnitDTO getCareProvider() {
        return careProvider;
    }

    public void setCareProvider(CertificateUnitDTO careProvider) {
        this.careProvider = careProvider;
    }

    public CertificatePatientDTO getPatient() {
        return patient;
    }

    public void setPatient(CertificatePatientDTO patient) {
        this.patient = patient;
    }

    public CertificateRelationsDTO getRelations() {
        return relations;
    }

    public void setRelations(CertificateRelationsDTO relations) {
        this.relations = relations;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
