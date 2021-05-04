package se.inera.intyg.common.support.facade.model.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = CertificateDataConfigCategory.CertificateDataConfigCategoryBuilder.class)
@Value
@Builder
public class CertificateDataConfigCategory implements CertificateDataConfig {

    CertificateDataConfigTypes type = CertificateDataConfigTypes.CATEGORY;
    String header;
    String icon;
    String text;
    String description;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateDataConfigCategoryBuilder {

    }
}
