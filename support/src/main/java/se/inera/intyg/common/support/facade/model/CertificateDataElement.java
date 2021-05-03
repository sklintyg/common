package se.inera.intyg.common.support.facade.model;

import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.ValidationError;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;

public class CertificateDataElement {

    private String id;
    private String parent;
    private int index;
    private CertificateDataConfig config;
    private CertificateDataValue value;
    private CertificateDataValidation[] validation;
    private ValidationError[] validationError;

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

    public CertificateDataConfig getConfig() {
        return config;
    }

    public void setConfig(CertificateDataConfig config) {
        this.config = config;
    }

    public CertificateDataValue getValue() {
        return value;
    }

    public void setValue(CertificateDataValue value) {
        this.value = value;
    }

    public CertificateDataValidation[] getValidation() {
        return validation;
    }

    public void setValidation(CertificateDataValidation[] validation) {
        this.validation = validation;
    }

    public ValidationError[] getValidationError() {
        return validationError;
    }

    public void setValidationError(ValidationError[] validationError) {
        this.validationError = validationError;
    }
}
