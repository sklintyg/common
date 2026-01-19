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
package se.inera.intyg.common.ts_bas.v7.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.ts_parent.rest.TsParentModuleApi.REGISTER_CERTIFICATE_VERSION1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
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
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.metadata.CertificateMetadata;
import se.inera.intyg.common.support.facade.model.metadata.CertificateSummary;
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
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_bas.v7.model.converter.CertificateToInternal;
import se.inera.intyg.common.ts_bas.v7.model.converter.InternalToCertificate;
import se.inera.intyg.common.ts_bas.v7.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.v7.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_bas.v7.model.internal.TsBasUtlatandeV7;
import se.inera.intyg.common.ts_bas.v7.utils.ScenarioFinder;
import se.inera.intyg.common.ts_bas.v7.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc. are correct.
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {BefattningService.class})
class TsBasModuleApiTest {

    private static final String INTYG_TYPE_VERSION_7_0 = "7.0";

    @Spy
    private final ObjectMapper objectMapper = new CustomObjectMapper();
    @Spy
    private final WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();
    @Mock
    private IntygTextsService intygTexts;
    @Mock
    private SendTSClient sendTsBasClient;
    @Mock
    private RevokeCertificateResponderInterface revokeCertificateClient;
    @Mock
    private InternalToCertificate internalToCertificate;
    @Mock
    private CertificateToInternal certificateToInternal;
    @Mock
    private SummaryConverter summaryConverter;
    @Mock
    private UnitMapperUtil unitMapperUtil;

    @InjectMocks
    private final TsBasModuleApiV7 moduleApi = new TsBasModuleApiV7();

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
        ReflectionTestUtils.setField(moduleApi, "webcertModelFactory", webcertModelFactory);
        ReflectionTestUtils.setField(moduleApi, "registerCertificateVersion", REGISTER_CERTIFICATE_VERSION1);
        ReflectionTestUtils.setField(webcertModelFactory, "intygTexts", intygTexts);
    }

    @Test
    void shouldDecorateWithMappedCareProvider() throws ScenarioNotFoundException, ModuleException, JsonProcessingException {
        final var json = "{}";
        when(objectMapper.readValue(json, TsBasUtlatandeV7.class))
            .thenReturn(ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel());

        moduleApi.getInternal(json);

        verify(unitMapperUtil).decorateWithMappedCareProvider(any(Utlatande.class));
    }

    @Test
    void copyContainsOriginalData() throws Exception {
        final var scenario = ScenarioFinder.getInternalScenario("valid-maximal");
        final var holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), scenario.asInternalModel());

        assertNotNull(holder);
        final var utlatande = objectMapper.readValue(holder, TsBasUtlatandeV7.class);
        assertEquals(true, Objects.requireNonNull(utlatande.getSyn()).getSynfaltsdefekter());
    }

    @Test
    void createNewInternal() throws ModuleException {
        final var holder = createNewDraftHolder();
        final var response = moduleApi.createNewInternal(holder);
        assertNotNull(response);
    }

    @Test
    void testSendCertificateToRecipient() throws Exception {
        final var xmlBody = "xmlBody";
        final var logicalAddress = "logicalAddress";
        final var recipientId = "recipient";
        final var response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(false);
        when(sendTsBasClient.registerCertificate(xmlBody, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);

        verify(sendTsBasClient).registerCertificate(xmlBody, logicalAddress);
    }

    @Test
    void testSendCertificateToRecipientFault() throws SOAPException {
        final var xmlBody = "xmlBody";
        final var logicalAddress = "logicalAddress";
        final var recipientId = "recipient";
        final var response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(true);
        when(sendTsBasClient.registerCertificate(xmlBody, logicalAddress)).thenReturn(response);
        assertThrows(ModuleException.class, () ->
            moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId)
        );
    }

    @Test
    void testGetUtlatandeWhenXmlIsInTransportFormat() throws Exception {
        final var xml = getResourceAsString(new ClassPathResource("v7/scenarios/rivtav3/valid-minimal.xml"));
        final var res = moduleApi.getUtlatandeFromXml(xml);
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

        final var validMinimalJson = getResourceAsString(new ClassPathResource("v7/scenarios/internal/valid-minimal.json"));
        when(objectMapper.readValue(validMinimalJson, TsBasUtlatandeV7.class)).thenReturn(
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
        final var result = mock(ResultType.class);
        doReturn(ResultCodeType.OK).when(result).getResultCode();

        final var response = mock(RevokeCertificateResponseType.class);
        doReturn(result).when(response).getResult();
        doReturn(response).when(revokeCertificateClient).revokeCertificate(anyString(),
            any(RevokeCertificateType.class));

        final var logicalAddress = "Logical address";
        moduleApi.revokeCertificate(xmlBody, logicalAddress);

        verify(revokeCertificateClient, times(1)).revokeCertificate(anyString(),
            any(RevokeCertificateType.class));
    }

    @Test
    void testRevokeCertificateResponseINFO() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var skapatAv = createHosPersonal();
        final var meddelande = "Revoke message";
        final var xmlBody = moduleApi.createRevokeRequest(utlatande, skapatAv, meddelande);
        final var result = mock(ResultType.class);
        doReturn(ResultCodeType.INFO).when(result).getResultCode();

        final var response = mock(RevokeCertificateResponseType.class);
        doReturn(result).when(response).getResult();
        doReturn(response).when(revokeCertificateClient).revokeCertificate(anyString(),
            any(RevokeCertificateType.class));

        final var logicalAddress = "Logical address";
        final var expectedMessage = "Could not send revoke to " + logicalAddress;
        try {
            moduleApi.revokeCertificate(xmlBody, logicalAddress);
            fail();
        } catch (ExternalServiceCallException ex) {
            assertEquals(expectedMessage, ex.getMessage());
        }

        verify(revokeCertificateClient, times(1)).revokeCertificate(anyString(),
            any(RevokeCertificateType.class));
    }

    @Test
    void testRevokeCertificateResponseERROR() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var skapatAv = createHosPersonal();
        final var meddelande = "Revoke message";
        final var xmlBody = moduleApi.createRevokeRequest(utlatande, skapatAv, meddelande);
        final var result = mock(ResultType.class);
        doReturn(ResultCodeType.ERROR).when(result).getResultCode();

        final var response = mock(RevokeCertificateResponseType.class);
        doReturn(result).when(response).getResult();
        doReturn(response).when(revokeCertificateClient).revokeCertificate(anyString(),
            any(RevokeCertificateType.class));

        final var logicalAddress = "Logical address";
        final var expectedMessage = "Could not send revoke to " + logicalAddress;
        try {
            moduleApi.revokeCertificate(xmlBody, logicalAddress);
            fail();
        } catch (ExternalServiceCallException ex) {
            assertEquals(expectedMessage, ex.getMessage());
        }

        verify(revokeCertificateClient, times(1)).revokeCertificate(anyString(),
            any(RevokeCertificateType.class));
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

        final var internalCertificate = TsBasUtlatandeV7.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(getGrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, TsBasUtlatandeV7.class);

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

        final var internalCertificate = TsBasUtlatandeV7.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(new GrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(certificateAsJson, TsBasUtlatandeV7.class);

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

    @Test
    void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande()
        throws ModuleException, ScenarioNotFoundException {
        final var utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = moduleApi.getJsonFromUtlatande(utlatande);

        assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> moduleApi.getJsonFromUtlatande(null));
    }

    private String toJsonString(TsBasUtlatandeV7 utlatande) throws ModuleException {
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
        return new CreateNewDraftHolder("Id1", INTYG_TYPE_VERSION_7_0, hosPersonal, patient);
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