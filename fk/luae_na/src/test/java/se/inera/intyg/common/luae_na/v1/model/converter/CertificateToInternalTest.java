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

package se.inera.intyg.common.luae_na.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionAktivitetsbegransningar;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnosForNyBedomning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionDiagnosgrund;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFormagaTrotsBegransning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionForslagTillAtgard;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningAnnan;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningBalansKoordination;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningIntellektuell;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningKommunikation;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningKoncentration;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningPsykisk;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningSynHorselTal;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionKannedomOmPatient;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingAvslutadBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingPagaendeBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingPlaneradBehandling;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskBehandlingSubstansintag;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMedicinskaForutsattningarForArbete;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMotiveringTillInteBaseratPaUndersokning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionNyBedomningDiagnosgrund;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionSjukdomsforlopp;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlag;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlagBaseratPa;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionUnderlagFinns;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class CertificateToInternalTest {


    private CertificateTextProvider texts;
    private LuaenaUtlatandeV1 expectedInternalCertificate;
    private Certificate certificate;
    private static String annanBeskrivning = "annanBeskrivning";
    private static String motivering = "motivering";
    private final String DIAGNOSIS_DESCRIPTION = "Beskrivning med egen text";
    private final String DIAGNOSIS_DISPLAYNAME = "Namn att visa upp";

    @Mock
    private WebcertModuleService moduleService;

    @Nested
    class HappyScenario {

        @BeforeEach
        private void setup() {
            final var grundData = new GrundData();
            final var hosPersonal = new HoSPersonal();
            final var vardenhet = new Vardenhet();
            final var patient = new Patient();
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);
            grundData.setPatient(patient);
            patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());

            expectedInternalCertificate = LuaenaUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
                .setJournaluppgifter(new InternalDate(LocalDate.now()))
                .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
                .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
                .setMotiveringTillInteBaseratPaUndersokning(motivering)
                .setAnnatGrundForMUBeskrivning(annanBeskrivning)
                .setKannedomOmPatient(new InternalDate(LocalDate.now()))
                .setUnderlagFinns(true)
                .setUnderlag(List.of(
                    Underlag.create(
                        UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    ),
                    Underlag.create(
                        UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    ),
                    Underlag.create(
                        UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    )
                ))
                .setSjukdomsforlopp("sjukdomsforlopp")
                .setDiagnoser(
                    Arrays.asList(
                        Diagnos.create("", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME))
                )
                .setDiagnosgrund("Diagnosbakgrund test")
                .setNyBedomningDiagnosgrund(true)
                .setDiagnosForNyBedomning("Diagnos för ny bedömning")
                .setFunktionsnedsattningIntellektuell("funktionsnedsattningIntellektuell")
                .setFunktionsnedsattningKommunikation("funktionsnedsattningKommunikation")
                .setFunktionsnedsattningKoncentration("funktionsnedsattningKoncentration")
                .setFunktionsnedsattningPsykisk("funktionsnedsattningPsykisk")
                .setFunktionsnedsattningSynHorselTal("funktionsnedsattningSynHorselTal")
                .setFunktionsnedsattningBalansKoordination("funktionsnedsattningKoordination")
                .setFunktionsnedsattningAnnan("funktionsnedsattningAnnan")
                .setAktivitetsbegransning("aktivitetsbegransningar")
                .setAvslutadBehandling("avslutadBehandling")
                .setPagaendeBehandling("pagaendeBehandling")
                .setPlaneradBehandling("planeradBehandling")
                .setSubstansintag("substansintag")
                .setMedicinskaForutsattningarForArbete("medicinskaForutsattningarForArbete")
                .setFormagaTrotsBegransning("formagaTrotsBegransning")
                .setForslagTillAtgard("forslagTillAtgard")
                .setOvrigt("ovrigt")
                .build();

            texts = Mockito.mock(CertificateTextProvider.class);
            when(texts.get(Mockito.any(String.class))).thenReturn("Test string");

            certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, texts))
                .addElement(QuestionUnderlagBaseratPa.toCertificate(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now()),
                    new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now()), 0, texts
                ))
                .addElement(QuestionAnnatBeskrivning.toCertificate(annanBeskrivning, 0, texts))
                .addElement(QuestionMotiveringTillInteBaseratPaUndersokning.toCertificate(motivering, 0, texts))
                .addElement(QuestionKannedomOmPatient.toCertificate(new InternalDate(LocalDate.now()), 0, texts))
                .addElement(QuestionUnderlagFinns.toCertificate(true, 0, texts))
                .addElement(QuestionUnderlag.toCertificate(List.of(
                    Underlag.create(
                        UnderlagsTyp.UTREDNING_AV_ANNAN_SPECIALISTKLINIK,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    ),
                    Underlag.create(
                        UnderlagsTyp.NEUROPSYKIATRISKT_UTLATANDE,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    ),
                    Underlag.create(
                        UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT,
                        new InternalDate(LocalDate.now()),
                        "hamtasFran"
                    )), 0, texts))
                .addElement(QuestionSjukdomsforlopp.toCertificate(expectedInternalCertificate.getSjukdomsforlopp(), 0, texts))
                .addElement(QuestionDiagnoser.toCertificate(
                    Arrays.asList(
                        Diagnos.create("", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                        Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME)),
                    0, texts
                ))
                .addElement(QuestionDiagnosgrund.toCertificate("Diagnosbakgrund test", 0, texts))
                .addElement(QuestionNyBedomningDiagnosgrund.toCertificate(true, 0, texts))
                .addElement(QuestionDiagnosForNyBedomning.toCertificate("Diagnos för ny bedömning", 0, texts))
                .addElement(QuestionFunktionsnedsattningIntellektuell.toCertificate(
                    expectedInternalCertificate.getFunktionsnedsattningIntellektuell(), 0, texts))
                .addElement(QuestionFunktionsnedsattningKommunikation.toCertificate(
                    expectedInternalCertificate.getFunktionsnedsattningKommunikation(), 0, texts))
                .addElement(QuestionFunktionsnedsattningKoncentration.toCertificate(
                    expectedInternalCertificate.getFunktionsnedsattningKoncentration(), 0, texts))
                .addElement(QuestionFunktionsnedsattningPsykisk.toCertificate(
                    expectedInternalCertificate.getFunktionsnedsattningPsykisk(), 0, texts))
                .addElement(QuestionFunktionsnedsattningSynHorselTal.toCertificate(
                    expectedInternalCertificate.getFunktionsnedsattningSynHorselTal(), 0, texts))
                .addElement(QuestionFunktionsnedsattningBalansKoordination.toCertificate(
                    expectedInternalCertificate.getFunktionsnedsattningBalansKoordination(), 0, texts))
                .addElement(QuestionFunktionsnedsattningAnnan.toCertificate(
                    expectedInternalCertificate.getFunktionsnedsattningAnnan(), 0, texts))
                .addElement(QuestionAktivitetsbegransningar.toCertificate(
                    expectedInternalCertificate.getAktivitetsbegransning(), 0, texts)
                )
                .addElement(QuestionMedicinskBehandlingAvslutadBehandling.toCertificate(
                    expectedInternalCertificate.getAvslutadBehandling(), 0, texts
                ))
                .addElement(QuestionMedicinskBehandlingPagaendeBehandling.toCertificate(
                    expectedInternalCertificate.getPagaendeBehandling(), 0, texts
                ))
                .addElement(QuestionMedicinskBehandlingPlaneradBehandling.toCertificate(
                    expectedInternalCertificate.getPlaneradBehandling(), 0, texts
                ))
                .addElement(QuestionMedicinskBehandlingSubstansintag.toCertificate(
                    expectedInternalCertificate.getSubstansintag(), 0, texts
                ))
                .addElement(QuestionMedicinskaForutsattningarForArbete.toCertificate(
                    expectedInternalCertificate.getMedicinskaForutsattningarForArbete(), 0, texts)
                )
                .addElement(
                    QuestionFormagaTrotsBegransning.toCertificate(expectedInternalCertificate.getFormagaTrotsBegransning(), 0, texts)
                )
                .addElement(
                    QuestionForslagTillAtgard.toCertificate(expectedInternalCertificate.getForslagTillAtgard(), 0, texts)
                )
                .addElement(
                    QuestionOvrigt.toCertificate(expectedInternalCertificate.getOvrigt(), 0, texts)
                )
                .build();
        }

        @Test
        void shallIncludeId() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(expectedInternalCertificate.getId(), actualInternalCertificate.getId());
        }

        @Test
        void shallIncludeTextVersion() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(expectedInternalCertificate.getTextVersion(), actualInternalCertificate.getTextVersion());
        }

        @Test
        void shallIncludeGrundData() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertNotNull(actualInternalCertificate.getGrundData(), "GrundData is missing!");
        }

        @Test
        void shallIncludeUndersokningAvPatienten() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getUndersokningAvPatienten(), expectedInternalCertificate.getUndersokningAvPatienten());
        }

        @Test
        void shallIncludeJournaluppgifter() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getJournaluppgifter(), expectedInternalCertificate.getJournaluppgifter());
        }

        @Test
        void shallIncludeAnhorigBeskrivningAvPatient() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getAnhorigsBeskrivningAvPatienten(),
                expectedInternalCertificate.getAnhorigsBeskrivningAvPatienten());
        }

        @Test
        void shallIncludeAnnatGrundForMU() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getAnnatGrundForMU(), expectedInternalCertificate.getAnnatGrundForMU());
        }

        @Test
        void shallIncludeAnnatGrundForMUBeskrivning() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getAnnatGrundForMUBeskrivning(),
                expectedInternalCertificate.getAnnatGrundForMUBeskrivning());
        }

        @Test
        void shallIncludeAnnatGrundForMUMotivering() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getMotiveringTillInteBaseratPaUndersokning(),
                expectedInternalCertificate.getMotiveringTillInteBaseratPaUndersokning());
        }

        @Test
        void shallIncludeKannedomOmPatient() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getKannedomOmPatient(),
                expectedInternalCertificate.getKannedomOmPatient());
        }

        @Test
        void shallIncludeUnderlagFinns() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getUnderlagFinns(),
                expectedInternalCertificate.getUnderlagFinns());
        }

        @Test
        void shallIncludeUnderlag() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getUnderlag(),
                expectedInternalCertificate.getUnderlag());
        }

        @Test
        void shallIncludeSjukdomsforlopp() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getSjukdomsforlopp(),
                expectedInternalCertificate.getSjukdomsforlopp());
        }
        @Test
        void shallIncludeDiagnoser() {
            when(moduleService.getDescriptionFromDiagnosKod(anyString(), anyString())).thenReturn(DIAGNOSIS_DISPLAYNAME);
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            for (int i = 0; i < actualInternalCertificate.getDiagnoser().size(); i++) {
                assertEquals(actualInternalCertificate.getDiagnoser().get(i),expectedInternalCertificate.getDiagnoser().get(i));
            }
        }

        @Test
        void shallIncludeDiagnosgrund() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getDiagnosgrund(),
                expectedInternalCertificate.getDiagnosgrund());
        }

        @Test
        void shallIncludeNyBedomningDiagnosgrund() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getNyBedomningDiagnosgrund(),
                expectedInternalCertificate.getNyBedomningDiagnosgrund());
        }

        @Test
        void shallIncludeDiagnosForNyBedomning() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getDiagnosForNyBedomning(),
                expectedInternalCertificate.getDiagnosForNyBedomning());
        }

        @Test
        void shallIncludeFunktionsnedsattningIntellektuell() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getFunktionsnedsattningIntellektuell(),
                expectedInternalCertificate.getFunktionsnedsattningIntellektuell());
        }

        @Test
        void shallIncludeFunktionsnedsattningKommunikation() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getFunktionsnedsattningKommunikation(),
                expectedInternalCertificate.getFunktionsnedsattningKommunikation());
        }

        @Test
        void shallIncludeFunktionsnedsattningKoncentration() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getFunktionsnedsattningKoncentration(),
                expectedInternalCertificate.getFunktionsnedsattningKoncentration());
        }

        @Test
        void shallIncludeFunktionsnedsattningPsykisk() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getFunktionsnedsattningPsykisk(),
                expectedInternalCertificate.getFunktionsnedsattningPsykisk());
        }

        @Test
        void shallIncludeFunktionsnedsattningSynHorselTal() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getFunktionsnedsattningSynHorselTal(),
                expectedInternalCertificate.getFunktionsnedsattningSynHorselTal());
        }

        @Test
        void shallIncludeFunktionsnedsattningBalansKoordination() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getFunktionsnedsattningBalansKoordination(),
                expectedInternalCertificate.getFunktionsnedsattningBalansKoordination());
        }

        @Test
        void shallIncludeFunktionsnedsattningAnnan() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getFunktionsnedsattningAnnan(),
                expectedInternalCertificate.getFunktionsnedsattningAnnan());
        }

        @Test
        void shallIncludeAktivitetsbegransningar() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getAktivitetsbegransning(),
                expectedInternalCertificate.getAktivitetsbegransning());
        }

        @Test
        void shallIncludeMedicinskaForutsattningarForArbete() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getMedicinskaForutsattningarForArbete(),
                expectedInternalCertificate.getMedicinskaForutsattningarForArbete());
        }

        @Test
        void shallIncludeFormagaTrotsBegransning() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getFormagaTrotsBegransning(), expectedInternalCertificate.getFormagaTrotsBegransning());
        }

        @Test
        void shallIncludeForslagTillAtgard() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate, moduleService);
            assertEquals(actualInternalCertificate.getForslagTillAtgard(), expectedInternalCertificate.getForslagTillAtgard());
        }

        @Test
        void shallIncludeOvrigt() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getOvrigt(), expectedInternalCertificate.getOvrigt());
        }
    }
}