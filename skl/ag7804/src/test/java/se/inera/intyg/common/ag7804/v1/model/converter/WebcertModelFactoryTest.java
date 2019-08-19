/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag7804.v1.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.core.io.ClassPathResource;

import se.inera.intyg.common.ag7804.support.Ag7804EntryPoint;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(MockitoJUnitRunner.class)
public class WebcertModelFactoryTest {

    private static final String INTYG_ID = "intyg-123";
    private static final String INTYG_TYPE_VERSION_1 = "1.0";
    private static final String INTYG_TYPE_VERSION_1_2 = "1.2";
    private static final String LISJP_TESTFILE_UTLATANDE_TEMPLATE = "v1/WebcertModelFactory/lisjp-template.json";
    private static final String AG7804_TESTFILE_UTLATANDE_EXPECTED = "v1/WebcertModelFactory/ag7804-result-copy.json";
    @InjectMocks
    WebcertModelFactoryImpl modelFactory;

    @Mock
    private IntygTextsService intygTextsService;

    @Mock
    private WebcertModuleService webcertModuleService;

    private CustomObjectMapper customObjectMapper = new CustomObjectMapper();

    @Before
    public void setUp() {
        when(intygTextsService.getLatestVersionForSameMajorVersion(eq(Ag7804EntryPoint.MODULE_ID), eq(INTYG_TYPE_VERSION_1)))
            .thenReturn(INTYG_TYPE_VERSION_1_2);
    }

    @Test
    public void testNormalCopy() throws ConverterException {
        HoSPersonal newHosPersonal = buildHosPersonal();
        newHosPersonal.setPersonId("3333");
        CreateDraftCopyHolder copyHolder = new CreateDraftCopyHolder("123", newHosPersonal);

        Ag7804UtlatandeV1 template = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        Ag7804UtlatandeV1 draft = modelFactory.createCopy(copyHolder, template);
        assertNotNull(draft);
        assertEquals("VG1", draft.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        assertEquals("VE1", draft.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        assertEquals("3333", draft.getGrundData().getSkapadAv().getPersonId());
        assertEquals("191212121212", draft.getGrundData().getPatient().getPersonId().getPersonnummer());
        assertEquals(INTYG_TYPE_VERSION_1_2, draft.getTextVersion());
    }

    @Test
    public void testCopyFromLisjpTemplate() throws ConverterException, IOException, JSONException {
        HoSPersonal newHosPersonal = buildHosPersonal();
        Patient patient = buildPatient();

        CreateDraftCopyHolder copyHolder = new CreateDraftCopyHolder("ag7804-intyg", newHosPersonal);
        copyHolder.setPatient(patient);
        copyHolder.setIntygTypeVersion("1.1");

        LisjpUtlatandeV1 template = customObjectMapper.readValue(new ClassPathResource(
            LISJP_TESTFILE_UTLATANDE_TEMPLATE).getFile(), LisjpUtlatandeV1.class);

        Ag7804UtlatandeV1 expectedUtlatande = customObjectMapper.readValue(new ClassPathResource(
            AG7804_TESTFILE_UTLATANDE_EXPECTED).getFile(), Ag7804UtlatandeV1.class);

        Ag7804UtlatandeV1 draft = modelFactory.createCopy(copyHolder, template);
        assertNotNull(draft);

        JSONAssert.assertEquals(serializeUtlatande(expectedUtlatande), serializeUtlatande(draft), false);

    }

    @Test
    public void testCreateNewDraftHappyPath() throws ConverterException {
        Ag7804UtlatandeV1 draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        assertNotNull(draft);
        assertEquals("VG1", draft.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        assertEquals("VE1", draft.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        assertEquals("TST12345678", draft.getGrundData().getSkapadAv().getPersonId());
        assertEquals("191212121212", draft.getGrundData().getPatient().getPersonId().getPersonnummer());
        assertEquals(INTYG_TYPE_VERSION_1_2, draft.getTextVersion());
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
        Ag7804UtlatandeV1 draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        WebcertModelFactoryUtil.updateSkapadAv(draft, buildHosPersonal(), LocalDateTime.now());
    }

    @Test
    public void testCreateNewWebcertDraftDoesNotGenerateIncompleteSvarInTransportFormat() throws ConverterException {
        // this to follow schema during CertificateStatusUpdateForCareV2
        Ag7804UtlatandeV1 draft = modelFactory.createNewWebcertDraft(buildNewDraftData(INTYG_ID));
        assertTrue(InternalToTransport.convert(draft, webcertModuleService).getIntyg().getSvar().isEmpty());
    }

    private CreateNewDraftHolder buildNewDraftData(String intygId) {
        CreateNewDraftHolder draftHolder = new CreateNewDraftHolder(intygId, INTYG_TYPE_VERSION_1, buildHosPersonal(), buildPatient());
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
        vardenhet.setEpost("enhet3@webcert.invalid.se");
        vardenhet.setPostadress("Nygatan 4");
        vardenhet.setPostnummer("22233");
        vardenhet.setPostort("Nyorten");
        vardenhet.setVardgivare(new Vardgivare());
        vardenhet.getVardgivare().setVardgivarid("VG1");
        vardenhet.getVardgivare().setVardgivarnamn("vg1");
        return vardenhet;
    }

    private String serializeUtlatande(Utlatande utlatande) throws ConverterException {
        StringWriter jsonWriter = new StringWriter();
        try {
            customObjectMapper.writeValue(jsonWriter, utlatande);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonWriter.toString();
    }
}
