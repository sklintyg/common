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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHANDLING_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.BEHANDLING_DIABETES_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULINBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.INSULINBEHANDLING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KOSTBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.KOSTBEHANDLING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TABLETTBEHANDLING_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TABLETTBEHANDLING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_ID;

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
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;

public class QuestionDiabetesBehandling {

    public static CertificateDataElement toCertificate(Diabetes diabetes, int index, CertificateTextProvider texts) {
        final var insulin = diabetes != null && diabetes.getInsulin() != null ? diabetes.getInsulin() : null;
        final var kost = diabetes != null && diabetes.getKost() != null ? diabetes.getKost() : null;
        final var tabletter = diabetes != null && diabetes.getTabletter() != null ? diabetes.getTabletter() : null;
        return CertificateDataElement.builder()
            .id(BEHANDLING_DIABETES_SVAR_ID)
            .parent(HAR_DIABETES_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(BEHANDLING_DIABETES_SVAR_TEXT_ID))
                    .list(
                        List.of(
                            CheckboxMultipleCode.builder()
                                .id(KOSTBEHANDLING_DELSVAR_JSON_ID)
                                .label(texts.get(KOSTBEHANDLING_DELSVAR_TEXT_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(TABLETTBEHANDLING_DELSVAR_JSON_ID)
                                .label(texts.get(TABLETTBEHANDLING_DELSVAR_TEXT_ID))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(INSULINBEHANDLING_DELSVAR_JSON_ID)
                                .label(texts.get(INSULINBEHANDLING_DELSVAR_TEXT_ID))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(
                        getValues(kost, tabletter, insulin)
                    )
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(BEHANDLING_DIABETES_SVAR_ID)
                        .expression(
                            multipleOrExpression(
                                KOSTBEHANDLING_DELSVAR_JSON_ID, TABLETTBEHANDLING_DELSVAR_JSON_ID, INSULINBEHANDLING_DELSVAR_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(TYP_AV_DIABETES_SVAR_ID)
                        .expression(singleExpression(DiabetesKod.DIABETES_TYP_2.name()))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueCode> getValues(Boolean kost, Boolean tabletter, Boolean insulin) {
        List<CertificateDataValueCode> certificateDataValueCodes = new ArrayList<>();
        if (kost != null && kost) {
            certificateDataValueCodes.add(
                CertificateDataValueCode.builder()
                    .id(KOSTBEHANDLING_DELSVAR_JSON_ID)
                    .code(KOSTBEHANDLING_DELSVAR_JSON_ID)
                    .build());
        }
        if (tabletter != null && tabletter) {
            certificateDataValueCodes.add(CertificateDataValueCode.builder()
                .id(TABLETTBEHANDLING_DELSVAR_JSON_ID)
                .code(TABLETTBEHANDLING_DELSVAR_JSON_ID)
                .build());
        }
        if (insulin != null && insulin) {
            certificateDataValueCodes.add(
                CertificateDataValueCode.builder()
                    .id(INSULINBEHANDLING_DELSVAR_JSON_ID)
                    .code(INSULINBEHANDLING_DELSVAR_JSON_ID)
                    .build());
        }
        return certificateDataValueCodes;
    }

    public static Boolean toInternal(Certificate certificate, String id) {
        final var value = codeListValue(certificate.getData(), BEHANDLING_DIABETES_SVAR_ID);
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
