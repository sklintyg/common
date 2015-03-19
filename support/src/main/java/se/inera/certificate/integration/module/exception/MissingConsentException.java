package se.inera.certificate.integration.module.exception;

/**
 * @author andreaskaltenbach
 */
public class MissingConsentException extends RuntimeException {

    private static final long serialVersionUID = -2935854410295967047L;

    public MissingConsentException(String civicRegistrationNumber) {
        super(String.format("Consent required from user %s", civicRegistrationNumber));
    }
}
