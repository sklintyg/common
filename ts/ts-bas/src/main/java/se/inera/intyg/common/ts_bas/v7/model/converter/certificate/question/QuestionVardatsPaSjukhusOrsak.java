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
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigTextField;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationMandatory;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataTextValue;
import se.inera.intyg.common.ts_bas.v7.model.internal.Sjukhusvard;

public class QuestionVardatsPaSjukhusOrsak {

    private static final short TEXT_LIMIT = 50;

    public static CertificateDataElement toCertificate(Sjukhusvard sjukhusvard, int index, CertificateTextProvider textProvider) {

        final var anledning = sjukhusvard != null ? sjukhusvard.getAnledning() : null;

        return CertificateDataElement.builder()
            .id(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID)
            .parent(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID)
            .index(index)
            .config(
                CertificateDataConfigTextField.builder()
                    .text(textProvider.get(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_TEXT_ID))
                    .id(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .id(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID)
                    .text(anledning)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationText.builder()
                        .id(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID)
                        .limit(TEXT_LIMIT)
                        .build(),
                    CertificateDataValidationMandatory.builder()
                        .questionId(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID)
                        .expression(singleExpression(ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID))
                        .build(),
                    CertificateDataValidationShow.builder()
                        .questionId(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID)
                        .expression(singleExpression(FOREKOMST_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    public static String toInternal(Certificate certificate) {
        return textValue(certificate.getData(), ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_ID,
            ORSAK_VARD_SJUKHUS_KONTAKT_LAKARE_DELSVAR_JSON_ID);
    }
}
