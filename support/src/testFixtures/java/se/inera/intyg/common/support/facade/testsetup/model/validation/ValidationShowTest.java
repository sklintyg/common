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
package se.inera.intyg.common.support.facade.testsetup.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;

public abstract class ValidationShowTest extends ValidationTest {

    protected abstract String getQuestionId();

    protected abstract String getExpression();

    @Override
    protected CertificateDataValidationType getType() {
        return CertificateDataValidationType.SHOW_VALIDATION;
    }

    @Test
    void shouldIncludeValidationShowQuestionId() {
        final var question = getElement();
        final var certificateDataValidation = (CertificateDataValidationShow) question.getValidation()[getValidationIndex()];
        assertEquals(getQuestionId(), certificateDataValidation.getQuestionId());
    }

    @Test
    void shouldIncludeValidationShowExpression() {
        final var question = getElement();
        final var certificateDataValidation = (CertificateDataValidationShow) question.getValidation()[getValidationIndex()];
        assertEquals(getExpression(), certificateDataValidation.getExpression());
    }
}
