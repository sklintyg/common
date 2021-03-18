package se.inera.intyg.common.support.modules.support.facade.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = CertificateDataBooleanValueDTO.class)
public class CertificateDataBooleanValueDTO extends CertificateDataValueDTO {

    private Boolean selected;
    private String selectedText;
    private String unselectedText;

    public CertificateDataBooleanValueDTO() {
        setType(CertificateDataValueTypeDTO.BOOLEAN);
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public String getUnselectedText() {
        return unselectedText;
    }

    public void setUnselectedText(String unselectedText) {
        this.unselectedText = unselectedText;
    }
}
