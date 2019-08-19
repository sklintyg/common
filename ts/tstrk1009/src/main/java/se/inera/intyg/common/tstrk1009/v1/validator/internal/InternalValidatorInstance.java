/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.validator.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;
import se.inera.intyg.common.tstrk1009.v1.model.internal.KorkortBehorighetGrupp;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;

public class InternalValidatorInstance {

    private static final String CATEGORY_INTYG_AVSER = "intygAvser";
    private static final String CATEGORY_IDENTITET = "identitet";
    private List<ValidationMessage> validationMessages;

    private ValidationContext context;

    public InternalValidatorInstance() {
        validationMessages = new ArrayList<>();
    }

    /**
     * Validates an internal draft of an {@link Tstrk1009UtlatandeV1} (this means the object being validated is not
     * necessarily
     * complete).
     *
     * @param utlatande an internal {@link Tstrk1009UtlatandeV1}
     * @return a ValidateDraftResponseHolder with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(Tstrk1009UtlatandeV1 utlatande) {

        if (utlatande == null) {
            ValidatorUtil.addValidationError(validationMessages, "utlatande", "utlatande", ValidationMessageType.EMPTY,
                "tstrk1009.validation.utlatande.missing");

        } else {

            context = new ValidationContext(utlatande);

            // OBS! Utökas formuläret i framtiden, lägg in validering i rätt ordning nedan.
            PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);
            validateIntygAvser(utlatande.getIntygetAvserBehorigheter().getTyper());
            ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);
        }

        return new ValidateDraftResponse(ValidatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateIntygAvser(final Set<KorkortBehorighetGrupp> intygetAvserBehorigheter) {

        if (intygetAvserBehorigheter == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_INTYG_AVSER, "intygAvser", ValidationMessageType.EMPTY);
            return;
        }

        if (intygetAvserBehorigheter.isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_INTYG_AVSER, "intygAvser.korkortstyp",
                ValidationMessageType.EMPTY);
        }
    }
}
