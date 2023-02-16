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
package se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.exists;
import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.support.facade.util.ValueToolkit.booleanValue;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_JA_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.SVAR_NEJ_TEXT;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.VARD_SJUKHUS_KONTAKT_LAKARE_CATEGORY_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.ts_bas.v7.model.internal.Sjukhusvard;

public class QuestionVardatsPaSjukhus {
    public static CertificateDataElement toCertificate(Sjukhusvard sjukhusvard, int index, CertificateTextProvider textProvider) {

        final var sjukhusEllerLakarkontakt = sjukhusvard != null ? sjukhusvard.getSjukhusEllerLakarkontakt() : null;

        return CertificateDataElement.builder()
            .index(index)
            .id(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID)
            .parent(VARD_SJUKHUS_KONTAKT_LAKARE_CATEGORY_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .id(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID)
                    .text(textProvider.get(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_TEXT_ID))
                    .selectedText(SVAR_JA_TEXT)
                    .unselectedText(SVAR_NEJ_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder()
                    .id(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID)
                    .selected(sjukhusEllerLakarkontakt)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationMandatory.builder()
                        .questionId(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID)
                        .expression(exists(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static Boolean toInternal(Certificate certificate) {
        return booleanValue(certificate.getData(), FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID,
            FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID);
    }
}
