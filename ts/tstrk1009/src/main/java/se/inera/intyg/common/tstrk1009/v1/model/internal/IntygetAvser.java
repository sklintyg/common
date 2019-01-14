package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class IntygetAvser {

    @JsonCreator
    public static IntygetAvser create(@JsonProperty("typ") KorkortBehorighetGrupp typ) {
        return new AutoValue_IntygetAvser(typ);
    }

    @Nullable
    public abstract KorkortBehorighetGrupp getTyp();
}
