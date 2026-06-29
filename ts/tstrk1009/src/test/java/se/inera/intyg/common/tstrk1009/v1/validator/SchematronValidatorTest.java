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
package se.inera.intyg.common.tstrk1009.v1.validator;

import static com.google.common.io.Resources.getResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;
import se.inera.intyg.common.tstrk1009.support.Tstrk1009EntryPoint;

class SchematronValidatorTest {

  private static final RegisterCertificateValidator VALIDATOR =
      new RegisterCertificateValidator(Tstrk1009EntryPoint.SCHEMATRON_FILE);

  @Test
  void validMinimalXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v1/scenarios/transport/valid-min.xml"), StandardCharsets.UTF_8);
    doPassingTest(inputXml);
  }

  @Test
  void validMaximalXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v1/scenarios/transport/valid-max.xml"), StandardCharsets.UTF_8);
    doPassingTest(inputXml);
  }

  @Test
  void validKanInteTaStallningXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v1/scenarios/transport/valid-kan-inte-ta-stallning.xml"),
            StandardCharsets.UTF_8);
    doPassingTest(inputXml);
  }

  // R8 and R10
  @Test
  void invalidKanInteTaStallningXmlFailsTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v1/scenarios/transport/invalid-kan-inte-ta-stallning.xml"),
            StandardCharsets.UTF_8);
    doFailingTest(inputXml, 2);
  }

  private void doPassingTest(String inputXml) throws ModuleException {
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertTrue(
        response.getValidationErrors().isEmpty(),
        response.getValidationErrors().stream().collect(Collectors.joining(", ")));
  }

  private void doFailingTest(String inputXml, int expectedNumFails) throws ModuleException {
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertEquals(
        expectedNumFails,
        response.getValidationErrors().size(),
        response.getValidationErrors().stream().collect(Collectors.joining(", ")));
  }
}
