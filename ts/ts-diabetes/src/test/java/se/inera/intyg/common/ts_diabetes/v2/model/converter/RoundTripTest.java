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
package se.inera.intyg.common.ts_diabetes.v2.model.converter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.xml.bind.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_diabetes.v2.utils.Scenario;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioFinder;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioNotFoundException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.ObjectFactory;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateType;

@RunWith(Parameterized.class)
@ContextConfiguration(classes = {BefattningService.class})
public class RoundTripTest {

    private Scenario scenario;

    private CustomObjectMapper objectMapper = new CustomObjectMapper();
    private ObjectFactory objectFactory = new ObjectFactory();
    private se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory rivtav3ObjectFactory = new se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory();
    private se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.ObjectFactory transformedObjectFactory = new se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.ObjectFactory();
    private static Marshaller marshaller;
    private static XslTransformer transformer;

    static {
        try {
            marshaller = JAXBContext.newInstance(RegisterTSDiabetesType.class, RegisterCertificateType.class, DatePeriodType.class,
                PartialDateType.class, se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType.class)
                .createMarshaller();
            transformer = new XslTransformer("xsl/transform-ts-diabetes.xsl");
        } catch (JAXBException e) {
        }
    }

    private String name;

    public RoundTripTest(String name, Scenario scenario) throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
        this.scenario = scenario;
        this.name = name;
    }

    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        return ScenarioFinder.getInternalScenarios("transform-valid-*").stream()
            .map(u -> new Object[]{u.getName(), u})
            .collect(Collectors.toList());
    }

    @Test
    public void testRoundTrip() throws Exception {
        RegisterTSDiabetesType transport = InternalToTransportConverter.convert(scenario.asInternalModel());

        StringWriter expected = new StringWriter();
        StringWriter actual = new StringWriter();
        marshaller.marshal(objectFactory.createRegisterTSDiabetes(scenario.asTransportModel()), expected);
        marshaller.marshal(objectFactory.createRegisterTSDiabetes(transport), actual);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expected.toString()))
            .withTest(Input.fromString(actual.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAttributes("id")))
            .build();
        assertFalse(name + " " + diff.toString(), diff.hasDifferences());

        JsonNode tree = objectMapper.valueToTree(TransportToInternalConverter.convert(transport.getIntyg()));
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @Test
    public void testConvertToRivtav3() throws Exception {
        TsDiabetesUtlatandeV2 internal = TransportToInternalConverter.convert(scenario.asTransportModel().getIntyg());
        RegisterCertificateType actual = new RegisterCertificateType();
        actual.setIntyg(UtlatandeToIntyg.convert(internal));

        StringWriter expected = new StringWriter();
        StringWriter actualSw = new StringWriter();
        marshaller.marshal(rivtav3ObjectFactory.createRegisterCertificate(scenario.asRivtaV3TransportModel()), expected);
        marshaller.marshal(rivtav3ObjectFactory.createRegisterCertificate(actual), actualSw);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expected.toString()))
            .withTest(Input.fromString(actualSw.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAttributes("id")))
            .build();
        assertFalse(name + " " + diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testTransportTransform() throws Exception {
        StringWriter transformingString = new StringWriter();
        marshaller.marshal(objectFactory.createRegisterTSDiabetes(scenario.asTransportModel()), transformingString);
        String actual = transformer.transform(transformingString.toString());

        StringWriter expected = new StringWriter();
        marshaller.marshal(transformedObjectFactory.createRegisterCertificate(scenario.asTransformedTransportModel()), expected);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expected.toString()))
            .withTest(Input.fromString(actual))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAttributes("id")))
            .build();
        assertFalse(name + " " + diff.toString(), diff.hasDifferences());
    }
}
