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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate.question;

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.ARBETSRESOR_SVAR_JSON_ID_34;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;

import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionArbetsresor;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionArbetsresor extends AbstractQuestionArbetsresor {

    public static CertificateDataElement toCertificate(Boolean value, int index,
        CertificateTextProvider texts) {
        return toCertificate(value, ARBETSRESOR_SVAR_ID_34, BEDOMNING_CATEGORY_ID, ARBETSRESOR_SVAR_JSON_ID_34, index, texts);
    }

    public static Boolean toInternal(Certificate certificate) {
        return toInternal(certificate, ARBETSRESOR_SVAR_ID_34, ARBETSRESOR_SVAR_JSON_ID_34);
    }
}
