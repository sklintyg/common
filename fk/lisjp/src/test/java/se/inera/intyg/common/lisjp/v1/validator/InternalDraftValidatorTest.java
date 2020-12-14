/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.v1.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.common.lisjp.v1.model.internal.LisjpUtlatandeV1;
import se.inera.intyg.common.lisjp.model.internal.Prognos;
import se.inera.intyg.common.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.common.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.common.lisjp.model.internal.Sysselsattning.SysselsattningsTyp;
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

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @InjectMocks
    InternalDraftValidatorImpl validator;

    @InjectMocks
    ValidatorUtilFK validatorUtil;

    LisjpUtlatandeV1.Builder builderTemplate;

    @Mock
    WebcertModuleService moduleService;

    @Before
    public void setUp() throws Exception {
        builderTemplate = LisjpUtlatandeV1.builder()
            .setId("intygsId")
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
            .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
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
        when(moduleService.validateDiagnosisCodeFormat(anyString())).thenReturn(true);

        // use reflection to set InternalValidatorUtil in InternalDraftValidator
        Field field = InternalDraftValidatorImpl.class.getDeclaredField("validatorUtilFK");
        field.setAccessible(true);
        field.set(validator, validatorUtil);
    }

    @Test
    public void validateDraft() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUUndersokningAvPatientenInvalidDate() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setTelefonkontaktMedPatienten(new InternalDate(LocalDate.now()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUTelefonkontaktMedPatientenInvalidDate() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setJournaluppgifter(new InternalDate(LocalDate.now()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUJournaluppgifterInvalidDate() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
            .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMUInvalidDate() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.grund-for-mu.annat.beskrivning.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUDatumIFramtidenWarning() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setJournaluppgifter(new InternalDate(LocalDate.now().minusDays(1)))
            .setTelefonkontaktMedPatienten(new InternalDate(LocalDate.now().plusDays(10)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("common.validation.c-06", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.OTHER, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSysselsattning(new ArrayList<>())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningTypMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSysselsattning(Arrays.asList(Sysselsattning.create(null)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningNuvarandeArbete() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)))
            .setNuvarandeArbete("nuvarandeArbete")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSysselsattningNuvarandeArbeteBeskrivingMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning", res.getValidationErrors().get(0).getCategory());
        assertEquals("nuvarandeArbete", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningNuvarandeArbeteBeskrivingOnly() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
            .setNuvarandeArbete("nuvarandeArbete")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.sysselsattning.nuvarandearbete.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningTooMany() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE), Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE), Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        List<String> errorMessages = res.getValidationErrors().stream().map(it -> it.getMessage()).collect(Collectors.toList());

        assertTrue(errorMessages.contains("lisjp.validation.sysselsattning.too-many"));
        assertTrue(errorMessages.contains("lisjp.validation.sysselsattning.invalid_combination"));
    }

    @Test
    public void validateDiagnosis() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setFunktionsnedsattning(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateFunktionsnedsattningBlank() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setFunktionsnedsattning(" ")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAktivitetsbegransningMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(new ArrayList<>())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningSjukskrivingsgradMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(null,
                new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(2))))))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing",
            res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningSjukskrivingsgradSameCodeNotAllowed() throws Exception {
        InternalLocalDateInterval date1 = new InternalLocalDateInterval(new InternalDate(LocalDate.now()),
            new InternalDate(LocalDate.now().plusDays(2)));
        InternalLocalDateInterval date2 = new InternalLocalDateInterval(new InternalDate(LocalDate.now().plusDays(4)),
            new InternalDate(LocalDate.now().plusDays(6)));

        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(Arrays.asList(
                Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, date1),
                Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, date2)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);
        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.invalid_combination",
            res.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void validateSjukskrivningPeriodMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, null)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.periodHELT_NEDSATT.missing",
            res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodInvalidAvoidsNullpointer() throws Exception {
        InternalLocalDateInterval intervalMissingTom = new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate());
        // work-around for constructor not allowing null values (but might exist in json)
        intervalMissingTom.setTom(null);
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(false)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningTrue() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing",
            res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMotiveringMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggning(false)
            .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect",
            res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMotiveringWhenHeltNedsatt() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(
                new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination",
            res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningWhenHeltNedsattWithoutMotivering() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(
                new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
            .setArbetstidsforlaggningMotivering(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(0, res.getValidationErrors().size());
    }

    @Test
    public void validateBedomningFMB() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setForsakringsmedicinsktBeslutsstod("forskningsmedicinsktBeslutsstod")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningFMBNull() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setForsakringsmedicinsktBeslutsstod(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningFMBBlank() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setForsakringsmedicinsktBeslutsstod(" ")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setPrognos(Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningPrognosAterXAntalDagarPrognosDagarTillArbeteMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setPrognos(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, PrognosDagarTillArbeteTyp.DAGAR_30))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.prognos.dagarTillArbete.invalid_combination",
            res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
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
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setArbetslivsinriktadeAtgarder(
                Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)))
            .setArbetslivsinriktadeAtgarderBeskrivning(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateAtgarderInteAktuellCombined() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setArbetslivsinriktadeAtgarder(
                Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("lisjp.validation.atgarder.inte_aktuellt_no_combine", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("lisjp.validation.atgarder.invalid_combination", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateAtgarderInteAktuellBeskrivning() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setArbetslivsinriktadeAtgarder(
                Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)))
            .setArbetslivsinriktadeAtgarderBeskrivning("beskrivning")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.atgarder.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderAktuell() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setArbetslivsinriktadeAtgarder(
                Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
            .setArbetslivsinriktadeAtgarderBeskrivning("Beskrivning arbetsanpassning")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateAtgarderTooMany() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setArbetslivsinriktadeAtgarder(
                Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE),
                    ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        List<String> errors = res.getValidationErrors()
            .stream().map(ValidationMessage::getMessage).collect(Collectors.toList());
        assertEquals(2, res.getValidationErrors().size());
        assertTrue("Expected too-many", errors.contains("lisjp.validation.atgarder.too-many"));
        assertTrue("Expected invalid_combination",
            errors.contains("lisjp.validation.atgarder.typ.invalid_combination"));
    }

    @Test
    public void validateAtgarderTypSameCodeNotAllowed() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setArbetslivsinriktadeAtgarder(Arrays.asList(
                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.atgarder.typ.invalid_combination",
            res.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void validateKontaktNull() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setKontaktMedFk(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktTrue() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setKontaktMedFk(true)
            .setAnledningTillKontakt("anledningTillKontakt")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktTrueNoAnledning() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setKontaktMedFk(true)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktFalse() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setKontaktMedFk(false)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktFalseAndAnledning() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setKontaktMedFk(false)
            .setAnledningTillKontakt("anledningTillKontakt")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.kontakt.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegAnledningTillKontakt() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setAnledningTillKontakt(" ")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegAnnatGrundForMUBeskrivning() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
            .setAnnatGrundForMUBeskrivning(" ")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("grundformu", res.getValidationErrors().get(0).getCategory());
        assertEquals("annatGrundForMUBeskrivning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateBlankstegPagaendeBehandling() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setPagaendeBehandling(" ")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegPlaneradBehandling() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setPlaneradBehandling(" ")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegOvrigt() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate
            .setOvrigt(" ")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostadressMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostadressBlank() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerBlank() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerInvalid() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer("invalid");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostortMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostortBlank() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetTelefonnummerMissing() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet", res.getValidationErrors().get(0).getCategory());
        assertEquals("grunddata.skapadAv.vardenhet.telefonnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetTelefonnummerBlank() throws Exception {
        LisjpUtlatandeV1 utlatande = builderTemplate.build();
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
