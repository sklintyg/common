package se.inera.intyg.common.tstrk1062.v1.model.internal;

import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.ALLMANT_INMATNING_DELSVAR_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DiagnosRegistrering {

    @JsonCreator
    public static DiagnosRegistrering create(@JsonProperty(ALLMANT_INMATNING_DELSVAR_JSON_ID) DiagnosRegistreringsTyp typ) {
        return new AutoValue_DiagnosRegistrering(typ);
    }

    @Nullable
    public abstract DiagnosRegistreringsTyp getTyp();

    public enum DiagnosRegistreringsTyp {
        DIAGNOS_KODAD,
        DIAGNOS_FRITEXT;
    }
}
