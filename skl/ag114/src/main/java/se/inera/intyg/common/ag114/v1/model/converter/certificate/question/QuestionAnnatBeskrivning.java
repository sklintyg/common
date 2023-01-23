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

import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionAnnatBeskrivning {

    private static final short LIMIT = 3500;

    public static CertificateDataElement toCertificate(String annatBeskrivning, int index, CertificateTextProvider texts) {

        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID)
            .parent(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID)
            .index(index)
            .config(
                CertificateDataConfigTextField.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID)
                    .text(texts.get(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID)
                    .text(annatBeskrivning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID)
                        .expression(singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID)
                        .expression(singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID))
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID)
                        .limit(LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID,
            GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_JSON_ID);
    }
}
