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

package se.inera.intyg.common.ag114.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_ARBETSFORMAGA_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_BEDOMNING_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_DIAGNOS_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_GRUNDFORMU_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_KONTAKT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_OVRIGT_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.CATEGORY_SYSSELSATTNING_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.OVRIGT_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_HEADER_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_DIAGNOS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
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

    private Ag114UtlatandeV1 internalCertificate;

    @BeforeEach
    void setUp() {
        when(textProvider.get(anyString())).thenReturn("Test string");

        internalToCertificate = new InternalToCertificate();
        internalCertificate = Ag114UtlatandeV1.builder()
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
    void shallIncludeCategoryGrundForMedicinsktUnderlag() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(0, actualCertificate.getData().get(CATEGORY_GRUNDFORMU_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIntygetBaseratPa() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(1, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAnnatBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(2, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_ANNATBESKRIVNING_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategorySysselsattning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(3, actualCertificate.getData().get(CATEGORY_SYSSELSATTNING_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSysselsattningTyp() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(4, actualCertificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionNuvarandeArbete() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(5, actualCertificate.getData().get(NUVARANDE_ARBETE_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryDiagnos() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(6, actualCertificate.getData().get(CATEGORY_DIAGNOS_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOnskaFormedlaDiagnos() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(7, actualCertificate.getData().get(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiagnos() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(8, actualCertificate.getData().get(TYP_AV_DIAGNOS_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryArbetsformaga() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(9, actualCertificate.getData().get(CATEGORY_ARBETSFORMAGA_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionNedsattArbetsformaga() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(10, actualCertificate.getData().get(NEDSATT_ARBETSFORMAGA_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetsformagaTrotsSjukdom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(11, actualCertificate.getData().get(ARBETSFORMAGA_TROTS_SJUKDOM_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetsformagaTrotsSjukdomBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(12, actualCertificate.getData().get(ARBETSFORMAGA_TROTS_SJUKDOM_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryBedomning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(13, actualCertificate.getData().get(CATEGORY_BEDOMNING_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSjukskrivningsgradHeader() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(14, actualCertificate.getData().get(SJUKSKRIVNINGSGRAD_HEADER_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSjukskrivningsgrad() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(15, actualCertificate.getData().get(SJUKSKRIVNINGSGRAD_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSjukskrivningsperiod() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(16, actualCertificate.getData().get(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSjukskrivningsperiodMessage() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(17, actualCertificate.getData().get(SJUKSKRIVNINGSGRAD_PERIOD_MESSAGE_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryOvrigt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(18, actualCertificate.getData().get(CATEGORY_OVRIGT_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOvrigaUpplysningar() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(19, actualCertificate.getData().get(OVRIGT_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryKontakt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(20, actualCertificate.getData().get(CATEGORY_KONTAKT_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionKontaktMedArbetsgivaren() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(21, actualCertificate.getData().get(KONTAKT_ONSKAS_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAnledningTillKontakt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(22, actualCertificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID).getIndex());
    }
}
