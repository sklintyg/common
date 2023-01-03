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

import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_AUTOFILL_AFTER_MESSAGE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_AUTO_FILL_AFTER_MESSAGE_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_CATEGORY_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TO_EPOCH_DAY;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TWENTY_EIGHT_DAYS;
import static se.inera.intyg.common.support.facade.util.PatientToolkit.birthDate;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.appendAttribute;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.moreThan;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.schemas.contract.Personnummer;

public class QuestionAutoFillMessageAfter28DaysBarn {

    public static CertificateDataElement toCertificate(Personnummer personId, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(BARN_AUTOFILL_AFTER_MESSAGE_DELSVAR_ID)
            .parent(BARN_CATEGORY_ID)
            .index(index)
            .visible(false)
            .config(
                CertificateDataConfigMessage.builder()
                    .message(texts.get(BARN_AUTO_FILL_AFTER_MESSAGE_ID))
                    .level(MessageLevel.OBSERVE)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
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
                        .build()
                }
            )
            .build();
    }
}
