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
package se.inera.intyg.common.sos_parent.model.converter.certificate.question;

import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_QUESTION_SELECTED_QUESTION;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_QUESTION_TEXT_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_QUESTION_UNSELECTED_QUESTION;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TO_EPOCH_DAY;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TWENTY_EIGHT_DAYS;
import static se.inera.intyg.common.support.facade.util.PatientToolkit.birthDate;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.appendAttribute;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.lessThanOrEqual;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.moreThan;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationAutoFill;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationDisable;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.schemas.contract.Personnummer;


public class QuestionBarn {

    public static CertificateDataElement toCertificate(Personnummer personId, Boolean dodsdatumSakert, int index,
        CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(BARN_DELSVAR_ID)
            .parent(BARN_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(BARN_JSON_ID)
                    .text(texts.get(BARN_QUESTION_TEXT_ID))
                    .selectedText(texts.get(BARN_QUESTION_SELECTED_QUESTION))
                    .unselectedText(texts.get(BARN_QUESTION_UNSELECTED_QUESTION))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(BARN_JSON_ID)
                    .selected(dodsdatumSakert)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(BARN_DELSVAR_ID)
                        .expression(singleExpression(BARN_JSON_ID))
                        .build(),
                    CertificateDataValidationAutoFill.builder()
                        .questionId(DODSDATUM_DELSVAR_ID)
                        .expression(
                            lessThanOrEqual(
                                singleExpression(
                                    appendAttribute(DODSDATUM_JSON_ID, TO_EPOCH_DAY)
                                ),
                                birthDate(personId)
                                    .plusDays(TWENTY_EIGHT_DAYS)
                                    .toEpochDay()
                            )
                        )
                        .fillValue(
                            CertificateDataValueBoolean.builder()
                                .id(BARN_JSON_ID)
                                .selected(true)
                                .build()
                        )
                        .build(),
                    CertificateDataValidationAutoFill.builder()
                        .questionId(DODSDATUM_DELSVAR_ID)
                        .expression(
                            moreThan(
                                singleExpression(
                                    appendAttribute(DODSDATUM_JSON_ID, TO_EPOCH_DAY)
                                ),
                                birthDate(personId)
                                    .plusDays(TWENTY_EIGHT_DAYS)
                                    .toEpochDay()
                            )
                        )
                        .fillValue(
                            CertificateDataValueBoolean.builder()
                                .id(BARN_JSON_ID)
                                .selected(false)
                                .build()
                        )
                        .build(),
                    CertificateDataValidationDisable.builder()
                        .questionId(DODSDATUM_DELSVAR_ID)
                        .expression(singleExpression(DODSDATUM_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), BARN_DELSVAR_ID, BARN_JSON_ID);
    }
}
