/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ag114.v1.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.ag114.pdf.PdfGenerator;
import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
import se.inera.intyg.common.ag114.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ag114.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.ag114.v1.utils.ScenarioFinder;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateXmlResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.IntygsStatus;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

import javax.xml.soap.SOAPFactory;
import javax.xml.transform.Source;
import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class Ag114ModuleApiTest {

    private static final String LOGICAL_ADDRESS = "logical address";
    private static final String TEST_HSA_ID = "hsaId";
    private static final String TEST_PATIENT_PERSONNR = "191212121212";
    private static final String INTYG_TYPE_VERSION_1 = "1.0";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private GetCertificateResponderInterface getCertificateResponderInterface;

    @Mock
    private RegisterCertificateValidator validator;

    @Mock
    private InternalDraftValidator<Utlatande> internalDraftValidator;

    @Spy
    private IntygTextsService intygTextsServiceMock;

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Mock
    private PdfGenerator pdfGenerator;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @Mock
    private WebcertModuleService moduleService;

    @InjectMocks
    private Ag114ModuleApiV1 moduleApi;

    public Ag114ModuleApiTest() {
        MockitoAnnotations.initMocks(this);
    }


    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(webcertModelFactory, "intygTexts", intygTextsServiceMock);
        when(intygTextsServiceMock.getLatestVersionForSameMajorVersion(eq(Ag114EntryPoint.MODULE_ID), eq(INTYG_TYPE_VERSION_1)))
            .thenReturn(INTYG_TYPE_VERSION_1);
    }

    @Test
    public void testValidateDraft() throws Exception {
        when(internalDraftValidator.validateDraft(any(Utlatande.class)))
            .thenReturn(new ValidateDraftResponse(ValidationStatus.VALID, new ArrayList<>()));

        ValidateDraftResponse res = moduleApi.validateDraft(getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json"));

        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
        verify(internalDraftValidator).validateDraft(any(Utlatande.class));
    }

    @Test
    public void testGetAdditionalInfo() throws Exception {
        Ag114UtlatandeV1 utlatande = getUtlatandeFromFile();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande, moduleService);
        String result = moduleApi.getAdditionalInfo(intyg);

        assertEquals("2018-11-10 - 2018-11-20", result);
    }

    @Test
    public void testGetCertificate() throws Exception {
        GetCertificateResponseType result = createGetCertificateResponseType(StatusKod.SENTTO, "FKASSA");

        when(getCertificateResponderInterface.getCertificate(anyString(), any())).thenReturn(result);
        final CertificateResponse response = moduleApi.getCertificate("id", LOGICAL_ADDRESS, "INVANA");
        assertFalse(response.isRevoked());
    }

    @Test
    public void testGetCertificateWhenRevoked() throws Exception {
        GetCertificateResponseType result = createGetCertificateResponseType(StatusKod.CANCEL, "FKASSA");

        when(getCertificateResponderInterface.getCertificate(anyString(), any())).thenReturn(result);
        final CertificateResponse response = moduleApi.getCertificate("id", LOGICAL_ADDRESS, "INVANA");
        assertTrue(response.isRevoked());
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateWhenSOAPExceptionThrowsModuleException() throws Exception {
        SOAPFaultException ex = new SOAPFaultException(SOAPFactory.newInstance().createFault());
        doThrow(ex).when(getCertificateResponderInterface).getCertificate(anyString(), any());

        moduleApi.getCertificate("id", LOGICAL_ADDRESS, "INVANA");
    }

    @Test
    public void testRegisterCertificate() throws IOException, ModuleException {
        final String json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");

        Ag114UtlatandeV1 utlatande = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        when(objectMapper.readValue(json, Ag114UtlatandeV1.class)).thenReturn(utlatande);

        RegisterCertificateResponseType result = createReturnVal(ResultCodeType.OK);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(anyString(), eq(Ag114UtlatandeV1.class));

        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));

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

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(anyString(), eq(Ag114UtlatandeV1.class));

        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("INFO"));

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
    public void testRegisterCertificateThrowsExternalServiceCallExceptionOnErrorResultCode() throws IOException, ModuleException {
        RegisterCertificateResponseType result = createReturnVal(ResultCodeType.ERROR);
        final String json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");

        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);
        moduleApi.registerCertificate(json, LOGICAL_ADDRESS);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendCertificate() throws IOException, ModuleException {
        moduleApi.sendCertificateToRecipient("<xml/>", "logicalAddress", "recipient");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateCompletionFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());

        final String completionFromTemplate = moduleApi.createCompletionFromTemplate(draftCertificateHolder, getUtlatandeFromFile(), "No!");

        Ag114UtlatandeV1 copy = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(completionFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());

        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(Ag114UtlatandeV1.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateRenewalFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());

        final String renewalFromTemplate = moduleApi.createRenewalFromTemplate(draftCertificateHolder, getUtlatandeFromFile());

        Ag114UtlatandeV1 copy = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(renewalFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());

        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(Ag114UtlatandeV1.class));
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
        CreateDraftCopyHolder draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());

        final String renewalFromTemplate = moduleApi.createNewInternalFromTemplate(draftCertificateHolder, getUtlatandeFromFile());

        Ag114UtlatandeV1 copy = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(renewalFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());

        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(Ag114UtlatandeV1.class));
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        CreateNewDraftHolder createNewDraftHolder =
            new CreateNewDraftHolder("1", INTYG_TYPE_VERSION_1, createHosPersonal(),
                createPatient("fornamn", "efternamn", TEST_PATIENT_PERSONNR));

        final String renewalFromTemplate = moduleApi.createNewInternal(createNewDraftHolder);

        Ag114UtlatandeV1 copy = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(renewalFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());
        assertEquals(TEST_PATIENT_PERSONNR, copy.getGrundData().getPatient().getPersonId().getPersonnummer());

    }

    @Test
    public void testUpdateHoSPersonalBeforeSave() throws IOException, ModuleException {
        final String json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");

        Ag114UtlatandeV1 utlatandeBeforeSave = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(TEST_HSA_ID, utlatandeBeforeSave.getGrundData().getSkapadAv().getPersonId());

        when(objectMapper.readValue(json, Ag114UtlatandeV1.class)).thenReturn(utlatandeBeforeSave);

        final String res = moduleApi.updateBeforeSave(json, createHosPersonal());
        final Utlatande responseUtlatande = moduleApi.getUtlatandeFromJson(res);
        assertEquals(TEST_HSA_ID, responseUtlatande.getGrundData().getSkapadAv().getPersonId());
    }

    @Test
    public void testUpdatePatientBeforeSave() throws IOException, ModuleException {
        final String json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");
        final Patient updatedPatient = createPatient("Nytt", "Namn", TEST_PATIENT_PERSONNR);

        Ag114UtlatandeV1 utlatandeBeforeSave = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(updatedPatient, utlatandeBeforeSave.getGrundData().getPatient());

        when(objectMapper.readValue(json, Ag114UtlatandeV1.class)).thenReturn(utlatandeBeforeSave);

        final String res = moduleApi.updateBeforeSave(json, updatedPatient);
        final Ag114UtlatandeV1 utlatandeAfterSave = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(res);
        assertEquals(updatedPatient, utlatandeAfterSave.getGrundData().getPatient());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");
        ;
        final LocalDateTime signDate = LocalDateTime.now();

        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn("Other Person");
        assertNotEquals(hosPersonal, moduleApi.getUtlatandeFromJson(json).getGrundData().getSkapadAv());

        String res = moduleApi.updateBeforeSigning(json, hosPersonal, signDate);
        assertNotNull(res);
        Ag114UtlatandeV1 utlatandeAfterSigning = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(res);
        assertEquals(hosPersonal, utlatandeAfterSigning.getGrundData().getSkapadAv());
        assertEquals(signDate, utlatandeAfterSigning.getGrundData().getSigneringsdatum());
    }

    @Test
    public void testUpdateBeforeViewing() throws IOException, ModuleException {
        final String json = getResourceAsString("v1/internal/scenarios/pass-minimal.json");
        final Patient updatedPatient = createPatient("Nytt", "Namn", TEST_PATIENT_PERSONNR);

        Ag114UtlatandeV1 utlatandeBeforeViewing = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(updatedPatient, utlatandeBeforeViewing.getGrundData().getPatient());

        when(objectMapper.readValue(json, Ag114UtlatandeV1.class)).thenReturn(utlatandeBeforeViewing);

        final String res = moduleApi.updateBeforeViewing(json, updatedPatient);
        final Utlatande utlatandeAfterViewing = moduleApi.getUtlatandeFromJson(res);
        assertEquals(updatedPatient, utlatandeAfterViewing.getGrundData().getPatient());
    }

    @Test
    public void testRevokeCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = getResourceAsString("v1/Ag114ModuleApiTest/revokerequest.xml");

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateThrowsExternalServiceCallException() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = getResourceAsString("v1/Ag114ModuleApiTest/revokerequest.xml");

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "resultText"));
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        fail();
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "revokeMessage";
        final String intygId = "intygId";

        GrundData gd = new GrundData();
        gd.setPatient(createPatient("fornamn", "efternamn", TEST_PATIENT_PERSONNR));
        HoSPersonal skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);

        Utlatande utlatande = Ag114UtlatandeV1.builder().setId(intygId).setGrundData(gd).setTextVersion("1.0")
            .setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)))
            .build();

        String res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    // Successful usage of getUtlatandeFromJson is used in a lot of other tests.
    @Test(expected = IOException.class)
    public void testGetUtlatandeFromJsonInvalidJson() throws Exception {
        moduleApi.getUtlatandeFromJson("{ invalidJson: }");
    }

    // Successful usage of getUtlatandeFromXml is used in a lot of other tests
    // using method createGetCertificateResponseType()

    @Test(expected = ModuleException.class)
    public void testGetUtlatandeFromXmlConverterException() throws Exception {
        moduleApi.getUtlatandeFromXml("<xml/>");
    }

    @Test
    public void testTransformToStatisticsService() throws Exception {
        final String inputString = "input string";
        String res = moduleApi.transformToStatisticsService(inputString);
        assertEquals(inputString, res);
    }

    @Test
    public void testValidateXml() throws Exception {
        String xmlBody = getResourceAsString("v1/ag114-simple-valid.xml");
        when(validator.validateSchematron(any(Source.class))).thenReturn(new SchematronOutputType());
        ValidateXmlResponse res = moduleApi.validateXml(xmlBody);
        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
    }

    // Successful usage of getIntygFromUtlatande is used in a lot of other tests
    // using method createGetCertificateResponseType()

    @Test(expected = ModuleException.class)
    public void testGetIntygFromUtlatandeConverterException() throws Exception {
        Utlatande failingUtlatande = Ag114UtlatandeV1.builder().setId("").setGrundData(new GrundData()).setTextVersion("1.0")
            .setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)))
            .build();

        moduleApi.getIntygFromUtlatande(failingUtlatande);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetModuleSpecificArendeParameters() throws Exception {
        moduleApi.getModuleSpecificArendeParameters(getUtlatandeFromFile(), Arrays.asList("1", "2"));
    }

    private GetCertificateResponseType createGetCertificateResponseType(final StatusKod statusKod, final String part)
        throws IOException, ModuleException {
        GetCertificateResponseType response = new GetCertificateResponseType();

        String xmlContents = getResourceAsString("v1/ag114-simple-valid.xml");
        Utlatande utlatandeFromXml = moduleApi.getUtlatandeFromXml(xmlContents);
        Intyg intyg = moduleApi.getIntygFromUtlatande(utlatandeFromXml);

        intyg.getStatus().add(createStatus(statusKod.name(), part));

        response.setIntyg(intyg);

        return response;
    }

    private IntygsStatus createStatus(String statuskod, String recipientID) {
        IntygsStatus intygsStatus = new IntygsStatus();
        Statuskod sk = new Statuskod();
        sk.setCode(statuskod);
        intygsStatus.setStatus(sk);
        Part part = new Part();
        part.setCode(recipientID);
        intygsStatus.setPart(part);
        intygsStatus.setTidpunkt(LocalDateTime.now());
        return intygsStatus;
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

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId(TEST_HSA_ID);
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

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        ResultType value = new ResultType();

        RegisterCertificateResponseType responseType = new RegisterCertificateResponseType();

        value.setResultCode(res);
        responseType.setResult(value);
        return responseType;
    }

    private Ag114UtlatandeV1 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper()
            .readValue(Resources.getResource("v1/Ag114ModuleApiTest/valid-utkast-sample.json"), Ag114UtlatandeV1.class);
    }

    private String getResourceAsString(String resourceName) throws IOException {
        return (resourceName == null) ?
            null :
            Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
    }
}
