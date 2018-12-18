package se.inera.intyg.common.af00251.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

import static se.inera.intyg.common.af00251.v1.model.converter.AF00251RespConstants.*;

/**
 *
 */
@AutoValue
@JsonDeserialize(builder = AutoValue_BegransningSjukfranvaro.Builder.class)
public abstract class BegransningSjukfranvaro {

    // Fråga 7.1 - Kan sjukfrånvaro begränsas
    @Nullable
    public abstract Boolean getKanBegransas();

    // Fråga 7.2 - Beskrivning av hur sjukfrånvaro begränsas
    @Nullable
    public abstract String getBeskrivning();

    public static BegransningSjukfranvaro.Builder builder() {
        return new AutoValue_BegransningSjukfranvaro.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract BegransningSjukfranvaro build();

        @JsonProperty(BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_71)
        public abstract BegransningSjukfranvaro.Builder setKanBegransas(Boolean kanBegransas);

        @JsonProperty(BEGRANSNING_SJUKFRANVARO_SVAR_JSON_ID_72)
        public abstract BegransningSjukfranvaro.Builder setBeskrivning(String beskrivning);
    }






}
