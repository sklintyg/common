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
package se.inera.intyg.common.ts_diabetes.v2.model.converter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.stream.Stream;
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
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMappingConfigLoader;
import se.inera.intyg.common.support.modules.converter.mapping.MappedCareProvider;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.utils.Scenario;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioFinder;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioNotFoundException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.ObjectFactory;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateType;

@SuppressWarnings("checkstyle:EmptyCatchBlock")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BefattningService.class, CareProviderMappingConfigLoader.class, CareProviderMapperUtil.class,
    InternalConverterUtil.class})
class RoundTripTest {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();
    private final ObjectFactory objectFactory = new ObjectFactory();
    private final se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory rivtav3ObjectFactory =
        new se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory();
    private final se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.ObjectFactory transformedObjectFactory =
        new se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.ObjectFactory();
    private static final Marshaller marshaller;
    private static final XslTransformer transformer;

    static {
        try {
            marshaller = JAXBContext.newInstance(RegisterTSDiabetesType.class, RegisterCertificateType.class, DatePeriodType.class,
                    PartialDateType.class,
                    se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType.class)
                .createMarshaller();
            transformer = new XslTransformer("xsl/transform-ts-diabetes.xsl");

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void initUtils() {
        final var mapper = mock(CareProviderMapperUtil.class);

        when(mapper.getMappedCareprovider(any(), any()))
            .thenAnswer(inv -> new MappedCareProvider(
                inv.getArgument(0, String.class),
                inv.getArgument(1, String.class)
            ));

        new InternalConverterUtil(mapper).initialize();
        new TransportConverterUtil(mapper).initialize();
    }

    static Stream<Arguments> scenarioProvider() throws ScenarioNotFoundException {
        return ScenarioFinder.getInternalScenarios("transform-valid-*").stream()
            .map(scenario -> Arguments.of(scenario.getName(), scenario));
    }

    @ParameterizedTest(name = "{index}: Scenario: {0}")
    @MethodSource("scenarioProvider")
    void testRoundTrip(String name, Scenario scenario) throws Exception {
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
        assertFalse(diff.hasDifferences(), name + " " + diff.toString());

        JsonNode tree = objectMapper.valueToTree(TransportToInternalConverter.convert(transport.getIntyg()));
        JsonNode expectedTree = objectMapper.valueToTree(scenario.asInternalModel());
        JSONAssert.assertEquals(expectedTree.toString(), tree.toString(), false);
    }

    @ParameterizedTest(name = "{index}: Scenario: {0}")
    @MethodSource("scenarioProvider")
    void testConvertToRivtav3(String name, Scenario scenario) throws Exception {
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
        assertFalse(diff.hasDifferences(), name + " " + diff.toString());
    }

    @ParameterizedTest(name = "{index}: Scenario: {0}")
    @MethodSource("scenarioProvider")
    void testTransportTransform(String name, Scenario scenario) throws Exception {
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
        assertFalse(diff.hasDifferences(), name + " " + diff.toString());
    }
}

