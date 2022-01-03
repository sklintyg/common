/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.common.enumerations.KvIntygstyp;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
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

@RunWith(MockitoJUnitRunner.class)
public class UtlatandeToIntygTest {

    TsTrk1062UtlatandeV1.Builder builderTemplate;

    @Mock
    private WebcertModuleService webcertModuleService;

    @Before
    public void setUp() {
        builderTemplate = TsTrk1062UtlatandeV1.builder().setGrundData(buildGrundData(LocalDateTime.now()));
        when(webcertModuleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);
    }

    @Test
    public void convertUtlatandeIntygsTyp() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        assertIntygsTyp(utlatande, intyg);
    }

    @Test
    public void convertUtlatandeIntygsVersion() {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setTextVersion("TextVersion")
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        assertEquals("Intygsversion is not equal", utlatande.getTextVersion(), intyg.getVersion());
    }

    @Test
    public void convertUtlatandeIntygAvses() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvser.BehorighetsTyp.IAV11)))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertIntygAvser(utlatande.getIntygAvser(), svar.getAllDelsvar(INTYG_AVSER_SVAR_ID_1, INTYG_AVSER_DELSVAR_ID_1));
    }

    @Test
    public void convertUtlatandeIdKontroll() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setIdKontroll(IdKontroll.create(IdKontrollKod.KORKORT))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertIdKontroll(utlatande.getIdKontroll(), svar.getDelsvar(ID_KONTROLL_SVAR_ID_1, ID_KONTROLL_DELSVAR_ID_1));
    }

    @Test
    public void convertUtlatandeDiagnosFritext() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_FRITEXT))
            .setDiagnosFritext(DiagnosFritext.create("Diagnoser", "2017"))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertDiagnosFritext(utlatande.getDiagnosFritext(),
            svar.getDelsvar(ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID, ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_ID),
            svar.getDelsvar(ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_ID, ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_ID));
    }

    @Test
    public void convertUtlatandeWithDiagnosKodadToIntyg() throws Exception {
        final DiagnosKodad diagnosKodadA01 = DiagnosKodad.create("A01",
            "ICD_10_SE", "Beskrivning för A01", "DisplayName för A01", "2018");

        final DiagnosKodad diagnosKodadB01 = DiagnosKodad.create("B01",
            "ICD_10_SE", "Beskrivning för B01", "DisplayName för B01", "2019");

        final List<DiagnosKodad> diagnosKodadList = new ArrayList<DiagnosKodad>(1);
        diagnosKodadList.add(diagnosKodadA01);
        diagnosKodadList.add(diagnosKodadB01);

        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
            .setDiagnosKodad(diagnosKodadList)
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertDiagnosKodad(utlatande.getDiagnosKodad(), svar);
    }

    @Test
    public void convertUtlatandeLakemedelsbehandlingNej() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setLakemedelsbehandling(Lakemedelsbehandling.create(false, null, null, null, null, null, null,
                null))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertLakemedelsbehandlingNej(utlatande.getLakemedelsbehandling(), svar);
    }

    @Test
    public void convertUtlatandeLakemedelsbehandlingJaPagande() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setLakemedelsbehandling(Lakemedelsbehandling.create(true, true, "Aktuell behandling", true, true, true, null,
                null))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertLakemedelsbehandlingJaPagar(utlatande.getLakemedelsbehandling(), svar);
    }

    @Test
    public void convertUtlatandeLakemedelsbehandlingNejAvslutad() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setLakemedelsbehandling(Lakemedelsbehandling.create(true, false, null, null, null, null, "Förra månaden",
                "Avslutad orsak."))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertLakemedelsbehandlingJaAvslutad(utlatande.getLakemedelsbehandling(), svar);
    }

    @Test
    public void convertUtlatandeBedomningAvSymptom() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setBedomningAvSymptom("Bedömning av...")
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertBedomningAvSymptom(utlatande.getBedomningAvSymptom(),
            svar.getDelsvar(SYMPTOM_BEDOMNING_SVAR_ID, SYMPTOM_BEDOMNING_DELSVAR_ID));
    }

    @Test
    public void convertUtlatandeWithPrognosTillstandNej() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setPrognosTillstand(PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.NEJ))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertPrognosTillstandAsBoolean("false", svar.getDelsvar(SYMPTOM_PROGNOS_SVAR_ID, SYMPTOM_PROGNOS_DELSVAR_ID));
    }

    @Test
    public void convertUtlatandeWithPrognosTillstandJa() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setPrognosTillstand(PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.JA))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertPrognosTillstandAsBoolean("true", svar.getDelsvar(SYMPTOM_PROGNOS_SVAR_ID, SYMPTOM_PROGNOS_DELSVAR_ID));
    }

    @Test
    public void convertUtlatandeWithPrognosTillstandKanEjBedoma() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setPrognosTillstand(PrognosTillstand.create(PrognosTillstand.PrognosTillstandTyp.KANEJBEDOMA))
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertPrognosTillstandAsCode(utlatande.getPrognosTillstand(), svar.getDelsvar(SYMPTOM_PROGNOS_SVAR_ID, SYMPTOM_PROGNOS_DELSVAR_ID));
    }

    @Test
    public void convertUtlatandeOvrigaKommentarer() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setOvrigaKommentarer("Ovriga kommentarer")
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertOvrigaKommentarer(utlatande.getOvrigaKommentarer(),
            svar.getDelsvar(OVRIGT_OVRIGA_KOMMENTARER_SVAR_ID, OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_ID));
    }

    @Test
    public void convertUtlatandeBedomning() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = builderTemplate
            .setBedomning(Bedomning.builder().setUppfyllerBehorighetskrav(EnumSet.of(Bedomning.BehorighetsTyp.VAR11)).build())
            .build();

        final Intyg intyg = UtlatandeToIntyg.convert(utlatande, webcertModuleService);

        final SvarsWrapper svar = new SvarsWrapper(intyg.getSvar());
        assertBedomning(utlatande.getBedomning(), svar.getAllDelsvar(BEDOMNING_UPPFYLLER_SVAR_ID, BEDOMNING_UPPFYLLER_DELSVAR_ID));
    }

    private void assertBedomning(Bedomning bedomning, List<Svar.Delsvar> delsvarList) throws ConverterException {
        final Set<Bedomning.BehorighetsTyp> behorighetsTyper = bedomning.getUppfyllerBehorighetskrav();

        assertEquals("Number of UppfyllerBehorighetsKrav not equal", behorighetsTyper.size(), delsvarList.size());

        int i = 0;
        for (Bedomning.BehorighetsTyp behorighetsTyp : behorighetsTyper) {
            final Svar.Delsvar delsvar = delsvarList.get(i++);
            assertEquals("Behorighetstyp not equal", behorighetsTyp.toString(), getCVSvarContent(delsvar).getCode());
        }
    }

    private void assertIntygsTyp(final TsTrk1062UtlatandeV1 utlatande, Intyg intyg) {
        assertEquals("Intygstyp.code is not equal", KvIntygstyp.TSTRK1062.getCodeValue(), intyg.getTyp().getCode());
        assertEquals("Intygstyp.codeSystem is not equal", KvIntygstyp.TSTRK1062.getCodeSystem(), intyg.getTyp().getCodeSystem());
        assertEquals("Intygstyp.displayName is not equal", KvIntygstyp.TSTRK1062.getDisplayName(), intyg.getTyp().getDisplayName());
    }

    private void assertOvrigaKommentarer(String ovrigaKommentarer, Svar.Delsvar delsvar) {
        assertNotNull("ÖvrigaKommentarer should not be null", delsvar);
        assertEquals("ÖvrigaKommentarer not equal", ovrigaKommentarer, getStringContent(delsvar));
    }

    private void assertBedomningAvSymptom(String bedomningAvSymptom, Svar.Delsvar delsvar) {
        assertNotNull("BedömningAvSymptom should not be null", delsvar);
        assertEquals("BedömningAvSymptom not equal", bedomningAvSymptom, getStringContent(delsvar));
    }

    private void assertPrognosTillstandAsBoolean(String prognosTillstand, Svar.Delsvar delsvar) {
        assertNotNull("PrognosTillstand should not be null", delsvar);
        assertEquals("PrognosTillstand not equal", prognosTillstand, getStringContent(delsvar));
    }

    private void assertPrognosTillstandAsCode(PrognosTillstand prognosTillstand, Svar.Delsvar delsvar) throws ConverterException {
        assertNotNull("PrognosTillstand should not be null", delsvar);
        assertEquals("PrognosTillstand.kod not equal", prognosTillstand.getTyp().getCode(), getCVSvarContent(delsvar).getCode());
        assertEquals("PrognosTillstand.kodsystem not equal", KV_V3_CODE_SYSTEM_NULLFLAVOR_CODE_SYSTEM,
            getCVSvarContent(delsvar).getCodeSystem());
        assertEquals("PrognosTillstand.beskrivning not equal", prognosTillstand.getTyp().getDescription(),
            getCVSvarContent(delsvar).getDisplayName());
    }

    private void assertIdKontroll(IdKontroll idKontroll, Svar.Delsvar delsvar) throws ConverterException {
        assertEquals("Idkontroll not equal", idKontroll.getTyp().getCode(), getCVSvarContent(delsvar).getCode());
    }

    private void assertIntygAvser(IntygAvser intygAvser, List<Svar.Delsvar> delsvarList) throws ConverterException {
        final Set<IntygAvser.BehorighetsTyp> behorighetsTyper = intygAvser.getBehorigheter();

        assertEquals("Number of IntygAvser not equal", behorighetsTyper.size(), delsvarList.size());

        int i = 0;
        for (IntygAvser.BehorighetsTyp behorighetsTyp : behorighetsTyper) {
            final Svar.Delsvar delsvar = delsvarList.get(i++);
            assertEquals("Behorighetstyp not equal", behorighetsTyp.toString(), getCVSvarContent(delsvar).getCode());
        }
    }

    private void assertDiagnosFritext(DiagnosFritext diagnosFritext, Svar.Delsvar delsvarFritext, Svar.Delsvar delsvarArtal)
        throws ConverterException {
        assertEquals("DiagnosFritext not equal", diagnosFritext.getDiagnosFritext(), getStringContent(delsvarFritext));
        assertEquals("DiagnosArtal not equal", diagnosFritext.getDiagnosArtal(), getPartialDateContent(delsvarArtal).getValue().toString());
    }

    private void assertDiagnosKodad(ImmutableList<DiagnosKodad> diagnosKodad, SvarsWrapper svar) throws ConverterException {
        final List<Svar> svarList = svar.getSvarList(ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID);

        assertEquals("DiagnosKodad is not same length", diagnosKodad.size(), svarList.size());

        for (int i = 0; i < svarList.size(); i++) {
            final Svar.Delsvar delsvarKod = svar.getDelsvar(ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID, i + 1,
                ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_ID);
            final Svar.Delsvar delsvarBeskrivning = svar.getDelsvar(ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID, i + 1,
                ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_ID);
            final Svar.Delsvar delsvarArtal = svar.getDelsvar(ALLMANT_DIAGNOSKOD_KODAD_SVAR_ID, i + 1,
                ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_ID);

            assertEquals("DiagnosKod not equal", diagnosKodad.get(i).getDiagnosKod(), getCVSvarContent(delsvarKod).getCode());
            assertEquals("KodSystem not equal", "1.2.752.116.1.1.1.1.3", getCVSvarContent(delsvarKod).getCodeSystem());
            assertEquals("Beskrivning not equal", diagnosKodad.get(i).getDiagnosBeskrivning(), getStringContent(delsvarBeskrivning));
            assertEquals("Artal not equal", diagnosKodad.get(i).getDiagnosArtal(),
                getPartialDateContent(delsvarArtal).getValue().toString());
        }
    }

    private void assertLakemedelsbehandlingNej(Lakemedelsbehandling lakemedelsbehandling, SvarsWrapper svar) throws ConverterException {
        final Svar.Delsvar delsvarHarHaft = svar.getDelsvar(LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID,
            LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID);
        assertNotNull("HarHaft should not be null", delsvarHarHaft);
        assertEquals("HarHaft not equal", lakemedelsbehandling.getHarHaft(), getBooleanContent(delsvarHarHaft));

        final Svar.Delsvar delsvarPagar = svar.getDelsvar(LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID, LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID);
        assertNull("Pagar should be null", delsvarPagar);

        final Svar.Delsvar delsvarAktuell = svar.getDelsvar(LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID, LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID);
        assertNull("Aktuell should be null", delsvarAktuell);

        final Svar.Delsvar delsvarPagatt = svar.getDelsvar(LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID, LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID);
        assertNull("Pagar should be null", delsvarPagatt);

        final Svar.Delsvar delsvarEffekt = svar.getDelsvar(LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID, LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID);
        assertNull("Effekt should be null", delsvarEffekt);

        final Svar.Delsvar delsvarFoljsamhet = svar.getDelsvar(LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID,
            LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID);
        assertNull("Foljsamhet should be null", delsvarFoljsamhet);

        final Svar.Delsvar delsvarAvslutadTidpunkt = svar.getDelsvar(LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID,
            LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID);
        assertNull("AvslutadTidpunkt should be null", delsvarAvslutadTidpunkt);

        final Svar.Delsvar delsvarAvslutadOrsak = svar.getDelsvar(LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID,
            LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID);
        assertNull("AvslutadOrsak should be null", delsvarAvslutadOrsak);
    }

    private void assertLakemedelsbehandlingJaPagar(Lakemedelsbehandling lakemedelsbehandling, SvarsWrapper svar) throws ConverterException {
        final Svar.Delsvar delsvarHarHaft = svar.getDelsvar(LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID,
            LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID);
        assertNotNull("HarHaft should not be null", delsvarHarHaft);
        assertEquals("HarHaft not equal", lakemedelsbehandling.getHarHaft(), getBooleanContent(delsvarHarHaft));

        final Svar.Delsvar delsvarPagar = svar.getDelsvar(LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID, LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID);
        assertNotNull("Pagar should not be null", delsvarPagar);
        assertEquals("Pagar not equal", lakemedelsbehandling.getPagar(), getBooleanContent(delsvarPagar));

        final Svar.Delsvar delsvarAktuell = svar.getDelsvar(LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID, LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID);
        assertNotNull("Aktuell should not be null", delsvarAktuell);
        assertEquals("Aktuell not equal", lakemedelsbehandling.getAktuell(), getStringContent(delsvarAktuell));

        final Svar.Delsvar delsvarPagatt = svar.getDelsvar(LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID, LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID);
        assertNotNull("Pagar should not be null", delsvarPagatt);
        assertEquals("Pagar not equal", lakemedelsbehandling.getPagar(), getBooleanContent(delsvarPagatt));

        final Svar.Delsvar delsvarEffekt = svar.getDelsvar(LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID, LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID);
        assertNotNull("Effekt should not be null", delsvarEffekt);
        assertEquals("Effekt not equal", lakemedelsbehandling.getEffekt(), getBooleanContent(delsvarEffekt));

        final Svar.Delsvar delsvarFoljsamhet = svar.getDelsvar(LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID,
            LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID);
        assertNotNull("Foljsamhet should not be null", delsvarFoljsamhet);
        assertEquals("Foljsamhet not equal", lakemedelsbehandling.getFoljsamhet(), getBooleanContent(delsvarFoljsamhet));

        final Svar.Delsvar delsvarAvslutadTidpunkt = svar.getDelsvar(LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID,
            LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID);
        assertNull("AvslutadTidpunkt should be null", delsvarAvslutadTidpunkt);

        final Svar.Delsvar delsvarAvslutadOrsak = svar.getDelsvar(LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID,
            LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID);
        assertNull("AvslutadOrsak should be null", delsvarAvslutadOrsak);
    }

    private void assertLakemedelsbehandlingJaAvslutad(Lakemedelsbehandling lakemedelsbehandling, SvarsWrapper svar)
        throws ConverterException {
        final Svar.Delsvar delsvarHarHaft = svar.getDelsvar(LAKEMEDELSBEHANDLING_FOREKOMMIT_SVAR_ID,
            LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_ID);
        assertNotNull("HarHaft should not be null", delsvarHarHaft);
        assertEquals("HarHaft not equal", lakemedelsbehandling.getHarHaft(), getBooleanContent(delsvarHarHaft));

        final Svar.Delsvar delsvarPagar = svar.getDelsvar(LAKEMEDELSBEHANDLING_PAGAR_SVAR_ID, LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_ID);
        assertNotNull("Pagar should not be null", delsvarPagar);
        assertEquals("Pagar not equal", lakemedelsbehandling.getPagar(), getBooleanContent(delsvarPagar));

        final Svar.Delsvar delsvarAktuell = svar.getDelsvar(LAKEMEDELSBEHANDLING_AKTUELL_SVAR_ID, LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_ID);
        assertNull("Aktuell should be null", delsvarAktuell);

        final Svar.Delsvar delsvarPagatt = svar.getDelsvar(LAKEMEDELSBEHANDLING_MER_3_AR_SVAR_ID, LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_ID);
        assertNull("Pagar should be null", delsvarPagatt);

        final Svar.Delsvar delsvarEffekt = svar.getDelsvar(LAKEMEDELSBEHANDLING_EFFEKT_SVAR_ID, LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_ID);
        assertNull("Effekt should be null", delsvarEffekt);

        final Svar.Delsvar delsvarFoljsamhet = svar.getDelsvar(LAKEMEDELSBEHANDLING_FOLJSAMHET_SVAR_ID,
            LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_ID);
        assertNull("Foljsamhet should be null", delsvarFoljsamhet);

        final Svar.Delsvar delsvarAvslutadTidpunkt = svar.getDelsvar(LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID,
            LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_ID);
        assertNotNull("AvslutadTidpunkt should not be null", delsvarAvslutadTidpunkt);
        assertEquals("AvslutadTidpunkt not equal", lakemedelsbehandling.getAvslutadTidpunkt(),
            getStringContent(delsvarAvslutadTidpunkt));

        final Svar.Delsvar delsvarAvslutadOrsak = svar.getDelsvar(LAKEMEDELSBEHANDLING_AVSLUTAD_SVAR_ID,
            LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_ID);
        assertNotNull("AvslutadOrsak should not be null", delsvarAvslutadOrsak);
        assertEquals("AvslutadOrsak not equal", lakemedelsbehandling.getAvslutadOrsak(), getStringContent(delsvarAvslutadOrsak));
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
