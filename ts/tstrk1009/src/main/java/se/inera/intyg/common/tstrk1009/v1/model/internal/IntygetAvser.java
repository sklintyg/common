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
public abstract class IntygetAvser {

    @JsonCreator
    public static IntygetAvser create(@JsonProperty("typer") Set<KorkortBehorighetGrupp> korkortBehorigheter) {

        final Set<KorkortBehorighetGrupp> behorigheter = (korkortBehorigheter == null)
            ? EnumSet.noneOf(KorkortBehorighetGrupp.class)
            : korkortBehorigheter;

        return new AutoValue_IntygetAvser(behorigheter);
    }

    @Nullable
    @JsonSerialize(using = IntygAvserEnumSetSerializer.class)
    @JsonDeserialize(using = IntygAvserEnumSetDeserializer.class)
    public abstract Set<KorkortBehorighetGrupp> getTyper();

    public static class IntygAvserEnumSetSerializer extends AbstractEnumSetSerializer<KorkortBehorighetGrupp> {

        protected IntygAvserEnumSetSerializer() {
            super(KorkortBehorighetGrupp.class);
        }
    }

    public static class IntygAvserEnumSetDeserializer extends AbstractEnumSetDeserializer<KorkortBehorighetGrupp> {

        protected IntygAvserEnumSetDeserializer() {
            super(KorkortBehorighetGrupp.class);
        }
    }
}
