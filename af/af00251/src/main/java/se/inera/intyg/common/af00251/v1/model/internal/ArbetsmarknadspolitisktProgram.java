package se.inera.intyg.common.af00251.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.*;

/**
 * Fråga 2
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_ArbetsmarknadspolitisktProgram.Builder.class)
public abstract class ArbetsmarknadspolitisktProgram {

    @Nullable
    public abstract String getMedicinskBedomning();
    @Nullable
    public abstract Omfattning getOmfattning();
    @Nullable
    public abstract Integer getOmfattningDeltid();


    public static ArbetsmarknadspolitisktProgram.Builder builder() {
        return new AutoValue_ArbetsmarknadspolitisktProgram.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract ArbetsmarknadspolitisktProgram build();

        @JsonProperty(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_21)
        public abstract ArbetsmarknadspolitisktProgram.Builder setMedicinskBedomning(String medicinskBedomning);

        @JsonProperty(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_22)
        public abstract ArbetsmarknadspolitisktProgram.Builder setOmfattning(Omfattning omfattning);

        @JsonProperty(ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID_23)
        public abstract ArbetsmarknadspolitisktProgram.Builder setOmfattningDeltid(Integer omfattningDeltid);
    }

    public enum Omfattning {

        HELTID("HELTID", "Heltid (40 timmar/vecka)"),
        DELTID("DELTID", "Deltid"),
        OKAND("OKAND", "Okänd");

        private String id;
        private String label;

        Omfattning(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public static String KODVERK = "kv-omfattning-arbetsmarknadspolitiskt-program";

        @JsonValue
        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        @JsonCreator
        public static Omfattning fromId(@JsonProperty("id") String id) {
            String normId = id != null ? id.trim() : null;
            for (Omfattning typ : values()) {
                if (typ.id.equals(normId)) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }


}
