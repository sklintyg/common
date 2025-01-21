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
package se.inera.intyg.common.ts_bas.v6.rest;

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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.ts_parent.rest.TsParentModuleApi.REGISTER_CERTIFICATE_VERSION1;

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
import java.io.StringWriter;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.modules.transformer.XslTransformerUtil;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_bas.v6.model.converter.InternalToCertificate;
import se.inera.intyg.common.ts_bas.v6.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.v6.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_bas.v6.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioFinder;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc. are correct.
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {BefattningService.class})
class TsBasModuleApiTest {

    private static final String INTYG_TYPE_VERSION_6_8 = "6.8";

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Mock
    private IntygTextsService intygTexts;

    @Mock
    private SendTSClient sendTsBasClient;
    @Mock
    private InternalToCertificate internalToCertificate;

    @Mock
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;

    @Mock
    private SummaryConverter summaryConverter;

    @Spy
    @InjectMocks
    private TsBasModuleApiV6 moduleApi = new TsBasModuleApiV6();

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
        ReflectionTestUtils.setField(moduleApi, "registerCertificateVersion", REGISTER_CERTIFICATE_VERSION1);
        ReflectionTestUtils.setField(webcertModelFactory, "intygTexts", intygTexts);
    }

    @Test
    void copyContainsOriginalData() throws Exception {
        final var  scenario = ScenarioFinder.getInternalScenario("valid-maximal");
        final var holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), scenario.asInternalModel());
        assertNotNull(holder);

        final var utlatande = objectMapper.readValue(holder, TsBasUtlatandeV6.class);
        assertEquals(true, Objects.requireNonNull(utlatande.getSyn()).getSynfaltsdefekter());
    }

    @Test
    void createNewInternal() throws ModuleException {
        final var holder = createNewDraftHolder();
        final var response = moduleApi.createNewInternal(holder);
        assertNotNull(response);
    }

    @Test
    void testXmlPayloadIsRegisterTsBas() throws Exception {
        // We don't test the actual transformation, only the logic within
        // the transformaPayload method
        final var xmlBody = getResourceAsString(new ClassPathResource("v6/scenarios/transport/valid-minimal.xml"));
        boolean result = XslTransformerUtil.isRegisterTsBas(xmlBody);
        assertTrue(result);
    }

    @Test
    void testTransformPayload_TransportToV1() throws Exception {
        // We don't test the actual transformation, only the logic within
        // the transformaPayload method
        final var xmlBody = getResourceAsString(new ClassPathResource("v6/scenarios/transport/valid-minimal.xml"));
        assertDoesNotThrow(() -> moduleApi.transformPayload(xmlBody));
    }

    @Test
    void testTransformPayload_TransportToV3() throws Exception {
        setRegisterCertificateVersion();

        // We don't test the actual transformation, only the logic within
        // the transformaPayload method
        final var xmlBody = getResourceAsString(new ClassPathResource("v6/scenarios/transport/valid-minimal.xml"));
        assertDoesNotThrow(() -> moduleApi.transformPayload(xmlBody));

    }

    @Test
    void testTransformPayload_V3ToV1() throws Exception {
        // We don't test the actual transformation, only the logic within
        // the transformaPayload method
        final var xmlBody = getResourceAsString(new ClassPathResource("v6/scenarios/rivtav3/valid-minimal.xml"));
        assertDoesNotThrow(() -> moduleApi.transformPayload(xmlBody));

    }

    @Test
    void testSendCertificateToRecipient() throws Exception {
        final var xmlBody = "xmlBody";
        final var logicalAddress = "logicalAddress";
        final var recipientId = "recipient";
        final var transformedXml = "transformedXml";

        doReturn(transformedXml).when(moduleApi).transformPayload(xmlBody);

        final var response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(false);

        when(sendTsBasClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);

        verify(moduleApi).transformPayload(xmlBody);
        verify(sendTsBasClient).registerCertificate(transformedXml, logicalAddress);
    }

    @Test
    void testSendCertificateToRecipientFault() throws ModuleException, SOAPException {
        final var xmlBody = "xmlBody";
        final var logicalAddress = "logicalAddress";
        final var recipientId = "recipient";
        final var transformedXml = "transformedXml";

        when(moduleApi.transformPayload(xmlBody)).thenReturn(transformedXml);

        final var response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(true);
        when(sendTsBasClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId)
        );
    }

    @Test
    void testGetUtlatandeWhenXmlIsInTransportFormat() throws Exception {
        final var originalXml = xmlToString(ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel());
        final var res = moduleApi.getUtlatandeFromXml(originalXml);
        assertNotNull(res);
    }

    @Test
    void getAdditionalInfoFromUtlatandeTest() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var intyg = UtlatandeToIntyg.convert(utlatande);
        final var result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C1E, C, CE, D1, D1E, D, DE, Taxi, Annat", result);
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
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        final var result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("D1E", result);
    }

    @Test
    void getAdditionalInfoMultipleResultsTest() throws ModuleException {
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        final var s = new Svar();
        s.setId("1");
        final var  delsvar = new Delsvar();
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

        final var validMinimalJson = getResourceAsString(new ClassPathResource("v6/scenarios/internal/valid-minimal.json"));
        when(objectMapper.readValue(validMinimalJson, TsBasUtlatandeV6.class)).thenReturn(
            ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(validMinimalJson);
        final var res = moduleApi.updateBeforeViewing(validMinimalJson, updatedPatient);

        assertNotNull(res);
        JSONAssert.assertEquals(validMinimalJson, res, JSONCompareMode.LENIENT);
    }

    @Test
    void testRevokeCertificateResponseOK() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var skapatAv = createHosPersonal();
        final var meddelande = "Revoke message";
        final var xmlBody = moduleApi.createRevokeRequest(utlatande, skapatAv, meddelande);

        final var result = mock(ResultOfCall.class);
        doReturn(ResultCodeEnum.OK).when(result).getResultCode();

        final var response = mock(RevokeMedicalCertificateResponseType.class);
        doReturn(result).when(response).getResult();

        doReturn(response).when(revokeCertificateClient).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));

        final var logicalAddress = "Logical address";
        moduleApi.revokeCertificate(xmlBody, logicalAddress);

        verify(revokeCertificateClient, times(1)).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));
    }

    @Test
    void testRevokeCertificateResponseINFO() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var skapatAv = createHosPersonal();
        final var meddelande = "Revoke message";
        final var xmlBody = moduleApi.createRevokeRequest(utlatande, skapatAv, meddelande);

        final var result = mock(ResultOfCall.class);
        doReturn(ResultCodeEnum.INFO).when(result).getResultCode();

        final var response = mock(RevokeMedicalCertificateResponseType.class);
        doReturn(result).when(response).getResult();

        doReturn(response).when(revokeCertificateClient).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));

        final var logicalAddress = "Logical address";
        moduleApi.revokeCertificate(xmlBody, logicalAddress);

        verify(revokeCertificateClient, times(1)).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));
    }

    @Test
    void testRevokeCertificateResponseERROR() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var skapatAv = createHosPersonal();
        final var meddelande = "Revoke message";
        final var xmlBody = moduleApi.createRevokeRequest(utlatande, skapatAv, meddelande);
        final var errorMessage = "felmeddelande";
        final var result = mock(ResultOfCall.class);
        doReturn(ResultCodeEnum.ERROR).when(result).getResultCode();
        doReturn(errorMessage).when(result).getErrorText();

        final var response = mock(RevokeMedicalCertificateResponseType.class);
        doReturn(result).when(response).getResult();

        doReturn(response).when(revokeCertificateClient).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));

        final var logicalAddress = "Logical address";
        final var expectedMessage = "Revoke sent to " + logicalAddress + " failed with error: " + errorMessage;
        try {
            moduleApi.revokeCertificate(xmlBody, logicalAddress);
            fail();
        } catch (ExternalServiceCallException ex) {
            assertEquals(expectedMessage, ex.getMessage());
        }

        verify(revokeCertificateClient, times(1)).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));
    }

    @Test
    void testReadAndWriteOfOldJsonFormatOfBedomningKanInteTaStallning() throws IOException, ModuleException {
        final var originalJson = getResourceAsString(
            new ClassPathResource("v6/scenarios/internal/ts-bas-bedomning-kan-inte-ta-stallning-old-format.json"));
        final var originalUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(originalJson);
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(originalUtlatande.getBedomning()).getKorkortstyp()).size());
        assertTrue(originalUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.KAN_INTE_TA_STALLNING));

        final var updatedJson = moduleApi.updateBeforeSave(originalJson, createHosPersonal());
        final var updatedUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(updatedJson);
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(updatedUtlatande.getBedomning()).getKorkortstyp()).size());
        assertTrue(updatedUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.KAN_INTE_TA_STALLNING));
    }

    @Test
    void testReadAndWriteOfOldJsonFormatOfBedomningC() throws IOException, ModuleException {
        final var originalJson = getResourceAsString(new ClassPathResource("v6/scenarios/internal/ts-bas-bedomning-c-old-format.json"));
        final var originalUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(originalJson);
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(originalUtlatande.getBedomning()).getKorkortstyp()).size());
        assertTrue(originalUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.C));

        final var updatedJson = moduleApi.updateBeforeSave(originalJson, createHosPersonal());
        final var updatedUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(updatedJson);
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(updatedUtlatande.getBedomning()).getKorkortstyp()).size());
        assertTrue(updatedUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.C));
    }

    @Test
    void testReadAndWriteOfChangedJsonFormatOfBedomningKanInteTaStallning() throws IOException, ModuleException {
        final var originalJson = getResourceAsString(
            new ClassPathResource("v6/scenarios/internal/ts-bas-bedomning-kan-inte-ta-stallning-changed-format.json"));
        final var originalUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(originalJson);
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(originalUtlatande.getBedomning()).getKorkortstyp()).size());
        assertTrue(originalUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.KAN_INTE_TA_STALLNING));

        final var updatedJson = moduleApi.updateBeforeSave(originalJson, createHosPersonal());
        final var updatedUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(updatedJson);
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(updatedUtlatande.getBedomning()).getKorkortstyp()).size());
        assertTrue(updatedUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.KAN_INTE_TA_STALLNING));
    }

    @Test
    void testReadAndWriteOfChangedJsonFormatOfBedomningC() throws IOException, ModuleException {
        final var originalJson = getResourceAsString(new ClassPathResource("v6/scenarios/internal/ts-bas-bedomning-c-changed-format.json"));
        final var originalUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(originalJson);
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(originalUtlatande.getBedomning()).getKorkortstyp()).size());
        assertTrue(originalUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.C));

        final var updatedJson = moduleApi.updateBeforeSave(originalJson, createHosPersonal());
        final var updatedUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(updatedJson);
        assertEquals(1, Objects.requireNonNull(Objects.requireNonNull(updatedUtlatande.getBedomning()).getKorkortstyp()).size());
        assertTrue(updatedUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.C));
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

        final var internalCertificate = TsBasUtlatandeV6.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(getGrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, TsBasUtlatandeV6.class);

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

    @Test
    void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande() throws ModuleException, ScenarioNotFoundException {
        final TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = moduleApi.getJsonFromUtlatande(utlatande);

        assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> moduleApi.getJsonFromUtlatande(null));
    }

    private String toJsonString(TsBasUtlatandeV6 utlatande) throws ModuleException {
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
        return new CreateNewDraftHolder("Id1", INTYG_TYPE_VERSION_6_8, hosPersonal, patient);
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

    private String getResourceAsString(ClassPathResource cpr) throws IOException {
        return Resources.toString(cpr.getURL(), Charsets.UTF_8);
    }

    private void setRegisterCertificateVersion() {
        ReflectionTestUtils.setField(moduleApi, "registerCertificateVersion",
            se.inera.intyg.common.ts_parent.rest.TsParentModuleApi.REGISTER_CERTIFICATE_VERSION3);
    }

    private String xmlToString(RegisterTSBasType registerCertificateType) {
        final var stringWriter = new StringWriter();
        JAXB.marshal(registerCertificateType, stringWriter);
        return stringWriter.toString();
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
