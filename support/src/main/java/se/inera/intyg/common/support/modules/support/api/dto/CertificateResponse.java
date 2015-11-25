package se.inera.intyg.common.support.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

public class CertificateResponse {

    private final String internalModel;
    private final Utlatande utlatande;
    private final CertificateMetaData metaData;
    private final boolean revoked;

    public CertificateResponse(String internalModel, Utlatande utlatande, CertificateMetaData metaData, boolean revoked) {
        hasText(internalModel, "'internalModel' must not be empty");
        notNull(metaData, "'metaData' must not be null");
        this.internalModel = internalModel;
        this.utlatande = utlatande;
        this.metaData = metaData;
        this.revoked = revoked;
    }

    public String getInternalModel() {
        return internalModel;
    }

    public Utlatande getUtlatande() {
        return utlatande;
    }

    public CertificateMetaData getMetaData() {
        return metaData;
    }

    public boolean isRevoked() {
        return revoked;
    }

}
