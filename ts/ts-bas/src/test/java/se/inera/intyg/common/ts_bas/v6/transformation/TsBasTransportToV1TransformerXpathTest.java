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
import se.inera.intyg.common.ts_parent.transformation.test.XPathEvaluator;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.AlkoholNarkotikaLakemedel;
import se.inera.intygstjanster.ts.services.v1.DiabetesTypBas;
import se.inera.intygstjanster.ts.services.v1.HjartKarlSjukdomar;
import se.inera.intygstjanster.ts.services.v1.HorselBalanssinne;
import se.inera.intygstjanster.ts.services.v1.KorkortsbehorighetTsBas;
import se.inera.intygstjanster.ts.services.v1.Medvetandestorning;
import se.inera.intygstjanster.ts.services.v1.OvrigMedicinering;
import se.inera.intygstjanster.ts.services.v1.Patient;
import se.inera.intygstjanster.ts.services.v1.RorelseorganenFunktioner;
import se.inera.intygstjanster.ts.services.v1.SkapadAv;
import se.inera.intygstjanster.ts.services.v1.SynskarpaMedKorrektion;
import se.inera.intygstjanster.ts.services.v1.SynskarpaUtanKorrektion;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;
import se.inera.intygstjanster.ts.services.v1.Utvecklingsstorning;
import se.inera.intygstjanster.ts.services.v1.Vardenhet;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v1.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v1.Utlatande;

class TsBasTransportToV1TransformerXpathTest {

  private static XslTransformer transformer;

  @BeforeAll
  static void setup() {
    transformer = new XslTransformer("xsl/transportToV1.xsl");
  }

  @Test
  void testMaximaltIntyg()
      throws IOException,
          ParserConfigurationException,
          JAXBException,
          XPathExpressionException,
          SAXException,
          TransformerException {
    performTests(new ClassPathResource("v6/scenarios/transport/valid-maximal.xml"));
  }

  @Test
  void testMinimaltIntyg()
      throws IOException,
          ParserConfigurationException,
          JAXBException,
          XPathExpressionException,
          SAXException,
          TransformerException {
    performTests(new ClassPathResource("v6/scenarios/transport/valid-minimal.xml"));
  }

