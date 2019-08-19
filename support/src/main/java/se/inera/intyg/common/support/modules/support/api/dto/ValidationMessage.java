/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.support.modules.support.api.dto;

import org.springframework.util.Assert;

import com.google.common.base.Objects;

public final class ValidationMessage {

    private final String category;

    private final String field;

    private final String message;

    private final ValidationMessageType type;

    private final String dynamicKey;

    public ValidationMessage(String category, String field, ValidationMessageType type) {
        Assert.hasText(category, "'category' must not be empty");
        Assert.hasText(field, "'field' must not be empty");
        Assert.notNull(type, "'type' must not be empty");
        this.category = category;
        this.field = field;
        this.type = type;
        this.message = null;
        this.dynamicKey = null;
    }

    public ValidationMessage(String category, String field, ValidationMessageType type, String message) {
        Assert.hasText(category, "'category' must not be empty");
        Assert.hasText(field, "'field' must not be empty");
        Assert.notNull(type, "'type' must not be empty");
        Assert.hasText(message, "'message' must not be empty");
        this.category = category;
        this.field = field;
        this.type = type;
        this.message = message;
        this.dynamicKey = null;
    }

    public ValidationMessage(String category, String field, ValidationMessageType type, String message, String dynamicKey) {
        Assert.hasText(category, "'category' must not be empty");
        Assert.hasText(field, "'field' must not be empty");
        Assert.notNull(type, "'type' must not be empty");
        Assert.hasText(message, "'message' must not be empty");
        Assert.hasText(dynamicKey, "'dynamicLabel' must not be empty");
        this.category = category;
        this.field = field;
        this.type = type;
        this.message = message;
        this.dynamicKey = dynamicKey;
    }

    public String getCategory() {
        return category;
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

    public String getDynamicKey() {
        return dynamicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidationMessage that = (ValidationMessage) o;
        return Objects.equal(category, that.category)
            && Objects.equal(field, that.field)
            && Objects.equal(message, that.message)
            && type == that.type
            && Objects.equal(dynamicKey, that.dynamicKey);
    }

    @Override
    public String toString() {
        return "ValidationMessage{"
            + "category='" + category + '\''
            + ", field='" + field + '\''
            + ", message='" + message + '\''
            + ", type=" + type
            + ", dynamicKey='" + dynamicKey + '\''
            + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(category, field, message, type, dynamicKey);
    }
}
