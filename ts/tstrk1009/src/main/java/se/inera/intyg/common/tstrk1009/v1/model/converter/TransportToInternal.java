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
package se.inera.intyg.common.tstrk1009.v1.model.converter;

import static com.google.common.base.Strings.emptyToNull;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getBooleanContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.ANMALAN_AVSER_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.ANMALAN_AVSER_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INFORMATION_OM_TS_BESLUT_ONSKAS_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INFORMATION_OM_TS_BESLUT_ONSKAS_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INTYGET_AVSER_BEHORIGHET_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.INTYGET_AVSER_BEHORIGHET_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.MEDICINSKA_FORHALLANDEN_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.MEDICINSKA_FORHALLANDEN_SVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.SENASTE_UNDERSOKNINGSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1009.v1.model.converter.RespConstants.SENASTE_UNDERSOKNINGSDATUM_SVAR_ID;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import java.util.EnumSet;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.tstrk1009.v1.model.internal.AnmalanAvser;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IdKontroll;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IdentitetStyrktGenom;
import se.inera.intyg.common.tstrk1009.v1.model.internal.IntygetAvser;
import se.inera.intyg.common.tstrk1009.v1.model.internal.KorkortBehorighetGrupp;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsbehorighet;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Korkortsolamplighet;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;

public final class TransportToInternal {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToTransport.class);

    private TransportToInternal() {
    }

    public static Tstrk1009UtlatandeV1 convert(final Intyg intygSource) throws ConverterException {
        Tstrk1009UtlatandeV1.Builder utlatande = Tstrk1009UtlatandeV1.builder();
        setMetaData(utlatande, intygSource);
        setSvar(utlatande, intygSource);
        return utlatande.build();
    }

    private static void setMetaData(final Tstrk1009UtlatandeV1.Builder utlatande, final Intyg intygSource) throws ConverterException {
        utlatande.setId(intygSource.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(intygSource, false));
        utlatande.setTextVersion(intygSource.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(intygSource.getUnderskrift()));
    }

    private static void setSvar(final Tstrk1009UtlatandeV1.Builder utlatande, final Intyg intygSource) throws ConverterException {

        final EnumSet<Korkortsbehorighet> korkortsbehorigheter = EnumSet.noneOf(Korkortsbehorighet.class);

        for (final Svar svar : intygSource.getSvar()) {
            switch (svar.getId()) {
                case IDENTITET_STYRKT_GENOM_SVAR_ID:
                    handleIdentitetStyrktGenom(utlatande, svar);
                    break;
                case ANMALAN_AVSER_SVAR_ID:
                    handleAnmalanAvser(utlatande, svar);
                    break;
                case MEDICINSKA_FORHALLANDEN_SVAR_ID:
                    handleMedicinskaForhallanden(utlatande, svar);
                    break;
                case SENASTE_UNDERSOKNINGSDATUM_SVAR_ID:
                    handleSenasteUndersokningsdatum(utlatande, svar);
                    break;
                case INTYGET_AVSER_BEHORIGHET_SVAR_ID:
                    handleIntygetAvserBehorighet(korkortsbehorigheter, svar);
                    break;
                case INFORMATION_OM_TS_BESLUT_ONSKAS_SVAR_ID:
                    handleInformationOmTsBeslutOnskas(utlatande, svar);
                    break;
                default:
                    throw new IllegalArgumentException();
            }

        }

        handleKorkortsBehorigheter(utlatande, korkortsbehorigheter);
    }

    private static void handleIdentitetStyrktGenom(Tstrk1009UtlatandeV1.Builder utlatande, final Svar svar) throws ConverterException {
        for (final Svar.Delsvar delsvar : svar.getDelsvar()) {
            if (IDENTITET_STYRKT_GENOM_DELSVAR_ID.equals(delsvar.getId())) {
                utlatande.setIdentitetStyrktGenom(IdentitetStyrktGenom.create(IdKontroll.fromCode(getCVSvarContent(delsvar).getCode())));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleAnmalanAvser(Tstrk1009UtlatandeV1.Builder utlatande, final Svar svar) throws ConverterException {
        for (final Svar.Delsvar delsvar : svar.getDelsvar()) {
            if (ANMALAN_AVSER_DELSVAR_ID.equals(delsvar.getId())) {
                utlatande.setAnmalanAvser(AnmalanAvser.create(Korkortsolamplighet.fromCode(getCVSvarContent(delsvar).getCode())));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleMedicinskaForhallanden(Tstrk1009UtlatandeV1.Builder utlatande, final Svar svar) {
        for (final Svar.Delsvar delsvar : svar.getDelsvar()) {
            if (MEDICINSKA_FORHALLANDEN_DELSVAR_ID.equals(delsvar.getId())) {
                utlatande.setMedicinskaForhallanden(emptyToNull(getStringContent(delsvar)));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSenasteUndersokningsdatum(Tstrk1009UtlatandeV1.Builder utlatande, final Svar svar) {
        for (final Svar.Delsvar delsvar : svar.getDelsvar()) {
            if (SENASTE_UNDERSOKNINGSDATUM_DELSVAR_ID.equals(delsvar.getId())) {
                utlatande.setSenasteUndersokningsdatum(new InternalDate(getStringContent(delsvar)));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleIntygetAvserBehorighet(
        EnumSet<Korkortsbehorighet> korkortsbehorigheter,
        final Svar svar) throws ConverterException {

        for (final Svar.Delsvar delsvar : svar.getDelsvar()) {
            if (INTYGET_AVSER_BEHORIGHET_DELSVAR_ID.equals(delsvar.getId())) {
                korkortsbehorigheter.add(Korkortsbehorighet.fromCode(getCVSvarContent(delsvar).getCode()));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleInformationOmTsBeslutOnskas(Tstrk1009UtlatandeV1.Builder utlatande, final Svar svar) {
        for (final Svar.Delsvar delsvar : svar.getDelsvar()) {
            if (INFORMATION_OM_TS_BESLUT_ONSKAS_DELSVAR_ID.equals(delsvar.getId())) {
                utlatande.setInformationOmTsBeslutOnskas(getBooleanContent(delsvar));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleKorkortsBehorigheter(
        final Tstrk1009UtlatandeV1.Builder utlatande,
        final EnumSet<Korkortsbehorighet> korkortsbehorigheter) {

        final EnumSet<KorkortBehorighetGrupp> intygetAvserBehorigheter = EnumSet.noneOf(KorkortBehorighetGrupp.class);

        if (korkortsbehorigheter.containsAll(Korkortsbehorighet.getAllaBehorigheter())) {
            intygetAvserBehorigheter.add(KorkortBehorighetGrupp.ALLA);
        } else if (korkortsbehorigheter.containsAll(Korkortsbehorighet.getKanintetastallning())) {
            intygetAvserBehorigheter.add(KorkortBehorighetGrupp.KANINTETASTALLNING);
        } else {

            if (korkortsbehorigheter.containsAll(Korkortsbehorighet.getABTraktorBehorigheter())) {
                intygetAvserBehorigheter.add(KorkortBehorighetGrupp.A_B_TRAKTOR);
            }

            if (korkortsbehorigheter.containsAll(Korkortsbehorighet.getCEBehorigHeter())) {
                intygetAvserBehorigheter.add(KorkortBehorighetGrupp.C_E);
            }

            if (korkortsbehorigheter.containsAll(Korkortsbehorighet.getDBehorigHeter())) {
                intygetAvserBehorigheter.add(KorkortBehorighetGrupp.D);
            }

            if (korkortsbehorigheter.containsAll(Korkortsbehorighet.getTaxiBehorigheter())) {
                intygetAvserBehorigheter.add(KorkortBehorighetGrupp.TAXI);
            }
        }

        if (CollectionUtils.isNotEmpty(intygetAvserBehorigheter)) {
            utlatande.setIntygetAvserBehorigheter(IntygetAvser.create(intygetAvserBehorigheter));
        }
    }
}
