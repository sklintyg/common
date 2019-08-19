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

import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalDate;

/**
 * Created by marced on 2018-09-03.
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_Hypoglykemier.Builder.class)
public abstract class Hypoglykemier {

    @Nullable
    public abstract Boolean getSjukdomenUnderKontroll();

    @Nullable
    public abstract Boolean getNedsattHjarnfunktion();

    @Nullable
    public abstract Boolean getForstarRisker();

    @Nullable
    public abstract Boolean getFortrogenMedSymptom();

    @Nullable
    public abstract Boolean getSaknarFormagaVarningstecken();

    @Nullable
    public abstract Boolean getKunskapLampligaAtgarder();

    @Nullable
    public abstract Boolean getEgenkontrollBlodsocker();

    @Nullable
    public abstract Boolean getAterkommandeSenasteAret();

    @Nullable
    public abstract InternalDate getAterkommandeSenasteTidpunkt();

    @Nullable
    public abstract Boolean getAterkommandeSenasteKvartalet();

    @Nullable
    public abstract InternalDate getSenasteTidpunktVaken();

    @Nullable
    public abstract Boolean getForekomstTrafik();

    @Nullable
    public abstract InternalDate getForekomstTrafikTidpunkt();

    public static Builder builder() {
        return new AutoValue_Hypoglykemier.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Hypoglykemier build();

        @JsonProperty(HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID)
        public abstract Builder setSjukdomenUnderKontroll(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID)
        public abstract Builder setNedsattHjarnfunktion(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID)
        public abstract Builder setForstarRisker(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID)
        public abstract Builder setFortrogenMedSymptom(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID)
        public abstract Builder setSaknarFormagaVarningstecken(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID)
        public abstract Builder setKunskapLampligaAtgarder(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID)
        public abstract Builder setEgenkontrollBlodsocker(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID)
        public abstract Builder setAterkommandeSenasteAret(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID)
        public abstract Builder setAterkommandeSenasteTidpunkt(InternalDate value);

        @JsonProperty(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID)
        public abstract Builder setAterkommandeSenasteKvartalet(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID)
        public abstract Builder setSenasteTidpunktVaken(InternalDate value);

        @JsonProperty(HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID)
        public abstract Builder setForekomstTrafik(Boolean value);

        @JsonProperty(HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID)
        public abstract Builder setForekomstTrafikTidpunkt(InternalDate value);
    }
}
