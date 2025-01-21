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
package se.inera.intyg.common.ag114.v1.model.converter;

import static se.inera.intyg.common.ag114.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_9;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_2;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.BEDOMNING_SVAR_ID_7;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_10_3;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_10_2;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_10;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_10_1;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_9;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_9;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_DELSVAR_ID_5;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_ID_5;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.NUVARANDE_ARBETE_DELSVAR_ID_2;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_2;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_3;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_3;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.OVRIGT_DELSVAR_ID_8;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.OVRIGT_SVAR_ID_8;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.TYP_AV_DIAGNOS_SVAR_ID_4;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_DELSVAR_ID_1;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_1;
import static se.inera.intyg.common.ag114.v1.model.converter.TransportToInternalUtil.handleDiagnos;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getDatePeriodTypeContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getGrundData;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getPQSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.ag114.model.converter.RespConstants;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static Ag114UtlatandeV1 convert(Intyg source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }
        Ag114UtlatandeV1.Builder utlatande = Ag114UtlatandeV1.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(getGrundData(source, PatientInfo.BASIC));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(Ag114UtlatandeV1.Builder utlatande, Intyg source) throws ConverterException {

        List<Diagnos> diagnoser = new ArrayList<>();
        List<Sysselsattning> sysselsattningar = new ArrayList<>();

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_10:
                    handleGrundForMedicinsktUnderlag(utlatande, svar);
                    break;
                case TYP_AV_SYSSELSATTNING_SVAR_ID_1:
                    handleSysselsattning(sysselsattningar, svar);
                    break;
                case NUVARANDE_ARBETE_SVAR_ID_2:
                    handleNuvarandeArbete(utlatande, svar);
                    break;
                case ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_3:
                    handleOnskarFormedla(utlatande, svar);
                    break;
                case TYP_AV_DIAGNOS_SVAR_ID_4:
                    handleDiagnos(diagnoser, svar);
                    break;
                case NEDSATT_ARBETSFORMAGA_SVAR_ID_5:
                    handleNedsattArbetsFormaga(utlatande, svar);
                    break;
                case ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID_6:
                    handleArbetsformagaTrotsSjukdom(utlatande, svar);
                    break;
                case BEDOMNING_SVAR_ID_7:
                    handleBedomning(utlatande, svar);
                    break;
                case OVRIGT_SVAR_ID_8:
                    handleOvrigaUpplysningar(utlatande, svar);
                    break;
                case KONTAKT_ONSKAS_SVAR_ID_9:
                    handleOnskarKontakt(utlatande, svar);
                    break;
            }
        }

        utlatande.setSysselsattning(sysselsattningar);
        utlatande.setDiagnoser(diagnoser);
    }

    private static void handleGrundForMedicinsktUnderlag(Ag114UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        InternalDate grundForMedicinsktUnderlagDatum = null;
        RespConstants.ReferensTyp grundForMedicinsktUnderlagTyp = RespConstants.ReferensTyp.ANNAT;
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_10_1:
                    String referensTypString = getCVSvarContent(delsvar).getCode();
                    grundForMedicinsktUnderlagTyp = RespConstants.ReferensTyp.byTransportId(referensTypString);
                    break;
                case GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_10_2:
                    grundForMedicinsktUnderlagDatum = new InternalDate(getStringContent(delsvar));
                    break;
                case GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_10_3:
                    utlatande.setAnnatGrundForMUBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        switch (grundForMedicinsktUnderlagTyp) {
            case UNDERSOKNING:
                utlatande.setUndersokningAvPatienten(grundForMedicinsktUnderlagDatum);
                break;
            case JOURNAL:
                utlatande.setJournaluppgifter(grundForMedicinsktUnderlagDatum);
                break;
            case TELEFONKONTAKT:
                utlatande.setTelefonkontaktMedPatienten(grundForMedicinsktUnderlagDatum);
                break;
            case ANNAT:
                utlatande.setAnnatGrundForMU(grundForMedicinsktUnderlagDatum);
                break;
        }
    }


    private static void handleNuvarandeArbete(Ag114UtlatandeV1.Builder utlatande, Svar svar) {
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case NUVARANDE_ARBETE_DELSVAR_ID_2:
                    utlatande.setNuvarandeArbete(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSysselsattning(List<Sysselsattning> sysselsattning, Svar svar) throws ConverterException {
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TYP_AV_SYSSELSATTNING_DELSVAR_ID_1:
                    String sysselsattningsTypString = getCVSvarContent(delsvar).getCode();
                    sysselsattning.add(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.fromId(sysselsattningsTypString)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOnskarFormedla(Ag114UtlatandeV1.Builder utlatande, Svar svar) {
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_3:
                    utlatande.setOnskarFormedlaDiagnos(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleNedsattArbetsFormaga(Ag114UtlatandeV1.Builder utlatande, Svar svar) {
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case NEDSATT_ARBETSFORMAGA_DELSVAR_ID_5:
                    utlatande.setNedsattArbetsformaga(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleArbetsformagaTrotsSjukdom(Ag114UtlatandeV1.Builder utlatande, Svar svar) {
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_1:
                    utlatande.setArbetsformagaTrotsSjukdom(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                case ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID_6_2:
                    utlatande.setArbetsformagaTrotsSjukdomBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBedomning(Ag114UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {

        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case SJUKSKRIVNINGSGRAD_DELSVAR_ID_7_1:
                    utlatande.setSjukskrivningsgrad(String.valueOf((int) getPQSvarContent(delsvar).getValue()));
                    break;
                case SJUKSKRIVNINGSPERIOD_DELSVAR_ID_7_2:
                    DatePeriodType datePeriod = getDatePeriodTypeContent(delsvar);
                    InternalLocalDateInterval period = new InternalLocalDateInterval(datePeriod.getStart().toString(),
                        datePeriod.getEnd().toString());
                    utlatande.setSjukskrivningsperiod(period);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOvrigaUpplysningar(Ag114UtlatandeV1.Builder utlatande, Svar svar) {
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case OVRIGT_DELSVAR_ID_8:
                    utlatande.setOvrigaUpplysningar(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleOnskarKontakt(Ag114UtlatandeV1.Builder utlatande, Svar svar) {
        for (Svar.Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case KONTAKT_ONSKAS_DELSVAR_ID_9:
                    utlatande.setKontaktMedArbetsgivaren(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                case ANLEDNING_TILL_KONTAKT_DELSVAR_ID_9:
                    utlatande.setAnledningTillKontakt(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

}
