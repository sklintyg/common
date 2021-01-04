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
package se.inera.intyg.common.af00251.v1.model.internal;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_71;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_72;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

/**
 *
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_BegransningSjukfranvaro.Builder.class)
public abstract class BegransningSjukfranvaro {

    // Fråga 7.1 - Kan sjukfrånvaro begränsas
    @Nullable
    public abstract Boolean getKanBegransas();

    // Fråga 7.2 - Beskrivning av hur sjukfrånvaro begränsas
    @Nullable
    public abstract String getBeskrivning();

    public static BegransningSjukfranvaro.Builder builder() {
        return new AutoValue_BegransningSjukfranvaro.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract BegransningSjukfranvaro build();

        @JsonProperty(BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_71)
        public abstract BegransningSjukfranvaro.Builder setKanBegransas(Boolean kanBegransas);

        @JsonProperty(BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_72)
        public abstract BegransningSjukfranvaro.Builder setBeskrivning(String beskrivning);
    }


}
