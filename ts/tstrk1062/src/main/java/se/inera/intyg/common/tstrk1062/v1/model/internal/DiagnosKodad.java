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

import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_DISPLAYNAME_JSON_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_KODSYSTEM_JSON_ID;
import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_JSON_ID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class DiagnosKodad {

    @JsonCreator
    public static DiagnosKodad create(@JsonProperty(ALLMANT_DIAGNOSKOD_KODAD_KOD_DELSVAR_JSON_ID) String diagnosKod,
        @JsonProperty(ALLMANT_DIAGNOSKOD_KODAD_KOD_KODSYSTEM_JSON_ID) String diagnosKodSystem,
        @JsonProperty(ALLMANT_DIAGNOSKOD_KODAD_KOD_TEXT_DELSVAR_JSON_ID) String diagnosBeskrivning,
        @JsonProperty(ALLMANT_DIAGNOSKOD_KODAD_KOD_DISPLAYNAME_JSON_ID) String diagnosDisplayName,
        @JsonProperty(ALLMANT_DIAGNOSKOD_KODAD_KOD_ARTAL_DELSVAR_JSON_ID) String diagnosArtal) {
        return new AutoValue_DiagnosKodad(diagnosKod, diagnosKodSystem, diagnosBeskrivning, diagnosDisplayName, diagnosArtal);
    }

    @Nullable
    public abstract String getDiagnosKod();

    @Nullable
    public abstract String getDiagnosKodSystem();

    @Nullable
    public abstract String getDiagnosBeskrivning();

    @Nullable
    public abstract String getDiagnosDisplayName();

    @Nullable
    public abstract String getDiagnosArtal();

}
