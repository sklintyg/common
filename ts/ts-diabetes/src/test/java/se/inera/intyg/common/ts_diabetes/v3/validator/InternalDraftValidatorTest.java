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
package se.inera.intyg.common.ts_diabetes.v3.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableSet;

import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants;
import se.inera.intyg.common.ts_diabetes.v3.model.internal.TsDiabetesUtlatandeV3;
import se.inera.intyg.common.ts_diabetes.v3.utils.ScenarioFinder;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @InjectMocks
    InternalDraftValidatorImpl validator;

    @Mock
    WebcertModuleService moduleService;

    @Test
    public void validateMinimalValidUtkast() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
    }

    @Test
    public void validateCompleteValidUtkast() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("pass-complete").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
    }

    @Test
    public void failureDueToRule1() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R1").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        Assert.assertEquals(RespConstants.BEDOMNING_JSON_ID, error.getCategory());
        Assert.assertEquals(RespConstants.BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule2() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R2").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("allmant", error.getCategory());
        Assert.assertEquals(RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
    }

    @Test
    public void failureDueToRule3() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R3").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("allmant", error.getCategory());
        Assert.assertEquals(RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule4() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R4").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("allmant", error.getCategory());
        Assert.assertEquals(RespConstants.ALLMANT_BEHANDLING_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType());
    }

    @Test
    public void failureDueToRule5() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R5").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("allmant", error.getCategory());
        Assert.assertEquals(RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule6() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R6").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(10, res.getValidationErrors().size());
        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID,
                        ValidationMessageType.EMPTY),
                new ValidationMessage("hypoglykemier",
                        RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID,
                        ValidationMessageType.EMPTY));
        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule7() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R7").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("allmant", error.getCategory());
        Assert.assertEquals(RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
    }

    @Test
    public void failureDueToRule8() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R8").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("hypoglykemier", error.getCategory());
        Assert.assertEquals(RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule9() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R9").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("hypoglykemier", error.getCategory());
        Assert.assertEquals(RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule10() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R10").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("hypoglykemier", error.getCategory());
        Assert.assertEquals(RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule12() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R12").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(3, res.getValidationErrors().size());
        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY),
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY),
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY));
        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule13() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R13").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(3, res.getValidationErrors().size());
        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY),
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY),
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY));
        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule14() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R14").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(3, res.getValidationErrors().size());
        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY),
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY),
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY));
        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule15() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R15").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(3, res.getValidationErrors().size());
        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY),
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY),
                new ValidationMessage("synfunktion",
                        (RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                        ValidationMessageType.EMPTY));
        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule16() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R16").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("allmant", error.getCategory());
        Assert.assertEquals(RespConstants.ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule17() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R17").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(10, res.getValidationErrors().size());
    }

    @Test
    public void failureDueToRule18() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R18").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("allmant", error.getCategory());
        Assert.assertEquals(RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }
}
