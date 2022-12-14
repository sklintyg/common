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

package se.inera.intyg.common.ts_bas.v7.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionNattblindhet;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionProgressivOgonsjukdom;
import se.inera.intyg.common.ts_bas.v7.model.converter.certificate.question.QuestionSynfaltsdefekter;
import se.inera.intyg.common.ts_bas.v7.model.internal.Syn;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class CertificateToInternalTest {

    private CertificateToInternal certificateToInternal;
    private Certificate certificate;
    private TsBasUtlatandeV7 expectedInternalCertificate;
    @Mock
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setup() {
        certificateToInternal = new CertificateToInternal();

        final var syn = Syn.builder()
            .setSynfaltsdefekter(true)
            .setNattblindhet(true)
            .setProgressivOgonsjukdom(true)
            .build();

        expectedInternalCertificate = TsBasUtlatandeV7.builder()
            .setId("id")
            .setTextVersion("textVersion")
            .setGrundData(getGrundData())
            .setSyn(syn)
            .build();

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, textProvider))
            .addElement(QuestionSynfaltsdefekter.toCertificate(syn, 0, textProvider))
            .addElement(QuestionNattblindhet.toCertificate(syn, 0, textProvider))
            .addElement(QuestionProgressivOgonsjukdom.toCertificate(syn, 0, textProvider))
            .build();
    }

    private static GrundData getGrundData() {
        final var grundData = new GrundData();
        final var hosPersonal = new HoSPersonal();
        final var vardenhet = new Vardenhet();
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
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
    void shallIncludeSynfaltsdefekter() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getSynfaltsdefekter(), actualInternalCertificate.getSyn().getSynfaltsdefekter());
    }

    @Test
    void shallIncludeNattblindhet() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getNattblindhet(), actualInternalCertificate.getSyn().getNattblindhet());
    }

    @Test
    void shallIncludeProgressivOgonsjukdom() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getSyn().getProgressivOgonsjukdom(),
            actualInternalCertificate.getSyn().getProgressivOgonsjukdom());
    }
}
