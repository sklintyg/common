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

import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

/**
 * Created by marced on 2018-09-03.
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Synfunktion.Builder.class)
public abstract class Synfunktion {

    @Nullable
    public abstract Boolean getMisstankeOgonsjukdom();

    @Nullable
    public abstract Boolean getSkickasSeparat();

    @Nullable
    public abstract Synskarpevarden getVanster();

    @Nullable
    public abstract Synskarpevarden getHoger();

    @Nullable
    public abstract Synskarpevarden getBinokulart();

    public static Builder builder() {
        return new AutoValue_Synfunktion.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Synfunktion build();

        @JsonProperty(SYNFUNKTION_MISSTANKE_OGONSJUKDOM_JSON_ID)
        public abstract Builder setMisstankeOgonsjukdom(Boolean value);

        @JsonProperty(SYNFUNKTION_SYNSKARPA_SKICKAS_SEPARAT_JSON_ID)
        public abstract Builder setSkickasSeparat(Boolean value);

        @JsonProperty(SYNFUNKTION_SYNSKARPA_VANSTER_JSON_ID)
        public abstract Builder setVanster(Synskarpevarden value);

        @JsonProperty(SYNFUNKTION_SYNSKARPA_HOGER_JSON_ID)
        public abstract Builder setHoger(Synskarpevarden value);

        @JsonProperty(SYNFUNKTION_SYNSKARPA_BINOKULART_JSON_ID)
        public abstract Builder setBinokulart(Synskarpevarden value);
    }
}
