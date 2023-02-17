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

package se.inera.intyg.common.ag7804.v1.model.converter.certificate.question;


import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_FUNKTIONSNEDSATTNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_TEXT;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionFunktionsnedsattning {

    public static CertificateDataElement toCertificate(String value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_SVAR_ID_35)
            .index(index)
            .parent(CATEGORY_FUNKTIONSNEDSATTNING)
            .config(
                CertificateDataConfigTextArea.builder()
                    .header(texts.get(FUNKTIONSNEDSATTNING_SVAR_TEXT))
                    .text(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_TEXT))
                    .description(texts.get(FUNKTIONSNEDSATTNING_DELSVAR_BESKRIVNING))
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FUNKTIONSNEDSATTNING_SVAR_ID_35)
                        .expression(
                            singleExpression(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(exists(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), FUNKTIONSNEDSATTNING_SVAR_ID_35, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35);
    }
}
