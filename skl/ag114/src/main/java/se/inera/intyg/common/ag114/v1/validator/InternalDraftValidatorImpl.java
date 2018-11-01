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
package se.inera.intyg.common.ag114.v1.validator;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_1;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_9;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_8;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1;

@Component(value = "ag114.InternalDraftValidatorImpl.v1")
public class InternalDraftValidatorImpl implements InternalDraftValidator<Ag114UtlatandeV1> {

    private static final int MAX_UNDERLAG = 3;

    private static final String CATEGORY_SYSSELSATTNING = "sysselsattning";
    private static final String CATEGORY_ONSKAR_FORMEDLA_DIAGNOS = "onskarFormedlaDiagnos";
    private static final String CATEGORY_NEDSATT_ARBETSFORMAGA = "nedsattArbetsformaga";
    private static final String CATEGORY_ARBETSFORMAGA_TROTS_SJUKDOM = "arbetsformagaTrotsSjukdom";
    private static final String CATEGORY_OVRIGT = "ovrigt";

    // private static final String CATEGORY_GRUNDFORMU = "grundformu";
    // private static final String CATEGORY_FUNKTIONSNEDSATTNING = "funktionsnedsattning";
    // private static final String CATEGORY_OVRIGT = "ovrigt";
    private static final String CATEGORY_KONTAKT = "kontakt";

    @Autowired
    private ValidatorUtilSKL validatorUtilSKL;

