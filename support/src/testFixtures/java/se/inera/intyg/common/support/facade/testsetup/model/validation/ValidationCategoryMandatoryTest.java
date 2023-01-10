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

package se.inera.intyg.common.support.facade.testsetup.model.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationCategoryMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;
import se.inera.intyg.common.support.facade.model.validation.ExpressionTypeEnum;

public abstract class ValidationCategoryMandatoryTest extends ValidationTest {

    protected abstract ExpressionTypeEnum getExpressionType();

    protected abstract List<CertificateDataValidationMandatory> getListOfQuestions();

    @Override
    protected CertificateDataValidationType getType() {
        return CertificateDataValidationType.CATEGORY_MANDATORY_VALIDATION;
    }

    @Test
    void shouldIncludeListOfQuestions() {
        final var element = getElement();
        final var validation = (CertificateDataValidationCategoryMandatory) element.getValidation()[getValidationIndex()];
        assertIterableEquals(validation.getQuestions(), getListOfQuestions());
    }

    @Test
    void shouldIncludeExpressionType() {
        final var element = getElement();
        final var validation = (CertificateDataValidationCategoryMandatory) element.getValidation()[getValidationIndex()];
        assertEquals(validation.getExpressionType(), getExpressionType());
    }
}
