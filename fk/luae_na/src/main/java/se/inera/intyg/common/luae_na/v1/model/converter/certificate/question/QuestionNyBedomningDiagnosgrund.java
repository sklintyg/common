/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT_JA;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT_NEJ;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.NYDIAGNOS_SVAR_ID_45;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.util.ValueToolkit;

public class QuestionNyBedomningDiagnosgrund {

    public static CertificateDataElement toCertificate(Boolean value, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .index(index)
            .id(NYDIAGNOS_SVAR_ID_45)
            .parent(DIAGNOS_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45)
                    .text(textProvider.get(DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT))
                    .description(textProvider.get(DIAGNOSGRUND_NYBEDOMNING_SVAR_BESKRIVNING))
                    .selectedText(textProvider.get(DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT_JA))
                    .unselectedText(textProvider.get(DIAGNOSGRUND_NYBEDOMNING_SVAR_TEXT_NEJ))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45)
                    .selected(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45)
                        .expression(singleExpression(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return ValueToolkit.booleanValue(certificate.getData(),NYDIAGNOS_SVAR_ID_45, DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45);
    }
}
