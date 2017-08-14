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
package se.inera.intyg.common.sos_doi.model.converter;

import com.google.common.primitives.Ints;
import se.inera.intyg.common.sos_doi.model.internal.BidragandeSjukdom;
import se.inera.intyg.common.sos_doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande.Builder;
import se.inera.intyg.common.sos_doi.model.internal.Foljd;
import se.inera.intyg.common.sos_doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.sos_doi.model.internal.OmOperation;
import se.inera.intyg.common.sos_doi.model.internal.Specifikation;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SPECIFIKATION_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_SVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UPPGIFT_SAKNAS_CODE;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

public final class TransportToInternal {
    private static final int TILLAGGSFRAGA_START = 9001;

    private TransportToInternal() {
    }

    public static DoiUtlatande convert(Intyg intyg) throws ConverterException {
        Builder utlatande = DoiUtlatande.builder();
        utlatande.setId(intyg.getIntygsId().getExtension());
        utlatande.setTextVersion(intyg.getVersion());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(intyg, true));
        setSvar(utlatande, intyg);
        return utlatande.build();
    }

    private static void setSvar(Builder utlatande, Intyg intyg) throws ConverterException {
        List<Foljd> foljd = new ArrayList<>();
        List<BidragandeSjukdom> bidragandeSjukdomar = new ArrayList<>();
        List<Dodsorsaksgrund> grunder = new ArrayList<>();
        List<Tillaggsfraga> tillaggsfragor = new ArrayList<>();

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
            case DODSORSAK_SVAR_ID:
                handleDodsorsak(utlatande, svar);
                break;
            case FOLJD_SVAR_ID:
                handleFoljd(foljd, svar);
                break;
            case BIDRAGANDE_SJUKDOM_SVAR_ID:
                handleBidragandeSjukdom(bidragandeSjukdomar, svar);
                break;
            case OPERATION_SVAR_ID:
                handleOperation(utlatande, svar);
                break;
            case FORGIFTNING_SVAR_ID:
                handleForgiftning(utlatande, svar);
                break;
            case GRUNDER_SVAR_ID:
                handleGrund(grunder, svar);
                break;
            case LAND_SVAR_ID:
                handleLand(utlatande, svar);
                break;
            default:
                Integer parsedInt = Ints.tryParse(svar.getId());
                if (parsedInt != null && parsedInt >= TILLAGGSFRAGA_START) {
                    handleTillaggsfraga(tillaggsfragor, svar);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }

        utlatande.setFoljd(foljd);
        utlatande.setBidragandeSjukdomar(bidragandeSjukdomar);
        utlatande.setGrunder(grunder);
        utlatande.setTillaggsfragor(tillaggsfragor);
    }

    private static void handleLand(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        if (delsvar == null) {
            throw new IllegalArgumentException();
        }
        switch (delsvar.getId()) {
        case LAND_DELSVAR_ID:
            utlatande.setLand(getStringContent(delsvar));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleGrund(List<Dodsorsaksgrund> grunder, Svar svar) throws ConverterException {
        Delsvar delsvar = svar.getDelsvar().get(0);
        if (delsvar == null) {
            throw new IllegalArgumentException();
        }
        switch (delsvar.getId()) {
        case GRUNDER_DELSVAR_ID:
            grunder.add(Dodsorsaksgrund.valueOf(getCVSvarContent(delsvar).getCode()));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleForgiftning(Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case FORGIFTNING_OM_DELSVAR_ID:
                utlatande.setForgiftning(Boolean.parseBoolean(getStringContent(delsvar)));
                break;
            case FORGIFTNING_ORSAK_DELSVAR_ID:
                utlatande.setForgiftningOrsak(ForgiftningOrsak.valueOf(getCVSvarContent(delsvar).getCode()));
                break;
            case FORGIFTNING_DATUM_DELSVAR_ID:
                utlatande.setForgiftningDatum(new InternalDate(getStringContent(delsvar)));
                break;
            case FORGIFTNING_UPPKOMMELSE_DELSVAR_ID:
                utlatande.setForgiftningUppkommelse(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOperation(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case OPERATION_OM_DELSVAR_ID:
                String content = getStringContent(delsvar);
                if (UPPGIFT_SAKNAS_CODE.equals(content)) {
                    utlatande.setOperation(OmOperation.UPPGIFT_SAKNAS);
                } else {
                    utlatande.setOperation(Boolean.parseBoolean(content) ? OmOperation.JA : OmOperation.NEJ);
                }
                break;
            case OPERATION_DATUM_DELSVAR_ID:
                utlatande.setOperationDatum(new InternalDate(getStringContent(delsvar)));
                break;
            case OPERATION_ANLEDNING_DELSVAR_ID:
                utlatande.setOperationAnledning(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBidragandeSjukdom(List<BidragandeSjukdom> bidragandeSjukdomar, Svar svar) throws ConverterException {
        String description = null;
        InternalDate date = null;
        Specifikation specification = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case BIDRAGANDE_SJUKDOM_OM_DELSVAR_ID:
                description = getStringContent(delsvar);
                break;
            case BIDRAGANDE_SJUKDOM_DATUM_DELSVAR_ID:
                date = new InternalDate(getStringContent(delsvar));
                break;
            case BIDRAGANDE_SJUKDOM_SPECIFIKATION_DELSVAR_ID:
                specification = Specifikation.fromId(getCVSvarContent(delsvar).getCode());
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        bidragandeSjukdomar.add(BidragandeSjukdom.create(description, date, specification));
    }

    private static void handleFoljd(List<Foljd> foljd, Svar svar) throws ConverterException {
        String description = null;
        InternalDate date = null;
        Specifikation specification = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case FOLJD_OM_DELSVAR_ID:
                description = getStringContent(delsvar);
                break;
            case FOLJD_DATUM_DELSVAR_ID:
                date = new InternalDate(getStringContent(delsvar));
                break;
            case FOLJD_SPECIFIKATION_DELSVAR_ID:
                specification = Specifikation.fromId(getCVSvarContent(delsvar).getCode());
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        foljd.add(Foljd.create(description, date, specification));
    }

    private static void handleDodsorsak(Builder utlatande, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case DODSORSAK_DELSVAR_ID:
                utlatande.setDodsorsak(getStringContent(delsvar));
                break;
            case DODSORSAK_DATUM_DELSVAR_ID:
                utlatande.setDodsorsakDatum(new InternalDate(getStringContent(delsvar)));
                break;
            case DODSORSAK_SPECIFIKATION_DELSVAR_ID:
                utlatande.setDodsorsakSpecifikation(Specifikation.fromId(getCVSvarContent(delsvar).getCode()));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBarn(Builder utlatande, Svar svar) {
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

    private static void handleDodsplats(Builder utlatande, Svar svar) throws ConverterException {
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

    private static void handleDodsdatum(Builder utlatande, Svar svar) {
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

    private static void handleIdentitetStyrkt(Builder utlatande, Svar svar) {
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

    private static void handleTillaggsfraga(List<Tillaggsfraga> tillaggsfragor, Svar svar) {
        // En tilläggsfråga har endast ett delsvar
        if (svar.getDelsvar().size() > 1) {
            throw new IllegalArgumentException();
        }

        Delsvar delsvar = svar.getDelsvar().get(0);
        // Kontrollera att ID matchar
        if (delsvar.getId().equals(svar.getId() + ".1")) {
            tillaggsfragor.add(Tillaggsfraga.create(svar.getId(), getStringContent(delsvar)));
        } else {
            throw new IllegalArgumentException();
        }
    }
}
