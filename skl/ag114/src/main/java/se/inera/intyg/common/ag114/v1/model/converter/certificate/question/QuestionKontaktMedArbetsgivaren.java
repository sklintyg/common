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

package se.inera.intyg.common.ag114.v1.model.converter.certificate.question;

import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_KONTAKT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_LABEL_ID;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionKontaktMedArbetsgivaren {

    public static CertificateDataElement toCertificate(Boolean kontaktMedArbetsgivaren, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(KONTAKT_ONSKAS_SVAR_ID)
            .parent(CATEGORY_KONTAKT_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(KONTAKT_ONSKAS_SVAR_JSON_ID)
                    .label(textProvider.get(KONTAKT_ONSKAS_SVAR_LABEL_ID))
                    .selectedText(ANSWER_YES)
                    .unselectedText(ANSWER_NOT_SELECTED)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(KONTAKT_ONSKAS_SVAR_JSON_ID)
                    .selected(kontaktMedArbetsgivaren)
                    .build()
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), KONTAKT_ONSKAS_SVAR_ID, KONTAKT_ONSKAS_SVAR_JSON_ID);
    }
}
