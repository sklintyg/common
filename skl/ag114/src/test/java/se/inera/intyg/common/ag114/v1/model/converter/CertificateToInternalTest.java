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
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_ICD_10_ID;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionAnledningTillKontakt;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionArbetsformagaTrotsSjukdom;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionArbetsformagaTrotsSjukdomBeskrivning;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionDiagnos;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionIntygetBaseratPa;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionKontaktMedArbetsgivaren;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionNedsattArbetsformaga;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionNuvarandeArbete;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionOnskaFormedlaDiagnos;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionOvrigaUpplysningar;
import se.inera.intyg.common.ag114.v1.model.converter.certificate.question.QuestionSysselsattningTyp;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
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

    private Ag114UtlatandeV1 expectedInternalCertificate;
    @Mock
    private CertificateTextProvider textProvider;
    private Certificate certificate;
    @Mock
    private WebcertModuleService webcertModuleService;
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
            .setSysselsattning(List.of(
                Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)
            ))
            .setNuvarandeArbete("nuvarandeArbete")
            .setOnskarFormedlaDiagnos(true)
            .setDiagnoser(
                List.of(Diagnos.create("F502", DIAGNOS_ICD_10_ID, "diagnosBeskrivning", null))
            )
            .setNedsattArbetsformaga("nedsattArbetsformaga")
            .setArbetsformagaTrotsSjukdom(true)
            .setArbetsformagaTrotsSjukdomBeskrivning("trotsSjukdomBeskrivning")
            .setOvrigaUpplysningar("ovrigaUpplysningar")
            .setKontaktMedArbetsgivaren(true)
            .setAnledningTillKontakt("anledningTillKontakt")
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
            .addElement(
                QuestionSysselsattningTyp.toCertificate(0, textProvider)
            )
            .addElement(
                QuestionNuvarandeArbete.toCertificate(expectedInternalCertificate.getNuvarandeArbete(), 0, textProvider)
            )
            .addElement(
                QuestionOnskaFormedlaDiagnos.toCertificate(expectedInternalCertificate.getOnskarFormedlaDiagnos(), 0, textProvider)
            )
            .addElement(
                QuestionDiagnos.toCertificate(expectedInternalCertificate.getDiagnoser(), 0, textProvider)
            )
            .addElement(
                QuestionNedsattArbetsformaga.toCertificate(expectedInternalCertificate.getNedsattArbetsformaga(), 0, textProvider)
            )
            .addElement(
                QuestionArbetsformagaTrotsSjukdom.toCertificate(expectedInternalCertificate.getArbetsformagaTrotsSjukdom(), 0, textProvider)
            )
            .addElement(
                QuestionArbetsformagaTrotsSjukdomBeskrivning.toCertificate(
                    expectedInternalCertificate.getArbetsformagaTrotsSjukdomBeskrivning(), 0, textProvider)
            )
            .addElement(
                QuestionOvrigaUpplysningar.toCertificate(expectedInternalCertificate.getOvrigaUpplysningar(), 0, textProvider)
            )
            .addElement(
                QuestionKontaktMedArbetsgivaren.toCertificate(expectedInternalCertificate.getKontaktMedArbetsgivaren(), 0, textProvider)
            )
            .addElement(
                QuestionAnledningTillKontakt.toCertificate(expectedInternalCertificate.getAnledningTillKontakt(), 0, textProvider)
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

    @Test
    void shallIncludeSysselsattningsTyp() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSysselsattning(),
            actualInternalCertificate.getSysselsattning());
    }

    @Test
    void shallIncludeNuvarandeArbete() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNuvarandeArbete(),
            actualInternalCertificate.getNuvarandeArbete());
    }

    @Test
    void shallIncludeOnskaFormedlaDiagnos() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getOnskarFormedlaDiagnos(),
            actualInternalCertificate.getOnskarFormedlaDiagnos());
    }

    @Test
    void shallIncludeDiagnos() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDiagnoser(),
            actualInternalCertificate.getDiagnoser());
    }

    @Test
    void shallIncludeNedsattArbetsformaga() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getNedsattArbetsformaga(),
            actualInternalCertificate.getNedsattArbetsformaga());
    }

    @Test
    void shallIncludeArbetsformagaTrotsSjukdom() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getArbetsformagaTrotsSjukdom(),
            actualInternalCertificate.getArbetsformagaTrotsSjukdom());
    }

    @Test
    void shallIncludeArbetsformagaTrotsSjukdomBeskrivning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getArbetsformagaTrotsSjukdomBeskrivning(),
            actualInternalCertificate.getArbetsformagaTrotsSjukdomBeskrivning());
    }

    @Test
    void shallIncludeOvrigaUpplysningar() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getOvrigaUpplysningar(),
            actualInternalCertificate.getOvrigaUpplysningar());
    }

    @Test
    void shallIncludeKontaktMedArbetsgivaren() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getKontaktMedArbetsgivaren(),
            actualInternalCertificate.getKontaktMedArbetsgivaren());
    }

    @Test
    void shallIncludeAnledningTillKontakt() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAnledningTillKontakt(),
            actualInternalCertificate.getAnledningTillKontakt());
    }
}
