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
package se.inera.intyg.common.db.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_PRINT_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_PRINT_MESSAGE_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.Message;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;

public class QuestionPrintMessagePolisanmalan {

    public static CertificateDataElement toCertificate(Boolean polisanmalan, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(POLISANMALAN_PRINT_MESSAGE_DELSVAR_ID)
            .parent(POLISANMALAN_CATEGORY_ID)
            .index(index)
            .visible(polisanmalan)
            .config(
                CertificateDataConfigMessage.builder()
                    .message(
                        Message.builder()
                            .content(texts.get(POLISANMALAN_PRINT_MESSAGE_ID))
                            .level(MessageLevel.INFO)
                            .build()
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(POLISANMALAN_DELSVAR_ID)
                        .expression(singleExpression(POLISANMALAN_JSON_ID))
                        .build()
                }
            )
            .build();
    }
}
