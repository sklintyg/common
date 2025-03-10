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
package se.inera.intyg.common.luae_fs.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class InternalToCertificateTest {

    @Mock
    private CertificateTextProvider textProvider;

    private InternalToCertificate internalToCertificate;

    private LuaefsUtlatandeV1 internalCertificate;

    @BeforeEach
    void setUp() {
        when(textProvider.get(anyString())).thenReturn("Test string");

        internalToCertificate = new InternalToCertificate();
        internalCertificate = LuaefsUtlatandeV1.builder()
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
    void shallIncludeQuestionDiagnoser() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(8, actualCertificate.getData().get(DIAGNOS_SVAR_ID_6).getIndex());
    }

    @Test
    void shallIncludeCategoryFunktionsnedsattning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(9, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningDebut() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(10, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_DEBUT_SVAR_ID_15).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningPaverkan() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(11, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_PAVERKAN_SVAR_ID_16).getIndex());
    }

    @Test
    void shallIncludeCategoryOvrigt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(12, actualCertificate.getData().get(OVRIGT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOvrigt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(13, actualCertificate.getData().get(OVRIGT_SVAR_ID_25).getIndex());
    }

    @Test
    void shallIncludeCategoryKontakt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(14, actualCertificate.getData().get(KONTAKT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionKontaktOnskas() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(15, actualCertificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_26).getIndex());
    }

    @Test
    void shallIncludeQuestionKontaktAnledning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(16, actualCertificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26).getIndex());
    }
}
