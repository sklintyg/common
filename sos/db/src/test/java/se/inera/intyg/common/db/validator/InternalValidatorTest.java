/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.db.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.common.db.model.internal.DbUtlatande;
import se.inera.intyg.common.db.utils.ScenarioFinder;
import se.inera.intyg.common.db.utils.ScenarioNotFoundException;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }

    @Test
    public void testValidateUtkast() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(0, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testR1() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats.dodsdatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR2() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats.dodsdatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR3_1() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats.antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR3_2() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats.antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR5_1() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R5-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("explosivImplantat.explosivAvlagsnat", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR5_2() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R5-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("explosivImplantat.explosivAvlagsnat", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR7() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R7").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("yttreUndersokning.undersokningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR7_2() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R7-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("yttreUndersokning.undersokningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("db.validation.undersokningDatum.after.dodsdatum", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR19() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R19").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("polisanmalan.polisanmalan", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_1() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R20-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit.barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_2() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R20-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit.barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_3() throws ScenarioNotFoundException {
        // Same as R20_1 but with samordningsnummer
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R20-3").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit.barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_4() throws ScenarioNotFoundException {
        // Same as R20_2 but with samordningsnummer
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R20-4").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit.barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }
}
