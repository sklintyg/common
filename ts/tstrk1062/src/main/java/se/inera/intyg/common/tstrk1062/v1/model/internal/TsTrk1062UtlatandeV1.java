/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.tstrk1062.v1.model.internal;

import static se.inera.intyg.common.tstrk1062.v1.model.converter.TSTRK1062Constants.*;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.tstrk1062.support.TsTrk1062EntryPoint;

@AutoValue
@JsonDeserialize(builder = AutoValue_TsTrk1062UtlatandeV1.Builder.class)
public abstract class TsTrk1062UtlatandeV1 implements Utlatande {

    @Override
    public String getTyp() {
        return TsTrk1062EntryPoint.MODULE_ID;
    }

    @Override
    @Nullable
    public abstract String getId();

    @Override
    @Nullable
    public abstract GrundData getGrundData();

    @Override
    @Nullable
    public abstract String getTextVersion();

    @Override
    @Nullable
    public abstract String getSignature();

    @Nullable
    public abstract String getKommentar();

    @Nullable
    public abstract IntygAvser getIntygAvser();

    @Nullable
    public abstract IdKontroll getIdKontroll();

    @Nullable
    public abstract DiagnosRegistrering getDiagnosRegistrering();

    @Nullable
    public abstract ImmutableList<DiagnosKodad> getDiagnosKodad();

    @Nullable
    public abstract DiagnosFritext getDiagnosFritext();

    @Nullable
    public abstract Lakemedelsbehandling getLakemedelsbehandling();

    @Nullable
    public abstract String getBedomningAvSymptom();

    @Nullable
    public abstract PrognosTillstand getPrognosTillstand();

    @Nullable
    public abstract String getOvrigaKommentarer();

    @Nullable
    public abstract Bedomning getBedomning();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_TsTrk1062UtlatandeV1.Builder()
            .setDiagnosKodad(ImmutableList.of())
            .setSignature(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract TsTrk1062UtlatandeV1 build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXT_VERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(SIGNATURE_JSON_ID)
        public abstract Builder setSignature(String signature);

        @JsonProperty(KOMMENTAR_JSON_ID)
        public abstract Builder setKommentar(String kommentar);

        @JsonProperty(INTYG_AVSER_SVAR_JSON_ID)
        public abstract Builder setIntygAvser(IntygAvser intygAvser);

        @JsonProperty(ID_KONTROLL_SVAR_JSON_ID)
        public abstract Builder setIdKontroll(IdKontroll idKontroll);

        @JsonProperty(ALLMANT_INMATNING_SVAR_JSON_ID)
        public abstract Builder setDiagnosRegistrering(DiagnosRegistrering diagnosRegistrering);

        @JsonProperty(ALLMANT_DIAGNOSKOD_KODAD_SVAR_JSON_ID)
        public Builder setDiagnosKodad(List<DiagnosKodad> diagnosKodadList) {
            return setDiagnosKodad(ImmutableList.copyOf(diagnosKodadList));
        }

        abstract Builder setDiagnosKodad(ImmutableList<DiagnosKodad> diagnosKodad);

        @JsonProperty(ALLMANT_DIAGNOSKOD_FRITEXT_SVAR_JSON_ID)
        public abstract Builder setDiagnosFritext(DiagnosFritext diagnosFritext);

        @JsonProperty(LAKEMEDELSBEHANDLING_JSON_ID)
        public abstract Builder setLakemedelsbehandling(Lakemedelsbehandling lakemedelsbehandling);

        @JsonProperty(SYMPTOM_BEDOMNING_DELSVAR_JSON_ID)
        public abstract Builder setBedomningAvSymptom(String bedomningAvSymptom);

        @JsonProperty(SYMPTOM_PROGNOS_SVAR_JSON_ID)
        public abstract Builder setPrognosTillstand(PrognosTillstand prognosTillstand);

        @JsonProperty(OVRIGT_OVRIGA_KOMMENTARER_DELSVAR_JSON_ID)
        public abstract Builder setOvrigaKommentarer(String ovrigaKommentarer);

        @JsonProperty(BEDOMNING_UPPFYLLER_SVAR_JSON_ID)
        public abstract Builder setBedomning(Bedomning bedomning);
    }
}
