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
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_HEADER_SVAR_ID;

import java.util.Set;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.BedomningKorkortstyp;

public class QuestionBedomningKorkortstyp {

    private static final String NOT_PROVIDED = "Kan inte ta ställning";

    public static CertificateDataElement toCertificate(Bedomning bedomning, int index, CertificateTextProvider textProvider) {
        final var korkort = bedomning != null ? bedomning.getKorkortstyp() : null;
        final var korkortsTyper = getKorkortstypName(korkort);
        return CertificateDataElement.builder()
            .id(BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_ID)
            .parent(BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_HEADER_SVAR_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(textProvider.get(BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_DELSVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(korkortsTyper != null ? multipleStringValuesWithComma(korkortsTyper) : NOT_PROVIDED)
                    .build()
            )
            .build();

    }

    private static String[] getKorkortstypName(Set<BedomningKorkortstyp> korkort) {
        return korkort != null && !korkort.isEmpty() ? korkort.stream()
            .map(Enum::name)
            .toArray(String[]::new) : null;
    }
}

