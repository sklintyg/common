/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00251.v1.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static se.inera.intyg.common.af00251.v1.model.internal.ArbetsmarknadspolitisktProgram.Omfattning;
import static se.inera.intyg.common.af00251.v1.model.internal.ArbetsmarknadspolitisktProgram.builder;
import static se.inera.intyg.common.af00251.v1.utils.Asserts.assertValidationMessage;
import static se.inera.intyg.common.af00251.v1.utils.Asserts.assertValidationMessages;
import static se.inera.intyg.common.af00251.v1.validator.InternalDraftValidatorImpl.CATEGORY_ARBETSMARKNADS_PROGRAM;
import static se.inera.intyg.common.af00251.v1.validator.InternalDraftValidatorImpl.CATEGORY_BEDOMNING;
import static se.inera.intyg.common.af00251.v1.validator.InternalDraftValidatorImpl.CATEGORY_KONSEKVENSER;
import static se.inera.intyg.common.af00251.v1.validator.InternalDraftValidatorImpl.CATEGORY_MEDICINSKT_UNDERLAG;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.af00251.v1.model.internal.BegransningSjukfranvaro;
import se.inera.intyg.common.af00251.v1.model.internal.PrognosAtergang;
import se.inera.intyg.common.af00251.v1.model.internal.Sjukfranvaro;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {


    InternalDraftValidatorImpl validator = new InternalDraftValidatorImpl();

    AF00251UtlatandeV1.Builder builderTemplate;

    @Before
    public void setUp() {
        builderTemplate = AF00251UtlatandeV1.builder()
            .setId("intygsId")
            .setGrundData(buildGrundData(LocalDateTime.now()))
            .setUndersokningsDatum(new InternalDate(LocalDate.now()))
            .setAnnatDatum(new InternalDate(LocalDate.now()))
            .setAnnatBeskrivning("Annan beskrivning")
            .setArbetsmarknadspolitisktProgram(
                builder()
                    .setMedicinskBedomning("Kan jobba")
                    .setOmfattning(Omfattning.HELTID)
                    .build())
            .setFunktionsnedsattning("funktionsnedsättning")
            .setAktivitetsbegransning("aktivitetsnedsättning")
            .setHarForhinder(true)
            .setSjukfranvaro(Arrays.asList(Sjukfranvaro.builder()
                .setChecked(true)
                .setNiva(44)
                .setPeriod(new InternalLocalDateInterval(
                    new InternalDate(LocalDate.now()),
                    new InternalDate(LocalDate.now()
                        .plusDays(5))))
                .build()))
            .setBegransningSjukfranvaro(BegransningSjukfranvaro.builder()
                .setKanBegransas(true)
                .setBeskrivning("många pauser")
                .build())
            .setPrognosAtergang(PrognosAtergang.builder()
                .setPrognos(PrognosAtergang.Prognos.EJ_MOJLIGT_AVGORA)
                .setAnpassningar("Behöver hjälp.")
                .build())
            .setTextVersion("");
    }

    @Test
    public void validateDraft() {
        AF00251UtlatandeV1 utlatande = builderTemplate.build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertThat(res.hasErrorMessages(), is(false));
        assertThat(res.getValidationErrors(), is(empty()));
    }

    @Test
    public void validateMedicinskUnderlagNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setUndersokningsDatum(null)
            .setAnnatDatum(null)
            .setAnnatBeskrivning(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_MEDICINSKT_UNDERLAG), is("undersokning"), is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateMedicinskUnderlagInvalidDate() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setUndersokningsDatum(new InternalDate("123123123321321"))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_MEDICINSKT_UNDERLAG), is("undersokningsDatum"), is(ValidationMessageType.INVALID_FORMAT));
    }

    @Test
    public void validateMedicinskUnderlagInvalidInstanceAnnatWithNoDescription() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setAnnatBeskrivning(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_MEDICINSKT_UNDERLAG), is("annatBeskrivning"),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateMedicinskUnderlagFutureAnnatDatum() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setAnnatDatum(new InternalDate(LocalDate.now().plusDays(5)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_MEDICINSKT_UNDERLAG), is("annatDatum"),
            is(ValidationMessageType.INVALID_FORMAT), is("af00251.validation.undersokning.future-date"));
    }

    @Test
    public void validateMedicinskUnderlagFutureUndersokningsDatum() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setUndersokningsDatum(new InternalDate(LocalDate.now().plusDays(5)))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_MEDICINSKT_UNDERLAG), is("undersokningsDatum"),
            is(ValidationMessageType.INVALID_FORMAT), is("af00251.validation.undersokning.future-date"));
    }

    @Test
    public void validateArbetsmarknadspolitisktProgramNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setArbetsmarknadspolitisktProgram(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
    }

    @Test
    public void validateArbetsmarknadspolitisktProgramInvalidInstanceEmpty() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setArbetsmarknadspolitisktProgram(builder().build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(2));
    }

    @Test
    public void validateArbetsmarknadspolitisktProgramInvalidInstanceNoOmfattning() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setArbetsmarknadspolitisktProgram(builder()
                .setMedicinskBedomning("bedömning")
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
    }

    @Test
    public void validateArbetsmarknadspolitisktProgramInvalidInstanceNoBedomning() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setArbetsmarknadspolitisktProgram(builder()
                .setOmfattning(Omfattning.HELTID)
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
    }

    @Test
    public void validateArbetsmarknadspolitisktProgramInvalidInstanceDeltidNoHours() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setArbetsmarknadspolitisktProgram(builder()
                .setOmfattning(Omfattning.DELTID)
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(2));

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_ARBETSMARKNADS_PROGRAM), is("arbetsmarknadspolitisktProgram.medicinskBedomning"),
            is(ValidationMessageType.EMPTY));
        assertValidationMessage(validationErrors.get(1),
            is(CATEGORY_ARBETSMARKNADS_PROGRAM), is("arbetsmarknadspolitisktProgram.omfattningDeltid"),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateArbetsmarknadspolitisktProgramInvalidInstanceDeltidInvalidHoursMin() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setArbetsmarknadspolitisktProgram(builder()
                .setOmfattning(Omfattning.DELTID)
                .setMedicinskBedomning("bedömning")
                .setOmfattningDeltid(0)
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_ARBETSMARKNADS_PROGRAM), is("arbetsmarknadspolitisktProgram.omfattningDeltid"),
            is(ValidationMessageType.INVALID_FORMAT),
            is("af00251.validation.arbetsmarknadspolitisktProgram.omfattningDeltid.invalid-range"));
    }

    @Test
    public void validateArbetsmarknadspolitisktProgramInvalidInstanceDeltidInvalidHoursMax() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setArbetsmarknadspolitisktProgram(builder()
                .setOmfattning(Omfattning.DELTID)
                .setMedicinskBedomning("bedömning")
                .setOmfattningDeltid(40)
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_ARBETSMARKNADS_PROGRAM), is("arbetsmarknadspolitisktProgram.omfattningDeltid"),
            is(ValidationMessageType.INVALID_FORMAT),
            is("af00251.validation.arbetsmarknadspolitisktProgram.omfattningDeltid.invalid-range"));
    }

    @Test
    public void validateFunktionsnedsattningNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setFunktionsnedsattning(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_KONSEKVENSER), is("funktionsnedsattning"), is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateFunktionsnedsattningEmtpy() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setFunktionsnedsattning("")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_KONSEKVENSER), is("funktionsnedsattning"), is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateAktivitetsbegransningNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setAktivitetsbegransning(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_KONSEKVENSER), is("aktivitetsbegransning"), is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateAktivitetsbegransningEmtpy() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setAktivitetsbegransning("")
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_KONSEKVENSER), is("aktivitetsbegransning"), is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateHarForhinderNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("harForhinder"), is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateHarForhinderTrueWithNullSjukfranvaro() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(true)
            .setSjukfranvaro(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro"), is(ValidationMessageType.EMPTY),
            is("af00251.validation.sjukfranvaro.missing"));
    }

    @Test
    public void validateHarForhinderFalseWithSjukfranvaro() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(false)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(1));
        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("harForhinder"), is(ValidationMessageType.INCORRECT_COMBINATION),
            is("af00251.validation.harForhinder.forbidden-sjukfranvaro"));
    }

    @Test
    public void validateSjukfranvaroNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(false)
            .setSjukfranvaro(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(0));
    }

    @Test
    public void validateSjukfranvaroEmty() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(false)
            .setSjukfranvaro(new ArrayList<>())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertThat(validationErrors, hasSize(0));
    }

    @Test
    public void validateSjukfranvaroTooMany() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(true)
            .setSjukfranvaro(Arrays.asList(Sjukfranvaro.builder()
                    .setChecked(true)
                    .setPeriod(new InternalLocalDateInterval("2018-08-01", "2018-08-30"))
                    .setNiva(100)
                    .build(),
                Sjukfranvaro.builder()
                    .setChecked(true)
                    .setPeriod(new InternalLocalDateInterval("2018-09-01", "2018-09-30"))
                    .setNiva(80)
                    .build(),
                Sjukfranvaro.builder()
                    .setChecked(true)
                    .setPeriod(new InternalLocalDateInterval("2018-10-01", "2018-10-30"))
                    .setNiva(60)
                    .build(),
                Sjukfranvaro.builder()
                    .setChecked(true)
                    .setPeriod(new InternalLocalDateInterval("2018-11-01", "2018-11-30"))
                    .setNiva(40)
                    .build(),
                Sjukfranvaro.builder()
                    .setChecked(true)
                    .setPeriod(new InternalLocalDateInterval("2018-12-01", "2018-12-30"))
                    .setNiva(20)
                    .build()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro"),
            is(ValidationMessageType.OTHER), is("af00251.validation.sjukfranvaro.too-many"));
    }

    @Test
    public void validateSjukfranvaroNivaMin() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(true)
            .setSjukfranvaro(Arrays.asList(Sjukfranvaro.builder()
                .setPeriod(new InternalLocalDateInterval("2018-10-01", "2018-10-31"))
                .setNiva(0)
                .setChecked(true)
                .build()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[0].niva"),
            is(ValidationMessageType.INVALID_FORMAT));
    }

    @Test
    public void validateSjukfranvaroNivaMax() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(true)
            .setSjukfranvaro(Arrays.asList(Sjukfranvaro.builder()
                .setPeriod(new InternalLocalDateInterval("2018-10-01", "2018-10-31"))
                .setNiva(101)
                .setChecked(true)
                .build()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[0].niva"),
            is(ValidationMessageType.INVALID_FORMAT));
    }

    @Test
    public void validateSjukfranvaroPeriodInvalidInterval() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(true)
            .setSjukfranvaro(Arrays.asList(Sjukfranvaro.builder()
                .setPeriod(new InternalLocalDateInterval("2018-11-01", "2018-10-31"))
                .setNiva(90)
                .setChecked(true)
                .build()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[0].period"),
            is(ValidationMessageType.INCORRECT_COMBINATION));
    }

    @Test
    public void validateSjukfranvaroPeriodIntervalOverlap() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(true)
            .setSjukfranvaro(Arrays.asList(Sjukfranvaro.builder()
                    .setPeriod(new InternalLocalDateInterval("2018-10-01", "2018-10-31"))
                    .setNiva(90)
                    .setChecked(true)
                    .build(),
                Sjukfranvaro.builder()
                    .setPeriod(new InternalLocalDateInterval("2018-09-15", "2018-10-15"))
                    .setNiva(80)
                    .setChecked(true)
                    .build()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 2);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[0].period.from"),
            is(ValidationMessageType.PERIOD_OVERLAP));

        assertValidationMessage(validationErrors.get(1),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[1].period.tom"),
            is(ValidationMessageType.PERIOD_OVERLAP));
    }

    @Test
    public void validateSjukfranvaroPeriodIntervalInvalidDateFormat() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(true)
            .setSjukfranvaro(Arrays.asList(Sjukfranvaro.builder()
                .setPeriod(new InternalLocalDateInterval("qwerty", "asd"))
                .setNiva(90)
                .setChecked(true)
                .build()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 2);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[0].period.from"),
            is(ValidationMessageType.INVALID_FORMAT));

        assertValidationMessage(validationErrors.get(1),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[0].period.tom"),
            is(ValidationMessageType.INVALID_FORMAT));
    }

    @Test
    public void validateSjukfranvaroPeriodIntervalOverlapSameStart() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setHarForhinder(true)
            .setSjukfranvaro(Arrays.asList(Sjukfranvaro.builder()
                    .setPeriod(new InternalLocalDateInterval("2018-10-01", "2018-10-31"))
                    .setNiva(90)
                    .setChecked(true)
                    .build(),
                Sjukfranvaro.builder()
                    .setPeriod(new InternalLocalDateInterval("2018-10-01", "2018-10-15"))
                    .setNiva(80)
                    .setChecked(true)
                    .build()))
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 4);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[0].period.from"),
            is(ValidationMessageType.PERIOD_OVERLAP));
        assertValidationMessage(validationErrors.get(1),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[0].period.tom"),
            is(ValidationMessageType.PERIOD_OVERLAP));

        assertValidationMessage(validationErrors.get(2),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[1].period.from"),
            is(ValidationMessageType.PERIOD_OVERLAP));
        assertValidationMessage(validationErrors.get(3),
            is(CATEGORY_BEDOMNING), is("sjukfranvaro[1].period.tom"),
            is(ValidationMessageType.PERIOD_OVERLAP));
    }

    @Test
    public void validateBegransningSjukfranvaroNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setBegransningSjukfranvaro(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("begransningSjukfranvaro.kanBegransas"),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateBegransningSjukfranvaroBooleanNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setBegransningSjukfranvaro(BegransningSjukfranvaro.builder()
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("begransningSjukfranvaro.kanBegransas"),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateBegransningSjukfranvaroBooleanTrueNoBeskrivning() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setBegransningSjukfranvaro(BegransningSjukfranvaro.builder()
                .setKanBegransas(true)
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("begransningSjukfranvaro.beskrivning"),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validateBegransningSjukfranvaroBooleanTrueWithBeskrivning() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setBegransningSjukfranvaro(BegransningSjukfranvaro.builder()
                .setKanBegransas(true)
                .setBeskrivning("hej")
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 0);
    }

    @Test
    public void validateBegransningSjukfranvaroBooleanFalse() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setBegransningSjukfranvaro(BegransningSjukfranvaro.builder()
                .setKanBegransas(false)
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 0);
    }

    @Test
    public void validatePrognosAtergangNull() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setPrognosAtergang(null)
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("prognosAtergang.prognos"),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validatePrognosAtergangEmpty() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setPrognosAtergang(PrognosAtergang.builder()
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("prognosAtergang.prognos"),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void validatePrognosAtergangOk() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setPrognosAtergang(PrognosAtergang.builder()
                .setPrognos(PrognosAtergang.Prognos.UTAN_ANPASSNING)
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 0);

    }

    @Test
    public void validatePrognosAtergangMedAnpassningNoText() {
        AF00251UtlatandeV1 utlatande = builderTemplate
            .setPrognosAtergang(PrognosAtergang.builder()
                .setPrognos(PrognosAtergang.Prognos.MED_ANPASSNING)
                .build())
            .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        final List<ValidationMessage> validationErrors = res.getValidationErrors();
        assertValidationMessages(validationErrors, 1);

        assertValidationMessage(validationErrors.get(0),
            is(CATEGORY_BEDOMNING), is("prognosAtergang.anpassningar"),
            is(ValidationMessageType.EMPTY));
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
        patient.setPersonId(Personnummer.createPersonnummer("19121212-1212")
            .get());
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
