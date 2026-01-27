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
package se.inera.intyg.common.luae_fs.v1.rest;

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
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22;
import static se.inera.intyg.common.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22;
import static se.inera.intyg.common.fkparent.rest.FkParentModuleApi.PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.common.luae_fs.support.LuaefsEntryPoint;
import se.inera.intyg.common.luae_fs.v1.model.converter.CertificateToInternal;
import se.inera.intyg.common.luae_fs.v1.model.converter.InternalToCertificate;
import se.inera.intyg.common.luae_fs.v1.model.converter.SvarIdHelperImpl;
import se.inera.intyg.common.luae_fs.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
import se.inera.intyg.common.luae_fs.v1.utils.ScenarioFinder;
import se.inera.intyg.common.luae_fs.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.services.texts.model.IntygTexts;
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
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedUnit;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.services.BefattningService;
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

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {BefattningService.class})
class LuaefsModuleApiTest {

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
    private IntygTextsService intygTextsServiceMock;
    @Spy
    private WebcertModelFactoryImpl webcertModelFactory;
    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();
    @Mock
    private WebcertModuleService moduleService;
    @Mock
    private RevokeCertificateResponderInterface revokeClient;
    @Mock
    private SummaryConverter summaryConverter;
    @Spy
    private SvarIdHelperImpl svarIdHelper;
    @Mock
    private UnitMapperUtil unitMapperUtil;

