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
package se.inera.intyg.common.af00213.v2.validator;

import static se.inera.intyg.common.af00213.v2.model.converter.RespConstants.SKIPPAR_BALTE_DELSVAR_JSON_ID_4_1;
import static se.inera.intyg.common.af00213.v2.model.converter.RespConstants.SKIPPAR_BALTE_SVAR_JSON_ID_4_2;
import static se.inera.intyg.common.af00213.validator.BaseInternalDraftValidator.validateAktivitetsbegransning;
import static se.inera.intyg.common.af00213.validator.BaseInternalDraftValidator.validateBlanksForOptionalFields;
import static se.inera.intyg.common.af00213.validator.BaseInternalDraftValidator.validateFunktionsnedsattning;
import static se.inera.intyg.common.af00213.validator.BaseInternalDraftValidator.validateUtredningBehandling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import se.inera.intyg.common.af00213.v2.model.internal.Af00213UtlatandeV2;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.ValidatorUtil;

@Component("af00213.v2.InternalDraftValidatorImpl")
public class InternalDraftValidatorImpl implements InternalDraftValidator<Af00213UtlatandeV2> {

    private static final String CATEGORY_TRAFIK_BETEENDE = "trafikbeteende";

    @Override
    public ValidateDraftResponse validateDraft(Af00213UtlatandeV2 utlatande) {

        List<ValidationMessage> validationMessages = new ArrayList<>();

        // Kategori 1 - Funktionsnedsättning
        validateFunktionsnedsattning(utlatande, validationMessages);

        // Kategori 2 - Aktivitetsbegränsning
        validateAktivitetsbegransning(utlatande, validationMessages);

        // Kategori 3 - Behandling / Utredning
        validateUtredningBehandling(utlatande, validationMessages);

        // Kategori 4 - trafikbeteende
        validateSkipparBalte(utlatande, validationMessages);

        // Kategori 5 – Övrigt
        validateBlanksForOptionalFields(utlatande, validationMessages);

        // vårdenhet
        ValidatorUtil.validateVardenhet(utlatande.getGrundData(), validationMessages);

        return ValidatorUtil.buildValidateDraftResponse(validationMessages);
    }

    private void validateSkipparBalte(Af00213UtlatandeV2 utlatande, List<ValidationMessage> validationMessages) {
        // Yes or no must be specified.
        if (utlatande.getHarSkipparBalte() == null) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_TRAFIK_BETEENDE, SKIPPAR_BALTE_DELSVAR_JSON_ID_4_1,
                    ValidationMessageType.EMPTY);
        }

        if (isSetToTrue(utlatande.getHarSkipparBalte()) && Strings.nullToEmpty(utlatande.getSkipparBalteMotivering()).trim().isEmpty()) {
            ValidatorUtil.addValidationError(validationMessages, CATEGORY_TRAFIK_BETEENDE, SKIPPAR_BALTE_SVAR_JSON_ID_4_2,
                    ValidationMessageType.EMPTY);
        }
    }

    private boolean isSetToTrue(Boolean bool) {
        return bool != null && bool;
    }
}
