/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v6.transformation;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.transformation.test.XPathEvaluator;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsBas;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TsBasTransportToV3TransformerXpathTest {

    private static XslTransformer transformer;

    @BeforeClass
    public static void setup() {
        transformer = new XslTransformer("xsl/transportToV3.xsl");
    }

    @Test
    public void testMaximaltIntyg() throws IOException, ParserConfigurationException, JAXBException, XPathExpressionException, SAXException,
            TransformerException {
        performTests(new ClassPathResource("v6/scenarios/transport/valid-maximal.xml"));
    }

    @Test
    public void testMinimaltIntyg() throws IOException, ParserConfigurationException, JAXBException, XPathExpressionException, SAXException,
            TransformerException {
        performTests(new ClassPathResource("v6/scenarios/transport/valid-minimal.xml"));

    }

    private void performTests(ClassPathResource cpr) throws ParserConfigurationException, JAXBException, SAXException, IOException, TransformerException,
            XPathExpressionException {

        String xmlContent = Resources.toString(cpr.getURL(), Charsets.UTF_8);
        TSBasIntyg utlatande = JAXB.unmarshal(cpr.getFile(), RegisterTSBasType.class).getIntyg();
        String transformed = transformer.transform(xmlContent);

        // Create an xPath evaluator that operates on the transport model.
        XPathEvaluator xPath = createXPathEvaluator(transformed);

        // Check intyg against xpath
        assertEquals("Intyg - typ", "TSTRK1007", xPath.evaluate(XPathExpressionsV3.TYP_AV_INTYG_XPATH));

        assertEquals("Intyg - version", Integer.parseInt(utlatande.getVersion())+"."+Integer.parseInt(utlatande.getUtgava()), xPath.evaluate(XPathExpressionsV3.TS_VERSION_XPATH));

        // Patient
        Patient patient = utlatande.getGrundData().getPatient();

        assertEquals("Patient - personnummer", patient.getPersonId().getExtension().replace("-",""),
                xPath.evaluate(XPathExpressionsV3.PATIENT_PERSONNUMMER_XPATH));
        assertEquals("Patient - förnamn", patient.getFornamn(), xPath.evaluate(XPathExpressionsV3.PATIENT_FORNAMN_XPATH));
        assertEquals("Patient - efternamn", patient.getEfternamn(), xPath.evaluate(XPathExpressionsV3.PATIENT_EFTERNAMN_XPATH));
        assertEquals("Patient - postadress", patient.getPostadress(), xPath.evaluate(XPathExpressionsV3.PATIENT_POSTADRESS_XPATH));
        assertEquals("Patient - postnummer", patient.getPostnummer(), xPath.evaluate(XPathExpressionsV3.PATIENT_POSTNUMMER_XPATH));
        assertEquals("Patient - postort", patient.getPostort(), xPath.evaluate(XPathExpressionsV3.PATIENT_POSTORT_XPATH));

        // Signeringsdatum
        assertEquals("Signeringsdatum", utlatande.getGrundData().getSigneringsTidstampel(), xPath.evaluate(XPathExpressionsV3.SIGNERINGTIDPUNKT_XPATH));

        // Skapad Av
        SkapadAv skapadAv = utlatande.getGrundData().getSkapadAv();

        if (!skapadAv.getBefattningar().isEmpty()) {
            assertEquals("Skapad av - befattningar", skapadAv.getBefattningar().get(0),
                xPath.evaluate(XPathExpressionsV3.SKAPAD_AV_BEFATTNING_XPATH));
        }

        assertEquals("Skapad av - fullständigt namn", skapadAv.getFullstandigtNamn(),
                xPath.evaluate(XPathExpressionsV3.SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH));
        assertEquals("Skapad av - hsa-id", skapadAv.getPersonId().getExtension(),
                xPath.evaluate(XPathExpressionsV3.SKAPAD_AV_HSAID_XPATH));

        if (!skapadAv.getSpecialiteter().isEmpty()) {
            assertEquals("Skapad av - specialitet", skapadAv.getSpecialiteter().get(0),
                    xPath.evaluate(XPathExpressionsV3.SKAPAD_AV_SPECIALISTKOMPETENS_XPATH));
        }

        // Vardenhet
        Vardenhet vardenhet = skapadAv.getVardenhet();
        assertEquals("Enhet - enhetsid", vardenhet.getEnhetsId().getExtension(),
                xPath.evaluate(XPathExpressionsV3.ENHET_ID_XPATH));
        assertEquals("Enhet - enhetsnamn", vardenhet.getEnhetsnamn(),
                xPath.evaluate(XPathExpressionsV3.ENHET_VARDINRATTNINGENS_NAMN_XPATH));
        assertEquals("Enhet - postadress", vardenhet.getPostadress(),
                xPath.evaluate(XPathExpressionsV3.ENHET_POSTADRESS_XPATH));
        assertEquals("Enhet - postnummer", vardenhet.getPostnummer(),
                xPath.evaluate(XPathExpressionsV3.ENHET_POSTNUMMER_XPATH));
        assertEquals("Enhet - postort", vardenhet.getPostort(),
                xPath.evaluate(XPathExpressionsV3.ENHET_POSTORT_XPATH));
        assertEquals("Enhet - postort", vardenhet.getTelefonnummer(),
                xPath.evaluate(XPathExpressionsV3.ENHET_TELEFONNUMMER_XPATH));

        // Vardgivare
        assertEquals("Enhet - vardgivare - id", vardenhet.getVardgivare().getVardgivarid().getExtension(),
                xPath.evaluate(XPathExpressionsV3.VARDGIVARE_ID_XPATH));
        assertEquals("Enhet - vardgivare - id", vardenhet.getVardgivare().getVardgivarnamn(),
                xPath.evaluate(XPathExpressionsV3.VARDGIVARE_NAMN_XPATH));

        // IntygAvser
        for (KorkortsbehorighetTsBas t : utlatande.getIntygAvser().getKorkortstyp()) {
            assertTrue(xPath.evaluate(XPathExpressionsV3.booleanXPath(XPathExpressionsV3.INTYG_AVSER_TEMPLATE,
                    IntygAvserKod.valueOf(t.value().value()).getCode())));
        }

        // ID-kontroll
        assertTrue(xPath.evaluate(XPathExpressionsV3.booleanXPath(XPathExpressionsV3.ID_KONTROLL_TEMPLATE,
                utlatande.getIdentitetStyrkt().getIdkontroll().value())));

        // And so on...

    }

    private XPathEvaluator createXPathEvaluator(String xml) throws ParserConfigurationException,
            JAXBException, SAXException, IOException, TransformerException {

        SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
        namespaces.bindDefaultNamespaceUri("urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3");
        namespaces.bindNamespaceUri("ns3", "urn:riv:clinicalprocess:healthcond:certificate:3");
        namespaces.bindNamespaceUri("ns2", "urn:riv:clinicalprocess:healthcond:certificate:types:3");

        XPath xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(namespaces);

        Node document = generateDocumentFor(xml);

        return new XPathEvaluator(xPath, document);
    }

    private Node generateDocumentFor(String xml) throws ParserConfigurationException, JAXBException, SAXException, IOException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Node node = parser.newDocument();

        InputStream is = new ByteArrayInputStream(xml.getBytes());

        RegisterCertificateType register = JAXB.unmarshal(is, RegisterCertificateType.class);

        JAXBElement<Intyg> jaxbElement = new JAXBElement<>(new QName("intyg"), Intyg.class, register.getIntyg());
        JAXBContext context = JAXBContext.newInstance(Intyg.class);
        context.createMarshaller().marshal(jaxbElement, node);

        // Output the Document
        outputDocument(node);
        return node;
    }

    private void outputDocument(Node node) throws TransformerException {
        StreamResult result = new StreamResult(System.out);
        DOMSource source = new DOMSource(node);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.transform(source, result);
    }
}
