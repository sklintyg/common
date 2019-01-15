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
package se.inera.intyg.common.tstrk1009.v1.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.tstrk1009.support.Tstrk1009EntryPoint;

@AutoValue
@JsonDeserialize(builder = AutoValue_Tstrk1009UtlatandeV1.Builder.class)
public abstract class Tstrk1009UtlatandeV1 implements Utlatande {

    @Override
    public String getTyp() {
        return Tstrk1009EntryPoint.MODULE_ID;
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
    public abstract IdentitetStyrktGenom getIdentitetStyrktGenom();

    @Nullable
    public abstract AnmalanAvser getAnmalanAvser();

    @Nullable
    public abstract String getMedicinskaForhallanden();

    @Nullable
    public abstract InternalDate getSenasteUndersokningsdatum();

    @Nullable
    public abstract IntygetAvser getIntygetAvserBehorigheter();

    @Nullable
    public abstract Boolean getInformationOmTsBeslutOnskas();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_Tstrk1009UtlatandeV1.Builder()
                .setIntygetAvserBehorigheter(IntygetAvser.create(null))
                .setSignature(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Tstrk1009UtlatandeV1 build();

        @JsonProperty("id")
        public abstract Builder setId(final String id);

        @JsonProperty("grundData")
        public abstract Builder setGrundData(final GrundData grundData);

        @JsonProperty("textVersion")
        public abstract Builder setTextVersion(final String textVersion);

        @JsonProperty("signature")
        public abstract Builder setSignature(final String signature);

        @JsonProperty("identitetStyrktGenom")
        public abstract Builder setIdentitetStyrktGenom(final IdentitetStyrktGenom identitetStyrktGenom);

        @JsonProperty("anmalanAvser")
        public abstract Builder setAnmalanAvser(final AnmalanAvser anmalanAvser);

        @JsonProperty("medicinskaForhallanden")
        public abstract Builder setMedicinskaForhallanden(final String medicinskaForhallanden);

        @JsonProperty("senasteUndersokningsdatum")
        public abstract Builder setSenasteUndersokningsdatum(final InternalDate internalDate);

        @JsonProperty("intygetAvserBehorigheter")
        public abstract Builder setIntygetAvserBehorigheter(final IntygetAvser intygetAvserBehorigheter);

        @JsonProperty("informationOmTsBeslutOnskas")
        public abstract Builder setInformationOmTsBeslutOnskas(final Boolean informationOmTsBeslutOnskas);
    }
}
