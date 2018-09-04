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

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * Created by marced on 2018-09-03.
 */
@AutoValue
public abstract class Behandling {

    @JsonCreator
    public static Behandling create(@JsonProperty("endastKost") Boolean endastKost,
            @JsonProperty("tabletter") Boolean tabletter,
            @JsonProperty("tablettRiskHypoglykemi") Boolean tablettRiskHypoglykemi,
            @JsonProperty("insulin") Boolean insulin,
            @JsonProperty("insulinSedanAr") String insulinSedanAr,
            @JsonProperty("annanBehandling") Boolean annanBehandling,
           @JsonProperty("annanBehandlingBeskrivning") String annanBehandlingBeskrivning) {
        return new AutoValue_Behandling(endastKost, tabletter, tablettRiskHypoglykemi, insulin, insulinSedanAr, annanBehandling, annanBehandlingBeskrivning);
    }
    @Nullable
    public abstract Boolean getEndastKost();

    @Nullable
    public abstract Boolean getTabletter();

    @Nullable
    public abstract Boolean getTablettRiskHypoglykemi();

    @Nullable
    public abstract Boolean getInsulin();

    @Nullable
    public abstract String getInsulinSedanAr();

    @Nullable
    public abstract Boolean getAnnanBehandling();

    @Nullable
    public abstract String getAnnanBehandlingBeskrivning();


}
