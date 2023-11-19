/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import jakarta.xml.bind.JAXB;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
import se.inera.intyg.common.support.modules.converter.SummaryConverter;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TsDiabetesModuleApiV4Test {

    public static final String TESTFILE_UTLATANDE = "v4/internal/scenarios/pass-minimal.json";

    private final String LOGICAL_ADDRESS = "logical address";
    @Mock
    private CertificateToInternal certificateToInternal;
    @Mock
    private InternalToCertificate internalToCertificate;

    @Mock
    private IntygTextsService intygTexts;
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

    @InjectMocks
    private TsDiabetesModuleApiV4 moduleApi;

    public TsDiabetesModuleApiV4Test() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendCertificateShouldUseXml() {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any())).thenReturn(createReturnVal(ResultCodeType.OK));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v4/transport/scenarios/pass-complete.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);

            verify(registerCertificateResponderInterface, times(1)).registerCertificate(same(LOGICAL_ADDRESS), any());

        } catch (ModuleException | IOException e) {
            fail();
        }
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailWhenErrorIsReturned() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.ERROR));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v4/transport/scenarios/pass-complete.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSendCertificateShouldSucceedWhenInfoIsReturned() throws ModuleException {
        when(registerCertificateResponderInterface.registerCertificate(anyString(), any()))
            .thenReturn(createReturnVal(ResultCodeType.INFO));
        try {
            String xmlContents = Resources.toString(Resources.getResource("v4/transport/scenarios/pass-complete.xml"), Charsets.UTF_8);
            moduleApi.sendCertificateToRecipient(xmlContents, LOGICAL_ADDRESS, null);
        } catch (IOException e) {
            fail();
        }
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnEmptyXml() throws ModuleException {
        moduleApi.sendCertificateToRecipient(null, LOGICAL_ADDRESS, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnNullLogicalAddress() throws ModuleException {
        moduleApi.sendCertificateToRecipient("blaha", null, null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnEmptyLogicalAddress() throws ModuleException {
        moduleApi.sendCertificateToRecipient("blaha", "", null);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateShouldFailOnMissingIntygTypeVersion() throws ModuleException {
        moduleApi.sendCertificateToRecipient("blaha", "", null);
    }

    @Test
    public void testValidateShouldUseValidator() throws Exception {
        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue("internal model", TsDiabetesUtlatandeV4.class);
        moduleApi.validateDraft("internal model");
        verify(validator, times(1)).validateDraft(any());
    }

    @Test
    public void testCreateNewInternal() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenReturn(null);
        moduleApi.createNewInternal(createDraftHolder());
        verify(webcertModelFactory, times(1)).createNewWebcertDraft(any());
    }

    @Test(expected = ModuleException.class)
    public void testCreateNewInternalThrowsModuleException() throws Exception {
        when(webcertModelFactory.createNewWebcertDraft(any())).thenThrow(new ConverterException());
        moduleApi.createNewInternal(createDraftHolder());
        fail();
    }

    @Test
    public void testCreateNewInternalFromTemplate() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenReturn(null);

        moduleApi.createNewInternalFromTemplate(createCopyHolder(), getUtlatandeFromFile());

        verify(webcertModelFactory, times(1)).createCopy(any(), any());
    }

    @Test(expected = ModuleException.class)
    public void testCreateNewInternalFromTemplateThrowsModuleException() throws Exception {
        when(webcertModelFactory.createCopy(any(), any())).thenThrow(new ConverterException());
        moduleApi.createNewInternalFromTemplate(createCopyHolder(), getUtlatandeFromFile());
        fail();
    }

    @Test
    public void testGetCertificate() throws Exception {
        final String certificateId = "certificateId";
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";

        when(getCertificateResponder.getCertificate(eq(logicalAddress), any())).thenReturn(createGetCertificateResponseType());
        when(objectMapper.writeValueAsString(any())).thenReturn(internalModel);

        CertificateResponse certificate = moduleApi.getCertificate(certificateId, logicalAddress, "INVANA");

        ArgumentCaptor<GetCertificateType> captor = ArgumentCaptor.forClass(GetCertificateType.class);
        verify(getCertificateResponder, times(1)).getCertificate(eq(logicalAddress), captor.capture());
        assertEquals(certificateId, captor.getValue().getIntygsId().getExtension());
        assertEquals(internalModel, certificate.getInternalModel());
        assertFalse(certificate.isRevoked());
    }

    @Test(expected = ModuleException.class)
    public void testGetCertificateThrowsModuleException() throws ModuleException, SOAPException {
        final String certificateId = "certificateId";
        final String logicalAddress = "logicalAddress";
        when(getCertificateResponder.getCertificate(eq(logicalAddress), any()))
            .thenThrow(new SOAPFaultException(SOAPFactory.newInstance().createFault()));
        moduleApi.getCertificate(certificateId, logicalAddress, "INVANA");
        fail();
    }

    @Test
    public void testRegisterCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.okResult());

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(internalModel, logicalAddress);

        ArgumentCaptor<RegisterCertificateType> captor = ArgumentCaptor.forClass(RegisterCertificateType.class);
        verify(registerCertificateResponderInterface, times(1)).registerCertificate(eq(logicalAddress), captor.capture());
        assertNotNull(captor.getValue().getIntyg());
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("Certificate already exists"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

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
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.infoResult("INFO"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        try {
            moduleApi.registerCertificate(internalModel, logicalAddress);
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRegisterCertificateShouldThrowExceptionOnFailedCallToIT() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();
        response.setResult(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "resultText"));

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        when(registerCertificateResponderInterface.registerCertificate(eq(logicalAddress), any())).thenReturn(response);

        moduleApi.registerCertificate(internalModel, logicalAddress);

        fail();
    }

    @Test(expected = ModuleConverterException.class)
    public void testRegisterCertificateShouldThrowExceptionOnBadCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        final String internalModel = "internal model";

        doReturn(null)
            .when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        moduleApi.registerCertificate(internalModel, logicalAddress);

        fail();
    }

    @Test
    public void testGetUtlatandeFromJson() throws Exception {
        final String utlatandeJson = "utlatandeJson";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(utlatandeJson, TsDiabetesUtlatandeV4.class);
        Utlatande utlatandeFromJson = moduleApi.getUtlatandeFromJson(utlatandeJson);
        assertNotNull(utlatandeFromJson);
    }

    @Test
    public void parseInvalidXmlFromProdDatabaseLegacyTest() throws Exception {
        // partial unmarshalling
        String xml = Resources.toString(Resources.getResource("v4/transport/ts-diabetes-invalid-xml-in-prod.xml"), Charsets.UTF_8);
        JAXB.unmarshal(new StringReader(xml), RegisterTSDiabetesType.class);
    }

    @Test(expected = UnmarshallingFailureException.class)
    public void parseInvalidXmlFromProdDatabaseNewTest() throws Exception {
        String xml = Resources.toString(Resources.getResource("v4/transport/ts-diabetes-invalid-xml-in-prod.xml"), Charsets.UTF_8);
        XmlMarshallerHelper.unmarshal(xml);
    }


    @Test
    public void testUpdateBeforeSave() throws Exception {
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        doReturn(internalModel)
            .when(objectMapper)
            .writeValueAsString(any());

        String response = moduleApi.updateBeforeSave(internalModel, createHosPersonal());
        assertNotNull(response);
        assertEquals(internalModel, response);
        verify(moduleService, times(0)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    public void testUpdateBeforeSigning() throws Exception {
        final String internalModel = "internal model";

        doReturn(ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel())
            .when(objectMapper)
            .readValue(internalModel, TsDiabetesUtlatandeV4.class);

        doReturn(internalModel)
            .when(objectMapper)
            .writeValueAsString(any());

        String response = moduleApi.updateBeforeSigning(internalModel, createHosPersonal(), null);
        assertNotNull(response);
        assertEquals(internalModel, response);
        verify(moduleService, times(0)).getDescriptionFromDiagnosKod(anyString(), anyString());
    }

    @Test
    public void testRevokeCertificate() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = Resources.toString(Resources.getResource("v4/revokerequest.xml"), Charsets.UTF_8);

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.okResult());
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        verify(revokeClient, times(1)).revokeCertificate(eq(logicalAddress), any());
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testRevokeCertificateThrowsExternalServiceCallException() throws Exception {
        final String logicalAddress = "logicalAddress";
        String xmlContents = Resources.toString(Resources.getResource("v4/revokerequest.xml"), Charsets.UTF_8);

        RevokeCertificateResponseType returnVal = new RevokeCertificateResponseType();
        returnVal.setResult(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "resultText"));
        when(revokeClient.revokeCertificate(eq(logicalAddress), any())).thenReturn(returnVal);
        moduleApi.revokeCertificate(xmlContents, logicalAddress);
        fail();
    }

    @Test
    public void testCreateRevokeRequest() throws Exception {
        final String meddelande = "revokeMessage";
        final String intygId = "intygId";

        GrundData gd = new GrundData();
        gd.setPatient(createPatient("", "", "191212121212"));
        HoSPersonal skapadAv = createHosPersonal();
        gd.setSkapadAv(skapadAv);

        Utlatande utlatande = TsDiabetesUtlatandeV4.builder().setId(intygId).setGrundData(gd).setTextVersion("").build();

        String res = moduleApi.createRevokeRequest(utlatande, skapadAv, meddelande);
        assertNotNull(res);
        assertNotEquals("", res);
    }

    @Test
    public void shallConvertInternalToCertificate() throws Exception {
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
            .when(objectMapper).readValue(eq(certificateAsJson), eq(TsDiabetesUtlatandeV4.class));

        doReturn(convertedCertificate)
            .when(internalToCertificate).convert(eq(internalCertificate), any(CertificateTextProvider.class));

        doReturn(CertificateSummary.builder().build())
            .when(summaryConverter).convert(eq(moduleApi), any(Intyg.class));

        final var actualCertificate = moduleApi.getCertificateFromJson(certificateAsJson, typeAheadProvider);
        assertEquals(expectedCertificate, actualCertificate);
    }


    @Test
    public void shallConvertCertificateToInternal() throws Exception {
        final var expectedJson = "expectedJson";
        final var certificate = CertificateBuilder.create().build();
        final var certificateAsJson = "certificateAsJson";

        final var internalCertificate = TsDiabetesUtlatandeV4.builder()
            .setId("123")
            .setTextVersion("1.0")
            .setGrundData(new GrundData())
            .build();

        doReturn(internalCertificate)
            .when(objectMapper).readValue(eq(certificateAsJson), eq(TsDiabetesUtlatandeV4.class));

        doReturn(expectedJson)
            .when(objectMapper).writeValueAsString(internalCertificate);

        doReturn(internalCertificate)
            .when(certificateToInternal).convert(certificate, internalCertificate);

        final var actualJson = moduleApi.getJsonFromCertificate(certificate, certificateAsJson);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void getCertficateMessagesProviderGetExistingKey() {
        final var certificateMessagesProvider = moduleApi.getMessagesProvider();

        assertEquals(certificateMessagesProvider.get("common.continue"), "Fortsätt");
    }

    @Test
    public void getCertficateMessagesProviderGetMissingKey() {
        final var certificateMessagesProvider = moduleApi.getMessagesProvider();

        assertNull(certificateMessagesProvider.get("not.existing"));
    }

    @Test
    public void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande()
        throws ModuleException, ScenarioNotFoundException {
        final var utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = moduleApi.getJsonFromUtlatande(utlatande);

        assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    public void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
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
        GetCertificateResponseType res = new GetCertificateResponseType();
        RegisterCertificateType registerType = ScenarioFinder.getInternalScenario("pass-minimal").asTransportModel();
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
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId("hsaId");
        hosPersonal.setFullstandigtNamn("namn");
        hosPersonal.setVardenhet(new Vardenhet());
        hosPersonal.getVardenhet().setVardgivare(new Vardgivare());
        return hosPersonal;
    }

    private Patient createPatient() {
        String PNR_TOLVAN = "19121212-1212";
        return createPatient("fornamn", "efternamn", PNR_TOLVAN);
    }

    private Patient createPatient(String fornamn, String efternamn, String pnr) {
        Patient patient = new Patient();
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
        RegisterCertificateResponseType retVal = new RegisterCertificateResponseType();
        ResultType value = new ResultType();
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
