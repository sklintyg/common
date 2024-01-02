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
package se.inera.intyg.common.luae_na.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionMedicinskBehandlingPagaendeBehandling {

    private static final short TEXT_LIMIT = 3500;

    public static CertificateDataElement toCertificate(String textValue, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(PAGAENDEBEHANDLING_DELSVAR_ID_19)
            .parent(PAGAENDEBEHANDLING_SVAR_ID_19)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                    .text(texts.get(PAGAENDEBEHANDLING_DELSVAR_TEXT))
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                    .text(textValue)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
                        .limit(TEXT_LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), PAGAENDEBEHANDLING_DELSVAR_ID_19, PAGAENDEBEHANDLING_SVAR_JSON_ID_19);
    }
}
