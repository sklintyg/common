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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;

public class QuestionDiabetesBehandlingAnnan {

    private static final short TEXT_LIMIT = 53;

    public static CertificateDataElement toCertificate(Allmant allmant, int index, CertificateTextProvider textProvider) {
        final var behandling = allmant != null && allmant.getBehandling() != null ? allmant.getBehandling() : null;
        final var annanAngeVilken = behandling != null ? behandling.getAnnanAngeVilken() : null;
        return CertificateDataElement.builder()
            .id(ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID)
            .parent(ALLMANT_BEHANDLING_SVAR_ID)
            .index(index)
            .config(
                CertificateDataConfigTextField.builder()
                    .text(textProvider.get(ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_TEXT_ID))
                    .id(ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID)
                    .build()
            )
            .value(
                CertificateDataValueText.builder()
                    .id(ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID)
                    .text(annanAngeVilken)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID)
                        .limit(TEXT_LIMIT)
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID)
                        .expression(singleExpression(ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(ALLMANT_BEHANDLING_SVAR_ID)
                        .expression(singleExpression(ALLMANT_BEHANDLING_ANNAN_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_DELSVAR_ID,
            ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID);
    }
}
