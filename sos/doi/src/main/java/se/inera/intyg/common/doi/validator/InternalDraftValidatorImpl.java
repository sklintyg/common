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
package se.inera.intyg.common.doi.validator;

import com.google.common.base.Strings;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.doi.support.DoiModuleEntryPoint.MODULE_ID;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateBarn;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsdatum;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsplats;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateIdentitetStyrkt;

public class InternalDraftValidatorImpl implements InternalDraftValidator<DoiUtlatande> {
    private static final int MAX_GRUNDER = 5;
    private static final int MAX_BIDRAGANDE_SJUKDOMAR = 8;
    private static final int MAX_FOLJD = 3;

    @Override
    public ValidateDraftResponse validateDraft(DoiUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        validateIdentitetStyrkt(utlatande, validationMessages, MODULE_ID);
        validateDodsdatum(utlatande, validationMessages, MODULE_ID);
        validateDodsplats(utlatande, validationMessages, MODULE_ID);
        validateBarn(utlatande, validationMessages, MODULE_ID);
        validateDodsorsak(utlatande, validationMessages);
        validateFoljd(utlatande, validationMessages);
        validateBidragandeSjukdomar(utlatande, validationMessages);
        validateOperation(utlatande, validationMessages);
        validateForgiftning(utlatande, validationMessages);
        validateGrunder(utlatande, validationMessages);
        validateLand(utlatande, validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateLand(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getLand()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "personuppgifter.land", ValidationMessageType.EMPTY);
        }
    }

