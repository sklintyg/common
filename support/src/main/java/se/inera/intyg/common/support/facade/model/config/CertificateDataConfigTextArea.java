package se.inera.intyg.common.support.facade.model.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CertificateDataConfigTextArea.class)
public class CertificateDataConfigTextArea extends CertificateDataConfig {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
