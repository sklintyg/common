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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static se.inera.intyg.common.support.Constants.KV_V3_CODE_SYSTEM_NULLFLAVOR_CODE_SYSTEM;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getBooleanContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getCVSvarContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getPartialDateContent;
import static se.inera.intyg.common.support.modules.converter.TransportConverterUtil.getStringContent;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.BEDOMNING_UPPFYLLER_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.BEDOMNING_UPPFYLLER_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ID_KONTROLL_DELSVAR_ID_1;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ID_KONTROLL_SVAR_ID_1;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.INTYG_AVSER_DELSVAR_ID_1;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.INTYG_AVSER_SVAR_ID_1;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.OVRIGT_OVRIGA_KOMMENTARER_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_BEDOMNING_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_PROGNOS_DELSVAR_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_PROGNOS_SVAR_ID;

import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.common.tstrk1062.v1.model.internal.Bedomning;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosFritext;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosKodad;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosRegistrering;
import se.inera.intyg.common.tstrk1062.v1.model.internal.IdKontroll;
import se.inera.intyg.common.tstrk1062.v1.model.internal.IntygAvser;
import se.inera.intyg.common.tstrk1062.v1.model.internal.Lakemedelsbehandling;
import se.inera.intyg.common.tstrk1062.v1.model.internal.PrognosTillstand;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(
    classes = {UnitMappingConfigLoader.class, UnitMapperUtil.class, InternalConverterUtil.class})
class UtlatandeToIntygTest {

  TsTrk1062UtlatandeV1.Builder builderTemplate;

  @Mock private WebcertModuleService webcertModuleService;

  @BeforeEach
  void setUp() {
    builderTemplate =
        TsTrk1062UtlatandeV1.builder().setGrundData(buildGrundData(LocalDateTime.now()));
    lenient()
        .when(webcertModuleService.validateDiagnosisCode(anyString(), anyString()))
        .thenReturn(true);
  }

  @Test
  void convertUtlatandeIntygsTyp() {
    final TsTrk1062UtlatandeV1 utlatande = builderTemplate.build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    assertIntygsTyp(utlatande, intyg);
  }

  @Test
  void convertUtlatandeIntygsVersion() {
    final TsTrk1062UtlatandeV1 utlatande = builderTemplate.setTextVersion("TextVersion").build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    assertEquals(utlatande.getTextVersion(), intyg.getVersion(), "Intygsversion is not equal");
  }

