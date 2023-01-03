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

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.PROGNOS_ATERGANG_SVAR_JSON_ID_81;
import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.PROGNOS_ATERGANG_SVAR_JSON_ID_82;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

/**
 *
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_PrognosAtergang.Builder.class)
public abstract class PrognosAtergang {

    // Delfråga 8.1
    @Nullable
    public abstract Prognos getPrognos();

    // Delfråga 8.2
    @Nullable
    public abstract String getAnpassningar();


    public static PrognosAtergang.Builder builder() {
        return new AutoValue_PrognosAtergang.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract PrognosAtergang build();

        @JsonProperty(PROGNOS_ATERGANG_SVAR_JSON_ID_81)
        public abstract PrognosAtergang.Builder setPrognos(Prognos prognos);

        @JsonProperty(PROGNOS_ATERGANG_SVAR_JSON_ID_82)
        public abstract PrognosAtergang.Builder setAnpassningar(String anpassningar);

    }


    public enum Prognos {

        UTAN_ANPASSNING("ATERGA_UTAN_ANPASSNING", "Patienten kan återgå utan anpassning"),
        MED_ANPASSNING("ATERGA_MED_ANPASSNING", "Patienten kan återgå med anpassning"),
        KAN_EJ_ATERGA("KAN_EJ_ATERGA", "Patienten inte kan återgå"),
        EJ_MOJLIGT_AVGORA("EJ_MOJLIGT_AVGORA", "Det inte är möjligt att avgöra om patienten kan återgå");


        private final String id;
        private final String label;

        Prognos(String id, String label) {
            this.id = id;
            this.label = label;
        }

        @JsonValue
        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static Prognos fromId(@JsonProperty("id") String id) {
            String normId = id != null ? id.trim() : null;
            for (Prognos typ : values()) {
                if (typ.id.equals(normId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