    private void validateDodsorsak(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getTerminalDodsorsak() == null || Strings.nullToEmpty(utlatande.getTerminalDodsorsak().getBeskrivning()).trim()
                .isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak.terminalDodsorsak", ValidationMessageType.EMPTY);
        }
        if (utlatande.getTerminalDodsorsak() != null && utlatande.getTerminalDodsorsak().getDatum() != null && !utlatande
                .getTerminalDodsorsak().getDatum().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak.dodsorsakDatum", ValidationMessageType.INVALID_FORMAT);
        }
    }

    private void validateFoljd(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getFoljd().size() > MAX_FOLJD) {
            ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak.foljd",
                    ValidationMessageType.INCORRECT_COMBINATION);
        }
        for (int i = 0; i < utlatande.getFoljd().size(); i++) {
            Dodsorsak foljd = utlatande.getFoljd().get(i);
            if (Strings.nullToEmpty(foljd.getBeskrivning()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages,
                        "utlatandeOrsak.foljd." + i + ".beskrivning",
                        ValidationMessageType.EMPTY);
            }
            if (foljd.getDatum() != null && !foljd.getDatum().isValidDate()) {
                ValidatorUtil.addValidationError(validationMessages,
                        "utlatandeOrsak.foljd." + i + ".datum",
                        ValidationMessageType.INVALID_FORMAT);
            }
        }
    }

    private void validateBidragandeSjukdomar(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getBidragandeSjukdomar().size() > MAX_BIDRAGANDE_SJUKDOMAR) {
            ValidatorUtil.addValidationError(validationMessages, "utlatandeOrsak.bidragandeSjukdomar",
                    ValidationMessageType.INCORRECT_COMBINATION);
        }
        for (int i = 0; i < utlatande.getBidragandeSjukdomar().size(); i++) {
            Dodsorsak bidragandeSjukdom = utlatande.getBidragandeSjukdomar().get(i);
            if (Strings.nullToEmpty(bidragandeSjukdom.getBeskrivning()).trim().isEmpty()) {
                ValidatorUtil
                        .addValidationError(validationMessages, "utlatandeOrsak.bidragandeSjukdomar." + i + ".beskrivning",
                                ValidationMessageType.EMPTY);
            }
            if (bidragandeSjukdom.getDatum() != null && !bidragandeSjukdom.getDatum().isValidDate()) {
                ValidatorUtil
                        .addValidationError(validationMessages, "utlatandeOrsak.bidragandeSjukdomar." + i + ".datum",
                                ValidationMessageType.INVALID_FORMAT);

            }
        }
    }

    private void validateOperation(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getOperation() == null) {
            ValidatorUtil.addValidationError(validationMessages, "operation.operation", ValidationMessageType.EMPTY);
        } else if (utlatande.getOperation() == OmOperation.JA) {
            // R13
            if (utlatande.getOperationDatum() == null) {
                ValidatorUtil.addValidationError(validationMessages, "operation.operationDatum", ValidationMessageType.EMPTY);
            } else if (!utlatande.getOperationDatum().isValidDate()) {
                ValidatorUtil.addValidationError(validationMessages, "operation.operationDatum", ValidationMessageType.INVALID_FORMAT);
            } else if (utlatande.getOperationDatum() != null && utlatande.getOperationDatum().isValidDate()
                    && utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate() && utlatande.getOperationDatum()
                    .asLocalDate().isAfter(utlatande.getDodsdatum().asLocalDate())) {
                ValidatorUtil
                        .addValidationError(validationMessages, "operation.operationDatum", ValidationMessageType.INCORRECT_COMBINATION);
            }
            if (Strings.nullToEmpty(utlatande.getOperationAnledning()).isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, "operation.operationAnledning", ValidationMessageType.EMPTY);
            }
        } else {
            if (utlatande.getOperationDatum() != null) {
                ValidatorUtil
                        .addValidationError(validationMessages, "operation.operationDatum", ValidationMessageType.INCORRECT_COMBINATION,
                                "operation.operationDatum.operationNejUppgiftSaknas");
            }
            if (utlatande.getOperationAnledning() != null) {
                ValidatorUtil
                        .addValidationError(validationMessages, "operation.operationAnledning",
                                ValidationMessageType.INCORRECT_COMBINATION,
                                "operation.operationAnledning.operationNejUppgiftSaknas");
            }
        }
    }

    private void validateForgiftning(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getForgiftning() == null) {
            ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftning", ValidationMessageType.EMPTY);
        } else if (utlatande.getForgiftning()) {
            // R14
            if (utlatande.getForgiftningOrsak() == null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftningOrsak", ValidationMessageType.EMPTY);
            }
            // R16
            if (utlatande.getForgiftningDatum() == null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftningDatum", ValidationMessageType.EMPTY);
            } else if (!utlatande.getForgiftningDatum().isValidDate()) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftningDatum", ValidationMessageType.INVALID_FORMAT);
            } else if (utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate() && utlatande.getForgiftningDatum()
                    .asLocalDate().isAfter(utlatande.getDodsdatum().asLocalDate())) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftningDatum",
                        ValidationMessageType.INCORRECT_COMBINATION);
            }
            // R17
            if (Strings.nullToEmpty(utlatande.getForgiftningUppkommelse()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftningUppkommelse", ValidationMessageType.EMPTY);
            }
        } else {
            if (utlatande.getForgiftningOrsak() != null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftningOrsak",
                        ValidationMessageType.INCORRECT_COMBINATION);
            }
            if (utlatande.getForgiftningDatum() != null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftningDatum",
                        ValidationMessageType.INCORRECT_COMBINATION);
            }
            if (utlatande.getForgiftningUppkommelse() != null) {
                ValidatorUtil.addValidationError(validationMessages, "forgiftning.forgiftningUppkommelse",
                        ValidationMessageType.INCORRECT_COMBINATION);
            }
        }
    }

    private void validateGrunder(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // R18
        if (utlatande.getGrunder().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "dodsorsakgrund.grunder", ValidationMessageType.EMPTY);
        } else if (utlatande.getGrunder().size() > MAX_GRUNDER) {
            ValidatorUtil.addValidationError(validationMessages, "dodsorsakgrund.grunder", ValidationMessageType.OTHER);
        } else if (utlatande.getGrunder().size() != utlatande.getGrunder().stream().distinct().count()) {
            ValidatorUtil.addValidationError(validationMessages, "dodsorsakgrund.grunder", ValidationMessageType.INCORRECT_COMBINATION);
        }
    }
}
