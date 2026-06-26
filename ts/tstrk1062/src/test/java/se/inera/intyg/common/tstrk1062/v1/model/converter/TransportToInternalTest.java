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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jakarta.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint;
import se.inera.intyg.common.tstrk1062.v1.model.internal.Bedomning;
import se.inera.intyg.common.tstrk1062.v1.model.internal.IntygAvser;
import se.inera.intyg.common.tstrk1062.v1.model.internal.PrognosTillstand;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

class TransportToInternalTest {

  @BeforeAll
  static void setUp() {
    final var mapper = mock(UnitMapperUtil.class);

    when(mapper.getMappedUnit(any(), any(), any(), any(), any()))
        .thenAnswer(
            inv ->
                new MappedUnit(
                    inv.getArgument(0, String.class),
                    inv.getArgument(1, String.class),
                    inv.getArgument(2, String.class),
                    inv.getArgument(3, String.class)));

    new InternalConverterUtil(mapper).initialize();
    new TransportConverterUtil(mapper).initialize();
  }

  @Test
  void convertIntygsTyp() throws Exception {
    final String href = "v1/transport/scenarios/convert/intygAvser.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertEquals(TsTrk1062EntryPoint.MODULE_ID, utlatande.getTyp(), "");
  }

  @Test
  void convertIntygAvser() throws Exception {
    final String href = "v1/transport/scenarios/convert/intygAvser.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertNotNull(utlatande.getIntygAvser(), "IntygAvser should not be null");
    assertEquals(
        1, utlatande.getIntygAvser().getBehorigheter().size(), "IntygAvser size not equal");
    assertEquals(
        IntygAvser.BehorighetsTyp.IAV11,
        utlatande.getIntygAvser().getBehorigheter().iterator().next(),
        "IntygAvser not equal");
  }

  @Test
  void convertIdKontroll() throws Exception {
    final String href = "v1/transport/scenarios/convert/idKontroll.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertNotNull(utlatande.getIdKontroll(), "IdKontroll should not be null");
    assertEquals(IdKontrollKod.ID_KORT, utlatande.getIdKontroll().getTyp(), "IdKontroll not equal");
  }

  @Test
  void convertDiagnosFritext() throws Exception {
    final String href = "v1/transport/scenarios/convert/diagnosFritext.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertNotNull(utlatande.getDiagnosFritext(), "DiagnosFritext should not be null");
    assertEquals(
        "Test", utlatande.getDiagnosFritext().getDiagnosFritext(), "DiagnosFritext not equal");
    assertEquals(
        "2018", utlatande.getDiagnosFritext().getDiagnosArtal(), "DiagnosFritextArtal not equal");
  }

  @Test
  void convertDiagnosKodad() throws Exception {
    final String href = "v1/transport/scenarios/convert/diagnosKodad.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertNotNull(utlatande.getDiagnosKodad(), "DiagnosKodad should not be null");
    assertEquals(4, utlatande.getDiagnosKodad().size(), "DiagnosKodad size not equal");

    assertEquals("A01", utlatande.getDiagnosKodad().get(0).getDiagnosKod(), "DiagnosKod not equal");
    assertEquals(
        "ICD_10_SE",
        utlatande.getDiagnosKodad().get(0).getDiagnosKodSystem(),
        "DiagnosKodSystem not equal");
    assertEquals(
        "Tyfoidfeber och paratyfoidfeber",
        utlatande.getDiagnosKodad().get(0).getDiagnosBeskrivning(),
        "DiagnosBeskrivning not equal");
    assertEquals("2018", utlatande.getDiagnosKodad().get(0).getDiagnosArtal(), "Artal not equal");

    assertEquals("B02", utlatande.getDiagnosKodad().get(1).getDiagnosKod(), "DiagnosKod not equal");
    assertEquals(
        "ICD_10_SE",
        utlatande.getDiagnosKodad().get(1).getDiagnosKodSystem(),
        "DiagnosKodSystem not equal");
    assertEquals(
        "Bältros",
        utlatande.getDiagnosKodad().get(1).getDiagnosBeskrivning(),
        "DiagnosBeskrivning not equal");
    assertEquals("2017", utlatande.getDiagnosKodad().get(1).getDiagnosArtal(), "Artal not equal");

    assertEquals("C03", utlatande.getDiagnosKodad().get(2).getDiagnosKod(), "DiagnosKod not equal");
    assertEquals(
        "ICD_10_SE",
        utlatande.getDiagnosKodad().get(2).getDiagnosKodSystem(),
        "DiagnosKodSystem not equal");
    assertEquals(
        "Malign tumör i tandköttet",
        utlatande.getDiagnosKodad().get(2).getDiagnosBeskrivning(),
        "DiagnosBeskrivning not equal");
    assertEquals("2018", utlatande.getDiagnosKodad().get(2).getDiagnosArtal(), "Artal not equal");

    assertEquals("D04", utlatande.getDiagnosKodad().get(3).getDiagnosKod(), "DiagnosKod not equal");
    assertEquals(
        "ICD_10_SE",
        utlatande.getDiagnosKodad().get(3).getDiagnosKodSystem(),
        "DiagnosKodSystem not equal");
    assertEquals(
        "Cancer in situ i huden",
        utlatande.getDiagnosKodad().get(3).getDiagnosBeskrivning(),
        "DiagnosBeskrivning not equal");
    assertEquals("2011", utlatande.getDiagnosKodad().get(3).getDiagnosArtal(), "Artal not equal");
  }

