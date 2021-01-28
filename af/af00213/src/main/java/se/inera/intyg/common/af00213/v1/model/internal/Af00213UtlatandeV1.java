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
package se.inera.intyg.common.af00213.v1.model.internal;

// CHECKSTYLE:OFF LineLength

import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_41;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ARBETETS_PAVERKAN_SVAR_JSON_ID_42;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.SIGNATURE;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.TEXTVERSION_JSON_ID;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_31;
import static se.inera.intyg.common.af00213.v1.model.converter.RespConstants.UTREDNING_BEHANDLING_SVAR_JSON_ID_32;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import se.inera.intyg.common.af00213.support.Af00213EntryPoint;
import se.inera.intyg.common.af_parent.model.internal.AfUtlatande;
import se.inera.intyg.common.support.model.common.internal.GrundData;

// CHECKSTYLE:ON LineLength

@AutoValue
@JsonDeserialize(builder = AutoValue_Af00213UtlatandeV1.Builder.class)
public abstract class Af00213UtlatandeV1 implements AfUtlatande {

    public static Builder builder() {
        return new AutoValue_Af00213UtlatandeV1.Builder()
            .setSignature(null);
    }

    @Override
    public String getTyp() {
        return Af00213EntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    // Kategori 1 - Sjukdomens konsekvenser

    @Override
    @Nullable
    public abstract String getSignature();

    // Fråga 1 - Funktionsnedsättning
    // Fråga 1.1
    @Nullable
    public abstract Boolean getHarFunktionsnedsattning();

    // Fråga 1.2
    @Nullable
    public abstract String getFunktionsnedsattning();

    // Kategori 2 - Aktivitetsbegränsning
    // Fråga 2 Aktivitetsbegränsning
    // Fråga 2.1
    @Nullable
    public abstract Boolean getHarAktivitetsbegransning();

    @Nullable
    public abstract String getAktivitetsbegransning();

    // Kategori 3 - Utredning och behandling
    // Fråga 3 -Pågående medicinska behandlingar
    // Fråga 3.1 - Typ av pågående medicinska behandlingar
    @Nullable
    public abstract Boolean getHarUtredningBehandling();

    @Nullable
    public abstract String getUtredningBehandling();

    // Fråga 4 - Planerad medicinsk behandling
    // Fråga 4.1
    @Nullable
    public abstract Boolean getHarArbetetsPaverkan();

    @Nullable
    public abstract String getArbetetsPaverkan();

    // Kategori 5 - Övrigt
    // Fråga 5
    @Nullable
    public abstract String getOvrigt();

    /*
     * Retrieve a builder from an existing Af00213UtlatandeV1 object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Af00213UtlatandeV1 build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_11)
        public abstract Builder setHarFunktionsnedsattning(Boolean harFunktionsnedsattning);

        @JsonProperty(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_12)
        public abstract Builder setFunktionsnedsattning(String funktionsnedsattning);

        @JsonProperty(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_21)
        public abstract Builder setHarAktivitetsbegransning(Boolean harAktivitetsbegransning);

        @JsonProperty(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_22)
        public abstract Builder setAktivitetsbegransning(String aktivitetsbegransning);

        @JsonProperty(UTREDNING_BEHANDLING_SVAR_JSON_ID_31)
        public abstract Builder setHarUtredningBehandling(Boolean harUtredningBehandling);

        @JsonProperty(UTREDNING_BEHANDLING_SVAR_JSON_ID_32)
        public abstract Builder setUtredningBehandling(String utredningBehandling);

        @JsonProperty(ARBETETS_PAVERKAN_SVAR_JSON_ID_41)
        public abstract Builder setHarArbetetsPaverkan(Boolean harArbetetsPaverkan);

        @JsonProperty(ARBETETS_PAVERKAN_SVAR_JSON_ID_42)
        public abstract Builder setArbetetsPaverkan(String arbetetsPaverkan);

        @JsonProperty(OVRIGT_SVAR_JSON_ID_5)
        public abstract Builder setOvrigt(String ovrigt);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);
    }

}
