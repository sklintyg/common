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

package se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.stringValue;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Allmant;

public class QuestionDiabetesTyp {

    private static final String DIABETES_TYP_1 = "TYP1";
    private static final String DIABETES_TYP_1_RESPONSE = "Typ 1";
    private static final String DIABETES_TYP_2 = "TYP2";
    private static final String DIABETES_TYP_2_RESPONSE = "Typ 2";
    private static final String ANNAN = "ANNAN";
    private static final String ANNAN_RESPONSE = "Annan";

    public static CertificateDataElement toCertificate(Allmant allmant, int index, CertificateTextProvider textProvider) {
        final var diabetesTyp = allmant != null && allmant.getTypAvDiabetes() != null ? allmant.getTypAvDiabetes().name() : null;
        return CertificateDataElement.builder()
            .id(ALLMANT_TYP_AV_DIABETES_SVAR_ID)
            .parent(ALLMANT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(textProvider.get(ALLMANT_TYP_AV_DIABETES_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(stringValue(getDiabetesKod(diabetesTyp)))
                    .build()
            )
            .build();
    }

    private static String getDiabetesKod(String diabetesTyp) {
        if (diabetesTyp == null) {
            return null;
        }
        switch (diabetesTyp) {
            case DIABETES_TYP_1:
                return DIABETES_TYP_1_RESPONSE;
            case DIABETES_TYP_2:
                return DIABETES_TYP_2_RESPONSE;
            case ANNAN:
                return ANNAN_RESPONSE;
            default:
                return null;
        }
    }
}
