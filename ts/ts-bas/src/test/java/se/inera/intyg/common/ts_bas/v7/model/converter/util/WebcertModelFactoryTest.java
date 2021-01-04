/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v7.model.converter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;
import se.inera.intyg.common.ts_bas.v7.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.v7.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(MockitoJUnitRunner.class)
public class WebcertModelFactoryTest {

    private static final String INTYG_TYPE_VERSION_1 = "1.0";
    private static final String INTYG_TYPE_VERSION_1_1 = "1.1";
    @Mock
    private IntygTextsService intygTexts;

    @InjectMocks
    private WebcertModelFactoryImpl factory;

    @Before
    public void setUp() {
        when(intygTexts.getLatestVersionForSameMajorVersion(eq(TsBasEntryPoint.MODULE_ID), eq(INTYG_TYPE_VERSION_1)))
            .thenReturn(INTYG_TYPE_VERSION_1_1);
    }

    @Test
    public void testCreateEditableModel() throws Exception {

        TsBasUtlatandeV7 utlatande = factory.createNewWebcertDraft(buildNewDraftData("testID"));

        assertNotNull(utlatande);
        assertEquals(TsBasEntryPoint.MODULE_ID, utlatande.getTyp());
        assertNotNull(utlatande.getGrundData().getSkapadAv());
        assertNotNull(utlatande.getGrundData().getPatient());

        /** Just verify some stuff from the json to make sure all is well.. */
        assertEquals("testID", utlatande.getId());
        assertEquals("Johnny Jobs Appleseed", utlatande.getGrundData().getPatient().getFullstandigtNamn());
        assertEquals("Testvägen 12", utlatande.getGrundData().getPatient().getPostadress());
        assertEquals("13337", utlatande.getGrundData().getPatient().getPostnummer());
        assertEquals("Huddinge", utlatande.getGrundData().getPatient().getPostort());
        assertEquals(INTYG_TYPE_VERSION_1_1, utlatande.getTextVersion());
    }

    @Test
    public void testCreateNewWebcertDraftDoesNotGenerateIncompleteSvarInRivtaV3Format() throws ConverterException {
        // this to follow schema during CertificateStatusUpdateForCareV2
        TsBasUtlatandeV7 draft = factory.createNewWebcertDraft(buildNewDraftData("INTYG_ID"));
        assertTrue(UtlatandeToIntyg.convert(draft).getSvar().isEmpty());
    }

    @Test(expected = ConverterException.class)
    public void testCreateCopyCertificateIdMissing() throws Exception {
        factory.createCopy(new CreateDraftCopyHolder("", new HoSPersonal()), TsBasUtlatandeV7.builder().build());
    }

    @Test
    public void testCreateCopyRemovesSigneringsdatumIntyg4576() throws Exception {
        //Given
        GrundData grundData = new GrundData();
        grundData.setSigneringsdatum(LocalDateTime.now());
        final HoSPersonal hoSPersonal = new HoSPersonal();
        final Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("1234");
        hoSPersonal.setVardenhet(vardenhet);
        grundData.setSkapadAv(hoSPersonal);
        grundData.setPatient(new Patient());

        final TsBasUtlatandeV7 tsBasUtlatande = TsBasUtlatandeV7.builder().setGrundData(grundData).build();

        //When
        TsBasUtlatandeV7 utlatande = factory.createCopy(new CreateDraftCopyHolder("abc123", hoSPersonal), tsBasUtlatande);

        //Then
        assertNull(utlatande.getGrundData().getSigneringsdatum());
    }

    private CreateNewDraftHolder buildNewDraftData(String intygId) {
        Patient patient = new Patient();
        patient.setFornamn("Johnny");
        patient.setMellannamn("Jobs");
        patient.setEfternamn("Appleseed");
        patient.setFullstandigtNamn("Johnny Jobs Appleseed");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        patient.setPostadress("Testvägen 12");
        patient.setPostnummer("13337");
        patient.setPostort("Huddinge");
        return new CreateNewDraftHolder(intygId, "1.0", createHosPersonal(), patient);
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("hsaId1");
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
    }

    private Vardenhet createVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("hsaId");
        vardenhet.setEnhetsnamn("ve1");
        vardenhet.setVardgivare(new Vardgivare());
        vardenhet.getVardgivare().setVardgivarid("vg1");
        vardenhet.getVardgivare().setVardgivarnamn("vg1");
        return vardenhet;
    }
}
