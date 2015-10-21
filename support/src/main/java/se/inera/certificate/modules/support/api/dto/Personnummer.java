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
        return HashUtility.hash(getNormalizedPnr());
    }

    /**
     * Get the personnummer in a standardized format regardless of how it was entered.
     */
    private String getNormalizedPnr() {
        return getPersonnummerWithoutDash(); //This is a simple start but will not fix all, e.g. 2 vs 4 digits year, - vs +, etc.
    }

    public boolean isSamordningsNummer() {
        return SamordningsnummerValidator.isSamordningsNummer(pnr);
    }

    public String getPersonnummerWithoutDash() {
        if (pnr == null) {
            return null;
        }
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
        return Objects.equal(getNormalizedPnr(), that.getNormalizedPnr());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNormalizedPnr());
    }

    public static String getPnrHashSafe(Personnummer personnummer) {
        return personnummer == null ? HashUtility.hash(null) : personnummer.getPnrHash();
    }

    public static Personnummer empty() {
        return new Personnummer(null);
    }

}
