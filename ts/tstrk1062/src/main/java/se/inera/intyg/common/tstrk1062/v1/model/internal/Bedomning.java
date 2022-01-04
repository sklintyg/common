/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1062.v1.model.internal;

import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID;

import java.util.Set;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

@AutoValue
@JsonDeserialize(builder = AutoValue_Bedomning.Builder.class)
public abstract class Bedomning {

    @Nullable
    @JsonSerialize(using = Bedomning.BehorighetsTypSetEnumSetSerializer.class)
    @JsonDeserialize(using = Bedomning.BehorighetsTypSetDeserializer.class)
    public abstract Set<BehorighetsTyp> getUppfyllerBehorighetskrav();

    public static class BehorighetsTypSetEnumSetSerializer extends AbstractEnumSetSerializer<BehorighetsTyp> {

        protected BehorighetsTypSetEnumSetSerializer() {
            super(BehorighetsTyp.class);
        }
    }

    public static class BehorighetsTypSetDeserializer extends AbstractEnumSetDeserializer<BehorighetsTyp> {

        protected BehorighetsTypSetDeserializer() {
            super(BehorighetsTyp.class);
        }
    }

    public static Builder builder() {
        return new AutoValue_Bedomning.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Bedomning build();

        @JsonProperty(BEDOMNING_UPPFYLLER_DELSVAR_JSON_ID)
        @JsonDeserialize(using = Bedomning.BehorighetsTypSetDeserializer.class)
        public abstract Builder setUppfyllerBehorighetskrav(Set<BehorighetsTyp> uppfyllerBehorighetskrav);
    }

    public enum BehorighetsTyp {
        VAR12,
        VAR13,
        VAR14,
        VAR15,
        VAR16,
        VAR17,
        VAR18,
        VAR1,
        VAR2,
        VAR3,
        VAR4,
        VAR5,
        VAR6,
        VAR7,
        VAR8,
        VAR9,
        VAR11
    }
}
