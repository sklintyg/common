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

package se.inera.intyg.common.ag7804.v1.model.converter.certificate.question;


import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_SYSSELSATTNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_ARBETE;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_ARBETSSOKANDE;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_FORALDRALEDIG;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_STUDIER;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_SVAR_BESKRIVNING;
import static se.inera.intyg.common.ag7804.converter.RespConstants.SYSSELSATTNING_SVAR_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.AbstractQuestionSysselsattning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;

public class QuestionSysselsattning extends AbstractQuestionSysselsattning {

    public static CertificateDataElement toCertificate(List<Sysselsattning> value, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
            .index(index)
            .parent(CATEGORY_SYSSELSATTNING)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(SYSSELSATTNING_SVAR_TEXT))
                    .description(texts.get(SYSSELSATTNING_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                                .label(texts.get(SYSSELSATTNING_ARBETE))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.ARBETSSOKANDE.getId())
                                .label(texts.get(SYSSELSATTNING_ARBETSSOKANDE))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId())
                                .label(texts.get(SYSSELSATTNING_FORALDRALEDIG))
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(SysselsattningsTyp.STUDIER.getId())
                                .label(texts.get(SYSSELSATTNING_STUDIER))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(createSysselsattningCodeList(value))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(TYP_AV_SYSSELSATTNING_SVAR_ID_28)
                        .expression(
                            multipleOrExpressionWithExists(
                                SysselsattningsTyp.NUVARANDE_ARBETE.getId(),
                                SysselsattningsTyp.ARBETSSOKANDE.getId(),
                                SysselsattningsTyp.FORADLRARLEDIGHET_VARD_AV_BARN.getId(),
                                SysselsattningsTyp.STUDIER.getId()
                            )
                        )
                        .build(),
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build()
                }
            )
            .build();
    }

    private static List<CertificateDataValueCode> createSysselsattningCodeList(List<Sysselsattning> value) {
        if (value == null) {
            return Collections.emptyList();
        }

        return value.stream()
            .map(sysselsattning -> CertificateDataValueCode.builder()
                .id(Objects.requireNonNull(sysselsattning.getTyp()).getId())
                .code(sysselsattning.getTyp().getId())
                .build())
            .collect(Collectors.toList());
    }

    public static List<Sysselsattning> toInternal(Certificate certificate) {
        var codeList = codeListValue(certificate.getData(), TYP_AV_SYSSELSATTNING_SVAR_ID_28);
        return codeList
            .stream()
            .map(
                code -> Sysselsattning.create(
                    Sysselsattning.SysselsattningsTyp.fromId(code.getId()))
            )
            .collect(Collectors.toList());
    }
}
