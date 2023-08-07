/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillUtils.getValidatedBoolean;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillUtils.getValidatedCVTypeCodeContent;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillUtils.getValidatedCVTypeContent;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillUtils.getValidatedDatePeriodTypeContent;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillUtils.getValidatedDateString;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillUtils.getValidatedString;
import static se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillUtils.nullToEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import se.inera.intyg.common.fkparent.model.converter.RespConstants;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.lisjp.v1.model.converter.prefill.PrefillResult.PrefillEventType;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1.Builder;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

/**
 * Decorates a {@link LisjpUtlatandeV1} with corresponding values from a {@link Forifyllnad} element.
 * This process is similar to that of what {@link se.inera.intyg.common.lisjp.v1.model.converter.TransportToInternal} executes when
 * converting
 * a stored signed {@link se.riv.clinicalprocess.healthcond.certificate.v3.Intyg} to a {@link LisjpUtlatandeV1}.
 *
 * Since the CreateDraftCertificate schema is rather relaxed about what a Delsvar element may contain and the source is not a fully
 * validated
 * intyg, there are a lot more errorhandling and defensive measures in place to make sure the creation of a draft is not stopped by any
 * problems during the
 * prefill process.
 */
public class PrefillHandler {

    static final String WARNING_INVALID_CVTYPE = "Ignoring - invalid CVType";
    static final String WARNING_INVALID_CVTYPE_CODE_VALUE = "Ignoring - invalid CVType code value";
    static final String WARNING_INVALID_CVTYPE_CODESYSTEM = "Ignoring - invalid CVType codeSystem";
    static final String WARNING_INVALID_BOOLEAN_FIELD = "Ignoring - expected a String with 'true'/'false'";
    static final String WARNING_INVALID_STRING_FIELD = "Ignoring - expected a String delsvar element";
    static final String WARNING_INVALID_STRING_MAXLENGTH = "Ignoring - expected a String delsvar with maxlength of %s but was %s";
    static final String WARNING_INVALID_DATEPERIOD_CONTENT = "Ignoring - failed to parse DatePeriod";
    static final String WARNING_INVALID_DATE_CONTENT = "Ignoring - expected a date string in format yyyy-MM-dd";
    static final String WARNING_MISSING_DATE_DEFAULTING_TO = "No valid yyyy-MM-dd date provided for delsvar %s - defaulting to %s";
    static final String INFO_DUPLICATE_VALUES = "Value already exists and will be ignored";
    private static final String WARNING_MISSING_MANDATORY_CVTYPE_FOR_DELSVAR = "Ignoring - Missing mandatory CVType for delsvar %s";
    private static final String WARNING_INVALID_SVAR_ID = "Ignoring - invalid svar id";
    private static final String WARNING_INVALID_DELSVAR_ID = "Ignoring - invalid delsvar id";
    private static final String WARNING_MISSING_SJUKSKRIVNINGSNIVA = "Ignoring - missing sjukskrivningsniva";
    private static final List<String> VALID_DIAGNOSE_CODESYSTEM_VALUES = Arrays.asList(Diagnoskodverk.ICD_10_SE.getCodeSystem());
    private static final Logger LOG = LoggerFactory.getLogger(PrefillHandler.class);
    private static final int DEFAULT_FREETEXT_MAXLENGTH = 4000;
    private static final int DEFAULT_DIAGNOSDESCRIPTION_MAXLENGTH = 81;
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
            //These properties are lists and are collected throughout the loop and added after all Svar has been processed.
            List<Diagnos> diagnoser = new ArrayList<>();
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
                            handleSysselsattning(sysselsattning, svar, pr);
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
                            handleArbetslivsinriktadeAtgarder(arbetslivsinriktadeAtgarder, svar, pr);
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
                            throw new PrefillWarningException(svar, WARNING_INVALID_SVAR_ID);
                    }
                } catch (PrefillWarningException pw) {
                    pr.addMessage(PrefillEventType.WARNING, pw.getSvarId(), pw.getMessage(), pw.getSourceContent());
                } catch (Exception e) {
                    LOG.error(
                        "Unexpected error while processing svar (NOTE: this indicates a bug or unhandled scenario in the prefill process!)",
                        e);
                    pr.addMessage(PrefillEventType.WARNING, svar, "Ignoring - Unexpected error while processing svar " + e.getMessage());
                }
            }

            utlatande.setSjukskrivningar(sjukskrivningar);
            utlatande.setSysselsattning(sysselsattning);
            utlatande.setArbetslivsinriktadeAtgarder(arbetslivsinriktadeAtgarder);
            utlatande.setDiagnoser(diagnoser);
        }

        return pr;
    }

    private void handleAvstangningSmittskydd(Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {

            switch (delsvar.getId()) {
                case AVSTANGNING_SMITTSKYDD_DELSVAR_ID_27:
                    utlatande.setAvstangningSmittskydd(getValidatedBoolean(delsvar));
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
            //We fail at individual delsvar so that other (correct) delsvarsinfo can be processed.
            try {
                switch (delsvar.getId()) {
                    case GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1:
                        grundForMedicinsktUnderlagDatum = new InternalDate(getValidatedDateString(delsvar));
                        break;
                    case GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1:
                        String validatedCVType = getValidatedCVTypeCodeContent(delsvar, GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM);
                        try {
                            grundForMedicinsktUnderlagTyp = RespConstants.ReferensTyp.byTransportId(validatedCVType);
                        } catch (Exception e) {
                            throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
                        }

                        break;
                    case GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1:
                        utlatande.setAnnatGrundForMUBeskrivning(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
                        break;
                    default:
                        throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
                }
            } catch (PrefillWarningException e) {
                pr.addMessage(PrefillEventType.WARNING, e.getSvarId(), e.getMessage(), e.getSourceContent());
            }
        }

        if (grundForMedicinsktUnderlagTyp == null) {
            throw new PrefillWarningException(svar,
                String.format(WARNING_MISSING_MANDATORY_CVTYPE_FOR_DELSVAR, GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1));
        }

        //Fallback handling
        if (grundForMedicinsktUnderlagDatum == null || StringUtils.isEmpty(grundForMedicinsktUnderlagDatum.getDate())) {
            grundForMedicinsktUnderlagDatum = new InternalDate(LocalDate.now());

            pr.addMessage(PrefillEventType.INFO, svar,
                String.format(WARNING_MISSING_DATE_DEFAULTING_TO, GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1,
                    grundForMedicinsktUnderlagDatum.getDate()));

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

    private void handleArbetslivsinriktadeAtgarder(List<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder, Svar svar,
        PrefillResult pr)
        throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID_40:
                    String arbetslivsinriktadeAtgarderValKod = getValidatedCVTypeCodeContent(delsvar,
                        ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM);
                    try {
                        final var arbetslivsinriktadAtgard = ArbetslivsinriktadeAtgarder
                            .create(ArbetslivsinriktadeAtgarderVal.fromId(arbetslivsinriktadeAtgarderValKod));
                        addIfNotPresentOrElseLog(arbetslivsinriktadeAtgarder, arbetslivsinriktadAtgard, pr, svar);
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
                    utlatande.setArbetslivsinriktadeAtgarderBeskrivning(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
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
                    prognosKod = getValidatedCVTypeCodeContent(delsvar, PROGNOS_CODE_SYSTEM);
                    break;
                case PROGNOS_DAGAR_TILL_ARBETE_DELSVAR_ID_39:
                    dagarTillArbete = getValidatedCVTypeCodeContent(delsvar, PROGNOS_DAGAR_TILL_ARBETE_CODE_SYSTEM);
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
                    utlatande.setArbetsresor(getValidatedBoolean(delsvar));
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
                    utlatande.setArbetstidsforlaggning(getValidatedBoolean(delsvar));
                    break;
                case ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33:
                    utlatande.setArbetstidsforlaggningMotivering(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
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
                    utlatande.setForsakringsmedicinsktBeslutsstod(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
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
                        sjukskrivningsnivaString = getValidatedCVTypeCodeContent(delsvar, SJUKSKRIVNING_CODE_SYSTEM);
                        try {
                            sjukskrivningsgrad = SjukskrivningsGrad.fromId(sjukskrivningsnivaString);
                        } catch (Exception e) {
                            throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
                        }
                        break;
                    case BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32:
                        DatePeriodType datePeriod = getValidatedDatePeriodTypeContent(delsvar, defaultStartDate, pr);
                        period = new InternalLocalDateInterval(nullToEmpty(datePeriod.getStart()), nullToEmpty(datePeriod.getEnd()));
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
                String.format(WARNING_MISSING_DATE_DEFAULTING_TO, BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID_32, defaultStartDate));
            period = new InternalLocalDateInterval(defaultStartDate.toString(), "");
        }
        final var sjukskrivning = Sjukskrivning.create(sjukskrivningsgrad, period);
        addIfNotPresentOrElseLog(sjukskrivningar, sjukskrivning, pr, svar);
    }


    private void handleFunktionsnedsattning(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case FUNKTIONSNEDSATTNING_DELSVAR_ID_35:
                    utlatande.setFunktionsnedsattning(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
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
                    utlatande.setNuvarandeArbete(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleSysselsattning(List<Sysselsattning> sysselsattningar, Svar svar, PrefillResult pr) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case TYP_AV_SYSSELSATTNING_DELSVAR_ID_28:
                    String sysselsattningsTypString = getValidatedCVTypeCodeContent(delsvar,
                        TYP_AV_SYSSELSATTNING_CODE_SYSTEM);
                    try {
                        final var sysselsattning = Sysselsattning.create(SysselsattningsTyp.fromId(sysselsattningsTypString));
                        addIfNotPresentOrElseLog(sysselsattningar, sysselsattning, pr, svar);
                    } catch (Exception e) {
                        throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE);
                    }

                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    public static <T> void addIfNotPresentOrElseLog(List<T> list, T entry, PrefillResult pr, Svar svar) {
        if (list.contains(entry)) {
            pr.addMessage(PrefillEventType.INFO, svar, INFO_DUPLICATE_VALUES);
        } else {
            list.add(entry);
        }
    }

    private void handleAktivitetsbegransning(LisjpUtlatandeV1.Builder utlatande, Svar svar) throws PrefillWarningException {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
                case AKTIVITETSBEGRANSNING_DELSVAR_ID_17:
                    utlatande.setAktivitetsbegransning(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
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
                    utlatande.setPagaendeBehandling(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
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
                    utlatande.setPlaneradBehandling(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
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
                    utlatande.setOvrigt(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
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
                    utlatande.setKontaktMedFk(getValidatedBoolean(delsvar));
                    break;
                case ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26:
                    utlatande.setAnledningTillKontakt(getValidatedString(delsvar, DEFAULT_FREETEXT_MAXLENGTH));
                    break;
                default:
                    throw new PrefillWarningException(delsvar, WARNING_INVALID_DELSVAR_ID);
            }
        }
    }

    private void handleDiagnos(List<Diagnos> diagnoser, Svar svar, PrefillResult pr) {
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
                        CVType diagnos = getValidatedCVTypeContent(delsvar, VALID_DIAGNOSE_CODESYSTEM_VALUES);
                        //Also verify that the supplied code value is one we have in our diagnose repository
                        ensureKnownAndValidDiagnosisCode(delsvar, diagnos);
                        diagnosKod = diagnos.getCode();
                        diagnosKodSystem = diagnos.getCodeSystem();
                        diagnosDisplayName = diagnos.getDisplayName();
                        diagnoskodverk = Diagnoskodverk.getEnumByCodeSystem(diagnosKodSystem);
                        break;
                    case DIAGNOS_BESKRIVNING_DELSVAR_ID_6:
                        diagnosBeskrivning = getValidatedString(delsvar, DEFAULT_DIAGNOSDESCRIPTION_MAXLENGTH);
                        break;
                    case BIDIAGNOS_1_DELSVAR_ID_6:
                        CVType bidiagnos1 = getValidatedCVTypeContent(delsvar, VALID_DIAGNOSE_CODESYSTEM_VALUES);
                        ensureKnownAndValidDiagnosisCode(delsvar, bidiagnos1);
                        bidiagnosKod1 = bidiagnos1.getCode();
                        bidiagnosKodSystem1 = bidiagnos1.getCodeSystem();
                        bidiagnosDisplayName1 = bidiagnos1.getDisplayName();
                        bidiagnoskodverk1 = Diagnoskodverk.getEnumByCodeSystem(bidiagnosKodSystem1);
                        break;
                    case BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6:
                        bidiagnosBeskrivning1 = getValidatedString(delsvar, DEFAULT_DIAGNOSDESCRIPTION_MAXLENGTH);
                        break;
                    case BIDIAGNOS_2_DELSVAR_ID_6:
                        CVType bidiagnos2 = getValidatedCVTypeContent(delsvar, VALID_DIAGNOSE_CODESYSTEM_VALUES);
                        ensureKnownAndValidDiagnosisCode(delsvar, bidiagnos2);
                        bidiagnosKod2 = bidiagnos2.getCode();
                        bidiagnosKodSystem2 = bidiagnos2.getCodeSystem();
                        bidiagnosDisplayName2 = bidiagnos2.getDisplayName();
                        bidiagnoskodverk2 = Diagnoskodverk.getEnumByCodeSystem(bidiagnosKodSystem2);
                        break;
                    case BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6:
                        bidiagnosBeskrivning2 = getValidatedString(delsvar, DEFAULT_DIAGNOSDESCRIPTION_MAXLENGTH);
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
            final var diagnos = Diagnos.create(diagnosKod, diagnoskodverk.toString(), diagnosBeskrivning, diagnosDisplayName);
            addIfNotPresentOrElseLog(diagnoser, diagnos, pr, svar);
        }
        if (bidiagnosKod1 != null) {
            if (StringUtils.isEmpty(bidiagnosBeskrivning1)) {
                bidiagnosBeskrivning1 = handleDiagnoseLookup(pr, bidiagnosKod1, bidiagnoskodverk1.name(),
                    BIDIAGNOS_1_BESKRIVNING_DELSVAR_ID_6);
            }
            final var diagnos = Diagnos.create(bidiagnosKod1, bidiagnoskodverk1.toString(), bidiagnosBeskrivning1, bidiagnosDisplayName1);
            addIfNotPresentOrElseLog(diagnoser, diagnos, pr, svar);
        }
        if (bidiagnosKod2 != null) {
            if (StringUtils.isEmpty(bidiagnosBeskrivning2)) {
                bidiagnosBeskrivning2 = handleDiagnoseLookup(pr, bidiagnosKod2, bidiagnoskodverk2.name(),
                    BIDIAGNOS_2_BESKRIVNING_DELSVAR_ID_6);
            }
            final var diagnos = Diagnos.create(bidiagnosKod2, bidiagnoskodverk2.toString(), bidiagnosBeskrivning2, bidiagnosDisplayName2);
            addIfNotPresentOrElseLog(diagnoser, diagnos, pr, svar);
        }
    }

    private void ensureKnownAndValidDiagnosisCode(Delsvar delsvar, CVType diagnos) throws PrefillWarningException {
        if (!moduleService.validateDiagnosisCodeFormat(diagnos.getCode())
            || !moduleService.validateDiagnosisCode(diagnos.getCode(), Diagnoskodverk.getEnumByCodeSystem(diagnos.getCodeSystem()))) {
            throw new PrefillWarningException(delsvar, WARNING_INVALID_CVTYPE_CODE_VALUE + " for diagnosis code " + diagnos.getCode());
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


}
