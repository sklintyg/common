/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.appendAttribute;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.lessThanOrEqual;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.multipleOrExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.wrapWithParenthesis;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;

import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;

public class QuestionMotiveringTidigtStartdatum {

    private static final String VALIDATION_DAYS_TIDIGT_START_DATUM = "-7";
    private static final short LIMIT_MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING = (short) 150;

    public static CertificateDataElement toCertificate(String value, int index) {
        var attribute = "from";
        return CertificateDataElement.builder()
            .id(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32)
            .index(index)
            .parent(BEDOMNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text("Ange orsak för att starta perioden mer än 7 dagar bakåt i tiden.")
                    .description("Observera att detta inte är en fråga från Försäkringskassan."
                        + "Information om varför sjukskrivningen startar mer än en vecka före"
                        + " dagens datum kan vara till hjälp för Försäkringskassan "
                        + "i deras handläggning.</br></br>"
                        + "Informationen du anger nedan, kommer att överföras till fältet \"Övriga upplysningar\" vid signering.")
                    .icon("lightbulb_outline")
                    .id(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID)
                    .build()
            )
            .value(
                CertificateDataValueText.builder()
                    .id(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID)
                    .text(value)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
                        .expression(
                            multipleOrExpression(
                                getWrapWithParenthesis(attribute, SjukskrivningsGrad.NEDSATT_1_4),
                                getWrapWithParenthesis(attribute, SjukskrivningsGrad.NEDSATT_HALFTEN),
                                getWrapWithParenthesis(attribute, SjukskrivningsGrad.NEDSATT_3_4),
                                getWrapWithParenthesis(attribute, SjukskrivningsGrad.HELT_NEDSATT)
                            )
                        )
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID)
                        .limit(LIMIT_MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING)
                        .build()
                }
            )
            .build();
    }

    private static String getWrapWithParenthesis(String attribute, SjukskrivningsGrad sjukskrivningsGrad) {
        return wrapWithParenthesis(
            singleExpression(
                lessThanOrEqual(
                    appendAttribute(
                        sjukskrivningsGrad.getId(), attribute), VALIDATION_DAYS_TIDIGT_START_DATUM)));
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32,
            MOTIVERING_TILL_TIDIGT_STARTDATUM_FOR_SJUKSKRIVNING_ID);
    }
}
