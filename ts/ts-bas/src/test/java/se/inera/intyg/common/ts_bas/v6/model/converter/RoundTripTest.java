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
package se.inera.intyg.common.ts_bas.v6.model.converter;

import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;
import se.inera.intyg.common.ts_bas.v6.utils.Scenario;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioFinder;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioNotFoundException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

@RunWith(Parameterized.class)
@ContextConfiguration(classes = {BefattningService.class})
public class RoundTripTest {

    private Scenario scenario;

    @SuppressWarnings("unused") // It is actually used to name the test
    private String name;

    public RoundTripTest(String name, Scenario scenario) throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
        this.scenario = scenario;
        this.name = name;
    }

    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        return ScenarioFinder.getInternalScenarios("valid-*").stream()
                .map(u -> new Object[] { u.getName(), u })
                .collect(Collectors.toList());
    }

    /**
     * Test that no information is lost when mapping json -> xml -> json.
     * This represents the case where the certificate is originally from Webcert and is read from Intygstjansten.
     */
    @Test
    public void testRoundTripInternalFirst() throws Exception {
        CustomObjectMapper objectMapper = new CustomObjectMapper();
        ObjectFactory objectFactory = new ObjectFactory();

        RegisterCertificateType transport = InternalToTransport.convert(scenario.asInternalModel());

        String expected = XmlMarshallerHelper.marshal(objectFactory.createRegisterCertificate(scenario.asRivtaV3TransportModel()));
        String actual = XmlMarshallerHelper.marshal(objectFactory.createRegisterCertificate(transport));

        Diff diff = DiffBuilder
                .compare(Input.fromString(actual))
                .withTest(Input.fromString(expected))
                .ignoreComments()
                .ignoreWhitespace()
                .checkForSimilar()
                .build();
        assertFalse(diff.toString(), diff.hasDifferences());

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
        ObjectFactory objectFactory = new ObjectFactory();

        TsBasUtlatandeV6 internal = TransportToInternal.convert(scenario.asRivtaV3TransportModel().getIntyg());

        JsonNode tree = objectMapper.valueToTree(internal);
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);


        String expected = XmlMarshallerHelper.marshal(objectFactory.createRegisterCertificate(scenario.asRivtaV3TransportModel()));
        String actual = XmlMarshallerHelper.marshal(objectFactory.createRegisterCertificate(InternalToTransport.convert(internal)));

        Diff diff = DiffBuilder
                .compare(Input.fromString(actual))
                .withTest(Input.fromString(expected))
                .ignoreComments()
                .ignoreWhitespace()
                .checkForSimilar()
                .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }

}
