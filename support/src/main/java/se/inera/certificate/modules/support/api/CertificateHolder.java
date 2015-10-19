package se.inera.certificate.modules.support.api;

import java.util.List;

import org.joda.time.LocalDateTime;
import se.inera.certificate.modules.support.api.dto.Personnummer;

public class CertificateHolder {

    /**
     * Id of the certificate.
     */
    private String id;

    /**
     * Certificate document.
     */
    private String document;

    /**
     * The transport model (XML) that was used to generate this entity.
     */
    private String originalCertificate;

    /**
     * Type of the certificate.
     */
    private String type;

    /**
     * Name of the doctor that signed the certificate.
     */
    private String signingDoctorName;

    /**
     * Id of care unit.
     */
    private String careUnitId;

    /**
     * Name of care unit.
     */
    private String careUnitName;

    /**
     * Id of care unit.
     */
    private String careGiverId;

    /**
     * Civic registration number for patient.
     */
    private Personnummer civicRegistrationNumber;

    /**
     * Time this certificate was signed.
     */
    private LocalDateTime signedDate;

    /**
     * Time from which this certificate is valid.
     */
    private String validFromDate;

    /**
     * Time to which this certificate is valid.
     */
    private String validToDate;

    /**
     * Additional information.
     */
    private String additionalInfo;

    /**
     * Deleted flag.
     */
    private boolean deleted;

    /**
     * If this certificate is no longer used by the care giver.
     * <p>
     * This can be due to that the care giver has stopped using WebCert and have their certificates persisted elsewhere.
     * The certificate can be deleted from the database as soon as the citizen no longer has access to the certificate
     * (by revoking its consent or stops being a citizen).
     */
    private boolean deletedByCareGiver;

    /**
     * If this certificate was wireTapped.
     */
    private boolean wireTapped = false;

    /**
     * Certificate states.
     */
    private List<CertificateStateHolder> certificateStates;

    /**
     * If this certificate is revoked.
     */
    private boolean revoked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getOriginalCertificate() {
        return originalCertificate;
    }

