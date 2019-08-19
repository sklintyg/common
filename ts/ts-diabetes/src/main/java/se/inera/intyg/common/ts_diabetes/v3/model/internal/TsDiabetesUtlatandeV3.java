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

// CHECKSTYLE:OFF LineLength

import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ALLMANT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.BEDOMNING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.HYPOGLYKEMIER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.IDENTITET_STYRKT_GENOM_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.INTYGETAVSER_SVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.OVRIGT_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SIGNATURE;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.SYNFUNKTION_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v3.model.converter.RespConstants.TEXTVERSION_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.ts_diabetes.support.TsDiabetesEntryPoint;

// CHECKSTYLE:ON LineLength

@AutoValue
@JsonDeserialize(builder = AutoValue_TsDiabetesUtlatandeV3.Builder.class)
public abstract class TsDiabetesUtlatandeV3 implements Utlatande {

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

    // Fråga 1 - Intyget avser behörighet
    // Fråga 1.1 - Intyget avser behörighet
    @Nullable
    public abstract IntygAvser getIntygAvser();

    // Kategori 2 - Identitet
    // Fråga 2 - Identitet Styrkt genom
    // Fråga 2.1 - Identitet Styrkt genom
    @Nullable
    public abstract IdKontroll getIdentitetStyrktGenom();


    // Kategori 3 - Allmänt (Fråga 35, 18, 109)
    @Nullable
    public abstract Allmant getAllmant();

    // Kategori 4 - Hypoglykemier (Fråga 100, 37, 101, 102, 38, 36, 105, 106, 107, 108)
    @Nullable
    public abstract Hypoglykemier getHypoglykemier();

    // Kategori 5 - Synfunktion (Fråga 103, 104, 8)
    @Nullable
    public abstract Synfunktion getSynfunktion();

    // Kategori 6 - Övrigt (Fråga 32)
    @Nullable
    public abstract String getOvrigt();

    // Kategori 7 - Bedomning (Fråga 33, 45, 34)
    @Nullable
    public abstract Bedomning getBedomning();


    /*
     * Retrieve a builder from an existing TsDiabetesUtlatandeV3 object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_TsDiabetesUtlatandeV3.Builder()
                .setSignature(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract TsDiabetesUtlatandeV3 build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);

        @JsonProperty(INTYGETAVSER_SVAR_JSON_ID)
        public abstract Builder setIntygAvser(IntygAvser intygAvser);

        @JsonProperty(IDENTITET_STYRKT_GENOM_JSON_ID)
        public abstract Builder setIdentitetStyrktGenom(IdKontroll identitetStyrktGenom);

        @JsonProperty(ALLMANT_JSON_ID)
        public abstract Builder setAllmant(Allmant allmant);

        @JsonProperty(HYPOGLYKEMIER_JSON_ID)
        public abstract Builder setHypoglykemier(Hypoglykemier hypoglykemier);

        @JsonProperty(SYNFUNKTION_JSON_ID)
        public abstract Builder setSynfunktion(Synfunktion synfunktion);

        @JsonProperty(OVRIGT_DELSVAR_JSON_ID)
        public abstract Builder setOvrigt(String ovrigt);

        @JsonProperty(BEDOMNING_JSON_ID)
        public abstract Builder setBedomning(Bedomning bedomning);


    }

}
