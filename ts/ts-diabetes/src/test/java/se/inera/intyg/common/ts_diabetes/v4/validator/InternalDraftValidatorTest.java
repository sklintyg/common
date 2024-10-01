/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants;
import se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder;

public class InternalDraftValidatorTest {

    private final InternalDraftValidatorImpl validator = new InternalDraftValidatorImpl();

    @Test
    public void validateMinimalValidUtkast() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertAll(
            () -> assertFalse(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages())
        );
    }

    @Test
    public void validateCompleteValidUtkast() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("pass-complete").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        assertAll(
            () -> assertFalse(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages())
        );
    }

    @Test
    public void failureDueToRule2a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R2a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals("common.validation.d-02", error.getMessage())
        );
    }

    @Test
    public void failureDueToRule2b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R2b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals("common.validation.d-02", error.getMessage())
        );
    }

    @Test
    public void failureDueToRule2c() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R2c").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.INVALID_FORMAT, error.getType()),
            () -> assertEquals("common.validation.b-02b", error.getMessage())
        );
    }

    @Test
    public void failureDueToRule3a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R3a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRule3b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R3b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType())
        );
    }

    @Test
    public void failureDueToRule4() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R4").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRule8() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R8").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var expectedErrors = ImmutableSet.of(
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID),
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_DELSVAR_ID),
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_DELSVAR_ID));

        assertAll(
            () -> assertTrue(res.hasErrorMessages(), "Should have error messages"),
            () -> assertFalse(res.hasWarningMessages(), "Should not have warning messages"),
            () -> assertEquals(3, res.getValidationErrors().size(), "Wrong number of error messages"),
            () -> assertTrue(res.getValidationErrors().containsAll(expectedErrors), "Should have expected error messages")
        );
    }

    @Test
    public void failureDueToRule9() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R9").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        ValidationMessage error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID, error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRule18a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R18a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_BEHANDLING_JSON_ID + "." + ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID,
                error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }


    @Test
    public void failureDueToRule18b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R18b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_CATEGORY_ID + "." + ALLMANT_BEHANDLING_JSON_ID + "." + ALLMANT_BEHANDLING_ANNAN_ANGE_VILKEN_JSON_ID,
                error.getField()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType())
        );
    }

    @Test
    public void failureDueToRule19a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R19a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-11")
        );
    }

    @Test
    public void failureDueToRule19b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R19b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-08")
        );
    }

    @Test
    public void failureDueToRule19c() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R19c").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.INVALID_FORMAT, error.getType())
        );
    }

    @Test
    public void failureDueToRule20a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R20a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-11")
        );
    }

    @Test
    public void failureDueToRule20b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R20b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID, error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-08")
        );
    }

    @Test
    public void failureDueToRule21a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R21a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID,
                error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-11")
        );
    }

    @Test
    public void failureDueToRule21b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R21b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID,
                error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-08")
        );
    }

    @Test
    public void failureDueToRule22a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R22a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID,
                error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-11")
        );
    }

    @Test
    public void failureDueToRule22b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R22b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID,
                error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-08")
        );
    }

    @Test
    public void failureDueToRules23_24() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R23").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(BEDOMNING_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(BEDOMNING_CATEGORY_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID, error.getField()),
            () -> assertEquals(BEDOMNING_SVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType())
        );
    }

    @Test
    public void failureDueToRule27a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R27a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID, error.getField()),
            () -> assertEquals(HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRules27b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R27b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID, error.getField()),
            () -> assertEquals(HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType())
        );
    }

    @Test
    public void failureDueToRule28a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R28a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRule28b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R28b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var expectedErrors = ImmutableSet.of(
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_SVAR_ID),
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_SVAR_ID));

        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(2, res.getValidationErrors().size()),
            () -> assertTrue(res.getValidationErrors().containsAll(expectedErrors))
        );
    }

    @Test
    public void failureDueToRule29a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R29a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(OVRIGT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(OVRIGT_CATEGORY_ID + "." + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID, error.getField()),
            () -> assertEquals(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRule29b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R29b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(OVRIGT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(OVRIGT_CATEGORY_ID + "." + OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID, error.getField()),
            () -> assertEquals(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.OTHER, error.getType())
        );
    }

    @Test
    public void failureDueToRules30a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R30a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRules30b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R30b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var expectedErrors = ImmutableSet.of(
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_SVAR_ID),
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_SVAR_ID),
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_SVAR_ID),
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_SVAR_ID),
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_SVAR_ID),
            ValidationMessage.create(HYPOGLYKEMI_CATEGORY_ID,
                (RespConstants.HYPOGLYKEMI_CATEGORY_ID + '.' + RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID),
                ValidationMessageType.EMPTY, HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_SVAR_ID));

        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(6, res.getValidationErrors().size()),
            () -> assertTrue(res.getValidationErrors().containsAll(expectedErrors))
        );
    }

    @Test
    public void failureDueToRules30c() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R30c").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_CATEGORY_ID + "." + ALLMANT_BEHANDLING_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRules30d() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R30d").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_CATEGORY_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID,
                error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRules32() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R32").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(ALLMANT_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(ALLMANT_JSON_ID + "." + ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID, error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRules33() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R33").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID,
                error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRules34() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R34").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(HYPOGLYKEMI_CATEGORY_ID + "." + HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID,
                error.getField()),
            () -> assertEquals(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_DELSVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }

    @Test
    public void failureDueToRule35a() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R35a").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(BEDOMNING_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(BEDOMNING_CATEGORY_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID, error.getField()),
            () -> assertEquals(BEDOMNING_SVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-12")
        );
    }

    @Test
    public void failureDueToRule35b() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-R35b").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals(BEDOMNING_CATEGORY_ID, error.getCategory()),
            () -> assertEquals(BEDOMNING_CATEGORY_ID + "." + BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID, error.getField()),
            () -> assertEquals(BEDOMNING_SVAR_ID, error.getQuestionId()),
            () -> assertEquals(ValidationMessageType.INCORRECT_COMBINATION, error.getType()),
            () -> assertEquals(error.getMessage(), "common.validation.d-12")
        );
    }

    @Test
    public void failureDueToMissingVardenhetPostnummer() throws Exception {
        final var utlatande = ScenarioFinder.getInternalScenario("fail-vardenhetensPostNummerSaknas").asInternalModel();
        final var res = validator.validateDraft(utlatande);

        final var error = res.getValidationErrors().get(0);
        assertAll(
            () -> assertTrue(res.hasErrorMessages()),
            () -> assertFalse(res.hasWarningMessages()),
            () -> assertEquals(1, res.getValidationErrors().size()),
            () -> assertEquals("vardenhet", error.getCategory()),
            () -> assertEquals("grunddata.skapadAv.vardenhet.postnummer", error.getField()),
            () -> assertEquals(ValidationMessageType.EMPTY, error.getType())
        );
    }
}
