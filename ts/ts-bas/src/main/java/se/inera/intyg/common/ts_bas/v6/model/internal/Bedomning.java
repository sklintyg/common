/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import java.util.EnumSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.annotation.Nullable;

@JsonDeserialize(builder = Bedomning.Builder.class)
public final class Bedomning {

    private Set<BedomningKorkortstyp> korkortstyp;
    private String lakareSpecialKompetens;

    private Bedomning(Set<BedomningKorkortstyp> korkortstyp, String lakareSpecialKompetens) {
        this.korkortstyp = korkortstyp;
        this.lakareSpecialKompetens = lakareSpecialKompetens;
    }

    public static Builder builder() {
        return new Bedomning.Builder();
    }

    public static class Builder {

        private Set<BedomningKorkortstyp> korkortstyp;
        private String lakareSpecialKompetens;

        public Bedomning build() {
            return new Bedomning(this.korkortstyp, this.lakareSpecialKompetens);
        }

        @JsonProperty("korkortstyp")
        @JsonDeserialize(using = BedomningKorkortstypEnumSetDeserializer.class)
        public Builder setKorkortstyp(Set<BedomningKorkortstyp> korkortstyp) {
            this.korkortstyp = korkortstyp;
            return this;
        }

        /**
         * @deprecated This method exist only to be backward compatible with the old json-format. It is only used when reading the
         * old json-format from the database. When Bedomning is store in the database this variable is not used, instead
         * KAN_INTE_TA_STALLNING is stored in korkortstyp.<br/>
         * The method is not used from the front-end code.<br/>
         */
        @Deprecated
        @JsonProperty("kanInteTaStallning")
        public Builder setKanInteTaStallning(Boolean kanInteTaStallning) {
            if (kanInteTaStallning) {
                if (this.korkortstyp != null && !this.korkortstyp.isEmpty()) {
                    throw new IllegalStateException("Both kanInteTaStallning and korkortstyp is set in the Bedomning object read from "
                        + "the database. This indicates that the code is not working as excepted.");
                }
                this.korkortstyp = EnumSet.of(BedomningKorkortstyp.KAN_INTE_TA_STALLNING);
            }
            return this;
        }

        @JsonProperty("lakareSpecialKompetens")
        public Builder setLakareSpecialKompetens(String lakareSpecialKompetens) {
            this.lakareSpecialKompetens = lakareSpecialKompetens;
            return this;
        }

    }

    @Nullable
    @JsonSerialize(using = BedomningKorkortstypEnumSetSerializer.class)
    public Set<BedomningKorkortstyp> getKorkortstyp() {
        return this.korkortstyp;
    }

    @Nullable
    public String getLakareSpecialKompetens() {
        return this.lakareSpecialKompetens;
    }

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
