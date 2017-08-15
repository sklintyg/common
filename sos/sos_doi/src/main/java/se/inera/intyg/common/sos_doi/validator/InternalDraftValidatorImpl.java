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
package se.inera.intyg.common.sos_doi.validator;

import com.google.common.base.Strings;
import se.inera.intyg.common.sos_doi.model.internal.BidragandeSjukdom;
import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.sos_doi.model.internal.Foljd;
import se.inera.intyg.common.sos_doi.model.internal.OmOperation;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.sos_doi.support.DoiModuleEntryPoint.MODULE_ID;
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

    private void validateDodsorsak(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getDodsorsak()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".dodsorsak", ValidationMessageType.EMPTY);
        }
        if (utlatande.getDodsdatum() != null && !utlatande.getDodsorsakDatum().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".dodsorsakDatum", ValidationMessageType.INVALID_FORMAT);
        }
    }

    private void validateFoljd(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getFoljd().size() > MAX_FOLJD) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".foljd",
                    ValidationMessageType.INCORRECT_COMBINATION);
        }
        for (int i = 0; i < utlatande.getFoljd().size(); i++) {
            Foljd foljd = utlatande.getFoljd().get(i);
            if (Strings.nullToEmpty(foljd.getBeskrivning()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".foljd.1.beskrivning", ValidationMessageType.EMPTY);
            }
            if (foljd.getDatum() != null && !foljd.getDatum().isValidDate()) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".foljd.1.datum", ValidationMessageType.INVALID_FORMAT);

            }
        }
    }

    private void validateBidragandeSjukdomar(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getBidragandeSjukdomar().size() > MAX_BIDRAGANDE_SJUKDOMAR) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".bidragandeSjukdomar",
                    ValidationMessageType.INCORRECT_COMBINATION);
        }
        for (int i = 0; i < utlatande.getBidragandeSjukdomar().size(); i++) {
            BidragandeSjukdom bidragandeSjukdom = utlatande.getBidragandeSjukdomar().get(i);
            if (Strings.nullToEmpty(bidragandeSjukdom.getBeskrivning()).trim().isEmpty()) {
                ValidatorUtil
                        .addValidationError(validationMessages, MODULE_ID + ".foljd." + i + ".beskrivning", ValidationMessageType.EMPTY);
            }
            if (bidragandeSjukdom.getDatum() != null && !bidragandeSjukdom.getDatum().isValidDate()) {
                ValidatorUtil
                        .addValidationError(validationMessages, MODULE_ID + ".foljd." + i + ".datum", ValidationMessageType.INVALID_FORMAT);

            }
        }
    }

    private void validateOperation(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getOperation() == null) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".operation", ValidationMessageType.EMPTY);
        } else if (utlatande.getOperation() == OmOperation.JA) {
            // R13
            if (utlatande.getOperationDatum() == null) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".operationDatum", ValidationMessageType.EMPTY);
            } else if (!utlatande.getOperationDatum().isValidDate()) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".operationDatum", ValidationMessageType.INVALID_FORMAT);
            } else if (utlatande.getOperationDatum() != null && utlatande.getOperationDatum().isValidDate()
                    && utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate() && utlatande.getOperationDatum()
                    .asLocalDate().isAfter(utlatande.getDodsdatum().asLocalDate())) {
                ValidatorUtil
                        .addValidationError(validationMessages, MODULE_ID + ".operationDatum", ValidationMessageType.INCORRECT_COMBINATION);
            }
        }
    }

    private void validateForgiftning(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getForgiftning() == null) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".forgiftning", ValidationMessageType.EMPTY);
        } else if (utlatande.getForgiftning()) {
            // R14
            if (utlatande.getForgiftningOrsak() == null) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".forgiftningOrsak", ValidationMessageType.EMPTY);
            }
            // R16
            if (utlatande.getForgiftningDatum() == null) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".forgiftningDatum", ValidationMessageType.EMPTY);
            } else if (!utlatande.getForgiftningDatum().isValidDate()) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".forgiftningDatum", ValidationMessageType.INVALID_FORMAT);
            } else if (utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate() && utlatande.getForgiftningDatum()
                    .asLocalDate().isAfter(utlatande.getDodsdatum().asLocalDate())) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".forgiftningDatum",
                        ValidationMessageType.INCORRECT_COMBINATION);
            }
            // R17
            if (Strings.nullToEmpty(utlatande.getForgiftningUppkommelse()).trim().isEmpty()) {
                ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".forgiftningUppkommelse", ValidationMessageType.EMPTY);
            }
        }
    }

    private void validateGrunder(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // R18
        if (utlatande.getGrunder().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".grunder", ValidationMessageType.EMPTY);
        } else if (utlatande.getGrunder().size() > MAX_GRUNDER) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".grunder", ValidationMessageType.OTHER);
        } else if (utlatande.getGrunder().size() != utlatande.getGrunder().stream().distinct().count()) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".grunder", ValidationMessageType.INCORRECT_COMBINATION);
        }
    }

    private void validateLand(DoiUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getLand()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, MODULE_ID + ".land", ValidationMessageType.EMPTY);
        }
    }
}
