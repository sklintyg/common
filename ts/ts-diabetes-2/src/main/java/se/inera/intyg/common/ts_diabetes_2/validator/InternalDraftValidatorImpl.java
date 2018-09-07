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

import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.OVRIGT_DELSVAR_JSON_ID;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.ts_diabetes_2.model.internal.TsDiabetes2Utlatande;

public class InternalDraftValidatorImpl implements InternalDraftValidator<TsDiabetes2Utlatande> {

    private static final String CATEGORY_INTYGET_AVSER_BEHORIGHET = "intygAvser";
    private static final String CATEGORY_IDENTITET_STYRKT_GENOM = "'identitetStyrktGenom'";
    private static final String CATEGORY_ALLMANT = "allmant";
    private static final String CATEGORY_HYPOGLYKEMIER = "hypoglykemier";
    private static final String CATEGORY_SYNFUNKTION = "synfunktion";
    private static final String CATEGORY_OVRIGT = "ovrigt";
    private static final String CATEGORY_BEDOMNING = "bedomning";

    private static <T> boolean containsUnique(List<T> list) {
        Set<T> set = new HashSet<>();
        return list.stream().allMatch(t -> set.add(t));
    }

    @Override
    public ValidateDraftResponse validateDraft(TsDiabetes2Utlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        // TODO: Only handles ovrigt category for now
        // Kategori 6 – Övrigt
        validateBlanksForOptionalFields(utlatande, validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateBlanksForOptionalFields(TsDiabetes2Utlatande utlatande, List<ValidationMessage> validationMessages) {

        if (ValidatorUtil.isBlankButNotNull(utlatande.getOvrigt())) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_OVRIGT, OVRIGT_DELSVAR_JSON_ID, ValidationMessageType.EMPTY,
                    "ts-diabetes-2.validation.blanksteg.otillatet");
        }
    }

    private boolean isSetToTrue(Boolean bool) {
        return bool != null && bool;
    }
}
