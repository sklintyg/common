/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.doi.v1.validator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_DATUM_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_DELSVAR_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_DELSVAR_ID;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.doi.v1.model.internal.DoiUtlatandeV1;
import se.inera.intyg.common.doi.v1.utils.ScenarioFinder;
import se.inera.intyg.common.doi.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.doi.validator.InternalValidatorHelper;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadEnum;
import se.inera.intyg.common.support.modules.support.facade.TypeAheadProvider;
import se.inera.intyg.schemas.contract.Personnummer;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorImplTest {

    @Mock
    private TypeAheadProvider typeAheadProvider;
    
    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    private static InternalValidatorHelper internalValidatorHelper = new InternalValidatorHelper();

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }

    private DoiUtlatandeV1 setupBarnSomAvliditDates(DoiUtlatandeV1 utlatande, int daysLived) {
        LocalDate date = LocalDate.now().minusDays(30);
        Personnummer personnummer = Personnummer.createPersonnummer(
            date.format(DateTimeFormatter.ISO_LOCAL_DATE).replace("-", "") + "-4321").get();
        utlatande.getGrundData().getPatient().setPersonId(personnummer);
        utlatande.getDodsdatum().setDate(date.plusDays(daysLived).format(DateTimeFormatter.ISO_LOCAL_DATE));
        utlatande.getOperationDatum().setDate(date.plusDays(5).format(DateTimeFormatter.ISO_LOCAL_DATE));
        return utlatande;
    }

    @Before
    public void setup() {
        when(typeAheadProvider.getValues(TypeAheadEnum.MUNICIPALITIES))
            .thenReturn(List.of("kommun", "NACKA", "sdf"));
    }

    @Test
    public void testValidateUtkast() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-1").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(0, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testValidateUtkast2() throws ScenarioNotFoundException {
        // bidragandeSjukdomar datum och specifikation (10.2 och 10.3) är ej obligatoriska
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-2").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(0, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testR1() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R1").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("dodsdatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(DODSDATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR3() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getAntraffatDodDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals("doi.validation.antraffatDod.dodsdatumSakert", internalValidationResponse.getValidationErrors().getFirst().getMessage());
        assertEquals(ANTRAFFAT_DOD_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR3_1() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("validation-fail-R3-1").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.OTHER, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals("common.validation.date.today.or.earlier", internalValidationResponse.getValidationErrors().getFirst().getMessage());
        assertEquals(ANTRAFFAT_DOD_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR3_2() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3-2").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals("doi.validation.datum.innanDodsdatum", internalValidationResponse.getValidationErrors().getFirst().getMessage());
        assertEquals(ANTRAFFAT_DOD_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR3_4() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("no-round-trip-fail-R3-4").asInternalModel();
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToCurrentYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(ANTRAFFAT_DOD_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR13_1() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-1").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("operation", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("operationDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(OPERATION_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());

    }

    @Test
    public void testR13_2() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-2").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("operation", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("operationDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(OPERATION_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR13_3() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-3").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("operation", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("operationDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(OPERATION_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR13_5() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-5").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("operation", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("operationAnledning", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(OPERATION_ANLEDNING_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());

    }

    @Test
    public void testR14_1() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R14-1").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("forgiftningOrsak", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(FORGIFTNING_ORSAK_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());

    }

    @Test
    public void testR14_2() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R14-2").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(3, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals("doi.validation.forgiftning.orsak.incorrect_combination",
            internalValidationResponse.getValidationErrors().getFirst().getMessage());
        assertEquals(FORGIFTNING_ORSAK_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(1).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(1).getCategory());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(1).getField());
        assertEquals("doi.validation.forgiftning.datum.incorrect_combination",
            internalValidationResponse.getValidationErrors().get(1).getMessage());
        assertEquals(FORGIFTNING_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().get(1).getQuestionId());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(2).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(2).getCategory());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(2).getField());
        assertEquals("doi.validation.forgiftning.uppkommelse.incorrect_combination",
            internalValidationResponse.getValidationErrors().get(2).getMessage());
        assertEquals(FORGIFTNING_UPPKOMMELSE_DELSVAR_ID, internalValidationResponse.getValidationErrors().get(2).getQuestionId());

    }

    @Test
    public void testR16_1() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R16-1").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("forgiftningDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(FORGIFTNING_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR16_2() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R16-2").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getForgiftningDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("forgiftningDatum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(FORGIFTNING_DATUM_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR17() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R17").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("forgiftningUppkommelse", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(FORGIFTNING_UPPKOMMELSE_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR18_1() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R18-1").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("dodsorsakgrund", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("grunder", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(GRUNDER_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR18_2() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R18-2").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getOperationDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("dodsorsakgrund", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("grunder", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(GRUNDER_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR20_1() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = setupBarnSomAvliditDates(
            ScenarioFinder.getInternalScenario("fail-R20-1").asInternalModel(), 29);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(BARN_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR20_2() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = setupBarnSomAvliditDates(
            ScenarioFinder.getInternalScenario("fail-R20-2").asInternalModel(), 10);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(BARN_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR20_3() throws ScenarioNotFoundException {
        // Same as R20_1 but with samordningsnummer
        DoiUtlatandeV1 utlatandeFromJson = setupBarnSomAvliditDates(
            ScenarioFinder.getInternalScenario("fail-R20-3").asInternalModel(), 29);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(BARN_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR20_4() throws ScenarioNotFoundException {
        // Same as R20_2 but with samordningsnummer
        DoiUtlatandeV1 utlatandeFromJson = setupBarnSomAvliditDates(
            ScenarioFinder.getInternalScenario("fail-R20-4").asInternalModel(), 10);
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(BARN_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR22_1() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R22-1").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getTerminalDodsorsak().getDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("utlatandeOrsak", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("terminalDodsorsak.datum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(DODSORSAK_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }

    @Test
    public void testR22_2() throws ScenarioNotFoundException {
        DoiUtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R22-2").asInternalModel();
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getDodsdatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getAntraffatDodDatum());
        internalValidatorHelper.setDateToLastYear(utlatandeFromJson.getTerminalDodsorsak().getDatum());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson, typeAheadProvider);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().getFirst().getType());
        assertEquals("utlatandeOrsak", internalValidationResponse.getValidationErrors().getFirst().getCategory());
        assertEquals("terminalDodsorsak.datum", internalValidationResponse.getValidationErrors().getFirst().getField());
        assertEquals(DODSORSAK_DELSVAR_ID, internalValidationResponse.getValidationErrors().getFirst().getQuestionId());
    }
}
