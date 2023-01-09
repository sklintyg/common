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
package se.inera.intyg.common.luse.v1.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSGRUND_SVAR_ID_7;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.DIAGNOSGRUND_SVAR_JSON_ID_7;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.KANNEDOM_SVAR_JSON_ID_2;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.fkparent.rest.FkParentModuleApi.PREFIX;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.luse.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.luse.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.luse.v1.model.converter.SvarIdHelperImpl;
import se.inera.intyg.common.luse.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.luse.v1.model.internal.LuseUtlatandeV1;
import se.inera.intyg.common.luse.v1.utils.ScenarioFinder;
import se.inera.intyg.common.luse.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.luse.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

@RunWith(MockitoJUnitRunner.class)
public class LuseModuleApiV1Test {

    private static final String LOGICAL_ADDRESS = "logical address";
    private static final String INTYG_TYPE_VERSION_1 = "1.0";

    private final String PNR_TOLVAN = "19121212-1212";
    @Mock
    private CertificateToInternal certificateToInternal;
    @Mock
    private InternalToCertificate internalToCertificate;

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private WebcertModelFactoryImpl webcertModelFactory;

    @Mock
    private InternalDraftValidatorImpl internalDraftValidator;

    @Spy
    private CustomObjectMapper objectMapper;

    @Mock
    private GetCertificateResponderInterface getCertificateResponder;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @Spy
    private SvarIdHelperImpl svarIdHelper;

    @InjectMocks
    private LuseModuleApiV1 moduleApi;

    @Test
    public void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v1/luse.xml"), Charsets.UTF_8);
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
            String xmlContents = Resources.toString(Resources.getResource("v1/luse.xml"), Charsets.UTF_8);
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
    public void testValidateShouldUseValidator() throws Exception {

        doReturn(null)
            .when(objectMapper)
            .readValue("internal model", LuseUtlatandeV1.class);

        moduleApi.validateDraft("internal model");
        verify(internalDraftValidator, times(1)).validateDraft(any());
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenReturn(null);
        when(objectMapper.writeValueAsString(any())).thenReturn("internal model");
        moduleApi.createNewInternal(createDraftHolder());
        verify(webcertModelFactory, times(1)).createNewWebcertDraft(any());
    }

    @Test(expected = ModuleException.class)
    public void testCreateNewInternalThrowsModuleException() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenThrow(new ConverterException());
        moduleApi.createNewInternal(createDraftHolder());
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenReturn(null);

