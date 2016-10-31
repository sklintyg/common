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
        assertEquals("Expected validation failure for postadress", "patient.grunddata.patient.postadress", validations.get(0).getField());
    }

    @Test
    public void failPatientMissingPostort() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostnummer("12345");
        List<ValidationMessage> validations = new ArrayList<ValidationMessage>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals("Expected validation failure for postort", "patient.grunddata.patient.postort", validations.get(0).getField());
    }

    @Test
    public void failPatientMissingPostnummer() {
        Patient patient = new Patient();
        patient.setPostadress("Testadress");
        patient.setPostort("Postort");
        List<ValidationMessage> validations = new ArrayList<ValidationMessage>();
        PatientValidator.validate(patient, validations);
        assertEquals("Expected 1 validation errors", 1, validations.size());
        assertEquals("Expected validation failure for postnummer", "patient.grunddata.patient.postnummer", validations.get(0).getField());
        assertEquals(validations.get(0).getField(), "patient.grunddata.patient.postnummer", validations.get(0).getField());
    }

    @Test(expected=RuntimeException.class)
    public void exceptionThrownWithNullPatient() {
        PatientValidator.validate(null, new ArrayList<ValidationMessage>());
    }
}
