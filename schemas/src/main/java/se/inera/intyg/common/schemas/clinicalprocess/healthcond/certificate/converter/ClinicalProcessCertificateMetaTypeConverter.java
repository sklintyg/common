package se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.CertificateMetaType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Status;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;

public final class ClinicalProcessCertificateMetaTypeConverter {

    private ClinicalProcessCertificateMetaTypeConverter() { }

    public static CertificateMetaData toCertificateMetaData(CertificateMetaType certificateMetaType) {
        CertificateMetaData metaData = new CertificateMetaData();
        metaData.setCertificateId(certificateMetaType.getCertificateId());
        metaData.setCertificateType(certificateMetaType.getCertificateType());
        metaData.setValidFrom(certificateMetaType.getValidFrom());
        metaData.setValidTo(certificateMetaType.getValidTo());
        metaData.setIssuerName(certificateMetaType.getIssuerName());
        metaData.setFacilityName(certificateMetaType.getFacilityName());
        metaData.setSignDate(certificateMetaType.getSignDate());
        metaData.setAdditionalInfo(certificateMetaType.getComplemantaryInfo());
        metaData.setAvailable(certificateMetaType.getAvailable().toLowerCase().equals("true"));
        List<Status> statuses = toStatusList(certificateMetaType.getStatus());
        metaData.setStatus(statuses);
        return metaData;
    }

    public static List<Status> toStatusList(List<UtlatandeStatus> certificateStatuses) {
        List<Status> statuses = new ArrayList<>(certificateStatuses.size());
        for (UtlatandeStatus certificateStatus : certificateStatuses) {
            statuses.add(toStatus(certificateStatus));
        }
        return statuses;
    }

    public static Status toStatus(UtlatandeStatus certificateStatus) {
        Status status = new Status(
                CertificateState.valueOf(certificateStatus.getType().value()),
                certificateStatus.getTarget(),
                certificateStatus.getTimestamp());
        return status;
    }
}
