/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
package se.inera.intyg.common.ts_bas.v7.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.ts_bas.support.TsBasEntryPoint;

@AutoValue
@JsonDeserialize(builder = AutoValue_TsBasUtlatandeV7.Builder.class)
public abstract class TsBasUtlatandeV7 implements Utlatande {

    @Override
    public String getTyp() {
        return TsBasEntryPoint.MODULE_ID;
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
    public abstract Syn getSyn();

    @Nullable
    public abstract HorselBalans getHorselBalans();

    @Nullable
    public abstract Funktionsnedsattning getFunktionsnedsattning();

    @Nullable
    public abstract HjartKarl getHjartKarl();

    @Nullable
    public abstract Diabetes getDiabetes();

    @Nullable
    public abstract Neurologi getNeurologi();

    @Nullable
    public abstract Medvetandestorning getMedvetandestorning();

    @Nullable
    public abstract Njurar getNjurar();

    @Nullable
    public abstract Kognitivt getKognitivt();

    @Nullable
    public abstract SomnVakenhet getSomnVakenhet();

    @Nullable
    public abstract NarkotikaLakemedel getNarkotikaLakemedel();

    @Nullable
    public abstract Psykiskt getPsykiskt();

    @Nullable
    public abstract Utvecklingsstorning getUtvecklingsstorning();

    @Nullable
    public abstract Sjukhusvard getSjukhusvard();

    @Nullable
    public abstract Medicinering getMedicinering();

    @Nullable
    public abstract Bedomning getBedomning();

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_TsBasUtlatandeV7.Builder()
            .setPsykiskt(Psykiskt.create(null))
            .setKognitivt(Kognitivt.create(null))
            .setNjurar(Njurar.create(null))
            .setSomnVakenhet(SomnVakenhet.create(null))
            .setNeurologi(Neurologi.create(null))
            .setUtvecklingsstorning(Utvecklingsstorning.builder().build())
            .setSignature(null);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract TsBasUtlatandeV7 build();

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

        @JsonProperty("syn")
        public abstract Builder setSyn(Syn syn);

        @JsonProperty("horselBalans")
        public abstract Builder setHorselBalans(HorselBalans horselBalans);

        @JsonProperty("funktionsnedsattning")
        public abstract Builder setFunktionsnedsattning(Funktionsnedsattning funktionsnedsattning);

        @JsonProperty("hjartKarl")
        public abstract Builder setHjartKarl(HjartKarl hjartKarl);

        @JsonProperty("diabetes")
        public abstract Builder setDiabetes(Diabetes diabetes);

        @JsonProperty("neurologi")
        public abstract Builder setNeurologi(Neurologi neurologi);

        @JsonProperty("medvetandestorning")
        public abstract Builder setMedvetandestorning(Medvetandestorning medvetandestorning);

        @JsonProperty("njurar")
        public abstract Builder setNjurar(Njurar njurar);

        @JsonProperty("kognitivt")
        public abstract Builder setKognitivt(Kognitivt kognitivt);

        @JsonProperty("somnVakenhet")
        public abstract Builder setSomnVakenhet(SomnVakenhet somnVakenhet);

        @JsonProperty("narkotikaLakemedel")
        public abstract Builder setNarkotikaLakemedel(NarkotikaLakemedel narkotikaLakemedel);

        @JsonProperty("psykiskt")
        public abstract Builder setPsykiskt(Psykiskt psykiskt);

        @JsonProperty("utvecklingsstorning")
        public abstract Builder setUtvecklingsstorning(Utvecklingsstorning utvecklingsstorning);

        @JsonProperty("sjukhusvard")
        public abstract Builder setSjukhusvard(Sjukhusvard sjukhusvard);

        @JsonProperty("medicinering")
        public abstract Builder setMedicinering(Medicinering medicinering);

        @JsonProperty("bedomning")
        public abstract Builder setBedomning(Bedomning bedomning);
    }
}
