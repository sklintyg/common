/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

class PatientValidatorTest {

  @Test
  void passPatientWithCorrectInfo() {
    Patient patient = new Patient();
    patient.setPostadress("Testadress");
    patient.setPostort("Postort");
    patient.setPostnummer("12345");
    List<ValidationMessage> validations = new ArrayList<>();
    PatientValidator.validate(patient, validations);
    assertEquals(0, validations.size(), "Expected 0 validation errors");
  }

  @Test
  void failPatientMissingPostadress() {
    Patient patient = new Patient();
    patient.setPostort("Postort");
    patient.setPostnummer("12345");
    List<ValidationMessage> validations = new ArrayList<>();
    PatientValidator.validate(patient, validations);
    assertEquals(1, validations.size(), "Expected 1 validation errors");
    assertEquals(
        "grunddata.patient.postadress",
        validations.get(0).getField(),
        "Expected validation failure for postadress");
    assertEquals("patient", validations.get(0).getCategory());
  }

  @Test
  void failPatientMissingPostort() {
    Patient patient = new Patient();
    patient.setPostadress("Testadress");
    patient.setPostnummer("12345");
    List<ValidationMessage> validations = new ArrayList<>();
    PatientValidator.validate(patient, validations);
    assertEquals(1, validations.size(), "Expected 1 validation errors");
    assertEquals(
        "grunddata.patient.postort",
        validations.get(0).getField(),
        "Expected validation failure for postort");
    assertEquals("patient", validations.get(0).getCategory());
  }

  @Test
  void failPatientMissingPostnummer() {
    Patient patient = new Patient();
    patient.setPostadress("Testadress");
    patient.setPostort("Postort");
    List<ValidationMessage> validations = new ArrayList<>();
    PatientValidator.validate(patient, validations);
    assertEquals(1, validations.size(), "Expected 1 validation errors");
    assertEquals(
        "grunddata.patient.postnummer",
        validations.get(0).getField(),
        "Expected validation failure for postnummer");
    assertEquals(
        ValidationMessageType.EMPTY, validations.get(0).getType(), "Expected EMPTY for postort");
    assertEquals("patient", validations.get(0).getCategory());
  }

  @Test
  void failPatientInvalidPostnummer() {
    Patient patient = new Patient();
    patient.setPostadress("Testadress");
    patient.setPostnummer("123456");
    patient.setPostort("Postort");
    List<ValidationMessage> validations = new ArrayList<>();
    PatientValidator.validate(patient, validations);
    assertEquals(1, validations.size(), "Expected 1 validation errors");
    assertEquals(
        "grunddata.patient.postnummer",
        validations.get(0).getField(),
        "Expected validation failure for postort");
    assertEquals(
        ValidationMessageType.INVALID_FORMAT,
        validations.get(0).getType(),
        "Expected INVALID_FORMAT for postort");
    assertEquals("patient", validations.get(0).getCategory());
  }

  @Test
  void exceptionThrownWithNullPatient() {
    assertThrows(
        RuntimeException.class,
        () -> {
          PatientValidator.validate(null, new ArrayList<>());
        });
  }
}
