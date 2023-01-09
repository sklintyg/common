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
package se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KANNEDOM_SVAR_JSON_ID_2;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KANNEDOM_SVAR_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.dateValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDate;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionKannedomOmPatient {

    private static final short LIMIT = 0;

    public static CertificateDataElement toCertificate(InternalDate kannedomOmPatient, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(KANNEDOM_SVAR_ID_2)
            .parent(GRUNDFORMU_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigDate.builder()
                    .id(KANNEDOM_SVAR_JSON_ID_2)
                    .text(texts.get(KANNEDOM_SVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueDate.builder()
                    .id(KANNEDOM_SVAR_JSON_ID_2)
                    .date(validDate(kannedomOmPatient) ? kannedomOmPatient.asLocalDate() : null)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(KANNEDOM_SVAR_ID_2)
                        .expression(singleExpression(KANNEDOM_SVAR_JSON_ID_2))
                        .build(),
                    CertificateDataValidationMaxDate.builder()
                        .id(KANNEDOM_SVAR_JSON_ID_2)
                        .numberOfDays(LIMIT)
                        .build()
                }
            )
            .build();
    }

    private static boolean validDate(InternalDate date) {
        return date != null && date.isValidDate();
    }

    public static InternalDate toInternal(Certificate certificate) {
        final var localDate = dateValue(certificate.getData(), KANNEDOM_SVAR_ID_2, KANNEDOM_SVAR_JSON_ID_2);
        return localDate != null ? new InternalDate(localDate) : null;
    }
}