  @Test
  void convertUtlatandeIntygAvses() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvser.BehorighetsTyp.IAV11)))
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertIntygAvser(
        utlatande.getIntygAvser(),
        svar.getAllDelsvar(INTYG_AVSER_SVAR_ID_1, INTYG_AVSER_DELSVAR_ID_1));
  }

  @Test
  void convertUtlatandeIdKontroll() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate.setIdKontroll(IdKontroll.create(IdKontrollKod.KORKORT)).build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertIdKontroll(
        utlatande.getIdKontroll(),
        svar.getDelsvar(ID_KONTROLL_SVAR_ID_1, ID_KONTROLL_DELSVAR_ID_1));
  }

  @Test
  void convertUtlatandeDiagnosFritext() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setDiagnosRegistrering(
                DiagnosRegistrering.create(
                    DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
            .setDiagnosFritext(DiagnosFritext.create("Diagnoser", "2017"))
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertDiagnosFritext(
        utlatande.getDiagnosFritext(),
        svar.getDelsvar(
            ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID, ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_ID),
        svar.getDelsvar(
            ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID, ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_ID));
  }

  @Test
  void convertUtlatandeWithDiagnosKodadToIntyg() throws Exception {
    final DiagnosKodad diagnosKodadA01 =
        DiagnosKodad.create(
            "A01", "ICD_10_SE", "Beskrivning för A01", "DisplayName för A01", "2018");

    final DiagnosKodad diagnosKodadB01 =
        DiagnosKodad.create(
            "B01", "ICD_10_SE", "Beskrivning för B01", "DisplayName för B01", "2019");

    final List<DiagnosKodad> diagnosKodadList = new ArrayList<DiagnosKodad>(1);
    diagnosKodadList.add(diagnosKodadA01);
    diagnosKodadList.add(diagnosKodadB01);

    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setDiagnosRegistrering(
                DiagnosRegistrering.create(
                    DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
            .setDiagnosKodad(diagnosKodadList)
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertDiagnosKodad(utlatande.getDiagnosKodad(), svar);
  }

  @Test
  void convertUtlatandeLakemedelsbehandlingNej() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setLakemedelsbehandling(
                Lakemedelsbehandling.create(false, null, null, null, null, null, null, null))
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertLakemedelsbehandlingNej(utlatande.getLakemedelsbehandling(), svar);
  }

  @Test
  void convertUtlatandeLakemedelsbehandlingJaPagande() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setLakemedelsbehandling(
                Lakemedelsbehandling.create(
                    true, true, "Aktuell behandling", true, true, true, null, null))
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertLakemedelsbehandlingJaPagar(utlatande.getLakemedelsbehandling(), svar);
  }

  @Test
  void convertUtlatandeLakemedelsbehandlingNejAvslutad() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setLakemedelsbehandling(
                Lakemedelsbehandling.create(
                    true, false, null, null, null, null, "Förra månaden", "Avslutad orsak."))
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertLakemedelsbehandlingJaAvslutad(utlatande.getLakemedelsbehandling(), svar);
  }

  @Test
  void convertUtlatandeBedomningAvSymptom() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate.setBedomningAvSymptom("Bedömning av...").build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertBedomningAvSymptom(
        utlatande.getBedomningAvSymptom(),
        svar.getDelsvar(SYMPTOM_BEDOMNING_SVAR_ID, SYMPTOM_BEDOMNING_DELSVAR_ID));
  }

  @Test
  void convertUtlatandeWithPrognosTillstandNej() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setPrognosTillstand(PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.NEJ))
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertPrognosTillstandAsBoolean(
        "false", svar.getDelsvar(SYMPTOM_PROGNOS_SVAR_ID, SYMPTOM_PROGNOS_DELSVAR_ID));
  }

  @Test
  void convertUtlatandeWithPrognosTillstandJa() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setPrognosTillstand(PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.JA))
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertPrognosTillstandAsBoolean(
        "true", svar.getDelsvar(SYMPTOM_PROGNOS_SVAR_ID, SYMPTOM_PROGNOS_DELSVAR_ID));
  }

  @Test
  void convertUtlatandeWithPrognosTillstandKanEjBedoma() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setPrognosTillstand(
                PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.KANEJBEDOMA))
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertPrognosTillstandAsCode(
        utlatande.getPrognosTillstand(),
        svar.getDelsvar(SYMPTOM_PROGNOS_SVAR_ID, SYMPTOM_PROGNOS_DELSVAR_ID));
  }

  @Test
  void convertUtlatandeOvrigaKommentarer() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate.setOvrigaKommentarer("Ovriga kommentarer").build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertOvrigaKommentarer(
        utlatande.getOvrigaKommentarer(),
        svar.getDelsvar(OVRIGT_OVRIGA_KOMMENTARER_SVAR_ID, OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID));
  }

  @Test
  void convertUtlatandeBedomning() throws Exception {
    final TsTrk1062UtlatandeV1 utlatande =
        builderTemplate
            .setBedomning(
                Bedomning.builder()
                    .setUppfyllerBehorighetskrav(EnumSet.of(Bedomning.BehorighetsTyp.VAR11))
                    .build())
            .build();

    final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

    final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
    assertBedomning(
        utlatande.getBedomning(),
        svar.getAllDelsvar(BEDOMNING_UPPFYLLER_SVAR_ID, BEDOMNING_UPPFYLLER_DELSVAR_ID));
  }

  private void assertBedomning(Bedomning bedomning, List<Svar.Delsvar> delsvarList)
      throws ConverterException {
    final Set<Bedomning.BehorighetsTyp> behorighetsTyper = bedomning.getUppfyllerBehorighetskrav();

    assertEquals(
        behorighetsTyper.size(),
        delsvarList.size(),
        "Number of UppfyllerBehorighetsKrav not equal");

    int i = 0;
    for (Bedomning.BehorighetsTyp behorighetsTyp : behorighetsTyper) {
      final Svar.Delsvar delsvar = delsvarList.get(i++);
      assertEquals(
          behorighetsTyp.toString(),
          getCVSvarContent(delsvar).getCode(),
          "Behorighetstyp not equal");
    }
  }

  private void assertIntygsTyp(final TsTrk1062UtlatandeV1 utlatande, Intyg intyg) {
    assertEquals(
        KvIntygstyp.TSTRK1062.getCodeValue(),
        intyg.getTyp().getCode(),
        "Intygstyp.code is not equal");
    assertEquals(
        KvIntygstyp.TSTRK1062.getCodeSystem(),
        intyg.getTyp().getCodeSystem(),
        "Intygstyp.codeSystem is not equal");
    assertEquals(
        KvIntygstyp.TSTRK1062.getDisplayName(),
        intyg.getTyp().getDisplayName(),
        "Intygstyp.displayName is not equal");
  }

  private void assertOvrigaKommentarer(String ovrigaKommentarer, Svar.Delsvar delsvar) {
    assertNotNull(delsvar, "ÖvrigaKommentarer should not be null");
    assertEquals(ovrigaKommentarer, getStringContent(delsvar), "ÖvrigaKommentarer not equal");
  }

  private void assertBedomningAvSymptom(String bedomningAvSymptom, Svar.Delsvar delsvar) {
    assertNotNull(delsvar, "BedömningAvSymptom should not be null");
    assertEquals(bedomningAvSymptom, getStringContent(delsvar), "BedömningAvSymptom not equal");
  }

  private void assertPrognosTillstandAsBoolean(String prognosTillstand, Svar.Delsvar delsvar) {
    assertNotNull(delsvar, "PrognosTillstand should not be null");
    assertEquals(prognosTillstand, getStringContent(delsvar), "PrognosTillstand not equal");
  }

  private void assertPrognosTillstandAsCode(PrognosTillstand prognosTillstand, Svar.Delsvar delsvar)
      throws ConverterException {
    assertNotNull(delsvar, "PrognosTillstand should not be null");
    assertEquals(
        prognosTillstand.getTyp().getCode(),
        getCVSvarContent(delsvar).getCode(),
        "PrognosTillstand.kod not equal");
    assertEquals(
        KV_V3_CODE_SYSTEM_NULLFLAVOR_CODE_SYSTEM,
        getCVSvarContent(delsvar).getCodeSystem(),
        "PrognosTillstand.kodsystem not equal");
    assertEquals(
        prognosTillstand.getTyp().getDescription(),
        getCVSvarContent(delsvar).getDisplayName(),
        "PrognosTillstand.beskrivning not equal");
  }

  private void assertIdKontroll(IdKontroll idKontroll, Svar.Delsvar delsvar)
      throws ConverterException {
    assertEquals(
        idKontroll.getTyp().getCode(), getCVSvarContent(delsvar).getCode(), "Idkontroll not equal");
  }

  private void assertIntygAvser(IntygAvser intygAvser, List<Svar.Delsvar> delsvarList)
      throws ConverterException {
    final Set<IntygAvser.BehorighetsTyp> behorighetsTyper = intygAvser.getBehorigheter();

    assertEquals(behorighetsTyper.size(), delsvarList.size(), "Number of IntygAvser not equal");

    int i = 0;
    for (IntygAvser.BehorighetsTyp behorighetsTyp : behorighetsTyper) {
      final Svar.Delsvar delsvar = delsvarList.get(i++);
      assertEquals(
          behorighetsTyp.toString(),
          getCVSvarContent(delsvar).getCode(),
          "Behorighetstyp not equal");
    }
  }

  private void assertDiagnosFritext(
      DiagnosFritext diagnosFritext, Svar.Delsvar delsvarFritext, Svar.Delsvar delsvarArtal)
      throws ConverterException {
    assertEquals(
        diagnosFritext.getDiagnosFritext(),
        getStringContent(delsvarFritext),
        "DiagnosFritext not equal");
    assertEquals(
        diagnosFritext.getDiagnosArtal(),
        getPartialDateContent(delsvarArtal).getValue().toString(),
        "DiagnosArtal not equal");
  }

  private void assertDiagnosKodad(ImmutableList<DiagnosKodad> diagnosKodad, SvarsWrapper svar)
      throws ConverterException {
    final List<Svar> svarList = svar.getSvarList(ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID);

    assertEquals(diagnosKodad.size(), svarList.size(), "DiagnosKodad is not same length");

    for (int i = 0; i < svarList.size(); i++) {
      final Svar.Delsvar delsvarKod =
          svar.getDelsvar(
              ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID, i + 1, ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_ID);
      final Svar.Delsvar delsvarBeskrivning =
          svar.getDelsvar(
              ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID,
              i + 1,
              ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_ID);
      final Svar.Delsvar delsvarArtal =
          svar.getDelsvar(
              ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID,
              i + 1,
              ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_ID);

      assertEquals(
          diagnosKodad.get(i).getDiagnosKod(),
          getCVSvarContent(delsvarKod).getCode(),
          "DiagnosKod not equal");
      assertEquals(
          "1.2.752.116.1.1.1.1.3",
          getCVSvarContent(delsvarKod).getCodeSystem(),
          "KodSystem not equal");
      assertEquals(
          diagnosKodad.get(i).getDiagnosBeskrivning(),
          getStringContent(delsvarBeskrivning),
          "Beskrivning not equal");
      assertEquals(
          diagnosKodad.get(i).getDiagnosArtal(),
          getPartialDateContent(delsvarArtal).getValue().toString(),
          "Artal not equal");
    }
  }

  private void assertLakemedelsbehandlingNej(
      Lakemedelsbehandling lakemedelsbehandling, SvarsWrapper svar) throws ConverterException {
    final Svar.Delsvar delsvarHarHaft =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID, LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID);
    assertNotNull(delsvarHarHaft, "HarHaft should not be null");
    assertEquals(
        lakemedelsbehandling.getHarHaft(), getBooleanContent(delsvarHarHaft), "HarHaft not equal");

    final Svar.Delsvar delsvarPagar =
        svar.getDelsvar(LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID, LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID);
    assertNull(delsvarPagar, "Pagar should be null");

    final Svar.Delsvar delsvarAktuell =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID, LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID);
    assertNull(delsvarAktuell, "Aktuell should be null");

    final Svar.Delsvar delsvarPagatt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID, LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID);
    assertNull(delsvarPagatt, "Pagar should be null");

    final Svar.Delsvar delsvarEffekt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID, LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID);
    assertNull(delsvarEffekt, "Effekt should be null");

    final Svar.Delsvar delsvarFoljsamhet =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID, LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID);
    assertNull(delsvarFoljsamhet, "Foljsamhet should be null");

    final Svar.Delsvar delsvarAvslutadTidpunkt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID, LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID);
    assertNull(delsvarAvslutadTidpunkt, "AvslutadTidpunkt should be null");

    final Svar.Delsvar delsvarAvslutadOrsak =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID, LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID);
    assertNull(delsvarAvslutadOrsak, "AvslutadOrsak should be null");
  }

  private void assertLakemedelsbehandlingJaPagar(
      Lakemedelsbehandling lakemedelsbehandling, SvarsWrapper svar) throws ConverterException {
    final Svar.Delsvar delsvarHarHaft =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID, LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID);
    assertNotNull(delsvarHarHaft, "HarHaft should not be null");
    assertEquals(
        lakemedelsbehandling.getHarHaft(), getBooleanContent(delsvarHarHaft), "HarHaft not equal");

    final Svar.Delsvar delsvarPagar =
        svar.getDelsvar(LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID, LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID);
    assertNotNull(delsvarPagar, "Pagar should not be null");
    assertEquals(
        lakemedelsbehandling.getPagar(), getBooleanContent(delsvarPagar), "Pagar not equal");

    final Svar.Delsvar delsvarAktuell =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID, LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID);
    assertNotNull(delsvarAktuell, "Aktuell should not be null");
    assertEquals(
        lakemedelsbehandling.getAktuell(), getStringContent(delsvarAktuell), "Aktuell not equal");

    final Svar.Delsvar delsvarPagatt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID, LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID);
    assertNotNull(delsvarPagatt, "Pagar should not be null");
    assertEquals(
        lakemedelsbehandling.getPagar(), getBooleanContent(delsvarPagatt), "Pagar not equal");

    final Svar.Delsvar delsvarEffekt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID, LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID);
    assertNotNull(delsvarEffekt, "Effekt should not be null");
    assertEquals(
        lakemedelsbehandling.getEffekt(), getBooleanContent(delsvarEffekt), "Effekt not equal");

    final Svar.Delsvar delsvarFoljsamhet =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID, LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID);
    assertNotNull(delsvarFoljsamhet, "Foljsamhet should not be null");
    assertEquals(
        lakemedelsbehandling.getFoljsamhet(),
        getBooleanContent(delsvarFoljsamhet),
        "Foljsamhet not equal");

    final Svar.Delsvar delsvarAvslutadTidpunkt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID, LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID);
    assertNull(delsvarAvslutadTidpunkt, "AvslutadTidpunkt should be null");

    final Svar.Delsvar delsvarAvslutadOrsak =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID, LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID);
    assertNull(delsvarAvslutadOrsak, "AvslutadOrsak should be null");
  }

  private void assertLakemedelsbehandlingJaAvslutad(
      Lakemedelsbehandling lakemedelsbehandling, SvarsWrapper svar) throws ConverterException {
    final Svar.Delsvar delsvarHarHaft =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID, LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID);
    assertNotNull(delsvarHarHaft, "HarHaft should not be null");
    assertEquals(
        lakemedelsbehandling.getHarHaft(), getBooleanContent(delsvarHarHaft), "HarHaft not equal");

    final Svar.Delsvar delsvarPagar =
        svar.getDelsvar(LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID, LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID);
    assertNotNull(delsvarPagar, "Pagar should not be null");
    assertEquals(
        lakemedelsbehandling.getPagar(), getBooleanContent(delsvarPagar), "Pagar not equal");

    final Svar.Delsvar delsvarAktuell =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID, LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID);
    assertNull(delsvarAktuell, "Aktuell should be null");

    final Svar.Delsvar delsvarPagatt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID, LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID);
    assertNull(delsvarPagatt, "Pagar should be null");

    final Svar.Delsvar delsvarEffekt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID, LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID);
    assertNull(delsvarEffekt, "Effekt should be null");

    final Svar.Delsvar delsvarFoljsamhet =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID, LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID);
    assertNull(delsvarFoljsamhet, "Foljsamhet should be null");

    final Svar.Delsvar delsvarAvslutadTidpunkt =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID, LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID);
    assertNotNull(delsvarAvslutadTidpunkt, "AvslutadTidpunkt should not be null");
    assertEquals(
        lakemedelsbehandling.getAvslutadTidpunkt(),
        getStringContent(delsvarAvslutadTidpunkt),
        "AvslutadTidpunkt not equal");

    final Svar.Delsvar delsvarAvslutadOrsak =
        svar.getDelsvar(
            LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID, LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID);
    assertNotNull(delsvarAvslutadOrsak, "AvslutadOrsak should not be null");
    assertEquals(
        lakemedelsbehandling.getAvslutadOrsak(),
        getStringContent(delsvarAvslutadOrsak),
        "AvslutadOrsak not equal");
  }

  private GrundData buildGrundData(LocalDateTime timeStamp) {
    Vardgivare vardgivare = new Vardgivare();
    vardgivare.setVardgivarid("vardgivareId");
    vardgivare.setVardgivarnamn("vardgivareNamn");

    Vardenhet vardenhet = new Vardenhet();
    vardenhet.setEnhetsid("enhetId");
    vardenhet.setEnhetsnamn("enhetNamn");
    vardenhet.setVardgivare(vardgivare);
    vardenhet.setPostadress("postadress");
    vardenhet.setPostnummer("11111");
    vardenhet.setPostort("postort");
    vardenhet.setTelefonnummer("0112312313");

    HoSPersonal skapadAv = new HoSPersonal();
    skapadAv.setVardenhet(vardenhet);
    skapadAv.setPersonId("HSAID_123");
    skapadAv.setFullstandigtNamn("Torsten Ericsson");

    Patient patient = new Patient();
    patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
    patient.setPostadress("postadress");
    patient.setPostnummer("11111");
    patient.setPostort("postort");

    GrundData grundData = new GrundData();
    grundData.setSkapadAv(skapadAv);
    grundData.setPatient(patient);
    grundData.setSigneringsdatum(timeStamp);

    return grundData;
  }
}
