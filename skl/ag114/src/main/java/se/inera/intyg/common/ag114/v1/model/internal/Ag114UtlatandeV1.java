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
package se.inera.intyg.common.ag114.v1.model.internal;

// CHECKSTYLE:OFF LineLength

import static se.inera.intyg.common.agparent.model.converter.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_2;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.SIGNATURE;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TEXTVERSION_JSON_ID;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_DIAGNOS_SVAR_JSON_ID_4;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.ag114.support.Ag114EntryPoint;
import se.inera.intyg.common.agparent.model.converter.RespConstants;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

// CHECKSTYLE:ON LineLength

@AutoValue
@JsonDeserialize(builder = AutoValue_Ag114UtlatandeV1.Builder.class)
public abstract class Ag114UtlatandeV1 implements Utlatande {

    @Override
    public abstract String getId();

    @Override
    public abstract GrundData getGrundData();

    @Override
    public abstract String getTextVersion();

    @Override
    @Nullable
    public abstract String getSignature();

    @Override
    public String getTyp() {
        return Ag114EntryPoint.MODULE_ID;
    }

    // - - - - - - - - - - - - - - - - - - - - - -
    // Kategori 1 – Sysselsättning
    // - - - - - - - - - - - - - - - - - - - - - -

    // Fråga 1 - Sysselsättning
    // Fråga 1.1
    @Nullable
    public abstract List<Sysselsattning> getSysselsattning();

    // Fråga 2 - Nuvarande arbete
    // Fråga 2.1
    @Nullable
    public abstract String getNuvarandeArbete();

    // Fråga 3 - Inkludera ...
    @Nullable
    public abstract Boolean getOnskarFormedlaDiagnos();

    // Fråga 5 - Inkludera ...
    @Nullable
    public abstract String getNedsattArbetsFormaga();

    /*
     * Retrieve a builder from an existing Ag114Utlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_Ag114UtlatandeV1.Builder()
                .setSignature(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Ag114UtlatandeV1 build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);

        @JsonProperty(TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_1)
        public abstract Builder setSysselsattning(List<Sysselsattning> sysselsattning);

        @JsonProperty(NUVARANDE_ARBETE_SVAR_JSON_ID_2)
        public abstract Builder setNuvarandeArbete(String nuvarandeArbete);

        @JsonProperty(ONSKAR_FORMEDLA_DIAGNOS_SVAR_JSON_ID_3)
        public abstract Builder setOnskarFormedlaDiagnos(Boolean onskarFormedlaDiagnos);

//        @JsonProperty(TYP_AV_DIAGNOS_SVAR_JSON_ID_4)
//        public abstract Builder setTypAvDiagnos(List<Diagnos> diagnos);

        @JsonProperty(NEDSATT_ARBETSFORMAGA_SVAR_JSON_ID_5)
        public abstract Builder setNedsattArbetsFormaga(String nedsattArbetsFormaga);
    }

}
