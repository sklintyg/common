package se.inera.intyg.common.sos_doi.model.internal;

import static se.inera.intyg.common.sos_parent.support.RespConstants.ANTRAFFAT_DOD_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BARN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.BIDRAGANDE_SJUKDOM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSDATUM_SAKERT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSORSAK_SPECIFIKATION_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_BOENDE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.DODSPLATS_KOMMUN_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FOLJD_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_ORSAK_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.FORGIFTNING_UPPKOMMELSE_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.GRUNDER_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.IDENTITET_STYRKT_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.LAND_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_ANLEDNING_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_DATUM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.OPERATION_OM_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TEXTVERSION_JSON_ID;
import static se.inera.intyg.common.sos_parent.support.RespConstants.TILLAGGSFRAGOR_SVAR_JSON_ID;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import autovalue.shaded.com.google.common.common.collect.ImmutableList;
import se.inera.intyg.common.services.texts.model.Tillaggsfraga;
import se.inera.intyg.common.sos_doi.support.DodsorsaksintygModuleEntryPoint;
import se.inera.intyg.common.sos_parent.model.internal.DodsplatsBoende;
import se.inera.intyg.common.sos_parent.model.internal.SosUtlatande;
import se.inera.intyg.common.support.model.common.internal.GrundData;

@AutoValue
@JsonDeserialize(builder = AutoValue_DodsorsaksintygUtlatande.Builder.class)
public abstract class DodsorsaksintygUtlatande implements SosUtlatande {

    public static Builder builder() {
        return new AutoValue_DodsorsaksintygUtlatande.Builder().setTillaggsfragor(ImmutableList.<Tillaggsfraga> of()).setGrunder(
                ImmutableList.<Dodsorsaksgrund> of()).setBidragandeSjukdomar(ImmutableList.<BidragandeSjukdom> of())
                .setFoljd(ImmutableList.<Foljd> of());
    }

    @Override
    public String getTyp() {
        return DodsorsaksintygModuleEntryPoint.MODULE_ID;
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
    public abstract String getDodsdatum();

    @Override
    @Nullable
    public abstract LocalDate getAntraffatDodDatum();

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
    public abstract String getLand();

    @Nullable
    public abstract String getDodsorsak();

    @Nullable
    public abstract LocalDate getDodsorsakDatum();

    @Nullable
    public abstract Specifikation getDodsorsakSpecifikation();

    public abstract ImmutableList<Foljd> getFoljd();

    public abstract ImmutableList<BidragandeSjukdom> getBidragandeSjukdomar();

    @Nullable
    public abstract Boolean getOperation();

    @Nullable
    public abstract LocalDate getOperationDatum();

    @Nullable
    public abstract String getOperationAnledning();

    @Nullable
    public abstract Boolean getForgiftning();

    @Nullable
    public abstract ForgiftningOrsak getForgiftningOrsak();

    @Nullable
    public abstract LocalDate getForgiftningDatum();

    @Nullable
    public abstract String getForgiftningUppkommelse();

    public abstract ImmutableList<Dodsorsaksgrund> getGrunder();

    public abstract ImmutableList<Tillaggsfraga> getTillaggsfragor();

    /*
     * Retrieve a builder from an existing LuseUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract DodsorsaksintygUtlatande build();

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
        public abstract Builder setDodsdatum(String dodsdatum);

        @JsonProperty(ANTRAFFAT_DOD_DATUM_JSON_ID)
        public abstract Builder setAntraffatDodDatum(LocalDate antraffatDodDatum);

        @JsonProperty(DODSPLATS_KOMMUN_JSON_ID)
        public abstract Builder setDodsplatsKommun(String dodsplatsKommun);

        @JsonProperty(DODSPLATS_BOENDE_JSON_ID)
        public abstract Builder setDodsplatsBoende(DodsplatsBoende dodsplatsBoende);

        @JsonProperty(BARN_JSON_ID)
        public abstract Builder setBarn(Boolean barn);

        @JsonProperty(LAND_JSON_ID)
        public abstract Builder setLand(String land);

        @JsonProperty(DODSORSAK_JSON_ID)
        public abstract Builder setDodsorsak(String dodsorsak);

        @JsonProperty(DODSORSAK_DATUM_JSON_ID)
        public abstract Builder setDodsorsakDatum(LocalDate dodsorsakDatum);

        @JsonProperty(DODSORSAK_SPECIFIKATION_JSON_ID)
        public abstract Builder setDodsorsakSpecifikation(Specifikation dodsorsakSpecifikation);

        @JsonProperty(FOLJD_JSON_ID)
        public Builder setFoljd(List<Foljd> foljd) {
            return setFoljd(ImmutableList.copyOf(foljd));
        }

        abstract Builder setFoljd(ImmutableList<Foljd> foljd);

        @JsonProperty(BIDRAGANDE_SJUKDOM_JSON_ID)
        public Builder setBidragandeSjukdomar(List<BidragandeSjukdom> bidragandeSjukdomar) {
            return setBidragandeSjukdomar(ImmutableList.copyOf(bidragandeSjukdomar));
        }

        abstract Builder setBidragandeSjukdomar(ImmutableList<BidragandeSjukdom> bidragandeSjukdomar);

        @JsonProperty(OPERATION_OM_JSON_ID)
        public abstract Builder setOperation(Boolean operation);

        @JsonProperty(OPERATION_DATUM_JSON_ID)
        public abstract Builder setOperationDatum(LocalDate operationDatum);

        @JsonProperty(OPERATION_ANLEDNING_JSON_ID)
        public abstract Builder setOperationAnledning(String operationAnledning);

        @JsonProperty(FORGIFTNING_OM_JSON_ID)
        public abstract Builder setForgiftning(Boolean forgiftning);

        @JsonProperty(FORGIFTNING_ORSAK_JSON_ID)
        public abstract Builder setForgiftningOrsak(ForgiftningOrsak forgiftningOrsak);

        @JsonProperty(FORGIFTNING_DATUM_JSON_ID)
        public abstract Builder setForgiftningDatum(LocalDate forgiftningDatum);

        @JsonProperty(FORGIFTNING_UPPKOMMELSE_JSON_ID)
        public abstract Builder setForgiftningUppkommelse(String forgiftningUppkommelse);

        @JsonProperty(GRUNDER_JSON_ID)
        public Builder setGrunder(List<Dodsorsaksgrund> grunder) {
            return setGrunder(ImmutableList.copyOf(grunder));
        }

        abstract Builder setGrunder(ImmutableList<Dodsorsaksgrund> grunder);

        @JsonProperty(TILLAGGSFRAGOR_SVAR_JSON_ID)
        public Builder setTillaggsfragor(List<Tillaggsfraga> tillaggsfragor) {
            return setTillaggsfragor(ImmutableList.copyOf(tillaggsfragor));
        }

        /* package private */
        abstract Builder setTillaggsfragor(ImmutableList<Tillaggsfraga> tillaggsfragor);
    }
}
