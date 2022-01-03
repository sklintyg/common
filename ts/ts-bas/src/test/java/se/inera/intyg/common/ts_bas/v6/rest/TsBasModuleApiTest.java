/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;
import static se.inera.intyg.common.ts_parent.rest.TsParentModuleApi.REGISTER_CERTIFICATE_VERSION1;
import static se.inera.intyg.common.ts_parent.rest.TsParentModuleApi.REGISTER_CERTIFICATE_VERSION3;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.xml.bind.JAXB;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3.wsaddressing10.AttributedURIType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.transformer.XslTransformerUtil;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_bas.v6.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.ts_bas.v6.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_bas.v6.model.internal.BedomningKorkortstyp;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;
import se.inera.intyg.common.ts_bas.v6.utils.Scenario;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioFinder;
import se.inera.intyg.common.ts_parent.integration.SendTSClient;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TsBasModuleApiTest {

    private static final String INTYG_TYPE_VERSION_6_8 = "6.8";
    @InjectMocks
    @Spy
    private TsBasModuleApiV6 moduleApi = new TsBasModuleApiV6();

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Mock
    private IntygTextsService intygTexts;

    @Mock
    private SendTSClient sendTsBasClient;

    @Mock
    private RevokeMedicalCertificateResponderInterface revokeCertificateClient;

    public TsBasModuleApiTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setup() throws Exception {
        // Init the default beahviour
        setRegisterCertificateVersion(REGISTER_CERTIFICATE_VERSION1);

        // use reflection to set IntygTextsService mock in webcertModelFactory
        Field field = WebcertModelFactoryImpl.class.getDeclaredField("intygTexts");
        field.setAccessible(true);
        field.set(webcertModelFactory, intygTexts);
    }

    @Test
    public void copyContainsOriginalData() throws Exception {
        Scenario scenario = ScenarioFinder.getInternalScenario("valid-maximal");
        String holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), scenario.asInternalModel());

        assertNotNull(holder);
        TsBasUtlatandeV6 utlatande = objectMapper.readValue(holder, TsBasUtlatandeV6.class);
        assertEquals(true, utlatande.getSyn().getSynfaltsdefekter());
    }

    @Test
    public void createNewInternal() throws ModuleException {
        CreateNewDraftHolder holder = createNewDraftHolder();
        String response = moduleApi.createNewInternal(holder);
        assertNotNull(response);
    }

    @Test
    public void testXmlPayloadIsRegisterTsBas() throws Exception {
        // We don't test the actual transformation, only the logic within
        // the transformaPayload method
        final String xmlBody = getResourceAsString(new ClassPathResource("v6/scenarios/transport/valid-minimal.xml"));
        boolean result = XslTransformerUtil.isRegisterTsBas(xmlBody);
        assertTrue(result);
    }

    @Test
    public void testTransformPayload_TransportToV1() throws Exception {
        // We don't test the actual transformation, only the logic within
        // the transformaPayload method
        final String xmlBody = getResourceAsString(new ClassPathResource("v6/scenarios/transport/valid-minimal.xml"));

        moduleApi.transformPayload(xmlBody);
    }

    @Test
    public void testTransformPayload_TransportToV3() throws Exception {
        setRegisterCertificateVersion(REGISTER_CERTIFICATE_VERSION3);

        // We don't test the actual transformation, only the logic within
        // the transformaPayload method
        final String xmlBody = getResourceAsString(new ClassPathResource("v6/scenarios/transport/valid-minimal.xml"));

        moduleApi.transformPayload(xmlBody);
    }

    @Test
    public void testTransformPayload_V3ToV1() throws Exception {
        // We don't test the actual transformation, only the logic within
        // the transformaPayload method
        final String xmlBody = getResourceAsString(new ClassPathResource("v6/scenarios/rivtav3/valid-minimal.xml"));

        moduleApi.transformPayload(xmlBody);
    }

    @Test
    public void testSendCertificateToRecipient() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";

        doReturn(transformedXml).when(moduleApi).transformPayload(xmlBody);

        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(false);

        when(sendTsBasClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);

        verify(moduleApi).transformPayload(xmlBody);
        verify(sendTsBasClient).registerCertificate(transformedXml, logicalAddress);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientFault() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";

        when(moduleApi.transformPayload(xmlBody)).thenReturn(transformedXml);

        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(true);
        when(sendTsBasClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);
    }

    @Test
    public void testGetUtlatandeWhenXmlIsInTransportFormat() throws Exception {
        final String originalXml = xmlToString(ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel());

        TsBasUtlatandeV6 res = moduleApi.getUtlatandeFromXml(originalXml);
        assertNotNull(res);
    }

    @Test
    @Ignore
    public void testGetUtlatandeWhenXmlIsInV3Format() throws Exception {
        String xml = xmlToString(ScenarioFinder.getTransportScenario("valid-minimal").asRivtaV3TransportModel());
        TsBasUtlatandeV6 res = moduleApi.getUtlatandeFromXml(xml);
        assertNotNull(res);
    }

    @Test
    public void getAdditionalInfoFromUtlatandeTest() throws Exception {
        TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C1E, C, CE, D1, D1E, D, DE, Taxi, Annat", result);
    }

    @Test
    public void getAdditionalInfoOneResultTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("D1E", result);
    }

    @Test
    public void getAdditionalInfoMultipleResultsTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV1", null));
        s.getDelsvar().add(delsvar);
        Svar s2 = new Svar();
        s2.setId("1");
        Delsvar delsvar2 = new Delsvar();
        delsvar2.setId("1.1");
        delsvar2.getContent().add(aCV(null, "IAV3", null));
        s2.getDelsvar().add(delsvar2);
        Svar s3 = new Svar();
        s3.setId("1");
        Delsvar delsvar3 = new Delsvar();
        delsvar3.setId("1.1");
        delsvar3.getContent().add(aCV(null, "IAV9", null));
        s3.getDelsvar().add(delsvar3);
        intyg.getSvar().add(s);
        intyg.getSvar().add(s2);
        intyg.getSvar().add(s3);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C, Taxi", result);
    }

    @Test
    public void getAdditionalInfoSvarNotFoundTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("2"); // wrong svarId
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.1");
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertNull(result);
    }

    @Test
    public void getAdditionalInfoDelsvarNotFoundTest() throws ModuleException {
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygId");
        Svar s = new Svar();
        s.setId("1");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("1.3"); // wrong delsvarId
        delsvar.getContent().add(aCV(null, "IAV6", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertNull(result);
    }

    @Test
    public void testUpdateBeforeViewing() throws Exception {
        Patient updatedPatient = new Patient();
        updatedPatient.setEfternamn("updated lastName");
        updatedPatient.setMellannamn("updated middle-name");
        updatedPatient.setFornamn("updated firstName");
        updatedPatient.setFullstandigtNamn("updated full name");
        updatedPatient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        updatedPatient.setPostadress("updated postal address");
        updatedPatient.setPostnummer("54321");
        updatedPatient.setPostort("updated post city");

        final String validMinimalJson = getResourceAsString(new ClassPathResource("v6/scenarios/internal/valid-minimal.json"));
        when(objectMapper.readValue(validMinimalJson, TsBasUtlatandeV6.class)).thenReturn(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());
        when(objectMapper.writeValueAsString(any())).thenReturn(validMinimalJson);
        final String res = moduleApi.updateBeforeViewing(validMinimalJson, updatedPatient);

        assertNotNull(res);
        JSONAssert.assertEquals(validMinimalJson, res, JSONCompareMode.LENIENT);
    }

    @Test
    public void testRevokeCertificateResponseOK() throws Exception {
        final TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final HoSPersonal skapatAv = createHosPersonal();
        final String meddelande = "Revoke message";

        final String xmlBody = moduleApi.createRevokeRequest(utlatande, skapatAv, meddelande);

        ResultOfCall result = mock(ResultOfCall.class);
        doReturn(ResultCodeEnum.OK).when(result).getResultCode();

        RevokeMedicalCertificateResponseType response = mock(RevokeMedicalCertificateResponseType.class);
        doReturn(result).when(response).getResult();

        doReturn(response).when(revokeCertificateClient).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));

        final String logicalAddress = "Logical address";
        moduleApi.revokeCertificate(xmlBody, logicalAddress);

        verify(revokeCertificateClient, times(1)).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));
    }

    @Test
    public void testRevokeCertificateResponseINFO() throws Exception {
        final TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final HoSPersonal skapatAv = createHosPersonal();
        final String meddelande = "Revoke message";

        final String xmlBody = moduleApi.createRevokeRequest(utlatande, skapatAv, meddelande);

        ResultOfCall result = mock(ResultOfCall.class);
        doReturn(ResultCodeEnum.INFO).when(result).getResultCode();

        RevokeMedicalCertificateResponseType response = mock(RevokeMedicalCertificateResponseType.class);
        doReturn(result).when(response).getResult();

        doReturn(response).when(revokeCertificateClient).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));

        final String logicalAddress = "Logical address";
        moduleApi.revokeCertificate(xmlBody, logicalAddress);

        verify(revokeCertificateClient, times(1)).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));
    }

    @Test
    public void testRevokeCertificateResponseERROR() throws Exception {
        final TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        final HoSPersonal skapatAv = createHosPersonal();
        final String meddelande = "Revoke message";

        final String xmlBody = moduleApi.createRevokeRequest(utlatande, skapatAv, meddelande);

        final String errorMessage = "felmeddelande";
        ResultOfCall result = mock(ResultOfCall.class);
        doReturn(ResultCodeEnum.ERROR).when(result).getResultCode();
        doReturn(errorMessage).when(result).getErrorText();

        RevokeMedicalCertificateResponseType response = mock(RevokeMedicalCertificateResponseType.class);
        doReturn(result).when(response).getResult();

        doReturn(response).when(revokeCertificateClient).revokeMedicalCertificate(any(AttributedURIType.class),
            any(RevokeMedicalCertificateRequestType.class));

        final String logicalAddress = "Logical address";

        final String expectedMessage = "Revoke sent to " + logicalAddress + " failed with error: " + errorMessage;
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
    public void testReadAndWriteOfOldJsonFormatOfBedomningKanInteTaStallning() throws IOException, ModuleException {
        String originalJson = getResourceAsString(
            new ClassPathResource("v6/scenarios/internal/ts-bas-bedomning-kan-inte-ta-stallning-old-format.json"));
        TsBasUtlatandeV6 originalUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(originalJson);
        assertEquals(originalUtlatande.getBedomning().getKorkortstyp().size(), 1);
        assertTrue(originalUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.KAN_INTE_TA_STALLNING));

        String updatedJson = moduleApi.updateBeforeSave(originalJson, createHosPersonal());
        TsBasUtlatandeV6 updatedUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(updatedJson);
        assertEquals(updatedUtlatande.getBedomning().getKorkortstyp().size(), 1);
        assertTrue(updatedUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.KAN_INTE_TA_STALLNING));
    }

    @Test
    public void testReadAndWriteOfOldJsonFormatOfBedomningC() throws IOException, ModuleException {
        String originalJson = getResourceAsString(new ClassPathResource("v6/scenarios/internal/ts-bas-bedomning-c-old-format.json"));
        TsBasUtlatandeV6 originalUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(originalJson);
        assertEquals(originalUtlatande.getBedomning().getKorkortstyp().size(), 1);
        assertTrue(originalUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.C));

        String updatedJson = moduleApi.updateBeforeSave(originalJson, createHosPersonal());
        TsBasUtlatandeV6 updatedUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(updatedJson);
        assertEquals(updatedUtlatande.getBedomning().getKorkortstyp().size(), 1);
        assertTrue(updatedUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.C));
    }

    @Test
    public void testReadAndWriteOfChangedJsonFormatOfBedomningKanInteTaStallning() throws IOException, ModuleException {
        String originalJson = getResourceAsString(
            new ClassPathResource("v6/scenarios/internal/ts-bas-bedomning-kan-inte-ta-stallning-changed-format.json"));
        TsBasUtlatandeV6 originalUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(originalJson);
        assertEquals(originalUtlatande.getBedomning().getKorkortstyp().size(), 1);
        assertTrue(originalUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.KAN_INTE_TA_STALLNING));

        String updatedJson = moduleApi.updateBeforeSave(originalJson, createHosPersonal());
        TsBasUtlatandeV6 updatedUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(updatedJson);
        assertEquals(updatedUtlatande.getBedomning().getKorkortstyp().size(), 1);
        assertTrue(updatedUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.KAN_INTE_TA_STALLNING));
    }

    @Test
    public void testReadAndWriteOfChangedJsonFormatOfBedomningC() throws IOException, ModuleException {
        String originalJson = getResourceAsString(new ClassPathResource("v6/scenarios/internal/ts-bas-bedomning-c-changed-format.json"));
        TsBasUtlatandeV6 originalUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(originalJson);
        assertEquals(originalUtlatande.getBedomning().getKorkortstyp().size(), 1);
        assertTrue(originalUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.C));

        String updatedJson = moduleApi.updateBeforeSave(originalJson, createHosPersonal());
        TsBasUtlatandeV6 updatedUtlatande = (TsBasUtlatandeV6) moduleApi.getUtlatandeFromJson(updatedJson);
        assertEquals(updatedUtlatande.getBedomning().getKorkortstyp().size(), 1);
        assertTrue(updatedUtlatande.getBedomning().getKorkortstyp().contains(BedomningKorkortstyp.C));
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        HoSPersonal hosPersonal = createHosPersonal();
        Patient patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        return new CreateNewDraftHolder("Id1", INTYG_TYPE_VERSION_6_8, hosPersonal, patient);
    }

    private CreateDraftCopyHolder createNewDraftCopyHolder() {
        return new CreateDraftCopyHolder("Id1", createHosPersonal());
    }

    private HoSPersonal createHosPersonal() {
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("hsaId1");
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

    private String getResourceAsString(ClassPathResource cpr) throws IOException {
        return Resources.toString(cpr.getURL(), Charsets.UTF_8);
    }

    private void setRegisterCertificateVersion(String version) {
        ReflectionTestUtils.setField(moduleApi, "registerCertificateVersion", version);
    }

    private String xmlToString(RegisterTSBasType registerCertificateType) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(registerCertificateType, stringWriter);
        return stringWriter.toString();
    }

    private String xmlToString(RegisterCertificateType registerCertificateType) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(registerCertificateType, stringWriter);
        return stringWriter.toString();
    }

}
