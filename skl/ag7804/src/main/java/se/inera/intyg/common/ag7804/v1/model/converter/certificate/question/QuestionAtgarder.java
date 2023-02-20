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


import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_TEXT;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.CATEGORY_ATGARDER;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.v1.model.converter.certificate.question.AbstractQuestionAtgarder;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.config.CheckboxMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisableSubElement;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;

public class QuestionAtgarder extends AbstractQuestionAtgarder {

    public static CertificateDataElement toCertificate(List<ArbetslivsinriktadeAtgarder> atgarder, int index,
        CertificateTextProvider texts) {
        final var questionAtgarderValueManager = new QuestionAtgarderConfigProvider(
            getCheckboxMultipleCodeList(),
            getMandatoryValidationIds(),
            getDisableAndSubElementValidationIds(),
            getArbetslivsinriktadeAtgarderValue(atgarder),
            ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId());
        return toCertificate(questionAtgarderValueManager, ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, CATEGORY_ATGARDER, index, texts);
    }


    public static CertificateDataElement toCertificates(List<ArbetslivsinriktadeAtgarder> atgarder,
        int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
            .index(index)
            .parent(CATEGORY_ATGARDER)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(ARBETSLIVSINRIKTADE_ATGARDER_TEXT))
                    .list(
                        Arrays.asList(
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getLabel())
                                .build(),
                            CheckboxMultipleCode.builder()
                                .id(ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                                .label(ArbetslivsinriktadeAtgarderVal.OVRIGT.getLabel())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(createAtgarderCodeList(atgarder))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpressionWithExists(
                                ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId(),
                                ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId(),
                                ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId(),
                                ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId(),
                                ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId(),
                                ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId(),
                                ArbetslivsinriktadeAtgarderVal.OVRIGT.getId()
                            )
                        )
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            multipleOrExpressionWithExists(
                                ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId(),
                                ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId(),
                                ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId(),
                                ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId(),
                                ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId(),
                                ArbetslivsinriktadeAtgarderVal.OVRIGT.getId()
                            )
                        )
                        .id(Collections.singletonList(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId()))
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
                        .questionId(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40)
                        .expression(
                            exists(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT.getId())
                        )
                        .id(
                            Arrays.asList(
                                ArbetslivsinriktadeAtgarderVal.ARBETSTRANING.getId(),
                                ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN.getId(),
                                ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING.getId(),
                                ArbetslivsinriktadeAtgarderVal.HJALPMEDEL.getId(),
                                ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD.getId(),
                                ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER.getId(),
                                ArbetslivsinriktadeAtgarderVal.OVRIGT.getId()
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

    private static List<CertificateDataValueCode> createAtgarderCodeList(List<ArbetslivsinriktadeAtgarder> atgarder) {
        if (atgarder == null) {
            return Collections.emptyList();
        }

        return atgarder.stream()
            .map(atgard -> CertificateDataValueCode.builder()
                .id(atgard.getTyp().getId())
                .code(atgard.getTyp().getId())
                .build())
            .collect(Collectors.toList());
    }

    public static List<ArbetslivsinriktadeAtgarder> toInternal(Certificate certificate) {
        var codeList = codeListValue(certificate.getData(), ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
        return codeList
            .stream()
            .map(
                code -> ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.fromId(code.getId()))
            )
            .collect(Collectors.toList());
    }
}
