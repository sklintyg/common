/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

@AutoValue
public abstract class IntygetAvserBehorighet {

    @JsonCreator
    public static IntygetAvserBehorighet create(@JsonProperty("korkortstyp") final EnumSet<IntygetAvserBehorighetTyp> typerToCreate) {

        final EnumSet<IntygetAvserBehorighetTyp> behorighetTyper = (typerToCreate == null)
                ? EnumSet.noneOf(IntygetAvserBehorighetTyp.class)
                : typerToCreate;

        return new AutoValue_IntygetAvserBehorighet(behorighetTyper);
    }

    @Nullable
    @JsonSerialize(using = IntygetAvserBehorighetEnumSetSerializer.class)
    @JsonDeserialize(using = IntygetAvserBehorighetEnumSetDeserializer.class)
    public abstract Set<IntygetAvserBehorighetTyp> getBehorighetTyper();

    public static class IntygetAvserBehorighetEnumSetSerializer extends AbstractEnumSetSerializer<IntygetAvserBehorighetTyp> {
        protected IntygetAvserBehorighetEnumSetSerializer() {
            super(IntygetAvserBehorighetTyp.class);
        }
    }

    public static class IntygetAvserBehorighetEnumSetDeserializer extends AbstractEnumSetDeserializer<IntygetAvserBehorighetTyp> {
        protected IntygetAvserBehorighetEnumSetDeserializer() {
            super(IntygetAvserBehorighetTyp.class);
        }
    }
}
