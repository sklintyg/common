package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import se.inera.intyg.common.support.model.InternalDate;

import javax.annotation.Nullable;

@AutoValue
public abstract class Lakemedelsbehandling {

    @JsonCreator
    public static Lakemedelsbehandling create(@JsonProperty("harHaft") Boolean harHaft,
                                              @JsonProperty("pagar") Boolean pagar,
                                              @JsonProperty("aktuell") String aktuell,
                                              @JsonProperty("pagatt") Boolean pagatt,
                                              @JsonProperty("effekt") Boolean effekt,
                                              @JsonProperty("foljsamhet") Boolean foljsamhet,
                                              @JsonProperty("avslutadTidpunkt") InternalDate avslutadTidpunkt,
                                              @JsonProperty("avslutadOrsak") String avslutadOrsak
    ) {
        return new AutoValue_Lakemedelsbehandling(harHaft, pagar, aktuell, effekt, pagatt, foljsamhet, avslutadTidpunkt, avslutadOrsak);
    }


    @Nullable
    public abstract Boolean getHarHaft();

    @Nullable
    public abstract Boolean getPagar();

    @Nullable
    public abstract String getAktuell();

    @Nullable
    public abstract Boolean getPagatt();

    @Nullable
    public abstract Boolean getEffekt();

    @Nullable
    public abstract Boolean getFoljsamhet();

    @Nullable
    public abstract InternalDate getAvslutadTidpunkt();

    @Nullable
    public abstract String getAvslutadOrsak();
}
