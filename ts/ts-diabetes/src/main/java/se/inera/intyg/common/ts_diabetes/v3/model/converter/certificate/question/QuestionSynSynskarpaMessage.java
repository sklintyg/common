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

package se.inera.intyg.common.ts_diabetes.v3.model.converter.certificate.question;

import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_VARDEN_SYNSKARPA_MESSAGE_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_VARDEN_SYNSKARPA_MESSAGE_TEXT_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_VARDEN_SYNSKARPA_SVAR_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.Message;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;

public class QuestionSynSynskarpaMessage {


    public static CertificateDataElement toCertificate(int index, CertificateTextProvider textProvider) {
        return CertificateDataElement.builder()
            .id(SYN_VARDEN_SYNSKARPA_MESSAGE_ID)
            .parent(SYN_CATEGORY_ID)
            .index(index)
            .config(
                CertificateDataConfigMessage.builder()
                    .text(textProvider.get(SYN_VARDEN_SYNSKARPA_SVAR_TEXT_ID))
                    .message(
                        Message.builder()
                            .content(textProvider.get(SYN_VARDEN_SYNSKARPA_MESSAGE_TEXT_ID))
                            .level(MessageLevel.OBSERVE)
                            .build()
                    )
                    .build()
            )
            .build();
    }
}
