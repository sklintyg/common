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
package se.inera.intyg.common.af00213.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_DELSVAR_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_DELSVAR_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_CATEGORY_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_DELSVAR_ID_32;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

@DisplayName("Should convert AF00213Utlatande to Certificate")
class InternalToCertificateTest {

    private GrundData grundData;
    private CertificateTextProvider texts;
    private Af00213UtlatandeV1 internalCertificate;

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
        
        internalCertificate = Af00213UtlatandeV1.builder()
            .setGrundData(grundData)
            .setId("id")
            .setTextVersion("TextVersion")
            .setHarFunktionsnedsattning(true)
            .setFunktionsnedsattning("Funktionsnedsättning")
            .setHarAktivitetsbegransning(false)
            .setAktivitetsbegransning("Aktivitetsbegränsning")
            .setHarUtredningBehandling(true)
            .setUtredningBehandling("Utredning och behandling")
            .setHarArbetetsPaverkan(false)
            .setArbetetsPaverkan("Arbetspåverkan")
            .setOvrigt("Övrigt")
            .build();

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");
    }

    @Test
    void shallIncludeMetaData() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertNotNull(actualCertificate.getMetadata(), "Metadata is missing!");
    }

    @Test
    void shallIncludeCategoryFunktionsnedsattningInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(0, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHarFunktionsnedsattningInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(1, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_11).getIndex());
    }

    @Test
    void shallIncludeQuestionFunktionsnedsattningInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(2, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_DELSVAR_ID_12).getIndex());
    }

    @Test
    void shallIncludeCategoryAktivitetsbegransningInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(3, actualCertificate.getData().get(AKTIVITETSBEGRANSNING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHarAktivitetsbegransningInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(4, actualCertificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_21).getIndex());
    }

    @Test
    void shallIncludeQuestionAktivitetsbegransningInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(5, actualCertificate.getData().get(AKTIVITETSBEGRANSNING_DELSVAR_ID_22).getIndex());
    }

    @Test
    void shallIncludeCategoryUtredningBehandlingInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(6, actualCertificate.getData().get(UTREDNING_BEHANDLING_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHarUtredningBehandlingInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(7, actualCertificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_31).getIndex());
    }

    @Test
    void shallIncludeQuestionUtredningBehandlingInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(8, actualCertificate.getData().get(UTREDNING_BEHANDLING_DELSVAR_ID_32).getIndex());
    }

    @Test
    void shallIncludeCategoryArbetspaverkanInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(9, actualCertificate.getData().get(ARBETETS_PAVERKAN_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHarArbetspaverkanInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(10, actualCertificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_41).getIndex());
    }

    @Test
    void shallIncludeQuestionArbetspaverkanInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(11, actualCertificate.getData().get(ARBETETS_PAVERKAN_DELSVAR_ID_42).getIndex());
    }

    @Test
    void shallIncludeCategoryOvrigtInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(12, actualCertificate.getData().get(OVRIGT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionOvrigtInCorrectPosition() {
        final var actualCertificate = InternalToCertificate.convert(internalCertificate, texts);
        assertEquals(13, actualCertificate.getData().get(OVRIGT_DELSVAR_ID_5).getIndex());
    }
}
