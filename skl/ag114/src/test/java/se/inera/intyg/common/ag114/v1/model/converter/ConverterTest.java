/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag114.v1.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.SVRLHelper;

import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.rest.Ag114ModuleApiV1;
import se.inera.intyg.common.ag114.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.ag114.v1.validator.ValidatorUtilSKL;
import se.inera.intyg.common.agparent.model.converter.RegisterCertificateTestValidator;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.PQType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class ConverterTest {

    @Mock
    private ValidatorUtilSKL validatorUtilSKL;

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    @Mock
    private WebcertModuleService webcertModuleService;

    private ObjectMapper objectMapper = new CustomObjectMapper();

    public ConverterTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setup() {
        when(webcertModuleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);
        when(webcertModuleService.validateDiagnosisCodeFormat(anyString())).thenReturn(true);
    }

    @Test
    public void doSchematronValidationAg114() throws Exception {
        String xmlContents = Resources.toString(getResource("v1/transport/ag114-2.xml"), Charsets.UTF_8);

        RegisterCertificateTestValidator generalValidator = new RegisterCertificateTestValidator();
        assertTrue(generalValidator.validateGeneral(xmlContents));

        RegisterCertificateValidator validator = new RegisterCertificateValidator(Ag114ModuleApiV1.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(xmlContents.getBytes(Charsets.UTF_8))));

        assertEquals(0, SVRLHelper.getAllFailedAssertions(result).size());
    }

    @Test
    public void outputJsonFromXml() throws Exception {

        String xmlContents = Resources.toString(getResource("v1/transport/ag114-2.xml"), Charsets.UTF_8);
        RegisterCertificateType transport = JAXB.unmarshal(new StringReader(xmlContents), RegisterCertificateType.class);

        String json = getJsonFromTransport(transport);
        Ag114UtlatandeV1 utlatandeFromJson = objectMapper.readValue(json, Ag114UtlatandeV1.class);

        RegisterCertificateType transportConvertedALot = InternalToTransport.convert(utlatandeFromJson, webcertModuleService);
        String convertedXML = getXmlFromModel(transportConvertedALot);

        // Do schematron validation on the xml-string from the converted transport format
        RegisterCertificateValidator validator = new RegisterCertificateValidator(Ag114ModuleApiV1.SCHEMATRON_FILE);
        SchematronOutputType result = validator
            .validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));
        assertEquals(getErrorString(result), 0, SVRLHelper.getAllFailedAssertions(result).size());

        // Why not validate internal model as well?
        internalValidator.validateDraft(utlatandeFromJson);
    }

    private String getErrorString(SchematronOutputType result) {
        StringBuilder errorMsg = new StringBuilder();
        SVRLHelper.getAllFailedAssertions(result).stream()
            .map(e -> e.getText())
            .collect(Collectors.toList())
            .forEach(e -> errorMsg.append(e));
        return errorMsg.toString();
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    private String getXmlFromModel(RegisterCertificateType transport) throws IOException, JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class, DatePeriodType.class, PQType.class);
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(transport);
        jaxbContext.createMarshaller().marshal(requestElement, sw);
        return sw.toString();
    }

    private String getJsonFromTransport(RegisterCertificateType transport) throws ConverterException {
        StringWriter jsonWriter = new StringWriter();
        Ag114UtlatandeV1 internal = TransportToInternal.convert(transport.getIntyg());
        try {
            objectMapper.writeValue(jsonWriter, internal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonWriter.toString();
    }

}
