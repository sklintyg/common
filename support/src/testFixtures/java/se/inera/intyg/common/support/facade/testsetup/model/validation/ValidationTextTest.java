/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.facade.testsetup.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;

public abstract class ValidationTextTest extends ValidationTest {

    protected abstract short getLimit();

    @Override
    protected CertificateDataValidationType getType() {
        return CertificateDataValidationType.TEXT_VALIDATION;
    }

    @Test
    void shouldIncludeValidationTextLimit() {
        final var question = getElement();
        final var certificateDataValidationText = (CertificateDataValidationText) question.getValidation()[getValidationIndex()];
        assertEquals(getLimit(), certificateDataValidationText.getLimit());
    }
}
