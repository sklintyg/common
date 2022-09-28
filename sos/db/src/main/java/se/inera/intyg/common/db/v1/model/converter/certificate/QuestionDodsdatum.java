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

package se.inera.intyg.common.db.v1.model.converter.certificate;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateValue;

import java.time.LocalDate;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.model.InternalDate;


public class QuestionDodsdatum {

    public static final short NUMBER_OF_DAYS_IN_FUTURE = (short) 0;

    public static CertificateDataElement toCertificate(LocalDate dodsdatum, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DODSDATUM_DELSVAR_ID)
            .parent(DODSDATUM_SAKERT_DELSVAR_ID)
            .index(index)
            .config(
                CertificateDataConfigDate.builder()
                    .text(texts.get(DODSDATUM_QUESTION_TEXT_ID))
                    .id(DODSDATUM_JSON_ID)
                    .build()
            )
            .value(
                CertificateDataValueDate.builder()
                    .id(DODSDATUM_JSON_ID)
                    .date(dodsdatum)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DODSDATUM_DELSVAR_ID)
                        .expression(singleExpression(DODSDATUM_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(DODSDATUM_SAKERT_DELSVAR_ID)
                        .expression(singleExpression(DODSDATUM_SAKERT_JSON_ID))
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(DODSDATUM_JSON_ID)
                        .numberOfDays(NUMBER_OF_DAYS_IN_FUTURE)
                        .build()
                }
            )
            .build();
    }

    public static InternalDate toInternal(Certificate certificate) {
        final var localDate = dateValue(certificate.getData(), DODSDATUM_DELSVAR_ID, DODSDATUM_JSON_ID);
        return localDate != null ? new InternalDate(localDate) : null;
    }
}
