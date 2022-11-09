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

package se.inera.intyg.common.doi.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.doi.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionForgiftningDatum;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionForgiftningOm;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionForgiftningOrsak;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionGrunderDodsorsaksuppgifter;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionLand;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionOperation;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionOperationAnledning;
import se.inera.intyg.common.doi.v1.model.converter.certificate.question.QuestionOperationDatum;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionAntraffadDod;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionBarn;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsdatum;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsdatumSakert;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplatsBoende;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionDodsplatsKommun;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionIdentitetenStyrkt;
import se.inera.intyg.common.sos_parent.model.converter.certificate.question.QuestionOsakertDodsdatum;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class CertificateToInternalTest {

    private CertificateToInternal certificateToInternal;

    private CertificateTextProvider texts;
    private DoiUtlatandeV1 expectedInternalCertificate;
    private Certificate certificate;

    @BeforeEach
    void setUp() {
        certificateToInternal = new CertificateToInternal();
    }

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

            expectedInternalCertificate = DoiUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setIdentitetStyrkt("IdentitetStyrkt")
                .setLand("Land")
                .setDodsdatumSakert(true)
                .setDodsdatum(new InternalDate(LocalDate.now()))
                .setAntraffatDodDatum(new InternalDate(LocalDate.now()))
                .setDodsplatsKommun("DodsplatsKommun")
                .setDodsplatsBoende(DodsplatsBoende.SJUKHUS)
                .setBarn(true)
                .setOperation(OmOperation.JA)
                .setOperationDatum(new InternalDate(LocalDate.now()))
                .setOperationAnledning("OperationAnledning")
                .setForgiftning(true)
                .setForgiftningOrsak(ForgiftningOrsak.OLYCKSFALL)
                .setForgiftningDatum(new InternalDate(LocalDate.now()))
                .setGrunder(List.of(Dodsorsaksgrund.UNDERSOKNING_FORE_DODEN))
                .build();

            texts = Mockito.mock(CertificateTextProvider.class);
            when(texts.get(Mockito.any(String.class))).thenReturn("Test string");

            certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, texts))
                .addElement(QuestionIdentitetenStyrkt.toCertificate(expectedInternalCertificate.getIdentitetStyrkt(), 0, texts))
                .addElement(QuestionLand.toCertificate(expectedInternalCertificate.getLand(), 0, texts))
                .addElement(QuestionDodsdatumSakert.toCertificate(expectedInternalCertificate.getDodsdatumSakert(), 0, texts))
                .addElement(QuestionDodsdatum.toCertificate(expectedInternalCertificate.getDodsdatum().asLocalDate(), 0, texts))
                .addElement(QuestionAntraffadDod.toCertificate(expectedInternalCertificate.getAntraffatDodDatum().asLocalDate(), 0, texts))
                .addElement(
                    QuestionDodsplatsKommun.toCertificate(Collections.emptyList(), expectedInternalCertificate.getDodsplatsKommun(), 0,
                        texts))
                .addElement(QuestionDodsplatsBoende.toCertificate(expectedInternalCertificate.getDodsplatsBoende(), 0, texts))
                .addElement(QuestionBarn.toCertificate(expectedInternalCertificate.getGrundData().getPatient().getPersonId(),
                    expectedInternalCertificate.getBarn(), 0, texts))
                .addElement(QuestionOperation.toCertificate(expectedInternalCertificate.getOperation(), 0, texts))
                .addElement(QuestionOperationDatum.toCertificate(expectedInternalCertificate.getOperationDatum().asLocalDate(), 0, texts))
                .addElement(QuestionOperationAnledning.toCertificate(expectedInternalCertificate.getOperationAnledning(), 0, texts))
                .addElement(QuestionForgiftningOm.toCertificate(expectedInternalCertificate.getForgiftning(), 0, texts))
                .addElement(QuestionForgiftningOrsak.toCertificate(expectedInternalCertificate.getForgiftningOrsak(), 0, texts))
                .addElement(
                    QuestionForgiftningDatum.toCertificate(expectedInternalCertificate.getForgiftningDatum().asLocalDate(), 0, texts))
                .addElement(QuestionGrunderDodsorsaksuppgifter.toCertificate(expectedInternalCertificate.getGrunder(), 0, texts))
                .build();
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
        void shallIncludeIdentitetStyrkt() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate,
                expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getIdentitetStyrkt(), actualInternalCertificate.getIdentitetStyrkt());
        }

        @Test
        void shallIncludeLand() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getLand(), actualInternalCertificate.getLand());
        }

        @Test
        void shallIncludeDodsdatumSakert() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getDodsdatumSakert(), actualInternalCertificate.getDodsdatumSakert());
        }

        @Test
        void shallIncludeDodsdatum() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getDodsdatum(), actualInternalCertificate.getDodsdatum());
        }

        @Test
        void shallIncludeAntraffadDoddatum() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getAntraffatDodDatum(), actualInternalCertificate.getAntraffatDodDatum());
        }

        @Test
        void shallIncludeDodsplatsKommun() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getDodsplatsKommun(), actualInternalCertificate.getDodsplatsKommun());
        }

        @Test
        void shallIncludeDodsplatsBoende() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getDodsplatsBoende(), actualInternalCertificate.getDodsplatsBoende());
        }

        @Test
        void shallIncludeBarn() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getBarn(), actualInternalCertificate.getBarn());
        }

        @Test
        void shallIncludeOperation() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getOperation(), actualInternalCertificate.getOperation());
        }

        @Test
        void shallIncludeOperationDatum() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getOperationDatum(), actualInternalCertificate.getOperationDatum());
        }

        @Test
        void shallIncludeOperationAnledning() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getOperationAnledning(), actualInternalCertificate.getOperationAnledning());
        }

        @Test
        void shallIncludeForgiftningOm() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getForgiftning(), actualInternalCertificate.getForgiftning());
        }

        @Test
        void shallIncludeForgiftningOrsak() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getForgiftningOrsak(), actualInternalCertificate.getForgiftningOrsak());
        }

        @Test
        void shallIncludeForgiftningDatum() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getForgiftningDatum(), actualInternalCertificate.getForgiftningDatum());
        }

        @Test
        void shallIncludeDodsorsakGrunder() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getGrunder(), actualInternalCertificate.getGrunder());
        }
    }

    @Nested
    class AlternativeScenarioDodsdatumOsakertWithValue {

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

            expectedInternalCertificate = DoiUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .setIdentitetStyrkt("IdentitetStyrkt")
                .setDodsdatumSakert(false)
                .setDodsdatum(new InternalDate("2022-00-00"))
                .setAntraffatDodDatum(new InternalDate(LocalDate.now()))
                .setDodsplatsKommun("DodsplatsKommun")
                .setDodsplatsBoende(DodsplatsBoende.SJUKHUS)
                .setBarn(true)
                .build();

            texts = Mockito.mock(CertificateTextProvider.class);
            when(texts.get(Mockito.any(String.class))).thenReturn("Test string");

            certificate = CertificateBuilder.create()
                .metadata(
                    MetaDataGrundData.toCertificate(expectedInternalCertificate,
                        texts))
                .addElement(QuestionIdentitetenStyrkt.toCertificate(expectedInternalCertificate.getIdentitetStyrkt(), 0, texts))
                .addElement(QuestionDodsdatumSakert.toCertificate(expectedInternalCertificate.getDodsdatumSakert(), 0, texts))
                .addElement(QuestionOsakertDodsdatum.toCertificate(expectedInternalCertificate.getDodsdatum().toString(), 0, texts))
                .addElement(QuestionAntraffadDod.toCertificate(expectedInternalCertificate.getAntraffatDodDatum().asLocalDate(), 0, texts))
                .addElement(
                    QuestionDodsplatsKommun.toCertificate(Collections.emptyList(), expectedInternalCertificate.getDodsplatsKommun(), 0,
                        texts))
                .addElement(QuestionDodsplatsBoende.toCertificate(expectedInternalCertificate.getDodsplatsBoende(), 0, texts))
                .addElement(QuestionBarn.toCertificate(expectedInternalCertificate.getGrundData().getPatient().getPersonId(),
                    expectedInternalCertificate.getBarn(), 0, texts))
                .build();
        }

        @Test
        void shallIncludeOsakertDodsdatum() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate,
                expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getDodsdatum(), actualInternalCertificate.getDodsdatum());
        }
    }

    @Nested
    class AlternativeScenarioDodsdatumOsakertNull {

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

            expectedInternalCertificate = DoiUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

            texts = Mockito.mock(CertificateTextProvider.class);
            when(texts.get(Mockito.any(String.class))).thenReturn("Test string");

            certificate = CertificateBuilder.create()
                .metadata(
                    MetaDataGrundData.toCertificate(expectedInternalCertificate,
                        texts))
                .addElement(QuestionOsakertDodsdatum.toCertificate(null, 0, texts))
                .build();
        }

        @Test
        void shallIncludeOsakertDodsdatum() {
            final var actualInternalCertificate = certificateToInternal.convert(certificate,
                expectedInternalCertificate);
            assertEquals(expectedInternalCertificate.getDodsdatum(), actualInternalCertificate.getDodsdatum());
        }
    }
}
