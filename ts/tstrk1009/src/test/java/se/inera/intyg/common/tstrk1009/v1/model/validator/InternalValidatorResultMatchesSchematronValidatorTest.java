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
package se.inera.intyg.common.tstrk1009.v1.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.helger.commons.debug.GlobalDebug;
import com.helger.schematron.svrl.SVRLHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.tstrk1009.support.Tstrk1009EntryPoint;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;
import se.inera.intyg.common.tstrk1009.v1.utils.Scenario;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioFinder;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.tstrk1009.v1.validator.internal.InternalValidatorInstance;

@RunWith(Parameterized.class)
public class InternalValidatorResultMatchesSchematronValidatorTest {

    private Scenario scenario;

    private boolean shouldFail;

    // Used for labeling tests.
    private String name;

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    private static InternalValidatorInstance internalValidator;

    public InternalValidatorResultMatchesSchematronValidatorTest(String name, Scenario scenario, boolean shouldFail) {
        this.scenario = scenario;
        this.shouldFail = shouldFail;
        this.name = name;
    }

    @Before
    public void setup() {
        internalValidator = new InternalValidatorInstance();
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

        List<Object[]> retList = new ArrayList<>();
        // Failing tests
//        retList.addAll(ScenarioFinder.getInternalScenarios("fail-*").stream()
//                .map(u -> new Object[] { u.getName(), u, true })
//                .collect(Collectors.toList()));
        // Passing tests
        retList.addAll(
            ScenarioFinder.getInternalScenarios("valid-max*").stream()
                .map(u -> new Object[]{u.getName(), u, false})
                .collect(Collectors.toList()));
        return retList;
    }

    @Test
    public void testScenarios() throws Exception {
        doInternalAndSchematronValidation(scenario, shouldFail);
    }

    /**
     * Perform internal and schematron validation on the supplied Scenario.
     *
     * @param fail Whether the test should expect validation errors or not.
     */
    private void doInternalAndSchematronValidation(Scenario scenario, boolean fail) throws Exception {
        Tstrk1009UtlatandeV1 utlatandeFromJson = scenario.asInternalModel();

        ValidateDraftResponse internalValidationResponse = internalValidator.validate(utlatandeFromJson);

        final List<ValidationMessage> filteredValidations = internalValidationResponse.getValidationErrors().stream()
            .filter(error -> !error.getCategory().equals("patient"))
            .collect(Collectors.toList());

        internalValidationResponse = new ValidateDraftResponse(
            filteredValidations.isEmpty() ? ValidationStatus.VALID : ValidationStatus.INVALID,
            filteredValidations);

        RegisterCertificateType intyg = scenario.asTransportModel();
        String convertedXML = getXmlFromIntyg(intyg);

        RegisterCertificateValidator validator = new RegisterCertificateValidator(Tstrk1009EntryPoint.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));

        String internalValidationErrors = getInternalValidationErrorString(internalValidationResponse);

        String transportValidationErrors = getTransportValidationErrorString(result);

        doAssertions(fail, internalValidationResponse, result, internalValidationErrors, transportValidationErrors);
    }

    private void doAssertions(boolean fail, ValidateDraftResponse internalValidationResponse, SchematronOutputType result,
        String internalValidationErrors, String transportValidationErrors) {
        if (fail) {
            assertEquals(
                String.format("Scenario: %s\n Transport: %s \n Internal: %s\n Expected number of validation-errors to be the same.",
                    name, transportValidationErrors, internalValidationErrors),
                getNumberOfTransportValidationErrors(result), getNumberOfInternalValidationErrors(internalValidationResponse));

            assertEquals(String.format("File: %s, Internal validation, expected ValidationStatus.INVALID",
                name), ValidationStatus.INVALID, internalValidationResponse.getStatus());

            assertTrue(String.format("File: %s, Schematronvalidation, expected errors > 0",
                name),
                SVRLHelper.getAllFailedAssertions(result).size() > 0);
        } else {
            assertEquals(String.format("File: %s, Internal validation, expected ValidationStatus.VALID \n Validation-errors: %s",
                name, internalValidationErrors), ValidationStatus.VALID, internalValidationResponse.getStatus());

            assertEquals(String.format("File: %s, Schematronvalidation, expected 0 errors \n Validation-errors: %s",
                name, transportValidationErrors), 0, SVRLHelper.getAllFailedAssertions(result).size());
        }
    }

    private static String getXmlFromIntyg(RegisterCertificateType intyg) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter xml = new StringWriter();

        marshaller.marshal(wrapJaxb(intyg), xml);

        return xml.toString();
    }

    private static JAXBElement<?> wrapJaxb(RegisterCertificateType ws) {
        return new JAXBElement<>(
            new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3", "RegisterCertificate"),
            RegisterCertificateType.class, ws);
    }

    private static String getTransportValidationErrorString(SchematronOutputType result) {
        return SVRLHelper.getAllFailedAssertions(result).stream()
            .map(e -> String.format("Test: %s, Text: %s", e.getTest(), e.getText()))
            .collect(Collectors.joining(";"));
    }

    private static String getInternalValidationErrorString(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().stream()
            .map(ValidationMessage::getMessage)
            .collect(Collectors.joining(", "));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        // Rules R33-35 is validated differently between schematron and internal due to frontend limitations.
        Long numberOfspecialErrors = internalValidationResponse.getValidationErrors().stream()
            .filter(e -> e.getMessage().contains("ts-bas.validation.syn.r3")).count();
        if (numberOfspecialErrors > 1) {
            return internalValidationResponse.getValidationErrors().size() - (numberOfspecialErrors.intValue() - 1);
        }
        return internalValidationResponse.getValidationErrors().size() - numberOfspecialErrors.intValue();
    }

    private static int getNumberOfTransportValidationErrors(SchematronOutputType result) {
        return SVRLHelper.getAllFailedAssertions(result).size();
    }
}
