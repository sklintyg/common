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
package se.inera.intyg.common.af00213.v1.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
class InternalDraftValidatorTest {

  InternalDraftValidatorImpl validator = new InternalDraftValidatorImpl();

  Af00213UtlatandeV1.Builder builderTemplate;

  @BeforeEach
  void setUp() {
    builderTemplate =
        Af00213UtlatandeV1.builder()
            .setId("intygsId")
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .setHarFunktionsnedsattning(true)
            .setFunktionsnedsattning("funktionsnedsattning")
            .setHarAktivitetsbegransning(true)
            .setAktivitetsbegransning("aktivitetsbegransning")
            .setHarUtredningBehandling(true)
            .setUtredningBehandling("utredningBehandling")
            .setHarArbetetsPaverkan(true)
            .setArbetetsPaverkan("arbetetsPaverkan")
            .setOvrigt("ovrigt")
            .setTextVersion("");
  }

  @Test
  void validateDraft() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertFalse(res.hasErrorMessages());
    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateFunktionsnedsattningJaNejNotSpecified() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setHarFunktionsnedsattning(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("harFunktionsnedsattning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateFunktionsnedsattningMissing() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setFunktionsnedsattning(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("funktionsnedsattning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateFunktionsnedsattningBlank() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setFunktionsnedsattning(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("funktionsnedsattning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateAktivitetsbegransningMissing() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setAktivitetsbegransning(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("aktivitetsbegransning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("aktivitetsbegransning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateAktivitetsbegransningBlank() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setAktivitetsbegransning(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("aktivitetsbegransning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("aktivitetsbegransning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateAktivitetsbegransningUnspecifiedWhenHarFunktionsnedsattning() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setHarAktivitetsbegransning(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("aktivitetsbegransning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("harAktivitetsbegransning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateUtredningBehandlingJaNejNotSpecified() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setHarUtredningBehandling(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("harUtredningBehandling", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateUtredningBehandlingMissingText() {
    Af00213UtlatandeV1 utlatande =
        builderTemplate.setHarUtredningBehandling(true).setUtredningBehandling(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("utredningBehandling", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateUtredningBehandlingDoesNotRequireTextWhenNej() {
    Af00213UtlatandeV1 utlatande =
        builderTemplate.setHarUtredningBehandling(false).setUtredningBehandling(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(0, res.getValidationErrors().size());
  }

  @Test
  void validateUtredningBehandlingJaRequiresText() {
    Af00213UtlatandeV1 utlatande =
        builderTemplate
            .setHarAktivitetsbegransning(false)
            .setHarArbetetsPaverkan(false)
            .setHarFunktionsnedsattning(false)
            .setHarUtredningBehandling(true)
            .setUtredningBehandling(null)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBlankstegPlaneradBehandling() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setUtredningBehandling(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBlankstegOvrigt() {
    Af00213UtlatandeV1 utlatande = builderTemplate.setOvrigt(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "af00213.validation.blanksteg.otillatet",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetPostadressMissing() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(null);

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetPostadressBlank() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(" ");

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetPostnummerMissing() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(null);

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetPostnummerBlank() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(" ");

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetPostnummerInvalid() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer("invalid");

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().getFirst().getField());
    assertEquals(
        ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetPostortMissing() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(null);

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetPostortBlank() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(" ");

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetTelefonnummerMissing() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(null);

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.telefonnummer",
        res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetTelefonnummerBlank() {
    Af00213UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(" ");

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.telefonnummer",
        res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
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
    patient.setPersonId(Personnummer.createPersonnummer("19121212-1212").orElseThrow());
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
