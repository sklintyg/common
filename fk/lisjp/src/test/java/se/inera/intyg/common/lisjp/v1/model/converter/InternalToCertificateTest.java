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
package se.inera.intyg.common.lisjp.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ATGARDER_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOS_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKABEHANDLINGAR_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SYSSELSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
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

    private LisjpUtlatandeV1 internalCertificate;

    @BeforeEach
    void setUp() {
        when(textProvider.get(any(String.class))).thenReturn("Test string");

        internalCertificate = LisjpUtlatandeV1.builder()
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
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertNotNull(actualCertificate.getMetadata(), "Shall contain metadata");
    }

    @Test
    void shallIncludeCategorySmittbararpenning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(0, actualCertificate.getData().get(AVSTANGNING_SMITTSKYDD_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAvstangningSmittskydd() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(1, actualCertificate.getData().get(AVSTANGNING_SMITTSKYDD_SVAR_ID_27).getIndex());
    }

    @Test
    void shallIncludeCategoryGrundForMU() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(2, actualCertificate.getData().get(GRUNDFORMU_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIntygetBaseratPa() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(3, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeQuestionAnnatGrundForMUBeskrivning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(4, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeQuestionMotiveringEjUndersokning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(5, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeCategorySysselsattning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(6, actualCertificate.getData().get(SYSSELSATTNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSysselsattning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(7, actualCertificate.getData().get(TYP_AV_SYSSELSATTNING_SVAR_ID_28).getIndex());
    }

    @Test
    void shallIncludeQuestionSysselsattningYrke() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(8, actualCertificate.getData().get(NUVARANDE_ARBETE_SVAR_ID_29).getIndex());
    }

    @Test
    void shallIncludeCategoryDiagnos() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(9, actualCertificate.getData().get(DIAGNOS_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiagnoser() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(10, actualCertificate.getData().get(DIAGNOS_SVAR_ID_6).getIndex());
    }

    @Test
    void shallIncludeCategoryFunktionsnedsattning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(11, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(12, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_SVAR_ID_35).getIndex());
    }

    @Test
    void shallIncludeQuestionAktivitetsbegransningar() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(13, actualCertificate.getData().get(AKTIVITETSBEGRANSNING_SVAR_ID_17).getIndex());
    }

    @Test
    void shallIncludeCategoryMedicinskaBehandlingar() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(14, actualCertificate.getData().get(MEDICINSKABEHANDLINGAR_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionPagaendeBehandling() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(15, actualCertificate.getData().get(PAGAENDEBEHANDLING_SVAR_ID_19).getIndex());
    }

    @Test
    void shallIncludeQuestionPlaneradBehandling() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(16, actualCertificate.getData().get(PLANERADBEHANDLING_SVAR_ID_20).getIndex());
    }

    @Test
    void shallIncludeCategoryBedomning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(17, actualCertificate.getData().get(BEDOMNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionBehovAvSjukskrivning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(18, actualCertificate.getData().get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32).getIndex());
    }

    @Test
    void shallIncludeQuestionMotiveringTidigtStartdatum() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(19, actualCertificate.getData().get(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID_32).getIndex());
    }

    @Test
    void shallIncludQuestionForsakringsmedicinsktBeslutsstod() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(20, actualCertificate.getData().get(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetstidsforlaggning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(21, actualCertificate.getData().get(ARBETSTIDSFORLAGGNING_SVAR_ID_33).getIndex());
    }

    @Test
    void shallIncludeQuestionMotiveringArbetstidsforlaggning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(22, actualCertificate.getData().get(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetsresor() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(23, actualCertificate.getData().get(ARBETSRESOR_SVAR_ID_34).getIndex());
    }

    @Test
    void shallIncludeQuestionPrognos() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(24, actualCertificate.getData().get(PROGNOS_SVAR_ID_39).getIndex());
    }

    @Test
    void shallIncludeQuestionPrognosTimePeriod() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(25, actualCertificate.getData().get(PROGNOS_BESKRIVNING_DELSVAR_ID_39).getIndex());
    }

    @Test
    void shallIncludeCategoryAtgarder() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(26, actualCertificate.getData().get(ATGARDER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionAtgarder() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(27, actualCertificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40).getIndex());
    }

    @Test
    void shallIncludeQuestionAtgarderBeskrivning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(28, actualCertificate.getData().get(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44).getIndex());
    }

    @Test
    void shallIncludeCategoryOvrigt() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(29, actualCertificate.getData().get(OVRIGT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludQuestionOvrigt() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(30, actualCertificate.getData().get(OVRIGT_SVAR_ID_25).getIndex());
    }

    @Test
    void shallIncludeCategoryKontakt() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(31, actualCertificate.getData().get(KONTAKT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionKontakt() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(32, actualCertificate.getData().get(KONTAKT_ONSKAS_SVAR_ID_26).getIndex());
    }

    @Test
    void shallIncludeQuestionKontaktBeskrivning() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(33, actualCertificate.getData().get(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26).getIndex());
    }
}
