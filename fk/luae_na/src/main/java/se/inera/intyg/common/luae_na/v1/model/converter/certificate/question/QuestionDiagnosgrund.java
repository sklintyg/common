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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_SVAR_ID_7;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_SVAR_JSON_ID_7;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.support.facade.util.ValueToolkit;

public class QuestionDiagnosgrund {

    private static final short TEXT_LIMIT = 3500;

    public static CertificateDataElement toCertificate(String diagnosgrund, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .index(index)
            .id(DIAGNOSGRUND_SVAR_ID_7)
            .parent(DIAGNOS_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(DIAGNOSGRUND_SVAR_JSON_ID_7)
                    .text(texts.get(DIAGNOSGRUND_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueText.builder()
                    .id(DIAGNOSGRUND_SVAR_JSON_ID_7)
                    .text(diagnosgrund)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(DIAGNOSGRUND_SVAR_JSON_ID_7)
                        .limit(TEXT_LIMIT)
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(DIAGNOSGRUND_SVAR_ID_7)
                        .expression(singleExpression(DIAGNOSGRUND_SVAR_JSON_ID_7))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return ValueToolkit.textValue(certificate.getData(), DIAGNOSGRUND_SVAR_ID_7, DIAGNOSGRUND_SVAR_JSON_ID_7);
    }
}
