/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.rest;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.bind.JAXB;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.RegisterTSBasResponder.v1.RegisterTSBasType;
import se.inera.intygstjanster.ts.services.v1.IntygMeta;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

/**
 * Sets up an actual HTTP server and client to test the {@link ModuleApi} service. This is the place to verify that
 * response headers and response statuses etc are correct.
 */
//@RunWith(MockitoJUnitRunner.class)
public class Tstrk1009ModuleApiTest {

    private static final String INTYG_TYPE_VERSION_6_8 = "6.8";
/*    @InjectMocks
    @Spy
    private Tstrk1009ModuleApiV1 moduleApi = new Tstrk1009ModuleApiV1();

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Spy
    private WebcertModelFactoryImpl webcertModelFactory = new WebcertModelFactoryImpl();

    @Mock
    private XslTransformerFactory xslTransformerFactory;

    @Mock
    private IntygTextsService intygTexts;*/

/*    @Mock
    private SendTSClient sendTsClient;*/

    /*@Before
    public void setup() throws Exception {
        // Init the default beahviour
        setRegisterCertificateVersion(REGISTER_CERTIFICATE_VERSION1);

        // use reflection to set IntygTextsService mock in webcertModelFactory
        Field field = WebcertModelFactoryImpl.class.getDeclaredField("intygTexts");
        field.setAccessible(true);
        field.set(webcertModelFactory, intygTexts);

        when(xslTransformerFactory.get(any(XslTransformerType.class))).thenReturn(mock(XslTransformer.class));
    }*/

/*    @Test
    public void createNewInternal() throws ModuleException {
        CreateNewDraftHolder holder = createNewDraftHolder();
        String response = moduleApi.createNewInternal(holder);
        assertNotNull(response);
    }*/

/*    @Test
    public void testSendCertificateToRecipient() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";

        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(false);

        when(sendTsClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);

        verify(sendTsClient).registerCertificate(transformedXml, logicalAddress);
    }*/

/*    @Test(expected = ModuleException.class)
    public void testSendCertificateToRecipientFault() throws Exception {
        final String xmlBody = "xmlBody";
        final String logicalAddress = "logicalAddress";
        final String recipientId = "recipient";
        final String transformedXml = "transformedXml";

        SOAPMessage response = mock(SOAPMessage.class);
        when(response.getSOAPPart()).thenReturn(mock(SOAPPart.class));
        when(response.getSOAPPart().getEnvelope()).thenReturn(mock(SOAPEnvelope.class));
        when(response.getSOAPPart().getEnvelope().getBody()).thenReturn(mock(SOAPBody.class));
        when(response.getSOAPPart().getEnvelope().getBody().hasFault()).thenReturn(true);
        when(sendTsClient.registerCertificate(transformedXml, logicalAddress)).thenReturn(response);

        moduleApi.sendCertificateToRecipient(xmlBody, logicalAddress, recipientId);
    }*/

/*
    @Test
    public void getAdditionalInfoFromUtlatandeTest() throws Exception {
       Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-maximal").asInternalModel();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        String result = moduleApi.getAdditionalInfo(intyg);
        assertEquals("C1, C1E, C, CE, D1, D1E, D, DE, TAXI, ANNAT", result);
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
        assertEquals("C1, C, TAXI", result);
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
    }*/

/*    @Test
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
        final String res = moduleApi.updateBeforeViewing(validMinimalJson, updatedPatient);

        assertNotNull(res);
        JSONAssert.assertEquals(validMinimalJson,res, JSONCompareMode.LENIENT);
    }*/

    private IntygMeta createMeta() throws ScenarioNotFoundException {
        IntygMeta meta = new IntygMeta();
        meta.setAdditionalInfo("C");
        meta.setAvailable("true");
        return meta;
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

/*
    private void setRegisterCertificateVersion(String version) {
        ReflectionTestUtils.setField(moduleApi, "registerCertificateVersion", version);
    }
*/

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
