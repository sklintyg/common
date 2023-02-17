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

import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SELECTED_TEXT;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_TEXT_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_UNSELECTED_TEXT;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionDiagnosgrundNyBedomning {

    public static CertificateDataElement toCertificate(Boolean nyBedomning, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID)
            .parent(DIAGNOS_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID)
                    .text(textProvider.get(DIAGNOSGRUND_NY_BEDOMNING_SVAR_TEXT_ID))
                    .description(textProvider.get(DIAGNOSGRUND_NY_BEDOMNING_SVAR_DESCRIPTION_ID))
                    .selectedText(DIAGNOSGRUND_NY_BEDOMNING_SELECTED_TEXT)
                    .unselectedText(DIAGNOSGRUND_NY_BEDOMNING_UNSELECTED_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID)
                    .selected(nyBedomning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID)
                        .expression(exists(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID, DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID);
    }
}
