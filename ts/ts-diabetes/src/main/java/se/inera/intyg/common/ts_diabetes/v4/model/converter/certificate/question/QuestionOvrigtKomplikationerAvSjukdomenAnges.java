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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DESCRIPTION_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;

public class QuestionOvrigtKomplikationerAvSjukdomenAnges {

    private static final short textLimit = 189;

    public static CertificateDataElement toCertificate(Ovrigt ovrigt, int index, CertificateTextProvider textProvider) {
        final var komplikationerAvSjukdomenAnges =
            ovrigt != null && ovrigt.getKomplikationerAvSjukdomenAnges() != null ? ovrigt.getKomplikationerAvSjukdomenAnges() : null;
        return CertificateDataElement.builder()
            .id(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID)
            .parent(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID)
            .index(index)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID)
                    .text(textProvider.get(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_TEXT_ID))
                    .description(textProvider.get(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DESCRIPTION_ID))
                    .build()
            )
            .value(
                CertificateDataValueText.builder()
                    .id(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID)
                    .text(komplikationerAvSjukdomenAnges)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID)
                        .expression(singleExpression(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID))
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID)
                        .expression(singleExpression(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID))
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID)
                        .limit(textLimit)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID,
            OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID);
    }
}
