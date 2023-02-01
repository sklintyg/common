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

package se.inera.intyg.common.ts_diabetes.v3.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.IDENTITET_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.INTYG_AVSER_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYN_CATEGORY_ID;

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
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class InternalToCertificateTest {

    @Mock
    private CertificateTextProvider textProvider;

    private InternalToCertificate internalToCertificate;

    private TsDiabetesUtlatandeV3 internalCertificate;

    @BeforeEach
    void setUp() {
        when(textProvider.get(anyString())).thenReturn("Test string");

        internalToCertificate = new InternalToCertificate();
        internalCertificate = TsDiabetesUtlatandeV3.builder()
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
    void shallIncludeCategoryIntygetAvser() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(0, actualCertificate.getData().get(INTYG_AVSER_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryIdentitetStyrktGenom() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(1, actualCertificate.getData().get(IDENTITET_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryAllmant() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(2, actualCertificate.getData().get(ALLMANT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryHypoglykemi() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(3, actualCertificate.getData().get(HYPOGLYKEMI_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeCategorySyn() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(4, actualCertificate.getData().get(SYN_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryOvrigt() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(5, actualCertificate.getData().get(OVRIGT_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeCategoryBedomning() {
        final var actualCertificate = internalToCertificate.convert(internalCertificate, textProvider);
        assertEquals(6, actualCertificate.getData().get(BEDOMNING_CATEGORY_ID).getIndex());
    }
}
