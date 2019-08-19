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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class PatientDetailResolveOrder {

    private String predecessorType;
    private List<ResolveOrder> adressStrategy;
    private List<ResolveOrder> avlidenStrategy;
    private List<ResolveOrder> other;

    /**
     * Create a default PatientDetailResolveOrder, <br/>
     * with:<br/>
     * no adress-strategy,<br/>
     * avlidenStrategy(PU, PARAMS) and <br/>
     * other ie name and sekretess-strategy (PU, PARAMS).
     */
    public static PatientDetailResolveOrder defaultOrder() {
        return new PatientDetailResolveOrder(null, ImmutableList.of(),
            ImmutableList.of(ResolveOrder.PARAMS_OR_PU),
            ImmutableList.of(ResolveOrder.PU, ResolveOrder.PARAMS));
    }

    public PatientDetailResolveOrder(String predecessorType, List<ResolveOrder> adressStrategy,
        List<ResolveOrder> avlidenStrategy, List<ResolveOrder> other) {
        checkNotNull(adressStrategy);
        checkNotNull(avlidenStrategy);
        checkNotNull(other);

        // No predecessor --> can't use predecessor strategy.
        Preconditions.checkArgument((!Strings.isNullOrEmpty(predecessorType)
                || ImmutableList.of(adressStrategy, avlidenStrategy, other).stream()
                .flatMap(List::stream)
                .noneMatch(Predicate.isEqual(ResolveOrder.PREDECESSOR))),
            "Can't use predecessor strategy without predecessor.");

        this.predecessorType = predecessorType;
        this.adressStrategy = ImmutableList.copyOf(adressStrategy);
        this.avlidenStrategy = ImmutableList.copyOf(avlidenStrategy);
        this.other = ImmutableList.copyOf(other);
    }

    public String getPredecessorType() {
        return predecessorType;
    }

    public List<ResolveOrder> getAdressStrategy() {
        return adressStrategy;
    }

    public List<ResolveOrder> getAvlidenStrategy() {
        return avlidenStrategy;
    }

    public List<ResolveOrder> getOtherStrategy() {
        return other;
    }

    public enum ResolveOrder {
        /**
         * PARAMS || PU (used for avliden).
         */
        PARAMS_OR_PU,
        /**
         * From integrations parameters.
         */
        PARAMS,
        /**
         * From PU.
         */
        PU,
        /**
         * From existing intyg, type specified by predecessorType.
         */
        PREDECESSOR
    }

}
