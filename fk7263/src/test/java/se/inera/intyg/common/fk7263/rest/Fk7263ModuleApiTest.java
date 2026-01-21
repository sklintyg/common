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
package se.inera.intyg.common.fk7263.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aDatePeriod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.fk7263.integration.RegisterMedicalCertificateResponderImpl;
import se.inera.intyg.common.fk7263.model.converter.InternalToTransport;
import se.inera.intyg.common.fk7263.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.fk7263.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.support.facade.model.CertificateLink;
import se.inera.intyg.common.support.facade.model.CertificateText;
import se.inera.intyg.common.support.facade.model.CertificateTextType;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMapperUtil;
import se.inera.intyg.common.support.modules.converter.mapping.UnitMappingConfigLoader;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException;
import se.inera.intyg.common.support.modules.support.api.exception.ExternalServiceCallException.ErrorIdEnum;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

/**
 * @author andreaskaltenbach
 */
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {UnitMappingConfigLoader.class, UnitMapperUtil.class, InternalConverterUtil.class})
public class Fk7263ModuleApiTest {

    public static final String TESTFILE_UTLATANDE = "Fk7263ModuleApiTest/utlatande.json";
    public static final String TESTFILE_UTLATANDE_MINIMAL = "Fk7263ModuleApiTest/utlatande-minimal.json";

    @Mock
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient;
    @Spy
    private WebcertModelFactory<Fk7263Utlatande> webcertModelFactory = new WebcertModelFactoryImpl();
    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @InjectMocks
    private Fk7263ModuleApi fk7263ModuleApi;

    @Test
    void updateChangesHosPersonalInfo() throws IOException, ModuleException {
        final var utlatande = getUtlatandeFromFile();
        final var vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivarId");
        vardgivare.setVardgivarnamn("vardgivarNamn");

        final var vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");

        final var hosPerson = new HoSPersonal();
        hosPerson.setPersonId("nyId");
        hosPerson.setFullstandigtNamn("nyNamn");
        hosPerson.setForskrivarKod("nyForskrivarkod");
        hosPerson.setVardenhet(vardenhet);

        final var signingDate = LocalDate.parse("2014-08-01").atStartOfDay();
        final var updatedHolder = fk7263ModuleApi.updateBeforeSigning(toJsonString(utlatande), hosPerson, signingDate);
        final var updatedIntyg = objectMapper.readValue(updatedHolder, Fk7263Utlatande.class);

        assertEquals(signingDate, updatedIntyg.getGrundData().getSigneringsdatum());
        assertEquals("nyId", updatedIntyg.getGrundData().getSkapadAv().getPersonId());
        assertEquals("nyNamn", updatedIntyg.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals("nyForskrivarkod", updatedIntyg.getGrundData().getSkapadAv().getForskrivarKod());
        assertEquals(vardenhet.getEnhetsnamn(), updatedIntyg.getGrundData().getSkapadAv().getVardenhet()
            .getEnhetsnamn());
    }

    @Test
    void updatePatientBeforeSave() throws IOException, ModuleException {
        final var utlatande = getUtlatandeFromFile();
        final var updatedPatient = createUpdatedPatient();
        final var res = fk7263ModuleApi.updateBeforeSave(toJsonString(utlatande), updatedPatient);
        assertNotNull(res);
        assertEquals(updatedPatient, fk7263ModuleApi.getUtlatandeFromJson(res).getGrundData().getPatient());
    }

    @Test
    void testUpdatePatientBeforeViewing() throws Exception {
        final var utlatande = getUtlatandeFromFile();
        final var updatedPatient = createUpdatedPatient();

        final var res = fk7263ModuleApi.updateBeforeViewing(toJsonString(utlatande), updatedPatient);
        assertNotNull(res);
        assertEquals(updatedPatient, fk7263ModuleApi.getUtlatandeFromJson(res).getGrundData().getPatient());
    }

    @Test
    void copyContainsOriginalData() throws IOException, ModuleException {
        final var utlatande = getUtlatandeFromFile();

        final var patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(createPnr("19121212-1212"));
        final var copyHolder = createDraftCopyHolder(patient);

        final var holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, utlatande);

        assertNotNull(holder);
        final var creatededUtlatande = objectMapper.readValue(holder, Fk7263Utlatande.class);
        assertEquals("2011-03-07", creatededUtlatande.getNedsattMed50().getFrom().getDate());
        assertNull(creatededUtlatande.getGrundData().getPatient().getFornamn());
        assertNull(creatededUtlatande.getGrundData().getPatient().getEfternamn());
    }

