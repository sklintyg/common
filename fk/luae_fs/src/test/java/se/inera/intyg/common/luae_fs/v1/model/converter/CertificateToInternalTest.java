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

package se.inera.intyg.common.luae_fs.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.common.luae_fs.v1.model.converter.RespConstants.DIAGNOS_ICD_10_ID;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.MetaDataGrundData;
import se.inera.intyg.common.luae_fs.v1.model.converter.certificate.question.QuestionDiagnoser;
import se.inera.intyg.common.luae_fs.v1.model.internal.LuaefsUtlatandeV1;
import se.inera.intyg.common.services.texts.CertificateTextProvider;
import se.inera.intyg.common.support.facade.builder.CertificateBuilder;
import se.inera.intyg.common.support.facade.model.Certificate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class CertificateToInternalTest {

    private Certificate certificate;
    private LuaefsUtlatandeV1 expectedInternalCertificate;
    @Mock
    private CertificateTextProvider textProvider;

    @Mock
    private WebcertModuleService webcertModuleService;

    @InjectMocks
    private CertificateToInternal certificateToInternal;

    private static final String DIAGNOSIS_DISPLAYNAME = "Namn att visa upp";

    @BeforeEach
    void setup() {
        expectedInternalCertificate = LuaefsUtlatandeV1.builder()
            .setId("id")
            .setTextVersion("textVersion")
            .setGrundData(getGrundData())
            .setDiagnoser(
                Arrays.asList(
                    Diagnos.create("F500", DIAGNOS_ICD_10_ID, "Beskrivning1", DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F501", DIAGNOS_ICD_10_ID, "Beskrivning2", DIAGNOSIS_DISPLAYNAME),
                    Diagnos.create("F502", DIAGNOS_ICD_10_ID, "Beskrivning3", DIAGNOSIS_DISPLAYNAME))
            )
            .build();

        certificate = CertificateBuilder.create()
            .metadata(MetaDataGrundData.toCertificate(expectedInternalCertificate, textProvider))
            .addElement(QuestionDiagnoser.toCertificate(expectedInternalCertificate.getDiagnoser(), 0, textProvider))
            .build();
    }

    private static GrundData getGrundData() {
        final var grundData = new GrundData();
        final var hosPersonal = new HoSPersonal();
        final var vardenhet = new Vardenhet();
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
        hosPersonal.setVardenhet(vardenhet);
        grundData.setSkapadAv(hosPersonal);
        grundData.setPatient(patient);
        return grundData;
    }

    @Test
    void shallIncludeId() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getId(), actualInternalCertificate.getId());
    }

    @Test
    void shallIncludeTextVersion() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertEquals(expectedInternalCertificate.getTextVersion(), actualInternalCertificate.getTextVersion());
    }

    @Test
    void shallIncludeGrundData() {
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        assertNotNull(actualInternalCertificate.getGrundData(), "GrundData is missing!");
    }

    @Test
    void shallIncludeDiagnoser() {
        doReturn(DIAGNOSIS_DISPLAYNAME).when(webcertModuleService).getDescriptionFromDiagnosKod(anyString(), anyString());
        final var actualInternalCertificate = certificateToInternal.convert(certificate, expectedInternalCertificate);
        for (int i = 0; i < actualInternalCertificate.getDiagnoser().size(); i++) {
            assertEquals(actualInternalCertificate.getDiagnoser().get(i), expectedInternalCertificate.getDiagnoser().get(i));
        }
    }
}