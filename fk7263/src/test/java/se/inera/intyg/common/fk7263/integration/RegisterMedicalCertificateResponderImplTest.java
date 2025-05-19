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
package se.inera.intyg.common.fk7263.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import iso.v21090.dt.v1.CD;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import java.time.LocalDate;
import java.util.Optional;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ErrorIdEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.common.fk7263.model.converter.TransportToInternal;
import se.inera.intyg.common.fk7263.model.converter.util.ConverterUtil;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.rest.Fk7263ModuleApi;
import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMappingConfigLoader;
import se.inera.intyg.common.support.modules.converter.mapping.CareProviderMapperUtil;
import se.inera.intyg.common.support.modules.support.ModuleEntryPoint;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {CareProviderMappingConfigLoader.class, CareProviderMapperUtil.class, TransportToInternal.class})
 class RegisterMedicalCertificateResponderImplTest {

    @Mock
    private ModuleEntryPoint moduleEntryPoint = mock(ModuleEntryPoint.class);

    @Mock
    private Fk7263ModuleApi moduleRestApi = mock(Fk7263ModuleApi.class);

    @Mock
    private ModuleContainerApi moduleContainer = mock(ModuleContainerApi.class);

    private RegisterMedicalCertificateType request;
    private String xml;
    private Fk7263Utlatande utlatande;
    private CertificateHolder certificateHolder;

    @InjectMocks
    private RegisterMedicalCertificateResponderImpl responder = new RegisterMedicalCertificateResponderImpl();

    @BeforeEach
     void initializeResponder() throws JAXBException {
        responder.initializeJaxbContext();
    }

    @BeforeEach
     void prepareRequest() throws Exception {

        ClassPathResource file = new ClassPathResource(
            "RegisterMedicalCertificateResponderImplTest/fk7263.xml");

        JAXBContext context = JAXBContext.newInstance(RegisterMedicalCertificateType.class);
        JAXBElement<RegisterMedicalCertificateType> registerMedicalCertificate = context.createUnmarshaller().unmarshal(
            new StreamSource(file.getInputStream()), RegisterMedicalCertificateType.class);
        request = registerMedicalCertificate.getValue();

        xml = Resources.toString(file.getURL(), Charsets.UTF_8);
        utlatande = TransportToInternal.convert(request.getLakarutlatande());
        certificateHolder = ConverterUtil.toCertificateHolder(utlatande);
        certificateHolder.setOriginalCertificate(xml);
    }

    @Test
     void testReceiveCertificate() throws Exception {

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateTomtTypAvUtlatande() throws Exception {
        request.getLakarutlatande().setTypAvUtlatande("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknarTypAvUtlatande() throws Exception {
        request.getLakarutlatande().setTypAvUtlatande(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateGodtyckligtTypAvUtlatande() throws Exception {
        request.getLakarutlatande().setTypAvUtlatande("godtycklig string");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateUtanAktivitetsbegransningFalt5() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateUtanPrognosangivelseFalt10() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream().filter(ft -> ft.getArbetsformaga() != null)
            .forEach(ft -> ft.getArbetsformaga().setPrognosangivelse(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadNedsattningsgrad() {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getArbetsformaga() != null && !ft.getArbetsformaga().getArbetsformagaNedsattning().isEmpty())
            .forEach(ft -> ft.getArbetsformaga().getArbetsformagaNedsattning().stream().forEach(n -> n.setNedsattningsgrad(null)));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsformaganedsattning element found 8b!.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatSigneringsdatum() {
        request.getLakarutlatande().setSigneringsdatum(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Field 14: No signeringsDatum found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatSkickatDatum() {
        request.getLakarutlatande().setSkickatDatum(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Header: No or wrong skickatDatum found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatPersonId() throws Exception {
        request.getLakarutlatande().getPatient().setPersonId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No Patient Id found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtPersonId() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No Patient Id found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatReferensdatum() throws Exception {
        request.getLakarutlatande().getReferens().stream().forEach(r -> r.setDatum(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Field 4: Referens is missing datum\n" +
            "Validation Error:Field 4: Referens is missing datum\n" +
            "Validation Error:No or wrong date for referens - journal found!\n" +
            "Validation Error:No or wrong date for referens - annat found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatReferensdatumSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getReferens().stream().forEach(r -> r.setDatum(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Field 4: Referens is missing datum\n" +
            "Validation Error:Field 4: Referens is missing datum", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatIntygId() throws Exception {
        request.getLakarutlatande().setLakarutlatandeId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Head: Utlatande Id is mandatory!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    // // INTYG-4086, namn skall ej längre skickas med.
//    @Test
//     void testRegisterMedicalCertificateSaknadPatient() throws Exception {
//        request.getLakarutlatande().setPatient(null);
//        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);
//
//        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
//        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
//        assertEquals("Validation Error(s) found: Validation Error:No Patient element found!", response.getResult().getErrorText());
//
//        Mockito.verifyZeroInteractions(moduleContainer);
//    }

    @Test
     void testRegisterMedicalCertificateFelaktigPersonIdKod() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateFelaktigtPersonnr() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setExtension("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificatePersonnrUtanSekelsiffror() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setExtension("121212-1212");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificatePersonnrUtanBindestreckKorrigeras() throws Exception {
        request.getLakarutlatande().getPatient().getPersonId().setExtension("191212121212");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        ArgumentCaptor<CertificateHolder> certificateCaptor = ArgumentCaptor.forClass(CertificateHolder.class);
        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(certificateCaptor.capture());
        assertEquals("191212121212", certificateCaptor.getValue().getCivicRegistrationNumber().getPersonnummer());
    }

    @Test
     void testRegisterMedicalCertificateSaknadHoSPersonal() throws Exception {
        request.getLakarutlatande().setSkapadAvHosPersonal(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No SkapadAvHosPersonal element found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadPersonalId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().setPersonalId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No personal-id found!\n" +
            "Validation Error:Wrong o.i.d. for personalId! Should be 1.2.752.129.2.1.4.1", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateFelaktigPersonalIdKod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getPersonalId().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d. for personalId! Should be 1.2.752.129.2.1.4.1",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtPersonalId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getPersonalId().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No personal-id found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatPersonalnamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().setFullstandigtNamn(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No skapadAvHosPersonal fullstandigtNamn found.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtPersonalnamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().setFullstandigtNamn("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No skapadAvHosPersonal fullstandigtNamn found.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadEnhet() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().setEnhet(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhet element found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatEnhetId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setEnhetsId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhets-id found!\n" +
            "Validation Error:Wrong o.i.d. for enhetsId! Should be 1.2.752.129.2.1.4.1", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateFelaktigEnhetIdKod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d. for enhetsId! Should be 1.2.752.129.2.1.4.1",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtEnhetId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getEnhetsId().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhets-id found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatEnhetnamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setEnhetsnamn(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhetsnamn found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtEnhetnamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setEnhetsnamn("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No enhetsnamn found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadEnhetpostaddress() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostadress(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postadress found for enhet!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomEnhetpostaddress() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostadress("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postadress found for enhet!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatEnhetpostnummer() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostnummer(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postnummer found for enhet!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtEnhetpostnummer() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostnummer("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postnummer found for enhet!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatEnhetpostort() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostort(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postort found for enhet!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtEnhetpostort() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setPostort("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No postort found for enhet!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatEnhettelefonnummer() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setTelefonnummer(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No telefonnummer found for enhet!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtEnhettelefonnummer() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setTelefonnummer("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No telefonnummer found for enhet!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadVardgivare() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setVardgivare(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardgivare element found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadVardgivareId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().setVardgivareId(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardgivare-id found!\n" +
            "Validation Error:Wrong o.i.d. for vardgivareId! Should be 1.2.752.129.2.1.4.1", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateFelaktigVardgivareIdKod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivareId().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d. for vardgivareId! Should be 1.2.752.129.2.1.4.1",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtVardgivareId() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().getVardgivareId().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardgivare-id found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknatVardgivarenamn() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getVardgivare().setVardgivarnamn(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardgivarenamn found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadArbetsplatskod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().setArbetsplatskod(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsplatskod element found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateFelaktigArbetsplatskodKod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setRoot("invalid");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Wrong o.i.d for arbetsplatskod, should be 1.2.752.29.4.71",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomArbetsplatskod() throws Exception {
        request.getLakarutlatande().getSkapadAvHosPersonal().getEnhet().getArbetsplatskod().setExtension("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsplatskod found!", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadFunktionstillstandAktivitet() throws Exception {
        Optional<FunktionstillstandType> kroppsfunktion = request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).findFirst();
        request.getLakarutlatande().getFunktionstillstand().clear();
        request.getLakarutlatande().getFunktionstillstand().add(kroppsfunktion.get());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No funktionstillstand - aktivitet element found!\n" +
            "Validation Error:No arbetsformaga element found for field 8a!\n" +
            "Validation Error:No arbetsformaga element found 8b!.", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadMedicinsktTillstand() throws Exception {
        request.getLakarutlatande().setMedicinsktTillstand(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No medicinsktTillstand element found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadDiagnoskod() throws Exception {
        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No tillstandskod in medicinsktTillstand found!\n" +
                "Validation Error:Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10 OR KSH97P",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadDiagnoskodSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().setMedicinsktTillstand(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadDiagnoskodSystem() throws Exception {
        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(new CD());
        request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().setCode("M25");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10 OR KSH97P",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateFelaktigDiagnoskodSystem() throws Exception {
        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(new CD());
        request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().setCodeSystem("invalid");
        request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().setCode("M25");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10 OR KSH97P",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomtBedomtTillstandBeskrivning() throws Exception {
        request.getLakarutlatande().getBedomtTillstand().setBeskrivning("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadBedomtTillstandBeskrivning() throws Exception {
        request.getLakarutlatande().getBedomtTillstand().setBeskrivning(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Beskrivning must be set for Falt3 Aktuellt Sjukdomsforlopp",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadFunktionstillstandKroppsfunktion() throws Exception {
        Optional<FunktionstillstandType> aktivitet = request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET).findFirst();
        request.getLakarutlatande().getFunktionstillstand().clear();
        request.getLakarutlatande().getFunktionstillstand().add(aktivitet.get());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No funktionstillstand - kroppsfunktion element found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadFunktionstillstandKroppsfunktionSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        Optional<FunktionstillstandType> aktivitet = request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET).findFirst();
        request.getLakarutlatande().getFunktionstillstand().clear();
        request.getLakarutlatande().getFunktionstillstand().add(aktivitet.get());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateTomFunktionstillstandKroppsfunktionBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).forEach(ft -> ft.setBeskrivning(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No beskrivning in funktionstillstand - kroppsfunktion found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomFunktionstillstandKroppsfunktionBeskrivningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).forEach(ft -> ft.setBeskrivning(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadFunktionstillstandKroppsfunktionBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).forEach(ft -> ft.setBeskrivning(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No beskrivning in funktionstillstand - kroppsfunktion found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadFunktionstillstandKroppsfunktionBeskrivningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.KROPPSFUNKTION).forEach(ft -> ft.setBeskrivning(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadeVardkontakterReferenser() throws Exception {
        request.getLakarutlatande().getVardkontakt().clear();
        request.getLakarutlatande().getReferens().clear();
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No vardkontakt or referens element found ! At least one must be set!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadeVardkontakterReferenserSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getVardkontakt().clear();
        request.getLakarutlatande().getReferens().clear();
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadeVardkontaktTid() throws Exception {
        request.getLakarutlatande().getVardkontakt().stream()
            .filter(vk -> vk.getVardkontakttyp() == Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN)
            .forEach(vk -> vk.setVardkontaktstid(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No or wrong date for vardkontakt - min undersokning av patienten found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomAktivitetsbegransningBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET).forEach(ft -> ft.setBeskrivning(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadBeskrivningVidRekommendationOvrigt() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.OVRIGT);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Ovrigt",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadBeskrivningVidRekommendationOvrigtSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.OVRIGT);
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(1).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Ovrigt",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomBeskrivningVidRekommendationOvrigt() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.OVRIGT);
        request.getLakarutlatande().getAktivitet().get(0).setBeskrivning("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadBeskrivningVidBehandlingVarden() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0)
            .setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående åtgärd inom sjukvården",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadBeskrivningVidBehandlingVardenSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0)
            .setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(1).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående åtgärd inom sjukvården",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomBeskrivningVidBehandlingVarden() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0)
            .setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        request.getLakarutlatande().getAktivitet().get(0).setBeskrivning("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadBeskrivningVidBehandlingAnnan() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående annan atgärd",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadBeskrivningVidBehandningAnnanSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(1).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående annan atgärd",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomBeskrivningVidBehandlingAnnan() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        request.getLakarutlatande().getAktivitet().get(0).setBeskrivning("");
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadKommentarReferensAnnat() throws Exception {
        request.getLakarutlatande().getReferens().clear();
        request.getLakarutlatande().getReferens().add(new ReferensType());
        request.getLakarutlatande().getReferens().get(0).setReferenstyp(Referenstyp.ANNAT);
        request.getLakarutlatande().getReferens().get(0).setDatum(LocalDate.now());
        request.getLakarutlatande().setKommentar(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Upplysningar should contain data as field 4 or fields 10 is checked.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadKommentarPrognosGarEjAttBedomma() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().setPrognosangivelse(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA));
        request.getLakarutlatande().setKommentar(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Upplysningar should contain data as field 4 or fields 10 is checked.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadArbetsformaga() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.setArbetsformaga(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsformaga element found for field 8a!\n" +
            "Validation Error:No arbetsformaga element found 8b!.", response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadSysselsattning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getSysselsattning().clear());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals(
            "Validation Error(s) found: Validation Error:No sysselsattning element found for field 8a! Nuvarande arbete, arbestloshet or foraldraledig should be set.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadSysselsattningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getSysselsattning().clear());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadArbetsuppgift() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().setArbetsuppgift(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsuppgift element found when arbete set in field 8a!.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadArbetsuppgiftSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().setArbetsuppgift(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadArbetsuppgiftBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No typAvArbetsuppgift element found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadArbetsuppgiftBeskrivningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No typAvArbetsuppgift element found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomArbetsuppgiftBeskrivning() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No typAvArbetsuppgift found when arbete set in field 8a!.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateTomArbetsuppgiftBeskrivningSmL() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getArbetsuppgift().setTypAvArbetsuppgift(""));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateSaknadVaraktighet() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getArbetsformagaNedsattning().clear());
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No arbetsformaganedsattning element found 8b!.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadVaraktighetFrom() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetFrom(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No or wrong date for helt nedsatt from date found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateSaknadVaraktighetTom() throws Exception {
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetTom(null));
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:No or wrong date for helt nedsatt tom date found!",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testRegisterMedicalCertificateOverlappandeVaraktigheter() throws Exception {
        final LocalDate from = LocalDate.now().minusDays(1);
        final LocalDate to = LocalDate.now().plusDays(1);
        request.getLakarutlatande().getFunktionstillstand().stream()
            .filter(ft -> ft.getTypAvFunktionstillstand() == TypAvFunktionstillstand.AKTIVITET)
            .forEach(ft -> {
                ft.getArbetsformaga().getArbetsformagaNedsattning().clear();
                ft.getArbetsformaga().getArbetsformagaNedsattning().add(new ArbetsformagaNedsattningType());
                ft.getArbetsformaga().getArbetsformagaNedsattning().add(new ArbetsformagaNedsattningType());
                ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
                ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetFrom(from);
                ft.getArbetsformaga().getArbetsformagaNedsattning().get(0).setVaraktighetTom(to);
                ft.getArbetsformaga().getArbetsformagaNedsattning().get(1).setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_2);
                ft.getArbetsformaga().getArbetsformagaNedsattning().get(1).setVaraktighetFrom(from);
                ft.getArbetsformaga().getArbetsformagaNedsattning().get(1).setVaraktighetTom(to);
            });
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());

        Mockito.verify(moduleContainer, Mockito.only()).certificateReceived(any(CertificateHolder.class));
    }

    @Test
     void testRegisterMedicalCertificateMultiplaRessatt() throws Exception {
        request.getLakarutlatande().getAktivitet().clear();
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(0).setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
        request.getLakarutlatande().getAktivitet().add(new AktivitetType());
        request.getLakarutlatande().getAktivitet().get(1)
            .setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Validation Error(s) found: Validation Error:Only one forandrat ressatt could be set for field 11.",
            response.getResult().getErrorText());

        Mockito.verifyNoInteractions(moduleContainer);
    }

    @Test
     void testWithExistingCertificate() throws Exception {
        Mockito.doThrow(new CertificateAlreadyExistsException(request.getLakarutlatande().getLakarutlatandeId())).when(moduleContainer)
            .certificateReceived(any(CertificateHolder.class));

        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);
        assertEquals(ResultCodeEnum.INFO, response.getResult().getResultCode());
    }

    @Test
     void testWithInvalidCertificate() throws Exception {
        request.getLakarutlatande().setSkapadAvHosPersonal(null);
        RegisterMedicalCertificateResponseType response = responder.registerMedicalCertificate(null, request);
        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
    }
}
