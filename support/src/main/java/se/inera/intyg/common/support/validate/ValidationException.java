package se.inera.intyg.common.support.validate;

/**
 * @author andreaskaltenbach
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
