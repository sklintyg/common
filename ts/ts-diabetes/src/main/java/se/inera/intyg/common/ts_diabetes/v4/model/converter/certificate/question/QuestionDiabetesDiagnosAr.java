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

package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_TEXT_ID;

import java.time.LocalDate;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigYear;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMaxYear;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMinYear;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;

public class QuestionDiabetesDiagnosAr {

    private static final short YEAR_LIMIT = 0;
    private static final int[] SUBSTRING_INDEX = {0, 4};

    public static CertificateDataElement toCertificate(Allmant allmant, String patientId, int index, CertificateTextProvider textProvider) {
        final var diagnosAr = allmant != null ? allmant.getDiabetesDiagnosAr() : null;
        return CertificateDataElement.builder()
            .id(ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID)
            .parent(ALLMANT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigYear.builder()
                    .id(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID)
                    .text(textProvider.get(ALLMANT_DIABETES_DIAGNOS_AR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID)
                    .text(diagnosAr)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID)
                        .expression(singleExpression(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID))
                        .build(),
                    CertificateDataValidationMaxYear.builder()
                        .id(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID)
                        .numberOfYears(YEAR_LIMIT)
                        .build(),
                    CertificateDataValidationMinYear.builder()
                        .id(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID)
                        .numberOfYears(getNumberOfYears(patientId))
                        .build()
                }
            )
            .build();

    }

    private static short getNumberOfYears(String patientId) {
        if (patientId == null) {
            return 0;
        }
        return (short) (Short.parseShort(String.valueOf(LocalDate.now().getYear())) - Short.parseShort(
            patientId.substring(SUBSTRING_INDEX[0], SUBSTRING_INDEX[1])));
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID, ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID);
    }
}
