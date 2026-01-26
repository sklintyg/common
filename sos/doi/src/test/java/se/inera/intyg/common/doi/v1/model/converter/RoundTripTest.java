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
package se.inera.intyg.common.doi.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.doi.v1.utils.Scenario;
import se.inera.intyg.common.doi.v1.utils.ScenarioFinder;
import se.inera.intyg.common.doi.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BefattningService.class})
class RoundTripTest {

    @BeforeAll
    static void initUtils() {
        final var mapper = mock(UnitMapperUtil.class);

        when(mapper.getMappedUnit(any(), any(), any(), any(), any()))
            .thenAnswer(inv -> new MappedUnit(
                inv.getArgument(0, String.class),
                inv.getArgument(1, String.class),
                inv.getArgument(2, String.class),
                inv.getArgument(3, String.class)
            ));

        new InternalConverterUtil(mapper).initialize();
        new TransportConverterUtil(mapper).initialize();
    }

    static Stream<Arguments> scenarioProvider() throws ScenarioNotFoundException {
        return Stream.concat(
            Stream.concat(
                ScenarioFinder.getInternalScenarios("pass-*").stream(),
                ScenarioFinder.getInternalScenarios("fail-*").stream()
            ),
            ScenarioFinder.getInternalScenarios("validation-*").stream()
        ).map(scenario -> Arguments.of(scenario.getName(), scenario));
    }

    /**
     * Test that no information is lost when mapping json -> xml -> json.
     * This represents the case where the certificate is originally from Webcert and is read from Intygstjansten.
     */
    @ParameterizedTest(name = "{index}: Scenario: {0}")
    @MethodSource("scenarioProvider")
    void testRoundTripInternalFirst(String name, Scenario scenario) throws Exception {
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        RegisterCertificateType transport = InternalToTransport.convert(scenario.asInternalModel());

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter expected = new StringWriter();
        StringWriter actual = new StringWriter();
        marshaller.marshal(wrapJaxb(scenario.asTransportModel()), expected);
        marshaller.marshal(wrapJaxb(transport), actual);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expected.toString()))
            .withTest(Input.fromString(actual.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAttributes("id")))
            .build();
        assertFalse(diff.hasDifferences(), name + " " + diff.toString());

        JsonNode tree = objectMapper.valueToTree(TransportToInternal.convert(transport.getIntyg()));

        DoiUtlatandeV1 expectedInternal = objectMapper.readValue(
            getClass().getResourceAsStream("/v1/internal/scenarios/roundtripjson/" + name + ".json"),
            DoiUtlatandeV1.class);
        JsonNode expectedTree = objectMapper.valueToTree(expectedInternal);
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    /**
     * Test that no information is lost when mapping xml -> json -> xml.
     * This represents the case where the certificate is from another medical journaling system and is read from
     * Intygstjansten.
     */
    @ParameterizedTest(name = "{index}: Scenario: {0}")
    @MethodSource("scenarioProvider")
    void testRoundTripTransportFirst(String name, Scenario scenario) throws Exception {
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        DoiUtlatandeV1 internal = TransportToInternal.convert(scenario.asTransportModel().getIntyg());

        JsonNode tree = objectMapper.valueToTree(internal);

        DoiUtlatandeV1 expectedInternal = objectMapper.readValue(
            getClass().getResourceAsStream("/v1/internal/scenarios/roundtripjson/" + name + ".json"),
            DoiUtlatandeV1.class);
        JsonNode expectedTree = objectMapper.valueToTree(expectedInternal);
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter expected = new StringWriter();
        StringWriter actual = new StringWriter();
        marshaller.marshal(wrapJaxb(scenario.asTransportModel()), expected);
        marshaller.marshal(wrapJaxb(InternalToTransport.convert(internal)), actual);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expected.toString()))
            .withTest(Input.fromString(actual.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAttributes("id")))
            .build();
        assertFalse(diff.hasDifferences(), name + " " + diff.toString());
    }

    private JAXBElement<?> wrapJaxb(RegisterCertificateType ws) {
        return new JAXBElement<>(
            new QName("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3", "RegisterCertificate"),
            RegisterCertificateType.class, ws);
    }
}