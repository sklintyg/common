/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_DESCRIPTION_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_TEXT_ID;

import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionFunktionsnedsattning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.util.ValueToolkit;

public class QuestionFunktionsnedsattningPsykisk extends AbstractQuestionFunktionsnedsattning {

    public static CertificateDataElement toCertificate(String funktionsnedsattningPsykisk, int index,
        CertificateTextProvider textProvider) {
        return toCertificate(
            funktionsnedsattningPsykisk,
            FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11,
            FUNKTIONSNEDSATTNING_PSYKISK_TEXT_ID,
            FUNKTIONSNEDSATTNING_PSYKISK_DESCRIPTION_ID,
            FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_TEXT_ID,
            FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11,
            index,
            textProvider
        );
    }

    public static String toInternal(Certificate certificate) {
        return ValueToolkit.textValue(certificate.getData(), FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11,
            FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11);
    }
}
