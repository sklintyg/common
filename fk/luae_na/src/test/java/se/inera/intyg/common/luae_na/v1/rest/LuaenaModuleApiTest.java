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
package se.inera.intyg.common.luae_na.v1.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSLAG_TILL_ATGARD_SVAR_ID_24;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SUBSTANSINTAG_SVAR_ID_21;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.SUBSTANSINTAG_SVAR_JSON_ID_21;
import static se.inera.intyg.common.fkparent.rest.FkParentModuleApi.PREFIX;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.luae_na.v1.model.converter.SvarIdHelperImpl;
import se.inera.intyg.common.luae_na.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.luae_na.v1.model.internal.LuaenaUtlatandeV1;
import se.inera.intyg.common.luae_na.v1.utils.ScenarioFinder;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

@RunWith(MockitoJUnitRunner.class)
public class LuaenaModuleApiTest {

    private static final String LOGICAL_ADDRESS = "logical address";

    private final String PNR_TOLVAN = "19121212-1212";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @Spy
    private CustomObjectMapper objectMapper;

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private WebcertModelFactoryImpl webcertModelFactory;

    @Spy
    private SvarIdHelperImpl svarIdHelper;

    @InjectMocks
    private LuaenaModuleApiV1 moduleApi;

    @Test
    public void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v1/luae_na.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);

            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());

        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailWhenErrorIsReturned() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.ERROR));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v1/luae_na.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnNullModelHolder() throws ModuleException {
        moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnEmptyXml() throws ModuleException {
        moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnNullLogicalAddress() throws ModuleException {
        moduleApi.sendCertificateToRecipient("blaha", null, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnEmptyLogicalAddress() throws ModuleException {
        moduleApi.sendCertificateToRecipient("blaha", "", null);
    }

    @Test
    public void testRevokeCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = Resources.toString(Resources.getResource("v1/revokerequest.xml"), Charsets.UTF_8);

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateThrowsExternalServiceCallException() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = Resources.toString(Resources.getResource("v1/revokerequest.xml"), Charsets.UTF_8);

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "resultText"));
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        fail();
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));

        Mockito.doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LuaenaUtlatandeV1.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
            assertEquals("Certificate already exists", e.getMessage());
        }
    }

    @Test
    public void testRegisterCertificateGenericInfoResult() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("INFO"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LuaenaUtlatandeV1.class);
        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "revokeMessage";
        final String intygId = "intygId";

        HoSPersonal skapadAv = createHosPersonal();

        GrundData gd = new GrundData();
        gd.setPatient(createPatient());
        gd.setSkapadAv(skapadAv);

        Utlatande utlatande = LuaenaUtlatandeV1.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();

        String res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(anyString(), eq(LuaenaUtlatandeV1.class));
        doReturn(internalModel)
            .when(objectMapper)
            .writeValueAsString(any());
        String response = moduleApi.updateBeforeSave(internalModel, createHosPersonal());
        assertEquals(internalModel, response);
        verify(moduleService, times(1)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(anyString(), eq(LuaenaUtlatandeV1.class));

        doReturn(internalModel)
            .when(objectMapper)
            .writeValueAsString(any());

        String response = moduleApi.updateBeforeSigning(internalModel, createHosPersonal(), LocalDateTime.now());
        assertEquals(internalModel, response);
        verify(moduleService, times(1)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    public void testGetModuleSpecificArendeParameters() throws Exception {
        LuaenaUtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();

        Map<String, List<String>> res = moduleApi.getModuleSpecificArendeParameters(utlatande,
            Arrays.asList(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, FORSLAG_TILL_ATGARD_SVAR_ID_24, SUBSTANSINTAG_SVAR_ID_21));

        assertNotNull(res);
        assertEquals(3, res.keySet().size());
        assertNotNull(res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1));
        assertEquals(2, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
            res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(1));
        assertNotNull(res.get(FORSLAG_TILL_ATGARD_SVAR_ID_24));
        assertEquals(1, res.get(FORSLAG_TILL_ATGARD_SVAR_ID_24).size());
        assertEquals(FORSLAG_TILL_ATGARD_SVAR_JSON_ID_24, res.get(FORSLAG_TILL_ATGARD_SVAR_ID_24).get(0));
        assertNotNull(res.get(SUBSTANSINTAG_SVAR_ID_21));
        assertEquals(1, res.get(SUBSTANSINTAG_SVAR_ID_21).size());
        assertEquals(SUBSTANSINTAG_SVAR_JSON_ID_21, res.get(SUBSTANSINTAG_SVAR_ID_21).get(0));
    }

    @Test
    public void testGetAdditionalInfo() throws Exception {
        Intyg intyg = ScenarioFinder.getInternalScenario("pass-minimal").asTransportModel().getIntyg();

        String additionalInfo = moduleApi.getAdditionalInfo(intyg);

        assertNotNull(additionalInfo);
        assertEquals("Skada på multipla böjmuskler och deras senor på handleds- och handnivå", additionalInfo);
    }

    @Test
    public void testGetAdditionalInfoHuvuddiagnos() throws Exception {
        Intyg intyg = ScenarioFinder.getInternalScenario("pass-diagnos-med-bidiagnoser").asTransportModel().getIntyg();

        String additionalInfo = moduleApi.getAdditionalInfo(intyg);

        assertNotNull(additionalInfo);
        assertEquals("Skada på multipla böjmuskler och deras senor på handleds- och handnivå", additionalInfo);
    }

    @Test
    public void testCreateCompletionFromTemplateWithComment() throws Exception {

        final String ovrigt = "övrigtText";
        final String kommentar = "kommentarText";

        LuaenaUtlatandeV1 utlatande = LuaenaUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        String result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        LuaenaUtlatandeV1 utlatandeFromJson = (LuaenaUtlatandeV1) moduleApi.getUtlatandeFromJson(result);

        assertEquals(ovrigt + "\n\n" + PREFIX + kommentar, utlatandeFromJson.getOvrigt());

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    public void testCreateCompletionFromTemplateWithNoComment() throws Exception {

        final String ovrigt = "övrigtText";
        final String kommentar = "";

        LuaenaUtlatandeV1 utlatande = LuaenaUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        String result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        LuaenaUtlatandeV1 utlatandeFromJson = (LuaenaUtlatandeV1) moduleApi.getUtlatandeFromJson(result);

        assertEquals(ovrigt, utlatandeFromJson.getOvrigt());

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    public void testCreateCompletionFromTemplateWithNoOvrigt() throws Exception {

        final String ovrigt = "";
        final String kommentar = "kommentarText";

        LuaenaUtlatandeV1 utlatande = LuaenaUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        String result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        LuaenaUtlatandeV1 utlatandeFromJson = (LuaenaUtlatandeV1) moduleApi.getUtlatandeFromJson(result);

        assertEquals(PREFIX + kommentar, utlatandeFromJson.getOvrigt());

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        RegisterCertificateResponseType retVal = new RegisterCertificateResponseType();
        ResultType value = new ResultType();
        value.setResultCode(res);
        retVal.setResult(value);
        return retVal;
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("hsaId1");
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
    }

    private Patient createPatient() {
        return createPatient("fornamn", "efternamn", PNR_TOLVAN);
    }

    private Patient createPatient(String fornamn, String efternamn, String pnr) {
        Patient patient = new Patient();
        if (StringUtils.isNotEmpty(fornamn)) {
            patient.setFornamn(fornamn);
        }
        if (StringUtils.isNotEmpty(efternamn)) {
            patient.setEfternamn(efternamn);
        }
        patient.setPersonId(createPnr(pnr));
        return patient;
    }

    private Personnummer createPnr(String civicRegistrationNumber) {
        return Personnummer.createPersonnummer(civicRegistrationNumber).get();
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

    private CreateDraftCopyHolder createCopyHolder() {
        return new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
    }
}
