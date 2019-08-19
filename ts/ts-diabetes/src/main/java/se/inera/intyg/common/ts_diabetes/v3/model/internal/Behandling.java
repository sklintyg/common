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
package se.inera.intyg.common.ts_diabetes.v3.model.internal;

import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ANNAN_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_ENDAST_KOST_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

/**
 * Created by marced on 2018-09-03.
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Behandling.Builder.class)
public abstract class Behandling {

    @Nullable
    public abstract Boolean getEndastKost();

    @Nullable
    public abstract Boolean getTabletter();

    @Nullable
    public abstract Boolean getTablettRiskHypoglykemi();

    @Nullable
    public abstract Boolean getInsulin();

    @Nullable
    public abstract String getInsulinSedanAr();

    @Nullable
    public abstract Boolean getAnnanBehandling();

    @Nullable
    public abstract String getAnnanBehandlingBeskrivning();

    public static Builder builder() {
        return new AutoValue_Behandling.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Behandling build();

        @JsonProperty(ALLMANT_BEHANDLING_ENDAST_KOST_JSON_ID)
        public abstract Builder setEndastKost(Boolean value);

        @JsonProperty(ALLMANT_BEHANDLING_TABLETTER_JSON_ID)
        public abstract Builder setTabletter(Boolean value);

        @JsonProperty(ALLMANT_BEHANDLING_TABLETTER_RISK_HYPOGLYKEMI_JSON_ID)
        public abstract Builder setTablettRiskHypoglykemi(Boolean value);

        @JsonProperty(ALLMANT_BEHANDLING_INSULIN_JSON_ID)
        public abstract Builder setInsulin(Boolean value);

        @JsonProperty(ALLMANT_BEHANDLING_INSULIN_SEDAN_AR_JSON_ID)
        public abstract Builder setInsulinSedanAr(String value);

        @JsonProperty(ALLMANT_BEHANDLING_ANNAN_BEHANDLING_JSON_ID)
        public abstract Builder setAnnanBehandling(Boolean value);

        @JsonProperty(ALLMANT_BEHANDLING_ANNAN_BEHANDLING_BESKRIVNING_JSON_ID)
        public abstract Builder setAnnanBehandlingBeskrivning(String value);
    }

}
