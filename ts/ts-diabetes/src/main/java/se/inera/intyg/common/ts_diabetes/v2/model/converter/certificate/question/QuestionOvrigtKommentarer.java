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

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.stringValue;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.OVRIGT_KOMMENTARER_SVAR_ID;

import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;

public class QuestionOvrigtKommentarer {

    public static CertificateDataElement toCertificate(String kommentarer, int index) {
        return CertificateDataElement.builder()
            .id(OVRIGT_KOMMENTARER_SVAR_ID)
            .parent(OVRIGT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(stringValue(kommentarer))
                    .build()
            )
            .build();
    }
}