  @Test
  void convertLakemedelsbehandlingSaknas() throws Exception {
    final String href = "v1/transport/scenarios/convert/lakemedelsbehandlingSaknas.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertNotNull(utlatande.getLakemedelsbehandling(), "Lakemedelsbehandling should not be null");
    assertEquals(
        Boolean.FALSE, utlatande.getLakemedelsbehandling().getHarHaft(), "HarHaft not equal");
    assertNull(utlatande.getLakemedelsbehandling().getPagar(), "Pagar is not null");
    assertNull(utlatande.getLakemedelsbehandling().getAktuell(), "Aktuell is not null");
    assertNull(utlatande.getLakemedelsbehandling().getPagatt(), "Pagatt is not null");
    assertNull(utlatande.getLakemedelsbehandling().getEffekt(), "Effekt is not null");
    assertNull(utlatande.getLakemedelsbehandling().getFoljsamhet(), "Foljsamhet is not null");
    assertNull(
        utlatande.getLakemedelsbehandling().getAvslutadTidpunkt(), "AvslutadTidpunk is not null");
    assertNull(utlatande.getLakemedelsbehandling().getAvslutadOrsak(), "AvslutadOrsak is not null");
  }

  @Test
  void convertLakemedelsbehandlingPagar() throws Exception {
    final String href = "v1/transport/scenarios/convert/lakemedelsbehandlingPagar.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertNotNull(utlatande.getLakemedelsbehandling(), "Lakemedelsbehandling should not be null");
    assertEquals(
        Boolean.TRUE, utlatande.getLakemedelsbehandling().getHarHaft(), "HarHaft not equal");
    assertEquals(Boolean.TRUE, utlatande.getLakemedelsbehandling().getPagar(), "Pagar not equal");
    assertEquals(
        "Läkemedel a, b och c",
        utlatande.getLakemedelsbehandling().getAktuell(),
        "Aktuell not equal");
    assertEquals(Boolean.TRUE, utlatande.getLakemedelsbehandling().getPagatt(), "Pagatt not equal");
    assertEquals(
        Boolean.FALSE, utlatande.getLakemedelsbehandling().getEffekt(), "Effekt not equal");
    assertEquals(
        Boolean.TRUE, utlatande.getLakemedelsbehandling().getFoljsamhet(), "Foljsamhet not equal");
    assertNull(
        utlatande.getLakemedelsbehandling().getAvslutadTidpunkt(), "AvslutadTidpunk is not null");
    assertNull(utlatande.getLakemedelsbehandling().getAvslutadOrsak(), "AvslutadOrsak is not null");
  }

