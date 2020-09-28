package se.inera.intyg.common.support.modules.support.facade.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = CertificateDataValueDeserializer.class)
abstract public class CertificateDataValueDTO {

    public static final String TYPE_FIELD = "type";
    private CertificateDataValueTypeDTO type;

    public CertificateDataValueTypeDTO getType() {
        return type;
    }

    public void setType(CertificateDataValueTypeDTO type) {
        this.type = type;
    }
}
