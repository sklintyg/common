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

import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANSWER_NO;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_DIAGNOS_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionOnskaFormedlaDiagnos {

    public static CertificateDataElement toCertificate(Boolean formedlaDiagnos, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID)
            .parent(CATEGORY_DIAGNOS_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID)
                    .text(textProvider.get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_TEXT_ID))
                    .description(textProvider.get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_DESCRIPTION_ID))
                    .selectedText(ANSWER_YES)
                    .unselectedText(ANSWER_NO)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID)
                    .selected(formedlaDiagnos)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID)
                        .expression(singleExpression(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID, ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID);
    }
}
