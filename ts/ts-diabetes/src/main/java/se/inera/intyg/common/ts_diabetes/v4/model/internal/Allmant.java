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
package se.inera.intyg.common.ts_diabetes.v4.model.internal;

import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BEHANDLING_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_FOR_DIABETES_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_PATIENTEN_FOLJS_AV_JSON_ID;
import static se.inera.intyg.common.ts_diabetes.v4.model.converter.RespConstants.ALLMANT_TYP_AV_DIABETES_JSON_ID;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvTypAvDiabetes;
import se.inera.intyg.common.ts_diabetes.v4.model.kodverk.KvVardniva;

@AutoValue
@JsonDeserialize(builder = AutoValue_Allmant.Builder.class)
public abstract class Allmant {

    @Nullable
    public abstract KvVardniva getPatientenFoljsAv();

    @Nullable
    public abstract String getDiabetesDiagnosAr();

    @Nullable
    public abstract KvTypAvDiabetes getTypAvDiabetes();

    @Nullable
    public abstract String getBeskrivningAnnanTypAvDiabetes();

    @Nullable
    public abstract Boolean getMedicineringForDiabetes();

    @Nullable
    public abstract Boolean getMedicineringMedforRiskForHypoglykemi();

    @Nullable
    public abstract Behandling getBehandling();

    @Nullable
    public abstract InternalDate getMedicineringMedforRiskForHypoglykemiTidpunkt();

    public static Builder builder() {
        return new AutoValue_Allmant.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Allmant build();

        @JsonProperty(ALLMANT_PATIENTEN_FOLJS_AV_JSON_ID)
        public abstract Builder setPatientenFoljsAv(KvVardniva value);

        @JsonProperty(ALLMANT_DIABETES_DIAGNOS_AR_JSON_ID)
        public abstract Builder setDiabetesDiagnosAr(String value);

        @JsonProperty(ALLMANT_TYP_AV_DIABETES_JSON_ID)
        public abstract Builder setTypAvDiabetes(KvTypAvDiabetes value);

        @JsonProperty(ALLMANT_BESKRIVNING_ANNAN_TYP_AV_DIABETES_JSON_ID)
        public abstract Builder setBeskrivningAnnanTypAvDiabetes(String value);

        @JsonProperty(ALLMANT_MEDICINERING_FOR_DIABETES_JSON_ID)
        public abstract Builder setMedicineringForDiabetes(Boolean value);

        @JsonProperty(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_JSON_ID)
        public abstract Builder setMedicineringMedforRiskForHypoglykemi(Boolean value);

        @JsonProperty(ALLMANT_BEHANDLING_JSON_ID)
        public abstract Builder setBehandling(Behandling value);

        @JsonProperty(ALLMANT_MEDICINERING_MEDFOR_RISK_FOR_HYPOGYKEMI_TIDPUNKT_JSON_ID)
        public abstract Builder setMedicineringMedforRiskForHypoglykemiTidpunkt(InternalDate value);

    }
}
