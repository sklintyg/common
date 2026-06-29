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
package se.inera.intyg.common.ts_bas.v6.validator;

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
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;

class SchematronValidatorTest {

  private static final RegisterCertificateValidator VALIDATOR =
      new RegisterCertificateValidator(TsBasEntryPoint.SCHEMATRON_FILE_V6);

  @Test
  void validMaximalXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-maximal.xml"), StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validMinimalXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-minimal.xml"), StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validPersontransportXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-persontransport.xml"), StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validSamordningXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-samordning.xml"), StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validDiabetesTyp2XmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-diabetes-typ2-kost.xml"),
            StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validKorrigeradSynskarpaXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-korrigerad-synskarpa.xml"),
            StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validSjukhusvardXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-sjukhusvard.xml"), StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validUtanSynskarpaXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-utan-korrigerad-synskarpa.xml"),
            StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validVersion0608XmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-version0608.xml"), StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void validR34PassesTest() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/valid-rule34.xml"), StandardCharsets.UTF_8);
    doTest(inputXml);
  }

  @Test
  void invalidMinimalTestFails() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/fail-minimal.xml"), StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertEquals(
        1,
        response.getValidationErrors().size(),
        response.getValidationErrors().stream().collect(Collectors.joining(", ")));
  }

  @Test
  void invalidAnnatFelsynskarpaTestFails() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/fail-annat-felsynskarpa.xml"),
            StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertEquals(
        1,
        response.getValidationErrors().size(),
        response.getValidationErrors().stream().collect(Collectors.joining(", ")));
  }

  @Test
  void invalidMinimalR35TestFails() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/fail-minimal-r35.xml"), StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertEquals(
        1,
        response.getValidationErrors().size(),
        response.getValidationErrors().stream().collect(Collectors.joining(", ")));
  }

  @Test
  void invalidR34TestFails() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v6/scenarios/rivtav3/invalid-rule34.xml"), StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertEquals(
        1,
        response.getValidationErrors().size(),
        response.getValidationErrors().stream().collect(Collectors.joining(", ")));
  }

  private void doTest(String inputXml) throws ModuleException {
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertTrue(
        response.getValidationErrors().isEmpty(),
        response.getValidationErrors().stream().collect(Collectors.joining(", ")));
  }
}
