/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.af00251.v1.model.internal;

// CHECKSTYLE:OFF LineLength

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_4;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_7;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.FORHINDER_SVAR_JSON_ID_5;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_3;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.MEDICINSKUNDERLAG_SVAR_JSON_ANNAT_BESKRIVNING;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.MEDICINSKUNDERLAG_SVAR_JSON_ANNAT_DATUM;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.MEDICINSKUNDERLAG_SVAR_JSON_UNDERSOKNINGS_DATUM;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.PROGNOS_ATERGANG_SVAR_JSON_ID_8;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.SIGNATURE;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.SJUKFRANVARO_SVAR_JSON_ID_6;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.TEXTVERSION_JSON_ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import se.inera.intyg.common.af00251.support.AF00251EntryPoint;
import se.inera.intyg.common.af_parent.model.internal.AfUtlatande;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;

// CHECKSTYLE:ON LineLength

@AutoValue
@JsonDeserialize(builder = AutoValue_AF00251UtlatandeV1.Builder.class)
public abstract class AF00251UtlatandeV1 implements AfUtlatande {

    public static Builder builder() {
        return new AutoValue_AF00251UtlatandeV1.Builder()
            .setSjukfranvaro(ImmutableList.<Sjukfranvaro>of())
            .setSignature(null);
    }

    @Override
    public String getTyp() {
        return AF00251EntryPoint.MODULE_ID;
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

    // Fråga 1
    // Kardinalitet 1-2
    @Nullable
    public abstract InternalDate getUndersokningsDatum();

    @Nullable
    public abstract InternalDate getAnnatDatum();

    @Nullable
    public abstract String getAnnatBeskrivning();

    // Fråga 2
    @Nullable
    public abstract ArbetsmarknadspolitisktProgram getArbetsmarknadspolitisktProgram();

    // Fråga 3.1 - Funktionsnedsättning
    @Nullable
    public abstract String getFunktionsnedsattning();

    // Fråga 4.1 - Aktivitetsbegränsning
    @Nullable
    public abstract String getAktivitetsbegransning();

    // Fråga 5.1 - Förhinder
    @Nullable
    public abstract Boolean getHarForhinder();

    // Fråga 6
    // Kardinalitet 0-4
    @Nullable
    public abstract ImmutableList<Sjukfranvaro> getSjukfranvaro();

    // Fråga 7
    @Nullable
    public abstract BegransningSjukfranvaro getBegransningSjukfranvaro();

    // Fråga 8
    @Nullable
    public abstract PrognosAtergang getPrognosAtergang();


    /*
     * Retrieve a builder from an existing Af00251UtlatandeV1 object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract AF00251UtlatandeV1 build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(MEDICINSKUNDERLAG_SVAR_JSON_UNDERSOKNINGS_DATUM)
        public abstract Builder setUndersokningsDatum(InternalDate undersokningsDatum);

        @JsonProperty(MEDICINSKUNDERLAG_SVAR_JSON_ANNAT_DATUM)
        public abstract Builder setAnnatDatum(InternalDate undersokningsDatum);

        @JsonProperty(MEDICINSKUNDERLAG_SVAR_JSON_ANNAT_BESKRIVNING)
        public abstract Builder setAnnatBeskrivning(String annatBeskrivning);


        @JsonProperty(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_2)
        public abstract Builder setArbetsmarknadspolitisktProgram(ArbetsmarknadspolitisktProgram arbetsmarknadspolitisktProgram);

        @JsonProperty(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_3)
        public abstract Builder setFunktionsnedsattning(String funktionsnedsattning);

        @JsonProperty(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_4)
        public abstract Builder setAktivitetsbegransning(String aktivitetsbegransning);

        @JsonProperty(FORHINDER_SVAR_JSON_ID_5)
        public abstract Builder setHarForhinder(Boolean harForhinder);

        // Svar 1
        @JsonProperty(SJUKFRANVARO_SVAR_JSON_ID_6)
        public Builder setSjukfranvaro(List<Sjukfranvaro> sjukfranvaro) {
            return setSjukfranvaro(ImmutableList.copyOf(sjukfranvaro));
        }

        public abstract Builder setSjukfranvaro(ImmutableList<Sjukfranvaro> sjukfranvaro);

        @JsonProperty(BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_7)
        public abstract Builder setBegransningSjukfranvaro(BegransningSjukfranvaro begransningSjukfranvaro);

        @JsonProperty(PROGNOS_ATERGANG_SVAR_JSON_ID_8)
        public abstract Builder setPrognosAtergang(PrognosAtergang prognosAtergang);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);
    }

}
