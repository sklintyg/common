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

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.multipleStringValuesWithComma;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_CATEGORY_ID;

import java.util.ArrayList;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.Allmant;

public class QuestionDiabetesBehandling {

    private static final String KOST = "Endast kost";
    private static final String TABLETTER = "Tabletter";
    private static final String INSULIN = "Insulin";
    private static final String ANNAN = "Annan behandling";

    public static CertificateDataElement toCertificate(Allmant allmant, int index, CertificateTextProvider textProvider) {
        final var behandling = allmant != null && allmant.getBehandling() != null ? allmant.getBehandling() : null;
        final var insulin = behandling != null && behandling.getInsulin() != null ? behandling.getInsulin() : null;
        final var kost = behandling != null && behandling.getEndastKost() != null ? behandling.getEndastKost() : null;
        final var tabletter = behandling != null && behandling.getTabletter() != null ? behandling.getTabletter() : null;
        final var annan = behandling != null && behandling.getAnnanBehandling() != null ? behandling.getAnnanBehandling() : null;
        final var behandlingar = getBehandling(kost, tabletter, insulin, annan);
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
                    .text(multipleStringValuesWithComma(behandlingar))
                    .build()
            )
            .build();
    }

    private static String[] getBehandling(Boolean kost, Boolean tabletter, Boolean insulin, Boolean annan) {
        if (kost == null && tabletter == null && insulin == null && annan == null) {
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

        if (annan != null && annan) {
            behandlingList.add(ANNAN);
        }

        final var behandling = new String[behandlingList.size()];
        return behandlingList.toArray(behandling);
    }
}
