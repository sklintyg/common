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
package se.inera.intyg.common.db.v1.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.soap.SOAPFaultException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.db.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.db.v1.utils.ScenarioFinder;
import se.inera.intyg.common.db.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.db.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
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
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.services.BefattningService;
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
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class DbModuleApiTest {

    private static final String LOGICAL_ADDRESS = "logical address";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private GetCertificateResponderInterface getCertificateResponder;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @Mock
    private WebcertModelFactoryImpl webcertModelFactory;

    @Mock
    private InternalDraftValidatorImpl internalDraftValidator;

    @InjectMocks
    private DbModuleApiV1 moduleApi;

    public DbModuleApiTest() {
        MockitoAnnotations.initMocks(this);
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
        when(objectMapper.readValue(eq("internal model"), eq(DbUtlatandeV1.class))).thenReturn(null);
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

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        final String utlatandeJson = "utlatandeJson";
        when(objectMapper.readValue(eq(utlatandeJson), eq(DbUtlatandeV1.class)))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        Utlatande utlatandeFromJson = moduleApi.getUtlatandeFromJson(utlatandeJson);
        assertNotNull(utlatandeFromJson);
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String internalModel = "internal model";
        when(objectMapper.readValue(anyString(), eq(DbUtlatandeV1.class)))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);
        String response = moduleApi.updateBeforeSave(internalModel, createHosPersonal());
        assertEquals(internalModel, response);
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String internalModel = "internal model";
        when(objectMapper.readValue(anyString(), eq(DbUtlatandeV1.class)))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);
        String response = moduleApi.updateBeforeSigning(internalModel, createHosPersonal(), null);
        assertEquals(internalModel, response);
    }

    @Test
    public void testUpdateBeforeViewing() throws Exception {
        Patient updatedPatient = createPatient("fornamn", "efternamn", "19121212-1212");
        updatedPatient.setPostadress("updated postal address");
        updatedPatient.setPostnummer("54321");
        updatedPatient.setPostort("updated post city");

        final String validMinimalJson = getResourceAsString(new ClassPathResource("v1/internal/scenarios/pass-1.json"));
        final String res = moduleApi.updateBeforeViewing(validMinimalJson, updatedPatient);
        assertNotNull(res);
        JSONAssert.assertEquals(validMinimalJson, res, JSONCompareMode.LENIENT);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailWhenErrorIsReturned() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.ERROR));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v1/db.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v1/db.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);

            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());

        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test
    public void testRegisterCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());

        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
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

        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ExternalServiceCallException.ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
            assertEquals("Certificate already exists", e.getMessage());
        }
    }

    @Test
    public void testRegisterCertificateGenericInfoResult() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("INFO"));

        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ExternalServiceCallException.ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateShouldThrowExceptionOnFailedCallToIT() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "resultText"));

        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(internalModel, logicalAddress);
    }

    @Test(expected = ModuleConverterException.class)
    public void testRegisterCertificateShouldThrowExceptionOnBadCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class)).thenReturn(null);

        moduleApi.registerCertificate(internalModel, logicalAddress);
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
    public void testRevokeCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = Resources.toString(Resources.getResource("revokerequest.xml"), Charsets.UTF_8);

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateThrowsExternalServiceCallException() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = Resources.toString(Resources.getResource("revokerequest.xml"), Charsets.UTF_8);

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "resultText"));
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "revokeMessage";
        final String intygId = "intygId";

        GrundData gd = new GrundData();
        gd.setPatient(new Patient());
        gd.getPatient().setPersonId(Personnummer.createPersonnummer("191212121212").get());
        HoSPersonal skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);

        Utlatande utlatande = createUtlatande();// DbUtlatande.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();

        String res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    public void testGetAdditionalInfo() throws Exception {
        String additionalInfo = moduleApi.getAdditionalInfo(null);

        assertNotNull(additionalInfo);
        assertEquals("", additionalInfo);
    }

    @Test
    public void testGetUtlatandeFromXml() {
        try {
            String xmlContents = Resources.toString(Resources.getResource("v1/db.xml"), Charsets.UTF_8);
            DbUtlatandeV1 res = (DbUtlatandeV1) moduleApi.getUtlatandeFromXml(xmlContents);

            assertEquals("1234567", res.getId());
            assertEquals("körkort", res.getIdentitetStyrkt());
            assertEquals(DodsplatsBoende.SJUKHUS, res.getDodsplatsBoende());
        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    private Utlatande createUtlatande() {
        GrundData gd = new GrundData();
        gd.setPatient(new Patient());
        gd.getPatient().setPersonId(Personnummer.createPersonnummer("191212121212").get());
        HoSPersonal skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);
        return DbUtlatandeV1.builder().setId("intygId").setGrundData(gd).setTextVersion("").build();
    }

    private GetCertificateResponseType createGetCertificateResponseType() throws ScenarioNotFoundException {
        GetCertificateResponseType res = new GetCertificateResponseType();
        RegisterCertificateType registerType = ScenarioFinder.getInternalScenario("pass-1").asTransportModel();
        res.setIntyg(registerType.getIntyg());
        return res;
    }

    private CreateDraftCopyHolder createCopyHolder() {
        return new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
    }

    private CreateNewDraftHolder createDraftHolder() {
        Patient patient = new Patient();
        patient.setFornamn("fornamn");
        patient.setEfternamn("efternamn");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        return new CreateNewDraftHolder("certificateId", "1.0", createHosPersonal(), patient);
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("hsaId1");
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
    }

    private Patient createPatient(String fornamn, String efternamn, String personnummer) {
        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer((personnummer != null) ? personnummer : "191212121212").get());
        patient.setFornamn(fornamn);
        patient.setEfternamn(efternamn);
        return patient;
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

    private String getResourceAsString(ClassPathResource cpr) throws IOException {
        return Resources.toString(cpr.getURL(), Charsets.UTF_8);
    }
}
