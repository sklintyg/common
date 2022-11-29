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

package se.inera.intyg.common.luae_na.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMU_CATEGORY_ID;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KANNEDOM_DELSVAR_ID_2;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.UNDERLAGFINNS_DELSVAR_ID_3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

class InternalToCertificateTest {

    private GrundData grundData;
    private LuaenaUtlatandeV1 internalCertificate;
    private CertificateTextProvider textProvider;

    @BeforeEach
    void setUp() {
        final var unit = new Vardenhet();

        final var skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(unit);

        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());

        grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);

        internalCertificate = LuaenaUtlatandeV1.builder()
            .setId("certificateId")
            .setTextVersion("1.0")
            .setGrundData(grundData)
            .build();
        textProvider = mock(CertificateTextProvider.class);
    }

    @Test
    void shallIncludeMetadata() {
        final var actualCertificate = InternalToCertificate.toCertificate(internalCertificate, textProvider);
        assertNotNull(actualCertificate.getMetadata(), "Shall contain metadata");
    }

    @Test
    void shallIncludeCategoryGrundForMU() {
        final var actualCertificate = InternalToCertificate.toCertificate(internalCertificate, textProvider);
        assertEquals(0, actualCertificate.getData().get(GRUNDFORMU_CATEGORY_ID).getIndex());
    }

    @Test
    void shallIncludeQuestionGrundForMTypAvGrund() {
        final var actualCertificate = InternalToCertificate.toCertificate(internalCertificate, textProvider);
        assertEquals(1, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeQuestionGrundForMUAnnanBeskrivning() {
        final var actualCertificate = InternalToCertificate.toCertificate(internalCertificate, textProvider);
        assertEquals(2, actualCertificate.getData().get(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeQuestionGrundForMUMotivering() {
        final var actualCertificate = InternalToCertificate.toCertificate(internalCertificate, textProvider);
        assertEquals(3, actualCertificate.getData().get(MOTIVERING_TILL_INTE_BASERAT_PA_UNDERLAG_DELSVAR_ID_1).getIndex());
    }

    @Test
    void shallIncludeQuestionGrundForMUKannedomOmPatient() {
        final var actualCertificate = InternalToCertificate.toCertificate(internalCertificate, textProvider);
        assertEquals(4, actualCertificate.getData().get(KANNEDOM_DELSVAR_ID_2).getIndex());
    }

    @Test
    void shallIncludeQuestionGrundForMUUnderlagFinns() {
        final var actualCertificate = InternalToCertificate.toCertificate(internalCertificate, textProvider);
        assertEquals(5, actualCertificate.getData().get(UNDERLAGFINNS_DELSVAR_ID_3).getIndex());
    }

    @Test
    void shallIncludeFunktionsnedsattningCategory() {
        final var actualCertificate = InternalToCertificate.toCertificate(internalCertificate, textProvider);
        assertEquals(6, actualCertificate.getData().get(FUNKTIONSNEDSATTNING_CATEGORY_ID).getIndex());
    }
}

