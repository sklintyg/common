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
package se.inera.intyg.common.tstrk1009.v1.model.validator;


import com.google.common.base.Charsets;
import com.helger.base.debug.GlobalDebug;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
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
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.api.BeforeEach;
import java.util.stream.Stream;

public class InternalValidatorResultMatchesSchematronValidatorTest {

  private String name;





  static {
    // avoid com.helger debug log
    GlobalDebug.setDebugModeDirect(false);
  }

  private static InternalValidatorInstance internalValidator;


  @BeforeEach
  public void setup() {
    internalValidator = new InternalValidatorInstance();
  }

  /**
   * Process test data and supply it to the test. The format for the test data needs to be: {name to
   * display for current test, the scenario to test, expected outcome of the test}.
   *
   * @return Collection<Object [ ]>
   */
    static Stream<Arguments> data() throws ScenarioNotFoundException {

    List<Arguments> retList = new ArrayList<>();
    // Failing tests
    //        retList.addAll(ScenarioFinder.getInternalScenarios("fail-*").stream()
    //                .map(u -> Arguments.of( u.getName(), u, true ))
    //                .collect(Collectors.toList()));
    // Passing tests
    retList.addAll(
        ScenarioFinder.getInternalScenarios("valid-max*").stream()
            .map(u -> Arguments.of(u.getName(), u, false))
            .collect(Collectors.toList()));
    return retList.stream();
  }

  @ParameterizedTest(name = "{index}: Scenario: {0}")
  @MethodSource("data")
  public void testScenarios(String name, Scenario scenario, boolean shouldFail) throws Exception {
    this.name = name;
    doInternalAndSchematronValidation(scenario, shouldFail);
  }

  /**
   * Perform internal and schematron validation on the supplied Scenario.
   *
   * @param fail Whether the test should expect validation errors or not.
   */
  private void doInternalAndSchematronValidation(Scenario scenario, boolean fail) throws Exception {
    Tstrk1009UtlatandeV1 utlatandeFromJson = scenario.asInternalModel();

    ValidateDraftResponse internalValidationResponse =
        internalValidator.validate(utlatandeFromJson);

    final List<ValidationMessage> filteredValidations =
        internalValidationResponse.getValidationErrors().stream()
            .filter(error -> !error.getCategory().equals("patient"))
            .collect(Collectors.toList());

    internalValidationResponse =
        new ValidateDraftResponse(
            filteredValidations.isEmpty() ? ValidationStatus.VALID : ValidationStatus.INVALID,
            filteredValidations);

    RegisterCertificateType intyg = scenario.asTransportModel();
    String convertedXML = getXmlFromIntyg(intyg);

    RegisterCertificateValidator validator =
        new RegisterCertificateValidator(Tstrk1009EntryPoint.SCHEMATRON_FILE);
    SchematronOutputType result =
        validator.validateSchematron(
            new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));

    String internalValidationErrors = getInternalValidationErrorString(internalValidationResponse);

    String transportValidationErrors = getTransportValidationErrorString(result);

    doAssertions(
        fail,
        internalValidationResponse,
        result,
        internalValidationErrors,
        transportValidationErrors);
  }

  private void doAssertions(
      boolean fail,
      ValidateDraftResponse internalValidationResponse,
      SchematronOutputType result,
      String internalValidationErrors,
      String transportValidationErrors) {
    if (fail) {
      assertEquals(
          getNumberOfTransportValidationErrors(result),
          getNumberOfInternalValidationErrors(internalValidationResponse),
          String.format(
              "Scenario: %s\n Transport: %s \n Internal: %s\n Expected number of validation-errors to be the same.",
              name, transportValidationErrors, internalValidationErrors));

      assertEquals(
          ValidationStatus.INVALID,
          internalValidationResponse.getStatus(),
          String.format("File: %s, Internal validation, expected ValidationStatus.INVALID", name));

      assertTrue(
          SVRLHelper.getAllFailedAssertions(result).size() > 0,
          String.format("File: %s, Schematronvalidation, expected errors > 0", name));
    } else {
      assertEquals(
          ValidationStatus.VALID,
          internalValidationResponse.getStatus(),
          String.format(
              "File: %s, Internal validation, expected ValidationStatus.VALID \n Validation-errors: %s",
              name, internalValidationErrors));

      assertEquals(
          0,
          SVRLHelper.getAllFailedAssertions(result).size(),
          String.format(
              "File: %s, Schematronvalidation, expected 0 errors \n Validation-errors: %s",
              name, transportValidationErrors));
    }
  }

  private static String getXmlFromIntyg(RegisterCertificateType intyg) throws Exception {
    JAXBContext jaxbContext =
        JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
    Marshaller marshaller = jaxbContext.createMarshaller();
    StringWriter xml = new StringWriter();

    marshaller.marshal(wrapJaxb(intyg), xml);

    return xml.toString();
  }

  private static JAXBElement<?> wrapJaxb(RegisterCertificateType ws) {
    return new JAXBElement<>(
        new QName(
            "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3",
            "RegisterCertificate"),
        RegisterCertificateType.class,
        ws);
  }

  private static String getTransportValidationErrorString(SchematronOutputType result) {
    return SVRLHelper.getAllFailedAssertions(result).stream()
        .map(e -> String.format("Test: %s, Text: %s", e.getTest(), e.getText()))
        .collect(Collectors.joining(";"));
  }

  private static String getInternalValidationErrorString(
      ValidateDraftResponse internalValidationResponse) {
    return internalValidationResponse.getValidationErrors().stream()
        .map(ValidationMessage::getMessage)
        .collect(Collectors.joining(", "));
  }

  private static int getNumberOfInternalValidationErrors(
      ValidateDraftResponse internalValidationResponse) {
    // Rules R33-35 is validated differently between schematron and internal due to frontend
    // limitations.
    Long numberOfspecialErrors =
        internalValidationResponse.getValidationErrors().stream()
            .filter(e -> e.getMessage().contains("ts-bas.validation.syn.r3"))
            .count();
    if (numberOfspecialErrors > 1) {
      return internalValidationResponse.getValidationErrors().size()
          - (numberOfspecialErrors.intValue() - 1);
    }
    return internalValidationResponse.getValidationErrors().size()
        - numberOfspecialErrors.intValue();
  }

  private static int getNumberOfTransportValidationErrors(SchematronOutputType result) {
    return SVRLHelper.getAllFailedAssertions(result).size();
  }
}
