package se.inera.certificate.modules.support.api.dto;

import se.inera.certificate.logging.HashUtility;
import se.inera.certificate.validate.SamordningsnummerValidator;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Objects;

public class Personnummer {

    private final String pnr;

    public Personnummer(String pnr) {
        this.pnr = pnr;
    }

    @JsonValue
    public String getPersonnummer() {
        return pnr;
    }

    /**
     * Will return the hashed personnummer to make sure the real personnummer is not accidentally logged.
     * @return Hashed personnummer
     */
    @Override
    public String toString() {
        return getPnrHash();
    }

    public String getPnrHash() {
        return HashUtility.hash(pnr);
    }

    public boolean isSamordningsNummer() {
        return SamordningsnummerValidator.isSamordningsNummer(pnr);
    }

    //Should pnr be with or without dash, maybe this method is not needed
    public String getPersonnummerWithoutDash() {
        return pnr.replace("-", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Personnummer that = (Personnummer) o;
        return Objects.equal(pnr, that.pnr);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pnr);
    }

    public static String getPnrHashSafe(Personnummer personnummer) {
        return personnummer == null ? HashUtility.hash(null) : personnummer.getPnrHash();
    }

}
