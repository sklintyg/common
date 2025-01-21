/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.doi.v1.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.SIGNATURE;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TERMINAL_DODSORSAK_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TEXTVERSION_JSON_ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import se.inera.intyg.common.doi.model.internal.Dodsorsak;
import se.inera.intyg.common.doi.model.internal.Dodsorsaksgrund;
import se.inera.intyg.common.doi.model.internal.ForgiftningOrsak;
import se.inera.intyg.common.doi.model.internal.OmOperation;
import se.inera.intyg.common.doi.support.DoiModuleEntryPoint;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;

@AutoValue
@JsonDeserialize(builder = AutoValue_DoiUtlatandeV1.Builder.class)
public abstract class DoiUtlatandeV1 implements SosUtlatande {

    public static Builder builder() {
        return new AutoValue_DoiUtlatandeV1.Builder().setGrunder(ImmutableList.<Dodsorsaksgrund>of())
            .setBidragandeSjukdomar(ImmutableList.<Dodsorsak>of()).setFoljd(ImmutableList.<Dodsorsak>of());
    }

    @Override
    public String getTyp() {
        return DoiModuleEntryPoint.MODULE_ID;
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
    public abstract String getLand();

    @Nullable
    public abstract Dodsorsak getTerminalDodsorsak();

    public abstract ImmutableList<Dodsorsak> getFoljd();

    public abstract ImmutableList<Dodsorsak> getBidragandeSjukdomar();

    @Nullable
    public abstract OmOperation getOperation();

    @Nullable
    public abstract InternalDate getOperationDatum();

    @Nullable
    public abstract String getOperationAnledning();

    @Nullable
    public abstract Boolean getForgiftning();

    @Nullable
    public abstract ForgiftningOrsak getForgiftningOrsak();

    @Nullable
    public abstract InternalDate getForgiftningDatum();

    @Nullable
    public abstract String getForgiftningUppkommelse();

    public abstract ImmutableList<Dodsorsaksgrund> getGrunder();

    @Nullable
    @Override
    public abstract String getSignature();

    /*
     * Retrieve a builder from an existing DoiUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract DoiUtlatandeV1 build();

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

        @JsonProperty(LAND_JSON_ID)
        public abstract Builder setLand(String land);

        @JsonProperty(TERMINAL_DODSORSAK_JSON_ID)
        public abstract Builder setTerminalDodsorsak(Dodsorsak dodsorsak);

        @JsonProperty(FOLJD_JSON_ID)
        public Builder setFoljd(List<Dodsorsak> dodsorsak) {
            return setFoljd(ImmutableList.copyOf(dodsorsak));
        }

        abstract Builder setFoljd(ImmutableList<Dodsorsak> dodsorsak);

        @JsonProperty(BIDRAGANDE_SJUKDOM_JSON_ID)
        public Builder setBidragandeSjukdomar(List<Dodsorsak> bidragandeSjukdomar) {
            return setBidragandeSjukdomar(ImmutableList.copyOf(bidragandeSjukdomar));
        }

        abstract Builder setBidragandeSjukdomar(ImmutableList<Dodsorsak> bidragandeSjukdomar);

        @JsonProperty(OPERATION_OM_JSON_ID)
        public abstract Builder setOperation(OmOperation operation);

        @JsonProperty(OPERATION_DATUM_JSON_ID)
        public abstract Builder setOperationDatum(InternalDate operationDatum);

        @JsonProperty(OPERATION_ANLEDNING_JSON_ID)
        public abstract Builder setOperationAnledning(String operationAnledning);

        @JsonProperty(FORGIFTNING_OM_JSON_ID)
        public abstract Builder setForgiftning(Boolean forgiftning);

        @JsonProperty(FORGIFTNING_ORSAK_JSON_ID)
        public abstract Builder setForgiftningOrsak(ForgiftningOrsak forgiftningOrsak);

        @JsonProperty(FORGIFTNING_DATUM_JSON_ID)
        public abstract Builder setForgiftningDatum(InternalDate forgiftningDatum);

        @JsonProperty(FORGIFTNING_UPPKOMMELSE_JSON_ID)
        public abstract Builder setForgiftningUppkommelse(String forgiftningUppkommelse);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);

        @JsonProperty(GRUNDER_JSON_ID)
        public Builder setGrunder(List<Dodsorsaksgrund> grunder) {
            return setGrunder(ImmutableList.copyOf(grunder));
        }

        abstract Builder setGrunder(ImmutableList<Dodsorsaksgrund> grunder);
    }
}
