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
package se.inera.intyg.common.af00213.v1.model.converter.certificate;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_DESCRIPTION;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_TEXT;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;

public class QuestionAktivitetsbegransning {

    public static CertificateDataElement toCertificate(String aktivitetsbegransning, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_DELSVAR_ID_22)
            .parent(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_TEXT))
                    .description(texts.get(AKTIVITETSBEGRANSNING_DELSVAR_DESCRIPTION))
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22)
                    .build()
            )
            .value(
                CertificateDataValueText.builder()
                    .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22)
                    .text(aktivitetsbegransning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_22)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(AKTIVITETSBEGRANSNING_DELSVAR_ID_21)
                        .expression(singleExpression(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), AKTIVITETSBEGRANSNING_DELSVAR_ID_22, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22);
    }
}
