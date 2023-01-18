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

import static se.inera.intyg.common.support.facade.util.PatientToolkit.birthYear;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.yearValue;
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
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueYear;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.schemas.contract.Personnummer;

public class QuestionDiabetesDiagnosAr {

    private static final String CURRENT_YEAR = String.valueOf(LocalDate.now().getYear());

    public static CertificateDataElement toCertificate(Allmant allmant, Personnummer patientId, int index,
        CertificateTextProvider textProvider) {
        final var diagnosAr = allmant != null ? allmant.getDiabetesDiagnosAr() : null;
        return CertificateDataElement.builder()
            .id(ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID)
            .parent(ALLMANT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigYear.builder()
                    .id(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID)
                    .text(textProvider.get(ALLMANT_DIABETES_DIAGNOS_AR_TEXT_ID))
                    .maxYear(CURRENT_YEAR)
                    .minYear(String.valueOf(birthYear(patientId)))
                    .build()
            )
            .value(
                CertificateDataValueYear.builder()
                    .id(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID)
                    .year(diagnosAr)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID)
                        .expression(singleExpression(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return yearValue(certificate.getData(), ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID, ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID);
    }
}
