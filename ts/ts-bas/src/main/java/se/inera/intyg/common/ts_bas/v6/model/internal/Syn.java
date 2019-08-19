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

@AutoValue
@JsonDeserialize(builder = AutoValue_Syn.Builder.class)
public abstract class Syn {

    @Nullable
    public abstract Boolean getSynfaltsdefekter();

    @Nullable
    public abstract Boolean getNattblindhet();

    @Nullable
    public abstract Boolean getProgressivOgonsjukdom();

    @Nullable
    public abstract Boolean getDiplopi();

    @Nullable
    public abstract Boolean getNystagmus();

    @Nullable
    public abstract Boolean getKorrektionsglasensStyrka();

    @Nullable
    public abstract Synskarpevarden getHogerOga();

    @Nullable
    public abstract Synskarpevarden getVansterOga();

    @Nullable
    public abstract Synskarpevarden getBinokulart();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_Syn.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Syn build();

        @JsonProperty("hogerOga")
        public abstract Builder setHogerOga(Synskarpevarden hogerOga);

        @JsonProperty("vansterOga")
        public abstract Builder setVansterOga(Synskarpevarden vansterOga);

        @JsonProperty("binokulart")
        public abstract Builder setBinokulart(Synskarpevarden binokulart);

        @JsonProperty("synfaltsdefekter")
        public abstract Builder setSynfaltsdefekter(Boolean synfaltsdefekter);

        @JsonProperty("nattblindhet")
        public abstract Builder setNattblindhet(Boolean nattblindhet);

        @JsonProperty("progressivOgonsjukdom")
        public abstract Builder setProgressivOgonsjukdom(Boolean progressivOgonsjukdom);

        @JsonProperty("diplopi")
        public abstract Builder setDiplopi(Boolean diplopi);

        @JsonProperty("nystagmus")
        public abstract Builder setNystagmus(Boolean nystagmus);

        @JsonProperty("korrektionsglasensStyrka")
        public abstract Builder setKorrektionsglasensStyrka(Boolean korrektionsglasensStyrka);
    }
}
