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
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionFunktionsnedsattningIntellektuell;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionKannedomOmPatient;
import se.inera.intyg.common.luae_na.v1.model.converter.certificate.question.QuestionMotiveringTillInteBaseratPaUndersokning;
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
import se.inera.intyg.schemas.contract.Personnummer;

class CertificateToInternalTest {


    private CertificateTextProvider texts;
    private LuaenaUtlatandeV1 expectedInternalCertificate;
    private Certificate certificate;
    private static String annanBeskrivning = "annanBeskrivning";
    private static String motivering = "motivering";

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
                .setFunktionsnedsattningIntellektuell("funktionsnedsattningIntellektuell")
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
                .addElement(QuestionFunktionsnedsattningIntellektuell.toCertificate(
                    expectedInternalCertificate.getFunktionsnedsattningIntellektuell(), 0, texts))
                .build();
        }

        @Test
        void shallIncludeId() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getId(), actualInternalCertificate.getId());
        }

        @Test
        void shallIncludeTextVersion() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getTextVersion(), actualInternalCertificate.getTextVersion());
        }

        @Test
        void shallIncludeGrundData() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertNotNull(actualInternalCertificate.getGrundData(), "GrundData is missing!");
        }

        @Test
        void shallIncludeUndersokningAvPatienten() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getUndersokningAvPatienten(), expectedInternalCertificate.getUndersokningAvPatienten());
        }

        @Test
        void shallIncludeJournaluppgifter() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getJournaluppgifter(), expectedInternalCertificate.getJournaluppgifter());
        }

        @Test
        void shallIncludeAnhorigBeskrivningAvPatient() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getAnhorigsBeskrivningAvPatienten(),
                expectedInternalCertificate.getAnhorigsBeskrivningAvPatienten());
        }

        @Test
        void shallIncludeAnnatGrundForMU() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getAnnatGrundForMU(), expectedInternalCertificate.getAnnatGrundForMU());
        }

        @Test
        void shallIncludeAnnatGrundForMUBeskrivning() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getAnnatGrundForMUBeskrivning(),
                expectedInternalCertificate.getAnnatGrundForMUBeskrivning());
        }

        @Test
        void shallIncludeAnnatGrundForMUMotivering() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getMotiveringTillInteBaseratPaUndersokning(),
                expectedInternalCertificate.getMotiveringTillInteBaseratPaUndersokning());
        }

        @Test
        void shallIncludeKannedomOmPatient() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getKannedomOmPatient(),
                expectedInternalCertificate.getKannedomOmPatient());
        }

        @Test
        void shallIncludeUnderlagFinns() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getUnderlagFinns(),
                expectedInternalCertificate.getUnderlagFinns());
        }

        @Test
        void shallIncludeFunktionsnedsattningIntellektuell() {
            final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(actualInternalCertificate.getFunktionsnedsattningIntellektuell(),
                expectedInternalCertificate.getFunktionsnedsattningIntellektuell());
        }
    }
}