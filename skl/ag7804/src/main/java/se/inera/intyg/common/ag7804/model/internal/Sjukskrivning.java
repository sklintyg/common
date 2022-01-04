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
package se.inera.intyg.common.ag7804.model.internal;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;

@AutoValue
public abstract class Sjukskrivning {

    @JsonCreator
    public static Sjukskrivning create(@JsonProperty("sjukskrivningsgrad") SjukskrivningsGrad sjukskrivningsgrad,
        @JsonProperty("period") InternalLocalDateInterval period) {
        return new AutoValue_Sjukskrivning(sjukskrivningsgrad, period);
    }

    @Nullable
    public abstract SjukskrivningsGrad getSjukskrivningsgrad();

    @Nullable
    public abstract InternalLocalDateInterval getPeriod();

    public enum SjukskrivningsGrad {
        /**
         * Helt nedsatt.
         */
        HELT_NEDSATT("HELT_NEDSATT", "100%"),
        /**
         * Nedsatt till 3/4.
         */
        NEDSATT_3_4("TRE_FJARDEDEL", "75%"),
        /**
         * Nedsatt till hälften.
         */
        NEDSATT_HALFTEN("HALFTEN", "50%"),
        /**
         * Nedsatt till 1/4.
         */
        NEDSATT_1_4("EN_FJARDEDEL", "25%");

        private final String id;
        private final String label;

        SjukskrivningsGrad(String id, String label) {
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

        @JsonCreator
        public static SjukskrivningsGrad fromId(@JsonProperty("id") String id) {
            String normId = id != null ? id.trim() : null;
            for (SjukskrivningsGrad typ : values()) {
                if (typ.id.equals(normId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }

}
