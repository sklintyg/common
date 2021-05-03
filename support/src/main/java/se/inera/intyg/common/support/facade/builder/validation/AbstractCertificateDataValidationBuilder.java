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

package se.inera.intyg.common.support.facade.builder.validation;

import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationType;

public abstract class AbstractCertificateDataValidationBuilder<T extends AbstractCertificateDataValidationBuilder> {

    protected CertificateDataValidationType type;
    protected String questionId;
    protected String expression;

    protected AbstractCertificateDataValidationBuilder(CertificateDataValidationType type) {
        this.type = type;
    }

    public T questionId(String questionId) {
        this.questionId = questionId;
        return (T) this;
    }

    public T expression(String expression) {
        this.expression = expression;
        return (T) this;
    }

    public abstract <T extends CertificateDataValidation> T build();
}
