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
package se.inera.intyg.common.luse.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionDiagnosgrundForNyBedomning {

    private static final short LIMIT = 3500;


    public static CertificateDataElement toCertificate(String nyBedomning, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_ID)
            .parent(DIAGNOS_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_JSON_ID)
                    .text(texts.get(DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_JSON_ID)
                    .text(nyBedomning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID)
                        .expression(singleExpression(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_ID)
                        .expression(singleExpression(DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_JSON_ID))
                        .build(),
                    CertificateDataValidationText.builder()
                        .limit(LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_ID, DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_JSON_ID);
    }
}
