package se.inera.intyg.common.support.facade.model.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.inera.intyg.common.support.facade.util.CertificateDataConfigDeserializer;

@JsonDeserialize(using = CertificateDataConfigDeserializer.class)
public class CertificateDataConfig {

    public static final String TYPE_FIELD = "type";

    private String header;
    private String icon;
    private String text;
    private String description;
    private CertificateDataConfigTypes type;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CertificateDataConfigTypes getType() {
        return type;
    }

    public void setType(CertificateDataConfigTypes type) {
        this.type = type;
    }
}
