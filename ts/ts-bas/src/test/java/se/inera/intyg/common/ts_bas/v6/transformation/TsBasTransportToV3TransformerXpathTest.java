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
package se.inera.intyg.common.ts_bas.v6.transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intyg.common.ts_parent.transformation.test.XPathEvaluator;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.AlkoholNarkotikaLakemedel;
import se.inera.intygstjanster.ts.services.v1.BedomningTypBas;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypBas;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypVarden;
import se.inera.intygstjanster.ts.services.v1.HjartKarlSjukdomar;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsBas;
import se.inera.intygstjanster.ts.services.v1.Medvetandestorning;
import se.inera.intygstjanster.ts.services.v1.OvrigMedicinering;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.Sjukhusvard;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynfunktionBas;
import se.inera.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.inera.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;
import se.inera.intygstjanster.ts.services.v1.Utvecklingsstorning;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

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

    private void performTests(ClassPathResource cpr)
        throws ParserConfigurationException, JAXBException, SAXException, IOException, TransformerException,
        XPathExpressionException {

        String xmlContent = Resources.toString(cpr.getURL(), Charsets.UTF_8);
        TSBasIntyg utlatande = JAXB.unmarshal(cpr.getFile(), RegisterTSBasType.class).getIntyg();
        String transformed = transformer.transform(xmlContent);

        // Create an xPath evaluator that operates on the transport model.
        XPathEvaluator xPath = createXPathEvaluator(transformed);

        // Check intyg against xpath
        assertEquals("Intyg - typ", "TSTRK1007", xPath.evaluate(XPathExpressionsV3.TYP_AV_INTYG_XPATH));

        assertEquals("Intyg - version", Integer.parseInt(utlatande.getVersion()) + "." + Integer.parseInt(utlatande.getUtgava()),
            xPath.evaluate(XPathExpressionsV3.TS_VERSION_XPATH));

        // Patient
        Patient patient = utlatande.getGrundData().getPatient();

        assertEquals("Patient - personnummer", patient.getPersonId().getExtension().replace("-", ""),
            xPath.evaluate(XPathExpressionsV3.PATIENT_PERSONNUMMER_XPATH));
        assertEquals("Patient - förnamn", patient.getFornamn(), xPath.evaluate(XPathExpressionsV3.PATIENT_FORNAMN_XPATH));
        assertEquals("Patient - efternamn", patient.getEfternamn(), xPath.evaluate(XPathExpressionsV3.PATIENT_EFTERNAMN_XPATH));
        assertEquals("Patient - postadress", patient.getPostadress(), xPath.evaluate(XPathExpressionsV3.PATIENT_POSTADRESS_XPATH));
        assertEquals("Patient - postnummer", patient.getPostnummer(), xPath.evaluate(XPathExpressionsV3.PATIENT_POSTNUMMER_XPATH));
        assertEquals("Patient - postort", patient.getPostort(), xPath.evaluate(XPathExpressionsV3.PATIENT_POSTORT_XPATH));

        // Signeringsdatum
        assertEquals("Signeringsdatum", utlatande.getGrundData().getSigneringsTidstampel(),
            xPath.evaluate(XPathExpressionsV3.SIGNERINGTIDPUNKT_XPATH));

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

        // Synfunktion
        SynfunktionBas synfunktion = utlatande.getSynfunktion();

        assertEquals(Boolean.toString(synfunktion.isHarSynfaltsdefekt()), xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_DEFEKT_XPATH));
        assertEquals(Boolean.toString(synfunktion.isHarNattblindhet()), xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_NATTBLINDHET_XPATH));
        assertEquals(Boolean.toString(synfunktion.isHarProgressivOgonsjukdom()),
            xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_PROGRESSIV_XPATH));
        assertEquals(Boolean.toString(synfunktion.isHarDiplopi()), xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_DIPLOPI_XPATH));
        assertEquals(Boolean.toString(synfunktion.isHarNystagmus()), xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_NYSTAGMUS_XPATH));

        SynskarpaUtanKorrektion synskarpaUtanKorrektion = synfunktion.getSynskarpaUtanKorrektion();
        SynskarpaMedKorrektion synskarpaMedKorrektion = synfunktion.getSynskarpaMedKorrektion();

        assertEquals(Double.toString(synskarpaUtanKorrektion.getHogerOga()),
            xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_UTAN_KORREKTION_HOGER_XPATH));
        assertEquals(Double.toString(synskarpaUtanKorrektion.getVansterOga()),
            xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_UTAN_KORREKTION_VANSTER_XPATH));
        assertEquals(Double.toString(synskarpaUtanKorrektion.getBinokulart()),
            xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_UTAN_KORREKTION_BINOKULART_XPATH));

        if (synskarpaMedKorrektion.getHogerOga() != null) {
            assertEquals(Double.toString(synskarpaMedKorrektion.getHogerOga()),
                xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_MED_KORREKTION_HOGER_XPATH));
        }
        if (synskarpaMedKorrektion.getVansterOga() != null) {
            assertEquals(Double.toString(synskarpaMedKorrektion.getVansterOga()),
                xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_MED_KORREKTION_VANSTER_XPATH));
        }
        if (synskarpaMedKorrektion.getBinokulart() != null) {
            assertEquals(Double.toString(synskarpaMedKorrektion.getBinokulart()),
                xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_MED_KORREKTION_BINOKULART_XPATH));
        }

        assertEquals(Boolean.toString(synskarpaMedKorrektion.isHarKontaktlinsHogerOga()),
            xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_KONTAKTLINS_HOGER_XPATH));
        assertEquals(Boolean.toString(synskarpaMedKorrektion.isHarKontaktlinsVansterOga()),
            xPath.evaluate(XPathExpressionsV3.SYNFUNKTION_KONTAKTLINS_VANSTER_XPATH));

        // Hörsel och balanssinne
        assertEquals(Boolean.toString(utlatande.getHorselBalanssinne().isHarBalansrubbningYrsel()),
            xPath.evaluate(XPathExpressionsV3.HORSEL_BALANS_XPATH));

        // Rörelseorganens funktioner
        assertEquals(Boolean.toString(utlatande.getRorelseorganensFunktioner().isHarRorelsebegransning()),
            xPath.evaluate(XPathExpressionsV3.RORELSEORGAN_SJUKDOM_XPATH));

        if (utlatande.getRorelseorganensFunktioner().isHarRorelsebegransning()) {
            assertEquals(utlatande.getRorelseorganensFunktioner().getRorelsebegransningBeskrivning(),
                xPath.evaluate(XPathExpressionsV3.RORELSEORGAN_BESKRICNING_XPATH));
        }

        // Hjärt- och kärlsjukdomar
        HjartKarlSjukdomar hjartKarlSjukdomar = utlatande.getHjartKarlSjukdomar();

        assertEquals(Boolean.toString(hjartKarlSjukdomar.isHarRiskForsamradHjarnFunktion()),
            xPath.evaluate(XPathExpressionsV3.HJARTKARL_RISK_FUNKTION_XPATH));
        assertEquals(Boolean.toString(hjartKarlSjukdomar.isHarHjarnskadaICNS()), xPath.evaluate(XPathExpressionsV3.HJARTKARL_SKADA_XPATH));
        assertEquals(Boolean.toString(hjartKarlSjukdomar.isHarRiskfaktorerStroke()),
            xPath.evaluate(XPathExpressionsV3.HJARTKARL_RISK_STROKE_XPATH));

        if (hjartKarlSjukdomar.isHarRiskfaktorerStroke()) {
            assertEquals(hjartKarlSjukdomar.getRiskfaktorerStrokeBeskrivning(),
                xPath.evaluate(XPathExpressionsV3.HJARTKARL_RISK_STROKE_BESKRIVNING_XPATH));
        }

        // Diabetes
        DiabetesTypBas diabetes = utlatande.getDiabetes();

        assertEquals(Boolean.toString(diabetes.isHarDiabetes()), xPath.evaluate(XPathExpressionsV3.DIABETES_XPATH));

        if (diabetes.isHarDiabetes()) {
            if (diabetes.getDiabetesTyp().equals(DiabetesTypVarden.TYP_1)) {
                assertEquals("Diabetes typ1", "E10", xPath.evaluate(XPathExpressionsV3.DIABETES_TYPE_XPATH));
            } else if (diabetes.getDiabetesTyp().equals(DiabetesTypVarden.TYP_2)) {
                assertEquals("Diabetes typ2", "E11", xPath.evaluate(XPathExpressionsV3.DIABETES_TYPE_XPATH));
            }

            if (diabetes.isHarBehandlingKost() != null) {
                assertEquals(Boolean.toString(diabetes.isHarBehandlingKost()), xPath.evaluate(XPathExpressionsV3.DIABETES_KOST_XPATH));
            }
            if (diabetes.isHarBehandlingTabletter() != null) {
                assertEquals(Boolean.toString(diabetes.isHarBehandlingTabletter()),
                    xPath.evaluate(XPathExpressionsV3.DIABETES_TABLETTER_XPATH));
            }
            if (diabetes.isHarBehandlingInsulin() != null) {
                assertEquals(Boolean.toString(diabetes.isHarBehandlingInsulin()),
                    xPath.evaluate(XPathExpressionsV3.DIABETES_INSULIN_XPATH));
            }
        }

        // Neurologiska sjukdomar
        assertEquals(Boolean.toString(utlatande.isNeurologiskaSjukdomar()),
            xPath.evaluate(XPathExpressionsV3.NEUROLOGISKA_SJUKDOMAR_XPATH));

        // Epilepsi, epileptiskt anfall och annan medvetandestörning
        Medvetandestorning medvetandestorning = utlatande.getMedvetandestorning();
        assertEquals(Boolean.toString(medvetandestorning.isHarMedvetandestorning()),
            xPath.evaluate(XPathExpressionsV3.MEDVETANDESTORNING_XPATH));

        if (medvetandestorning.isHarMedvetandestorning()) {
            assertEquals(medvetandestorning.getMedvetandestorningBeskrivning(),
                xPath.evaluate(XPathExpressionsV3.MEDVETANDESTORNING_BESKRIVNING_XPATH));
        }

        // Njursjukdom
        assertEquals(Boolean.toString(utlatande.isHarNjurSjukdom()), xPath.evaluate(XPathExpressionsV3.NJURSJUKDOM_XPATH));

        // Demens och andra kognitiva störningar
        assertEquals(Boolean.toString(utlatande.isHarKognitivStorning()), xPath.evaluate(XPathExpressionsV3.DEMENS_XPATH));

        // Sömn- och vakenhetsstörningar
        assertEquals(Boolean.toString(utlatande.isHarSomnVakenhetStorning()),
            xPath.evaluate(XPathExpressionsV3.SOMN_OCH_VAKENHETSSTORNINGAR_XPATH));

        // Alkohol, narkotika och läkemedel
        AlkoholNarkotikaLakemedel alkoholNarkotikaLakemedel = utlatande.getAlkoholNarkotikaLakemedel();
        assertEquals(Boolean.toString(alkoholNarkotikaLakemedel.isHarTeckenMissbruk()),
            xPath.evaluate(XPathExpressionsV3.ALKOHOL_TECKEN_MISSBRUK_XPATH));
        assertEquals(Boolean.toString(alkoholNarkotikaLakemedel.isHarVardinsats()),
            xPath.evaluate(XPathExpressionsV3.ALKOHOL_HAR_VARDINSATS_XPATH));

        if (alkoholNarkotikaLakemedel.isHarVardinsats()) {
            assertEquals(Boolean.toString(alkoholNarkotikaLakemedel.isHarVardinsatsProvtagningBehov()),
                xPath.evaluate(XPathExpressionsV3.ALKOHOL_HAR_VARDINSATS_PROVTAGNING_XPATH));
        }

        assertEquals(Boolean.toString(alkoholNarkotikaLakemedel.isHarLakarordineratLakemedelsbruk()),
            xPath.evaluate(XPathExpressionsV3.ALKOHOL_ORDINERAT_XPATH));

        if (alkoholNarkotikaLakemedel.isHarLakarordineratLakemedelsbruk()) {
            assertEquals(alkoholNarkotikaLakemedel.getLakarordineratLakemedelOchDos(),
                xPath.evaluate(XPathExpressionsV3.ALKOHOL_ORDINERAT_LAKEMEDEL_XPATH));
        }

        // Psykiska sjukdomar och störningar
        assertEquals(Boolean.toString(utlatande.getMedvetandestorning().isHarMedvetandestorning()),
            xPath.evaluate(XPathExpressionsV3.PSYKISKA_SJUKDOMAR_XPATH));

        // ADHD, autismspektrumtillstånd och likartade tillstånd samt psykisk utvecklingsstörning
        Utvecklingsstorning utvecklingsstorning = utlatande.getUtvecklingsstorning();
        assertEquals(Boolean.toString(utvecklingsstorning.isHarPsykiskUtvecklingsstorning()),
            xPath.evaluate(XPathExpressionsV3.UTVECKLINGSSTORNING_PSYKISK_XPATH));
        assertEquals(Boolean.toString(utvecklingsstorning.isHarAndrayndrom()),
            xPath.evaluate(XPathExpressionsV3.UTVECKLINGSSTORNING_ANDRA_XPATH));

        // Sjukhusvård
        Sjukhusvard sjukhusvard = utlatande.getSjukhusvard();
        assertEquals(Boolean.toString(sjukhusvard.isHarSjukhusvardEllerLakarkontakt()),
            xPath.evaluate(XPathExpressionsV3.SJUKHUSVARD_XPATH));

        if (sjukhusvard.isHarSjukhusvardEllerLakarkontakt()) {
            assertEquals(sjukhusvard.getSjukhusvardEllerLakarkontaktDatum(), xPath.evaluate(XPathExpressionsV3.SJUKHUSVARD_DATUM_XPATH));
            assertEquals(sjukhusvard.getSjukhusvardEllerLakarkontaktVardinrattning(),
                xPath.evaluate(XPathExpressionsV3.SJUKHUSVARD_INRATTNING_XPATH));
            assertEquals(sjukhusvard.getSjukhusvardEllerLakarkontaktAnledning(),
                xPath.evaluate(XPathExpressionsV3.SJUKHUSVARD_ANLEDNING_XPATH));
        }

        // Övrig medicinering
        OvrigMedicinering ovrigMedicinering = utlatande.getOvrigMedicinering();
        assertEquals(Boolean.toString(ovrigMedicinering.isHarStadigvarandeMedicinering()),
            xPath.evaluate(XPathExpressionsV3.OVRIG_MEDICIN_XPATH));

        if (ovrigMedicinering.isHarStadigvarandeMedicinering()) {
            assertEquals(ovrigMedicinering.getStadigvarandeMedicineringBeskrivning(),
                xPath.evaluate(XPathExpressionsV3.OVRIG_MEDICIN_BESKRIVNING_XPATH));
        }

        // Övrig kommentar
        if (utlatande.getOvrigKommentar() != null) {
            assertEquals("Övrig kommentar", utlatande.getOvrigKommentar(), xPath.evaluate(XPathExpressionsV3.OVRIG_BESKRIVNING_XPATH));
        }

        // Bedömning
        BedomningTypBas bedomning = utlatande.getBedomning();

        for (KorkortsbehorighetTsBas t : bedomning.getKorkortstyp()) {
            KorkortsbehorighetKod kod = KorkortsbehorighetKod.valueOf(t.value().value());
            assertTrue(kod.getDescription(),
                xPath.evaluate(XPathExpressionsV3.booleanXPath(XPathExpressionsV3.BEDOMNING_BEHORIGHET_TEMPLATE,
                    kod.getCode())));
        }

        if (utlatande.getBedomning().isKanInteTaStallning() != null && utlatande.getBedomning().isKanInteTaStallning()) {
            assertTrue(KorkortsbehorighetKod.KANINTETASTALLNING.getDescription(),
                xPath.evaluate(XPathExpressionsV3
                    .booleanXPath(XPathExpressionsV3.BEDOMNING_BEHORIGHET_TEMPLATE, KorkortsbehorighetKod.KANINTETASTALLNING.getCode())));
        }

        if (bedomning.getBehovAvLakareSpecialistKompetens() != null) {
            assertEquals(bedomning.getBehovAvLakareSpecialistKompetens(), xPath.evaluate(XPathExpressionsV3.BEDOMNING_SPECIALIST_XPATH));
        }
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

    private Node generateDocumentFor(String xml)
        throws ParserConfigurationException, JAXBException, SAXException, IOException, TransformerException {
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
