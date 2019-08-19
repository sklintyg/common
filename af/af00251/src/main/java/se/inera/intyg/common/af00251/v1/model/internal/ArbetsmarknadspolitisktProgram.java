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
package se.inera.intyg.common.af00251.v1.model.internal;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_21;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_22;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

/**
 * Fråga 2.
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_ArbetsmarknadspolitisktProgram.Builder.class)
public abstract class ArbetsmarknadspolitisktProgram {

    @Nullable
    public abstract String getMedicinskBedomning();

    @Nullable
    public abstract Omfattning getOmfattning();

    @Nullable
    public abstract Integer getOmfattningDeltid();


    public static ArbetsmarknadspolitisktProgram.Builder builder() {
        return new AutoValue_ArbetsmarknadspolitisktProgram.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract ArbetsmarknadspolitisktProgram build();

        @JsonProperty(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_21)
        public abstract ArbetsmarknadspolitisktProgram.Builder setMedicinskBedomning(String medicinskBedomning);

        @JsonProperty(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_22)
        public abstract ArbetsmarknadspolitisktProgram.Builder setOmfattning(Omfattning omfattning);

        @JsonProperty(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23)
        public abstract ArbetsmarknadspolitisktProgram.Builder setOmfattningDeltid(Integer omfattningDeltid);
    }

    public enum Omfattning {

        HELTID("HELTID", "Heltid (40 timmar/vecka)"),
        DELTID("DELTID", "Deltid"),
        OKAND("OKAND", "Okänd");

        private final String id;
        private final String label;

        Omfattning(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public static final String KODVERK = "kv-omfattning-arbetsmarknadspolitiskt-program";

        @JsonValue
        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        @JsonCreator
        public static Omfattning fromId(@JsonProperty("id") String id) {
            String normId = id != null ? id.trim() : null;
            for (Omfattning typ : values()) {
                if (typ.id.equals(normId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }


}
