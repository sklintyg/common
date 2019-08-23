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
package se.inera.intyg.common.lisjp.v1.model.converter.prefill;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_OM_DELSVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_1_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BIDIAGNOS_2_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_DELSVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_DELSVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_TILL_ARBETE_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SJUKSKRIVNING_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_CODE_SYSTEM;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_DELSVAR_ID_28;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.childElements;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.parseDelsvarType;

import com.google.common.primitives.Ints;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillResult.PrefillEventType;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1.Builder;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

public class PrefillHandler {

    protected static final String WARNING_INVALID_SVAR_ID = "Ignoring - invalid svar id";
    protected static final String WARNING_INVALID_DELSVAR_ID = "Ignoring - invalid delsvar id";
    protected static final String WARNING_INVALID_DELSVAR_CONTENT = "Ignoring - invalid delsvar content";
    protected static final String WARNING_INVALID_CVTYPE = "Ignoring - invalid CVType";
    protected static final String WARNING_INVALID_CVTYPE_CODE_VALUE = "Ignoring - invalid CVType code value";
    protected static final String WARNING_INVALID_CVTYPE_CODESYSTEM = "Ignoring - invalid CVType codeSystem";
    protected static final String WARNING_INVALID_DATEPERIOD_ATTRIBUTE = "Ignoring - invalid DatePeriod attribute";
    protected static final String WARNING_MISSING_SJUKSKRIVNINGSNIVA = "Ignoring - missing sjukskrivningsniva";
    private static final List<String> VALID_DIAGNOSE_CODESYSTEM_VALUES = Arrays
        .asList(Diagnoskodverk.ICD_10_SE.getCodeSystem(), Diagnoskodverk.KSH_97_P.getCodeSystem());
    private static final Logger LOG = LoggerFactory.getLogger(PrefillHandler.class);
    private static final int TILLAGGSFRAGA_START = 9001;
    private final String intygsId;
    private final String intygsTyp;
    private final String intygsVersion;
    private WebcertModuleService moduleService;

    public PrefillHandler(WebcertModuleService moduleService, String intygsId, String intygsTyp, String intygsVersion) {
        this.moduleService = moduleService;
        this.intygsId = intygsId;
        this.intygsTyp = intygsTyp;
        this.intygsVersion = intygsVersion;
    }


    public PrefillResult prefill(Builder utlatande, Forifyllnad forifyllnad) {

        PrefillResult pr = new PrefillResult(intygsId, intygsTyp, intygsVersion);

        if (forifyllnad != null) {
            List<Diagnos> diagnoser = new ArrayList<>();
            List<Tillaggsfraga> tillaggsfragor = new ArrayList<>();
            List<Sjukskrivning> sjukskrivningar = new ArrayList<>();
            List<Sysselsattning> sysselsattning = new ArrayList<>();
            List<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder = new ArrayList<>();

            for (Svar svar : forifyllnad.getSvar()) {
                try {
                    switch (svar.getId()) {
                        case AVSTANGNING_SMITTSKYDD_SVAR_ID_27:
                            handleAvstangningSmittskydd(utlatande, svar);
                            break;
                        case GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1:
                            handleGrundForMedicinsktUnderlag(utlatande, svar, pr);
                            break;
                        case TYP_AV_SYSSELSATTNING_SVAR_ID_28:
                            handleSysselsattning(sysselsattning, svar);
                            break;
                        case NUVARANDE_ARBETE_SVAR_ID_29:
                            handleNuvarandeArbete(utlatande, svar);
                            break;
                        case DIAGNOS_SVAR_ID_6:
                            handleDiagnos(diagnoser, svar, pr);
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
                            handleBehovAvSjukskrivning(sjukskrivningar, svar, pr);
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
                        case KONTAKT_ONSKAS_SVAR_ID_26:
                            handleOnskarKontakt(utlatande, svar);
                            break;
                        default:
                            Integer parsedInt = Ints.tryParse(svar.getId());
                            if (parsedInt != null && parsedInt >= TILLAGGSFRAGA_START) {
                                handleTillaggsfraga(tillaggsfragor, svar, pr);
                            } else {
                                throw new PrefillWarningException(svar, WARNING_INVALID_SVAR_ID);
                            }
                            break;
                    }
                } catch (PrefillWarningException pw) {
                    pr.addMessage(PrefillEventType.WARNING, pw.getSvarId(), pw.getMessage(), pw.getSourceContent());
                } catch (Exception e) {
                    //This type of errors should be logged to the normal application error log, as it indicates a bug / unhandled
                    // scenario in the prefill process.
                    LOG.error("Unexpected error while processing svar", e);
                    pr.addMessage(PrefillEventType.WARNING, svar, "Ignoring - Unexpected error while processing svar" + e.getMessage());
                }
            }

            utlatande.setSjukskrivningar(sjukskrivningar);
            utlatande.setSysselsattning(sysselsattning);
            utlatande.setArbetslivsinriktadeAtgarder(arbetslivsinriktadeAtgarder);
            utlatande.setDiagnoser(diagnoser);
            utlatande.setTillaggsfragor(tillaggsfragor);
        }

        return pr;
    }

