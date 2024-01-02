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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_DAGAR_180;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_DAGAR_30;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_DAGAR_365;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_DAGAR_60;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_DAGAR_90;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpressionWithExists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.util.Arrays;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.CertificateDataElementStyleEnum;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigDropdown;
import se.inera.intyg.common.support.facade.model.config.DropdownItem;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationEnable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationHide;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;

public abstract class AbstractQuestionPrognosTimePeriod {

    public static CertificateDataElement toCertificate(QuestionPrognosTimePeriodConfigProvider configProvider, String questionId,
        String parent, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(questionId)
            .index(index)
            .parent(parent)
            .config(
                CertificateDataConfigDropdown.builder()
                    .list(
                        Arrays.asList(
                            DropdownItem.builder()
                                .id("")
                                .label("Välj tidsperiod")
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_30.getId())
                                .label(texts.get(PROGNOS_DAGAR_30))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_60.getId())
                                .label(texts.get(PROGNOS_DAGAR_60))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_90.getId())
                                .label(texts.get(PROGNOS_DAGAR_90))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_180.getId())
                                .label(texts.get(PROGNOS_DAGAR_180))
                                .build(),
                            DropdownItem.builder()
                                .id(PrognosDagarTillArbeteTyp.DAGAR_365.getId())
                                .label(texts.get(PROGNOS_DAGAR_365))
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                CertificateDataValueCode.builder()
                    .id(getPrognosDagarTillArbeteValue(configProvider.getValue()))
                    .code(getPrognosDagarTillArbeteValue(configProvider.getValue()))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationHide.builder()
                        .questionId(AVSTANGNING_SMITTSKYDD_SVAR_ID_27)
                        .expression(singleExpression(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(questionId)
                        .expression(
                            multipleOrExpressionWithExists(
                                PrognosDagarTillArbeteTyp.DAGAR_30.getId(),
                                PrognosDagarTillArbeteTyp.DAGAR_60.getId(),
                                PrognosDagarTillArbeteTyp.DAGAR_90.getId(),
                                PrognosDagarTillArbeteTyp.DAGAR_180.getId(),
                                PrognosDagarTillArbeteTyp.DAGAR_365.getId()
                            )
                        )
                        .build(),
                    CertificateDataValidationEnable.builder()
                        .questionId(PROGNOS_SVAR_ID_39)
                        .expression(exists(PrognosTyp.ATER_X_ANTAL_DGR.getId()))
                        .build()
                }
            )
            .style(CertificateDataElementStyleEnum.HIDDEN)
            .build();
    }

    private static String getPrognosDagarTillArbeteValue(QuestionPrognosTimePeriodValue timePeriodValue) {
        return timePeriodValue != null ? timePeriodValue.getId() : null;
    }

    public static class QuestionPrognosTimePeriodConfigProvider {

        private final QuestionPrognosTimePeriodValue value;

        public QuestionPrognosTimePeriodConfigProvider(QuestionPrognosTimePeriodValue value) {
            this.value = value;
        }

        public QuestionPrognosTimePeriodValue getValue() {
            return value;
        }
    }

    public static class QuestionPrognosTimePeriodValue {

        private final String id;

        public QuestionPrognosTimePeriodValue(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
