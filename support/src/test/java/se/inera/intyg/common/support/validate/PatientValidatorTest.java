package se.inera.intyg.common.support.validate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;

public class PatientValidatorTest {

    @Test
    public void passPatientWithCorrectInfo() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostort("Postort");
        patient.setPostnummer("12345");
        List<ValidationMessage> validations = new ArrayList<ValidationMessage>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 0 validation errors", 0, validations.size());
    }

    @Test
    public void failPatientMissingPostadress() {
        Patient patient = new Patient();
        patient.setPostort("Postort");
        patient.setPostnummer("12345");
        List<ValidationMessage> validations = new ArrayList<ValidationMessage>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals(String.format("Expected validation message: %s", validations.get(0).getMessage()), "common.validation.patient.postadress.missing",
                validations.get(0).getMessage());
    }

    @Test
    public void failPatientMissingPostort() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostnummer("12345");
        List<ValidationMessage> validations = new ArrayList<ValidationMessage>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals(String.format("Expected validation message: %s", validations.get(0).getMessage()), "common.validation.patient.postort.missing",
                validations.get(0).getMessage());
    }

    @Test
    public void failPatientMissingPostnummer() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostort("Postort");
        List<ValidationMessage> validations = new ArrayList<ValidationMessage>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals(String.format("Expected validation message: %s", validations.get(0).getMessage()), "common.validation.patient.postnummer.missing",
                validations.get(0).getMessage());
    }

    @Test(expected=RuntimeException.class)
    public void exceptionThrownWithNullPatient() {
        PatientValidator.validate(null, new ArrayList<ValidationMessage>());
    }
}
