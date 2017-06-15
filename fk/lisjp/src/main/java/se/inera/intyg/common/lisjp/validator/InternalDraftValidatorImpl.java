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
package se.inera.intyg.common.lisjp.validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.common.fkparent.model.validator.InternalDraftValidator;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class InternalDraftValidatorImpl implements InternalDraftValidator<LisjpUtlatande> {

    static final int MAX_ARBETSLIVSINRIKTADE_ATGARDER = 10;
    static final int MAX_SYSSELSATTNING = 4;
    static final int VARNING_FOR_TIDIG_SJUKSKRIVNING_ANTAL_DAGAR = 7;
    static final int VARNING_FOR_LANG_SJUKSKRIVNING_ANTAL_MANADER = 6;

    @Autowired
    private ValidatorUtilFK validatorUtilFK;

    @Override
    public ValidateDraftResponse validateDraft(LisjpUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Patientens adressuppgifter
        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);

        // Kategori 1 – Grund för medicinskt underlag
        validateGrundForMU(utlatande, validationMessages);

        // Kategori 2 – Sysselsättning
        if (!isAvstangningSmittskydd(utlatande)) {
            validateSysselsattning(utlatande, validationMessages);
        }

        // Kategori 3 – Diagnos
        validatorUtilFK.validateDiagnose(utlatande.getDiagnoser(), validationMessages);

        // Kategori 4 – Sjukdomens konsekvenser
        if (!isAvstangningSmittskydd(utlatande)) {
            validateFunktionsnedsattning(utlatande, validationMessages);
            validateAktivitetsbegransning(utlatande, validationMessages);
        }

        // Kategori 5 – Medicinska behandlingar/åtgärder

        // Kategori 6 – Bedömning
        validateBedomning(utlatande, validationMessages);

        // Kategori 7 – Åtgärder
        if (!isAvstangningSmittskydd(utlatande)) {
            validateAtgarder(utlatande, validationMessages);
        }

        // Kategori 8 – Övrigt

        // Kategori 9 – Kontakt
        validateKontakt(utlatande, validationMessages);

        validateBlanksForOptionalFields(utlatande, validationMessages);
        // vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateGrundForMU(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required if not smittskydd
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getUndersokningAvPatienten() == null
                    && utlatande.getTelefonkontaktMedPatienten() == null
                    && utlatande.getJournaluppgifter() == null
                    && utlatande.getAnnatGrundForMU() == null) {
                ValidatorUtil.addValidationError(validationMessages, "grundformu.baserasPa", ValidationMessageType.EMPTY);
            }
        }

        if (utlatande.getUndersokningAvPatienten() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getUndersokningAvPatienten(), validationMessages,
                    ValidatorUtilFK.GrundForMu.UNDERSOKNING);
        }
        if (utlatande.getJournaluppgifter() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getJournaluppgifter(), validationMessages,
                    ValidatorUtilFK.GrundForMu.JOURNALUPPGIFTER);
        }
        if (utlatande.getTelefonkontaktMedPatienten() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getTelefonkontaktMedPatienten(), validationMessages,
                    ValidatorUtilFK.GrundForMu.TELEFONKONTAKT);
        }
        if (utlatande.getAnnatGrundForMU() != null) {
            ValidatorUtilFK.validateGrundForMuDate(utlatande.getAnnatGrundForMU(), validationMessages, ValidatorUtilFK.GrundForMu.ANNAT);
        }

        // INTYG-3310
        if (utlatande.getUndersokningAvPatienten() == null && (utlatande.getJournaluppgifter() != null
                || utlatande.getTelefonkontaktMedPatienten() != null || utlatande.getAnnatGrundForMU() != null)
                && Strings.nullToEmpty(utlatande.getMotiveringTillInteBaseratPaUndersokning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.motiveringTillInteBaseratPaUndersokning",
                    ValidationMessageType.EMPTY);
        }

        // R2
        if (utlatande.getAnnatGrundForMU() != null && Strings.nullToEmpty(utlatande.getAnnatGrundForMUBeskrivning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annatGrundForMUBeskrivning", ValidationMessageType.EMPTY);
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !Strings.isNullOrEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annat", ValidationMessageType.EMPTY,
                    "lisjp.validation.grund-for-mu.annat.beskrivning.invalid_combination");
        }
    }

    private void validateSysselsattning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSysselsattning() == null
                || !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() != null)) {
            ValidatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY);
        } else {

            // R5
            if (!containsUnique(utlatande.getSysselsattning()
                    .stream().map(Sysselsattning::getTyp).collect(Collectors.toList()))) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning",
                        ValidationMessageType.INCORRECT_COMBINATION,
                        "lisjp.validation.sysselsattning.invalid_combination");
            }

            // R9
            if (Strings.nullToEmpty(utlatande.getNuvarandeArbete()).trim().isEmpty()
                    && utlatande.getSysselsattning().stream()
                            .anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning.nuvarandeArbete", ValidationMessageType.EMPTY);
            }

            // R10
            if (!Strings.nullToEmpty(utlatande.getNuvarandeArbete()).trim().isEmpty()
                    && !utlatande.getSysselsattning().stream()
                            .anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.nuvarandearbete.invalid_combination");
            }

            // No more than 4 entries are allowed
            if (utlatande.getSysselsattning().size() > MAX_SYSSELSATTNING) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning", ValidationMessageType.EMPTY,
                        "lisjp.validation.sysselsattning.too-many");
            }
        }
    }

    private void validateFunktionsnedsattning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getFunktionsnedsattning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning", ValidationMessageType.EMPTY);
        }
    }

    private void validateAktivitetsbegransning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getAktivitetsbegransning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning.aktivitetsbegransning", ValidationMessageType.EMPTY);
        }
    }

    private void validateBedomning(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Sjukskrivningar
        validateSjukskrivningar(utlatande, validationMessages);

        // FMB
        if (utlatande.getForsakringsmedicinsktBeslutsstod() != null
                && Strings.nullToEmpty(utlatande.getForsakringsmedicinsktBeslutsstod()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "bedomning.forsakringsmedicinsktBeslutsstod", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.fmb.empty");
        }

        // Prognos
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getPrognos() == null || utlatande.getPrognos().getTyp() == null) {
                ValidatorUtil.addValidationError(validationMessages, "bedomning.prognos", ValidationMessageType.EMPTY);
            } else {
                // New rule since INTYG-2286
                if (utlatande.getPrognos().getTyp() == PrognosTyp.ATER_X_ANTAL_DGR && utlatande.getPrognos().getDagarTillArbete() == null) {
                    ValidatorUtil.addValidationError(validationMessages, "bedomning.prognos.dagarTillArbete", ValidationMessageType.EMPTY);
                } else if (utlatande.getPrognos().getTyp() != PrognosTyp.ATER_X_ANTAL_DGR
                        && utlatande.getPrognos().getDagarTillArbete() != null) {
                    ValidatorUtil.addValidationError(validationMessages, "bedomning.prognos", ValidationMessageType.EMPTY,
                            "lisjp.validation.bedomning.prognos.dagarTillArbete.invalid_combination");
                }
            }
        }
    }

    private void validateSjukskrivningar(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {

        // Check if there are any at all
        if (utlatande.getSjukskrivningar() == null || utlatande.getSjukskrivningar().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.sjukskrivningar.missing");
        } else {

            // Validate sjukskrivningar, checks that dates exist and are valid
            utlatande.getSjukskrivningar()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(sjukskrivning -> validateSjukskrivning(validationMessages, sjukskrivning));

            // R17 Validate no sjukskrivningperiods overlap
            validateSjukskrivningPeriodOverlap(utlatande, validationMessages);

            // INTYG-3207: Show warning if any period starts earlier than 7 days before now
            validateSjukskrivningIsTooEarly(utlatande, validationMessages);

            // INTYG-3747: Show warning if the total period exceeds 6 months
            validateSjukskrivningIsTooLong(utlatande, validationMessages);

            // Arbetstidsforlaggning R13, R14, R15, R16
            if (isArbetstidsforlaggningMandatory(utlatande)) {
                if (utlatande.getArbetstidsforlaggning() == null) {
                    ValidatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggning", ValidationMessageType.EMPTY,
                            "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing");
                } else {
                    if (utlatande.getArbetstidsforlaggning()
                            && Strings.nullToEmpty(utlatande.getArbetstidsforlaggningMotivering()).trim().isEmpty()) {
                        ValidatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggningMotivering",
                                ValidationMessageType.EMPTY);
                    } else if (!utlatande.getArbetstidsforlaggning()
                            && !Strings.nullToEmpty(utlatande.getArbetstidsforlaggningMotivering()).trim().isEmpty()) {
                        ValidatorUtil.addValidationError(validationMessages, "bedomning.arbetstidsforlaggning", ValidationMessageType.EMPTY,
                                "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect");
                    }
                }
            } else if (isArbetstidsforlaggningMotiveringForbidden(utlatande)
                    && !Strings.nullToEmpty(utlatande.getArbetstidsforlaggningMotivering()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                        "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination");
            }

            // R22
            if (!containsUnique(utlatande.getSjukskrivningar().stream()
                    .map(Sjukskrivning::getSjukskrivningsgrad).collect(Collectors.toList()))) {
                ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar",
                        ValidationMessageType.INCORRECT_COMBINATION,
                        "lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.invalid_combination");
            }

        }
    }

    private void validateSjukskrivningPeriodOverlap(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        utlatande.getSjukskrivningar()
                .stream()
                .filter(Objects::nonNull)
                .forEach(sjukskrivning -> checkSjukskrivningPeriodOverlapAgainstList(validationMessages, sjukskrivning,
                        utlatande.getSjukskrivningar()));
    }

    private void validateSjukskrivningIsTooEarly(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSjukskrivningar()
            .stream()
            .filter(Objects::nonNull)
            .anyMatch(sjukskrivning -> sjukskrivning.getPeriod() != null
                && sjukskrivning.getPeriod().getFrom() != null
                && sjukskrivning.getPeriod().getFrom().isValidDate()
                && sjukskrivning.getPeriod().getFrom().isBeforeNumDays(VARNING_FOR_TIDIG_SJUKSKRIVNING_ANTAL_DAGAR))) {
            ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.WARN,
                "lisjp.validation.bedomning.sjukskrivningar.tidigtstartdatum");
        }
    }

    private void validateSjukskrivningIsTooLong(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        List<Sjukskrivning> list = utlatande.getSjukskrivningar().stream().filter(Objects::nonNull).collect(Collectors.toList());

        // 1. Hämta ut starten för sjukskrivningen
        Sjukskrivning min = list.stream()
            .min(Comparator.comparing(item -> item.getPeriod().fromAsLocalDate()))
            .get();

        LocalDate minDate = min.getPeriod() != null ? min.getPeriod().fromAsLocalDate() : null;
        if (minDate == null) {
            return; // return fast
        }

        // 2. Hämta ut slutet för sjukskrivningen
        Sjukskrivning max = list.stream()
            .max(Comparator.comparing(item -> item.getPeriod().tomAsLocalDate()))
            .get();

        LocalDate maxDate = max.getPeriod() != null ? max.getPeriod().tomAsLocalDate() : null;
        if (maxDate == null) {
            return; // return fast
        }

        // 3. Kontrollera ifall maxDate - 6 månader > minDate
        if (maxDate.minusMonths(VARNING_FOR_LANG_SJUKSKRIVNING_ANTAL_MANADER).isAfter(minDate)) {
            ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.WARN,
                "lisjp.validation.bedomning.sjukskrivningar.sentslutdatum");
        }
    }

    private void checkSjukskrivningPeriodOverlapAgainstList(List<ValidationMessage> validationMessages, Sjukskrivning sjukskrivning,
            ImmutableList<Sjukskrivning> sjukskrivningar) {

        if (sjukskrivning == null) {
            return;
        }

        Optional<Sjukskrivning> overlappingPeriod = getPeriodIntervalsOverlapping(sjukskrivning, sjukskrivningar);
        if (overlappingPeriod.isPresent()) {
            if (sjukskrivning.getPeriod().getFrom().equals(overlappingPeriod.get().getPeriod().getFrom())) {
                ValidatorUtil.addValidationError(validationMessages,
                        "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from",
                        ValidationMessageType.PERIOD_OVERLAP);
                ValidatorUtil.addValidationError(validationMessages,
                        "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".tom",
                        ValidationMessageType.PERIOD_OVERLAP);
            } else if (sjukskrivning.getPeriod().getFrom().asLocalDate()
                    .isBefore(overlappingPeriod.get().getPeriod().getFrom().asLocalDate())) {
                ValidatorUtil.addValidationError(validationMessages,
                        "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".tom",
                        ValidationMessageType.PERIOD_OVERLAP);
            } else {
                ValidatorUtil.addValidationError(validationMessages,
                        "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from",
                        ValidationMessageType.PERIOD_OVERLAP);
            }
        }
    }

    private Optional<Sjukskrivning> getPeriodIntervalsOverlapping(Sjukskrivning sjukskrivning,
            ImmutableList<Sjukskrivning> sjukskrivningar) {
        return sjukskrivningar
                .stream()
                .filter(Objects::nonNull)
                .filter(e -> e != sjukskrivning)
                .filter(e -> e.getPeriod() != null && e.getPeriod().overlaps(sjukskrivning.getPeriod()))
                .findFirst();
    }

    private void validateSjukskrivning(List<ValidationMessage> validationMessages, Sjukskrivning sjukskrivning) {
        if (sjukskrivning.getSjukskrivningsgrad() == null) {
            // Should never happen but just in case
            ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing");
        } else {
            if (sjukskrivning.getPeriod() == null) {
                ValidatorUtil.addValidationError(validationMessages, "bedomning.sjukskrivningar", ValidationMessageType.EMPTY,
                        "lisjp.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".missing");
            } else {

                boolean fromDateValid = ValidatorUtil.validateDate(sjukskrivning.getPeriod().getFrom(), validationMessages,
                        "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from", null);

                boolean toDateValid = ValidatorUtil.validateDate(sjukskrivning.getPeriod().getTom(), validationMessages,
                        "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".tom", null);

                if (fromDateValid && toDateValid && !sjukskrivning.getPeriod().isValid()) {
                    ValidatorUtil.addValidationError(validationMessages,
                            "bedomning.sjukskrivningar.period." + sjukskrivning.getSjukskrivningsgrad().getId(),
                            ValidationMessageType.INCORRECT_COMBINATION);
                }
            }
        }
    }

    private boolean isArbetstidsforlaggningMandatory(LisjpUtlatande utlatande) {
        return !isAvstangningSmittskydd(utlatande) && utlatande.getSjukskrivningar()
                .stream()
                .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_3_4
                        || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_HALFTEN
                        || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_1_4);
    }

    private boolean isArbetstidsforlaggningMotiveringForbidden(LisjpUtlatande utlatande) {
        return utlatande.getSjukskrivningar()
                .stream()
                .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT);
    }

    private void validateAtgarder(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // Anything checked at all?
        if (utlatande.getArbetslivsinriktadeAtgarder() == null || utlatande.getArbetslivsinriktadeAtgarder().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "atgarder.arbetslivsinriktadeAtgarder", ValidationMessageType.EMPTY);
        } else {

            // R21 If INTE_AKTUELLT is checked it must be the only selection
            if (utlatande.getArbetslivsinriktadeAtgarder().stream()
                    .anyMatch(e -> e.getTyp() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && utlatande.getArbetslivsinriktadeAtgarder().size() > 1) {
                ValidatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisjp.validation.atgarder.inte_aktuellt_no_combine");
            }

            // R27 If INTE_AKTUELLT is checked utlatande.getArbetslivsinriktadeAtgarderBeskrivning() must not be
            // answered
            if (utlatande.getArbetslivsinriktadeAtgarder().stream()
                    .anyMatch(e -> e.getTyp() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                    && !Strings.nullToEmpty(utlatande.getArbetslivsinriktadeAtgarderBeskrivning()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisjp.validation.atgarder.invalid_combination");
            }

            // No more than 10 entries are allowed
            if (utlatande.getArbetslivsinriktadeAtgarder().size() > MAX_ARBETSLIVSINRIKTADE_ATGARDER) {
                ValidatorUtil.addValidationError(validationMessages, "atgarder", ValidationMessageType.EMPTY,
                        "lisjp.validation.atgarder.too-many");
            }

            // R29
            if (!containsUnique(utlatande.getArbetslivsinriktadeAtgarder()
                    .stream().map(ArbetslivsinriktadeAtgarder::getTyp).collect(Collectors.toList()))) {
                ValidatorUtil.addValidationError(validationMessages, "atgarder",
                        ValidationMessageType.INCORRECT_COMBINATION,
                        "lisjp.validation.atgarder.typ.invalid_combination");
            }
        }
    }

    private void validateKontakt(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedFk() != null && !utlatande.getKontaktMedFk()
                && !Strings.nullToEmpty(utlatande.getAnledningTillKontakt()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "kontakt", ValidationMessageType.EMPTY,
                    "lisjp.validation.kontakt.invalid_combination");
        }
    }

    private void validateBlanksForOptionalFields(LisjpUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationError(validationMessages, "anledningtillkontakt.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationError(validationMessages, "grundformu.annat.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, "pagaendebehandling.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, "planeradbehandling.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, "ovrigt.blanksteg", ValidationMessageType.EMPTY,
                    "lisjp.validation.blanksteg.otillatet");
        }
    }

    private boolean isAvstangningSmittskydd(LisjpUtlatande utlatande) {
        return utlatande.getAvstangningSmittskydd() != null && utlatande.getAvstangningSmittskydd();
    }

    private static <T> boolean containsUnique(List<T> list) {
        Set<T> set = new HashSet<>();
        return list.stream().allMatch(t -> set.add(t));
    }
}