  @Test
  void convertLakemedelsbehandlingAvslutad() throws Exception {
    final String href = "v1/transport/scenarios/convert/lakemedelsbehandlingAvslutad.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertNotNull(utlatande.getLakemedelsbehandling(), "Lakemedelsbehandling should not be null");
    assertEquals(
        Boolean.TRUE, utlatande.getLakemedelsbehandling().getHarHaft(), "HarHaft not equal");
    assertEquals(Boolean.FALSE, utlatande.getLakemedelsbehandling().getPagar(), "Pagar not equal");
    assertNull(utlatande.getLakemedelsbehandling().getAktuell(), "Aktuell is not null");
    assertNull(utlatande.getLakemedelsbehandling().getPagatt(), "Pagatt is not null");
    assertNull(utlatande.getLakemedelsbehandling().getEffekt(), "Effekt is not null");
    assertNull(utlatande.getLakemedelsbehandling().getFoljsamhet(), "Foljsamhet is not null");
    assertNotNull(
        utlatande.getLakemedelsbehandling().getAvslutadTidpunkt(), "AvslutadTidpunk is null");
    assertEquals(
        "2019-01-10",
        utlatande.getLakemedelsbehandling().getAvslutadTidpunkt(),
        "AvslutadTidpunkt not equal");
    assertEquals(
        "Behandlingen var fruktlös",
        utlatande.getLakemedelsbehandling().getAvslutadOrsak(),
        "AvslutadOrsak not equal");
  }

  @Test
  void convertSymptomBedomningTrue() throws Exception {
    final String href = "v1/transport/scenarios/convert/symptomBedomningTrue.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertEquals(
        "Bedömning av aktuella symptom",
        utlatande.getBedomningAvSymptom(),
        "BedomningAvSymptom not equal");
    assertEquals(
        PrognosTillstand.PrognosTillstandTyp.JA,
        utlatande.getPrognosTillstand().getTyp(),
        "PrognosTillstand not equal");
  }

  @Test
  void convertSymptomBedomningFalse() throws Exception {
    final String href = "v1/transport/scenarios/convert/symptomBedomningFalse.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertEquals(
        "Bedömning av aktuella symptom",
        utlatande.getBedomningAvSymptom(),
        "BedomningAvSymptom not equal");
    assertEquals(
        PrognosTillstand.PrognosTillstandTyp.NEJ,
        utlatande.getPrognosTillstand().getTyp(),
        "PrognosTillstand not equal");
  }

  @Test
  void convertSymptomBedomningNI() throws Exception {
    final String href = "v1/transport/scenarios/convert/symptomBedomningNI.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertEquals(
        "Bedömning av aktuella symptom",
        utlatande.getBedomningAvSymptom(),
        "BedomningAvSymptom not equal");
    assertEquals(
        PrognosTillstand.PrognosTillstandTyp.KANEJBEDOMA,
        utlatande.getPrognosTillstand().getTyp(),
        "PrognosTillstand not equal");
  }

  @Test
  void convertOvrigt() throws Exception {
    final String href = "v1/transport/scenarios/convert/ovrigaKommentarer.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertEquals(
        "Inga övriga kommentarer", utlatande.getOvrigaKommentarer(), "OvrigaKommentarer not equal");
  }

  @Test
  void convertBedomning() throws Exception {
    final String href = "v1/transport/scenarios/convert/bedomning.xml";
    final Intyg intyg = getIntyg(href);
    final TsTrk1062UtlatandeV1 utlatande = TransportToInternal.convert(intyg);

    assertNotNull(utlatande, "Utlatande should not be null");
    assertNotNull(utlatande.getBedomning(), "Bedomning should not be null");
    assertEquals(
        1,
        utlatande.getBedomning().getUppfyllerBehorighetskrav().size(),
        "Bedomning size not equal");
    assertEquals(
        Bedomning.BehorighetsTyp.VAR12,
        utlatande.getBedomning().getUppfyllerBehorighetskrav().iterator().next(),
        "Bedomning not equal");
  }

  private Intyg getIntyg(String href) throws IOException {
    final String xml = Resources.toString(getResource(href), Charsets.UTF_8);
    return JAXB.unmarshal(new StringReader(xml), RegisterCertificateType.class).getIntyg();
  }

  private static URL getResource(String href) {
    return Thread.currentThread().getContextClassLoader().getResource(href);
  }
}
