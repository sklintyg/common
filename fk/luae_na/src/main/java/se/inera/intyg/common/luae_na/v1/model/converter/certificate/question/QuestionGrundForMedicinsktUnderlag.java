/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionGrundForMedicinsktUnderlag {

    public static CertificateDataElement toCertificate(int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id("1.1")
            .parent("1")
            .index(index)
            .config(
                CertificateDataConfigTextField.builder()
                    .id("1.1.0")
                    .text(textProvider.get(IDENTITET_STYRKT_QUESTION_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(IDENTITET_STYRKT_JSON_ID)
                    .text(identitetStyrkt)
                    .build()
            )
            .build();
    }
}
