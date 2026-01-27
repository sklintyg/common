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
package se.inera.intyg.common.lisjp.v1.rest;

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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.PROGNOS_SVAR_JSON_ID_39;
import static se.inera.intyg.common.lisjp.v1.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.lisjp.v1.rest.LisjpModuleApiV1.PREFIX;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.v1.model.converter.SvarIdHelperImpl;
import se.inera.intyg.common.lisjp.v1.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.v1.utils.ScenarioFinder;
import se.inera.intyg.common.lisjp.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.facade.model.CertificateDataElement;
import se.inera.intyg.common.support.facade.model.CertificateLink;
import se.inera.intyg.common.support.facade.model.CertificateText;
import se.inera.intyg.common.support.facade.model.CertificateTextType;
import se.inera.intyg.common.support.facade.model.config.CertificateDataConfigCheckboxDateRangeList;
import se.inera.intyg.common.support.facade.model.config.CheckboxDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.integration.converter.util.ResultTypeUtil;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleConverterException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.validate.InternalDraftValidator;
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
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {BefattningService.class, UnitMappingConfigLoader.class, UnitMapperUtil.class,
    InternalConverterUtil.class})
public class LisjpModuleApiTest {

    public static final String TESTFILE_UTLATANDE = "v1/internal/scenarios/pass-flera-sysselsattningar.json";
    private static final String INTYG_TYPE_VERSION_1 = "1.0";
    private static final String LOGICAL_ADDRESS = "logical address";
    private static final String PNR_TOLVAN = "19121212-1212";

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private WebcertModelFactory<LisjpUtlatandeV1> webcertModelFactory;

    @Mock
    private InternalDraftValidator<LisjpUtlatandeV1> internalDraftValidator;

    @Spy
    private CustomObjectMapper objectMapper;

    @Mock
    private GetCertificateResponderInterface getCertificateResponder;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @Spy
    private SvarIdHelperImpl svarIdHelper;

    @Mock
    private UnitMapperUtil unitMapperUtil;

    @InjectMocks
    private LisjpModuleApiV1 moduleApi;

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

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
        ReflectionTestUtils.setField(moduleApi, "internalDraftValidator", internalDraftValidator);
        ReflectionTestUtils.setField(moduleApi, "svarIdHelper", svarIdHelper);
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

    @Test
    void testPdfEmployerNotAllowed() {
        final var statuses = new ArrayList<Status>();
        final var optionalFields = new ArrayList<String>();
        assertThrows(RuntimeException.class, () ->
            moduleApi.pdfEmployer("internal model", statuses, ApplicationOrigin.WEBCERT, optionalFields, UtkastStatus.SIGNED)
        );
    }

