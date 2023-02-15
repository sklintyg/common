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

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleAndExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.not;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithParenthesis;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;

public class QuestionMotiveringEjUndersokning {

    private static final short LIMIT_MOTIVERING_INTE_BASERAT_PA_UNDERLAG = (short) 150;

    public static CertificateDataElement toCertificate(String value, int index) {
        return CertificateDataElement.builder()
            .id(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1)
            .index(index)
            .parent(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Motivering till varför det medicinska underlaget inte baseras på en undersökning av patienten")
                    .description(
                        "Observera att detta inte är en fråga från Försäkringskassan. Information om varför sjukskrivningen startar "
                            + "mer än en vecka före dagens datum kan vara till hjälp för Försäkringskassan i deras handläggning.\n"
                            + "Informationen du anger nedan, kommer att överföras till fältet \"Övriga upplysningar\" vid signering.")
                    .icon("lightbulb_outline")
                    .id(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                        .expression(
                            multipleAndExpression(
                                not(
                                    singleExpression(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
                                ),
                                wrapWithParenthesis(
                                    multipleOrExpression(
                                        singleExpression(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1),
                                        singleExpression(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1),
                                        singleExpression(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                                    )
                                )
                            )
                        )
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1)
                        .limit(LIMIT_MOTIVERING_INTE_BASERAT_PA_UNDERLAG)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
            MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1);
    }
}
