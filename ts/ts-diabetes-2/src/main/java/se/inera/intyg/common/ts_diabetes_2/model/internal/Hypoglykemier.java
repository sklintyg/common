/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_diabetes_2.model.internal;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalDate;

import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes_2.model.converter.RespConstants.HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID;

/**
 * Created by marced on 2018-09-03.
 */
@AutoValue
public abstract class Hypoglykemier {

    @JsonCreator
    public static Hypoglykemier create(@JsonProperty(HYPOGLYKEMIER_SJUKDOMEN_UNDER_KONTROLL_JSON_ID) Boolean sjukdomenUnderkontroll,
            @JsonProperty(HYPOGLYKEMIER_NEDSATT_HJARNFUNKTION_JSON_ID) Boolean nedsattHjarnfunktion,
            @JsonProperty(HYPOGLYKEMIER_FORSTAR_RISKER_JSON_ID) Boolean forstarRisker,
            @JsonProperty(HYPOGLYKEMIER_FORTROGEN_MED_SYMPTOM_JSON_ID) Boolean fortrogenMedSymptom,
            @JsonProperty(HYPOGLYKEMIER_SAKNAR_FORMAGA_VARNINGSTECKEN_JSON_ID) Boolean saknarFormagaVarningstecken,
            @JsonProperty(HYPOGLYKEMIER_KUNSKAP_LAMPLIGA_JSON_ID) Boolean kunskapLampligaAtgarder,
            @JsonProperty(HYPOGLYKEMIER_EGENKONTROLL_BLODSOCKER_JSON_ID) Boolean egenkontrollBlodsocker,
            @JsonProperty(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_JSON_ID) Boolean aterkommandeSenasteAret,
            @JsonProperty(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID) InternalDate aterkommandeSenasteTidpunkt,
            @JsonProperty(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_KVARTALET_JSON_ID) Boolean aterkommandeSenasteKvartalet,
            @JsonProperty(HYPOGLYKEMIER_ATERKOMMANDE_SENASTE_TIDPUNKT_VAKEN_JSON_ID) InternalDate senasteTidpunktVaken,
            @JsonProperty(HYPOGLYKEMIER_FOREKOMST_TRAFIK_JSON_ID) Boolean forekomstTrafik,
            @JsonProperty(HYPOGLYKEMIER_FOREKOMST_TRAFIK_TIDPUNKT_JSON_ID) InternalDate forekomstTrafikTidpunkt) {
        return new AutoValue_Hypoglykemier(sjukdomenUnderkontroll, nedsattHjarnfunktion, forstarRisker, fortrogenMedSymptom,
                saknarFormagaVarningstecken, kunskapLampligaAtgarder, egenkontrollBlodsocker, aterkommandeSenasteAret,
                aterkommandeSenasteTidpunkt, aterkommandeSenasteKvartalet, senasteTidpunktVaken, forekomstTrafik, forekomstTrafikTidpunkt);
    }

    @Nullable
    public abstract Boolean getSjukdomenUnderkontroll();

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
}
