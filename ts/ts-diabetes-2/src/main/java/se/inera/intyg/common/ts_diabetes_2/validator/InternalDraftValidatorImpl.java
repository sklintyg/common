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
package se.inera.intyg.common.ts_diabetes_2.validator;

import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_diabetes_2.model.internal.TsDiabetes2Utlatande;

public class InternalDraftValidatorImpl implements InternalDraftValidator<TsDiabetes2Utlatande> {

    private static final String CATEGORY_FUNKTIONSNEDSATTNING = "funktionsnedsattning";
    private static final String CATEGORY_AKTIVITETSBEGRANSNING = "aktivitetsbegransning";
    private static final String CATEGORY_UTREDNING_BEHANDLING = "utredningBehandling";
    private static final String CATEGORY_ARBETETS_PAVERKAN = "arbetetsPaverkan";
    private static final String CATEGORY_OVRIGT = "ovrigt";

    private static <T> boolean containsUnique(List<T> list) {
        Set<T> set = new HashSet<>();
        return list.stream().allMatch(t -> set.add(t));
    }

    @Override
    public ValidateDraftResponse validateDraft(TsDiabetes2Utlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 - Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);

        // Kategori 2 - Aktivitetsbegränsning
        validateAktivitetsbegransning(utlatande, validationMessages);

        // Kategori 3 - Behandling / Utredning
        validateUtredningBehandling(utlatande, validationMessages);

        // Kategori 4 - arbetetsPaverkan
        validateArbetetsPaverkan(utlatande, validationMessages);

        // Kategori 5 – Övrigt
        validateBlanksForOptionalFields(utlatande, validationMessages);

        // vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateFunktionsnedsattning(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
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

    private void validateAktivitetsbegransning(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
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

    private void validateUtredningBehandling(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
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

    private void validateArbetetsPaverkan(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {
        // Yes or no must be specified.
        if (utlatande.getHarArbetetsPaverkan() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ARBETETS_PAVERKAN, ARBETETS_PAVERKAN_SVAR_JSON_ID_41,
                    ValidationMessageType.EMPTY);
        }

        if (isSetToTrue(utlatande.getHarArbetetsPaverkan()) && Strings.nullToEmpty(utlatande.getArbetetsPaverkan()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ARBETETS_PAVERKAN, ARBETETS_PAVERKAN_SVAR_JSON_ID_42,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateBlanksForOptionalFields(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {

        if (ValidatorUtil.isBlankButNotNull(utlatande.getUtredningBehandling())) {
            ValidatorUtil.addValidationError(validationMessages,
                    CATEGORY_UTREDNING_BEHANDLING, UTREDNING_BEHANDLING_SVAR_JSON_ID_32, ValidationMessageType.EMPTY,
                    "ts-diabetes-2.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_SVAR_JSON_ID_5, ValidationMessageType.EMPTY,
                    "ts-diabetes-2.validation.blanksteg.otillatet");
        }
    }

    private boolean isSetToTrue(Boolean bool) {
        return bool != null && bool;
    }
}
