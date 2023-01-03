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

import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_DELSVAR_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionOperationAnledning {

    public static final short LIMIT = (short) 31;

    public static CertificateDataElement toCertificate(String anledningOperation, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(OPERATION_ANLEDNING_DELSVAR_ID)
            .parent(OPERATION_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextField.builder()
                    .text(texts.get(OPERATION_ANLEDNING_QUESTION_TEXT_ID))
                    .id(OPERATION_ANLEDNING_JSON_ID)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(OPERATION_ANLEDNING_JSON_ID)
                    .text(anledningOperation)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(OPERATION_ANLEDNING_DELSVAR_ID)
                        .expression(singleExpression(OPERATION_ANLEDNING_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(OPERATION_OM_DELSVAR_ID)
                        .expression(singleExpression(OmOperation.JA.name()))
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(OPERATION_ANLEDNING_JSON_ID)
                        .limit(LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), OPERATION_ANLEDNING_DELSVAR_ID, OPERATION_ANLEDNING_JSON_ID);
    }
}
