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
package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_QUESTION_SELECTED_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_QUESTION_UNKNOWN_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_QUESTION_UNSELECTED_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;

import java.util.Arrays;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;

public class QuestionOperation {

    public static CertificateDataElement toCertificate(OmOperation omOperation, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(OPERATION_OM_DELSVAR_ID)
            .parent(OPERATION_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(OPERATION_QUESTION_TEXT_ID))
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id(OmOperation.JA.name())
                                .label(texts.get(OPERATION_QUESTION_SELECTED_TEXT_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(OmOperation.NEJ.name())
                                .label(texts.get(OPERATION_QUESTION_UNSELECTED_TEXT_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(OmOperation.UPPGIFT_SAKNAS.name())
                                .label(texts.get(OPERATION_QUESTION_UNKNOWN_TEXT_ID))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                omOperation != null ? CertificateDataValueCode.builder()
                    .id(omOperation.name())
                    .code(omOperation.name())
                    .build() : CertificateDataValueCode.builder().build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(OPERATION_OM_DELSVAR_ID)
                        .expression(
                            multipleOrExpressionWithExists(
                                OmOperation.JA.name(),
                                OmOperation.NEJ.name(),
                                OmOperation.UPPGIFT_SAKNAS.name()
                            )
                        )
                        .build()
                }
            )
            .build();
    }

    public static OmOperation toInternal(Certificate certificate) {
        final var codeValueString = codeValue(certificate.getData(), OPERATION_OM_DELSVAR_ID);
        if (codeValueString == null) {
            return null;
        }
        return OmOperation.valueOf(codeValueString);
    }

}
