/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.support.facade.util;

import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

public final class CertificateValidationFactory {

    public static CertificateDataValidationMandatory mandatory(String id, String expression) {
       return CertificateDataValidationMandatory.builder()
                        .questionId(id)
                        .expression(expression)
                        .build();
    }

    public static CertificateDataValidationMandatory mandatory(String id, CertificateConfig config) {
        return CertificateDataValidationMandatory.builder()
                .questionId(id)
                .expression(singleExpression(config.getElement(id).getValueId()))
                .build();
    }

    public static CertificateDataValidationShow show(String id, String expression) {
        return CertificateDataValidationShow.builder()
                .questionId(id)
                .expression(expression)
                .build();
    }

    public static CertificateDataValidationShow showIfParent(String id, CertificateConfig config) {
        final var parentId = config.getElement(id).getParent();
        final var parent = config.getElement(parentId);
        return CertificateDataValidationShow.builder()
                .questionId(parentId)
                .expression(singleExpression(parent.getValueId()))
                .build();
    }
}
