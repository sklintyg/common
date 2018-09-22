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
package se.inera.intyg.common.af00213.validator;

import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.model.converter.Af00213RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;

import java.util.List;

import com.google.common.base.Strings;

import se.inera.intyg.common.af00213.model.internal.Af00213Utlatande;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.ValidatorUtil;

public final class BaseInternalDraftValidator {

    private static final String CATEGORY_FUNKTIONSNEDSATTNING = "funktionsnedsattning";
    private static final String CATEGORY_AKTIVITETSBEGRANSNING = "aktivitetsbegransning";
    private static final String CATEGORY_UTREDNING_BEHANDLING = "utredningBehandling";
    private static final String CATEGORY_OVRIGT = "ovrigt";

    private BaseInternalDraftValidator() {
    }

    public static void validateFunktionsnedsattning(Af00213Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Yes or no must be specified.
        if (utlatande.getHarFunktionsnedsattning() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11,
                    ValidationMessageType.EMPTY);
        }
        if (isSetToTrue(utlatande.getHarFunktionsnedsattning())
                && Strings.nullToEmpty(utlatande.getFunktionsnedsattning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12,
                    ValidationMessageType.EMPTY);
        }
    }

    public static void validateAktivitetsbegransning(Af00213Utlatande utlatande, List<ValidationMessage> validationMessages) {
        if (isSetToTrue(utlatande.getHarFunktionsnedsattning()) && isSetToTrue(utlatande.getHarAktivitetsbegransning())
                && Strings.nullToEmpty(utlatande.getAktivitetsbegransning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_AKTIVITETSBEGRANSNING, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22,
                    ValidationMessageType.EMPTY);
        }

        if (isSetToTrue(utlatande.getHarFunktionsnedsattning()) && utlatande.getHarAktivitetsbegransning() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_AKTIVITETSBEGRANSNING, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21,
                    ValidationMessageType.INCORRECT_COMBINATION);
        }
    }

    public static void validateUtredningBehandling(Af00213Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Yes or no must be specified.
        if (utlatande.getHarUtredningBehandling() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_UTREDNING_BEHANDLING, UTREDNING_BEHANDLING_SVAR_JSON_ID_31,
                    ValidationMessageType.EMPTY);
        }

        if (isSetToTrue(utlatande.getHarUtredningBehandling())
                && (utlatande.getUtredningBehandling() == null || utlatande.getUtredningBehandling().isEmpty())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_UTREDNING_BEHANDLING, UTREDNING_BEHANDLING_SVAR_JSON_ID_32,
                    ValidationMessageType.EMPTY);
        }
    }

    public static void validateBlanksForOptionalFields(Af00213Utlatande utlatande, List<ValidationMessage> validationMessages) {

        if (ValidatorUtil.isBlankButNotNull(utlatande.getUtredningBehandling())) {
            ValidatorUtil.addValidationError(validationMessages,
                    CATEGORY_UTREDNING_BEHANDLING, UTREDNING_BEHANDLING_SVAR_JSON_ID_32, ValidationMessageType.EMPTY,
                    "af00213.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_SVAR_JSON_ID_5, ValidationMessageType.EMPTY,
                    "af00213.validation.blanksteg.otillatet");
        }
    }

    public static boolean isSetToTrue(Boolean bool) {
        return bool != null && bool;
    }
}
