package se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter;

import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.StatusType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.v1.UtlatandeStatus;
import se.inera.certificate.modules.support.api.CertificateStateHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public final class CertificateStateHolderConverter {

    private CertificateStateHolderConverter() {
    }

    public static List<UtlatandeStatus> toCertificateStatusType(List<CertificateStateHolder> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }

        List<UtlatandeStatus> states = new ArrayList<>();
        for (CertificateStateHolder state : source) {
            states.add(toCertificateStatusType(state));
        }
        return states;
    }

    private static UtlatandeStatus toCertificateStatusType(CertificateStateHolder source) {
        UtlatandeStatus status = new UtlatandeStatus();
        status.setTarget(source.getTarget());
        status.setTimestamp(source.getTimestamp());
        status.setType(StatusType.valueOf(source.getState().name()));
        return status;
    }
}
