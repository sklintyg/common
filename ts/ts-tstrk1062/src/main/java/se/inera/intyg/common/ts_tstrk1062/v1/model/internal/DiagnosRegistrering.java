package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DiagnosRegistrering {

    @JsonCreator
    public static DiagnosRegistrering create(@JsonProperty("typ") DiagnosRegistreringsTyp typ) {
        return new AutoValue_DiagnosRegistrering(typ);
    }

    @Nullable
    public abstract DiagnosRegistreringsTyp getTyp();

    public enum DiagnosRegistreringsTyp {
        DIAGNOS_KODAD,
        DIAGNOS_FRITEXT;
    }
}
