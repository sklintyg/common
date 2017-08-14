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
package se.inera.intyg.common.luae_na.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.common.luae_na.model.internal.LuaenaUtlatande;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LuaenaUtlatande> {

    private static final int MAX_UNDERLAG = 3;

    @Autowired
    ValidatorUtilFK validatorUtilFK;

    @Override
    public ValidateDraftResponse validateDraft(LuaenaUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);
        // Kategori 2 – Andra medicinska utredningar och underlag
        validateUnderlag(utlatande, validationMessages);
        // Kategori 3 – Sjukdomsförlopp
        validateSjukdomsforlopp(utlatande, validationMessages);
        // Kategori 4 – Diagnos
        validatorUtilFK.validateDiagnose(utlatande.getDiagnoser(), validationMessages);
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

    private void validateGrundForMU(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getJournaluppgifter() == null
                && utlatande.getAnhorigsBeskrivningAvPatienten() == null && utlatande.getAnnatGrundForMU() == null) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.baserasPa", ValidationMessageType.EMPTY);
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

        // INTYG-3314
        boolean existsOtherMU = Stream.of(
                utlatande.getJournaluppgifter(),
                utlatande.getAnhorigsBeskrivningAvPatienten(),
                utlatande.getAnnatGrundForMU()).filter(Objects::nonNull).findAny().isPresent();
        if (utlatande.getUndersokningAvPatienten() == null
                && existsOtherMU
                && Strings.nullToEmpty(utlatande.getMotiveringTillInteBaseratPaUndersokning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.motiveringTillInteBaseratPaUndersokning",
                    ValidationMessageType.EMPTY);
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && Strings.nullToEmpty(utlatande.getAnnatGrundForMUBeskrivning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY);
        }
        // R3
        if (utlatande.getAnnatGrundForMU() == null && !Strings.isNullOrEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "luae_na.validation.grund-for-mu.incorrect_combination_annat_beskrivning");
        }

        if (utlatande.getKannedomOmPatient() == null) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.EMPTY);
        } else {
            boolean dateIsValid = ValidatorUtil.validateDateAndWarnIfFuture(utlatande.getKannedomOmPatient(), validationMessages,
                    "grundformu.kannedomOmPatient");
            if (dateIsValid) {
                if (utlatande.getUndersokningAvPatienten() != null && utlatande.getUndersokningAvPatienten().isValidDate()
                        && utlatande.getKannedomOmPatient().asLocalDate().isAfter(utlatande.getUndersokningAvPatienten().asLocalDate())) {
                    ValidatorUtil.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.OTHER,
                            "luae_na.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.UNDERSOKNING.RBK");
                }
                if (utlatande.getAnhorigsBeskrivningAvPatienten() != null && utlatande.getAnhorigsBeskrivningAvPatienten().isValidDate()
                        && utlatande.getKannedomOmPatient().asLocalDate()
                                .isAfter(utlatande.getAnhorigsBeskrivningAvPatienten().asLocalDate())) {
                    ValidatorUtil.addValidationError(validationMessages, "grundformu.kannedomOmPatient", ValidationMessageType.OTHER,
                            "luae_na.validation.grund-for-mu.kannedom.after", "KV_FKMU_0001.ANHORIG.RBK");
                }
            }
        }

    }

    private void validateUnderlag(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getUnderlagFinns() == null) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.underlagFinns", ValidationMessageType.EMPTY);
        } else if (utlatande.getUnderlagFinns() && utlatande.getUnderlag().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.underlag", ValidationMessageType.EMPTY);
        } else if (!utlatande.getUnderlagFinns() && !utlatande.getUnderlag().isEmpty()) {
            // R6
            ValidatorUtil.addValidationError(validationMessages, "grundformu.underlagFinns", ValidationMessageType.OTHER,
                    "luae_na.validation.underlagfinns.incorrect_combination");
        }

        if (utlatande.getUnderlag().size() > MAX_UNDERLAG) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.underlag", ValidationMessageType.OTHER,
                    "luae_na.validation.underlag.too_many");
        }
        for (int i = 0; i < utlatande.getUnderlag().size(); i++) {
            Underlag underlag = utlatande.getUnderlag().get(i);
            // Alla underlagstyper är godkända här utom Underlag från skolhälsovård
            if (underlag.getTyp() == null) {
                ValidatorUtil.addValidationError(validationMessages, "grundformu." + i + ".underlag", ValidationMessageType.EMPTY,
                        "luae_na.validation.underlag.missing");
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
                ValidatorUtil.addValidationError(validationMessages, "grundformu.underlag." + i + ".typ",
                        ValidationMessageType.INVALID_FORMAT,
                        "luae_na.validation.underlag.incorrect_format");
            }
            if (underlag.getDatum() == null) {
                ValidatorUtil.addValidationError(validationMessages, "grundformu.underlag." + i + ".datum", ValidationMessageType.EMPTY,
                        "luae_na.validation.underlag.date.missing");
            } else {
                ValidatorUtil.validateDate(underlag.getDatum(), validationMessages, "grundformu.underlag." + i + ".datum", null);
            }
            if (Strings.nullToEmpty(underlag.getHamtasFran()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, "grundformu.underlag." + i + ".hamtasFran",
                        ValidationMessageType.EMPTY,
                        "luae_na.validation.underlag.hamtas-fran.missing");
            }
        }
    }

    private void validateSjukdomsforlopp(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getSjukdomsforlopp()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "sjukdomsforlopp", ValidationMessageType.EMPTY);
        }
    }

    private void validateAktivitetsbegransning(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getAktivitetsbegransning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "aktivitetsbegransning", ValidationMessageType.EMPTY);
        }
    }

    private void validateMedicinskaForutsattningarForArbete(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getMedicinskaForutsattningarForArbete()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "medicinskaforutsattningarforarbete", ValidationMessageType.EMPTY);
        }
    }

    private void validateFunktionsnedsattning(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        if (Strings.nullToEmpty(utlatande.getFunktionsnedsattningAnnan()).trim().isEmpty()
                && Strings.nullToEmpty(utlatande.getFunktionsnedsattningBalansKoordination()).trim().isEmpty()
                && Strings.nullToEmpty(utlatande.getFunktionsnedsattningIntellektuell()).trim().isEmpty()
                && Strings.nullToEmpty(utlatande.getFunktionsnedsattningKommunikation()).trim().isEmpty()
                && Strings.nullToEmpty(utlatande.getFunktionsnedsattningKoncentration()).trim().isEmpty()
                && Strings.nullToEmpty(utlatande.getFunktionsnedsattningPsykisk()).trim().isEmpty()
                && Strings.nullToEmpty(utlatande.getFunktionsnedsattningSynHorselTal()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnosgrund(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (Strings.nullToEmpty(utlatande.getDiagnosgrund()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnosgrund", ValidationMessageType.EMPTY);
        }

        if (utlatande.getNyBedomningDiagnosgrund() == null) {
            ValidatorUtil.addValidationError(validationMessages, "diagnos.nyBedomningDiagnosgrund", ValidationMessageType.EMPTY);
        }

        // R13
        if (utlatande.getNyBedomningDiagnosgrund() != null && utlatande.getNyBedomningDiagnosgrund()
                && Strings.nullToEmpty(utlatande.getDiagnosForNyBedomning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "diagnos.diagnosForNyBedomning", ValidationMessageType.EMPTY);
        }
        // R14 Inverted test of R13
        if ((utlatande.getNyBedomningDiagnosgrund() == null || !utlatande.getNyBedomningDiagnosgrund())
                && !Strings.isNullOrEmpty(utlatande.getDiagnosForNyBedomning())) {
            ValidatorUtil.addValidationError(validationMessages, "diagnos.nyBedomningDiagnosgrund",
                    ValidationMessageType.INCORRECT_COMBINATION,
                    "luae_na.validation.diagnosfornybedomning.incorrect_combination");
        }
    }

    private void validateKontaktMedFk(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // R11
        if ((utlatande.getKontaktMedFk() == null || !utlatande.getKontaktMedFk())
                && !Strings.nullToEmpty(utlatande.getAnledningTillKontakt()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "Kontakt", ValidationMessageType.INCORRECT_COMBINATION,
                    "luae_na.validation.kontakt.incorrect_combination");
        }
    }

    private void validateBlanksForOptionalFields(LuaenaUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getForslagTillAtgard())) {
            ValidatorUtil.addValidationError(validationMessages, "forslagtillatgard.blanksteg", ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationError(validationMessages, "anledningtillkontakt.blanksteg", ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annat.blanksteg", ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAvslutadBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, "avslutadBehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getFormagaTrotsBegransning())) {
            ValidatorUtil.addValidationError(validationMessages, "formagatrotsbegransning.blanksteg", ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, "pagaendebehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, "planeradbehandling.blanksteg", ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getSubstansintag())) {
            ValidatorUtil.addValidationError(validationMessages, "substansintag.blanksteg", ValidationMessageType.BLANK);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, "ovrigt.blanksteg", ValidationMessageType.BLANK);
        }
    }
}
