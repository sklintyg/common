/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.validator;

import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ATGARDER_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_JSON_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

@Component("lisjp.v1.InternalDraftValidatorImpl")
public class InternalDraftValidatorImpl implements InternalDraftValidator<LisjpUtlatandeV1> {

    static final int MAX_ARBETSLIVSINRIKTADE_ATGARDER = 10;
    static final int MAX_SYSSELSATTNING = 4;
    static final int VARNING_FOR_TIDIG_SJUKSKRIVNING_ANTAL_DAGAR = 7;
    static final int VARNING_FOR_LANG_SJUKSKRIVNING_ANTAL_MANADER = 6;

    @Autowired
    private ValidatorUtilFK validatorUtilFK;

    private static <T> boolean containsUnique(List<T> list) {
        Set<T> set = new HashSet<>();
        return list.stream().allMatch(t -> set.add(t));
    }

    @Override
    public ValidateDraftResponse validateDraft(LisjpUtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

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

    private void validateGrundForMU(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {

        // R1 - no need to check. they are already separated and cannot occur twice.

        // One of the following is required if not smittskydd
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getUndersokningAvPatienten() == null
                && utlatande.getTelefonkontaktMedPatienten() == null
                && utlatande.getJournaluppgifter() == null
                && utlatande.getAnnatGrundForMU() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, GRUNDFORMU_CATEGORY_ID,
                    GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, ValidationMessageType.EMPTY, GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1);
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

        // R2
        if (utlatande.getAnnatGrundForMU() != null && Strings.nullToEmpty(utlatande.getAnnatGrundForMUBeskrivning()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, GRUNDFORMU_CATEGORY_ID,
                GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1, ValidationMessageType.EMPTY,
                GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);
        }

        // R3
        if (utlatande.getAnnatGrundForMU() == null && !Strings.isNullOrEmpty(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, GRUNDFORMU_CATEGORY_ID,
                GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1, ValidationMessageType.EMPTY,
                "lisjp.validation.grund-for-mu.annat.beskrivning.invalid_combination", GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);
        }
    }

    private void validateSysselsattning(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getSysselsattning() == null
            || !utlatande.getSysselsattning().stream().anyMatch(e -> e.getTyp() != null)) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, SYSSELSATTNING_CATEGORY_ID,
                TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28, ValidationMessageType.EMPTY, TYP_AV_SYSSELSATTNING_SVAR_ID_28);
        } else {

            // R5
            if (!containsUnique(utlatande.getSysselsattning()
                .stream().map(Sysselsattning::getTyp).collect(Collectors.toList()))) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, SYSSELSATTNING_CATEGORY_ID,
                    TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28, ValidationMessageType.INCORRECT_COMBINATION,
                    "lisjp.validation.sysselsattning.invalid_combination", TYP_AV_SYSSELSATTNING_SVAR_ID_28);
            }

            // R9
            if (Strings.nullToEmpty(utlatande.getNuvarandeArbete()).trim().isEmpty()
                && utlatande.getSysselsattning().stream()
                .anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, SYSSELSATTNING_CATEGORY_ID,
                    NUVARANDE_ARBETE_SVAR_JSON_ID_29, ValidationMessageType.EMPTY, NUVARANDE_ARBETE_SVAR_ID_29);
            }

            // R10
            if (!Strings.nullToEmpty(utlatande.getNuvarandeArbete()).trim().isEmpty()
                && !utlatande.getSysselsattning().stream()
                .anyMatch(e -> e.getTyp() == Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, SYSSELSATTNING_CATEGORY_ID,
                    TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28, ValidationMessageType.EMPTY,
                    "lisjp.validation.sysselsattning.nuvarandearbete.invalid_combination", TYP_AV_SYSSELSATTNING_SVAR_ID_28);
            }

            // No more than 4 entries are allowed
            if (utlatande.getSysselsattning().size() > MAX_SYSSELSATTNING) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, SYSSELSATTNING_CATEGORY_ID,
                    TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28, ValidationMessageType.EMPTY,
                    "lisjp.validation.sysselsattning.too-many", TYP_AV_SYSSELSATTNING_SVAR_ID_28);
            }
        }
    }

    private void validateFunktionsnedsattning(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getFunktionsnedsattning()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, FUNKTIONSNEDSATTNING_CATEGORY_ID,
                FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35, ValidationMessageType.EMPTY,
                "lisjp.validation.funktionsnedsattning.missing", FUNKTIONSNEDSATTNING_SVAR_ID_35);
        }
    }

    private void validateAktivitetsbegransning(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getAktivitetsbegransning()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, FUNKTIONSNEDSATTNING_CATEGORY_ID,
                AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, ValidationMessageType.EMPTY,
                "lisjp.validation.aktivitetsbegransning.missing", AKTIVITETSBEGRANSNING_SVAR_ID_17);
        }
    }

    private void validateBedomning(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {

        // Sjukskrivningar
        validateSjukskrivningar(utlatande, validationMessages);

        // Prognos
        if (!isAvstangningSmittskydd(utlatande)) {
            if (utlatande.getPrognos() == null || utlatande.getPrognos().getTyp() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID, PROGNOS_SVAR_JSON_ID_39,
                    ValidationMessageType.EMPTY, PROGNOS_SVAR_ID_39);
            } else {
                // New rule since INTYG-2286
                if (utlatande.getPrognos().getTyp() == PrognosTyp.ATER_X_ANTAL_DGR && utlatande.getPrognos().getDagarTillArbete() == null) {
                    ValidatorUtil
                        .addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                            PROGNOS_SVAR_JSON_ID_39 + ".dagarTillArbete", ValidationMessageType.EMPTY, PROGNOS_BESKRIVNING_DELSVAR_ID_39);
                } else if (utlatande.getPrognos().getTyp() != PrognosTyp.ATER_X_ANTAL_DGR
                    && utlatande.getPrognos().getDagarTillArbete() != null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID, PROGNOS_SVAR_JSON_ID_39,
                        ValidationMessageType.EMPTY, "lisjp.validation.bedomning.prognos.dagarTillArbete.invalid_combination",
                        PROGNOS_SVAR_ID_39);
                }
            }

        }
    }

    private void validateSjukskrivningar(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {

        // Check if there are any at all
        if (utlatande.getSjukskrivningar() == null || utlatande.getSjukskrivningar().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32, ValidationMessageType.EMPTY,
                "lisjp.validation.bedomning.sjukskrivningar.missing", BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
        } else {

            // Validate sjukskrivningar, checks that dates exist and are valid
            utlatande.getSjukskrivningar()
                .stream()
                .filter(Objects::nonNull)
                .forEach(sjukskrivning -> validateSjukskrivning(validationMessages, sjukskrivning));

            // R17 Validate no sjukskrivningperiods overlap
            validateSjukskrivningPeriodOverlap(utlatande, validationMessages);

            // Arbetstidsforlaggning R13, R14, R15, R16
            if (isArbetstidsforlaggningMandatory(utlatande)) {
                if (utlatande.getArbetstidsforlaggning() == null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                        ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33, ValidationMessageType.EMPTY,
                        "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing", ARBETSTIDSFORLAGGNING_SVAR_ID_33);
                } else {
                    if (utlatande.getArbetstidsforlaggning()
                        && Strings.nullToEmpty(utlatande.getArbetstidsforlaggningMotivering()).trim().isEmpty()) {
                        ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                            ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33, ValidationMessageType.EMPTY,
                            ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33);
                    } else if (!utlatande.getArbetstidsforlaggning()
                        && !Strings.nullToEmpty(utlatande.getArbetstidsforlaggningMotivering()).trim().isEmpty()) {
                        ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                            ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33, ValidationMessageType.EMPTY,
                            "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect",
                            ARBETSTIDSFORLAGGNING_SVAR_ID_33);
                    }
                }
            } else if (isArbetstidsforlaggningMotiveringForbidden(utlatande)) {
                boolean hasMotivering = !Strings.nullToEmpty(utlatande.getArbetstidsforlaggningMotivering()).trim().isEmpty();

                // If arbetstidsförläggning is not allowed, we must not have a motivering or a true/false value.
                if (hasMotivering || utlatande.getArbetstidsforlaggning() != null) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages,
                        BEDOMNING_CATEGORY_ID, BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32, ValidationMessageType.EMPTY,
                        "lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination",
                        BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
                }
            }

            // R22
            if (!containsUnique(utlatande.getSjukskrivningar().stream()
                .map(Sjukskrivning::getSjukskrivningsgrad).collect(Collectors.toList()))) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32, ValidationMessageType.INCORRECT_COMBINATION,
                    "lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.invalid_combination",
                    BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
            }

        }
    }

    private void validateSjukskrivningPeriodOverlap(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        utlatande.getSjukskrivningar()
            .stream()
            .filter(Objects::nonNull)
            .forEach(sjukskrivning -> checkSjukskrivningPeriodOverlapAgainstList(validationMessages, sjukskrivning,
                utlatande.getSjukskrivningar()));
    }

    private Predicate<Sjukskrivning> isValidPeriod() {
        return item -> item != null && item.getPeriod() != null && item.getPeriod().isValid();
    }

    private void checkSjukskrivningPeriodOverlapAgainstList(List<ValidationMessage> validationMessages, Sjukskrivning sjukskrivning,
        ImmutableList<Sjukskrivning> sjukskrivningar) {

        if (sjukskrivning == null) {
            return;
        }

        Optional<Sjukskrivning> overlappingPeriod = getPeriodIntervalsOverlapping(sjukskrivning, sjukskrivningar);
        if (overlappingPeriod.isPresent()) {
            if (sjukskrivning.getPeriod().getFrom().equals(overlappingPeriod.get().getPeriod().getFrom())) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages,
                    BEDOMNING_CATEGORY_ID,
                    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 + ".period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from",
                    ValidationMessageType.PERIOD_OVERLAP, BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages,
                    BEDOMNING_CATEGORY_ID,
                    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 + ".period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".tom",
                    ValidationMessageType.PERIOD_OVERLAP, BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
            } else if (sjukskrivning.getPeriod().getFrom().asLocalDate()
                .isBefore(overlappingPeriod.get().getPeriod().getFrom().asLocalDate())) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages,
                    BEDOMNING_CATEGORY_ID,
                    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 + ".period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".tom",
                    ValidationMessageType.PERIOD_OVERLAP, BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
            } else {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages,
                    BEDOMNING_CATEGORY_ID,
                    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 + ".period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from",
                    ValidationMessageType.PERIOD_OVERLAP, BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
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
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32, ValidationMessageType.EMPTY,
                "lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing", BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
        } else {
            if (sjukskrivning.getPeriod() == null) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32, ValidationMessageType.EMPTY,
                    "lisjp.validation.bedomning.sjukskrivningar.period" + sjukskrivning.getSjukskrivningsgrad().getId() + ".missing",
                    BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
            } else {

                boolean fromDateValid = ValidatorUtil.validateDate(sjukskrivning.getPeriod().getFrom(), validationMessages,
                    BEDOMNING_CATEGORY_ID,
                    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 + ".period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".from",
                    null, BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                boolean toDateValid = ValidatorUtil.validateDate(sjukskrivning.getPeriod().getTom(), validationMessages,
                    BEDOMNING_CATEGORY_ID,
                    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 + ".period." + sjukskrivning.getSjukskrivningsgrad().getId() + ".tom",
                    null, BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);

                if (fromDateValid && toDateValid && !sjukskrivning.getPeriod().isValid()) {
                    ValidatorUtil.addValidationErrorWithQuestionId(validationMessages,
                        BEDOMNING_CATEGORY_ID,
                        BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32 + ".period." + sjukskrivning.getSjukskrivningsgrad().getId(),
                        ValidationMessageType.INCORRECT_COMBINATION, BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32);
                }
            }
        }
    }

    private boolean isArbetstidsforlaggningMandatory(LisjpUtlatandeV1 utlatande) {
        return !isAvstangningSmittskydd(utlatande) && utlatande.getSjukskrivningar()
            .stream()
            .anyMatch(e -> e.getPeriod() != null && e.getPeriod().isValid()
                && (e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_3_4
                || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_HALFTEN
                || e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.NEDSATT_1_4));
    }

    private boolean isArbetstidsforlaggningMotiveringForbidden(LisjpUtlatandeV1 utlatande) {
        return utlatande.getSjukskrivningar()
            .stream()
            .anyMatch(e -> e.getSjukskrivningsgrad() == Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT);
    }

    private void validateAtgarder(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // Anything checked at all?
        if (utlatande.getArbetslivsinriktadeAtgarder() == null || utlatande.getArbetslivsinriktadeAtgarder().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, ATGARDER_CATEGORY_ID,
                ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40, ValidationMessageType.EMPTY, ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
        } else {

            // R21 If INTE_AKTUELLT is checked it must be the only selection
            if (utlatande.getArbetslivsinriktadeAtgarder().stream()
                .anyMatch(e -> e.getTyp() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                && utlatande.getArbetslivsinriktadeAtgarder().size() > 1) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, ATGARDER_CATEGORY_ID,
                    ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40, ValidationMessageType.EMPTY,
                    "lisjp.validation.atgarder.inte_aktuellt_no_combine", ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
            }

            // R27 If INTE_AKTUELLT is checked utlatande.getArbetslivsinriktadeAtgarderBeskrivning() must not be
            // answered
            if (utlatande.getArbetslivsinriktadeAtgarder().stream()
                .anyMatch(e -> e.getTyp() == ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)
                && !Strings.nullToEmpty(utlatande.getArbetslivsinriktadeAtgarderBeskrivning()).trim().isEmpty()) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, ATGARDER_CATEGORY_ID,
                    ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40, ValidationMessageType.EMPTY,
                    "lisjp.validation.atgarder.invalid_combination", ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
            }

            // No more than 10 entries are allowed
            if (utlatande.getArbetslivsinriktadeAtgarder().size() > MAX_ARBETSLIVSINRIKTADE_ATGARDER) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, ATGARDER_CATEGORY_ID,
                    ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40, ValidationMessageType.EMPTY,
                    "lisjp.validation.atgarder.too-many", ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
            }

            // R29
            if (!containsUnique(utlatande.getArbetslivsinriktadeAtgarder()
                .stream().map(ArbetslivsinriktadeAtgarder::getTyp).collect(Collectors.toList()))) {
                ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, ATGARDER_CATEGORY_ID,
                    ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40, ValidationMessageType.INCORRECT_COMBINATION,
                    "lisjp.validation.atgarder.typ.invalid_combination", ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40);
            }
        }
    }

    private void validateKontakt(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getKontaktMedFk() != null && !utlatande.getKontaktMedFk()
            && !Strings.nullToEmpty(utlatande.getAnledningTillKontakt()).trim().isEmpty()) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, KONTAKT_CATEGORY_ID, KONTAKT_ONSKAS_SVAR_JSON_ID_26,
                ValidationMessageType.EMPTY, "lisjp.validation.kontakt.invalid_combination", KONTAKT_ONSKAS_SVAR_ID_26);
        }
    }

    private void validateBlanksForOptionalFields(LisjpUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // FMB
        if (ValidatorUtil.isBlankButNotNull(utlatande.getForsakringsmedicinsktBeslutsstod())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, BEDOMNING_CATEGORY_ID,
                FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37, ValidationMessageType.EMPTY,
                "lisjp.validation.blanksteg.otillatet", FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnledningTillKontakt())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, KONTAKT_CATEGORY_ID,
                ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_26, ValidationMessageType.EMPTY,
                "lisjp.validation.blanksteg.otillatet", ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getAnnatGrundForMUBeskrivning())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, GRUNDFORMU_CATEGORY_ID,
                GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1,
                ValidationMessageType.EMPTY, "lisjp.validation.blanksteg.otillatet", GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages,
                MEDICINSKABEHANDLINGAR_CATEGORY_ID, PAGAENDEBEHANDLING_SVAR_JSON_ID_19, ValidationMessageType.EMPTY,
                "lisjp.validation.blanksteg.otillatet", PAGAENDEBEHANDLING_SVAR_ID_19);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, MEDICINSKABEHANDLINGAR_CATEGORY_ID,
                PLANERADBEHANDLING_SVAR_JSON_ID_20, ValidationMessageType.EMPTY,
                "lisjp.validation.blanksteg.otillatet", PLANERADBEHANDLING_SVAR_ID_20);
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationErrorWithQuestionId(validationMessages, OVRIGT_CATEGORY_ID, OVRIGT_SVAR_JSON_ID_25,
                ValidationMessageType.EMPTY, "lisjp.validation.blanksteg.otillatet", OVRIGT_SVAR_ID_25);
        }
    }

    private boolean isAvstangningSmittskydd(LisjpUtlatandeV1 utlatande) {
        return utlatande.getAvstangningSmittskydd() != null && utlatande.getAvstangningSmittskydd();
    }
}
