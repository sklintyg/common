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
package se.inera.intyg.common.db.validator;

import se.inera.intyg.common.db.model.internal.DbUtlatande;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.db.support.DbModuleEntryPoint.MODULE_ID;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateBarn;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsdatum;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsplats;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateIdentitetStyrkt;

public class InternalDraftValidatorImpl implements InternalDraftValidator<DbUtlatande> {

    @Override
    public ValidateDraftResponse validateDraft(DbUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        validateIdentitetStyrkt(utlatande, validationMessages, MODULE_ID);
        validateDodsdatum(utlatande, validationMessages, MODULE_ID);
        validateDodsplats(utlatande, validationMessages, MODULE_ID);
        validateBarn(utlatande, validationMessages, MODULE_ID);
        validateExplosivtImplantat(utlatande, validationMessages);
        validateUndersokning(utlatande, validationMessages);
        validatePolisanmalan(utlatande, validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateExplosivtImplantat(DbUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // R5
        if (utlatande.getExplosivImplantat() == null) {
            ValidatorUtil.addValidationError(validationMessages, "explosivImplantat.explosivImplantat", ValidationMessageType.EMPTY);
        } else if (utlatande.getExplosivImplantat() && utlatande.getExplosivAvlagsnat() == null) {
            ValidatorUtil.addValidationError(validationMessages, "explosivImplantat.explosivAvlagsnat", ValidationMessageType.EMPTY);
        } else if (!utlatande.getExplosivImplantat() && utlatande.getExplosivAvlagsnat() != null) {
            ValidatorUtil
                    .addValidationError(validationMessages, "explosivImplantat.explosivAvlagsnat",
                            ValidationMessageType.INCORRECT_COMBINATION,
                            MODULE_ID + ".validation.explosivAvlagsnat.explosivImplantatFalse");
        }
    }

    private void validateUndersokning(DbUtlatande utlatande, List<ValidationMessage> validationMessages) {
        // R6 & R7
        if (utlatande.getUndersokningYttre() == null) {
            ValidatorUtil.addValidationError(validationMessages, "yttreUndersokning.undersokningYttre", ValidationMessageType.EMPTY);
        } else if (utlatande.getUndersokningYttre() == Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN) {
            if (utlatande.getUndersokningDatum() == null) {
                ValidatorUtil.addValidationError(validationMessages, "yttreUndersokning.undersokningDatum", ValidationMessageType.EMPTY);
            } else if (!utlatande.getUndersokningDatum().isValidDate()) {
                ValidatorUtil
                        .addValidationError(validationMessages, "yttreUndersokning.undersokningDatum",
                                ValidationMessageType.INVALID_FORMAT);
            } else if (!utlatande.getUndersokningDatum().isReasonable() ||
                    utlatande.getUndersokningDatum().asLocalDate().isAfter(LocalDate.now())) {
                ValidatorUtil
                        .addValidationError(validationMessages, "yttreUndersokning.undersokningDatum",
                                ValidationMessageType.INVALID_FORMAT, "common.validation.date_out_of_range");
            } else if (utlatande.getDodsdatum() != null && utlatande.getDodsdatum().isValidDate()
                    && utlatande.getUndersokningDatum().asLocalDate().isAfter(utlatande.getDodsdatum().asLocalDate())) {
                ValidatorUtil
                        .addValidationError(validationMessages, "yttreUndersokning.undersokningDatum",
                                ValidationMessageType.INCORRECT_COMBINATION,
                                "db.validation.undersokningDatum.after.dodsdatum");
            }
        }
    }

    private void validatePolisanmalan(DbUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (utlatande.getPolisanmalan() == null) {
            ValidatorUtil.addValidationError(validationMessages, "polisanmalan.polisanmalan", ValidationMessageType.EMPTY);
        } else if (utlatande.getUndersokningYttre() == Undersokning.UNDERSOKNING_SKA_GORAS && !utlatande.getPolisanmalan()) {
            // R19
            ValidatorUtil.addValidationError(validationMessages, "polisanmalan.polisanmalan", ValidationMessageType.INCORRECT_COMBINATION);
        }
    }
}
