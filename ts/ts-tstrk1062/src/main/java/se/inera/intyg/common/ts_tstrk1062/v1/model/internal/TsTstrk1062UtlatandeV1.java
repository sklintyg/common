/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_tstrk1062.v1.model.internal;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.ts_tstrk1062.support.TsTstrk1062EntryPoint;

// test
@AutoValue
@JsonDeserialize(builder = AutoValue_TsTstrk1062UtlatandeV1.Builder.class)
public abstract class TsTstrk1062UtlatandeV1 implements Utlatande {

    @Override
    public String getTyp() {
        return TsTstrk1062EntryPoint.MODULE_ID;
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
    public abstract Vardkontakt getVardkontakt();

    @Nullable
    public abstract IntygAvser getIntygAvser();

    @Nullable
    public abstract IdKontroll getIdKontroll();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_TsTstrk1062UtlatandeV1.Builder()
                .setSignature(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract TsTstrk1062UtlatandeV1 build();

        @JsonProperty("id")
        public abstract Builder setId(String id);

        @JsonProperty("grundData")
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty("textVersion")
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty("signature")
        public abstract Builder setSignature(String signature);

        @JsonProperty("kommentar")
        public abstract Builder setKommentar(String kommentar);

        @JsonProperty("vardkontakt")
        public abstract Builder setVardkontakt(Vardkontakt vardkontakt);

        @JsonProperty("intygAvser")
        public abstract Builder setIntygAvser(IntygAvser intygAvser);

        @JsonProperty("idKontroll")
        public abstract Builder setIdKontroll(IdKontroll idKontroll);
    }
}
