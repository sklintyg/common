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
package se.inera.intyg.common.ts_diabetes.v2.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aCV;


import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3.wsaddressing10.AttributedURIType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificate.rivtabp20.v1.RevokeMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.revokemedicalcertificateresponder.v1.RevokeMedicalCertificateResponseType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.transformer.XslTransformer;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_diabetes.v2.integration.RegisterTSDiabetesResponderImpl;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BefattningService.class)
public class TsDiabetesModuleApiV2Test {

    private static final String INTYG_TYPE_VERSION_2_8 = "2.8";
    private final String INTYG_ID = "test-id";
    private final String LOGICAL_ADDRESS = "logicalAddress";

    private static ClassPathResource revokeCertificateFile;

    @InjectMocks
    private TsDiabetesModuleApiV2 moduleApi;

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


    public TsDiabetesModuleApiV2Test() {
        MockitoAnnotations.initMocks(this);
    }
    @Before
    public void setup() throws Exception {
        revokeCertificateFile = new ClassPathResource("revokeCertificate.xml");
        // use reflection to set IntygTextsService mock in webcertModelFactory
        Field field = WebcertModelFactoryImpl.class.getDeclaredField("intygTexts");
        field.setAccessible(true);
        field.set(webcertModelFactory, intygTexts);
    }

    @Test
    public void createNewInternal() throws ModuleException {
        CreateNewDraftHolder holder = createNewDraftHolder();

        String response = moduleApi.createNewInternal(holder);

        assertNotNull(response);
    }

