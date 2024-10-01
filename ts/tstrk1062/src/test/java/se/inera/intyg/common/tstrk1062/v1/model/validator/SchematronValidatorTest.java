/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1062.v1.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.common.tstrk1062.v1.rest.TsTrk1062ModuleApiV1.SCHEMATRON_FILE;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.commons.debug.GlobalDebug;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator(SCHEMATRON_FILE);

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    @Test
    public void validXmlDiagnosFritext() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/success/diagnosFritext.xml");

        assertNoError(validationErrors);
    }

    @Test
    public void validXmlDiagnosKodad() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/success/diagnosKodad.xml");

        assertNoError(validationErrors);
    }

    @Test
    public void validXmlDiagnosKodadWithoutDescription() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/success/diagnosKodadUtanBeskrivning.xml");

        assertNoError(validationErrors);
    }

    @Test
    public void validXmlDiagnosKodadBehandlingAvslutad() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/success/diagnosKodadBehandlingAvslutad.xml");

        assertNoError(validationErrors);
    }

    @Test
    public void validXmlDiagnosKodadBehandlingPagar() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/success/diagnosKodadBehandlingPagar.xml");

        assertNoError(validationErrors);
    }

    @Test
    public void validXmlDiagnosKodadBehandlingSaknas() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/success/diagnosKodadBehandlingSaknas.xml");

        assertNoError(validationErrors);
    }

    @Test
    public void validXmlDiagnosKodadMedOvrigt() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/success/diagnosKodadMedOvrigt.xml");

        assertNoError(validationErrors);
    }

    @Test
    public void validXmlDiagnosKodadUtanOvrigt() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/success/diagnosKodadUtanOvrigt.xml");

        assertNoError(validationErrors);
    }

    @Test
    public void failOnRule1() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule1.xml");

        assertOneError(validationErrors, "'Läkemedelsbehandling för angivna diagnoser pågår' besvaras.");
    }

    @Test
    public void failOnRule2() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule2.xml");

        assertOneError(validationErrors, "'Aktuell läkemedelsbehandling' besvaras.");
    }

    @Test
    public void failOnRule3() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule3.xml");

        assertOneError(validationErrors, "'Läkemedelsbehandling i mer än tre år' besvaras.");
    }

    @Test
    public void failOnRule4() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule4.xml");

        assertOneError(validationErrors, "'Behandlingseffekten har varit god under senaste två åren' besvaras.");
    }

    @Test
    public void failOnRule5() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule5.xml");

        assertOneError(validationErrors, "'Behandlingsföljsamheten är god' besvaras.");
    }

    @Test
    public void failOnRule6() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule6.xml");

        assertOneError(validationErrors, "'Tidpunkt då läkemedelsbehandling avslutades'");
    }

    @Test
    public void failOnRule7() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule7.xml");

        assertOneError(validationErrors, "'Orsak till att läkemedelsbehandling avslutades'");
    }

    @Test
    public void failOnRule6And7() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule6-7.xml");

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains("'Tidpunkt då läkemedelsbehandling avslutades'"));
        assertTrue(validationErrors.get(0).contains("'Orsak till att läkemedelsbehandling avslutades'"));
    }

    @Test
    public void failOnRule8And9() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule8-9.xml");

        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).contains("'Uppfyller krav för behörighet'"));
        assertTrue(validationErrors.get(0).contains("VAR11"));
    }

    @Test
    public void failOnRule10() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule10.xml");

        assertOneError(validationErrors, "'Årtal för diagnos'");
    }

    @Test
    public void failOnRule12And13() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failRule12-13.xml");

        assertOneError(validationErrors, "'Diagnos kodad' eller 'Diagnos fritext'");
    }

    @Test
    public void failOnMissingQ1() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failMissingQ1.xml");

        assertOneError(validationErrors, "'Intyget avser behörighet'");
    }

    @Test
    public void failOnMissingQ2() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failMissingQ2.xml");

        assertOneError(validationErrors, "'Identitet styrkt genom'");
    }

    @Test
    public void failOnMissingQ33() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failMissingQ33.xml");

        assertOneError(validationErrors, "'Uppfyller krav för behörighet'");
    }

    @Test
    public void failOnMissingQ53() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failMissingQ53.xml");

        assertOneError(validationErrors, "'Läkemedelsbehandling för angivna diagnoser har förekommit'");
    }

    @Test
    public void failOnMissingQ60() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failMissingQ60.xml");

        assertOneError(validationErrors, "'Bedömning av symptom, funktionshinder och prognos för fortsatt stabilitet'");
    }

    @Test
    public void failOnMissingQ61() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failMissingQ61.xml");

        assertOneError(validationErrors, "'Prognos för fortsatt stabilt tillstånd är god'");
    }

    @Test
    public void failOnUnexpectedCodeForQ61() throws Exception {
        final List<String> validationErrors = validateXML("v1/transport/scenarios/fail/failUnexpectedCodeForQ61.xml");

        assertOneError(validationErrors, "'code' kan endast vara NI");
    }

    private List<String> validateXML(String href) throws Exception {
        String inputXml = Resources.toString(getResource(href), Charsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        return response.getValidationErrors();
    }

    private void assertOneError(List<String> validationErrors, String containsError) throws Exception {
        assertFalse(String.join("\n", validationErrors), validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0) + "should contain: " + containsError, validationErrors.get(0).contains(containsError));
    }

    private void assertNoError(List<String> validationErrors) throws Exception {
        assertTrue(validationErrors.stream().collect(Collectors.joining("\n")), validationErrors.isEmpty());
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }
}
