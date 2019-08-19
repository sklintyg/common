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
package se.inera.intyg.common.doi.v1.validator;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.doi.support.DoiModuleEntryPoint.MODULE_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_JSON_ID;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateBarn;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsdatum;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsplats;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateIdentitetStyrkt;

@Component("doi.v1.InternalDraftValidatorImpl")
public class InternalDraftValidatorImpl implements InternalDraftValidator<DoiUtlatandeV1> {

    private static final int MAX_GRUNDER = 5;
    private static final int MAX_BIDRAGANDE_SJUKDOMAR = 8;
    private static final int MAX_FOLJD = 3;
    public static final int FOUR_WEEKS_IN_DAYS = 28;

    @Override
    public ValidateDraftResponse validateDraft(DoiUtlatandeV1 utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);

        validateIdentitetStyrkt(utlatande, validationMessages);
        validateDodsdatum(utlatande, validationMessages, MODULE_ID);
        validateDodsplats(utlatande, validationMessages);
        validateBarn(utlatande, validationMessages);
        validateDodsorsak(utlatande, validationMessages);
        validateFoljd(utlatande, validationMessages);
        validateBidragandeSjukdomar(utlatande, validationMessages);
        validateOperation(utlatande, validationMessages);
        validateForgiftning(utlatande, validationMessages);
        validateGrunder(utlatande, validationMessages);
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateDodsorsak(DoiUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getTerminalDodsorsak() == null || Strings.nullToEmpty(utlatande.getTerminalDodsorsak().getBeskrivning()).trim()
            .isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", TERMINAL_DODSORSAK_JSON_ID, ValidationMessageType.EMPTY);
        }
        // R22 - "Datum terminal dödsorsak"
        if (utlatande.getTerminalDodsorsak() != null && utlatande.getTerminalDodsorsak().getDatum() != null) {

            final String validationField = TERMINAL_DODSORSAK_JSON_ID + "." + DODSORSAK_DATUM_JSON_ID;

            if (ValidatorUtil.validateDateAndCheckIfFuture(utlatande.getTerminalDodsorsak().getDatum(),
                validationMessages, "utlatandeOrsak", validationField, "common.validation.date.today.or.earlier")) {

                if (ValidatorUtil.isNotNullTrue(utlatande.getDodsdatumSakert())
                    // R22-1 - får inte infalla efter dödsdatum, om dödsdatumet är säkert.
                    && utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate()
                    && ValidatorUtil.isDateAfter(utlatande.getTerminalDodsorsak().getDatum(), utlatande.getDodsdatum())) {
                    ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", validationField,
                        ValidationMessageType.INCORRECT_COMBINATION, "doi.validation.terminalDodsorsak.datum.efterDodsdatum");
                } else if (ValidatorUtil.isNotNullFalse(utlatande.getDodsdatumSakert())
                    // R22-2 - får inte infalla efter datumet då man anträffade den döde, om dödsdatumet är ej säkert.
                    && utlatande.getAntraffatDodDatum() != null && utlatande.getAntraffatDodDatum().isValidDate()
                    && ValidatorUtil.isDateAfter(utlatande.getTerminalDodsorsak().getDatum(), utlatande.getAntraffatDodDatum())) {
                    ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", validationField,
                        ValidationMessageType.INCORRECT_COMBINATION,
                        "doi.validation.terminalDodsorsak.datum.efterAntraffatDodsdatum");
                }
            }
        }
    }

    private void validateFoljd(DoiUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getFoljd().size() > MAX_FOLJD) {
            ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", FOLJD_JSON_ID,
                ValidationMessageType.INCORRECT_COMBINATION);
        }
        // To check R21 we need to keep track of earliest date seen so far.
        // From beginning its dodsdatum and then each foljd in instance order should be before or equal to the previous dates.
        LocalDate minDate = (utlatande.getTerminalDodsorsak() != null && utlatande.getTerminalDodsorsak().getDatum() != null && utlatande
            .getTerminalDodsorsak().getDatum().isValidDate())
            ? utlatande.getTerminalDodsorsak().getDatum().asLocalDate()
            : null;
        for (int i = 0; i < utlatande.getFoljd().size(); i++) {
            Dodsorsak foljd = utlatande.getFoljd().get(i);
            if (Strings.nullToEmpty(foljd.getBeskrivning()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages,
                    "utlatandeOrsak",
                    FOLJD_JSON_ID + "[" + i + "].beskrivning",
                    ValidationMessageType.EMPTY);
            }
            if (foljd.getDatum() != null) {
                String validationField = FOLJD_JSON_ID + "[" + i + "].datum";
                Boolean validDate = ValidatorUtil.validateDate(foljd.getDatum(),
                    validationMessages, "utlatandeOrsak", validationField, null);

                if (validDate) {
                    // R21 b, check with today if no earlier date
                    if (minDate == null && foljd.getDatum().asLocalDate().isAfter(LocalDate.now())) {
                        ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", validationField,
                            ValidationMessageType.INCORRECT_COMBINATION, "common.validation.date.today.or.earlier");
                        validDate = false;
                    } else if (minDate != null && foljd.getDatum().asLocalDate().isAfter(minDate)) {
                        String message = "doi.validation.foljd.datum.f.val-050";
                        if (i > 0) {
                            message = "doi.validation.foljd.datum.f.val-051";
                        }
                        ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", validationField,
                            ValidationMessageType.INCORRECT_COMBINATION, message);
                        validDate = false;
                    }
                }
                if (validDate) {
                    minDate = foljd.getDatum().asLocalDate();
                }
            }
        }
    }

    private void validateBidragandeSjukdomar(DoiUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getBidragandeSjukdomar().size() > MAX_BIDRAGANDE_SJUKDOMAR) {
            ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", BIDRAGANDE_SJUKDOM_JSON_ID,
                ValidationMessageType.INCORRECT_COMBINATION);
        }
        for (int i = 0; i < utlatande.getBidragandeSjukdomar().size(); i++) {
            Dodsorsak bidragandeSjukdom = utlatande.getBidragandeSjukdomar().get(i);
            //Skip if all fields are empty.
            if (Strings.nullToEmpty(bidragandeSjukdom.getBeskrivning()).trim().isEmpty() && bidragandeSjukdom.getDatum() == null
                && bidragandeSjukdom.getSpecifikation() == null) {
                continue;
            }
            if (Strings.nullToEmpty(bidragandeSjukdom.getBeskrivning()).trim().isEmpty()) {
                ValidatorUtil
                    .addValidationError(validationMessages, "utlatandeOrsak", BIDRAGANDE_SJUKDOM_JSON_ID + "[" + i + "]."
                        + DODSORSAK_OM_JSON_ID, ValidationMessageType.EMPTY);
            }
            if (bidragandeSjukdom.getDatum() != null) {
                String validationField = BIDRAGANDE_SJUKDOM_JSON_ID + "[" + i + "]." + DODSORSAK_DATUM_JSON_ID;
                if (bidragandeSjukdom.getDatum().isValidDate()
                    && ValidatorUtil.isDateAfter(bidragandeSjukdom.getDatum(), utlatande.getDodsdatum())) {
                    ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", validationField,
                        ValidationMessageType.INCORRECT_COMBINATION, "doi.validation.terminalDodsorsak.datum.efterDodsdatum");
                } else if (bidragandeSjukdom.getDatum().isValidDate()
                    && ValidatorUtil.isDateAfter(bidragandeSjukdom.getDatum(), utlatande.getAntraffatDodDatum())) {
                    ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak", validationField,
                        ValidationMessageType.INCORRECT_COMBINATION,
                        "doi.validation.terminalDodsorsak.datum.efterAntraffatDodsdatum");
                }
                ValidatorUtil.validateDate(bidragandeSjukdom.getDatum(), validationMessages, "utlatandeOrsak",
                    validationField, "common.validation.ue-date.invalid_format");
            }
        }
    }

    private void validateOperation(DoiUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getOperation() == null) {
            ValidatorUtil.addValidationError(validationMessages, "operation",
                OPERATION_OM_JSON_ID, ValidationMessageType.EMPTY);
        } else if (utlatande.getOperation() == OmOperation.JA) {
            // R13
            if (ValidatorUtil.validateDate(utlatande.getOperationDatum(), validationMessages, "operation",
                OPERATION_DATUM_JSON_ID, null)) {
                if (ValidatorUtil.isDateAfter(utlatande.getOperationDatum(), utlatande.getDodsdatum())) {
                    ValidatorUtil
                        .addValidationError(validationMessages, "operation", OPERATION_DATUM_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION, "operation.operationDatum.efterDodsdatum");
                } else if (ValidatorUtil.isDateAfter(utlatande.getOperationDatum(), utlatande.getAntraffatDodDatum())) {
                    ValidatorUtil
                        .addValidationError(validationMessages, "operation", OPERATION_DATUM_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION, "operation.operationDatum.efterAntraffatDodDatum");
                } else if (utlatande.getDodsdatumSakert() != null && utlatande.getDodsdatumSakert()
                    && ValidatorUtil.isDateAfter(utlatande.getDodsdatum(),
                    new InternalDate(utlatande.getOperationDatum().asLocalDate().plusDays(FOUR_WEEKS_IN_DAYS)))) {
                    ValidatorUtil
                        .addValidationError(validationMessages, "operation", OPERATION_DATUM_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION,
                            "common.validation.date.e-06");
                } else if (utlatande.getOperationDatum().isBeforeBeginningOfLastYear()) {
                    ValidatorUtil
                        .addValidationError(validationMessages, "operation", OPERATION_DATUM_JSON_ID,
                            ValidationMessageType.OTHER,
                            "common.validation.date.beforeLastYear");
                }
            }
            if (Strings.nullToEmpty(utlatande.getOperationAnledning()).isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, "operation", OPERATION_ANLEDNING_JSON_ID, ValidationMessageType.EMPTY);
            }
        } else {
            if (utlatande.getOperationDatum() != null) {
                ValidatorUtil
                    .addValidationError(validationMessages, "operation", OPERATION_DATUM_JSON_ID,
                        ValidationMessageType.INCORRECT_COMBINATION, "operation.operationDatum.operationNejUppgiftSaknas");
            }
            if (utlatande.getOperationAnledning() != null) {
                ValidatorUtil
                    .addValidationError(validationMessages, "operation", OPERATION_ANLEDNING_JSON_ID,
                        ValidationMessageType.INCORRECT_COMBINATION,
                        "operation.operationAnledning.operationNejUppgiftSaknas");
            }
        }
    }

    private void validateForgiftning(DoiUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getForgiftning() == null) {
            ValidatorUtil.addValidationError(validationMessages, "forgiftning", FORGIFTNING_OM_JSON_ID, ValidationMessageType.EMPTY);
        } else if (utlatande.getForgiftning()) {
            // R14
            if (utlatande.getForgiftningOrsak() == null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning", FORGIFTNING_ORSAK_JSON_ID, ValidationMessageType.EMPTY);
            }
            // R16
            if (ValidatorUtil.validateDate(utlatande.getForgiftningDatum(), validationMessages, "forgiftning",
                FORGIFTNING_DATUM_JSON_ID, null)) {
                if (ValidatorUtil.isDateAfter(utlatande.getForgiftningDatum(), utlatande.getDodsdatum())) {
                    ValidatorUtil
                        .addValidationError(validationMessages, "forgiftning", FORGIFTNING_DATUM_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION, "forgiftning.forgiftningDatum.efterDodsdatum");
                } else if (ValidatorUtil.isDateAfter(utlatande.getForgiftningDatum(), utlatande.getAntraffatDodDatum())) {
                    ValidatorUtil
                        .addValidationError(validationMessages, "forgiftning", FORGIFTNING_DATUM_JSON_ID,
                            ValidationMessageType.INCORRECT_COMBINATION, "forgiftning.forgiftningDatum.efterAntraffatDodDatum");
                }
            }

            // R17
            if (Strings.nullToEmpty(utlatande.getForgiftningUppkommelse()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning", FORGIFTNING_UPPKOMMELSE_JSON_ID,
                    ValidationMessageType.EMPTY);
            }
        } else {
            if (utlatande.getForgiftningOrsak() != null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning", FORGIFTNING_OM_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION, "doi.validation.forgiftning.orsak.incorrect_combination");
            }
            if (utlatande.getForgiftningDatum() != null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning", FORGIFTNING_OM_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION, "doi.validation.forgiftning.datum.incorrect_combination");
            }
            if (utlatande.getForgiftningUppkommelse() != null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning", FORGIFTNING_OM_JSON_ID,
                    ValidationMessageType.INCORRECT_COMBINATION,
                    "doi.validation.forgiftning.uppkommelse.incorrect_combination");
            }
        }
    }

    private void validateGrunder(DoiUtlatandeV1 utlatande, List<ValidationMessage> validationMessages) {
        // R18
        if (utlatande.getGrunder().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "dodsorsakgrund", GRUNDER_JSON_ID, ValidationMessageType.EMPTY);
        } else if (utlatande.getGrunder().size() > MAX_GRUNDER) {
            ValidatorUtil.addValidationError(validationMessages, "dodsorsakgrund", GRUNDER_JSON_ID, ValidationMessageType.OTHER);
        } else if (utlatande.getGrunder().size() != utlatande.getGrunder().stream().distinct().count()) {
            ValidatorUtil.addValidationError(validationMessages, "dodsorsakgrund", GRUNDER_JSON_ID,
                ValidationMessageType.INCORRECT_COMBINATION);
        }
    }
}
