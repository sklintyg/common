/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.db.v1.model.converter;

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
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotNull;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getInternalDateContent;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getTypAvIntyg;

import java.util.List;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(DbUtlatandeV1 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.EXTENDED_WITH_ADDRESS_DETAILS_SOURCE);
        intyg.setTyp(getTypAvIntyg(KvIntygstyp.DB));
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }


    private static List<Svar> getSvar(DbUtlatandeV1 utlatande) {
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
                            utlatande.getUndersokningYttre().getBeskrivning()));
                }
            }
            if (utlatande.getUndersokningDatum() != null && utlatande.getUndersokningDatum().isValidDate()) {
                undersokning.withDelsvar(UNDERSOKNING_DATUM_DELSVAR_ID, getInternalDateContent(utlatande.getUndersokningDatum()));
            }
            svar.add(undersokning.build());
        }

        // Svar 7
        addIfNotNull(svar, POLISANMALAN_SVAR_ID, POLISANMALAN_DELSVAR_ID, utlatande.getPolisanmalan());

        return svar;
    }
}
