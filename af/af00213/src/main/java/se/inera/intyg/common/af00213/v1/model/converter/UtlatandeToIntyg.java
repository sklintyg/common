/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00213.v1.model.converter;

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_2;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_ID_4;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_1;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_SVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_ID_3;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aSvar;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.addIfNotBlank;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.getTypAvIntyg;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    public static Intyg convert(Af00213UtlatandeV1 utlatande) {
        Intyg intyg = InternalConverterUtil.getIntyg(utlatande, PatientInfo.BASIC);
        intyg.setTyp(getTypAvIntyg(KvIntygstyp.AF00213));
        intyg.getSvar().addAll(getSvar(utlatande));
        intyg.setUnderskrift(InternalConverterUtil.base64StringToUnderskriftType(utlatande));
        return intyg;
    }

    private static List<Svar> getSvar(Af00213UtlatandeV1 source) {
        List<Svar> svars = new ArrayList<>();

        if (source.getHarFunktionsnedsattning() != null) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_SVAR_ID_1)
                .withDelsvar(FUNKTIONSNEDSATTNING_DELSVAR_ID_11, source.getHarFunktionsnedsattning().toString())
                .withDelsvar(FUNKTIONSNEDSATTNING_DELSVAR_ID_12, source.getFunktionsnedsattning())
                .build());
        }

        if (source.getHarAktivitetsbegransning() != null) {
            svars.add(aSvar(AKTIVITETSBEGRANSNING_SVAR_ID_2)
                .withDelsvar(AKTIVITETSBEGRANSNING_DELSVAR_ID_21, source.getHarAktivitetsbegransning().toString())
                .withDelsvar(AKTIVITETSBEGRANSNING_DELSVAR_ID_22, source.getAktivitetsbegransning())
                .build());
        }

        if (source.getHarUtredningBehandling() != null) {
            svars.add(aSvar(UTREDNING_BEHANDLING_SVAR_ID_3)
                .withDelsvar(UTREDNING_BEHANDLING_DELSVAR_ID_31, source.getHarUtredningBehandling().toString())
                .withDelsvar(UTREDNING_BEHANDLING_DELSVAR_ID_32, source.getUtredningBehandling())
                .build());
        }

        if (source.getHarArbetetsPaverkan() != null) {
            svars.add(aSvar(ARBETETS_PAVERKAN_SVAR_ID_4)
                .withDelsvar(ARBETETS_PAVERKAN_DELSVAR_ID_41, source.getHarArbetetsPaverkan().toString())
                .withDelsvar(ARBETETS_PAVERKAN_DELSVAR_ID_42, source.getArbetetsPaverkan())
                .build());
        }

        addIfNotBlank(svars, OVRIGT_SVAR_ID_5, OVRIGT_DELSVAR_ID_5, buildOvrigaUpplysningar(source));

        return svars;
    }

    // Original taken and then modified from luse/../UtlatandeToIntyg.java, INTYG-3024
    private static String buildOvrigaUpplysningar(Af00213UtlatandeV1 source) {
        String ovrigt = null;

        if (!Strings.nullToEmpty(source.getOvrigt()).trim().isEmpty()) {
            ovrigt = source.getOvrigt();
        }
        return ovrigt;
    }

}
