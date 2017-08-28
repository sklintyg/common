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
package se.inera.intyg.common.sos_parent.validator;

import com.google.common.base.Strings;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.util.List;

public final class SosInternalDraftValidator {
    private SosInternalDraftValidator() {
    }

    public static void validateIdentitetStyrkt(SosUtlatande utlatande, List<ValidationMessage> validationMessages, String prefix) {
        if (Strings.nullToEmpty(utlatande.getIdentitetStyrkt()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "personuppgifter.identitetStyrkt", ValidationMessageType.EMPTY);
        }
    }

    public static void validateDodsdatum(SosUtlatande utlatande, List<ValidationMessage> validationMessages, String prefix) {
        if (utlatande.getDodsdatumSakert() == null) {
            ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats.dodsdatumSakert", ValidationMessageType.EMPTY);
            return;
        }

        // R1 & R2
        if (utlatande.getDodsdatum() == null) {
            ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats.dodsdatum", ValidationMessageType.EMPTY);
        } else if (!utlatande.getDodsdatum().isValidDate()) {
            ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats.dodsdatum", ValidationMessageType.INVALID_FORMAT);
        }

        // R3
        if (!utlatande.getDodsdatumSakert()) {
            if (utlatande.getAntraffatDodDatum() == null) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats.antraffatDod", ValidationMessageType.EMPTY);
            } else if (!utlatande.getAntraffatDodDatum().isValidDate()) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats.antraffatDod",
                        ValidationMessageType.INVALID_FORMAT);
            }
        } else {
            if (utlatande.getAntraffatDodDatum() != null) {
                ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats.antraffatDod",
                        ValidationMessageType.INCORRECT_COMBINATION, prefix + ".validation.antraffatDod.dodsdatumSakert");
            }
        }
    }

    public static void validateDodsplats(SosUtlatande utlatande, List<ValidationMessage> validationMessages, String prefix) {
        if (Strings.nullToEmpty(utlatande.getDodsplatsKommun()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats.dodsplatsKommun", ValidationMessageType.EMPTY);
        }

        // R4
        if (utlatande.getDodsplatsBoende() == null) {
            ValidatorUtil.addValidationError(validationMessages, "dodsdatumOchdodsPlats.dodsplatsBoende", ValidationMessageType.EMPTY);
        }
    }

    public static void validateBarn(SosUtlatande utlatande, List<ValidationMessage> validationMessages, String prefix) {
        if (utlatande.getBarn() == null) {
            ValidatorUtil.addValidationError(validationMessages, "barnSomAvlidit.barn", ValidationMessageType.EMPTY);
        }
    }

}
