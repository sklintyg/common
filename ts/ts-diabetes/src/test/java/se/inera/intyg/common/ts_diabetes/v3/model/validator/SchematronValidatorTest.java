/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v3.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE10_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE12_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE13_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE14_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE15_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE16_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE17_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE18_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE1_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE2_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE3_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE4_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE5_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE6_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE7_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE8_MESSAGE;
import static se.inera.intyg.common.ts_diabetes.v3.model.validator.ValidationMessages.RULE9_MESSAGE;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.commons.debug.GlobalDebug;
import org.junit.Test;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;
import se.inera.intyg.common.ts_diabetes.v3.rest.TsDiabetesModuleApiV3;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator(TsDiabetesModuleApiV3.SCHEMATRON_FILE);

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    @Test
    public void brokenXmlFailsTest() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/ts-diabetes-v3_broken.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertFalse(response.getValidationErrors().isEmpty());
    }

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/ts-diabetes-v3-pass-complete.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().stream().collect(Collectors.joining("\n")), response.getValidationErrors().isEmpty());
    }

    @Test
    public void failOnRule1() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R1.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE1_MESSAGE));
    }

    @Test
    public void failOnRule2CheckLegitDate() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R2.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE2_MESSAGE));
    }

    @Test
    public void failOnRule3() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R3.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE3_MESSAGE));
    }

    @Test
    public void failOnRule4() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R4.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE4_MESSAGE));
    }

    @Test
    public void failOnRule5() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R5.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE5_MESSAGE));
    }

    @Test
    public void failOnRule6() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R6.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE6_MESSAGE));
    }

    @Test
    public void failOnRule7() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R7.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE7_MESSAGE));
    }

    @Test
    public void failOnRule8() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R8.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE8_MESSAGE));
    }

    @Test
    public void failOnRule9() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R9.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE9_MESSAGE));
    }

    @Test
    public void failOnRule10() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R10.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE10_MESSAGE));
    }

    //DISCLAIMER: Går ej att testa Regel 11 i backend!

    @Test
    public void failOnRule12() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R12.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE12_MESSAGE));
    }

    @Test
    public void failOnRule13() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R13.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE13_MESSAGE));
    }

    @Test
    public void failOnRule14() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R14.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE14_MESSAGE));
    }

    @Test
    public void failOnRule15() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R15.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE15_MESSAGE));
    }

    @Test
    public void failOnRule16() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R16.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE16_MESSAGE));
    }

    @Test
    public void failOnRule17() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R17.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE17_MESSAGE));
    }

    @Test
    public void failOnRule18() throws Exception {
        String inputXml = Resources.toString(getResource("v3/transport/scenarios/fail/fail-R18.xml"), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        final List<String> validationErrors = response.getValidationErrors();

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains(RULE18_MESSAGE));
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
