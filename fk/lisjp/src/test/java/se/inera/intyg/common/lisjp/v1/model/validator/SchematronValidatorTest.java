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
package se.inera.intyg.common.lisjp.v1.model.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.commons.debug.GlobalDebug;
import java.net.URL;
import org.junit.Test;
import se.inera.intyg.common.lisjp.v1.rest.LisjpModuleApiV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator(LisjpModuleApiV1.SCHEMATRON_FILE);

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    @Test
    public void brokenXmlFailsTest() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/lisjp_broken.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertFalse(response.getValidationErrors().isEmpty());
    }

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/lisjp.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().isEmpty());
    }

    @Test
    public void invalidAntalDiagnoser() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/diagnosMaxTreDiagnoser.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertFalse(response.getValidationErrors().isEmpty());
    }

    @Test
    // Since change request ID06 (INTYG-2286), Delfråga 39.2 is no longer in use.
    public void delfraga392IsNoLongerValid() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/prognosMedDelfraga39-2.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertFalse(response.getValidationErrors().isEmpty());
    }

    @Test
    public void invalidHeltNedsattOchOmArbetstidsforlaggning() throws Exception {
        String inputXml = Resources
            .toString(getResource("v1/transport/failingSjukskrivningHeltNedsattOchOmArbetstidsforlaggning.xml"),
                Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(String.format("Expected 1 error but was %s",
            response.getValidationErrors().size()), response.getValidationErrors().size() == 1);
    }

    @Test
    public void validFleraSjukskrivningarOchOmArbetstidsforlaggning() throws Exception {
        String inputXml = Resources
            .toString(getResource("v1/transport/fleraSjukskrivningOchOmArbetstidsforlaggning.xml"),
                Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(String.format("Expected 0 error but was %s",
            response.getValidationErrors().size()), response.getValidationErrors().size() == 0);
    }


    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
