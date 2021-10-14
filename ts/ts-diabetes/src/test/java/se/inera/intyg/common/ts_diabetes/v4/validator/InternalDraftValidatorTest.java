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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.ts_diabetes.v4.model.internal.TsDiabetesUtlatandeV4;
import se.inera.intyg.common.ts_diabetes.v4.utils.ScenarioFinder;
import se.inera.intyg.common.ts_diabetes.v4.validator.InternalValidatorHelper;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @InjectMocks
    InternalDraftValidatorImpl validator;

    private static InternalValidatorHelper internalValidatorHelper = new InternalValidatorHelper();

    public static TsDiabetesUtlatandeV4 setupPassingHypoglykemierDates(TsDiabetesUtlatandeV4 utlatande) {
        internalValidatorHelper.setNowMinusDays(utlatande.getHypoglykemier().getAterkommandeSenasteTidpunkt(), 10);
        internalValidatorHelper.setNowMinusDays(utlatande.getHypoglykemier().getForekomstTrafikTidpunkt(), 10);
        internalValidatorHelper.setNowMinusDays(utlatande.getHypoglykemier().getSenasteTidpunktVaken(), 10);
        return utlatande;
    }

    @Test
    public void validateMinimalValidUtkast() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
    }

    @Test
    public void validateCompleteValidUtkast() throws Exception {
        TsDiabetesUtlatandeV4 utlatande = setupPassingHypoglykemierDates(
            ScenarioFinder.getInternalScenario("pass-complete").asInternalModel());

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertFalse(res.hasWarningMessages());
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
