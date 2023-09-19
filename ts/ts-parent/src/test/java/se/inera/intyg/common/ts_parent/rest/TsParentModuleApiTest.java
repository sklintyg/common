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
package se.inera.intyg.common.ts_parent.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.xml.bind.JAXB;
import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_parent.validator.InternalDraftValidator;
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
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.IntygsStatus;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TsParentModuleApiTest {

    private static final String INTYG_TYPE_VERSION_1 = "1.0";

    private final String INTYG_ID = "test-id";
    private final String LOGICAL_ADDRESS = "logicalAddress";

    private static ClassPathResource getCertificateFile;
    private static ClassPathResource registerCertificateFile;
    private static ClassPathResource revokeCertificateFile;
    private static Utlatande utlatande;
    private static String json;

    @SuppressWarnings("unchecked")
    @InjectMocks
    private TsParentModuleApi<Utlatande> moduleApi = mock(TsParentModuleApi.class, Mockito.CALLS_REAL_METHODS);
    @Mock
    private InternalDraftValidator<Utlatande> internalDraftValidator;
    @Mock
    private WebcertModelFactory<Utlatande> webcertModelFactory;
    @Mock
    private GetCertificateResponderInterface getCertificateResponderInterface;
    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;
    @Mock
    private RevokeCertificateResponderInterface revokeCertificateClient;
    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    public TsParentModuleApiTest() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeClass
    public static void set() throws Exception {
        getCertificateFile = new ClassPathResource("getCertificate.xml");
        registerCertificateFile = new ClassPathResource("registerCertificate.xml");
        revokeCertificateFile = new ClassPathResource("revokeCertificate.xml");
        json = Resources.toString(new ClassPathResource("utlatande.json").getURL(), Charsets.UTF_8);
        utlatande = new CustomObjectMapper().readValue(json, TestUtlatande.class);
    }

    @Before
    public void setup() throws Exception {
        Field field = TsParentModuleApi.class.getDeclaredField("type");
        field.setAccessible(true);
        field.set(moduleApi, TestUtlatande.class);
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
        CreateNewDraftHolder draftCertificateHolder = new CreateNewDraftHolder(INTYG_ID, INTYG_TYPE_VERSION_1, new HoSPersonal(),
            new Patient());
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

    @Test(expected = ModuleException.class)
    public void testPdfEmployer() throws Exception {
        moduleApi.pdfEmployer("internalModel", new ArrayList<>(), ApplicationOrigin.INTYGSTJANST, null, UtkastStatus.SIGNED);
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String otherHosPersonalName = "Other Person";

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(otherHosPersonalName);
        String res = moduleApi.updateBeforeSave(json, hosPersonal);
        assertNotNull(res);
        assertEquals(otherHosPersonalName, moduleApi.getInternal(res).getGrundData().getSkapadAv().getFullstandigtNamn());
        assertNull(moduleApi.getInternal(res).getGrundData().getSigneringsdatum());
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
        final LocalDateTime signDate = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn(otherHosPersonalName);
        String res = moduleApi.updateBeforeSigning(json, hosPersonal, signDate);
        assertNotNull(res);
        assertEquals(otherHosPersonalName, moduleApi.getInternal(res).getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals(signDate, moduleApi.getInternal(res).getGrundData().getSigneringsdatum());
    }

    @Test(expected = ModuleException.class)
    public void testUpdateBeforeSigningInvalidJson() throws Exception {
        moduleApi.updateBeforeSigning("invalidJson", new HoSPersonal(), LocalDateTime.now());
    }

    @Test
    public void testUpdateBeforeViewing() throws Exception {
        Patient updatedPatient = createUpdatedPatient();

        String res = moduleApi.updateBeforeViewing(json, updatedPatient);
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
    public void testGetIntygFromUtlatande() throws Exception {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension(INTYG_ID);
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

    /*
        @Test(expected = UnsupportedOperationException.class)
        public void testvalidateXml() throws Exception {
            moduleApi.validateXml("xmlBody");
        }
    */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetModuleSpecificArendeParameters() throws Exception {
        moduleApi.getModuleSpecificArendeParameters(new TestUtlatande(), new ArrayList<>());
    }

    @Test
    public void testGetAdditionalInfo() throws Exception {
        Intyg intyg = JAXB.unmarshal(getCertificateFile.getFile(), GetCertificateResponseType.class).getIntyg();
        assertEquals("C1, C1E, C", moduleApi.getAdditionalInfo(intyg));
    }

    @Test
    public void testGetAdditionalInfoConverterException() throws Exception {
        Intyg intyg = JAXB.unmarshal(getCertificateFile.getFile(), GetCertificateResponseType.class).getIntyg();
        intyg.getSvar().get(0).getDelsvar().get(0).getContent().clear();
        assertNull(moduleApi.getAdditionalInfo(intyg));
    }

    @Test
    public void testGetAdditionalInfoNoTypes() throws Exception {
        Intyg intyg = JAXB.unmarshal(getCertificateFile.getFile(), GetCertificateResponseType.class).getIntyg();
        intyg.getSvar().clear();
        assertNull(moduleApi.getAdditionalInfo(intyg));
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
            assertEquals(ExternalServiceCallException.ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
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
            assertEquals(ExternalServiceCallException.ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
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
            assertEquals(ExternalServiceCallException.ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
        }
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

    @Test
    public void testHandleResponseInfo() throws Exception {
        RegisterCertificateResponseType response = createRegisterCertificateResponse(ResultCodeType.INFO);
        RegisterCertificateType request = new RegisterCertificateType();

        moduleApi.handleResponse(response, request);
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testHandleResponseError() throws Exception {
        RegisterCertificateResponseType response = createRegisterCertificateResponse(ResultCodeType.ERROR);
        RegisterCertificateType request = new RegisterCertificateType();

        moduleApi.handleResponse(response, request);
    }

    @Test
    public void shouldReturnAdditionalInfoLabel() {
        final var response = moduleApi.getAdditionalInfoLabel();

        assertEquals("Avser behörighet", response);
    }

    private RegisterCertificateResponseType createRegisterCertificateResponse(ResultCodeType resultCodeType) {
        var response = new RegisterCertificateResponseType();
        ResultType resultType = new ResultType();
        resultType.setResultCode(resultCodeType);
        response.setResult(resultType);
        return response;
    }

    private Patient createUpdatedPatient() {
        Patient updatedPatient = new Patient();
        updatedPatient.setEfternamn("updated lastName");
        updatedPatient.setMellannamn("updated middle-name");
        updatedPatient.setFornamn("updated firstName");
        updatedPatient.setFullstandigtNamn("updated full name");
        updatedPatient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        updatedPatient.setPostadress("updated postal address");
        updatedPatient.setPostnummer("54321");
        updatedPatient.setPostort("updated post city");

        return updatedPatient;
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
