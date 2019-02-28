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

        DIAGNOS_KODAD("KODAD", "Ange diagnos enligt ICD-10"),
        DIAGNOS_FRITEXT("FRITEXT", "Ange diagnos som fritext (används t.ex. om diagnoser ska anges enligt DSM)");

        final String code;
        final String description;

        DiagnosRegistreringsTyp(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
