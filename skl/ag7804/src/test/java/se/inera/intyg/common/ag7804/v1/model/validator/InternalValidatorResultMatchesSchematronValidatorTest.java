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
package se.inera.intyg.common.ag7804.v1.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getInternalValidationErrorString;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getNumberOfInternalValidationErrors;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getNumberOfTransportValidationErrors;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getTransportValidationErrorString;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getXmlFromModel;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.google.common.base.Charsets;
import com.helger.commons.debug.GlobalDebug;
import com.helger.schematron.svrl.SVRLHelper;

import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.ag7804.v1.rest.Ag7804ModuleApiV1;
import se.inera.intyg.common.ag7804.v1.utils.Scenario;
import se.inera.intyg.common.ag7804.v1.utils.ScenarioFinder;
import se.inera.intyg.common.ag7804.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ag7804.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.ag7804.v1.validator.ValidatorUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

/**
 * Data driven test that uses Scenario and ScenarioFinder along with the JUnit Parameterized test runner,
 * uses test data from internal/scenarios and transport/scenarios, so in order to create new tests, just add
 * corresponding json- and XML-files in these directories.
 *
 * @author erik
 */
@RunWith(Parameterized.class)
public class InternalValidatorResultMatchesSchematronValidatorTest {

    private static final String CORRECT_DIAGNOSKODSYSTEM1 = "ICD_10_SE";
    private static final String CORRECT_DIAGNOSKODSYSTEM2 = "KSH_97_P";
    private static final String CORRECT_DIAGNOSKOD1 = "S47";
    private static final String CORRECT_DIAGNOSKOD2 = "Z731";
    private static final String CORRECT_DIAGNOSKOD3 = "A039";
    private static final String CORRECT_DIAGNOSKOD4 = "A00-";

    // Used for labeling tests.
    private static String name;
    @Mock
    private static WebcertModuleService mockModuleService;
    @InjectMocks
    private static InternalDraftValidatorImpl internalValidator;

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    private Scenario scenario;
    private boolean shouldFail;
    @InjectMocks
    private ValidatorUtil validatorUtil;

    public InternalValidatorResultMatchesSchematronValidatorTest(String name, Scenario scenario, boolean shouldFail) {
        this.scenario = scenario;
        this.shouldFail = shouldFail;
        InternalValidatorResultMatchesSchematronValidatorTest.name = name;
    }

    /**
     * Process test data and supply it to the test.
     * The format for the test data needs to be: {name to display for current test, the scenario to test, expected
     * outcome of the test}.
     *
     * @return Collection<Object [ ]>
     */
    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        List<Object[]> retList = ScenarioFinder.getInternalScenarios("fail-forMangaSysselsattningar").stream()
            .map(u -> new Object[]{u.getName(), u, true})
            .collect(Collectors.toList());
        retList.addAll(
            ScenarioFinder.getInternalScenarios("pass-*").stream()
                .map(u -> new Object[]{u.getName(), u, false})
                .collect(Collectors.toList()));
        return retList;
    }

    /**
     * Perform internal and schematron validation on the supplied Scenario.
     *
     * @param fail Whether the test should expect validation errors or not.
     */
    private static void doInternalAndSchematronValidation(Scenario scenario, boolean fail) throws Exception {
        Ag7804UtlatandeV1 utlatandeFromJson = scenario.asInternalModel();

        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);

        RegisterCertificateType intyg = scenario.asTransportModel();
        String convertedXML = getXmlFromModel(intyg);

        RegisterCertificateValidator validator = new RegisterCertificateValidator(Ag7804ModuleApiV1.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));

        String internalValidationErrors = getInternalValidationErrorString(internalValidationResponse);

        String transportValidationErrors = getTransportValidationErrorString(result);

        doAssertions(fail, internalValidationResponse, result, internalValidationErrors, transportValidationErrors);
    }

    private static void doAssertions(boolean fail, ValidateDraftResponse internalValidationResponse, SchematronOutputType result,
        String internalValidationErrors, String transportValidationErrors) {
        if (fail) {
            assertEquals(
                String.format("Scenario: %s\n Transport: %s \n Internal: %s\n Expected number of validation-errors to be the same.",
                    name, transportValidationErrors, internalValidationErrors),
                getNumberOfTransportValidationErrors(result),
                getNumberOfInternalValidationErrors(internalValidationResponse));
            assertTrue(String.format("File: %s, Internal validation, expected ValidationStatus.INVALID",
                name),
                internalValidationResponse.getStatus().equals(ValidationStatus.INVALID));

            assertTrue(String.format("File: %s, Schematronvalidation, expected errors > 0",
                name),
                SVRLHelper.getAllFailedAssertions(result).size() > 0);
        } else {
            assertTrue(String.format("File: %s, Internal validation, expected ValidationStatus.VALID \n Validation-errors: %s",
                name, internalValidationErrors),
                internalValidationResponse.getStatus().equals(ValidationStatus.VALID));
            assertTrue(String.format("File: %s, Schematronvalidation, expected 0 errors \n Validation-errors: %s",
                name, transportValidationErrors),
                SVRLHelper.getAllFailedAssertions(result).size() == 0);
        }
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // use reflection to set InternalValidatorUtil in InternalDraftValidator
        Field field = InternalDraftValidatorImpl.class.getDeclaredField("validatorUtil");
        field.setAccessible(true);
        field.set(internalValidator, validatorUtil);
    }

    @Test
    public void testScenarios() throws Exception {
        when(mockModuleService.validateDiagnosisCode(CORRECT_DIAGNOSKOD1, CORRECT_DIAGNOSKODSYSTEM1)).thenReturn(true);
        when(mockModuleService.validateDiagnosisCode(CORRECT_DIAGNOSKOD2, CORRECT_DIAGNOSKODSYSTEM1)).thenReturn(true);
        when(mockModuleService.validateDiagnosisCode(CORRECT_DIAGNOSKOD3, CORRECT_DIAGNOSKODSYSTEM1)).thenReturn(true);
        when(mockModuleService.validateDiagnosisCode(CORRECT_DIAGNOSKOD4, CORRECT_DIAGNOSKODSYSTEM2)).thenReturn(true);
        doInternalAndSchematronValidation(scenario, shouldFail);
    }

}
