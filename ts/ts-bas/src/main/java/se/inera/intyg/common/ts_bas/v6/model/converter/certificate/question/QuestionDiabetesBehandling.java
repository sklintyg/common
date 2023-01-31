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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.multipleStringValues;
import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.multipleStringValuesWithComma;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.BEHANDLING_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.BEHANDLING_DIABETES_SVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HAR_DIABETES_CATEGORY_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.ts_bas.v6.model.internal.Diabetes;

public class QuestionDiabetesBehandling {

    private static final String NOT_PROVIDED = "Ej Angivet";

    public static CertificateDataElement toCertificate(Diabetes diabetes, int index, CertificateTextProvider texts) {
        final var insulin = diabetes != null && diabetes.getInsulin() != null ? diabetes.getInsulin() : null;
        final var kost = diabetes != null && diabetes.getKost() != null ? diabetes.getKost() : null;
        final var tabletter = diabetes != null && diabetes.getTabletter() != null ? diabetes.getTabletter() : null;
        return CertificateDataElement.builder()
            .id(BEHANDLING_DIABETES_SVAR_ID)
            .parent(HAR_DIABETES_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(texts.get(BEHANDLING_DIABETES_SVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(
                        multipleStringValues(kostToString(kost), tabletterToString(tabletter), insulinToString(insulin)).equals(
                            NOT_PROVIDED)
                            ? multipleStringValues(kostToString(kost), tabletterToString(tabletter), insulinToString(insulin))
                            : multipleStringValues(
                                multipleStringValuesWithComma(kostToString(kost), tabletterToString(tabletter), insulinToString(insulin)))
                    )
                    .build()
            )
            .build();
    }

    private static String kostToString(Boolean kost) {
        if (kost == null || !kost) {
            return null;
        }
        return "Kost";
    }

    private static String insulinToString(Boolean insulin) {
        if (insulin == null || !insulin) {
            return null;
        }
        return "Insulin";
    }

    private static String tabletterToString(Boolean tabletter) {
        if (tabletter == null || !tabletter) {
            return null;
        }
        return "Tabletter";
    }
}
