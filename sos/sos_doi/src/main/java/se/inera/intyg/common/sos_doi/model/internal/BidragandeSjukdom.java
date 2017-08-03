package se.inera.intyg.common.sos_doi.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_SPECIFIKATION_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalDate;

@AutoValue
public abstract class BidragandeSjukdom {

    @JsonCreator
    public static BidragandeSjukdom create(@JsonProperty(BIDRAGANDE_SJUKDOM_OM_JSON_ID) String beskrivning,
            @JsonProperty(BIDRAGANDE_SJUKDOM_DATUM_JSON_ID) InternalDate datum,
            @JsonProperty(BIDRAGANDE_SJUKDOM_SPECIFIKATION_JSON_ID) Specifikation specifikation) {
        return new AutoValue_BidragandeSjukdom(beskrivning, datum, specifikation);
    }

    @Nullable
    public abstract String getBeskrivning();

    @Nullable
    public abstract InternalDate getDatum();

    @Nullable
    public abstract Specifikation getSpecifikation();
}
