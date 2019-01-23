package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class DiagnosFritext {

    @JsonCreator
    public static DiagnosFritext create(@JsonProperty("diagnosFritext") String diagnosFritext,
                                      @JsonProperty("diagnosArtal") String diagnosArtal) {
        return new AutoValue_DiagnosFritext(diagnosFritext, diagnosArtal);
    }


    @Nullable
    public abstract String getDiagnosFritext();

    @Nullable
    public abstract String getDiagnosArtal();

}
