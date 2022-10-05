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

package se.inera.intyg.common.db.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.db.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionAntraffadDod;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionBarn;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionDodsdatum;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionDodsdatumSakert;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionDodsplatsBoende;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionDodsplatsKommun;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionExplosivtAvlagsnat;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionExplosivtImplantat;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionIdentitetenStyrkt;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionPolisanmalan;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionUndersokningYttre;
import se.inera.intyg.common.db.v1.model.converter.certificate.QuestionUndersokningsdatum;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class CertificateToInternalTest {

    private CertificateTextProvider texts;
    private DbUtlatandeV1 expectedInternalCertificate;
    private Certificate certificate;

    @BeforeEach
    private void setup() {
        final var grundData = new GrundData();
        final var hosPersonal = new HoSPersonal();
        final var vardenhet = new Vardenhet();
        final var patient = new Patient();
        hosPersonal.setVardenhet(vardenhet);
        grundData.setSkapadAv(hosPersonal);
        grundData.setPatient(patient);
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());

        expectedInternalCertificate = DbUtlatandeV1.builder()
            .setGrundData(grundData)
            .setId("id")
            .setTextVersion("TextVersion")
            .setIdentitetStyrkt("IdentitetStyrkt")
            .setDodsdatumSakert(true)
            .setDodsdatum(new InternalDate(LocalDate.now()))
            .setAntraffatDodDatum(new InternalDate(LocalDate.now()))
            .setDodsplatsKommun("DodsplatsKommun")
            .setDodsplatsBoende(DodsplatsBoende.SJUKHUS)
            .setBarn(true)
            .setExplosivImplantat(true)
            .setExplosivAvlagsnat(true)
            .setUndersokningYttre(Undersokning.UNDERSOKNING_GJORT_KORT_FORE_DODEN)
            .setUndersokningDatum(new InternalDate(LocalDate.now()))
            .setPolisanmalan(true)
            .build();

        texts = Mockito.mock(CertificateTextProvider.class);
        when(texts.get(Mockito.any(String.class))).thenReturn("Test string");

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, texts))
            .addElement(QuestionIdentitetenStyrkt.toCertificate(expectedInternalCertificate.getIdentitetStyrkt(), 0, texts))
            .addElement(QuestionDodsdatumSakert.toCertificate(expectedInternalCertificate.getDodsdatumSakert(), 0, texts))
            .addElement(QuestionDodsdatum.toCertificate(expectedInternalCertificate.getDodsdatum().asLocalDate(), 0, texts))
            .addElement(QuestionAntraffadDod.toCertificate(expectedInternalCertificate.getAntraffatDodDatum().asLocalDate(), 0, texts))
            // TODO: Retrieve municipalities
            .addElement(
                QuestionDodsplatsKommun.toCertificate(Collections.emptyList(), expectedInternalCertificate.getDodsplatsKommun(), 0, texts))
            .addElement(QuestionDodsplatsBoende.toCertificate(expectedInternalCertificate.getDodsplatsBoende(), 0, texts))
            .addElement(QuestionBarn.toCertificate(expectedInternalCertificate.getGrundData().getPatient().getPersonId(),
                expectedInternalCertificate.getBarn(), 0, texts))
            .addElement(QuestionExplosivtImplantat.toCertificate(expectedInternalCertificate.getExplosivImplantat(), 0, texts))
            .addElement(QuestionExplosivtAvlagsnat.toCertificate(expectedInternalCertificate.getExplosivAvlagsnat(), 0, texts))
            .addElement(QuestionUndersokningYttre.toCertificate(expectedInternalCertificate.getUndersokningYttre(), 0, texts))
            .addElement(
                QuestionUndersokningsdatum.toCertificate(expectedInternalCertificate.getUndersokningDatum().asLocalDate(), 0, texts))
            .addElement(QuestionPolisanmalan.toCertificate(expectedInternalCertificate.getPolisanmalan(), 0, texts))
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
    void shallIncludeIdentitetStyrkt() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getIdentitetStyrkt(), actualInternalCertificate.getIdentitetStyrkt());
    }

    @Test
    void shallIncludeDodsdatumSakert() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDodsdatumSakert(), actualInternalCertificate.getDodsdatumSakert());
    }

    @Test
    void shallIncludeDodsdatum() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDodsdatum(), actualInternalCertificate.getDodsdatum());
    }

    @Test
    void shallIncludeAntraffadDoddatum() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAntraffatDodDatum(), actualInternalCertificate.getAntraffatDodDatum());
    }

    @Test
    void shallIncludeDodsplatsKommun() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDodsplatsKommun(), actualInternalCertificate.getDodsplatsKommun());
    }

    @Test
    void shallIncludeDodsplatsBoende() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getDodsplatsBoende(), actualInternalCertificate.getDodsplatsBoende());
    }

    @Test
    void shallIncludeBarn() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getBarn(), actualInternalCertificate.getBarn());
    }

    @Test
    void shallIncludeExplosivtImplantat() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getExplosivImplantat(), actualInternalCertificate.getExplosivImplantat());
    }

    @Test
    void shallIncludeExplosivtAvlagsnat() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getExplosivAvlagsnat(), actualInternalCertificate.getExplosivAvlagsnat());
    }

    @Test
    void shallIncludeUndersokningYttre() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getUndersokningYttre(), actualInternalCertificate.getUndersokningYttre());
    }

    @Test
    void shallIncludeUndersokningsdatum() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getUndersokningDatum(), actualInternalCertificate.getUndersokningDatum());
    }

    @Test
    void shallIncludePolisanmalan() {
        final var actualInternalCertificate = CertificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getPolisanmalan(), actualInternalCertificate.getPolisanmalan());
    }
}