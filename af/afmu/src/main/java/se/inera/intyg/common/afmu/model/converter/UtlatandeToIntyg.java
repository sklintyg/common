/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.afmu.model.converter;

import com.google.common.base.Strings;
import se.inera.intyg.common.afmu.model.internal.AfmuUtlatande;
import se.inera.intyg.common.afmu.support.AfmuEntryPoint;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.afmu.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_2;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_ID_4;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_1;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.OVRIGT_SVAR_ID_5;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_ID_3;
import static se.inera.intyg.common.support.Constants.KV_INTYGSTYP_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(AfmuUtlatande utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, false);
        intyg.setTyp(getTypAvIntyg(utlatande));
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static TypAvIntyg getTypAvIntyg(AfmuUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(KV_INTYGSTYP_CODE_SYSTEM);
        typAvIntyg.setDisplayName(AfmuEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static List<Svar> getSvar(AfmuUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_SVAR_ID_1, FUNKTIONSNEDSATTNING_DELSVAR_ID_12, source.getFunktionsnedsattning());
        addIfNotBlank(svars, AKTIVITETSBEGRANSNING_SVAR_ID_2, AKTIVITETSBEGRANSNING_DELSVAR_ID_22, source.getAktivitetsbegransning());
        addIfNotBlank(svars, UTREDNING_BEHANDLING_SVAR_ID_3, UTREDNING_BEHANDLING_DELSVAR_ID_32, source.getUtredningBehandling());
        addIfNotBlank(svars, ARBETETS_PAVERKAN_SVAR_ID_4, ARBETETS_PAVERKAN_DELSVAR_ID_42, source.getArbetetsPaverkan());

        addIfNotBlank(svars, OVRIGT_SVAR_ID_5, OVRIGT_DELSVAR_ID_5, buildOvrigaUpplysningar(source));

        return svars;
    }

    // Original taken and then modified from luse/../UtlatandeToIntyg.java, INTYG-3024
    private static String buildOvrigaUpplysningar(AfmuUtlatande source) {
        String motiveringTillInteBaseratPaUndersokning = null;
        String motiveringTillTidigSjukskrivning = null;
        String ovrigt = null;

        if (!Strings.nullToEmpty(source.getOvrigt()).trim().isEmpty()) {
            ovrigt = source.getOvrigt();
        }
        return ovrigt;
    }

}
