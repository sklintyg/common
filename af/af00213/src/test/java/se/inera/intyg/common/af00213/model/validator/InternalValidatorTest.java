/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00213.model.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.af00213.model.internal.Af00213Utlatande;
import se.inera.intyg.common.af00213.utils.ScenarioFinder;
import se.inera.intyg.common.af00213.utils.ScenarioNotFoundException;
import se.inera.intyg.common.af00213.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    @Test
    public void testFunktionsnedsattningSaknas() throws ScenarioNotFoundException {
        final int numErrors = 1;
        Af00213Utlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattningSaknas").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testAktivitetsbegransningSaknas() throws ScenarioNotFoundException {
        final int numErrors = 1;
        Af00213Utlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-aktivitetsbegransningSaknas").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testArbetetsPaverkanSaknas() throws ScenarioNotFoundException {
        final int numErrors = 1;
        Af00213Utlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-arbetetsPaverkanSaknas").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
