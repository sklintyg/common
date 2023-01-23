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

package se.inera.intyg.common.ag114.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionIntygetBaseratPa;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class CertificateToInternalTest {

    private Ag114UtlatandeV1 expectedInternalCertificate;
    @Mock
    private CertificateTextProvider textProvider;
    private Certificate certificate;
    @InjectMocks
    private CertificateToInternal certificateToInternal;

    @BeforeEach
    void setUp() {
        when(textProvider.get(anyString())).thenReturn("Test string");

        expectedInternalCertificate = Ag114UtlatandeV1.builder()
            .setId("id")
            .setTextVersion("textVersion")
            .setGrundData(getGrundData())
            .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
            .setTelefonkontaktMedPatienten(new InternalDate(LocalDate.now()))
            .setJournaluppgifter(new InternalDate(LocalDate.now()))
            .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
            .setAnnatGrundForMUBeskrivning("annatBeskrivning")
            .build();

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, textProvider))
            .addElement(
                QuestionIntygetBaseratPa.toCertificate(expectedInternalCertificate.getUndersokningAvPatienten(),
                    expectedInternalCertificate.getTelefonkontaktMedPatienten(),
                    expectedInternalCertificate.getJournaluppgifter(), expectedInternalCertificate.getAnnatGrundForMU(), 0, textProvider)
            )
            .addElement(
                QuestionAnnatBeskrivning.toCertificate(expectedInternalCertificate.getAnnatGrundForMUBeskrivning(), 0, textProvider)
            )
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
    void shallIncludeUndersokningAvPatienten() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getUndersokningAvPatienten(), actualInternalCertificate.getUndersokningAvPatienten());
    }

    @Test
    void shallIncludeTelefonkontaktMedPatient() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getTelefonkontaktMedPatienten(),
            actualInternalCertificate.getTelefonkontaktMedPatienten());
    }

    @Test
    void shallIncludeJournaluppgifter() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getJournaluppgifter(), actualInternalCertificate.getJournaluppgifter());
    }

    @Test
    void shallIncludeAnnatGrundForMU() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAnnatGrundForMU(), actualInternalCertificate.getAnnatGrundForMU());
    }

    @Test
    void shallIncludeAnnatBeskrivning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAnnatGrundForMUBeskrivning(),
            actualInternalCertificate.getAnnatGrundForMUBeskrivning());
    }
}
