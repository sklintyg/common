package se.inera.certificate.modules.support.api.exception;

public class ExternalServiceCallException extends ModuleException {

    private static final long serialVersionUID = 4316452926850518463L;

    private final ErrorIdEnum erroIdEnum;

    public ExternalServiceCallException() {
        super();
        this.erroIdEnum = ErrorIdEnum.APPLICATION_ERROR;
    }

    public ExternalServiceCallException(String message, Throwable cause) {
        super(message, cause);
        this.erroIdEnum = ErrorIdEnum.APPLICATION_ERROR;
    }

    public ExternalServiceCallException(String message) {
        super(message);
        this.erroIdEnum = ErrorIdEnum.APPLICATION_ERROR;
    }

    public ExternalServiceCallException(Throwable cause) {
        super(cause);
        this.erroIdEnum = ErrorIdEnum.APPLICATION_ERROR;
    }

    public ExternalServiceCallException(String message, ErrorIdEnum errorIdEnum) {
        super(message);
        this.erroIdEnum = errorIdEnum;
    }

    public ErrorIdEnum getErroIdEnum() {
        return this.erroIdEnum;
    }

    public enum ErrorIdEnum {
        VALIDATION_ERROR,
        TRANSFORMATION_ERROR,
        APPLICATION_ERROR,
        TECHNICAL_ERROR;
    }

}
