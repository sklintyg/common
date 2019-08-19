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
package se.inera.intyg.common.af00251.v1.model.validator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static se.inera.intyg.common.af00251.v1.utils.Asserts.assertValidationMessage;
import static se.inera.intyg.common.af00251.v1.utils.Asserts.assertValidationMessages;
import static se.inera.intyg.common.af00251.v1.validator.InternalDraftValidatorImpl.CATEGORY_ARBETSMARKNADS_PROGRAM;
import static se.inera.intyg.common.af00251.v1.validator.InternalDraftValidatorImpl.CATEGORY_BEDOMNING;
import static se.inera.intyg.common.af00251.v1.validator.InternalDraftValidatorImpl.CATEGORY_KONSEKVENSER;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants;
import se.inera.intyg.common.af00251.v1.model.internal.AF00251UtlatandeV1;
import se.inera.intyg.common.af00251.v1.utils.ScenarioFinder;
import se.inera.intyg.common.af00251.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.af00251.v1.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    private static final String DOT = ".";
    private static final String FIRST_ELEMENT_IN_ARRAY = "[0]";
    private InternalDraftValidator internalValidator = new InternalDraftValidatorImpl();

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors()
            .size();
    }

    @Test
    public void testFunktionsnedsattningSaknas() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattningSaknas")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
        assertValidationMessage(internalValidationResponse.getValidationErrors()
                .get(0),
            is(CATEGORY_KONSEKVENSER), is("funktionsnedsattning"), is(ValidationMessageType.EMPTY));
    }

    @Test
    public void testArbetsMarknadsPolitisktProgramSaknas() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-arbetsMarknadsPolititisktProgramSaknas")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
        assertValidationMessage(internalValidationResponse.getValidationErrors()
                .get(0),
            is(CATEGORY_ARBETSMARKNADS_PROGRAM), is(AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void testAktivitetsbegransningSaknas() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-aktivitetsbegransningSaknas")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
    }

    @Test
    public void testArbetetsPaverkanSaknas() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-begransningSjukfranvaroSaknas")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
    }

    @Test
    public void testMedicinsktUnderlagSaknas() throws ScenarioNotFoundException {
        final int numErrors = 1;
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-medicinsktUnderlagSaknas")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors,
            getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testOmfattningDeltidSaknas() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-omfattningDeltidSaknas")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
        assertValidationMessage(internalValidationResponse.getValidationErrors()
                .get(0),
            is(CATEGORY_ARBETSMARKNADS_PROGRAM), is(AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2 + DOT +
                AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23),
            is(ValidationMessageType.EMPTY));
    }

    @Test
    public void testOmfattningDeltid0() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-omfattningDeltid0")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
        assertValidationMessage(internalValidationResponse.getValidationErrors()
                .get(0),
            is(CATEGORY_ARBETSMARKNADS_PROGRAM), is(AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2 + DOT +
                AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23),
            is(ValidationMessageType.INVALID_FORMAT));
    }

    @Test
    public void testOmfattningDeltid40() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-omfattningDeltid40")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
        assertValidationMessage(internalValidationResponse.getValidationErrors()
                .get(0),
            is(CATEGORY_ARBETSMARKNADS_PROGRAM), is(AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2 + DOT +
                AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23),
            is(ValidationMessageType.INVALID_FORMAT));
    }

    @Test
    public void testSjukfranvaroniva0() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-sjukfranvaroniva0")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
        assertValidationMessage(internalValidationResponse.getValidationErrors()
                .get(0),
            is(CATEGORY_BEDOMNING), is(AF00251RespConstants.SJUKFRANVARO_SVAR_JSON_ID_6 + FIRST_ELEMENT_IN_ARRAY + DOT +
                AF00251RespConstants.SJUKFRANVARO_SVAR_JSON_ID_61),
            is(ValidationMessageType.INVALID_FORMAT));
    }

    @Test
    public void testSjukfranvaroniva101() throws ScenarioNotFoundException {
        AF00251UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-sjukfranvaroniva101")
            .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertValidationMessages(internalValidationResponse.getValidationErrors(), 1);
        assertValidationMessage(internalValidationResponse.getValidationErrors()
                .get(0),
            is(CATEGORY_BEDOMNING), is(AF00251RespConstants.SJUKFRANVARO_SVAR_JSON_ID_6 + FIRST_ELEMENT_IN_ARRAY + DOT +
                AF00251RespConstants.SJUKFRANVARO_SVAR_JSON_ID_61),
            is(ValidationMessageType.INVALID_FORMAT));
    }
}
