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
package se.inera.intyg.common.ag7804.v1.model.converter;

import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSRESOR_OM_DELSVAR_ID_34;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_DELSVAR_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_DELSVAR_ID_25;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_DELSVAR_ID_28;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.ag7804.converter.TransportToInternalUtil.handleDiagnos;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getBooleanContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getDatePeriodTypeContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.common.ag7804.converter.RespConstants;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.Prognos;
import se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.ag7804.model.internal.PrognosTyp;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.support.common.enumerations.PatientInfo;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static Ag7804UtlatandeV1 convert(Intyg source) throws ConverterException {
        Ag7804UtlatandeV1.Builder utlatande = Ag7804UtlatandeV1.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(TransportConverterUtil.getGrundData(source, PatientInfo.BASIC));
        utlatande.setTextVersion(source.getVersion());
        utlatande.setSignature(TransportConverterUtil.signatureTypeToBase64(source.getUnderskrift()));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static void setSvar(Ag7804UtlatandeV1.Builder utlatande, Intyg source) throws ConverterException {
        List<Diagnos> diagnoser = new ArrayList<>();
        List<Sjukskrivning> sjukskrivningar = new ArrayList<>();
        List<Sysselsattning> sysselsattning = new ArrayList<>();
        List<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder = new ArrayList<>();

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case AVSTANGNING_SMITTSKYDD_SVAR_ID_27:
                    handleAvstangningSmyttskydd(utlatande, svar);
                    break;
                case GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1:
                    handleGrundForMedicinsktUnderlag(utlatande, svar);
                    break;
                case TYP_AV_SYSSELSATTNING_SVAR_ID_28:
                    handleSysselsattning(sysselsattning, svar);
                    break;
                case NUVARANDE_ARBETE_SVAR_ID_29:
                    handleNuvarandeArbete(utlatande, svar);
                    break;
                case ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100:
                    handleOnskarFormedlaDiagnos(utlatande, svar);
                    break;
                case DIAGNOS_SVAR_ID_6:
                    handleDiagnos(diagnoser, svar);
                    break;
                case FUNKTIONSNEDSATTNING_SVAR_ID_35:
                    handleFunktionsnedsattning(utlatande, svar);
                    break;
                case AKTIVITETSBEGRANSNING_SVAR_ID_17:
                    handleAktivitetsbegransning(utlatande, svar);
                    break;
                case PAGAENDEBEHANDLING_SVAR_ID_19:
                    handlePagaendeBehandling(utlatande, svar);
                    break;
                case PLANERADBEHANDLING_SVAR_ID_20:
                    handlePlaneradBehandling(utlatande, svar);
                    break;
                case BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32:
                    handleBehovAvSjukskrivning(sjukskrivningar, svar);
                    break;
                case FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37:
                    handleForsakringsmedicinsktBeslutsstod(utlatande, svar);
                    break;
                case ARBETSTIDSFORLAGGNING_SVAR_ID_33:
                    handleArbetstidsforlaggning(utlatande, svar);
                    break;
                case ARBETSRESOR_SVAR_ID_34:
                    handleArbetsresor(utlatande, svar);
                    break;
                case PROGNOS_SVAR_ID_39:
                    handlePrognos(utlatande, svar);
                    break;
                case ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40:
                    handleArbetslivsinriktadeAtgarder(arbetslivsinriktadeAtgarder, svar);
                    break;
                case ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44:
                    handleArbetslivsinriktadeAtgarderBeskrivning(utlatande, svar);
                    break;
                case OVRIGT_SVAR_ID_25:
                    handleOvrigt(utlatande, svar);
                    break;
                case KONTAKT_ONSKAS_SVAR_ID_103:
                    handleOnskarKontakt(utlatande, svar);
                    break;
            }
        }

        utlatande.setSjukskrivningar(sjukskrivningar);
        utlatande.setSysselsattning(sysselsattning);
        utlatande.setArbetslivsinriktadeAtgarder(arbetslivsinriktadeAtgarder);
        utlatande.setDiagnoser(diagnoser);
    }

    private static void handleAvstangningSmyttskydd(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27:
                    utlatande.setAvstangningSmittskydd(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleArbetslivsinriktadeAtgarder(List<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder, Svar svar)
        throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40:
                    String arbetslivsinriktadeAtgarderValKod = getCVSvarContent(delsvar).getCode();
                    arbetslivsinriktadeAtgarder.add(ArbetslivsinriktadeAtgarder
                        .create(ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.fromId(arbetslivsinriktadeAtgarderValKod)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleArbetslivsinriktadeAtgarderBeskrivning(Ag7804UtlatandeV1.Builder utlatande, Svar svar)
        throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44:
                    utlatande.setArbetslivsinriktadeAtgarderBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handlePrognos(Ag7804UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        String prognosKod = null;
        String dagarTillArbete = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PROGNOS_BESKRIVNING_DELSVAR_ID_39:
                    prognosKod = getCVSvarContent(delsvar).getCode();
                    break;
                case PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39:
                    dagarTillArbete = getCVSvarContent(delsvar).getCode();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            if (prognosKod != null) {
                utlatande.setPrognos(Prognos.create(PrognosTyp.fromId(prognosKod),
                    dagarTillArbete != null ? PrognosDagarTillArbeteTyp.fromId(dagarTillArbete) : null));
            }
        }
    }

    private static void handleArbetsresor(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSRESOR_OM_DELSVAR_ID_34:
                    utlatande.setArbetsresor(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleArbetstidsforlaggning(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33:
                    utlatande.setArbetstidsforlaggning(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                case ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33:
                    utlatande.setArbetstidsforlaggningMotivering(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleForsakringsmedicinsktBeslutsstod(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37:
                    utlatande.setForsakringsmedicinsktBeslutsstod(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBehovAvSjukskrivning(List<Sjukskrivning> sjukskrivningar, Svar svar) throws ConverterException {
        String sjukskrivningsnivaString = null;
        InternalLocalDateInterval period = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32:
                    sjukskrivningsnivaString = getCVSvarContent(delsvar).getCode();
                    break;
                case BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32:
                    DatePeriodType datePeriod = getDatePeriodTypeContent(delsvar);
                    period = new InternalLocalDateInterval(datePeriod.getStart().toString(), datePeriod.getEnd().toString());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            if (sjukskrivningsnivaString != null && period != null) {
                sjukskrivningar.add(Sjukskrivning.create(SjukskrivningsGrad.fromId(sjukskrivningsnivaString), period));
            }
        }
    }

    private static void handleOnskarFormedlaDiagnos(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_ID_100:
                    utlatande.setOnskarFormedlaDiagnos(getBooleanContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

    }

    private static void handleFunktionsnedsattning(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FUNKTIONSNEDSATTNING_DELSVAR_ID_35:
                    utlatande.setFunktionsnedsattning(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

    }

    private static void handleNuvarandeArbete(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case NUVARANDE_ARBETE_DELSVAR_ID_29:
                    utlatande.setNuvarandeArbete(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSysselsattning(List<Sysselsattning> sysselsattning, Svar svar) throws ConverterException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TYP_AV_SYSSELSATTNING_DELSVAR_ID_28:
                    String sysselsattningsTypString = getCVSvarContent(delsvar).getCode();
                    sysselsattning.add(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.fromId(sysselsattningsTypString)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static void handleGrundForMedicinsktUnderlag(Ag7804UtlatandeV1.Builder utlatande, Svar svar) throws ConverterException {
        InternalDate grundForMedicinsktUnderlagDatum = null;
        RespConstants.ReferensTyp grundForMedicinsktUnderlagTyp = RespConstants.ReferensTyp.ANNAT;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1:
                    grundForMedicinsktUnderlagDatum = new InternalDate(getStringContent(delsvar));
                    break;
                case GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1:
                    String referensTypString = getCVSvarContent(delsvar).getCode();
                    grundForMedicinsktUnderlagTyp = RespConstants.ReferensTyp.byTransportId(referensTypString);
                    break;
                case GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1:
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

    private static void handleAktivitetsbegransning(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
            case AKTIVITETSBEGRANSNING_DELSVAR_ID_17:
                utlatande.setAktivitetsbegransning(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void handlePagaendeBehandling(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
            case PAGAENDEBEHANDLING_DELSVAR_ID_19:
                utlatande.setPagaendeBehandling(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void handlePlaneradBehandling(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
            case PLANERADBEHANDLING_DELSVAR_ID_20:
                utlatande.setPlaneradBehandling(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void handleOvrigt(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
            case OVRIGT_DELSVAR_ID_25:
                utlatande.setOvrigt(getStringContent(delsvar));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static void handleOnskarKontakt(Ag7804UtlatandeV1.Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case KONTAKT_ONSKAS_DELSVAR_ID_103:
                    utlatande.setKontaktMedAg(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                case ANLEDNING_TILL_KONTAKT_DELSVAR_ID_103:
                    utlatande.setAnledningTillKontakt(getStringContent(delsvar));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

}
