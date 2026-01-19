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
package se.inera.intyg.common.ts_diabetes.v2.rest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jakarta.xml.bind.JAXB;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.MappedCareProvider;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_diabetes.v2.integration.RegisterTSDiabetesResponderImpl;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.InternalToCertificate;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_diabetes.v2.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.IntygAvserKategori;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.pdf.PdfGeneratorImpl;
import se.inera.intyg.common.ts_diabetes.v2.utils.Scenario;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioFinder;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ts_parent.integration.ResultTypeUtil;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.ObjectFactory;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponderInterface;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = BefattningService.class)
class TsDiabetesModuleApiV2Test {

    private static final String INTYG_TYPE_VERSION_2_8 = "2.8";
    private static final String INTYG_ID = "test-id";
    private static final String LOGICAL_ADDRESS = "logicalAddress";
    private static ClassPathResource revokeCertificateFile;

    @Mock
    private InternalToCertificate internalToCertificate;
    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();
    @Mock
    private RegisterTSDiabetesResponderInterface registerTSDiabetesResponderInterface;
    @Mock
    private GetTSDiabetesResponderInterface getTSDiabetesResponderInterface;
    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();
    @Mock
    private PdfGeneratorImpl pdfGenerator;
    @Mock
    private IntygTextsService intygTexts;
    @Mock
    private XslTransformer xslTransformer;
    @Mock
    private SendTSClient sendTsDiabetesClient;
    @Mock
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;
    @Mock
    private SummaryConverter summaryConverter;
    @Mock
    private UnitMapperUtil unitMapperUtil;
    @InjectMocks
    private TsDiabetesModuleApiV2 moduleApi;

    @BeforeAll
    static void initUtils() {
        final var mapper = mock(UnitMapperUtil.class);

        when(mapper.getMappedCareprovider(any(), any()))
            .thenAnswer(inv -> new MappedCareProvider(
                inv.getArgument(0, String.class),
                inv.getArgument(1, String.class)
            ));

        new InternalConverterUtil(mapper).initialize();
        new TransportConverterUtil(mapper).initialize();
    }

    @BeforeEach
    void init() {
        revokeCertificateFile = new ClassPathResource("revokeCertificate.xml");
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
        ReflectionTestUtils.setField(webcertModelFactory, "intygTexts", intygTexts);
    }

    @Test
    void shouldDecorateWithMappedCareProvider() throws ScenarioNotFoundException, ModuleException {
        moduleApi.getInternal(
            toJsonString(
                ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel()
            )
        );

        verify(unitMapperUtil).decorateWithMappedCareProvider(any(Utlatande.class));
    }

    @Test
    void createNewInternal() throws ModuleException {
        final var holder = createNewDraftHolder();
        final var response = moduleApi.createNewInternal(holder);
        assertNotNull(response);
    }

