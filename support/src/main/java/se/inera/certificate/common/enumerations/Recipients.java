package se.inera.certificate.common.enumerations;

/**
* Created by Magnus Ekstrand on 25/02/15.
*/
public enum Recipients {
    FK("FK"),
    TS("TS");

    private final String text;

    Recipients(final String text) {
        this.text = text;
    }

    @Override public String toString() {
        return text;
    }
}
