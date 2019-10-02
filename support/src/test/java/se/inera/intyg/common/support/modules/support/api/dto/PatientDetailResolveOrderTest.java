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
package se.inera.intyg.common.support.modules.support.api.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class PatientDetailResolveOrderTest {

    @Test
    public void constructorShouldThrowWhenGivenNullList() {
        List<PatientDetailResolveOrder.ResolveOrder> any = ImmutableList.of();

        assertThatThrownBy(() -> new PatientDetailResolveOrder("", null, any)).isExactlyInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new PatientDetailResolveOrder("", any, null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    public void disallowUsingPredecessorStrategyWithoutPredecessor() {
        List<PatientDetailResolveOrder.ResolveOrder> predecessorStrat = ImmutableList
                .of(PatientDetailResolveOrder.ResolveOrder.PREDECESSOR);
        List<PatientDetailResolveOrder.ResolveOrder> other = ImmutableList.of(PatientDetailResolveOrder.ResolveOrder.PU);

        assertThatThrownBy(() -> new PatientDetailResolveOrder("", predecessorStrat, other))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

}
