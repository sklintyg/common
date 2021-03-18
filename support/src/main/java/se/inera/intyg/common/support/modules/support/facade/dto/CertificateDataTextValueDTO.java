package se.inera.intyg.common.support.modules.support.facade.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CertificateDataTextValueDTO.class)
public class CertificateDataTextValueDTO extends CertificateDataValueDTO {

    private String text;
    private Integer limit;

    public CertificateDataTextValueDTO() {
        setType(CertificateDataValueTypeDTO.TEXT);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
