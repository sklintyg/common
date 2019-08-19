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
package se.inera.intyg.common.db.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import se.inera.intyg.common.db.model.internal.Undersokning;
import se.inera.intyg.common.db.support.DbModuleEntryPoint;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;

import javax.annotation.Nullable;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_AVLAGSNAT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.SIGNATURE;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TEXTVERSION_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_JSON_ID;

/**
 * NOTE: If/when a new major version is released, parts common between V1, V2 could be placed in a parent DbUtlatande.
 * The validation of those common properties could also be extracted to a common parent validator.
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_DbUtlatandeV1.Builder.class)
public abstract class DbUtlatandeV1 implements SosUtlatande {

    public static Builder builder() {
        return new AutoValue_DbUtlatandeV1.Builder();
    }

    @Override
    public String getTyp() {
        return DbModuleEntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    @Override
    @Nullable
    public abstract String getIdentitetStyrkt();

    @Override
    @Nullable
    public abstract Boolean getDodsdatumSakert();

    @Override
    @Nullable
    public abstract InternalDate getDodsdatum();

    @Override
    @Nullable
    public abstract InternalDate getAntraffatDodDatum();

    @Override
    @Nullable
    public abstract String getDodsplatsKommun();

    @Override
    @Nullable
    public abstract DodsplatsBoende getDodsplatsBoende();

    @Override
    @Nullable
    public abstract Boolean getBarn();

    @Nullable
    public abstract Boolean getExplosivImplantat();

    @Nullable
    public abstract Boolean getExplosivAvlagsnat();

    @Nullable
    public abstract Undersokning getUndersokningYttre();

    @Nullable
    public abstract InternalDate getUndersokningDatum();

    @Nullable
    public abstract Boolean getPolisanmalan();

    @Nullable
    @Override
    public abstract String getSignature();

    /*
     * Retrieve a builder from an existing LuseUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract DbUtlatandeV1 build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(IDENTITET_STYRKT_JSON_ID)
        public abstract Builder setIdentitetStyrkt(String identitetStyrkt);

        @JsonProperty(DODSDATUM_SAKERT_JSON_ID)
        public abstract Builder setDodsdatumSakert(Boolean dodsdatumSakert);

        @JsonProperty(DODSDATUM_JSON_ID)
        public abstract Builder setDodsdatum(InternalDate dodsdatum);

        @JsonProperty(ANTRAFFAT_DOD_DATUM_JSON_ID)
        public abstract Builder setAntraffatDodDatum(InternalDate antraffatDodDatum);

        @JsonProperty(DODSPLATS_KOMMUN_JSON_ID)
        public abstract Builder setDodsplatsKommun(String dodsplatsKommun);

        @JsonProperty(DODSPLATS_BOENDE_JSON_ID)
        public abstract Builder setDodsplatsBoende(DodsplatsBoende dodsplatsBoende);

        @JsonProperty(BARN_JSON_ID)
        public abstract Builder setBarn(Boolean barn);

        @JsonProperty(EXPLOSIV_IMPLANTAT_JSON_ID)
        public abstract Builder setExplosivImplantat(Boolean explosivImplantat);

        @JsonProperty(EXPLOSIV_AVLAGSNAT_JSON_ID)
        public abstract Builder setExplosivAvlagsnat(Boolean explosivAvlagsnat);

        @JsonProperty(UNDERSOKNING_YTTRE_JSON_ID)
        public abstract Builder setUndersokningYttre(Undersokning undersokningYttre);

        @JsonProperty(UNDERSOKNING_DATUM_JSON_ID)
        public abstract Builder setUndersokningDatum(InternalDate undersokningDatum);

        @JsonProperty(POLISANMALAN_JSON_ID)
        public abstract Builder setPolisanmalan(Boolean polisanmalan);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);
    }
}
