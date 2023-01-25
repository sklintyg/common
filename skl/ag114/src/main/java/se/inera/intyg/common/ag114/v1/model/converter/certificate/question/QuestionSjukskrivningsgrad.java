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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_HEADER_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_TEXT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_UNIT_OF_MEASURE;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.integerValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigInteger;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueInteger;

public class QuestionSjukskrivningsgrad {

    private static final Integer MIN = 0;
    private static final Integer MAX = 100;

    public static CertificateDataElement toCertificate(String sjukskrivningsgrad, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(SJUKSKRIVNINGSGRAD_SVAR_ID)
            .parent(SJUKSKRIVNINGSGRAD_HEADER_ID)
            .index(index)
            .config(
                CertificateDataConfigInteger.builder()
                    .id(SJUKSKRIVNINGSGRAD_SVAR_JSON_ID)
                    .text(textProvider.get(SJUKSKRIVNINGSGRAD_SVAR_TEXT_ID))
                    .unit(SJUKSKRIVNINGSGRAD_UNIT_OF_MEASURE)
                    .min(MIN)
                    .max(MAX)
                    .build()
            )
            .value(
                CertificateDataValueInteger.builder()
                    .id(SJUKSKRIVNINGSGRAD_SVAR_JSON_ID)
                    .value(isNumeric(sjukskrivningsgrad) ? Integer.valueOf(sjukskrivningsgrad) : null)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(SJUKSKRIVNINGSGRAD_SVAR_ID)
                        .expression(singleExpression(SJUKSKRIVNINGSGRAD_SVAR_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    private static boolean isNumeric(String stringNumber) {
        if (stringNumber == null || stringNumber.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(stringNumber);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }

    public static String toInternal(Certificate certificate) {
        return integerValue(certificate.getData(), SJUKSKRIVNINGSGRAD_SVAR_ID, SJUKSKRIVNINGSGRAD_SVAR_JSON_ID);
    }
}
