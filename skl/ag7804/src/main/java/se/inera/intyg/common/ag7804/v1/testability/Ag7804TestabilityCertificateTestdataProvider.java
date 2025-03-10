/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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

package se.inera.intyg.common.ag7804.v1.testability;

import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSRESOR_SVAR_ID_34;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSRESOR_SVAR_JSON_ID_34;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27;
import static se.inera.intyg.common.ag7804.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32;
import static se.inera.intyg.common.ag7804.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35;
import static se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.KONTAKT_ONSKAS_SVAR_JSON_ID_103;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NO_ID;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.NUVARANDE_ARBETE_SVAR_JSON_ID_29;
import static se.inera.intyg.common.ag7804.converter.RespConstants.ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.intyg.common.ag7804.converter.RespConstants.OVRIGT_SVAR_JSON_ID_25;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_JSON_ID_19;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PLANERADBEHANDLING_SVAR_JSON_ID_20;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID_39;
import static se.inera.intyg.common.ag7804.converter.RespConstants.PROGNOS_SVAR_ID_39;
import static se.inera.intyg.common.ag7804.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID_28;
import static se.inera.intyg.common.ag7804.converter.RespConstants.YES_ID;
import static se.inera.intyg.common.support.facade.util.TestabilityToolkit.getCertificateDataValueDateRangeListWithSeveralPeriods;
import static se.inera.intyg.common.support.facade.util.TestabilityToolkit.getCertificateDataValueDiagnosisList;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import se.inera.intyg.common.ag7804.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.common.ag7804.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.common.ag7804.model.internal.PrognosTyp;
import se.inera.intyg.common.ag7804.model.internal.Sysselsattning;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueText;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValue;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueBoolean;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCode;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueCodeList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDate;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateList;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRange;
import se.inera.intyg.common.support.facade.model.value.CertificateDataValueDateRangeList;
import se.inera.intyg.common.support.facade.util.TestabilityCertificateTestdataProvider;

public class Ag7804TestabilityCertificateTestdataProvider implements TestabilityCertificateTestdataProvider {

    private static String EXAMPLE_TEXT = "Detta är ett exempel";
    private static final int DEFAULT_SICK_LEAVE_LENGTH = 14;

    @Override
    public Map<String, CertificateDataValue> getMinimumValues() {
        final var values = new HashMap<String, CertificateDataValue>();

        final CertificateDataValueBoolean avstangningSmittskydd = CertificateDataValueBoolean.builder()
            .id(AVSTANGNING_SMITTSKYDD_SVAR_JSON_ID_27)
            .selected(true)
            .build();
        values.put(AVSTANGNING_SMITTSKYDD_SVAR_ID_27, avstangningSmittskydd);

        final CertificateDataValueCode shouldIncludeDiagnoses = CertificateDataValueCode.builder()
            .code(NO_ID)
            .id(NO_ID)
            .build();
        values.put(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100, shouldIncludeDiagnoses);

        final var bedomning = CertificateDataValueDateRangeList.builder()
            .list(
                Collections.singletonList(
                    CertificateDataValueDateRange.builder()
                        .id("HELT_NEDSATT")
                        .from(LocalDate.now())
                        .to(LocalDate.now().plusDays(DEFAULT_SICK_LEAVE_LENGTH))
                        .build()
                )
            )
            .build();
        values.put(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, bedomning);

        return values;
    }

    @Override
    public Map<String, CertificateDataValue> getMaximumValues() {
        final var values = new HashMap<String, CertificateDataValue>();

        final CertificateDataValueCode shouldIncludeDiagnoses = CertificateDataValueCode.builder()
            .code(YES_ID)
            .id(YES_ID)
            .build();
        values.put(ONSKAR_FORMEDLA_DIAGNOS_SVAR_ID_100, shouldIncludeDiagnoses);

        final var baseratPa = CertificateDataValueDateList.builder()
            .list(
                Collections.singletonList(
                    CertificateDataValueDate.builder()
                        .id(se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNAT_SVAR_JSON_ID_1)
                        .date(LocalDate.now())
                        .build()

                )
            )
            .build();
        values.put(se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1, baseratPa);

        final var motiveringAnnat = CertificateDataValueText.builder()
            .id(se.inera.intyg.common.ag7804.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_BESKRIVNING_DELSVAR_JSON_ID_1)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1, motiveringAnnat);

