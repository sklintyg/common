package se.inera.certificate.modules.support.api.dto;

import org.springframework.util.Assert;

public class ValidationMessage {

    private final String field;

    private final String message;

    private final ValidationMessageType type;

    public ValidationMessage(String field, ValidationMessageType type, String message) {
        Assert.hasText(field, "'field' must not be empty");
        Assert.notNull(type, "'type' must not be empty");
        Assert.hasText(message, "'message' must not be empty");
        this.field = field;
        this.type = type;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public ValidationMessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return field + ":" + type.toString() + " " + message;
    }
}
