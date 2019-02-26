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

package se.inera.intyg.common.ts_tstrk1062.v1.rest;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.UtkastStatus;
import se.inera.intyg.common.support.model.common.internal.*;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.common.support.modules.support.api.dto.PdfResponse;
import se.inera.intyg.common.support.services.BefattningService;
import se.inera.intyg.common.ts_tstrk1062.support.TsTstrk1062EntryPoint;
import se.inera.intyg.common.ts_tstrk1062.v1.model.converter.WebcertModelFactoryImpl;
import se.inera.intyg.common.ts_tstrk1062.v1.model.internal.TsTstrk1062UtlatandeV1;
import se.inera.intyg.common.ts_tstrk1062.v1.pdf.PdfGenerator;
import se.inera.intyg.common.ts_tstrk1062.v1.validator.InternalValidatorInstanceImpl;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v2.GetCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v2.RevokeCertificateResponderInterface;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BefattningService.class })
public class TsTstrk1062ModuleApiV1Test {

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
    private TsTstrk1062ModuleApiV1 moduleApi;

    private final static String TEXT_VERSION = "v1";
    private final static String INTERNAL_MODEL = "INTERNAL_MODEL";
    private final static String INTYGS_ID = "IntygsId";

    public TsTstrk1062ModuleApiV1Test() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPDF() throws Exception {
        final GrundData grundData = buildGrundData(LocalDateTime.now());

        final TsTstrk1062UtlatandeV1 mockUtlatande = Mockito.mock(TsTstrk1062UtlatandeV1.class);
        final IntygTexts mockIntygTexts = new IntygTexts("1.0", "", null, null, null, null, null);
        final ApplicationOrigin mockApplicationOrigin = ApplicationOrigin.WEBCERT;
        final UtkastStatus mockUtkastStatus = UtkastStatus.SIGNED;
        final PdfResponse expectedPdfResponse = Mockito.mock(PdfResponse.class);
        final List statuses = new ArrayList<Status>();

        doReturn(mockUtlatande).when(objectMapper).readValue(INTERNAL_MODEL, TsTstrk1062UtlatandeV1.class);
        doReturn(TEXT_VERSION).when(mockUtlatande).getTextVersion();
        doReturn(grundData).when(mockUtlatande).getGrundData();
        doReturn(INTYGS_ID).when(mockUtlatande).getId();

        doReturn(mockIntygTexts).when(intygTextsService).getIntygTextsPojo(TsTstrk1062EntryPoint.MODULE_ID, TEXT_VERSION);
        doReturn(expectedPdfResponse).when(pdfGenerator).generatePdf(INTYGS_ID, INTERNAL_MODEL, grundData.getPatient().getPersonId(),
                mockIntygTexts,
                statuses, mockApplicationOrigin, mockUtkastStatus);

        final PdfResponse actualPdfResponse = moduleApi.pdf(INTERNAL_MODEL, statuses, mockApplicationOrigin, mockUtkastStatus);

        assertNotNull("PdfResponse is null", actualPdfResponse);
        assertEquals("PdfResponse is not equal", expectedPdfResponse, actualPdfResponse);
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
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
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
