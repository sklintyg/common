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
package se.inera.intyg.common.tstrk1062.v1.rest;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PARAMS;
import static se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder.ResolveOrder.PU;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXB;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PatientDetailResolveOrder;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.support.validate.RegisterCertificateValidator;
import se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint;
import se.inera.intyg.common.tstrk1062.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosKodad;
import se.inera.intyg.common.tstrk1062.v1.model.internal.DiagnosRegistrering;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.inera.intyg.common.tstrk1062.v1.pdf.PdfGenerator;
import se.inera.intyg.common.tstrk1062.v1.validator.InternalValidatorInstanceImpl;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v3.ResultType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BefattningService.class})
public class TsTrk1062ModuleApiV1Test {

    @Mock
    private RegisterCertificateResponderInterface registerCertificateResponderInterface;

    @Mock
    private WebcertModuleService moduleService;

    @Mock
    private WebcertModelFactoryImpl webcertModelFactory;

    @Mock
    private InternalValidatorInstanceImpl validator;

    @Mock
    private IntygTextsService intygTextsService;

    @Mock
    private PdfGenerator pdfGenerator;

    @Spy
    private CustomObjectMapper objectMapper;

    @Mock
    private GetCertificateResponderInterface getCertificateResponder;

    @Mock
    private RevokeCertificateResponderInterface revokeClient;

    @InjectMocks
    private TsTrk1062ModuleApiV1 moduleApi;

    private static final String TEXT_VERSION = "v1";
    private static final String INTERNAL_MODEL = "INTERNAL_MODEL";
    private static final String INTYGS_ID = "IntygsId";
    private static final String LOGICAL_ADDRESS = "LOGICAL_ADDRESS";
    private static final String RECIPIENT_ID = "RECIPIENT_ID";

    public TsTrk1062ModuleApiV1Test() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPDF() throws Exception {
        final GrundData grundData = buildGrundData(LocalDateTime.now());

        final TsTrk1062UtlatandeV1 mockUtlatande = mock(TsTrk1062UtlatandeV1.class);
        final IntygTexts mockIntygTexts = new IntygTexts("1.0", "", null, null, null, null, null);
        final ApplicationOrigin mockApplicationOrigin = ApplicationOrigin.WEBCERT;
        final UtkastStatus mockUtkastStatus = UtkastStatus.SIGNED;
        final PdfResponse expectedPdfResponse = mock(PdfResponse.class);
        final List<Status> statuses = new ArrayList<>();

        doReturn(mockUtlatande).when(objectMapper).readValue(INTERNAL_MODEL, TsTrk1062UtlatandeV1.class);
        doReturn(TEXT_VERSION).when(mockUtlatande).getTextVersion();
        doReturn(grundData).when(mockUtlatande).getGrundData();
        doReturn(INTYGS_ID).when(mockUtlatande).getId();

        doReturn(mockIntygTexts).when(intygTextsService).getIntygTextsPojo(TsTrk1062EntryPoint.MODULE_ID, TEXT_VERSION);
        doReturn(expectedPdfResponse).when(pdfGenerator).generatePdf(INTYGS_ID, INTERNAL_MODEL, grundData.getPatient().getPersonId(),
            mockIntygTexts,
            statuses, mockApplicationOrigin, mockUtkastStatus);

        final PdfResponse actualPdfResponse = moduleApi.pdf(INTERNAL_MODEL, statuses, mockApplicationOrigin, mockUtkastStatus);

        assertNotNull("PdfResponse is null", actualPdfResponse);
        assertEquals("PdfResponse is not equal", expectedPdfResponse, actualPdfResponse);
    }

