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
package se.inera.intyg.common.luse.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_DESCRIPTION_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigHeader;

public class QuestionAktivitetsbegransningarHeader {

    public static CertificateDataElement toCertificate(int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(AKTIVITETSBEGRANSNING_SVAR_ID)
            .parent(AKTIVITETSBEGRANSNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigHeader.builder()
                    .text(textProvider.get(AKTIVITETSBEGRANSNING_SVAR_TEXT_ID))
                    .description(textProvider.get(AKTIVITETSBEGRANSNING_SVAR_DESCRIPTION_ID))
                    .build()
            )
            .build();
    }
}
