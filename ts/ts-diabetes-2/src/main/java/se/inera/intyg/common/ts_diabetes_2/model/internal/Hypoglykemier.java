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

/**
 * Created by marced on 2018-09-03.
 */
@AutoValue
public abstract class Hypoglykemier {

    @JsonCreator
    public static Hypoglykemier create(@JsonProperty("sjukdomenUnderkontroll") Boolean sjukdomenUnderkontroll,
            @JsonProperty("nedsattHjarnfunktion") Boolean nedsattHjarnfunktion,
            @JsonProperty("forstarRisker") Boolean forstarRisker,
            @JsonProperty("fortrogenMedSymptom") Boolean fortrogenMedSymptom,
            @JsonProperty("saknarFormagaVarningstecken") Boolean saknarFormagaVarningstecken,
            @JsonProperty("kunskapLampligaAtgarder") Boolean kunskapLampligaAtgarder,
            @JsonProperty("egenkontrollBlodsocker") Boolean egenkontrollBlodsocker,
            @JsonProperty("aterkommandeSenasteAret") Boolean aterkommandeSenasteAret,
            @JsonProperty("aterkommandeSenasteTidpunkt") InternalDate aterkommandeSenasteTidpunkt,
            @JsonProperty("aterkommandeSenasteKvartalet") Boolean aterkommandeSenasteKvartalet,
            @JsonProperty("senasteTidpunktVaken") InternalDate senasteTidpunktVaken,
            @JsonProperty("forekomstTrafikTidpunkt") InternalDate forekomstTrafikTidpunkt) {
        return new AutoValue_Hypoglykemier(sjukdomenUnderkontroll, nedsattHjarnfunktion, forstarRisker, fortrogenMedSymptom,
                saknarFormagaVarningstecken, kunskapLampligaAtgarder, egenkontrollBlodsocker, aterkommandeSenasteAret,
                aterkommandeSenasteTidpunkt, aterkommandeSenasteKvartalet, senasteTidpunktVaken, forekomstTrafikTidpunkt);
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
    public abstract InternalDate getForekomstTrafikTidpunkt();
}
