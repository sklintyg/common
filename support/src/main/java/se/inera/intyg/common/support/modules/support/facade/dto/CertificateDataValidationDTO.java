package se.inera.intyg.common.support.modules.support.facade.dto;

public class CertificateDataValidationDTO {

    private boolean required;
    private String requiredProp;
    private String hideExpression;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getRequiredProp() {
        return requiredProp;
    }

    public void setRequiredProp(String requiredProp) {
        this.requiredProp = requiredProp;
    }

    public String getHideExpression() {
        return hideExpression;
    }

    public void setHideExpression(String hideExpression) {
        this.hideExpression = hideExpression;
    }
}
