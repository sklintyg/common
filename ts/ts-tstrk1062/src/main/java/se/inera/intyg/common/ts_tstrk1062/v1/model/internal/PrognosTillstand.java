package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import static se.inera.intyg.common.ts_tstrk1062.v1.model.converter.TSTRK1062Constants.SYMPTOM_PROGNOS_DELSVAR_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class PrognosTillstand {
    @JsonCreator
    public static PrognosTillstand create(@JsonProperty(SYMPTOM_PROGNOS_DELSVAR_JSON_ID) PrognosTillstandTyp typ) {
        return new AutoValue_PrognosTillstand(typ);
    }

    @Nullable
    public abstract PrognosTillstandTyp getTyp();

    public enum PrognosTillstandTyp {

        JA("true", "Ja"),
        NEJ("false", "Nej"),
        KANEJBEDOMA("NI", "Kan ej bedöma");

        final String code;
        final String description;

        PrognosTillstandTyp(String code, String description) {
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
