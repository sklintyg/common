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
package se.inera.intyg.common.ts_bas.v6.transformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
import se.inera.intyg.common.ts_parent.transformation.test.XPathEvaluator;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v1.Utlatande;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

class TsBasV3ToV1TransformerXpathTest {

  private static XslTransformer transformer;

  @BeforeAll
  static void setup() {
    transformer = new XslTransformer("xsl/V3ToV1.xsl");
  }

  @Test
  void testMaximaltIntyg()
      throws IOException,
          ParserConfigurationException,
          JAXBException,
          XPathExpressionException,
          SAXException,
          TransformerException {
    performTests(new ClassPathResource("v6/scenarios/rivtav3/valid-maximal.xml"));
  }

  @Test
  void testMinimaltIntyg()
      throws IOException,
          ParserConfigurationException,
          JAXBException,
          XPathExpressionException,
          SAXException,
          TransformerException {
    performTests(new ClassPathResource("v6/scenarios/rivtav3/valid-minimal.xml"));
  }

  private void performTests(ClassPathResource cpr)
      throws ParserConfigurationException,
          JAXBException,
          SAXException,
          IOException,
          TransformerException,
          XPathExpressionException {
    String xmlContent = Resources.toString(cpr.getURL(), StandardCharsets.UTF_8);
    String transformed = transformer.transform(xmlContent);

    // Create an xPath evaluator that operates on the transport model.
    XPathEvaluator xPathV1 = createXPathEvaluator(transformed);
    XPathEvaluator xPathV3 = createXPathEvaluatorV3(xmlContent);

    // Check utlatande against xpath
    assertEquals(
        "TSTRK1007", xPathV1.evaluate(XPathExpressionsV1.TYP_AV_UTLATANDE_XPATH), "UtlatandeTyp");

    // assertEquals("Utlatande-utgåva", xPathV3.evaluate(XPathExpressionsV3.TS_VERSION_XPATH),
    // xPathV1.evaluate(XPathExpressionsV1.TS_VERSION_XPATH) + "." +
    // xPathV1.evaluate(XPathExpressionsV1.TS_UTGAVA_XPATH));

    // Patient
    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.PATIENT_FORNAMN_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.INVANARE_FORNAMN_XPATH),
        "Patient förnamn");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.PATIENT_EFTERNAMN_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.INVANARE_EFTERNAMN_XPATH),
        "Patient efternamn");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.PATIENT_PERSONNUMMER_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.INVANARE_PERSONNUMMER_XPATH).replace("-", ""),
        "Patient personnummer");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.PATIENT_POSTADRESS_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.INVANARE_POSTADRESS_XPATH),
        "Patient postadress");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.PATIENT_POSTNUMMER_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.INVANARE_POSTNUMMER_XPATH),
        "Patient postnummer");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.PATIENT_POSTORT_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.INVANARE_POSTORT_XPATH),
        "Patient postort");

    // Signeringsdatum
    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SIGNERINGTIDPUNKT_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.SIGNERINGSDATUM_XPATH),
        "Signeringsdatum");

    // Skapad Av
    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SKAPAD_AV_BEFATTNING_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.SKAPAD_AV_BEFATTNING_XPATH),
        "Skapad av - befattningar");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH),
        "Skapad av - fullständigt namn");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SKAPAD_AV_HSAID_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.SKAPAD_AV_HSAID_XPATH),
        "Skapad av - hsa-id");

    if (!xPathV3.evaluate(XPathExpressionsV3.SKAPAD_AV_SPECIALISTKOMPETENS_XPATH).isEmpty()) {
      assertEquals(
          xPathV3.evaluate(XPathExpressionsV3.SKAPAD_AV_SPECIALISTKOMPETENS_XPATH),
          xPathV1.evaluate(XPathExpressionsV1.SKAPAD_AV_SPECIALISTKOMPETENS_BESKRVNING_XPATH),
          "Skapad av - specialitet");
    }

    // Vardenhet
    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.ENHET_ID_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.ENHET_ID_XPATH),
        "Enhet - enhetsid");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.ENHET_VARDINRATTNINGENS_NAMN_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.ENHET_VARDINRATTNINGENS_NAMN_XPATH),
        "Enhet - enhetsnamn");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.ENHET_POSTADRESS_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.ENHET_POSTADRESS_XPATH),
        "Enhet - postadress");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.ENHET_POSTNUMMER_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.ENHET_POSTNUMMER_XPATH),
        "Enhet - postnummer");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.ENHET_POSTORT_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.ENHET_POSTORT_XPATH),
        "Enhet - postort");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.ENHET_TELEFONNUMMER_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.ENHET_TELEFONNUMMER_XPATH),
        "Enhet - postort");

    // Vardgivare
    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.VARDGIVARE_ID_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.VARDGIVARE_ID_XPATH),
        "Enhet - vardgivare - id");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.VARDGIVARE_NAMN_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.VARDGIVARE_NAMN_XPATH),
        "Enhet - vardgivare - id");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.OVRIG_BESKRIVNING_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.OVRIG_BESKRIVNING_XPATH),
        "Övrig beskrivning");

    // IntygAvser
    // for (KorkortsbehorighetTsBas t : utlatande.getIntygAvser().getKorkortstyp()) {
    //
    // assertTrue(xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.INTYG_AVSER_TEMPLATE, IntygAvserKod.valueOf(t.value().value()).getCode())));
    // }

    // ID-kontroll
    // assertTrue(xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.ID_KONTROLL_TEMPLATE,
    //         XPathExpressionsV3.booleanXPath(XPathExpressionsV3.ID_KONTROLL_TEMPLATE))));

    /*
            // Aktiviteter
            boolean harGlasStyrkaOver8Dioptrier = utlatande.getSynfunktion().isHarGlasStyrkaOver8Dioptrier() == null ? false : utlatande.getSynfunktion()
                    .isHarGlasStyrkaOver8Dioptrier();
            assertEquals(
                    harGlasStyrkaOver8Dioptrier,
                    xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.AKTIVITET_FOREKOMST_TEMPLATE, "AKT17", harGlasStyrkaOver8Dioptrier)),
                    "8 dioptrier");

    */

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.AKTIVITET_FOREKOMST_TEMPLATE,
                "AKT15",
                xPathV3.evaluate(XPathExpressionsV3.ALKOHOL_HAR_VARDINSATS_XPATH))),
        "Har vårdinsats för missbruk");

    /*        assertTrue("provtagning narkotika",
    xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.AKTIVITET_FOREKOMST_TEMPLATE, "AKT14", xPathV3.evaluate(XPathExpressionsV3.ALKOHOL_HAR_VARDINSATS_PROVTAGNING_XPATH))));
    */

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.AKTIVITET_FOREKOMST_TEMPLATE,
                "AKT19",
                xPathV3.evaluate(XPathExpressionsV3.SJUKHUSVARD_XPATH))),
        "Sjukhusvård eller läkarkontakt");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SJUKHUSVARD_DATUM_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.VARD_PA_SJUKHUS_TID_XPATH),
        "Sjukhusvård eller läkarkontakt - datum");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SJUKHUSVARD_INRATTNING_XPATH),
        xPathV1.evaluate(XPathExpressionsV1.VARD_PA_SJUKHUS_VARDINRATTNING_XPATH),
        "Sjukhusvård eller läkarkontakt - plats");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SJUKHUSVARD_ANLEDNING_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.AKTIVITET_BESKRIVNING_TEMPLATE, "AKT19")),
        "Sjukhusvård eller läkarkontakt - anledning");

    // Observationer
    // Synobservationer
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "H53.4",
                xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_DEFEKT_XPATH))),
        "Synfältsdefekter");

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "H53.6",
                xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_NATTBLINDHET_XPATH))),
        "Nattblindhet");

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS1",
                xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_PROGRESSIV_XPATH))),
        "Progressiv ögonsjukdom");

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "H53.2",
                xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_DIPLOPI_XPATH))),
        "Diplopi");

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "H55.9",
                xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_NYSTAGMUS_XPATH))),
        "Nystagmus");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_UTAN_KORREKTION_HOGER_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "24028007")),
        "Ej korrigerad synskärpa höger");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_MED_KORREKTION_HOGER_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "24028007")),
        "Korrigerad synskärpa höger");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_UTAN_KORREKTION_VANSTER_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "7771000")),
        "Ej korrigerad synskärpa vänster");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_MED_KORREKTION_VANSTER_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "7771000")),
        "Korrigerad synskärpa vänster");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_UTAN_KORREKTION_BINOKULART_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "51440002")),
        "Ej korrigerad synskärpa binokulärt");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_MED_KORREKTION_BINOKULART_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "51440002")),
        "Korrigerad synskärpa binokulärt");

    assertEquals(
        Boolean.valueOf(xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_KONTAKTLINS_HOGER_XPATH)),
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_CODE_LATERALITET,
                "285049007",
                "24028007")),
        "Kontaktlinser höger");

    assertEquals(
        Boolean.valueOf(xPathV3.evaluate(XPathExpressionsV3.SYNFUNKTION_KONTAKTLINS_VANSTER_XPATH)),
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_CODE_LATERALITET, "285049007", "7771000")),
        "Kontaktlinser vänster");

    // Hörsel & balans
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS2",
                xPathV3.evaluate(XPathExpressionsV3.HORSEL_BALANS_XPATH))),
        "Balansrubbningar");

    // Rörelseorganens förmåga
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS4",
                xPathV3.evaluate(XPathExpressionsV3.RORELSEORGAN_SJUKDOM_XPATH))),
        "Har rörelsebegränsning");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.RORELSEORGAN_BESKRICNING_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE,
                "OBS4",
                xPathV3.evaluate(XPathExpressionsV3.RORELSEORGAN_BESKRICNING_XPATH))),
        "Rörelsebegränsning beskrivning");

    // Hjärt & kärlsjukdom
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS6",
                xPathV3.evaluate(XPathExpressionsV3.HJARTKARL_RISK_FUNKTION_XPATH))),
        "Risk försämrad hjärnfunktion");

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS8",
                xPathV3.evaluate(XPathExpressionsV3.HJARTKARL_SKADA_XPATH))),
        "Tecken på hjärnskada");

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS7",
                xPathV3.evaluate(XPathExpressionsV3.HJARTKARL_RISK_STROKE_XPATH))),
        "Riskfaktorer för stroke");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.HJARTKARL_RISK_STROKE_BESKRIVNING_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE,
                "OBS7",
                xPathV3.evaluate(XPathExpressionsV3.HJARTKARL_RISK_STROKE_BESKRIVNING_XPATH))),
        "Riskfaktorer stroke - beskrivning");

    // Diabetes
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "73211009",
                xPathV3.evaluate(XPathExpressionsV3.DIABETES_XPATH))),
        "Har diabetes");

    /*        if (diabetes.isHarDiabetes()) {
        assertTrue("Diabetes typ2",
                xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "E11")));

        if (diabetes.getDiabetesTyp().name().equals("TYP1")) {
            assertTrue("Diabetes typ1",
                    xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, xPathV3.evaluate(XPathExpressionsV3.DIABETES_TYPE_XPATH))));
        }
        else if (diabetes.getDiabetesTyp().name().equals("TYP2")) {
            assertTrue("Diabetes typ2",
                    xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "E11")));
        }

        if (diabetes.isHarBehandlingKost() != null) {
            assertTrue(
                    "Diabetes kostbehandling",
                    xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "OBS9",
                            diabetes.isHarBehandlingKost())));
        }

        if (diabetes.isHarBehandlingInsulin() != null) {
            assertTrue(
                    "Diabetes insulinbehandling",
                    xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "170747006", diabetes.isHarBehandlingInsulin())));
        }

        if (diabetes.isHarBehandlingTabletter() != null) {
            assertTrue(
                    "Diabetes tablettbehandling",
                    xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "170746002", diabetes.isHarBehandlingTabletter())));
        }
    }*/

    // Neurologi
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "407624006",
                xPathV3.evaluate(XPathExpressionsV3.NEUROLOGISKA_SJUKDOMAR_XPATH))),
        "Tecken på neurologisk sjukdom");

    // Medvetandestörning
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "G40.9",
                xPathV3.evaluate(XPathExpressionsV3.MEDVETANDESTORNING_XPATH))),
        "Har medvetandestörning");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.MEDVETANDESTORNING_BESKRIVNING_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE,
                "G40.9",
                xPathV3.evaluate(XPathExpressionsV3.MEDVETANDESTORNING_BESKRIVNING_XPATH))),
        "Medvetandestörning - beskrivning");

    // Njursjukdom
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS11",
                xPathV3.evaluate(XPathExpressionsV3.NJURSJUKDOM_XPATH))),
        "Har njursjukdom");

    // Sviktande kognitiv förmåga
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS12",
                xPathV3.evaluate(XPathExpressionsV3.DEMENS_XPATH))),
        "kognitiv förmåga");

    // Somn vakenhet
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS13",
                xPathV3.evaluate(XPathExpressionsV3.SOMN_OCH_VAKENHETSSTORNINGAR_XPATH))),
        "Somn vakenhet");

    // alkohol och narkotika
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS14",
                xPathV3.evaluate(XPathExpressionsV3.ALKOHOL_TECKEN_MISSBRUK_XPATH))),
        "Tecken på missbruk");

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS15",
                xPathV3.evaluate(XPathExpressionsV3.ALKOHOL_ORDINERAT_XPATH))),
        "Läkemedelsbruk");

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.ALKOHOL_ORDINERAT_LAKEMEDEL_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE, "OBS15")),
        "Läkemedelsbruk - beskrivning");

    // Psykisk sjukdom
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS16",
                xPathV3.evaluate(XPathExpressionsV3.PSYKISKA_SJUKDOMAR_XPATH))),
        "Psykisk sjukdom");

    // Utvecklingsstörning
    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "129104009",
                xPathV3.evaluate(XPathExpressionsV3.UTVECKLINGSSTORNING_PSYKISK_XPATH))),
        "Har psykisk utvecklingsstörning");

    assertTrue(
        xPathV1.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS17",
                xPathV3.evaluate(XPathExpressionsV3.UTVECKLINGSSTORNING_ANDRA_XPATH))),
        "Har ADHD eller damp");

    // Stadigvarande medicinering
    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.OVRIG_MEDICIN_BESKRIVNING_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE, "OBS18")),
        "Har stadigvarande medicinering- beskrivning");
    /*
    // Rekommendationer
    assertTrue("Rekommendation REK8",
            xPathV1.evaluate(new BooleanXPathExpression("utlatande/p:rekommendation/p:rekommendationskod/@code = 'REK8'")));

    for (KorkortsbehorighetTsBas t : utlatande.getBedomning().getKorkortstyp()) {
        KorkortsbehorighetKod k = KorkortsbehorighetKod.valueOf(t.value().value());
        assertTrue(String.format("Rekommendationsvärde %s", k.getCode()),
                xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.REKOMMENDATION_VARDE_TEMPLATE, k.getCode())));
    }
    if (utlatande.getBedomning().isKanInteTaStallning() !=  null && utlatande.getBedomning().isKanInteTaStallning()) {
        assertTrue("Rekommendationsvärde Kan inte ta ställning (VAR11)",
                xPathV1.evaluate(XPathExpressionsV1.booleanXPath(XPathExpressionsV1.REKOMMENDATION_VARDE_TEMPLATE, KorkortsbehorighetKod.KANINTETASTALLNING.getCode())));
    }
    */

    assertEquals(
        xPathV3.evaluate(XPathExpressionsV3.BEDOMNING_SPECIALIST_XPATH),
        xPathV1.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.REKOMMENDATION_BESKRIVNING_TEMPLATE, "REK9")),
        "Bedömning beskrivning");
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
    namespaces.bindNamespaceUri("p2", "urn:riv:clinicalprocess:healthcond:certificate:ts-bas:1");
    xPath.setNamespaceContext(namespaces);
    Node document = generateDocumentFor(xml);

    return new XPathEvaluator(xPath, document);
  }

  private XPathEvaluator createXPathEvaluatorV3(String xml)
      throws ParserConfigurationException,
          JAXBException,
          SAXException,
          IOException,
          TransformerException {

    SimpleNamespaceContext namespaces = new SimpleNamespaceContext();
    namespaces.bindDefaultNamespaceUri(
        "urn:riv:clinicalprocess:healthcond:certificate:RegisterCertificateResponder:3");
    namespaces.bindNamespaceUri("ns3", "urn:riv:clinicalprocess:healthcond:certificate:3");
    namespaces.bindNamespaceUri("ns2", "urn:riv:clinicalprocess:healthcond:certificate:types:3");

    XPath xPath = XPathFactory.newInstance().newXPath();
    xPath.setNamespaceContext(namespaces);

    Node document = generateDocumentForV3(xml);

    return new XPathEvaluator(xPath, document);
  }

  private Node generateDocumentForV3(String xml)
      throws ParserConfigurationException,
          JAXBException,
          SAXException,
          IOException,
          TransformerException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = factory.newDocumentBuilder();
    Node node = parser.newDocument();

    InputStream is = new ByteArrayInputStream(xml.getBytes());

    se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType
        register =
            JAXB.unmarshal(
                is,
                se.riv
                    .clinicalprocess
                    .healthcond
                    .certificate
                    .registerCertificate
                    .v3
                    .RegisterCertificateType
                    .class);

    JAXBElement<Intyg> jaxbElement =
        new JAXBElement<>(new QName("intyg"), Intyg.class, register.getIntyg());
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

    return node;
  }
}
