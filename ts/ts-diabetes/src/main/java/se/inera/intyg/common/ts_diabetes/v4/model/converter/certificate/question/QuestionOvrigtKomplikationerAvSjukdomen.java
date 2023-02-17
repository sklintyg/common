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
package se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SVAR_JA_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SVAR_NEJ_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;

public class QuestionOvrigtKomplikationerAvSjukdomen {

    public static CertificateDataElement toCertificate(Ovrigt ovrigt, int index, CertificateTextProvider textProvider) {
        final var komplikationerAvSjukdomen =
            ovrigt != null && ovrigt.getKomplikationerAvSjukdomen() != null ? ovrigt.getKomplikationerAvSjukdomen() : null;
        return CertificateDataElement.builder()
            .id(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID)
            .parent(OVRIGT_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID)
                    .text(textProvider.get(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_TEXT_ID))
                    .selectedText(textProvider.get(SVAR_JA_TEXT_ID))
                    .unselectedText(textProvider.get(SVAR_NEJ_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID)
                    .selected(komplikationerAvSjukdomen)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID)
                        .expression(exists(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_SVAR_ID,
            OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID);
    }
}
