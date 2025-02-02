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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_BEDOMNING_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_TEXT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.from;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.moreThanOrEqual;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.subtract;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.to;

import java.time.temporal.ChronoUnit;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.Message;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;

public class QuestionSjukskrivningsperiodMessage {

    private static final long DATE_RANGE_LIMIT = 14;

    public static CertificateDataElement toCertificate(InternalLocalDateInterval sjukskrivningsperiod, int index,
        CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_ID)
            .parent(CATEGORY_BEDOMNING_ID)
            .index(index)
            .visible(moreThanOrEqualFourteenDays(sjukskrivningsperiod))
            .config(
                CertificateDataConfigMessage.builder()
                    .message(
                        Message.builder()
                            .content(textProvider.get(SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_TEXT_ID))
                            .level(MessageLevel.INFO)
                            .build()
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID)
                        .expression(
                            moreThanOrEqual(subtract(to(SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID), from(SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID)),
                                DATE_RANGE_LIMIT)
                        )
                        .build()
                }
            )
            .build();
    }

    private static Boolean moreThanOrEqualFourteenDays(InternalLocalDateInterval sjukskrivningsperiod) {
        if (sjukskrivningsperiod == null || sjukskrivningsperiod.getFrom() == null || sjukskrivningsperiod.getTom() == null) {
            return false;
        }

        return ChronoUnit.DAYS.between(sjukskrivningsperiod.fromAsLocalDate(), sjukskrivningsperiod.tomAsLocalDate()) >= DATE_RANGE_LIMIT;
    }
}
