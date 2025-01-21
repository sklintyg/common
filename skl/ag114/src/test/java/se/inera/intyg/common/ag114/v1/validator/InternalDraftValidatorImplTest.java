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
package se.inera.intyg.common.ag114.v1.validator;

import static org.junit.Assert.assertEquals;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_9;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.CATEGORY_ARBETSFORMAGA;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.CATEGORY_BEDOMNING;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.CATEGORY_DIAGNOS;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.CATEGORY_GRUNDFORMU;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.CATEGORY_KONTAKT;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.CATEGORY_SYSSELSATTNING;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_10;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_2;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_JSON_ID_7_1;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.SJUKSKRIVNINGSPERIOD_SVAR_JSON_ID_7_2;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.TYP_AV_DIAGNOS_SVAR_JSON_ID_4;
import static se.inera.intyg.common.ag114.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.SJUKSKRIVNINGSGRAD_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_DIAGNOS_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID;
import static se.inera.intyg.common.ag114.v1.validator.InternalDraftValidatorImpl.AG114_SJUKSKRIVNINGSGRAD_INVALID_PERCENT;
import static se.inera.intyg.common.ag114.v1.validator.InternalDraftValidatorImpl.COMMON_VALIDATION_DATE_PERIOD_INVALID_ORDER;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.inera.intyg.common.ag114.v1.model.internal.Ag114UtlatandeV1;
import se.inera.intyg.common.ag114.v1.model.validator.TestConfiguration;
import se.inera.intyg.common.ag114.v1.utils.ScenarioFinder;
import se.inera.intyg.common.ag114.v1.utils.ScenarioNotFoundException;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.InternalDraftValidator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class InternalDraftValidatorImplTest {

    @Autowired
    private InternalDraftValidator<Ag114UtlatandeV1> internalValidator;

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }

    @Test
    public void testSuccessMinimalUtlatande() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("pass-minimal");
        assertEquals(0, getNumberOfInternalValidationErrors(res));
    }

    @Test
    public void testSuccessMaxedUtlatande() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("pass-maxed");
        assertEquals(0, getNumberOfInternalValidationErrors(res));
    }

    @Test
    public void testFailsWhenMissingGrundForMU() throws ScenarioNotFoundException {
        Ag114UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-missing-grundformu").asInternalModel();

        ValidateDraftResponse res = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_GRUNDFORMU, error.getCategory());
        Assert.assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_JSON_ID_10, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenMissingSysselsattning() throws ScenarioNotFoundException {
        Ag114UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-missing-sysselsattning").asInternalModel();

        ValidateDraftResponse res = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_SYSSELSATTNING, error.getCategory());
        Assert.assertEquals(TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(TYP_AV_SYSSELSATTNING_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenMissingNuvarandeArbete() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-missing-nuvarandearbete");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_SYSSELSATTNING, error.getCategory());
        Assert.assertEquals(NUVARANDE_ARBETE_SVAR_JSON_ID_2, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(NUVARANDE_ARBETE_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenMissingFormedlaDiagnos() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-missing-diagnosformedling");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_DIAGNOS, error.getCategory());
        Assert.assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenMissingDiagnoserButOnskarFormedla_R14() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-missing-diagnoser");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_DIAGNOS, error.getCategory());
        Assert.assertEquals(TYP_AV_DIAGNOS_SVAR_JSON_ID_4, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(TYP_AV_DIAGNOS_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenMissingDiagnosKod1ButOnskarFormedla_R14() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-missing-diagnos1");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_DIAGNOS, error.getCategory());
        Assert.assertEquals(TYP_AV_DIAGNOS_SVAR_JSON_ID_4 + "[1].diagnosbeskrivning", error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(TYP_AV_DIAGNOS_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenMissingNedsattArbetsformaga() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-missing-arbetsformaga");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_ARBETSFORMAGA, error.getCategory());
        Assert.assertEquals(NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(NEDSATT_ARBETSFORMAGA_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenMissingSjukskrivningsgrad() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-missing-sjukskrivningsgrad");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_BEDOMNING, error.getCategory());
        Assert.assertEquals(SJUKSKRIVNINGSGRAD_SVAR_JSON_ID_7_1, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(SJUKSKRIVNINGSGRAD_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenInvalidSjukskrivningsgrad() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-invalid-sjukskrivningsgrad");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_BEDOMNING, error.getCategory());
        Assert.assertEquals(SJUKSKRIVNINGSGRAD_SVAR_JSON_ID_7_1, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(AG114_SJUKSKRIVNINGSGRAD_INVALID_PERCENT, error.getMessage());
        assertEquals(SJUKSKRIVNINGSGRAD_SVAR_ID, error.getQuestionId());

    }

    @Test
    public void testFailsWhenInvalidSjukskrivningsperiod() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-invalid-sjukskrivningsperiod");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_BEDOMNING, error.getCategory());
        Assert.assertEquals(SJUKSKRIVNINGSPERIOD_SVAR_JSON_ID_7_2 + ".tom", error.getField());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType());
        assertEquals(COMMON_VALIDATION_DATE_PERIOD_INVALID_ORDER, error.getMessage());
        assertEquals(SJUKSKRIVNINGSGRAD_PERIOD_SVAR_ID, error.getQuestionId());
    }

    @Test
    public void testFailsWhenMissingAnledningTillKontakt() throws ScenarioNotFoundException {
        ValidateDraftResponse res = validateScenarioFile("fail-missing-kontaktanledning");

        assertEquals(1, getNumberOfInternalValidationErrors(res));
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(CATEGORY_KONTAKT, error.getCategory());
        Assert.assertEquals(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_9, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
        assertEquals(ANLEDNING_TILL_KONTAKT_DELSVAR_ID, error.getQuestionId());
    }

    private ValidateDraftResponse validateScenarioFile(String scenario) throws ScenarioNotFoundException {
        Ag114UtlatandeV1 utlatandeFromJson = ScenarioFinder.getInternalScenario(scenario).asInternalModel();
        return internalValidator.validateDraft(utlatandeFromJson);
    }
}
