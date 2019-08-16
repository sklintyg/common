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
package se.inera.intyg.common.fk7263.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateRequestType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getmedicalcertificate.v1.GetMedicalCertificateResponseType;
import se.inera.intyg.common.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.v1.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultCodeType;

@RunWith(MockitoJUnitRunner.class)
public class GetMedicalCertificateResponderImplTest {

    private static final String PERSON_ID = "19121212-1212";
    private static final String INTYG_ID = "123456";
    private static final String HSVARD_RECIPIENT_ID = "HSVARD";
    private static final String INVANA_RECIPIENT_ID = "INVANA";

    @InjectMocks
    private GetMedicalCertificateResponderImpl responder;

    @Mock
    private ModuleContainerApi moduleContainer;

    @Test
    public void getMedicalCertificate() throws Exception {
        when(moduleContainer.getCertificate(INTYG_ID, createPnr(PERSON_ID), false)).thenReturn(createCertificateHolder());

        GetMedicalCertificateResponseType response = responder.getMedicalCertificate(null, createGetMedicalCertificateRequest());

        verify(moduleContainer).getCertificate(INTYG_ID, createPnr(PERSON_ID), false);
        assertEquals(ResultCodeType.OK, response.getResult().getResultCode());
        assertNotNull(response.getMeta());
        assertEquals(INTYG_ID, response.getMeta().getCertificateId());
        assertEquals(Fk7263EntryPoint.MODULE_ID, response.getMeta().getCertificateType());
        assertNotNull(response.getLakarutlatande());
        assertEquals(INTYG_ID, response.getLakarutlatande().getLakarutlatandeId());
        assertEquals(createPnr(PERSON_ID).getPersonnummerWithDash(),
            response.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void getMedicalCertificateWithUnknownCertificateId() throws Exception {
        Personnummer pnr = createPnr(PERSON_ID);
        when(moduleContainer.getCertificate(INTYG_ID, pnr, false)).thenThrow(new InvalidCertificateException("123456", pnr));

        GetMedicalCertificateResponseType response = responder.getMedicalCertificate(null, createGetMedicalCertificateRequest());

        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Certificate '123456' does not exist for user '9a8b138a666f84da32e9383b49a15f46f6e08d2c492352aa0dfcc3f993773b0d'",
            response.getResult().getResultText());
        assertNull(response.getMeta());
        assertNull(response.getLakarutlatande());
    }

    @Test
    public void getMedicalCertificateWrongType() throws Exception {
        CertificateHolder certificateHolder = new CertificateHolder();
        certificateHolder.setType("luse");
        when(moduleContainer.getCertificate(INTYG_ID, createPnr(PERSON_ID), false)).thenReturn(certificateHolder);

        GetMedicalCertificateResponseType response = responder.getMedicalCertificate(null, createGetMedicalCertificateRequest());

        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("Certificate '123456' does not exist for user '9a8b138a666f84da32e9383b49a15f46f6e08d2c492352aa0dfcc3f993773b0d'",
            response.getResult().getResultText());
        assertNull(response.getMeta());
        assertNull(response.getLakarutlatande());
    }

    @Test
    public void getMedicalCertificateWrongCivicRegistrationNumber() throws Exception {
        CertificateHolder certificateHolder = new CertificateHolder();
        certificateHolder.setType("fk7263");
        certificateHolder.setCivicRegistrationNumber(createPnr("19010101-0101"));
        when(moduleContainer.getCertificate(INTYG_ID, createPnr(PERSON_ID), false)).thenReturn(certificateHolder);

        GetMedicalCertificateResponseType response = responder.getMedicalCertificate(null, createGetMedicalCertificateRequest());

        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, response.getResult().getErrorId());
        assertEquals("nationalIdentityNumber mismatch", response.getResult().getResultText());
        assertNull(response.getMeta());
        assertNull(response.getLakarutlatande());
    }

