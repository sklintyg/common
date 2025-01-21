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

package se.inera.intyg.common.ts_bas.v6.model.converter.certificate.question;

import static se.inera.intyg.common.support.facade.util.ViewTextToolkit.stringValue;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.IDENTITET_CATEGORY_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.ts_bas.v6.codes.RespConstantsV6.IDENTITET_STYRKT_GENOM_TEXT_ID;

import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigViewText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueViewText;
import se.inera.intyg.common.ts_bas.v6.model.internal.Vardkontakt;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;

public class QuestionIdentitet {

    public static final String ID_KORT = "ID_KORT";
    private static final String FORETAG_ELLER_TJANSTEKORT = "FORETAG_ELLER_TJANSTEKORT";
    private static final String KORKORT = "KORKORT";
    private static final String PERS_KANNEDOM = "PERS_KANNEDOM";
    private static final String FORSAKRAN_KAP18 = "FORSAKRAN_KAP18";
    private static final String PASS = "PASS";

    public static CertificateDataElement toCertificate(Vardkontakt vardkontakt, int index, CertificateTextProvider textProvider) {
        var identitetStyrktGenom = vardkontakt != null && vardkontakt.getIdkontroll() != null ? vardkontakt.getIdkontroll() : null;
        if (identitetStyrktGenom != null) {
            identitetStyrktGenom = getDescriptionValue(identitetStyrktGenom);
        }
        return CertificateDataElement.builder()
            .id(IDENTITET_STYRKT_GENOM_SVAR_ID)
            .index(index)
            .parent(IDENTITET_CATEGORY_ID)
            .config(
                CertificateDataConfigViewText.builder()
                    .text(textProvider.get(IDENTITET_STYRKT_GENOM_TEXT_ID))
                    .build()
            )
            .value(
                CertificateDataValueViewText.builder()
                    .text(stringValue(identitetStyrktGenom))
                    .build()
            )
            .build();
    }

    private static String getDescriptionValue(String identitetStyrktGenom) {
        switch (identitetStyrktGenom) {
            case ID_KORT:
                return IdKontrollKod.ID_KORT.getDescription();
            case FORETAG_ELLER_TJANSTEKORT:
                return IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getDescription();
            case KORKORT:
                return IdKontrollKod.KORKORT.getDescription();
            case PERS_KANNEDOM:
                return IdKontrollKod.PERS_KANNEDOM.getDescription();
            case FORSAKRAN_KAP18:
                return IdKontrollKod.FORSAKRAN_KAP18.getDescription();
            case PASS:
                return IdKontrollKod.PASS.getDescription();
            default:
                return null;
        }
    }
}
