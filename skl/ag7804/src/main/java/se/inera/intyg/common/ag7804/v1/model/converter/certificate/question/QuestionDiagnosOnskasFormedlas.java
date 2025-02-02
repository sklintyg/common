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

package se.inera.intyg.common.ag7804.v1.model.converter.certificate.question;

import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_DIAGNOS;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NO_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.YES_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;

import java.util.Arrays;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHighlight;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;

public class QuestionDiagnosOnskasFormedlas {

    public static CertificateDataElement toCertificate(Boolean value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
            .index(index)
            .parent(CATEGORY_DIAGNOS)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(ONSKAR_FORMEDLA_DIAGNOS_TEXT))
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id(YES_ID)
                                .label(ANSWER_YES)
                                .build(),
                            RadioMultipleCode.builder()
                                .id(NO_ID)
                                .label(ANSWER_NO)
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCode.builder()
                    .id(convertRadioBooleanToCode(value))
                    .code(convertRadioBooleanToCode(value))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
                        .expression(multipleOrExpressionWithExists(YES_ID, NO_ID))
                        .build(),
                    CertificateDataValidationHighlight.builder()
                        .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100)
                        .expression("1")
                        .build(),
                }
            )
            .build();
    }

    private static String convertRadioBooleanToCode(Boolean value) {
        if (value == null) {
            return null;
        } else if (value) {
            return YES_ID;
        } else {
            return NO_ID;
        }
    }

    public static Boolean toInternal(Certificate certificate) {
        final var code = codeValue(certificate.getData(), ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100);
        if (code == null) {
            return null;
        }
        return code.equals(YES_ID);
    }
}
