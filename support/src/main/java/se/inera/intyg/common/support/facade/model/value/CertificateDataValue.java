package se.inera.intyg.common.support.facade.model.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.inera.intyg.common.support.facade.util.CertificateDataValueDeserializer;

@JsonDeserialize(using = CertificateDataValueDeserializer.class)
abstract public class CertificateDataValue {

    public static final String TYPE_FIELD = "type";
    private CertificateDataValueType type;

    public CertificateDataValueType getType() {
        return type;
    }

    public void setType(CertificateDataValueType type) {
        this.type = type;
    }
}