    @Test
    public void testSendCertificateToRecipient() throws Exception {
        final String xml = getXml("v1/transport/scenarios/success/diagnosFritext.xml");

        final RegisterCertificateResponseType mockResponse = mock(RegisterCertificateResponseType.class);
        final ResultType mockResult = mock(ResultType.class);

        doReturn(mockResponse).when(registerCertificateResponderInterface).registerCertificate(eq(LOGICAL_ADDRESS), any());

        doReturn(mockResult).when(mockResponse).getResult();
        doReturn(ResultCodeType.OK).when(mockResult).getResultCode();

        moduleApi.sendCertificateToRecipient(xml, LOGICAL_ADDRESS, RECIPIENT_ID);

        ArgumentCaptor<RegisterCertificateType> captor = ArgumentCaptor.forClass(RegisterCertificateType.class);
        verify(registerCertificateResponderInterface, times(1)).registerCertificate(eq(LOGICAL_ADDRESS), captor.capture());
        Assert.assertNotNull(captor.getValue().getIntyg());
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientXmlNull() throws Exception {
        final String xml = null;
        moduleApi.sendCertificateToRecipient(xml, LOGICAL_ADDRESS, RECIPIENT_ID);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientXmlEmpty() throws Exception {
        final String xml = "";
        moduleApi.sendCertificateToRecipient(xml, LOGICAL_ADDRESS, RECIPIENT_ID);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientLogicalAdressNull() throws Exception {
        final String xml = getXml("v1/transport/scenarios/success/diagnosFritext.xml");
        moduleApi.sendCertificateToRecipient(xml, null, RECIPIENT_ID);
    }

    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientLogicalAdressEmpty() throws Exception {
        final String xml = getXml("v1/transport/scenarios/success/diagnosFritext.xml");
        moduleApi.sendCertificateToRecipient(xml, "", RECIPIENT_ID);
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testSendCertificateToRecipientWithErrorResult() throws Exception {
        final String xml = getXml("v1/transport/scenarios/success/diagnosFritext.xml");

        final RegisterCertificateResponseType mockResponse = mock(RegisterCertificateResponseType.class);
        final ResultType mockResult = mock(ResultType.class);

        doReturn(mockResponse).when(registerCertificateResponderInterface).registerCertificate(eq(LOGICAL_ADDRESS), any());

        doReturn(mockResult).when(mockResponse).getResult();
        doReturn(ResultCodeType.ERROR).when(mockResult).getResultCode();

        moduleApi.sendCertificateToRecipient(xml, LOGICAL_ADDRESS, RECIPIENT_ID);
    }

    @Test(expected = ExternalServiceCallException.class)
    public void testSendCertificateToRecipientWithSOAPError() throws Exception {
        final String xml = getXml("v1/transport/scenarios/success/diagnosFritext.xml");

        final RegisterCertificateResponseType mockResponse = mock(RegisterCertificateResponseType.class);
        final SOAPFault mockFault = mock(SOAPFault.class);

        doThrow(new SOAPFaultException(mockFault)).when(registerCertificateResponderInterface).registerCertificate(eq(LOGICAL_ADDRESS),
            any());

        moduleApi.sendCertificateToRecipient(xml, LOGICAL_ADDRESS, RECIPIENT_ID);
    }

    @Test
    public void testUtlatandeFromXml() throws Exception {
        final String xml = getXml("v1/transport/scenarios/success/diagnosFritext.xml");

        final TsTrk1062UtlatandeV1 utlatande = moduleApi.getUtlatandeFromXml(xml);

        assertNotNull("Utlatande is null", utlatande);
    }

    @Test(expected = ModuleException.class)
    public void testUtlatandeFromXmlIncorrectXml() throws Exception {
        final String xml = "INCORRECT XML";

        moduleApi.getUtlatandeFromXml(xml);
    }

    @Test
    public void testConvertUtlatandeToIntyg() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = TsTrk1062UtlatandeV1.builder()
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .build();

        final Intyg intyg = moduleApi.utlatandeToIntyg(utlatande);

        assertNotNull("Intyg is null", intyg);
    }

    @Test
    public void testGetRegisterCertificateValidator() throws Exception {
        final RegisterCertificateValidator actualRegisterCertificateValidator = moduleApi.getRegisterCertificateValidator();

        assertNotNull("RegisterCertificateValidator is null", actualRegisterCertificateValidator);
    }

    @Test
    public void testInternalToTransport() throws Exception {
        final TsTrk1062UtlatandeV1 utlatande = TsTrk1062UtlatandeV1.builder()
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .build();

        final RegisterCertificateType actualRegisterCertificateType = moduleApi.internalToTransport(utlatande);

        assertNotNull("RegisterCertificateType", actualRegisterCertificateType);
    }

    @Test
    public void testGetPatientDetailResolveOrderAddress() {
        final List<PatientDetailResolveOrder.ResolveOrder> expectedAddressOrder = Arrays.asList(PARAMS, PU);

        final PatientDetailResolveOrder actualPatientDetailResolveOrder = moduleApi.getPatientDetailResolveOrder();

        assertNotNull("PatientDetailResolveOrder", actualPatientDetailResolveOrder);
        final List<PatientDetailResolveOrder.ResolveOrder> actualAdressOrder = actualPatientDetailResolveOrder.getAdressStrategy();
        assertNotNull("AddressResolveOrder should not be null", actualAdressOrder);
        assertEquals("AddressResolveOrder not equal length ", expectedAddressOrder.size(), actualAdressOrder.size());
        for (int i = 0; i < expectedAddressOrder.size(); i++) {
            assertEquals("AddressOrder element: " + i + " not equal", expectedAddressOrder.get(i), actualAdressOrder.get(i));
        }
    }

    @Test
    public void testGetPatientDetailResolveOrderOther() {
        final List<PatientDetailResolveOrder.ResolveOrder> expectedOtherOrder = Arrays.asList(PU, PARAMS);

        final PatientDetailResolveOrder actualPatientDetailResolveOrder = moduleApi.getPatientDetailResolveOrder();

        assertNotNull("PatientDetailResolveOrder", actualPatientDetailResolveOrder);
        final List<PatientDetailResolveOrder.ResolveOrder> actualOtherOrder = actualPatientDetailResolveOrder.getOtherStrategy();
        assertNotNull("OtherResolveOrder should not be null", actualOtherOrder);
        assertEquals("OtherResolveOrder not equal length ", expectedOtherOrder.size(), actualOtherOrder.size());
        for (int i = 0; i < expectedOtherOrder.size(); i++) {
            assertEquals("OtherOrder element: " + i + " not equal", expectedOtherOrder.get(i), actualOtherOrder.get(i));
        }
    }

    @Test
    public void testTransportToInternal() throws Exception {
        final String href = "v1/transport/scenarios/convert/intygAvser.xml";
        final Intyg intyg = getIntyg(href);

        final TsTrk1062UtlatandeV1 actualUtlatande = moduleApi.transportToInternal(intyg);

        assertNotNull("Utlatande should not be null", actualUtlatande);
    }

    @Test
    public void testdecorateDiagnoserWithDescriptions() throws Exception {
        final String expectedDiagnosDisplayName = "Detta är diagnosbeskrivningen";
        final DiagnosKodad expectedDiagnosKodad = DiagnosKodad.create("A01", "ICD10",
            "Diagnosbeskrivning", null, "Årtal");

        final List<DiagnosKodad> expectedDiagnosKodadList = new ArrayList<>(1);
        expectedDiagnosKodadList.add(expectedDiagnosKodad);

        final TsTrk1062UtlatandeV1 utlatande = TsTrk1062UtlatandeV1.builder()
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .setDiagnosRegistrering(DiagnosRegistrering.create(DiagnosRegistrering.DiagnosRegistreringsTyp.DIAGNOS_KODAD))
            .setDiagnosKodad(expectedDiagnosKodadList)
            .build();

        doReturn(expectedDiagnosDisplayName).when(moduleService).getDescriptionFromDiagnosKod(expectedDiagnosKodad.getDiagnosKod(),
            expectedDiagnosKodad.getDiagnosKodSystem());

        final TsTrk1062UtlatandeV1 actualUtlatande = moduleApi.decorateDiagnoserWithDescriptions(utlatande);

        assertNotNull("Utlatande should not be null", actualUtlatande);
        final List<DiagnosKodad> actualDiagnosKodadList = actualUtlatande.getDiagnosKodad();
        assertNotNull("DiagnosKodadList should not be null", actualDiagnosKodadList);
        assertEquals("DiagnosKodadList should be length one", expectedDiagnosKodadList.size(), actualDiagnosKodadList.size());
        final DiagnosKodad actualDiagnosKodad = actualDiagnosKodadList.get(0);
        assertEquals("DiagnosKodad kod should be same", expectedDiagnosKodad.getDiagnosKod(), actualDiagnosKodad.getDiagnosKod());
        assertEquals("DiagnosKodad beskrivning should be same", expectedDiagnosKodad.getDiagnosBeskrivning(),
            actualDiagnosKodad.getDiagnosBeskrivning());
        assertEquals("DiagnosKodad kodsystem should be same", expectedDiagnosKodad.getDiagnosKodSystem(),
            actualDiagnosKodad.getDiagnosKodSystem());
        assertEquals("DiagnosKodad artal should be same", expectedDiagnosKodad.getDiagnosArtal(), actualDiagnosKodad.getDiagnosArtal());
        assertEquals("DiagnosKodad displayname should be same", expectedDiagnosDisplayName, actualDiagnosKodad.getDiagnosDisplayName());
    }

    @Test
    public void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande()
        throws ModuleException {
        final var utlatande = TsTrk1062UtlatandeV1.builder()
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .build();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = moduleApi.getJsonFromUtlatande(utlatande);

        Assert.assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    public void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> moduleApi.getJsonFromUtlatande(null));
    }

    private String toJsonString(TsTrk1062UtlatandeV1 utlatande) throws ModuleException {
        try {
            return objectMapper.writeValueAsString(utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
    }

    private Intyg getIntyg(String href) throws Exception {
        final String xml = getXml(href);
        return JAXB.unmarshal(new StringReader(xml), RegisterCertificateType.class).getIntyg();
    }

    private String getXml(String href) throws Exception {
        return Resources.toString(getResource(href), Charsets.UTF_8);
    }

    private static URL getResource(String href) {
        return Thread.currentThread().getContextClassLoader().getResource(href);
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("vardgivareNamn");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("0112312313");

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId("HSAID_123");
        skapadAv.setFullstandigtNamn("Torsten Ericsson");

        Patient patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
        patient.setPostadress("postadress");
        patient.setPostnummer("11111");
        patient.setPostort("postort");

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }
}
