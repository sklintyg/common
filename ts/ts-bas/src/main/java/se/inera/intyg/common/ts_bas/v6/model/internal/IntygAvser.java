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
package se.inera.intyg.common.ts_bas.v6.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nullable;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

/**
 * The Korkortstyp[er] a specific Utlatande concerns.
 *
 * @author erik
 */
@AutoValue
public abstract class IntygAvser {

    @JsonCreator
    public static IntygAvser create(@JsonProperty("korkortstyp") EnumSet<IntygAvserKategori> korkortstyp) {
        EnumSet<IntygAvserKategori> korkortstyper;
        if (korkortstyp == null) {
            korkortstyper = EnumSet.noneOf(IntygAvserKategori.class);
        } else {
            korkortstyper = korkortstyp;
        }
        return new AutoValue_IntygAvser(korkortstyper);
    }

    @Nullable
    @JsonSerialize(using = IntygAvserEnumSetSerializer.class)
    @JsonDeserialize(using = IntygAvserEnumSetDeserializer.class)
    public abstract Set<IntygAvserKategori> getKorkortstyp();

    public static class IntygAvserEnumSetSerializer extends AbstractEnumSetSerializer<IntygAvserKategori> {

        protected IntygAvserEnumSetSerializer() {
            super(IntygAvserKategori.class);
        }
    }

    public static class IntygAvserEnumSetDeserializer extends AbstractEnumSetDeserializer<IntygAvserKategori> {

        protected IntygAvserEnumSetDeserializer() {
            super(IntygAvserKategori.class);
        }
    }

}
