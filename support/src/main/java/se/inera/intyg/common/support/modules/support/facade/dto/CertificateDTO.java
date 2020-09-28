package se.inera.intyg.common.support.modules.support.facade.dto;

import java.util.HashMap;
import java.util.Map;

public class CertificateDTO {

    private CertificateMetaDataDTO metadata;
    private Map<String, CertificateDataElementDTO> data = new HashMap<>();

    public CertificateMetaDataDTO getMetadata() {
        return metadata;
    }

    public void setMetadata(CertificateMetaDataDTO metadata) {
        this.metadata = metadata;
    }

    public Map<String, CertificateDataElementDTO> getData() {
        return data;
    }

    public void setData(
        Map<String, CertificateDataElementDTO> data) {
        this.data = data;
    }
}
