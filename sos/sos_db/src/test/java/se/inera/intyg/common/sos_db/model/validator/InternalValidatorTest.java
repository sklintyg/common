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
package se.inera.intyg.common.sos_db.model.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.common.sos_db.model.internal.DbUtlatande;
import se.inera.intyg.common.sos_db.utils.ScenarioFinder;
import se.inera.intyg.common.sos_db.utils.ScenarioNotFoundException;
import se.inera.intyg.common.sos_db.validator.InternalDraftValidatorImpl;
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
        assertEquals("DB.dodsdatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR3() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DB.antraffatDod", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR5() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R5").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DB.explosivAvlagsnat", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR6() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R6").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DB.undersokningDetaljer", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR7() throws ScenarioNotFoundException {
        DbUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R7").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DB.undersokningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }
}
