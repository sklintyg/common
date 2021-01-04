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
package se.inera.intyg.common.ts_diabetes.v3.model.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class WebcertModelFactoryTest {

    private static final String INTYG_ID = "intyg-123";
    private static final String INTYG_TYPE_VERSION_3 = "3.0";
    private static final String INTYG_TYPE_VERSION_3_1 = "3.1";

    @InjectMocks
    WebcertModelFactoryImpl modelFactory;

    @Mock
    private IntygTextsService intygTextsService;

    @Before
    public void setUp() {
        when(intygTextsService.getLatestVersionForSameMajorVersion(eq(TsDiabetesEntryPoint.MODULE_ID), eq(INTYG_TYPE_VERSION_3)))
            .thenReturn(INTYG_TYPE_VERSION_3_1);
    }

    public WebcertModelFactoryTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHappyPath() throws ConverterException {
        TsDiabetesUtlatandeV3 draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        assertNotNull(draft);
        Assert.assertEquals("VG1", draft.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        Assert.assertEquals("VE1", draft.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        Assert.assertEquals("TST12345678", draft.getGrundData().getSkapadAv().getPersonId());
        Assert.assertEquals("191212121212", draft.getGrundData().getPatient().getPersonId().getPersonnummer());
        Assert.assertEquals(INTYG_TYPE_VERSION_3_1, draft.getTextVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUtlatandeIdThrowsIllegalArgumentException() throws ConverterException {
        modelFactory.createNewWebcertDraft(buildNewDraftData(null));
    }

    @Test(expected = ConverterException.class)
    public void testBlankUtlatandeIdThrowsIllegalArgumentException() throws ConverterException {
        modelFactory.createNewWebcertDraft(buildNewDraftData(" "));
    }

    @Test
    public void testUpdateSkapadAv() throws ConverterException {
        TsDiabetesUtlatandeV3 draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        WebcertModelFactoryUtil.updateSkapadAv(draft, buildHosPersonal(), LocalDateTime.now());
    }

    @Test
    public void testCreateNewWebcertDraftDoesNotGenerateIncompleteSvarInTransportFormat() throws ConverterException {
        TsDiabetesUtlatandeV3 draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        assertTrue(InternalToTransport.convert(draft).getIntyg().getSvar().isEmpty());
    }

    private CreateNewDraftHolder buildNewDraftData(String intygId) {
        CreateNewDraftHolder draftHolder = new CreateNewDraftHolder(intygId, INTYG_TYPE_VERSION_3, buildHosPersonal(), buildPatient());
        return draftHolder;
    }

    private Patient buildPatient() {
        Patient patient = new Patient();
        patient.setFornamn("fornamn");
        patient.setEfternamn("efternamn");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        return patient;
    }

    private HoSPersonal buildHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("TST12345678");
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
    }

    private Vardenhet createVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("VE1");
        vardenhet.setEnhetsnamn("ve1");
        vardenhet.setVardgivare(new Vardgivare());
        vardenhet.getVardgivare().setVardgivarid("VG1");
        vardenhet.getVardgivare().setVardgivarnamn("vg1");
        return vardenhet;
    }
}
