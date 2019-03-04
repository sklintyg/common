package se.inera.intyg.common.tstrk1062.v1.model.internal;

import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.*;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DiagnosKodad {

    @JsonCreator
    public static DiagnosKodad create(@JsonProperty(ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID) String diagnosKod,
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
