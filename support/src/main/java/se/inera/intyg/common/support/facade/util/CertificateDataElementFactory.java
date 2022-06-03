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
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTypes;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;

public final class CertificateDataElementFactory {

    public static CertificateDataElement element(String id, CertificateConfig config, CertificateDataValidation[] validations, Object value) {
        if (config.getElement(id).getConfigType() == CertificateDataConfigTypes.CATEGORY) {
            return category(id, config);
        } else {
            return question(id, config, validations, value);
        }
    }

    public static CertificateDataElement element(String id, CertificateConfig config) {
        return element(id, config, null, null);
    }

    private static CertificateDataElement category(String id, CertificateConfig config) {
        final var element = config.getElement(id);
        return CertificateDataElement.builder()
                .id(id)
                .index(element.getIndex())
                .config(
                        CertificateDataConfigCategory.builder()
                                .text(CertificateTextFactory.categoryText(config.getTextProvider(), element.getTextId()))
                                .description(CertificateTextFactory.categoryDescription(config.getTextProvider(), element.getTextId()))
                                .build()
                )
                .build();
    }

    private static CertificateDataElement question(String id, CertificateConfig config, CertificateDataValidation[] validations, Object value) {
        final var element = config.getElement(id);
        switch (element.getConfigType()) {
            case UE_TEXTAREA:
                return CertificateQuestionFactory.textarea(id, config, validations, (String) value);
            case UE_RADIO_BOOLEAN:
                return CertificateQuestionFactory.radioBoolean(id, config, validations, (Boolean) value);
            default:
                return null;
        }
    }
}
