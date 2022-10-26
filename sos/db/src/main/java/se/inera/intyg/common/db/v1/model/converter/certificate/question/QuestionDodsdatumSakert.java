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

package se.inera.intyg.common.db.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DODSPLATS_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_QUESTION_DESCRIPTION_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_QUESTION_SELECTED_TEXT;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_QUESTION_UNSELECTED_TEXT;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;


public class QuestionDodsdatumSakert {

    public static CertificateDataElement toCertificate(Boolean dodsdatumSakert, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(DODSDATUM_SAKERT_DELSVAR_ID)
            .parent(DODSDATUM_DODSPLATS_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(DODSDATUM_SAKERT_JSON_ID)
                    .text(texts.get(DODSDATUM_SAKERT_QUESTION_TEXT_ID))
                    .description(texts.get(DODSDATUM_SAKERT_QUESTION_DESCRIPTION_ID))
                    .selectedText(texts.get(DODSDATUM_SAKERT_QUESTION_SELECTED_TEXT))
                    .unselectedText(texts.get(DODSDATUM_SAKERT_QUESTION_UNSELECTED_TEXT))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(DODSDATUM_SAKERT_JSON_ID)
                    .selected(dodsdatumSakert)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(DODSDATUM_SAKERT_DELSVAR_ID)
                        .expression(singleExpression(DODSDATUM_SAKERT_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), DODSDATUM_SAKERT_DELSVAR_ID, DODSDATUM_SAKERT_JSON_ID);
    }
}
