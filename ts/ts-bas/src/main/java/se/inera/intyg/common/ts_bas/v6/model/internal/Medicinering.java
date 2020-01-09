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
@JsonDeserialize(builder = AutoValue_Medicinering.Builder.class)
public abstract class Medicinering {

    public static Builder builder() {
        return new AutoValue_Medicinering.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Medicinering build();

        @JsonProperty("stadigvarandeMedicinering")
        public abstract Builder setStadigvarandeMedicinering(Boolean stadigvarandeMedicinering);

        @JsonProperty("beskrivning")
        public abstract Builder setBeskrivning(String beskrivning);

    }

    @Nullable
    public abstract Boolean getStadigvarandeMedicinering();

    @Nullable
    public abstract String getBeskrivning();

}
