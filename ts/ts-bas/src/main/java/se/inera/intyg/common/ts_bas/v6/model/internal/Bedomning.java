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
package se.inera.intyg.common.ts_bas.v6.model.internal;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nullable;

@AutoValue
@JsonDeserialize(builder = AutoValue_Bedomning.Builder.class)
public abstract class Bedomning {

    public static Builder builder() {
        return new AutoValue_Bedomning.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        abstract Bedomning autoBuild();

        public Bedomning build() {
            return autoBuild();
        };

        @JsonProperty("korkortstyp")
        @JsonDeserialize(using = BedomningKorkortstypEnumSetDeserializer.class)
        public abstract Builder setKorkortstyp(Set<BedomningKorkortstyp> korkortstyp);

        @JsonProperty("lakareSpecialKompetens")
        public abstract Builder setLakareSpecialKompetens(String lakareSpecialKompetens);

    }

    @Nullable
    @JsonSerialize(using = BedomningKorkortstypEnumSetSerializer.class)
    public abstract Set<BedomningKorkortstyp> getKorkortstyp();

    @Nullable
    public abstract String getLakareSpecialKompetens();

    public static class BedomningKorkortstypEnumSetSerializer extends AbstractEnumSetSerializer<BedomningKorkortstyp> {
        protected BedomningKorkortstypEnumSetSerializer() {
            super(BedomningKorkortstyp.class);
        }
    }

    public static class BedomningKorkortstypEnumSetDeserializer extends
            AbstractEnumSetDeserializer<BedomningKorkortstyp> {
        protected BedomningKorkortstypEnumSetDeserializer() {
            super(BedomningKorkortstyp.class);
        }
    }
}
