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
@JsonDeserialize(builder = AutoValue_Sjukhusvard.Builder.class)
public abstract class Sjukhusvard {
    //har patienten vårdats på sjukhus eller haft kontakt med läkare med anledning av punkterna 1-13

    public static Builder builder() {
        return new AutoValue_Sjukhusvard.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Sjukhusvard build();

        @JsonProperty("tidpunkt")
        public abstract Builder setTidpunkt(String tidpunkt);

        @JsonProperty("vardinrattning")
        public abstract Builder setVardinrattning(String vardinrattning);

        @JsonProperty("anledning")
        public abstract Builder setAnledning(String anledning);

        @JsonProperty("sjukhusEllerLakarkontakt")
        public abstract Builder setSjukhusEllerLakarkontakt(Boolean sjukhusEllerLakarkontakt);
    }

    @Nullable
    public abstract String getTidpunkt();

    @Nullable
    public abstract String getVardinrattning();

    @Nullable
    public abstract String getAnledning();

    @Nullable
    public abstract Boolean getSjukhusEllerLakarkontakt();
}
