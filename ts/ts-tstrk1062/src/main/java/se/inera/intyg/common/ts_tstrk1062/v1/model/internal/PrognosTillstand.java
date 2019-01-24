package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.stream.Stream;

@AutoValue
public abstract class PrognosTillstand {
    @JsonCreator
    public static PrognosTillstand create(@JsonProperty("typ") se.inera.intyg.common.ts_tstrk1062.v1.model.internal.PrognosTillstand.PrognosTillstandTyp typ) {
        return new AutoValue_PrognosTillstand(typ);
    }

    @Nullable
    public abstract se.inera.intyg.common.ts_tstrk1062.v1.model.internal.PrognosTillstand.PrognosTillstandTyp getTyp();

    public enum PrognosTillstandTyp {

        JA("true", "Ja"),
        NEJ("false", "Nej"),
        KAN_EJ_BEDOMA("NI", "Kan ej bedöma");

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

        public static se.inera.intyg.common.ts_tstrk1062.v1.model.internal.PrognosTillstand.PrognosTillstandTyp fromCode(String code) {
            return Stream.of(se.inera.intyg.common.ts_tstrk1062.v1.model.internal.PrognosTillstand.PrognosTillstandTyp.values()).filter(s -> code.equals(s.getCode())).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(code));
        }
    }
}
