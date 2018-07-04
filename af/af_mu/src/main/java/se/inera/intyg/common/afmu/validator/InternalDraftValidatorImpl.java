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
package se.inera.intyg.common.afmu.validator;

import com.google.common.base.Strings;
import se.inera.intyg.common.afmu.model.internal.AfmuUtlatande;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static se.inera.intyg.common.afmu.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_2;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_3;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_4;

public class InternalDraftValidatorImpl implements InternalDraftValidator<AfmuUtlatande> {

    private static final String CATEGORY_FUNKTIONSNEDSATTNING = "funktionsnedsattning";
    private static final String CATEGORY_MEDICINSKABEHANDLINGAR = "medicinskabehandlingar";
    private static final String CATEGORY_OVRIGT = "ovrigt";

    private static <T> boolean containsUnique(List<T> list) {
        Set<T> set = new HashSet<>();
        return list.stream().allMatch(t -> set.add(t));
    }

    @Override
    public ValidateDraftResponse validateDraft(AfmuUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        validateFunktionsnedsattning(utlatande, validationMessages);
        validateAktivitetsbegransning(utlatande, validationMessages);

        // Kategori 5 – Medicinska behandlingar/åtgärder
        validateBlanksForOptionalFields(utlatande, validationMessages);
        // vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateFunktionsnedsattning(AfmuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getFunktionsnedsattning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING, FUNKTIONSNEDSATTNING_SVAR_JSON_ID_1,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateAktivitetsbegransning(AfmuUtlatande utlatande, List<ValidationMessage> validationMessages) {
        if (Strings.nullToEmpty(utlatande.getAktivitetsbegransning()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_FUNKTIONSNEDSATTNING, AKTIVITETSBEGRANSNING_SVAR_JSON_ID_2,
                    ValidationMessageType.EMPTY);
        }
    }

    private void validateBlanksForOptionalFields(AfmuUtlatande utlatande, List<ValidationMessage> validationMessages) {

        if (ValidatorUtil.isBlankButNotNull(utlatande.getPagaendeBehandling())) {
            ValidatorUtil.addValidationError(validationMessages,
                    CATEGORY_MEDICINSKABEHANDLINGAR, PAGAENDEBEHANDLING_SVAR_JSON_ID_3, ValidationMessageType.EMPTY,
                    "afmu.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getPlaneradBehandling())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_MEDICINSKABEHANDLINGAR, PLANERADBEHANDLING_SVAR_JSON_ID_4,
                    ValidationMessageType.EMPTY, "afmu.validation.blanksteg.otillatet");
        }
        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_SVAR_JSON_ID_5, ValidationMessageType.EMPTY,
                    "afmu.validation.blanksteg.otillatet");
        }
    }
}
