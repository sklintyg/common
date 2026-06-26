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
package se.inera.intyg.common.ag7804.v1.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_JSON_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.ag7804.model.internal.Prognos;
import se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.ag7804.model.internal.PrognosTyp;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class InternalDraftValidatorTest {

  @Mock WebcertModuleService moduleService;

  @InjectMocks InternalDraftValidatorImpl validator;

  @InjectMocks ValidatorUtil validatorUtil;

  Ag7804UtlatandeV1.Builder builderTemplate;

  @BeforeEach
  void setUp() throws Exception {
    builderTemplate =
        Ag7804UtlatandeV1.builder()
            .setId("intygsId")
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
            .setSysselsattning(List.of(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
            .setOnskarFormedlaDiagnos(true)
            .setDiagnoser(buildDiagnoser("J22"))
            .setFunktionsnedsattning("funktionsnedsattning")
            .setAktivitetsbegransning("aktivitetsbegransning")
            .setPrognos(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, null))
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()),
                            new InternalDate(LocalDate.now().plusDays(7))))))
            .setArbetslivsinriktadeAtgarder(
                List.of(
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.ARBETSTRANING)))
            .setArbetslivsinriktadeAtgarderBeskrivning(
                "arbetslivsinriktadeAtgarderAktuelltBeskrivning")
            .setTextVersion("");

    when(moduleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);

    // use reflection to set InternalValidatorUtil in InternalDraftValidator
    Field field = InternalDraftValidatorImpl.class.getDeclaredField("validatorUtil");
    field.setAccessible(true);
    field.set(validator, validatorUtil);
  }

  @Test
  void validateDraft() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertFalse(res.hasErrorMessages());
    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateGrundForMUMissing() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setUndersokningAvPatienten(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());

    assertEquals("grundformu", res.getValidationErrors().getFirst().getCategory());
    assertEquals("baseratPa", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateGrundForMUUndersokningAvPatienten() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setUndersokningAvPatienten(new InternalDate(LocalDate.now())).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateGrundForMUUndersokningAvPatientenInvalidDate() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setUndersokningAvPatienten(new InternalDate("invalid")).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());

    assertEquals("grundformu", res.getValidationErrors().getFirst().getCategory());
    assertEquals("undersokningAvPatienten", res.getValidationErrors().getFirst().getField());
    assertEquals(
        ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateGrundForMUTelefonkontaktMedPatienten() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setTelefonkontaktMedPatienten(new InternalDate(LocalDate.now())).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateGrundForMUTelefonkontaktMedPatientenInvalidDate() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setTelefonkontaktMedPatienten(new InternalDate("invalid")).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());

    assertEquals("grundformu", res.getValidationErrors().getFirst().getCategory());
    assertEquals("telefonkontaktMedPatienten", res.getValidationErrors().getFirst().getField());
    assertEquals(
        ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateGrundForMUJournaluppgifter() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setJournaluppgifter(new InternalDate(LocalDate.now())).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateGrundForMUJournaluppgifterInvalidDate() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setJournaluppgifter(new InternalDate("invalid")).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());

    assertEquals("grundformu", res.getValidationErrors().getFirst().getCategory());
    assertEquals("journaluppgifter", res.getValidationErrors().getFirst().getField());
    assertEquals(
        ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateGrundForMUAnnatGrundForMU() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
            .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateGrundForMUAnnatGrundForMUInvalidDate() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setAnnatGrundForMU(new InternalDate("invalid"))
            .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());

    assertEquals("grundformu", res.getValidationErrors().getFirst().getCategory());
    assertEquals("annatGrundForMU", res.getValidationErrors().getFirst().getField());
    assertEquals(
        ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateGrundForMUAnnatGrundForMUBeskrivingMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setAnnatGrundForMU(new InternalDate(LocalDate.now())).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("grundformu", res.getValidationErrors().getFirst().getCategory());
    assertEquals("annatGrundForMUBeskrivning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateGrundForMUAnnatGrundForMUBeskrivingOnly() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.grund-for-mu.annat.beskrivning.invalid_combination",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateGrundForMUFutureDateError() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setJournaluppgifter(new InternalDate(LocalDate.now().plusDays(1))).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("grundformu", res.getValidationErrors().getFirst().getCategory());
    assertEquals("journaluppgifter", res.getValidationErrors().getFirst().getField());
    assertEquals("common.validation.c-06", res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.OTHER, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSysselsattningMissing() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setSysselsattning(new ArrayList<>()).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("sysselsattning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSysselsattningTypMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setSysselsattning(List.of(Sysselsattning.create(null))).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("sysselsattning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSysselsattningNuvarandeArbete() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSysselsattning(List.of(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)))
            .setNuvarandeArbete("nuvarandeArbete")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateSysselsattningNuvarandeArbeteBeskrivingMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSysselsattning(List.of(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("sysselsattning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("nuvarandeArbete", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSysselsattningNuvarandeArbeteBeskrivingOnly() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSysselsattning(List.of(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
            .setNuvarandeArbete("nuvarandeArbete")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.sysselsattning.nuvarandearbete.invalid_combination",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSysselsattningTooMany() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSysselsattning(
                Arrays.asList(
                    Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                    Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                    Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                    Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                    Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                    Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE)))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(2, res.getValidationErrors().size());
    List<String> errorMessages =
        res.getValidationErrors().stream().map(ValidationMessage::getMessage).toList();

    assertTrue(errorMessages.contains("ag7804.validation.sysselsattning.too-many"));
    assertTrue(errorMessages.contains("ag7804.validation.sysselsattning.invalid_combination"));
  }

  @Test
  void validateDiagnosisOnskarFormedlaUnknown() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setOnskarFormedlaDiagnos(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100,
        res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateDiagnosisNotWantedButPresent() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setOnskarFormedlaDiagnos(false).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(DIAGNOS_SVAR_JSON_ID_6, res.getValidationErrors().getFirst().getField());
    assertEquals(
        ValidationMessageType.INCORRECT_COMBINATION,
        res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateDiagnosis() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setDiagnoser(List.of(Diagnos.create(null, null, null, null))).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(2, res.getValidationErrors().size());
    assertEquals(
        "common.validation.diagnos.codemissing", res.getValidationErrors().get(0).getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    assertEquals(
        "common.validation.diagnos.description.missing",
        res.getValidationErrors().get(1).getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
  }

  @Test
  void validateFunktionsnedsattningMissing() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setFunktionsnedsattning(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("funktionsnedsattning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateFunktionsnedsattningBlank() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setFunktionsnedsattning(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("funktionsnedsattning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateAktivitetsbegransningMissing() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setAktivitetsbegransning(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("funktionsnedsattning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("aktivitetsbegransning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateAktivitetsbegransningBlank() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setAktivitetsbegransning(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("funktionsnedsattning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("aktivitetsbegransning", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningarEmpty() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setSjukskrivningar(new ArrayList<>()).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.bedomning.sjukskrivningar.missing",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningSjukskrivingsgradMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        null,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningSjukskrivingsgradSameCodeNotAllowed() {
    InternalLocalDateInterval date1 =
        new InternalLocalDateInterval(
            new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(2)));
    InternalLocalDateInterval date2 =
        new InternalLocalDateInterval(
            new InternalDate(LocalDate.now().plusDays(4)),
            new InternalDate(LocalDate.now().plusDays(6)));

    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                Arrays.asList(
                    Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, date1),
                    Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, date2)))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);
    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.invalid_combination",
        res.getValidationErrors().getFirst().getMessage());
  }

  @Test
  void validateSjukskrivningPeriodMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, null)))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.bedomning.sjukskrivningar.periodHELT_NEDSATT.missing",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningPeriodInvalidAvoidsNullpointer() {
    InternalLocalDateInterval intervalMissingTom =
        new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate());
    // work-around for constructor not allowing null values (but might exist in json)
    intervalMissingTom.setTom(null);
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, intervalMissingTom)))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("bedomning", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "sjukskrivningar.period.HELT_NEDSATT.tom", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningPeriodFromDateOutOfRange() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.parse("1800-01-01")),
                            new InternalDate(LocalDate.now())))))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "common.validation.date_out_of_range", res.getValidationErrors().getFirst().getMessage());
    assertEquals(
        ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningPeriodTomDateOutOfRange() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()),
                            new InternalDate(LocalDate.parse("2100-01-01"))))))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "common.validation.date_out_of_range", res.getValidationErrors().getFirst().getMessage());
    assertEquals(
        ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningPeriodNoOverlap() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                Arrays.asList(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()),
                            new InternalDate(LocalDate.now().plusDays(2)))),
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(3)),
                            new InternalDate(LocalDate.now().plusDays(4))))))
            .setArbetstidsforlaggning(false)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateSjukskrivningPeriodOverlapHeltNedsattBeforeNedsattHalften() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                Arrays.asList(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()),
                            new InternalDate(LocalDate.now().plusDays(2)))),
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(false)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(2, res.getValidationErrors().size());
    assertEquals("bedomning", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "sjukskrivningar.period.HELT_NEDSATT.tom", res.getValidationErrors().get(0).getField());
    assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(0).getType());
    assertEquals("bedomning", res.getValidationErrors().get(1).getCategory());
    assertEquals(
        "sjukskrivningar.period.HALFTEN.from", res.getValidationErrors().get(1).getField());
    assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(1).getType());
  }

  @Test
  void validateSjukskrivningPeriodOverlapHeltNedsattAfterNedsattHalften() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                Arrays.asList(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2)))),
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(false)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(2, res.getValidationErrors().size());
    assertEquals("bedomning", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "sjukskrivningar.period.HELT_NEDSATT.from", res.getValidationErrors().get(0).getField());
    assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(0).getType());
    assertEquals("bedomning", res.getValidationErrors().get(1).getCategory());
    assertEquals("sjukskrivningar.period.HALFTEN.tom", res.getValidationErrors().get(1).getField());
    assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(1).getType());
  }

  @Test
  void validateSjukskrivningPeriodOverlapSameStartDate() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                Arrays.asList(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()),
                            new InternalDate(LocalDate.now().plusDays(2)))),
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now()),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(false)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(4, res.getValidationErrors().size());
    assertEquals("bedomning", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "sjukskrivningar.period.HELT_NEDSATT.from", res.getValidationErrors().get(0).getField());
    assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(0).getType());
    assertEquals("bedomning", res.getValidationErrors().get(1).getCategory());
    assertEquals(
        "sjukskrivningar.period.HELT_NEDSATT.tom", res.getValidationErrors().get(1).getField());
    assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(1).getType());
    assertEquals("bedomning", res.getValidationErrors().get(2).getCategory());
    assertEquals(
        "sjukskrivningar.period.HALFTEN.from", res.getValidationErrors().get(2).getField());
    assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(2).getType());
    assertEquals("bedomning", res.getValidationErrors().get(3).getCategory());
    assertEquals("sjukskrivningar.period.HALFTEN.tom", res.getValidationErrors().get(3).getField());
    assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(3).getType());
  }

  @Test
  void validateSjukskrivningArbetstidsforlaggningFalse() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(false)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateSjukskrivningArbetstidsforlaggningTrue() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(true)
            .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateSjukskrivningArbetstidsforlaggningMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningArbetstidsforlaggningMotiveringMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(true)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("bedomning", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "arbetstidsforlaggningMotivering", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningArbetstidsforlaggningMotiveringOnly() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.NEDSATT_HALFTEN,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(false)
            .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningArbetstidsforlaggningMotiveringWhenHeltNedsatt() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateSjukskrivningArbetstidsforlaggningWhenHeltNedsattWithoutMotivering() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setSjukskrivningar(
                List.of(
                    Sjukskrivning.create(
                        SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(
                            new InternalDate(LocalDate.now().plusDays(1)),
                            new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggningMotivering(null)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(0, res.getValidationErrors().size());
  }

  @Test
  void validateBedomningFMB() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setForsakringsmedicinsktBeslutsstod("forskningsmedicinsktBeslutsstod")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateBedomningFMBNull() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setForsakringsmedicinsktBeslutsstod(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateBedomningFMBBlank() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setForsakringsmedicinsktBeslutsstod(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.blanksteg.otillatet", res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBedomningPrognosMissing() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setPrognos(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("bedomning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("prognos", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBedomningPrognosTypMissing() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setPrognos(Prognos.create(null, null)).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("bedomning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("prognos", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBedomningPrognosAterXAntalDagar() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setPrognos(
                Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateBedomningPrognosAterXAntalDagarPrognosDagarTillArbeteMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setPrognos(Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, null)).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("bedomning", res.getValidationErrors().getFirst().getCategory());
    assertEquals("prognos.dagarTillArbete", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBedomningPrognosPrognosDagarTillArbeteOnly() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setPrognos(
                Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, PrognosDagarTillArbeteTyp.DAGAR_30))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.bedomning.prognos.dagarTillArbete.invalid_combination",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateAtgarderMissing() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate.setArbetslivsinriktadeAtgarder(new ArrayList<>()).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("atgarder", res.getValidationErrors().getFirst().getCategory());
    assertEquals("arbetslivsinriktadeAtgarder", res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateAtgarderInteAktuell() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setArbetslivsinriktadeAtgarder(
                List.of(
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)))
            .setArbetslivsinriktadeAtgarderBeskrivning(null)
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateAtgarderInteAktuellCombined() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setArbetslivsinriktadeAtgarder(
                Arrays.asList(
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT),
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(2, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.atgarder.inte_aktuellt_no_combine",
        res.getValidationErrors().get(0).getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    assertEquals(
        "ag7804.validation.atgarder.invalid_combination",
        res.getValidationErrors().get(1).getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
  }

  @Test
  void validateAtgarderInteAktuellBeskrivning() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setArbetslivsinriktadeAtgarder(
                List.of(
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)))
            .setArbetslivsinriktadeAtgarderBeskrivning("beskrivning")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.atgarder.invalid_combination",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateAtgarderAktuell() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setArbetslivsinriktadeAtgarder(
                List.of(
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
            .setArbetslivsinriktadeAtgarderBeskrivning("Beskrivning arbetsanpassning")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateAtgarderTooMany() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setArbetslivsinriktadeAtgarder(
                Arrays.asList(
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING),
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.ARBETSTRANING),
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN),
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL),
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD),
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                    ArbetslivsinriktadeAtgarder.create(
                        ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    List<String> errors =
        res.getValidationErrors().stream().map(ValidationMessage::getMessage).toList();
    assertEquals(2, res.getValidationErrors().size());
    assertTrue(errors.contains("ag7804.validation.atgarder.too-many"), "Expected too-many");
    assertTrue(
        errors.contains("ag7804.validation.atgarder.typ.invalid_combination"),
        "Expected invalid_combination");
  }

  @Test
  void validateAtgarderTypSameCodeNotAllowed() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setArbetslivsinriktadeAtgarder(
                Arrays.asList(
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT)))
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.atgarder.typ.invalid_combination",
        res.getValidationErrors().getFirst().getMessage());
  }

  @Test
  void validateKontaktNull() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setKontaktMedAg(null).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateKontaktTrue() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setKontaktMedAg(true)
            .setAnledningTillKontakt("anledningTillKontakt")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateKontaktTrueNoAnledning() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setKontaktMedAg(true).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateKontaktFalse() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setKontaktMedAg(false).build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertTrue(res.getValidationErrors().isEmpty());
  }

  @Test
  void validateKontaktFalseAndAnledning() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setKontaktMedAg(false)
            .setAnledningTillKontakt("anledningTillKontakt")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.kontakt.invalid_combination",
        res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBlankstegAnledningTillKontakt() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setAnledningTillKontakt(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.blanksteg.otillatet", res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBlankstegAnnatGrundForMUBeskrivning() {
    Ag7804UtlatandeV1 utlatande =
        builderTemplate
            .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
            .setAnnatGrundForMUBeskrivning(" ")
            .build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(2, res.getValidationErrors().size());
    assertEquals("grundformu", res.getValidationErrors().getFirst().getCategory());
    assertEquals("annatGrundForMUBeskrivning", res.getValidationErrors().get(0).getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    assertEquals(
        "ag7804.validation.blanksteg.otillatet", res.getValidationErrors().get(1).getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
  }

  @Test
  void validateBlankstegPagaendeBehandling() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setPagaendeBehandling(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.blanksteg.otillatet", res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBlankstegPlaneradBehandling() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setPlaneradBehandling(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.blanksteg.otillatet", res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateBlankstegOvrigt() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.setOvrigt(" ").build();

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals(
        "ag7804.validation.blanksteg.otillatet", res.getValidationErrors().getFirst().getMessage());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  @Test
  void validateEnhetPostadressMissing() {
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
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
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
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
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
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
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
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
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
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
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
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
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
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
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
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
    Ag7804UtlatandeV1 utlatande = builderTemplate.build();
    utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(" ");

    ValidateDraftResponse res = validator.validateDraft(utlatande);

    assertEquals(1, res.getValidationErrors().size());
    assertEquals("vardenhet", res.getValidationErrors().getFirst().getCategory());
    assertEquals(
        "grunddata.skapadAv.vardenhet.telefonnummer",
        res.getValidationErrors().getFirst().getField());
    assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().getFirst().getType());
  }

  private List<Diagnos> buildDiagnoser(String... diagnosKoder) {
    List<Diagnos> diagnoser = new ArrayList<>();

    for (String kod : diagnosKoder) {
      diagnoser.add(Diagnos.create(kod, "ICD-10-SE", "En beskrivning...", "Ett namn..."));
    }

    return diagnoser;
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
