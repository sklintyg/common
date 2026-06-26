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
package se.inera.intyg.common.ag114.v1.model.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getInternalValidationErrorString;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getNumberOfInternalValidationErrors;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getNumberOfTransportValidationErrors;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getTransportValidationErrorString;
import static se.inera.intyg.common.agparent.model.validator.InternalToSchematronValidatorTestUtil.getXmlFromModel;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.helger.base.debug.GlobalDebug;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.utils.Scenario;
import se.inera.intyg.common.ag114.v1.utils.ScenarioFinder;
import se.inera.intyg.common.ag114.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ag114.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.ag114.v1.validator.ValidatorUtilSKL;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

/**
 * Data driven test that uses Scenario and ScenarioFinder along with the JUnit Parameterized test
 * runner, uses test data from internal/scenarios and transport/scenarios, so in order to create new
 * tests, just add corresponding json- and XML-files in these directories.
 *
 * @author erik
 */
class InternalValidatorResultMatchesSchematronValidatorTest {

  private String name;

  /*
   * Due to the existence of virtual intyg fields in Webcert, there is a discrepancy between the numbers of errors in
   * the schematron validation vs the Webcert validation. Thus those fields should be ignored for the purposes of
   * comparing the internal (Webcert) validation and the schematron validation of intyg.
   */
  private static final ImmutableList<String> IGNORED_FIELDS =
      ImmutableList.of("motiveringTillInteBaseratPaUndersokning");
  private static InternalDraftValidatorImpl internalValidator;

  static {
    // avoid com.helger debug log
    GlobalDebug.setDebugModeDirect(false);
    internalValidator = new InternalDraftValidatorImpl();
    ReflectionTestUtils.setField(internalValidator, "validatorUtilSKL", new ValidatorUtilSKL());
  }

  /**
   * Process test data and supply it to the test. The format for the test data needs to be: {name to
   * display for current test, the scenario to test, expected outcome of the test}.
   *
   * @return Collection<Object [ ]>
   */
  static Stream<Arguments> data() throws ScenarioNotFoundException {
    List<Arguments> retList =
        ScenarioFinder.getInternalScenarios("fail-*").stream()
            .map(u -> Arguments.of(u.getName(), u, true))
            .collect(Collectors.toList());

    retList.addAll(
        ScenarioFinder.getInternalScenarios("pass-*").stream()
            .map(u -> Arguments.of(u.getName(), u, false))
            .collect(Collectors.toList()));
    return retList.stream();
  }

  /**
   * Perform internal and schematron validation on the supplied Scenario.
   *
   * @param fail Whether the test should expect validation errors or not.
   */
  private void doInternalAndSchematronValidation(Scenario scenario, boolean fail) throws Exception {
    Ag114UtlatandeV1 utlatandeFromJson = scenario.asInternalModel();

    ValidateDraftResponse internalValidationResponse =
        internalValidator.validateDraft(utlatandeFromJson);

    RegisterCertificateType intyg = scenario.asTransportModel();
    String convertedXML = getXmlFromModel(intyg);

    RegisterCertificateValidator validator = new RegisterCertificateValidator("ag114.v1.sch");
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
          getNumberOfInternalValidationErrors(internalValidationResponse, IGNORED_FIELDS),
          String.format(
              "Scenario: %s\n Transport: %s \n Internal: %s\n Expected number of validation-errors to be the same.",
              name, transportValidationErrors, internalValidationErrors));
      assertTrue(
          internalValidationResponse.getStatus().equals(ValidationStatus.INVALID),
          String.format("File: %s, Internal validation, expected ValidationStatus.INVALID", name));

      assertTrue(
          SVRLHelper.getAllFailedAssertions(result).size() > 0,
          String.format("File: %s, Schematronvalidation, expected errors > 0", name));
    } else {
      assertTrue(
          internalValidationResponse.getStatus().equals(ValidationStatus.VALID),
          String.format(
              "File: %s, Internal validation, expected ValidationStatus.VALID \n Validation-errors: %s",
              name, internalValidationErrors));
      assertTrue(
          SVRLHelper.getAllFailedAssertions(result).size() == 0,
          String.format(
              "File: %s, Schematronvalidation, expected 0 errors \n Validation-errors: %s",
              name, transportValidationErrors));
    }
  }

  @BeforeEach
  void setUp() throws Exception {}

  @ParameterizedTest(name = "{index}: Scenario: {0}")
  @MethodSource("data")
  public void testScenarios(String name, Scenario scenario, boolean shouldFail) throws Exception {
    this.name = name;
    doInternalAndSchematronValidation(scenario, shouldFail);
  }
}
