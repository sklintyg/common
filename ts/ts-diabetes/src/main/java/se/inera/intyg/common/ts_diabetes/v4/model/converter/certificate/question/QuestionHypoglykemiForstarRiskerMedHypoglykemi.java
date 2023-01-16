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

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SVAR_JA_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SVAR_NEJ_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;

public class QuestionHypoglykemiForstarRiskerMedHypoglykemi {

    public static CertificateDataElement toCertificate(Hypoglykemi hypoglykemi, int index, CertificateTextProvider textProvider) {
        final var forstarRisker =
            hypoglykemi != null && hypoglykemi.getForstarRiskerMedHypoglykemi() != null
                ? hypoglykemi.getForstarRiskerMedHypoglykemi() : null;
        return CertificateDataElement.builder()
            .id(HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID)
            .parent(HYPOGLYKEMI_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DELSVAR_ID)
                    .text(textProvider.get(HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_TEXT_ID))
                    .description(textProvider.get(HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_DESCRIPTION_ID))
                    .selectedText(textProvider.get(SVAR_JA_TEXT_ID))
                    .unselectedText(textProvider.get(SVAR_NEJ_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID)
                    .selected(forstarRisker)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_SVAR_ID)
                        .expression(singleExpression(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID)
                        .expression(singleExpression(HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID,
            HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID);
    }
}
