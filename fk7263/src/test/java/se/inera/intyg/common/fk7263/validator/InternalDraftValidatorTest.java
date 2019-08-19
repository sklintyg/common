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
package se.inera.intyg.common.fk7263.validator;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.fk7263.model.internal.Fk7263Utlatande;
import se.inera.intyg.common.fk7263.utils.Scenario;
import se.inera.intyg.common.fk7263.utils.ScenarioFinder;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @Mock
    private WebcertModuleService mockModuleService;

    @InjectMocks
    private InternalDraftValidator validator = new InternalDraftValidator();

    /**
     * Utility method for getting a single element from a collection.
     *
     * @param collection the collection
     * @return a single element, throws IllegalArgumentException in case the collection contains more than one element
     */
    public static <T> T getSingleElement(Collection<T> collection) {
        if (collection.size() != 1) {
            throw new java.lang.IllegalArgumentException("Expected collection with exactly one element");
        }
        return collection.iterator().next();
    }

    @Before
    public void setUpModuleServiceExpectation() {
        Mockito.when(mockModuleService.validateDiagnosisCode(Mockito.argThat(new DiagnosKodArgmentMatcher()), Mockito.anyString()))
                .thenReturn(true);
    }

    @Test
    public void testValidate() throws Exception {
        for (se.inera.intyg.common.fk7263.utils.Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Fk7263Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
            assertEquals(
                    "Error in scenario " + scenario.getName() + "\n"
                            + Joiner.on(", ").join(validationResponse.getValidationErrors()),
                    ValidationStatus.VALID, validationResponse.getStatus());

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + Joiner.on(", ").join(validationResponse.getValidationErrors()), validationResponse
                            .getValidationErrors().isEmpty());

        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("invalid-*")) {

            Fk7263Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals("Error in scenario " + scenario.getName() + "\n",
                    ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    /**
     * ArgumentMatcher that validates a supplied diagnos code argument. If its not in the list, it is invalid!
     *
     * @author npet
     */
    class DiagnosKodArgmentMatcher implements ArgumentMatcher<String> {

        private List<String> ALLOWED_CODES = Arrays.asList("S47", "TEST1", "TEST2", "TEST3", "Z233", "A000");

        @Override
        public boolean matches(String arg) {
            return ALLOWED_CODES.contains(arg);
        }
    }
}
