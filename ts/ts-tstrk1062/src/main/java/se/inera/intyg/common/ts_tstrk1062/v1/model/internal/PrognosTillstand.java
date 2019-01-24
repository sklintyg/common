package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.stream.Stream;

@AutoValue
public abstract class PrognosTillstand {
    @JsonCreator
    public static PrognosTillstand create(@JsonProperty("typ") PrognosTillstandTyp typ) {
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

        public static PrognosTillstandTyp fromCode(String code) {
            return Stream.of(PrognosTillstandTyp.values()).filter(s -> code.equals(s.getCode())).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(code));
        }
    }
}
