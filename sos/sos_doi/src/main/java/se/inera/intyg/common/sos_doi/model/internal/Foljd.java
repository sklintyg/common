package se.inera.intyg.common.sos_doi.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_SPECIFIKATION_JSON_ID;

import java.time.LocalDate;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Foljd {

    @JsonCreator
    public static Foljd create(@JsonProperty(FOLJD_OM_JSON_ID) String beskrivning,
            @JsonProperty(FOLJD_DATUM_JSON_ID) LocalDate datum,
            @JsonProperty(FOLJD_SPECIFIKATION_JSON_ID) Specifikation specifikation) {
        return new AutoValue_Foljd(beskrivning, datum, specifikation);
    }

    public abstract String getBeskrivning();

    @Nullable
    public abstract LocalDate getDatum();

    @Nullable
    public abstract Specifikation getSpecifikation();
}
