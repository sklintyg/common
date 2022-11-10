/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_QUESTION_DESCRIPTION_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.codeValue;

import java.util.Arrays;
import se.inera.intyg.common.doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;

public class QuestionForgiftningOrsak {

    public static CertificateDataElement toCertificate(ForgiftningOrsak forgiftningOrsak, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FORGIFTNING_ORSAK_DELSVAR_ID)
            .parent(FORGIFTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(FORGIFTNING_ORSAK_QUESTION_TEXT_ID))
                    .description(texts.get(FORGIFTNING_ORSAK_QUESTION_DESCRIPTION_ID))
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id(ForgiftningOrsak.OLYCKSFALL.name())
                                .label(ForgiftningOrsak.OLYCKSFALL.getBeskrivning())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(ForgiftningOrsak.SJALVMORD.name())
                                .label(ForgiftningOrsak.SJALVMORD.getBeskrivning())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(ForgiftningOrsak.AVSIKTLIGT_VALLAD.name())
                                .label(ForgiftningOrsak.AVSIKTLIGT_VALLAD.getBeskrivning())
                                .build(),
                            RadioMultipleCode.builder()
                                .id(ForgiftningOrsak.OKLART.name())
                                .label(ForgiftningOrsak.OKLART.getBeskrivning())
                                .build()
                        )
                    )
                    .build()
            )
            .value(
                forgiftningOrsak != null ? CertificateDataValueCode.builder()
                    .id(forgiftningOrsak.name())
                    .code(forgiftningOrsak.name())
                    .build() : CertificateDataValueCode.builder().build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FORGIFTNING_ORSAK_DELSVAR_ID)
                        .expression(
                            multipleOrExpression(
                                singleExpression(ForgiftningOrsak.OLYCKSFALL.name()),
                                singleExpression(ForgiftningOrsak.SJALVMORD.name()),
                                singleExpression(ForgiftningOrsak.AVSIKTLIGT_VALLAD.name()),
                                singleExpression(ForgiftningOrsak.OKLART.name())
                            )
                        )
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(FORGIFTNING_OM_DELSVAR_ID)
                        .expression(singleExpression(FORGIFTNING_OM_JSON_ID))
                        .build(),
                }
            )
            .build();
    }

    public static ForgiftningOrsak toInternal(Certificate certificate) {
        final var codeValueString = codeValue(certificate.getData(), FORGIFTNING_ORSAK_DELSVAR_ID);
        if (codeValueString == null) {
            return null;
        }
        return ForgiftningOrsak.valueOf(codeValueString);
    }
}
