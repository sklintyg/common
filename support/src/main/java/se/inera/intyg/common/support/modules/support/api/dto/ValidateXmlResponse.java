/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
 * A validate draft response contains a binary VALID/INVALID status and a list of validation messages.
 */
public class ValidateXmlResponse {

    private final ValidationStatus status;

    private final List<String> validationErrors;

    public ValidateXmlResponse(ValidationStatus status, List<String> validationErrors) {
        Assert.notNull(status, "'status' must not be null");
        Assert.notNull(validationErrors, "'validationErrors' must not be null");
        this.status = status;
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public boolean hasErrorMessages() {
        return !this.validationErrors.isEmpty();
    }

    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public ValidationStatus getStatus() {
        return status;
    }
}
