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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;

import java.util.List;
import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionFunktionsnedsattningLisjp;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionFunktionsnedsattning extends AbstractQuestionFunktionsnedsattningLisjp {

    public static CertificateDataElement toCertificate(String value,
        List<String> disabilityCategories, int index,
        CertificateTextProvider texts) {
        return toCertificate(value, FUNKTIONSNEDSATTNING_SVAR_ID_35, FUNKTIONSNEDSATTNING_CATEGORY_ID, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35,
            disabilityCategories, index, texts);
    }

    public static String toInternalTextValue(Certificate certificate) {
        return toInternalTextValue(certificate, FUNKTIONSNEDSATTNING_SVAR_ID_35, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35);
    }

    public static List<String> toInternalCodeValue(Certificate certificate) {
        return toInternalCodeValue(certificate, FUNKTIONSNEDSATTNING_SVAR_ID_35, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35);
    }
}
