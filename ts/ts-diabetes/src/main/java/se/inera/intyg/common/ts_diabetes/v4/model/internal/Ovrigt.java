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
 *
 */

package se.inera.intyg.common.ts_diabetes.v4.model.internal;

import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_Ovrigt.Builder.class)
public abstract class Ovrigt {

    @Nullable
    public abstract Boolean getKomplikationerAvSjukdomen();

    @Nullable
    public abstract String getKomplikationerAvSjukdomenAnges();

    @Nullable
    public abstract String getBorUndersokasAvSpecialist();

    public static Builder builder() {
        return new AutoValue_Ovrigt.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Ovrigt build();

        @JsonProperty(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_JSON_ID)
        public abstract Builder setKomplikationerAvSjukdomen(Boolean value);

        @JsonProperty(OVRIGT_KOMPLIKATIONER_AV_SJUKDOMEN_ANGES_JSON_ID)
        public abstract Builder setKomplikationerAvSjukdomenAnges(String value);

        @JsonProperty(OVRIGT_BOR_UNDERSOKAS_AV_SPECIALIST_JSON_ID)
        public abstract Builder setBorUndersokasAvSpecialist(String value);
    }

}
