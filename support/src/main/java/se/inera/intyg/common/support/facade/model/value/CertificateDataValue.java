package se.inera.intyg.common.support.facade.model.value;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = CertificateDataTextValue.class, name = "TEXT"),
    @Type(value = CertificateDataValueBoolean.class, name = "BOOLEAN")
})
public interface CertificateDataValue {
    CertificateDataValueType getType();
}
