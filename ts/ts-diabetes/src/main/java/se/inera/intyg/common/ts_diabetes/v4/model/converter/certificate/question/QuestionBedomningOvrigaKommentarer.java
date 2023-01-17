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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;

public class QuestionBedomningOvrigaKommentarer {

    private static final short textLimit = 189;

    public static CertificateDataElement toCertificate(Bedomning bedomning, int index, CertificateTextProvider textProvider) {
        final var ovrigaKommentarer =
            bedomning != null && bedomning.getOvrigaKommentarer() != null ? bedomning.getOvrigaKommentarer() : null;
        return CertificateDataElement.builder()
            .id(BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID)
            .parent(BEDOMNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID)
                    .text(textProvider.get(BEDOMNING_OVRIGA_KOMMENTARER_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID)
                    .text(ovrigaKommentarer)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID)
                        .limit(textLimit)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), BEDOMNING_OVRIGA_KOMMENTARER_SVAR_ID, BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID);
    }
}
