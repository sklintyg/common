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

package se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.multipleStringValuesWithComma;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_BEHANDLING_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_CATEGORY_ID;

import java.util.ArrayList;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;

public class QuestionDiabetesBehandling {

    private static final String KOST = "Endast kost";
    private static final String TABLETTER = "Tabletter";
    private static final String INSULIN = "Insulin";

    public static CertificateDataElement toCertificate(Boolean kost, Boolean tabletter, Boolean insulin, int index,
        CertificateTextProvider textProvider) {
        final var behandling = getBehandling(kost, tabletter, insulin);
        return CertificateDataElement.builder()
            .id(ALLMANT_BEHANDLING_SVAR_ID)
            .parent(ALLMANT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(textProvider.get(ALLMANT_BEHANDLING_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(multipleStringValuesWithComma(behandling))
                    .build()
            )
            .build();
    }

    private static String[] getBehandling(Boolean kost, Boolean tabletter, Boolean insulin) {
        if (kost == null && tabletter == null && insulin == null) {
            return null;
        }

        final var behandlingList = new ArrayList<String>();

        if (kost != null && kost) {
            behandlingList.add(KOST);
        }
        if (tabletter != null && tabletter) {
            behandlingList.add(TABLETTER);
        }
        if (insulin != null && insulin) {
            behandlingList.add(INSULIN);
        }

        final var behandling = new String[behandlingList.size()];
        return behandlingList.toArray(behandling);
    }
}
