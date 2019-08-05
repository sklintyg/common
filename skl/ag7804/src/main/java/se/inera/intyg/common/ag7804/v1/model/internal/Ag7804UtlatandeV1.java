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
package se.inera.intyg.common.ag7804.v1.model.internal;

// CHECKSTYLE:OFF LineLength

import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSRESOR_SVAR_JSON_ID_34;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_JSON_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_JSON_ID_39;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.GRUNDDATA_SVAR_JSON_ID;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.ID_JSON_ID;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.SIGNATURE;
import static se.inera.intyg.common.agparent.model.converter.RespConstants.TEXTVERSION_JSON_ID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.Prognos;
import se.inera.intyg.common.ag7804.model.internal.Sjukskrivning;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.ag7804.support.Ag7804EntryPoint;
import se.inera.intyg.common.agparent.model.internal.Diagnos;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

// CHECKSTYLE:ON LineLength

@AutoValue
@JsonDeserialize(builder = AutoValue_Ag7804UtlatandeV1.Builder.class)
public abstract class Ag7804UtlatandeV1 implements Utlatande {

    public static Builder builder() {
        return new AutoValue_Ag7804UtlatandeV1.Builder()
                .setDiagnoser(ImmutableList.<Diagnos> of())
                .setSjukskrivningar(ImmutableList.<Sjukskrivning> of())
                .setSysselsattning(ImmutableList.<Sysselsattning> of())
                .setArbetslivsinriktadeAtgarder(ImmutableList.<ArbetslivsinriktadeAtgarder> of())
                .setSignature(null);
    }

