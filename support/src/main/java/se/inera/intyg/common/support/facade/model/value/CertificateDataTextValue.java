package se.inera.intyg.common.support.facade.model.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = CertificateDataTextValue.CertificateDataTextValueBuilder.class)
@Value
@Builder
public class CertificateDataTextValue implements CertificateDataValue {

    String id;
    String text;
    CertificateDataValueType type = CertificateDataValueType.TEXT;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateDataTextValueBuilder {

    }
}
