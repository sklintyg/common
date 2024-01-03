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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_HEADER_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_FROM_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_TOM_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateRangeValue;

import java.time.LocalDate;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDateRange;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;

public class QuestionSjukskrivningsperiod {

    public static CertificateDataElement toCertificate(InternalLocalDateInterval sjukskrivningsPeriod, int index,
        CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID)
            .index(index)
            .parent(SJUKSKRIVNINGSGRAD_HEADER_ID)
            .config(
                CertificateDataConfigDateRange.builder()
                    .id(SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID)
                    .text(textProvider.get(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_TEXT_ID))
                    .fromLabel(textProvider.get(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_FROM_ID))
                    .toLabel(textProvider.get(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_LABEL_TOM_ID))
                    .build()
            )
            .value(
                CertificateDataValueDateRange.builder()
                    .id(SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID)
                    .from(sjukskrivningsPeriod != null ? sjukskrivningsPeriod.fromAsLocalDate()
                        : LocalDate.now())
                    .to(sjukskrivningsPeriod != null ? sjukskrivningsPeriod.tomAsLocalDate()
                        : null)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID)
                        .expression(singleExpression(SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static InternalLocalDateInterval toInternal(Certificate certificate) {
        return dateRangeValue(certificate.getData(), SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID, SJUKSKRIVNINGSGRAD_PERIOD_JSON_ID);
    }
}
