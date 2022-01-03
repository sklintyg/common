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
package se.inera.intyg.common.ts_diabetes.v2.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.integration.module.exception.InvalidCertificateException;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.CertificateStateHolder;
import se.inera.intyg.common.support.modules.support.api.ModuleContainerApi;
import se.inera.intyg.common.support.xml.XmlMarshallerHelper;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioFinder;
import se.inera.intyg.schemas.contract.Personnummer;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesResponseType;
import se.inera.intygstjanster.ts.services.GetTSDiabetesResponder.v1.GetTSDiabetesType;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.ObjectFactory;
import se.inera.intygstjanster.ts.services.RegisterTSDiabetesResponder.v1.RegisterTSDiabetesType;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.ErrorIdType;
import se.inera.intygstjanster.ts.services.v1.ResultCodeType;
import se.inera.intygstjanster.ts.services.v1.Status;

@RunWith(MockitoJUnitRunner.class)
public class GetTSDiabetesResponderImplTest {

    private final String LOGICAL_ADDRESS = "logicalAddress";
    private final String PNR_FJORTON = "19141214-1414";
    private final String PNR_TOLVAN = "19121212-1212";

    @Mock
    private ModuleContainerApi moduleContainer;

    @InjectMocks
    private GetTSDiabetesResponderImpl responder;

    @Test
    public void testGetTSDiabetes() throws Exception {
        final String intygId = "intygId";
        final String target = "target";
        final CertificateState state = CertificateState.RECEIVED;
        final LocalDateTime timestamp = LocalDateTime.now();
        final String additionalInfo = "additionalInfo";

        RegisterTSDiabetesType originalCertificate = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        Personnummer pnr = createPnr(PNR_TOLVAN);

        CertificateHolder certificate = new CertificateHolder();
        certificate.setId(intygId);
        certificate.setType("ts-diabetes");
        certificate.setCareUnitId("hsa-id-enheten");
        certificate.setCivicRegistrationNumber(pnr);
        certificate.setCertificateStates(Arrays.asList(new CertificateStateHolder(target, state, timestamp)));
        certificate.setOriginalCertificate(xmlToString(originalCertificate));
        certificate.setAdditionalInfo(additionalInfo);
        certificate.setDeleted(false);
        when(moduleContainer.getCertificate(intygId, pnr, false)).thenReturn(certificate);

        GetTSDiabetesType request = new GetTSDiabetesType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);

        GetTSDiabetesResponseType res = responder.getTSDiabetes(LOGICAL_ADDRESS, request);

