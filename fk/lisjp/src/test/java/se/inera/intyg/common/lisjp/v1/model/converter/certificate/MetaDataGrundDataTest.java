/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.lisjp.v1.model.converter.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.lisjp.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.lisjp.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class MetaDataGrundDataTest {

    @Mock
    WebcertModuleService moduleService;
    private LisjpUtlatandeV1 internalCertificate;
    private GrundData grundData;
    private CertificateTextProvider texts;

    @BeforeEach
    void setup() {
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Nested
    class CommonMetadata {

        @BeforeEach
        void createInternalCertificateToConvert() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();

        }

        @Test
        void shouldIncludeCertificateId() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(internalCertificate.getId(), certificate.getMetadata().getId());
        }

        @Test
        void shouldIncludeCertificateType() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(internalCertificate.getTyp(), certificate.getMetadata().getType());
        }

        @Test
        void shouldIncludeCertificateTypeName() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals("FK 7804", certificate.getMetadata().getTypeName());
        }

        @Test
        void shouldIncludeCertificateTypeVersion() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(internalCertificate.getTextVersion(), certificate.getMetadata().getTypeVersion());
        }

        @Test
        void shouldIncludeCertificateName() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals("Läkarintyg för sjukpenning", certificate.getMetadata().getName());
        }

        @Test
        void shouldIncludeCertificateDescription() {
            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertTrue(certificate.getMetadata().getDescription().trim().length() > 0, "Should contain description");
        }
    }

    @Nested
    class ValidateUnit {

        private Vardenhet unit;

        @BeforeEach
        void createInternalCertificateToConvert() {
            unit = new Vardenhet();

            grundData.getSkapadAv().setVardenhet(unit);

            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        @Test
        void shallIncludeUnitId() {
            final var expectedUnitId = "UnitID";
            unit.setEnhetsid(expectedUnitId);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedUnitId, certificate.getMetadata().getUnit().getUnitId());
        }

        @Test
        void shallIncludeUnitName() {
            final var expectedUnitName = "UnitName";
            unit.setEnhetsnamn(expectedUnitName);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedUnitName, certificate.getMetadata().getUnit().getUnitName());
        }

        @Test
        void shallIncludeUnitAddress() {
            final var expectedUnitAddress = "UnitAddress";
            unit.setPostadress(expectedUnitAddress);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedUnitAddress, certificate.getMetadata().getUnit().getAddress());
        }

        @Test
        void shallIncludeUnitZipCode() {
            final var expectedUnitZipCode = "UnitZipCode";
            unit.setPostnummer(expectedUnitZipCode);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedUnitZipCode, certificate.getMetadata().getUnit().getZipCode());
        }

        @Test
        void shallIncludeUnitCity() {
            final var expectedUnitCity = "UnitCity";
            unit.setPostort(expectedUnitCity);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedUnitCity, certificate.getMetadata().getUnit().getCity());
        }

        @Test
        void shallIncludeUnitEmail() {
            final var expectedUnitEmail = "UnitEmail";
            unit.setEpost(expectedUnitEmail);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedUnitEmail, certificate.getMetadata().getUnit().getEmail());
        }

        @Test
        void shallIncludeUnitPhoneNumber() {
            final var expectedUnitPhoneNumber = "UnitPhoneNumber";
            unit.setTelefonnummer(expectedUnitPhoneNumber);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedUnitPhoneNumber, certificate.getMetadata().getUnit().getPhoneNumber());
        }
    }

    @Nested
    class ValidateIssuedBy {

        @BeforeEach
        void createInternalCertificateToConvert() {
            internalCertificate = LisjpUtlatandeV1.builder()
                .setGrundData(grundData)
                .setId("id")
                .setTextVersion("TextVersion")
                .build();
        }

        @Test
        void shallIncludePersonId() {
            final var expectedPersonId = "PersonId";
            grundData.getSkapadAv().setPersonId(expectedPersonId);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedPersonId, certificate.getMetadata().getIssuedBy().getPersonId());
        }

        @Test
        void shallIncludeFullName() {
            final var expectedFullName = "Fullname";
            grundData.getSkapadAv().setFullstandigtNamn(expectedFullName);

            final var certificate = InternalToCertificate.convert(internalCertificate, texts);

            assertEquals(expectedFullName, certificate.getMetadata().getIssuedBy().getFullName());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GrundDataTest {

        private LisjpUtlatandeV1 internalCertificate;
        private Vardenhet vardenhet;

        @BeforeEach
        void setup() {
            final var grundData = new GrundData();
            final var patient = new Patient();
            patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
            grundData.setPatient(patient);
            final var hosPersonal = new HoSPersonal();
            vardenhet = new Vardenhet();
            hosPersonal.setVardenhet(vardenhet);
            grundData.setSkapadAv(hosPersonal);

            internalCertificate =
                LisjpUtlatandeV1.builder()
                    .setId("id")
                    .setTextVersion("1.0")
                    .setGrundData(grundData)
                    .build();
        }

        Stream<String> values() {
            return Stream.of("test string", "", null);
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitAddress(String expectedValue) {
            vardenhet.setPostadress(expectedValue);

            final var certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostadress());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitCity(String expectedValue) {
            vardenhet.setPostort(expectedValue);

            final var certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostort());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitZipCode(String expectedValue) {
            vardenhet.setPostnummer(expectedValue);

            final var certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getPostnummer());
        }

        @ParameterizedTest
        @MethodSource("values")
        void shouldIncludeUnitPhonenumber(String expectedValue) {
            vardenhet.setTelefonnummer(expectedValue);

            final var certificate = CertificateBuilder.create()
                .metadata(MetaDataGrundData.toCertificate(internalCertificate, texts))
                .build();

            final var updatedCertificate = CertificateToInternal.convert(certificate, internalCertificate, moduleService);

            assertEquals(expectedValue, updatedCertificate.getGrundData().getSkapadAv().getVardenhet().getTelefonnummer());
        }
    }
}
