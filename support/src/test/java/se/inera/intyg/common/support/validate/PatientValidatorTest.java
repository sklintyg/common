/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

public class PatientValidatorTest {

    @Test
    public void passPatientWithCorrectInfo() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostort("Postort");
        patient.setPostnummer("12345");
        List<ValidationMessage> validations = new ArrayList<>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 0 validation errors", 0, validations.size());
    }

    @Test
    public void failPatientMissingPostadress() {
        Patient patient = new Patient();
        patient.setPostort("Postort");
        patient.setPostnummer("12345");
        List<ValidationMessage> validations = new ArrayList<>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals("Expected validation failure for postadress", "grunddata.patient.postadress", validations.get(0).getField());
        assertEquals("patient", validations.get(0).getCategory());
    }

    @Test
    public void failPatientMissingPostort() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostnummer("12345");
        List<ValidationMessage> validations = new ArrayList<>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals("Expected validation failure for postort", "grunddata.patient.postort", validations.get(0).getField());
        assertEquals("patient", validations.get(0).getCategory());
    }

    @Test
    public void failPatientMissingPostnummer() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostort("Postort");
        List<ValidationMessage> validations = new ArrayList<>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals("Expected validation failure for postnummer", "grunddata.patient.postnummer", validations.get(0).getField());
        assertEquals("Expected EMPTY for postort", ValidationMessageType.EMPTY, validations.get(0).getType());
        assertEquals("patient", validations.get(0).getCategory());
    }

    @Test
    public void failPatientInvalidPostnummer() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostnummer("123456");
        patient.setPostort("Postort");
        List<ValidationMessage> validations = new ArrayList<>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals("Expected validation failure for postort", "grunddata.patient.postnummer", validations.get(0).getField());
        assertEquals("Expected INVALID_FORMAT for postort", ValidationMessageType.INVALID_FORMAT, validations.get(0).getType());
        assertEquals("patient", validations.get(0).getCategory());
    }

    @Test(expected = RuntimeException.class)
    public void exceptionThrownWithNullPatient() {
        PatientValidator.validate(null, new ArrayList<>());
    }
}
