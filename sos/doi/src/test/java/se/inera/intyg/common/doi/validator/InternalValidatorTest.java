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
package se.inera.intyg.common.doi.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.doi.model.internal.DoiUtlatande;
import se.inera.intyg.common.doi.utils.ScenarioFinder;
import se.inera.intyg.common.doi.utils.ScenarioNotFoundException;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v3.RegisterCertificateType;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }

    @Test
    public void testValidateUtkast() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(0, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testValidateUtkast2() throws ScenarioNotFoundException {
        // bidragandeSjukdomar datum och specifikation (10.2 och 10.3) är ej obligatoriska
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(0, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testR1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("dodsdatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR3() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("doi.validation.antraffatDod.dodsdatumSakert", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR3_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("validation-fail-R3-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.OTHER, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("common.validation.date.today.or.earlier", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR3_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("doi.validation.datum.innanDodsdatum", internalValidationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testR3_4() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R3-4").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INVALID_FORMAT, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsdatumOchdodsPlats", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("antraffatDodDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR13_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("operation", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("operationDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR13_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("operation", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("operationDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR13_3() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-3").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("operation", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("operationDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR13_5() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R13-5").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("operation", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("operationAnledning", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR14_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R14-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("forgiftningOrsak", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR14_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R14-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(3, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(0).getField());
        assertEquals("doi.validation.forgiftning.orsak.incorrect_combination", internalValidationResponse.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(1).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(1).getCategory());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(1).getField());
        assertEquals("doi.validation.forgiftning.datum.incorrect_combination", internalValidationResponse.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(2).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(2).getCategory());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(2).getField());
        assertEquals("doi.validation.forgiftning.uppkommelse.incorrect_combination", internalValidationResponse.getValidationErrors().get(2).getMessage());
    }

    @Test
    public void testR16_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R16-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("forgiftningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR16_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R16-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("forgiftningDatum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR17() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R17").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("forgiftning", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("forgiftningUppkommelse", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR18_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R18-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.EMPTY, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsorsakgrund", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("grunder", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR18_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R18-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("dodsorsakgrund", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("grunder", internalValidationResponse.getValidationErrors().get(0).getField());
    }


    @Test
    public void testR20_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R20-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R20-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_3() throws ScenarioNotFoundException {
        // Same as R20_1 but with samordningsnummer
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R20-3").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR20_4() throws ScenarioNotFoundException {
        // Same as R20_2 but with samordningsnummer
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R20-4").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("barnSomAvlidit", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("barn", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR22_1() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R22-1").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("utlatandeOrsak", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("terminalDodsorsak.datum", internalValidationResponse.getValidationErrors().get(0).getField());
    }

    @Test
    public void testR22_2() throws ScenarioNotFoundException {
        DoiUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-R22-2").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(1, getNumberOfInternalValidationErrors(internalValidationResponse));
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, internalValidationResponse.getValidationErrors().get(0).getType());
        assertEquals("utlatandeOrsak", internalValidationResponse.getValidationErrors().get(0).getCategory());
        assertEquals("terminalDodsorsak.datum", internalValidationResponse.getValidationErrors().get(0).getField());
    }
}
