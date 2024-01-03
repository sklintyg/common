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

package se.inera.intyg.common.fk7263.model.converter.certificate.question;

import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ANNAT_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.ANNAT_DELSVAR_TEXT;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.INTYGET_BASERAS_PA_CATEGORY_ID;
import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.stringValue;

import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.support.model.InternalDate;

public class QuestionIntygetBaserasPaAnnat {

    public static CertificateDataElement toCertificate(InternalDate annat, int index) {
        final var date = validDate(annat) ? annat.toString() : null;
        return CertificateDataElement.builder()
            .id(ANNAT_DELSVAR_ID)
            .parent(INTYGET_BASERAS_PA_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(ANNAT_DELSVAR_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(stringValue(date))
                    .build()
            )
            .build();
    }

    private static boolean validDate(InternalDate date) {
        return date != null && date.isValidDate();
    }
}
