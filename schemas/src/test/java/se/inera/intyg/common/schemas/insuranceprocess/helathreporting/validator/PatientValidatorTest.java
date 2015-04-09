package se.inera.intyg.common.schemas.insuranceprocess.helathreporting.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import iso.v21090.dt.v1.II;
import org.junit.Before;
import org.junit.Test;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.validator.PatientValidator;

import java.util.ArrayList;
import java.util.List;


public class PatientValidatorTest {
    private PatientType patient;
    private II patientId;
    private List<String> errors;

    @Before
    public void setup() {
        patient = new PatientType();
        patientId = new II();
        patientId.setRoot("1.2.752.129.2.1.3.1");
        patientId.setExtension("19121212-1212");
        patient.setPersonId(patientId);
        patient.setFullstandigtNamn("namn");
        errors = new ArrayList<>();
    }

    @Test
    public void testNullPatient() {
        patient = null;
        assertTrue(validatePatient().contains("No Patient element found!"));
    }

    @Test
    public void testNullPatientId() {
        patient.setPersonId(null);
        assertTrue(validatePatient().contains("No Patient Id found!"));
    }

    @Test
    public void testEmtpyPatientIdRoot() {
        patientId.setRoot(null);
        assertTrue(validatePatient().contains("Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3"));
    }

    private List<String> validatePatient() {
        return PatientValidator.validateAndCorrect(patient);
    }

    @Test
    public void testIncorrectPatientIdRoot() {
        patientId.setRoot("incorrect");
        assertTrue(validatePatient().contains("Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3"));
    }

    @Test
    public void testEmtpyPatientIdExtension() {
        patientId.setExtension(null);
        assertTrue(validatePatient().contains("No Patient Id found!"));
    }

    @Test
    public void testInvalidPatientIdExtension() {
        patientId.setExtension("19121212-121X");
        assertTrue(validatePatient().contains("Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX."));
    }

    @Test
    public void testDashInPatientIdExtensionIsCorrected() {
        patientId.setExtension("191212121212");
        assertTrue(validatePatient().isEmpty());
        assertEquals("19121212-1212", patientId.getExtension());
    }

}
