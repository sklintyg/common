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
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_JSON_ID_9;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID_9;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_TEXT_ID_9;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_TEXT_ID_9;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;

public class QuestionKorrektionsglasensStyrka {

    public static CertificateDataElement toCertificate(Syn syn, int index, CertificateTextProvider textProvider) {
        final var korrektionsglasensStyrka = syn != null ? syn.getKorrektionsglasensStyrka() : null;
        return CertificateDataElement.builder()
            .id(UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID_9)
            .parent(SYNFUNKTIONER_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_JSON_ID_9)
                    .text(textProvider.get(UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_TEXT_ID_9))
                    .label(textProvider.get(UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_TEXT_ID_9))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_JSON_ID_9)
                    .selected(korrektionsglasensStyrka)
                    .build()
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_SVAR_ID_9,
            UNDERSOKNING_8_DIOPTRIERS_KORREKTIONSGRAD_JSON_ID_9);
    }

}