        final var sysselsattning = CertificateDataValueCodeList.builder()
            .list(
                Collections.singletonList(
                    CertificateDataValueCode.builder()
                        .id(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                        .code(Sysselsattning.SysselsattningsTyp.NUVARANDE_ARBETE.getId())
                        .build()
                )
            )
            .build();
        values.put(TYP_AV_SYSSELSATTNING_SVAR_ID_28, sysselsattning);

        final var arbetsuppgifter = CertificateDataValueText.builder()
            .id(NUVARANDE_ARBETE_SVAR_JSON_ID_29)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(NUVARANDE_ARBETE_SVAR_ID_29, arbetsuppgifter);

        final var diagnos = getCertificateDataValueDiagnosisList();
        values.put(DIAGNOS_SVAR_ID_6, diagnos);

        final var funktionsnedsattning = CertificateDataValueText.builder()
            .id(FUNKTIONSNEDSATTNING_SVAR_JSON_ID_35)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(FUNKTIONSNEDSATTNING_SVAR_ID_35, funktionsnedsattning);

        final var aktivitetsbegransning = CertificateDataValueText.builder()
            .id(AKTIVITETSBEGRANSNING_SVAR_JSON_ID_17)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(AKTIVITETSBEGRANSNING_SVAR_ID_17, aktivitetsbegransning);

        final var pagaendeBehandling = CertificateDataValueText.builder()
            .id(PAGAENDEBEHANDLING_SVAR_JSON_ID_19)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(PAGAENDEBEHANDLING_SVAR_ID_19, pagaendeBehandling);

        final var planeradBehandling = CertificateDataValueText.builder()
            .id(PLANERADBEHANDLING_SVAR_JSON_ID_20)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(PLANERADBEHANDLING_SVAR_ID_20, planeradBehandling);

        final CertificateDataValueDateRangeList bedomning = getCertificateDataValueDateRangeListWithSeveralPeriods(true);
        values.put(BEHOV_AV_SJUKSKRIVNING_SVAR_ID_32, bedomning);

        final var forsakringsMedicinsktBeslutsstod = CertificateDataValueText.builder()
            .id(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID_37)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID_37, forsakringsMedicinsktBeslutsstod);

        final var arbetstidsforlaggning = CertificateDataValueBoolean.builder()
            .selected(true)
            .id(ARBETSTIDSFORLAGGNING_SVAR_JSON_ID_33)
            .build();
        values.put(ARBETSTIDSFORLAGGNING_SVAR_ID_33, arbetstidsforlaggning);

        final var motiveringArbetstidsforlaggning = CertificateDataValueText.builder()
            .id(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID_33)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID_33, motiveringArbetstidsforlaggning);

        final var arbetsresor = CertificateDataValueBoolean.builder()
            .selected(true)
            .id(ARBETSRESOR_SVAR_JSON_ID_34)
            .build();
        values.put(ARBETSRESOR_SVAR_ID_34, arbetsresor);

        final var prognos = CertificateDataValueCode.builder()
            .id(PrognosTyp.ATER_X_ANTAL_DGR.getId())
            .code(PrognosTyp.ATER_X_ANTAL_DGR.getId())
            .build();
        values.put(PROGNOS_SVAR_ID_39, prognos);

        final var prognosTimePeriod = CertificateDataValueCode.builder()
            .id(PrognosDagarTillArbeteTyp.DAGAR_30.getId())
            .code(PrognosDagarTillArbeteTyp.DAGAR_30.getId())
            .build();
        values.put(PROGNOS_BESKRIVNING_DELSVAR_ID_39, prognosTimePeriod);

        final var atgarder = CertificateDataValueCodeList.builder()
            .list(
                Collections.singletonList(
                    CertificateDataValueCode.builder()
                        .id(ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                        .code(ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal.OVRIGT.getId())
                        .build()
                )
            )
            .build();
        values.put(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID_40, atgarder);

        final var atgarderBeskrivning = CertificateDataValueText.builder()
            .id(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_JSON_ID_44)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(ARBETSLIVSINRIKTADE_ATGARDER_BESKRIVNING_SVAR_ID_44, atgarderBeskrivning);

        final var ovrigt = CertificateDataValueText.builder()
            .id(OVRIGT_SVAR_JSON_ID_25)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(OVRIGT_SVAR_ID_25, ovrigt);

        final var kontakt = CertificateDataValueBoolean.builder()
            .selected(true)
            .id(KONTAKT_ONSKAS_SVAR_JSON_ID_103)
            .build();
        values.put(KONTAKT_ONSKAS_SVAR_ID_103, kontakt);

        final var kontaktMotivering = CertificateDataValueText.builder()
            .id(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103)
            .id(ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID_103)
            .text(EXAMPLE_TEXT)
            .build();
        values.put(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_103, kontaktMotivering);

        return values;
    }
}
