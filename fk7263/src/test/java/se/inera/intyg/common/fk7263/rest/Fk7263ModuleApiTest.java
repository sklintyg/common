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
package se.inera.intyg.common.fk7263.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aDatePeriod;


import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.bind.JAXBElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.w3.wsaddressing10.AttributedURIType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.fk7263.integration.RegisterMedicalCertificateResponderImpl;
import se.inera.intyg.common.fk7263.model.converter.InternalToTransport;
import se.inera.intyg.common.fk7263.model.converter.UtlatandeToIntyg;
import se.inera.intyg.common.fk7263.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.WebcertModelFactory;
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
@RunWith(MockitoJUnitRunner.class)
public class Fk7263ModuleApiTest {

    public static final String TESTFILE_UTLATANDE = "Fk7263ModuleApiTest/utlatande.json";
    public static final String TESTFILE_UTLATANDE_MINIMAL = "Fk7263ModuleApiTest/utlatande-minimal.json";

    @Mock
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient;

    @Spy
    private WebcertModelFactory<Fk7263Utlatande> webcertModelFactory = new WebcertModelFactoryImpl();

    @InjectMocks
    private Fk7263ModuleApi fk7263ModuleApi;

    @Spy
    private ObjectMapper objectMapper = new CustomObjectMapper();

    @Test
    public void updateChangesHosPersonalInfo() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivarId");
        vardgivare.setVardgivarnamn("vardgivarNamn");
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");
        HoSPersonal hosPerson = new HoSPersonal();
        hosPerson.setPersonId("nyId");
        hosPerson.setFullstandigtNamn("nyNamn");
        hosPerson.setForskrivarKod("nyForskrivarkod");
        hosPerson.setVardenhet(vardenhet);
        LocalDateTime signingDate = LocalDate.parse("2014-08-01").atStartOfDay();
        String updatedHolder = fk7263ModuleApi.updateBeforeSigning(toJsonString(utlatande), hosPerson, signingDate);
        Fk7263Utlatande updatedIntyg = objectMapper.readValue(updatedHolder, Fk7263Utlatande.class);

        assertEquals(signingDate, updatedIntyg.getGrundData().getSigneringsdatum());
        assertEquals("nyId", updatedIntyg.getGrundData().getSkapadAv().getPersonId());
        assertEquals("nyNamn", updatedIntyg.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals("nyForskrivarkod", updatedIntyg.getGrundData().getSkapadAv().getForskrivarKod());
        assertEquals(vardenhet.getEnhetsnamn(), updatedIntyg.getGrundData().getSkapadAv().getVardenhet()
            .getEnhetsnamn());
    }

    @Test
    public void updatePatientBeforeSave() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();
        Patient updatedPatient = createUpdatedPatient();

