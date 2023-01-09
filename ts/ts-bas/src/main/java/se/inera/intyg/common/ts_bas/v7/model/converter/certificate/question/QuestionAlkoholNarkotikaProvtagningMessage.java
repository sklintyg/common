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

import static se.inera.intyg.common.support.facade.util.ValidationExpressionToolkit.singleExpression;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.MISSBRUK_BEROENDE_LAKEMEDEL_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_JSON_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v7.codes.RespConstantsV7.PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidation;
import se.inera.intyg.common.support.facade.model.validation.CertificateDataValidationShow;
import se.inera.intyg.common.ts_bas.v7.model.internal.NarkotikaLakemedel;

public class QuestionAlkoholNarkotikaProvtagningMessage {

    public static CertificateDataElement toCertificate(NarkotikaLakemedel narkotikaLakemedel, int index, CertificateTextProvider texts) {

        return CertificateDataElement.builder()
            .id(PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_ID)
            .parent(MISSBRUK_BEROENDE_LAKEMEDEL_CATEGORY_ID)
            .index(index)
            .visible(getVisibility(narkotikaLakemedel))
            .config(
                CertificateDataConfigMessage.builder()
                    .message(texts.get(PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_MESSAGE_TEXT_ID))
                    .level(MessageLevel.INFO)
                    .build()
            )
            .validation(
                new CertificateDataValidation[]{
                    CertificateDataValidationShow.builder()
                        .questionId(PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_DELSVAR_ID)
                        .expression(singleExpression(PROVTAGNING_AVSEENDE_AKTUELLT_BRUK_JSON_ID))
                        .build()
                }
            )
            .build();
    }

    private static Boolean getVisibility(NarkotikaLakemedel narkotikaLakemedel) {
        final var provtagningBehovs = narkotikaLakemedel != null ? narkotikaLakemedel.getProvtagningBehovs() : null;
        return provtagningBehovs != null && provtagningBehovs;
    }
}
