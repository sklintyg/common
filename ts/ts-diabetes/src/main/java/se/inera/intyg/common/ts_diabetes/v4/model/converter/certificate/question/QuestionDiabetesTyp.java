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

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_ANNAN_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_LADA_LABEL_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_TYP1_LABEL_ID;

import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.Layout;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;

public class QuestionDiabetesTyp {

    public static CertificateDataElement toCertificate(Allmant allmant, int index, CertificateTextProvider texts) {
        final var diabetesTyp = allmant != null && allmant.getTypAvDiabetes() != null ? allmant.getTypAvDiabetes() : null;
        return CertificateDataElement.builder()
            .id(ALLMANT_TYP_AV_DIABETES_SVAR_ID)
            .index(index)
            .parent(ALLMANT_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(ALLMANT_TYP_AV_DIABETES_TEXT_ID))
                    .layout(Layout.ROWS)
                    .list(
                        List.of(
                            RadioMultipleCode.builder()
                                .id(KvTypAvDiabetes.TYP1.getCode())
                                .label(texts.get(ALLMANT_TYP_AV_DIABETES_TYP1_LABEL_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(KvTypAvDiabetes.TYP2.getCode())
                                .label(texts.get(ALLMANT_TYP_AV_DIABETES_TYP1_LABEL_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(KvTypAvDiabetes.LADA.getCode())
                                .label(texts.get(ALLMANT_TYP_AV_DIABETES_LADA_LABEL_ID))
                                .build(),
                            RadioMultipleCode.builder()
                                .id(KvTypAvDiabetes.ANNAN.getCode())
                                .label(texts.get(ALLMANT_TYP_AV_DIABETES_ANNAN_LABEL_ID))
                                .build())
                    )
                    .build()
            )
            .value(
                diabetesTyp != null
                    ? CertificateDataValueCode.builder()
                    .id(diabetesTyp.getCode())
                    .code(diabetesTyp.getCode())
                    .build() : CertificateDataValueCode.builder().build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ALLMANT_TYP_AV_DIABETES_SVAR_ID)
                        .expression(
                            multipleOrExpression(
                                KvTypAvDiabetes.TYP1.getCode(),
                                KvTypAvDiabetes.TYP2.getCode(),
                                KvTypAvDiabetes.LADA.getCode(),
                                KvTypAvDiabetes.ANNAN.getCode()
                            )
                        )
                        .build()
                }
            )
            .build();
    }

    public static KvTypAvDiabetes toInternal(Certificate certificate) {
        final var codeValue = codeValue(certificate.getData(), ALLMANT_TYP_AV_DIABETES_SVAR_ID);
        if (codeValue == null || codeValue.isEmpty()) {
            return null;
        }
        return KvTypAvDiabetes.fromCode(codeValue);
    }
}
