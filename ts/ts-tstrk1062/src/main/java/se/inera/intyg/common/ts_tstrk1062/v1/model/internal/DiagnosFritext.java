package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID;
import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DiagnosFritext {

    @JsonCreator
    public static DiagnosFritext create(@JsonProperty(ALLMANT_DIAGNOSKOD_FRITEXT_FRITEXT_DELSVAR_JSON_ID) String diagnosFritext,
            @JsonProperty(ALLMANT_DIAGNOSKOD_FRITEXT_ARTAL_DELSVAR_JSON_ID) String diagnosArtal) {
        return new AutoValue_DiagnosFritext(diagnosFritext, diagnosArtal);
    }

    @Nullable
    public abstract String getDiagnosFritext();

    @Nullable
    public abstract String getDiagnosArtal();

}
