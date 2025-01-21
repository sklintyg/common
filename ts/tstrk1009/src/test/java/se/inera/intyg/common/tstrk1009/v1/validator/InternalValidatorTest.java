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
package se.inera.intyg.common.tstrk1009.v1.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.tstrk1009.v1.model.internal.Tstrk1009UtlatandeV1;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioFinder;
import se.inera.intyg.common.tstrk1009.v1.utils.ScenarioNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    private InternalDraftValidatorImpl testee = new InternalDraftValidatorImpl();

    @Test
    public void minimalValidTstrk1009ShouldPass() throws ScenarioNotFoundException {
        // Given
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-min").asInternalModel();

        // When
        ValidateDraftResponse vdr = testee.validateDraft(utlatande);

        // Then
        assertThat(vdr.getStatus()).isEqualTo(ValidationStatus.VALID);
        assertThat(vdr.getValidationErrors()).isEmpty();
        assertThat(vdr.getValidationWarnings()).isEmpty();
    }

    @Test
    public void maximumValidTstrk1009ShouldPass() throws ScenarioNotFoundException {
        // Given
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("valid-max").asInternalModel();

        // When
        ValidateDraftResponse vdr = testee.validateDraft(utlatande);

        // Then
        assertThat(vdr.getStatus()).isEqualTo(ValidationStatus.VALID);
        assertThat(vdr.getValidationErrors()).isEmpty();
        assertThat(vdr.getValidationWarnings()).isEmpty();
    }

    @Test
    public void missingMandatoryAnswersShouldFail() throws ScenarioNotFoundException {
        // Given
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("fail-mandatory-missing").asInternalModel();

        // When
        ValidateDraftResponse vdr = testee.validateDraft(utlatande);

        // Then
        assertThat(vdr.getStatus()).isEqualTo(ValidationStatus.INVALID);
        assertThat(vdr.getValidationErrors())
            .hasSize(5)
            .extracting("category", "field", "type")
            .contains(tuple("identitet", "identitetStyrktGenom.typ", ValidationMessageType.EMPTY),
                tuple("anmalan", "anmalanAvser.typ", ValidationMessageType.EMPTY),
                tuple("medicinskaforhallanden", "medicinskaForhallanden", ValidationMessageType.EMPTY),
                tuple("medicinskaforhallanden", "senasteUndersokningsdatum", ValidationMessageType.EMPTY),
                tuple("bedomning", "intygetAvserBehorigheter.typer", ValidationMessageType.EMPTY));
        assertThat(vdr.getValidationWarnings()).isEmpty();
    }

    // R2
    @Test
    public void choosingBehorighetAllaAndOthersShouldFail() throws ScenarioNotFoundException {
        // Given
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("fail-alla-behorighet").asInternalModel();

        // When
        ValidateDraftResponse vdr = testee.validateDraft(utlatande);

        // Then
        assertThat(vdr.getStatus()).isEqualTo(ValidationStatus.INVALID);
        assertThat(vdr.getValidationErrors())
            .hasSize(1)
            .extracting("category", "field", "type")
            .contains(tuple("bedomning", "intygetAvserBehorigheter.typer", ValidationMessageType.OTHER));
        assertThat(vdr.getValidationWarnings()).isEmpty();
    }

    // R8
    @Test
    public void choosingBehorighetKanintetastallningAndOthersShouldFail() throws ScenarioNotFoundException {
        // Given
        Tstrk1009UtlatandeV1 utlatande = ScenarioFinder.getInternalScenario("fail-kanintetastallning-behorighet").asInternalModel();

        // When
        ValidateDraftResponse vdr = testee.validateDraft(utlatande);

        // Then
        assertThat(vdr.getStatus()).isEqualTo(ValidationStatus.INVALID);
        assertThat(vdr.getValidationErrors())
            .hasSize(1)
            .extracting("category", "field", "type")
            .contains(tuple("bedomning", "intygetAvserBehorigheter.typer", ValidationMessageType.OTHER));
        assertThat(vdr.getValidationWarnings()).isEmpty();
    }
}
