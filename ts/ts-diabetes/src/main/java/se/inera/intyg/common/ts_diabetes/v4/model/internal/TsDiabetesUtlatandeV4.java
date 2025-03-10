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
package se.inera.intyg.common.ts_diabetes.v4.model.internal;

// CHECKSTYLE:OFF LineLength

import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.HYPOGLYKEMI_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.INTYG_AVSER_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.OVRIGT_CATEGORY_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.SIGNATURE;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.TEXTVERSION_JSON_ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;

// CHECKSTYLE:ON LineLength

@AutoValue
@JsonDeserialize(builder = AutoValue_TsDiabetesUtlatandeV4.Builder.class)
public abstract class TsDiabetesUtlatandeV4 implements Utlatande {

    @Override
    public String getTyp() {
        return TsDiabetesEntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    @Override
    @Nullable
    public abstract String getSignature();

    // Kategori 1 - Intyget avser
    @Nullable
    public abstract IntygAvser getIntygAvser();

    // Kategori 2 - Identitet
    @Nullable
    public abstract IdKontroll getIdentitetStyrktGenom();

    // Kategori 3 - Allmänt
    @Nullable
    public abstract Allmant getAllmant();

    // Kategori 4 - Hypoglykemi
    @Nullable
    public abstract Hypoglykemi getHypoglykemi();

    // Kategori 6 - Övrigt
    @Nullable
    public abstract Ovrigt getOvrigt();

    // Kategori 7 - Bedomning
    @Nullable
    public abstract Bedomning getBedomning();


    /*
     * Retrieve a builder from an existing TsDiabetesUtlatandeV4 object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_TsDiabetesUtlatandeV4.Builder()
            .setSignature(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract TsDiabetesUtlatandeV4 build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);

        @JsonProperty(INTYG_AVSER_SVAR_JSON_ID)
        public abstract Builder setIntygAvser(IntygAvser intygAvser);

        @JsonProperty(IDENTITET_STYRKT_GENOM_JSON_ID)
        public abstract Builder setIdentitetStyrktGenom(IdKontroll identitetStyrktGenom);

        @JsonProperty(ALLMANT_JSON_ID)
        public abstract Builder setAllmant(Allmant allmant);

        @JsonProperty(HYPOGLYKEMI_CATEGORY_ID)
        public abstract Builder setHypoglykemi(Hypoglykemi hypoglykemi);

        @JsonProperty(OVRIGT_CATEGORY_ID)
        public abstract Builder setOvrigt(Ovrigt ovrigt);

        @JsonProperty(BEDOMNING_CATEGORY_ID)
        public abstract Builder setBedomning(Bedomning bedomning);
    }

}
