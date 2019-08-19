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
package se.inera.intyg.common.fkparent.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.fkparent.model.converter.SvarIdHelper;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.stub.IntygTestDataBuilder;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
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
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.IntygsStatus;

import javax.xml.bind.JAXB;
import javax.xml.transform.Source;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class FkParentModuleApiTest {

    private static final String INTYG_ID = "test-id";
    private static final String LOGICAL_ADDRESS = "logicalAddress";
    private static final String INTYG_TYPE_VERSION_1 = "1.0";
    private static ClassPathResource registerCertificateFile;
    private static ClassPathResource getCertificateFile;
    private static ClassPathResource revokeCertificateFile;
    private static Utlatande utlatande;
    private static String json;
    @SuppressWarnings("unchecked")
    @InjectMocks
    FkParentModuleApi<Utlatande> moduleApi = mock(FkParentModuleApi.class, Mockito.CALLS_REAL_METHODS);
    @Mock
    private InternalDraftValidator<Utlatande> internalDraftValidator;
    @Mock
    private WebcertModelFactory<Utlatande> webcertModelFactory;
    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;
    @Mock
    private GetCertificateResponderInterface getCertificateResponderInterface;
    @Mock
    private RegisterCertificateValidator validator;
    @Mock
    private SvarIdHelper<Utlatande> svarIdHelper;
    @Mock
    private RevokeCertificateResponderInterface revokeCertificateClient;
    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    public FkParentModuleApiTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void set() throws Exception {
        registerCertificateFile = new ClassPathResource("registerCertificate.xml");
        getCertificateFile = new ClassPathResource("getCertificate.xml");
        revokeCertificateFile = new ClassPathResource("revokeCertificate.xml");
        utlatande = IntygTestDataBuilder.getUtlatande();
        json = new CustomObjectMapper().writeValueAsString(utlatande);
    }

    @Before
    public void setup() throws Exception {
        Field field = FkParentModuleApi.class.getDeclaredField("type");
        field.setAccessible(true);
        field.set(moduleApi, TestUtlatande.class);
        Field field2 = FkParentModuleApi.class.getDeclaredField("validator");
        field2.setAccessible(true);
        field2.set(moduleApi, validator);
    }

    @Test
    public void testValidateDraft() throws Exception {
        when(internalDraftValidator.validateDraft(any(Utlatande.class)))
                .thenReturn(new ValidateDraftResponse(ValidationStatus.VALID, new ArrayList<>()));

        ValidateDraftResponse res = moduleApi.validateDraft(json);

        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
        verify(internalDraftValidator).validateDraft(any(Utlatande.class));
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        CreateNewDraftHolder draftCertificateHolder = new CreateNewDraftHolder(INTYG_ID, INTYG_TYPE_VERSION_1, new HoSPersonal(), new Patient());
        String res = moduleApi.createNewInternal(draftCertificateHolder);

        assertNotNull(res);
        verify(webcertModelFactory).createNewWebcertDraft(draftCertificateHolder);
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateNewInternalConverterException() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any(CreateNewDraftHolder.class))).thenThrow(new ConverterException());
        moduleApi.createNewInternal(new CreateNewDraftHolder(INTYG_ID, INTYG_TYPE_VERSION_1, new HoSPersonal(), new Patient()));
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCopyHolder = new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal());
        String res = moduleApi.createNewInternalFromTemplate(draftCopyHolder, utlatande);

        assertNotNull(res);
        verify(webcertModelFactory).createCopy(eq(draftCopyHolder), any(Utlatande.class));
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateNewInternalFromTemplateConverterException() throws Exception {
        when(webcertModelFactory.createCopy(any(CreateDraftCopyHolder.class), any(Utlatande.class))).thenThrow(new ConverterException());
        moduleApi.createNewInternalFromTemplate(new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal()), utlatande);
    }

    @Test
    public void testCreateRenewalFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCopyHolder = new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal());
        String res = moduleApi.createRenewalFromTemplate(draftCopyHolder, utlatande);

        assertNotNull(res);
        verify(webcertModelFactory).createCopy(eq(draftCopyHolder), any(Utlatande.class));
    }

    @Test(expected = ModuleConverterException.class)
    public void testCreateRenewalFromTemplateConverterException() throws Exception {
        when(webcertModelFactory.createCopy(any(CreateDraftCopyHolder.class), any(Utlatande.class))).thenThrow(new ConverterException());
        moduleApi.createRenewalFromTemplate(new CreateDraftCopyHolder(INTYG_ID, new HoSPersonal()), utlatande);
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testSendCertificateToRecipientResponseError() throws Exception {
        String xmlBody = Resources.toString(registerCertificateFile.getURL(), Charsets.UTF_8);

        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.TECHNICAL_ERROR, "error"));
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenReturn(response);
        moduleApi.sendCertificateToRecipient(xmlBody, LOGICAL_ADDRESS, "recipientId");
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testSendCertificateToRecipientSoapFault() throws Exception {
        String xmlBody = Resources.toString(registerCertificateFile.getURL(), Charsets.UTF_8);

        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenThrow(mock(SOAPFaultException.class));
        moduleApi.sendCertificateToRecipient(xmlBody, LOGICAL_ADDRESS, "recipientId");
    }

    @Test
    public void testSendCertificateToRecipientResponseResultMissing() throws Exception {
        String xmlBody = Resources.toString(registerCertificateFile.getURL(), Charsets.UTF_8);

        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenReturn(new RegisterCertificateResponseType());
        moduleApi.sendCertificateToRecipient(xmlBody, LOGICAL_ADDRESS, "recipientId");
        verify(registerCertificateResponderInterface).registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class));
    }

    @Test
    public void testSendCertificateToRecipient() throws Exception {
        String xmlBody = Resources.toString(registerCertificateFile.getURL(), Charsets.UTF_8);

        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenReturn(response);
        moduleApi.sendCertificateToRecipient(xmlBody, LOGICAL_ADDRESS, "recipientId");
        verify(registerCertificateResponderInterface).registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class));
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientXmlBodyMissing() throws Exception {
        moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, "recipientId");
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientLogicalAddressMissing() throws Exception {
        moduleApi.sendCertificateToRecipient("xml", "", "recipientId");
    }

    @Test
    public void testGetCertificate() throws Exception {
        GetCertificateResponseType getCertificateResponse = JAXB.unmarshal(getCertificateFile.getFile(), GetCertificateResponseType.class);
        when(getCertificateResponderInterface.getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class)))
                .thenReturn(getCertificateResponse);
        doReturn("additionalInfo").when(moduleApi).getAdditionalInfo(any(Intyg.class));
        doReturn(utlatande).when(moduleApi).transportToInternal(any(Intyg.class));

        CertificateResponse res = moduleApi.getCertificate(INTYG_ID, LOGICAL_ADDRESS, "INVANA");
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getMetaData().getCertificateId());
        assertEquals("additionalInfo", res.getMetaData().getAdditionalInfo());
        assertFalse(res.isRevoked());
        ArgumentCaptor<GetCertificateType> parametersCaptor = ArgumentCaptor.forClass(GetCertificateType.class);
        verify(getCertificateResponderInterface).getCertificate(eq(LOGICAL_ADDRESS), parametersCaptor.capture());
        assertEquals(INTYG_ID, parametersCaptor.getValue().getIntygsId().getExtension());
        assertNotNull(parametersCaptor.getValue().getIntygsId().getRoot());
    }

    @Test
    public void testGetCertificateRevoked() throws Exception {
        GetCertificateResponseType getCertificateResponse = JAXB.unmarshal(getCertificateFile.getFile(), GetCertificateResponseType.class);
        IntygsStatus revokedStatus = new IntygsStatus();
        revokedStatus.setPart(new Part());
        revokedStatus.getPart().setCode("HSVARD");
        revokedStatus.setStatus(new Statuskod());
        revokedStatus.getStatus().setCode(StatusKod.CANCEL.name());
        getCertificateResponse.getIntyg().getStatus().add(revokedStatus);
        when(getCertificateResponderInterface.getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class)))
                .thenReturn(getCertificateResponse);
        doReturn("additionalInfo").when(moduleApi).getAdditionalInfo(any(Intyg.class));
        doReturn(utlatande).when(moduleApi).transportToInternal(any(Intyg.class));

        CertificateResponse res = moduleApi.getCertificate(INTYG_ID, LOGICAL_ADDRESS, "INVANA");
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getMetaData().getCertificateId());
        assertEquals("additionalInfo", res.getMetaData().getAdditionalInfo());
        assertTrue(res.isRevoked());
        verify(getCertificateResponderInterface).getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class));
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateConvertException() throws Exception {
        GetCertificateResponseType getCertificateResponse = JAXB.unmarshal(getCertificateFile.getFile(), GetCertificateResponseType.class);
        when(getCertificateResponderInterface.getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class)))
                .thenReturn(getCertificateResponse);
        doThrow(new ConverterException()).when(moduleApi).transportToInternal(any(Intyg.class));

        moduleApi.getCertificate(INTYG_ID, LOGICAL_ADDRESS, "INVANA");
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateSoapFault() throws Exception {
        when(getCertificateResponderInterface.getCertificate(eq(LOGICAL_ADDRESS), any(GetCertificateType.class)))
                .thenThrow(mock(SOAPFaultException.class));

        moduleApi.getCertificate(INTYG_ID, LOGICAL_ADDRESS, "INVANA");
    }

    @Test
    public void testRegisterCertificate() throws Exception {
        RegisterCertificateType registerCertificateType = JAXB.unmarshal(registerCertificateFile.getFile(), RegisterCertificateType.class);
        doReturn(registerCertificateType).when(moduleApi).internalToTransport(any(Utlatande.class));
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenReturn(response);

        moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
    }

    @Test(expected = ModuleConverterException.class)
    public void testRegisterCertificateConverterException() throws Exception {
        doThrow(new ConverterException()).when(moduleApi).internalToTransport(any(Utlatande.class));

        moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
    }

    @Test
    public void testRegisterCertificateResponseError() throws Exception {
        RegisterCertificateType registerCertificateType = JAXB.unmarshal(registerCertificateFile.getFile(), RegisterCertificateType.class);
        doReturn(registerCertificateType).when(moduleApi).internalToTransport(any(Utlatande.class));
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenReturn(response);

        try {
            moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
            fail("should throw");
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
        }
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        RegisterCertificateType registerCertificateType = JAXB.unmarshal(registerCertificateFile.getFile(), RegisterCertificateType.class);
        doReturn(registerCertificateType).when(moduleApi).internalToTransport(any(Utlatande.class));
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenReturn(response);

        try {
            moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
            fail("should throw");
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
        }
    }

    @Test
    public void testRegisterCertificateOtherInfoResult() throws Exception {
        RegisterCertificateType registerCertificateType = JAXB.unmarshal(registerCertificateFile.getFile(), RegisterCertificateType.class);
        doReturn(registerCertificateType).when(moduleApi).internalToTransport(any(Utlatande.class));
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Other info"));
        when(registerCertificateResponderInterface.registerCertificate(eq(LOGICAL_ADDRESS), any(RegisterCertificateType.class)))
                .thenReturn(response);

        try {
            moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
            fail("should throw");
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
        }
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String otherHosPersonalName = "Other Person";
        doAnswer(invocation -> (Utlatande) invocation.getArguments()[0]).when(moduleApi)
                .decorateDiagnoserWithDescriptions(any(Utlatande.class));

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(otherHosPersonalName);
        String res = moduleApi.updateBeforeSave(json, hosPersonal);
        assertNotNull(res);
        assertEquals(otherHosPersonalName, moduleApi.getInternal(res).getGrundData().getSkapadAv().getFullstandigtNamn());
        assertNull(moduleApi.getInternal(res).getGrundData().getSigneringsdatum());
        verify(moduleApi).decorateDiagnoserWithDescriptions(any(Utlatande.class));
    }

    @Test(expected = ModuleException.class)
    public void testUpdateBeforeSaveInvalidJson() throws Exception {
        moduleApi.updateBeforeSave("invalidJson", new HoSPersonal());
    }

    @Test
    public void testUpdatePatientBeforeSave() throws Exception {
        Patient updatedPatient = createUpdatedPatient();
        String res = moduleApi.updateBeforeSave(json, updatedPatient);
        assertNotNull(res);
        assertEquals(updatedPatient, moduleApi.getInternal(res).getGrundData().getPatient());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String otherHosPersonalName = "Other Person";
        final LocalDateTime signDate = LocalDateTime.now();
        doAnswer(invocation -> (Utlatande) invocation.getArguments()[0]).when(moduleApi)
                .decorateDiagnoserWithDescriptions(any(Utlatande.class));

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(otherHosPersonalName);
        String res = moduleApi.updateBeforeSigning(json, hosPersonal, signDate);
        assertNotNull(res);
        assertEquals(otherHosPersonalName, moduleApi.getInternal(res).getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals(signDate, moduleApi.getInternal(res).getGrundData().getSigneringsdatum());
        verify(moduleApi).decorateDiagnoserWithDescriptions(any(Utlatande.class));
    }

    @Test(expected = ModuleException.class)
    public void testUpdateBeforeSigningInvalidJson() throws Exception {
        moduleApi.updateBeforeSigning("invalidJson", new HoSPersonal(), LocalDateTime.now());
    }

    @Test
    public void testUpdatePatientBeforeViewing() throws Exception {
        Patient updatedPatient = createUpdatedPatient();
        String res = moduleApi.updateBeforeViewing(json,updatedPatient);
        assertNotNull(res);
        assertEquals(updatedPatient, moduleApi.getInternal(res).getGrundData().getPatient());
    }

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        Utlatande res = moduleApi.getUtlatandeFromJson(json);
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getId());
        assertNotNull(res.getGrundData());
    }

    @Test(expected = IOException.class)
    public void testGetUtlatandeFromJsonInvalidJson() throws Exception {
        moduleApi.getUtlatandeFromJson("invalidJson");
    }

    @Test
    public void testGetUtlatandeFromXml() throws Exception {
        String xmlBody = Resources.toString(registerCertificateFile.getURL(), Charsets.UTF_8);
        doReturn(utlatande).when(moduleApi).transportToInternal(any(Intyg.class));
        Utlatande res = moduleApi.getUtlatandeFromXml(xmlBody);
        assertNotNull(res);
        assertEquals(utlatande, res);
    }

    @Test(expected = ModuleException.class)
    public void testGetUtlatandeFromXmlConverterException() throws Exception {
        String xmlBody = Resources.toString(registerCertificateFile.getURL(), Charsets.UTF_8);
        doThrow(new ConverterException()).when(moduleApi).transportToInternal(any(Intyg.class));
        moduleApi.getUtlatandeFromXml(xmlBody);
    }

    @Test
    public void testGetIntygFromUtlatande() throws Exception {
        Intyg intyg = JAXB.unmarshal(registerCertificateFile.getFile(), RegisterCertificateType.class).getIntyg();
        doReturn(intyg).when(moduleApi).utlatandeToIntyg(any(Utlatande.class));
        Intyg res = moduleApi.getIntygFromUtlatande(new TestUtlatande());
        assertNotNull(res);
        assertEquals(INTYG_ID, res.getIntygsId().getExtension());
    }

    @Test(expected = ModuleException.class)
    public void testGetIntygFromUtlatandeConverterException() throws Exception {
        doThrow(new ConverterException()).when(moduleApi).utlatandeToIntyg(any(Utlatande.class));
        moduleApi.getIntygFromUtlatande(new TestUtlatande());
    }

    @Test
    public void testTransformToStatisticsService() throws Exception {
        final String inputString = "input string";
        String res = moduleApi.transformToStatisticsService(inputString);
        assertEquals(inputString, res);
    }

    @Test
    public void testvalidateXml() throws Exception {
        String xmlBody = Resources.toString(registerCertificateFile.getURL(), Charsets.UTF_8);
        when(validator.validateSchematron(any(Source.class))).thenReturn(new SchematronOutputType());
        ValidateXmlResponse res = moduleApi.validateXml(xmlBody);
        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
    }

    @Test
    public void testGetModuleSpecificArendeParameters() throws Exception {
        final String svarIdHelperAnswer1 = "svarIdHelperAnswer1";
        final String svarIdHelperAnswer2 = "svarIdHelperAnswer2";
        List<String> frageIds = Arrays.asList("1", "2", "9001");
        when(svarIdHelper.calculateFrageIdHandleForGrundForMU(any(Utlatande.class)))
                .thenReturn(Arrays.asList(svarIdHelperAnswer1, svarIdHelperAnswer2));

        Map<String, List<String>> res = moduleApi.getModuleSpecificArendeParameters(new TestUtlatande(), frageIds);
        assertEquals(3, res.keySet().size());
        assertEquals(2, res.get("1").size());
        assertEquals(svarIdHelperAnswer1, res.get("1").get(0));
        assertEquals(svarIdHelperAnswer2, res.get("1").get(1));
        assertEquals(1, res.get("2").size());
        assertEquals("kannedomOmPatient", res.get("2").get(0));
        assertEquals(1, res.get("9001").size());
        assertEquals("tillaggsfragor", res.get("9001").get(0));
        verify(svarIdHelper).calculateFrageIdHandleForGrundForMU(any(Utlatande.class));
    }

    @Test
    public void testRevokeCertificate() throws Exception {
        String xmlBody = Resources.toString(revokeCertificateFile.getURL(), Charsets.UTF_8);
        RevokeCertificateResponseType revokeResponse = new RevokeCertificateResponseType();
        revokeResponse.setResult(ResultTypeUtil.okResult());
        when(revokeCertificateClient.revokeCertificate(eq(LOGICAL_ADDRESS), any(RevokeCertificateType.class))).thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
        ArgumentCaptor<RevokeCertificateType> parametersCaptor = ArgumentCaptor.forClass(RevokeCertificateType.class);
        verify(revokeCertificateClient).revokeCertificate(eq(LOGICAL_ADDRESS), parametersCaptor.capture());
        assertNotNull(parametersCaptor.getValue());
        assertEquals(INTYG_ID, parametersCaptor.getValue().getIntygsId().getExtension());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateResponseError() throws Exception {
        String xmlBody = Resources.toString(revokeCertificateFile.getURL(), Charsets.UTF_8);
        RevokeCertificateResponseType revokeResponse = new RevokeCertificateResponseType();
        revokeResponse.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(revokeCertificateClient.revokeCertificate(eq(LOGICAL_ADDRESS), any(RevokeCertificateType.class))).thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "meddelande";

        String res = moduleApi.createRevokeRequest(utlatande, utlatande.getGrundData().getSkapadAv(), meddelande);
        RevokeCertificateType resultObject = JAXB.unmarshal(new StringReader(res), RevokeCertificateType.class);
        assertNotNull(resultObject);
        assertEquals(meddelande, resultObject.getMeddelande());
        assertEquals(INTYG_ID, resultObject.getIntygsId().getExtension());
    }

    private Patient createUpdatedPatient() throws Exception {
        Patient patient = new Patient();
        patient.setEfternamn("updated lastName");
        patient.setMellannamn("updated middle-name");
        patient.setFornamn("updated firstName");
        patient.setFullstandigtNamn("updated full name");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        patient.setPostadress("updated postal address");
        patient.setPostnummer("54321");
        patient.setPostort("updated post city");

        return patient;
    }

    public static class TestUtlatande implements Utlatande {

        private String typ;

        private String id;

        private String textVersion;

        private GrundData grundData = new GrundData();

        private String signature;

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getTyp() {
            return typ;
        }

        @Override
        public GrundData getGrundData() {
            return grundData;
        }

        @Override
        public String getTextVersion() {
            return textVersion;
        }

        @Override
        public String getSignature() {
            return signature;
        }
    }

}
