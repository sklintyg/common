package se.inera.intyg.common.support.facade.model.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = CertificateDataConfigBoolean.class, name = "UE_RADIO_BOOLEAN"),
    @Type(value = CertificateDataConfigCategory.class, name = "CATEGORY"),
    @Type(value = CertificateDataConfigTextArea.class, name = "UE_TEXTAREA")
})
public interface CertificateDataConfig {

    CertificateDataConfigTypes getType();

    String getHeader();

    String getIcon();

    String getText();

    String getDescription();
}
