/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.lisjp.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class Sysselsattning {

    @JsonCreator
    public static Sysselsattning create(@JsonProperty("typ") SysselsattningsTyp typ) {
        return new AutoValue_Sysselsattning(typ);
    }

    @Nullable
    public abstract SysselsattningsTyp getTyp();

    public enum SysselsattningsTyp {
        NUVARANDE_ARBETE("NUVARANDE_ARBETE", "Nuvarande arbete"),
        ARBETSSOKANDE("ARBETSSOKANDE", "Arbetssökande"),
        FORADLRARLEDIGHET_VARD_AV_BARN("FORALDRALEDIG", "Föräldraledighet för vård av barn"),
        STUDIER("STUDIER", "Studier");

        private final String id;
        private final String label;

        SysselsattningsTyp(String id, String label) {
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
        public static SysselsattningsTyp fromId(@JsonProperty("id") String id) {
            String normId = id != null ? id.trim() : null;
            for (SysselsattningsTyp typ : values()) {
                if (typ.id.equals(normId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }
}