        moduleApi.createNewInternalFromTemplate(createCopyHolder(), null);

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test(expected = ModuleException.class)
    public void testCreateNewInternalFromTemplateThrowsModuleException() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenThrow(new ConverterException());
        moduleApi.createNewInternalFromTemplate(createCopyHolder(), ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel());
    }

    @Test
    public void testGetCertificate() throws Exception {
        final String certificateId = "certificateId";
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";

        when(getCertificateResponder.getCertificate(eq(logicalAddress), any())).thenReturn(createGetCertificateResponseType());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);

        CertificateResponse certificate = moduleApi.getCertificate(certificateId, logicalAddress, "INVANA");

        ArgumentCaptor<GetCertificateType> captor = ArgumentCaptor.forClass(GetCertificateType.class);
        verify(getCertificateResponder, times(1)).getCertificate(eq(logicalAddress), captor.capture());
        assertEquals(certificateId, captor.getValue().getIntygsId().getExtension());
        assertEquals(internalModel, certificate.getInternalModel());
        assertEquals(false, certificate.isRevoked());
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateThrowsModuleException() throws ModuleException, SOAPException {
        final String certificateId = "certificateId";
        final String logicalAddress = "logicalAddress";
        when(getCertificateResponder.getCertificate(eq(logicalAddress), any()))
            .thenThrow(new SOAPFaultException(SOAPFactory.newInstance().createFault()));
        moduleApi.getCertificate(certificateId, logicalAddress, "INVANA");
    }

    @Test
    public void testRegisterCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LuseUtlatandeV1.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(internalModel, logicalAddress);

        ArgumentCaptor<RegisterCertificateType> captor = ArgumentCaptor.forClass(RegisterCertificateType.class);
        verify(registerCertificateResponderInterface, times(1)).registerCertificate(eq(logicalAddress), captor.capture());
        assertNotNull(captor.getValue().getIntyg());
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LuseUtlatandeV1.class);

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
            .readValue(internalModel, LuseUtlatandeV1.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateShouldThrowExceptionOnFailedCallToIT() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "resultText"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LuseUtlatandeV1.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(internalModel, logicalAddress);
    }

    @Test(expected = ModuleConverterException.class)
    public void testRegisterCertificateShouldThrowExceptionOnBadCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";

        doReturn(null)
            .when(objectMapper)
            .readValue(internalModel, LuseUtlatandeV1.class);

        moduleApi.registerCertificate(internalModel, logicalAddress);
    }

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        final String utlatandeJson = "utlatandeJson";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(utlatandeJson, LuseUtlatandeV1.class);

        Utlatande utlatandeFromJson = moduleApi.getUtlatandeFromJson(utlatandeJson);
        assertNotNull(utlatandeFromJson);
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LuseUtlatandeV1.class);

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
            .readValue(internalModel, LuseUtlatandeV1.class);

        doReturn(internalModel)
            .when(objectMapper)
            .writeValueAsString(any());

        String response = moduleApi.updateBeforeSigning(internalModel, createHosPersonal(), null);
        assertEquals(internalModel, response);
        verify(moduleService, times(1)).getDescriptionFromDiagnosKod(anyString(), anyString());
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
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "revokeMessage";
        final String intygId = "intygId";

        HoSPersonal skapadAv = createHosPersonal();

        GrundData gd = new GrundData();
        gd.setPatient(createPatient());
        gd.setSkapadAv(skapadAv);

        Utlatande utlatande = LuseUtlatandeV1.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();

        String res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    public void testCreateCompletionFromTemplateWithComment() throws Exception {

        final String ovrigt = "övrigtText";
        final String kommentar = "kommentarText";

        LuseUtlatandeV1 utlatande = LuseUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        String result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        LuseUtlatandeV1 utlatandeFromJson = (LuseUtlatandeV1) moduleApi.getUtlatandeFromJson(result);

        assertEquals(ovrigt + "\n\n" + PREFIX + kommentar, utlatandeFromJson.getOvrigt());

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    public void testCreateCompletionFromTemplateWithNoComment() throws Exception {

        final String ovrigt = "övrigtText";
        final String kommentar = "";

        LuseUtlatandeV1 utlatande = LuseUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        String result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        LuseUtlatandeV1 utlatandeFromJson = (LuseUtlatandeV1) moduleApi.getUtlatandeFromJson(result);

        assertEquals(ovrigt, utlatandeFromJson.getOvrigt());

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    public void testCreateCompletionFromTemplateWithNoOvrigt() throws Exception {

        final String ovrigt = "";
        final String kommentar = "kommentarText";

        LuseUtlatandeV1 utlatande = LuseUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        String result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        LuseUtlatandeV1 utlatandeFromJson = (LuseUtlatandeV1) moduleApi.getUtlatandeFromJson(result);

        assertEquals(PREFIX + kommentar, utlatandeFromJson.getOvrigt());

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    public void testGetModuleSpecificArendeParameters() throws Exception {
        LuseUtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();

        Map<String, List<String>> res = moduleApi.getModuleSpecificArendeParameters(utlatande,
            Arrays.asList(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, KANNEDOM_SVAR_ID_2, PLANERADBEHANDLING_SVAR_ID_20, DIAGNOSGRUND_SVAR_ID_7));

        assertNotNull(res);
        assertEquals(4, res.keySet().size());
        assertNotNull(res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1));
        assertEquals(2, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
            res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(1));
        assertNotNull(res.get(KANNEDOM_SVAR_ID_2));
        assertEquals(1, res.get(KANNEDOM_SVAR_ID_2).size());
        assertEquals(KANNEDOM_SVAR_JSON_ID_2, res.get(KANNEDOM_SVAR_ID_2).get(0));
        assertNotNull(res.get(DIAGNOSGRUND_SVAR_ID_7));
        assertEquals(1, res.get(DIAGNOSGRUND_SVAR_ID_7).size());
        assertEquals(DIAGNOSGRUND_SVAR_JSON_ID_7, res.get(DIAGNOSGRUND_SVAR_ID_7).get(0));
        assertNotNull(res.get(PLANERADBEHANDLING_SVAR_ID_20));
        assertEquals(1, res.get(PLANERADBEHANDLING_SVAR_ID_20).size());
        assertEquals(PLANERADBEHANDLING_SVAR_JSON_ID_20, res.get(PLANERADBEHANDLING_SVAR_ID_20).get(0));
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
    public void shallConvertInternalToCertificate() throws Exception {
        final var expectedCertificate = CertificateBuilder.create().build();
        final var certificateAsJson = "certificateAsJson";
        final var typeAheadProvider = mock(TypeAheadProvider.class);

        final var internalCertificate = LuseUtlatandeV1.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(new GrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(eq(certificateAsJson), eq(LuseUtlatandeV1.class));

        doReturn(expectedCertificate)
            .when(internalToCertificate).convert(eq(internalCertificate), any(CertificateTextProvider.class));

        final var actualCertificate = moduleApi.getCertificateFromJson(certificateAsJson, typeAheadProvider);

        assertEquals(expectedCertificate, actualCertificate);
    }


    @Test
    public void shallConvertCertificateToInternal() throws Exception {
        final var expectedJson = "expectedJson";
        final var certificate = CertificateBuilder.create().build();
        final var certificateAsJson = "certificateAsJson";

        final var internalCertificate = LuseUtlatandeV1.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(new GrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(eq(certificateAsJson), eq(LuseUtlatandeV1.class));

        doReturn(expectedJson)
            .when(objectMapper).writeValueAsString(internalCertificate);

        doReturn(internalCertificate)
            .when(certificateToInternal).convert(certificate, internalCertificate);

        final var actualJson = moduleApi.getJsonFromCertificate(certificate, certificateAsJson);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void getCertficateMessagesProviderGetExistingKey() {

        final var certificateMessagesProvider = moduleApi.getMessagesProvider();

        assertEquals(certificateMessagesProvider.get("common.continue"), "Fortsätt");
    }

    @Test
    public void getCertficateMessagesProviderGetMissingKey() {

        final var certificateMessagesProvider = moduleApi.getMessagesProvider();

        assertNull(certificateMessagesProvider.get("not.existing"));
    }

    private GetCertificateResponseType createGetCertificateResponseType() throws ScenarioNotFoundException {
        GetCertificateResponseType res = new GetCertificateResponseType();
        RegisterCertificateType registerType = ScenarioFinder.getInternalScenario("pass-minimal").asTransportModel();
        res.setIntyg(registerType.getIntyg());
        return res;
    }

    private CreateDraftCopyHolder createCopyHolder() {
        return new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
    }

    private CreateNewDraftHolder createDraftHolder() {
        return new CreateNewDraftHolder("certificateId", INTYG_TYPE_VERSION_1, createHosPersonal(), createPatient());
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

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        RegisterCertificateResponseType retVal = new RegisterCertificateResponseType();
        ResultType value = new ResultType();
        value.setResultCode(res);
        retVal.setResult(value);
        return retVal;
    }
}
