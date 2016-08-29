/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.support.modules.validator;

import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;

import java.util.List;

/**
 * Created by BESA on 2016-08-26.
 */
public class PatientValidator {

    private static final ModuleValidatorUtil moduleValidator = new ModuleValidatorUtil();

    public void validate(Patient patient, List<ValidationMessage> validationMessages) {
        if (patient == null) {
            return;
        }
        moduleValidator.assertStringNotEmpty(validationMessages, patient.getPostadress(), "patient.postadress", "common.validation.patient.postadress.missing");
        moduleValidator.assertStringNotEmpty(validationMessages, patient.getPostnummer(), "patient.postnummer", "common.validation.patient.postnummer.missing");
        moduleValidator.assertStringNotEmpty(validationMessages, patient.getPostort(), "patient.postort", "common.validation.patient.postort.missing");
    }

}
