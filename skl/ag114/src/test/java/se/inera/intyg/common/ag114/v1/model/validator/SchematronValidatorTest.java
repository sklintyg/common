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
package se.inera.intyg.common.ag114.v1.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.io.Resources;
import com.helger.commons.debug.GlobalDebug;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.support.validate.XmlValidator;

public class SchematronValidatorTest {

    private static final RegisterCertificateValidator VALIDATOR = new RegisterCertificateValidator("ag114.v1.sch");

    static {
        // avoid com.helger debug log
        GlobalDebug.setDebugModeDirect(false);
    }

    private URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    @Test
    public void validXmlPassesTest() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/pass-minimal.xml"), StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertTrue(response.getValidationErrors().isEmpty());
    }

    @Test
    public void failsOnInvalidSjukskrivningsgrad() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-invalid-sjukskrivningsgrad.xml"),
            StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0)
            .contains("'Nedsättningsgrad arbetsförmåga' måste besvaras med ett värde mellan 0 och 100%"));
    }

    @Test
    public void failsOnInvalidSjukskrivningsPeriod() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-invalid-sjukskrivningsperiod.xml"),
            StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("'from' måste vara mindre än eller lika med 'to'"));
    }

    @Test
    public void failsOnMissingArbetsformaga() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-arbetsformaga.xml"), StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains(
            "Om 'Finns arbetsförmåga trots sjukdom' besvarats med 'Ja' måste 'Beskriv arbetsförmåga trots sjukdom' besvaras."));
    }

    @Test
    public void failsOnMissingDiagnoses() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-diagnoser.xml"), StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains(
            "Om 'Önskar förmedla diagnos (FRG 3.1)' besvaras med 'Ja' är frågan 'Typ av diagnos (FRG 4)' obligatorisk att besvara."));
    }

    @Test
    public void failsOnMissingDiagnosBeskrivning() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-diagnos1.xml"), StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("Sträng kan inte vara tom."));
    }

    @Test
    public void failsOnMissingOnskarFormedlaDiagnos() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-diagnosformedling.xml"),
            StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("Ett 'AG1-14' måste innehålla 1 'Önskar förmedla diagnoser'."));
    }

    @Test
    public void failsOnMissingOnskarFormedlaDiagnosDelsvar() throws Exception {
        String inputXml = Resources
            .toString(getResource("v1/transport/scenarios/fail-missing-diagnosformedlingdelsvar.xml"), StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0).contains("'Önskar förmedla diagnoser' måste ha ett 'Önskar förmedla diagnoser'."));
    }

    @Test
    public void failsOnMissingKontaktanledning() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-kontaktanledning.xml"),
            StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0)
            .contains("Om 'Kontakt med arbetsgivaren önskas' besvarats med 'Ja' måste 'Kontakt vem och varför' besvaras."));
    }

    @Test
    public void failsOnMissingNuvarandeArbete() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-nuvarandearbete.xml"),
            StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0)
            .contains("Sträng kan inte vara tom."));
    }

    @Test
    public void failsOnMissingBedomningAvNedsattningAvArbetsformaga() throws Exception {
        String inputXml = Resources
            .toString(getResource("v1/transport/scenarios/fail-missing-sjukskrivningbedomningavnedsattningavarbetsformaga.xml"),
                StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0)
            .contains("Ett 'AG1-14' måste innehålla 1 'Bedömning av nedsättning av arbetsförmåga'."));
    }

    @Test
    public void failsOnMissingSjukskrivningsgrad() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-sjukskrivningsgrad.xml"),
            StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0)
            .contains("'Bedömning av nedsättning av arbetsförmåga' måste ha ett 'Nedsättningsgrad arbetsförmåga'."));
    }

    @Test
    public void failsOnMissingSjukskrivningsperiod() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-sjukskrivningsperiod.xml"),
            StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0)
            .contains("'Bedömning av nedsättning av arbetsförmåga' måste ha ett 'Period för nedsatt arbetsförmåga'."));
    }

    @Test
    public void failsOnMissingSysselsattning() throws Exception {
        String inputXml = Resources.toString(getResource("v1/transport/scenarios/fail-missing-sysselsattning.xml"), StandardCharsets.UTF_8);
        ValidateXmlResponse response = XmlValidator.validate(VALIDATOR, inputXml);
        assertEquals(1, response.getValidationErrors().size());
        assertTrue(response.getValidationErrors().get(0)
            .contains("'Typ av sysselsättning' måste ha värdet NUVARANDE_ARBETE."));
    }

}
