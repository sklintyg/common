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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;

public class QuestionDiabetesBehandling {

    public static CertificateDataElement toCertificate(Allmant allmant, int index, CertificateTextProvider texts) {
        final var behandling = allmant != null && allmant.getBehandling() != null ? allmant.getBehandling() : null;
        final var insulin = behandling != null && behandling.getInsulin() != null ? behandling.getInsulin() : null;
        final var tabletter = behandling != null && behandling.getTabletter() != null ? behandling.getTabletter() : null;
        final var annan = behandling != null && behandling.getAnnan() != null ? behandling.getAnnan() : null;

        return CertificateDataElement.builder()
            .id(ALLMANT_BEHANDLING_SVAR_ID)
            .parent(ALLMANT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(ALLMANT_BEHANDLING_TEXT_ID))
                    .list(
                        List.of(
                            CheckboxMultipleCode.builder()
                                .id(ALLMANT_BEHANDLING_INSULIN_JSON_ID)
                                .label(texts.get(ALLMANT_BEHANDLING_INSULIN_TEXT_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ALLMANT_BEHANDLING_TABLETTER_JSON_ID)
                                .label(texts.get(ALLMANT_BEHANDLING_TABLETTER_TEXT_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ALLMANT_BEHANDLING_ANNAN_JSON_ID)
                                .label(texts.get(ALLMANT_BEHANDLING_ANNAN_TEXT_ID))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(
                        getValues(insulin, tabletter, annan)
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ALLMANT_BEHANDLING_SVAR_ID)
                        .expression(
                            multipleOrExpressionWithExists(
                                ALLMANT_BEHANDLING_INSULIN_JSON_ID,
                                ALLMANT_BEHANDLING_TABLETTER_JSON_ID,
                                ALLMANT_BEHANDLING_ANNAN_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID)
                        .expression(singleExpression(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueCode> getValues(Boolean insulin, Boolean tabletter, Boolean annan) {
        List<CertificateDataValueCode> certificateDataValueCodes = new ArrayList<>();
        if (insulin != null && insulin) {
            certificateDataValueCodes.add(
                CertificateDataValueCode.builder()
                    .id(ALLMANT_BEHANDLING_INSULIN_JSON_ID)
                    .code(ALLMANT_BEHANDLING_INSULIN_JSON_ID)
                    .build());
        }
        if (tabletter != null && tabletter) {
            certificateDataValueCodes.add(CertificateDataValueCode.builder()
                .id(ALLMANT_BEHANDLING_TABLETTER_JSON_ID)
                .code(ALLMANT_BEHANDLING_TABLETTER_JSON_ID)
                .build());
        }
        if (annan != null && annan) {
            certificateDataValueCodes.add(
                CertificateDataValueCode.builder()
                    .id(ALLMANT_BEHANDLING_ANNAN_JSON_ID)
                    .code(ALLMANT_BEHANDLING_ANNAN_JSON_ID)
                    .build());
        }
        return certificateDataValueCodes;
    }

    public static Boolean toInternal(Certificate certificate, String id) {
        final var value = codeListValue(certificate.getData(), ALLMANT_BEHANDLING_SVAR_ID);
        if (value == null || value.isEmpty()) {
            return false;
        }

        for (CertificateDataValueCode code : value) {
            if (code.getId() != null && code.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
}
