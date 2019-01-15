package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.common.ts_parent.json.AbstractEnumSetSerializer;

@AutoValue
public abstract class IntygetAvser {

    @JsonCreator
    public static IntygetAvser create(@JsonProperty("typer") Set<KorkortBehorighetGrupp> korkortBehorigheter) {

        final Set<KorkortBehorighetGrupp> behorigheter = (korkortBehorigheter == null)
                ? EnumSet.noneOf(KorkortBehorighetGrupp.class)
                : korkortBehorigheter;

        return new AutoValue_IntygetAvser(behorigheter);
    }

    @Nullable
    @JsonSerialize(using = IntygAvserEnumSetSerializer.class)
    @JsonDeserialize(using = IntygAvserEnumSetDeserializer.class)
    public abstract Set<KorkortBehorighetGrupp> getTyper();

    public static class IntygAvserEnumSetSerializer extends AbstractEnumSetSerializer<KorkortBehorighetGrupp> {
        protected IntygAvserEnumSetSerializer() {
            super(KorkortBehorighetGrupp.class);
        }
    }

    public static class IntygAvserEnumSetDeserializer extends AbstractEnumSetDeserializer<KorkortBehorighetGrupp> {
        protected IntygAvserEnumSetDeserializer() {
            super(KorkortBehorighetGrupp.class);
        }
    }
}
