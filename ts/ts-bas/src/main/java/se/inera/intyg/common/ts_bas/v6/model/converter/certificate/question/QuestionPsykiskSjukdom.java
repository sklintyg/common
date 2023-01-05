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

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.booleanValue;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PSYKISK_SJUKDOM_STORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PSYKISK_SJUKDOM_STORNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.PSYKISK_SJUKDOM_STORNING_DELSVAR_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.ts_bas.v6.model.internal.Psykiskt;

public class QuestionPsykiskSjukdom {

    public static CertificateDataElement toCertificate(Psykiskt psykiskt, int index, CertificateTextProvider textProvider) {

        final var psykiskSjukdom = psykiskt != null ? psykiskt.getPsykiskSjukdom() : null;

        return CertificateDataElement.builder()
            .id(PSYKISK_SJUKDOM_STORNING_DELSVAR_ID)
            .parent(PSYKISK_SJUKDOM_STORNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(textProvider.get(PSYKISK_SJUKDOM_STORNING_DELSVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(booleanValue(psykiskSjukdom))
                    .build()
            )
            .build();
    }
}
