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
package se.inera.intyg.common.sos_db.validator;

import se.inera.intyg.common.sos_db.model.internal.DbUtlatande;
import se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateBarn;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsdatum;
import static se.inera.intyg.common.sos_parent.validator.SosInternalDraftValidator.validateDodsplats;

public class InternalDraftValidatorImpl implements InternalDraftValidator<DbUtlatande> {
    private static final String PREFIX = "sos_db";

    @Override
    public ValidateDraftResponse validateDraft(DbUtlatande utlatande) {
        List<ValidationMessage> validationMessages = new ArrayList<>();

        PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);

        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        SosInternalDraftValidator.validateIdentitetStyrkt(utlatande, validationMessages, PREFIX);

        validateDodsdatum(utlatande, validationMessages, PREFIX);

        validateDodsplats(utlatande, validationMessages, PREFIX);

        validateBarn(utlatande, validationMessages, PREFIX);
        // Validate question 5
        // Validate question 6
        // Validate question 7
        // Validate question 8

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

}
