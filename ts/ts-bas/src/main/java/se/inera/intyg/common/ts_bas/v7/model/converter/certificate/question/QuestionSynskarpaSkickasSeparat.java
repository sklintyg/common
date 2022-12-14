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

package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNFUNKTIONER_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_DESCRIPTION_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_JSON_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_SVAR_TEXT_ID_8;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SYNKARPA_SKICKAS_SEPARAT_TEXT_ID_8;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;

public class QuestionSynskarpaSkickasSeparat {

    public static CertificateDataElement toCertificate(Syn syn, int index, CertificateTextProvider textProvider) {
        final var synfaltsdefekter = syn != null ? syn.getSynskarpaSkickasSeparat() : null;
        return CertificateDataElement.builder()
            .id(SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID_8)
            .parent(SYNFUNKTIONER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(SYNKARPA_SKICKAS_SEPARAT_JSON_ID_8)
                    .text(textProvider.get(SYNKARPA_SKICKAS_SEPARAT_TEXT_ID_8))
                    .description(textProvider.get(SYNKARPA_SKICKAS_SEPARAT_DESCRIPTION_ID_8))
                    .label(textProvider.get(SYNKARPA_SKICKAS_SEPARAT_SVAR_TEXT_ID_8))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(SYNKARPA_SKICKAS_SEPARAT_JSON_ID_8)
                    .selected(synfaltsdefekter)
                    .build()
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), SYNKARPA_SKICKAS_SEPARAT_DELSVAR_ID_8, SYNKARPA_SKICKAS_SEPARAT_JSON_ID_8);
    }

}
