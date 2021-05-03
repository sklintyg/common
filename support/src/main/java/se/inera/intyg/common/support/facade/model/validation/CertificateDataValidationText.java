package se.inera.intyg.common.support.facade.model.validation;

public class CertificateDataValidationText extends CertificateDataValidation {

    private String id;
    private short limit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public short getLimit() {
        return limit;
    }

    public void setLimit(short limit) {
        this.limit = limit;
    }
}
