/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.model.converter;

import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;
import se.inera.intyg.common.tstrk1009.v1.utils.Scenario;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioFinder;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;

@RunWith(Parameterized.class)
public class RoundTripTest {

    private Scenario scenario;

    @SuppressWarnings("unused") // It is used to name the test
    private String name;

    public RoundTripTest(String name, Scenario scenario) {
        this.scenario = scenario;
        this.name = name;
    }

    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        return ScenarioFinder.getInternalScenarios("valid-max*").stream()
            .map(u -> new Object[]{u.getName(), u})
            .collect(Collectors.toList());
    }

    /**
     * Test that no information is lost when mapping json -> xml -> json.
     * This represents the case where the certificate is originally from Webcert and is read from Intygstjansten.
     */
    @Test
    public void testRoundTripInternalFirst() throws Exception {
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        RegisterCertificateType transport = InternalToTransport.convert(scenario.asInternalModel());

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter expected = new StringWriter();
        StringWriter actual = new StringWriter();
        marshaller.marshal(wrapJaxb(scenario.asTransportModel()), expected);
        marshaller.marshal(wrapJaxb(transport), actual);

        Diff diff = DiffBuilder
            .compare(Input.fromString(actual.toString()))
            .withTest(Input.fromString(expected.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .build();

        final List<Difference> differences = Lists.newArrayList(diff.getDifferences()).stream()
            .filter(difference -> !difference.getComparison().getControlDetails().getParentXPath()
                .contains("patient")) //patientdetails should be trimmed
            .collect(Collectors.toList());

        assertTrue(differences.isEmpty());

        JsonNode tree = objectMapper.valueToTree(TransportToInternal.convert(transport.getIntyg()));
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Test that no information is lost when mapping xml -> json -> xml.
     * This represents the case where the certificate is from another medical journaling system and is read from
     * Intygstjansten.
     */
    @Test
    public void testRoundTripTransportFirst() throws Exception {
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        Tstrk1009UtlatandeV1 internal = TransportToInternal.convert(scenario.asTransportModel().getIntyg());

        JsonNode tree = objectMapper.valueToTree(internal);
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter expected = new StringWriter();
        StringWriter actual = new StringWriter();
        marshaller.marshal(wrapJaxb(scenario.asTransportModel()), expected);
        marshaller.marshal(wrapJaxb(InternalToTransport.convert(internal)), actual);

        Diff diff = DiffBuilder
            .compare(Input.fromString(actual.toString()))
            .withTest(Input.fromString(expected.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .build();

        final List<Difference> differences = Lists.newArrayList(diff.getDifferences()).stream()
            .filter(difference -> !difference.getComparison().getControlDetails().getParentXPath()
                .contains("patient")) //patientdetails should be trimmed
            .collect(Collectors.toList());

        assertTrue(differences.toString(), differences.isEmpty());
    }

    private JAXBElement<?> wrapJaxb(RegisterCertificateType ws) {
        return new JAXBElement<>(
            new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3", "RegisterCertificate"),
            RegisterCertificateType.class, ws);
    }
}
