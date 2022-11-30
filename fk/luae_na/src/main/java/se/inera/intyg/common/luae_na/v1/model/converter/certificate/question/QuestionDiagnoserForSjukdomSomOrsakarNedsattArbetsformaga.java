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

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_BESKRIVNING;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_TEXT;

import java.util.Arrays;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCode;

public class QuestionDiagnoserForSjukdomSomOrsakarNedsattArbetsformaga {

    public static CertificateDataElement toCertificate( int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DIAGNOS_SVAR_ID_6)
            .parent(DIAGNOS_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioMultipleCode.builder()
                    .text(texts.get(DIAGNOS_SVAR_TEXT))
                    .description(texts.get(DIAGNOS_SVAR_BESKRIVNING))
                    .list(
                        Arrays.asList(
                            RadioMultipleCode.builder()
                                .id("ICD_10_SE")
                                .label("ICD_10_SE")
                                .build(),
                            RadioMultipleCode.builder()
                                .id("KSH97-P")
                                .label("KSH97-P (Primärvård)")
                                .build()
                        )
                    )
                    .build()
            )
            .build();
    }
}
