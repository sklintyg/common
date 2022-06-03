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

import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfig;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;

public final class CertificateQuestionFactory {

    private static CertificateDataElement element(String id, CertificateConfig certificateConfig, CertificateDataConfig dataElementConfig, CertificateDataValue value, CertificateDataValidation[] validations) {
        final var element = certificateConfig.getElement(id);
        return CertificateDataElement.builder()
                .id(id)
                .parent(element.getParent())
                .index(element.getIndex())
                .config(
                        dataElementConfig
                )
                .value(value)
                .validation(validations)
                .build();
    }

    public static CertificateDataElement textarea(String id, CertificateConfig config, CertificateDataValidation[] validations, String value) {
        final var elementConfig = CertificateDataElementConfigFactory.textarea(id, config.getElement(id).getTextId(), config.getTextProvider());
        final var elementValue = CertificateDataElementValueFactory.textarea(id, value);
        return element(id, config, elementConfig, elementValue, validations);
    }

    public static CertificateDataElement radioBoolean(String id, CertificateConfig config, CertificateDataValidation[] validations, Boolean value) {
        final var elementConfig = CertificateDataElementConfigFactory.createBoolean(id, config.getElement(id).getTextId(), config.getTextProvider());
        final var elementValue = CertificateDataElementValueFactory.createBoolean(id, value);
        return element(id, config, elementConfig, elementValue, validations);
    }
}
