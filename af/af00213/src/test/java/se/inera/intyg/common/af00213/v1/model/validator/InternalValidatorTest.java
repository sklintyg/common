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
package se.inera.intyg.common.af00213.v1.model.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import se.inera.intyg.common.af00213.v1.model.internal.Af00213UtlatandeV1;
import se.inera.intyg.common.af00213.v1.utils.ScenarioFinder;
import se.inera.intyg.common.af00213.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.af00213.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.validate.InternalDraftValidator;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class InternalValidatorTest {

  private InternalDraftValidator internalValidator = new InternalDraftValidatorImpl();

  private static int getNumberOfInternalValidationErrors(
      ValidateDraftResponse internalValidationResponse) {
    return internalValidationResponse.getValidationErrors().size();
  }

  @Test
  public void testFunktionsnedsattningSaknas() throws ScenarioNotFoundException {
    final int numErrors = 1;
    Af00213UtlatandeV1 utlatandeFromJson =
        ScenarioFinder.getInternalScenario("fail-funktionsnedsattningSaknas").asInternalModel();
    ValidateDraftResponse internalValidationResponse =
        internalValidator.validateDraft(utlatandeFromJson);
    assertEquals(
        numErrors,
        getNumberOfInternalValidationErrors(internalValidationResponse),
        String.format("Expected %s validation errors", numErrors));
  }

  @Test
  public void testAktivitetsbegransningSaknas() throws ScenarioNotFoundException {
    final int numErrors = 1;
    Af00213UtlatandeV1 utlatandeFromJson =
        ScenarioFinder.getInternalScenario("fail-aktivitetsbegransningSaknas").asInternalModel();
    ValidateDraftResponse internalValidationResponse =
        internalValidator.validateDraft(utlatandeFromJson);
    assertEquals(
        numErrors,
        getNumberOfInternalValidationErrors(internalValidationResponse),
        String.format("Expected %s validation errors", numErrors));
  }

  @Test
  public void testArbetetsPaverkanSaknas() throws ScenarioNotFoundException {
    final int numErrors = 1;
    Af00213UtlatandeV1 utlatandeFromJson =
        ScenarioFinder.getInternalScenario("fail-arbetetsPaverkanSaknas").asInternalModel();
    ValidateDraftResponse internalValidationResponse =
        internalValidator.validateDraft(utlatandeFromJson);
    assertEquals(
        numErrors,
        getNumberOfInternalValidationErrors(internalValidationResponse),
        String.format("Expected %s validation errors", numErrors));
  }

  @Test
  public void testUtredningBehandlingSaknas() throws ScenarioNotFoundException {
    final int numErrors = 1;
    Af00213UtlatandeV1 utlatandeFromJson =
        ScenarioFinder.getInternalScenario("fail-utredningBehandlingSaknas").asInternalModel();
    ValidateDraftResponse internalValidationResponse =
        internalValidator.validateDraft(utlatandeFromJson);
    assertEquals(
        numErrors,
        getNumberOfInternalValidationErrors(internalValidationResponse),
        String.format("Expected %s validation errors", numErrors));
  }
}
