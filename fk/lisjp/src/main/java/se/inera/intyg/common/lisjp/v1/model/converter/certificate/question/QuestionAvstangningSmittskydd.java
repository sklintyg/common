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

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;

import se.inera.intyg.common.fkparent.model.converter.certificate.AbstractQuestionAvstangningSmittskydd;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;

public class QuestionAvstangningSmittskydd extends AbstractQuestionAvstangningSmittskydd {

    public static CertificateDataElement toCertificate(Boolean value, int index,
        CertificateTextProvider texts) {
        return toCertificate(value, AVSTANGNING_SMITTSKYDD_SVAR_ID_27, AVSTANGNING_SMITTSKYDD_CATEGORY_ID,
            AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27, index, texts);
    }

    public static Boolean toInternal(Certificate certificate) {
        return toInternal(certificate, AVSTANGNING_SMITTSKYDD_SVAR_ID_27, AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27);
    }
}
