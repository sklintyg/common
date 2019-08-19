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
package se.inera.intyg.common.fk7263.validator;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.model.internal.PrognosBedomning;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;

public class InternalDraftValidator {

    private static final Logger LOG = LoggerFactory.getLogger(InternalDraftValidator.class);

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    public ValidateDraftResponse validateDraft(Fk7263Utlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // intyget baseras på
        validateVardkontakter(utlatande, validationMessages);
        validateReferenser(utlatande, validationMessages);
        // fält 2
        validateDiagnose(utlatande, validationMessages);
        // Falt 4
        validateFunktionsnedsattning(utlatande, validationMessages);
        // Falt 5
        validateAktivitetsbegransning(utlatande, validationMessages);
        // fält 8
        validateArbetsformaga(utlatande, validationMessages);
        // fält 6a
        validateOvrigaRekommendationer(utlatande, validationMessages);
        // fält 11
        validateRessatt(utlatande, validationMessages);
        // fält 13
        validateKommentar(utlatande, validationMessages);
        // vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return new ValidateDraftResponse(ValidatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateVardkontakter(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getTelefonkontaktMedPatienten() != null) {
            ValidatorUtil.validateDate(utlatande.getTelefonkontaktMedPatienten(), validationMessages, "intygbaseratpa", "telefonkontakt",
                null);
        }
        if (utlatande.getUndersokningAvPatienten() != null) {
            ValidatorUtil.validateDate(utlatande.getUndersokningAvPatienten(),
                validationMessages,
                "intygbaseratpa",
                "undersokning",
                null);
        }
    }

    private void validateReferenser(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {

        // Fält 4b - höger Check that we at least got one field set regarding
        // what the certificate is based on if not smittskydd
        if (!utlatande.isAvstangningSmittskydd()) {

            if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                && utlatande.getJournaluppgifter() == null && utlatande.getAnnanReferens() == null) {
                ValidatorUtil.addValidationError(validationMessages, "intygbaseratpa", "intygbaseratpa", ValidationMessageType.EMPTY);
            }
        }

        if (utlatande.getAnnanReferens() != null) {
            ValidatorUtil.validateDate(utlatande.getAnnanReferens(), validationMessages, "intygbaseratpa", "referenser", null);
        }
        if (utlatande.getAnnanReferens() != null && Strings.nullToEmpty(utlatande.getAnnanReferensBeskrivning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "intygbaseratpa", "annat", ValidationMessageType.EMPTY);
        }
        if (utlatande.getJournaluppgifter() != null) {
            ValidatorUtil.validateDate(utlatande.getJournaluppgifter(), validationMessages, "intygbaseratpa", "journaluppgifter", null);
        }
    }

    private void validateKommentar(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        if (utlatande.getPrognosBedomning() == PrognosBedomning.arbetsformagaPrognosGarInteAttBedoma
            && Strings.nullToEmpty(utlatande.getArbetsformagaPrognosGarInteAttBedomaBeskrivning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "prognos", "arbetsformagaPrognosGarInteAttBedomaBeskrivning",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateRessatt(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 11 - optional
        boolean inForandratRessatt = utlatande.isRessattTillArbeteAktuellt();
        boolean inEjForandratRessatt = utlatande.isRessattTillArbeteEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt && inEjForandratRessatt) {
            ValidatorUtil.addValidationError(validationMessages, "rekommendationer", "ressattTillArbete", ValidationMessageType.OTHER,
                "fk7263.validation.forandrat-ressatt.choose-one");
        }
    }

    private void validateArbetsformaga(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 8a - arbetsformoga - sysselsattning - applies of not smittskydd is set
        if (!utlatande.isAvstangningSmittskydd()) {
            if (!utlatande.isNuvarandeArbete() && !utlatande.isArbetsloshet() && !utlatande.isForaldrarledighet()) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning", "sysselsattning", ValidationMessageType.EMPTY);
            } else if (utlatande.isNuvarandeArbete() && Strings.nullToEmpty(utlatande.getNuvarandeArbetsuppgifter()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, "sysselsattning", "nuvarandearbetsuppgifter",
                    ValidationMessageType.EMPTY);
            }
        }

        // validate 8b - regardless of smittskydd
        if (ValidatorUtil.isInvalidTjanstgoringstid(utlatande.getTjanstgoringstid())) {
            ValidatorUtil.addValidationError(validationMessages, "nedsattning", "nedsattning", ValidationMessageType.OTHER,
                "fk7263.validation.nedsattning.tjanstgoringstid");
        }

        // Check that from and tom is valid in all present intervals before doing more checks
        if (isValidDateInIntervals(validationMessages, utlatande)) {
            validateIntervals(validationMessages, "nedsattning", "arbetsformaga", utlatande.getNedsattMed100(),
                utlatande.getNedsattMed75(), utlatande.getNedsattMed50(), utlatande.getNedsattMed25());
        }
    }

    private boolean isValidDateInIntervals(List<ValidationMessage> validationMessages, Fk7263Utlatande utlatande) {
        boolean success = true;
        InternalLocalDateInterval[] intervals = {utlatande.getNedsattMed100(), utlatande.getNedsattMed75(), utlatande.getNedsattMed50(),
            utlatande.getNedsattMed25()};
        final int nedsattmed100Index = 0;
        final int nedsattmed75Index = 1;
        final int nedsattmed50Index = 2;
        final int nedsattmed25Index = 3;

        if (ValidatorUtil.allNulls(intervals)) {
            ValidatorUtil.addValidationError(validationMessages, "nedsattning", "arbetsformaga", ValidationMessageType.EMPTY);
            return false;
        }
        // if the interval is not null and either from or tom is invalid, raise validation error
        // use independent conditions to check this to be able to give specific validation errors for each case
        if (intervals[nedsattmed100Index] != null) {
            success &= ValidatorUtil.validateInternalDateInterval(intervals[nedsattmed100Index], validationMessages,
                "nedsattning", "nedsattMed100", "fk7263.validation.nedsattning.nedsattmed100.incorrect-format");
        }
        if (intervals[nedsattmed75Index] != null) {
            success &= ValidatorUtil.validateInternalDateInterval(intervals[nedsattmed75Index], validationMessages,
                "nedsattning", "nedsattMed75", "fk7263.validation.nedsattning.nedsattmed75.incorrect-format");
        }
        if (intervals[nedsattmed50Index] != null) {
            success &= ValidatorUtil.validateInternalDateInterval(intervals[nedsattmed50Index], validationMessages,
                "nedsattning", "nedsattMed50", "fk7263.validation.nedsattning.nedsattmed50.incorrect-format");
        }
        if (intervals[nedsattmed25Index] != null) {
            success &= ValidatorUtil.validateInternalDateInterval(intervals[nedsattmed25Index], validationMessages,
                "nedsattning", "nedsattMed25", "fk7263.validation.nedsattning.nedsattmed25.incorrect-format");
        }
        return success;
    }

    private void validateAktivitetsbegransning(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 5 Aktivitetsbegränsning relaterat till diagnos och funktionsnedsättning
        String aktivitetsbegransning = utlatande.getAktivitetsbegransning();
        if (!utlatande.isAvstangningSmittskydd() && Strings.nullToEmpty(aktivitetsbegransning).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "aktivitetsbegransning", "aktivitetsbegransning",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateFunktionsnedsattning(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        String funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (!utlatande.isAvstangningSmittskydd() && Strings.nullToEmpty(funktionsnedsattning).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "funktionsnedsattning", "funktionsnedsattning",
                ValidationMessageType.EMPTY);
        }
    }

    private void validateDiagnose(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {

        // Fält 3 - always optional regardless of smittskydd

        // Fält 2 - Medicinskt tillstånd kod - mandatory if not smittskydd
        if (utlatande.isAvstangningSmittskydd()) {
            return;
        }

        if (!Strings.nullToEmpty(utlatande.getDiagnosKod()).trim().isEmpty()) {
            String kodsystem = utlatande.getDiagnosKodsystem1();
            if (Strings.nullToEmpty(kodsystem).trim().isEmpty()) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod(), kodsystem, "diagnos.diagnosKod",
                "fk7263.validation.diagnos.invalid", validationMessages);
        } else {
            ValidatorUtil.addValidationError(validationMessages, "diagnos", "diagnosKod",
                ValidationMessageType.EMPTY, "fk7263.validation.diagnos.missing");
        }

        // Validate bidiagnos 1
        if (!Strings.nullToEmpty(utlatande.getDiagnosKod2()).trim().isEmpty()) {
            String kodsystem = utlatande.getDiagnosKodsystem2();
            if (Strings.nullToEmpty(kodsystem).trim().isEmpty()) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod2(), kodsystem,
                "diagnos.diagnosKod2", "fk7263.validation.diagnos2.invalid", validationMessages);
        }

        // Validate bidiagnos 2
        if (!Strings.nullToEmpty(utlatande.getDiagnosKod3()).trim().isEmpty()) {
            String kodsystem = utlatande.getDiagnosKodsystem3();
            if (Strings.nullToEmpty(kodsystem).trim().isEmpty()) {
                // Default to ICD-10
                kodsystem = Diagnoskodverk.ICD_10_SE.name();
            }
            validateDiagnosKod(utlatande.getDiagnosKod3(), kodsystem,
                "diagnosKod3", "fk7263.validation.diagnos3.invalid", validationMessages);
        }

    }

    private void validateDiagnosKod(String diagnosKod, String kodsystem, String field, String msgKey,
        List<ValidationMessage> validationMessages) {
        // if moduleService is not available, skip this validation
        if (moduleService == null) {
            LOG.warn("Forced to skip validation of diagnosKod since an implementation of ModuleService is not available");
            return;
        }

        if (!moduleService.validateDiagnosisCode(diagnosKod, kodsystem)) {
            ValidatorUtil.addValidationError(validationMessages, "diagnos", field, ValidationMessageType.INVALID_FORMAT, msgKey);
        }
    }

    private void validateOvrigaRekommendationer(Fk7263Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Fält 6a - If Övrigt is checked, something must be entered.
        if (utlatande.isRekommendationOvrigtCheck() && Strings.nullToEmpty(utlatande.getRekommendationOvrigt()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "rekommendationer", "rekommendationovrigt", ValidationMessageType.EMPTY,
                "fk7263.validation.rekommendationer.ovriga");
        }
    }

    /**
     * @param validationMessages list collecting message
     * @param fieldId field id
     * @param intervals intervals
     * @return booleans
     */
    protected boolean validateIntervals(List<ValidationMessage> validationMessages, String categoryId, String fieldId,
        InternalLocalDateInterval... intervals) {
        if (intervals == null || ValidatorUtil.allNulls(intervals)) {
            ValidatorUtil.addValidationError(validationMessages, categoryId, fieldId, ValidationMessageType.EMPTY,
                "fk7263.validation.nedsattning.choose-at-least-one");
            return false;
        }

        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i] != null) {
                for (int j = i + 1; j < intervals.length; j++) {
                    // Overlap OR abuts(one intervals tom day == another's from day) is considered invalid
                    if (intervals[j] != null && intervals[i].overlaps(intervals[j])) {
                        ValidatorUtil.addValidationError(validationMessages, categoryId, fieldId, ValidationMessageType.OTHER,
                            "fk7263.validation.nedsattning.overlapping-date-interval");
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
