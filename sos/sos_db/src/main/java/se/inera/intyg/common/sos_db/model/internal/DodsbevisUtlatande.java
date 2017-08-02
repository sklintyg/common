package se.inera.intyg.common.sos_db.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_AVLAGSNAT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.EXPLOSIV_IMPLANTAT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.POLISANMALAN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TEXTVERSION_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TILLAGGSFRAGOR_SVAR_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_DETALJER_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.UNDERSOKNING_YTTRE_JSON_ID;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import autovalue.shaded.com.google.common.common.collect.ImmutableList;
import se.inera.intyg.common.sos_db.support.DodsbevisModuleEntryPoint;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Tillaggsfraga;

@AutoValue
@JsonDeserialize(builder = AutoValue_DodsbevisUtlatande.Builder.class)
public abstract class DodsbevisUtlatande implements SosUtlatande {

    public static Builder builder() {
        return new AutoValue_DodsbevisUtlatande.Builder().setTillaggsfragor(ImmutableList.<Tillaggsfraga> of());
    }

    @Override
    public String getTyp() {
        return DodsbevisModuleEntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    @Override
    @Nullable
    public abstract String getIdentitetStyrkt();

    @Override
    @Nullable
    public abstract Boolean getDodsdatumSakert();

    @Override
    @Nullable
    public abstract InternalDate getDodsdatum();

    @Override
    @Nullable
    public abstract InternalDate getAntraffatDodDatum();

    @Override
    @Nullable
    public abstract String getDodsplatsKommun();

    @Override
    @Nullable
    public abstract DodsplatsBoende getDodsplatsBoende();

    @Override
    @Nullable
    public abstract Boolean getBarn();

    @Nullable
    public abstract Boolean getExplosivImplantat();

    @Nullable
    public abstract Boolean getExplosivAvlagsnat();

    @Nullable
    public abstract Boolean getUndersokningYttre();

    @Nullable
    public abstract Undersokning getUndersokningDetaljer();

    @Nullable
    public abstract InternalDate getUndersokningDatum();

    @Nullable
    public abstract Boolean getPolisanmalan();

    public abstract ImmutableList<Tillaggsfraga> getTillaggsfragor();

    /*
     * Retrieve a builder from an existing LuseUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract DodsbevisUtlatande build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(IDENTITET_STYRKT_JSON_ID)
        public abstract Builder setIdentitetStyrkt(String identitetStyrkt);

        @JsonProperty(DODSDATUM_SAKERT_JSON_ID)
        public abstract Builder setDodsdatumSakert(Boolean dodsdatumSakert);

        @JsonProperty(DODSDATUM_JSON_ID)
        public abstract Builder setDodsdatum(InternalDate dodsdatum);

        @JsonProperty(ANTRAFFAT_DOD_DATUM_JSON_ID)
        public abstract Builder setAntraffatDodDatum(InternalDate antraffatDodDatum);

        @JsonProperty(DODSPLATS_KOMMUN_JSON_ID)
        public abstract Builder setDodsplatsKommun(String dodsplatsKommun);

        @JsonProperty(DODSPLATS_BOENDE_JSON_ID)
        public abstract Builder setDodsplatsBoende(DodsplatsBoende dodsplatsBoende);

        @JsonProperty(BARN_JSON_ID)
        public abstract Builder setBarn(Boolean barn);

        @JsonProperty(EXPLOSIV_IMPLANTAT_JSON_ID)
        public abstract Builder setExplosivImplantat(Boolean explosivImplantat);

        @JsonProperty(EXPLOSIV_AVLAGSNAT_JSON_ID)
        public abstract Builder setExplosivAvlagsnat(Boolean explosivAvlagsnat);

        @JsonProperty(UNDERSOKNING_YTTRE_JSON_ID)
        public abstract Builder setUndersokningYttre(Boolean undersokningYttre);

        @JsonProperty(UNDERSOKNING_DETALJER_JSON_ID)
        public abstract Builder setUndersokningDetaljer(Undersokning undersokningDetaljer);

        @JsonProperty(UNDERSOKNING_DATUM_JSON_ID)
        public abstract Builder setUndersokningDatum(InternalDate undersokningDatum);

        @JsonProperty(POLISANMALAN_JSON_ID)
        public abstract Builder setPolisanmalan(Boolean polisanmalan);

        @JsonProperty(TILLAGGSFRAGOR_SVAR_JSON_ID)
        public Builder setTillaggsfragor(List<Tillaggsfraga> tillaggsfragor) {
            return setTillaggsfragor(ImmutableList.copyOf(tillaggsfragor));
        }

        /* package private */
        abstract Builder setTillaggsfragor(ImmutableList<Tillaggsfraga> tillaggsfragor);
    }
}
