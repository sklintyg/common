/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag7804.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.ag7804.model.internal.Prognos;
import se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.ag7804.model.internal.PrognosTyp;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionAktivitetsbegransningar;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionAnnatGrundForMUBeskrivning;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionArbetsresor;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionArbetstidsforlaggning;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionAtgarder;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionAtgarderBeskrivning;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionAvstangningSmittskydd;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionBehovAvSjukskrivning;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionDiagnosOnskasFormedlas;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionForsakringsmedicinsktBeslutsstod;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionFunktionsnedsattning;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionIntygetBaseratPa;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionKontakt;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionKontaktBeskrivning;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionMotiveringArbetstidsforlaggning;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionPagaendeBehandling;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionPlaneradBehandling;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionPrognos;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionPrognosTimePeriod;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionSysselsattning;
import se.inera.intyg.common.ag7804.v1.model.converter.certificate.question.QuestionSysselsattningYrke;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class CertificateToInternalTest {

    private Certificate certificate;
    private Ag7804UtlatandeV1 expectedInternalCertificate;

    @Mock
    private CertificateTextProvider textProvider;

    @Mock
    private WebcertModuleService webcertModuleService;

    private static final String DIAGNOSIS_DISPLAYNAME = "Namn att visa upp";
    private static final String DIAGNOSIS_DESCRIPTION = "Beskrivning med egen text";

    @BeforeEach
    void setup() {
        when(textProvider.get(anyString())).thenReturn("Test string");
        final var internalLocalDateInterval = new InternalLocalDateInterval();
        internalLocalDateInterval.setFrom(new InternalDate(LocalDate.now()));
        internalLocalDateInterval.setTom(new InternalDate(LocalDate.now()));
        expectedInternalCertificate = Ag7804UtlatandeV1.builder()
            .setId("id")
            .setTextVersion("textVersion")
            .setGrundData(getGrundData())
            .setAvstangningSmittskydd(true)
            .setAnnatGrundForMUBeskrivning("beskrivning")
            .setSysselsattning(List.of(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
            .setNuvarandeArbete("nuvarandeArbete")
            .setDiagnoser(
                Arrays.asList(
                    Diagnos.create("", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F501", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F502", "ICD-10", DIAGNOSIS_DESCRIPTION, DIAGNOSIS_DISPLAYNAME))
            )
            .setFunktionsnedsattning("funktionsnedsattning")
            .setPagaendeBehandling("pagaendeBehandling")
            .setPlaneradBehandling("planeradBehandling")
            .setSjukskrivningar(List.of(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, internalLocalDateInterval)))
            .setForsakringsmedicinsktBeslutsstod("forskningsmedicinsktBeslut")
            .setArbetstidsforlaggning(true)
            .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
            .setArbetsresor(true)
            .setPrognos(Prognos.create(PrognosTyp.PROGNOS_OKLAR, PrognosDagarTillArbeteTyp.DAGAR_30))
            .setArbetslivsinriktadeAtgarder(List.of(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
            .setArbetslivsinriktadeAtgarderBeskrivning("arbetslivsinriktadeAtgarderBeskrivning")
            .setOvrigt("ovrigt")
            .setKontaktMedAg(true)
            .setAnledningTillKontakt("anledningTillKontakt")
            .setAktivitetsbegransning("aktivitetsbegransning")
            .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
            .setTelefonkontaktMedPatienten(new InternalDate(LocalDate.now()))
            .setJournaluppgifter(new InternalDate(LocalDate.now()))
            .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
            .setOnskarFormedlaDiagnos(true)
            .build();

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, textProvider))
            .addElement(
                QuestionAvstangningSmittskydd.toCertificate(expectedInternalCertificate.getAvstangningSmittskydd(), 0, textProvider)
            )
            .addElement(QuestionIntygetBaseratPa.toCertificate(expectedInternalCertificate, 0, textProvider))
            .addElement(
                QuestionAnnatGrundForMUBeskrivning.toCertificate(expectedInternalCertificate.getAnnatGrundForMUBeskrivning(), 0,
                    textProvider))
            .addElement(QuestionSysselsattning.toCertificate(expectedInternalCertificate.getSysselsattning(), 0, textProvider))
            .addElement(QuestionSysselsattningYrke.toCertificate(expectedInternalCertificate.getNuvarandeArbete(), 0, textProvider))
            .addElement(QuestionDiagnoser.toCertificate(expectedInternalCertificate.getDiagnoser(), 0, textProvider))
            .addElement(QuestionPagaendeBehandling.toCertificate(expectedInternalCertificate.getPagaendeBehandling(), 0, textProvider))
            .addElement(QuestionPlaneradBehandling.toCertificate(expectedInternalCertificate.getPlaneradBehandling(), 0, textProvider))
            .addElement(
                QuestionAktivitetsbegransningar.toCertificate(expectedInternalCertificate.getAktivitetsbegransning(), 0, textProvider))
            .addElement(
                QuestionFunktionsnedsattning.toCertificate(expectedInternalCertificate.getFunktionsnedsattning(), 0, textProvider))
            .addElement(QuestionBehovAvSjukskrivning.toCertificate(expectedInternalCertificate.getSjukskrivningar(), 0, textProvider,
                expectedInternalCertificate.getGrundData().getRelation()))
            .addElement(
                QuestionForsakringsmedicinsktBeslutsstod.toCertificate(expectedInternalCertificate.getForsakringsmedicinsktBeslutsstod(), 0,
                    textProvider))
            .addElement(
                QuestionArbetstidsforlaggning.toCertificate(expectedInternalCertificate.getArbetstidsforlaggning(), 0, textProvider))
            .addElement(
                QuestionMotiveringArbetstidsforlaggning.toCertificate(expectedInternalCertificate.getArbetstidsforlaggningMotivering(), 0,
                    textProvider))
            .addElement(QuestionArbetsresor.toCertificate(expectedInternalCertificate.getArbetsresor(), 0, textProvider))
            .addElement(QuestionPrognos.toCertificate(expectedInternalCertificate.getPrognos(), 0, textProvider))
            .addElement(QuestionPrognosTimePeriod.toCertificate(expectedInternalCertificate.getPrognos(), 0, textProvider))
            .addElement(QuestionAtgarder.toCertificate(expectedInternalCertificate.getArbetslivsinriktadeAtgarder(), 0, textProvider))
            .addElement(
                QuestionAtgarderBeskrivning.toCertificate(expectedInternalCertificate.getArbetslivsinriktadeAtgarderBeskrivning(), 0,
                    textProvider))
            .addElement(QuestionOvrigt.toCertificate(expectedInternalCertificate.getOvrigt(), 0, textProvider))
            .addElement(QuestionKontakt.toCertificate(expectedInternalCertificate.getKontaktMedAg(), 0, textProvider))
            .addElement(QuestionKontaktBeskrivning.toCertificate(expectedInternalCertificate.getAnledningTillKontakt(), 0, textProvider))
            .addElement(
                QuestionDiagnosOnskasFormedlas.toCertificate(expectedInternalCertificate.getOnskarFormedlaDiagnos(), 0, textProvider))
            .build();
    }

    private static GrundData getGrundData() {
        final var grundData = new GrundData();
        final var hosPersonal = new HoSPersonal();
        final var vardenhet = new Vardenhet();
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        hosPersonal.setVardenhet(vardenhet);
        grundData.setSkapadAv(hosPersonal);
        grundData.setPatient(patient);
        return grundData;
    }

    @Test
    void shallIncludeId() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getId(), actualInternalCertificate.getId());
    }

    @Test
    void shallIncludeTextVersion() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getTextVersion(), actualInternalCertificate.getTextVersion());
    }

    @Test
    void shallIncludeGrundData() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertNotNull(actualInternalCertificate.getGrundData(), "GrundData is missing!");
    }

    @Test
    void shallIncludeAvstangningSmittskydd() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getAvstangningSmittskydd(), actualInternalCertificate.getAvstangningSmittskydd());
    }

    @Test
    void shallIncludeAnnatGrundForMUBeskrivning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getAnnatGrundForMUBeskrivning(),
            actualInternalCertificate.getAnnatGrundForMUBeskrivning());
    }

    @Test
    void shallIncludeSysselsattning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getSysselsattning(), actualInternalCertificate.getSysselsattning());
    }

    @Test
    void shallIncludeNuvarandeArbete() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getNuvarandeArbete(), actualInternalCertificate.getNuvarandeArbete());
    }

    @Test
    void shallIncludeDiagnoser() {
        doReturn(DIAGNOSIS_DISPLAYNAME).when(webcertModuleService).getDescriptionFromDiagnosKod(anyString(), anyString());
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getDiagnoser(), actualInternalCertificate.getDiagnoser());
    }

    @Test
    void shallIncludeFunktionsnedsattning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getFunktionsnedsattning(), actualInternalCertificate.getFunktionsnedsattning());
    }

    @Test
    void shallIncludePagaendeBehandling() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getPagaendeBehandling(), actualInternalCertificate.getPagaendeBehandling());
    }

    @Test
    void shallIncludePlaneradBehandling() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getPlaneradBehandling(), actualInternalCertificate.getPlaneradBehandling());
    }

    @Test
    void shallIncludeSjukskrivningar() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getSjukskrivningar(), actualInternalCertificate.getSjukskrivningar());
    }


    @Test
    void shallIncludeForsakringsmedicinsktBeslutsstod() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getForsakringsmedicinsktBeslutsstod(),
            actualInternalCertificate.getForsakringsmedicinsktBeslutsstod());
    }

    @Test
    void shallIncludeArbetstidsforlaggning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getArbetstidsforlaggning(),
            actualInternalCertificate.getArbetstidsforlaggning());
    }

    @Test
    void shallIncludeArbetstidsforlaggningMotivering() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getArbetstidsforlaggningMotivering(),
            actualInternalCertificate.getArbetstidsforlaggningMotivering());
    }

    @Test
    void shallIncludeArbetsresor() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getArbetsresor(), actualInternalCertificate.getArbetsresor());
    }

    @Test
    void shallIncludePrognos() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getPrognos(), actualInternalCertificate.getPrognos());
    }

    @Test
    void shallIncludeArbetslivsinriktadeAtgarder() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getArbetslivsinriktadeAtgarder(),
            actualInternalCertificate.getArbetslivsinriktadeAtgarder());
    }

    @Test
    void shallIncludeArbetslivsinriktadeAtgarderBeskrivning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getArbetslivsinriktadeAtgarderBeskrivning(),
            actualInternalCertificate.getArbetslivsinriktadeAtgarderBeskrivning());
    }

    @Test
    void shallIncludeOvrigt() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getOvrigt(), actualInternalCertificate.getOvrigt());
    }

    @Test
    void shallIncludeKontaktMedFk() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getKontaktMedAg(), actualInternalCertificate.getKontaktMedAg());
    }

    @Test
    void shallIncludeAnledningTillKontakt() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getAnledningTillKontakt(), actualInternalCertificate.getAnledningTillKontakt());
    }

    @Test
    void shallIncludeAktivitetsbegransning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getAktivitetsbegransning(), actualInternalCertificate.getAktivitetsbegransning());
    }

    @Test
    void shallIncludeUndersokningAvPatienten() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getUndersokningAvPatienten(),
            actualInternalCertificate.getUndersokningAvPatienten());
    }

    @Test
    void shallIncludeTelefonkontaktMedPatienten() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getTelefonkontaktMedPatienten(),
            actualInternalCertificate.getTelefonkontaktMedPatienten());
    }

    @Test
    void shallIncludeJournaluppgifter() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getJournaluppgifter(),
            actualInternalCertificate.getJournaluppgifter());
    }

    @Test
    void shallIncludeAnnatGrundForMU() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getAnnatGrundForMU(),
            actualInternalCertificate.getAnnatGrundForMU());
    }

    @Test
    void shallIncludeDiagnosOnskasFormedlas() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate,
            expectedInternalCertificate, webcertModuleService);
        assertEquals(expectedInternalCertificate.getOnskarFormedlaDiagnos(),
            actualInternalCertificate.getOnskarFormedlaDiagnos());
    }
}
