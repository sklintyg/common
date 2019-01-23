package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class DiagnosKodad {

    @JsonCreator
    public static DiagnosKodad create(@JsonProperty("diagnosKod") String diagnosKod,
                                      @JsonProperty("diagnosKodSystem") String diagnosKodSystem,
                                      @JsonProperty("diagnosBeskrivning") String diagnosBeskrivning,
                                      @JsonProperty("diagnosDisplayName") String diagnosDisplayName,
                                      @JsonProperty("diagnosArtal") String diagnosArtal) {
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
