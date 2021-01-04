/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_RISK_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.BEHANDLING_ROOT_FIELD_PATH;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.CATEGORY_ALLMANT;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.CATEGORY_BEDOMNING;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.CATEGORY_HYPOGLYKEMIER;
import static se.inera.intyg.common.ts_diabetes.v3.validator.InternalDraftValidatorImpl.CATEGORY_SYNFUNKTION;

import com.google.common.collect.ImmutableSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
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

//    @Mock
//    WebcertModuleService moduleService;

    private static InternalValidatorHelper internalValidatorHelper = new InternalValidatorHelper();

    public static TsDiabetesUtlatandeV3 setupPassingHypoglykemierDates(TsDiabetesUtlatandeV3 utlatande) {
        internalValidatorHelper.setNowMinusDays(utlatande.getHypoglykemier().getAterkommandeSenasteTidpunkt(), 10);
        internalValidatorHelper.setNowMinusDays(utlatande.getHypoglykemier().getForekomstTrafikTidpunkt(), 10);
        internalValidatorHelper.setNowMinusDays(utlatande.getHypoglykemier().getSenasteTidpunktVaken(), 10);
        return utlatande;
    }

    @Test
    public void validateMinimalValidUtkast() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
    }

    @Test
    public void validateCompleteValidUtkast() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = setupPassingHypoglykemierDates(
            ScenarioFinder.getInternalScenario("pass-complete").asInternalModel());

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
        Assert.assertEquals(CATEGORY_BEDOMNING, error.getCategory());
        Assert.assertEquals(BEDOMNING_JSON_ID + "." + BEDOMNING_LAMPLIGHET_ATT_INNEHA_JSON_ID, error.getField());
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
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID_11, error.getField());
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
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID, error.getField());
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
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule5() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = setupPassingHypoglykemierDates(
            ScenarioFinder.getInternalScenario("fail-R5").asInternalModel());

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + "." + ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID,
            error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule6() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R6").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(2, res.getValidationErrors().size());
        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID,
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID,
                ValidationMessageType.EMPTY));
        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule7() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = setupPassingHypoglykemierDates(
            ScenarioFinder.getInternalScenario("fail-R7a").asInternalModel());

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + "." + ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID,
            error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());

        utlatande = setupPassingHypoglykemierDates(
            ScenarioFinder.getInternalScenario("fail-R7b").asInternalModel());

        res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + "." + ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID,
            error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
    }

    @Test
    public void failureDueToRule8() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = setupPassingHypoglykemierDates(
            ScenarioFinder.getInternalScenario("fail-R8").asInternalModel());

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        Assert.assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule9() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R9").asInternalModel();
        //Test json can't have dynamic dates som we override it here.
        // Maybe we should allow getInternalScenario to have arguments that replaces tfeilds in the json file when reading?
        final String lastMonth = LocalDate.now().minus(1, ChronoUnit.MONTHS).format(
            DateTimeFormatter.ISO_DATE);

        utlatande.getHypoglykemier().getAterkommandeSenasteTidpunkt().setDate(lastMonth);
        utlatande.getHypoglykemier().getForekomstTrafikTidpunkt().setDate(lastMonth);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        Assert.assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule10() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = setupPassingHypoglykemierDates(
            ScenarioFinder.getInternalScenario("fail-R10").asInternalModel());

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        Assert.assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID, error.getField());
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
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
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
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
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
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
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
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.'
                    + SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
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
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
            new ValidationMessage(CATEGORY_ALLMANT,
                BEHANDLING_ROOT_FIELD_PATH + ALLMANT_BEHANDLING_RISK_HYPOGLYKEMI_JSON_ID,
                ValidationMessageType.EMPTY));
        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule17() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R17").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(2, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID,
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID,
                ValidationMessageType.EMPTY));
        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule18() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R18").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(
            ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + "." + ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID,
            error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule19() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = setupPassingHypoglykemierDates(
            ScenarioFinder.getInternalScenario("fail-R19").asInternalModel());
        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(6, res.getValidationErrors().size());

        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (RespConstants.SYNFUNKTION_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.'
                    + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.d-03"),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (RespConstants.SYNFUNKTION_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID + '.'
                    + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.d-03"),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (RespConstants.SYNFUNKTION_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.'
                    + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_UTAN_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.d-03"),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (RespConstants.SYNFUNKTION_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID + '.'
                    + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.d-03"),
            new ValidationMessage(CATEGORY_SYNFUNKTION,
                (RespConstants.SYNFUNKTION_JSON_ID + '.' + RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID + '.'
                    + RespConstants.SYNFUNKTION_SYNSKARPA_VARDEN_MED_KORREKTION_JSON_ID),
                ValidationMessageType.INVALID_FORMAT, "common.validation.d-03"));

        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule20() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R20a").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-11");

        utlatande = ScenarioFinder.getInternalScenario("fail-R20b").asInternalModel();

        res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-08");
    }

    @Test
    public void failureDueToRules21() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R21a").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-11");

        utlatande = ScenarioFinder.getInternalScenario("fail-R21b").asInternalModel();

        res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-08");
    }

    @Test
    public void failureDueToRules22() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R22a").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-11");

        utlatande = ScenarioFinder.getInternalScenario("fail-R22b").asInternalModel();

        res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMIER, error.getCategory());
        assertEquals(HYPOGLYKEMIER_JSON_ID + "." + HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-08");
    }

    @Test
    public void failureDueToRules23_24() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R23").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_BEDOMNING, error.getCategory());
        assertEquals(BEDOMNING_JSON_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType());
    }

    @Test
    public void failureDueToRules25() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R25").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());

        assertEquals(5, res.getValidationErrors().size());

        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                (HYPOGLYKEMIER_JSON_ID + '.' + HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                (HYPOGLYKEMIER_JSON_ID + '.' + HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                (HYPOGLYKEMIER_JSON_ID + '.' + HYPOGLYKEMIER_FORMAGA_VARNINGSTECKEN_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                (HYPOGLYKEMIER_JSON_ID + '.' + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(CATEGORY_HYPOGLYKEMIER,
                (HYPOGLYKEMIER_JSON_ID + '.' + HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID),
                ValidationMessageType.EMPTY));

        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRules26() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-R26").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_SYNFUNKTION, error.getCategory());
        assertEquals(SYNFUNKTION_JSON_ID + "." + SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType());
    }

    @Test
    public void failureDueToMissingVardenhetPostnummer() throws Exception {
        TsDiabetesUtlatandeV3 utlatande = ScenarioFinder.getInternalScenario("fail-vardenhetensPostNummerSaknas").asInternalModel();
        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals("vardenhet", error.getCategory());
        Assert.assertEquals("grunddata.skapadAv.vardenhet.postnummer",
            error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }
}
