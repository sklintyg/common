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
package se.inera.intyg.common.fk7263.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.ObjectFactory;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.utils.ModelAssert;
import se.inera.intyg.common.fk7263.utils.Scenario;
import se.inera.intyg.common.fk7263.utils.ScenarioFinder;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;

/**
 * @author marced, andreaskaltenbach
 */
public class InternalToTransportConverterTest {

    private static final String INTERNALTOTRANSPORTCONVERTERTEST_PATH = "InternalToTransportConverterTest";

    private ObjectMapper objectMapper = new CustomObjectMapper();
    private ObjectFactory objectFactory = new ObjectFactory();

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
        Fk7263Utlatande internalFormat = getFk7263UtlatandeFromFile(getFilepath("fk7263-utan-falt5.json"));
        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        Diff diff = compare(getFilepath("fk7263-utan-falt5.xml"), registerMedicalCertificate);
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionMaximal() throws JAXBException, IOException, SAXException, ConverterException {
        Fk7263Utlatande internalFormat = getFk7263UtlatandeFromFile(getFilepath("maximalt-fk7263-internal.json"));
        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
       Diff diff = compare(getFilepath("maximalt-fk7263-transport.xml"), registerMedicalCertificate);
       assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionWithDiagnosisAsKSH97() throws JAXBException, IOException, SAXException, ConverterException {
        Fk7263Utlatande internalFormat = getFk7263UtlatandeFromFile(getFilepath("maximalt-fk7263-with-ksh97.json"));
        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(internalFormat);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        Diff diff = compare(getFilepath("maximalt-fk7263-with-ksh97.xml"), registerMedicalCertificate);
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionMinimal() throws JAXBException, IOException, SAXException, ConverterException {
        Fk7263Utlatande externalFormat = getFk7263UtlatandeFromFile(getFilepath("minimalt-fk7263-internal.json"));
        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(externalFormat);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        Diff diff = compare(getFilepath("minimalt-fk7263-transport.xml"), registerMedicalCertificate);
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionKommentar() throws JAXBException, IOException, SAXException, ConverterException {
        Fk7263Utlatande externalFormat = getFk7263UtlatandeFromFile(getFilepath("friviligttext-fk7263-internal.json"));

        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(externalFormat);

        String expected = "8b: " + "nedsattMed25Beskrivning. " + "nedsattMed50Beskrivning. " + "nedsattMed75Beskrivning. kommentar";
        String result = registerMedicalCertificate.getLakarutlatande().getKommentar();
        assertEquals(expected, result);
    }

    @Test
    public void testConversionOrimligtDatum() throws JAXBException, IOException, SAXException, ConverterException {
        Fk7263Utlatande externalFormat = getFk7263UtlatandeFromFile(getFilepath("minimalt-fk7263-internal-orimligt-datum.json"));
        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(externalFormat);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        Diff diff = compare(getFilepath("minimalt-fk7263-transport-orimligt-datum.xml"), registerMedicalCertificate);
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    @Test
    public void testConversionMinimalSmiL() throws JAXBException, IOException, SAXException, ConverterException {
        Fk7263Utlatande externalFormat = getFk7263UtlatandeFromFile(getFilepath("minimalt-SmiL-fk7263-internal.json"));
        RegisterMedicalCertificateType registerMedicalCertificate = InternalToTransport.getJaxbObject(externalFormat);

        // read expected XML and compare with resulting RegisterMedicalCertificateType
        Diff diff = compare(getFilepath("minimalt-SmiL-fk7263-transport.xml"), registerMedicalCertificate);
        assertFalse(diff.toString(), diff.hasDifferences());
    }

    private Fk7263Utlatande getFk7263UtlatandeFromFile(String s) throws IOException {
        return objectMapper.readValue(
            new ClassPathResource(s).getInputStream(), Fk7263Utlatande.class);
    }

    @Test
    public void testPersonnummerRoot() throws Exception {
        final String pnr = "19121212-1212";
        Fk7263Utlatande utlatande = createFk7263Utlatande(createPnr(pnr));
        RegisterMedicalCertificateType res = InternalToTransport.getJaxbObject(utlatande);

        assertEquals(Constants.PERSON_ID_OID, res.getLakarutlatande().getPatient().getPersonId().getRoot());
        assertEquals(pnr, res.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void testSamordningRoot() throws Exception {
        final String pnr = "19800191-0002";
        Fk7263Utlatande utlatande = createFk7263Utlatande(createPnr(pnr));
        RegisterMedicalCertificateType res = InternalToTransport.getJaxbObject(utlatande);

        assertEquals(Constants.SAMORDNING_ID_OID, res.getLakarutlatande().getPatient().getPersonId().getRoot());
        assertEquals(pnr, res.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    private Diff compare(String filePath, RegisterMedicalCertificateType registerMedicalCertificate) throws IOException {
        String expected = Resources.toString(new ClassPathResource(filePath).getURL(), Charsets.UTF_8);
        String actual = XmlMarshallerHelper.marshal(objectFactory.createRegisterMedicalCertificate(registerMedicalCertificate));

        return DiffBuilder
            .compare(Input.fromString(expected))
            .withTest(Input.fromString(actual))
            .ignoreComments()
            .ignoreWhitespace()
            .checkForSimilar()
            .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
            .build();
    }

    private Fk7263Utlatande createFk7263Utlatande(Personnummer personnummer) {
        Patient patient = new Patient();
        patient.setPersonId(personnummer);

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(new Vardgivare());

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);

        GrundData grundData = new GrundData();
        grundData.setPatient(patient);
        grundData.setSkapadAv(skapadAv);

        Fk7263Utlatande utlatande = new Fk7263Utlatande();
        utlatande.setGrundData(grundData);

        return utlatande;
    }

    private Personnummer createPnr(String civicRegistrationNumber) {
        return Personnummer.createPersonnummer(civicRegistrationNumber).get();
    }

    private String getFilepath(String fileName) {
        return INTERNALTOTRANSPORTCONVERTERTEST_PATH + "/" + fileName;
    }

}
