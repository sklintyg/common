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
package se.inera.intyg.common.ts_bas.v6.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

/**
 * Class encapsulating the values for synskarpa for a single eye.
 *
 * @author erik
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Synskarpevarden.Builder.class)
public abstract class Synskarpevarden {

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_Synskarpevarden.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Synskarpevarden build();

        @JsonProperty("utanKorrektion")
        public abstract Builder setUtanKorrektion(Double utanKorrektion);

        @JsonProperty("medKorrektion")
        public abstract Builder setMedKorrektion(Double medKorrektion);

        @JsonProperty("kontaktlins")
        public abstract Builder setKontaktlins(Boolean kontaktlins);
    }

    @Nullable
    public abstract Double getUtanKorrektion();

    @Nullable
    public abstract Double getMedKorrektion();

    @Nullable
    public abstract Boolean getKontaktlins();

}
