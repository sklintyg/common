package se.inera.intyg.common.support.common.enumerations;

/**
* Created by Magnus Ekstrand on 25/02/15.
*/
public enum CertificateTypes {
    FK7263("fk7263"),
    TSBAS("ts-bas"),
    TSDIABETES("ts-diabetes");

    private final String text;

    CertificateTypes(final String text) {
        this.text = text;
    }

    @Override public String toString() {
        return text;
    }
}
