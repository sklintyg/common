/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.luse.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AVSLUTADBEHANDLING_DELSVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.AVSLUTADBEHANDLING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.BAKGRUND_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOSGRUND_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.DIAGNOS_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FORMAGATROTSBEGRANSNING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.OVRIGT_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.SUBSTANSINTAG_DELSVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.SUBSTANSINTAG_SVAR_ID;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.luse.v1.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.luse.v1.model.internal.LuseUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class InternalToCertificateTest {

    @Mock
    CertificateTextProvider textProvider;

    private InternalToCertificate internalToCertificate;

    private LuseUtlatandeV1 internalCertificate;

    @BeforeEach
    void setUp() {
        when(textProvider.get(anyString())).thenReturn("Test string");

        internalToCertificate = new InternalToCertificate();
        internalCertificate = LuseUtlatandeV1.builder()
            .setId("certificateId")
            .setTextVersion("1.0")
            .setGrundData(getGrundData())
            .build();
    }

    private static GrundData getGrundData() {
        final var unit = new Vardenhet();
        final var skapadAv = new HoSPersonal();
        final var patient = new Patient();
        final var grundData = new GrundData();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        skapadAv.setVardenhet(unit);
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        return grundData;
    }

    @Test
    void shallIncludeMetadata() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertNotNull(actualCertificate.getMetadata(), "Shall contain metadata");
    }

    @Test
    void shallIncludeCategoryGrundForMU() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(0, actualCertificate.getData().get(GRUNDFORMU_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionUtlatandeBaseratPa() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(1, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeQuestionAnnatBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(2, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeQuestionMotiveringTillInteBaseratPaUndersokning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(3, actualCertificate.getData().get(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeQuestionKannedomOmPatient() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(4, actualCertificate.getData().get(KANNEDOM_SVAR_ID_2).getIndex());
    }

    @Test
    void shallIncludeQuestionUnderlagFinns() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(5, actualCertificate.getData().get(UNDERLAGFINNS_SVAR_ID_3).getIndex());
    }

    @Test
    void shallIncludeQuestionUnderlag() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(6, actualCertificate.getData().get(UNDERLAG_SVAR_ID_4).getIndex());
    }

    @Test
    void shallIncludeCategoryDiagnos() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(7, actualCertificate.getData().get(DIAGNOS_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiagnos() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(8, actualCertificate.getData().get(DIAGNOS_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiagnosgrund() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(9, actualCertificate.getData().get(DIAGNOSGRUND_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiagnosgrundNyBedomning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(10, actualCertificate.getData().get(DIAGNOSGRUND_NY_BEDOMNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiagnosgrundForNyBedomning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(11, actualCertificate.getData().get(DIAGNOSGRUND_FOR_NY_BEDOMNING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryBakgrund() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(12, actualCertificate.getData().get(BAKGRUND_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSjukdomsforlopp() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(13, actualCertificate.getData().get(SJUKDOMSFORLOPP_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryFunktionsnedsattning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(14, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningIntellektuell() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(15, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningKommunikation() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(16, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningKoncentration() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(17, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningPsykisk() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(18, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningSynHorselTal() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(19, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningBalansKoordination() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(20, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningAnnan() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(21, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryAktivitetsbegransningar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(22, actualCertificate.getData().get(AKTIVITETSBEGRANSNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAktivitetsbegransningarHeader() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(23, actualCertificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAktivitetsbegransningar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(24, actualCertificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryMedicinskBehandling() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(25, actualCertificate.getData().get(MEDICINSKABEHANDLINGAR_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskBehandlingAvslutadBehandlingHeader() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(26, actualCertificate.getData().get(AVSLUTADBEHANDLING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskBehandlingAvslutadBehandling() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(27, actualCertificate.getData().get(AVSLUTADBEHANDLING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskBehandlingPagaendeBehandlingHeader() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(28, actualCertificate.getData().get(PAGAENDEBEHANDLING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskBehandlingPagaendeBehandling() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(29, actualCertificate.getData().get(PAGAENDEBEHANDLING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskBehandlingPlaneradBehandlingHeader() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(30, actualCertificate.getData().get(PLANERADBEHANDLING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskBehandlingPlaneradBehandling() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(31, actualCertificate.getData().get(PLANERADBEHANDLING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskBehandlingSubstansintagHeader() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(32, actualCertificate.getData().get(SUBSTANSINTAG_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskBehandlingSubstansintag() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(33, actualCertificate.getData().get(SUBSTANSINTAG_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryMedicinskaForutsattningarForArbete() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(34, actualCertificate.getData().get(CATEGORY_MEDICINSKAFORUTSATTNINGARFORARBETE).getIndex());
    }

    @Test
    void shallIncludeQuestionMedicinskaForutsattningarForArbete() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(35, actualCertificate.getData().get(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFormagaTrotsBegransning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(36, actualCertificate.getData().get(FORMAGATROTSBEGRANSNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryOvrigt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(37, actualCertificate.getData().get(OVRIGT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOvrigt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(38, actualCertificate.getData().get(OVRIGT_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryKontakt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(39, actualCertificate.getData().get(KONTAKT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionKontaktMedFk() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(40, actualCertificate.getData().get(KONTAKT_ONSKAS_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAnledningTillKontakt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(41, actualCertificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID).getIndex());
    }
}
