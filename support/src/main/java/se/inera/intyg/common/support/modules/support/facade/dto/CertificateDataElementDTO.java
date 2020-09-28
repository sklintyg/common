package se.inera.intyg.common.support.modules.support.facade.dto;

public class CertificateDataElementDTO {

    private String id;
    private String parent;
    private int index;
    private boolean visible;
    private boolean readOnly;
    private boolean mandatory;
    private CertificateDataConfigDTO config;
    private CertificateDataValueDTO value;
    private CertificateDataValidationDTO validation;
    private ValidationErrorDTO[] validationError;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public CertificateDataConfigDTO getConfig() {
        return config;
    }

    public void setConfig(CertificateDataConfigDTO config) {
        this.config = config;
    }

    public CertificateDataValueDTO getValue() {
        return value;
    }

    public void setValue(CertificateDataValueDTO value) {
        this.value = value;
    }

    public CertificateDataValidationDTO getValidation() {
        return validation;
    }

    public void setValidation(CertificateDataValidationDTO validation) {
        this.validation = validation;
    }

    public ValidationErrorDTO[] getValidationError() {
        return validationError;
    }

    public void setValidationError(ValidationErrorDTO[] validationError) {
        this.validationError = validationError;
    }
}
