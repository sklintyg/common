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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_TEXT;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeListValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
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

public abstract class AbstractQuestionAtgarder {

    public static CertificateDataElement toCertificate(QuestionAtgarderConfigProvider valueManager, String questionId, String parent,
        int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(questionId)
            .index(index)
            .parent(parent)
            .config(
                CertificateDataConfigCheckboxMultipleCode.builder()
                    .text(texts.get(ARBETSLIVSINRIKTADE_ATGARDER_TEXT))
                    .list(
                        valueManager.getCheckboxMultipleCodes()
                    )
                    .build()
            )
            .value(
                CertificateDataValueCodeList.builder()
                    .list(createAtgarderCodeList(valueManager))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(questionId)
                        .expression(
                            multipleOrExpressionWithExists(
                                valueManager.getMandatoryValidation()
                            )
                        )
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
                        .questionId(questionId)
                        .expression(
                            multipleOrExpressionWithExists(
                                valueManager.getDisableValidation()
                            )
                        )
                        .id(Collections.singletonList(valueManager.getNotCurrentId()))
                        .build(),
                    CertificateDataValidationDisableSubElement.builder()
                        .questionId(questionId)
                        .expression(
                            exists(valueManager.getNotCurrentId())
                        )
                        .id(
                            Arrays.asList(
                                valueManager.getDisableValidation()
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

    private static List<CertificateDataValueCode> createAtgarderCodeList(QuestionAtgarderConfigProvider valueManager) {
        if (valueManager.getValues() == null) {
            return Collections.emptyList();
        }

        return valueManager.getValues().stream()
            .map(atgard -> CertificateDataValueCode.builder()
                .id(atgard.getId())
                .code(atgard.getId())
                .build())
            .collect(Collectors.toList());
    }

    public static List<ArbetslivsinriktadeAtgarder> toInternal(Certificate certificate, String questionId) {
        var codeList = codeListValue(certificate.getData(), questionId);
        return codeList
            .stream()
            .map(
                code -> ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.fromId(code.getId()))
            )
            .collect(Collectors.toList());
    }

    public static class QuestionAtgarderConfigProvider {

        private final List<CheckboxMultipleCode> checkboxMultipleCodes;

        private final String[] mandatoryValidation;
        private final String[] disableValidation;

        private final String notCurrentId;

        private final List<QuestionAtgarderValueProvider> values;


        QuestionAtgarderConfigProvider(List<CheckboxMultipleCode> listOfArbetslivsinriktadeAtgarderVal, String[] mandatoryValidation,
            String[] disableValidation, List<QuestionAtgarderValueProvider> values, String notCurrentId) {
            this.checkboxMultipleCodes = listOfArbetslivsinriktadeAtgarderVal;
            this.mandatoryValidation = mandatoryValidation;
            this.disableValidation = disableValidation;
            this.values = values;
            this.notCurrentId = notCurrentId;
        }

        public String getNotCurrentId() {
            return notCurrentId;
        }

        public List<CheckboxMultipleCode> getCheckboxMultipleCodes() {
            return checkboxMultipleCodes;
        }

        public String[] getMandatoryValidation() {
            return mandatoryValidation;
        }

        public String[] getDisableValidation() {
            return disableValidation;
        }

        public List<QuestionAtgarderValueProvider> getValues() {
            return values;
        }
    }

    static class QuestionAtgarderValueProvider {

        private final String id;

        QuestionAtgarderValueProvider(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