    @Test
    public void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            final var xmlContents = Resources.toString(Resources.getResource("v1/transport/lisjp.xml"), Charsets.UTF_8);
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
        final var xmlContents = Resources.toString(Resources.getResource("v1/transport/lisjp.xml"), Charsets.UTF_8);

        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null)
        );
    }


    @Test
    public void testSendCertificateShouldSucceedWhenInfoIsReturned() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.INFO));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v1/transport/lisjp.xml"), Charsets.UTF_8);
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
    public void testValidateShouldUseValidator() throws Exception {
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue("internal model", LisjpUtlatandeV1.class);
        moduleApi.validateDraft("internal model");
        verify(internalDraftValidator, times(1)).validateDraft(any());
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenReturn(null);
        moduleApi.createNewInternal(createDraftHolder());
        verify(webcertModelFactory, times(1)).createNewWebcertDraft(any());
    }

    @Test
    void testCreateNewInternalThrowsModuleException() {
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null)
        );
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
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
    public void testGetCertificate() throws Exception {
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
    public void testRegisterCertificate() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LisjpUtlatandeV1.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(internalModel, logicalAddress);

        final var captor = ArgumentCaptor.forClass(RegisterCertificateType.class);
        verify(registerCertificateResponderInterface, times(1)).registerCertificate(eq(logicalAddress), captor.capture());
        assertNotNull(captor.getValue().getIntyg());
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LisjpUtlatandeV1.class);

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
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("INFO"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LisjpUtlatandeV1.class);

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
    void testRegisterCertificateShouldThrowExceptionOnFailedCallToIT() throws ScenarioNotFoundException, JsonProcessingException {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        final var response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "resultText"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(internalModel, LisjpUtlatandeV1.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.registerCertificate(internalModel, logicalAddress)
        );
    }

    @Test
    void testRegisterCertificateShouldThrowExceptionOnBadCertificate() throws JsonProcessingException {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";

        doReturn(null).when(objectMapper).readValue(internalModel, LisjpUtlatandeV1.class);

        assertThrows(ModuleConverterException.class, () ->
            moduleApi.registerCertificate(internalModel, logicalAddress)
        );
    }

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        final String utlatandeJson = "utlatandeJson";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(utlatandeJson, LisjpUtlatandeV1.class);
        Utlatande utlatandeFromJson = moduleApi.getUtlatandeFromJson(utlatandeJson);
        assertNotNull(utlatandeFromJson);
    }

    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, LisjpUtlatandeV1.class);

        doReturn(internalModel)
            .when(objectMapper)
            .writeValueAsString(any());

        String response = moduleApi.updateBeforeSave(internalModel, createHosPersonal(), LocalDateTime.now());
        assertEquals(internalModel, response);
        verify(moduleService, times(1)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final var internalModel = "internal model";

        doReturn(internalModel).when(objectMapper).writeValueAsString(any());
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(internalModel, LisjpUtlatandeV1.class);

        final var response = moduleApi.updateBeforeSigning(internalModel, createHosPersonal(), null);
        assertEquals(internalModel, response);
        verify(moduleService, times(1)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    public void testRevokeCertificate() throws Exception {
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
        final var xmlContents = Resources.toString(Resources.getResource("revokerequest.xml"), StandardCharsets.UTF_8);
        final var returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "resultText"));

        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);

        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.revokeCertificate(xmlContents, logicalAddress)
        );
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final var meddelande = "revokeMessage";
        final var intygId = "intygId";
        final var gd = new GrundData();
        gd.setPatient(createPatient("", "", "191212121212"));
        final var skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);
        final var utlatande = LisjpUtlatandeV1.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();
        final var res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    public void testGetModuleSpecificArendeParameters() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        final var res = moduleApi.getModuleSpecificArendeParameters(utlatande,
            Arrays.asList(PROGNOS_SVAR_ID_39, GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, ARBETSTIDSFORLAGGNING_SVAR_ID_33));

        assertNotNull(res);
        assertEquals(3, res.keySet().size());
        assertNotNull(res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1));
        assertEquals(2, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
            res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(1));
        assertNotNull(res.get(PROGNOS_SVAR_ID_39));
        assertEquals(1, res.get(PROGNOS_SVAR_ID_39).size());
        assertEquals(PROGNOS_SVAR_JSON_ID_39, res.get(PROGNOS_SVAR_ID_39).get(0));
        assertNotNull(res.get(ARBETSTIDSFORLAGGNING_SVAR_ID_33));
        assertEquals(1, res.get(ARBETSTIDSFORLAGGNING_SVAR_ID_33).size());
        assertEquals(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33, res.get(ARBETSTIDSFORLAGGNING_SVAR_ID_33).get(0));
    }

    @Test
    public void getAdditionalInfoFromUtlatandeTest() throws Exception {
        final var utlatande = getUtlatandeFromFile();
        final var intyg = UtlatandeToIntyg.convert(utlatande, moduleService);
        final var result = moduleApi.getAdditionalInfo(intyg);

        assertEquals("2015-12-07 - 2015-12-10", result);
    }

    @Test
    public void getAdditionalInfoOneTimePeriodTest() throws Exception {
        final var fromString = "2015-12-12";
        final var toString = "2016-03-02";
        final var utlatandeBuilder = getUtlatandeFromFile().toBuilder().setSjukskrivningar(List.of(
            Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(fromString, toString))));
        final var intyg = UtlatandeToIntyg.convert(utlatandeBuilder.build(), moduleService);
        final var result = moduleApi.getAdditionalInfo(intyg);

        assertEquals(fromString + " - " + toString, result);
    }

    @Test
    public void testCreateCompletionFromTemplateWithComment() throws Exception {
        final var ovrigt = "övrigtText";
        final var kommentar = "kommentarText";
        final var utlatande = LisjpUtlatandeV1.builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        final var result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        final var utlatandeFromJson = (LisjpUtlatandeV1) moduleApi.getUtlatandeFromJson(result);

        assertEquals(ovrigt + "\n\n" + PREFIX + kommentar, utlatandeFromJson.getOvrigt());

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    public void testCreateCompletionFromTemplateWithNoComment() throws Exception {
        final var ovrigt = "övrigtText";
        final var kommentar = "";
        final var utlatande = LisjpUtlatandeV1.builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        final var result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        final var utlatandeFromJson = (LisjpUtlatandeV1) moduleApi.getUtlatandeFromJson(result);
        assertEquals(ovrigt, utlatandeFromJson.getOvrigt());
        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    public void testCreateCompletionFromTemplateWithNoOvrigt() throws Exception {
        final String ovrigt = "";
        final String kommentar = "kommentarText";
        final var utlatande = LisjpUtlatandeV1.builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        when(webcertModelFactory.createCopy(any(), any())).thenReturn(utlatande);

        final var result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        final var utlatandeFromJson = (LisjpUtlatandeV1) moduleApi.getUtlatandeFromJson(result);
        assertEquals(PREFIX + kommentar, utlatandeFromJson.getOvrigt());
        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    public void getAdditionalInfoMultiplePeriodsTest() throws Exception {
        final var fromString = "2015-12-12";
        final var middleDate1 = "2015-12-13";
        final var middleDate2 = "2015-12-14";
        final var middleDate3 = "2015-12-15";
        final var middleDate4 = "2015-12-16";
        final var toString = "2016-03-02";

        final var utlatandeBuilder = getUtlatandeFromFile().toBuilder().setSjukskrivningar(Arrays.asList(
            Sjukskrivning.create(Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT,
                new InternalLocalDateInterval(middleDate2, middleDate3)),
            Sjukskrivning.create(Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(middleDate4, toString)),
            Sjukskrivning.create(Sjukskrivning.SjukskrivningsGrad.HELT_NEDSATT,
                new InternalLocalDateInterval(fromString, middleDate1))));
        final var intyg = UtlatandeToIntyg.convert(utlatandeBuilder.build(), moduleService);

        final var result = moduleApi.getAdditionalInfo(intyg);

        assertEquals(fromString + " - " + toString, result);
    }

    @Test
    public void getCertficateMessagesProviderGetExistingKey() {
        final var certificateMessagesProvider = moduleApi.getMessagesProvider();
        assertEquals("Fortsätt", certificateMessagesProvider.get("common.continue"));
    }

    @Test
    public void getCertficateMessagesProviderGetMissingKey() {
        final var certificateMessagesProvider = moduleApi.getMessagesProvider();
        assertNull(certificateMessagesProvider.get("not.existing"));
    }

    @Test
    public void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande() throws ModuleException, IOException {
        final var utlatande = getUtlatandeFromFile();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = moduleApi.getJsonFromUtlatande(utlatande);
        assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    public void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> moduleApi.getJsonFromUtlatande(null));
    }

    @Test
    public void shouldReturnAdditionalInfoLabel() {
        final var response = moduleApi.getAdditionalInfoLabel();
        assertEquals("Gäller intygsperiod", response);
    }

    @Test
    public void shouldReturnPreambleForCitizens() {
        final var expectedResult = CertificateText.builder()
            .type(CertificateTextType.PREAMBLE_TEXT)
            .text("Det här är ditt intyg. Intyget innehåller all information som vården fyllt i. "
                + "Du kan inte ändra något i ditt intyg. Har du frågor kontaktar du den som skrivit ditt intyg. "
                + "Om du vill ansöka om sjukpenning, gör du det på {linkFK}.")
            .links(List.of(
                CertificateLink.builder()
                    .url("http://www.forsakringskassan.se/sjuk")
                    .name("Försäkringskassan")
                    .id("linkFK")
                    .build()
            ))
            .build();

        assertEquals(expectedResult, moduleApi.getPreambleForCitizens());
    }

    @Nested
    class DecorateTests {

        @Test
        void shouldNotDecorateCertificateIfQuestionNotPresentInData() {
            final var certificate = mock(Certificate.class);
            moduleApi.decorate(certificate, Collections.emptyMap());
            verifyNoInteractions(certificate);
        }

        @Test
        void shouldDecorateCertificateWithPreviousLastDateRange() {
            final var certificate = new Certificate();
            final var id = "id";
            final var date = LocalDate.now();
            final var label = "label";

            final var expectedPreviousText = "På det ursprungliga intyget var slutdatumet för den sista perioden %s och omfattningen var %s.".formatted(
                date, label);

            final var certificateDataElement = CertificateDataElement.builder()
                .id(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
                .config(
                    CertificateDataConfigCheckboxDateRangeList.builder()
                        .list(
                            List.of(
                                CheckboxDateRange.builder()
                                    .id(id)
                                    .label(label)
                                    .build()
                            )
                        )
                        .previousDateRangeText(null)
                        .build()
                )
                .build();

            final var certificateDataElementMap = new HashMap<String, CertificateDataElement>();
            certificateDataElementMap.put(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, certificateDataElement);
            certificate.setData(certificateDataElementMap);

            final var certificateDataElementWithPreviousDateRange = CertificateDataElement.builder()
                .value(
                    CertificateDataValueDateRangeList.builder()
                        .list(
                            List.of(
                                CertificateDataValueDateRange.builder()
                                    .id(id)
                                    .to(date)
                                    .build()
                            )
                        )
                        .build()
                )
                .build();

            final var data = new HashMap<String, CertificateDataElement>();
            data.put(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, certificateDataElementWithPreviousDateRange);

            moduleApi.decorate(certificate, data);
            final var config = (CertificateDataConfigCheckboxDateRangeList) certificate.getData()
                .get(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32)
                .getConfig();

            assertEquals(expectedPreviousText, config.getPreviousDateRangeText());
        }
    }

    private String toJsonString(LisjpUtlatandeV1 utlatande) throws ModuleException {
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
        return new CreateNewDraftHolder("certificateId", INTYG_TYPE_VERSION_1, createHosPersonal(), createPatient());
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
        return createPatient("fornamn", "efternamn", PNR_TOLVAN);
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

    private LisjpUtlatandeV1 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
            TESTFILE_UTLATANDE).getFile(), LisjpUtlatandeV1.class);
    }
}