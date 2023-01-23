/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v2.validator;

import org.springframework.stereotype.Component;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.validator.internal.InternalValidatorInstance;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;

@Component("ts-diabetes.v2.InternalDraftValidator")
public class Validator implements InternalDraftValidator<TsDiabetesUtlatandeV2> {

    /**
     * Validates an external Utlatande.
     *
     * @param utlatande se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2
     * @return List of validation errors, or an empty string if validated correctly
     */
    @Override
    public ValidateDraftResponse validateDraft(TsDiabetesUtlatandeV2 utlatande) {
        InternalValidatorInstance instance = new InternalValidatorInstance();
        return instance.validate(utlatande);
    }

}
