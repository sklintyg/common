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
package se.inera.intyg.common.af00251.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;

import javax.annotation.Nullable;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.*;

/**
 *
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Sjukfranvaro.Builder.class)
public abstract class Sjukfranvaro {

    @Nullable
    public abstract Boolean getChecked();

    @Nullable
    public abstract Integer getNiva();

    @Nullable
    public abstract InternalLocalDateInterval getPeriod();

    public static Sjukfranvaro.Builder builder() {
        return new AutoValue_Sjukfranvaro.Builder();
    }

    public abstract Sjukfranvaro.Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Sjukfranvaro build();

        @JsonProperty(SJUKFRANVARO_SVAR_JSON_CHECKED)
        public abstract Sjukfranvaro.Builder setChecked(Boolean checked);

        @JsonProperty(SJUKFRANVARO_SVAR_JSON_ID_61)
        public abstract Sjukfranvaro.Builder setNiva(Integer niva);

        @JsonProperty(SJUKFRANVARO_SVAR_JSON_ID_62)
        public abstract Sjukfranvaro.Builder setPeriod(InternalLocalDateInterval period);

    }
}
