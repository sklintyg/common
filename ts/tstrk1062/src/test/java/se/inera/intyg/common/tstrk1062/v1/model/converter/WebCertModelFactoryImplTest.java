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
package se.inera.intyg.common.tstrk1062.v1.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint;
import se.inera.intyg.common.tstrk1062.v1.model.internal.TsTrk1062UtlatandeV1;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WebCertModelFactoryImplTest {

  private static final String INTYG_ID = "intygsId";
  private static final String INTYG_TYPE_VERSION_1 = "1.0";
  private static final String INTYG_TYPE_VERSION_1_2 = "1.0";

  private IntygTextsService intygTextsService;

  private WebcertModelFactoryImpl modelFactory;

  @BeforeEach
  void setUp() {
    intygTextsService = mock(IntygTextsService.class);
    modelFactory = new WebcertModelFactoryImpl(intygTextsService);
    when(intygTextsService.getLatestVersionForSameMajorVersion(
            eq(TsTrk1062EntryPoint.MODULE_ID), eq(INTYG_TYPE_VERSION_1)))
        .thenReturn(INTYG_TYPE_VERSION_1_2);
  }

  @Test
  void testCreateNewWebcertDraft() throws Exception {
    final Vardenhet vardenhet = getVardenhet();
    final HoSPersonal hosPersonal = getHoSPersonal(vardenhet);
    final Patient patient = getPatient();
    final CreateNewDraftHolder createNewDraftHolder =
        new CreateNewDraftHolder(INTYG_ID, INTYG_TYPE_VERSION_1, hosPersonal, patient);

    final TsTrk1062UtlatandeV1 utlatande = modelFactory.createNewWebcertDraft(createNewDraftHolder);

    assertNotNull(utlatande, "Utlatande is null");
    final HoSPersonal skapadAv = utlatande.getGrundData().getSkapadAv();
    assertNotNull(skapadAv, "SkapadAv is null");
    assertEquals(
        vardenhet.getVardgivare().getVardgivarid(),
        skapadAv.getVardenhet().getVardgivare().getVardgivarid(),
        "Vårdgivare not equal");
    assertEquals(
        vardenhet.getEnhetsid(), skapadAv.getVardenhet().getEnhetsid(), "Vårdenhet not equal");
    assertEquals(hosPersonal.getPersonId(), skapadAv.getPersonId(), "PersonalId not equal");
    final Patient actualPatient = utlatande.getGrundData().getPatient();
    assertNotNull(actualPatient, "Patient is null");
    assertEquals(
        patient.getPersonId().getPersonnummer(),
        actualPatient.getPersonId().getPersonnummer(),
        "Personnummer not equal");
    assertEquals(INTYG_TYPE_VERSION_1_2, utlatande.getTextVersion());
  }

  @Test
  void testCreateCopy() throws Exception {
    TsTrk1062UtlatandeV1 utlatande =
        TsTrk1062UtlatandeV1.builder().setGrundData(buildGrundData(LocalDateTime.now())).build();

    final Vardenhet vardenhet = getVardenhet();
    final HoSPersonal hosPersonal = getHoSPersonal(vardenhet);
    final CreateDraftCopyHolder createDraftCopyHolder =
        new CreateDraftCopyHolder(INTYG_ID, hosPersonal);

    final TsTrk1062UtlatandeV1 utlatandeCopy =
        modelFactory.createCopy(createDraftCopyHolder, utlatande);

    assertNotNull(utlatandeCopy, "UtlatandeCopy is null");
    assertEquals(INTYG_ID, utlatandeCopy.getId(), "IntygsId not equal");
    assertNotNull(utlatandeCopy.getGrundData(), "Grunddata not null");
    assertNotNull(utlatandeCopy.getGrundData().getPatient(), "Patient not null");
    assertEquals(
        "191212121212",
        utlatandeCopy.getGrundData().getPatient().getPersonId().getPersonnummer(),
        "PatientId not equal");
    assertNotNull(utlatandeCopy.getGrundData().getSkapadAv(), "SkapadAv not null");
    assertEquals("TST12345678", utlatandeCopy.getGrundData().getSkapadAv().getPersonId(), "");
    assertEquals(
        "VårdenhetsId",
        utlatandeCopy.getGrundData().getSkapadAv().getVardenhet().getEnhetsid(),
        "");
    assertEquals(
        "VårdgivarId",
        utlatandeCopy.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid(),
        "");
    assertNull(utlatandeCopy.getSignature(), "Signature is not null");
  }

  @Test
  void testCreateCopyOfOtherType() throws Exception {
    assertThrows(
        ConverterException.class,
        () -> {
          final Vardenhet vardenhet = getVardenhet();
          final HoSPersonal hosPersonal = getHoSPersonal(vardenhet);
          final CreateDraftCopyHolder createDraftCopyHolder =
              new CreateDraftCopyHolder(INTYG_ID, hosPersonal);

          modelFactory.createCopy(createDraftCopyHolder, otherTypeOfUtlatande());
        });
  }

  @Test
  void testCreateCopyWithoutId() throws Exception {
    assertThrows(
        ConverterException.class,
        () -> {
          TsTrk1062UtlatandeV1 utlatande =
              TsTrk1062UtlatandeV1.builder()
                  .setGrundData(buildGrundData(LocalDateTime.now()))
                  .build();

          final Vardenhet vardenhet = getVardenhet();
          final HoSPersonal hosPersonal = getHoSPersonal(vardenhet);
          final CreateDraftCopyHolder createDraftCopyHolder =
              new CreateDraftCopyHolder("", hosPersonal);

          modelFactory.createCopy(createDraftCopyHolder, utlatande);
        });
  }

  private Utlatande otherTypeOfUtlatande() {
    return new Utlatande() {
      @Override
      public String getId() {
        return "123";
      }

      @Override
      public String getTyp() {
        return "OtherType";
      }

      @Override
      public GrundData getGrundData() {
        return getGrundData();
      }

      @Override
      public String getTextVersion() {
        return "v99";
      }

      @Override
      public String getSignature() {
        return "Signature";
      }
    };
  }

  private Patient getPatient() {
    Patient patient = new Patient();
    patient.setFornamn("Tolvan");
    patient.setEfternamn("Tolvansson");
    patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").get());
    return patient;
  }

  private HoSPersonal getHoSPersonal(Vardenhet vardenhet) {
    HoSPersonal hosPerson = new HoSPersonal();
    hosPerson.setPersonId("TST12345678");
    hosPerson.setFullstandigtNamn("Doktor Ture Demo");
    hosPerson.setVardenhet(vardenhet);
    return hosPerson;
  }

  private Vardenhet getVardenhet() {
    Vardenhet vardenhet = new Vardenhet();
    vardenhet.setEnhetsid("VårdenhetsId");
    vardenhet.setEnhetsnamn("Vårdenhetsnamn");
    vardenhet.setVardgivare(new Vardgivare());
    vardenhet.getVardgivare().setVardgivarid("VårdgivarId");
    vardenhet.getVardgivare().setVardgivarnamn("Vårdgivarnamn");
    return vardenhet;
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
