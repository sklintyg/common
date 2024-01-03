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

package se.inera.intyg.common.ts_diabetes.v2.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.multipleStringValuesWithComma;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;

import java.util.Set;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvserKategori;

public class QuestionIntygetAvser {

    public static CertificateDataElement toCertificate(IntygAvser intygetAvser, int index) {
        final var korkort = intygetAvser != null ? intygetAvser.getKorkortstyp() : null;
        final var korkortsTyper = getKorkortstypName(korkort);
        return CertificateDataElement.builder()
            .id(INTYG_AVSER_SVAR_ID)
            .parent(INTYG_AVSER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(multipleStringValuesWithComma(korkortsTyper))
                    .build()
            )
            .build();

    }

    private static String[] getKorkortstypName(Set<IntygAvserKategori> korkort) {
        return korkort != null ? korkort.stream()
            .map(Enum::name)
            .toArray(String[]::new) : null;
    }
}
