/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v6.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.ts_bas.v6.model.internal.Sjukhusvard;
import se.inera.intyg.common.ts_bas.v6.model.internal.TsBasUtlatandeV6;
import se.inera.intyg.common.ts_bas.v6.utils.Scenario;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioFinder;
import se.inera.intyg.common.ts_bas.v6.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ts_bas.v6.validator.TsBasValidator;

public class InternalValidatorTest {

    private TsBasValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new TsBasValidator();
    }

    @Test
    public void testValidate() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            TsBasUtlatandeV6 utlatande = scenario.asInternalModel();
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

            TsBasUtlatandeV6 utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals(ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    @Test
    public void testInvalidDiabetesTyp2MissingBehandling() throws Exception {
        TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-typ2-missing-behandling")
            .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("diabetes.behandlingsTyp", getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidSynskarpa() throws Exception {
        TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("invalid-korrigerad-synskarpa").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("syn.vansterOga.utanKorrektion", getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testFunktionshinderBeskrivningMissing() throws Exception {
        TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("invalid-funktionshinder-beskrivning-missing")
            .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("funktionsnedsattning.beskrivning", getSingleElement(validationResponse.getValidationErrors())
            .getField());
    }

    @Test
    public void testIdentitetMissing() throws Exception {
        TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("invalid-missing-identitet")
            .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("vardkontakt.idkontroll", getSingleElement(validationResponse.getValidationErrors())
            .getField());
    }

    @Test
    public void testSjukhusvardValidationOrder() throws ScenarioNotFoundException {
        TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("valid-sjukhusvard").asInternalModel();
        Boolean sjukhusEllerLakar = utlatande.getSjukhusvard().getSjukhusEllerLakarkontakt();
        Sjukhusvard.Builder sjukhusvard = Sjukhusvard.builder();
        sjukhusvard.setSjukhusEllerLakarkontakt(sjukhusEllerLakar);
        utlatande = utlatande.toBuilder().setSjukhusvard(sjukhusvard.build()).build();

        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
        int index = 0;
        assertEquals("sjukhusvard.tidpunkt", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("sjukhusvard.vardinrattning", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("sjukhusvard.anledning", validationResponse.getValidationErrors().get(index++).getField());
    }

    @Test
    public void testInvalidMinimalR35TestFails() throws Exception {
        TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("fail-minimal-r35")
            .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
        int index = 0;
        assertEquals("syn.hogerOga.medKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.vansterOga.medKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.binokulart.medKorrektion", validationResponse.getValidationErrors().get(index++).getField());
    }

    @Test
    public void testInvalidMinimalTestFails() throws Exception {
        TsBasUtlatandeV6 utlatande = ScenarioFinder.getInternalScenario("fail-annat-felsynskarpa")
            .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
        int index = 0;
        assertEquals("syn.hogerOga.medKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.vansterOga.medKorrektion", validationResponse.getValidationErrors().get(index++).getField());
    }

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
}
