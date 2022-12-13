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

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NYUPPDATERING_SVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.NYDIAGNOS_SVAR_ID_45;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.util.ValueToolkit;

public class QuestionDiagnosForNyBedomning {


    public static CertificateDataElement toCertificate(String nyBedomning, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .index(index)
            .id(DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45)
            .parent(NYDIAGNOS_SVAR_ID_45)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45)
                    .text(texts.get(DIAGNOSGRUND_NYUPPDATERING_SVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45)
                    .text(nyBedomning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_45)
                        .expression(singleExpression(DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45)
                        .expression(singleExpression(DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return ValueToolkit.textValue(
            certificate.getData(),DIAGNOS_FOR_NY_BEDOMNING_DELSVAR_ID_45, DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45);
    }
}
