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
package se.inera.intyg.common.ag7804.v1.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.ag7804.model.internal.Prognos;
import se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.ag7804.model.internal.PrognosTyp;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.v1.model.internal.Ag7804UtlatandeV1;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
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

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @Mock
    WebcertModuleService moduleService;

    @InjectMocks
    InternalDraftValidatorImpl validator;

    @InjectMocks
    ValidatorUtil validatorUtil;

    Ag7804UtlatandeV1.Builder builderTemplate;

    @Before
    public void setUp() throws Exception {
        builderTemplate = Ag7804UtlatandeV1.builder()
                .setId("intygsId")
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
                .setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE)))
                .setOnskarFormedlaDiagnos(true)
                .setDiagnoser(buildDiagnoser("J22"))
                .setFunktionsnedsattning("funktionsnedsattning")
                .setAktivitetsbegransning("aktivitetsbegransning")
                .setPrognos(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, null))
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(7))))))
                .setArbetslivsinriktadeAtgarder(
                        Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING)))
                .setArbetslivsinriktadeAtgarderBeskrivning("arbetslivsinriktadeAtgarderAktuelltBeskrivning")
                .setTextVersion("");

        when(moduleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);

        // use reflection to set InternalValidatorUtil in InternalDraftValidator
        Field field = InternalDraftValidatorImpl.class.getDeclaredField("validatorUtil");
        field.setAccessible(true);
        field.set(validator, validatorUtil);
    }

    @Test
    public void validateDraft() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setUndersokningAvPatienten(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("baseratPa", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUUndersokningAvPatienten() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUUndersokningAvPatientenInvalidDate() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setUndersokningAvPatienten(new InternalDate("invalid"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("undersokningAvPatienten", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUTelefonkontaktMedPatienten() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setTelefonkontaktMedPatienten(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUTelefonkontaktMedPatientenInvalidDate() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setTelefonkontaktMedPatienten(new InternalDate("invalid"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("telefonkontaktMedPatienten", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUJournaluppgifter() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setJournaluppgifter(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUJournaluppgifterInvalidDate() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setJournaluppgifter(new InternalDate("invalid"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("journaluppgifter", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMU() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
                .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMUInvalidDate() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setAnnatGrundForMU(new InternalDate("invalid"))
                .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("annatGrundForMU", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMUBeskrivingMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("annatGrundForMUBeskrivning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMUBeskrivingOnly() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.grund-for-mu.annat.beskrivning.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUFutureDateError() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setJournaluppgifter(new InternalDate(LocalDate.now().plusDays(1)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("journaluppgifter", res.getValidationErrors().get(0).getField());
        assertEquals("common.validation.c-06", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.OTHER, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSysselsattning(new ArrayList<>())
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningTypMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(null)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningNuvarandeArbete() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)))
                .setNuvarandeArbete("nuvarandeArbete")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSysselsattningNuvarandeArbeteBeskrivingMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning", res.getValidationErrors().get(0).getCategory());
        assertEquals("nuvarandeArbete", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningNuvarandeArbeteBeskrivingOnly() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE)))
                .setNuvarandeArbete("nuvarandeArbete")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.sysselsattning.nuvarandearbete.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningTooMany() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(Sysselsattning.SysselsattningsTyp.ARBETSSOKANDE)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        List<String> errorMessages = res.getValidationErrors().stream().map(it -> it.getMessage()).collect(Collectors.toList());

        assertTrue(errorMessages.contains("ag7804.validation.sysselsattning.too-many"));
        assertTrue(errorMessages.contains("ag7804.validation.sysselsattning.invalid_combination"));
    }

    @Test
    public void validateDiagnosisOnskarFormedlaUnknown() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setOnskarFormedlaDiagnos(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals(ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100, res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateDiagnosisNotWantedButPresent() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setOnskarFormedlaDiagnos(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals(DIAGNOS_SVAR_JSON_ID_6, res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, res.getValidationErrors().get(0).getType());

    }

    @Test
    public void validateDiagnosis() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setDiagnoser(Arrays.asList(Diagnos.create(null, null, null, null)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("common.validation.diagnos.codemissing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("common.validation.diagnos.description.missing", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateFunktionsnedsattningMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setFunktionsnedsattning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateFunktionsnedsattningBlank() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setFunktionsnedsattning(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAktivitetsbegransningMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setAktivitetsbegransning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getCategory());
        assertEquals("aktivitetsbegransning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAktivitetsbegransningBlank() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setAktivitetsbegransning(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getCategory());
        assertEquals("aktivitetsbegransning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningarEmpty() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(new ArrayList<>())
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.bedomning.sjukskrivningar.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningSjukskrivingsgradMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(null,
                        new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(2))))))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing",
                res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningSjukskrivingsgradSameCodeNotAllowed() throws Exception {
        InternalLocalDateInterval date1 = new InternalLocalDateInterval(new InternalDate(LocalDate.now()),
                new InternalDate(LocalDate.now().plusDays(2)));
        InternalLocalDateInterval date2 = new InternalLocalDateInterval(new InternalDate(LocalDate.now().plusDays(4)),
                new InternalDate(LocalDate.now().plusDays(6)));

        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(
                        Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, date1),
                        Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, date2)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);
        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.invalid_combination",
                res.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void validateSjukskrivningPeriodMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, null)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.bedomning.sjukskrivningar.periodHELT_NEDSATT.missing",
                res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodInvalidAvoidsNullpointer() throws Exception {
        InternalLocalDateInterval intervalMissingTom = new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate());
        // work-around for constructor not allowing null values (but might exist in json)
        intervalMissingTom.setTom(null);
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, intervalMissingTom)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning", res.getValidationErrors().get(0).getCategory());
        assertEquals("sjukskrivningar.period.HELT_NEDSATT.tom", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodFromDateOutOfRange() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(new InternalDate(LocalDate.parse("1800-01-01")), new InternalDate(LocalDate.now())))))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("common.validation.date_out_of_range", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodTomDateOutOfRange() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.parse("2100-01-01"))))))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("common.validation.date_out_of_range", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodNoOverlap() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(
                        Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                                new InternalLocalDateInterval(new InternalDate(LocalDate.now()),
                                        new InternalDate(LocalDate.now().plusDays(2)))),
                                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                                        new InternalDate(LocalDate.now().plusDays(3)), new InternalDate(LocalDate.now().plusDays(4))))))
                .setArbetstidsforlaggning(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSjukskrivningPeriodOverlapHeltNedsattBeforeNedsattHalften() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(
                        Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                                new InternalLocalDateInterval(new InternalDate(LocalDate.now()),
                                        new InternalDate(LocalDate.now().plusDays(2)))),
                                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("bedomning", res.getValidationErrors().get(0).getCategory());
        assertEquals("sjukskrivningar.period.HELT_NEDSATT.tom", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(0).getType());
        assertEquals("bedomning", res.getValidationErrors().get(1).getCategory());
        assertEquals("sjukskrivningar.period.HALFTEN.from", res.getValidationErrors().get(1).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateSjukskrivningPeriodOverlapHeltNedsattAfterNedsattHalften() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(
                        Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                                new InternalLocalDateInterval(new InternalDate(LocalDate.now().plusDays(1)),
                                        new InternalDate(LocalDate.now().plusDays(2)))),
                                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                                        new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("bedomning", res.getValidationErrors().get(0).getCategory());
        assertEquals("sjukskrivningar.period.HELT_NEDSATT.from", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(0).getType());
        assertEquals("bedomning", res.getValidationErrors().get(1).getCategory());
        assertEquals("sjukskrivningar.period.HALFTEN.tom", res.getValidationErrors().get(1).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateSjukskrivningPeriodOverlapSameStartDate() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(
                        Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                                new InternalLocalDateInterval(new InternalDate(LocalDate.now()),
                                        new InternalDate(LocalDate.now().plusDays(2)))),
                                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                                        new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(4, res.getValidationErrors().size());
        assertEquals("bedomning", res.getValidationErrors().get(0).getCategory());
        assertEquals("sjukskrivningar.period.HELT_NEDSATT.from", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(0).getType());
        assertEquals("bedomning", res.getValidationErrors().get(1).getCategory());
        assertEquals("sjukskrivningar.period.HELT_NEDSATT.tom", res.getValidationErrors().get(1).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(1).getType());
        assertEquals("bedomning", res.getValidationErrors().get(2).getCategory());
        assertEquals("sjukskrivningar.period.HALFTEN.from", res.getValidationErrors().get(2).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(2).getType());
        assertEquals("bedomning", res.getValidationErrors().get(3).getCategory());
        assertEquals("sjukskrivningar.period.HALFTEN.tom", res.getValidationErrors().get(3).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(3).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningFalse() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningTrue() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(true)
                .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing",
                res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMotiveringMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(true)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning", res.getValidationErrors().get(0).getCategory());
        assertEquals("arbetstidsforlaggningMotivering", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMotiveringOnly() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(false)
                .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect",
                res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMotiveringWhenHeltNedsatt() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination",
                res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningWhenHeltNedsattWithoutMotivering() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggningMotivering(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(0, res.getValidationErrors().size());
    }

    @Test
    public void validateBedomningFMB() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setForsakringsmedicinsktBeslutsstod("forskningsmedicinsktBeslutsstod")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningFMBNull() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setForsakringsmedicinsktBeslutsstod(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningFMBBlank() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setForsakringsmedicinsktBeslutsstod(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setPrognos(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning", res.getValidationErrors().get(0).getCategory());
        assertEquals("prognos", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosTypMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setPrognos(Prognos.create(null, null))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning", res.getValidationErrors().get(0).getCategory());
        assertEquals("prognos", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosAterXAntalDagar() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setPrognos(Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningPrognosAterXAntalDagarPrognosDagarTillArbeteMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setPrognos(Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, null))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning", res.getValidationErrors().get(0).getCategory());
        assertEquals("prognos.dagarTillArbete", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosPrognosDagarTillArbeteOnly() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setPrognos(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, PrognosDagarTillArbeteTyp.DAGAR_30))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.bedomning.prognos.dagarTillArbete.invalid_combination",
                res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(new ArrayList<>())
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("atgarder", res.getValidationErrors().get(0).getCategory());
        assertEquals("arbetslivsinriktadeAtgarder", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderInteAktuell() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(
                        Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)))
                .setArbetslivsinriktadeAtgarderBeskrivning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateAtgarderInteAktuellCombined() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(
                        Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("ag7804.validation.atgarder.inte_aktuellt_no_combine", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("ag7804.validation.atgarder.invalid_combination", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateAtgarderInteAktuellBeskrivning() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(
                        Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)))
                .setArbetslivsinriktadeAtgarderBeskrivning("beskrivning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.atgarder.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderAktuell() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(
                        Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
                .setArbetslivsinriktadeAtgarderBeskrivning("Beskrivning arbetsanpassning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateAtgarderTooMany() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(
                        Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        List<String> errors = res.getValidationErrors()
                .stream().map(ValidationMessage::getMessage).collect(Collectors.toList());
        assertEquals(2, res.getValidationErrors().size());
        assertTrue("Expected too-many", errors.contains("ag7804.validation.atgarder.too-many"));
        assertTrue("Expected invalid_combination",
                errors.contains("ag7804.validation.atgarder.typ.invalid_combination"));
    }

    @Test
    public void validateAtgarderTypSameCodeNotAllowed() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(Arrays.asList(
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.atgarder.typ.invalid_combination",
                res.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void validateKontaktNull() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setKontaktMedAg(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktTrue() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setKontaktMedAg(true)
                .setAnledningTillKontakt("anledningTillKontakt")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktTrueNoAnledning() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setKontaktMedAg(true)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktFalse() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setKontaktMedAg(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktFalseAndAnledning() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setKontaktMedAg(false)
                .setAnledningTillKontakt("anledningTillKontakt")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.kontakt.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegAnledningTillKontakt() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setAnledningTillKontakt(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegAnnatGrundForMUBeskrivning() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
                .setAnnatGrundForMUBeskrivning(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("annatGrundForMUBeskrivning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("ag7804.validation.blanksteg.otillatet", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateBlankstegPagaendeBehandling() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setPagaendeBehandling(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegPlaneradBehandling() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setPlaneradBehandling(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegOvrigt() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate
                .setOvrigt(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("ag7804.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostadressMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostadressBlank() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerBlank() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerInvalid() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer("invalid");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostortMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostortBlank() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetTelefonnummerMissing() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.telefonnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetTelefonnummerBlank() throws Exception {
        Ag7804UtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.telefonnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
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
