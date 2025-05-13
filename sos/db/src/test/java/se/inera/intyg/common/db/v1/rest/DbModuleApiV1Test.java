/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.db.support.DbModuleEntryPoint;
import se.inera.intyg.common.db.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.db.v1.utils.ScenarioFinder;
import se.inera.intyg.common.db.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.db.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.support.facade.model.CertificateText;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.config.CareProviderMappingConfigLoader;
import se.inera.intyg.common.support.modules.converter.CareProviderMapperUtil;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadEnum;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
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
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {BefattningService.class, CareProviderMappingConfigLoader.class, CareProviderMapperUtil.class, InternalConverterUtil.class})
class DbModuleApiV1Test {

    private static final String LOGICAL_ADDRESS = "logical address";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;
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
    @Mock
    private IntygTextsService intygTexts;
    @Mock
    private SummaryConverter summaryConverter;

    @InjectMocks
    private DbModuleApiV1 moduleApi;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
        ReflectionTestUtils.setField(moduleApi, "internalDraftValidator", internalDraftValidator);
    }

    @Test
    void testSendCertificateShouldFailOnNullModelHolder() {
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null)
        );
    }

    @Test
    void testSendCertificateShouldFailOnEmptyXml() {
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null)
        );
    }

    @Test
    void testSendCertificateShouldFailOnNullLogicalAddress() {
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient("blaha", null, null)
        );
    }

    @Test
    void testSendCertificateShouldFailOnEmptyLogicalAddress() {
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient("blaha", "", null)
        );
    }

    @Test
    void testValidateShouldUseValidator() throws Exception {
        when(objectMapper.readValue("internal model", DbUtlatandeV1.class)).thenReturn(null);
        moduleApi.validateDraft("internal model");
        verify(internalDraftValidator, times(1)).validateDraft(any());
    }

    @Test
    void testCreateNewInternal() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenReturn(null);
        when(objectMapper.writeValueAsString(any())).thenReturn("internal model");
        moduleApi.createNewInternal(createDraftHolder());
        verify(webcertModelFactory, times(1)).createNewWebcertDraft(any());
    }

    @Test
    void testCreateNewInternalThrowsModuleException() throws ConverterException {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenThrow(new ConverterException());
        assertThrows(ModuleException.class, () ->
            moduleApi.createNewInternal(createDraftHolder())
        );
    }

    @Test
    void testCreateNewInternalFromTemplate() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenReturn(null);
        moduleApi.createNewInternalFromTemplate(createCopyHolder(), null);
        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    void testGetCertificate() throws Exception {
        final var certificateId = "certificateId";
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";

        when(getCertificateResponder.getCertificate(eq(logicalAddress), any())).thenReturn(createGetCertificateResponseType());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);

        final var certificate = moduleApi.getCertificate(certificateId, logicalAddress, "INVANA");
        final var captor = ArgumentCaptor.forClass(GetCertificateType.class);
        verify(getCertificateResponder, times(1)).getCertificate(eq(logicalAddress), captor.capture());
        assertEquals(certificateId, captor.getValue().getIntygsId().getExtension());
        assertEquals(internalModel, certificate.getInternalModel());
        assertFalse(certificate.isRevoked());
    }

    @Test
    void testGetUtlatandeFromJson() throws Exception {
        final var utlatandeJson = "utlatandeJson";
        when(objectMapper.readValue(utlatandeJson, DbUtlatandeV1.class))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());

        final var utlatandeFromJson = moduleApi.getUtlatandeFromJson(utlatandeJson);
        assertNotNull(utlatandeFromJson);
    }

    @Test
    void testUpdateBeforeSave() throws Exception {
        final var internalModel = "internal model";
        when(objectMapper.readValue(anyString(), eq(DbUtlatandeV1.class)))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);

        final var response = moduleApi.updateBeforeSave(internalModel, createHosPersonal());
        assertEquals(internalModel, response);
    }

    @Test
    void testUpdateBeforeSigning() throws Exception {
        final var internalModel = "internal model";
        when(objectMapper.readValue(anyString(), eq(DbUtlatandeV1.class)))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);

        final var response = moduleApi.updateBeforeSigning(internalModel, createHosPersonal(), null);
        assertEquals(internalModel, response);
    }

    @Test
    void testUpdateBeforeViewing() throws Exception {
        final var updatedPatient = createPatient();
        updatedPatient.setPostadress("updated postal address");
        updatedPatient.setPostnummer("54321");
        updatedPatient.setPostort("updated post city");

        final var validMinimalJson = getResourceAsString(new ClassPathResource("v1/internal/scenarios/pass-1.json"));
        when(objectMapper.readValue(validMinimalJson, DbUtlatandeV1.class)).thenReturn(
            ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(validMinimalJson);

        final var res = moduleApi.updateBeforeViewing(validMinimalJson, updatedPatient);
        assertNotNull(res);
        JSONAssert.assertEquals(validMinimalJson, res, JSONCompareMode.LENIENT);
    }

    @Test
    void testSendCertificateShouldFailWhenErrorIsReturned() throws IOException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.ERROR));

        final var xmlContents = Resources.toString(Resources.getResource("v1/db.xml"), Charsets.UTF_8);
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null)
        );
    }

    @Test
    void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            final var xmlContents = Resources.toString(Resources.getResource("v1/db.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);

            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());

        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test
    void testRegisterCertificate() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);
        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());

        moduleApi.registerCertificate(internalModel, logicalAddress);

        final var captor = ArgumentCaptor.forClass(RegisterCertificateType.class);
        verify(registerCertificateResponderInterface, times(1)).registerCertificate(eq(logicalAddress), captor.capture());
        assertNotNull(captor.getValue().getIntyg());
    }

    @Test
    void testRegisterCertificateAlreadyExists() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);
        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ExternalServiceCallException.ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
            assertEquals("Certificate already exists", e.getMessage());
        }
    }

    @Test
    void testRegisterCertificateGenericInfoResult() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
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

    @Test
    void testRegisterCertificateShouldThrowExceptionOnFailedCallToIT() throws IOException, ScenarioNotFoundException {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "resultText"));

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);
        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class))
            .thenReturn(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());

        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.registerCertificate(internalModel, logicalAddress)
        );
    }

    @Test
    void testRegisterCertificateShouldThrowExceptionOnBadCertificate() throws IOException {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        when(objectMapper.readValue(internalModel, DbUtlatandeV1.class)).thenReturn(null);
        assertThrows(ModuleConverterException.class, () ->
            moduleApi.registerCertificate(internalModel, logicalAddress)
        );
    }

    @Test
    void testGetCertificateThrowsModuleException() throws SOAPException {
        final var certificateId = "certificateId";
        final var logicalAddress = "logicalAddress";
        when(getCertificateResponder.getCertificate(eq(logicalAddress), any()))
            .thenThrow(new SOAPFaultException(SOAPFactory.newInstance().createFault()));
        assertThrows(ModuleException.class, () ->
            moduleApi.getCertificate(certificateId, logicalAddress, "INVANA")
        );
    }

    @Test
    void testRevokeCertificate() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var xmlContents = Resources.toString(Resources.getResource("revokerequest.xml"), Charsets.UTF_8);
        final var returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test
    void testRevokeCertificateThrowsExternalServiceCallException() throws IOException {
        final var logicalAddress = "logicalAddress";
        final var xmlContents = Resources.toString(Resources.getResource("revokerequest.xml"), Charsets.UTF_8);
        final var returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "resultText"));

        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);

        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.revokeCertificate(xmlContents, logicalAddress)
        );
    }

    @Test
    void testCreateRevokeRequest() throws Exception {
        final var meddelande = "revokeMessage";
        final var gd = new GrundData();
        gd.setPatient(new Patient());
        gd.getPatient().setPersonId(Personnummer.createPersonnummer("191212121212").orElseThrow());
        final var skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);
        final var utlatande = createUtlatande();// DbUtlatande.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();
        final var res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    void testGetAdditionalInfo() throws Exception {
        final var additionalInfo = moduleApi.getAdditionalInfo(null);

        assertNotNull(additionalInfo);
        assertEquals("", additionalInfo);
    }

    @Test
    void testGetUtlatandeFromXml() {
        try {
            final var xmlContents = Resources.toString(Resources.getResource("v1/db.xml"), Charsets.UTF_8);
            final var res = (DbUtlatandeV1) moduleApi.getUtlatandeFromXml(xmlContents);
            assertEquals("1234567", res.getId());
            assertEquals("körkort", res.getIdentitetStyrkt());
            assertEquals(DodsplatsBoende.SJUKHUS, res.getDodsplatsBoende());
        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    private Utlatande createUtlatande() {
        final var gd = new GrundData();
        gd.setPatient(new Patient());
        gd.getPatient().setPersonId(Personnummer.createPersonnummer("191212121212").orElseThrow());
        final var skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);
        return DbUtlatandeV1.builder().setId("intygId").setGrundData(gd).setTextVersion("").build();
    }

    private GetCertificateResponseType createGetCertificateResponseType() throws ScenarioNotFoundException {
        final var res = new GetCertificateResponseType();
        final var registerType = ScenarioFinder.getInternalScenario("pass-1").asTransportModel();
        res.setIntyg(registerType.getIntyg());
        return res;
    }

    private CreateDraftCopyHolder createCopyHolder() {
        return new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
    }

    private CreateNewDraftHolder createDraftHolder() {
        final var patient = new Patient();
        patient.setFornamn("fornamn");
        patient.setEfternamn("efternamn");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        return new CreateNewDraftHolder("certificateId", "1.0", createHosPersonal(), patient);
    }

    private HoSPersonal createHosPersonal() {
        final var hosPerson = new HoSPersonal();
        hosPerson.setPersonId("hsaId1");
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
    }

    private Patient createPatient() {
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        patient.setFornamn("fornamn");
        patient.setEfternamn("efternamn");
        return patient;
    }

    private Vardenhet createVardenhet() {
        final var vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("hsaId");
        vardenhet.setEnhetsnamn("ve1");
        vardenhet.setVardgivare(new Vardgivare());
        vardenhet.getVardgivare().setVardgivarid("vg1");
        vardenhet.getVardgivare().setVardgivarnamn("vg1");
        return vardenhet;
    }

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        final var retVal = new RegisterCertificateResponseType();
        final var value = new ResultType();
        value.setResultCode(res);
        retVal.setResult(value);
        return retVal;
    }

    private String getResourceAsString(ClassPathResource cpr) throws IOException {
        return Resources.toString(cpr.getURL(), Charsets.UTF_8);
    }

    @Test
    void getCertficateMessagesProviderGetExistingKey() {
        final var certificateMessagesProvider = moduleApi.getMessagesProvider();
        assertEquals("Fortsätt", certificateMessagesProvider.get("common.continue"));
    }

    @Test
    void getCertficateMessagesProviderGetMissingKey() {
        final var certificateMessagesProvider = moduleApi.getMessagesProvider();
        assertNull(certificateMessagesProvider.get("not.existing"));
    }

    @Test
    void shallRetrieveMunicipalitiesWhenGetCertificateFromJson() throws Exception {
        final var typeAheadProvider = mock(TypeAheadProvider.class);
        final var grundData = getGrundData();
        when(objectMapper.readValue("internal model", DbUtlatandeV1.class)).thenReturn(DbUtlatandeV1.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(grundData)
            .build());
        when(intygTexts.getIntygTextsPojo(any(), any())).thenReturn(
            new IntygTexts("1.0", DbModuleEntryPoint.MODULE_ID, LocalDate.now(), LocalDate.now().plusDays(1),
                Maps.newTreeMap(), Collections.emptyList(), new Properties()));
        doReturn(CertificateSummary.builder().build())
            .when(summaryConverter).convert(eq(moduleApi), any(Intyg.class));

        moduleApi.getCertificateFromJson("internal model", typeAheadProvider);
        verify(typeAheadProvider).getValues(TypeAheadEnum.MUNICIPALITIES);
    }

    @Test
    void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande()
        throws ModuleException, ScenarioNotFoundException {
        final var utlatande = ScenarioFinder.getInternalScenario("pass-1").asInternalModel();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = moduleApi.getJsonFromUtlatande(utlatande);

        assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> moduleApi.getJsonFromUtlatande(null));
    }

    @Test
    void shouldReturnPreambleForCitizens() {
        final var expectedResponse = CertificateText.builder()
            .text("")
            .build();

        final var response = moduleApi.getPreambleForCitizens();

        assertEquals(expectedResponse, response);
    }

    private String toJsonString(DbUtlatandeV1 utlatande) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
    }

    private static GrundData getGrundData() {
        final var unit = new Vardenhet();
        final var skapadAv = new HoSPersonal();
        final var patient = new Patient();
        final var grundData = new GrundData();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        final var vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("id");
        unit.setVardgivare(vardgivare);
        skapadAv.setVardenhet(unit);
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        return grundData;
    }
}
