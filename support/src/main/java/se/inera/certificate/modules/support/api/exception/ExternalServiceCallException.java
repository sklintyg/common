package se.inera.certificate.modules.support.api.exception;

public class ExternalServiceCallException extends ModuleException {

    private static final long serialVersionUID = 4316452926850518463L;

    public ExternalServiceCallException() {
        super();
    }

    public ExternalServiceCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalServiceCallException(String message) {
        super(message);
    }

    public ExternalServiceCallException(Throwable cause) {
        super(cause);
    }
}
