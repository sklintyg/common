/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAD_DOD_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleAndExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.not;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.withCitation;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateValue;

import java.time.LocalDate;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.model.InternalDate;


public class QuestionAntraffadDod {

    public static CertificateDataElement toCertificate(LocalDate antraffadDodDate, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ANTRAFFAT_DOD_DATUM_DELSVAR_ID)
            .parent(DODSDATUM_SAKERT_DELSVAR_ID)
            .index(index)
            .config(
                CertificateDataConfigDate.builder()
                    .text(texts.get(ANTRAFFAD_DOD_QUESTION_TEXT_ID))
                    .maxDate(LocalDate.now())
                    .id(ANTRAFFAT_DOD_DATUM_JSON_ID)
                    .build()
            )
            .value(
                CertificateDataValueDate.builder()
                    .id(ANTRAFFAT_DOD_DATUM_JSON_ID)
                    .date(antraffadDodDate)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ANTRAFFAT_DOD_DATUM_DELSVAR_ID)
                        .expression(singleExpression(ANTRAFFAT_DOD_DATUM_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(DODSDATUM_SAKERT_DELSVAR_ID)
                        .expression(
                            multipleAndExpression(
                                exists(withCitation(DODSDATUM_SAKERT_JSON_ID)),
                                not(withCitation(DODSDATUM_SAKERT_JSON_ID))
                            )
                        )
                        .build()
                }
            )
            .build();
    }

    public static InternalDate toInternal(Certificate certificate) {
        final var localDate = dateValue(certificate.getData(), ANTRAFFAT_DOD_DATUM_DELSVAR_ID, ANTRAFFAT_DOD_DATUM_JSON_ID);
        if (localDate == null) {
            return null;
        }
        return new InternalDate(localDate);
    }
}
