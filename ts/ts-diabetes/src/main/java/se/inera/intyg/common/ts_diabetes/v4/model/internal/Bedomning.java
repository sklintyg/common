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
package se.inera.intyg.common.ts_diabetes.v4.model.internal;

import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import java.util.Set;
import javax.annotation.Nullable;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

@AutoValue
@JsonDeserialize(builder = AutoValue_Bedomning.Builder.class)
public abstract class Bedomning {

    @Nullable
    @JsonSerialize(using = Bedomning.BedomningKorkortstypSetEnumSetSerializer.class)
    @JsonDeserialize(using = Bedomning.BedomningKorkortstypSetDeserializer.class)
    public abstract Set<BedomningKorkortstyp> getUppfyllerBehorighetskrav();

    @Nullable
    public abstract String getOvrigaKommentarer();

    public static class BedomningKorkortstypSetEnumSetSerializer extends AbstractEnumSetSerializer<BedomningKorkortstyp> {

        protected BedomningKorkortstypSetEnumSetSerializer() {
            super(BedomningKorkortstyp.class);
        }
    }

    public static class BedomningKorkortstypSetDeserializer extends AbstractEnumSetDeserializer<BedomningKorkortstyp> {

        protected BedomningKorkortstypSetDeserializer() {
            super(BedomningKorkortstyp.class);
        }
    }

    public static Builder builder() {
        return new AutoValue_Bedomning.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Bedomning build();

        @JsonProperty(BEDOMNING_UPPFYLLER_BEHORIGHETSKRAV_JSON_ID)
        @JsonDeserialize(using = Bedomning.BedomningKorkortstypSetDeserializer.class)
        public abstract Builder setUppfyllerBehorighetskrav(Set<BedomningKorkortstyp> uppfyllerBehorighetskrav);

        @JsonProperty(BEDOMNING_OVRIGA_KOMMENTARER_JSON_ID)
        public abstract Builder setOvrigaKommentarer(String ovrigaKommentarer);
    }
}
