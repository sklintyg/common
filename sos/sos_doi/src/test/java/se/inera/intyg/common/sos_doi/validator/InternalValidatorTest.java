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
package se.inera.intyg.common.sos_doi.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.common.sos_doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.sos_doi.utils.ScenarioFinder;
import se.inera.intyg.common.sos_doi.utils.ScenarioNotFoundException;
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
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(0, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testR1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.dodsdatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR3_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.antraffatDod", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR3_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.antraffatDod", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR13_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.operationDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR13_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.operationDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR13_3() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-3").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.operationDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR14_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R14-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.forgiftningOrsak", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR16_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R16-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.forgiftningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR16_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R16-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.forgiftningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR17() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R17").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.forgiftningUppkommelse", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR18_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R18-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.grunder", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR18_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R18-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("DOI.grunder", internalValidationResponse.getValidationErrors().get(0).getField());
    }
}
