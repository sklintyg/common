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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.OVRIGA_KOMMENTARER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.OVRIGA_KOMMENTARER_DELSVARSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.OVRIGA_KOMMENTARER_DELSVARSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.OVRIGA_KOMMENTARER_DELSVARSVAR_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionOvrigt {
    private static final short TEXT_LIMIT = 500;

    public static CertificateDataElement toCertificate(String komentar, int index, CertificateTextProvider textProvider) {

        return CertificateDataElement.builder()
            .id(OVRIGA_KOMMENTARER_DELSVARSVAR_ID)
            .parent(OVRIGA_KOMMENTARER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(textProvider.get(OVRIGA_KOMMENTARER_DELSVARSVAR_TEXT_ID))
                    .id(OVRIGA_KOMMENTARER_DELSVARSVAR_JSON_ID)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(OVRIGA_KOMMENTARER_DELSVARSVAR_JSON_ID)
                    .text(komentar)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(OVRIGA_KOMMENTARER_DELSVARSVAR_JSON_ID)
                        .limit(TEXT_LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), OVRIGA_KOMMENTARER_DELSVARSVAR_ID, OVRIGA_KOMMENTARER_DELSVARSVAR_JSON_ID);
    }
}
