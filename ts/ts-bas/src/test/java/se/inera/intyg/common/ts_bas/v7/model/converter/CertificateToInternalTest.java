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

package se.inera.intyg.common.ts_bas.v7.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.EnumSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAdhdAddDampAsbergersTourettes;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaJournaluppgifter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaLakarordinerat;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaOrdineratLakamedel;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaProvtagning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionAlkoholNarkotikaVardinsatser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBalansrubbningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionBeskrivningRiskfaktorer;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionDubbelseende;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionFunktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionFunktionsnedsattningBeskrivning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHarDiabetes;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHjarnskadaEfterTrauma;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionHjartOchKarlsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKognitivFormoga;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionKorrektionsglasensStyrka;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionMedvetandestorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionMedvetandestorningBeskrivning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNattblindhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNedsattNjurfunktion;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNystagmus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionOtillrackligRorelseFormoga;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionProgressivOgonsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionPsykiskSjukdomStorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionPsykiskUtvecklingsstorning;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionRiskfaktorerForStroke;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSomnOchVakenhetsstorningar;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynfaltsdefekter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpa;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynskarpaSkickasSeparat;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTeckenPaNeurologiskSjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionTidpunktVardPaSjukhus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionUppfattaSamtal4Meter;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardatsPaSjukhus;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardatsPaSjukhusOrsak;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionVardinrattningensNamn;
import se.inera.intyg.common.ts_bas.v7.model.internal.Diabetes;
import se.inera.intyg.common.ts_bas.v7.model.internal.Funktionsnedsattning;
import se.inera.intyg.common.ts_bas.v7.model.internal.HjartKarl;
import se.inera.intyg.common.ts_bas.v7.model.internal.HorselBalans;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvser;
import se.inera.intyg.common.ts_bas.v7.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_bas.v7.model.internal.Kognitivt;
import se.inera.intyg.common.ts_bas.v7.model.internal.Medvetandestorning;
import se.inera.intyg.common.ts_bas.v7.model.internal.NarkotikaLakemedel;
import se.inera.intyg.common.ts_bas.v7.model.internal.Neurologi;
import se.inera.intyg.common.ts_bas.v7.model.internal.Njurar;
import se.inera.intyg.common.ts_bas.v7.model.internal.Psykiskt;
import se.inera.intyg.common.ts_bas.v7.model.internal.Sjukhusvard;
import se.inera.intyg.common.ts_bas.v7.model.internal.SomnVakenhet;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v7.model.internal.Synskarpevarden;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.common.ts_bas.v7.model.internal.Utvecklingsstorning;
import se.inera.intyg.common.ts_bas.v7.model.internal.Vardkontakt;
import se.inera.intyg.common.ts_parent.codes.IdKontrollKod;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class CertificateToInternalTest {

    private CertificateToInternal certificateToInternal;
    private Certificate certificate;
    private TsBasUtlatandeV7 expectedInternalCertificate;
    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setup() {
        certificateToInternal = new CertificateToInternal();

        final var syn = Syn.builder()
            .setSynfaltsdefekter(true)
            .setNattblindhet(true)
            .setProgressivOgonsjukdom(true)
            .setDiplopi(true)
            .setNystagmus(true)
            .setSynskarpaSkickasSeparat(true)
            .setKorrektionsglasensStyrka(true)
            .setBinokulart(
                Synskarpevarden.builder()
                    .setUtanKorrektion(2.0)
                    .setMedKorrektion(2.0)
                    .build()
            )
            .setHogerOga(
                Synskarpevarden.builder()
                    .setUtanKorrektion(2.0)
                    .setMedKorrektion(2.0)
                    .setKontaktlins(true)
                    .build()
            )
            .setVansterOga(
                Synskarpevarden.builder()
                    .setUtanKorrektion(2.0)
                    .setMedKorrektion(2.0)
                    .setKontaktlins(true)
                    .build()
            )
            .build();

        final var horselBalans = HorselBalans.builder()
            .setBalansrubbningar(true)
            .setSvartUppfattaSamtal4Meter(true)
            .build();

        final var funktionsnedsattning = Funktionsnedsattning.builder()
            .setFunktionsnedsattning(true)
            .setBeskrivning("beskrivning")
            .setOtillrackligRorelseformaga(true)
            .build();

        final var hjartKarl = HjartKarl.builder()
            .setHjartKarlSjukdom(true)
            .setHjarnskadaEfterTrauma(true)
            .setRiskfaktorerStroke(true)
            .setBeskrivningRiskfaktorer("beskrivning")
            .build();

        final var diabetes = Diabetes.builder()
            .setHarDiabetes(true)
            .setDiabetesTyp("diabetesTyp")
            .setKost(true)
            .setInsulin(true)
            .setTabletter(true)
            .build();

        final var medvetandestorning = Medvetandestorning.builder()
            .setMedvetandestorning(true)
            .setBeskrivning("beskrivning")
            .build();

        final var narkotikaLakemedel = NarkotikaLakemedel.builder()
            .setTeckenMissbruk(true)
            .setForemalForVardinsats(true)
            .setProvtagningBehovs(true)
            .setLakarordineratLakemedelsbruk(true)
            .setLakemedelOchDos("Ipren")
            .build();

        final var sjukhusVard = Sjukhusvard.builder()
            .setSjukhusEllerLakarkontakt(true)
            .setTidpunkt("Igår")
            .setVardinrattning("Akuten AB")
            .setAnledning("Migrän")
            .build();

        final var utvecklingsstorning = Utvecklingsstorning.builder()
            .setPsykiskUtvecklingsstorning(true)
            .setHarSyndrom(true)
            .build();

        expectedInternalCertificate = TsBasUtlatandeV7.builder()
            .setId("id")
            .setTextVersion("textVersion")
            .setGrundData(getGrundData())
            .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvserKategori.IAV1, IntygAvserKategori.IAV2)))
            .setSyn(syn)
            .setVardkontakt(Vardkontakt.create(null, IdKontrollKod.FORETAG_ELLER_TJANSTEKORT.getCode()))
            .setHorselBalans(horselBalans)
            .setFunktionsnedsattning(funktionsnedsattning)
            .setHjartKarl(hjartKarl)
            .setDiabetes(diabetes)
            .setNeurologi(Neurologi.create(true))
            .setMedvetandestorning(medvetandestorning)
            .setNjurar(Njurar.create(true))
            .setKognitivt(Kognitivt.create(true))
            .setSomnVakenhet(SomnVakenhet.create(true))
            .setNarkotikaLakemedel(narkotikaLakemedel)
            .setPsykiskt(Psykiskt.create(true))
            .setUtvecklingsstorning(utvecklingsstorning)
            .setSjukhusvard(sjukhusVard)
            .build();

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, textProvider))
            .addElement(
                QuestionIntygetAvser.toCertificate(expectedInternalCertificate.getIntygAvser(), 0, textProvider)
            )
            .addElement(QuestionSynfaltsdefekter.toCertificate(syn, 0, textProvider))
            .addElement(QuestionNattblindhet.toCertificate(syn, 0, textProvider))
            .addElement(QuestionProgressivOgonsjukdom.toCertificate(syn, 0, textProvider))
            .addElement(QuestionDubbelseende.toCertificate(syn, 0, textProvider))
            .addElement(QuestionNystagmus.toCertificate(syn, 0, textProvider))
            .addElement(QuestionSynskarpaSkickasSeparat.toCertificate(syn, 0, textProvider))
            .addElement(QuestionKorrektionsglasensStyrka.toCertificate(syn, 0, textProvider))
            .addElement(QuestionIdentitetStyrktGenom.toCertificate(
                expectedInternalCertificate.getVardkontakt(), 0, textProvider))
            .addElement(QuestionSynskarpa.toCertificate(syn, 0, textProvider))
            .addElement(QuestionBalansrubbningar.toCertificate(horselBalans, 0, textProvider))
            .addElement(QuestionUppfattaSamtal4Meter.toCertificate(horselBalans, 0, textProvider))
            .addElement(QuestionFunktionsnedsattning.toCertificate(funktionsnedsattning, 0, textProvider))
            .addElement(QuestionFunktionsnedsattningBeskrivning.toCertificate(funktionsnedsattning, 0, textProvider))
            .addElement(QuestionOtillrackligRorelseFormoga.toCertificate(funktionsnedsattning, 0, textProvider))
            .addElement(QuestionHjartOchKarlsjukdom.toCertificate(hjartKarl, 0, textProvider))
            .addElement(QuestionHjarnskadaEfterTrauma.toCertificate(hjartKarl, 0, textProvider))
            .addElement(QuestionRiskfaktorerForStroke.toCertificate(hjartKarl, 0, textProvider))
            .addElement(QuestionBeskrivningRiskfaktorer.toCertificate(hjartKarl, 0, textProvider))
            .addElement(QuestionHarDiabetes.toCertificate(diabetes, 0, textProvider))
            .addElement(QuestionDiabetesTyp.toCertificate(diabetes, 0, textProvider))
            .addElement(QuestionDiabetesBehandling.toCertificate(diabetes, 0, textProvider))
            .addElement(QuestionTeckenPaNeurologiskSjukdom.toCertificate(Neurologi.create(true), 0, textProvider))
            .addElement(QuestionMedvetandestorning.toCertificate(medvetandestorning, 0, textProvider))
            .addElement(QuestionMedvetandestorningBeskrivning.toCertificate(medvetandestorning, 0, textProvider))
            .addElement(QuestionNedsattNjurfunktion.toCertificate(Njurar.create(true), 0, textProvider))
            .addElement(QuestionKognitivFormoga.toCertificate(Kognitivt.create(true), 0, textProvider))
            .addElement(QuestionSomnOchVakenhetsstorningar.toCertificate(SomnVakenhet.create(true), 0, textProvider))
            .addElement(QuestionAlkoholNarkotikaJournaluppgifter.toCertificate(narkotikaLakemedel, 0, textProvider))
            .addElement(QuestionAlkoholNarkotikaVardinsatser.toCertificate(narkotikaLakemedel, 0, textProvider))
            .addElement(QuestionAlkoholNarkotikaProvtagning.toCertificate(narkotikaLakemedel, 0, textProvider))
            .addElement(QuestionAlkoholNarkotikaLakarordinerat.toCertificate(narkotikaLakemedel, 0, textProvider))
            .addElement(QuestionAlkoholNarkotikaOrdineratLakamedel.toCertificate(narkotikaLakemedel, 0, textProvider))
            .addElement(QuestionPsykiskSjukdomStorning.toCertificate(Psykiskt.create(true), 0, textProvider))
            .addElement(QuestionPsykiskUtvecklingsstorning.toCertificate(utvecklingsstorning, 0, textProvider))
            .addElement(QuestionAdhdAddDampAsbergersTourettes.toCertificate(utvecklingsstorning, 0, textProvider))
            .addElement(QuestionVardatsPaSjukhus.toCertificate(sjukhusVard, 0, textProvider))
            .addElement(QuestionTidpunktVardPaSjukhus.toCertificate(sjukhusVard, 0, textProvider))
            .addElement(QuestionVardinrattningensNamn.toCertificate(sjukhusVard, 0, textProvider))
            .addElement(QuestionVardatsPaSjukhusOrsak.toCertificate(sjukhusVard, 0, textProvider))
            .build();
    }

    private static GrundData getGrundData() {
        final var grundData = new GrundData();
        final var hosPersonal = new HoSPersonal();
        final var vardenhet = new Vardenhet();
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        hosPersonal.setVardenhet(vardenhet);
        grundData.setSkapadAv(hosPersonal);
        grundData.setPatient(patient);
        return grundData;
    }

    @Test
    void shallIncludeId() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getId(), actualInternalCertificate.getId());
    }

    @Test
    void shallIncludeTextVersion() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getTextVersion(), actualInternalCertificate.getTextVersion());
    }

    @Test
    void shallIncludeGrundData() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertNotNull(actualInternalCertificate.getGrundData(), "GrundData is missing!");
    }

    @Test
    void shallIncludeIntygetAvser() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getIntygAvser(), expectedInternalCertificate.getIntygAvser());
    }

    @Test
    void shallIncludeSynfaltsdefekter() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getSynfaltsdefekter(), actualInternalCertificate.getSyn().getSynfaltsdefekter());
    }

    @Test
    void shallIncludeNattblindhet() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getNattblindhet(), actualInternalCertificate.getSyn().getNattblindhet());
    }

    @Test
    void shallIncludeProgressivOgonsjukdom() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getProgressivOgonsjukdom(),
            actualInternalCertificate.getSyn().getProgressivOgonsjukdom());
    }

    @Test
    void shallIncludeDubbelseende() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getDiplopi(),
            actualInternalCertificate.getSyn().getDiplopi());
    }

    @Test
    void shallIncludeNystagmus() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getNystagmus(),
            actualInternalCertificate.getSyn().getNystagmus());
    }

    @Test
    void shallIncludeSynskarpaSkickasSeparat() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getSynskarpaSkickasSeparat(),
            actualInternalCertificate.getSyn().getSynskarpaSkickasSeparat());
    }

    @Test
    void shallIncludeKorrektionsglasensStyrka() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getKorrektionsglasensStyrka(),
            actualInternalCertificate.getSyn().getKorrektionsglasensStyrka());
    }

    @Test
    void shallIncludeIdentitetStyrktGenom() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getVardkontakt(), expectedInternalCertificate.getVardkontakt(), "Vardkontakt is missing!");
    }

    @Test
    void shallIncludeSynskarpaHogerOga() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getHogerOga(),
            actualInternalCertificate.getSyn().getHogerOga());
    }

    @Test
    void shallIncludeSynskarpaVansterOga() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getVansterOga(),
            actualInternalCertificate.getSyn().getVansterOga());
    }

    @Test
    void shallIncludeSynskarpaBinokulart() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getBinokulart(),
            actualInternalCertificate.getSyn().getBinokulart());
    }

    @Test
    void shallIncludeBalansrubbningar() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHorselBalans().getBalansrubbningar(),
            actualInternalCertificate.getHorselBalans().getBalansrubbningar());
    }

    @Test
    void shallIncludeSvartAttUppfattaSamtal4Meter() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHorselBalans().getSvartUppfattaSamtal4Meter(),
            actualInternalCertificate.getHorselBalans().getSvartUppfattaSamtal4Meter());
    }

    @Test
    void shallIncludeFunktionsnedsattning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getFunktionsnedsattning().getFunktionsnedsattning(),
            actualInternalCertificate.getFunktionsnedsattning().getFunktionsnedsattning());
    }

    @Test
    void shallIncludeFunktionsnedsattningBeskrivning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getFunktionsnedsattning().getBeskrivning(),
            actualInternalCertificate.getFunktionsnedsattning().getBeskrivning());
    }

    @Test
    void shallIncludeOtillrackligRorelseformoga() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getFunktionsnedsattning().getOtillrackligRorelseformaga(),
            actualInternalCertificate.getFunktionsnedsattning().getOtillrackligRorelseformaga());
    }

    @Test
    void shallIncludeHjartOchKarlsjukdom() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHjartKarl().getHjartKarlSjukdom(),
            actualInternalCertificate.getHjartKarl().getHjartKarlSjukdom());
    }

    @Test
    void shallIncludeHjarnskadaEfterTrauma() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHjartKarl().getHjarnskadaEfterTrauma(),
            actualInternalCertificate.getHjartKarl().getHjarnskadaEfterTrauma());
    }

    @Test
    void shallIncludeRiskfaktorerStroke() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHjartKarl().getRiskfaktorerStroke(),
            actualInternalCertificate.getHjartKarl().getRiskfaktorerStroke());
    }

    @Test
    void shallIncludeBeskrivningRiskfaktorer() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHjartKarl().getBeskrivningRiskfaktorer(),
            actualInternalCertificate.getHjartKarl().getBeskrivningRiskfaktorer());
    }

    @Test
    void shallIncludeHarDiabetes() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDiabetes().getHarDiabetes(),
            actualInternalCertificate.getDiabetes().getHarDiabetes());
    }

    @Test
    void shallIncludeDiabetesTyp() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDiabetes().getDiabetesTyp(),
            actualInternalCertificate.getDiabetes().getDiabetesTyp());
    }

    @Test
    void shallIncludeDiabetesKost() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDiabetes().getKost(),
            actualInternalCertificate.getDiabetes().getKost());
    }

    @Test
    void shallIncludeDiabetesInsulin() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDiabetes().getInsulin(),
            actualInternalCertificate.getDiabetes().getInsulin());
    }

    @Test
    void shallIncludeDiabetesTabletter() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDiabetes().getTabletter(),
            actualInternalCertificate.getDiabetes().getTabletter());
    }

    @Test
    void shallIncludeTeckenPaNeurologiskSjukdom() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNeurologi().getNeurologiskSjukdom(),
            actualInternalCertificate.getNeurologi().getNeurologiskSjukdom());
    }

    @Test
    void shallIncludeMedvetandestorningar() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getMedvetandestorning().getMedvetandestorning(),
            actualInternalCertificate.getMedvetandestorning().getMedvetandestorning());
    }

    @Test
    void shallIncludeMedvetandestorningarBeskrivning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getMedvetandestorning().getBeskrivning(),
            actualInternalCertificate.getMedvetandestorning().getBeskrivning());
    }

    @Test
    void shallIncludeNedsattNjurfunktion() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNjurar().getNedsattNjurfunktion(),
            actualInternalCertificate.getNjurar().getNedsattNjurfunktion());
    }

    @Test
    void shallIncludeNedsattKognitivFormaga() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getKognitivt().getSviktandeKognitivFunktion(),
            actualInternalCertificate.getKognitivt().getSviktandeKognitivFunktion());
    }

    @Test
    void shallIncludeSomnVakenhet() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSomnVakenhet(),
            actualInternalCertificate.getSomnVakenhet());
    }

    @Test
    void shallIncludeAlkoholNarkotikaJournaluppgifter() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNarkotikaLakemedel().getTeckenMissbruk(),
            actualInternalCertificate.getNarkotikaLakemedel().getTeckenMissbruk());
    }

    @Test
    void shallIncludeAlkoholNarkotikaVardinsatser() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNarkotikaLakemedel().getForemalForVardinsats(),
            actualInternalCertificate.getNarkotikaLakemedel().getForemalForVardinsats());
    }

    @Test
    void shallIncludeProvtagningBehovs() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNarkotikaLakemedel().getProvtagningBehovs(),
            actualInternalCertificate.getNarkotikaLakemedel().getProvtagningBehovs());
    }

    @Test
    void shallIncludeLakarordineratLakemedelsbruk() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNarkotikaLakemedel().getLakarordineratLakemedelsbruk(),
            actualInternalCertificate.getNarkotikaLakemedel().getLakarordineratLakemedelsbruk());
    }

    @Test
    void shallIncludeLakemedelOchDos() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNarkotikaLakemedel().getLakemedelOchDos(),
            actualInternalCertificate.getNarkotikaLakemedel().getLakemedelOchDos());
    }
    @Test
    void shallIncludePsykiskt() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getPsykiskt().getPsykiskSjukdom(),
            actualInternalCertificate.getPsykiskt().getPsykiskSjukdom());
    }

    @Test
    void shallIncludePsykiskUtvecklingsstorning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getUtvecklingsstorning().getPsykiskUtvecklingsstorning(),
            actualInternalCertificate.getUtvecklingsstorning().getPsykiskUtvecklingsstorning());
    }

    @Test
    void shallIncludeHarSyndrom() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getUtvecklingsstorning().getHarSyndrom(),
            actualInternalCertificate.getUtvecklingsstorning().getHarSyndrom());
    }

    @Test
    void shallIncludeSjukhusvardSjukhusEllerLakarkontakt() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSjukhusvard().getSjukhusEllerLakarkontakt(),
            actualInternalCertificate.getSjukhusvard().getSjukhusEllerLakarkontakt());
    }

    @Test
    void shallIncludeSjukhusvardTidpunkt() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSjukhusvard().getTidpunkt(),
            actualInternalCertificate.getSjukhusvard().getTidpunkt());
    }

    @Test
    void shallIncludeSjukhusvardVardinrattning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSjukhusvard().getVardinrattning(),
            actualInternalCertificate.getSjukhusvard().getVardinrattning());
    }

    @Test
    void shallIncludeSjukhusvardAnlednin() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSjukhusvard().getAnledning(),
            actualInternalCertificate.getSjukhusvard().getAnledning());
    }
}
