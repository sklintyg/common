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
package se.inera.intyg.common.luae_fs.v1.validator;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DEBUT_SVAR_JSON_ID_15;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KANNEDOM_SVAR_JSON_ID_2;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_SVAR_JSON_ID_4;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

@Component(value = "luae_fs.InternalDraftValidatorImpl.v1")
public class InternalDraftValidatorImpl implements InternalDraftValidator<LuaefsUtlatandeV1> {

    private static final int MAX_UNDERLAG = 3;
    private static final String CATEGORY_GRUNDFORMU = "grundformu";
    private static final String CATEGORY_FUNKTIONSNEDSATTNING = "funktionsnedsattning";
    private static final String CATEGORY_OVRIGT = "ovrigt";
    private static final String CATEGORY_KONTAKT = "kontakt";

    final ValidatorUtilFK validatorUtilFK;

    public InternalDraftValidatorImpl(ValidatorUtilFK validatorUtilFK) {
        this.validatorUtilFK = validatorUtilFK;
    }

    @Override
    public ValidateDraftResponse validateDraft(LuaefsUtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);

        // Kategori 2 - Andra medicinska utredningar och underlag
        validateUnderlag(utlatande, validationMessages);

        // Kategori 3 – Diagnos
        validateDiagnose(utlatande, validationMessages);

        // Kategori 4 – Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);

        // Kategori 8 – Övrigt

        // Kategori 9 – Kontakt
        validateKontakt(utlatande, validationMessages);

        validateBlanksForOptionalFields(utlatande, validationMessages);

        // Vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    void validateGrundForMU(LuaefsUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required
        if (utlatande.getUndersokningAvPatienten() == null
            && utlatande.getJournaluppgifter() == null
            && utlatande.getAnhorigsBeskrivningAvPatienten() == null
            && utlatande.getAnnatGrundForMU() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, ValidationMessageType.EMPTY, GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);
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
                GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !Strings.isNullOrEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, ValidationMessageType.EMPTY,
                "luae_fs.validation.grund-for-mu.annat.beskrivning.invalid_combination", GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);
        }

        if (utlatande.getKannedomOmPatient() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
                ValidationMessageType.EMPTY, KANNEDOM_SVAR_ID_2);
        } else {
            boolean dateIsValid = ValidatorUtil.validateDateAndCheckIfFuture(utlatande.getKannedomOmPatient(), validationMessages,
                    CATEGORY_GRUNDFORMU, "kannedomOmPatient", "common.validation.c-06");
            if (dateIsValid && checkIfNotFuture(utlatande.getKannedomOmPatient().asLocalDate())) {
                if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
                    && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
                        ValidationMessageType.OTHER, "luae_fs.validation.grund-for-mu.kannedom.after",
                        "KV_FKMU_0001.UNDERSOKNING.RBK", KANNEDOM_SVAR_ID_2);
                }
                if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
                    && utlatande.getKannedomOmPatient().asLocalDate()
                    .isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
                        ValidationMessageType.OTHER, "luae_fs.validation.grund-for-mu.kannedom.after",
                        "KV_FKMU_0001.ANHORIG.RBK", KANNEDOM_SVAR_ID_2);
                }
            }
        }
    }

    private boolean checkIfNotFuture(LocalDate date) {
        return date.isBefore(LocalDate.now().plusDays(1));
    }

    void validateUnderlag(LuaefsUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUnderlagFinns() == null) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAGFINNS_SVAR_JSON_ID_3,
                ValidationMessageType.EMPTY, UNDERLAGFINNS_SVAR_ID_3);
            // If the flag is null, we cant determine whether underlag should be a list or not, so we can't do any
            // further validation..
            return;
        } else if (utlatande.getUnderlagFinns() && (utlatande.getUnderlag() == null || utlatande.getUnderlag().isEmpty())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAG_SVAR_JSON_ID_4,
                ValidationMessageType.EMPTY, UNDERLAG_SVAR_ID_4);
        } else if (!(utlatande.getUnderlagFinns() || utlatande.getUnderlag().isEmpty())) {
            // R6
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAGFINNS_SVAR_JSON_ID_3,
                ValidationMessageType.INVALID_FORMAT, "luae_fs.validation.underlagfinns.incorrect_combination",
                UNDERLAGFINNS_SVAR_ID_3);
        }

        if (utlatande.getUnderlag().size() > MAX_UNDERLAG) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAG_SVAR_JSON_ID_4,
                ValidationMessageType.OTHER, "luae_fs.validation.underlag.too_many", UNDERLAG_SVAR_ID_4);
        }
        for (int i = 0; i < utlatande.getUnderlag().size(); i++) {
            Underlag underlag = utlatande.getUnderlag().get(i);
            // Alla underlagstyper är godkända här utom Underlag från företagshälsovård
            if (underlag.getTyp() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ", ValidationMessageType.EMPTY,
                    "luae_fs.validation.underlag.missing", UNDERLAG_SVAR_ID_4);
            } else if (!underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANSKOLHALSOVARD.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId())
                && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.OVRIGT.getId())) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ", ValidationMessageType.INVALID_FORMAT,
                    "luae_fs.validation.underlag.incorrect_format", UNDERLAG_SVAR_ID_4);
            }
            if (underlag.getDatum() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum", ValidationMessageType.EMPTY,
                    "luae_fs.validation.underlag.date.missing", UNDERLAG_SVAR_ID_4);
            } else {
                ValidatorUtil.validateDateAndCheckIfFuture(underlag.getDatum(), validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum", "common.validation.c-06");
            }
            if (underlag.getHamtasFran() == null || underlag.getHamtasFran().trim().isEmpty()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                    UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].hamtasFran", ValidationMessageType.EMPTY,
                    "luae_fs.validation.underlag.hamtas-fran.missing", UNDERLAG_SVAR_ID_4);
            }
        }

        if (utlatande.getUnderlag().size() > 1 && !validateFirstUnderlagIsPresent(utlatande.getUnderlag())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
                UNDERLAG_SVAR_JSON_ID_4 + "[0]",
                ValidationMessageType.INCORRECT_COMBINATION,
                "common.validation.c-05");
        }
    }

    private Boolean validateFirstUnderlagIsPresent(List<Underlag> underlagList) {
        Underlag underlag = underlagList.get(0);
        return underlag.getDatum() != null
            || !Strings.nullToEmpty(underlag.getHamtasFran()).trim().isEmpty()
            || underlag.getTyp() != null;
    }

    void validateDiagnose(LuaefsUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        validatorUtilFK.validateDiagnose(utlatande.getDiagnoser(), validationMessages);
    }

    void validateFunktionsnedsattning(LuaefsUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getFunktionsnedsattningDebut()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING,
                FUNKTIONSNEDSATTNING_DEBUT_SVAR_JSON_ID_15, ValidationMessageType.EMPTY,
                "luae_fs.validation.funktionsnedsattning.debut.missing", FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15);
        }
        if (Strings.nullToEmpty(utlatande.getFunktionsnedsattningPaverkan()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING,
                FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16,  ValidationMessageType.EMPTY,
                "luae_fs.validation.funktionsnedsattning.paverkan.missing", FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16);
        }
    }

    void validateKontakt(LuaefsUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedFk() != null && !utlatande.getKontaktMedFk()
            && !Strings.nullToEmpty(utlatande.getAnledningTillKontakt()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_KONTAKT, KONTAKT_ONSKAS_SVAR_JSON_ID_26,
                ValidationMessageType.EMPTY, "luae_fs.validation.kontakt.invalid_combination", KONTAKT_ONSKAS_SVAR_ID_26);
        }
    }

    private void validateBlanksForOptionalFields(LuaefsUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_KONTAKT, ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26,
                ValidationMessageType.EMPTY, "luae_fs.validation.blanksteg.otillatet", KONTAKT_ONSKAS_SVAR_ID_26);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_GRUNDFORMU,
                GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, ValidationMessageType.EMPTY,
                "luae_fs.validation.blanksteg.otillatet", GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, CATEGORY_OVRIGT, OVRIGT_SVAR_JSON_ID_25,
                ValidationMessageType.EMPTY, "luae_fs.validation.blanksteg.otillatet", OVRIGT_SVAR_ID_25);
        }
    }
}
