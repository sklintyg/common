/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHANDLING_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULINBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULIN_ELLER_TABLETT_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULIN_ELLER_TABLETT_MESSAGE_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TABLETTBEHANDLING_DELSVAR_JSON_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.Message;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;

public class QuestionTablettEllerInsulinMessage {

    public static CertificateDataElement toCertificate(Diabetes diabetes, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(INSULIN_ELLER_TABLETT_MESSAGE_ID)
            .index(index)
            .parent(HAR_DIABETES_CATEGORY_ID)
            .visible(getVisibility(diabetes))
            .config(
                CertificateDataConfigMessage.builder()
                    .message(
                        Message.builder()
                            .content(textProvider.get(INSULIN_ELLER_TABLETT_MESSAGE_TEXT_ID))
                            .level(MessageLevel.INFO)
                            .build()
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(BEHANDLING_DIABETES_SVAR_ID)
                        .expression(
                            multipleOrExpressionWithExists(TABLETTBEHANDLING_DELSVAR_JSON_ID, INSULINBEHANDLING_DELSVAR_JSON_ID)
                        )
                        .build()
                }
            )
            .build();
    }

    private static Boolean getVisibility(Diabetes diabetes) {
        if (diabetes == null) {
            return false;
        }
        return isTrue(diabetes.getInsulin()) || isTrue(diabetes.getTabletter());
    }

    private static Boolean isTrue(Boolean value) {
        return value != null ? value : false;
    }
}
