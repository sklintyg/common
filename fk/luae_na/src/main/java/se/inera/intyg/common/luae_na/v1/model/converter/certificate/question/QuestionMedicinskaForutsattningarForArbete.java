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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;

public class QuestionMedicinskaForutsattningarForArbete {

    public static CertificateDataElement toCertificate(String medicinskaForutsattningarForArbete, int index,
        CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22)
            .parent(CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(textProvider.get(MEDICINSKAFORUTSATTNINGARFORARBETE_TEXT_ID))
                    .description(textProvider.get(MEDICINSKAFORUTSATTNINGARFORARBETE_DESCRIPTION_ID))
                    .id(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22)
                    .build()
            )
            .value(
                CertificateDataValueText.builder()
                    .id(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22)
                    .text(medicinskaForutsattningarForArbete)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22)
                        .expression(singleExpression(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22,
            MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22);
    }
}
