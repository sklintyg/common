package se.inera.intyg.common.support.facade.model.value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CertificateDataTextValue.class)
public class CertificateDataTextValue extends CertificateDataValue {

    private String id;
    private String text;

    public CertificateDataTextValue() {
        setType(CertificateDataValueType.TEXT);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