    @Override
    public String getTyp() {
        return Ag7804EntryPoint.MODULE_ID;
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

    // Kategori 10 - Smittbärarpenning
    // Fråga 27
    @Nullable
    public abstract Boolean getAvstangningSmittskydd();

    // Kategori 1 – Grund för medicinskt underlag
    // Fråga 1
    @Nullable
    public abstract InternalDate getUndersokningAvPatienten();

    @Nullable
    public abstract InternalDate getTelefonkontaktMedPatienten();

    @Nullable
    public abstract InternalDate getJournaluppgifter();

    @Nullable
    public abstract InternalDate getAnnatGrundForMU();

    // Fråga 1.3 Annan grund för MU
    @Nullable
    public abstract String getAnnatGrundForMUBeskrivning();

    // Kategori 2 - Sysselsättning
    // Fråga 28
    // Fråga 28.1
    @Nullable
    public abstract ImmutableList<Sysselsattning> getSysselsattning();

    // Fråga 29 - Nuvarande arbete
    // Fråga 29.1
    @Nullable
    public abstract String getNuvarandeArbete();

    // Kategori 3 - Diagnos
    // Fråga 103 - Önskar patienten förmedla diagnoser
    // Fråga 103.1
    @Nullable
    public abstract Boolean getOnskarFormedlaDiagnos();

    // Fråga 6
    @Nullable
    public abstract ImmutableList<Diagnos> getDiagnoser();

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

    // Kategori 6 - Bedömning
    // Fråga 32 - Behov av sjukskrivning
    // 32.1
    public abstract ImmutableList<Sjukskrivning> getSjukskrivningar();

    // Fråga 37 - försäkringsmedicinskt beslutsstöd
    // 37.1
    @Nullable
    public abstract String getForsakringsmedicinsktBeslutsstod();

    // Fråga 33 - Arbetstidsförläggning
    // 33.1
    @Nullable
    public abstract Boolean getArbetstidsforlaggning();

    // 33.2
    @Nullable
    public abstract String getArbetstidsforlaggningMotivering();

    // Fråga 34 - Arbetsresor
    // 34.1
    @Nullable
    public abstract Boolean getArbetsresor();

    // Fråga 39
    @Nullable
    public abstract Prognos getPrognos();

    // Kategori 7 - Åtgärder
    // Fråga 40 - Arbetslivsinriktade åtgärder
    public abstract ImmutableList<ArbetslivsinriktadeAtgarder> getArbetslivsinriktadeAtgarder();

    @Nullable
    // Fråga 44 - Arbetslivsinriktade åtgärder beksrivning
    public abstract String getArbetslivsinriktadeAtgarderBeskrivning();

    // Kategori 8 - Övrigt
    // Fråga 25
    @Nullable
    public abstract String getOvrigt();

    // Kategori 9 - Kontakt
    // Fråga 103.1
    @Nullable
    public abstract Boolean getKontaktMedAg();

    // Fråga 103.2
    @Nullable
    public abstract String getAnledningTillKontakt();


    /*
     * Retrieve a builder from an existing LisjpUtlatande object. The builder can then be used
     * to create a new copy with modified attributes.
     */
    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Ag7804UtlatandeV1 build();

        @JsonProperty(ID_JSON_ID)
        public abstract Builder setId(String id);

        @JsonProperty(GRUNDDATA_SVAR_JSON_ID)
        public abstract Builder setGrundData(GrundData grundData);

        @JsonProperty(TEXTVERSION_JSON_ID)
        public abstract Builder setTextVersion(String textVersion);

        @JsonProperty(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
        public abstract Builder setAvstangningSmittskydd(Boolean avstangningSmittskydd);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID_1)
        public abstract Builder setUndersokningAvPatienten(InternalDate undersokningAvPatienten);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_TELEFONKONTAKT_PATIENT_SVAR_JSON_ID_1)
        public abstract Builder setTelefonkontaktMedPatienten(InternalDate telefonkontaktMedPatienten);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_JOURNALUPPGIFTER_SVAR_JSON_ID_1)
        public abstract Builder setJournaluppgifter(InternalDate journaluppgifter);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
        public abstract Builder setAnnatGrundForMU(InternalDate annatGrundForMU);

        @JsonProperty(GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
        public abstract Builder setAnnatGrundForMUBeskrivning(String annatGrundForMUBeskrivning);

        @JsonProperty(TYP_AV_SYSSELSATTNING_SVAR_JSON_ID_28)
        public Builder setSysselsattning(List<Sysselsattning> sysselsattning) {
            return setSysselsattning(ImmutableList.copyOf(sysselsattning));
        }

        /* package private */
        abstract Builder setSysselsattning(ImmutableList<Sysselsattning> sysselsattning);

        @JsonProperty(NUVARANDE_ARBETE_SVAR_JSON_ID_29)
        public abstract Builder setNuvarandeArbete(String nuvarandeArbete);

        @JsonProperty(ONSKAR_FORMEDLA_DIAGNOS_DELSVAR_JSON_ID_100)
        public abstract Builder setOnskarFormedlaDiagnos(Boolean onskarFormedlaDiagnos);

        @JsonProperty(DIAGNOS_SVAR_JSON_ID_6)
        public Builder setDiagnoser(List<Diagnos> diagnoser) {
            return setDiagnoser(ImmutableList.copyOf(diagnoser));
        }

        /* package private */
        abstract Builder setDiagnoser(ImmutableList<Diagnos> diagnoser);

        @JsonProperty(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
        public abstract Builder setFunktionsnedsattning(String funktionsnedsattning);

        @JsonProperty(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
        public abstract Builder setAktivitetsbegransning(String aktivitetsbegransning);

        @JsonProperty(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
        public abstract Builder setPagaendeBehandling(String pagaendeBehandling);

        @JsonProperty(PLANERADBEHANDLING_SVAR_JSON_ID_20)
        public abstract Builder setPlaneradBehandling(String planeradBehandling);

        @JsonProperty(BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID_32)
        public Builder setSjukskrivningar(List<Sjukskrivning> sjukskrivningar) {
            return setSjukskrivningar(ImmutableList.copyOf(sjukskrivningar));
        }

        /* package private */
        abstract Builder setSjukskrivningar(ImmutableList<Sjukskrivning> sjukskrivningar);

        @JsonProperty(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37)
        public abstract Builder setForsakringsmedicinsktBeslutsstod(String forskningsmedicinsktBeslutsstod);

        @JsonProperty(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
        public abstract Builder setArbetstidsforlaggning(Boolean arbetstidsforlaggning);

        @JsonProperty(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33)
        public abstract Builder setArbetstidsforlaggningMotivering(String arbetstidsforlaggningMotivering);

        @JsonProperty(ARBETSRESOR_SVAR_JSON_ID_34)
        public abstract Builder setArbetsresor(Boolean arbetsresor);

        @JsonProperty(PROGNOS_SVAR_JSON_ID_39)
        public abstract Builder setPrognos(Prognos prognos);

        @JsonProperty(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID_40)
        public Builder setArbetslivsinriktadeAtgarder(List<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder) {
            return setArbetslivsinriktadeAtgarder(ImmutableList.copyOf(arbetslivsinriktadeAtgarder));
        }

        /* package private */
        abstract Builder setArbetslivsinriktadeAtgarder(ImmutableList<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder);

        @JsonProperty(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44)
        public abstract Builder setArbetslivsinriktadeAtgarderBeskrivning(String arbetslivsinriktadeAtgarderBeskrivning);

        @JsonProperty(OVRIGT_SVAR_JSON_ID_25)
        public abstract Builder setOvrigt(String ovrigt);

        @JsonProperty(KONTAKT_ONSKAS_SVAR_JSON_ID_103)
        public abstract Builder setKontaktMedAg(Boolean kontaktMedAg);

        @JsonProperty(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103)
        public abstract Builder setAnledningTillKontakt(String anledningTillKontakt);

        @JsonProperty(SIGNATURE)
        public abstract Builder setSignature(String signature);
    }

}
