package se.inera.intyg.common.support.facade.model.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = CertificateDataConfigTextArea.CertificateDataConfigTextAreaBuilder.class)
@Value
@Builder
public class CertificateDataConfigTextArea implements CertificateDataConfig {

    CertificateDataConfigTypes type = CertificateDataConfigTypes.UE_TEXTAREA;
    String header;
    String icon;
    String text;
    String description;
    String id;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateDataConfigTextAreaBuilder {

    }
}
