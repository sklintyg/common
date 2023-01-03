/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.HAR_DIABETES_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INSULIN_ELLER_TABLETT_MESSAGE_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.INSULIN_ELLER_TABLETT_MESSAGE_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.ts_bas.v6.model.internal.Diabetes;

public class QuestionTablettEllerInsulinMessage {

    public static CertificateDataElement toCertificate(Diabetes diabetes, int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(INSULIN_ELLER_TABLETT_MESSAGE_ID)
            .parent(HAR_DIABETES_CATEGORY_ID)
            .visible(setVisibility(diabetes))
            .index(index)
            .config(
                CertificateDataConfigMessage.builder()
                    .message(textProvider.get(INSULIN_ELLER_TABLETT_MESSAGE_TEXT_ID))
                    .level(MessageLevel.INFO)
                    .build()
            )
            .build();
    }

    private static Boolean setVisibility(Diabetes diabetes) {
        final var tabletter = diabetes != null && diabetes.getTabletter() != null ? diabetes.getTabletter() : null;
        final var insulin = diabetes != null && diabetes.getInsulin() != null ? diabetes.getInsulin() : null;
        return tabletter != null && tabletter || insulin != null && insulin;
    }
}
