package se.inera.intyg.common.support.facade.builder;

import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;

public final class CertificateDataElementBuilder {

    private String id;
    private String parent;
    private int index;
    private CertificateDataConfig certificateDataConfig;
    private CertificateDataValue certificateDataValue;
    private CertificateDataValidation[] certificateDataValidations;

    public static CertificateDataElementBuilder create() {
        return new CertificateDataElementBuilder();
    }

    private CertificateDataElementBuilder() {

    }

    public CertificateDataElementBuilder id(String id) {
        this.id = id;
        return this;
    }

    public CertificateDataElementBuilder parent(String parent) {
        this.parent = parent;
        return this;
    }

    public CertificateDataElement build() {
        final var certificateDataElement = new CertificateDataElement();
        certificateDataElement.setId(id);
        certificateDataElement.setParent(parent);
        certificateDataElement.setIndex(index);
        certificateDataElement.setConfig(certificateDataConfig);
        certificateDataElement.setValue(certificateDataValue);
        certificateDataElement.setValidation(certificateDataValidations);
        return certificateDataElement;
    }

    public CertificateDataElementBuilder index(int index) {
        this.index = index;
        return this;
    }

    public CertificateDataElementBuilder config(CertificateDataConfig certificateDataConfig) {
        this.certificateDataConfig = certificateDataConfig;
        return this;
    }

    public CertificateDataElementBuilder value(CertificateDataValue certificateDataValue) {
        this.certificateDataValue = certificateDataValue;
        return this;
    }

    public CertificateDataElementBuilder validation(CertificateDataValidation[] certificateDataValidations) {
        this.certificateDataValidations = certificateDataValidations;
        return this;
    }
}
