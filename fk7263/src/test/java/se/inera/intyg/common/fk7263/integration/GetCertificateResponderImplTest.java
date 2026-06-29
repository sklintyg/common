/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.ERROR;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.INFO;
import static se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum.OK;

import com.google.common.io.Resources;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificateresponder.v1.GetCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ErrorIdEnum;
import se.inera.intyg.common.fk7263.model.converter.util.ConverterUtil;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.rest.Fk7263ModuleApi;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;

/**
 * @author andreaskaltenbach
 */
@ExtendWith(MockitoExtension.class)
class GetCertificateResponderImplTest {

  private static final String CIVIC_REGISTRATION_NUMBER = "19350108-1234";
  private static final String CERTIFICATE_ID = "123456";

  private final CustomObjectMapper objectMapper = new CustomObjectMapper();

  @InjectMocks private GetCertificateResponderImpl responder;

  @Mock private ModuleContainerApi moduleContainer;

  @Mock private Fk7263ModuleApi moduleRestApi;

  @Test
  void getCertificate() throws Exception {
    String document =
        Resources.toString(
            new ClassPathResource("GetCertificateResponderImplTest/maximalt-fk7263-internal.json")
                .getURL(),
            StandardCharsets.UTF_8);
    Fk7263Utlatande utlatande = objectMapper.readValue(document, Fk7263Utlatande.class);
    CertificateHolder certificate = ConverterUtil.toCertificateHolder(utlatande);
    String xmlFile =
        Resources.toString(
            new ClassPathResource("GetCertificateResponderImplTest/fk7263.xml").getURL(),
            StandardCharsets.UTF_8);
    certificate.setOriginalCertificate(xmlFile);

    when(moduleContainer.getCertificate(CERTIFICATE_ID, createPnr(CIVIC_REGISTRATION_NUMBER), true))
        .thenReturn(certificate);

    GetCertificateRequestType parameters =
        createGetCertificateRequest(CIVIC_REGISTRATION_NUMBER, CERTIFICATE_ID);

    GetCertificateResponseType response = responder.getCertificate(null, parameters);

    verify(moduleContainer)
        .getCertificate(CERTIFICATE_ID, createPnr(CIVIC_REGISTRATION_NUMBER), true);
    verify(moduleContainer)
        .logCertificateRetrieved(
            certificate.getId(), certificate.getType(), certificate.getCareUnitId(), null);
    assertNotNull(response.getMeta());
    assertEquals(OK, response.getResult().getResultCode());
  }

  @Test
  void getCertificateWithUnknownCertificateId() throws Exception {

    when(moduleContainer.getCertificate(CERTIFICATE_ID, createPnr(CIVIC_REGISTRATION_NUMBER), true))
        .thenThrow(new InvalidCertificateException("123456", null));

    GetCertificateRequestType parameters =
        createGetCertificateRequest(CIVIC_REGISTRATION_NUMBER, CERTIFICATE_ID);

    GetCertificateResponseType response = responder.getCertificate(null, parameters);

    assertNull(response.getMeta());
    assertNull(response.getCertificate());
    assertEquals(ERROR, response.getResult().getResultCode());
    assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
    assertEquals("Unknown certificate ID: 123456", response.getResult().getErrorText());
  }

  @Test
  void getRevokedCertificate() throws Exception {
    String document =
        Resources.toString(
            new ClassPathResource("GetCertificateResponderImplTest/maximalt-fk7263-internal.json")
                .getURL(),
            StandardCharsets.UTF_8);
    Fk7263Utlatande utlatande = objectMapper.readValue(document, Fk7263Utlatande.class);
    CertificateHolder certificate = ConverterUtil.toCertificateHolder(utlatande);
    certificate.setRevoked(true);

    when(moduleContainer.getCertificate(CERTIFICATE_ID, createPnr(CIVIC_REGISTRATION_NUMBER), true))
        .thenReturn(certificate);

    GetCertificateRequestType parameters =
        createGetCertificateRequest(CIVIC_REGISTRATION_NUMBER, CERTIFICATE_ID);

    GetCertificateResponseType response = responder.getCertificate(null, parameters);

    assertNull(response.getMeta());
    assertNull(response.getCertificate());
    assertEquals(INFO, response.getResult().getResultCode());
    assertEquals("Certificate '123456' has been revoked", response.getResult().getInfoText());
  }

  @Test
  void getCertificateWithNullCertificateId() {
    GetCertificateRequestType request = createGetCertificateRequest("", null);
    GetCertificateResponseType response = responder.getCertificate(null, request);
    assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
    verifyNoInteractions(moduleRestApi);
  }

  @Test
  void getCertificateWithBlankCertificateId() {
    GetCertificateRequestType request = createGetCertificateRequest("", "");
    GetCertificateResponseType response = responder.getCertificate(null, request);
    assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
    verifyNoInteractions(moduleRestApi);
  }

  @Test
  void getCertificateWithNullIdentityNumber() {
    GetCertificateRequestType request = createGetCertificateRequest(null, CERTIFICATE_ID);
    GetCertificateResponseType response = responder.getCertificate(null, request);
    assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
    verifyNoInteractions(moduleRestApi);
  }

  @Test
  void getCertificateWithBlankIdentityNumber() {
    GetCertificateRequestType request = createGetCertificateRequest("", CERTIFICATE_ID);
    GetCertificateResponseType response = responder.getCertificate(null, request);
    assertEquals(ErrorIdEnum.VALIDATION_ERROR, response.getResult().getErrorId());
    verifyNoInteractions(moduleRestApi);
  }

  private GetCertificateRequestType createGetCertificateRequest(
      String civicRegistrationNumber, String certificateId) {
    GetCertificateRequestType parameters = new GetCertificateRequestType();
    if (civicRegistrationNumber == null) {
      parameters.setNationalIdentityNumber(null);
    } else if (civicRegistrationNumber.isEmpty()) {
      parameters.setNationalIdentityNumber("");
    } else {
      parameters.setNationalIdentityNumber(createPnr(civicRegistrationNumber).getPersonnummer());
    }
    parameters.setCertificateId(certificateId);
    return parameters;
  }

  private Personnummer createPnr(String civicRegistrationNumber) {
    return Personnummer.createPersonnummer(civicRegistrationNumber).orElseThrow();
  }
}
