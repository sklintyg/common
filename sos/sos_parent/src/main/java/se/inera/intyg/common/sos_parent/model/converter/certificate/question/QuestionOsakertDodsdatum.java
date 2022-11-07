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

package se.inera.intyg.common.sos_parent.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_OSAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.not;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.uncertainDateValue;

import java.time.Year;
import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigUncertainDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataUncertainDateValue;
import se.inera.intyg.common.support.model.InternalDate;


public class QuestionOsakertDodsdatum {

    public static CertificateDataElement toCertificate(String value, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DODSDATUM_OSAKERT_DELSVAR_ID)
            .parent(DODSDATUM_SAKERT_DELSVAR_ID)
            .index(index)
            .visible(value != null)
            .config(
                CertificateDataConfigUncertainDate.builder()
                    .id(DODSDATUM_JSON_ID)
                    .allowedYears(List.of(
                        String.valueOf(Year.now()),
                        String.valueOf(Year.now().minusYears(1))))
                    .unknownYear(true)
                    .unknownMonth(true)
                    .build()
            )
            .value(
                CertificateDataUncertainDateValue.builder()
                    .id(DODSDATUM_JSON_ID)
                    .value(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DODSDATUM_OSAKERT_DELSVAR_ID)
                        .expression(singleExpression(DODSDATUM_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(DODSDATUM_SAKERT_DELSVAR_ID)
                        .expression(not(singleExpression(DODSDATUM_SAKERT_JSON_ID)))
                        .build()
                }
            )
            .build();
    }

    public static InternalDate toInternal(Certificate certificate) {
        final var textValue = uncertainDateValue(certificate.getData(), DODSDATUM_OSAKERT_DELSVAR_ID, DODSDATUM_JSON_ID);
        if (textValue == null || textValue.isEmpty()) {
            return null;
        }
        return new InternalDate(textValue);
    }
}
