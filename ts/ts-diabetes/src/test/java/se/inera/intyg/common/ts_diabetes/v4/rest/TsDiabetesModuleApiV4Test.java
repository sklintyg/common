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
package se.inera.intyg.common.ts_diabetes.v4.rest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jakarta.xml.bind.JAXB;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.CertificateToInternal;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.InternalToCertificate;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder;
import se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ts_diabetes.v4.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
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
@ContextConfiguration(classes = {BefattningService.class})
public class TsDiabetesModuleApiV4Test {

    public static final String TESTFILE_UTLATANDE = "v4/internal/scenarios/pass-minimal.json";
    private static final String LOGICAL_ADDRESS = "logical address";

    @Mock
    private CertificateToInternal certificateToInternal;
    @Mock
    private IntygTextsService intygTextsService;
    @Mock
    private InternalToCertificate internalToCertificate;
    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;
    @Mock
    private WebcertModuleService moduleService;
    @Mock
    private WebcertModelFactoryImpl webcertModelFactory;
    @Mock
    private InternalDraftValidatorImpl validator;
    @Spy
    private CustomObjectMapper objectMapper;
    @Mock
    private GetCertificateResponderInterface getCertificateResponder;
    @Mock
    private RevokeCertificateResponderInterface revokeClient;
    @Mock
    private SummaryConverter summaryConverter;
    @Mock
    private UnitMapperUtil unitMapperUtil;

    @InjectMocks
    private TsDiabetesModuleApiV4 moduleApi;

    @BeforeAll
    static void initUtils() {
        final var mapper = mock(UnitMapperUtil.class);

        when(mapper.getMappedUnit(any(), any(), any(), any(), any()))
            .thenAnswer(inv -> new MappedUnit(
                inv.getArgument(0, String.class),
                inv.getArgument(1, String.class),
                inv.getArgument(2, String.class),
                inv.getArgument(3, String.class)
            ));

        new InternalConverterUtil(mapper).initialize();
        new TransportConverterUtil(mapper).initialize();
    }

