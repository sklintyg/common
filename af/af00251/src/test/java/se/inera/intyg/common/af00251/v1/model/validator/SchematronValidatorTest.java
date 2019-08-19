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
package se.inera.intyg.common.af00251.v1.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.commons.debug.GlobalDebug;
import java.net.URL;
import java.util.stream.Collectors;
import org.junit.Test;
import se.inera.intyg.common.af00251.v1.rest.AF00251ModuleApiV1;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator(AF00251ModuleApiV1.SCHEMATRON_FILE);

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    @Test
    public void brokenXmlFailsTest() throws Exception {
        String inputXml = Resources.toString(getResource("transport/af00251_broken.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);

        System.out.println("***************************");
        System.out.println(response.getValidationErrors()
            .stream()
            .collect(Collectors.joining("\n")));
        System.out.println("***************************");

        assertFalse(response.getValidationErrors().stream().collect(Collectors.joining("\n")),
            response.getValidationErrors().isEmpty());
    }

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("transport/af00251.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().stream().collect(Collectors.joining("\n")), response.getValidationErrors().isEmpty());
    }

    @Test
    public void failsOnOmfattningDeltidSaknas() throws Exception {
        String inputXml = Resources
            .toString(getResource("transport/scenarios/fail-omfattningDeltidSaknas.xml"), org.apache.commons.io.Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0)
            .contains("Om \"Programmets omfattning\" har besvarats med \"Deltid\" måste \"Omfatting deltid\" fyllas i."));
    }

    @Test
    public void failsOnOmfattningDeltidFelEnhet() throws Exception {
        String inputXml = Resources
            .toString(getResource("transport/scenarios/fail-omfattningDeltidFelEnhet.xml"), org.apache.commons.io.Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("\"Omfatting deltid\" måste anges i enhet \"h\"."));
    }

    @Test
    public void failsOnInvalidOmfattningDeltid0() throws Exception {
        String inputXml = Resources
            .toString(getResource("transport/scenarios/fail-omfattningDeltid0.xml"), org.apache.commons.io.Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("\"Omfatting deltid\" måste anges i timmar mellan 1 och 39."));
    }

    @Test
    public void failsOnInvalidOmfattningDeltid40() throws Exception {
        String inputXml = Resources
            .toString(getResource("transport/scenarios/fail-omfattningDeltid40.xml"), org.apache.commons.io.Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("\"Omfatting deltid\" måste anges i timmar mellan 1 och 39."));
    }

    @Test
    public void failsOnSjukfranvaronivaFelEnhet() throws Exception {
        String inputXml = Resources
            .toString(getResource("transport/scenarios/fail-sjukfranvaronivaFelEnhet.xml"), org.apache.commons.io.Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("\"Sjukfrånvaronivå\" måste anges i enhet \"%\"."));
    }

    @Test
    public void failsOnSjukfranvaroniva0() throws Exception {
        String inputXml = Resources
            .toString(getResource("transport/scenarios/fail-sjukfranvaroniva0.xml"), org.apache.commons.io.Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("\"Sjukfrånvaronivå\" måste anges i % mellan 1 och 100."));
    }

    @Test
    public void failsOnSjukfranvaroniva101() throws Exception {
        String inputXml = Resources
            .toString(getResource("transport/scenarios/fail-sjukfranvaroniva101.xml"), org.apache.commons.io.Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("\"Sjukfrånvaronivå\" måste anges i % mellan 1 och 100."));
    }

}
