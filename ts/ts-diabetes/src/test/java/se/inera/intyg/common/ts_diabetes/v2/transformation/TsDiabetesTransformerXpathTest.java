/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v2.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.AKTIVITET_FOREKOMST_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.ENHET_ID_XPATH;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.ENHET_POSTADRESS_XPATH;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.ENHET_POSTNUMMER_XPATH;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.ENHET_VARDINRATTNINGENS_NAMN_XPATH;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.ID_KONTROLL_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.INTYG_AVSER_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.OBSERVATION_BESKRIVNING_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.OBSERVATION_FOREKOMST_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.OBSERVATION_FOREKOMST_VARDE_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.OBSERVATION_TID_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.OBSERVATION_VARDE_CODE_LATERALITET;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.REKOMMENDATION_BESKRIVNING_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.REKOMMENDATION_VARDE_BOOL_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.REKOMMENDATION_VARDE_CODE_TEMPLATE;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.VARDGIVARE_ID_XPATH;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.VARDGIVARE_NAMN_XPATH;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.booleanXPath;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.dateXPath;
import static se.inera.intyg.common.ts_diabetes.v2.transformation.XPathExpressions.stringXPath;

import com.google.common.io.Resources;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.ts_parent.codes.IntygAvserKod;
import se.inera.intyg.common.ts_parent.codes.KorkortsbehorighetKod;
import se.inera.intyg.common.ts_parent.transformation.test.BooleanXPathExpression;
import se.inera.intyg.common.ts_parent.transformation.test.StringXPathExpression;
import se.inera.intyg.common.ts_parent.transformation.test.XPathEvaluator;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.Diabetes;
import se.inera.intygstjanster.ts.services.v1.Hypoglykemier;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsDiabetes;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynfunktionDiabetes;
import se.inera.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.inera.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v1.Utlatande;

class TsDiabetesTransformerXpathTest {

  private static XslTransformer transformer;

  @BeforeAll
  static void setup() {
    transformer = new XslTransformer("xsl/transform-ts-diabetes.xsl");
  }

  @Test
  void testMaximaltIntyg()
      throws IOException,
          ParserConfigurationException,
          JAXBException,
          XPathExpressionException,
          SAXException,
          TransformerException {
    performTest(new ClassPathResource("v2/scenarios/transport/transform-valid-maximal.xml"));
  }

  @Test
  void testMinimaltIntyg()
      throws IOException,
          ParserConfigurationException,
          JAXBException,
          XPathExpressionException,
          SAXException,
          TransformerException {
    performTest(new ClassPathResource("v2/scenarios/transport/transform-valid-minimal.xml"));
  }

