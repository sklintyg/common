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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.multipleStringValuesWithComma;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INTYG_AVSER_SVAR_TEXT_ID;

import java.util.Set;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.ts_bas.v6.model.internal.IntygAvser;
import se.inera.intyg.common.ts_bas.v6.model.internal.IntygAvserKategori;

public class QuestionIntygetAvser {

    public static CertificateDataElement toCertificate(IntygAvser intygAvser, int index,
        CertificateTextProvider texts) {
        final var korkort = intygAvser != null && intygAvser.getKorkortstyp() != null ? intygAvser.getKorkortstyp() : null;
        final var korkortsTyper = getKorkortstypName(korkort);

        return CertificateDataElement.builder()
            .id(INTYG_AVSER_SVAR_ID_1)
            .parent(INTYG_AVSER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(texts.get(INTYG_AVSER_SVAR_TEXT_ID))
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
