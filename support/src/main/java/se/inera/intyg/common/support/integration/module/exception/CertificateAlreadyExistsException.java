package se.inera.intyg.common.support.integration.module.exception;

/**
 * @author andreaskaltenbach
 */
public class CertificateAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 6746299605626528366L;

    public CertificateAlreadyExistsException(String certificateId) {
        super(String.format("Certificate '%s' already exists", certificateId));
    }
}
