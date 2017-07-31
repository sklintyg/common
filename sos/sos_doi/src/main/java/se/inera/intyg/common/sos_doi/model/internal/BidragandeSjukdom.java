package se.inera.intyg.common.sos_doi.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SPECIFIKATION_JSON_ID;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class BidragandeSjukdom {

    @JsonCreator
    public static BidragandeSjukdom create(@JsonProperty(BIDRAGANDE_SJUKDOM_OM_JSON_ID) String beskrivning,
            @JsonProperty(BIDRAGANDE_SJUKDOM_DATUM_JSON_ID) LocalDate datum,
            @JsonProperty(BIDRAGANDE_SJUKDOM_SPECIFIKATION_JSON_ID) Specifikation specifikation) {
        return new AutoValue_BidragandeSjukdom(beskrivning, datum, specifikation);
    }

    public abstract String getBeskrivning();

    public abstract LocalDate getDatum();

    public abstract Specifikation getSpecifikation();
}
