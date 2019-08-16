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
package se.inera.intyg.common.ts_diabetes.v2.model.converter;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.ts_diabetes.v2.model.internal.TsDiabetesUtlatandeV2;
import se.inera.intyg.common.ts_diabetes.v2.utils.Scenario;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioFinder;
import se.inera.intyg.common.ts_diabetes.v2.utils.ScenarioNotFoundException;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class ScenarioTest {

    private List<Scenario> internalScenarios;
    private List<Scenario> transportScenarios;

    @Before
    public void setUp() throws Exception {
        internalScenarios = ScenarioFinder.getInternalScenarios("diabetes-*");
        transportScenarios = ScenarioFinder.getTransportScenarios("diabetes-*");
    }

    @Test
    public void testInternalToTransport() throws ScenarioNotFoundException {
        for (Scenario internalScenario : internalScenarios) {
            Scenario transportScenario = getScenarioByName(internalScenario.getName(), transportScenarios);

            TSDiabetesIntyg expected = transportScenario.asTransportModel().getIntyg();
            TSDiabetesIntyg actual = InternalToTransportConverter.convert(internalScenario.asInternalModel()).getIntyg();

            ReflectionAssert.assertLenientEquals(expected, actual);
        }
    }

    @Test
    public void testTransportToInternal() throws ScenarioNotFoundException, ConverterException {
        for (Scenario scenario : transportScenarios) {
            Scenario internalScenario = getScenarioByName(scenario.getName(), internalScenarios);

            TsDiabetesUtlatandeV2 expected = internalScenario.asInternalModel();
            TsDiabetesUtlatandeV2 actual = TransportToInternalConverter.convert(scenario.asTransportModel().getIntyg());

            ReflectionAssert.assertLenientEquals(expected, actual);
        }
    }

    private Scenario getScenarioByName(String name, List<Scenario> scenarios) {
        return scenarios.stream().filter(s -> name.equalsIgnoreCase(s.getName())).findAny()
            .orElseThrow(() -> new IllegalArgumentException("No such scenario found"));
    }
}