    @Test
    public void testPdf() throws Exception {
        when(pdfGenerator.generatePDF(any(TsDiabetesUtlatandeV2.class), any(List.class), any(ApplicationOrigin.class), eq(UtkastStatus.SIGNED)))
                .thenReturn(new byte[] {});
        when(pdfGenerator.generatePdfFilename(any(TsDiabetesUtlatandeV2.class))).thenReturn("filename");
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            moduleApi
                    .pdf(objectMapper.writeValueAsString(scenario.asInternalModel()), Collections.emptyList(), ApplicationOrigin.MINA_INTYG,
                            UtkastStatus.SIGNED);
        }
    }

    @Test
    public void copyContainsOriginalData() throws Exception {
        Scenario scenario = ScenarioFinder.getTransportScenario("valid-minimal");

        String holder = moduleApi.createNewInternalFromTemplate(createNewDraftCopyHolder(), scenario.asInternalModel());

        assertNotNull(holder);
        TsDiabetesUtlatandeV2 utlatande = objectMapper.readValue(holder, TsDiabetesUtlatandeV2.class);
        assertEquals(true, utlatande.getIntygAvser().getKorkortstyp().contains(IntygAvserKategori.A1));
    }

    @Test
    public void getAdditionalInfoFromUtlatandeTest() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("AM, A1, A2, A, B, BE, Traktor, C1, C1E, C, CE, D1, D1E, D, DE, Taxi", result);
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
        delsvar.getContent().add(aCV(null, "IAV17", null));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("Traktor", result);
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
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = objectMapper
                .writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());

        RegisterTSDiabetesResponseType registerResponse = new RegisterTSDiabetesResponseType();
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
    public void testRegisterCertificateGenericInfoResult() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = objectMapper
                .writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());

        RegisterTSDiabetesResponseType registerResponse = new RegisterTSDiabetesResponseType();
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

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateErrorResult() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = objectMapper
                .writeValueAsString(ScenarioFinder.getInternalScenario("valid-minimal").asInternalModel());

        RegisterTSDiabetesResponseType registerResponse = new RegisterTSDiabetesResponseType();
        registerResponse.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        when(registerTSDiabetesResponderInterface.registerTSDiabetes(eq(logicalAddress), Mockito.any(RegisterTSDiabetesType.class)))
                .thenReturn(registerResponse);

        moduleApi.registerCertificate(internalModel, logicalAddress);
    }

    @Test
    public void testSendCertificateToRecipient() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";
        when(xslTransformer.transform(xmlBody)).thenReturn(transformedXml);
        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(false);
        when(sendTsDiabetesClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);

        verify(xslTransformer).transform(xmlBody);
        verify(sendTsDiabetesClient).registerCertificate(transformedXml, logicalAddress);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientFault() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";
        when(xslTransformer.transform(xmlBody)).thenReturn(transformedXml);
        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(true);
        when(sendTsDiabetesClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);
    }

    @Test
    public void testGetCertificate() throws ModuleException, ScenarioNotFoundException {
        GetTSDiabetesResponseType result = new GetTSDiabetesResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.okResult());
        Mockito.when(getTSDiabetesResponderInterface.getTSDiabetes(eq("TS"), Mockito.any(GetTSDiabetesType.class)))
                .thenReturn(result);

        CertificateResponse internal = moduleApi.getCertificate("cert-id", "TS", "INVANA");
        assertNotNull(internal);
    }

    @Test
    public void testGetCertificateRevokedReturnsCertificate() throws Exception {
        GetTSDiabetesResponseType result = new GetTSDiabetesResponseType();
        result.setIntyg(ScenarioFinder.getTransportScenario("valid-maximal").asTransportModel().getIntyg());
        result.setMeta(createMeta());
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.REVOKED, "error"));
        Mockito.when(getTSDiabetesResponderInterface.getTSDiabetes(eq("TS"), Mockito.any(GetTSDiabetesType.class)))
                .thenReturn(result);

        CertificateResponse internal = moduleApi.getCertificate("cert-id", "TS", "INVANA");
        assertNotNull(internal);
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateRevokedValidationError() throws Exception {
        GetTSDiabetesResponseType result = new GetTSDiabetesResponseType();
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "error"));
        Mockito.when(getTSDiabetesResponderInterface.getTSDiabetes(eq("TS"), Mockito.any(GetTSDiabetesType.class)))
                .thenReturn(result);

        moduleApi.getCertificate("cert-id", "TS", "INVANA");
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateRevokedApplicationError() throws Exception {
        GetTSDiabetesResponseType result = new GetTSDiabetesResponseType();
        result.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "error"));
        Mockito.when(getTSDiabetesResponderInterface.getTSDiabetes(eq("TS"), Mockito.any(GetTSDiabetesType.class)))
                .thenReturn(result);

        moduleApi.getCertificate("cert-id", "TS", "INVANA");
    }

    @Test
    public void testGetUtlatandeFromXml() throws Exception {
        String xml = xmlToString(ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel());
        TsDiabetesUtlatandeV2 res = moduleApi.getUtlatandeFromXml(xml);

        assertNotNull(res);
    }

    @Test(expected = ModuleException.class)
    public void testGetUtlatandeFromXmlConverterException() throws Exception {
        String xml = xmlToString(new RegisterTSDiabetesType());
        moduleApi.getUtlatandeFromXml(xml);
    }

    @Test
    public void testRevokeCertificate() throws Exception {
        String xmlBody = Resources.toString(revokeCertificateFile.getURL(), Charsets.UTF_8);
        RevokeMedicalCertificateResponseType revokeResponse = new RevokeMedicalCertificateResponseType();
        revokeResponse.setResult(ResultOfCallUtil.okResult());
        when(revokeCertificateClient.revokeMedicalCertificate(any(AttributedURIType.class), any(RevokeMedicalCertificateRequestType.class)))
                .thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
        ArgumentCaptor<AttributedURIType> attributedUriCaptor = ArgumentCaptor.forClass(AttributedURIType.class);
        ArgumentCaptor<RevokeMedicalCertificateRequestType> parametersCaptor = ArgumentCaptor
                .forClass(RevokeMedicalCertificateRequestType.class);
        verify(revokeCertificateClient).revokeMedicalCertificate(attributedUriCaptor.capture(), parametersCaptor.capture());
        assertNotNull(parametersCaptor.getValue());
        assertEquals(LOGICAL_ADDRESS, attributedUriCaptor.getValue().getValue());
        assertEquals(INTYG_ID, parametersCaptor.getValue().getRevoke().getLakarutlatande().getLakarutlatandeId());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateResponseError() throws Exception {
        String xmlBody = Resources.toString(revokeCertificateFile.getURL(), Charsets.UTF_8);
        RevokeMedicalCertificateResponseType revokeResponse = new RevokeMedicalCertificateResponseType();
        revokeResponse.setResult(ResultOfCallUtil.failResult("error"));
        when(revokeCertificateClient.revokeMedicalCertificate(any(AttributedURIType.class), any(RevokeMedicalCertificateRequestType.class)))
                .thenReturn(revokeResponse);

        moduleApi.revokeCertificate(xmlBody, LOGICAL_ADDRESS);
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "meddelande";

        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getTransportScenario("valid-minimal").asInternalModel();
        utlatande.setId(INTYG_ID);

        String res = moduleApi.createRevokeRequest(utlatande, utlatande.getGrundData().getSkapadAv(), meddelande);
        RevokeMedicalCertificateRequestType resultObject = JAXB.unmarshal(new StringReader(res), RevokeMedicalCertificateRequestType.class);
        assertNotNull(resultObject);
        assertEquals(meddelande, resultObject.getRevoke().getMeddelande());
        assertEquals(INTYG_ID, resultObject.getRevoke().getLakarutlatande().getLakarutlatandeId());
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

        final String validMinimalJson = getResourceAsString(new ClassPathResource("v2/scenarios/internal/valid-minimal.json"));
        final String res = moduleApi.updateBeforeViewing(validMinimalJson, updatedPatient);
        assertNotNull(res);
        JSONAssert.assertEquals(validMinimalJson,res, JSONCompareMode.LENIENT);
    }

    private CreateNewDraftHolder createNewDraftHolder() {
        HoSPersonal hosPersonal = createHosPersonal();
        Patient patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        return new CreateNewDraftHolder("Id1", INTYG_TYPE_VERSION_2_8, hosPersonal, patient);
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

    private IntygMeta createMeta() throws ScenarioNotFoundException {
        IntygMeta meta = new IntygMeta();
        meta.setAdditionalInfo("C");
        meta.setAvailable("true");
        return meta;
    }

    private String getResourceAsString(ClassPathResource cpr) throws IOException {
        return Resources.toString(cpr.getURL(), Charsets.UTF_8);
    }

    private String xmlToString(RegisterTSDiabetesType registerTsDiabetes) throws JAXBException {
        JAXBElement<RegisterTSDiabetesType> el = new ObjectFactory().createRegisterTSDiabetes(registerTsDiabetes);
        return XmlMarshallerHelper.marshal(el);
    }
}
