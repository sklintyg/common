package se.inera.intyg.common.support.facade.model.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@JsonDeserialize(builder = CertificateDataConfigTextArea.CertificateDataConfigTextAreaBuilder.class)
@Value
@Builder
public class CertificateDataConfigTextArea implements CertificateDataConfig {

    @Getter(onMethod = @__(@Override))
    CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_TEXTAREA;
    @Getter(onMethod = @__(@Override))
    String header;
    @Getter(onMethod = @__(@Override))
    String icon;
    @Getter(onMethod = @__(@Override))
    String text;
    @Getter(onMethod = @__(@Override))
    String description;
    String id;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateDataConfigTextAreaBuilder {

    }
}
