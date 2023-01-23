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
package se.inera.intyg.common.fk7263.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.utils.ModelAssert;
import se.inera.intyg.common.fk7263.utils.Scenario;
import se.inera.intyg.common.fk7263.utils.ScenarioFinder;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author marced, andreaskaltenbach
 */
public class InternalToTransportConverterTest {

    @Test
    public void testConvertUtlatande() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Fk7263Utlatande intUtlatande = scenario.asInternalModel();

            RegisterMedicalCertificateType actual = InternalToTransport.getJaxbObject(intUtlatande);

            RegisterMedicalCertificateType expected = scenario.asTransportModel();

            ModelAssert.assertEquals("Error in scenario " + scenario.getName(), expected, actual);
        }
    }

    @Test
    public void testConversionUtanFalt5() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande internalFormat = objectMapper.readValue(
            new ClassPathResource("InternalToTransportConverterTest/fk7263-utan-falt5.json").getInputStream(), Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/fk7263-utan-falt5.xml")
            .getURL(), Charsets.UTF_8);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expectation.toString()))
            .withTest(Input.fromString(stringWriter.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
            .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionMaximal() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande internalFormat = objectMapper.readValue(
            new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-internal.json").getInputStream(),
            Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-transport.xml")
            .getURL(), Charsets.UTF_8);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expectation.toString()))
            .withTest(Input.fromString(stringWriter.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
            .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionWithDiagnosisAsKSH97() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande internalFormat = objectMapper.readValue(
            new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-with-ksh97.json").getInputStream(),
            Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificate), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/maximalt-fk7263-with-ksh97.xml")
            .getURL(), Charsets.UTF_8);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expectation.toString()))
            .withTest(Input.fromString(stringWriter.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
            .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionMinimal() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(
            new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-internal.json").getInputStream(),
            Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-transport.xml")
            .getURL(), Charsets.UTF_8);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expectation.toString()))
            .withTest(Input.fromString(stringWriter.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
            .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionKommentar() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(
            new ClassPathResource("InternalToTransportConverterTest/friviligttext-fk7263-internal.json").getInputStream(),
            Fk7263Utlatande.class);
        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);
        String expected = "8b: " + "nedsattMed25Beskrivning. " + "nedsattMed50Beskrivning. " + "nedsattMed75Beskrivning. kommentar";
        String result = registerMedicalCertificateType.getLakarutlatande().getKommentar();
        assertEquals(expected, result);
    }

    @Test
    public void testConversionOrimligtDatum() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(
            new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-internal-orimligt-datum.json").getInputStream(),
            Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources
            .toString(new ClassPathResource("InternalToTransportConverterTest/minimalt-fk7263-transport-orimligt-datum.xml")
                .getURL(), Charsets.UTF_8);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expectation.toString()))
            .withTest(Input.fromString(stringWriter.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
            .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionMinimalSmiL() throws JAXBException, IOException, SAXException, ConverterException {

        ObjectMapper objectMapper = new CustomObjectMapper();
        Fk7263Utlatande externalFormat = objectMapper.readValue(
            new ClassPathResource("InternalToTransportConverterTest/minimalt-SmiL-fk7263-internal.json").getInputStream(),
            Fk7263Utlatande.class);

        RegisterMedicalCertificateType registerMedicalCertificateType = InternalToTransport.getJaxbObject(externalFormat);

        JAXBContext jaxbContext = JAXBContext.newInstance(RegisterMedicalCertificateType.class, LakarutlatandeType.class);
        StringWriter stringWriter = new StringWriter();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(wrapJaxb(registerMedicalCertificateType), stringWriter);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        String expectation = Resources.toString(new ClassPathResource("InternalToTransportConverterTest/minimalt-SmiL-fk7263-transport.xml")
            .getURL(), Charsets.UTF_8);

        Diff diff = DiffBuilder
            .compare(Input.fromString(expectation.toString()))
            .withTest(Input.fromString(stringWriter.toString()))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
            .build();
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testPersonnummerRoot() throws Exception {
        final String pnr = "19121212-1212";
        Fk7263Utlatande utlatande = new Fk7263Utlatande();
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        patient.setPersonId(createPnr(pnr));
        grundData.setPatient(patient);
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        utlatande.setGrundData(grundData);
        RegisterMedicalCertificateType res = InternalToTransport.getJaxbObject(utlatande);
        assertEquals(Constants.PERSON_ID_OID, res.getLakarutlatande().getPatient().getPersonId().getRoot());
        assertEquals(pnr, res.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void testSamordningRoot() throws Exception {
        final String pnr = "19800191-0002";
        Fk7263Utlatande utlatande = new Fk7263Utlatande();
        GrundData grundData = new GrundData();
        Patient patient = new Patient();
        patient.setPersonId(createPnr(pnr));
        grundData.setPatient(patient);
        HoSPersonal skapadAv = new HoSPersonal();
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());
        skapadAv.setVardenhet(vardenhet);
        grundData.setSkapadAv(skapadAv);
        utlatande.setGrundData(grundData);
        RegisterMedicalCertificateType res = InternalToTransport.getJaxbObject(utlatande);
        assertEquals(Constants.SAMORDNING_ID_OID, res.getLakarutlatande().getPatient().getPersonId().getRoot());
        assertEquals(pnr, res.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    private Personnummer createPnr(String civicRegistrationNumber) {
        return Personnummer.createPersonnummer(civicRegistrationNumber).get();
    }

    private JAXBElement<?> wrapJaxb(RegisterMedicalCertificateType ws) {
        JAXBElement<?> jaxbElement = new JAXBElement<>(
            new QName("urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3", "RegisterMedicalCertificate"),
            RegisterMedicalCertificateType.class, ws);
        return jaxbElement;
    }

}
