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
package se.inera.intyg.common.luae_fs.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_ICD_10_ID;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.internal.Underlag;
import se.inera.intyg.common.fkparent.model.internal.Underlag.UnderlagsTyp;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionAnnatBeskrivning;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionFunktionsnedsattningDebut;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionFunktionsnedsattningPaverkan;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionKannedomOmPatient;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionKontaktAnledning;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionKontaktOnskas;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionMotiveringTillInteBaseratPaUndersokning;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionOvrigt;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionUnderlag;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionUnderlagFinns;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionUtlatandeBaseratPa;
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
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

    private Certificate certificate;
    private LuaefsUtlatandeV1 expectedInternalCertificate;

    @Mock
    private CertificateTextProvider textProvider;

    @Mock
    private WebcertModuleService webcertModuleService;

    @InjectMocks
    private CertificateToInternal certificateToInternal;

    private static final String DIAGNOSIS_DISPLAYNAME = "Namn att visa upp";

    @BeforeEach
    void setup() {
        when(textProvider.get(anyString())).thenReturn("Test string");

        expectedInternalCertificate = LuaefsUtlatandeV1.builder()
            .setId("id")
            .setTextVersion("textVersion")
            .setGrundData(getGrundData())
            .setUndersokningAvPatienten(new InternalDate("2022-12-15"))
            .setJournaluppgifter(new InternalDate("2022-12-16"))
            .setAnhorigsBeskrivningAvPatienten(new InternalDate("2022-12-17"))
            .setAnnatGrundForMU(new InternalDate("2022-12-18"))
            .setAnnatGrundForMUBeskrivning("Annat beskrivning")
            .setMotiveringTillInteBaseratPaUndersokning("Motivering till ej bserat pa undersokning")
            .setKannedomOmPatient(new InternalDate(("2022-12-19")))
            .setUnderlagFinns(true)
            .setUnderlag(List.of(Underlag.create(UnderlagsTyp.OVRIGT, new InternalDate("2022-12-16"), "underlagstyp ovrigt")))
            .setDiagnoser(
                Arrays.asList(
                    Diagnos.create("F500", DIAGNOS_ICD_10_ID, "Beskrivning1", DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F501", DIAGNOS_ICD_10_ID, "Beskrivning2", DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F502", DIAGNOS_ICD_10_ID, "Beskrivning3", DIAGNOSIS_DISPLAYNAME))
            )
            .setFunktionsnedsattningDebut("Funktionsnedsattning debut")
            .setFunktionsnedsattningPaverkan("Funktionsnedsattning paverkan")
            .setOvrigt("ovrigt")
            .setKontaktMedFk(true)
            .setAnledningTillKontakt("Anledning till kontakt")
            .build();

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, textProvider))
            .addElement(QuestionUtlatandeBaseratPa.toCertificate(expectedInternalCertificate.getUndersokningAvPatienten(),
                expectedInternalCertificate.getJournaluppgifter(), expectedInternalCertificate.getAnhorigsBeskrivningAvPatienten(),
                expectedInternalCertificate.getAnnatGrundForMU(), 0, textProvider))
            .addElement(QuestionAnnatBeskrivning
                .toCertificate(expectedInternalCertificate.getAnnatGrundForMUBeskrivning(), 0, textProvider))
            .addElement(QuestionMotiveringTillInteBaseratPaUndersokning
                .toCertificate(expectedInternalCertificate.getMotiveringTillInteBaseratPaUndersokning(), 0, textProvider))
            .addElement(QuestionKannedomOmPatient.toCertificate(expectedInternalCertificate.getKannedomOmPatient(), 0, textProvider))
            .addElement(QuestionUnderlagFinns.toCertificate(expectedInternalCertificate.getUnderlagFinns(), 0, textProvider))
            .addElement(QuestionUnderlag.toCertificate(expectedInternalCertificate.getUnderlag(), 0, textProvider))
            .addElement(QuestionDiagnoser.toCertificate(expectedInternalCertificate.getDiagnoser(), 0, textProvider))
            .addElement(QuestionFunktionsnedsattningDebut.toCertificate(expectedInternalCertificate.getFunktionsnedsattningDebut(), 0,
                textProvider))
            .addElement(QuestionFunktionsnedsattningPaverkan.toCertificate(expectedInternalCertificate.getFunktionsnedsattningPaverkan(), 0,
                textProvider))
            .addElement(QuestionOvrigt.toCertificate(expectedInternalCertificate.getOvrigt(), 0, textProvider))
            .addElement(QuestionKontaktOnskas.toCertificate(expectedInternalCertificate.getKontaktMedFk(), 0, textProvider))
            .addElement(QuestionKontaktAnledning.toCertificate(expectedInternalCertificate.getAnledningTillKontakt(), 0,
                textProvider))
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
    void shallUndersokningAvPatienten() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getUndersokningAvPatienten(), actualInternalCertificate.getUndersokningAvPatienten());
    }

    @Test
    void shallIncludeJournalUppgifter() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getJournaluppgifter(), actualInternalCertificate.getJournaluppgifter());
    }

    @Test
    void shallIncludeAnhorigsBeskrivningAvPatienten() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAnhorigsBeskrivningAvPatienten(),
            actualInternalCertificate.getAnhorigsBeskrivningAvPatienten());
    }

    @Test
    void shallIncludeAnnatGrundForMU() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAnnatGrundForMU(), actualInternalCertificate.getAnnatGrundForMU());
    }

    @Test
    void shallIncludeAnnatGrundForMUBeskrivning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAnnatGrundForMUBeskrivning(),
            actualInternalCertificate.getAnnatGrundForMUBeskrivning());
    }

    @Test
    void shallIncludeMotiveringTillInteBaseratPaUndersokning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getMotiveringTillInteBaseratPaUndersokning(),
            actualInternalCertificate.getMotiveringTillInteBaseratPaUndersokning());
    }

    @Test
    void shallIncludeKannedomOmPatient() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getKannedomOmPatient(), actualInternalCertificate.getKannedomOmPatient());
    }

    @Test
    void shallIncludeUnderlagFinns() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getUnderlag(), actualInternalCertificate.getUnderlag());
    }

    @Test
    void shallIncludeDiagnoser() {
        doReturn(DIAGNOSIS_DISPLAYNAME).when(webcertModuleService).getDescriptionFromDiagnosKod(anyString(), anyString());
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        for (int i = 0; i < actualInternalCertificate.getDiagnoser().size(); i++) {
            assertEquals(actualInternalCertificate.getDiagnoser().get(i), expectedInternalCertificate.getDiagnoser().get(i));
        }
    }

    @Test
    void shallIncludeFunktionsnedsattningDebut() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getFunktionsnedsattningDebut(), actualInternalCertificate.getFunktionsnedsattningDebut());
    }

    @Test
    void shallIncludeFunktionsnedsattningPaverkan() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getFunktionsnedsattningPaverkan(),
            actualInternalCertificate.getFunktionsnedsattningPaverkan());
    }

    @Test
    void shallIncludeOvrigt() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getOvrigt(), actualInternalCertificate.getOvrigt());
    }

    @Test
    void shallIncludeKontaktOnskas() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getKontaktMedFk(), actualInternalCertificate.getKontaktMedFk());
    }

    @Test
    void shallIncludeKontaktAnledning() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAnledningTillKontakt(), actualInternalCertificate.getAnledningTillKontakt());
    }
}
