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
package se.inera.intyg.common.fkparent.model.converter.certificate;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ACCORDION_CLOSE_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ACCORDION_OPEN_TEXT;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.Accordion;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;

public abstract class AbstractQuestionFunktionsnedsattning {

    private static final short TEXT_LIMIT = (short) 3500;

    protected static CertificateDataElement toCertificate(String textValue, String questionId, String textId, String descriptionId,
        String headerId, String jsonId, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(questionId)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(textProvider.get(textId))
                    .description(textProvider.get(descriptionId))
                    .accordion(
                        Accordion.builder()
                            .openText(FUNKTIONSNEDSATTNING_ACCORDION_OPEN_TEXT)
                            .closeText(FUNKTIONSNEDSATTNING_ACCORDION_CLOSE_TEXT)
                            .header(textProvider.get(headerId))
                            .build()
                    )
                    .id(jsonId)
                    .build()
            )
            .value(
                CertificateDataValueText.builder()
                    .id(jsonId)
                    .text(textValue)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(jsonId)
                        .limit(TEXT_LIMIT)
                        .build()
                }
            )
            .build();
    }
}
