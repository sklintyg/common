/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.luae_na.v1.validator;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_SVAR_JSON_ID_18;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSGRUND_SVAR_JSON_ID_7;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KANNEDOM_DELSVAR_ID_2;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KANNEDOM_SVAR_JSON_ID_2;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_JSON_ID_5;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SUBSTANSINTAG_SVAR_JSON_ID_21;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_DELSVAR_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_JSON_ID_4;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.SJUKDOMSFORLOPP_DELSVAR_ID_5;
import static se.inera.intyg.common.luae_na.v1.model.converter.RespConstants.UNDERLAG_TYP_DELSVAR_ID_4;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

@Component(value = "luae_na.InternalDraftValidatorImpl.v1")
public class InternalDraftValidatorImpl implements InternalDraftValidator<LuaenaUtlatandeV1> {

    private static final int MAX_UNDERLAG = 3;
    private static final String CATEGORY_GRUNDFORMU = "grundformu";
    private static final String CATEGORY_SJUKDOMSFORLOPP = "sjukdomsforlopp";
    private static final String CATEGORY_DIAGNOS = "diagnos";
    private static final String CATEGORY_AKTIVITETSBEGRANSNING = "aktivitetsbegransning";
    private static final String CATEGORY_MEDICINSKABEHANDLINGAR = "medicinskabehandlingar";
    private static final String CATEGORY_KONTAKT = "kontakt";

    @Autowired
    ValidatorUtilFK validatorUtilFK;

