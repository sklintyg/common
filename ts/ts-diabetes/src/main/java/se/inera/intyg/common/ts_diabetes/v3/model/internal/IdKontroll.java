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
package se.inera.intyg.common.ts_diabetes.v3.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import se.inera.intyg.common.ts_diabetes.v3.model.kodverk.KvIdKontroll;

/**
 * Created by marced on 2018-09-05.
 */
@AutoValue
public abstract class IdKontroll {

    @JsonCreator
    public static IdKontroll create(@JsonProperty("typ") KvIdKontroll typ) {
        return new AutoValue_IdKontroll(typ);
    }

    @Nullable
    public abstract KvIdKontroll getTyp();

}
