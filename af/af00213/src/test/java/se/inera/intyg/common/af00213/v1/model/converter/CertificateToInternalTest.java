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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionAktivitetsbegransning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionArbetspaverkan;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionFunktionsnedsattning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionHarAktivitetsbegransning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionHarArbetspaverkan;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionHarFunktionsnedsattning;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionHarUtredningBehandling;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionOvrigt;
import se.inera.intyg.common.af00213.v1.model.converter.certificate.QuestionUtredningBehandling;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;

@DisplayName("Should convert Certificate to AF00213")
class CertificateToInternalTest {

    private CertificateTextProvider texts;
    private Af00213UtlatandeV1 expectedInternalCertificate;
    private Certificate certificate;

    @BeforeEach
    private void setup() {
        final var grundData = new GrundData();
        final var hosPersonal = new HoSPersonal();
        final var vardenhet = new Vardenhet();
        hosPersonal.setVardenhet(vardenhet);
        grundData.setSkapadAv(hosPersonal);

        expectedInternalCertificate = Af00213UtlatandeV1.builder()
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

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, texts))
            .addElement(QuestionHarFunktionsnedsattning.toCertificate(expectedInternalCertificate.getHarFunktionsnedsattning(), 0, texts))
            .addElement(QuestionFunktionsnedsattning.toCertificate(expectedInternalCertificate.getFunktionsnedsattning(), 0, texts))
            .addElement(QuestionHarAktivitetsbegransning.toCertificate(expectedInternalCertificate.getHarAktivitetsbegransning(), 0, texts))
            .addElement(QuestionAktivitetsbegransning.toCertificate(expectedInternalCertificate.getAktivitetsbegransning(), 0, texts))
            .addElement(QuestionHarUtredningBehandling.toCertificate(expectedInternalCertificate.getHarUtredningBehandling(), 0, texts))
            .addElement(QuestionUtredningBehandling.toCertificate(expectedInternalCertificate.getUtredningBehandling(), 0, texts))
            .addElement(QuestionHarArbetspaverkan.toCertificate(expectedInternalCertificate.getHarArbetetsPaverkan(), 0, texts))
            .addElement(QuestionArbetspaverkan.toCertificate(expectedInternalCertificate.getArbetetsPaverkan(), 0, texts))
            .addElement(QuestionOvrigt.toCerticate(expectedInternalCertificate.getOvrigt(), 0, texts))
            .build();
    }

    @Test
    void shallIncludeId() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getId(), actualInternalCertificate.getId());
    }

    @Test
    void shallIncludeTextVersion() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getTextVersion(), actualInternalCertificate.getTextVersion());
    }

    @Test
    void shallIncludeGrundData() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertNotNull(actualInternalCertificate.getGrundData(), "GrundData is missing!");
    }

    @Test
    void shallIncludeHarFunktionsNedsattning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHarFunktionsnedsattning(), actualInternalCertificate.getHarFunktionsnedsattning());
    }

    @Test
    void shallIncludeFunktionsNedsattning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getFunktionsnedsattning(), actualInternalCertificate.getFunktionsnedsattning());
    }

    @Test
    void shallIncludeHarAktivitetsbegransning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHarAktivitetsbegransning(), actualInternalCertificate.getHarAktivitetsbegransning());
    }

    @Test
    void shallIncludeAktivitetsbegransning() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAktivitetsbegransning(), actualInternalCertificate.getAktivitetsbegransning());
    }

    @Test
    void shallIncludeHarUtredningBehandling() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHarUtredningBehandling(), actualInternalCertificate.getHarUtredningBehandling());
    }

    @Test
    void shallIncludeUtredningBehandling() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getUtredningBehandling(), actualInternalCertificate.getUtredningBehandling());
    }

    @Test
    void shallIncludeHarArbetetsPaverkan() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHarArbetetsPaverkan(), actualInternalCertificate.getHarArbetetsPaverkan());
    }

    @Test
    void shallIncludeArbetetsPaverkan() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getArbetetsPaverkan(), actualInternalCertificate.getArbetetsPaverkan());
    }
}