    private void handleAvstangningSmittskydd(Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {

            switch (delsvar.getId()) {
                case AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27:
                    utlatande.setAvstangningSmittskydd(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleGrundForMedicinsktUnderlag(Builder utlatande, Svar svar, PrefillResult pr)
        throws PrefillWarningException {
        InternalDate grundForMedicinsktUnderlagDatum = null;
        RespConstants.ReferensTyp grundForMedicinsktUnderlagTyp = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1:
                    grundForMedicinsktUnderlagDatum = new InternalDate(getStringContent(delsvar));
                    break;
                case GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1:
                    String validatedCVType = getCVTypeCodeFromCodeSystem(delsvar, GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM);
                    try {
                        grundForMedicinsktUnderlagTyp = RespConstants.ReferensTyp.byTransportId(validatedCVType);
                    } catch (Exception e) {
                        throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
                    }

                    break;
                case GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1:
                    utlatande.setAnnatGrundForMUBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }

        if (grundForMedicinsktUnderlagTyp == null) {
            throw new PrefillWarningException(svar,
                "Ignoring - Missing mandatory CVType for " + GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);
        }

        //Fallback handling
        if (grundForMedicinsktUnderlagDatum == null) {
            grundForMedicinsktUnderlagDatum = new InternalDate(LocalDate.now());

            pr.addMessage(PrefillEventType.INFO, svar,
                "No " + GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1 + " date provided - defaulting to " + grundForMedicinsktUnderlagDatum
                    .getDate());
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
            default:
                throw new PrefillWarningException(svar, WARNING_INVALID_CVTYPE);
        }

    }

    private void handleArbetslivsinriktadeAtgarder(List<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder, Svar svar)
        throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40:
                    String arbetslivsinriktadeAtgarderValKod = getCVTypeCodeFromCodeSystem(delsvar,
                        ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM);
                    try {
                        arbetslivsinriktadeAtgarder.add(ArbetslivsinriktadeAtgarder
                            .create(ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.fromId(arbetslivsinriktadeAtgarderValKod)));
                    } catch (Exception e) {
                        throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
                    }
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleArbetslivsinriktadeAtgarderBeskrivning(LisjpUtlatandeV1.Builder utlatande, Svar svar)
        throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_DELSVAR_ID_44:
                    utlatande.setArbetslivsinriktadeAtgarderBeskrivning(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handlePrognos(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        String prognosKod = null;
        String dagarTillArbete = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PROGNOS_BESKRIVNING_DELSVAR_ID_39:
                    prognosKod = getCVTypeCodeFromCodeSystem(delsvar, PROGNOS_CODE_SYSTEM);
                    break;
                case PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39:
                    dagarTillArbete = getCVTypeCodeFromCodeSystem(delsvar, PROGNOS_DAGAR_TILL_ARBETE_CODE_SYSTEM);
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
            if (prognosKod != null) {
                utlatande.setPrognos(Prognos.create(PrognosTyp.fromId(prognosKod),
                    dagarTillArbete != null ? PrognosDagarTillArbeteTyp.fromId(dagarTillArbete) : null));
            }
        }
    }

    private void handleArbetsresor(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSRESOR_OM_DELSVAR_ID_34:
                    //TODO: getValidatedBooleanValue?
                    utlatande.setArbetsresor(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleArbetstidsforlaggning(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID_33:
                    utlatande.setArbetstidsforlaggning(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                case ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33:
                    //TODO: getValidated/SafeStringValue?
                    utlatande.setArbetstidsforlaggningMotivering(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleForsakringsmedicinsktBeslutsstod(LisjpUtlatandeV1.Builder utlatande, Svar svar)
        throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID_37:
                    utlatande.setForsakringsmedicinsktBeslutsstod(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleBehovAvSjukskrivning(List<Sjukskrivning> sjukskrivningar, Svar svar, PrefillResult pr)
        throws PrefillWarningException {
        String sjukskrivningsnivaString = null;
        InternalLocalDateInterval period = null;
        SjukskrivningsGrad sjukskrivningsgrad = null;

        LocalDate defaultStartDate = LocalDate.now();
        for (Delsvar delsvar : svar.getDelsvar()) {
            //We fail at individual delsvar so that other (correct) sjukskrivningsinfo can be processed.
            try {
                switch (delsvar.getId()) {
                    case BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32:
                        sjukskrivningsnivaString = getCVTypeCodeFromCodeSystem(delsvar, SJUKSKRIVNING_CODE_SYSTEM);
                        try {
                            sjukskrivningsgrad = SjukskrivningsGrad.fromId(sjukskrivningsnivaString);
                        } catch (Exception e) {
                            throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
                        }
                        break;
                    case BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32:
                        DatePeriodType datePeriod = getDatePeriodTypeContent(delsvar, defaultStartDate, pr);
                        period = new InternalLocalDateInterval(datePeriod.getStart().toString(), datePeriod.getEnd().toString());
                        break;
                    default:
                        throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
                }
             } catch (PrefillWarningException e) {
                pr.addMessage(PrefillEventType.WARNING, e.getSvarId(), e.getMessage(), e.getSourceContent());
            }
        }

        //Mandatory, otherwise abort delfraga
        if (sjukskrivningsgrad == null) {
            throw new PrefillWarningException(svar, WARNING_MISSING_SJUKSKRIVNINGSNIVA);
        }

        //If we could not find a period, we default to a period with a default startDate and an empty enddate
        if (period == null) {
            pr.addMessage(PrefillEventType.INFO, svar,
                "No DatePeriod provided - defaulting to period with start " + defaultStartDate);
            period = new InternalLocalDateInterval(defaultStartDate.toString(), "");
        }

        sjukskrivningar.add(Sjukskrivning.create(sjukskrivningsgrad, period));

    }


    private DatePeriodType getDatePeriodTypeContent(Delsvar delsvar, LocalDate defaultStartDate, PrefillResult pr)
        throws PrefillWarningException {
        try {
            return parseDelsvarType(delsvar, dpNode -> {
                final DatePeriodType datePeriodType = new DatePeriodType();
                childElements(dpNode, child -> {
                    switch (child.getLocalName()) {
                        case "start":
                            datePeriodType.setStart(LocalDate.parse(child.getTextContent()));
                            break;
                        case "end":
                            datePeriodType.setEnd(LocalDate.parse(child.getTextContent()));
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                });

                //Default startdate handling
                if (Objects.isNull(datePeriodType.getStart())) {
                    pr.addMessage(PrefillEventType.INFO, delsvar, "No startdate provided - defaulting to " + defaultStartDate);
                    datePeriodType.setStart(defaultStartDate);
                }

                return datePeriodType;
            });

        } catch (ConverterException e) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_CONTENT);
        } catch (IllegalArgumentException iae) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_DATEPERIOD_ATTRIBUTE);
        }
    }


    private void handleFunktionsnedsattning(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FUNKTIONSNEDSATTNING_DELSVAR_ID_35:
                    utlatande.setFunktionsnedsattning(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }

    }

    private void handleNuvarandeArbete(Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case NUVARANDE_ARBETE_DELSVAR_ID_29:
                    utlatande.setNuvarandeArbete(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleSysselsattning(List<Sysselsattning> sysselsattning, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TYP_AV_SYSSELSATTNING_DELSVAR_ID_28:
                    String sysselsattningsTypString = getCVTypeCodeFromCodeSystem(delsvar, TYP_AV_SYSSELSATTNING_CODE_SYSTEM);
                    try {
                        sysselsattning.add(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.fromId(sysselsattningsTypString)));
                    } catch (Exception e) {
                        throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
                    }

                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleAktivitetsbegransning(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        //TODO: verify just 1 delsvar?
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case AKTIVITETSBEGRANSNING_DELSVAR_ID_17:
                    utlatande.setAktivitetsbegransning(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handlePagaendeBehandling(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PAGAENDEBEHANDLING_DELSVAR_ID_19:
                    utlatande.setPagaendeBehandling(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handlePlaneradBehandling(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case PLANERADBEHANDLING_DELSVAR_ID_20:
                    utlatande.setPlaneradBehandling(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleOvrigt(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case OVRIGT_DELSVAR_ID_25:
                    utlatande.setOvrigt(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleOnskarKontakt(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case KONTAKT_ONSKAS_DELSVAR_ID_26:
                    utlatande.setKontaktMedFk(Boolean.valueOf(getStringContent(delsvar)));
                    break;
                case ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26:
                    utlatande.setAnledningTillKontakt(getStringContent(delsvar));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    public void handleDiagnos(List<Diagnos> diagnoser, Svar svar, PrefillResult pr) {
        // Huvuddiagnos
        String diagnosKod = null;
        String diagnosKodSystem = null;
        String diagnosDisplayName = null;
        String diagnosBeskrivning = null;
        Diagnoskodverk diagnoskodverk = null;

        // Bi-diagnos 1
        String bidiagnosKod1 = null;
        String bidiagnosKodSystem1 = null;
        String bidiagnosDisplayName1 = null;
        String bidiagnosBeskrivning1 = null;
        Diagnoskodverk bidiagnoskodverk1 = null;

        // Bi-diagnos 2
        String bidiagnosKod2 = null;
        String bidiagnosKodSystem2 = null;
        String bidiagnosDisplayName2 = null;
        String bidiagnosBeskrivning2 = null;
        Diagnoskodverk bidiagnoskodverk2 = null;

        for (Delsvar delsvar : svar.getDelsvar()) {
            //We fail per delsvar, so that other (correct) diagnoseinfo info can be processed
            try {
                switch (delsvar.getId()) {
                    case DIAGNOS_DELSVAR_ID_6:
                        CVType diagnos = getCVTypeFromCodeSystem(delsvar, VALID_DIAGNOSE_CODESYSTEM_VALUES);
                        diagnosKod = diagnos.getCode();
                        diagnosKodSystem = diagnos.getCodeSystem();
                        diagnosDisplayName = diagnos.getDisplayName();
                        diagnoskodverk = Diagnoskodverk.getEnumByCodeSystem(diagnosKodSystem);
                        break;
                    case DIAGNOS_BESKRIVNING_DELSVAR_ID_6:
                        diagnosBeskrivning = getStringContent(delsvar);
                        break;
                    case BIDIAGNOS_1_DELSVAR_ID_6:
                        CVType bidiagnos1 = getCVTypeFromCodeSystem(delsvar, VALID_DIAGNOSE_CODESYSTEM_VALUES);
                        bidiagnosKod1 = bidiagnos1.getCode();
                        bidiagnosKodSystem1 = bidiagnos1.getCodeSystem();
                        bidiagnosDisplayName1 = bidiagnos1.getDisplayName();
                        bidiagnoskodverk1 = Diagnoskodverk.getEnumByCodeSystem(bidiagnosKodSystem1);
                        break;
                    case BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6:
                        bidiagnosBeskrivning1 = getStringContent(delsvar);
                        break;
                    case BIDIAGNOS_2_DELSVAR_ID_6:
                        CVType bidiagnos2 = getCVTypeFromCodeSystem(delsvar, VALID_DIAGNOSE_CODESYSTEM_VALUES);
                        bidiagnosKod2 = bidiagnos2.getCode();
                        bidiagnosKodSystem2 = bidiagnos2.getCodeSystem();
                        bidiagnosDisplayName2 = bidiagnos2.getDisplayName();
                        bidiagnoskodverk2 = Diagnoskodverk.getEnumByCodeSystem(bidiagnosKodSystem2);
                        break;
                    case BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6:
                        bidiagnosBeskrivning2 = getStringContent(delsvar);
                        break;
                    default:
                        throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
                }
            } catch (PrefillWarningException e) {
                pr.addMessage(PrefillEventType.WARNING, e.getSvarId(), e.getMessage(), e.getSourceContent());
            }
        }

        //All potential values are now collected - which we now should prefill and if
        // any diagnoseDescription needs fallback handling, we handle it now

        if (diagnosKod != null) {
            if (StringUtils.isEmpty(diagnosBeskrivning)) {
                diagnosBeskrivning = handleDiagnoseLookup(pr, diagnosKod, diagnoskodverk.name(),
                    DIAGNOS_BESKRIVNING_DELSVAR_ID_6);
            }
            diagnoser.add(Diagnos.create(diagnosKod, diagnoskodverk.toString(), diagnosBeskrivning, diagnosDisplayName));
        }
        if (bidiagnosKod1 != null) {
            if (StringUtils.isEmpty(bidiagnosBeskrivning1)) {
                bidiagnosBeskrivning1 = handleDiagnoseLookup(pr, bidiagnosKod1, bidiagnoskodverk1.name(),
                    BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6);
            }
            diagnoser.add(Diagnos.create(bidiagnosKod1, bidiagnoskodverk1.toString(), bidiagnosBeskrivning1, bidiagnosDisplayName1));

        }
        if (bidiagnosKod2 != null) {
            if (StringUtils.isEmpty(bidiagnosBeskrivning2)) {
                bidiagnosBeskrivning2 = handleDiagnoseLookup(pr, bidiagnosKod2, bidiagnoskodverk2.name(),
                    BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6);
            }
            diagnoser.add(Diagnos.create(bidiagnosKod2, bidiagnoskodverk2.toString(), bidiagnosBeskrivning2, bidiagnosDisplayName2));
        }
    }

    private String handleDiagnoseLookup(PrefillResult pr, String diagnosKod, String diagnosKodSystem, String svarId) {
        String description = moduleService.getDescriptionFromDiagnosKod(diagnosKod, diagnosKodSystem);
        addDiagnoseDescriptionLookupMessage(pr, svarId, diagnosKod, description);
        return description;

    }

    private void addDiagnoseDescriptionLookupMessage(PrefillResult pr, String delsvarsId, String diagnosKod, String diagnosBeskrivning) {
        pr.addMessage(PrefillEventType.INFO, delsvarsId,
            "Missing diagnoseDescription for code " + diagnosKod + " - defaulting with lookup value '"
                + diagnosBeskrivning + "'", "");
    }

    private void handleTillaggsfraga(List<Tillaggsfraga> tillaggsFragor, Svar svar, PrefillResult pr) throws PrefillWarningException {
        // Must only have 1 delsvar per tillaggsfraga
        if (svar.getDelsvar().size() != 1) {
            throw new PrefillWarningException(svar, "Ignoring - Only exactly 1 delsvar per tillaggsfraga is allowed");
        }

        Delsvar delsvar = svar.getDelsvar().get(0);
        if (!delsvar.getId().equals(svar.getId() + ".1")) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
        }

        //TODO: check that tillaggsfraga exists in intygtext?
        tillaggsFragor.add(Tillaggsfraga.create(svar.getId(), getStringContent(delsvar)));
    }


    /**
     * Tries to parse a CVType for an element that at least has a code and (valid) codeSystem.
     * Throws {@link PrefillWarningException} if code or codeSystem is missing, or if {@link ConverterException} occurs
     */
    private CVType getCVTypeFromCodeSystem(Delsvar delsvar, List<String> validCodeSystems) throws PrefillWarningException {
        try {
            final CVType cv = getCVSvarContent(delsvar);

            if (cv == null) {
                throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE);
            }
            if (!validCodeSystems.contains(cv.getCodeSystem())) {
                throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODESYSTEM);
            }
            if (StringUtils.isEmpty(cv.getCode())) {
                throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
            }

            return cv;
        } catch (ConverterException e) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE);
        }
    }

    /**
     * Tries to parse a CVType code for an element that at least has a code and (valid) codeSystem.
     */
    private String getCVTypeCodeFromCodeSystem(Delsvar delsvar, String validCodeSystem) throws PrefillWarningException {
        return getCVTypeFromCodeSystem(delsvar, Arrays.asList(validCodeSystem)).getCode();

    }


}
