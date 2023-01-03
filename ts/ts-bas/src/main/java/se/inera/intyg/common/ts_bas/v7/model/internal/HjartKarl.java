/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
@JsonDeserialize(builder = AutoValue_HjartKarl.Builder.class)
public abstract class HjartKarl {

    public static Builder builder() {
        return new AutoValue_HjartKarl.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract HjartKarl build();

        @JsonProperty("hjartKarlSjukdom")
        public abstract Builder setHjartKarlSjukdom(Boolean hjartKarlSjukdom);

        @JsonProperty("hjarnskadaEfterTrauma")
        public abstract Builder setHjarnskadaEfterTrauma(Boolean hjarnskadaEfterTrauma);

        @JsonProperty("riskfaktorerStroke")
        public abstract Builder setRiskfaktorerStroke(Boolean riskfaktorerStroke);

        @JsonProperty("beskrivningRiskfaktorer")
        public abstract Builder setBeskrivningRiskfaktorer(String beskrivningRiskfaktorer);
    }

    @Nullable
    public abstract Boolean getHjartKarlSjukdom();

    @Nullable
    public abstract Boolean getHjarnskadaEfterTrauma();

    @Nullable
    public abstract Boolean getRiskfaktorerStroke();

    @Nullable
    public abstract String getBeskrivningRiskfaktorer();
}
