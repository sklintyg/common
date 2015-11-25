package se.inera.certificate.integration.module.exception;

import se.inera.certificate.modules.support.api.dto.Personnummer;

/**
 * @author andreaskaltenbach
 */
public class MissingConsentException extends RuntimeException {

    private static final long serialVersionUID = -2935854410295967047L;

    public MissingConsentException(Personnummer civicRegistrationNumber) {
        super(String.format("Consent required from user %s", Personnummer.getPnrHashSafe(civicRegistrationNumber)));
    }
}
