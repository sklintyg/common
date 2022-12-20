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

package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_JA_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_NEJ_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID_24;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionSomnOchVakenhetsstorningar {

    public static CertificateDataElement toCertificate(Boolean somnOchVakenhetsstorningar, int index, CertificateTextProvider textProvider) {

        return CertificateDataElement.builder()
            .index(index)
            .id(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID_24)
            .parent(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_JSON_ID)
                    .text(textProvider.get(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_TEXT_ID))
                    .selectedText(SVAR_JA_TEXT)
                    .unselectedText(SVAR_NEJ_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_JSON_ID)
                    .selected(somnOchVakenhetsstorningar)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID_24)
                        .expression(singleExpression(TECKEN_SOMN_ELLER_VAKENHETSSTORNING_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), TECKEN_SOMN_ELLER_VAKENHETSSTORNING_SVAR_ID_24, TECKEN_SOMN_ELLER_VAKENHETSSTORNING_JSON_ID);
    }
}
