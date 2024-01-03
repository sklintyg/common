/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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
@JsonDeserialize(builder = AutoValue_NarkotikaLakemedel.Builder.class)
public abstract class NarkotikaLakemedel {

    public static Builder builder() {
        return new AutoValue_NarkotikaLakemedel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract NarkotikaLakemedel build();

        @JsonProperty("teckenMissbruk")
        public abstract Builder setTeckenMissbruk(Boolean teckenMissbruk);

        @JsonProperty("foremalForVardinsats")
        public abstract Builder setForemalForVardinsats(Boolean foremalForVardinsats);

        @JsonProperty("provtagningBehovs")
        public abstract Builder setProvtagningBehovs(Boolean provtagningBehovs);

        @JsonProperty("lakarordineratLakemedelsbruk")
        public abstract Builder setLakarordineratLakemedelsbruk(Boolean lakarordineratLakemedelsbruk);

        @JsonProperty("lakemedelOchDos")
        public abstract Builder setLakemedelOchDos(String lakemedelOchDos);

    }

    @Nullable
    public abstract Boolean getTeckenMissbruk();

    @Nullable
    public abstract Boolean getForemalForVardinsats();

    @Nullable
    public abstract Boolean getProvtagningBehovs();

    @Nullable
    public abstract Boolean getLakarordineratLakemedelsbruk();

    @Nullable
    public abstract String getLakemedelOchDos();

}
