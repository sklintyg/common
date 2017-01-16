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
package se.inera.intyg.common.support.validate;

import java.util.List;

import com.google.common.base.Strings;

import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

/**
 * Created by BESA on 2016-08-26.
 */
public final class PatientValidator {

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    private PatientValidator() {
    }

    public static void validate(Patient patient, List<ValidationMessage> validationMessages) {
        if (patient == null) {
            throw new RuntimeException("No Patient found when attempting to validate");
        }
        validateString(validationMessages, patient.getPostadress(), "patient.grunddata.patient.postadress");
        validateString(validationMessages, patient.getPostnummer(), "patient.grunddata.patient.postnummer");
        if (!Strings.nullToEmpty(patient.getPostnummer()).trim().isEmpty() && !STRING_VALIDATOR.validateStringAsPostalCode(patient.getPostnummer())) {
            validationMessages.add(new ValidationMessage("patient.grunddata.patient.postnummer", ValidationMessageType.INVALID_FORMAT,
                    "common.validation.postnummer.incorrect-format"));
        }
        validateString(validationMessages, patient.getPostort(), "patient.grunddata.patient.postort");
    }

    private static void validateString(List<ValidationMessage> validationMessages, String text, String field) {
        if (Strings.nullToEmpty(text).trim().isEmpty()) {
            validationMessages.add(new ValidationMessage(field, ValidationMessageType.EMPTY));
        }
    }
}
