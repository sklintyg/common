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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.textValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_MEDVETANDESTORNING_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_MEDVETANDESTORNING_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.MEDVETANDESTORNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.MEDVETANDESTORNING_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.MEDVETANDESTORNING_SVAR_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextArea;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medvetandestorning;

public class QuestionMedvetandestorningBeskrivning {

    private static final short TEXT_LIMIT = 180;

    public static CertificateDataElement toCertificate(Medvetandestorning medvetandestorning, int index,
        CertificateTextProvider textProvider) {
        final var medvetandestorningBeskrivning =
            medvetandestorning != null && medvetandestorning.getBeskrivning() != null ? medvetandestorning.getBeskrivning() : null;
        return CertificateDataElement.builder()
            .id(FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID)
            .index(index)
            .parent(MEDVETANDESTORNING_CATEGORY_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .id(FOREKOMST_MEDVETANDESTORNING_JSON_ID)
                    .text(textProvider.get(FOREKOMST_MEDVETANDESTORNING_DELSVAR_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueText.builder()
                    .id(FOREKOMST_MEDVETANDESTORNING_JSON_ID)
                    .text(medvetandestorningBeskrivning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(MEDVETANDESTORNING_SVAR_ID)
                        .expression(singleExpression(MEDVETANDESTORNING_JSON_ID))
                        .build(),
                    CertificateDataValidationText.builder()
                        .id(FOREKOMST_MEDVETANDESTORNING_JSON_ID)
                        .limit(TEXT_LIMIT)
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), FOREKOMST_MEDVETANDESTORNING_DELSVAR_ID, FOREKOMST_MEDVETANDESTORNING_JSON_ID);
    }
}
