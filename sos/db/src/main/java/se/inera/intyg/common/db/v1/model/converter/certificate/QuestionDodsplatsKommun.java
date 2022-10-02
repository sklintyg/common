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

package se.inera.intyg.common.db.v1.model.converter.certificate;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DODSPLATS_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_LABEL_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_QUESTION_TEXT_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionDodsplatsKommun {

    private static final short TEXT_LIMIT = 28;

    public static CertificateDataElement toCertificate(String kommun, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DODSPLATS_KOMMUN_DELSVAR_ID)
            .parent(DODSDATUM_DODSPLATS_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigTextField.builder()
                    .text(texts.get(DODSPLATS_QUESTION_TEXT_ID))
                    .label(texts.get(DODSPLATS_KOMMUN_LABEL_TEXT_ID))
                    .id(DODSPLATS_KOMMUN_JSON_ID)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(DODSPLATS_KOMMUN_JSON_ID)
                    .text(kommun)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DODSPLATS_KOMMUN_DELSVAR_ID)
                        .expression(singleExpression(DODSPLATS_KOMMUN_JSON_ID))
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(DODSPLATS_KOMMUN_JSON_ID)
                        .limit(TEXT_LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), DODSPLATS_KOMMUN_DELSVAR_ID, DODSPLATS_KOMMUN_JSON_ID);
    }
}
