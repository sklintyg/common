/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v7.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_Diabetes.Builder.class)
public abstract class Diabetes {

    public static Builder builder() {
        return new AutoValue_Diabetes.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Diabetes build();

        @JsonProperty("harDiabetes")
        public abstract Builder setHarDiabetes(Boolean harDiabetes);

        @JsonProperty("diabetesTyp")
        public abstract Builder setDiabetesTyp(String diabetesTyp);

        @JsonProperty("kost")
        public abstract Builder setKost(Boolean kost);

        @JsonProperty("insulin")
        public abstract Builder setInsulin(Boolean insulin);

        @JsonProperty("tabletter")
        public abstract Builder setTabletter(Boolean tabletter);
    }

    @Nullable
    public abstract Boolean getHarDiabetes();

    @Nullable
    public abstract String getDiabetesTyp();

    @Nullable
    public abstract Boolean getKost();

    @Nullable
    public abstract Boolean getInsulin();

    @Nullable
    public abstract Boolean getTabletter();
}
