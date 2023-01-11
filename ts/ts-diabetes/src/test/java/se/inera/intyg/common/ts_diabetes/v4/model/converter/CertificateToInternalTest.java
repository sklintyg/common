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

package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.EnumSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBeskrivningAnnanTyp;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesHarMedicinering;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesMedicineringHypoglykemiRisk;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionPatientenFoljsAv;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvIdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvVardniva;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class CertificateToInternalTest {

    private Certificate certificate;
    private TsDiabetesUtlatandeV4 expectedInternalCertificate;
    @Mock
    private CertificateTextProvider textProvider;
    @InjectMocks
    private CertificateToInternal certificateToInternal;

    @BeforeEach
    void setup() {
        when(textProvider.get(anyString())).thenReturn("Test string");

        final var allmant = Allmant.builder()
            .setPatientenFoljsAv(KvVardniva.SPECIALISTVARD)
            .setTypAvDiabetes(KvTypAvDiabetes.ANNAN)
            .setBeskrivningAnnanTypAvDiabetes("Här är en beskrivning")
            .setMedicineringForDiabetes(true)
            .setMedicineringMedforRiskForHypoglykemi(true)
            .setBehandling(
                Behandling.builder()
                    .setInsulin(true)
                    .setTabletter(true)
                    .setAnnan(true)
                    .build()
            )
            .build();

        expectedInternalCertificate = TsDiabetesUtlatandeV4.builder()
            .setId("id")
            .setTextVersion("textVersion")
            .setGrundData(getGrundData())
            .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvserKategori.VAR1, IntygAvserKategori.VAR2)))
            .setIdentitetStyrktGenom(IdKontroll.create(KvIdKontroll.PERS_KANNEDOM))
            .setAllmant(allmant)
            .build();

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, textProvider))
            .addElement(
                QuestionIntygetAvser.toCertificate(expectedInternalCertificate.getIntygAvser(), 0, textProvider)
            )
            .addElement(
                QuestionIdentitetStyrktGenom.toCertificate(expectedInternalCertificate.getIdentitetStyrktGenom(), 0, textProvider)
            )
            .addElement(QuestionPatientenFoljsAv.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesTyp.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesBeskrivningAnnanTyp.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesHarMedicinering.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesMedicineringHypoglykemiRisk.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesBehandling.toCertificate(allmant, 0, textProvider))
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
    void shallIncludeIntygetAvser() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getIntygAvser(), expectedInternalCertificate.getIntygAvser());
    }

    @Test
    void shallIncludeIdentitetStyrktGenom() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getIdentitetStyrktGenom(), expectedInternalCertificate.getIdentitetStyrktGenom());
    }

    @Test
    void shallIncludePatientenFoljsAv() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getAllmant().getPatientenFoljsAv(),
            expectedInternalCertificate.getAllmant().getPatientenFoljsAv());
    }

    @Test
    void shallIncludeDiabetesTyp() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getAllmant().getTypAvDiabetes(),
            expectedInternalCertificate.getAllmant().getTypAvDiabetes());
    }

    @Test
    void shallIncludeDiabetesBeskrivningAnnanTyp() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getAllmant().getBeskrivningAnnanTypAvDiabetes(),
            expectedInternalCertificate.getAllmant().getBeskrivningAnnanTypAvDiabetes());
    }

    @Test
    void shallIncludeDiabetesHarMedicinering() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getAllmant().getMedicineringForDiabetes(),
            expectedInternalCertificate.getAllmant().getMedicineringForDiabetes());
    }

    @Test
    void shallIncludeDiabetesMedicineringHypoglykemiRisk() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getAllmant().getMedicineringMedforRiskForHypoglykemi(),
            expectedInternalCertificate.getAllmant().getMedicineringMedforRiskForHypoglykemi());
    }

    @Test
    void shallIncludeDiabetesInsulin() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAllmant().getBehandling().getInsulin(),
            actualInternalCertificate.getAllmant().getBehandling().getInsulin());
    }

    @Test
    void shallIncludeDiabetesTabletter() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAllmant().getBehandling().getTabletter(),
            actualInternalCertificate.getAllmant().getBehandling().getTabletter());
    }

    @Test
    void shallIncludeDiabetesAnnan() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAllmant().getBehandling().getAnnan(),
            actualInternalCertificate.getAllmant().getBehandling().getAnnan());
    }
}
