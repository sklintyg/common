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

package se.inera.intyg.common.ts_diabetes.v2.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_BEHANDLING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_FOREKOMST_BESKRIVNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_FOREKOMST_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_FOREKOMST_TRAFIKEN_BESKRIVNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_FOREKOMST_TRAFIKEN_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_FOREKOMST_VAKEN_TID_OBSERVATIONSTID_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_FOREKOMST_VAKEN_TID_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_EGENKONTROLL_BLODSOCKER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_KUNSKAP_OM_ATGARDER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_SAKNAR_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.HYPOGLYKEMI_TECKEN_NEDSATT_HJARNFUNKTION_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.IDENTITET_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.INTYG_AVSER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.SYN_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.SYN_DUBBELSEENDE_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.SYN_SEPARAT_OGONLAKARINTYG_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.SYN_UTAN_ANMARKNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v2.model.converter.RespConstants.SYN_VARDEN_SYNSKARPA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_CATEGORY_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class InternalToCertificateTest {

    @Mock
    private CertificateTextProvider textProvider;

    private InternalToCertificate internalToCertificate;

    private TsDiabetesUtlatandeV2 internalCertificate;

    @BeforeEach
    void setUp() {
        when(textProvider.get(anyString())).thenReturn("Test string");

        internalToCertificate = new InternalToCertificate();
        internalCertificate = new TsDiabetesUtlatandeV2();
        internalCertificate.setId("certificateId");
        internalCertificate.setTextVersion("1.0");
        internalCertificate.setGrundData(getGrundData());
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
    void shallIncludeCategoryIntygetAvser() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(0, actualCertificate.getData().get(INTYG_AVSER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIntygetAvser() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(1, actualCertificate.getData().get(INTYG_AVSER_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryIdentitetStyrktGenom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(2, actualCertificate.getData().get(IDENTITET_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionIdentitetStyrktGenom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(3, actualCertificate.getData().get(IDENTITET_STYRKT_GENOM_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryAllmant() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(4, actualCertificate.getData().get(ALLMANT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiabetesDiagnosAr() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(5, actualCertificate.getData().get(ALLMANT_DIABETES_DIAGNOS_AR_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiabetesTyp() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(6, actualCertificate.getData().get(ALLMANT_TYP_AV_DIABETES_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiabetesBehandling() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(7, actualCertificate.getData().get(ALLMANT_BEHANDLING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiabetesInsulinBehandlingsperiod() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(8, actualCertificate.getData().get(ALLMANT_BEHANDLING_INSULIN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionDiabetesAnnanBehandlingBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(9, actualCertificate.getData().get(ALLMANT_BEHANDLING_ANNAN_DELSVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryHypoglykemi() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(10, actualCertificate.getData().get(HYPOGLYKEMI_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiKunskapOmAtgarder() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(11, actualCertificate.getData().get(HYPOGLYKEMI_KUNSKAP_OM_ATGARDER_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiNedsattHjarnfunktion() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(12, actualCertificate.getData().get(HYPOGLYKEMI_TECKEN_NEDSATT_HJARNFUNKTION_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiSaknarFormagaKannaVarningstecken() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(13, actualCertificate.getData().get(HYPOGLYKEMI_SAKNAR_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiAllvarligForekomst() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(14, actualCertificate.getData().get(HYPOGLYKEMI_ALLVARLIG_FOREKOMST_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiAllvarligForekomstBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(15, actualCertificate.getData().get(HYPOGLYKEMI_ALLVARLIG_FOREKOMST_BESKRIVNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiAllvarligForekomstTrafiken() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(16, actualCertificate.getData().get(HYPOGLYKEMI_ALLVARLIG_FOREKOMST_TRAFIKEN_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiAllvarligForekomstTrafikenBeskrivning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(17, actualCertificate.getData().get(HYPOGLYKEMI_ALLVARLIG_FOREKOMST_TRAFIKEN_BESKRIVNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiEgenkontrollBlodsocker() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(18, actualCertificate.getData().get(HYPOGLYKEMI_EGENKONTROLL_BLODSOCKER_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiAllvarligForekomstVakenTid() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(19, actualCertificate.getData().get(HYPOGLYKEMI_ALLVARLIG_FOREKOMST_VAKEN_TID_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionHypoglykemiAllvarligForekomstVakenTidObervationstid() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(20, actualCertificate.getData().get(HYPOGLYKEMI_ALLVARLIG_FOREKOMST_VAKEN_TID_OBSERVATIONSTID_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeCategorySyn() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(21, actualCertificate.getData().get(SYN_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSynSeparatOgonlakarintyg() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(22, actualCertificate.getData().get(SYN_SEPARAT_OGONLAKARINTYG_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSynUtanAnmarkning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(23, actualCertificate.getData().get(SYN_UTAN_ANMARKNING_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSynskarpa() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(24, actualCertificate.getData().get(SYN_VARDEN_SYNSKARPA_SVAR_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionSynDubbelseende() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(25, actualCertificate.getData().get(SYN_DUBBELSEENDE_SVAR_ID).getIndex());
    }
}
