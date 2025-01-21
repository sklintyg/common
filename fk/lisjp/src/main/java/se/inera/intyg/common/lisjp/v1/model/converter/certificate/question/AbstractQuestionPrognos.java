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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_TEXT;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_ATER_X_ANTAL_DAGAR;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_PROGNOS_OKLAR;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_SANNOLIKT_INTE;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_STOR_SANNOLIKHET;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.util.List;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;

public abstract class AbstractQuestionPrognos {

    public static CertificateDataElement toCertificate(
        QuestionPrognosConfigProvider configProvider,
        String questionId, String parent,
        int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(questionId)
            .index(index)
            .parent(parent)
            .config(
                CertificateDataConfigRadioMultipleCodeOptionalDropdown.builder()
                    .text(texts.get(PROGNOS_SVAR_TEXT))
                    .description(texts.get(PROGNOS_SVAR_BESKRIVNING))
                    .list(
                        List.of(
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.MED_STOR_SANNOLIKHET.getId())
                                .label(texts.get(PROGNOS_SVAR_STOR_SANNOLIKHET))
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.ATER_X_ANTAL_DGR.getId())
                                .label(texts.get(PROGNOS_SVAR_ATER_X_ANTAL_DAGAR))
                                .dropdownQuestionId(PROGNOS_BESKRIVNING_DELSVAR_ID_39)
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId())
                                .label(texts.get(PROGNOS_SVAR_SANNOLIKT_INTE))
                                .build(),
                            RadioMultipleCodeOptionalDropdown.builder()
                                .id(PrognosTyp.PROGNOS_OKLAR.getId())
                                .label(texts.get(PROGNOS_SVAR_PROGNOS_OKLAR))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCode.builder()
                    .id(getPrognosValue(configProvider.getValue()))
                    .code(getPrognosValue(configProvider.getValue()))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(questionId)
                        .expression(
                            multipleOrExpressionWithExists(
                                PrognosTyp.MED_STOR_SANNOLIKHET.getId(),
                                PrognosTyp.ATER_X_ANTAL_DGR.getId(),
                                PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId(),
                                PrognosTyp.PROGNOS_OKLAR.getId()
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

    private static String getPrognosValue(QuestionPrognosValue value) {
        return (value != null && value.getId() != null) ? value.getId() : null;
    }


    public static class QuestionPrognosConfigProvider {

        private final QuestionPrognosValue value;

        public QuestionPrognosConfigProvider(QuestionPrognosValue value) {
            this.value = value;
        }

        public QuestionPrognosValue getValue() {
            return value;
        }
    }

    public static class QuestionPrognosValue {

        private final String id;

        public QuestionPrognosValue(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
