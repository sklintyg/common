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
package se.inera.intyg.common.ts_diabetes.v4.model.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class WebcertModelFactoryImplTest {

    private static final String CERTIFICATE_ID = "certificateId";
    private static final String DRAFT_COPY_ID = "draftCopyId";
    private static final String UNIT_HSA_ID = "testUnitHsaId";
    private static final String CAREPROVIDER_HSA_ID = "testCareProviderHsaId";
    private static final String HOSPERSONAL_HSA_ID = "testHoSPersonalHsaId";
    private static final String PATIENT_PERSONAL_ID = "191212121212";
    private static final String INTYG_TYPE_VERSION_4_0 = "4.0";
    private static final String INTYG_TYPE_VERSION_4_1 = "4.1";

    @Mock
    private IntygTextsService intygTexts;

    @InjectMocks
    private WebcertModelFactoryImpl webcertModelFactoryImpl;

    @Test
    public void shouldCreateTsDiabetesUtlatandeV4() throws Exception {
        final var newDraftHolder = new CreateNewDraftHolder(CERTIFICATE_ID, INTYG_TYPE_VERSION_4_0, createHosPersonal(), createPatient());

        when(intygTexts.getLatestVersionForSameMajorVersion(eq(TsDiabetesEntryPoint.MODULE_ID), eq(INTYG_TYPE_VERSION_4_0)))
            .thenReturn(INTYG_TYPE_VERSION_4_0);

        final var utlatande = webcertModelFactoryImpl.createNewWebcertDraft(newDraftHolder);

        assertAll(
            () -> assertNotNull(utlatande),
            () -> assertEquals(CERTIFICATE_ID, utlatande.getId()),
            () -> assertEquals(TsDiabetesEntryPoint.MODULE_ID, utlatande.getTyp()),
            () -> assertEquals(INTYG_TYPE_VERSION_4_0, utlatande.getTextVersion()),
            () -> assertEquals(PATIENT_PERSONAL_ID, utlatande.getGrundData().getPatient().getPersonId().getPersonnummer()),
            () -> assertEquals(HOSPERSONAL_HSA_ID, utlatande.getGrundData().getSkapadAv().getPersonId()),
            () -> assertEquals(UNIT_HSA_ID, utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsid()),
            () -> assertEquals(CAREPROVIDER_HSA_ID, utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid()),
            () -> assertNotNull(utlatande.getAllmant()),
            () -> assertNotNull(utlatande.getIntygAvser()),
            () -> assertNotNull(utlatande.getOvrigt()),
            () -> assertNotNull(utlatande.getBedomning())
        );
    }

    @Test
    public void shouldDefaultToLatestMinorVersion() throws Exception {
        final var newDraftHolder = new CreateNewDraftHolder(CERTIFICATE_ID, INTYG_TYPE_VERSION_4_0, createHosPersonal(), createPatient());

        when(intygTexts.getLatestVersionForSameMajorVersion(eq(TsDiabetesEntryPoint.MODULE_ID), eq(INTYG_TYPE_VERSION_4_0)))
            .thenReturn(INTYG_TYPE_VERSION_4_1);

        final var utlatande = webcertModelFactoryImpl.createNewWebcertDraft(newDraftHolder);

        assertAll(
            () -> assertNotNull(utlatande),
            () -> assertEquals(INTYG_TYPE_VERSION_4_1, utlatande.getTextVersion())
        );
    }


    @Test
    public void shouldThrowConversionExceptionIfNotVersion4() throws Exception {
        final var copyDraftHolder = new CreateDraftCopyHolder("", new HoSPersonal());
        final var wrongVersionUtlatande = createWrongVersionUtlatande();

        final var exception = assertThrows(ConverterException.class,
            () -> webcertModelFactoryImpl.createCopy(copyDraftHolder, wrongVersionUtlatande));

        assertSame(ConverterException.class, exception.getClass());
    }

    @Test
    public void shouldCreateDraftCopyWithAnswersFromCertificate() throws ConverterException {
        final var utlatande = createTsDiabetesUtlatandeV4();
        final var createDraftCopyHolder = new CreateDraftCopyHolder(DRAFT_COPY_ID, createHosPersonal());

        final var draftCopy = webcertModelFactoryImpl.createCopy(createDraftCopyHolder, utlatande);

        assertAll(
            () -> assertNotNull(draftCopy),
            () -> assertEquals(DRAFT_COPY_ID, draftCopy.getId()),
            () -> assertSame(utlatande.getAllmant(), draftCopy.getAllmant()),
            () -> assertSame(utlatande.getIntygAvser(), draftCopy.getIntygAvser()),
            () -> assertSame(utlatande.getBedomning(), draftCopy.getBedomning()),
            () -> assertSame(utlatande.getOvrigt(), draftCopy.getOvrigt()),
            () -> assertSame(utlatande.getIdentitetStyrktGenom(), draftCopy.getIdentitetStyrktGenom())
        );
    }

    @Test
    public void shouldRemoveSignatureDateFromCertificateInCopy() throws ConverterException {
        final var utlatande = createTsDiabetesUtlatandeV4();
        final var createDraftCopyHolder = new CreateDraftCopyHolder(DRAFT_COPY_ID, createHosPersonal());

        final var draftCopy = webcertModelFactoryImpl.createCopy(createDraftCopyHolder, utlatande);

        assertAll(
            () -> assertNotNull(draftCopy),
            () -> assertNull(draftCopy.getGrundData().getSigneringsdatum())

        );
    }

    private TsDiabetesUtlatandeV4 createTsDiabetesUtlatandeV4() {
        final var grundData = new GrundData();
        grundData.setSkapadAv(createHosPersonal());
        grundData.setSigneringsdatum(LocalDateTime.now());
        grundData.setPatient(createPatient());

        return TsDiabetesUtlatandeV4.builder()
            .setId(CERTIFICATE_ID)
            .setGrundData(grundData)
            .setTextVersion(INTYG_TYPE_VERSION_4_0)
            .build();
    }

    private Patient createPatient() {
        final var patient = new Patient();
        patient.setPersonId(Personnummer.createPersonnummer(PATIENT_PERSONAL_ID).orElse(null));
        return patient;
    }

    private HoSPersonal createHosPersonal() {
        final var hosPerson = new HoSPersonal();
        hosPerson.setPersonId(HOSPERSONAL_HSA_ID);
        hosPerson.setFullstandigtNamn("Doktor Testsson");
        hosPerson.setVardenhet(createUnit());
        return hosPerson;
    }

    private Vardenhet createUnit() {
        final var unit = new Vardenhet();
        final var careProvider = new Vardgivare();
        unit.setEnhetsid(UNIT_HSA_ID);
        unit.setEnhetsnamn("testUnitName");
        careProvider.setVardgivarid(CAREPROVIDER_HSA_ID);
        careProvider.setVardgivarnamn("testCareProviderName");
        unit.setVardgivare(careProvider);
        return unit;
    }

    private Utlatande createWrongVersionUtlatande() {
        return TsDiabetesUtlatandeV3.builder()
            .setId(CERTIFICATE_ID)
            .setGrundData(new GrundData())
            .setTextVersion("3.0")
            .build();
    }
}
