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
package se.inera.intyg.common.ts_tstrk1062.v1.validator.internal;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.PatientValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.RespConstants.*;

import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.IdKontroll;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.IntygAvser;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.TsTstrk1062UtlatandeV1;


/**
 * Class for validating drafts of the internal model.
 *
 * @author erik
 */
public class InternalValidatorInstance {

    private static final String CATEGORY_INTYG_AVSER = "intygAvser";
    private static final String CATEGORY_ID_KONTROLL = "idKontroll";

    private List<ValidationMessage> validationMessages;

    private ValidationContext context;

    public InternalValidatorInstance() {
        validationMessages = new ArrayList<>();
    }

    /**
     * Validates an internal draft of an {@link TsTstrk1062UtlatandeV1} (this means the object being validated is not
     * necessarily
     * complete).
     *
     * @param utlatande an internal {@link TsTstrk1062UtlatandeV1}
     * @return a ValidateDraftResponseHolder with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(TsTstrk1062UtlatandeV1 utlatande) {

        if (utlatande == null) {
            ValidatorUtil.addValidationError(validationMessages, "utlatande", "utlatande", ValidationMessageType.EMPTY,
                    "ts-tstrk1062.validation.utlatande.missing");

        } else {

            context = new ValidationContext(utlatande);

            // OBS! Utökas formuläret i framtiden, lägg in validering i rätt ordning nedan.
            PatientValidator.validate(utlatande.getGrundData().getPatient(), validationMessages);
            validateIntygAvser(utlatande.getIntygAvser());
            validateIdKontroll(utlatande.getIdKontroll());


            ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);
        }

        return new ValidateDraftResponse(ValidatorUtil.getValidationStatus(validationMessages), validationMessages);
    }

    private void validateIdKontroll(IdKontroll idKontroll) {
        if (idKontroll == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_ID_KONTROLL, ID_KONTROLL_SVAR_JSON_ID + ".typ", ValidationMessageType.EMPTY,
                    "ts-tstrk1062.validation.idkontroll.missing");
            return;
        }
    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_INTYG_AVSER, INTYG_AVSER_SVAR_JSON_ID, ValidationMessageType.EMPTY,
                    "ts-tstrk1062.validation.intygavses.missing");
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_INTYG_AVSER, INTYG_AVSER_SVAR_JSON_ID + ".korkortstyp",
                    ValidationMessageType.EMPTY);
        }
    }
}
