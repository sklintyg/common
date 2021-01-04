/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes.v3.model.internal;

import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_MED_KORREKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_UTAN_KORREKTION_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

/**
 * Class encapsulating the values for synskarpa for a single eye.
 *
 * @author marced
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Synskarpevarden.Builder.class)
public abstract class Synskarpevarden {

    @Nullable
    public abstract Double getUtanKorrektion();

    @Nullable
    public abstract Double getMedKorrektion();

    public static Builder builder() {
        return new AutoValue_Synskarpevarden.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Synskarpevarden build();

        @JsonProperty(SYNFUNKTION_SYNSKARPA_UTAN_KORREKTION_JSON_ID)
        public abstract Builder setUtanKorrektion(Double utanKorrektion);

        @JsonProperty(SYNFUNKTION_SYNSKARPA_MED_KORREKTION_JSON_ID)
        public abstract Builder setMedKorrektion(Double medKorrektion);
    }

}