  private void performTests(ClassPathResource cpr)
      throws ParserConfigurationException,
          JAXBException,
          SAXException,
          IOException,
          TransformerException,
          XPathExpressionException {
    String xmlContent = Resources.toString(cpr.getURL(), StandardCharsets.UTF_8);
    TSBasIntyg utlatande = JAXB.unmarshal(cpr.getFile(), RegisterTSBasType.class).getIntyg();
    String transformed = transformer.transform(xmlContent);

    // Create an xPath evaluator that operates on the transport model.
    XPathEvaluator xPath = createXPathEvaluator(transformed);

    // Check utlatande against xpath
    assertEquals(
        "TSTRK1007", xPath.evaluate(XPathExpressionsV1.TYP_AV_UTLATANDE_XPATH), "UtlatandeTyp");

    assertEquals(
        utlatande.getUtgava(),
        xPath.evaluate(XPathExpressionsV1.TS_UTGAVA_XPATH),
        "Utlatande-utgåva");

    assertEquals(
        utlatande.getVersion(),
        xPath.evaluate(XPathExpressionsV1.TS_VERSION_XPATH),
        "Utlatande-utgåva");

    // Patient
    Patient patient = utlatande.getGrundData().getPatient();

    assertEquals(
        patient.getFornamn(),
        xPath.evaluate(XPathExpressionsV1.INVANARE_FORNAMN_XPATH),
        "Patient förnamn");

    assertEquals(
        patient.getEfternamn(),
        xPath.evaluate(XPathExpressionsV1.INVANARE_EFTERNAMN_XPATH),
        "Patient efternamn");

    assertEquals(
        patient.getPersonId().getExtension(),
        xPath.evaluate(XPathExpressionsV1.INVANARE_PERSONNUMMER_XPATH),
        "Patient personnummer");

    assertEquals(
        patient.getPostadress(),
        xPath.evaluate(XPathExpressionsV1.INVANARE_POSTADRESS_XPATH),
        "Patient postadress");

    assertEquals(
        patient.getPostnummer(),
        xPath.evaluate(XPathExpressionsV1.INVANARE_POSTNUMMER_XPATH),
        "Patient postnummer");

    assertEquals(
        patient.getPostort(),
        xPath.evaluate(XPathExpressionsV1.INVANARE_POSTORT_XPATH),
        "Patient postort");

    // Signeringsdatum
    assertEquals(
        utlatande.getGrundData().getSigneringsTidstampel(),
        xPath.evaluate(XPathExpressionsV1.SIGNERINGSDATUM_XPATH),
        "Signeringsdatum");

    // Skapad Av
    SkapadAv skapadAv = utlatande.getGrundData().getSkapadAv();

    if (!skapadAv.getBefattningar().isEmpty()) {
      assertEquals(
          skapadAv.getBefattningar().get(0),
          xPath.evaluate(XPathExpressionsV1.SKAPAD_AV_BEFATTNING_XPATH),
          "Skapad av - befattningar");
    }

    assertEquals(
        skapadAv.getFullstandigtNamn(),
        xPath.evaluate(XPathExpressionsV1.SKAPAD_AV_NAMNFORTYDLIGANDE_XPATH),
        "Skapad av - fullständigt namn");

    assertEquals(
        skapadAv.getPersonId().getExtension(),
        xPath.evaluate(XPathExpressionsV1.SKAPAD_AV_HSAID_XPATH),
        "Skapad av - hsa-id");

    if (!skapadAv.getSpecialiteter().isEmpty()) {
      assertEquals(
          skapadAv.getSpecialiteter().get(0),
          xPath.evaluate(XPathExpressionsV1.SKAPAD_AV_SPECIALISTKOMPETENS_BESKRVNING_XPATH),
          "Skapad av - specialitet");
    }

    // Vardenhet
    Vardenhet vardenhet = skapadAv.getVardenhet();
    assertEquals(
        vardenhet.getEnhetsId().getExtension(),
        xPath.evaluate(XPathExpressionsV1.ENHET_ID_XPATH),
        "Enhet - enhetsid");

    assertEquals(
        vardenhet.getEnhetsnamn(),
        xPath.evaluate(XPathExpressionsV1.ENHET_VARDINRATTNINGENS_NAMN_XPATH),
        "Enhet - enhetsnamn");

    assertEquals(
        vardenhet.getPostadress(),
        xPath.evaluate(XPathExpressionsV1.ENHET_POSTADRESS_XPATH),
        "Enhet - postadress");

    assertEquals(
        vardenhet.getPostnummer(),
        xPath.evaluate(XPathExpressionsV1.ENHET_POSTNUMMER_XPATH),
        "Enhet - postnummer");

    assertEquals(
        vardenhet.getPostort(),
        xPath.evaluate(XPathExpressionsV1.ENHET_POSTORT_XPATH),
        "Enhet - postort");

    assertEquals(
        vardenhet.getTelefonnummer(),
        xPath.evaluate(XPathExpressionsV1.ENHET_TELEFONNUMMER_XPATH),
        "Enhet - postort");

    // Vardgivare
    assertEquals(
        vardenhet.getVardgivare().getVardgivarid().getExtension(),
        xPath.evaluate(XPathExpressionsV1.VARDGIVARE_ID_XPATH),
        "Enhet - vardgivare - id");

    assertEquals(
        vardenhet.getVardgivare().getVardgivarnamn(),
        xPath.evaluate(XPathExpressionsV1.VARDGIVARE_NAMN_XPATH),
        "Enhet - vardgivare - id");

    // IntygAvser
    for (KorkortsbehorighetTsBas t : utlatande.getIntygAvser().getKorkortstyp()) {
      assertTrue(
          xPath.evaluate(
              XPathExpressionsV1.booleanXPath(
                  XPathExpressionsV1.INTYG_AVSER_TEMPLATE,
                  IntygAvserKod.valueOf(t.value().value()).getCode())));
    }

    // ID-kontroll
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.ID_KONTROLL_TEMPLATE,
                utlatande.getIdentitetStyrkt().getIdkontroll().value())));

    // Aktiviteter
    boolean harGlasStyrkaOver8Dioptrier =
        utlatande.getSynfunktion().isHarGlasStyrkaOver8Dioptrier() == null
            ? false
            : utlatande.getSynfunktion().isHarGlasStyrkaOver8Dioptrier();
    assertEquals(
        harGlasStyrkaOver8Dioptrier,
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.AKTIVITET_FOREKOMST_TEMPLATE,
                "AKT17",
                harGlasStyrkaOver8Dioptrier)),
        "8 dioptrier");

    boolean harVardInsats = utlatande.getAlkoholNarkotikaLakemedel().isHarVardinsats();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.AKTIVITET_FOREKOMST_TEMPLATE, "AKT15", harVardInsats)),
        "Har vårdinsats för missbruk");

    Boolean provtagningBehovs =
        utlatande.getAlkoholNarkotikaLakemedel().isHarVardinsatsProvtagningBehov();
    if (provtagningBehovs != null) {
      assertTrue(
          xPath.evaluate(
              XPathExpressionsV1.booleanXPath(
                  XPathExpressionsV1.AKTIVITET_FOREKOMST_TEMPLATE, "AKT14", provtagningBehovs)),
          "provtagning narkotika");
    }

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.AKTIVITET_FOREKOMST_TEMPLATE,
                "AKT19",
                utlatande.getSjukhusvard().isHarSjukhusvardEllerLakarkontakt())),
        "Sjukhusvård eller läkarkontakt");

    if (utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktDatum() != null) {
      assertEquals(
          utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktDatum(),
          xPath.evaluate(XPathExpressionsV1.VARD_PA_SJUKHUS_TID_XPATH),
          "Sjukhusvård eller läkarkontakt - datum");
    }

    if (utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktVardinrattning() != null) {
      assertEquals(
          utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktVardinrattning(),
          xPath.evaluate(XPathExpressionsV1.VARD_PA_SJUKHUS_VARDINRATTNING_XPATH),
          "Sjukhusvård eller läkarkontakt - plats");
    }

    if (utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktAnledning() != null) {
      assertEquals(
          utlatande.getSjukhusvard().getSjukhusvardEllerLakarkontaktAnledning(),
          xPath.evaluate(
              XPathExpressionsV1.stringXPath(
                  XPathExpressionsV1.AKTIVITET_BESKRIVNING_TEMPLATE, "AKT19")),
          "Sjukhusvård eller läkarkontakt - anledning");
    }

    // Observationer
    // Synobservationer
    boolean synfaltsdefekter = utlatande.getSynfunktion().isHarSynfaltsdefekt();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "H53.4", synfaltsdefekter)),
        "Synfältsdefekter");

    boolean nattblindhet = utlatande.getSynfunktion().isHarNattblindhet();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "H53.6", nattblindhet)),
        "Nattblindhet");

    boolean progressivOgonsjukdom = utlatande.getSynfunktion().isHarProgressivOgonsjukdom();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "OBS1", progressivOgonsjukdom)),
        "Progressiv ögonsjukdom");

    boolean diplopi = utlatande.getSynfunktion().isHarDiplopi();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "H53.2", diplopi)),
        "Diplopi");

    boolean nystagmus = utlatande.getSynfunktion().isHarNystagmus();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "H55.9", nystagmus)),
        "Nystagmus");

    SynskarpaUtanKorrektion utanKorr = utlatande.getSynfunktion().getSynskarpaUtanKorrektion();

    SynskarpaMedKorrektion medKorr = utlatande.getSynfunktion().getSynskarpaMedKorrektion();

    assertEquals(
        String.valueOf(utanKorr.getHogerOga()),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "24028007")),
        "Ej korrigerad synskärpa höger");

    assertEquals(
        medKorr.getHogerOga() == null ? "" : String.valueOf(medKorr.getHogerOga()),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "24028007")),
        "Korrigerad synskärpa höger");

    assertEquals(
        String.valueOf(utanKorr.getVansterOga()),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "7771000")),
        "Ej korrigerad synskärpa vänster");

    assertEquals(
        medKorr.getVansterOga() == null ? "" : String.valueOf(medKorr.getVansterOga()),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "7771000")),
        "Korrigerad synskärpa vänster");

    assertEquals(
        String.valueOf(utanKorr.getBinokulart()),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "420050001", "51440002")),
        "Ej korrigerad synskärpa binokulärt");

    assertEquals(
        medKorr.getBinokulart() == null ? "" : String.valueOf(medKorr.getBinokulart()),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_VARDE_CODE_LATERALITET, "397535007", "51440002")),
        "Korrigerad synskärpa binokulärt");

    if (medKorr.isHarKontaktlinsHogerOga() != null) {
      assertEquals(
          medKorr.isHarKontaktlinsHogerOga(),
          xPath.evaluate(
              XPathExpressionsV1.booleanXPath(
                  XPathExpressionsV1.OBSERVATION_FOREKOMST_CODE_LATERALITET,
                  "285049007",
                  "24028007")),
          "Kontaktlinser höger");
    }

    if (medKorr.isHarKontaktlinsVansterOga() != null) {
      assertEquals(
          medKorr.isHarKontaktlinsVansterOga(),
          xPath.evaluate(
              XPathExpressionsV1.booleanXPath(
                  XPathExpressionsV1.OBSERVATION_FOREKOMST_CODE_LATERALITET,
                  "285049007",
                  "7771000")),
          "Kontaktlinser vänster");
    }

    // Hörsel & balans
    HorselBalanssinne horselBalans = utlatande.getHorselBalanssinne();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS2",
                horselBalans.isHarBalansrubbningYrsel())),
        "Balansrubbningar");

    if (horselBalans.isHarSvartUppfattaSamtal4Meter() != null) {
      assertTrue(
          xPath.evaluate(
              XPathExpressionsV1.booleanXPath(
                  XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                  "OBS3",
                  horselBalans.isHarSvartUppfattaSamtal4Meter())),
          "Svårt uppfatta samtal 4 meter");
    }

    // Rörelseorganens förmåga
    RorelseorganenFunktioner rorelse = utlatande.getRorelseorganensFunktioner();

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS4",
                rorelse.isHarRorelsebegransning())),
        "Har rörelsebegränsning");

    assertEquals(
        rorelse.getRorelsebegransningBeskrivning() == null
            ? ""
            : rorelse.getRorelsebegransningBeskrivning(),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE,
                "OBS4",
                rorelse.getRorelsebegransningBeskrivning())),
        "Rörelsebegränsning beskrivning");

    if (rorelse.isHarOtillrackligRorelseformagaPassagerare() != null) {
      assertTrue(
          xPath.evaluate(
              XPathExpressionsV1.booleanXPath(
                  XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                  "OBS5",
                  rorelse.isHarOtillrackligRorelseformagaPassagerare())),
          "Otillräcklig rörelseförmåga passagerare");
    }

    // Hjärt & kärlsjukdom
    HjartKarlSjukdomar hjartKarl = utlatande.getHjartKarlSjukdomar();

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS6",
                hjartKarl.isHarRiskForsamradHjarnFunktion())),
        "Risk försämrad hjärnfunktion");

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS8",
                hjartKarl.isHarHjarnskadaICNS())),
        "Tecken på hjärnskada");

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS7",
                hjartKarl.isHarRiskfaktorerStroke())),
        "Riskfaktorer för stroke");

    assertEquals(
        hjartKarl.getRiskfaktorerStrokeBeskrivning() == null
            ? ""
            : hjartKarl.getRiskfaktorerStrokeBeskrivning(),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE,
                "OBS7",
                hjartKarl.getRiskfaktorerStrokeBeskrivning())),
        "Riskfaktorer stroke - beskrivning");

    // Diabetes
    DiabetesTypBas diabetes = utlatande.getDiabetes();

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "73211009",
                diabetes.isHarDiabetes())),
        "Har diabetes");

    if (diabetes.isHarDiabetes()) {
      if (diabetes.getDiabetesTyp().name().equals("TYP1")) {
        assertTrue(
            xPath.evaluate(
                XPathExpressionsV1.booleanXPath(
                    XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "E10")),
            "Diabetes typ1");
      } else if (diabetes.getDiabetesTyp().name().equals("TYP2")) {
        assertTrue(
            xPath.evaluate(
                XPathExpressionsV1.booleanXPath(
                    XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE, "E11")),
            "Diabetes typ2");
      }

      if (diabetes.isHarBehandlingKost() != null) {
        assertTrue(
            xPath.evaluate(
                XPathExpressionsV1.booleanXPath(
                    XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                    "OBS9",
                    diabetes.isHarBehandlingKost())),
            "Diabetes kostbehandling");
      }

      if (diabetes.isHarBehandlingInsulin() != null) {
        assertTrue(
            xPath.evaluate(
                XPathExpressionsV1.booleanXPath(
                    XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                    "170747006",
                    diabetes.isHarBehandlingInsulin())),
            "Diabetes insulinbehandling");
      }

      if (diabetes.isHarBehandlingTabletter() != null) {
        assertTrue(
            xPath.evaluate(
                XPathExpressionsV1.booleanXPath(
                    XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                    "170746002",
                    diabetes.isHarBehandlingTabletter())),
            "Diabetes tablettbehandling");
      }
    }
    // Neurologi
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "407624006",
                utlatande.isNeurologiskaSjukdomar())),
        "Tecken på neurologisk sjukdom");

    // Medvetandestörning
    Medvetandestorning medvetande = utlatande.getMedvetandestorning();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "G40.9",
                medvetande.isHarMedvetandestorning())),
        "Har medvetandestörning");

    assertEquals(
        medvetande.getMedvetandestorningBeskrivning() == null
            ? ""
            : medvetande.getMedvetandestorningBeskrivning(),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE,
                "G40.9",
                medvetande.getMedvetandestorningBeskrivning())),
        "Medvetandestörning - beskrivning");

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS11",
                utlatande.isHarNjurSjukdom())),
        "Har medvetandestörning");

    // Sviktande kognitiv förmåga
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS12",
                utlatande.isHarKognitivStorning())),
        "kognitiv förmåga");

    // Somn vakenhet
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS13",
                utlatande.isHarSomnVakenhetStorning())),
        "Somn vakenhet");

    // alkohol och narkotika
    AlkoholNarkotikaLakemedel alkNark = utlatande.getAlkoholNarkotikaLakemedel();

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS14",
                alkNark.isHarTeckenMissbruk())),
        "Tecken på missbruk");

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS15",
                alkNark.isHarLakarordineratLakemedelsbruk())),
        "Läkemedelsbruk");

    assertEquals(
        alkNark.getLakarordineratLakemedelOchDos() == null
            ? ""
            : alkNark.getLakarordineratLakemedelOchDos(),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE, "OBS15")),
        "Läkemedelsbruk - beskrivning");

    // Psykisk sjukdom
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS16",
                utlatande.isHarPsykiskStorning())),
        "Läkemedelsbruk");

    // Utvecklingsstörning
    Utvecklingsstorning utvecklingsstorning = utlatande.getUtvecklingsstorning();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "129104009",
                utvecklingsstorning.isHarPsykiskUtvecklingsstorning())),
        "Har psykisk utvecklingsstörning");

    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS17",
                utvecklingsstorning.isHarAndrayndrom())),
        "Har ADHD eller damp");

    // Stadigvarande medicinering
    OvrigMedicinering medicinering = utlatande.getOvrigMedicinering();
    assertTrue(
        xPath.evaluate(
            XPathExpressionsV1.booleanXPath(
                XPathExpressionsV1.OBSERVATION_FOREKOMST_TEMPLATE,
                "OBS18",
                medicinering.isHarStadigvarandeMedicinering())),
        "Har stadigvarande medicinering");

    assertEquals(
        medicinering.getStadigvarandeMedicineringBeskrivning() == null
            ? ""
            : medicinering.getStadigvarandeMedicineringBeskrivning(),
        xPath.evaluate(
            XPathExpressionsV1.stringXPath(
                XPathExpressionsV1.OBSERVATION_BESKRIVNING_TEMPLATE, "OBS18")),
        "Har stadigvarande medicinering- beskrivning");

    // Rekommendationer
    assertTrue(
        xPath.evaluate(
            new BooleanXPathExpression(
                "utlatande/p:rekommendation/p:rekommendationskod/@code = 'REK8'")),
        "Rekommendation REK8");

    for (KorkortsbehorighetTsBas t : utlatande.getBedomning().getKorkortstyp()) {
      KorkortsbehorighetKod k = KorkortsbehorighetKod.valueOf(t.value().value());
      assertTrue(
          xPath.evaluate(
              XPathExpressionsV1.booleanXPath(
                  XPathExpressionsV1.REKOMMENDATION_VARDE_TEMPLATE, k.getCode())),
          String.format("Rekommendationsvärde %s", k.getCode()));
    }
    if (utlatande.getBedomning().isKanInteTaStallning() != null
        && utlatande.getBedomning().isKanInteTaStallning()) {
      assertTrue(
          xPath.evaluate(
              XPathExpressionsV1.booleanXPath(
                  XPathExpressionsV1.REKOMMENDATION_VARDE_TEMPLATE,
                  KorkortsbehorighetKod.KANINTETASTALLNING.getCode())),
          "Rekommendationsvärde Kan inte ta ställning (VAR11)");
    }

    if (utlatande.getBedomning().getBehovAvLakareSpecialistKompetens() != null) {
      assertEquals(
          utlatande.getBedomning().getBehovAvLakareSpecialistKompetens(),
          xPath.evaluate(
              XPathExpressionsV1.stringXPath(
                  XPathExpressionsV1.REKOMMENDATION_BESKRIVNING_TEMPLATE, "REK9")),
          String.format(
              "REK9 med beskrivning  %s",
              utlatande.getBedomning().getBehovAvLakareSpecialistKompetens()));
    }

    // Övrig kommentar
    if (utlatande.getOvrigKommentar() != null) {
      assertEquals(
          utlatande.getOvrigKommentar(),
          xPath.evaluate(XPathExpressionsV1.OVRIG_BESKRIVNING_XPATH),
          "Övrig beskrivning");
    }
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
