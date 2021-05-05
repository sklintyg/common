package se.inera.intyg.common.support.facade.model.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@JsonDeserialize(builder = CertificateDataConfigCategory.CertificateDataConfigCategoryBuilder.class)
@Value
@Builder
public class CertificateDataConfigCategory implements CertificateDataConfig {

    @Getter(onMethod = @__(@Override))
    CertificateDataConfigTypes type = CertificateDataConfigTypes.CATEGORY;
    @Getter(onMethod = @__(@Override))
    String header;
    @Getter(onMethod = @__(@Override))
    String icon;
    @Getter(onMethod = @__(@Override))
    String text;
    @Getter(onMethod = @__(@Override))
    String description;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateDataConfigCategoryBuilder {

    }
}
