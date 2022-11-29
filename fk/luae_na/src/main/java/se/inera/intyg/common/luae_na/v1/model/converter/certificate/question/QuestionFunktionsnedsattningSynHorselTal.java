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

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ACCORDION_CLOSE_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ACCORDION_OPEN_TEXT;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.Accordion;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.support.facade.util.ValueToolkit;

public class QuestionFunktionsnedsattningSynHorselTal {

    private static final short TEXT_LIMIT = (short) 4000;

    public static CertificateDataElement toCertificate(String funktionsnedattningSynHorselTal, int index,
        CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12)
            .parent(FUNKTIONSNEDSATTNING_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(textProvider.get(FUNKTIONSNEDSATTNING_SYNHORSELTAL_TEXT_ID))
                    .description(textProvider.get(FUNKTIONSNEDSATTNING_SYNHORSELTAL_DESCRIPTION_ID))
                    .accordion(
                        Accordion.builder()
                            .openText(FUNKTIONSNEDSATTNING_ACCORDION_OPEN_TEXT)
                            .closeText(FUNKTIONSNEDSATTNING_ACCORDION_CLOSE_TEXT)
                            .header(textProvider.get(FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_TEXT_ID))
                            .build()
                    )
                    .id(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12)
                    .text(funktionsnedattningSynHorselTal)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12)
                        .limit(TEXT_LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return ValueToolkit.textValue(certificate.getData(), FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12,
            FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID_12);
    }
}
