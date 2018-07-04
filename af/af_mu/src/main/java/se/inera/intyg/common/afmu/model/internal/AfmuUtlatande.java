/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.afmu.model.internal;

// CHECKSTYLE:OFF LineLength

import static se.inera.intyg.common.afmu.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_2;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_1;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.OVRIGT_SVAR_JSON_ID_5;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_3;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_4;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.SIGNATURE;
import static se.inera.intyg.common.afmu.model.converter.RespConstants.TEXTVERSION_JSON_ID;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.af_parent.model.internal.AfUtlatande;
import se.inera.intyg.common.afmu.support.AfmuEntryPoint;
import se.inera.intyg.common.support.model.common.internal.GrundData;

// CHECKSTYLE:ON LineLength

@AutoValue
@JsonDeserialize(builder = AutoValue_AfmuUtlatande.Builder.class)
public abstract class AfmuUtlatande implements AfUtlatande {

    @Override
    public String getTyp() {
        return AfmuEntryPoint.MODULE_ID;
    }

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    @Override
    @Nullable
    public abstract String getSignature();


    // Kategori 4 - Sjukdomens konsekvenser
    // Fråga 35 - Funktionsnedsättning
    // Fråga 35.1
    @Nullable
    public abstract String getFunktionsnedsattning();

    // Fråga 17 Aktivitetsbegränsning
    // Fråga 17.1
    @Nullable
    public abstract String getAktivitetsbegransning();

    // Kategori 5 - Medicinska behandlingar / åtgärder
    // Fråga 19 -Pågående medicinska behandlingar
    // Fråga 19.1 - Typ av pågående medicinska behandlingar
    @Nullable
    public abstract String getPagaendeBehandling();

    // Fråga 20 - Planerad medicinsk behandling
    // Fråga 20.1
    @Nullable
    public abstract String getPlaneradBehandling();

    // Kategori 8 - Övrigt
    // Fråga 25
    @Nullable
    public abstract String getOvrigt();

    /*
     * Retrieve a builder from an existing AfmuUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_AfmuUtlatande.Builder()
                .setSignature(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract AfmuUtlatande build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_1)
        public abstract Builder setFunktionsnedsattning(String funktionsnedsattning);

        @JsonProperty(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_2)
        public abstract Builder setAktivitetsbegransning(String aktivitetsbegransning);

        @JsonProperty(PAGAENDEBEHANDLING_SVAR_JSON_ID_3)
        public abstract Builder setPagaendeBehandling(String pagaendeBehandling);

        @JsonProperty(PLANERADBEHANDLING_SVAR_JSON_ID_4)
        public abstract Builder setPlaneradBehandling(String planeradBehandling);

        @JsonProperty(OVRIGT_SVAR_JSON_ID_5)
        public abstract Builder setOvrigt(String ovrigt);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);
    }

}