    @InjectMocks
    private LuaefsModuleApiV1 moduleApi;

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
        ReflectionTestUtils.setField(moduleApi, "svarIdHelper", svarIdHelper);
        ReflectionTestUtils.setField(webcertModelFactory, "intygTexts", intygTextsServiceMock);
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
    void testSendCertificateToRecipientShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v1/luae_fs-simple-valid.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);

            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());

        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test
    void testSendCertificateToRecipientFailsWithoutXmlModel() {
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null)
        );
    }

    @Test
    void testSendCertificateToRecipientFailsForNonOkResponse() throws IOException {
        final var xmlContents = Resources.toString(Resources.getResource("v1/luae_fs-simple-valid.xml"), Charsets.UTF_8);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.ERROR));
        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null)
        );
    }

    @Test
    void testGetCertificate() throws Exception {
        final var result = createGetCertificateResponseType(StatusKod.SENTTO);
        when(getCertificateResponderInterface.getCertificate(anyString(), any())).thenReturn(result);

        final CertificateResponse response = moduleApi.getCertificate("id", LOGICAL_ADDRESS, "INVANA");
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
    void testGetCertificateWhenSOAPExceptionThowsModuleException() throws SOAPException {
        final var ex = new SOAPFaultException(SOAPFactory.newInstance().createFault());
        doThrow(ex).when(getCertificateResponderInterface).getCertificate(anyString(), any());
        assertThrows(ModuleException.class, () ->
            moduleApi.getCertificate("id", LOGICAL_ADDRESS, "INVANA")
        );
    }

    @Test
    void testRegisterCertificate() throws IOException, ModuleException {
        final var json = Resources.toString(new ClassPathResource("v1/LuaefsModuleApiTest/valid-utkast-sample.json")
            .getURL(), Charsets.UTF_8);

        final var utlatande = (LuaefsUtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        when(objectMapper.readValue(json, LuaefsUtlatandeV1.class)).thenReturn(utlatande);

        final var result = createReturnVal(ResultCodeType.OK);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        assertDoesNotThrow(() -> moduleApi.registerCertificate(json, LOGICAL_ADDRESS));
    }

    @Test
    void testRegisterCertificateAlreadyExists() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var internalModel = "internal model";
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel()).when(objectMapper)
            .readValue(anyString(), eq(LuaefsUtlatandeV1.class));

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
            .readValue(anyString(), eq(LuaefsUtlatandeV1.class));

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
    void testCreateRenewalFromTemplate() throws Exception {
        final var draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());
        final var renewalFromTemplate = moduleApi.createRenewalFromTemplate(draftCertificateHolder, getUtlatandeFromFile());
        final var copy = (LuaefsUtlatandeV1) moduleApi.getUtlatandeFromJson(renewalFromTemplate);

        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());
        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(LuaefsUtlatandeV1.class));
    }

    @Test
    void testCreateNewInternalFromTemplate() throws Exception {
        final var draftCertificateHolder = new CreateDraftCopyHolder("1", createHosPersonal());
        final var renewalFromTemplate = moduleApi.createNewInternalFromTemplate(draftCertificateHolder, getUtlatandeFromFile());
        final var copy = (LuaefsUtlatandeV1) moduleApi.getUtlatandeFromJson(renewalFromTemplate);

        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());
        verify(webcertModelFactory).createCopy(eq(draftCertificateHolder), any(LuaefsUtlatandeV1.class));
    }

    @Test
    void testCreateNewInternal() throws Exception {
        when(intygTextsServiceMock.getLatestVersionForSameMajorVersion(LuaefsEntryPoint.MODULE_ID, INTYG_TYPE_VERSION_1))
            .thenReturn(INTYG_TYPE_VERSION_1);

        final var createNewDraftHolder = new CreateNewDraftHolder("1", INTYG_TYPE_VERSION_1, createHosPersonal(),
            createPatient());
        final var renewalFromTemplate = moduleApi.createNewInternal(createNewDraftHolder);
        final var copy = (LuaefsUtlatandeV1) moduleApi.getUtlatandeFromJson(renewalFromTemplate);
        assertEquals(TEST_HSA_ID, copy.getGrundData().getSkapadAv().getPersonId());
        assertEquals(TEST_PATIENT_PERSONNR, copy.getGrundData().getPatient().getPersonId().getPersonnummer());

    }

    @Test
    void testRegisterCertificateThrowsExternalServiceCallExceptionOnErrorResultCode() throws IOException {
        final var result = createReturnVal(ResultCodeType.ERROR);
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(result);

        final var json = Resources.toString(new ClassPathResource("v1/LuaefsModuleApiTest/valid-utkast-sample.json")
            .getURL(), Charsets.UTF_8);
        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.registerCertificate(json, LOGICAL_ADDRESS)
        );
    }

    /**
     * Verify that grundData is updated
     */
    @Test
    void testUpdateBeforeSave() throws IOException, ModuleException {
        final var json = Resources.toString(new ClassPathResource("v1/LuaefsModuleApiTest/valid-utkast-sample.json")
            .getURL(), Charsets.UTF_8);

        final var utlatandeBeforeSave = (LuaefsUtlatandeV1) moduleApi.getUtlatandeFromJson(json);
        assertNotEquals(TEST_HSA_ID, utlatandeBeforeSave.getGrundData().getSkapadAv().getPersonId());
        when(objectMapper.readValue(json, LuaefsUtlatandeV1.class)).thenReturn(utlatandeBeforeSave);

        final var internalModelResponse = moduleApi.updateBeforeSave(json, createHosPersonal());
        final var utlatandeFromJson = moduleApi.getUtlatandeFromJson(internalModelResponse);
        assertEquals(TEST_HSA_ID, utlatandeFromJson.getGrundData().getSkapadAv().getPersonId());

        verify(moduleService, times(3)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    void testRevokeCertificate() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var xmlContents = Resources.toString(Resources.getResource("v1/revokerequest.xml"), Charsets.UTF_8);
        final var returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test
    void testRevokeCertificateThrowsExternalServiceCallException() throws IOException {
        final var logicalAddress = "logicalAddress";
        final var xmlContents = Resources.toString(Resources.getResource("v1/revokerequest.xml"), Charsets.UTF_8);
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
        gd.setPatient(createPatient());
        final var skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);
        final var utlatande = LuaefsUtlatandeV1.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();
        final var res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    void testGetModuleSpecificArendeParameters() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        final var res = moduleApi.getModuleSpecificArendeParameters(utlatande,
            Arrays.asList(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22, FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11,
                GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, AKTIVITETSBEGRANSNING_SVAR_ID_17));

        assertNotNull(res);
        assertEquals(4, res.keySet().size());
        assertNotNull(res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1));
        assertEquals(2, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).size());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_1, res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(0));
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1,
            res.get(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1).get(1));
        assertNotNull(res.get(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22));
        assertEquals(1, res.get(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22).size());
        assertEquals(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID_22, res.get(MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22).get(0));
        assertNotNull(res.get(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11));
        assertEquals(1, res.get(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11).size());
        assertEquals(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID_11, res.get(FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11).get(0));
        assertNotNull(res.get(AKTIVITETSBEGRANSNING_SVAR_ID_17));
        assertEquals(1, res.get(AKTIVITETSBEGRANSNING_SVAR_ID_17).size());
        assertEquals(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17, res.get(AKTIVITETSBEGRANSNING_SVAR_ID_17).get(0));
    }

    @Test
    void testGetAdditionalInfo() throws Exception {
        final var intyg = ScenarioFinder.getInternalScenario("pass-minimal").asTransportModel().getIntyg();
        final var additionalInfo = moduleApi.getAdditionalInfo(intyg);

        assertNotNull(additionalInfo);
        assertEquals("Klämskada skuldra", additionalInfo);
    }

    @Test
    void testGetAdditionalInfoHuvuddiganos() throws Exception {
        final var intyg = ScenarioFinder.getInternalScenario("pass-diagnos-med-bidiagnoser").asTransportModel().getIntyg();
        final var additionalInfo = moduleApi.getAdditionalInfo(intyg);

        assertNotNull(additionalInfo);
        assertEquals("Klämskada skuldra", additionalInfo);
    }

    @Test
    void testCreateCompletionFromTemplateWithComment() throws Exception {
        final var ovrigt = "övrigtText";
        final var kommentar = "kommentarText";
        final var utlatande = LuaefsUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        doReturn(utlatande)
            .when(webcertModelFactory)
            .createCopy(any(), any());

        final var result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        final var utlatandeFromJson = (LuaefsUtlatandeV1) moduleApi.getUtlatandeFromJson(result);

        assertEquals(ovrigt + "\n\n" + PREFIX + kommentar, utlatandeFromJson.getOvrigt());
        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    void testCreateCompletionFromTemplateWithNoComment() throws Exception {
        final var ovrigt = "övrigtText";
        final var kommentar = "";
        final var utlatande = LuaefsUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        doReturn(utlatande).when(webcertModelFactory).createCopy(any(), any());

        final var result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        final var utlatandeFromJson = (LuaefsUtlatandeV1) moduleApi.getUtlatandeFromJson(result);
        assertEquals(ovrigt, utlatandeFromJson.getOvrigt());
        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    void testCreateCompletionFromTemplateWithNoOvrigt() throws Exception {
        final var ovrigt = "";
        final var kommentar = "kommentarText";
        final var utlatande = LuaefsUtlatandeV1
            .builder()
            .setId("utlatande-id")
            .setGrundData(new GrundData())
            .setTextVersion("textVersion")
            .setOvrigt(ovrigt)
            .build();

        doReturn(utlatande).when(webcertModelFactory).createCopy(any(), any());

        final var result = moduleApi.createCompletionFromTemplate(createCopyHolder(), utlatande, kommentar);
        final var utlatandeFromJson = (LuaefsUtlatandeV1) moduleApi.getUtlatandeFromJson(result);
        assertEquals(PREFIX + kommentar, utlatandeFromJson.getOvrigt());
        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test
    void shallConvertInternalToCertificate() throws Exception {
        final var expectedCertificate = CertificateBuilder.create()
            .metadata(
                CertificateMetadata.builder()
                    .summary(CertificateSummary.builder().build())
                    .build()
            ).build();
        final var convertedCertificate = CertificateBuilder.create().metadata(CertificateMetadata.builder().build()).build();
        final var certificateAsJson = "certificateAsJson";
        final var typeAheadProvider = mock(TypeAheadProvider.class);

        final var internalCertificate = LuaefsUtlatandeV1.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(getGrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, LuaefsUtlatandeV1.class);

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

        final var internalCertificate = LuaefsUtlatandeV1.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(new GrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, LuaefsUtlatandeV1.class);

        doReturn(expectedJson)
            .when(objectMapper).writeValueAsString(internalCertificate);

        doReturn(internalCertificate)
            .when(certificateToInternal).convert(certificate, internalCertificate);

        final var actualJson = moduleApi.getJsonFromCertificate(certificate, certificateAsJson, LocalDateTime.now());
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void getCertficateMessagesProviderGetExistingKey() {
        final var intygTexts1 = new IntygTexts("1.0", LuaefsEntryPoint.MODULE_ID, LocalDate.now(), LocalDate.now().plusDays(1),
            Collections.emptySortedMap(),
            Collections.emptyList(), new Properties());
        doReturn(intygTexts1).when(intygTexts).getIntygTextsPojo(any(), any());

        final var certificateMessagesProvider = moduleApi.getMessagesProvider();
        assertEquals("Fortsätt", certificateMessagesProvider.get("common.continue"));
    }

    @Test
    void getCertficateMessagesProviderGetMissingKey() {
        final var intygTexts1 = new IntygTexts("1.0", LuaefsEntryPoint.MODULE_ID, LocalDate.now(), LocalDate.now().plusDays(1),
            Collections.emptySortedMap(),
            Collections.emptyList(), new Properties());
        doReturn(intygTexts1).when(intygTexts).getIntygTextsPojo(any(), any());

        final var certificateMessagesProvider = moduleApi.getMessagesProvider();
        assertNull(certificateMessagesProvider.get("not.existing"));
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

    private String toJsonString(LuaefsUtlatandeV1 utlatande) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
    }

    private GetCertificateResponseType createGetCertificateResponseType(final StatusKod statusKod)
        throws IOException, ModuleException {
        final var response = new GetCertificateResponseType();
        final var xmlContents = Resources.toString(Resources.getResource("v1/luae_fs-simple-valid.xml"), Charsets.UTF_8);
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

    private Patient createPatient() {
        final var patient = new Patient();
        if (StringUtils.isNotEmpty("fornamn")) {
            patient.setFornamn("fornamn");
        }
        if (StringUtils.isNotEmpty("efternamn")) {
            patient.setEfternamn("efternamn");
        }
        patient.setPersonId(createPnr());
        return patient;
    }

    private Personnummer createPnr() {
        return Personnummer.createPersonnummer(LuaefsModuleApiTest.TEST_PATIENT_PERSONNR).orElseThrow();
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

    private LuaefsUtlatandeV1 getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper()
            .readValue(new ClassPathResource("v1/LuaefsModuleApiTest/valid-utkast-sample.json").getFile(), LuaefsUtlatandeV1.class);
    }

    private CreateDraftCopyHolder createCopyHolder() {
        return new CreateDraftCopyHolder("certificateId",
            createHosPersonal());
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