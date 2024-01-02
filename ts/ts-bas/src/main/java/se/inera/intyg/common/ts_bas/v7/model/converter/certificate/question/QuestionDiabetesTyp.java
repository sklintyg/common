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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.HAR_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_TYP1_LABEL_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TYP_AV_DIABETES_SVAR_TYP2_LABEL_ID;

import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_parent.codes.DiabetesKod;

public class QuestionDiabetesTyp {

    public static CertificateDataElement toCertificate(Diabetes diabetes, int index, CertificateTextProvider texts) {
        final var diabetesTyp = diabetes != null && diabetes.getDiabetesTyp() != null ? diabetes.getDiabetesTyp() : null;
        return CertificateDataElement.builder()
            .id(TYP_AV_DIABETES_SVAR_ID)
            .index(index)
            .parent(HAR_DIABETES_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(TYP_AV_DIABETES_SVAR_TEXT_ID))
                    .layout(Layout.ROWS)
                    .list(
                        List.of(
                            RadioMultipleCode.builder()
                                .id(DiabetesKod.DIABETES_TYP_1.name())
                                .label(texts.get(TYP_AV_DIABETES_SVAR_TYP1_LABEL_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(DiabetesKod.DIABETES_TYP_2.name())
                                .label(texts.get(TYP_AV_DIABETES_SVAR_TYP2_LABEL_ID))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                diabetesTyp != null
                    ? CertificateDataValueCode.builder()
                    .id(diabetesTyp)
                    .code(diabetesTyp)
                    .build() : CertificateDataValueCode.builder().build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(TYP_AV_DIABETES_SVAR_ID)
                        .expression(
                            multipleOrExpressionWithExists(DiabetesKod.DIABETES_TYP_1.name(), DiabetesKod.DIABETES_TYP_2.name())
                        )
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(HAR_DIABETES_SVAR_ID)
                        .expression(singleExpression(HAR_DIABETES_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return codeValue(certificate.getData(), TYP_AV_DIABETES_SVAR_ID);
    }
}
