/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.INTYG_AVSER_DELSVAR_JSON_ID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import java.util.Set;
import javax.annotation.Nullable;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

@AutoValue
public abstract class IntygAvser {

    @JsonCreator
    public static IntygAvser create(@JsonProperty(INTYG_AVSER_DELSVAR_JSON_ID) Set<BehorighetsTyp> behorigheter) {
        return new AutoValue_IntygAvser(behorigheter);
    }

    @Nullable
    @JsonSerialize(using = IntygAvser.BehorighetsTypSetEnumSetSerializer.class)
    @JsonDeserialize(using = IntygAvser.BehorighetsTypSetDeserializer.class)
    public abstract Set<IntygAvser.BehorighetsTyp> getBehorigheter();

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

    public enum BehorighetsTyp {
        IAV11,
        IAV12,
        IAV13,
        IAV14,
        IAV15,
        IAV16,
        IAV17,
        IAV1,
        IAV2,
        IAV3,
        IAV4,
        IAV5,
        IAV6,
        IAV7,
        IAV8,
        IAV9
    }
}
