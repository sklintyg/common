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
package se.inera.intyg.common.luse.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.SUBSTANSINTAG_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.SUBSTANSINTAG_SVAR_TEXT;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigHeader;

public class QuestionMedicinskBehandlingSubstansintagHeader {

    public static CertificateDataElement toCertificate(int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(SUBSTANSINTAG_SVAR_ID)
            .parent(MEDICINSKABEHANDLINGAR_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigHeader.builder()
                    .text(texts.get(SUBSTANSINTAG_SVAR_TEXT))
                    .build()
            )
            .build();
    }
}
