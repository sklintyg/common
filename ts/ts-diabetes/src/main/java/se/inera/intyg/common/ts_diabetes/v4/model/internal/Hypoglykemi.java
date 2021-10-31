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


import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import se.inera.intyg.common.support.model.InternalDate;

@AutoValue
@JsonDeserialize(builder = AutoValue_Hypoglykemi.Builder.class)
public abstract class Hypoglykemi {

    @Nullable
    public abstract Boolean getKontrollSjukdomstillstand();

    @Nullable
    public abstract String getKontrollSjukdomstillstandVarfor();

    @Nullable
    public abstract Boolean getForstarRiskerMedHypoglykemi();

    @Nullable
    public abstract Boolean getFormagaKannaVarningstecken();

    @Nullable
    public abstract Boolean getVidtaAdekvataAtgarder();

    @Nullable
    public abstract Boolean getAterkommandeSenasteAret();

    @Nullable
    public abstract InternalDate getAterkommandeSenasteAretTidpunkt();

    @Nullable
    public abstract Boolean getAterkommandeSenasteAretKontrolleras();

    @Nullable
    public abstract Boolean getAterkommandeSenasteAretTrafik();

    @Nullable
    public abstract Boolean getAterkommandeVaketSenasteTolv();

    @Nullable
    public abstract Boolean getAterkommandeVaketSenasteTre();

    @Nullable
    public abstract InternalDate getAterkommandeVaketSenasteTreTidpunkt();

    @Nullable
    public abstract Boolean getAllvarligSenasteTolvManaderna();

    @Nullable
    public abstract InternalDate getAllvarligSenasteTolvManadernaTidpunkt();

    @Nullable
    public abstract Boolean getRegelbundnaBlodsockerkontroller();


    public static Builder builder() {
        return new AutoValue_Hypoglykemi.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Hypoglykemi build();

        @JsonProperty(HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_JSON_ID)
        public abstract Builder setKontrollSjukdomstillstand(Boolean value);

        @JsonProperty(HYPOGLYKEMI_KONTROLL_SJUKDOMSTILLSTAND_VARFOR_JSON_ID)
        public abstract Builder setKontrollSjukdomstillstandVarfor(String value);

        @JsonProperty(HYPOGLYKEMI_FORSTAR_RISKER_MED_HYPOGLYKEMI_JSON_ID)
        public abstract Builder setForstarRiskerMedHypoglykemi(Boolean value);

        @JsonProperty(HYPOGLYKEMI_FORMAGA_KANNA_VARNINGSTECKEN_JSON_ID)
        public abstract Builder setFormagaKannaVarningstecken(Boolean value);

        @JsonProperty(HYPOGLYKEMI_VIDTA_ADEKVATA_ATGARDER_JSON_ID)
        public abstract Builder setVidtaAdekvataAtgarder(Boolean value);

        @JsonProperty(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_JSON_ID)
        public abstract Builder setAterkommandeSenasteAret(Boolean value);

        @JsonProperty(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TIDPUNKT_JSON_ID)
        public abstract Builder setAterkommandeSenasteAretTidpunkt(InternalDate value);

        @JsonProperty(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_KONTROLLERAS_JSON_ID)
        public abstract Builder setAterkommandeSenasteAretKontrolleras(Boolean value);

        @JsonProperty(HYPOGLYKEMI_ATERKOMMANDE_SENASTE_ARET_TRAFIK_JSON_ID)
        public abstract Builder setAterkommandeSenasteAretTrafik(Boolean value);

        @JsonProperty(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TOLV_JSON_ID)
        public abstract Builder setAterkommandeVaketSenasteTolv(Boolean value);

        @JsonProperty(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_JSON_ID)
        public abstract Builder setAterkommandeVaketSenasteTre(Boolean value);

        @JsonProperty(HYPOGLYKEMI_ATERKOMMANDE_VAKET_SENASTE_TRE_TIDPUNKT_JSON_ID)
        public abstract Builder setAterkommandeVaketSenasteTreTidpunkt(InternalDate value);

        @JsonProperty(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_JSON_ID)
        public abstract Builder setAllvarligSenasteTolvManaderna(Boolean value);

        @JsonProperty(HYPOGLYKEMI_ALLVARLIG_SENASTE_TOLV_MANADERNA_TIDPUNKT_JSON_ID)
        public abstract Builder setAllvarligSenasteTolvManadernaTidpunkt(InternalDate value);

        @JsonProperty(HYPOGLYKEMI_REGELBUNDNA_BLODSOCKERKONTROLLER_JSON_ID)
        public abstract Builder setRegelbundnaBlodsockerkontroller(Boolean value);
    }
}
