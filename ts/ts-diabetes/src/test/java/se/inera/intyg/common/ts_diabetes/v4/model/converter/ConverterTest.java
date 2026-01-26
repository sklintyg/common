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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.stream.Collectors;
import javax.xml.transform.stream.StreamSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.rest.TsDiabetesModuleApiV4;
import se.inera.intyg.common.ts_diabetes.v4.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PartialDateType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class ConverterTest {

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    public ConverterTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void setUp() {
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

    @Test
    public void doSchematronValidationTsDiabetesV4() throws Exception {
        String xmlContents = Resources.toString(getResource("v4/transport/scenarios/pass-complete.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator(TsDiabetesModuleApiV4.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void outputJsonFromXml() throws Exception {
        String xmlContents = Resources.toString(getResource("v4/transport/scenarios/pass-complete.xml"), Charsets.UTF_8);
        RegisterCertificateType transport = JAXB.unmarshal(new StringReader(xmlContents), RegisterCertificateType.class);

        String json = getJsonFromTransport(transport);
        TsDiabetesUtlatandeV4 utlatandeFromJson = objectMapper.readValue(json, TsDiabetesUtlatandeV4.class);

        RegisterCertificateType transportConvertedALot = InternalToTransport.convert(utlatandeFromJson);
        String convertedXML = getXmlFromModel(transportConvertedALot);

        // Do schematron validation on the xml-string from the converted transport format
        RegisterCertificateValidator validator = new RegisterCertificateValidator(TsDiabetesModuleApiV4.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));
        assertEquals(getErrorString(result), 0, SVRLHelper.getAllFailedAssertions(result).size());

        // Why not validate internal model as well?
        internalValidator.validateDraft(utlatandeFromJson);
    }

    private String getErrorString(SchematronOutputType result) {
        StringBuilder errorMsg = new StringBuilder();
        SVRLHelper.getAllFailedAssertions(result).stream()
            .map(AbstractSVRLMessage::getText)
            .collect(Collectors.toList())
            .forEach(errorMsg::append);
        return errorMsg.toString();
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    private String getXmlFromModel(RegisterCertificateType transport) throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class, PartialDateType.class);
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(transport);
        jaxbContext.createMarshaller().marshal(requestElement, sw);
        return sw.toString();
    }

    private String getJsonFromTransport(RegisterCertificateType transport) throws ConverterException {
        StringWriter jsonWriter = new StringWriter();
        TsDiabetesUtlatandeV4 internal = TransportToInternal
            .convert(transport.getIntyg());
        try {
            objectMapper.writeValue(jsonWriter, internal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonWriter.toString();
    }

}