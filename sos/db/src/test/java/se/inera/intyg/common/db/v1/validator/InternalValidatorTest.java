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
package se.inera.intyg.common.db.v1.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.db.v1.model.internal.DbUtlatandeV1;
import se.inera.intyg.common.db.v1.utils.ScenarioFinder;
import se.inera.intyg.common.db.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.schemas.contract.Personnummer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    private static InternalValidatorHelper internalValidatorHelper = new InternalValidatorHelper();

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }

    public static DbUtlatandeV1 setupUtlatandeDates(DbUtlatandeV1 utlatande) {
        internalValidatorHelper.setNowMinusDays(utlatande.getUndersokningDatum(), 4);
        internalValidatorHelper.setNowMinusDays(utlatande.getDodsdatum(), 3);
        internalValidatorHelper.setNowMinusDays(utlatande.getAntraffatDodDatum(), 2);
        return utlatande;
    }

    private DbUtlatandeV1 setupBarnSomAvliditDates(DbUtlatandeV1 utlatande, int daysLived) {
        LocalDate date = LocalDate.now().minusDays(30);
        Personnummer personnummer = Personnummer.createPersonnummer(
                date.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "") + "-4321").get();
        utlatande.getGrundData().getPatient().setPersonId(personnummer);
        utlatande.getDodsdatum().setDate(date.plusDays(daysLived).format(DateTimeFormatter.ISO_LOCAL_DATE));
        return utlatande;
    }

    @Test
    public void testValidateUtkast() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = setupUtlatandeDates(ScenarioFinder.getInternalScenario("pass-1").asInternalModel());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(0, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testR1() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R1").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getUndersokningDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("dodsdatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR2() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("validation-fail-R2").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getUndersokningDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("dodsdatum.month", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR3() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = setupUtlatandeDates(ScenarioFinder.getInternalScenario("fail-R3").asInternalModel());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("db.validation.antraffatDod.dodsdatumSakert", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR3_1() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("validation-fail-R3-1").asInternalModel();
        internalValidatorHelper.setNowMinusDays(utlatandeFromJson.getUndersokningDatum(), 4);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.OTHER, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("common.validation.date.today.or.earlier", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR3_2() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3-2").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getUndersokningDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("db.validation.datum.innanDodsdatum", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR3_4() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("validation-fail-R3-4").asInternalModel();
        internalValidatorHelper.setNowMinusDays(utlatandeFromJson.getDodsdatum(), 3);
        internalValidatorHelper.setNowMinusDays(utlatandeFromJson.getUndersokningDatum(), 4);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR5_1() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = setupUtlatandeDates(ScenarioFinder.getInternalScenario("fail-R5-1").asInternalModel());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("explosivImplantat", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("explosivAvlagsnat", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR5_2() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = setupUtlatandeDates(ScenarioFinder.getInternalScenario("fail-R5-2").asInternalModel());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("explosivImplantat", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("explosivAvlagsnat", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR7() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R7").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getAntraffatDodDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("yttreUndersokning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("undersokningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR7_2() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R7-2").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getUndersokningDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("yttreUndersokning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("undersokningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("db.validation.undersokningDatum.after.dodsdatum", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR7_3() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("validation-fail-R7-3").asInternalModel();
        internalValidatorHelper.setNowMinusDays(utlatandeFromJson.getUndersokningDatum(), 1);
        internalValidatorHelper.setNowMinusDays(utlatandeFromJson.getAntraffatDodDatum(), 2);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("yttreUndersokning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("undersokningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("db.validation.undersokningDatum.after.antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR7_4() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("validation-fail-R7-4").asInternalModel();
        LocalDate antraffatDatum = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 3);
        LocalDate dodsDatum = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 1);
        LocalDate undersokningDatum = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 1).minusDays(3);
        utlatandeFromJson.getAntraffatDodDatum().setDate(antraffatDatum.format(DateTimeFormatter.ISO_LOCAL_DATE));
        utlatandeFromJson.getDodsdatum().setDate(dodsDatum.format(DateTimeFormatter.ISO_LOCAL_DATE));
        utlatandeFromJson.getUndersokningDatum().setDate(undersokningDatum.format(DateTimeFormatter.ISO_LOCAL_DATE));
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.OTHER, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("yttreUndersokning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("undersokningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("db.validation.undersokningDatum.before.beginningOflastYear", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR19() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R19").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getAntraffatDodDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("polisanmalan", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("polisanmalan", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_1() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = setupBarnSomAvliditDates(
                ScenarioFinder.getInternalScenario("fail-R20-1").asInternalModel(), 29);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_2() throws ScenarioNotFoundException {
        DbUtlatandeV1 utlatandeFromJson = setupBarnSomAvliditDates(
                ScenarioFinder.getInternalScenario("fail-R20-2").asInternalModel(), 10);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_3() throws ScenarioNotFoundException {
        // Same as R20_1 but with samordningsnummer
        DbUtlatandeV1 utlatandeFromJson = setupBarnSomAvliditDates(
                ScenarioFinder.getInternalScenario("fail-R20-3").asInternalModel(), 29);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_4() throws ScenarioNotFoundException {
        // Same as R20_2 but with samordningsnummer
        DbUtlatandeV1 utlatandeFromJson = setupBarnSomAvliditDates(
                ScenarioFinder.getInternalScenario("fail-R20-4").asInternalModel(), 10);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }
}
