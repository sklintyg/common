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
package se.inera.intyg.common.ts_diabetes.v4.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.validator.InternalDraftValidatorImpl.CATEGORY_ALLMANT;
import static se.inera.intyg.common.ts_diabetes.v4.validator.InternalDraftValidatorImpl.CATEGORY_BEDOMNING;
import static se.inera.intyg.common.ts_diabetes.v4.validator.InternalDraftValidatorImpl.CATEGORY_HYPOGLYKEMI;
import static se.inera.intyg.common.ts_diabetes.v4.validator.InternalDraftValidatorImpl.CATEGORY_OVRIGT;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @InjectMocks
    InternalDraftValidatorImpl validator;


    @Test
    public void validateMinimalValidUtkast() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
    }

    @Test
    public void validateCompleteValidUtkast() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = ScenarioFinder.getInternalScenario("pass-complete").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
    }

    @Test
    public void failureDueToRule2() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R2").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
    }

    @Test
    public void failureDueToRule3() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R3").asInternalModel();

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
        TsDiabetesUtlatandeV4 utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R4").asInternalModel();

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
    public void failureDueToRule8() throws Exception {
        final var utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R8").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(3, res.getValidationErrors().size());

        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID),
                ValidationMessageType.EMPTY));

        assertTrue(res.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule9() throws Exception {
        final var utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R9").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        Assert.assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule18() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R18").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        Assert.assertEquals(
            ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + "." + ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID,
            error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule19a() throws Exception {
        final var utlatande19 = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R19a").asInternalModel();
        final var res19 = validator.validateDraft(utlatande19);

        assertTrue(res19.hasErrorMessages());
        assertFalse(res19.hasWarningMessages());
        assertEquals(1, res19.getValidationErrors().size());
        ValidationMessage error = res19.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-11");

    }

    @Test
    public void failureDueToRule19b() throws Exception {
        final var utlatande19 = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R19b").asInternalModel();
        final var res19 = validator.validateDraft(utlatande19);

        assertTrue(res19.hasErrorMessages());
        assertFalse(res19.hasWarningMessages());
        assertEquals(1, res19.getValidationErrors().size());
        ValidationMessage error = res19.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-08");
    }

    @Test
    public void failureDueToRule20() throws Exception {
        final var utlatande20a = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R20a").asInternalModel();
        final var res20a = validator.validateDraft(utlatande20a);

        assertTrue(res20a.hasErrorMessages());
        assertFalse(res20a.hasWarningMessages());
        assertEquals(1, res20a.getValidationErrors().size());
        ValidationMessage error = res20a.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-11");

        final var utlatande20b = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R20b").asInternalModel();
        final var res20b = validator.validateDraft(utlatande20b);

        assertTrue(res20b.hasErrorMessages());
        assertFalse(res20b.hasWarningMessages());
        assertEquals(1, res20b.getValidationErrors().size());
        error = res20b.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-08");
    }

    @Test
    public void failureDueToRule21() throws Exception {
        final var utlatande21a = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R21a").asInternalModel();
        final var res21a = validator.validateDraft(utlatande21a);

        assertTrue(res21a.hasErrorMessages());
        assertFalse(res21a.hasWarningMessages());
        assertEquals(1, res21a.getValidationErrors().size());
        ValidationMessage error = res21a.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-11");

        final var utlatande21b = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R21b").asInternalModel();
        final var res21b = validator.validateDraft(utlatande21b);

        assertTrue(res21b.hasErrorMessages());
        assertFalse(res21b.hasWarningMessages());
        assertEquals(1, res21b.getValidationErrors().size());
        error = res21b.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-08");
    }

    @Test
    public void failureDueToRule22() throws Exception {
        final var utlatande22a = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R22a").asInternalModel();
        final var res22a = validator.validateDraft(utlatande22a);

        assertTrue(res22a.hasErrorMessages());
        assertFalse(res22a.hasWarningMessages());
        assertEquals(1, res22a.getValidationErrors().size());
        ValidationMessage error = res22a.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-11");

        final var utlatande22b = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R22b").asInternalModel();
        final var res22b = validator.validateDraft(utlatande22b);

        assertTrue(res22b.hasErrorMessages());
        assertFalse(res22b.hasWarningMessages());
        assertEquals(1, res22b.getValidationErrors().size());
        error = res22b.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-08");
    }

    @Test
    public void failureDueToRules23_24() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R23").asInternalModel();

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
    public void failureDueToRule27a() throws Exception {
        final var utlatande27 = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R27a").asInternalModel();
        final var res27 = validator.validateDraft(utlatande27);

        assertTrue(res27.hasErrorMessages());
        assertFalse(res27.hasWarningMessages());
        assertEquals(1, res27.getValidationErrors().size());
        ValidationMessage error = res27.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRules27b() throws Exception {
        final var utlatande27 = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R27b").asInternalModel();
        final var res27 = validator.validateDraft(utlatande27);

        assertTrue(res27.hasErrorMessages());
        assertFalse(res27.hasWarningMessages());
        assertEquals(1, res27.getValidationErrors().size());
        ValidationMessage error = res27.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.OTHER, error.getType());
    }

    @Test
    public void failureDueToRule28() throws Exception {
        final var utlatande28a = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R28a").asInternalModel();
        final var res28a = validator.validateDraft(utlatande28a);

        assertTrue(res28a.hasErrorMessages());
        assertFalse(res28a.hasWarningMessages());
        assertEquals(1, res28a.getValidationErrors().size());
        ValidationMessage error1 = res28a.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error1.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID, error1.getField());
        assertEquals(ValidationMessageType.EMPTY, error1.getType());

        final var utlatande28b = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R28b").asInternalModel();
        final var res28b = validator.validateDraft(utlatande28b);

        assertTrue(res28b.hasErrorMessages());
        assertFalse(res28b.hasWarningMessages());
        assertEquals(2, res28b.getValidationErrors().size());

        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID),
                ValidationMessageType.EMPTY));

        assertTrue(res28b.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRule29() throws Exception {
        final var utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R29").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_OVRIGT, error.getCategory());
        assertEquals(OVRIGT_JSON_ID + "." + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRules30a() throws Exception {
        final var utlatande30 = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R30a").asInternalModel();
        final var res30 = validator.validateDraft(utlatande30);

        assertTrue(res30.hasErrorMessages());
        assertFalse(res30.hasWarningMessages());
        assertEquals(1, res30.getValidationErrors().size());
        ValidationMessage error = res30.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRules30b() throws Exception {
        final var utlatande30 = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R30b").asInternalModel();
        final var res30 = validator.validateDraft(utlatande30);

        assertTrue(res30.hasErrorMessages());
        assertFalse(res30.hasWarningMessages());
        assertEquals(6, res30.getValidationErrors().size());

        ImmutableSet<ValidationMessage> expectedErrors = ImmutableSet.of(
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID),
                ValidationMessageType.EMPTY),
            new ValidationMessage(HYPOGLYKEMI_JSON_ID,
                (RespConstants.HYPOGLYKEMI_JSON_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID),
                ValidationMessageType.EMPTY));

        assertTrue(res30.getValidationErrors().containsAll(expectedErrors));
    }

    @Test
    public void failureDueToRules30c() throws Exception {
        final var utlatande30 = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R30c").asInternalModel();
        final var res30 = validator.validateDraft(utlatande30);

        assertTrue(res30.hasErrorMessages());
        assertFalse(res30.hasWarningMessages());
        assertEquals(1, res30.getValidationErrors().size());
        ValidationMessage error = res30.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        assertEquals(CATEGORY_ALLMANT + "." + ALLMANT_BEHANDLING_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRules32() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R32").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_ALLMANT, error.getCategory());
        assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRules33() throws Exception {
        final var utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R33").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());

        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRules34() throws Exception {
        final var utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R34").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());

        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_HYPOGLYKEMI, error.getCategory());
        assertEquals(HYPOGLYKEMI_JSON_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.EMPTY, error.getType());
    }

    @Test
    public void failureDueToRule35a() throws Exception {
        final var utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R35a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_BEDOMNING, error.getCategory());
        assertEquals(BEDOMNING_JSON_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-12");
    }

    @Test
    public void failureDueToRule35b() throws Exception {
        final var utlatande = se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder.getInternalScenario("fail-R35b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertTrue(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
        assertEquals(1, res.getValidationErrors().size());
        ValidationMessage error = res.getValidationErrors().get(0);
        assertEquals(CATEGORY_BEDOMNING, error.getCategory());
        assertEquals(BEDOMNING_JSON_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID, error.getField());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType());
        assertEquals(error.getMessage(), "common.validation.d-12");
    }

    @Test
    public void failureDueToMissingVardenhetPostnummer() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = ScenarioFinder.getInternalScenario("fail-vardenhetensPostNummerSaknas").asInternalModel();
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
