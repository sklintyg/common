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

import se.inera.intyg.common.ts_diabetes_2.model.kodverk.KvTypAvDiabetes;

/**
 * Created by marced on 2018-09-03.
 */
@AutoValue
public abstract class Allmant {

    @JsonCreator
    public static Allmant create(@JsonProperty("diabetesDiagnosAr") String diabetesDiagnosAr,
            @JsonProperty("typAvDiabetes") KvTypAvDiabetes typAvDiabetes,
            @JsonProperty("beskrivningAnnanTypAvDiabetes") String beskrivningAnnanTypAvDiabetes,
            @JsonProperty("behandling") Behandling behandling) {
        return new AutoValue_Allmant(diabetesDiagnosAr, typAvDiabetes, beskrivningAnnanTypAvDiabetes, behandling);
    }

    //TO DO: Is there a better representation of a distinct year (yyyy) ?
    @Nullable
    public abstract String getDiabetesDiagnosAr();

    @Nullable
    public abstract KvTypAvDiabetes getTypAvDiabetes();

    @Nullable
    public abstract String getBeskrivningAnnanTypAvDiabetes();

    @Nullable
    public abstract Behandling getBehandling();

}
