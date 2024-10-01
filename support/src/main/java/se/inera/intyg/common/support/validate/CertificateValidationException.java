/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.validate;

import com.google.common.base.Joiner;
import java.util.List;

public class CertificateValidationException extends Exception {

    private static final long serialVersionUID = -5449346862818721648L;

    private static final String VALIDATION_ERROR_PREFIX = "Validation Error(s) found: ";

    public CertificateValidationException(String message) {
        super(VALIDATION_ERROR_PREFIX + message);
    }

    public CertificateValidationException(List<String> messages) {
        super(VALIDATION_ERROR_PREFIX + Joiner.on("\n").join(messages));
    }
}
