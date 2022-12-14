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
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAKARINTYG_AV_OGONSPECIALIST_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.LAKARINTYG_AV_OGONSPECIALIST_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROGRESSIV_OGONSJUKDOM_JSON_ID_5;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROGRESSIV_OGONSJUKDOM_SVAR_ID_5;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SEENDE_NEDSATT_BELYSNING_JSON_ID_4;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SEENDE_NEDSATT_BELYSNING_SVAR_ID_4;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFALTSDEFEKTER_JSON_ID_3;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFALTSDEFEKTER_SVAR_ID_3;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFUNKTIONER_CATEGORY_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;

public class QuestionLakarintygAvOgonspecialistMessage {

    public static CertificateDataElement toCertificate(int index, CertificateTextProvider texts) {

        return CertificateDataElement.builder()
            .id(LAKARINTYG_AV_OGONSPECIALIST_ID)
            .parent(SYNFUNKTIONER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigMessage.builder()
                    .message(texts.get(LAKARINTYG_AV_OGONSPECIALIST_TEXT_ID))
                    .level(MessageLevel.INFO)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(SYNFALTSDEFEKTER_SVAR_ID_3)
                        .expression(singleExpression(SYNFALTSDEFEKTER_JSON_ID_3))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(SEENDE_NEDSATT_BELYSNING_SVAR_ID_4)
                        .expression(singleExpression(SEENDE_NEDSATT_BELYSNING_JSON_ID_4))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(PROGRESSIV_OGONSJUKDOM_SVAR_ID_5)
                        .expression(singleExpression(PROGRESSIV_OGONSJUKDOM_JSON_ID_5))
                        .build()
                }
            )
            .build();
    }
}
