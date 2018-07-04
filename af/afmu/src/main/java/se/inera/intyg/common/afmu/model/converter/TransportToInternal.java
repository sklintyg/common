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

import se.inera.intyg.common.afmu.model.internal.AfmuUtlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import static se.inera.intyg.common.afmu.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_2;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_2;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_1;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_1;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.OVRIGT_SVAR_ID_5;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID_3;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_3;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID_4;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_4;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static AfmuUtlatande convert(Intyg source) throws ConverterException {
        AfmuUtlatande.Builder utlatande = AfmuUtlatande.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, false));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(AfmuUtlatande.Builder utlatande, Intyg source) throws ConverterException {

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
            case FUNKTIONSNEDSATTNING_SVAR_ID_1:
                handleFunktionsnedsattning(utlatande, svar);
                break;
            case AKTIVITETSBEGRANSNING_SVAR_ID_2:
                handleAktivitetsbegransning(utlatande, svar);
                break;
            case PAGAENDEBEHANDLING_SVAR_ID_3:
                handlePagaendeBehandling(utlatande, svar);
                break;
            case PLANERADBEHANDLING_SVAR_ID_4:
                handlePlaneradBehandling(utlatande, svar);
                break;
            case OVRIGT_SVAR_ID_5:
                handleOvrigt(utlatande, svar);
                break;
            default:
                break;
            }
        }
    }

    private static void handleFunktionsnedsattning(AfmuUtlatande.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case FUNKTIONSNEDSATTNING_DELSVAR_ID_1:
                utlatande.setFunktionsnedsattning(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }

    }

    private static void handleAktivitetsbegransning(AfmuUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AKTIVITETSBEGRANSNING_DELSVAR_ID_2:
            utlatande.setAktivitetsbegransning(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePagaendeBehandling(AfmuUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PAGAENDEBEHANDLING_DELSVAR_ID_3:
            utlatande.setPagaendeBehandling(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePlaneradBehandling(AfmuUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PLANERADBEHANDLING_DELSVAR_ID_4:
            utlatande.setPlaneradBehandling(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleOvrigt(AfmuUtlatande.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGT_DELSVAR_ID_5:
            utlatande.setOvrigt(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

}