    @Test
    public void getMedicalCertificateDeletedByCareGiverForCare() throws Exception {
        CertificateHolder certificateHolder = createCertificateHolder();
        certificateHolder.setDeletedByCareGiver(true);
        when(moduleContainer.getCertificate(INTYG_ID, createPnr(PERSON_ID), false)).thenReturn(certificateHolder);

        GetMedicalCertificateRequestType request = createGetMedicalCertificateRequest();
        request.setPart(HSVARD_RECIPIENT_ID);
        GetMedicalCertificateResponseType response = responder.getMedicalCertificate(null, request);

        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.APPLICATION_ERROR, response.getResult().getErrorId());
        assertEquals("Certificate '123456' has been deleted by care giver", response.getResult().getResultText());
        assertNull(response.getMeta());
        assertNull(response.getLakarutlatande());
    }

    @Test
    public void getMedicalCertificateDeletedByCareGiverForCitizen() throws Exception {
        CertificateHolder certificateHolder = createCertificateHolder();
        certificateHolder.setDeletedByCareGiver(true);
        when(moduleContainer.getCertificate(INTYG_ID, createPnr(PERSON_ID), false)).thenReturn(certificateHolder);

        GetMedicalCertificateRequestType request = createGetMedicalCertificateRequest();
        request.setPart(INVANA_RECIPIENT_ID);

        GetMedicalCertificateResponseType response = responder.getMedicalCertificate(null, request);

        verify(moduleContainer).getCertificate(INTYG_ID, createPnr(PERSON_ID), false);
        assertEquals(ResultCodeType.OK, response.getResult().getResultCode());
        assertNotNull(response.getMeta());
        assertEquals(INTYG_ID, response.getMeta().getCertificateId());
        assertEquals(Fk7263EntryPoint.MODULE_ID, response.getMeta().getCertificateType());
        assertNotNull(response.getLakarutlatande());
        assertEquals(INTYG_ID, response.getLakarutlatande().getLakarutlatandeId());
        assertEquals(createPnr(PERSON_ID).getPersonnummerWithDash(),
            response.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void getMedicalCertificateRevoked() throws Exception {
        CertificateHolder certificate = createCertificateHolder();
        certificate.setRevoked(true);
        when(moduleContainer.getCertificate(INTYG_ID, createPnr(PERSON_ID), false)).thenReturn(certificate);

        GetMedicalCertificateResponseType response = responder.getMedicalCertificate(null, createGetMedicalCertificateRequest());

        verify(moduleContainer).getCertificate(INTYG_ID, createPnr(PERSON_ID), false);
        assertEquals(ResultCodeType.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdType.REVOKED, response.getResult().getErrorId());
        assertEquals("Certificate '123456' has been revoked", response.getResult().getResultText());
        assertNotNull(response.getMeta());
        assertEquals(INTYG_ID, response.getMeta().getCertificateId());
        assertEquals(Fk7263EntryPoint.MODULE_ID, response.getMeta().getCertificateType());
        assertNotNull(response.getLakarutlatande());
        assertEquals(INTYG_ID, response.getLakarutlatande().getLakarutlatandeId());
        assertEquals(createPnr(PERSON_ID).getPersonnummerWithDash(),
            response.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    @Test
    public void getMedicalCertificateNoCivicRegistrationNumber() throws Exception {
        when(moduleContainer.getCertificate(INTYG_ID, null, false)).thenReturn(createCertificateHolder());

        GetMedicalCertificateRequestType request = createGetMedicalCertificateRequest();
        request.setNationalIdentityNumber(null);
        GetMedicalCertificateResponseType response = responder.getMedicalCertificate(null, request);

        verify(moduleContainer).getCertificate(INTYG_ID, null, false);
        assertEquals(ResultCodeType.OK, response.getResult().getResultCode());
        assertNotNull(response.getMeta());
        assertEquals(INTYG_ID, response.getMeta().getCertificateId());
        assertEquals(Fk7263EntryPoint.MODULE_ID, response.getMeta().getCertificateType());
        assertNotNull(response.getLakarutlatande());
        assertEquals(INTYG_ID, response.getLakarutlatande().getLakarutlatandeId());
        assertEquals(createPnr(PERSON_ID).getPersonnummerWithDash(),
            response.getLakarutlatande().getPatient().getPersonId().getExtension());
    }

    private CertificateHolder createCertificateHolder() throws Exception {
        CertificateHolder certificate = new CertificateHolder();
        certificate.setId(INTYG_ID);
        certificate.setType("fk7263");
        certificate.setCivicRegistrationNumber(createPnr(PERSON_ID));
        String xmlFile = Resources.toString(new ClassPathResource("GetMedicalCertificateResponderImplTest/fk7263.xml").getURL(),
            Charsets.UTF_8);
        certificate.setOriginalCertificate(xmlFile);
        return certificate;
    }

    private GetMedicalCertificateRequestType createGetMedicalCertificateRequest() {
        GetMedicalCertificateRequestType parameters = new GetMedicalCertificateRequestType();
        parameters.setCertificateId(INTYG_ID);
        parameters.setNationalIdentityNumber(createPnr(PERSON_ID).getPersonnummer());
        parameters.setPart(INVANA_RECIPIENT_ID);
        return parameters;
    }

    private Personnummer createPnr(String civicRegistrationNumber) {
        return Personnummer.createPersonnummer(civicRegistrationNumber).get();
    }

}