        String res = fk7263ModuleApi.updateBeforeSave(toJsonString(utlatande), updatedPatient);
        assertNotNull(res);
        assertEquals(updatedPatient, fk7263ModuleApi.getUtlatandeFromJson(res).getGrundData().getPatient());
    }

    @Test
    public void testUpdatePatientBeforeViewing() throws Exception {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();
        Patient updatedPatient = createUpdatedPatient();

        String res = fk7263ModuleApi.updateBeforeViewing(toJsonString(utlatande), updatedPatient);
        assertNotNull(res);
        assertEquals(updatedPatient, fk7263ModuleApi.getUtlatandeFromJson(res).getGrundData().getPatient());
    }

    @Test
    public void copyContainsOriginalData() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();

        Patient patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(createPnr("19121212-1212"));
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(patient);

        String holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, utlatande);

        assertNotNull(holder);
        Fk7263Utlatande creatededUtlatande = objectMapper.readValue(holder, Fk7263Utlatande.class);
        assertEquals("2011-03-07", creatededUtlatande.getNedsattMed50().getFrom().getDate());
        assertNull(creatededUtlatande.getGrundData().getPatient().getFornamn());
        assertNull(creatededUtlatande.getGrundData().getPatient().getEfternamn());
    }

    @Test
    public void copyContainsOriginalPersondetails() throws IOException, ModuleException {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();

        // create copyholder without Patient in it
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(null);

        String holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, utlatande);

        assertNotNull(holder);
        Fk7263Utlatande creatededUtlatande = objectMapper.readValue(holder, Fk7263Utlatande.class);
        assertNull(creatededUtlatande.getGrundData().getPatient().getEfternamn());
        assertEquals("191212121212", creatededUtlatande.getGrundData().getPatient().getPersonId().getPersonnummer());
    }

    @Test
    public void copyContainsNewPersonnummer() throws IOException, ModuleException {

        Personnummer newSSN = createPnr("19121212-1414");

        Fk7263Utlatande utlatande = getUtlatandeFromFile();

        Patient patient = new Patient();
        patient.setFornamn("Kalle");
        patient.setEfternamn("Kula");
        patient.setPersonId(createPnr("19121212-1212"));
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(patient);
        copyHolder.setNewPersonnummer(newSSN);

        String holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, utlatande);
        assertNotNull(holder);

        Fk7263Utlatande creatededUtlatande = objectMapper.readValue(holder, Fk7263Utlatande.class);
        assertNull(creatededUtlatande.getGrundData().getPatient().getFornamn());
        assertNull(creatededUtlatande.getGrundData().getPatient().getEfternamn());
        assertEquals(newSSN, creatededUtlatande.getGrundData().getPatient().getPersonId());
    }

    @Test
    public void testSendCertificateWhenRecipientIsOtherThanFk() throws Exception {
        String xml = marshall(Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE).getURL(), Charsets.UTF_8));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        // Wen
        when(registerMedicalCertificateClient.registerMedicalCertificate(
            any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(xml, "logicalAddress", null);

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(eq(address), Mockito.any(RegisterMedicalCertificateType.class));
    }

    @Test
    public void testSendFullCertificateWhenRecipientIsFk() throws Exception {
        String xml = marshall(Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE).getURL(), Charsets.UTF_8));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        when(registerMedicalCertificateClient.registerMedicalCertificate(
            any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(xml, "logicalAddress", "FK");

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(Mockito.eq(address), any(RegisterMedicalCertificateType.class));
    }

    @Test
    public void testSendMinimalCertificateWhenRecipientIsFk() throws Exception {
        String xml = marshall(Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE_MINIMAL).getURL(), Charsets.UTF_8));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        when(registerMedicalCertificateClient.registerMedicalCertificate(
            any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(xml, "logicalAddress", "FKASSA");

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(eq(address), any(RegisterMedicalCertificateType.class));
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndBadCertificateThenThrowException() throws Exception {

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(null, "logicalAddress", "FKASSA");
    }

    @Test
    public void whenFkIsRecipientThenSetCodeSystemToICD10() throws Exception {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request = fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);

        assertEquals("ICD-10", request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().getCodeSystemName());
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndNotSmittskyddAndNoMedicinsktTillstandThenThrowException() throws Exception {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request.getLakarutlatande().setMedicinsktTillstand(null);

        fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndNotSmittskyddAndNoTillstandskodThenThrowException() throws Exception {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(null);

        fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);
    }

    @Test
    public void getAdditionalInfoFromUtlatandeTest() throws Exception {
        Fk7263Utlatande utlatande = getUtlatandeFromFile();
        Intyg intyg = UtlatandeToIntyg.convert(utlatande);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertEquals("2011-01-26 - 2011-05-31", result);
    }

    @Test
    public void getAdditionalInfoOneTimePeriodTest() throws ModuleException {
        final String fromString = "2015-12-12";
        final String toString = "2016-03-02";
        LocalDate from = LocalDate.parse(fromString);
        LocalDate to = LocalDate.parse(toString);
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        Svar s = new Svar();
        s.setId("32");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertEquals(fromString + " - " + toString, result);
    }

    @Test
    public void getAdditionalInfoMultiplePeriodsTest() throws ModuleException {
        final String fromString = "2015-12-12";
        final String middleDate1 = "2015-12-13";
        final String middleDate2 = "2015-12-14";
        final String middleDate3 = "2015-12-15";
        final String middleDate4 = "2015-12-16";
        final String toString = "2016-03-02";
        LocalDate from = LocalDate.parse(fromString);
        LocalDate to = LocalDate.parse(toString);
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        Svar s = new Svar();
        s.setId("32");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(LocalDate.parse(middleDate2), LocalDate.parse(middleDate3)));
        s.getDelsvar().add(delsvar);
        Svar s2 = new Svar();
        s2.setId("32");
        Delsvar delsvar2 = new Delsvar();
        delsvar2.setId("32.2");
        delsvar2.getContent().add(aDatePeriod(LocalDate.parse(middleDate4), to));
        s2.getDelsvar().add(delsvar2);
        Svar s3 = new Svar();
        s3.setId("32");
        Delsvar delsvar3 = new Delsvar();
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
    public void getAdditionalInfoSvarNotFoundTest() throws ModuleException {
        final String fromString = "2015-12-12";
        final String toString = "2016-03-02";
        LocalDate from = LocalDate.parse(fromString);
        LocalDate to = LocalDate.parse(toString);
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        Svar s = new Svar();
        s.setId("30"); // wrong SvarId
        Delsvar delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertNull(result);
    }

    @Test
    public void getAdditionalInfoDelSvarNotFoundTest() throws ModuleException {
        final String fromString = "2015-12-12";
        final String toString = "2016-03-02";
        LocalDate from = LocalDate.parse(fromString);
        LocalDate to = LocalDate.parse(toString);
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        Svar s = new Svar();
        s.setId("32");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("32.1"); // wrong delsvarId
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertNull(result);
    }

    @Test
    public void testRegisterCertificateAlreadyExists() throws Exception {
        String json = Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE_MINIMAL).getURL(), Charsets.UTF_8);

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
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
    public void testRegisterCertificateGenericInfoResult() throws Exception {
        String json = Resources.toString(new ClassPathResource(TESTFILE_UTLATANDE_MINIMAL).getURL(), Charsets.UTF_8);

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
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

    private Fk7263Utlatande getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
            TESTFILE_UTLATANDE).getFile(), Fk7263Utlatande.class);
    }

    private String toJsonString(Fk7263Utlatande utlatande) throws ModuleException {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        return writer.toString();
    }

    private CreateDraftCopyHolder createDraftCopyHolder(Patient patient) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("hsaId0");
        vardgivare.setVardgivarnamn("vardgivare");
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("hsaId1");
        vardenhet.setEnhetsnamn("namn");
        vardenhet.setVardgivare(vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal();
        hosPersonal.setPersonId("Id1");
        hosPersonal.setFullstandigtNamn("Grodan Boll");
        hosPersonal.setForskrivarKod("forskrivarkod");
        hosPersonal.getBefattningar().add("befattning");
        hosPersonal.setVardenhet(vardenhet);

        CreateDraftCopyHolder holder = new CreateDraftCopyHolder("Id1", hosPersonal);

        if (patient != null) {
            holder.setPatient(patient);
        }

        return holder;
    }

    private Patient createUpdatedPatient() {
        Patient updatedPatient = new Patient();
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
        return Personnummer.createPersonnummer(civicRegistrationNumber).get();
    }

    private String marshall(String jsonString) throws Exception {
        Fk7263Utlatande internal = objectMapper.readValue(jsonString, Fk7263Utlatande.class);
        RegisterMedicalCertificateType external = InternalToTransport.getJaxbObject(internal);
        JAXBElement<RegisterMedicalCertificateType> el =
            new se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3
                .ObjectFactory().createRegisterMedicalCertificate(external);
        return XmlMarshallerHelper.marshal(el);
    }

}
