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
package se.inera.intyg.common.luse.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.BAKGRUND_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_JSON_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.util.ValueToolkit;

public class QuestionSjukdomsforlopp {

    private static final short TEXT_LIMIT = 3500;

    public static CertificateDataElement toCertificate(String textValue, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(SJUKDOMSFORLOPP_SVAR_ID)
            .parent(BAKGRUND_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(SJUKDOMSFORLOPP_SVAR_JSON_ID)
                    .text(textProvider.get(SJUKDOMSFORLOPP_SVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(SJUKDOMSFORLOPP_SVAR_JSON_ID)
                    .text(textValue)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(SJUKDOMSFORLOPP_SVAR_JSON_ID)
                        .limit(TEXT_LIMIT)
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(SJUKDOMSFORLOPP_SVAR_ID)
                        .expression(singleExpression(SJUKDOMSFORLOPP_SVAR_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return ValueToolkit.textValue(certificate.getData(), SJUKDOMSFORLOPP_SVAR_ID, SJUKDOMSFORLOPP_SVAR_JSON_ID);
    }
}