    @Test
    void shouldDecorateWithMappedCareProvider() throws ScenarioNotFoundException, ModuleException {
        moduleApi.getInternal(
            toJsonString(
                ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()
            )
        );

        verify(unitMapperUtil).decorateWithMappedCareProvider(any(Utlatande.class));
    }

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
        ReflectionTestUtils.setField(moduleApi, "validator", validator);
    }

    @Test
    void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            final var xmlContents = Resources.toString(Resources.getResource("v4/transport/scenarios/pass-complete.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);
            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());
        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test
    void testSendCertificateShouldFailWhenErrorIsReturned() throws IOException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.ERROR));
        final var xmlContents = Resources.toString(Resources.getResource("v4/transport/scenarios/pass-complete.xml"), Charsets.UTF_8);
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null)
        );
    }

    @Test
    void testSendCertificateShouldSucceedWhenInfoIsReturned() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.INFO));
        try {
            final var xmlContents = Resources.toString(Resources.getResource("v4/transport/scenarios/pass-complete.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);
        } catch (IOException e) {
            fail();
        }
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
    void testSendCertificateShouldFailOnMissingIntygTypeVersion() {
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient("blaha", "", null)
        );
    }

    @Test
    void testValidateShouldUseValidator() throws Exception {
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue("internal model", TsDiabetesUtlatandeV4.class);
        moduleApi.validateDraft("internal model");
        verify(validator, times(1)).validateDraft(any());
    }

    @Test
    void testCreateNewInternal() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenReturn(null);
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
        moduleApi.createNewInternalFromTemplate(createCopyHolder(), getUtlatandeFromFile());
        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    void testCreateNewInternalFromTemplateThrowsModuleException() throws ConverterException {
        when(webcertModelFactory.createCopy(any(), any())).thenThrow(new ConverterException());
        assertThrows(ModuleException.class, () ->
            moduleApi.createNewInternalFromTemplate(createCopyHolder(), getUtlatandeFromFile())
        );
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
    void testRegisterCertificate() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

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
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

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
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("INFO"));

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test
    void testRegisterCertificateShouldThrowExceptionOnFailedCallToIT() throws ScenarioNotFoundException, JsonProcessingException {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "resultText"));

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.registerCertificate(internalModel, logicalAddress)
        );
    }

    @Test
    void testRegisterCertificateShouldThrowExceptionOnBadCertificate() throws JsonProcessingException {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";

        doReturn(null)
            .when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);
        assertThrows(ModuleConverterException.class, () ->
            moduleApi.registerCertificate(internalModel, logicalAddress)
        );
    }

    @Test
    void testGetUtlatandeFromJson() throws Exception {
        final var utlatandeJson = "utlatandeJson";
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(utlatandeJson, TsDiabetesUtlatandeV4.class);

        final var utlatandeFromJson = moduleApi.getUtlatandeFromJson(utlatandeJson);
        assertNotNull(utlatandeFromJson);
    }

    @Test
    void parseInvalidXmlFromProdDatabaseLegacyTest() throws Exception {
        // partial unmarshalling
        final var xml = Resources.toString(Resources.getResource("v4/transport/ts-diabetes-invalid-xml-in-prod.xml"), Charsets.UTF_8);
        assertDoesNotThrow(() -> JAXB.unmarshal(new StringReader(xml), RegisterTSDiabetesType.class));
    }

    @Test
    void parseInvalidXmlFromProdDatabaseNewTest() throws IOException {
        final var xml = Resources.toString(Resources.getResource("v4/transport/ts-diabetes-invalid-xml-in-prod.xml"), Charsets.UTF_8);
        assertThrows(UnmarshallingFailureException.class, () ->
            XmlMarshallerHelper.unmarshal(xml)
        );
    }

    @Test
    void testUpdateBeforeSave() throws Exception {
        final var internalModel = "internal model";

        doReturn(internalModel).when(objectMapper).writeValueAsString(any());
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        final var response = moduleApi.updateBeforeSave(internalModel, createHosPersonal(), LocalDateTime.now());
        assertNotNull(response);
        assertEquals(internalModel, response);
        verify(moduleService, times(0)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    void testUpdateBeforeSigning() throws Exception {
        final var internalModel = "internal model";
        doReturn(internalModel).when(objectMapper).writeValueAsString(any());
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        final var response = moduleApi.updateBeforeSigning(internalModel, createHosPersonal(), null);
        assertNotNull(response);
        assertEquals(internalModel, response);
        verify(moduleService, times(0)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    void testRevokeCertificate() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var xmlContents = Resources.toString(Resources.getResource("v4/revokerequest.xml"), Charsets.UTF_8);
        final var returnVal = new RevokeCertificateResponseType();

        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test
    void testRevokeCertificateThrowsExternalServiceCallException() throws IOException {
        final var logicalAddress = "logicalAddress";
        final var xmlContents = Resources.toString(Resources.getResource("v4/revokerequest.xml"), Charsets.UTF_8);
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
        gd.setPatient(createPatient("", "", "191212121212"));
        final var skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);
        final var utlatande = TsDiabetesUtlatandeV4.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();
        final var res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
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
        final var internalCertificate = TsDiabetesUtlatandeV4.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(getGrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, TsDiabetesUtlatandeV4.class);

        doReturn(convertedCertificate)
            .when(internalToCertificate).convert(eq(internalCertificate), any(CertificateTextProvider.class));

        doReturn(CertificateSummary.builder().build())
            .when(summaryConverter).convert(eq(moduleApi), any(Intyg.class));

        final var actualCertificate = moduleApi.getCertificateFromJson(certificateAsJson, typeAheadProvider, LocalDateTime.now());
        assertEquals(expectedCertificate, actualCertificate);
    }


    @Test
    void shallConvertCertificateToInternal() throws Exception {
        final var expectedJson = "expectedJson";
        final var certificate = CertificateBuilder.create().build();
        final var certificateAsJson = "certificateAsJson";

        final var internalCertificate = TsDiabetesUtlatandeV4.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(new GrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, TsDiabetesUtlatandeV4.class);

        doReturn(expectedJson)
            .when(objectMapper).writeValueAsString(internalCertificate);

        doReturn(internalCertificate)
            .when(certificateToInternal).convert(certificate, internalCertificate);

        final var actualJson = moduleApi.getJsonFromCertificate(certificate, certificateAsJson, LocalDateTime.now());
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

    @Test
    void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande()
        throws ModuleException, ScenarioNotFoundException {
        final var utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = moduleApi.getJsonFromUtlatande(utlatande);

        assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> moduleApi.getJsonFromUtlatande(null));
    }

    private String toJsonString(TsDiabetesUtlatandeV4 utlatande) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
    }

    private GetCertificateResponseType createGetCertificateResponseType() throws ScenarioNotFoundException {
        final var res = new GetCertificateResponseType();
        final var registerType = ScenarioFinder.getInternalScenario("pass-minimal").asTransportModel();
        res.setIntyg(registerType.getIntyg());
        return res;
    }

    private CreateDraftCopyHolder createCopyHolder() {
        return new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
    }

    private CreateNewDraftHolder createDraftHolder() {
        return new CreateNewDraftHolder("certificateId", "1.0", createHosPersonal(), createPatient());
    }

    private HoSPersonal createHosPersonal() {
        final var hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId("hsaId");
        hosPersonal.setFullstandigtNamn("namn");
        hosPersonal.setVardenhet(new Vardenhet());
        hosPersonal.getVardenhet().setVardgivare(new Vardgivare());
        return hosPersonal;
    }

    private Patient createPatient() {
        final var pnrTolvan = "19121212-1212";
        return createPatient("fornamn", "efternamn", pnrTolvan);
    }

    private Patient createPatient(String fornamn, String efternamn, String pnr) {
        final var patient = new Patient();
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
        return Personnummer.createPersonnummer(civicRegistrationNumber).orElseThrow();
    }

    private RegisterCertificateResponseType createReturnVal(ResultCodeType res) {
        final var retVal = new RegisterCertificateResponseType();
        final var value = new ResultType();
        value.setResultCode(res);
        retVal.setResult(value);
        return retVal;
    }

    private TsDiabetesUtlatandeV4 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
            TESTFILE_UTLATANDE).getFile(), TsDiabetesUtlatandeV4.class);
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