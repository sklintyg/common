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

package se.inera.intyg.common.luae_na.v1.model.converter.certificate.category;

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import java.util.List;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCategory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationCategoryMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.ExpressionTypeEnum;

public class CategoryFunktionsnedsattning {

    public static CertificateDataElement toCertificate(int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCategory.builder()
                    .text(textProvider.get(FUNKTIONSNEDSATTNING_CATEGORY_TEXT_ID))
                    .description(textProvider.get(FUNKTIONSNEDSATTNING_CATEGORY_DESCRIPTION_ID))
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationCategoryMandatory.builder()
                        .expressionType(ExpressionTypeEnum.OR)
                        .questions(
                            List.of(
                                CertificateDataValidationMandatory.builder()
                                    .questionId(FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8)
                                    .expression(singleExpression(FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID_8))
                                    .build(),
                                CertificateDataValidationMandatory.builder()
                                    .questionId(FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9)
                                    .expression(singleExpression(FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID_9))
                                    .build(),
                                CertificateDataValidationMandatory.builder()
                                    .questionId(FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10)
                                    .expression(singleExpression(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID_10))
                                    .build(),
                                CertificateDataValidationMandatory.builder()
                                    .questionId(FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11)
                                    .expression(singleExpression(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11))
                                    .build(),
                                CertificateDataValidationMandatory.builder()
                                    .questionId(FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12)
                                    .expression(singleExpression(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12))
                                    .build(),
                                CertificateDataValidationMandatory.builder()
                                    .questionId(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13)
                                    .expression(singleExpression(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID_13))
                                    .build(),
                                CertificateDataValidationMandatory.builder()
                                    .questionId(FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14)
                                    .expression(singleExpression(FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID_14))
                                    .build()
                            )
                        )
                        .build()
                }
            )
            .build();
    }
}
