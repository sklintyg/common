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
package se.inera.intyg.common.ag114.v1.rest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.transform.Source;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
import se.inera.intyg.common.ag114.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.ag114.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.ag114.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ag114.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning;
import se.inera.intyg.common.ag114.v1.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag114.v1.utils.ScenarioFinder;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.config.CareProviderMappingConfigLoader;
import se.inera.intyg.common.support.modules.converter.CareProviderMapperUtil;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v3.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.IntygsStatus;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {BefattningService.class, CareProviderMappingConfigLoader.class, CareProviderMapperUtil.class, InternalConverterUtil.class})
class Ag114ModuleApiTest {

    private static final String LOGICAL_ADDRESS = "logical address";
    private static final String TEST_HSA_ID = "hsaId";
    private static final String TEST_PATIENT_PERSONNR = "191212121212";
    private static final String INTYG_TYPE_VERSION_1 = "1.0";

    @Mock
    private CertificateToInternal certificateToInternal;
    @Mock
    private InternalToCertificate internalToCertificate;
    @Mock
    private IntygTextsService intygTexts;
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
    private RevokeCertificateResponderInterface revokeClient;
    @Mock
    private WebcertModuleService moduleService;
    @Mock
    private SummaryConverter summaryConverter;

    @InjectMocks
    private Ag114ModuleApiV1 moduleApi;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(webcertModelFactory, "intygTexts", intygTextsServiceMock);
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
        ReflectionTestUtils.setField(moduleApi, "internalDraftValidator", internalDraftValidator);
    }

    @Test
    void testValidateDraft() throws Exception {
        when(internalDraftValidator.validateDraft(any(Utlatande.class)))
            .thenReturn(new ValidateDraftResponse(ValidationStatus.VALID, new ArrayList<>()));

        final var res = moduleApi.validateDraft(getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json"));

        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
        verify(internalDraftValidator).validateDraft(any(Utlatande.class));
    }

    @Test
    void testGetAdditionalInfo() throws Exception {
        final var utlatande = getUtlatandeFromFile();
        final var intyg = UtlatandeToIntyg.convert(utlatande, moduleService);
        final var result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("2018-11-10 - 2018-11-20", result);
    }

    @Test
    void testGetCertificate() throws Exception {
        final var result = createGetCertificateResponseType(StatusKod.SENTTO);
        when(getCertificateResponderInterface.getCertificate(anyString(), any())).thenReturn(result);

        final var response = moduleApi.getCertificate("id", LOGICAL_ADDRESS, "INVANA");
        assertFalse(response.isRevoked());
    }

    @Test
    void testGetCertificateWhenRevoked() throws Exception {
        final var result = createGetCertificateResponseType(StatusKod.CANCEL);
        when(getCertificateResponderInterface.getCertificate(anyString(), any())).thenReturn(result);

        final var response = moduleApi.getCertificate("id", LOGICAL_ADDRESS, "INVANA");
        assertTrue(response.isRevoked());
    }

    @Test
    void testGetCertificateWhenSOAPExceptionThrowsModuleException() throws SOAPException {
        final var ex = new SOAPFaultException(SOAPFactory.newInstance().createFault());
        doThrow(ex).when(getCertificateResponderInterface).getCertificate(anyString(), any());
        assertThrows(ModuleException.class, () ->
            moduleApi.getCertificate("id", LOGICAL_ADDRESS, "INVANA")
        );
    }

    @Test
    void testRegisterCertificate() throws IOException, ModuleException {
        final var json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");
        final var utlatande = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        when(objectMapper.readValue(json, Ag114UtlatandeV1.class)).thenReturn(utlatande);

        final var result = createReturnVal(ResultCodeType.OK);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        assertDoesNotThrow(() -> moduleApi.registerCertificate(json, LOGICAL_ADDRESS));
        verify(registerCertificateResponderInterface).registerCertificate(any(String.class), any(RegisterCertificateType.class));
    }

    @Test
    void testRegisterCertificateAlreadyExists() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(anyString(), eq(Ag114UtlatandeV1.class));

        final var response = new RegisterCertificateResponseType();
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
    void testRegisterCertificateGenericInfoResult() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(anyString(), eq(Ag114UtlatandeV1.class));

        final var response = new RegisterCertificateResponseType();
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

    @Test
    void testRegisterCertificateThrowsExternalServiceCallExceptionOnErrorResultCode() throws IOException {
        final var result = createReturnVal(ResultCodeType.ERROR);
        final var json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);
        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.registerCertificate(json, LOGICAL_ADDRESS)
        );
    }

    @Test
    void testSendCertificate() {
        assertThrows(UnsupportedOperationException.class, () ->
            moduleApi.sendCertificateToRecipient("<xml/>", "logicalAddress", "recipient")
        );
    }

    @Test
    void testCreateCompletionFromTemplate() {
        final var draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());
        assertThrows(UnsupportedOperationException.class, () ->
            moduleApi.createCompletionFromTemplate(draftCertificateHolder, getUtlatandeFromFile(), "No!")
        );
    }

    @Test
    void testCreateRenewalFromTemplate() {
        final var draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());
        assertThrows(UnsupportedOperationException.class, () ->
            moduleApi.createRenewalFromTemplate(draftCertificateHolder, getUtlatandeFromFile())
        );
    }

    @Test
    void testCreateNewInternalFromTemplate() throws Exception {
        final var draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());
        final var renewalFromTemplate = moduleApi.createNewInternalFromTemplate(draftCertificateHolder, getUtlatandeFromFile());
        final var copy = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(renewalFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());
        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(Ag114UtlatandeV1.class));
    }

    @Test
    void testCreateNewInternal() throws Exception {
        when(intygTextsServiceMock.getLatestVersionForSameMajorVersion(Ag114EntryPoint.MODULE_ID, INTYG_TYPE_VERSION_1))
            .thenReturn(INTYG_TYPE_VERSION_1);
        final var createNewDraftHolder = new CreateNewDraftHolder("1", INTYG_TYPE_VERSION_1, createHosPersonal(),
            createPatient("fornamn", "efternamn"));
        final var renewalFromTemplate = moduleApi.createNewInternal(createNewDraftHolder);
        final var copy = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(renewalFromTemplate);

        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());
        assertEquals(TEST_PATIENT_PERSONNR, copy.getGrundData().getPatient().getPersonId().getPersonnummer());

    }

    @Test
    void testUpdateHoSPersonalBeforeSave() throws IOException, ModuleException {
        final var json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");
        final var utlatandeBeforeSave = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(TEST_HSA_ID, utlatandeBeforeSave.getGrundData().getSkapadAv().getPersonId());
        when(objectMapper.readValue(json, Ag114UtlatandeV1.class)).thenReturn(utlatandeBeforeSave);

        final var res = moduleApi.updateBeforeSave(json, createHosPersonal());
        final var responseUtlatande = moduleApi.getUtlatandeFromJson(res);
        assertEquals(TEST_HSA_ID, responseUtlatande.getGrundData().getSkapadAv().getPersonId());
    }

    @Test
    void testUpdatePatientBeforeSave() throws IOException, ModuleException {
        final var json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");
        final var updatedPatient = createPatient("Nytt", "Namn");
        final var utlatandeBeforeSave = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(updatedPatient, utlatandeBeforeSave.getGrundData().getPatient());
        when(objectMapper.readValue(json, Ag114UtlatandeV1.class)).thenReturn(utlatandeBeforeSave);

        final var res = moduleApi.updateBeforeSave(json, updatedPatient);
        final var utlatandeAfterSave = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(res);
        assertEquals(updatedPatient, utlatandeAfterSave.getGrundData().getPatient());
    }

    @Test
    void testUpdateBeforeSigning() throws Exception {
        final var json = getResourceAsString("v1/Ag114ModuleApiTest/valid-utkast-sample.json");
        final var signDate = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        final var hosPersonal = new HoSPersonal();
        hosPersonal.setFullstandigtNamn("Other Person");
        assertNotEquals(hosPersonal, moduleApi.getUtlatandeFromJson(json).getGrundData().getSkapadAv());

        final var res = moduleApi.updateBeforeSigning(json, hosPersonal, signDate);
        assertNotNull(res);

        final var utlatandeAfterSigning = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(res);
        assertEquals(hosPersonal, utlatandeAfterSigning.getGrundData().getSkapadAv());
        assertEquals(signDate, utlatandeAfterSigning.getGrundData().getSigneringsdatum());
    }

    @Test
    void testUpdateBeforeViewing() throws IOException, ModuleException {
        final var json = getResourceAsString("v1/internal/scenarios/pass-minimal.json");
        final var updatedPatient = createPatient("Nytt", "Namn");
        final var utlatandeBeforeViewing = (Ag114UtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(updatedPatient, utlatandeBeforeViewing.getGrundData().getPatient());
        when(objectMapper.readValue(json, Ag114UtlatandeV1.class)).thenReturn(utlatandeBeforeViewing);

        final var res = moduleApi.updateBeforeViewing(json, updatedPatient);
        final var utlatandeAfterViewing = moduleApi.getUtlatandeFromJson(res);
        assertEquals(updatedPatient, utlatandeAfterViewing.getGrundData().getPatient());
    }

    @Test
    void testRevokeCertificate() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var xmlContents = getResourceAsString("v1/Ag114ModuleApiTest/revokerequest.xml");
        final var returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test
    void testRevokeCertificateThrowsExternalServiceCallException() throws IOException {
        final var logicalAddress = "logicalAddress";
        final var xmlContents = getResourceAsString("v1/Ag114ModuleApiTest/revokerequest.xml");
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
        final var intygId = "intygId";
        final var gd = new GrundData();
        gd.setPatient(createPatient("fornamn", "efternamn"));
        final var skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);
        final var utlatande = Ag114UtlatandeV1.builder().setId(intygId).setGrundData(gd).setTextVersion("1.0")
            .setSysselsattning(List.of(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)))
            .build();
        final var res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);

        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    void testGetUtlatandeFromJsonInvalidJson() {
        assertThrows(IOException.class, () ->
            moduleApi.getUtlatandeFromJson("{ invalidJson: }")
        );
    }


    // Successful usage of getUtlatandeFromXml is used in a lot of other tests
    // using method createGetCertificateResponseType()
    @Test
    void testGetUtlatandeFromXmlConverterException() {
        assertThrows(ModuleException.class, () ->
            moduleApi.getUtlatandeFromXml("<xml/>")
        );
    }

    @Test
    void testTransformToStatisticsService() throws Exception {
        final var inputString = "input string";
        final var res = moduleApi.transformToStatisticsService(inputString);
        assertEquals(inputString, res);
    }

    @Test
    void testValidateXml() throws Exception {
        final var xmlBody = getResourceAsString("v1/ag114-simple-valid.xml");
        when(validator.validateSchematron(any(Source.class))).thenReturn(new SchematronOutputType());

        final var res = moduleApi.validateXml(xmlBody);
        assertNotNull(res);
        assertEquals(ValidationStatus.VALID, res.getStatus());
    }

    // Successful usage of getIntygFromUtlatande is used in a lot of other tests
    // using method createGetCertificateResponseType()
    @Test
    void testGetIntygFromUtlatandeConverterException() {
        final var failingUtlatande = Ag114UtlatandeV1.builder().setId("").setGrundData(new GrundData()).setTextVersion("1.0")
            .setSysselsattning(List.of(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)))
            .build();
        assertThrows(ModuleException.class, () ->
            moduleApi.getIntygFromUtlatande(failingUtlatande)
        );
    }

    @Test
    void testGetModuleSpecificArendeParameters() {
        assertThrows(UnsupportedOperationException.class, () ->
            moduleApi.getModuleSpecificArendeParameters(getUtlatandeFromFile(), Arrays.asList("1", "2"))
        );
    }

    @Test
    void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande() throws ModuleException, IOException {
        final var utlatande = getUtlatandeFromFile();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = moduleApi.getJsonFromUtlatande(utlatande);

        assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> moduleApi.getJsonFromUtlatande(null));
    }

    @Test
    void shallConvertInternalToCertificate() throws Exception {
        final var expectedCertificate = CertificateBuilder.create()
            .metadata(
                CertificateMetadata.builder()
                    .summary(CertificateSummary.builder().build())
                    .build()
            ).build();

        final var convertedCertificate = CertificateBuilder.create()
            .metadata(
                CertificateMetadata.builder().build()
            ).build();

        final var certificateAsJson = "certificateAsJson";
        final var typeAheadProvider = mock(TypeAheadProvider.class);

        final var internalCertificate = Ag114UtlatandeV1.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(getGrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, Ag114UtlatandeV1.class);

        doReturn(convertedCertificate)
            .when(internalToCertificate).convert(eq(internalCertificate), any(CertificateTextProvider.class));

        doReturn(CertificateSummary.builder().build())
            .when(summaryConverter).convert(eq(moduleApi), any(Intyg.class));

        final var actualCertificate = moduleApi.getCertificateFromJson(certificateAsJson, typeAheadProvider);
        assertEquals(expectedCertificate, actualCertificate);
    }

    @Test
    void shallConvertCertificateToInternal() throws Exception {
        final var expectedJson = "expectedJson";
        final var certificate = CertificateBuilder.create().build();
        final var certificateAsJson = "certificateAsJson";

        final var internalCertificate = Ag114UtlatandeV1.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(new GrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, Ag114UtlatandeV1.class);

        doReturn(expectedJson)
            .when(objectMapper).writeValueAsString(internalCertificate);

        doReturn(internalCertificate)
            .when(certificateToInternal).convert(certificate, internalCertificate);

        final var actualJson = moduleApi.getJsonFromCertificate(certificate, certificateAsJson);
        assertEquals(expectedJson, actualJson);
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

    private String toJsonString(Ag114UtlatandeV1 utlatande) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
    }

    private GetCertificateResponseType createGetCertificateResponseType(final StatusKod statusKod)
        throws IOException, ModuleException {
        final var response = new GetCertificateResponseType();
        final var xmlContents = getResourceAsString("v1/ag114-simple-valid.xml");
        final var utlatandeFromXml = moduleApi.getUtlatandeFromXml(xmlContents);
        final var intyg = moduleApi.getIntygFromUtlatande(utlatandeFromXml);
        intyg.getStatus().add(createStatus(statusKod.name()));
        response.setIntyg(intyg);
        return response;
    }

    private IntygsStatus createStatus(String statuskod) {
        final var intygsStatus = new IntygsStatus();
        final var sk = new Statuskod();
        sk.setCode(statuskod);
        intygsStatus.setStatus(sk);
        final var part = new Part();
        part.setCode("FKASSA");
        intygsStatus.setPart(part);
        intygsStatus.setTidpunkt(LocalDateTime.now());
        return intygsStatus;
    }

    private Patient createPatient(String fornamn, String efternamn) {
        final var patient = new Patient();
        if (StringUtils.isNotEmpty(fornamn)) {
            patient.setFornamn(fornamn);
        }
        if (StringUtils.isNotEmpty(efternamn)) {
            patient.setEfternamn(efternamn);
        }
        patient.setPersonId(createPnr());
        return patient;
    }

    private Personnummer createPnr() {
        return Personnummer.createPersonnummer(Ag114ModuleApiTest.TEST_PATIENT_PERSONNR).orElseThrow();
    }

    private HoSPersonal createHosPersonal() {
        final var hosPerson = new HoSPersonal();
        hosPerson.setPersonId(TEST_HSA_ID);
        hosPerson.setFullstandigtNamn("Doktor A");
        hosPerson.setVardenhet(createVardenhet());
        return hosPerson;
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
        final var value = new ResultType();
        final var responseType = new RegisterCertificateResponseType();
        value.setResultCode(res);
        responseType.setResult(value);
        return responseType;
    }

    private Ag114UtlatandeV1 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper()
            .readValue(Resources.getResource("v1/Ag114ModuleApiTest/valid-utkast-sample.json"), Ag114UtlatandeV1.class);
    }

    private String getResourceAsString(String resourceName) throws IOException {
        return (resourceName == null)
            ? null : Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
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
