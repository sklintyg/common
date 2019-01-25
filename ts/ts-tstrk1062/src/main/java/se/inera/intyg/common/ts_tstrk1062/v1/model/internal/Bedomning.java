package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Stream;

@AutoValue
@JsonDeserialize(builder = AutoValue_Bedomning.Builder.class)
public abstract class Bedomning {

    @Nullable
    @JsonSerialize(using = Bedomning.BehorighetsTypSetEnumSetSerializer.class)
    @JsonDeserialize(using = Bedomning.BehorighetsTypSetDeserializer.class)
    public abstract Set<BehorighetsTyp> getUppfyllerBehorighetskrav();

    public static class BehorighetsTypSetEnumSetSerializer extends AbstractEnumSetSerializer<BehorighetsTyp> {
        protected BehorighetsTypSetEnumSetSerializer() {
            super(BehorighetsTyp.class);
        }
    }

    public static class BehorighetsTypSetDeserializer extends AbstractEnumSetDeserializer<BehorighetsTyp> {
        protected BehorighetsTypSetDeserializer() {
            super(BehorighetsTyp.class);
        }
    }

    public static Builder builder() {
        return new AutoValue_Bedomning.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Bedomning build();

        @JsonProperty("uppfyllerBehorighetskrav")
        @JsonDeserialize(using = Bedomning.BehorighetsTypSetDeserializer.class)
        public abstract Builder setUppfyllerBehorighetskrav(Set<BehorighetsTyp> uppfyllerBehorighetskrav);
    }

    public enum BehorighetsTyp {
        VAR12,
        VAR13,
        VAR14,
        VAR15,
        VAR16,
        VAR17,
        VAR18,
        VAR1,
        VAR2,
        VAR3,
        VAR4,
        VAR5,
        VAR6,
        VAR7,
        VAR8,
        VAR9,
        VAR11
    }
}

