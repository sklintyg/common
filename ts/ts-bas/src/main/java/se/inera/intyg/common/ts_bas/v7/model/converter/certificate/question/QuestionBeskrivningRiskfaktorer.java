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

package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HJART_ELLER_KARLSJUKDOM_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.RISKFAKTORER_STROKE_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.RISKFAKTORER_STROKE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_JSON_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.ts_bas.v7.model.internal.HjartKarl;

public class QuestionBeskrivningRiskfaktorer {

    public static CertificateDataElement toCertificate(HjartKarl hjartKarl, int index, CertificateTextProvider textProvider) {
        final var hjartKarlBeskrivningRiskfaktorer =
            hjartKarl != null && hjartKarl.getBeskrivningRiskfaktorer() != null ? hjartKarl.getBeskrivningRiskfaktorer() : null;
        return CertificateDataElement.builder()
            .id(TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID)
            .parent(HJART_ELLER_KARLSJUKDOM_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_JSON_ID)
                    .text(textProvider.get(TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_JSON_ID)
                    .text(hjartKarlBeskrivningRiskfaktorer)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID)
                        .expression(singleExpression(TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(RISKFAKTORER_STROKE_SVAR_ID)
                        .expression(singleExpression(RISKFAKTORER_STROKE_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_DELSVAR_ID,
            TYP_AV_SJUKDOM_RISKFAKTORER_STROKE_JSON_ID);
    }
}
