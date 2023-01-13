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
package se.inera.intyg.common.luse.v1.model.converter.certificate.question;

import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_DESCRIPTION_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_TEXT_ID;

import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionFunktionsnedsattning;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.util.ValueToolkit;

public class QuestionFunktionsnedsattningAnnan extends AbstractQuestionFunktionsnedsattning {

    public static CertificateDataElement toCertificate(String funktionsnedsattningAnnan, int index, CertificateTextProvider textProvider) {
        return toCertificate(
            funktionsnedsattningAnnan,
            FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID,
            FUNKTIONSNEDSATTNING_ANNAN_TEXT_ID,
            FUNKTIONSNEDSATTNING_ANNAN_DESCRIPTION_ID,
            FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_TEXT_ID,
            FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID,
            index,
            textProvider
        );
    }

    public static String toInternal(Certificate certificate) {
        return ValueToolkit.textValue(certificate.getData(), FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID,
            FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID);
    }
}
