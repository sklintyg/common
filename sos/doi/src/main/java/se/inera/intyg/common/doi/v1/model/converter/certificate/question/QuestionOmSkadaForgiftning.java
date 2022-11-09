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

package se.inera.intyg.common.doi.v1.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_QUESTION_SELECTED_TEXT;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_QUESTION_UNSELECTED_TEXT;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;

public class QuestionOmSkadaForgiftning {

    public static CertificateDataElement toCertificate(Boolean forgiftadEllerSkadad, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(FORGIFTNING_OM_DELSVAR_ID)
            .index(index)
            .parent(FORGIFTNING_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .text(texts.get(FORGIFTNING_OM_QUESTION_TEXT_ID))
                    .selectedText(texts.get(FORGIFTNING_OM_QUESTION_SELECTED_TEXT))
                    .unselectedText(texts.get(FORGIFTNING_OM_QUESTION_UNSELECTED_TEXT))
                    .id(FORGIFTNING_OM_JSON_ID)
                    .build())
            .value(CertificateDataValueBoolean.builder()
                .id(FORGIFTNING_OM_JSON_ID)
                .selected(forgiftadEllerSkadad)
                .build())
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FORGIFTNING_OM_DELSVAR_ID)
                        .expression(singleExpression(FORGIFTNING_OM_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), FORGIFTNING_OM_DELSVAR_ID, FORGIFTNING_OM_JSON_ID);
    }
}