    public void setOriginalCertificate(String originalCertificate) {
        this.originalCertificate = originalCertificate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSigningDoctorName() {
        return signingDoctorName;
    }

    public void setSigningDoctorName(String signingDoctorName) {
        this.signingDoctorName = signingDoctorName;
    }

    public String getCareUnitId() {
        return careUnitId;
    }

    public void setCareUnitId(String careUnitId) {
        this.careUnitId = careUnitId;
    }

    public String getCareUnitName() {
        return careUnitName;
    }

    public void setCareUnitName(String careUnitName) {
        this.careUnitName = careUnitName;
    }

    public String getCareGiverId() {
        return careGiverId;
    }

    public void setCareGiverId(String careGiverId) {
        this.careGiverId = careGiverId;
    }

    public Personnummer getCivicRegistrationNumber() {
        return civicRegistrationNumber;
    }

    public void setCivicRegistrationNumber(Personnummer civicRegistrationNumber) {
        this.civicRegistrationNumber = civicRegistrationNumber;
    }

    public LocalDateTime getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(LocalDateTime signedDate) {
        this.signedDate = signedDate;
    }

    public String getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(String validFromDate) {
        this.validFromDate = validFromDate;
    }

    public String getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(String validToDate) {
        this.validToDate = validToDate;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeletedByCareGiver() {
        return deletedByCareGiver;
    }

    public void setDeletedByCareGiver(boolean deletedByCareGiver) {
        this.deletedByCareGiver = deletedByCareGiver;
    }

    public boolean isWireTapped() {
        return wireTapped;
    }

    public void setWireTapped(boolean wireTapped) {
        this.wireTapped = wireTapped;
    }

    public List<CertificateStateHolder> getCertificateStates() {
        return certificateStates;
    }

    public void setCertificateStates(List<CertificateStateHolder> certificateStates) {
        this.certificateStates = certificateStates;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    @Override
    public String toString() {
        return "CertificateHolder [id=" + id + ", document=" + document + ", originalCertificate=" + originalCertificate + ", type=" + type
                + ", signingDoctorName=" + signingDoctorName + ", careUnitId=" + careUnitId + ", careUnitName=" + careUnitName
                + ", civicRegistrationNumber=" + civicRegistrationNumber + ", signedDate=" + signedDate + ", validFromDate=" + validFromDate
                + ", validToDate=" + validToDate + ", additionalInfo=" + additionalInfo + ", deleted=" + deleted + ", deletedByCareGiver="
                + deletedByCareGiver + ", certificateStates=" + certificateStates + ", revoked=" + revoked + "]";
    }

    @Override
    public int hashCode() {
        // CHECKSTYLE:OFF MagicNumber
        final int prime = 31;
        int result = 1;
        result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
        result = prime * result + ((careUnitId == null) ? 0 : careUnitId.hashCode());
        result = prime * result + ((careUnitName == null) ? 0 : careUnitName.hashCode());
        result = prime * result + ((careGiverId == null) ? 0 : careGiverId.hashCode());
        result = prime * result + ((certificateStates == null) ? 0 : certificateStates.hashCode());
        result = prime * result + ((civicRegistrationNumber == null) ? 0 : civicRegistrationNumber.hashCode());
        result = prime * result + (deleted ? 1231 : 1237);
        result = prime * result + (deletedByCareGiver ? 1231 : 1237);
        result = prime * result + (wireTapped ? 1231 : 1237);
        result = prime * result + ((document == null) ? 0 : document.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (revoked ? 1231 : 1237);
        result = prime * result + ((signedDate == null) ? 0 : signedDate.hashCode());
        result = prime * result + ((signingDoctorName == null) ? 0 : signingDoctorName.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((validFromDate == null) ? 0 : validFromDate.hashCode());
        result = prime * result + ((validToDate == null) ? 0 : validToDate.hashCode());
        return result;
        // CHECKSTYLE:ON MagicNumber
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CertificateHolder other = (CertificateHolder) obj;
        if (additionalInfo == null) {
            if (other.additionalInfo != null) {
                return false;
            }
        } else if (!additionalInfo.equals(other.additionalInfo)) {
            return false;
        }
        if (careUnitId == null) {
            if (other.careUnitId != null) {
                return false;
            }
        } else if (!careUnitId.equals(other.careUnitId)) {
            return false;
        }
        if (careUnitName == null) {
            if (other.careUnitName != null) {
                return false;
            }
        } else if (!careUnitName.equals(other.careUnitName)) {
            return false;
        }
        if (careGiverId == null) {
            if (other.careGiverId != null) {
                return false;
            }
        } else if (!careGiverId.equals(other.careGiverId)) {
            return false;
        }
        if (certificateStates == null) {
            if (other.certificateStates != null) {
                return false;
            }
        } else if (!certificateStates.equals(other.certificateStates)) {
            return false;
        }
        if (civicRegistrationNumber == null) {
            if (other.civicRegistrationNumber != null) {
                return false;
            }
        } else if (!civicRegistrationNumber.equals(other.civicRegistrationNumber)) {
            return false;
        }
        if (deleted != other.deleted) {
            return false;
        }
        if (deletedByCareGiver != other.deletedByCareGiver) {
            return false;
        }
        if (wireTapped != other.wireTapped) {
            return false;
        }
        if (document == null) {
            if (other.document != null) {
                return false;
            }
        } else if (!document.equals(other.document)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (revoked != other.revoked) {
            return false;
        }
        if (signedDate == null) {
            if (other.signedDate != null) {
                return false;
            }
        } else if (!signedDate.equals(other.signedDate)) {
            return false;
        }
        if (signingDoctorName == null) {
            if (other.signingDoctorName != null) {
                return false;
            }
        } else if (!signingDoctorName.equals(other.signingDoctorName)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (validFromDate == null) {
            if (other.validFromDate != null) {
                return false;
            }
        } else if (!validFromDate.equals(other.validFromDate)) {
            return false;
        }
        if (validToDate == null) {
            if (other.validToDate != null) {
                return false;
            }
        } else if (!validToDate.equals(other.validToDate)) {
            return false;
        }
        return true;
    }

}