    @Override
    public ValidateDraftResponse validateDraft(LuaenaUtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);
        // Kategori 2 – Andra medicinska utredningar och underlag
        validateUnderlag(utlatande, validationMessages);
        // Kategori 3 – Diagnos
        validatorUtilFK.validateDiagnose(utlatande.getDiagnoser(), validationMessages);
        // Kategori 4 – Sjukdomsförlopp
        validateSjukdomsforlopp(utlatande, validationMessages);
        // Diagnosgrund
        validateDiagnosgrund(utlatande, validationMessages);
        // Kategori 5 – Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);
        // Kategori 6 – Aktivitetsbegränsning
        validateAktivitetsbegransning(utlatande, validationMessages);
        // Kategori 7 – Medicinska behandlingar/åtgärder
        // Kategori 8 – Medicinska förutsättningar för arbete
        validateMedicinskaForutsattningarForArbete(utlatande, validationMessages);
        // Kategori 9 – Övrigt
        // Kategori 10 – Kontakt
        validateKontaktMedFk(utlatande, validationMessages);
        // vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        validateBlanksForOptionalFields(utlatande, validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateGrundForMU(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {

        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getJournaluppgifter() == null
            && utlatande.getAnhorigsBeskrivningAvPatienten() == null && utlatande.getAnnatGrundForMU() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1,
                ValidationMessageType.EMPTY, GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);
        }

        if (utlatande.getUndersokningAvPatienten() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages,
                ValidatorUtilFK.GrundForMu.UNDERSOKNING);
        }
        if (utlatande.getJournaluppgifter() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages,
                ValidatorUtilFK.GrundForMu.JOURNALUPPGIFTER);
        }
        if (utlatande.getAnhorigsBeskrivningAvPatienten() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getAnhorigsBeskrivningAvPatienten(), validationMessages,
                ValidatorUtilFK.GrundForMu.ANHORIGSBESKRIVNING);
        }
        if (utlatande.getAnnatGrundForMU() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getAnnatGrundForMU(), validationMessages, ValidatorUtilFK.GrundForMu.ANNAT);
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && Strings.nullToEmpty(utlatande.getAnnatGrundForMUBeskrivning()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, ValidationMessageType.EMPTY,
                GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1);
        }
        // R3
        if (utlatande.getAnnatGrundForMU() == null && !Strings.isNullOrEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, ValidationMessageType.EMPTY,
                "luae_na.validation.grund-for-mu.incorrect_combination_annat_beskrivning",
                GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1);
        }

        if (utlatande.getKannedomOmPatient() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
                ValidationMessageType.EMPTY,
                KANNEDOM_DELSVAR_ID_2);
        } else {
            boolean dateIsValid = ValidatorUtil.validateDateAndCheckIfFuture(utlatande.getKannedomOmPatient(), validationMessages,
                CATEGORY_GRUNDFORMU, "kannedomOmPatient", "common.validation.c-06", KANNEDOM_DELSVAR_ID_2);
            if (dateIsValid && checkIfNotFuture(utlatande.getKannedomOmPatient().asLocalDate())) {
                if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
                    && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
                        ValidationMessageType.OTHER,
                        "luae_na.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.UNDERSOKNING.RBK", KANNEDOM_DELSVAR_ID_2);
                }
                if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
                    && utlatande.getKannedomOmPatient().asLocalDate()
                    .isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
                        ValidationMessageType.OTHER,
                        "luae_na.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.ANHORIG.RBK", KANNEDOM_DELSVAR_ID_2);
                }
            }

        }
    }

    private boolean checkIfNotFuture(LocalDate date) {
        return date.isBefore(LocalDate.now().plusDays(1));
    }

    private void validateUnderlag(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUnderlagFinns() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAGFINNS_SVAR_JSON_ID_3,
                ValidationMessageType.EMPTY, UNDERLAGFINNS_DELSVAR_ID_3);
        } else if (utlatande.getUnderlagFinns() && utlatande.getUnderlag().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAG_SVAR_JSON_ID_4,
                ValidationMessageType.EMPTY, UNDERLAG_TYP_DELSVAR_ID_4);
        } else if (!utlatande.getUnderlagFinns() && !utlatande.getUnderlag().isEmpty()) {
            // R6
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAGFINNS_SVAR_JSON_ID_3,
                ValidationMessageType.OTHER,
                "luae_na.validation.underlagfinns.incorrect_combination", UNDERLAG_TYP_DELSVAR_ID_4);
        }

        if (utlatande.getUnderlag().size() > MAX_UNDERLAG) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAG_SVAR_JSON_ID_4,
                ValidationMessageType.OTHER,
                "luae_na.validation.underlag.too_many", UNDERLAG_TYP_DELSVAR_ID_4);
        }
        for (int i = 0; i < utlatande.getUnderlag().size(); i++) {
            Underlag underlag = utlatande.getUnderlag().get(i);
            // Alla underlagstyper är godkända här utom Underlag från skolhälsovård
            if (underlag.getTyp() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ",
                    ValidationMessageType.EMPTY,
                    "luae_na.validation.underlag.missing", UNDERLAG_TYP_DELSVAR_ID_4);
            } else if (!underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANFORETAGSHALSOVARD.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANSKOLHALSOVARD.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.OVRIGT.getId())) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ",
                    ValidationMessageType.INVALID_FORMAT,
                    "luae_na.validation.underlag.incorrect_format", UNDERLAG_TYP_DELSVAR_ID_4);
            }
            if (underlag.getDatum() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum", ValidationMessageType.EMPTY,
                    "luae_na.validation.underlag.date.missing", UNDERLAG_TYP_DELSVAR_ID_4);
            } else {
                ValidatorUtil.validateDateAndCheckIfFuture(underlag.getDatum(), validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum", "common.validation.c-06", UNDERLAG_TYP_DELSVAR_ID_4);
            }
            if (Strings.nullToEmpty(underlag.getHamtasFran()).trim().isEmpty()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].hamtasFran",
                    ValidationMessageType.EMPTY,
                    "luae_na.validation.underlag.hamtas-fran.missing", UNDERLAG_TYP_DELSVAR_ID_4);
            }
        }

        if (utlatande.getUnderlag().size() > 1 && !validateFirstUnderlagIsPresent(utlatande.getUnderlag())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                UNDERLAG_SVAR_JSON_ID_4 + "[0]",
                ValidationMessageType.INCORRECT_COMBINATION,
                "common.validation.c-05", UNDERLAG_TYP_DELSVAR_ID_4);
        }
    }

    private Boolean validateFirstUnderlagIsPresent(List<Underlag> underlagList) {
        Underlag underlag = underlagList.get(0);
        return underlag.getDatum() != null
            || !Strings.nullToEmpty(underlag.getHamtasFran()).trim().isEmpty()
            || underlag.getTyp() != null;
    }

    private void validateSjukdomsforlopp(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getSjukdomsforlopp()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_SJUKDOMSFORLOPP, SJUKDOMSFORLOPP_SVAR_JSON_ID_5,
                ValidationMessageType.EMPTY, SJUKDOMSFORLOPP_DELSVAR_ID_5);
        }
    }

    private void validateAktivitetsbegransning(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getAktivitetsbegransning()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_AKTIVITETSBEGRANSNING,
                AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17,
                ValidationMessageType.EMPTY, AKTIVITETSBEGRANSNING_DELSVAR_ID_17);
        }
    }

    private void validateMedicinskaForutsattningarForArbete(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getMedicinskaForutsattningarForArbete()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE,
                MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22, ValidationMessageType.EMPTY,
                MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22);
        }
    }

    private void validateFunktionsnedsattning(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        if (Strings.nullToEmpty(utlatande.getFunktionsnedsattningAnnan()).trim().isEmpty()
            && Strings.nullToEmpty(utlatande.getFunktionsnedsattningBalansKoordination()).trim().isEmpty()
            && Strings.nullToEmpty(utlatande.getFunktionsnedsattningIntellektuell()).trim().isEmpty()
            && Strings.nullToEmpty(utlatande.getFunktionsnedsattningKommunikation()).trim().isEmpty()
            && Strings.nullToEmpty(utlatande.getFunktionsnedsattningKoncentration()).trim().isEmpty()
            && Strings.nullToEmpty(utlatande.getFunktionsnedsattningPsykisk()).trim().isEmpty()
            && Strings.nullToEmpty(utlatande.getFunktionsnedsattningSynHorselTal()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, FUNKTIONSNEDSATTNING_CATEGORY_ID,
                FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35,
                ValidationMessageType.EMPTY, "common.validation.funktionsnedsattning.empty", FUNKTIONSNEDSATTNING_CATEGORY_ID);
        }
    }

    private void validateDiagnosgrund(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {

        if (Strings.nullToEmpty(utlatande.getDiagnosgrund()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS, DIAGNOSGRUND_SVAR_JSON_ID_7,
                ValidationMessageType.EMPTY);
        }

        if (utlatande.getNyBedomningDiagnosgrund() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS, DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45,
                ValidationMessageType.EMPTY);
        }

        // R13
        if (utlatande.getNyBedomningDiagnosgrund() != null && utlatande.getNyBedomningDiagnosgrund()
            && Strings.nullToEmpty(utlatande.getDiagnosForNyBedomning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS, DIAGNOS_FOR_NY_BEDOMNING_SVAR_JSON_ID_45,
                ValidationMessageType.EMPTY);
        }
        // R14 Inverted test of R13
        if ((utlatande.getNyBedomningDiagnosgrund() == null || !utlatande.getNyBedomningDiagnosgrund())
            && !Strings.isNullOrEmpty(utlatande.getDiagnosForNyBedomning())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_DIAGNOS, DIAGNOSGRUND_NY_BEDOMNING_SVAR_JSON_ID_45,
                ValidationMessageType.INCORRECT_COMBINATION,
                "luae_na.validation.diagnosfornybedomning.incorrect_combination");
        }
    }

    private void validateKontaktMedFk(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // R11
        if ((utlatande.getKontaktMedFk() == null || !utlatande.getKontaktMedFk())
            && !Strings.nullToEmpty(utlatande.getAnledningTillKontakt()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_KONTAKT, KONTAKT_ONSKAS_SVAR_JSON_ID_26,
                ValidationMessageType.INCORRECT_COMBINATION,
                "luae_na.validation.kontakt.incorrect_combination");
        }
    }

    private void validateBlanksForOptionalFields(LuaenaUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getForslagTillAtgard())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE,
                FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24, ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_KONTAKT, ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26,
                ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
                GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAvslutadBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKABEHANDLINGAR, AVSLUTADBEHANDLING_SVAR_JSON_ID_18,
                ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getFormagaTrotsBegransning())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE,
                FORMAGATROTSBEGRANSNING_SVAR_JSON_ID_23, ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKABEHANDLINGAR, PAGAENDEBEHANDLING_SVAR_JSON_ID_19,
                ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKABEHANDLINGAR, PLANERADBEHANDLING_SVAR_JSON_ID_20,
                ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getSubstansintag())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKABEHANDLINGAR, SUBSTANSINTAG_SVAR_JSON_ID_21,
                ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, OVRIGT_CATEGORY_ID, OVRIGT_SVAR_JSON_ID_25, ValidationMessageType.BLANK);
        }
    }
}