    @Override
    public ValidateDraftResponse validateDraft(Ag114UtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        validateSysselsattning(utlatande, validationMessages);

        validateOnskarFormedla(utlatande, validationMessages);

        validateDiagnoser(utlatande, validationMessages);

        validateNedsattArbetsformaga(utlatande, validationMessages);
        validateArbetsformagaTrotsSjukdom(utlatande, validationMessages);

        validateOvrigaUpplysningar(utlatande, validationMessages);
        validateKontakt(utlatande, validationMessages);
        // Vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateSysselsattning(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSysselsattning() == null
                || !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() != null)) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYSSELSATTNING, TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1,
                    ValidationMessageType.EMPTY);
        } else {

            // R5
            if (!containsUnique(utlatande.getSysselsattning()
                    .stream().map(Sysselsattning::getTyp).collect(Collectors.toList()))) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYSSELSATTNING, TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1,
                        ValidationMessageType.INCORRECT_COMBINATION,
                        "lisjp.validation.sysselsattning.invalid_combination");
            }

            // R9
            if (Strings.nullToEmpty(utlatande.getNuvarandeArbete()).trim().isEmpty()
                    && utlatande.getSysselsattning().stream()
                            .anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYSSELSATTNING, TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1,
                        ValidationMessageType.EMPTY);
            }

            // R10
            if (!Strings.nullToEmpty(utlatande.getNuvarandeArbete()).trim().isEmpty()
                    && !utlatande.getSysselsattning().stream()
                            .anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYSSELSATTNING, TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1,
                        ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.nuvarandearbete.invalid_combination");
            }

            // No more than 1 entries are allowed
            if (utlatande.getSysselsattning().size() > 1) {
                ValidatorUtil.addValidationError(validationMessages, CATEGORY_SYSSELSATTNING, TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1,
                        ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.too-many");
            }
        }
    }

    private void validateOnskarFormedla(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getOnskarFormedlaDiagnos() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ONSKAR_FORMEDLA_DIAGNOS, ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateNedsattArbetsformaga(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getNedsattArbetsformaga() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_NEDSATT_ARBETSFORMAGA, NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateArbetsformagaTrotsSjukdom(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getArbetsformagaTrotsSjukdom() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ARBETSFORMAGA_TROTS_SJUKDOM,
                    ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_1,
                    ValidationMessageType.EMPTY);
        } else if (utlatande.getArbetsformagaTrotsSjukdom() && Strings.isNullOrEmpty(utlatande.getArbetsformagaTrotsSjukdomBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ARBETSFORMAGA_TROTS_SJUKDOM,
                    ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_JSON_ID_6_2,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateOvrigaUpplysningar(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getOvrigaUpplysningar() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_OVRIGT,
                    OVRIGT_SVAR_JSON_ID_8,
                    ValidationMessageType.EMPTY);
        }
    }

    // void validateGrundForMU(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
    //
    // // R1 - no need to check. they are already separated and cannot occur twice.
    //
    // // One of the following is required
    // if (utlatande.getUndersokningAvPatienten() == null
    // && utlatande.getJournaluppgifter() == null
    // && utlatande.getAnhorigsBeskrivningAvPatienten() == null
    // && utlatande.getAnnatGrundForMU() == null) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU, GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1,
    // ValidationMessageType.EMPTY);
    // }
    //
    // if (utlatande.getUndersokningAvPatienten() != null) {
    // ValidatorUtilFK.validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages,
    // ValidatorUtilFK.GrundForMu.UNDERSOKNING);
    // }
    // if (utlatande.getJournaluppgifter() != null) {
    // ValidatorUtilFK.validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages,
    // ValidatorUtilFK.GrundForMu.JOURNALUPPGIFTER);
    // }
    // if (utlatande.getAnhorigsBeskrivningAvPatienten() != null) {
    // ValidatorUtilFK.validateGrundForMuDate(utlatande.getAnhorigsBeskrivningAvPatienten(), validationMessages,
    // ValidatorUtilFK.GrundForMu.ANHORIGSBESKRIVNING);
    // }
    // if (utlatande.getAnnatGrundForMU() != null) {
    // ValidatorUtilFK.validateGrundForMuDate(utlatande.getAnnatGrundForMU(), validationMessages,
    // ValidatorUtilFK.GrundForMu.ANNAT);
    // }
    //
    // // INTYG-3315
    // boolean existsOtherMU = Stream.of(
    // utlatande.getJournaluppgifter(),
    // utlatande.getAnhorigsBeskrivningAvPatienten(),
    // utlatande.getAnnatGrundForMU()).filter(Objects::nonNull).findAny().isPresent();
    // if (utlatande.getUndersokningAvPatienten() == null
    // && existsOtherMU
    // && Strings.nullToEmpty(utlatande.getMotiveringTillInteBaseratPaUndersokning()).trim().isEmpty()) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
    // MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_ID_1,
    // ValidationMessageType.EMPTY);
    // }
    //
    // // R2
    // if (utlatande.getAnnatGrundForMU() != null &&
    // Strings.nullToEmpty(utlatande.getAnnatGrundForMUBeskrivning()).trim().isEmpty()) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
    // GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, ValidationMessageType.EMPTY);
    // }
    //
    // // R3
    // if (utlatande.getAnnatGrundForMU() == null && !Strings.isNullOrEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
    // GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, ValidationMessageType.EMPTY,
    // "ag114.validation.grund-for-mu.annat.beskrivning.invalid_combination");
    // }
    //
    // if (utlatande.getKannedomOmPatient() == null) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
    // ValidationMessageType.EMPTY);
    // } else {
    // boolean dateIsValid = ValidatorUtil.validateDateAndWarnIfFuture(utlatande.getKannedomOmPatient(), validationMessages,
    // CATEGORY_GRUNDFORMU, "kannedomOmPatient");
    // if (dateIsValid) {
    // if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
    // && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
    // ValidationMessageType.OTHER,
    // "ag114.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.UNDERSOKNING.RBK");
    // }
    // if (utlatande.getAnhorigsBeskrivningAvPatienten() != null &&
    // utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
    // && utlatande.getKannedomOmPatient().asLocalDate()
    // .isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU, KANNEDOM_SVAR_JSON_ID_2,
    // ValidationMessageType.OTHER,
    // "ag114.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.ANHORIG.RBK");
    // }
    // }
    // }
    // }
    //
    // void validateUnderlag(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
    // if (utlatande.getUnderlagFinns() == null) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAGFINNS_SVAR_JSON_ID_3,
    // ValidationMessageType.EMPTY);
    // // If the flag is null, we cant determine whether underlag should be a list or not, so we can't do any
    // // further validation..
    // return;
    // } else if (utlatande.getUnderlagFinns() && (utlatande.getUnderlag() == null || utlatande.getUnderlag().isEmpty())) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAG_SVAR_JSON_ID_4,
    // ValidationMessageType.EMPTY);
    // } else if (!(utlatande.getUnderlagFinns() || utlatande.getUnderlag().isEmpty())) {
    // // R6
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAGFINNS_SVAR_JSON_ID_3,
    // ValidationMessageType.INVALID_FORMAT,
    // "ag114.validation.underlagfinns.incorrect_combination");
    // }
    //
    // if (utlatande.getUnderlag().size() > MAX_UNDERLAG) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU, UNDERLAG_SVAR_JSON_ID_4,
    // ValidationMessageType.OTHER,
    // "ag114.validation.underlag.too_many");
    // }
    // for (int i = 0; i < utlatande.getUnderlag().size(); i++) {
    // Underlag underlag = utlatande.getUnderlag().get(i);
    // // Alla underlagstyper är godkända här utom Underlag från företagshälsovård
    // if (underlag.getTyp() == null) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
    // UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ", ValidationMessageType.EMPTY,
    // "ag114.validation.underlag.missing");
    // } else if (!underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_HABILITERINGEN.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRAN_LOGOPED.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANPSYKOLOG.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UNDERLAG_FRANSKOLHALSOVARD.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS.getId())
    // && !underlag.getTyp().getId().equals(Underlag.UnderlagsTyp.OVRIGT.getId())) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
    // UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].typ",
    // ValidationMessageType.INVALID_FORMAT,
    // "ag114.validation.underlag.incorrect_format");
    // }
    // if (underlag.getDatum() == null) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
    // UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum", ValidationMessageType.EMPTY,
    // "ag114.validation.underlag.date.missing");
    // } else {
    // ValidatorUtil.validateDate(underlag.getDatum(), validationMessages, CATEGORY_GRUNDFORMU,
    // UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].datum", null);
    // }
    // if (underlag.getHamtasFran() == null || underlag.getHamtasFran().trim().isEmpty()) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
    // UNDERLAG_SVAR_JSON_ID_4 + "[" + i + "].hamtasFran",
    // ValidationMessageType.EMPTY,
    // "ag114.validation.underlag.hamtas-fran.missing");
    // }
    // }
    // }
    //
    private void validateDiagnoser(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Validera endast om patient önskar att förmedla diagnos.
        if (utlatande.getOnskarFormedlaDiagnos() != null && utlatande.getOnskarFormedlaDiagnos()) {
            validatorUtilSKL.validateDiagnose(utlatande.getDiagnoser(), validationMessages);
        }
    }

    //
    // void validateFunktionsnedsattning(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
    // if (Strings.nullToEmpty(utlatande.getFunktionsnedsattningDebut()).trim().isEmpty()) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING,
    // FUNKTIONSNEDSATTNING_DEBUT_SVAR_JSON_ID_15,
    // ValidationMessageType.EMPTY,
    // "ag114.validation.funktionsnedsattning.debut.missing");
    // }
    // if (Strings.nullToEmpty(utlatande.getFunktionsnedsattningPaverkan()).trim().isEmpty()) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING,
    // FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_JSON_ID_16,
    // ValidationMessageType.EMPTY,
    // "ag114.validation.funktionsnedsattning.paverkan.missing");
    // }
    // }
    //
    private void validateKontakt(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedArbetsgivaren() != null && !utlatande.getKontaktMedArbetsgivaren()
                && !Strings.nullToEmpty(utlatande.getAnledningTillKontakt()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_KONTAKT, KONTAKT_ONSKAS_SVAR_JSON_ID_9,
                    ValidationMessageType.EMPTY, "ag114.validation.kontakt.invalid_combination");
        }
    }
    //
    // private void validateBlanksForOptionalFields(Ag114UtlatandeV1 utlatande, List<ValidationMessage> validationMessages)
    // {
    // if (ValidatorUtil.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_KONTAKT, ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26,
    // ValidationMessageType.EMPTY,
    // "ag114.validation.blanksteg.otillatet");
    // }
    // if (ValidatorUtil.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_GRUNDFORMU,
    // GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, ValidationMessageType.EMPTY,
    // "ag114.validation.blanksteg.otillatet");
    // }
    // if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
    // ValidatorUtil.addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_SVAR_JSON_ID_25,
    // ValidationMessageType.EMPTY,
    // "ag114.validation.blanksteg.otillatet");
    // }
    // }

    private static <T> boolean containsUnique(List<T> list) {
        Set<T> set = new HashSet<>();
        return list.stream().allMatch(t -> set.add(t));
    }
}
