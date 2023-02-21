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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.util;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_ATER_X_ANTAL_DAGAR;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_PROGNOS_OKLAR;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_SANNOLIKT_INTE;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.PROGNOS_SVAR_STOR_SANNOLIKHET;

import java.util.List;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.config.RadioMultipleCodeOptionalDropdown;

public class QuestionPrognosCommonProvider {

    public static String[] getMandatoryValidation() {
        return new String[]{
            PrognosTyp.MED_STOR_SANNOLIKHET.getId(),
            PrognosTyp.ATER_X_ANTAL_DGR.getId(),
            PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId(),
            PrognosTyp.PROGNOS_OKLAR.getId()
        };
    }

    public static List<RadioMultipleCodeOptionalDropdown> getCodeOptionalDropdowns(CertificateTextProvider texts) {
        return List.of(
            RadioMultipleCodeOptionalDropdown.builder()
                .id(PrognosTyp.MED_STOR_SANNOLIKHET.getId())
                .label(texts.get(PROGNOS_SVAR_STOR_SANNOLIKHET))
                .build(),
            RadioMultipleCodeOptionalDropdown.builder()
                .id(PrognosTyp.ATER_X_ANTAL_DGR.getId())
                .label(texts.get(PROGNOS_SVAR_ATER_X_ANTAL_DAGAR))
                .dropdownQuestionId(PROGNOS_BESKRIVNING_DELSVAR_ID_39)
                .build(),
            RadioMultipleCodeOptionalDropdown.builder()
                .id(PrognosTyp.SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING.getId())
                .label(texts.get(PROGNOS_SVAR_SANNOLIKT_INTE))
                .build(),
            RadioMultipleCodeOptionalDropdown.builder()
                .id(PrognosTyp.PROGNOS_OKLAR.getId())
                .label(texts.get(PROGNOS_SVAR_PROGNOS_OKLAR))
                .build()
        );
    }

}
