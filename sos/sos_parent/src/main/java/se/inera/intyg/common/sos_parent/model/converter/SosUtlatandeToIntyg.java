/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.sos_parent.model.converter;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_CODE_SYSTEM;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_SVAR_ID;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotNull;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getInternalDateContent;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getInternalDateContentFillWithZeros;

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

/**
 * Converters for shared svar between DB and DOI.
 */
public final class SosUtlatandeToIntyg {

    private SosUtlatandeToIntyg() {
    }

    public static List<Svar> getSharedSvar(SosUtlatande utlatande) {
        List<Svar> svar = new ArrayList<>();

        // Svar 1
        addIfNotBlank(svar, IDENTITET_STYRKT_SVAR_ID, IDENTITET_STYRKT_DELSVAR_ID, utlatande.getIdentitetStyrkt());

        // Svar 2
        if (utlatande.getDodsdatumSakert() != null || utlatande.getDodsdatum() != null || utlatande.getAntraffatDodDatum() != null) {
            InternalConverterUtil.SvarBuilder dodsdatum = aSvar(DODSDATUM_SVAR_ID);
            if (utlatande.getDodsdatumSakert() != null) {
                dodsdatum.withDelsvar(DODSDATUM_SAKERT_DELSVAR_ID, utlatande.getDodsdatumSakert().toString());
            }
            dodsdatum.withDelsvar(DODSDATUM_DELSVAR_ID, getInternalDateContentFillWithZeros(utlatande.getDodsdatum()));
            if (utlatande.getAntraffatDodDatum() != null && utlatande.getAntraffatDodDatum().isValidDate()) {
                dodsdatum.withDelsvar(ANTRAFFAT_DOD_DATUM_DELSVAR_ID, getInternalDateContent(utlatande.getAntraffatDodDatum()));
            }
            svar.add(dodsdatum.build());
        }
        // Svar 3
        if (utlatande.getDodsplatsBoende() != null || utlatande.getDodsplatsKommun() != null) {
            InternalConverterUtil.SvarBuilder dodsplats = aSvar(DODSPLATS_SVAR_ID)
                .withDelsvar(DODSPLATS_KOMMUN_DELSVAR_ID, utlatande.getDodsplatsKommun());
            if (utlatande.getDodsplatsBoende() != null) {
                dodsplats.withDelsvar(DODSPLATS_BOENDE_DELSVAR_ID,
                    aCV(DODSPLATS_BOENDE_CODE_SYSTEM, utlatande.getDodsplatsBoende().name(),
                        utlatande.getDodsplatsBoende().getBeskrivning()));
            }
            svar.add(dodsplats.build());
        }

        // Svar 4
        addIfNotNull(svar, BARN_SVAR_ID, BARN_DELSVAR_ID, utlatande.getBarn());

        return svar;
    }
}
