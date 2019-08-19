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
package se.inera.intyg.common.tstrk1062.v1.model.internal;

import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.*;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Lakemedelsbehandling {

    // CHECKSTYLE:OFF ParameterNumber
    @JsonCreator
    public static Lakemedelsbehandling create(@JsonProperty(LAKEMEDELSBEHANDLING_FOREKOMMIT_DELSVAR_JSON_ID) Boolean harHaft,
        @JsonProperty(LAKEMEDELSBEHANDLING_PAGAR_DELSVAR_JSON_ID) Boolean pagar,
        @JsonProperty(LAKEMEDELSBEHANDLING_AKTUELL_DELSVAR_JSON_ID) String aktuell,
        @JsonProperty(LAKEMEDELSBEHANDLING_MER_3_AR_DELSVAR_JSON_ID) Boolean pagatt,
        @JsonProperty(LAKEMEDELSBEHANDLING_EFFEKT_DELSVAR_JSON_ID) Boolean effekt,
        @JsonProperty(LAKEMEDELSBEHANDLING_FOLJSAMHET_DELSVAR_JSON_ID) Boolean foljsamhet,
        @JsonProperty(LAKEMEDELSBEHANDLING_AVSLUTAD_DELSVAR_JSON_ID) String avslutadTidpunkt,
        @JsonProperty(LAKEMEDELSBEHANDLING_AVSLUTAD_ORSAK_DELSVAR_JSON_ID) String avslutadOrsak) {
        return new AutoValue_Lakemedelsbehandling(harHaft, pagar, aktuell, pagatt, effekt, foljsamhet, avslutadTidpunkt, avslutadOrsak);
    }
    // CHECKSTYLE:ON ParameterNumber

    @Nullable
    public abstract Boolean getHarHaft();

    @Nullable
    public abstract Boolean getPagar();

    @Nullable
    public abstract String getAktuell();

    @Nullable
    public abstract Boolean getPagatt();

    @Nullable
    public abstract Boolean getEffekt();

    @Nullable
    public abstract Boolean getFoljsamhet();

    @Nullable
    public abstract String getAvslutadTidpunkt();

    @Nullable
    public abstract String getAvslutadOrsak();
}