        verify(moduleContainer).logCertificateRetrieved(certificate.getId(), certificate.getType(), certificate.getCareUnitId(), null);
        assertNotNull(res);
        assertEquals(ResultCodeType.OK, res.getResultat().getResultCode());
        assertEquals(originalCertificate.getIntyg().getIntygsId(), res.getIntyg().getIntygsId());
        assertNotNull(res.getMeta());
        assertEquals(additionalInfo, res.getMeta().getAdditionalInfo());
        assertEquals("true", res.getMeta().getAvailable());
        assertEquals(1, res.getMeta().getStatus().size());
        assertEquals(target, res.getMeta().getStatus().get(0).getTarget());
        assertEquals(timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), res.getMeta().getStatus().get(0).getTimestamp());
        assertEquals(Status.RECEIVED, res.getMeta().getStatus().get(0).getType());
    }

    @Test
    public void testGetTSDiabetesDeleted() throws Exception {
        final String intygId = "intygId";

        RegisterTSDiabetesType originalCertificate = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        Personnummer pnr = createPnr(PNR_TOLVAN);

        CertificateHolder certificate = new CertificateHolder();
        certificate.setCivicRegistrationNumber(pnr);
        certificate
            .setCertificateStates(Arrays.asList(new CertificateStateHolder("target", CertificateState.RECEIVED, LocalDateTime.now())));
        certificate.setOriginalCertificate(xmlToString(originalCertificate));
        certificate.setDeleted(true);
        when(moduleContainer.getCertificate(intygId, pnr, false)).thenReturn(certificate);

        GetTSDiabetesType request = new GetTSDiabetesType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);

        GetTSDiabetesResponseType res = responder.getTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.OK, res.getResultat().getResultCode());
        assertEquals(originalCertificate.getIntyg().getIntygsId(), res.getIntyg().getIntygsId());
        assertEquals("false", res.getMeta().getAvailable());
    }

    @Test
    public void testGetTSDiabetesNoPersonId() throws Exception {
        final String intygId = "intygId";

        RegisterTSDiabetesType originalCertificate = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();

        CertificateHolder certificate = new CertificateHolder();
        certificate
            .setCertificateStates(Arrays.asList(new CertificateStateHolder("target", CertificateState.RECEIVED, LocalDateTime.now())));
        certificate.setOriginalCertificate(xmlToString(originalCertificate));
        when(moduleContainer.getCertificate(intygId, null, false)).thenReturn(certificate);

        GetTSDiabetesType request = new GetTSDiabetesType();
        request.setIntygsId(intygId);

        GetTSDiabetesResponseType res = responder.getTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.OK, res.getResultat().getResultCode());
        assertEquals(originalCertificate.getIntyg().getIntygsId(), res.getIntyg().getIntygsId());
    }

    @Test
    public void testGetTSDiabetesNoCertificateId() throws Exception {
        GetTSDiabetesResponseType res = responder.getTSDiabetes(LOGICAL_ADDRESS, new GetTSDiabetesType());

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.APPLICATION_ERROR, res.getResultat().getErrorId());
        assertEquals("non-existing certificateId", res.getResultat().getResultText());
    }

    @Test
    public void testGetTSDiabetesPersonIdMismatch() throws Exception {
        final String intygId = "intygId";

        CertificateHolder certificate = new CertificateHolder();
        certificate.setCivicRegistrationNumber(createPnr(PNR_FJORTON));
        when(moduleContainer.getCertificate(intygId, createPnr(PNR_TOLVAN), false)).thenReturn(certificate);

        GetTSDiabetesType request = new GetTSDiabetesType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);

        GetTSDiabetesResponseType res = responder.getTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, res.getResultat().getErrorId());
        assertEquals("nationalIdentityNumber mismatch", res.getResultat().getResultText());
    }

    @Test
    public void testGetTSDiabetesDeletedByCareGiver() throws Exception {
        final String intygId = "intygId";

        CertificateHolder certificate = new CertificateHolder();
        certificate.setDeletedByCareGiver(true);
        when(moduleContainer.getCertificate(intygId, null, false)).thenReturn(certificate);

        GetTSDiabetesType request = new GetTSDiabetesType();
        request.setIntygsId(intygId);

        GetTSDiabetesResponseType res = responder.getTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.APPLICATION_ERROR, res.getResultat().getErrorId());
        assertEquals("Certificate 'intygId' has been deleted by care giver", res.getResultat().getResultText());
    }

    @Test
    public void testGetTSDiabetesRevoked() throws Exception {
        final String intygId = "intygId";
        final String target = "target";
        final CertificateState state = CertificateState.RECEIVED;
        final LocalDateTime timestamp = LocalDateTime.now();
        final String additionalInfo = "additionalInfo";

        RegisterTSDiabetesType originalCertificate = ScenarioFinder.getTransportScenario("valid-minimal").asTransportModel();
        Personnummer pnr = createPnr(PNR_TOLVAN);

        CertificateHolder certificate = new CertificateHolder();
        certificate.setCivicRegistrationNumber(pnr);
        certificate.setCertificateStates(Arrays.asList(new CertificateStateHolder(target, state, timestamp)));
        certificate.setOriginalCertificate(xmlToString(originalCertificate));
        certificate.setAdditionalInfo(additionalInfo);
        certificate.setDeleted(false);
        certificate.setRevoked(true);
        when(moduleContainer.getCertificate(intygId, pnr, false)).thenReturn(certificate);

        GetTSDiabetesType request = new GetTSDiabetesType();
        request.setIntygsId(intygId);
        request.setPersonId(new II());
        request.getPersonId().setExtension(PNR_TOLVAN);

        GetTSDiabetesResponseType res = responder.getTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.REVOKED, res.getResultat().getErrorId());
        assertEquals("Certificate 'intygId' has been revoked", res.getResultat().getResultText());
        assertEquals(originalCertificate.getIntyg().getIntygsId(), res.getIntyg().getIntygsId());
        assertNotNull(res.getMeta());
        assertEquals(additionalInfo, res.getMeta().getAdditionalInfo());
        assertEquals("true", res.getMeta().getAvailable());
        assertEquals(1, res.getMeta().getStatus().size());
        assertEquals(target, res.getMeta().getStatus().get(0).getTarget());
        assertEquals(timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), res.getMeta().getStatus().get(0).getTimestamp());
        assertEquals(Status.RECEIVED, res.getMeta().getStatus().get(0).getType());
    }

    @Test
    public void testGetTSDiabetesInvalidCertificate() throws Exception {
        final String intygId = "intygId";

        when(moduleContainer.getCertificate(intygId, null, false)).thenThrow(new InvalidCertificateException(intygId, null));

        GetTSDiabetesType request = new GetTSDiabetesType();
        request.setIntygsId(intygId);

        GetTSDiabetesResponseType res = responder.getTSDiabetes(LOGICAL_ADDRESS, request);

        assertNotNull(res);
        assertEquals(ResultCodeType.ERROR, res.getResultat().getResultCode());
        assertEquals(ErrorIdType.VALIDATION_ERROR, res.getResultat().getErrorId());
        assertEquals("Unknown certificate ID: intygId", res.getResultat().getResultText());
    }

    private Personnummer createPnr(String pnr) {
        return Personnummer.createPersonnummer(pnr).get();
    }

    private String xmlToString(RegisterTSDiabetesType registerTsDiabetes) throws JAXBException {
        JAXBElement<RegisterTSDiabetesType> el = new ObjectFactory().createRegisterTSDiabetes(registerTsDiabetes);
        return XmlMarshallerHelper.marshal(el);
    }
}