    @Test
    void testPdf() throws Exception {
        ReflectionTestUtils.setField(moduleApi, "pdfMinaIntygMarginText", "minaIntygMarginText");
        when(pdfGenerator
            .generatePDF(any(TsDiabetesUtlatandeV2.class), any(List.class), any(ApplicationOrigin.class), eq(UtkastStatus.SIGNED),
                eq("minaIntygMarginText")))
            .thenReturn(new byte[]{});
        when(pdfGenerator.generatePdfFilename(any(TsDiabetesUtlatandeV2.class))).thenReturn("filename");
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            assertDoesNotThrow(() ->
                moduleApi.pdf(objectMapper.writeValueAsString(scenario.asInternalModel()), Collections.emptyList(),
                    ApplicationOrigin.MINA_INTYG, UtkastStatus.SIGNED)
            );
        }
    }

    @Test
    void copyContainsOriginalData() throws Exception {
        final var scenario = ScenarioFinder.getTransportScenario("valid-minimal");
        final var holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), scenario.asInternalModel());
        assertNotNull(holder);

        final var utlatande = objectMapper.readValue(holder, TsDiabetesUtlatandeV2.class);
        assertTrue(utlatande.getIntygAvser().getKorkortstyp().contains(IntygAvserKategori.A1));
    }

    @Test
    void getAdditionalInfoFromUtlatandeTest() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var intyg = UtlatandeToIntyg.convert(utlatande);
        final var result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("AM, A1, A2, A, B, BE, Traktor, C1, C1E, C, CE, D1, D1E, D, DE, Taxi", result);
    }

    @Test
    void getAdditionalInfoOneResultTest() throws ModuleException {
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        final var s = new Svar();
        s.setId("1");
        final var delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV17", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        final var result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("Traktor", result);
    }

    @Test
    void getAdditionalInfoMultipleResultsTest() throws ModuleException {
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        final var s = new Svar();
        s.setId("1");
        final var delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV1", null));
        s.getDelsvar().add(delsvar);
        final var s2 = new Svar();
        s2.setId("1");
        final var delsvar2 = new Delsvar();
        delsvar2.setId("1.1");
        delsvar2.getContent().add(aCV(null, "IAV3", null));
        s2.getDelsvar().add(delsvar2);
        final var s3 = new Svar();
        s3.setId("1");
        final var delsvar3 = new Delsvar();
        delsvar3.setId("1.1");
        delsvar3.getContent().add(aCV(null, "IAV9", null));
        s3.getDelsvar().add(delsvar3);
        intyg.getSvar().add(s);
        intyg.getSvar().add(s2);
        intyg.getSvar().add(s3);

        final var result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C, Taxi", result);
    }

    @Test
    void getAdditionalInfoSvarNotFoundTest() throws ModuleException {
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        final var s = new Svar();
        s.setId("2"); // wrong svarId
        final var delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        final var result = moduleApi.getAdditionalInfo(intyg);
        assertNull(result);
    }

    @Test
    void getAdditionalInfoDelsvarNotFoundTest() throws ModuleException {
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        final var s = new Svar();
        s.setId("1");
        final var delsvar = new Delsvar();
        delsvar.setId("1.3"); // wrong delsvarId
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        final var result = moduleApi.getAdditionalInfo(intyg);
        assertNull(result);
    }

    @Test
    void testRegisterCertificateAlreadyExists() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var registerResponse = new RegisterTSDiabetesResponseType();
        final var internalModel = objectMapper.writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal")
            .asInternalModel());

        registerResponse.setResultat(ResultTypeUtil.infoResult(RegisterTSDiabetesResponderImpl.CERTIFICATE_ALREADY_EXISTS));
        when(registerTSDiabetesResponderInterface.registerTSDiabetes(eq(logicalAddress), Mockito.any(RegisterTSDiabetesType.class)))
            .thenReturn(registerResponse);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
            assertEquals(RegisterTSDiabetesResponderImpl.CERTIFICATE_ALREADY_EXISTS, e.getMessage());
        }
    }

    @Test
    void testRegisterCertificateGenericInfoResult() throws Exception {
        final var logicalAddress = "logicalAddress";
        final var registerResponse = new RegisterTSDiabetesResponseType();
        final var internalModel = objectMapper.writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal")
            .asInternalModel());

        registerResponse.setResultat(ResultTypeUtil.infoResult("INFO"));
        when(registerTSDiabetesResponderInterface.registerTSDiabetes(eq(logicalAddress), Mockito.any(RegisterTSDiabetesType.class)))
            .thenReturn(registerResponse);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test
    void testRegisterCertificateErrorResult() throws ScenarioNotFoundException, JsonProcessingException {
        final var logicalAddress = "logicalAddress";
        final var registerResponse = new RegisterTSDiabetesResponseType();
        final var internalModel = objectMapper.writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal")
            .asInternalModel());

        registerResponse.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(registerTSDiabetesResponderInterface.registerTSDiabetes(eq(logicalAddress), Mockito.any(RegisterTSDiabetesType.class)))
            .thenReturn(registerResponse);
        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.registerCertificate(internalModel, logicalAddress)
        );
    }

    @Test
    void testSendCertificateToRecipient() throws Exception {
        final var xmlBody = "xmlBody";
        final var logicalAddress = "logicalAddress";
        final var recipientId = "recipient";
        final var transformedXml = "transformedXml";
        when(xslTransformer.transform(xmlBody)).thenReturn(transformedXml);
        final var response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(false);
        when(sendTsDiabetesClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);

        verify(xslTransformer).transform(xmlBody);
        verify(sendTsDiabetesClient).registerCertificate(transformedXml, logicalAddress);
    }

    @Test
    void testSendCertificateToRecipientFault() throws SOAPException {
        final var xmlBody = "xmlBody";
        final var logicalAddress = "logicalAddress";
        final var recipientId = "recipient";
        final var transformedXml = "transformedXml";
        when(xslTransformer.transform(xmlBody)).thenReturn(transformedXml);
        final var response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(true);
        when(sendTsDiabetesClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId)
        );
    }

    @Test
    void testGetCertificate() throws ModuleException, ScenarioNotFoundException {
        final var result = new GetTSDiabetesResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.okResult());
        when(getTSDiabetesResponderInterface.getTSDiabetes(eq("TS"), Mockito.any(GetTSDiabetesType.class)))
            .thenReturn(result);

        final var internal = moduleApi.getCertificate("cert-id", "TS", "INVANA");
        assertNotNull(internal);
    }

    @Test
    void testGetCertificateRevokedReturnsCertificate() throws Exception {
        final var result = new GetTSDiabetesResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.REVOKED, "error"));
        when(getTSDiabetesResponderInterface.getTSDiabetes(eq("TS"), Mockito.any(GetTSDiabetesType.class)))
            .thenReturn(result);

        final var internal = moduleApi.getCertificate("cert-id", "TS", "INVANA");
        assertNotNull(internal);
    }

    @Test
    void testGetCertificateRevokedValidationError() {
        final var result = new GetTSDiabetesResponseType();
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "error"));
        when(getTSDiabetesResponderInterface.getTSDiabetes(eq("TS"), Mockito.any(GetTSDiabetesType.class)))
            .thenReturn(result);
        assertThrows(ModuleException.class, () ->
            moduleApi.getCertificate("cert-id", "TS", "INVANA")
        );
    }

    @Test
    void testGetCertificateRevokedApplicationError() {
        final var result = new GetTSDiabetesResponseType();
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(getTSDiabetesResponderInterface.getTSDiabetes(eq("TS"), Mockito.any(GetTSDiabetesType.class)))
            .thenReturn(result);
        assertThrows(ModuleException.class, () ->
            moduleApi.getCertificate("cert-id", "TS", "INVANA")
        );
    }

    @Test
    void testGetUtlatandeFromXml() throws Exception {
        final var xml = xmlToString(ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel());
        final var res = moduleApi.getUtlatandeFromXml(xml);
        assertNotNull(res);
    }

    @Test
    void testGetUtlatandeFromXmlConverterException() {
        final var xml = xmlToString(new RegisterTSDiabetesType());
        assertThrows(ModuleException.class, () ->
            moduleApi.getUtlatandeFromXml(xml)
        );
    }

    @Test
    void testRevokeCertificate() throws Exception {
        final var xmlBody = Resources.toString(revokeCertificateFile.getURL(), Charsets.UTF_8);
        final var revokeResponse = new RevokeMedicalCertificateResponseType();
        revokeResponse.setResult(ResultOfCallUtil.okResult());
        when(revokeCertificateClient.revokeMedicalCertificate(any(AttributedURIType.class), any(RevokeMedicalCertificateRequestType.class)))
            .thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
        final var attributedUriCaptor = ArgumentCaptor.forClass(AttributedURIType.class);
        final var parametersCaptor = ArgumentCaptor.forClass(RevokeMedicalCertificateRequestType.class);
        verify(revokeCertificateClient).revokeMedicalCertificate(attributedUriCaptor.capture(), parametersCaptor.capture());
        assertNotNull(parametersCaptor.getValue());
        assertEquals(LOGICAL_ADDRESS, attributedUriCaptor.getValue().getValue());
        assertEquals(INTYG_ID, parametersCaptor.getValue().getRevoke().getLakarutlatande().getLakarutlatandeId());
    }

    @Test
    void testRevokeCertificateResponseError() throws IOException {
        final var xmlBody = Resources.toString(revokeCertificateFile.getURL(), Charsets.UTF_8);
        final var revokeResponse = new RevokeMedicalCertificateResponseType();
        revokeResponse.setResult(ResultOfCallUtil.failResult("error"));
        when(revokeCertificateClient.revokeMedicalCertificate(any(AttributedURIType.class), any(RevokeMedicalCertificateRequestType.class)))
            .thenReturn(revokeResponse);
        assertThrows(ExternalServiceCallException.class, () ->
            moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS)
        );
    }

    @Test
    void testCreateRevokeRequest() throws Exception {
        final var meddelande = "meddelande";
        final var utlatande = ScenarioFinder.getTransportScenario("valid-minimal").asInternalModel();
        utlatande.setId(INTYG_ID);

        final var res = moduleApi.createRevokeRequest(utlatande, utlatande.getGrundData().getSkapadAv(), meddelande);
        final var resultObject = JAXB.unmarshal(new StringReader(res), RevokeMedicalCertificateRequestType.class);
        assertNotNull(resultObject);
        assertEquals(meddelande, resultObject.getRevoke().getMeddelande());
        assertEquals(INTYG_ID, resultObject.getRevoke().getLakarutlatande().getLakarutlatandeId());
    }

    @Test
    void testUpdateBeforeViewing() throws Exception {
        final var updatedPatient = new Patient();
        updatedPatient.setEfternamn("updated lastName");
        updatedPatient.setMellannamn("updated middle-name");
        updatedPatient.setFornamn("updated firstName");
        updatedPatient.setFullstandigtNamn("updated full name");
        updatedPatient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        updatedPatient.setPostadress("updated postal address");
        updatedPatient.setPostnummer("54321");
        updatedPatient.setPostort("updated post city");

        final var validMinimalJson = getResourceAsString(new ClassPathResource("v2/scenarios/internal/valid-minimal.json"));
        when(objectMapper.readValue(validMinimalJson, TsDiabetesUtlatandeV2.class)).thenReturn(
            ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(validMinimalJson);
        final String res = moduleApi.updateBeforeViewing(validMinimalJson, updatedPatient);
        assertNotNull(res);
        JSONAssert.assertEquals(validMinimalJson, res, JSONCompareMode.LENIENT);
    }

    @Test
    void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande() throws ModuleException, ScenarioNotFoundException {
        final TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
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

        var internalCertificate = new TsDiabetesUtlatandeV2();
        internalCertificate.setId("123");
        internalCertificate.setTextVersion("1.0");
        internalCertificate.setGrundData(getGrundData());

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, TsDiabetesUtlatandeV2.class);

        doReturn(convertedCertificate)
            .when(internalToCertificate).convert(eq(internalCertificate), any(CertificateTextProvider.class));

        doReturn(CertificateSummary.builder().build())
            .when(summaryConverter).convert(eq(moduleApi), any(Intyg.class));

        final var actualCertificate = moduleApi.getCertificateFromJson(certificateAsJson, typeAheadProvider);
        assertEquals(expectedCertificate, actualCertificate);
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

    private String toJsonString(TsDiabetesUtlatandeV2 utlatande) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
    }


    private CreateNewDraftHolder createNewDraftHolder() {
        final var hosPersonal = createHosPersonal();
        final var patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        return new CreateNewDraftHolder("Id1", INTYG_TYPE_VERSION_2_8, hosPersonal, patient);
    }

    private CreateDraftCopyHolder createNewDraftCopyHolder() {
        return new CreateDraftCopyHolder("Id1", createHosPersonal());
    }

    private HoSPersonal createHosPersonal() {
        final var hosPerson = new HoSPersonal();
        hosPerson.setPersonId("hsaId1");
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

    private IntygMeta createMeta() {
        final var meta = new IntygMeta();
        meta.setAdditionalInfo("C");
        meta.setAvailable("true");
        return meta;
    }

    private String getResourceAsString(ClassPathResource cpr) throws IOException {
        return Resources.toString(cpr.getURL(), Charsets.UTF_8);
    }

    private String xmlToString(RegisterTSDiabetesType registerTsDiabetes) {
        final var el = new ObjectFactory().createRegisterTSDiabetes(registerTsDiabetes);
        return XmlMarshallerHelper.marshal(el);
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