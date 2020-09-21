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

import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_AVLAGSNAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DETALJER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_DELSVAR_ID;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static DbUtlatandeV1 convert(Intyg intyg) throws ConverterException {
        DbUtlatandeV1.Builder utlatande = DbUtlatandeV1.builder();
        utlatande.setId(intyg.getIntygsId().getExtension());
        utlatande.setTextVersion(intyg.getVersion());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(intyg,
                PatientInfo.EXTENDED_WITH_ADDRESS_DETAILS_SOURCE));
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(intyg.getUnderskrift()));
        setSvar(utlatande, intyg);
        return utlatande.build();
    }

    private static void setSvar(DbUtlatandeV1.Builder utlatande, Intyg intyg) throws ConverterException {
        for (Svar svar : intyg.getSvar()) {
            switch (svar.getId()) {
                case IDENTITET_STYRKT_SVAR_ID:
                    handleIdentitetStyrkt(utlatande, svar);
                    break;
                case DODSDATUM_SVAR_ID:
                    handleDodsdatum(utlatande, svar);
                    break;
                case DODSPLATS_SVAR_ID:
                    handleDodsplats(utlatande, svar);
                    break;
                case BARN_SVAR_ID:
                    handleBarn(utlatande, svar);
                    break;
                case EXPLOSIV_IMPLANTAT_SVAR_ID:
                    handleExplosivtImplantat(utlatande, svar);
                    break;
                case UNDERSOKNING_SVAR_ID:
                    handleUndersokning(utlatande, svar);
                    break;
                case POLISANMALAN_SVAR_ID:
                    handlePolisanmalan(utlatande, svar);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown svar id");
            }
        }
    }

    private static void handlePolisanmalan(DbUtlatandeV1.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        if (delsvar == null) {
            throw new IllegalArgumentException();
        }
        switch (delsvar.getId()) {
            case POLISANMALAN_DELSVAR_ID:
                utlatande.setPolisanmalan(Boolean.valueOf(getStringContent(delsvar)));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void handleUndersokning(DbUtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case UNDERSOKNING_YTTRE_DELSVAR_ID:
                    if (Boolean.parseBoolean(getStringContent(delsvar))) {
                        utlatande.setUndersokningYttre(Undersokning.JA);
                    }
                    break;
                case UNDERSOKNING_DETALJER_DELSVAR_ID:
                    CVType typ = getCVSvarContent(delsvar);
                    utlatande.setUndersokningYttre(Undersokning.fromTransport(typ.getCode()));
                    break;
                case UNDERSOKNING_DATUM_DELSVAR_ID:
                    utlatande.setUndersokningDatum(new InternalDate(getStringContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleExplosivtImplantat(DbUtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case EXPLOSIV_IMPLANTAT_DELSVAR_ID:
                    utlatande.setExplosivImplantat(Boolean.parseBoolean(getStringContent(delsvar)));
                    break;
                case EXPLOSIV_AVLAGSNAT_DELSVAR_ID:
                    utlatande.setExplosivAvlagsnat(Boolean.parseBoolean(getStringContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBarn(DbUtlatandeV1.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        if (delsvar == null) {
            throw new IllegalArgumentException();
        }
        switch (delsvar.getId()) {
            case BARN_DELSVAR_ID:
                utlatande.setBarn(Boolean.valueOf(getStringContent(delsvar)));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void handleDodsplats(DbUtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case DODSPLATS_KOMMUN_DELSVAR_ID:
                    utlatande.setDodsplatsKommun(getStringContent(delsvar));
                    break;
                case DODSPLATS_BOENDE_DELSVAR_ID:
                    CVType typ = getCVSvarContent(delsvar);
                    utlatande.setDodsplatsBoende(DodsplatsBoende.valueOf(typ.getCode()));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleDodsdatum(DbUtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case DODSDATUM_SAKERT_DELSVAR_ID:
                    utlatande.setDodsdatumSakert(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                case DODSDATUM_DELSVAR_ID:
                    utlatande.setDodsdatum(new InternalDate(getStringContent(delsvar)));
                    break;
                case ANTRAFFAT_DOD_DATUM_DELSVAR_ID:
                    utlatande.setAntraffatDodDatum(new InternalDate(getStringContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleIdentitetStyrkt(DbUtlatandeV1.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        if (delsvar == null) {
            throw new IllegalArgumentException();
        }
        switch (delsvar.getId()) {
            case IDENTITET_STYRKT_DELSVAR_ID:
                utlatande.setIdentitetStyrkt(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
