/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.sos_db.model.converter;

import se.inera.intyg.common.sos_db.model.internal.DbUtlatande;
import se.inera.intyg.common.sos_db.model.internal.Undersokning;
import se.inera.intyg.common.sos_db.support.DbModuleEntryPoint;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.util.List;

import static se.inera.intyg.common.sos_parent.model.converter.SosUtlatandeToIntyg.getSharedSvar;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_AVLAGSNAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DETALJER_CODE_SYSTEM;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DETALJER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_DELSVAR_ID;
import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotNull;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getInternalDateContent;

public final class UtlatandeToIntyg {
    private UtlatandeToIntyg() {
    }

    public static Intyg convert(DbUtlatande utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande);
        intyg.setTyp(getTypAvIntyg(utlatande));
        intyg.getSvar().addAll(getSvar(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(DbUtlatande utlatande) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(utlatande.getTyp());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(DbModuleEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(DbUtlatande utlatande) {
        List<Svar> svar = getSharedSvar(utlatande);

        // Svar 5
        if (utlatande.getExplosivAvlagsnat() != null || utlatande.getExplosivImplantat() != null) {
            InternalConverterUtil.SvarBuilder explosiv = aSvar(EXPLOSIV_IMPLANTAT_SVAR_ID);
            if (utlatande.getExplosivImplantat() != null) {
                explosiv.withDelsvar(EXPLOSIV_IMPLANTAT_DELSVAR_ID, utlatande.getExplosivImplantat().toString());
            }
            if (utlatande.getExplosivAvlagsnat() != null) {
                explosiv.withDelsvar(EXPLOSIV_AVLAGSNAT_DELSVAR_ID, utlatande.getExplosivAvlagsnat().toString());
            }
            svar.add(explosiv.build());
        }

        // Svar 6
        if (utlatande.getUndersokningYttre() != null || utlatande.getUndersokningDatum() != null) {
            InternalConverterUtil.SvarBuilder undersokning = aSvar(UNDERSOKNING_SVAR_ID);

            if (utlatande.getUndersokningYttre() != null) {
                if (utlatande.getUndersokningYttre() == Undersokning.JA) {
                    undersokning.withDelsvar(UNDERSOKNING_YTTRE_DELSVAR_ID, Boolean.TRUE.toString());
                } else {
                    undersokning.withDelsvar(UNDERSOKNING_YTTRE_DELSVAR_ID, Boolean.FALSE.toString());
                    undersokning.withDelsvar(UNDERSOKNING_DETALJER_DELSVAR_ID,
                            aCV(UNDERSOKNING_DETALJER_CODE_SYSTEM, utlatande.getUndersokningYttre().getTransport(),
                                    utlatande.getUndersokningYttre().getTransport()));
                }
            }
            if (utlatande.getUndersokningDatum() != null) {
                undersokning.withDelsvar(UNDERSOKNING_DATUM_DELSVAR_ID, getInternalDateContent(utlatande.getUndersokningDatum()));
            }
            svar.add(undersokning.build());
        }

        // Svar 7
        addIfNotNull(svar, POLISANMALAN_SVAR_ID, POLISANMALAN_DELSVAR_ID, utlatande.getPolisanmalan());

        for (Tillaggsfraga tillaggsfraga : utlatande.getTillaggsfragor()) {
            addIfNotBlank(svar, tillaggsfraga.getId(), tillaggsfraga.getId() + ".1", tillaggsfraga.getSvar());
        }

        return svar;
    }
}