  private void performTest(ClassPathResource cpr)
      throws ParserConfigurationException,
          JAXBException,
          SAXException,
          IOException,
          TransformerException,
          XPathExpressionException {
    String xmlContent = Resources.toString(cpr.getURL(), StandardCharsets.UTF_8);
    TSDiabetesIntyg utlatande =
        JAXB.unmarshal(cpr.getFile(), RegisterTSDiabetesType.class).getIntyg();
    String transformed = transformer.transform(xmlContent);
    // Create an xPath evaluator that operates on the transport model.
    XPathEvaluator xPath = createXPathEvaluator(transformed);

    // Check utlatande against xpath
    assertEquals(
        "TSTRK1031", xPath.evaluate(XPathExpressions.TYP_AV_UTLATANDE_XPATH), "UtlatandeTyp");

    assertEquals(
        utlatande.getUtgava(),
        xPath.evaluate(XPathExpressions.TS_UTGAVA_XPATH),
        "Utlatande-utgåva");

    assertEquals(
        utlatande.getVersion(),
        xPath.evaluate(XPathExpressions.TS_VERSION_XPATH),
        "Utlatande-version");

    // Patient
    Patient patient = utlatande.getGrundData().getPatient();

    assertEquals(
        patient.getFornamn(),
        xPath.evaluate(XPathExpressions.INVANARE_FORNAMN_XPATH),
        "Patient förnamn");

    assertEquals(
        patient.getEfternamn(),
        xPath.evaluate(XPathExpressions.INVANARE_EFTERNAMN_XPATH),
        "Patient efternamn");

    assertEquals(
        patient.getPersonId().getExtension(),
        xPath.evaluate(XPathExpressions.INVANARE_PERSONNUMMER_XPATH),
        "Patient personnummer");

    assertEquals(
        patient.getPostadress(),
        xPath.evaluate(XPathExpressions.INVANARE_POSTADRESS_XPATH),
        "Patient postadress");

    assertEquals(
        patient.getPostnummer(),
        xPath.evaluate(XPathExpressions.INVANARE_POSTNUMMER_XPATH),
        "Patient postnummer");

    assertEquals(
        patient.getPostort(),
        xPath.evaluate(XPathExpressions.INVANARE_POSTORT_XPATH),
        "Patient postnummer");

    // Signeringsdatum
    assertEquals(
        utlatande.getGrundData().getSigneringsTidstampel(),
        xPath.evaluate(XPathExpressions.SIGNERINGSDATUM_XPATH),
        "Signeringsdatum");

    // Skapad Av
    SkapadAv skapadAv = utlatande.getGrundData().getSkapadAv();

    if (!skapadAv.getBefattningar().isEmpty()) {
      assertEquals(
          skapadAv.getBefattningar().get(0),
          xPath.evaluate(XPathExpressions.SKAPAD_AV_BEFATTNING_XPATH),
          "Skapad av - befattningar");
    }

    assertEquals(
        skapadAv.getFullstandigtNamn(),
        xPath.evaluate(XPathExpressions.SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH),
        "Skapad av - fullständigt namn");

    assertEquals(
        skapadAv.getPersonId().getExtension(),
        xPath.evaluate(XPathExpressions.SKAPAD_AV_HSAID_XPATH),
        "Skapad av - hsa-id");

    if (!skapadAv.getSpecialiteter().isEmpty()) {
      assertEquals(
          skapadAv.getSpecialiteter().get(0),
          xPath.evaluate(XPathExpressions.SKAPAD_AV_SPECIALISTKOMPETENS_BESKRVNING_XPATH),
          "Skapad av - specialitet");
    }

    // Vardenhet
    Vardenhet vardenhet = skapadAv.getVardenhet();
    assertEquals(
        vardenhet.getEnhetsId().getExtension(), xPath.evaluate(ENHET_ID_XPATH), "Enhet - enhetsid");

    assertEquals(
        vardenhet.getEnhetsnamn(),
        xPath.evaluate(ENHET_VARDINRATTNINGENS_NAMN_XPATH),
        "Enhet - enhetsnamn");

    assertEquals(
        vardenhet.getPostadress(), xPath.evaluate(ENHET_POSTADRESS_XPATH), "Enhet - postadress");

    assertEquals(
        vardenhet.getPostnummer(), xPath.evaluate(ENHET_POSTNUMMER_XPATH), "Enhet - postnummer");

    assertEquals(
        vardenhet.getPostort(),
        xPath.evaluate(XPathExpressions.ENHET_POSTORT_XPATH),
        "Enhet - postort");

    assertEquals(
        vardenhet.getTelefonnummer(),
        xPath.evaluate(XPathExpressions.ENHET_TELEFONNUMMER_XPATH),
        "Enhet - postort");

    // Vardgivare
    assertEquals(
        vardenhet.getVardgivare().getVardgivarid().getExtension(),
        xPath.evaluate(VARDGIVARE_ID_XPATH),
        "Enhet - vardgivare - id");

    assertEquals(
        vardenhet.getVardgivare().getVardgivarnamn(),
        xPath.evaluate(VARDGIVARE_NAMN_XPATH),
        "Enhet - vardgivare - id");

    // IntygAvser
    for (KorkortsbehorighetTsDiabetes t : utlatande.getIntygAvser().getKorkortstyp()) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  INTYG_AVSER_TEMPLATE, IntygAvserKod.valueOf(t.value().value()).getCode())));
    }

    // ID-kontroll
    assertTrue(
        xPath.evaluate(
            booleanXPath(
                ID_KONTROLL_TEMPLATE, utlatande.getIdentitetStyrkt().getIdkontroll().value())));

    // Aktiviteter
    if (utlatande.getHypoglykemier() != null
        && utlatande.getHypoglykemier().isGenomforEgenkontrollBlodsocker() != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  AKTIVITET_FOREKOMST_TEMPLATE,
                  "308113006",
                  utlatande.getHypoglykemier().isGenomforEgenkontrollBlodsocker())),
          "Egenkontroll av blodsocker");
    }

    SynfunktionDiabetes synfunktion = utlatande.getSynfunktion();
    if (synfunktion != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  "utlatande/p:aktivitet/p:aktivitetskod/@code='86944008'",
                  synfunktion.isFinnsSynfaltsprovning())),
          "Synfältsprövning");

      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  "utlatande/p:aktivitet/p:aktivitetskod/@code='AKT18'",
                  synfunktion.isFinnsProvningOgatsRorlighet())),
          "Prövning av ögats rörlighet");
    }
    // Observationer
    // Hypoglykemier
    Hypoglykemier hypoglykemier = utlatande.getHypoglykemier();

    assertTrue(
        xPath.evaluate(
            booleanXPath(
                OBSERVATION_FOREKOMST_TEMPLATE, "OBS19", hypoglykemier.isHarKunskapOmAtgarder())),
        "Kunskap om åtgärder");

    assertTrue(
        xPath.evaluate(
            booleanXPath(
                OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS20",
                hypoglykemier.isHarTeckenNedsattHjarnfunktion())),
        "Hypoglykemi nedsatt hjärnfunktion");

    if (hypoglykemier.isHarTeckenNedsattHjarnfunktion()) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE,
                  "OBS21",
                  hypoglykemier.isSaknarFormagaKannaVarningstecken())),
          "Saknar känna förmåga varningstecken");

      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE,
                  "OBS22",
                  hypoglykemier.isHarAllvarligForekomst())),
          "Allvarlig hypoglykemi senaste året");
      assertEquals(
          hypoglykemier.getAllvarligForekomstBeskrivning() == null
              ? ""
              : hypoglykemier.getAllvarligForekomstBeskrivning(),
          xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS22")),
          "Allvarlig hypoglykemi senaste året - beskrivning");

      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE,
                  "OBS23",
                  hypoglykemier.isHarAllvarligForekomstTrafiken())),
          "Allvarlig förekomst i trafiken senaste året");
      assertEquals(
          hypoglykemier.getAllvarligForekomstTrafikBeskrivning() == null
              ? ""
              : hypoglykemier.getAllvarligForekomstTrafikBeskrivning(),
          xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS23")),
          "Allvarlig förekomst trafiken senaste året - beskrivning");
    }

    if (hypoglykemier.isHarAllvarligForekomstVakenTid() != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE,
                  "OBS24",
                  hypoglykemier.isHarAllvarligForekomstVakenTid())),
          "Allvarlig förekomst vaken tid senaste året");
      assertEquals(
          hypoglykemier.getAllvarligForekomstVakenTidAr() == null
              ? ""
              : hypoglykemier.getAllvarligForekomstVakenTidAr(),
          xPath.evaluate(dateXPath(OBSERVATION_TID_TEMPLATE, "yyyy-MM-dd", "OBS24")),
          "Allvarlig förekomst vaken tid senaste året - observationstid");
    }
    // Syn
    if (synfunktion != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE,
                  "OBS25",
                  synfunktion.isSynfaltsprovningUtanAnmarkning())),
          "Synfältsprovning utan anmärkning");

      assertTrue(
          xPath.evaluate(
              booleanXPath(OBSERVATION_FOREKOMST_TEMPLATE, "H53.2", synfunktion.isHarDiplopi())),
          "Diplopi");

      SynskarpaUtanKorrektion utanKorr = synfunktion.getSynskarpaUtanKorrektion();

      SynskarpaMedKorrektion medKorr = synfunktion.getSynskarpaMedKorrektion();

      assertEquals(
          String.valueOf(utanKorr.getHogerOga()),
          xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "24028007")),
          "Ej korrigerad synskärpa höger");

      assertEquals(
          String.valueOf(medKorr.getHogerOga()),
          xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "24028007")),
          "Korrigerad synskärpa höger");

      assertEquals(
          String.valueOf(utanKorr.getVansterOga()),
          xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "7771000")),
          "Ej korrigerad synskärpa vänster");

      assertEquals(
          String.valueOf(medKorr.getVansterOga()),
          xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "7771000")),
          "Korrigerad synskärpa vänster");

      assertEquals(
          String.valueOf(utanKorr.getBinokulart()),
          xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "51440002")),
          "Ej korrigerad synskärpa binokulärt");

      assertEquals(
          String.valueOf(medKorr.getBinokulart()),
          xPath.evaluate(stringXPath(OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "51440002")),
          "Korrigerad synskärpa binokulärt");
    }
    // Diabetes
    Diabetes diabetes = utlatande.getDiabetes();

    if (diabetes.getDiabetesTyp().get(0).equals("TYP1")) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE,
                  "E10",
                  diabetes.getDiabetesTyp().get(0).equals("TYP1"))),
          "Diabetes typ1");
    } else if (diabetes.getDiabetesTyp().get(0).equals("TYP2")) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE,
                  "E11",
                  diabetes.getDiabetesTyp().get(0).equals("TYP2"))),
          "Diabetes typ2");
      assertEquals(
          diabetes.getDebutArDiabetes(),
          xPath.evaluate(
              new StringXPathExpression(
                  "utlatande/p:observation/p:observationsperiod[(parent::p:observation/p:observationskod/@code='E11')]")),
          "Diabetes typ2 från år");
    }

    if (diabetes.isHarBehandlingKost() != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE, "170745003", diabetes.isHarBehandlingKost())),
          "Diabetes kostbehandling");
    }

    if (diabetes.isHarBehandlingInsulin() != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_TEMPLATE, "170747006", diabetes.isHarBehandlingInsulin())),
          "Diabetes insulinbehandling");

      if (diabetes.getInsulinBehandlingSedanAr() != null) {
        assertEquals(
            diabetes.getInsulinBehandlingSedanAr(),
            xPath.evaluate(
                new StringXPathExpression(
                    "utlatande/p:observation/p:observationsperiod[(parent::p:observation/p:observationskod/@code='170747006')]")),
            "Insulin sedan år");
      }
    }

    if (diabetes.isHarBehandlingTabletter() != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  OBSERVATION_FOREKOMST_VARDE_TEMPLATE,
                  "170746002",
                  diabetes.isHarBehandlingTabletter())),
          "Diabetes tablettbehandling");
    }

    if (diabetes.getAnnanBehandlingBeskrivning() != null) {
      assertEquals(
          diabetes.getAnnanBehandlingBeskrivning(),
          xPath.evaluate(stringXPath(OBSERVATION_BESKRIVNING_TEMPLATE, "OBS10")),
          "Diabetes annan behandling beskrivning");
    }

    // Rekommendationer
    assertTrue(
        xPath.evaluate(
            new BooleanXPathExpression(
                "utlatande/p:rekommendation/p:rekommendationskod/@code = 'REK8'")),
        "Rekommendation REK8");

    for (KorkortsbehorighetTsDiabetes t : utlatande.getBedomning().getKorkortstyp()) {
      KorkortsbehorighetKod k = KorkortsbehorighetKod.valueOf(t.value().value());
      assertTrue(
          xPath.evaluate(booleanXPath(REKOMMENDATION_VARDE_CODE_TEMPLATE, k.getCode())),
          String.format("Rekommendationsvärde %s", k.getCode()));
    }
    if (utlatande.getBedomning().isKanInteTaStallning() != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  REKOMMENDATION_VARDE_CODE_TEMPLATE,
                  KorkortsbehorighetKod.KANINTETASTALLNING.getCode())),
          "Rekommendationsvärde Kan inte ta ställning (VAR11)");
    }

    if (utlatande.getBedomning().getBehovAvLakareSpecialistKompetens() != null) {
      assertEquals(
          utlatande.getBedomning().getBehovAvLakareSpecialistKompetens(),
          xPath.evaluate(stringXPath(REKOMMENDATION_BESKRIVNING_TEMPLATE, "REK9")),
          String.format(
              "REK9 med beskrivning  %s",
              utlatande.getBedomning().getBehovAvLakareSpecialistKompetens()));
    }

    if (utlatande.getBedomning().isLamplighetInnehaBehorighetSpecial() != null) {
      assertTrue(
          xPath.evaluate(
              booleanXPath(
                  REKOMMENDATION_VARDE_BOOL_TEMPLATE,
                  "REK10",
                  utlatande.getBedomning().isLamplighetInnehaBehorighetSpecial())),
          "Rekommendation lämplighet inneha behörighet");
    }

    // Bilaga
    assertTrue(
        xPath.evaluate(
            new BooleanXPathExpression(
                String.format(
                    "utlatande/p2:bilaga/p:forekomst='%s'",
                    utlatande.isSeparatOgonLakarintygKommerSkickas()))),
        "Bilaga");
  }

  private XPathEvaluator createXPathEvaluator(String xml)
      throws ParserConfigurationException,
          JAXBException,
          SAXException,
          IOException,
          TransformerException {
    XPath xPath = XPathFactory.newInstance().newXPath();
    SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
    namespaces.bindDefaultNamespaceUri(
        "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:1");
    namespaces.bindNamespaceUri("ns2", "urn:riv:clinicalprocess:healthcond:certificate:types:1");
    namespaces.bindNamespaceUri("p", "urn:riv:clinicalprocess:healthcond:certificate:1");
    namespaces.bindNamespaceUri(
        "p2", "urn:riv:clinicalprocess:healthcond:certificate:ts-diabetes:1");
    xPath.setNamespaceContext(namespaces);
    Node document = generateDocumentFor(xml);

    return new XPathEvaluator(xPath, document);
  }

  private Node generateDocumentFor(String xml)
      throws ParserConfigurationException,
          JAXBException,
          SAXException,
          IOException,
          TransformerException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Node node = parser.newDocument();

    InputStream is = new ByteArrayInputStream(xml.getBytes());

    RegisterCertificateType register = JAXB.unmarshal(is, RegisterCertificateType.class);

    JAXBElement<Utlatande> jaxbElement =
        new JAXBElement<>(new QName("ns3:utlatande"), Utlatande.class, register.getUtlatande());
    JAXBContext context = JAXBContext.newInstance(Utlatande.class);
    context.createMarshaller().marshal(jaxbElement, node);

    // // Output the Document
    // TransformerFactory tf = TransformerFactory.newInstance();
    // Transformer t = tf.newTransformer();
    // DOMSource source = new DOMSource(node);
    // StreamResult result = new StreamResult(System.out);
    // t.transform(source, result);
    return node;
  }
}
