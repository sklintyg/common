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
package se.inera.intyg.common.ts_diabetes.v2.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Joiner;
import java.time.LocalDate;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.utils.Scenario;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioFinder;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioNotFoundException;
import se.inera.intyg.common.ts_diabetes.v2.validator.Validator;

public class InternalValidatorTest {

    private static final int T3 = 3;
    private Validator validator;

    @Before
    public void setUp() throws Exception {
        validator = new Validator();
    }

    @Test
    public void testValidate() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            TsDiabetesUtlatandeV2 utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals(
                "Error in scenario " + scenario.getName() + "\n"
                    + Joiner.on(", ").join(validationResponse.getValidationErrors()),
                ValidationStatus.VALID,
                validationResponse.getStatus());

            assertTrue(
                "Error in scenario " + scenario.getName() + "\n"
                    + Joiner.on(", ").join(validationResponse.getValidationErrors()),
                validationResponse
                    .getValidationErrors().isEmpty());

        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("invalid-*")) {

            TsDiabetesUtlatandeV2 utlatande = scenario.asInternalModel();
            utlatande.getDiabetes().setInsulinBehandlingsperiod(String.valueOf(LocalDate.now().getYear() + 1));
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals(String.format("Error in test: %s", scenario.getName()), ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    @Test
    public void testInvalidSynskarpa() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-korrigerad-synskarpa").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("syn.vanster.medKorrektion", getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidOgonlakarintygSaknasCorrectSortOrder() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-missing-ogonlakarintyg").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
        assertEquals(5, validationResponse.getValidationErrors().size());
        int index = 0;
        assertEquals("syn.synfaltsprovningUtanAnmarkning", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.hoger.utanKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.vanster.utanKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.binokulart.utanKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.diplopi", validationResponse.getValidationErrors().get(index++).getField());
    }

    @Test
    public void testInvalidAllmanDiabetesSaknasCorrectSortOrder() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = buildUtlatandeWithoutDiabetesFieldsSet();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
        assertEquals(3, validationResponse.getValidationErrors().size());
        int index = 0;
        assertEquals("diabetes.observationsperiod", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("diabetes.diabetesTyp", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("diabetes.behandlingsTyp", validationResponse.getValidationErrors().get(index).getField());
    }

    /*
     * Since the validation of this field (Hypoglykemier.AllvarligForekomstVakenTidObservationstid) depends on the actual date,
     * this must be done programmatically and can thus not be tested with the scenario based approach used above.
     */
    @Test
    public void testValidDateHypoglykemi() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-date-format-hypoglykemi").asInternalModel();
        utlatande.getHypoglykemier().setAllvarligForekomstVakenTidObservationstid(new InternalDate(LocalDate.now().minusMonths(6)));
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertTrue(validationResponse.getValidationErrors().isEmpty());
    }

    @Test
    public void testInvalidDateFormatHypoglykemi() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-date-format-hypoglykemi").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("hypoglykemier.allvarligForekomstVakenTidObservationstid",
            getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testDateHypoglykemiMoreThenOneYearInThePast() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-date-format-hypoglykemi").asInternalModel();
        utlatande.getHypoglykemier()
            .setAllvarligForekomstVakenTidObservationstid(new InternalDate(LocalDate.now().minusYears(1).minusDays(1)));
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("hypoglykemier.allvarligForekomstVakenTidObservationstid",
            getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidDiabetesMissing() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-missing-diabetes").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals(T3, validationResponse.getValidationErrors().size());
    }

    @Test
    public void testInvalidDiabetesInsulinperiod() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-insulinperiod").asInternalModel();
        utlatande.getDiabetes().setInsulinBehandlingsperiod(String.valueOf(LocalDate.now().getYear() + 1));
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
        assertEquals("diabetes.insulinBehandlingsperiod",
            getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidMutationsDiabetesInsulinperiod() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-insulinperiod").asInternalModel();
        ValidateDraftResponse validationResponse;

        utlatande.getDiabetes().setInsulinBehandlingsperiod("1111");
        validationResponse = validator.validateDraft(utlatande);
        assertEquals("diabetes.insulinBehandlingsperiod",
            getSingleElement(validationResponse.getValidationErrors()).getField());

        utlatande.getDiabetes().setInsulinBehandlingsperiod("");
        validationResponse = validator.validateDraft(utlatande);
        assertEquals("diabetes.insulinBehandlingsperiod",
            getSingleElement(validationResponse.getValidationErrors()).getField());

        utlatande.getDiabetes().setInsulinBehandlingsperiod("aaaaaaaaaaaaaaa");
        validationResponse = validator.validateDraft(utlatande);
        assertEquals("diabetes.insulinBehandlingsperiod",
            getSingleElement(validationResponse.getValidationErrors()).getField());

    }

    @Test
    public void testInvalidHypoglykemierMissing() throws Exception {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-missing-hypoglykemier-kunskap")
            .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("hypoglykemier.kunskapOmAtgarder", getSingleElement(validationResponse.getValidationErrors())
            .getField());
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

    private TsDiabetesUtlatandeV2 buildUtlatandeWithoutDiabetesFieldsSet() throws ScenarioNotFoundException {
        TsDiabetesUtlatandeV2 utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-insulinperiod").asInternalModel();
        utlatande.getDiabetes().setAnnanBehandlingBeskrivning(null);
        utlatande.getDiabetes().setDiabetestyp(null);
        utlatande.getDiabetes().setEndastKost(null);
        utlatande.getDiabetes().setInsulin(null);
        utlatande.getDiabetes().setInsulinBehandlingsperiod(null);
        utlatande.getDiabetes().setObservationsperiod(null);
        utlatande.getDiabetes().setTabletter(null);
        return utlatande;
    }
}
