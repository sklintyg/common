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

package se.inera.intyg.common.fk7263.model.converter.certificate.question;

import static se.inera.intyg.common.fk7263.model.converter.RespConstants.REKOMMENDATIONER_CATEGORY_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.REKOMMENDATIONER_KONTAKT_MED_FHV_DELSVAR_ID;
import static se.inera.intyg.common.fk7263.model.converter.RespConstants.REKOMMENDATIONER_KONTAKT_MED_FHV_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.radioBooleanValue;

import se.inera.intyg.common.services.messages.CertificateMessagesProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;

public class QuestionRekommendationerKontaktMedFhv {

    public static CertificateDataElement toCertificate(Boolean kontaktMedFhv, int index, CertificateMessagesProvider messagesProvider) {
        return CertificateDataElement.builder()
            .id(REKOMMENDATIONER_KONTAKT_MED_FHV_DELSVAR_ID)
            .parent(REKOMMENDATIONER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(messagesProvider.get(REKOMMENDATIONER_KONTAKT_MED_FHV_DELSVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(radioBooleanValue(kontaktMedFhv))
                    .build()
            )
            .build();
    }
}
