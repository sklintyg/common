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

package se.inera.intyg.common.fkparent.model.converter.certificate;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANSWER_NOT_SELECTED;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANSWER_YES;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_QUESTION_LABEL;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public abstract class AbstractQuestionAvstangningSmittskydd {

    public static CertificateDataElement toCertificate(Boolean value, String questionId, String parent, String jsonId, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(questionId)
            .index(index)
            .parent(parent)
            .config(
                CertificateDataConfigCheckboxBoolean.builder()
                    .id(jsonId)
                    .label(texts.get(AVSTANGNING_SMITTSKYDD_QUESTION_LABEL))
                    .selectedText(texts.get(ANSWER_YES))
                    .unselectedText(texts.get(ANSWER_NOT_SELECTED))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(jsonId)
                    .selected(value)
                    .build()
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate, String questionId, String jsonId) {
        return booleanValue(certificate.getData(), questionId, jsonId);
    }
}
