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
package se.inera.intyg.common.ts_diabetes_2.model.internal;

import java.util.Set;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.ts_diabetes_2.model.kodverk.KvKorkortsbehorighet;

/**
 * Created by marced on 2018-09-03.
 */
@AutoValue
public abstract class Bedomning {

    @JsonCreator
    public static Bedomning create(@JsonProperty("uppfyllerBehorighetskrav") Set<KvKorkortsbehorighet> uppfyllerBehorighetskrav,
            @JsonProperty("lampligtInnehav") Boolean lampligtInnehav,
            @JsonProperty("borUndersokasBeskrivning") String borUndersokasBeskrivning) {
        return new AutoValue_Bedomning(uppfyllerBehorighetskrav, lampligtInnehav, borUndersokasBeskrivning);
    }

    @Nullable
    public abstract Set<KvKorkortsbehorighet> getUppfyllerBehorighetskrav();

    @Nullable
    public abstract Boolean getLampligtInnehav();

    @Nullable
    public abstract String getBorUndersokasBeskrivning();
}
