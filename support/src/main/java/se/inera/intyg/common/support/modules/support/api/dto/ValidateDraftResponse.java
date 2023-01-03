/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

/**
 * A validate draft response contains a binary VALID/INVALID status and two lists of validation messages -
 * validation errors and validation warnings.
 */
public class ValidateDraftResponse {

    private final ValidationStatus status;

    private final List<ValidationMessage> validationErrors;
    private final List<ValidationMessage> validationWarnings;

    public ValidateDraftResponse(ValidationStatus status, List<ValidationMessage> validationErrors) {
        this(status, validationErrors, new ArrayList<>());
    }

    public ValidateDraftResponse(ValidationStatus status,
        List<ValidationMessage> validationErrors,
        List<ValidationMessage> validationWarnings) {
        Assert.notNull(status, "'status' must not be null");
        Assert.notNull(validationErrors, "'validationErrors' must not be null");
        Assert.notNull(validationWarnings, "'validationWarnings' must not be null");
        this.status = status;
        this.validationErrors = new ArrayList<>(validationErrors);
        this.validationWarnings = new ArrayList<>(validationWarnings);
    }

    public boolean hasErrorMessages() {
        return !this.validationErrors.isEmpty();
    }

    public boolean hasWarningMessages() {
        return !this.validationWarnings.isEmpty();
    }

    public List<ValidationMessage> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public List<ValidationMessage> getValidationWarnings() {
        return validationWarnings;
    }

    public ValidationStatus getStatus() {
        return status;
    }
}
