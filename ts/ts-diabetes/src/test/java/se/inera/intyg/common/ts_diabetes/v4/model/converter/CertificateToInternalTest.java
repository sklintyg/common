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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionBedomningOvrigaKommentarer;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionBedomningUppfyllerBehorighetskrav;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBehandling;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBehandlingAnnan;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesBeskrivningAnnanTyp;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesDiagnosAr;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesHarMedicinering;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesMedicineringHypoglykemiRisk;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesMedicineringHypoglykemiRiskDatum;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionDiabetesTyp;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAllvarligSenasteTolvManaderna;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAllvarligSenasteTolvManadernaTidpunkt;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAret;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretKontrolleras;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretTidpunkt;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeSenasteAretTrafik;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeVaketSenasteTolv;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeVaketSenasteTre;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiFormagaKannaVarningstecken;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiForstarRiskerMedHypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiKontrollSjukdomstillstand;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiKontrollSjukdomstillstandVarfor;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiRegelbundnaBlodsockerkontroller;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionHypoglykemiVidtaAdekvataAtgarder;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionIdentitetStyrktGenom;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionIntygetAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionOvrigtBorUndersokasAvSpecialist;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionOvrigtKomplikationerAvSjukdomen;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionOvrigtKomplikationerAvSjukdomenAnges;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.certificate.question.QuestionPatientenFoljsAv;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Allmant;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Bedomning;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Behandling;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Hypoglykemi;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IdKontroll;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvser;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.Ovrigt;
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
            .setDiabetesDiagnosAr("1990")
            .setTypAvDiabetes(KvTypAvDiabetes.ANNAN)
            .setBeskrivningAnnanTypAvDiabetes("Här är en beskrivning")
            .setMedicineringForDiabetes(true)
            .setMedicineringMedforRiskForHypoglykemi(true)
            .setBehandling(
                Behandling.builder()
                    .setInsulin(true)
                    .setTabletter(true)
                    .setAnnan(true)
                    .setAnnanAngeVilken("Det här är en annan!")
                    .build()
            )
            .setMedicineringMedforRiskForHypoglykemiTidpunkt(new InternalDate("2022-01-01"))
            .build();

        final var hypoglykemi = Hypoglykemi.builder()
            .setKontrollSjukdomstillstand(false)
            .setKontrollSjukdomstillstandVarfor("Kontroll sjukdomstillstånd varför")
            .setForstarRiskerMedHypoglykemi(true)
            .setFormagaKannaVarningstecken(true)
            .setVidtaAdekvataAtgarder(true)
            .setAterkommandeSenasteAret(true)
            .setAterkommandeSenasteAretTidpunkt(new InternalDate("2023-01-13"))
            .setAterkommandeSenasteAretKontrolleras(true)
            .setAterkommandeSenasteAretTrafik(true)
            .setAterkommandeVaketSenasteTolv(true)
            .setAterkommandeVaketSenasteTre(true)
            .setAterkommandeVaketSenasteTreTidpunkt(new InternalDate("2023-01-13"))
            .setAllvarligSenasteTolvManaderna(true)
            .setAllvarligSenasteTolvManadernaTidpunkt(new InternalDate("2023-01-13"))
            .setRegelbundnaBlodsockerkontroller(true)
            .build();

        final var ovrigt = Ovrigt.builder()
            .setKomplikationerAvSjukdomen(true)
            .setKomplikationerAvSjukdomenAnges("Komplikationer anges")
            .setBorUndersokasAvSpecialist("Specialist inom sjukvård")
            .build();

        final var bedomning = Bedomning.builder()
            .setUppfyllerBehorighetskrav(Set.of(BedomningKorkortstyp.VAR1, BedomningKorkortstyp.VAR12, BedomningKorkortstyp.VAR4))
            .setOvrigaKommentarer(("Övriga kommentarer"))
            .build();

        expectedInternalCertificate = TsDiabetesUtlatandeV4.builder()
            .setId("id")
            .setTextVersion("textVersion")
            .setGrundData(getGrundData())
            .setIntygAvser(IntygAvser.create(EnumSet.of(IntygAvserKategori.VAR1, IntygAvserKategori.VAR2)))
            .setIdentitetStyrktGenom(IdKontroll.create(KvIdKontroll.PERS_KANNEDOM))
            .setAllmant(allmant)
            .setHypoglykemi(hypoglykemi)
            .setOvrigt(ovrigt)
            .setBedomning(bedomning)
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
            .addElement(
                QuestionDiabetesDiagnosAr.toCertificate(allmant, Personnummer.createPersonnummer("19121212-1212").get(), 0, textProvider))
            .addElement(QuestionDiabetesTyp.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesBeskrivningAnnanTyp.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesHarMedicinering.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesMedicineringHypoglykemiRisk.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesBehandling.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesBehandlingAnnan.toCertificate(allmant, 0, textProvider))
            .addElement(QuestionDiabetesMedicineringHypoglykemiRiskDatum.toCertificate(allmant, null, 0, textProvider))
            .addElement(QuestionHypoglykemiKontrollSjukdomstillstand.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiKontrollSjukdomstillstandVarfor.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiForstarRiskerMedHypoglykemi.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiFormagaKannaVarningstecken.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiVidtaAdekvataAtgarder.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiAterkommandeSenasteAret.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiAterkommandeSenasteAretTidpunkt.toCertificate(hypoglykemi, null, 0, textProvider))
            .addElement(QuestionHypoglykemiAterkommandeSenasteAretKontrolleras.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiAterkommandeSenasteAretTrafik.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiAterkommandeVaketSenasteTolv.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiAterkommandeVaketSenasteTre.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiAterkommandeVaketSenasteTreTidpunkt.toCertificate(hypoglykemi, null, 0, textProvider))
            .addElement(QuestionHypoglykemiAllvarligSenasteTolvManaderna.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionHypoglykemiAllvarligSenasteTolvManadernaTidpunkt.toCertificate(hypoglykemi, null, 0, textProvider))
            .addElement(QuestionHypoglykemiRegelbundnaBlodsockerkontroller.toCertificate(hypoglykemi, 0, textProvider))
            .addElement(QuestionOvrigtKomplikationerAvSjukdomen.toCertificate(ovrigt, 0, textProvider))
            .addElement(QuestionOvrigtKomplikationerAvSjukdomenAnges.toCertificate(ovrigt, 0, textProvider))
            .addElement(QuestionOvrigtBorUndersokasAvSpecialist.toCertificate(ovrigt, 0, textProvider))
            .addElement(QuestionBedomningUppfyllerBehorighetskrav.toCertificate(bedomning, "textVersion", 0, textProvider))
            .addElement(QuestionBedomningOvrigaKommentarer.toCertificate(bedomning, 0, textProvider))
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
    void shallIncludeDiagnosAr() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(actualInternalCertificate.getAllmant().getDiabetesDiagnosAr(),
            expectedInternalCertificate.getAllmant().getDiabetesDiagnosAr());
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

    @Test
    void shallIncludeBehandlingAnnanVilken() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAllmant().getBehandling().getAnnanAngeVilken(),
            actualInternalCertificate.getAllmant().getBehandling().getAnnanAngeVilken());
    }

    @Test
    void shallIncludeMedicineringHypoglykemiRiskDatum() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getAllmant().getMedicineringMedforRiskForHypoglykemiTidpunkt(),
            actualInternalCertificate.getAllmant().getMedicineringMedforRiskForHypoglykemiTidpunkt());
    }

    @Test
    void shallNotIncludeBehandlingWhenMedicineringMedforRiskIsFalse() {
        final var allmant = Allmant.builder().setMedicineringMedforRiskForHypoglykemi(false).build();
        final var utlatande = TsDiabetesUtlatandeV4.builder().setId("id").setTextVersion("textVersion")
            .setGrundData(getGrundData()).build();
        final var cert = CertificateBuilder.create()
            .addElement(QuestionDiabetesMedicineringHypoglykemiRisk.toCertificate(allmant, 0, textProvider)).build();
        final var actualInternalCertificate = certificateToInternal.convert(cert, utlatande);
        assertNull(actualInternalCertificate.getAllmant().getBehandling());
    }

    @Test
    void shallNotIncludeBehandlingWhenMedicineringMedforRiskIsNull() {
        final var allmant = Allmant.builder().setMedicineringMedforRiskForHypoglykemi(null).build();
        final var utlatande = TsDiabetesUtlatandeV4.builder().setId("id").setTextVersion("textVersion")
            .setGrundData(getGrundData()).build();
        final var cert = CertificateBuilder.create()
            .addElement(QuestionDiabetesMedicineringHypoglykemiRisk.toCertificate(allmant, 0, textProvider)).build();
        final var actualInternalCertificate = certificateToInternal.convert(cert, utlatande);
        assertNull(actualInternalCertificate.getAllmant().getBehandling());
    }

    @Test
    void shallIncludeHypoglykemiSjudomstillstand() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getKontrollSjukdomstillstand(),
            actualInternalCertificate.getHypoglykemi().getKontrollSjukdomstillstand());
    }

    @Test
    void shallIncludeHypoglykemiSjudomstillstandVarfor() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getKontrollSjukdomstillstandVarfor(),
            actualInternalCertificate.getHypoglykemi().getKontrollSjukdomstillstandVarfor());
    }

    @Test
    void shallIncludeHypoglykemiForstarRiskerMedHypoglykemi() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getForstarRiskerMedHypoglykemi(),
            actualInternalCertificate.getHypoglykemi().getForstarRiskerMedHypoglykemi());
    }

    @Test
    void shallIncludeHypoglykemiFormagaKannaVarningstecken() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getFormagaKannaVarningstecken(),
            actualInternalCertificate.getHypoglykemi().getFormagaKannaVarningstecken());
    }

    @Test
    void shallIncludeHypoglykemiVidtaAdekvataAtgarder() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getVidtaAdekvataAtgarder(),
            actualInternalCertificate.getHypoglykemi().getVidtaAdekvataAtgarder());
    }

    @Test
    void shallIncludeHypoglykemiAterkommandeSenasteAret() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAterkommandeSenasteAret(),
            actualInternalCertificate.getHypoglykemi().getAterkommandeSenasteAret());
    }

    @Test
    void shallIncludeHypoglykemiAterkommandeSenasteAretTidpunkt() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAterkommandeSenasteAretTidpunkt(),
            actualInternalCertificate.getHypoglykemi().getAterkommandeSenasteAretTidpunkt());
    }

    @Test
    void shallIncludeHypoglykemiAterkommandeSenasteAretKontrolleras() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAterkommandeSenasteAretKontrolleras(),
            actualInternalCertificate.getHypoglykemi().getAterkommandeSenasteAretKontrolleras());
    }

    @Test
    void shallIncludeHypoglykemiAterkommandeSenasteAretTrafik() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAterkommandeSenasteAretTrafik(),
            actualInternalCertificate.getHypoglykemi().getAterkommandeSenasteAretTrafik());
    }

    @Test
    void shallIncludeHypoglykemiAterkommandeVaketSenasteTolv() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAterkommandeVaketSenasteTolv(),
            actualInternalCertificate.getHypoglykemi().getAterkommandeVaketSenasteTolv());
    }

    @Test
    void shallIncludeHypoglykemiAterkommandeVaketSenasteTre() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAterkommandeVaketSenasteTre(),
            actualInternalCertificate.getHypoglykemi().getAterkommandeVaketSenasteTre());
    }

    @Test
    void shallIncludeHypoglykemiAterkommandeVaketSenasteTreTidpunkt() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAterkommandeVaketSenasteTreTidpunkt(),
            actualInternalCertificate.getHypoglykemi().getAterkommandeVaketSenasteTreTidpunkt());
    }

    @Test
    void shallIncludeHypoglykemiAllvarligSenasteTolvManaderna() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAllvarligSenasteTolvManaderna(),
            actualInternalCertificate.getHypoglykemi().getAllvarligSenasteTolvManaderna());
    }

    @Test
    void shallIncludeHypoglykemiAllvarligSenasteTolvManadernaTidpunkt() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getAllvarligSenasteTolvManadernaTidpunkt(),
            actualInternalCertificate.getHypoglykemi().getAllvarligSenasteTolvManadernaTidpunkt());
    }

    @Test
    void shallIncludeHypoglykemiRegelbundnaBlodsockerkontroller() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getHypoglykemi().getRegelbundnaBlodsockerkontroller(),
            actualInternalCertificate.getHypoglykemi().getRegelbundnaBlodsockerkontroller());
    }

    @Test
    void shallIncludeOvrigtKomplikationerAvSjukdomen() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getOvrigt().getKomplikationerAvSjukdomen(),
            actualInternalCertificate.getOvrigt().getKomplikationerAvSjukdomen());
    }

    @Test
    void shallIncludeOvrigtKomplikationerAvSjukdomenAnges() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getOvrigt().getKomplikationerAvSjukdomenAnges(),
            actualInternalCertificate.getOvrigt().getKomplikationerAvSjukdomenAnges());
    }

    @Test
    void shallIncludeOvrigtBorUndersokasAvSpecialist() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getOvrigt().getBorUndersokasAvSpecialist(),
            actualInternalCertificate.getOvrigt().getBorUndersokasAvSpecialist());
    }

    @Test
    void shallIncludeBedomningUppfyllerBehorighetskrav() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getBedomning().getUppfyllerBehorighetskrav(),
            actualInternalCertificate.getBedomning().getUppfyllerBehorighetskrav());
    }

    @Test
    void shallIncludeBedomningOvrigaKommentarer() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getBedomning().getOvrigaKommentarer(),
            actualInternalCertificate.getBedomning().getOvrigaKommentarer());
    }
}
