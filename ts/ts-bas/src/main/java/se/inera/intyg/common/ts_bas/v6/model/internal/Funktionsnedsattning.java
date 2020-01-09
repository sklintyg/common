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
package se.inera.intyg.common.ts_bas.v6.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_Funktionsnedsattning.Builder.class)
public abstract class Funktionsnedsattning {

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_Funktionsnedsattning.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Funktionsnedsattning build();

        @JsonProperty("funktionsnedsattning")
        public abstract Builder setFunktionsnedsattning(Boolean funktionsnedsattning);

        @JsonProperty("beskrivning")
        public abstract Builder setBeskrivning(String beskrivning);

        @JsonProperty("otillrackligRorelseformaga")
        public abstract Builder setOtillrackligRorelseformaga(Boolean otillrackligRorelseformaga);
    }

    @Nullable
    public abstract Boolean getFunktionsnedsattning();

    @Nullable
    public abstract String getBeskrivning();

    @Nullable
    public abstract Boolean getOtillrackligRorelseformaga();

}
