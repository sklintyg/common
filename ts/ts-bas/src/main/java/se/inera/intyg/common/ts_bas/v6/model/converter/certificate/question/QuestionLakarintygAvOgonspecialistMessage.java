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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_TEXT_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.SYNFUNKTIONER_CATEGORY_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigMessage;
import se.inera.intyg.common.support.facade.model.config.Message;
import se.inera.intyg.common.support.facade.model.config.MessageLevel;
import se.inera.intyg.common.ts_bas.v6.model.internal.Syn;

public class QuestionLakarintygAvOgonspecialistMessage {

    public static CertificateDataElement toCertificate(Syn syn, int index, CertificateTextProvider texts) {
        return CertificateDataElement.builder()
            .id(LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_SVAR_ID)
            .parent(SYNFUNKTIONER_CATEGORY_ID)
            .index(index)
            .visible(messageVisibility(syn))
            .config(
                CertificateDataConfigMessage.builder()
                    .message(
                        Message.builder()
                            .content(texts.get(LAKARINTYG_AV_OGONSPECIALIST_MESSAGE_TEXT_ID))
                            .level(MessageLevel.INFO)
                            .build()
                    )
                    .build()
            )
            .build();
    }

    private static Boolean messageVisibility(Syn syn) {
        final var nattblindhet = syn != null ? syn.getNattblindhet() : null;
        final var progressivOgonsjukdom = syn != null ? syn.getProgressivOgonsjukdom() : null;
        final var synfaltsdefekter = syn != null ? syn.getSynfaltsdefekter() : null;

        return nattblindhet != null && nattblindhet || progressivOgonsjukdom != null
            && progressivOgonsjukdom || synfaltsdefekter != null && synfaltsdefekter;
    }
}
