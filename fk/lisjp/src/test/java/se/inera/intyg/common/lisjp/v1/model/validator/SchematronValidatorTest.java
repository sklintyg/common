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
package se.inera.intyg.common.lisjp.v1.model.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.io.Resources;
import com.helger.base.debug.GlobalDebug;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.lisjp.v1.rest.LisjpModuleApiV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;

class SchematronValidatorTest {

  private static final RegisterCertificateValidator VALIDATOR =
      new RegisterCertificateValidator(LisjpModuleApiV1.SCHEMATRON_FILE);

  static {
    // avoid com.helger debug log
    GlobalDebug.setDebugModeDirect(false);
  }

  @Test
  void brokenXmlFailsTest() throws Exception {
    String inputXml =
        Resources.toString(getResource("v1/transport/lisjp_broken.xml"), StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertFalse(response.getValidationErrors().isEmpty());
  }

  @Test
  void validXmlPassesTest() throws Exception {
    String inputXml =
        Resources.toString(getResource("v1/transport/lisjp.xml"), StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertTrue(response.getValidationErrors().isEmpty());
  }

  @Test
  void invalidAntalDiagnoser() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v1/transport/diagnosMaxTreDiagnoser.xml"), StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertFalse(response.getValidationErrors().isEmpty());
  }

  @Test
  // Since change request ID06 (INTYG-2286), Delfråga 39.2 is no longer in use.
  public void delfraga392IsNoLongerValid() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v1/transport/prognosMedDelfraga39-2.xml"), StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertFalse(response.getValidationErrors().isEmpty());
  }

  @Test
  void invalidHeltNedsattOchOmArbetstidsforlaggning() throws Exception {
    String inputXml =
        Resources.toString(
            getResource(
                "v1/transport/failingSjukskrivningHeltNedsattOchOmArbetstidsforlaggning.xml"),
            StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertTrue(
        response.getValidationErrors().size() == 1,
        String.format("Expected 1 error but was %s", response.getValidationErrors().size()));
  }

  @Test
  void validFleraSjukskrivningarOchOmArbetstidsforlaggning() throws Exception {
    String inputXml =
        Resources.toString(
            getResource("v1/transport/fleraSjukskrivningOchOmArbetstidsforlaggning.xml"),
            StandardCharsets.UTF_8);
    ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
    assertTrue(
        response.getValidationErrors().size() == 0,
        String.format("Expected 0 error but was %s", response.getValidationErrors().size()));
  }

  private static URL getResource(String href) {
    return Thread.currentThread().getContextClassLoader().getResource(href);
  }
}