    @Test
    void copyContainsOriginalPersondetails() throws IOException, ModuleException {
        final var utlatande = getUtlatandeFromFile();

        // create copyholder without Patient in it
        final var copyHolder = createDraftCopyHolder(null);

        final var holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, utlatande);

        assertNotNull(holder);
        final var creatededUtlatande = objectMapper.readValue(holder, Fk7263Utlatande.class);
        assertNull(creatededUtlatande.getGrundData().getPatient().getEfternamn());
        assertEquals("191212121212", creatededUtlatande.getGrundData().getPatient().getPersonId().getPersonnummer());
    }

    @Test
    void copyContainsNewPersonnummer() throws IOException, ModuleException {
        final var newSSN = createPnr("19121212-1414");
        final var utlatande = getUtlatandeFromFile();
        final var patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(createPnr("19121212-1212"));

        final var copyHolder = createDraftCopyHolder(patient);
        copyHolder.setNewPersonnummer(newSSN);

        final var holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, utlatande);
        assertNotNull(holder);

        final var creatededUtlatande = objectMapper.readValue(holder, Fk7263Utlatande.class);
        assertNull(creatededUtlatande.getGrundData().getPatient().getFornamn());
        assertNull(creatededUtlatande.getGrundData().getPatient().getEfternamn());
        assertEquals(newSSN, creatededUtlatande.getGrundData().getPatient().getPersonId());
    }

    @Test
    void testSendCertificateWhenRecipientIsOtherThanFk() throws Exception {
        final var xml = marshall(Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE).getURL(), Charsets.UTF_8));
        final var address = new AttributedURIType();
        address.setValue("logicalAddress");

        final var response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        when(registerMedicalCertificateClient.registerMedicalCertificate(
            any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        fk7263ModuleApi.sendCertificateToRecipient(xml, "logicalAddress", null);

        verify(registerMedicalCertificateClient).registerMedicalCertificate(eq(address), Mockito.any(RegisterMedicalCertificateType.class));
    }

    @Test
    void testSendFullCertificateWhenRecipientIsFk() throws Exception {
        final var xml = marshall(Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE).getURL(), Charsets.UTF_8));
        final var address = new AttributedURIType();
        address.setValue("logicalAddress");

        final var response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        when(registerMedicalCertificateClient.registerMedicalCertificate(
            any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        fk7263ModuleApi.sendCertificateToRecipient(xml, "logicalAddress", "FK");

        verify(registerMedicalCertificateClient).registerMedicalCertificate(Mockito.eq(address), any(RegisterMedicalCertificateType.class));
    }

    @Test
    void testSendMinimalCertificateWhenRecipientIsFk() throws Exception {
        final var xml = marshall(Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE_MINIMAL).getURL(), Charsets.UTF_8));

        final var address = new AttributedURIType();
        address.setValue("logicalAddress");

        final var response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        when(registerMedicalCertificateClient.registerMedicalCertificate(
            any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        fk7263ModuleApi.sendCertificateToRecipient(xml, "logicalAddress", "FKASSA");

        verify(registerMedicalCertificateClient).registerMedicalCertificate(eq(address), any(RegisterMedicalCertificateType.class));
    }

    @Test
    void whenFkIsRecipientAndBadCertificateThenThrowException() {
        final var address = new AttributedURIType();
        address.setValue("logicalAddress");
        assertThrows(ModuleException.class, () ->
            fk7263ModuleApi.sendCertificateToRecipient(null, "logicalAddress", "FKASSA")
        );
    }

    @Test
    void whenFkIsRecipientThenSetCodeSystemToICD10() throws Exception {
        final var utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);
        request = fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);

        assertEquals("ICD-10", request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().getCodeSystemName());
    }

    @Test
    void whenFkIsRecipientAndNotSmittskyddAndNoMedicinsktTillstandThenThrowException() throws IOException, ConverterException {
        final var utlatande = getUtlatandeFromFile();
        final var request = InternalToTransport.getJaxbObject(utlatande);
        request.getLakarutlatande().setMedicinsktTillstand(null);
        assertThrows(ModuleException.class, () ->
            fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request)
        );
    }

    @Test
    void whenFkIsRecipientAndNotSmittskyddAndNoTillstandskodThenThrowException() throws IOException, ConverterException {
        final var utlatande = getUtlatandeFromFile();
        final var request = InternalToTransport.getJaxbObject(utlatande);
        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(null);
        assertThrows(ModuleException.class, () ->
            fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request)
        );
    }

    @Test
    void getAdditionalInfoFromUtlatandeTest() throws Exception {
        final var utlatande = getUtlatandeFromFile();
        final var intyg = UtlatandeToIntyg.convert(utlatande);
        final var result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertEquals("2011-01-26 - 2011-05-31", result);
    }

    @Test
    void getAdditionalInfoOneTimePeriodTest() throws ModuleException {
        final var fromString = "2015-12-12";
        final var toString = "2016-03-02";
        final var from = LocalDate.parse(fromString);
        final var to = LocalDate.parse(toString);
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        final var s = new Svar();
        s.setId("32");
        final var delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        final var result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertEquals(fromString + " - " + toString, result);
    }

    @Test
    void getAdditionalInfoMultiplePeriodsTest() throws ModuleException {
        final var fromString = "2015-12-12";
        final var middleDate1 = "2015-12-13";
        final var middleDate2 = "2015-12-14";
        final var middleDate3 = "2015-12-15";
        final var middleDate4 = "2015-12-16";
        final var toString = "2016-03-02";
        final var from = LocalDate.parse(fromString);
        final var to = LocalDate.parse(toString);
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        final var s = new Svar();
        s.setId("32");
        final var delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(LocalDate.parse(middleDate2), LocalDate.parse(middleDate3)));
        s.getDelsvar().add(delsvar);
        final var s2 = new Svar();
        s2.setId("32");
        final var delsvar2 = new Delsvar();
        delsvar2.setId("32.2");
        delsvar2.getContent().add(aDatePeriod(LocalDate.parse(middleDate4), to));
        s2.getDelsvar().add(delsvar2);
        final var s3 = new Svar();
        s3.setId("32");
        final var delsvar3 = new Delsvar();
        delsvar3.setId("32.2");
        delsvar3.getContent().add(aDatePeriod(from, LocalDate.parse(middleDate1)));
        s3.getDelsvar().add(delsvar3);
        intyg.getSvar().add(s);
        intyg.getSvar().add(s2);
        intyg.getSvar().add(s3);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertEquals(fromString + " - " + toString, result);
    }

    @Test
    void getAdditionalInfoSvarNotFoundTest() throws ModuleException {
        final var fromString = "2015-12-12";
        final var toString = "2016-03-02";
        final var from = LocalDate.parse(fromString);
        final var to = LocalDate.parse(toString);
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        final var s = new Svar();
        s.setId("30"); // wrong SvarId
        final var delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        final var result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertNull(result);
    }

    @Test
    void getAdditionalInfoDelSvarNotFoundTest() throws ModuleException {
        final var fromString = "2015-12-12";
        final var toString = "2016-03-02";
        final var from = LocalDate.parse(fromString);
        final var to = LocalDate.parse(toString);
        final var intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        final var s = new Svar();
        s.setId("32");
        final var delsvar = new Delsvar();
        delsvar.setId("32.1"); // wrong delsvarId
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        final var result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertNull(result);
    }

    @Test
    void testRegisterCertificateAlreadyExists() throws Exception {
        final var json = Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE_MINIMAL).getURL(), Charsets.UTF_8);

        final var address = new AttributedURIType();
        address.setValue("logicalAddress");

        final var response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.infoResult(RegisterMedicalCertificateResponderImpl.CERTIFICATE_ALREADY_EXISTS));

        when(registerMedicalCertificateClient.registerMedicalCertificate(
            any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        try {
            fk7263ModuleApi.registerCertificate(json, "logicalAddress");
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.VALIDATION_ERROR, e.getErroIdEnum());
            assertEquals(RegisterMedicalCertificateResponderImpl.CERTIFICATE_ALREADY_EXISTS, e.getMessage());
        }
    }

    @Test
    void testRegisterCertificateGenericInfoResult() throws Exception {
        final var json = Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE_MINIMAL).getURL(), Charsets.UTF_8);

        final var address = new AttributedURIType();
        address.setValue("logicalAddress");

        final var response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.infoResult("INFO"));

        when(registerMedicalCertificateClient.registerMedicalCertificate(
            any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        try {
            fk7263ModuleApi.registerCertificate(json, "logicalAddress");
            fail();
        } catch (ExternalServiceCallException e) {
            assertEquals(ErrorIdEnum.APPLICATION_ERROR, e.getErroIdEnum());
            assertEquals("INFO", e.getMessage());
        }
    }

    @Test
    void getCertficateMessagesProviderGetExistingKey() {
        final var certificateMessagesProvider = fk7263ModuleApi.getMessagesProvider();

        assertEquals("Fortsätt", certificateMessagesProvider.get("common.continue"));
    }

    @Test
    void getCertficateMessagesProviderGetMissingKey() {
        final var certificateMessagesProvider = fk7263ModuleApi.getMessagesProvider();

        assertNull(certificateMessagesProvider.get("not.existing"));
    }

    @Test
    void getJsonFromUtlatandeshallReturnJsonRepresentationOfUtlatande() throws ModuleException, IOException {
        final var utlatande = getUtlatandeFromFile();
        final var expectedJsonString = toJsonString(utlatande);
        final var actualJsonString = fk7263ModuleApi.getJsonFromUtlatande(utlatande);

        assertEquals(expectedJsonString, actualJsonString);
    }

    @Test
    void getJsonFromUtlatandeShallThrowIllegalArgumentExceptionIfUtlatandeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fk7263ModuleApi.getJsonFromUtlatande(null));
    }

    @Test
    void shouldReturnAdditionalInfoLabel() {
        final var response = fk7263ModuleApi.getAdditionalInfoLabel();

        assertEquals("Gäller intygsperiod", response);
    }

    @Test
    void shouldReturnPreambleForCitizens() {
        final var expectedResult = CertificateText
            .builder()
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

        assertEquals(expectedResult, fk7263ModuleApi.getPreambleForCitizens());
    }

    private Fk7263Utlatande getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
            TESTFILE_UTLATANDE).getFile(), Fk7263Utlatande.class);
    }

    private String toJsonString(Fk7263Utlatande utlatande) throws ModuleException {
        final var writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        return writer.toString();
    }

    private CreateDraftCopyHolder createDraftCopyHolder(Patient patient) {
        final var vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("hsaId0");
        vardgivare.setVardgivarnamn("vardgivare");
        final var vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("hsaId1");
        vardenhet.setEnhetsnamn("namn");
        vardenhet.setVardgivare(vardgivare);
        final var hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId("Id1");
        hosPersonal.setFullstandigtNamn("Grodan Boll");
        hosPersonal.setForskrivarKod("forskrivarkod");
        hosPersonal.getBefattningar().add("befattning");
        hosPersonal.setVardenhet(vardenhet);

        final var holder = new CreateDraftCopyHolder("Id1", hosPersonal);

        if (patient != null) {
            holder.setPatient(patient);
        }

        return holder;
    }

    private Patient createUpdatedPatient() {
        final var updatedPatient = new Patient();
        updatedPatient.setEfternamn("updated lastName");
        updatedPatient.setMellannamn("updated middle-name");
        updatedPatient.setFornamn("updated firstName");
        updatedPatient.setFullstandigtNamn("updated full name");
        updatedPatient.setPersonId(createPnr("19121212-1212"));
        updatedPatient.setPostadress("updated postal address");
        updatedPatient.setPostnummer("54321");
        updatedPatient.setPostort("updated post city");

        return updatedPatient;
    }

    private Personnummer createPnr(String civicRegistrationNumber) {
        return Personnummer.createPersonnummer(civicRegistrationNumber).orElseThrow();
    }

    private String marshall(String jsonString) throws Exception {
        final var internal = objectMapper.readValue(jsonString, Fk7263Utlatande.class);
        final var external = InternalToTransport.getJaxbObject(internal);
        final var el = new se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3
            .ObjectFactory().createRegisterMedicalCertificate(external);
        return XmlMarshallerHelper.marshal(el);
    }
}